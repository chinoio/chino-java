package io.chino.examples.collections;

import io.chino.api.collection.Collection;
import io.chino.api.common.ChinoApiException;
import io.chino.api.document.Document;
import io.chino.api.document.GetDocumentsResponse;
import io.chino.api.repository.Repository;
import io.chino.api.schema.Schema;
import io.chino.client.ChinoAPI;
import io.chino.examples.schemas.SchemaStructureSample;
import io.chino.examples.util.Constants;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class CollectionSamples {

    ChinoAPI chino;
    String COLLECTION_ID = "";
    String REPOSITORY_ID = "";
    String SCHEMA_ID = "";
    String DOCUMENT_ID = "";

    public void testCollections() throws IOException, ChinoApiException {
        //You must first initialize your ChinoAPI variable with your customerId and your customerKey
        chino = new ChinoAPI(Constants.HOST, Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY);

        Collection collection = chino.collections.create("test_collection");
        COLLECTION_ID = collection.getCollectionId();

        Repository repository = chino.repositories.create("test_repository");
        REPOSITORY_ID = repository.getRepositoryId();

        Schema schema = chino.schemas.create(REPOSITORY_ID, "sample_description", SchemaStructureSample.class);
        SCHEMA_ID = schema.getSchemaId();

        HashMap<String, Object> content = new HashMap<String, Object>();
        content.put("test_string", "test_string_value");
        content.put("test_integer", 123);
        content.put("test_boolean", true);
        content.put("test_date", "1994-02-03");

        Document document = chino.documents.create(SCHEMA_ID, content);
        DOCUMENT_ID = document.getDocumentId();

        //We try to add a Document to the Collection
        System.out.println(chino.collections.addDocument(COLLECTION_ID, DOCUMENT_ID));

        //We create a new Document
        content = new HashMap<String, Object>();
        content.put("test_string", "new_test_string_value");
        content.put("test_integer", 1234);
        content.put("test_boolean", false);
        content.put("test_date", "1994-02-04");
        document = chino.documents.create(SCHEMA_ID, content);
        DOCUMENT_ID = document.getDocumentId();

        System.out.println(chino.collections.addDocument(COLLECTION_ID, DOCUMENT_ID));

        //And now we try to read the list of Documents in the Collection
        GetDocumentsResponse documents = chino.collections.listDocuments(COLLECTION_ID, 0);
        List<Document> documentList = documents.getDocuments();
        for(Document documentObject : documentList){
            System.out.println(documentObject.toString());
            System.out.println("Content of the document: "+chino.documents.read(documentObject.getDocumentId()).getContent());
        }

        System.out.println("");
        //Let's see the content of the response when we read a Collection
        System.out.println(chino.collections.read(COLLECTION_ID));

        //Now try to update the name of the Collection
        System.out.println(chino.collections.update(COLLECTION_ID, "test_collection_changed_name"));

        System.out.println("");

        //Let's try to remove the last Document and then read the list of Documents in the Collection again
        System.out.println(chino.collections.removeDocument(COLLECTION_ID, DOCUMENT_ID));

        documents = chino.collections.listDocuments(COLLECTION_ID, 0);
        documentList = documents.getDocuments();
        for(Document documentObject : documentList){
            System.out.println(documentObject.toString());
            System.out.println("Content of the document: "+chino.documents.read(documentObject.getDocumentId()).getContent());
        }
    }

}
