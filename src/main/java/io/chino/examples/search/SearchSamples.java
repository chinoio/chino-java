package io.chino.examples.search;

import io.chino.api.common.ChinoApiException;
import io.chino.api.document.Document;
import io.chino.api.document.GetDocumentsResponse;
import io.chino.api.repository.Repository;
import io.chino.api.schema.Schema;
import io.chino.api.search.FilterOption;
import io.chino.api.search.SearchRequest;
import io.chino.api.search.SortOption;
import io.chino.java.ChinoAPI;
import io.chino.examples.util.Constants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchSamples {

    ChinoAPI chino;
    String REPOSITORY_ID = "";
    String SCHEMA_ID = "";
    String DOCUMENT_ID_1 = "";
    String DOCUMENT_ID_2 = "";

    public void testSearch() throws IOException, ChinoApiException, InterruptedException {

        //We initialize the ChinoAPI variable with the customerId and customerKey
        chino = new ChinoAPI(Constants.HOST, Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY);

        //Now we need to add some Documents
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

        content = new HashMap<String, Object>();
        content.put("test_string", "new_test_string_value");
        content.put("test_integer", 1234);
        content.put("test_boolean", true);
        content.put("test_date", "1994-02-04");
        document = chino.documents.create(SCHEMA_ID, content);
        DOCUMENT_ID_2 = document.getDocumentId();

        //Now try to apply a search
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setFilterType("and");
        searchRequest.setResultType("FULL_CONTENT");
        List<SortOption> sortOptionList = new ArrayList<SortOption>();
        sortOptionList.add(new SortOption("test_string", "asc"));
        searchRequest.setSort(sortOptionList);

        List<FilterOption> filterOptionList = new ArrayList<FilterOption>();
        filterOptionList.add(new FilterOption("test_integer","gt", 123));
        searchRequest.setFilter(filterOptionList);

        //It is mandatory because the server needs 2 seconds to index the fields on the schema. If the documents are searched
        //before the fields are indexed, the result would be null
        Thread.sleep(5000);

        System.out.println(chino.search.searchDocuments(searchRequest, SCHEMA_ID));

        //Let's try to apply a search with the second method
        filterOptionList.add(new FilterOption("test_boolean", "eq", true));
        System.out.println(chino.search.searchDocuments(SCHEMA_ID, "FULL_CONTENT", "or", sortOptionList, filterOptionList));

        //Let's try to apply a search with this method
        GetDocumentsResponse documents = chino.search.where("test_integer").gt(123).and("test_date").eq("1994-02-04").sortAscBy("test_string").searchDocuments(SCHEMA_ID);
        System.out.println(documents);

        //Finally we delete everything we created
        System.out.println(chino.documents.delete(DOCUMENT_ID_1, true));
        System.out.println(chino.documents.delete(DOCUMENT_ID_2, true));
        System.out.println(chino.schemas.delete(SCHEMA_ID, true));
        System.out.println(chino.repositories.delete(REPOSITORY_ID, true));
    }
}
