
package io.chino.api.search;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "schema_id",
    "result_type",
    "filter_type",
    "sort",
    "filter",
    "without_index"
})
public class SearchRequest {

    @JsonProperty("schema_id")
    private String schemaId;
    @JsonProperty("result_type")
    private String resultType;
    @JsonProperty("without_index")
    private Boolean withoutIndex;
    @JsonProperty("filter_type")
    private String filterType;
    @JsonProperty("sort")
    private List<SortOption> sort = new ArrayList<SortOption>();
    @JsonProperty("filter")
    private List<FilterOption> filter = new ArrayList<FilterOption>();

    /**
     * 
     * @return
     *     The schemaId
     */
    @JsonProperty("schema_id")
    public String getSchemaId() {
        return schemaId;
    }

    /**
     * 
     * @param schemaId
     *     The schema_id
     */
    @JsonProperty("schema_id")
    public void setSchemaId(String schemaId) {
        this.schemaId = schemaId;
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

	public Boolean getWithoutIndex() {
		return withoutIndex;
	}

	public void setWithoutIndex(Boolean withoutIndex) {
		this.withoutIndex = withoutIndex;
	}

    public void addSortOption(String field, String order){
        sort.add(new SortOption(field, order));
    }
    public void addFilterOption(String field, String type, String value, Boolean caseSensitive){
        filter.add(new FilterOption(field, type, value, caseSensitive));
    }
}
