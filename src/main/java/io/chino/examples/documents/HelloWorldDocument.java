package io.chino.examples.documents;

import io.chino.api.common.ChinoApiException;
import io.chino.api.common.Field;
import io.chino.api.document.Document;
import io.chino.api.document.GetDocumentsResponse;
import io.chino.api.repository.Repository;
import io.chino.api.schema.Schema;
import io.chino.api.schema.SchemaStructure;
import io.chino.java.ChinoAPI;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HelloWorldDocument {

    public final static String HOST = "https://api.test.chino.io/v1";
    public final static String CUSTOMER_ID = "<YOUR_CUSTOMER_ID>";
    public final static String CUSTOMER_KEY = "<YOUR_CUSTOMER_KEY>";

    public static String REPOSITORY_ID = "";
    public static String SCHEMA_ID = "";
    public static String DOCUMENT_ID1 = "";
    public static String DOCUMENT_ID2 = "";
    public static String PATIENT_ID = "mariorossi@gmail.com";
    public static String DOCTOR_ID = "luigiverdi@gmail.com";

    public static void main(String args[]) throws IOException, ChinoApiException, ParseException, InterruptedException {

        /*
            You must first initialize your ChinoAPI variable with your customerId and your customerKey
            You need this variable in order to make all the calls to the server
            Using chino."name of the class" you can access all the methods of the classes like Repositories, Schemas, ecc...
         */
        ChinoAPI chino = new ChinoAPI(HOST, CUSTOMER_ID, CUSTOMER_KEY);

        /*
            Repositories are containers for Schemas and Schemas are container for Documents, hence you
            need to create first a Repository and a Schema in order to create a Document
         */

        //To create a Repository you need only a description, see the example below
        Repository repository = chino.repositories.create("first tutorial");

        //Let's store the repository id that will be used to create a Schema
        REPOSITORY_ID = repository.getRepositoryId();

        /*
            To create a Schema you need the id of the repository in which you want to create it, then a description and the structure
            of the Schema. The easiest way to do it is shown below, using a SchemaStructure and setting its list of Fields
        */
        SchemaStructure structure = new SchemaStructure();

        List<Field> fields = new ArrayList<Field>();

        fields.add(new Field("patient_id", "string", true));
        fields.add(new Field("doctor_id", "string", true));
        fields.add(new Field("visit_type", "string", true));
        fields.add(new Field("visit", "text"));
        fields.add(new Field("date", "datetime", true));

        structure.setFields(fields);

        Schema schema = chino.schemas.create(REPOSITORY_ID, "base_visit", structure);

        //Let's store the schema id that will be used to create a Document
        SCHEMA_ID = schema.getSchemaId();

        /*
            To create a Document you need to populate all the fields of the Schema in which you want to create it. You have to
            use a HashMap in which you store a String (which is the name of the variable) and an Object (which is the value of the
            variable). See the example below
         */
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = new Date();

        HashMap<String, Object> content = new HashMap<String, Object>();
        //Let's use emails as identification in this example
        content.put("doctor_id", DOCTOR_ID);
        content.put("patient_id", PATIENT_ID);
        content.put("visit", "test_visit");
        content.put("visit_type", "test_visit_type");
        content.put("date", dateFormat.format(date));

        Document document = chino.documents.create(SCHEMA_ID, content);

        //We have created a Document and we store the id to access it later
        DOCUMENT_ID1 = document.getDocumentId();

        //Let's create another document to see effectively the result of the list() and the search()

        date = new Date();
        content = new HashMap<String, Object>();
        content.put("doctor_id", DOCTOR_ID);
        content.put("patient_id", PATIENT_ID);
        content.put("visit", "test_visit1");
        content.put("visit_type", "test_visit_type1");
        content.put("date", dateFormat.format(date));

        document = chino.documents.create(SCHEMA_ID, content);

        //We have created a Document and we store the id to access it later
        DOCUMENT_ID2 = document.getDocumentId();

        GetDocumentsResponse documentsResponse = chino.documents.list(SCHEMA_ID, true);
        List<Document> documentList = documentsResponse.getDocuments();

        HashMap<String, HashMap<String, Object>> visits = new HashMap<String, HashMap<String, Object>>();

        for(Document d : documentList){
            visits.put(d.getDocumentId(), d.getContentAsHashMap());
            d.getContentAsHashMap();
        }

        //Let's see what's inside visits variable
        System.out.println(visits.toString());

        //It is mandatory because the server needs 2 seconds to index the fields on the schema. If the documents are searched
        //before the fields are indexed, the result would be null
        Thread.sleep(5000);

        List<Document> documents = chino.search.where("doctor_id").eq(DOCTOR_ID).sortAscBy("date").resultType("FULL_CONTENT").searchDocuments(SCHEMA_ID).getDocuments();

        System.out.println(documents);

        documents = chino.search.where("visit_type").eq("test_visit_type1").sortAscBy("date").resultType("FULL_CONTENT").searchDocuments(SCHEMA_ID).getDocuments();

        System.out.println(documents);

        //Finally we delete everything we created
        System.out.println(chino.documents.delete(DOCUMENT_ID1, true));
        System.out.println(chino.documents.delete(DOCUMENT_ID2, true));
        System.out.println(chino.schemas.delete(SCHEMA_ID, true));
        System.out.println(chino.repositories.delete(REPOSITORY_ID, true));
    }
}
