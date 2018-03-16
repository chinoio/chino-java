
package io.chino.api.user;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.chino.java.ChinoBaseAPI;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "username",
    "user_id",
    "schema_id",
    "insert_date",
    "groups",
    "attributes",
    "is_active",
    "last_update"
})
public class User {

    @JsonProperty("username")
    private String username;
    @JsonProperty("user_id")
    private String userId;
    @JsonProperty("schema_id")
    private String schemaId;
    @JsonProperty("insert_date")
    private Date insertDate;
    @JsonProperty("groups")
    private List<String> groups = new ArrayList<String>();
    @JsonProperty("attributes")
    private JsonNode attributes;
    @JsonProperty("is_active")
    private Boolean isActive;
    @JsonProperty("last_update")
    private Date lastUpdate;

    /**
     * 
     * @return
     *     The username
     */
    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    /**
     * 
     * @param username
     *     The username
     */
    @JsonProperty("username")
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 
     * @return
     *     The userId
     */
    @JsonProperty("user_id")
    public String getUserId() {
        return userId;
    }

    /**
     * 
     * @param userId
     *     The user_id
     */
    @JsonProperty("user_id")
    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getSchemaId() {
        return schemaId;
    }

    public void setSchemaId(String schemaId) {
        this.schemaId = schemaId;
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

    /**
     * 
     * @return
     *     The groups
     */
    @JsonProperty("groups")
    public List<String> getGroups() {
        return groups;
    }

    /**
     * 
     * @param groups
     *     The groups
     */
    @JsonProperty("groups")
    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    /**
     * 
     * @return
     *     The attributes
     */
    @JsonProperty("attributes")
    public JsonNode getAttributes() {
        return attributes;
    }

    /**
     * 
     * @param attributes
     *     The attributes
     */
    @JsonProperty("attributes")
    public void setAttributes(JsonNode attributes) {
        this.attributes = attributes;
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

    public HashMap<String, Object> getAttributesAsHashMap(){
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> result = mapper.convertValue(attributes, HashMap.class);
        return result;
    }
    
    @Override
    public String toString(){
    	String s="\n";
    	s+="username: "+username;
    	s+=",\nuser_id: "+userId;
    	s+=",\ninsert_date: "+insertDate;
    	try {
			s+=",\nattributes: "+ ChinoBaseAPI.getMapper().writeValueAsString(attributes);
		} catch (Exception e) {} 
    	s+=",\nis_active: "+isActive;
    	s+=",\nlast_update: "+lastUpdate;
       	 
    	s+=",\ngroups: {";
    	for (String group : groups) {
    		s+=group+",\n";
		}
    	s+=" }\n";


    	return s;
    }
}
