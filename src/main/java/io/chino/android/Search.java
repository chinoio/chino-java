package io.chino.android;

import com.fasterxml.jackson.databind.JsonNode;
import io.chino.api.common.ChinoApiException;
import io.chino.api.document.GetDocumentsResponse;
import io.chino.api.search.FilterOption;
import io.chino.api.search.SearchRequest;
import io.chino.api.search.SortOption;
import io.chino.api.user.GetUsersResponse;
import okhttp3.OkHttpClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Search extends ChinoBaseAPI {

    private SearchRequest searchRequest = new SearchRequest();
    private List<SortOption> sort = new ArrayList<SortOption>();
    private List<FilterOption> filter = new ArrayList<FilterOption>();
    private FilterOption filterOption;

    public Search(String hostUrl, OkHttpClient clientInitialized){
        super(hostUrl, clientInitialized);
    }

    /**
     * Used to search Documents
     * @param searchRequest the SearchRequest Object
     * @return a GetDocumentsResponse Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public GetDocumentsResponse searchDocuments(SearchRequest searchRequest, String schemaId) throws IOException, ChinoApiException {
        JsonNode data = postResource("/search/documents/"+schemaId, searchRequest);
        if(data!=null)
            return mapper.convertValue(data, GetDocumentsResponse.class);

        return null;
    }

    public GetUsersResponse searchUsers(SearchRequest searchRequest, String userSchemaId) throws IOException, ChinoApiException {
        JsonNode data = postResource("/search/users/"+userSchemaId, searchRequest);
        if(data!=null)
            return mapper.convertValue(data, GetUsersResponse.class);

        return null;
    }
    /**
     * Used to search Documents
     * @param schemaId the id of the Schema
     * @param resultType the type of result wanted in response
     * @param filterType the type of filter
     * @param sort the List of SortOption
     * @param filter the list of FilterOption
     * @return a GetDocumentsResponse Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public GetDocumentsResponse searchDocuments(String schemaId, String resultType, String filterType, List<SortOption> sort, List<FilterOption> filter) throws IOException, ChinoApiException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setResultType(resultType);
        searchRequest.setFilterType(filterType);
        searchRequest.setSort(sort);
        searchRequest.setFilter(filter);

        return searchDocuments(searchRequest, schemaId);
    }

    public GetUsersResponse searchUsers(String userSchemaId, String resultType, String filterType, List<SortOption> sort, List<FilterOption> filter) throws IOException, ChinoApiException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setResultType(resultType);
        searchRequest.setFilterType(filterType);
        searchRequest.setSort(sort);
        searchRequest.setFilter(filter);

        return searchUsers(searchRequest, userSchemaId);
    }

    /*
         * Those functions below are used to make a search in a different way
         */

    //This is called when you want to make a sort of a certain field in an ascending order
    public Search sortAscBy(String field)
    {
        //This simply adds a new SortOption to the private List "sort" of the class
        SortOption sortOption = new SortOption(field, "asc");
        sort.add(sortOption);
        //Everytime you add a new SortOption you have to store the List in the searchRequest variable, which will be finally used to make the request
        searchRequest.setSort(sort);
        return this;
    }

    //This is called when you want to make a sort of a certain field in a descending order
    public Search sortDescBy(String field)
    {
        SortOption sortOption = new SortOption(field, "desc");
        sort.add(sortOption);
        searchRequest.setSort(sort);
        return this;
    }

    //This is called when you want to specify a result type. If you don't call this function the default value is "FULL_CONTENT"
    public Search resultType(String resultType)
    {
        searchRequest.setResultType(resultType);
        return this;
    }

    /*
     * This is the first function that needs to be called and sets result_type and without_index variables at their default value.
     * It also calls the filterOperation function which creates a new FilterOption and sets its "field" value;
     */
    public Search where(String field)
    {
        searchRequest.setResultType("FULL_CONTENT");
        filterOperation(field);
        return this;
    }

    /*
     * This is the last function called which sets filter_type to "or" if there is only one FilterOption (initialized by the where(...) function)
     * It sets the schemaId and finally performs the search request, calling the function searchDocuments passing the class variable searchRequest
     */
    public GetDocumentsResponse searchDocuments(String schemaId) throws IOException, ChinoApiException {
        if (searchRequest.getFilterType() == null)
            searchRequest.setFilterType("or");
        return searchDocuments(searchRequest, schemaId);
    }

    public GetUsersResponse searchUsers(String userSchemaId) throws IOException, ChinoApiException {
        if (searchRequest.getFilterType() == null)
            searchRequest.setFilterType("or");
        return searchUsers(searchRequest, userSchemaId);
    }

    //This function is called if you want to make a request with filter_type set to "and"
    public Search and(String field) throws ChinoApiException {
        //If filter_type value is set to "or" it raises an error
        if (searchRequest.getFilterType() == "or")
            throw new ChinoApiException("Wrong filter operations!");
        //If the value is "and" or is "null"(which is the case of the first call) it sets the value to "and"
        searchRequest.setFilterType("and");
        return filterOperation(field);
    }

    //This function is called if you want to make a request with filter_type set to "or"
    public Search or(String field) throws ChinoApiException {
        if (searchRequest.getFilterType() == "and")
            throw new ChinoApiException("Wrong filter operations!");
        searchRequest.setFilterType("or");
        return filterOperation(field);
    }

    //This function creates a new FilterOption and adds it to the private List "filter", then sets the value of the searchRequest.filter variable to the List updated
    private Search filterOperation(String field)
    {
        filterOption = new FilterOption();
        filterOption.setField(field);
        filter.add(filterOption);
        searchRequest.setFilter(filter);
        return this;
    }

    //Those functions below set the value and type of the FilterOption
    public Search eq(Object value)
    {
        filterOption.setValue(value);
        filterOption.setType("eq");
        return this;
    }

    public Search gt(Object value)
    {
        filterOption.setValue(value);
        filterOption.setType("gt");
        return this;
    }

    public Search gte(Object value)
    {
        filterOption.setValue(value);
        filterOption.setType("gte");
        return this;
    }

    public Search lt(Object value)
    {
        filterOption.setValue(value);
        filterOption.setType("lt");
        return this;
    }

    public Search lte(Object value)
    {
        filterOption.setValue(value);
        filterOption.setType("lte");
        return this;
    }

}
