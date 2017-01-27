package io.chino.examples.documents;

import io.chino.api.common.ChinoApiException;
import io.chino.api.document.Document;
import io.chino.api.document.GetDocumentsResponse;
import io.chino.api.repository.Repository;
import io.chino.api.schema.Schema;
import io.chino.java.ChinoAPI;
import io.chino.examples.schemas.SchemaStructureSample;
import io.chino.examples.util.Constants;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class DocumentSamples {

    ChinoAPI chino;
    String REPOSITORY_ID = "";
    String SCHEMA_ID = "";
    String DOCUMENT_ID = "";

    public void testDocuments() throws IOException, ChinoApiException {

        //We initialize the ChinoAPI variable with the customerId and customerKey
        chino = new ChinoAPI(Constants.HOST, Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY);

        //We need a Repository and a Schema to create the Document
        Repository repository = chino.repositories.create("test_repository");
        REPOSITORY_ID = repository.getRepositoryId();

        Schema schema = chino.schemas.create(REPOSITORY_ID, "sample_description", SchemaStructureSample.class);
        SCHEMA_ID = schema.getSchemaId();

        HashMap<String, Object> content = new HashMap<String, Object>();
        content.put("test_string", "test_string_value");
        content.put("test_integer", 123);
        content.put("test_boolean", true);
        content.put("test_date", "1994-02-03");

        //This is a method for creating a Document passing a String that represents a json of the content
        String contentStringJson = "\"test_integer\":456, \"test_string\":\"string_value_json_method\", \"test_boolean\":true, \"test_date\":\"1992-01-03\"";
        Document document = chino.documents.create(SCHEMA_ID, contentStringJson);
        System.out.println(chino.documents.read(document.getDocumentId()));

        document = chino.documents.create(SCHEMA_ID, content);
        DOCUMENT_ID = document.getDocumentId();
        System.out.println(chino.documents.read(DOCUMENT_ID));

        /*
            In this case we have created a Class "MyDocument" that extends the Class "SchemaStructureSample". In this way we have
            the same fields and we can easily access the values
        */

        MyDocument myDocument = (MyDocument)chino.documents.read(DOCUMENT_ID, MyDocument.class);
        System.out.println("");
        System.out.println("myDocument.test_integer: "+myDocument.test_integer);
        System.out.println("myDocument.test_string: "+myDocument.test_string);
        System.out.println("myDocument.test_boolean: "+myDocument.test_boolean);
        System.out.println("myDocument.test_date: "+myDocument.test_date);
        System.out.println("");

        //Let's try to update the document
        System.out.println("Document Updated:");
        content.put("test_string", "test_string_value_updated");
        content.put("test_integer", 12345);
        content.put("test_boolean", false);
        chino.documents.update(DOCUMENT_ID, content);
        System.out.println(chino.documents.read(DOCUMENT_ID));

        //Now try to add a new Document to the Schema and read all the Documents of the Schema
        content.put("test_string", "second_document_test_string_value_updated");
        content.put("test_integer", 32);
        content.put("test_boolean", false);
        content.put("test_date", "1980-03-04");
        document = chino.documents.create(SCHEMA_ID, content);
        DOCUMENT_ID = document.getDocumentId();

        System.out.println("");
        GetDocumentsResponse documentsResponse = chino.documents.list(SCHEMA_ID, true);
        List<Document> documentList = documentsResponse.getDocuments();

        HashMap<String, HashMap<String, Object>> allContents = new HashMap<String, HashMap<String, Object>>();

        for(Document d : documentList){
            allContents.put(d.getDocumentId(), d.getContentAsHashMap());
            System.out.println(d);
        }

        System.out.println(allContents.toString());

        //Now we try to delete the last document and then read the list again
        System.out.println(chino.documents.delete(DOCUMENT_ID, true));

        System.out.println("");
        documentsResponse = chino.documents.list(SCHEMA_ID);
        documentList = documentsResponse.getDocuments();
        for(Document documentObject : documentList){
            System.out.println(documentObject);
        }

        //Finally we delete everything we created
        for(Document documentObject : documentList){
            System.out.println(chino.documents.delete(documentObject.getDocumentId(), true));
        }
        System.out.println(chino.schemas.delete(SCHEMA_ID, true));
        System.out.println(chino.repositories.delete(REPOSITORY_ID, true));
    }
}
