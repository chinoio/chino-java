package io.chino.api.application;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "count",
        "total_count",
        "limit",
        "offset",
        "applications"
})
public class GetApplicationsResponse {

    @JsonProperty("applications")
    private List<Application> applications;
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
     *     The applications
     */
    @JsonProperty("applications")
    public List<Application> getApplications() {
        return applications;
    }

    /**
     *
     * @param applications
     *     The applications
     */
    @JsonProperty("applications")
    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    @Override
    public String toString(){
        String s = "";
        s+="count: "+count;
        s+=",\ntotal_count: "+totalCount;
        s+=",\nlimit: "+limit;
        s+=",\noffset: "+offset;
        s+=",\ndocuments: "+applications;
        return s;
    }
}

