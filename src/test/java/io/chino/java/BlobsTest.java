package io.chino.java;

import io.chino.api.common.Field;
import io.chino.api.document.Document;
import io.chino.api.userschema.UserSchemaStructure;
import io.chino.java.testutils.ChinoBaseTest;
import io.chino.java.testutils.TestConstants;
import org.junit.*;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class BlobsTest extends ChinoBaseTest {

    ChinoAPI chino_customer;
    Blobs test;

    static String repoID, schemaID;
    Document owner;

    @BeforeClass
    public void setUpClass() throws Exception {
        chino_customer = new ChinoAPI(TestConstants.HOST, TestConstants.CUSTOMER_ID, TestConstants.CUSTOMER_KEY);
        test = (Blobs) ChinoBaseTest.init(chino_customer.blobs);
        ChinoBaseTest.beforeClass();

        repoID = chino_customer.repositories.create("BlobsTest").getRepositoryId();
        LinkedList<Field> fields = new LinkedList<>();
        UserSchemaStructure s = new UserSchemaStructure();
        schemaID = chino_customer.schemas.create(repoID, "This schema is used for Documents that store BLOBs in  BlobsTest");
    }

    @Before
    public void setUp() throws Exception {
        owner = chino_customer.documents.create()
    }

    @Test
    public void uploadBlob() {
    }

    @Test
    public void get() {
    }

    @Test
    public void delete() {
    }

    @After
    public void tearDown() throws Exception {
    }

    @AfterClass
    public void tearDownClass() throws Exception {
        ChinoBaseTest.afterClass();
    }
}