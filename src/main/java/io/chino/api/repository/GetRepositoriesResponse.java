
package io.chino.api.repository;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Wraps a {@link List} of {@link Repository Repositories} returned as a response to an API call
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "repositories",
    "count",
    "total_count",
    "limit",
    "offset"
})
public class GetRepositoriesResponse {

    @JsonProperty("repositories")
    private List<Repository> repositories = new ArrayList<>();
    @JsonProperty("count")
    private Integer count;
    @JsonProperty("total_count")
    private Integer totalCount;
    @JsonProperty("limit")
    private Integer limit;
    @JsonProperty("offset")
    private Integer offset;
    
    

	/**
    * 
    * @return
    * The count
    */
    @JsonProperty("count")
    public Integer getCount() {
    return count;
    }

    /**
    * 
    * @param count
    * The count
    */
    @JsonProperty("count")
    public void setCount(Integer count) {
    this.count = count;
    }

    /**
    * 
    * @return
    * The totalCount
    */
    @JsonProperty("total_count")
    public Integer getTotalCount() {
    return totalCount;
    }

    /**
    * 
    * @param totalCount
    * The total_count
    */
    @JsonProperty("total_count")
    public void setTotalCount(Integer totalCount) {
    this.totalCount = totalCount;
    }

    /**
    * 
    * @return
    * The limit
    */
    @JsonProperty("limit")
    public Integer getLimit() {
    return limit;
    }

    /**
    * 
    * @param limit
    * The limit
    */
    @JsonProperty("limit")
    public void setLimit(Integer limit) {
    this.limit = limit;
    }
    
    /**
    * 
    * @return
    * The offset
    */
    @JsonProperty("offset")
    public Integer getOffset() {
    return offset;
    }

    /**
    * 
    * @param offset
    * The offset
    */
    @JsonProperty("offset")
    public void setOffset(Integer offset) {
    this.offset = offset;
    }
    
    
    /**
     * 
     * @return
     *     The repositories
     */
    @JsonProperty("repositories")
    public List<Repository> getRepositories() {
        return repositories;
    }

    /**
     * 
     * @param repositories
     *     The repositories
     */
    @JsonProperty("repositories")
    public void setRepositories(List<Repository> repositories) {
        this.repositories = repositories;
    }

    @Override
    public String toString(){
        StringBuilder s= new StringBuilder();
        s.append("\ncount: ").append(count);
        s.append(",\ntotal_count: ").append(totalCount);
        s.append(",\noffset: ").append(offset);
        s.append(",\nlimit: ").append(limit);
        for(Repository r : repositories){
            s.append("\nRepository: ").append(r);
        }
        return s.toString();
    }

}
