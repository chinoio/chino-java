package io.chino.java;

import com.fasterxml.jackson.databind.JsonNode;
import io.chino.api.blob.*;
import io.chino.api.common.ChinoApiException;
import io.chino.api.common.MD5Calc;
import io.chino.api.common.SHA1Calc;
import okhttp3.Request;
import okhttp3.Response;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Blobs extends ChinoBaseAPI {

    static int chunkSize=1024*1024;
    private final ChinoAPI parent;
    private final String hostUrl;

    /**
     * The default constructor used by all {@link ChinoBaseAPI} subclasses
     *
     * @param baseApiUrl      the base URL of the Chino.io API. For testing, use:<br>
     *                        {@code https://api.test.chino.io/v1/}
     * @param parentApiClient the instance of {@link ChinoAPI} that created this object
     */
    public Blobs(String baseApiUrl, ChinoAPI parentApiClient) {
        super(baseApiUrl, parentApiClient);
        hostUrl = baseApiUrl;
        parent = parentApiClient;
    }

    /**
     * It uploads an entire File
     * @param path the path of the file to upload (without the name of the file)
     * @param documentId the id of the Document to which upload the file
     * @param field the name of the Field in the Document
     * @param fileName the name of the file to upload
     * @return CommitBlobUploadResponse Object which contains the status of the operation
     * @throws IOException
     * @throws ChinoApiException
     */
    public CommitBlobUploadResponse uploadBlob(String path, String documentId, String field, String fileName) throws IOException, ChinoApiException{
        checkNotNull(path, "path");
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
     * Returns the Blob requested
     * @param blobId the id of the blob to retrieve
     * @param destination the path where to save the file
     * @return GetBlobResponse Object which contains the Blob Object
     * @throws IOException
     * @throws ChinoApiException
     * @throws NoSuchAlgorithmException
     */
    public GetBlobResponse get(String blobId, String destination) throws IOException, ChinoApiException, NoSuchAlgorithmException {
        checkNotNull(blobId, "blob_id");
        checkNotNull(destination, "destination");
        GetBlobResponse getBlobResponse=new  GetBlobResponse();

        Request request = new Request.Builder().url(hostUrl+"/blobs/"+blobId).get().build();
        Response response = parent.getHttpClient().newCall(request).execute();

        getBlobResponse.setFilename(response.header("Content-Disposition").substring(response.header("Content-Disposition").indexOf("=")+1, response.header("Content-Disposition").length()));
        getBlobResponse.setPath(destination+ File.separator+getBlobResponse.getFilename());

        InputStream returnStream = response.body().byteStream();

        File file = new File(getBlobResponse.getPath());
        file.getParentFile().mkdirs();
        FileOutputStream fileOutputStream = new FileOutputStream(file);

        int read;
        byte[] bytes = new byte[8*1024];

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
     * It initialize the upload
     * @param documentId the id of the Document to which upload the file
     * @param field the name of the Field in the Document
     * @param fileName the name of the file to upload
     * @return CreateBlobUploadResponse Object which contains the upload_id used for the upload of the Blob
     * @throws IOException
     * @throws ChinoApiException
     */
    public CreateBlobUploadResponse initUpload(String documentId, String field, String fileName) throws IOException, ChinoApiException {
        CreateBlobUploadRequest createBlobUploadRequest=new CreateBlobUploadRequest(documentId, field, fileName);

        JsonNode data = postResource("/blobs", createBlobUploadRequest);
        if(data!=null)
            return mapper.convertValue(data, CreateBlobUploadResponse.class);

        return null;
    }

    /**
     * It uploads a chunk of data
     * @param uploadId the upload_id created on the initialization of the upload
     * @param chunkData an array of bytes that represents a chunk
     * @param offset the offset of the chunk
     * @param length the length of the chunk
     * @return CreateBlobUploadResponse Object which contains the status of the operation
     * @throws IOException
     * @throws ChinoApiException
     */
    public CreateBlobUploadResponse uploadChunk(String uploadId, byte[] chunkData, int offset, int length) throws IOException, ChinoApiException {
        JsonNode data = putResource("/blobs/"+uploadId, chunkData, offset, length);
        if(data!=null)
            return mapper.convertValue(data, CreateBlobUploadResponse.class);

        return null;
    }

    /**
     * It does the final commit when all the chunks are uploaded
     * @param uploadId the upload_id created on the initialization of the upload
     * @return CommitBlobUploadResponse Object which contains the status of the operation
     * @throws IOException
     * @throws ChinoApiException
     */
    public CommitBlobUploadResponse commitUpload(String uploadId) throws IOException, ChinoApiException {
        CommitBlobUploadRequest commitBlobUploadRequest = new CommitBlobUploadRequest(uploadId);

        JsonNode data = postResource("/blobs/commit", commitBlobUploadRequest);
        if(data!=null)
            return mapper.convertValue(data, CommitBlobUploadResponse.class);

        return null;
    }

    /**
     * It deletes a Blob
     * @param blobId the id of the Blob to delete
     * @param force if true, the resource cannot be restored
     * @return a String with the result of the operation
     * @throws IOException
     * @throws ChinoApiException
     */
    public String delete(String blobId, boolean force) throws IOException, ChinoApiException {
        checkNotNull(blobId, "blob_id");
        return deleteResource("/blobs/"+blobId, force);
    }
}
