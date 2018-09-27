
package io.chino.api.userschema;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Wraps a {@link List} of {@link UserSchema UserSchemas} returned as a response to an API call
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "result",
    "user_schemas",
    "count",
    "total_count",
    "limit",
    "offset"
})
public class GetUserSchemasResponse {

    @JsonProperty("result")
    private String result;
    @JsonProperty("user_schemas")
    private List<UserSchema> userSchemas;
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

	public List<UserSchema> getUserSchemas() {
		return userSchemas;
	}

	public void setUserSchemas(List<UserSchema> userSchemas) {
		this.userSchemas = userSchemas;
	}

    @Override
    public String toString(){
        String s = "";
        s+="count: "+count;
        s+=",\nresult: "+result;
        s+=",\ntotal_count: "+totalCount;
        s+=",\nlimit: "+limit;
        s+=",\noffset: "+offset;
        s+=",\nuser_schemas: "+userSchemas;
        return s;
    }
}
