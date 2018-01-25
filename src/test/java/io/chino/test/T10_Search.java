package io.chino.test;

import io.chino.api.common.ChinoApiException;
import io.chino.api.common.Field;
import io.chino.api.document.Document;
import io.chino.api.document.GetDocumentsResponse;
import io.chino.api.repository.Repository;
import io.chino.api.schema.Schema;
import io.chino.api.schema.SchemaStructure;
import io.chino.api.search.FilterOption;
import io.chino.api.search.SearchRequest;
import io.chino.api.search.SortOption;
import io.chino.api.user.GetUsersResponse;
import io.chino.api.user.User;
import io.chino.api.userschema.UserSchema;
import io.chino.api.userschema.UserSchemaStructure;
import io.chino.examples.search.SchemaStructureSample;
import io.chino.examples.util.DeleteAll;
import io.chino.java.ChinoAPI;
import io.chino.examples.search.SearchSamples;
import io.chino.examples.util.Constants;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class T10_Search {

    ChinoAPI chino;
    String REPOSITORY_ID = "";
    String SCHEMA_ID = "";

    @Before
    public void init(){
        chino = new ChinoAPI(Constants.HOST, Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY);
    }

    @Test
    public void testDocs() throws IOException, ChinoApiException,InterruptedException {
//        SearchSamples searchSamples = new SearchSamples();
//        searchSamples.testSearch();

        DeleteAll deleteAll = new DeleteAll();
        deleteAll.deleteAll(chino);

        //Now we need to add some Documents
        Repository repository = chino.repositories.create("test_repository");
        REPOSITORY_ID = repository.getRepositoryId();

        SchemaStructure structure = new SchemaStructure();

        List<Field> fields = new ArrayList<Field>();

        //The true value indicates that the value is indexed, if omitted the default value is false
        fields.add(new Field("patient_id", "string", true));
        fields.add(new Field("doctor_id", "string", true));
        fields.add(new Field("visit_type", "string", true));
        fields.add(new Field("visit", "text"));
        fields.add(new Field("date", "datetime", true));

        structure.setFields(fields);

        Schema schema = chino.schemas.create(repository.getRepositoryId(), "base_visit", structure);
        SCHEMA_ID = schema.getSchemaId();

        UserSchemaStructure userSchemaStructure = new UserSchemaStructure();

        fields = new ArrayList<Field>();

        fields.add(new Field("name", "string", true));
        fields.add(new Field("last_name", "string", true));
        fields.add(new Field("date_birth", "date", true));
        fields.add(new Field("role", "string"));
        userSchemaStructure.setFields(fields);

        UserSchema userSchema = chino.userSchemas.create("user_schema", userSchemaStructure);

        //It is mandatory because the server needs 2 seconds to index the fields on the schema. If the documents are searched
        //before the fields are indexed, the result would be null
        Thread.sleep(5000);

        HashMap<String, Object> content = new HashMap<String, Object>();
        content.put("name", "doctor_name");
        content.put("last_name", "doctor_last_name");
        content.put("date_birth", "1960-01-01");
        content.put("role", "doctor");

        //Create users
        User doctor = chino.users.create("doctor_username", "doctor_password", content, userSchema.getUserSchemaId());

        content = new HashMap<String, Object>();
        content.put("name", "first_patient_name");
        content.put("last_name", "first_patient_last_name");
        content.put("date_birth", "1960-01-01");
        content.put("role", "patient");

        User first_patient = chino.users.create("first_patient_username", "first_patient_password", content, userSchema.getUserSchemaId());

        content = new HashMap<String, Object>();
        content.put("name", "second_patient_name");
        content.put("last_name", "second_patient_last_name");
        content.put("date_birth", "1960-01-01");
        content.put("role", "patient");

        User second_patient = chino.users.create("second_patient_username", "second_patient_password", content, userSchema.getUserSchemaId());

        //Create documents

        content = new HashMap<String, Object>();
        content.put("patient_id", first_patient.getUserId());
        content.put("doctor_id", doctor.getUserId());
        content.put("visit_type", "routine");
        content.put("visit", "visit_description");
        content.put("date", "2017-01-04T10:25:36");

        Document first_document = chino.documents.create(SCHEMA_ID, content);

        content = new HashMap<String, Object>();
        content.put("patient_id", first_patient.getUserId());
        content.put("doctor_id", doctor.getUserId());
        content.put("visit_type", "routine");
        content.put("visit", "visit_description");
        content.put("date", "2017-01-06T12:03:45");

        Document second_document = chino.documents.create(SCHEMA_ID, content);

        content = new HashMap<String, Object>();
        content.put("patient_id", second_patient.getUserId());
        content.put("doctor_id", doctor.getUserId());
        content.put("visit_type", "insurance_exams");
        content.put("visit", "visit_description");
        content.put("date", "2017-01-06T15:03:34");

        Document third_document = chino.documents.create(SCHEMA_ID, content);

        //It is mandatory because the server needs 2 seconds to index the fields on the schema. If the documents are searched
        //before the fields are indexed, the result would be null
        Thread.sleep(5000);

        //Search all Documents with the id of a specific User

        System.out.println("\n\nSearch all Documents with the id of a specific User\n\n");

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setFilterType("or");
        searchRequest.setResultType("FULL_CONTENT");
        List<SortOption> sortOptionList = new ArrayList<SortOption>();
        sortOptionList.add(new SortOption("date", "asc"));
        searchRequest.setSort(sortOptionList);

        List<FilterOption> filterOptionList = new ArrayList<FilterOption>();
        filterOptionList.add(new FilterOption("patient_id","eq", first_patient.getUserId()));
        searchRequest.setFilter(filterOptionList);

        System.out.println(chino.search.searchDocuments(searchRequest, SCHEMA_ID));

        //Let's try to apply the same search with another method
        //In this case if there is only one filter option the filter type is set to "or"
        GetDocumentsResponse documents = chino.search.where("patient_id")
                .eq(first_patient.getUserId())
                .sortAscBy("date")
                .resultType("FULL_CONTENT")
                .searchDocuments(SCHEMA_ID);
        System.out.println(documents);


        //Search all Documents created after a specific date

        System.out.println("\n\nSearch all Documents created after a specific date\n\n");

        searchRequest = new SearchRequest();
        searchRequest.setFilterType("or");
        searchRequest.setResultType("FULL_CONTENT");
        sortOptionList = new ArrayList<SortOption>();
        sortOptionList.add(new SortOption("date", "asc"));
        searchRequest.setSort(sortOptionList);

        filterOptionList = new ArrayList<FilterOption>();
        filterOptionList.add(new FilterOption("date","gt", "2017-01-05T00:00:00"));
        searchRequest.setFilter(filterOptionList);

        System.out.println(chino.search.searchDocuments(searchRequest, SCHEMA_ID));

        //Let's try to apply the same search with another method
        documents = chino.search.where("date")
                .gt("2017-01-05T00:00:00")
                .sortAscBy("date")
                .resultType("FULL_CONTENT")
                .searchDocuments(SCHEMA_ID);
        System.out.println(documents);


        //Search User with a specific name

        System.out.println("\n\nSearch User with a specific name\n\n");

        searchRequest = new SearchRequest();
        searchRequest.setFilterType("and");
        searchRequest.setResultType("FULL_CONTENT");
        sortOptionList = new ArrayList<SortOption>();
        sortOptionList.add(new SortOption("date_birth", "asc"));
        searchRequest.setSort(sortOptionList);

        filterOptionList = new ArrayList<FilterOption>();
        filterOptionList.add(new FilterOption("name","eq", "first_patient_name"));
        searchRequest.setFilter(filterOptionList);

        System.out.println(chino.search.searchUsers(searchRequest, userSchema.getUserSchemaId()));

        //Let's try to apply the same search with another method
        GetUsersResponse users = chino.search.where("name")
                .eq("first_patient_name")
                .sortAscBy("date_birth")
                .resultType("FULL_CONTENT")
                .searchUsers(userSchema.getUserSchemaId());
        System.out.println(users);


        //Search if User with a specific name exists

        System.out.println("\n\nSearch if User with a specific name exists\n\n");

        searchRequest = new SearchRequest();
        searchRequest.setFilterType("and");
        searchRequest.setResultType("EXISTS");
        sortOptionList = new ArrayList<SortOption>();
        sortOptionList.add(new SortOption("date_birth", "asc"));
        searchRequest.setSort(sortOptionList);

        filterOptionList = new ArrayList<FilterOption>();
        filterOptionList.add(new FilterOption("name","eq", "first_patient_name"));
        searchRequest.setFilter(filterOptionList);

        System.out.println(chino.search.searchUsers(searchRequest, userSchema.getUserSchemaId()));


        //Let's try to apply the same search with another method
        users = chino.search.where("name")
                .eq("first_patient_name")
                .sortAscBy("date_birth")
                .resultType("EXISTS")
                .searchUsers(userSchema.getUserSchemaId());
        System.out.println(users);


        //Search if User with a specific username exists

        System.out.println("\n\nSearch if User with a specific username exists\n\n");

        searchRequest = new SearchRequest();
        searchRequest.setFilterType("and");
        searchRequest.setResultType("USERNAME_EXISTS");
        sortOptionList = new ArrayList<SortOption>();
        sortOptionList.add(new SortOption("date_birth", "asc"));
        searchRequest.setSort(sortOptionList);

        filterOptionList = new ArrayList<FilterOption>();
        filterOptionList.add(new FilterOption("username","eq", "first_patient_username"));
        searchRequest.setFilter(filterOptionList);

        System.out.println(chino.search.searchUsers(searchRequest, userSchema.getUserSchemaId()));


        //Let's try to apply the same search with another method
        users = chino.search.where("username")
                .eq("first_patient_username")
                .sortAscBy("date_birth")
                .resultType("USERNAME_EXISTS")
                .searchUsers(userSchema.getUserSchemaId());
        System.out.println(users);


        //Search for Documents created in a specific day

        System.out.println("\n\nSearch for Documents created in a specific day\n\n");

        searchRequest = new SearchRequest();
        searchRequest.setFilterType("and");
        searchRequest.setResultType("FULL_CONTENT");
        sortOptionList = new ArrayList<SortOption>();
        sortOptionList.add(new SortOption("date", "asc"));
        searchRequest.setSort(sortOptionList);

        filterOptionList = new ArrayList<FilterOption>();
        filterOptionList.add(new FilterOption("date","gt", "2017-01-04T00:00:00"));
        filterOptionList.add(new FilterOption("date","lt", "2017-01-05T00:00:00"));
        searchRequest.setFilter(filterOptionList);

        System.out.println(chino.search.searchDocuments(searchRequest, SCHEMA_ID));

        //Let's try to apply the same search with another method
        documents = chino.search.where("date")
                .gt("2017-01-04T00:00:00")
                .and("date")
                .lt("2017-01-05T00:00:00")
                .sortAscBy("date")
                .resultType("FULL_CONTENT")
                .searchDocuments(SCHEMA_ID);
        System.out.println(documents);


        //Search for Documents created after a specific date or with a visit_type pertaining to a list

        System.out.println("\n\nSearch for Documents created after a specific date or with a visit_type pertaining to a list\n\n");

        searchRequest = new SearchRequest();
        searchRequest.setFilterType("or");
        searchRequest.setResultType("ONLY_ID");
        sortOptionList = new ArrayList<SortOption>();
        sortOptionList.add(new SortOption("date", "asc"));
        searchRequest.setSort(sortOptionList);

        ArrayList<String> strings = new ArrayList<String>();
        strings.add("routine");
        strings.add("physical_insurance_exams");

        filterOptionList = new ArrayList<FilterOption>();
        filterOptionList.add(new FilterOption("date","gt", "2017-01-06T15:00:00"));
        filterOptionList.add(new FilterOption("visit_type","in", strings));
        searchRequest.setFilter(filterOptionList);

        System.out.println(chino.search.searchDocuments(searchRequest, SCHEMA_ID));

        //Let's try to apply the same search with another method
        documents = chino.search.where("date")
                .gt("2017-01-06T15:00:00")
                .or("visit_type")
                .in(strings)
                .sortAscBy("date")
                .resultType("FULL_CONTENT")
                .searchDocuments(SCHEMA_ID);
        System.out.println(documents);



        //Finally we delete everything we created
        System.out.println(chino.documents.delete(first_document.getDocumentId(), true));
        System.out.println(chino.documents.delete(second_document.getDocumentId(), true));
        System.out.println(chino.documents.delete(third_document.getDocumentId(), true));
        System.out.println(chino.users.delete(doctor.getUserId(), true));
        System.out.println(chino.users.delete(first_patient.getUserId(), true));
        System.out.println(chino.users.delete(second_patient.getUserId(), true));
        System.out.println(chino.userSchemas.delete(userSchema.getUserSchemaId(), true));
        System.out.println(chino.schemas.delete(SCHEMA_ID, true));
        System.out.println(chino.repositories.delete(REPOSITORY_ID, true));
    }
}
