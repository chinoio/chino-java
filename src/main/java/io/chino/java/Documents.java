package io.chino.java;

import com.fasterxml.jackson.databind.JsonNode;
import io.chino.api.common.ChinoApiConstants;
import io.chino.api.common.ChinoApiException;
import io.chino.api.document.CreateDocumentRequest;
import io.chino.api.document.Document;
import io.chino.api.document.GetDocumentResponse;
import io.chino.api.document.GetDocumentsResponse;
import okhttp3.OkHttpClient;
import java.io.IOException;
import java.util.HashMap;

public class Documents extends ChinoBaseAPI {

    public Documents(String hostUrl, OkHttpClient clientInitialized){
        super(hostUrl, clientInitialized);
    }

    /**
     * Returns a list of Documents
     * @param schemaId the id of the Schema
     * @param offset the offset from which it retrieves the Documents
     * @param limit number of results (max {@link io.chino.api.common.ChinoApiConstants#QUERY_DEFAULT_LIMIT})
     * @return GetDocumentsResponse Object which contains the list of Documents
     * @throws IOException
     * @throws ChinoApiException
     */
    public GetDocumentsResponse list(String schemaId, int offset, int limit) throws IOException, ChinoApiException {
        JsonNode data = getResource("/schemas/" + schemaId + "/documents", offset, limit);
        if(data!=null)
            return mapper.convertValue(data, GetDocumentsResponse.class);
        return null;
    }

    /**
     * Returns a list of Documents
     * @param schemaId the id of the Schema
     * @return GetDocumentsResponse Object which contains the list of Documents
     * @throws IOException
     * @throws ChinoApiException
     */
    public GetDocumentsResponse list(String schemaId) throws IOException, ChinoApiException {
        JsonNode data = getResource("/schemas/" + schemaId + "/documents", 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return mapper.convertValue(data, GetDocumentsResponse.class);
        return null;
    }

    /**
     * Returns a list of Documents
     * @param schemaId the id of the Schema
     * @param offset the offset from which it retrieves the Documents
     * @param limit number of results (max {@link io.chino.api.common.ChinoApiConstants#QUERY_DEFAULT_LIMIT})
     * @param fullDocument if true it returns a list of Documents with full content
     * @return GetDocumentsResponse Object which contains the list of Documents
     * @throws IOException
     * @throws ChinoApiException
     */
    public GetDocumentsResponse list(String schemaId, int offset, int limit, boolean fullDocument) throws IOException, ChinoApiException {
        JsonNode data;
        if(fullDocument) {
            data = getResource("/schemas/" + schemaId + "/documents?full_document=true");
        } else {
            data = getResource("/schemas/" + schemaId + "/documents", offset, limit);
        }
        if(data!=null)
            return mapper.convertValue(data, GetDocumentsResponse.class);
        return null;
    }

    /**
     * Returns a list of Documents
     * @param schemaId the id of the Schema
     * @param fullDocument if true it returns a list of Documents with full content
     * @return GetDocumentsResponse Object which contains the list of Documents
     * @throws IOException
     * @throws ChinoApiException
     */
    public GetDocumentsResponse list(String schemaId, boolean fullDocument) throws IOException, ChinoApiException {
        JsonNode data;
        if(fullDocument) {
            data = getResource("/schemas/" + schemaId + "/documents?full_document=true");
        } else {
            data = getResource("/schemas/" + schemaId + "/documents", 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        }
        if(data!=null)
            return mapper.convertValue(data, GetDocumentsResponse.class);
        return null;
    }

    /**
     * It retrieves a Document
     * @param documentId the id of the Document
     * @return Document Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public Document read(String documentId) throws IOException, ChinoApiException{
        JsonNode data = getResource("/documents/"+documentId, 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return mapper.convertValue(data, GetDocumentResponse.class).getDocument();

        return null;
    }

    /**
     * It reads a Document and converts it to an Object of the class "myClass"
     * @param documentId the id of the Document
     * @param myClass the Class that represents the structure of the Document
     * @return Object of the Class passed as argument
     * @throws IOException
     * @throws ChinoApiException
     */
    public Object read(String documentId, Class myClass) throws IOException, ChinoApiException{
        JsonNode data = getResource("/documents/"+documentId, 0 , ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null) {
            Document document = mapper.convertValue(data, GetDocumentResponse.class).getDocument();
            return mapper.convertValue(document.getContent(), myClass);
        }
        return null;
    }

    /**
     * It creates a new Document
     * @param schemaId the id of the Schema
     * @param content an HashMap of the content
     * @return Document Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public Document create(String schemaId, HashMap content) throws IOException, ChinoApiException {
        CreateDocumentRequest createDocumentRequest = new CreateDocumentRequest();
        createDocumentRequest.setContent(content);
        JsonNode data = postResource("/schemas/"+schemaId+"/documents", createDocumentRequest);
        if(data!=null)
            return mapper.convertValue(data, GetDocumentResponse.class).getDocument();

        return null;
    }

    /**
     * It creates a new Document
     * @param schemaId the id of the Schema
     * @param content a String that represents a json of the content
     * @return Document Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public Document create(String schemaId, String content) throws IOException, ChinoApiException {
        CreateDocumentRequest createDocumentRequest = new CreateDocumentRequest();
        createDocumentRequest.setContent(fromStringToHashMap(content));
        JsonNode data = postResource("/schemas/"+schemaId+"/documents", createDocumentRequest);
        if(data!=null)
            return mapper.convertValue(data, GetDocumentResponse.class).getDocument();

        return null;
    }

    /**
     * It updates a Document
     * @param documentId the id of the Document
     * @param content an HashMap of the content
     * @return Document Object updated
     * @throws IOException
     * @throws ChinoApiException
     */
    public Document update(String documentId, HashMap content) throws IOException, ChinoApiException {
        CreateDocumentRequest createDocumentRequest = new CreateDocumentRequest();
        createDocumentRequest.setContent(content);
        JsonNode data = putResource("/documents/"+documentId, createDocumentRequest);
        if(data!=null)
            return mapper.convertValue(data, GetDocumentResponse.class).getDocument();

        return null;
    }

    /**
     * It updates a Document
     * @param documentId the id of the Document
     * @param content a String that represents a json of the content
     * @return Document Object updated
     * @throws IOException
     * @throws ChinoApiException
     */
    public Document update(String documentId, String content) throws IOException, ChinoApiException {
        CreateDocumentRequest createDocumentRequest = new CreateDocumentRequest();
        createDocumentRequest.setContent(fromStringToHashMap(content));
        JsonNode data = putResource("/documents/"+documentId, createDocumentRequest);
        if(data!=null)
            return mapper.convertValue(data, GetDocumentResponse.class).getDocument();

        return null;
    }

    /**
     * It deletes a Document
     * @param documentId the id of the Document
     * @param force if true, the resource cannot be restored
     * @return a String with the result of the operation
     * @throws IOException
     * @throws ChinoApiException
     */
    public String delete(String documentId, boolean force) throws IOException, ChinoApiException {
        return deleteResource("/documents/"+documentId, force);
    }
}
