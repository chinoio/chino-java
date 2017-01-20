package io.chino.java;

import com.fasterxml.jackson.databind.JsonNode;
import io.chino.api.common.ChinoApiConstants;
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
     * It searches Documents
     * @param searchRequest the SearchRequest Object
     * @param schemaId the id of the Schema
     * @param offset the offset from which it retrieves the Documents
     * @param limit number of results (max {@link io.chino.api.common.ChinoApiConstants#QUERY_DEFAULT_LIMIT})
     * @return GetDocumentsResponse Object with the list of Documents
     * @throws IOException
     * @throws ChinoApiException
     */
    public GetDocumentsResponse searchDocuments(SearchRequest searchRequest, String schemaId, int offset, int limit) throws IOException, ChinoApiException {
        JsonNode data = postResource("/search/documents/"+schemaId, searchRequest, offset, limit);
        if(data!=null)
            return mapper.convertValue(data, GetDocumentsResponse.class);

        return null;
    }

    /**
     * It searches Documents
     * @param searchRequest the SearchRequest Object
     * @param schemaId the id of the schema
     * @return GetDocumentsResponse Object with the list of Documents
     * @throws IOException
     * @throws ChinoApiException
     */
    public GetDocumentsResponse searchDocuments(SearchRequest searchRequest, String schemaId) throws IOException, ChinoApiException {
        JsonNode data = postResource("/search/documents/"+schemaId, searchRequest, 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return mapper.convertValue(data, GetDocumentsResponse.class);

        return null;
    }

    /**
     * It searches Users
     * @param searchRequest the SearchRequest Object
     * @param userSchemaId the id of the UserSchema
     * @param offset the offset from which it retrieves the Documents
     * @param limit number of results (max {@link io.chino.api.common.ChinoApiConstants#QUERY_DEFAULT_LIMIT})
     * @return GetUsersResponse Object with the list of Users
     * @throws IOException
     * @throws ChinoApiException
     */
    public GetUsersResponse searchUsers(SearchRequest searchRequest, String userSchemaId, int offset, int limit) throws IOException, ChinoApiException {
        JsonNode data = postResource("/search/users/"+userSchemaId, searchRequest, offset, limit);
        if(data!=null)
            return mapper.convertValue(data, GetUsersResponse.class);

        return null;
    }

    /**
     * It searches Users
     * @param searchRequest the SearchRequest Object
     * @param userSchemaId the id of the UserSchema
     * @return GetUsersResponse Object with the list of Users
     * @throws IOException
     * @throws ChinoApiException
     */
    public GetUsersResponse searchUsers(SearchRequest searchRequest, String userSchemaId) throws IOException, ChinoApiException {
        JsonNode data = postResource("/search/users/"+userSchemaId, searchRequest, 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return mapper.convertValue(data, GetUsersResponse.class);

        return null;
    }

    /**
     * It searches Documents
     * @param schemaId the id of the Schema
     * @param resultType the type of result of the response
     * @param filterType the type of filter
     * @param sort the list of SortOption
     * @param filter the list of FilterOption
     * @param offset the offset from which it retrieves the Applications
     * @param limit number of results (max {@link io.chino.api.common.ChinoApiConstants#QUERY_DEFAULT_LIMIT})
     * @return GetDocumentsResponse Object which contains the list of Documents
     * @throws IOException
     * @throws ChinoApiException
     */
    public GetDocumentsResponse searchDocuments(String schemaId, String resultType, String filterType, List<SortOption> sort, List<FilterOption> filter, int offset, int limit) throws IOException, ChinoApiException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setResultType(resultType);
        searchRequest.setFilterType(filterType);
        searchRequest.setSort(sort);
        searchRequest.setFilter(filter);

        return searchDocuments(searchRequest, schemaId, offset, limit);
    }

    /**
     * It searches Documents
     * @param schemaId the id of the Schema
     * @param resultType the type of result of the response
     * @param filterType the type of filter
     * @param sort the list of SortOption
     * @param filter the list of FilterOption
     * @return GetDocumentsResponse Object which contains the list of Documents
     * @throws IOException
     * @throws ChinoApiException
     */
    public GetDocumentsResponse searchDocuments(String schemaId, String resultType, String filterType, List<SortOption> sort, List<FilterOption> filter) throws IOException, ChinoApiException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setResultType(resultType);
        searchRequest.setFilterType(filterType);
        searchRequest.setSort(sort);
        searchRequest.setFilter(filter);

        return searchDocuments(searchRequest, schemaId, 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
    }

    /**
     * It searches Users
     * @param userSchemaId the id of the Schema
     * @param resultType the type of result of the response
     * @param filterType the type of filter
     * @param sort the list of SortOption
     * @param filter the list of FilterOption
     * @return GetUsersResponse Object which contains the list of Users
     * @throws IOException
     * @throws ChinoApiException
     */
    public GetUsersResponse searchUsers(String userSchemaId, String resultType, String filterType, List<SortOption> sort, List<FilterOption> filter) throws IOException, ChinoApiException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setResultType(resultType);
        searchRequest.setFilterType(filterType);
        searchRequest.setSort(sort);
        searchRequest.setFilter(filter);

        return searchUsers(searchRequest, userSchemaId, 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
    }

    /**
     * It searches Users
     * @param userSchemaId the id of the Schema
     * @param resultType the type of result of the response
     * @param filterType the type of filter
     * @param sort the list of SortOption
     * @param filter the list of FilterOption
     * @param offset the offset from which it retrieves the Applications
     * @param limit number of results (max {@link io.chino.api.common.ChinoApiConstants#QUERY_DEFAULT_LIMIT})
     * @return GetUsersResponse Object which contains the list of Users
     * @throws IOException
     * @throws ChinoApiException
     */
    public GetUsersResponse searchUsers(String userSchemaId, String resultType, String filterType, List<SortOption> sort, List<FilterOption> filter, int offset, int limit) throws IOException, ChinoApiException {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setResultType(resultType);
        searchRequest.setFilterType(filterType);
        searchRequest.setSort(sort);
        searchRequest.setFilter(filter);

        return searchUsers(searchRequest, userSchemaId, offset, limit);
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
        //Every time you add a new SortOption you have to store the List in the searchRequest variable, which will be finally used to make the request
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
    public GetDocumentsResponse searchDocuments(String schemaId, int offset, int limit) throws IOException, ChinoApiException {
        if (searchRequest.getFilterType() == null)
            searchRequest.setFilterType("or");
        return searchDocuments(searchRequest, schemaId, offset, limit);
    }
    public GetDocumentsResponse searchDocuments(String schemaId) throws IOException, ChinoApiException {
        if (searchRequest.getFilterType() == null)
            searchRequest.setFilterType("or");
        return searchDocuments(searchRequest, schemaId, 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
    }

    public GetUsersResponse searchUsers(String userSchemaId) throws IOException, ChinoApiException {
        if (searchRequest.getFilterType() == null)
            searchRequest.setFilterType("or");
        return searchUsers(searchRequest, userSchemaId, 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
    }

    public GetUsersResponse searchUsers(String userSchemaId, int offset, int limit) throws IOException, ChinoApiException {
        if (searchRequest.getFilterType() == null)
            searchRequest.setFilterType("or");
        return searchUsers(searchRequest, userSchemaId, offset, limit);
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
