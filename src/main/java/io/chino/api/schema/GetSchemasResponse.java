
package io.chino.api.schema;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Wraps a {@link List} of {@link Schema Schemas} returned as a response to an API call
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "result",
    "schemas",
    "count",
    "total_count",
    "limit",
    "offset"
})
public class GetSchemasResponse {

    @JsonProperty("result")
    private String result;
    @JsonProperty("schemas")
    private List<Schema> schemas;
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
     *     The result
     */
    @JsonProperty("result")
    public String getResult() {
        return result;
    }

    /**
     * 
     * @param result
     *     The result
     */
    @JsonProperty("result")
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * 
     * @return
     *     The schema
     */
    @JsonProperty("schemas")
    public List<Schema> getSchemas() {
        return schemas;
    }

    /**
     * 
     * @param schemas
     *     The schemas
     */
    @JsonProperty("schemas")
    public void setSchemas(List<Schema> schemas) {
        this.schemas = schemas;
    }

    @Override
    public String toString(){
        String s = "";
        s+="count: "+count;
        s+=",\nresult: "+result;
        s+=",\ntotal_count: "+totalCount;
        s+=",\nlimit: "+limit;
        s+=",\noffset: "+offset;
        s+=",\nschemas: "+schemas;
        return s;
    }
}
