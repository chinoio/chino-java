
package io.chino.api.userschema;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.chino.java.ChinoBaseAPI;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "user_schema_id",
    "description",
    "is_active",
    "last_update",
    "groups",
    "structure",
    "insert_date"
})
public class UserSchema {

    @JsonProperty("user_schema_id")
    private String userSchemaId;
    @JsonProperty("description")
    private String description;
    @JsonProperty("is_active")
    private Boolean isActive;
    @JsonProperty("last_update")
    private Date lastUpdate;
    @JsonProperty("groups")
    private List<String> groups = new ArrayList<String>();
    @JsonProperty("structure")
    private UserSchemaStructure structure;
    @JsonProperty("insert_date")
    private Date insertDate;
 

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
     *     The structure
     */
    @JsonProperty("structure")
    public UserSchemaStructure getStructure() {
        return structure;
    }

    /**
     * 
     * @param structure
     *     The structure
     */
    @JsonProperty("structure")
    public void setStructure(UserSchemaStructure structure) {
        this.structure = structure;
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
    
    public String getUserSchemaId() {
		return userSchemaId;
	}

	public void setUserSchemaId(String userSchemaId) {
		this.userSchemaId = userSchemaId;
	}

	public List<String> getGroups() {
		return groups;
	}

	public void setGroups(List<String> groups) {
		this.groups = groups;
	}

	public String toString(){
    	String s="\n";
		s+="description: "+description;
		s+=",\nuser_schema_id: "+userSchemaId;
		s+=",\ninsert_date: "+insertDate.toString();
		s+=",\nlast_update: "+lastUpdate.toString();
		s+=",\nis_active: "+isActive;
    	
    	try {
			s+=",\nstructure: "+ ChinoBaseAPI.getMapper().writeValueAsString(structure);
		} catch (Exception e) {} 
        s+="\n";
    	return s;
    }

}
