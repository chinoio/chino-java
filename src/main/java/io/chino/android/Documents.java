package io.chino.android;

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
     * Used to get a list of Documents
     * @param schemaId the id of the Schema
     * @param offset the offset
     * @return a GetDocumentsResponse Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public GetDocumentsResponse list(String schemaId, int offset) throws IOException, ChinoApiException {
        JsonNode data = getResource("/schemas/" + schemaId + "/documents", offset, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return mapper.convertValue(data, GetDocumentsResponse.class);
        return null;
    }


    public GetDocumentsResponse list(String schemaId, int offset, boolean fullDocument) throws IOException, ChinoApiException {
        JsonNode data;
        if(fullDocument) {
            data = getResource("/schemas/" + schemaId + "/documents?full_document=true");
        } else {
            data = getResource("/schemas/" + schemaId + "/documents", offset, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        }
        if(data!=null)
            return mapper.convertValue(data, GetDocumentsResponse.class);
        return null;
    }

    /**
     * Used to get a Document
     * @param documentId the id of the Document
     * @return a Document Object
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
     * Used to read a Document
     * @param documentId the id of the Document
     * @param myClass the Class that represents the structure of the Document
     * @return an Object of the Class passed
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
     * Used to create a new Document
     * @param schemaId the id of the Schema
     * @param content an HashMap of the content
     * @return a Document Object
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
     * Used to create a new Document
     * @param schemaId the id of the Schema
     * @param content a String that represents a json of the content
     * @return a Document Object
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
     * Used to update a Document
     * @param documentId the id of the Document
     * @param content an HashMap of the content
     * @return a Document Object
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
     * Used to update a Document
     * @param documentId the id of the Document
     * @param content a String that represents a json of the content
     * @return a Document Object
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
     * Used to delete a Document
     * @param documentId the id of the Document
     * @param force the boolean force
     * @return a String that represents the result of the operation
     * @throws IOException
     * @throws ChinoApiException
     */
    public String delete(String documentId, boolean force) throws IOException, ChinoApiException {
        return deleteResource("/documents/"+documentId, force);
    }
}
