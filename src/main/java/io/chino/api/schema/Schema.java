
package io.chino.api.schema;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.chino.java.ChinoBaseAPI;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "repository_id",
    "schema_id",
    "description",
    "is_active",
    "last_update",
    "structure",
    "insert_date"
})
public class Schema {

    @JsonProperty("repository_id")
    private String repositoryId;
    @JsonProperty("schema_id")
    private String schemaId;
    @JsonProperty("description")
    private String description;
    @JsonProperty("is_active")
    private Boolean isActive;
    @JsonProperty("last_update")
    private Date lastUpdate;
    @JsonProperty("structure")
    private SchemaStructure structure;
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
     *     The schemaId
     */
    @JsonProperty("schema_id")
    public String getSchemaId() {
        return schemaId;
    }

    /**
     * 
     * @param schemaId
     *     The schema_id
     */
    @JsonProperty("schema_id")
    public void setSchemaId(String schemaId) {
        this.schemaId = schemaId;
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
     *     The structure
     */
    @JsonProperty("structure")
    public SchemaStructure getStructure() {
        return structure;
    }

    /**
     * 
     * @param structure
     *     The structure
     */
    @JsonProperty("structure")
    public void setStructure(SchemaStructure structure) {
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
    
    @Override
    public String toString(){
    	String s="\n";
		s+="description: "+description;
		s+=",\nschema_id: "+schemaId;
		s+=",\nrepository_id: "+repositoryId;
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
