
package io.chino.api.repository;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Date;
import java.util.Objects;

/**
 * A Repository of Chino.io
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "repository_id",
    "description",
    "is_active",
    "last_update",
    "insert_date"
})
public class Repository {

    @JsonProperty("repository_id")
    private String repositoryId;
    @JsonProperty("description")
    private String description;
    @JsonProperty("is_active")
    private Boolean isActive;
    @JsonProperty("last_update")
    private Date lastUpdate;
    @JsonProperty("insert_date")
    private Date insertDate;

    /**
     * 
     * @return
     *     The repositoryId
     */
    @JsonProperty("repository_id")
    public String getRepositoryId() {
        return repositoryId;
    }

    /**
     * 
     * @param repositoryId
     *     The repository_id
     */
    @JsonProperty("repository_id")
    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    /**
     * 
     * @return
     *     The description
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The description
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
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
    
    @Override
    public String toString(){
    	String s="";
		s+="\ndescription: "+description;
		s+=",\nrepository_id: "+repositoryId;
		s+=",\ninsert_date: "+insertDate.toString();
		s+=",\nlast_update: "+lastUpdate.toString();
		s+=",\nis_active: "+isActive;
        s+="\n";
    	return s;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Repository that = (Repository) o;
        return Objects.equals(repositoryId, that.repositoryId) &&
                Objects.equals(description, that.description) &&
                Objects.equals(isActive, that.isActive) &&
                Objects.equals(lastUpdate, that.lastUpdate) &&
                Objects.equals(insertDate, that.insertDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(repositoryId, description, isActive, lastUpdate, insertDate);
    }
}
