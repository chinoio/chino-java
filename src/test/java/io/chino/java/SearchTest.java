package io.chino.java;

import com.fasterxml.jackson.annotation.JsonProperty;
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
import io.chino.java.testutils.DeleteAll;
import io.chino.java.testutils.TestConstants;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static io.chino.api.search.SearchQueryBuilder.with;
import static org.junit.Assert.*;

public class SearchTest extends ChinoBaseTest {

    private static ChinoAPI chino_admin;

    private static String REPO_ID, SCHEMA_ID, USER_SCHEMA_ID;

    private final static String outputString = "test passed";
    private static class SampleUser {
        @JsonProperty("test_method")
        @indexed
        public String test_method;
        @JsonProperty("value")
        public String value;
        @JsonProperty("order")
        @indexed
        public int order;
    }

    @BeforeClass
    public static void beforeClass() throws IOException, ChinoApiException {
        ChinoBaseTest.beforeClass();
        chino_admin = new ChinoAPI(TestConstants.HOST, TestConstants.CUSTOMER_ID, TestConstants.CUSTOMER_KEY);
        ChinoBaseTest.init(chino_admin.search);

        // create a repository and a Schema
        ChinoBaseTest.checkResourceIsEmpty(
                chino_admin.repositories.list().getRepositories().isEmpty(),
                chino_admin.repositories
        );
        REPO_ID = chino_admin.repositories.create("SearchTest")
                .getRepositoryId();

        LinkedList<Field> fields = new LinkedList<>();
        fields.add(new Field("test_method", "string", true));
        fields.add(new Field("value", "string"));
        fields.add(new Field("order", "integer", true));

        SCHEMA_ID = chino_admin.schemas.create(
                REPO_ID,
                "this Schema is searched for Documents using the new Chino.io Search interface.",
                new SchemaStructure(fields)
        ).getSchemaId();

        // create a UserSchema
        USER_SCHEMA_ID = chino_admin.userSchemas.create(
                "This UserSchema is searched for Users using the new Chino.io Search interface.",
                SampleUser.class
        ).getUserSchemaId();

        for (int i=0; i<outputString.length(); i++) {
            HashMap<String, Object> content = new HashMap<>();
            content.put("test_method", "search documents test");
            content.put("value", outputString.charAt(i));
            content.put("order", i + 1);

            chino_admin.documents.create(SCHEMA_ID, content,true);
        }
        for (int i=0; i<outputString.length(); i++) {
            HashMap<String, Object> content = new HashMap<>();
            content.put("test_method", "search users test");
            content.put("value", outputString.charAt(i));
            content.put("order", i + 1);

            chino_admin.users.create("SearchUser" + i, "asdfgh_" + i, content, USER_SCHEMA_ID);
        }
    }

    @AfterClass
    public static void afterClass() throws IOException, ChinoApiException {
        ChinoBaseTest.afterClass();
    }

    @Test
    public void testSearchDocuments() throws IOException, ChinoApiException, InterruptedException {
        DocumentsSearch searchDocs = (DocumentsSearch) chino_admin.search.documents(SCHEMA_ID).setResultType(ResultType.FULL_CONTENT).addSortRule("order", SortRule.Order.ASC)
                .with("test_method", FilterOperator.EQUALS, "search documents test")
                .buildSearch();

        GetDocumentsResponse response_FULL = searchDocs.execute();
        List<Document> docs = response_FULL.getDocuments();
        List<String> ids = new LinkedList<>();
        assertNotNull("search docs returned 'null' list", docs);

        for (Document d : docs) {
            assertNotNull("Null document found", d);
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
            System.out.print(d.getContentAsHashMap().get("value"));
        }
    }

    @Test
    public void testSearchUsers_FULLCONTENT_COUNT() throws IOException, ChinoApiException, InterruptedException {
        UsersSearch searchUsers = (UsersSearch) chino_admin.search.users(USER_SCHEMA_ID).setResultType(ResultType.FULL_CONTENT).addSortRule("order", SortRule.Order.ASC)
                .with("test_method", FilterOperator.EQUALS, "search users test")
                .buildSearch();

        GetUsersResponse response_FULL = searchUsers.execute();
        List<User> users = response_FULL.getUsers();
        List<String> ids = new LinkedList<>();
        assertNotNull("search docs returned 'null' list", users);

        for (User u : users) {
            assertNotNull("Null document found", u);
            ids.add(u.getUserId());

            assertFalse(
                    "Document search with FULL_CONTENT doesn't return Document's content",
                    u.getAttributesAsHashMap() == null || u.getAttributesAsHashMap().isEmpty()
            );
        }
        

        searchUsers.setResultType(ResultType.COUNT);
        GetUsersResponse response_COUNT = searchUsers.execute();
        assertEquals(
                "COUNT is different from previous search result number",
                response_FULL.getTotalCount(),
                response_COUNT.getCount()
        );


        for (User u : users) {
            System.out.print(u.getAttributesAsHashMap().get("value"));
        }
    }

//    TODO: check USERNAME_EXISTS and EXISTS, then reactivate tests
//
//    @Test
//    public void testSearchUsers_EXISTS() throws IOException, ChinoApiException {
//        UsersSearch search = (UsersSearch) chino_admin.search.users(USER_SCHEMA_ID).setResultType(ResultType.EXISTS).addSortRule("order", SortRule.Order.ASC)
//                .with("order", FilterOperator.GREATER_THAN, 5)
//                .and(
//                        with("username", FilterOperator.EQUALS, "asdfgh_6")
//                        .or("username", FilterOperator.EQUALS, "asdfgh_7")
//                )
//        .buildSearch();
//
//        System.out.println(
//                Objects.requireNonNull(search.execute()).toString()
//        );
//    }
//
//    @Test
//    public void testSearchUsers_USERNAMEEXISTS() throws IOException, ChinoApiException {
//        UsersSearch search = (UsersSearch) chino_admin.search.users(USER_SCHEMA_ID).setResultType(ResultType.USERNAME_EXISTS).addSortRule("order", SortRule.Order.ASC)
//                .with("order", FilterOperator.GREATER_THAN, 5)
//                .and(
//                        with("username", FilterOperator.EQUALS, "asdfgh_6")
//                        .or("username", FilterOperator.EQUALS, "asdfgh_7")
//                )
//        .buildSearch();
//
//        System.out.println(
//                Objects.requireNonNull(search.execute()).toString()
//        );
//    }

}
