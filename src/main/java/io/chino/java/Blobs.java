package io.chino.java;

import com.fasterxml.jackson.databind.JsonNode;
import io.chino.api.blob.*;
import io.chino.api.common.ChinoApiException;
import io.chino.api.blob.MD5Calc;
import io.chino.api.blob.SHA1Calc;
import io.chino.api.document.Document;
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
     *
     */
    public Blobs(String baseApiUrl, ChinoAPI parentApiClient) {
        super(baseApiUrl, parentApiClient);
        hostUrl = baseApiUrl;
        parent = parentApiClient;
    }

    /**
     * Upload a local file to Chino.io as a BLOB. This method fully handles the upload of a BLOB.
     *
     * @param folderPath the path to the folder which contains the local file
     * @param documentId the ID of the Chino.io {@link Document} which contains this BLOB
     * @param fieldName the name of the field (of type "blob") that refers to this BLOB in the Document
     * @param fileName the name of the local file to upload
     *
     * @return A {@link CommitBlobUploadResponse} that wraps a {@link CommitBlobUploadResponseContent},
     * with information on the blob itself.
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public CommitBlobUploadResponse uploadBlob(String folderPath, String documentId, String fieldName, String fileName) throws IOException, ChinoApiException{
        checkNotNull(folderPath, "path");
        CreateBlobUploadResponse blobResponse = _initUpload(documentId, fieldName, fileName);
        String upload_id = blobResponse.getBlob().getUploadId();

        File file = new File(folderPath+File.separator+fileName);
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
            _uploadChunk(blobResponse.getBlob().getUploadId(), bytes, currentFilePosition, bytes.length);
            currentFilePosition=currentFilePosition+bytes.length;
            raf.seek(currentFilePosition);
        }
        raf.close();

        return _commitUpload(upload_id);
    }

    /**
     * Upload a local file to Chino.io as a BLOB. This method fully handles the upload of a BLOB.
     *
     * @param folderPath the path to the folder which contains the local file
     * @param document the Chino.io {@link Document} which contains this BLOB
     * @param fieldName the name of the field (of type "blob") that refers to this BLOB in the Document
     * @param fileName the name of the local file to upload
     *
     * @return A {@link CommitBlobUploadResponse} that wraps a {@link CommitBlobUploadResponseContent},
     * with information on the blob itself.
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public CommitBlobUploadResponse uploadBlob(String folderPath, Document document, String fieldName, String fileName) throws IOException, ChinoApiException {
        return uploadBlob(folderPath, document.getDocumentId(), fieldName, fileName);
    }

    /**
     * Returns the BLOB with the specified {@code blobId}
     *
     * @param blobId the id of the blob to retrieve
     * @param destination the path to a file where the BLOB will be saved
     *
     * @return {@link GetBlobResponse} which contains information about the downloaded BLOB
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     * @throws NoSuchAlgorithmException can't find MD5 / SHA algorythm
     */
    public GetBlobResponse get(String blobId, String destination) throws IOException, ChinoApiException, NoSuchAlgorithmException {
        checkNotNull(blobId, "blob_id");
        checkNotNull(destination, "destination");
        GetBlobResponse getBlobResponse=new  GetBlobResponse();

        Request request = new Request.Builder().url(hostUrl+"/blobs/"+blobId).get().build();
        Response response = parent.getHttpClient().newCall(request).execute();

        String contentDisposition = response.header("Content-Disposition");
        if (contentDisposition != null) {
            getBlobResponse.setFilename(
                    contentDisposition.substring(
                            contentDisposition.indexOf("=") + 1
                    )
            );
        }

        if (getBlobResponse.getFilename() != null) {
            getBlobResponse.setPath(destination + File.separator + getBlobResponse.getFilename());

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
        } else {
            throw new ChinoApiException("404, Blob doesn't exist");
        }

        return getBlobResponse;
    }


    /**
     * Create a BLOB object on Chino.io. This method is called by {@link #uploadBlob(String, String, String, String) uploadBlod()}
     * and should be <b>never called directly</b>. Its purpose is to initialize the metadata of the file that will be uploaded.
     *
     * @see #uploadBlob(String, String, String, String)
     *
     * @param documentId the Chino.io ID of the Document the new BLOB belongs to.
     * @param field the name of the field (with type "blob") in the Document.
     * @param fileName the name of the file to upload
     *
     * @return a {@link CreateBlobUploadResponse}, which contains the upload_id that is needed for upload the file.
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    // WARNING - When removing deprecated method:
    // rename '_initUpload' to 'initUpload' WITHOUT changing other calls, so that the public method is replaced by the private one.
    @Deprecated
    public CreateBlobUploadResponse initUpload(String documentId, String field, String fileName) throws IOException, ChinoApiException {
        return _initUpload(documentId, field, fileName);
    }

    // TODO remove deprecated method
    // rename '_initUpload' to 'initUpload' WITHOUT changing other calls, so that the public method is replaced by the private one.
    private CreateBlobUploadResponse _initUpload(String documentId, String field, String fileName) throws IOException, ChinoApiException {
        CreateBlobUploadRequest createBlobUploadRequest=new CreateBlobUploadRequest(documentId, field, fileName);

        JsonNode data = postResource("/blobs", createBlobUploadRequest);
        if(data!=null)
            return mapper.convertValue(data, CreateBlobUploadResponse.class);

        return null;
    }

    /**
     * Upload a chunk of data to the server. This method is called automatically by {@link #uploadBlob(String, String, String, String) uploadBlob()},
     * thus should be <b>never called directly</b>.
     *
     * @see #uploadBlob(String, String, String, String)
     *
     * @param uploadId the upload_id returned by {@link #initUpload(String, String, String) initUpload()}
     * @param chunkData a chunk of data in the form of a byte array
     * @param offset the offset of the chunk relative to the beginning of the file
     * @param length the length of the chunk
     * @return A {@link CreateBlobUploadResponse}, which contains the status of the operation
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    // WARNING - When removing deprecated method:
    // rename '_uploadChunk' to 'uploadChunk' WITHOUT changing other calls, so that the public method is replaced by the private one.
    @Deprecated
    public CreateBlobUploadResponse uploadChunk(String uploadId, byte[] chunkData, int offset, int length) throws IOException, ChinoApiException {
        return _uploadChunk(uploadId,chunkData,offset,length);
    }

    // TODO remove deprecated method
    // rename '_uploadChunk' to 'uploadChunk' WITHOUT changing other calls, so that the public method is replaced by the private one.
    private CreateBlobUploadResponse _uploadChunk(String uploadId, byte[] chunkData, int offset, int length) throws IOException, ChinoApiException {
        JsonNode data = putResource("/blobs/"+uploadId, chunkData, offset, length);
        if(data!=null)
            return mapper.convertValue(data, CreateBlobUploadResponse.class);

        return null;
    }

    /**
     * Confirm and complete the upload on Chino.io.This method is called automatically by {@link #uploadBlob(String, String, String, String) uploadBlob()},
     * thus should be <b>never called directly</b>.
     *
     * @see #uploadBlob(String, String, String, String)
     *
     * @param uploadId the upload_id returned by {@link #initUpload(String, String, String) initUpload()}
     *
     * @return A {@link CommitBlobUploadResponse}, which contains the status of the operation
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    // WARNING - When removing deprecated method:
    // rename '_commitUpload' to 'commitUpload' WITHOUT changing other calls, so that the public method is replaced by the private one.
    @Deprecated
    public CommitBlobUploadResponse commitUpload(String uploadId) throws IOException, ChinoApiException {
        return _commitUpload(uploadId);
    }

    // TODO remove deprecated method
    // rename '_commitUpload' to 'commitUpload' WITHOUT changing other calls, so that the public method is replaced by the private one.
    private CommitBlobUploadResponse _commitUpload(String uploadId) throws IOException, ChinoApiException {
        CommitBlobUploadRequest commitBlobUploadRequest = new CommitBlobUploadRequest(uploadId);

        JsonNode data = postResource("/blobs/commit", commitBlobUploadRequest);
        if(data!=null)
            return mapper.convertValue(data, CommitBlobUploadResponse.class);

        return null;
    }

    /**
     * Delete a BLOB from Chino.io. This operation can not be undone.
     *
     * @param blobId the id of the BLOB to delete
     * @param force if true, the resource cannot be restored. Otherwise, it will only become inactive. THIS FLAG IS IGNORED.
     *
     * @return a String with the result of the operation
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    @Deprecated
    public String delete(String blobId, boolean force) throws IOException, ChinoApiException {
        checkNotNull(blobId, "blob_id");
        return deleteResource("/blobs/"+blobId, force);
    }

    /**
     * Delete a BLOB from Chino.io. This operation can not be undone.
     *
     * @param blobId the id of the BLOB to delete
     *
     * @return a String with the result of the operation
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public String delete(String blobId) throws IOException, ChinoApiException {
        checkNotNull(blobId, "blob_id");
        return deleteResource("/blobs/"+blobId, false);
    }
}
