package io.chino.examples.documents;

import io.chino.api.common.ChinoApiException;
import io.chino.api.common.Field;
import io.chino.api.document.Document;
import io.chino.api.repository.Repository;
import io.chino.api.schema.Schema;
import io.chino.api.schema.SchemaStructure;
import io.chino.android.ChinoAPI;
import io.chino.examples.util.Constants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HelloWorldDocument {

    static ChinoAPI chino;
    static String REPOSITORY_ID = "";
    static String SCHEMA_ID = "";
    static String DOCUMENT_ID = "";

    public static void main(String args[]) throws IOException, ChinoApiException{

        /*
            You must first initialize your ChinoAPI variable with your customerId and your customerKey
            You need this variable in order to make all the calls to the server
            Using chino."name of the class" you can access all the methods of the classes like Repositories, Schemas, ecc...
         */
        chino = new ChinoAPI(Constants.HOST, Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY);

        /*
            Repositories are containers for Schemas and Schemas are container for Documents, hence you
            need to create first a Repository and a Schema in order to create a Document
         */

        //To create a Repository you need only a description, see the example below
        Repository repository = chino.repositories.create("test_repository");

        //Let's store the repository id that will be used to create a Schema
        REPOSITORY_ID = repository.getRepositoryId();

        /*
            To create a Schema you need the id of the repository in which you want to create it, then a description and the structure
            of the Schema. The easiest way to do it is shown below, using a SchemaStructure and setting its list of Fields
        */
        SchemaStructure schemaStructure = new SchemaStructure();
        List<Field> fields = new ArrayList<Field>();
        //To create a new Field you have to set the name of the variable and its type
        fields.add(new Field("test_string", "string"));
        fields.add(new Field("test_integer", "integer"));
        fields.add(new Field("test_boolean", "boolean"));
        fields.add(new Field("test_date", "date"));
        schemaStructure.setFields(fields);

        Schema schema = chino.schemas.create(REPOSITORY_ID, "sample_description", schemaStructure);

        //Let's store the schema id that will be used to create a Document
        SCHEMA_ID = schema.getSchemaId();

        /*
            To create a Document you need to populate all the fields of the Schema in which you want to create it. You have to
            use a HashMap in which you store a String (which is the name of the variable) and an Object (which is the value of the
            variable). See the example below
         */
        HashMap<String, Object> content = new HashMap<String, Object>();
        content.put("test_string", "hello world");
        content.put("test_integer", 123);
        content.put("test_boolean", true);
        content.put("test_date", "1994-02-03");

        Document document = chino.documents.create(SCHEMA_ID, content);

        //We have created a Document and we store the id to access it later
        DOCUMENT_ID = document.getDocumentId();

        //Let's try to read and print the Document created
        System.out.println(chino.documents.read(DOCUMENT_ID));

        //Finally we delete everything we created
        System.out.println(chino.documents.delete(DOCUMENT_ID, true));
        System.out.println(chino.schemas.delete(SCHEMA_ID, true));
        System.out.println(chino.repositories.delete(REPOSITORY_ID, true));
    }
}
