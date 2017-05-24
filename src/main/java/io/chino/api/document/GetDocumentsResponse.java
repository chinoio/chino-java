
package io.chino.api.document;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "documents",
    "count",
    "total_count",
    "limit",
    "offset"
})
public class GetDocumentsResponse {

    @JsonProperty("documents")
    private List<Document> documents = new ArrayList<Document>();
    @JsonProperty("count")
    private Integer count;
    @JsonProperty("total_count")
    private Integer totalCount;
    @JsonProperty("limit")
    private Integer limit;
    @JsonProperty("offset")
    private Integer offset;
    @JsonProperty("IDs")
    private List<String> ids = new ArrayList<String>();
    
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
     *     The documents
     */
    @JsonProperty("documents")
    public List<Document> getDocuments() {
        return documents;
    }

    /**
     * 
     * @param documents
     *     The documents
     */
    @JsonProperty("documents")
    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public String toString(){
        String s = "";
        s+="count: "+count;
        s+=",\ntotal_count: "+totalCount;
        s+=",\nlimit: "+limit;
        s+=",\noffset: "+offset;
        s+=",\ndocuments: "+documents;
        s+=",\nIDs: "+ids;
        return s;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
