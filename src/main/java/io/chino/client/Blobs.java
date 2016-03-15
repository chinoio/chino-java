package io.chino.client;

import io.chino.api.blob.*;
import io.chino.api.common.ChinoApiException;
import io.chino.api.common.ErrorResponse;
import io.chino.api.common.MD5Calc;
import io.chino.api.common.SHA1Calc;
import org.codehaus.jackson.JsonNode;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Blobs extends ChinoBaseAPI{

    static int chunkSize=1024*1024;

    public Blobs(String hostUrl, Client clientInitialized) {
        super(hostUrl, clientInitialized);
    }

    /**
     * Used to upload an entire File
     * @param path the path of the file to upload (without the name of the file)
     * @param documentId the id of the Document
     * @param field the name of the Field of the Document
     * @param fileName the name of the file to upload
     * @return a CommitBlobUploadResponse Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public CommitBlobUploadResponse uploadBlob(String path, String documentId, String field, String fileName) throws IOException, ChinoApiException{

        CreateBlobUploadResponse blobResponse = initUpload(documentId, field, fileName);
        String upload_id = blobResponse.getBlob().getUploadId();

        File file = new File(path+File.separator+fileName);
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
            uploadChunk(blobResponse.getBlob().getUploadId(), bytes, currentFilePosition, bytes.length);
            currentFilePosition=currentFilePosition+bytes.length;
            raf.seek(currentFilePosition);
        }
        raf.close();

        return commitUpload(upload_id);
    }

    /**
     * Used to get a Blob
     * @param blobId the id of the blob to get
     * @param destination the path used to save the file
     * @return a GetBlobResponse Object
     * @throws IOException
     * @throws ChinoApiException
     * @throws NoSuchAlgorithmException
     */
    public GetBlobResponse get(String blobId, String destination) throws IOException, ChinoApiException, NoSuchAlgorithmException {
        GetBlobResponse getBlobResponse=new  GetBlobResponse();
        System.out.println("getting file..");

        Response response= client.target(host).path("/blobs/"+blobId).request().get();

        getBlobResponse.setFilename(response.getHeaderString("Content-Disposition").substring(response.getHeaderString("Content-Disposition").indexOf("=")+1, response.getHeaderString("Content-Disposition").length()));
        getBlobResponse.setPath(destination+ File.separator+getBlobResponse.getFilename());

        InputStream returnStream= response.readEntity(InputStream.class);

        File file = new File(getBlobResponse.getPath());
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        int read;
        byte[] bytes = new byte[10240000];

        MessageDigest MD5Digest = MessageDigest.getInstance("MD5");
        MessageDigest SHA1Digest = MessageDigest.getInstance("SHA1");

        while ((read = returnStream.read(bytes)) != -1) {
            fileOutputStream.write(bytes, 0, read);

            MD5Digest.update(bytes, 0, read);
            SHA1Digest.update(bytes, 0, read);

        }

        fileOutputStream.close();
        returnStream.close();

        getBlobResponse.setSize(file.length());

        String SHA1 = SHA1Calc.getSHA1Checksum(SHA1Digest.digest());
        getBlobResponse.setSha1(SHA1);


        String MD5 = MD5Calc.getMD5Checksum(MD5Digest.digest());
        getBlobResponse.setMd5(MD5);

        return getBlobResponse;
    }


    /**
     * Used to init the upload
     * @param documentId the id of the Document
     * @param field the name of the Field of the Document
     * @param fileName the name of the file to upload
     * @return a CreateBlobUploadResponse Object that has the uploadId as one of the parameters
     * @throws IOException
     * @throws ChinoApiException
     */
    public CreateBlobUploadResponse initUpload(String documentId, String field, String fileName) throws IOException, ChinoApiException {
        CreateBlobUploadRequest createBlobUploadRequest=new CreateBlobUploadRequest();
        createBlobUploadRequest.setDocumentId(documentId);
        createBlobUploadRequest.setField(field);
        createBlobUploadRequest.setFileName(fileName);

        JsonNode data = postResource("/blobs", createBlobUploadRequest);
        if(data!=null)
            return mapper.readValue(data, CreateBlobUploadResponse.class);

        return null;
    }

    /**
     * Used to upload a chunk of data
     * @param uploadId the uploadId
     * @param chunkData an array of bytes that represents a chunk
     * @param offset the offset
     * @param length the length
     * @return a CreateBlobUploadResponse Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public CreateBlobUploadResponse uploadChunk(String uploadId, byte[] chunkData, int offset ,int length) throws IOException, ChinoApiException {

        JsonNode data = putResource("/blobs/"+uploadId, chunkData, offset, length);
        if(data!=null)
            return mapper.readValue(data, CreateBlobUploadResponse.class);

        return null;
    }

    /**
     * Used to commit when all the chunks are uploaded
     * @param uploadId the uploadId
     * @return a CommitBlobUploadResponse
     * @throws IOException
     * @throws ChinoApiException
     */
    public CommitBlobUploadResponse commitUpload(String uploadId) throws IOException, ChinoApiException {
        CommitBlobUploadRequest commitBlobUploadRequest = new CommitBlobUploadRequest();
        commitBlobUploadRequest.setUploadId(uploadId);

        JsonNode data = postResource("/blobs/commit", commitBlobUploadRequest);
        if(data!=null)
            return mapper.readValue(data, CommitBlobUploadResponse.class);

        return null;
    }

    /**
     * Used to delete a Blob
     * @param blobId the blobId to delete
     * @param force the boolean force
     * @return a String that represents the status of the operation
     * @throws IOException
     * @throws ChinoApiException
     */
    public String delete(String blobId, boolean force) throws IOException, ChinoApiException {
        return deleteResource("/blobs/"+blobId, force);
    }

    //An override of the function specifically to put an array of bytes
    private JsonNode putResource(String path, byte[] resource, int offset, int length) throws IOException, ChinoApiException {
        Response response = client.target(host).path(path).request(MediaType.APPLICATION_JSON_TYPE).header("offset", offset).header("length", length).put(Entity.json(resource));

        if(response.getStatus()==200){
            return mapper.readTree(response.readEntity(String.class)).get("data");
        }else{
            throw new ChinoApiException(mapper.readValue(response.readEntity(String.class), ErrorResponse.class));
        }
    }
}
