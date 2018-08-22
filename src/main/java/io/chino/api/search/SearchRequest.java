
package io.chino.api.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "result_type",
    "filter_type",
    "sort",
    "filter"
})
@Deprecated
public class SearchRequest {

    @JsonProperty("result_type")
    private String resultType;
    @JsonProperty("filter_type")
    private String filterType;
    @JsonProperty("sort")
    private List<SortOption> sort = new ArrayList<SortOption>();
    @JsonProperty("filter")
    private List<FilterOption> filter = new ArrayList<FilterOption>();

    public SearchRequest(){

    }

    public SearchRequest(String resultType, String filterType, List<SortOption> sort, List<FilterOption> filter){
        setResultType(resultType);
        setFilterType(filterType);
        setSort(sort);
        setFilter(filter);
    }

    /**
     *
     * @return
     *     The resultType
     */
    @JsonProperty("result_type")
    public String getResultType() {
        return resultType;
    }

    /**
     *
     * @param resultType
     *     The resultType
     */
    @JsonProperty("result_type")
    public void setResultType(String resultType) {
        if(resultType == null){
            throw new NullPointerException("result_type");
        }
        this.resultType = resultType;
    }

    /**
     * 
     * @return
     *     The filterType
     */
    @JsonProperty("filter_type")
    public String getFilterType() {
        return filterType;
    }

    /**
     * 
     * @param filterType
     *     The filter_type
     */
    @JsonProperty("filter_type")
    public void setFilterType(String filterType) {
        if(filterType == null){
            throw new NullPointerException("filter_type");
        }
        this.filterType = filterType;
    }

    /**
     * 
     * @return
     *     The sort
     */
    @JsonProperty("sort")
    public List<SortOption> getSort() {
        return sort;
    }

    /**
     * 
     * @param sort
     *     The sort
     */
    @JsonProperty("sort")
    public void setSort(List<SortOption> sort) {
        if(sort == null){
            throw new NullPointerException("sort");
        }
        this.sort = sort;
    }

    /**
     * 
     * @return
     *     The filter
     */
    @JsonProperty("filter")
    public List<FilterOption> getFilter() {
        return filter;
    }

    /**
     * 
     * @param filter
     *     The filter
     */
    @JsonProperty("filter")
    public void setFilter(List<FilterOption> filter) {
        if(filter == null){
            throw new NullPointerException("filter");
        }
        this.filter = filter;
    }

    public void addSortOption(String field, String order){
        sort.add(new SortOption(field, order));
    }
    public void addFilterOption(String field, String type, String value){
        filter.add(new FilterOption(field, type, value));
    }
}
