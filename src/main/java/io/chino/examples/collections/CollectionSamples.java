package io.chino.examples.collections;

import io.chino.api.collection.Collection;
import io.chino.api.common.ChinoApiException;
import io.chino.api.document.Document;
import io.chino.api.document.GetDocumentsResponse;
import io.chino.api.repository.Repository;
import io.chino.api.schema.Schema;
import io.chino.java.ChinoAPI;
import io.chino.examples.schemas.SchemaStructureSample;
import io.chino.test.util.Constants;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class CollectionSamples {

    ChinoAPI chino;
    String COLLECTION_ID = "";
    String REPOSITORY_ID = "";
    String SCHEMA_ID = "";
    String DOCUMENT_ID_1 = "";
    String DOCUMENT_ID_2 = "";

    public void testCollections() throws IOException, ChinoApiException {
        //We initialize the ChinoAPI variable with the customerId and customerKey
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
        DOCUMENT_ID_1 = document.getDocumentId();

        //We try to add a Document to the Collection
        System.out.println(chino.collections.addDocument(COLLECTION_ID, DOCUMENT_ID_1));

        //We create a new Document
        content = new HashMap<String, Object>();
        content.put("test_string", "new_test_string_value");
        content.put("test_integer", 1234);
        content.put("test_boolean", false);
        content.put("test_date", "1994-02-04");
        document = chino.documents.create(SCHEMA_ID, content);
        DOCUMENT_ID_2 = document.getDocumentId();

        System.out.println(chino.collections.addDocument(COLLECTION_ID, DOCUMENT_ID_2));

        //And now we try to read the list of Documents in the Collection
        GetDocumentsResponse documents = chino.collections.listDocuments(COLLECTION_ID);
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
        System.out.println(chino.collections.removeDocument(COLLECTION_ID, DOCUMENT_ID_2));

        documents = chino.collections.listDocuments(COLLECTION_ID);
        documentList = documents.getDocuments();
        for(Document documentObject : documentList){
            System.out.println(documentObject.toString());
            System.out.println("Content of the document: "+chino.documents.read(documentObject.getDocumentId()).getContent());
        }

        //Finally we delete everything we created
        System.out.println(chino.documents.delete(DOCUMENT_ID_1, true));
        System.out.println(chino.documents.delete(DOCUMENT_ID_2, true));
        System.out.println(chino.schemas.delete(SCHEMA_ID, true));
        System.out.println(chino.repositories.delete(REPOSITORY_ID, true));
        System.out.println(chino.collections.delete(COLLECTION_ID, true));
    }

}
