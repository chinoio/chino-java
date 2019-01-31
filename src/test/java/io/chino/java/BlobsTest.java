package io.chino.java;

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import io.chino.api.blob.CommitBlobUploadResponse;
import io.chino.api.blob.GetBlobResponse;
import io.chino.api.common.ChinoApiException;
import io.chino.api.common.Field;
import io.chino.api.document.Document;
import io.chino.api.repository.Repository;
import io.chino.api.schema.SchemaStructure;
import io.chino.java.testutils.ChinoBaseTest;
import io.chino.java.testutils.DeleteAll;
import io.chino.java.testutils.TestConstants;
import org.junit.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class BlobsTest extends ChinoBaseTest {

    private static ChinoAPI chino_admin;
    private static Blobs test;

    private static String REPO_ID, SCHEMA_ID;
    private static String blobFieldName = "the_blob";
    private static String blobFileName = "chino_logo.jpg",
            resFolder = "src/test/res/".replace("/",  File.separator);
    private static String outputFilePath = resFolder + "control";
    private static String blobId;

    private Document blobDocument;

    @BeforeClass
    public static void setUpClass() throws Exception {
        ChinoBaseTest.beforeClass();
        chino_admin = new ChinoAPI(TestConstants.HOST, TestConstants.CUSTOMER_ID, TestConstants.CUSTOMER_KEY);
        test = ChinoBaseTest.init(chino_admin.blobs);
        ChinoBaseTest.beforeClass();

        // Create a repository and a Schema for the Blob's Document
        boolean isClean = true;
        for (Repository r : chino_admin.repositories.list().getRepositories()) {
            if (r.getDescription().contains("BlobsTest")) {
                isClean = false;
            }
        }
        ChinoBaseTest.checkResourceIsEmpty(
                isClean,
                chino_admin.blobs
        );

        REPO_ID = chino_admin.repositories.create("BlobsTest").getRepositoryId();

        LinkedList<Field> fields = new LinkedList<>();
        fields.add(new Field("testName", "string"));
        fields.add(new Field(blobFieldName, "blob"));
        SchemaStructure s = new SchemaStructure(fields);
        SCHEMA_ID = chino_admin.schemas.create(REPO_ID, "This schema is used for Documents that store BLOBs in BlobsTest", s).getSchemaId();
    }

    @Before
    public void createBlobDocument() throws IOException, ChinoApiException {
        // create new Blob's Document
        HashMap<String, String> fileContent = new HashMap<>();
        fileContent.put("testName", "BlobsTest");
        blobDocument = chino_admin.documents.create(SCHEMA_ID, fileContent);
    }

    @Test
    public void testByFilepath_upload_get_delete() throws NoSuchAlgorithmException, ChinoApiException, IOException {
        String sourceFolder = resFolder;
        String filename = blobFileName;
        System.out.println("- Using File path:");
        runTestUploadGetDelete(sourceFolder, filename);
    }

    @Test
    public void testByFileInputStream_upload_get_delete() throws NoSuchAlgorithmException, ChinoApiException, IOException {
        FileInputStream sourceStream = new FileInputStream(resFolder + blobFileName);
        String filename = blobFileName.replace(".jpg", "_fileIS.jpg");
        System.out.println("- Using FileInputStream:");
        runTestUploadGetDelete(sourceStream, filename);
    }

    @Test
    public void testByByteInputStream_upload_get_delete() throws NoSuchAlgorithmException, ChinoApiException, IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(resFolder + blobFileName)); // keep the test file size SMALL!
        ByteInputStream sourceStream = new ByteInputStream(bytes, bytes.length);
        String filename = blobFileName.replace(".jpg", "_byteIS.jpg");
        System.out.println("- Using ByteInputStream:");
        runTestUploadGetDelete(sourceStream, filename);
    }

    private void runTestUploadGetDelete(Object source, String filename) throws IOException, ChinoApiException, NoSuchAlgorithmException {
        /* UPLOAD */
        CommitBlobUploadResponse res_upload;

        if (source instanceof String) {
            res_upload = chino_admin.blobs.uploadBlob((String) source, blobDocument, blobFieldName, filename);
        } else if (source instanceof InputStream) {
            res_upload = chino_admin.blobs.uploadBlob((InputStream) source, blobDocument, blobFieldName, filename);
        } else {
            String msg = "Unrecognized BLOB source: %s.\n" +
                    "You can only upload from an InputStream or specifying the path of the source file in a String.";
            fail(String.format(msg, source.getClass().getCanonicalName()));
            return; // it's here to prevent compilation errors
        }
        long expectedSize = res_upload.getBlob().getBytes();

        blobId = res_upload.getBlob().getBlobId();

        System.out.println("BLOB uploaded successfully! ID: " + blobId);
        System.out.println();

        /* GET */
        GetBlobResponse res_get = chino_admin.blobs.get(blobId, outputFilePath);
        long size = res_get.getSize();

        assertEquals(expectedSize, size);
        assertTrue("Created an empty blob!", expectedSize > 0);
        assertTrue("Got an empty blob!", size > 0);

        System.out.println("BLOB read successfully! size: " + size + ", filename: " + res_get.getFilename());
        System.out.println();

        /* DELETE */
        chino_admin.blobs.delete(blobId);

        try {
            chino_admin.blobs.get(blobId, outputFilePath);
            fail("Blob was not deleted!");
        } catch (ChinoApiException e) {
            blobId = null;
            System.out.println("BLOB deleted successfully!");
        }
    }

    @After
    public void tearDown() throws Exception {
        // delete Document
        if (blobDocument != null) {
            chino_admin.documents.delete(blobDocument.getDocumentId(), true);
            blobDocument = null;
        }

        File outputFile = new File(outputFilePath);
        if (outputFile.exists())
            outputFile.delete();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        // cleanup leftovers
        if(blobId != null)
            try {
                chino_admin.blobs.delete(blobId);
                blobId = null;
            } catch (IOException | ChinoApiException e) {
                System.err.println("Blob not deleted: " + blobId);
            }

        File outputFile = new File(outputFilePath + File.separator + blobFileName);
        if (outputFile.exists())
            outputFile.delete();

        new DeleteAll().deleteAll(chino_admin.documents);
    }
}