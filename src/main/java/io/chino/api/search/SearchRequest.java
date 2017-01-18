
package io.chino.api.search;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "result_type",
    "filter_type",
    "sort",
    "filter"
})
public class SearchRequest {


    @JsonProperty("result_type")
    private String resultType;
    @JsonProperty("filter_type")
    private String filterType;
    @JsonProperty("sort")
    private List<SortOption> sort = new ArrayList<SortOption>();
    @JsonProperty("filter")
    private List<FilterOption> filter = new ArrayList<FilterOption>();


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
        this.filter = filter;
    }

    public void addSortOption(String field, String order){
        sort.add(new SortOption(field, order));
    }
    public void addFilterOption(String field, String type, String value){
        filter.add(new FilterOption(field, type, value));
    }
}
