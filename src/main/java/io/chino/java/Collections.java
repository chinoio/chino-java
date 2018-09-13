package io.chino.java;

import com.fasterxml.jackson.databind.JsonNode;
import io.chino.api.collection.Collection;
import io.chino.api.collection.CreateCollectionRequest;
import io.chino.api.collection.GetCollectionResponse;
import io.chino.api.collection.GetCollectionsResponse;
import io.chino.api.common.ChinoApiConstants;
import io.chino.api.common.ChinoApiException;
import io.chino.api.document.GetDocumentsResponse;

import java.io.IOException;

public class Collections extends ChinoBaseAPI {

    /**
     * The default constructor used by all {@link ChinoBaseAPI} subclasses
     *
     * @param baseApiUrl      the base URL of the Chino.io API. For testing, use:<br>
     *                        {@code https://api.test.chino.io/v1/}
     * @param parentApiClient the instance of {@link ChinoAPI} that created this object
     */
    public Collections(String baseApiUrl, ChinoAPI parentApiClient) {
        super(baseApiUrl, parentApiClient);
    }

    /**
     * Returns a list of Collections
     * @param offset the offset from which it retrieves the Collections
     * @param limit number of results (max {@link io.chino.api.common.ChinoApiConstants#QUERY_DEFAULT_LIMIT})
     * @return GetCollectionsResponse Object which contains the list of Collections
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public GetCollectionsResponse list(int offset, int limit) throws IOException, ChinoApiException {
        JsonNode data = getResource("/collections", offset, limit);
        if(data!=null)
            return mapper.convertValue(data, GetCollectionsResponse.class);
        return null;
    }

    /**
     * Returns a list of Collections
     * @return GetCollectionsResponse Object which contains the list of Collections
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public GetCollectionsResponse list() throws IOException, ChinoApiException {
        JsonNode data = getResource("/collections", 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return mapper.convertValue(data, GetCollectionsResponse.class);
        return null;
    }

    /**
     * It retrieves a single Collection
     * @param collectionId the id of the Collection to read
     * @return Collection Object
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public Collection read(String collectionId) throws IOException, ChinoApiException{
        checkNotNull(collectionId, "collection_id");
        JsonNode data = getResource("/collections/"+collectionId, 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return mapper.convertValue(data, GetCollectionResponse.class).getCollection();

        return null;
    }

    /**
     * It creates a new Collection
     * @param name the name of the Collection
     * @return Collection Object
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public Collection create(String name) throws IOException, ChinoApiException {
        CreateCollectionRequest collectionRequest = new CreateCollectionRequest(name);
        JsonNode data = postResource("/collections", collectionRequest);
        if(data!=null)
            return mapper.convertValue(data, GetCollectionResponse.class).getCollection();

        return null;
    }

    /**
     * It updates a Collection
     * @param collectionId the id of the Collection to update
     * @param name the name of the new Collection
     * @return GetCollection Object which contains the Collection object
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public GetCollectionResponse update(String collectionId, String name) throws IOException, ChinoApiException {
        checkNotNull(collectionId, "collection_id");
        CreateCollectionRequest collectionRequest = new CreateCollectionRequest(name);
        JsonNode data = putResource("/collections/"+collectionId, collectionRequest);
        if(data!=null)
            return mapper.convertValue(data, GetCollectionResponse.class);
        return null;
    }

    /**
     * Returns a list of Documents in a Collection
     * @param collectionId the id of the Collection
     * @param offset the offset from which it retrieves the Collections
     * @param limit number of results (max {@link io.chino.api.common.ChinoApiConstants#QUERY_DEFAULT_LIMIT})
     * @return GetDocumentsResponse Object which contains the list of Documents
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public GetDocumentsResponse listDocuments(String collectionId, int offset, int limit)throws IOException, ChinoApiException {
        checkNotNull(collectionId, "collection_id");
        JsonNode data = getResource("/collections/"+collectionId+"/documents", offset, limit);
        if(data!=null)
            return mapper.convertValue(data, GetDocumentsResponse.class);
        return null;
    }

    /**
     * Returns a list of Documents in a Collection
     * @param collectionId the id of the Collection
     * @return GetDocumentsResponse Object which contains the list of Documents
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public GetDocumentsResponse listDocuments(String collectionId)throws IOException, ChinoApiException {
        checkNotNull(collectionId, "collection_id");
        JsonNode data = getResource("/collections/"+collectionId+"/documents", 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return mapper.convertValue(data, GetDocumentsResponse.class);
        return null;
    }

    /**
     * It adds a new Document to a Collection
     * @param collectionId the id of the Collection
     * @param documentId the id of the Document
     * @return a String with the result of the operation
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public String addDocument(String collectionId, String documentId)throws IOException, ChinoApiException {
        checkNotNull(collectionId, "collection_id");
        checkNotNull(documentId, "document_id");
        postResource("/collections/"+collectionId+"/documents/"+documentId, null);
        return "success";
    }

    /**
     * It removes a Document from a Collection
     * @param collectionId the id of the Collection
     * @param documentId the id of the Document
     * @return a String with the result of the operation
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public String removeDocument(String collectionId, String documentId)throws IOException, ChinoApiException {
        checkNotNull(collectionId, "collection_id");
        checkNotNull(documentId, "document_id");
        deleteResource("/collections/"+collectionId+"/documents/"+documentId, false);
        return "success";
    }

    /**
     * It deletes a Collection
     * @param collectionId the id of the Collection to delete
     * @param force if true, the resource cannot be restored
     * @return a String with the result of the operation
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public String delete(String collectionId, boolean force) throws IOException, ChinoApiException {
        checkNotNull(collectionId, "collection_id");
        return deleteResource("/collections/"+collectionId, force);
    }

}
