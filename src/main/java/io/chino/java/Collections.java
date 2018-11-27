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

/**
 * Manage {@link Collection Collections} of {@link io.chino.api.document.Document Documents} on Chino.io
 */
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
     * List all the {@link Collection Collections} that currently exist in Chino.io
     *
     * @param offset the list offset (how many are skipped)
     * @param limit maximum number of results (must be below
     *              {@link io.chino.api.common.ChinoApiConstants#QUERY_DEFAULT_LIMIT ChinoApiConstants.QUERY_DEFAULT_LIMIT})
     *
     * @return A {@link GetCollectionsResponse} which wraps the {@link java.util.List} of {@link Collection Collections}
     *
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
     * List all the {@link Collection Collections} that currently exist in Chino.io
     *
     * @return A {@link GetCollectionsResponse} which wraps the {@link java.util.List} of {@link Collection Collections}
     *
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
     * Read a single Collection based on its ID
     *
     * @param collectionId the id of the Collection to read
     *
     * @return the requested instance of {@link Collection}
     *
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
     * Create a new {@link Collection} on Chino.io
     *
     * @param name the name of the new Collection. Must be 36 characters or less.
     *
     * @return the new {@link Collection}
     *
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
     * Update the name of a {@link Collection}
     *
     * @param collectionId the id of the {@link Collection} to update
     * @param name the new name of the {@link Collection}
     *
     * @return a {@link GetCollectionResponse} which wraps the updated Collection.
     *
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
     * Get the {@link io.chino.api.document.Document Documents} in a Collection
     *
     * @param collectionId the id of the Collection
     * @param offset the list offset (how many are skipped)
     * @param limit maximum number of results (must be below {@link io.chino.api.common.ChinoApiConstants#QUERY_DEFAULT_LIMIT ChinoApiConstants.QUERY_DEFAULT_LIMIT})
     *
     * @return a {@link GetDocumentsResponse} which wraps the {@link java.util.List} of
     *          {@link io.chino.api.document.Document Documents}
     *
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
     * Get the {@link io.chino.api.document.Document Documents} in a Collection
     *
     * @param collectionId the id of the Collection
     *
     * @return a {@link GetDocumentsResponse} which wraps the {@link java.util.List} of
     *          {@link io.chino.api.document.Document Documents}
     *
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
     * Add a {@link io.chino.api.document.Document Document} to a {@link Collection}
     *
     * @param collectionId the id of the Collection
     * @param documentId the id of the Document
     *
     * @return a String with the result of the operation
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public String addDocument(String collectionId, String documentId)throws IOException, ChinoApiException {
        checkNotNull(collectionId, "collection_id");
        checkNotNull(documentId, "document_id");
        postResource("/collections/"+collectionId+"/documents/"+documentId, null);
        return SUCCESS_MSG;
    }

    /**
     * Remove a {@link io.chino.api.document.Document Document} from a {@link Collection}
     *
     * @param collectionId the id of the Collection
     * @param documentId the id of the Document
     *
     * @return a String with the result of the operation
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public String removeDocument(String collectionId, String documentId)throws IOException, ChinoApiException {
        checkNotNull(collectionId, "collection_id");
        checkNotNull(documentId, "document_id");
        deleteResource("/collections/"+collectionId+"/documents/"+documentId, false);
        return SUCCESS_MSG;
    }

    /**
     * Delete a Collection
     *
     * @param collectionId the id of the Collection to delete
     * @param force if set to {@code true}, the {@link Collection} cannot be restored.
     *              Otherwise it will only get deactivated.
     *
     * @return a String with the result of the operation
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public String delete(String collectionId, boolean force) throws IOException, ChinoApiException {
        checkNotNull(collectionId, "collection_id");
        return deleteResource("/collections/"+collectionId, force);
    }

}
