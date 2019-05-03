package io.chino.java;

import io.chino.api.collection.Collection;
import io.chino.api.common.ChinoApiException;
import io.chino.api.common.Field;
import io.chino.api.document.Document;
import io.chino.api.schema.SchemaStructure;
import io.chino.java.testutils.ChinoBaseTest;
import io.chino.java.testutils.TestConstants;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class CollectionsTest extends ChinoBaseTest {

    private static ChinoAPI chino_admin;
    private static Collections test;

    private static String REPO_ID, SCHEMA_ID;

    @BeforeClass
    public static void beforeClass() throws IOException, ChinoApiException {
        ChinoBaseTest.beforeClass();
        chino_admin = new ChinoAPI(TestConstants.HOST, TestConstants.CUSTOMER_ID, TestConstants.CUSTOMER_KEY);
        test = ChinoBaseTest.init(chino_admin.collections);

        // create a repository and a Schema

        ChinoBaseTest.checkResourceIsEmpty(
                chino_admin.collections.list().getCollections().isEmpty(),
                chino_admin.collections
        ); // cleans repositories too

        REPO_ID = chino_admin.repositories.create("CollectionsTest"  + " [" + TestConstants.JAVA + "]")
                .getRepositoryId();

        LinkedList<Field> fields = new LinkedList<>();
        fields.add(new Field("title", "string", true));

        SCHEMA_ID = chino_admin.schemas.create(
                REPO_ID,
                "this Schema is used to verify that users are logged in and can create Documents.",
                new SchemaStructure(fields)
        ).getSchemaId();
    }

    @Test
    public void test_CRUD() throws Exception {
        /* CREATE */
        String collectionName = "Collection for testing Chino.io SDK";
        Collection c = test.create(collectionName);
        assertNotNull("Collection was not created! (null reference)", c);

        /* READ */
        Collection r = test.read(c.getCollectionId());
        assertEquals("Read object is different from original", c, r);

        /* UPDATE */
        Collection u = test.update(c.getCollectionId(), "UPDATED")
                        .getCollection();
        assertNotEquals("Object was not updated", c, u);
        assertEquals("Update failed", "UPDATED", u.getName());

        /* DELETE */
        test.delete(u.getCollectionId(), true);

        try {
            test.read(c.getCollectionId());
            fail("Object was not deleted.");
        } catch (ChinoApiException e) {
            System.out.println("Success");
        }
    }

    @Test
    public void test_documents() throws Exception {
        Document[] docs = {
                newDoc("test_documents_doc1"),
                newDoc("test_documents_doc2"),
                newDoc("test_documents_doc3")
        };
        Collection coll = test.create("test_documents");

        /* ADD */
        for (Document doc : docs) {
            test.addDocument(coll.getCollectionId(), doc.getDocumentId());
        }

        /* LIST DOCUMENTS */
        assertEquals("Not all documents have been added",
                docs.length,
                test.listDocuments(coll.getCollectionId()).getDocuments().size()
        );

        assertEquals("Wrong list size",
                1,
                test.listDocuments(coll.getCollectionId(), 2, 2).getDocuments().size()
        );

        assertEquals("Wrong list size",
                2,
                test.listDocuments(coll.getCollectionId(), 1, 2).getDocuments().size()
        );

        /* REMOVE */
        test.removeDocument(coll.getCollectionId(), docs[0].getDocumentId());
        List<Document> ls = test.listDocuments(coll.getCollectionId()).getDocuments();
        assertEquals("Document not removed",
                docs.length - 1,
                ls.size()
        );
        for (Document doc : ls) {
            test.removeDocument(coll.getCollectionId(), doc.getDocumentId());
        }

        ls = test.listDocuments(coll.getCollectionId()).getDocuments();
        assertEquals("Not all documents have been removed",
                0,
                ls.size()
        );
    }

    @Test
    public void test_list() throws Exception {
        Collection[] collections = {
                test.create("test_list_coll1"),
                test.create("test_list_coll2"),
                test.create("test_list_coll3"),
                test.create("test_list_coll4"),
                test.create("test_list_coll5")
        };
        /* LIST (no args) */
        assertEquals( "Missing collections in list",
                collections.length,
                test.list().getCollections().size()
        );

        /* LIST (2 args) */
        int offset = 0;
        int limit = 2;
        assertEquals( "Wrong list size (1)",
                limit,
                test.list(offset, limit).getCollections().size()
        );

        offset = collections.length - 1;
        limit = collections.length;
        assertEquals( "Wrong list size (2)",
                1,
                test.list(offset, limit).getCollections().size()
        );

        offset = 2;
        limit = collections.length;
        assertEquals( "Wrong list size (3)",
                limit - offset,
                test.list(offset, limit).getCollections().size()
        );
    }

    @Test
    public void testActivate() throws IOException, ChinoApiException {
        Collection coll = test.create("test_activation");
        String id = coll.getCollectionId();
        // Set is_active = false
        test.delete(id, false);
        assertFalse("Failed to set inactive", test.read(id).getIsActive());
        // Set is_active = true
        test.update(true, id, "test_activation_updated");
        Collection control = test.read(id);
        // Verify update
        assertTrue("Failed to activate", control.getIsActive());
        assertNotEquals("Failed to update after activation",
                coll.getName(),
                control.getName()
        );

        test.delete(id, true);
    }

    private Document newDoc(String docTitle) throws IOException, ChinoApiException {
        HashMap<String, String> content = new HashMap<>();
        content.put("title", docTitle);

        return chino_admin.documents.create(SCHEMA_ID, content);
    }

    @AfterClass
    public static void afterClass() throws IOException, ChinoApiException {
        ChinoBaseTest.afterClass();
    }

}
