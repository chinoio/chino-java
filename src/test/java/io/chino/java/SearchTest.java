package io.chino.java;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.chino.api.common.ChinoApiException;
import io.chino.api.common.Field;
import io.chino.api.common.indexed;
import io.chino.api.document.Document;
import io.chino.api.document.GetDocumentsResponse;
import io.chino.api.schema.SchemaStructure;
import io.chino.api.search.*;
import io.chino.api.user.GetUsersResponse;
import io.chino.api.user.User;
import io.chino.java.testutils.ChinoBaseTest;
import io.chino.java.testutils.TestConstants;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static io.chino.api.search.FilterOperator.*;
import static io.chino.api.search.SearchQueryBuilder.not;
import static io.chino.api.search.SearchQueryBuilder.with;
import static org.junit.Assert.*;

public class SearchTest extends ChinoBaseTest {

    private static ChinoAPI chino_admin;

    private static String REPO_ID, SCHEMA_ID, USER_SCHEMA_ID;

    private final static String outputString = "test pass!";
    private final static String usernamePrefix = "SearchUser";

    @BeforeClass
    public static void beforeClass() throws IOException, ChinoApiException {
        ChinoBaseTest.runClass(SearchTest.class);
        ChinoBaseTest.beforeClass();
        chino_admin = new ChinoAPI(TestConstants.HOST, TestConstants.CUSTOMER_ID, TestConstants.CUSTOMER_KEY);
        ChinoBaseTest.init(chino_admin.search);

        // create a repository and a Schema
        ChinoBaseTest.checkResourceIsEmpty(
                chino_admin.repositories.list().getRepositories().isEmpty(),
                chino_admin.repositories
        );
        REPO_ID = chino_admin.repositories.create("SearchTest" + " [" + TestConstants.JAVA + "]")
                .getRepositoryId();

        // add some attributes that can be used to search users
        LinkedList<Field> fields = new LinkedList<>();
        fields.add(new Field("test_method", "string", true));
        fields.add(new Field("print_value", "string"));
        fields.add(new Field("internal_id", "integer", true));

        // create a Schema
        SCHEMA_ID = chino_admin.schemas.create(
                REPO_ID,
                "this Schema is searched for Documents using the new Chino.io Search interface.",
                new SchemaStructure(fields)
        ).getSchemaId();

        // create a UserSchema
        USER_SCHEMA_ID = chino_admin.userSchemas.create(
                "test new Search."  + " [" + TestConstants.JAVA + "]",
                SampleUser.class
        ).getUserSchemaId();

        // create a number of documents and assign to their print_value one char of the output string
        for (int i = 0; i < outputString.length(); i++) {
            HashMap<String, Object> content = new HashMap<>();
            content.put("test_method", "search documents test");
            content.put("print_value", outputString.charAt(i));
            content.put("internal_id", i + 1);

            chino_admin.documents.create(SCHEMA_ID, content, true);
        }

        // create a number of users and assign to their print_value one char of the output string
        for (int i = 0; i < outputString.length(); i++) {
            HashMap<String, Object> content = new HashMap<>();
            content.put("test_method", "search users test");
            content.put("print_value", outputString.charAt(i));
            content.put("internal_id", i + 1);

            chino_admin.users.create(usernamePrefix + i, "asdfgh_" + i, content, USER_SCHEMA_ID, true);
        }
    }

    @JsonPropertyOrder({
        "test_method",
        "print_value",
        "internal_id"
    })
    private static class SampleUser {
        @JsonProperty("test_method")
        @indexed
        public String test_method;
        @JsonProperty("print_value")
        public String print_value;
        @JsonProperty("internal_id")
        @indexed
        public int internal_id;
    }

    @Test
    public void testNewSearchDocuments() throws IOException, ChinoApiException {
        DocumentsSearch searchDocs = (DocumentsSearch) chino_admin.search.documents(SCHEMA_ID).setResultType(ResultType.FULL_CONTENT).addSortRule("internal_id", SortRule.Order.ASC)
                .with("test_method", EQUALS, "search documents test")
                .buildSearch();

        GetDocumentsResponse response_FULL = searchDocs.execute();
        List<Document> docs = response_FULL.getDocuments();
        List<String> ids = new LinkedList<>();
        assertNotNull("search docs returned 'null' list", docs);

        for (Document d : docs) {
            assertNotNull("Null document found at pos. " + docs.indexOf(d), d);
            ids.add(d.getDocumentId());

            assertTrue("Document search with FULL_CONTENT doesn't return Document's content", d.hasContent());
        }

        searchDocs.setResultType(ResultType.ONLY_ID);

        for (Document shortDocument : Objects.requireNonNull(searchDocs.execute()).getDocuments()) {
            assertFalse("Document fetched with ONLY_ID should not have content", shortDocument.hasContent());
            assertTrue("Document not found in docs list", ids.contains(shortDocument.getDocumentId()));
        }

        searchDocs.setResultType(ResultType.COUNT);
        GetDocumentsResponse response_COUNT = searchDocs.execute();
        assertEquals(
                "COUNT is different from previous search result number",
                (long) response_FULL.getTotalCount(),
                (long) response_COUNT.getCount()
        );
        assertTrue(response_COUNT.getDocuments().isEmpty());

        searchDocs.setResultType(ResultType.NO_CONTENT);
        List<Document> docMetadata = searchDocs.execute().getDocuments();

        for (Document d : docMetadata) {
            assertFalse(d.hasContent());
        }

        for (Document d : docs) {
            System.out.print(d.getContentAsHashMap().get("print_value"));
        }
        System.out.println();
    }

    @Test
    public void testNewSearchDocuments_InArray() throws IOException, ChinoApiException {
        List<Integer> integers = Arrays.asList(1, 999998, 999999);
        List<String> strings = Arrays.asList("search documents test", "", "a String");

        // Search value in an Array of int
        DocumentsSearch search = (DocumentsSearch) chino_admin.search.documents(SCHEMA_ID)
                .with("internal_id", FilterOperator.IN, integers)
                .buildSearch();
        GetDocumentsResponse result = search.execute();

        assertNotNull(result);
        assertEquals("Too many results!", 1, result.getTotalCount().intValue());

        Integer internalId = (Integer) result.getDocuments().get(0).getContentAsHashMap().get("internal_id");
        assertTrue(integers.contains(internalId));

        // Search value in an Array of strings
        search = (DocumentsSearch) chino_admin.search.documents(SCHEMA_ID)
                .with("test_method", FilterOperator.IN, strings)
                .buildSearch();
        result = search.execute();

        assertNotNull(result);
        assertEquals("Too many results!", outputString.length(), result.getTotalCount().intValue());

        String testMethod = (String) result.getDocuments().get(0).getContentAsHashMap().get("test_method");
        assertTrue(strings.contains(testMethod));

        // Print values
        for (Document d : result.getDocuments()) {
            System.out.print(d.getContentAsHashMap().get("print_value"));
            System.out.println();
        }
    }

    @Test
    public void testUserOffsetSize() throws IOException, ChinoApiException {
        UsersSearch searchUsers = (UsersSearch) chino_admin.search.users(USER_SCHEMA_ID).setResultType(ResultType.FULL_CONTENT).addSortRule("internal_id", SortRule.Order.ASC)
                .with("test_method", EQUALS, "search users test")
                .buildSearch();

        GetUsersResponse response_limit = searchUsers.execute( 0, 1);
        assertEquals(1, response_limit.getCount().intValue());
        int total = response_limit.getTotalCount();

        int half = total / 2 + 1;
        List<User> response_firstHalf = searchUsers.execute(0, half).getUsers();
        List<User> response_secondHalf = searchUsers.execute(half, half).getUsers();

        for (User u : response_firstHalf) {
            System.out.print(u.getAttributesAsHashMap().get("print_value"));
        }
        for (User u : response_secondHalf) {
            System.out.print(u.getAttributesAsHashMap().get("print_value"));
        }
        System.out.println();

        assertEquals("Total count and actual results not matching",
                total,
                response_firstHalf.size() + response_secondHalf.size());
    }

    @Test
    public void testDocsOffsetSize() throws IOException, ChinoApiException {
        DocumentsSearch searchDocs = (DocumentsSearch) chino_admin.search.documents(SCHEMA_ID).setResultType(ResultType.ONLY_ID).addSortRule("internal_id", SortRule.Order.ASC)
                .with("test_method", EQUALS, "search documents test")
                .buildSearch();

        GetDocumentsResponse response_limit = searchDocs.execute( 0, 1);
        assertEquals(1, response_limit.getCount().intValue());
        int total = response_limit.getTotalCount();

        int half = total / 2 + 1;
        searchDocs.setResultType(ResultType.FULL_CONTENT);
        List<Document> response_firstHalf = searchDocs.execute(0, half).getDocuments();
        List<Document> response_secondHalf = searchDocs.execute(half, half).getDocuments();

        for (Document d : response_firstHalf) {
            System.out.print(d.getContentAsHashMap().get("print_value"));
        }
        for (Document d : response_secondHalf) {
            System.out.print(d.getContentAsHashMap().get("print_value"));
        }
        System.out.println();

        assertEquals("Total count and actual results not matching",
                total,
                response_firstHalf.size() + response_secondHalf.size());
    }

    @Test
    public void testNewSearchUsers_FULLCONTENT_COUNT() throws IOException, ChinoApiException {
        UsersSearch searchUsers = (UsersSearch) chino_admin.search.users(USER_SCHEMA_ID).setResultType(ResultType.FULL_CONTENT).addSortRule("internal_id", SortRule.Order.ASC)
                .with("test_method", EQUALS, "search users test")
                .buildSearch();

        GetUsersResponse response_FULL = searchUsers.execute();
        List<User> users = response_FULL.getUsers();
        assertNotNull("search users returned 'null' list", users);

        for (User u : users) {
            assertNotNull("Null user found", u);

            assertFalse(
                    "User search with FULL_CONTENT doesn't return User's attributes",
                    u.getAttributesAsHashMap() == null || u.getAttributesAsHashMap().isEmpty()
            );
        }


        searchUsers.setResultType(ResultType.COUNT);
        GetUsersResponse response_COUNT = searchUsers.execute();
        assertEquals(
                "wrong value for COUNT.",
                response_FULL.getTotalCount(),
                response_COUNT.getCount()
        );


        for (User u : users) {
            System.out.print(u.getAttributesAsHashMap().get("print_value"));
        }
        System.out.println();
    }

    @Test
    public void testNewSearchUsers_EXISTS() throws IOException, ChinoApiException {
        UsersSearch search = (UsersSearch) chino_admin.search.users(USER_SCHEMA_ID).setResultType(ResultType.EXISTS).addSortRule("internal_id", SortRule.Order.ASC)
                .with("internal_id", GREATER_THAN, 5)
                .and(
                        with("username", EQUALS, usernamePrefix + "6")
                        .or("username", EQUALS, usernamePrefix + "7")
                )
        .buildSearch();

        assertTrue("Username should exists that matches: " + search.toString(),
                search.execute().getExists()
        );
    }

    @Test
    public void testNewSearchUsers_USERNAMEEXISTS() throws IOException, ChinoApiException {
        // check an existing username
        String theUser = usernamePrefix + 0;
        assertTrue("Unable to find user " + theUser,
                chino_admin.search.users(USER_SCHEMA_ID).usernameExists(theUser)
        );

        // check a non-existing username
        String theWrongUser = usernamePrefix + usernamePrefix + 1234;
        assertFalse("usernameExists was true for user " + theWrongUser,
                chino_admin.search.users(USER_SCHEMA_ID).usernameExists(theWrongUser)
        );
    }


    @Test
    @Deprecated
    public void testOldSearchDocuments_FULLCONTENT_COUNT() throws ChinoApiException, IOException {
        GetDocumentsResponse response_FULL = chino_admin.search
                .where("test_method").eq("search documents test")
                .resultType("FULL_CONTENT")
                .sortAscBy("internal_id")
                .searchDocuments(SCHEMA_ID);
        List<Document> docs = response_FULL.getDocuments();
        List<String> ids = new LinkedList<>();
        assertNotNull("search docs returned 'null' list", docs);

        for (Document d : docs) {
            assertNotNull("Null document found", d);
            assertTrue("Document search with FULL_CONTENT doesn't return Document's content", d.hasContent());

            ids.add(d.getDocumentId());
        }

        GetDocumentsResponse response_ONLYID = chino_admin.search
                .where("test_method").eq("search documents test")
                .resultType("ONLY_ID")
                .sortAscBy("internal_id")
                .searchDocuments(SCHEMA_ID);

        for (Document shortDocument : response_ONLYID.getDocuments()) {
            assertFalse("Document fetched with ONLY_ID should not have content", shortDocument.hasContent());
            assertTrue("Document not found in docs list", ids.contains(shortDocument.getDocumentId()));
        }

        GetDocumentsResponse response_COUNT = chino_admin.search
                .where("test_method").eq("search documents test")
                .resultType("COUNT")
                .sortAscBy("internal_id")
                .searchDocuments(SCHEMA_ID);
        assertEquals(
                "COUNT is different from previous search result number",
                (long) response_FULL.getTotalCount(),
                (long) response_COUNT.getCount()
        );
        assertTrue(response_COUNT.getDocuments().isEmpty());
        List<Document> docMetadata = response_COUNT.getDocuments();

        for (Document d : docMetadata) {
            assertFalse(d.hasContent());
        }

        for (Document d : docs) {
            System.out.print(d.getContentAsHashMap().get("print_value"));
        }
        System.out.println();
    }

    @Test
    @Deprecated
    public void testOldSearchUsers_FULLCONTENT_COUNT() throws ChinoApiException, IOException {
        // search full content and count
        GetUsersResponse response_FULL = chino_admin.search
                .where("username").eq(usernamePrefix + "6")
                .and("internal_id").gt(5)
                .resultType("FULL_CONTENT")
                .sortAscBy("internal_id")
                .searchUsers(USER_SCHEMA_ID);
        for (User u : response_FULL.getUsers()) {
            assertNotNull("Null user found", u);
            assertFalse(
                    "User search with FULL_CONTENT doesn't return User's attributes",
                    u.getAttributesAsHashMap() == null || u.getAttributesAsHashMap().isEmpty()
            );
        }

        GetUsersResponse response_COUNT = chino_admin.search
                .where("username").eq(usernamePrefix + "6")
                .and("internal_id").gt(5)
                .resultType("COUNT")
                .sortAscBy("internal_id")
                .searchUsers(USER_SCHEMA_ID);
        assertEquals(
                "COUNT is different from previous search result number",
                response_FULL.getTotalCount(),
                response_COUNT.getCount()
        );
    }

    @Test
    public void testNewSearch_Not() throws ChinoApiException, IOException {
        int ERRORS = 0;
        // Setup
        String customRepoId = chino_admin.repositories.create("testSearchDocsWithDateTime" + " [" + TestConstants.JAVA + "]").getRepositoryId();

        SchemaStructure structure = new SchemaStructure();
        List<Field> fields = new LinkedList<>();
        fields.add(new Field( "time", "time", true));
        fields.add(new Field( "date", "date", true));
        fields.add(new Field( "groupId", "string", true));
        structure.setFields(fields);
        String customSchemaId = chino_admin.schemas.create(customRepoId, "testSearchDocsWithDateTime", structure).getSchemaId();

        String content = "{\"time\": \"12:12:12\", \"date\": \"2018-12-01\", \"groupId\": \"77cf7318-851c-47df-844d-71898a370007\"}";

        String[] docIds = new String[2];
        synchronized (this) {
            for (int i=0; i<2; i++) {
                docIds[i] = chino_admin.documents.create(customSchemaId, content, true).getDocumentId();
                try {
                    wait(3000);
                } catch (InterruptedException ignored) {}
            }
        }

        // Search
        DocumentsSearch search = (DocumentsSearch) chino_admin.search.documents(customSchemaId)
                .setResultType(ResultType.FULL_CONTENT)
                .addSortRule("time", SortRule.Order.DESC)
                .with(
                        not("groupId", LOWER_THAN, "77cf7318-851c-47df-844d-71898a370007")
                )
                .and("date", EQUALS, "2018-12-01")
                .buildSearch();
        GetDocumentsResponse result = search.execute();

        System.out.println(search.toString());
        System.out.println("TOTAL COUNT: " + result.getTotalCount());
        System.err.println();
        System.out.flush();

        try {
            assertFalse("Search produced no results", result.getDocuments().isEmpty());
            for (String id : docIds) {
                assertTrue("Document <" + id + "> missing from search results", result.getIds().contains(id));
            }
        } catch (AssertionError e) {
            System.err.println(e);
            ERRORS ++;
        }

        // Tear down
        for (String id : docIds) {
            chino_admin.documents.delete(id, true);
        }
        chino_admin.schemas.delete(customSchemaId, true);
        chino_admin.repositories.delete(customRepoId, true);

        // Fail test on errors
        assertEquals(ERRORS + " errors occurred. See more on System.err output", 0, ERRORS);
    }
}
