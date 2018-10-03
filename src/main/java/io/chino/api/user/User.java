
package io.chino.api.user;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.chino.java.ChinoBaseAPI;

import java.util.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * A User of Chino.io
 */
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
    private List<String> groups = new ArrayList<>();
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
        return mapper.convertValue(attributes, HashMap.class);
    }
    
    @Override
    public String toString(){
    	StringBuilder s= new StringBuilder("\n");
    	s.append("username: ").append(username);
    	s.append(",\nuser_id: ").append(userId);
    	s.append(",\ninsert_date: ").append(insertDate);
    	try {
			s.append(",\nattributes: ").append(ChinoBaseAPI.getMapper().writeValueAsString(attributes));
		} catch (Exception e) {} 
    	s.append(",\nis_active: ").append(isActive);
    	s.append(",\nlast_update: ").append(lastUpdate);
       	 
    	s.append(",\ngroups: {");
    	for (String group : groups) {
    		s.append(group).append(",\n");
		}
    	s.append(" }\n");


    	return s.toString();
    }

    /**
     * Compare this User to another object and tell whether they are equal.
     * The following conditions will cause this method to return {@code false} when:
     * <ul>
     *     <li>
     *         One of the Users represents an updated version of the other,
     *     </li>
     *     <li>
     *         One of the Users has both the userId and the username set to {@code null}
     *         (the identity of a User can not be verified without those parameters),
     *     </li>
     *     <li>
     *         One of the objects is null.
     *     </li>
     * </ul>
     *
     * @param obj the {@link Object} to compare
     * @return {@code true} if the two objects represent the same User object on Chino.io
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true; // same instance

        if (!(obj instanceof User))
            return false; // obj is null, or is not an instance of class User

        User other = (User) obj;
        boolean condition;
        if (userId != null && other.getUserId() != null) {
            // compare userId
            condition = other.getUserId().equalsIgnoreCase(userId);
        } else if (username != null && other.getUsername() != null){
            // userId is null - compare username
            condition = other.getUsername().equals(username);
        } else {
            // not enough information to tell whether the two Users are the same
            condition = false;
        }

        // perform checks on the updated status of the User
        try {
            return condition
                    && other.getInsertDate().equals(insertDate)
                    && other.getLastUpdate().equals(lastUpdate)
                    && other.getAttributesAsHashMap().equals(this.getAttributesAsHashMap());
        } catch (NullPointerException e) {
            return false;
        }
    }
}
