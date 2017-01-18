package io.chino.api.collection;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "insert_date",
        "is_active",
        "last_update",
        "name",
        "collection_id"
})

public class Collection {

    @JsonProperty("insert_date")
    private Date insertDate;
    @JsonProperty("is_active")
    private Boolean isActive;
    @JsonProperty("last_update")
    private Date lastUpdate;
    @JsonProperty("name")
    private String name;
    @JsonProperty("collection_id")
    private String collectionId;

    /**
     *
     * @return
     *     The insertDate
     */
    @JsonProperty("insert_date")
    public Date getInsertDate() {
        return insertDate;
    }

    /**
     *
     * @param insertDate
     *     The insert_date
     */
    @JsonProperty("insert_date")
    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    /**
     *
     * @return
     *     The isActive
     */
    @JsonProperty("is_active")
    public Boolean getIsActive() {
        return isActive;
    }

    /**
     *
     * @param isActive
     *     The is_active
     */
    @JsonProperty("is_active")
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    /**
     *
     * @return
     *     The lastUpdate
     */
    @JsonProperty("last_update")
    public Date getLastUpdate() {
        return lastUpdate;
    }

    /**
     *
     * @param lastUpdate
     *     The last_update
     */
    @JsonProperty("last_update")
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
     *
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     *     The collectionId
     */
    @JsonProperty("collection_id")
    public String getCollectionId() {
        return collectionId;
    }

    /**
     *
     * @param collectionId
     *     The collectionId
     */
    @JsonProperty("collection_id")
    public void setCollectionId(String collectionId) {
        this.collectionId = collectionId;
    }


    public String toString(){
        String s="";
        s+="name: " + name;
        s+=", collection_id: " + collectionId;
        s+=", insert_date: "+insertDate;
        s+=", is_active: " + isActive;
        s+=", last_update: " + lastUpdate;

        return s;
    }
}
