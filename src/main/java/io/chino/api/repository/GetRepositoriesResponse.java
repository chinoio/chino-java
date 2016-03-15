
package io.chino.api.repository;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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
    private List<Repository> repositories = new ArrayList<Repository>();
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

    public String toString(){
        String s="";
        s+="\ncount: "+count;
        s+=",\ntotal_count: "+totalCount;
        s+=",\noffset: "+offset;
        s+=",\nlimit: "+limit;
        for(Repository r : repositories){
            s+="\nRepository: "+r;
        }
        return s;
    }

}
