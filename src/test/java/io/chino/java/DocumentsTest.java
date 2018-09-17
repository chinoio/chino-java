package io.chino.java;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.chino.api.common.ChinoApiConstants;
import io.chino.api.common.ChinoApiException;
import io.chino.api.common.Field;
import io.chino.api.document.Document;
import io.chino.api.document.GetDocumentsResponse;
import io.chino.api.schema.SchemaStructure;
import io.chino.api.search.DocumentsSearch;
import io.chino.api.search.ResultType;
import io.chino.java.testutils.ChinoBaseTest;
import io.chino.java.testutils.DeleteAll;
import io.chino.java.testutils.MappedDocument;
import io.chino.java.testutils.TestConstants;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static io.chino.api.search.FilterOperator.EQUALS;
import static org.junit.Assert.*;

public class DocumentsTest extends ChinoBaseTest {

    private static ChinoAPI chino_admin;
    private static Documents test;

    private static String REPO_ID, SCHEMA_ID;

    @BeforeClass
    public static void beforeClass() throws IOException, ChinoApiException {
        ChinoBaseTest.beforeClass();
        chino_admin = new ChinoAPI(TestConstants.HOST, TestConstants.CUSTOMER_ID, TestConstants.CUSTOMER_KEY);
        test = ChinoBaseTest.init(chino_admin.documents);

        // create a repository and a Schema
        ChinoBaseTest.checkResourceIsEmpty(
                chino_admin.repositories.list().getRepositories().isEmpty(),
                chino_admin.repositories
        );
        REPO_ID = chino_admin.repositories.create("DocumentsTest")
                    .getRepositoryId();

        LinkedList<Field> fields = new LinkedList<>();
        fields.add(new Field("testMethod", "string", true));

        SCHEMA_ID = chino_admin.schemas.create(
                REPO_ID,
                "this Schema is used to verify that users are logged in and can create Documents.",
                new SchemaStructure(fields)
        ).getSchemaId();
    }

    @AfterClass
    public static void afterClass() throws IOException, ChinoApiException {
        new DeleteAll().deleteAll(chino_admin);
    }

    @Test
    public void testToString_setContent() throws IOException, ChinoApiException {
        Document doc = newDoc("testToString_setContent");

        System.out.println("-- toString() ---------------");
        System.out.println(doc.toString());
        System.out.println("-- END ----------------------");

        clear(doc);
    }

    @Test
    public void testList() throws IOException, ChinoApiException {
        Document doc = newDoc("testCreateListUpdate_HashMap");
        String variant = "1 param";

        try {
            List<Document> docList = test.list(SCHEMA_ID, 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT, true).getDocuments();
            assertTrue(
                    docList.contains(doc)
            );

            variant = "2 params";
            List<Document> docsList = test.list(SCHEMA_ID, true).getDocuments();
            assertTrue(
                    docsList.contains(doc)
            );
            assertEquals(
                    docsList.get(0).getContentAsHashMap(),
                    doc.getContentAsHashMap()
            );

            variant = "3 params - expected 1 document";
            docsList = test.list(SCHEMA_ID, 0, 1, true).getDocuments();
            assertTrue(
                    docsList.contains(doc)
            );
            variant = "3 params - expected no documents";
            docsList = test.list(SCHEMA_ID, 1, 1, true).getDocuments();
            assertFalse(
                    docsList.contains(doc)
            );

            // 4 params variant is ignored bc already tested with the previous calls

        } catch (Exception e) {
            System.err.println("FAILED test variant with " + variant);
            throw e;
        } finally {
            clear(doc);
        }
    }

    @Test
    public void testCreateRead_async() throws IOException, ChinoApiException {
        // create hashmap / read
        Document docHM = newDoc("testCreateReadUpdate_async");
        Document check = test.read(docHM.getDocumentId());
        assertEquals( "Different content than expected was read in fetched Document (created with HashMap)",
                docHM.getContentAsHashMap(),
                check.getContentAsHashMap()
        );

        // create json string / read
        String jsonContent = new ObjectMapper().writeValueAsString(docHM.getContentAsHashMap());
        Document docStr = test.create(SCHEMA_ID, jsonContent);
        docStr = test.read(docStr.getDocumentId()); // fetch content
        assertEquals( "Different content than expected was read in fetched Document (created with JSON String)",
                docStr.getContentAsHashMap(),
                check.getContentAsHashMap()        // must have the same content as docStr
        );

        // read and map to class
        MappedDocument docMapped = (MappedDocument) test.read(
                docHM.getDocumentId(),
                MappedDocument.class
        );
        assertEquals( "Mapped document doesn't contain the right values",
                docHM.getContentAsHashMap().get("testMethod"),
                docMapped.testMethod
        );

        clear(docHM);
        clear(docStr);
    }

    @Test
    public void testCreateRead_sync() throws IOException, ChinoApiException {
        // create hashmap / read
        HashMap<String, String> content = new HashMap<>();
        String methodName = "testCreateRead_sync";
        content.put("testMethod", methodName);

        Document docHM = test.create(SCHEMA_ID, content, true);
        docHM.setContent(content);
        Document check = test.read(docHM.getDocumentId());
        assertEquals( "Different content than expected was read in fetched Document (created with HashMap)",
                docHM.getContentAsHashMap(),
                check.getContentAsHashMap()
        );

        // create json string / read
        String jsonContent = new ObjectMapper().writeValueAsString(docHM.getContentAsHashMap());
        DocumentsSearch search = (DocumentsSearch) chino_admin.search.documents(SCHEMA_ID)
                .with("testMethod", EQUALS, methodName)
                .buildSearch();

        Document docStr = test.create(SCHEMA_ID, jsonContent, true);
        docStr.setContent(docHM.getContentAsHashMap());
        GetDocumentsResponse result = search.execute();
        assertTrue(
                result.getDocuments().contains(docStr)
        );

        clear(docHM);
        clear(docStr);
    }

    @Test
    public void testUpdateDelete_async() throws IOException, ChinoApiException {
        String name = "testUpdateDelete_async";
        String newName = "testUpdateDelete_async_updated";
        Document doc = newDoc(name);
        HashMap<String, Object> content = doc.getContentAsHashMap();
        content.put("testMethod", newName);

        test.update(doc.getDocumentId(), content);
        Document check = test.read(doc.getDocumentId());
        assertNotEquals(
                check.getContentAsHashMap(),
                doc.getContentAsHashMap()
        );
        assertEquals( "read document and updated version do not match",
                check.getContentAsHashMap(),
                content
        );

        newName += "_String";
        content.put("testMethod", newName);

        String jsonContent = new ObjectMapper().writeValueAsString(content);
        test.update(doc.getDocumentId(), jsonContent);
        check = test.read(doc.getDocumentId());
        assertNotEquals( "Document was not updated!",
                check.getContentAsHashMap(),
                doc.getContentAsHashMap()
        );
        assertEquals( "read document and updated version do not match",
                check.getContentAsHashMap(),
                content
        );

        test.delete(doc.getDocumentId(), true);
    }

    @Test
    public void testUpdateDelete_sync() throws IOException, ChinoApiException {
        String name = "testUpdateDelete_async";
        String newName = "testUpdateDelete_async_updated";
        Document doc = newDoc(name);
        HashMap<String, Object> content = doc.getContentAsHashMap();
        content.put("testMethod", newName);

        test.update(doc.getDocumentId(), content);
        Document check = test.read(doc.getDocumentId());
        assertNotEquals(
                check.getContentAsHashMap(),
                doc.getContentAsHashMap()
        );
        assertEquals( "read document and updated version do not match",
                check.getContentAsHashMap(),
                content
        );

        newName += "_String";
        content.put("testMethod", newName);


        DocumentsSearch search = (DocumentsSearch) chino_admin.search.documents(SCHEMA_ID)
                .with("testMethod", EQUALS, newName)
                .buildSearch()
                .setResultType(ResultType.FULL_CONTENT);
        doc = test.update(doc.getDocumentId(), content, true);
        doc.setContent(content);
        GetDocumentsResponse result = search.execute();
        assertTrue(
                result.getDocuments().contains(doc)
        );

        test.delete(doc.getDocumentId(), true);
    }


    private Document newDoc(String methodName) throws IOException, ChinoApiException {
        HashMap<String, String> content = new HashMap<>();
        content.put("testMethod", methodName);
        Document doc = test.create(SCHEMA_ID, content);

        doc.setContent(content);

        System.out.println();
        System.out.println("Created Document for test " + methodName);

        return doc;
    }

    private void clear(Document document) throws IOException, ChinoApiException {
        test.delete(document.getDocumentId(), true);

        System.out.println("Deleted Document.");
    }
}
