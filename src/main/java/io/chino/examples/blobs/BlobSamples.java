package io.chino.examples.blobs;

import io.chino.api.blob.CommitBlobUploadResponse;
import io.chino.api.blob.CreateBlobUploadResponse;
import io.chino.api.blob.GetBlobResponse;
import io.chino.api.common.ChinoApiException;
import io.chino.api.document.Document;
import io.chino.api.repository.Repository;
import io.chino.api.schema.Schema;
import io.chino.android.ChinoAPI;
import io.chino.examples.schemas.SchemaStructureSample;
import io.chino.examples.util.Constants;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class BlobSamples {

    int chunkSize=100*1024;
    final String FILE_NAME = "003.pdf";
    final String FILE_NAME_2 = "0008mb.pdf";
    final String PATH = "attachments";
    String REPOSITORY_ID = "";
    String SCHEMA_ID = "";
    String DOCUMENT_ID = "";
    String DOCUMENT_ID_2 = "";
    String UPLOAD_ID = "";
    String FIRST_BLOB_ID = "";
    String SECOND_BLOB_ID = "";
    ChinoAPI chino;

    public void testBlobs() throws IOException, ChinoApiException {

        //We initialize the ChinoAPI variable with the customerId and customerKey
        chino = new ChinoAPI(Constants.HOST, Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY);

        //First of all we need a repository
        Repository repository = chino.repositories.create("test_repository");
        REPOSITORY_ID = repository.getRepositoryId();

        /*
            Then we need to create a Schema with a field of type "blob". In this case we try to construct it passing a Class
            in which there are some public variables that are used to construct the fields of the Schema.
            In this case the field "File" will automatically create a "blob" type field on the server
        */
        Schema schema = chino.schemas.create(REPOSITORY_ID, "sample_description", SchemaStructureSample.class);
        SCHEMA_ID = schema.getSchemaId();

        //And finally we want a Document
        HashMap<String, Object> content = new HashMap<String, Object>();
        content.put("test_string", "test_string_value");
        content.put("test_integer", 123);
        content.put("test_boolean", true);
        content.put("test_date", "1994-02-03");

        Document document = chino.documents.create(SCHEMA_ID, content);
        DOCUMENT_ID = document.getDocumentId();

        document = chino.documents.create(SCHEMA_ID, content);
        DOCUMENT_ID_2 = document.getDocumentId();

        //Now we try two way to upload a Blob, the first is very simple and is as follow
        CommitBlobUploadResponse commitBlobUploadResponse = chino.blobs.uploadBlob(PATH, DOCUMENT_ID, "test_file", FILE_NAME);
        System.out.println(commitBlobUploadResponse);
        FIRST_BLOB_ID = commitBlobUploadResponse.getBlob().getBlobId();

        //That's it! Now the second way, a bit more difficult and we try it with another document
        //We recommend to use this second way only if there is the necessity to manage how the file is divided in chunk and uploaded to the server
        CreateBlobUploadResponse blobResponse = chino.blobs.initUpload(DOCUMENT_ID_2, "test_file", FILE_NAME_2);
        UPLOAD_ID = blobResponse.getBlob().getUploadId();

        //We have instantiated the upload, and now we need to upload one chunk at time
        File file = new File(PATH+File.separator+FILE_NAME_2);
        RandomAccessFile raf = new RandomAccessFile(file, "r");

        byte[] bytes;
        int currentFilePosition=0;
        raf.seek(currentFilePosition);

        while (currentFilePosition<raf.length()){
            int distanceFromEnd=(int)(raf.length()-currentFilePosition);
            if(distanceFromEnd > chunkSize)
                bytes = new byte[chunkSize];
            else
                bytes = new byte[distanceFromEnd];

            raf.read(bytes);
            chino.blobs.uploadChunk(blobResponse.getBlob().getUploadId(), bytes, currentFilePosition, bytes.length);
            currentFilePosition=currentFilePosition+bytes.length;
            raf.seek(currentFilePosition);
        }
        raf.close();

        //Finally we commit the upload to end the connection
        commitBlobUploadResponse = chino.blobs.commitUpload(UPLOAD_ID);
        System.out.println(commitBlobUploadResponse);
        SECOND_BLOB_ID = commitBlobUploadResponse.getBlob().getBlobId();

        //And now we try to get the last Blob already created saving it in another folder
        GetBlobResponse getBlobResponse;
        try {
            getBlobResponse = chino.blobs.get(FIRST_BLOB_ID, PATH+"/get");
            System.out.println(getBlobResponse);
            getBlobResponse = chino.blobs.get(SECOND_BLOB_ID, PATH+"/get");
            System.out.println(getBlobResponse);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //Finally we delete everything we created
        System.out.println(chino.blobs.delete(FIRST_BLOB_ID, true));
        System.out.println(chino.blobs.delete(SECOND_BLOB_ID, true));
        System.out.println(chino.documents.delete(DOCUMENT_ID_2, true));
        System.out.println(chino.documents.delete(DOCUMENT_ID, true));
        System.out.println(chino.schemas.delete(SCHEMA_ID, true));
        System.out.println(chino.repositories.delete(REPOSITORY_ID, true));
    }
}
