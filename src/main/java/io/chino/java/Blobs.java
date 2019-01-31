package io.chino.java;

import com.fasterxml.jackson.databind.JsonNode;
import io.chino.api.blob.*;
import io.chino.api.common.ChinoApiException;
import io.chino.api.document.Document;
import okhttp3.Request;
import okhttp3.Response;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Manage upload and download of binary files as {@link Blobs BLOBs}.
 */
public class Blobs extends ChinoBaseAPI {

    /**
     * the default chunk size used by {@link #uploadBlob(String, String, String, String)}
     */
    private static final int CHUNK_SIZE = 1024 * 1024;

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
    public CommitBlobUploadResponse uploadBlob(String folderPath, String documentId, String fieldName, String fileName)
            throws IOException, ChinoApiException
    {
        checkNotNull(folderPath, "path");

        // get upload ID for uploading chunks
        CreateBlobUploadResponse blobResponse = initUpload(documentId, fieldName, fileName);
        String uploadId = blobResponse.getBlob().getUploadId();

        // open file to upload
        File file = new File(folderPath+File.separator+fileName);
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        raf.seek(0);

        // upload chunks
        int bytesUploaded=0;
        while (bytesUploaded < raf.length()){
            // read chunk
            byte[] currentChunk;
            int bytesLeft = (int) (raf.length() - bytesUploaded);
            currentChunk = (bytesLeft > CHUNK_SIZE)
                    ? new byte[CHUNK_SIZE]
                    : new byte[bytesLeft];
            raf.read(currentChunk);

            uploadChunk(uploadId, currentChunk, bytesUploaded, currentChunk.length);

            // move marker in file
            bytesUploaded += currentChunk.length;
            raf.seek(bytesUploaded);
        }
        raf.close();

        return commitUpload(uploadId);
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
    public CommitBlobUploadResponse uploadBlob(String folderPath, Document document, String fieldName, String fileName)
            throws IOException, ChinoApiException
    {
        return uploadBlob(folderPath, document.getDocumentId(), fieldName, fileName);
    }

    /**
     * Upload a local file to Chino.io as a BLOB, read from an {@link InputStream}. This method fully handles the
     * upload of a BLOB but does NOT close the stream.
     *
     * @param fileData an {@link InputStream} where the binary data to upload can be read from.
     *                 <b>WARNING:</b> the Stream must be {@link InputStream#close() closed} in order for the SDK to
     *                 complete the upload.
     * @param documentId the ID of the Chino.io {@link Document} which contains this BLOB
     * @param fieldName the name of the field (of type "blob") that refers to this BLOB in the Document
     * @param fileName the name of the file after the upload
     *
     * @return A {@link CommitBlobUploadResponse} that wraps a {@link CommitBlobUploadResponseContent},
     * with information on the blob itself.
     *
     * @throws IOException data processing error, e.g.: the {@link InputStream} was closed before the file was read
     * @throws ChinoApiException server error
     */
    public CommitBlobUploadResponse uploadBlob(InputStream fileData, String documentId, String fieldName, String fileName)
            throws IOException, ChinoApiException
    {
        checkNotNull(fileData, "file data stream");

        // get upload ID for uploading chunks
        CreateBlobUploadResponse blobResponse = initUpload(documentId, fieldName, fileName);
        String uploadId = blobResponse.getBlob().getUploadId();

        // upload chunks
        byte[] currentChunk = new byte[CHUNK_SIZE];
        int bytesUploaded=0;
        // read next chunk, max size: CHUNK_SIZE
        int bytesRead = fileData.read(currentChunk);
        while (bytesRead >= 0) {
            switch (bytesRead) {
                case CHUNK_SIZE:
                    // upload a full chunk
                    uploadChunk(uploadId, currentChunk, bytesUploaded, CHUNK_SIZE);
                    break;
                case 0: // no data in the current chunk, but the stream has not ended yet
                    break;
                default:
                    // upload a smaller chunk
                    byte[] smallChunk = new byte[bytesRead];
                    System.arraycopy(currentChunk, 0, smallChunk, 0, bytesRead);
                    uploadChunk(uploadId, smallChunk, bytesUploaded, bytesRead);
                    break;
            }
            // update offset0
            bytesUploaded += bytesRead;
            // read next chunk
            bytesRead = fileData.read(currentChunk);
        }

        return commitUpload(uploadId);
    }

    /**
     * Upload a local file to Chino.io as a BLOB, read from an {@link InputStream}. This method fully handles the
     * upload of a BLOB but does NOT close the stream.
     *
     * @param fileData an {@link InputStream} where the binary data to upload can be read from
     * @param document the Chino.io {@link Document} which contains this BLOB
     * @param fieldName the name of the field (of type "blob") that refers to this BLOB in the Document
     * @param fileName the name of the file after the upload
     *
     * @return A {@link CommitBlobUploadResponse} that wraps a {@link CommitBlobUploadResponseContent},
     * with information on the blob itself.
     *
     * @throws IOException data processing error, e.g.: the {@link InputStream} was closed before the file was read
     * @throws ChinoApiException server error
     */
    public CommitBlobUploadResponse uploadBlob(InputStream fileData, Document document, String fieldName, String fileName)
            throws IOException, ChinoApiException
    {
        return uploadBlob(fileData, document.getDocumentId(), fieldName, fileName);
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
     * @throws NoSuchAlgorithmException can't find MD5 / SHA algorithm
     */
    public GetBlobResponse get(String blobId, String destination)
            throws IOException, ChinoApiException, NoSuchAlgorithmException
    {
        checkNotNull(blobId, "blob_id");
        checkNotNull(destination, "destination");
        GetBlobResponse getBlobResponse=new  GetBlobResponse();

        Request request = new Request.Builder().url(hostUrl+"/blobs/"+blobId).get().build();
        Response response = parent.getHttpClient().newCall(request).execute();

        try {
            // read location of file from HTTP header, e.g.: "attachment; filename=chino_logo.jpg"
            String contentDisposition = response.header("Content-Disposition");
            if (contentDisposition != null) {
//                String fileName = contentDisposition.substring(contentDisposition.indexOf("=") + 1);

                /* Content-Disposition header can contain either 'filename*' or 'filename'.
                 * The former has precedence if both are present, so we try to read them in order.
                 * Learn more at https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Disposition#Syntax
                 */
                String fileName = contentDisposition.contains("filename*")
                        ? contentDisposition.split("filename\\*=")[1] // discard everything before "filename*="
                        : null;

                if (fileName == null) {
                    fileName = contentDisposition.contains("filename")
                            ? contentDisposition.split("filename=")[1] // discard everything before "filename="
                            : null;
                }
                getBlobResponse.setFilename(fileName);
            }

            if (getBlobResponse.getFilename() != null) {
                getBlobResponse.setPath(destination + File.separator + getBlobResponse.getFilename());

                // read bytes from response and write them to file
                InputStream returnStream = response.body().byteStream();

                File file = new File(getBlobResponse.getPath());
                file.getParentFile().mkdirs();
                FileOutputStream fileOutputStream = new FileOutputStream(file);

                // Compute MD5 and SHA1 hashes while writing file to disk
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

                String SHA1 = SHA1Calc.getSHA1Checksum(SHA1Digest.digest());
                String MD5 = MD5Calc.getMD5Checksum(MD5Digest.digest());

                getBlobResponse.setSize(file.length());
                getBlobResponse.setSha1(SHA1);
                getBlobResponse.setMd5(MD5);
            } else {
                throw new ChinoApiException("404, Blob doesn't exist");
            }
        } finally { // close response body to prevent warnings from OkHttp3
            response.body().close();
        }

        return getBlobResponse;
    }


    /**
     * Create a BLOB object on Chino.io and initialize the metadata of the file that will be uploaded.
     * This method must always be invoked <b>before</b> {@link #uploadChunk(String, byte[], int, int)}.
     *
     * @see #uploadBlob(String, String, String, String)
     *
     * @param documentId the Chino.io ID of the {@link Document} the new BLOB belongs to. Must contain a field of type
     *                   "blob"
     * @param field the name of the "blob" field in the Document
     * @param fileName the name that will be given to the uploaded file
     *
     * @return a {@link CreateBlobUploadResponse}, which contains the upload_id that is needed for upload the file.
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public CreateBlobUploadResponse initUpload(String documentId, String field, String fileName)
            throws IOException, ChinoApiException
    {
        CreateBlobUploadRequest createBlobUploadRequest=new CreateBlobUploadRequest(documentId, field, fileName);
        JsonNode data = postResource("/blobs", createBlobUploadRequest);
        if(data!=null)
            return mapper.convertValue(data, CreateBlobUploadResponse.class);
        return null;
    }

    /**
     * Upload a chunk of data to the server
     *
     * @see #uploadBlob(String, String, String, String)
     *
     * @param uploadId the upload ID returned by {@link #initUpload(String, String, String) initUpload()}
     * @param chunkData a chunk of data in the form of a byte array
     * @param offset the offset of the chunk relative to the beginning of the file
     * @param length the length of the chunk
     *
     * @return A {@link CreateBlobUploadResponse}, which contains the status of the operation
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public CreateBlobUploadResponse uploadChunk(String uploadId, byte[] chunkData, int offset, int length)
            throws IOException, ChinoApiException
    {
        JsonNode data = putResource("/blobs/"+uploadId, chunkData, offset, length);
        if(data!=null)
            return mapper.convertValue(data, CreateBlobUploadResponse.class);
        return null;
    }

    /**
     * Confirm and finalize the upload on Chino.io.
     * Make sure that <b>every chunk</b> has been successfully uploaded with
     * {@link #uploadChunk(String, byte[], int, int) uploadChunk()} before calling this method.
     *
     * @see #uploadBlob(String, String, String, String)
     *
     * @param uploadId the upload ID returned by {@link #initUpload(String, String, String) initUpload}
     *
     * @return A {@link CommitBlobUploadResponse}, which contains the status of the operation
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public CommitBlobUploadResponse commitUpload(String uploadId) throws IOException, ChinoApiException {
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
     * @param force if true, the resource cannot be restored. Otherwise, it will only become inactive.
     *              THIS FLAG IS IGNORED.
     *
     * @return a String with the result of the operation
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
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
