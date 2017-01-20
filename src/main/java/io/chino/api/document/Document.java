
package io.chino.api.document;

import java.util.Date;
import java.util.HashMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.chino.java.ChinoBaseAPI;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "content",
    "repository_id",
    "schema_id",
    "insert_date",
    "is_active",
    "document_id",
    "last_update"
})
public class Document {

    @JsonProperty("document_id")
    private String documentId;
    @JsonProperty("repository_id")
    private String repositoryId;
    @JsonProperty("schema_id")
    private String schemaId;
    @JsonProperty("insert_date")
    private Date insertDate;
    @JsonProperty("is_active")
    private Boolean isActive;
    @JsonProperty("last_update")
    private Date lastUpdate;
    @JsonProperty("content")
    private JsonNode content;

    /**
     * 
     * @return
     *     The content
     */
    @JsonProperty("content")
    public JsonNode getContent() {
        return content;
    }

    /**
     * 
     * @param content
     *     The content
     */
    @JsonProperty("content")
    public void setContent(JsonNode content) {
        this.content = content;
    }

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
     *     The documentId
     */
    @JsonProperty("document_id")
    public String getDocumentId() {
        return documentId;
    }

    /**
     * 
     * @param documentId
     *     The document_id
     */
    @JsonProperty("document_id")
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
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

    public HashMap<String, Object> getContentAsHashMap(){
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> result = mapper.convertValue(content, HashMap.class);
        return result;
    }

    
    public String toString(){
    	String s="\n";
		s+="document_id: "+documentId;
		s+=",\nrepository_id: "+repositoryId;
		s+=",\nschema_id: "+schemaId;
		s+=",\ninsert_date: "+insertDate.toString();
		s+=",\nlast_update: "+lastUpdate.toString();
		s+=",\nis_active: "+isActive;
    	
    	try {
			s+=",\ncontent: "+ ChinoBaseAPI.getMapper().writeValueAsString(content);
		} catch (Exception e) {} 
        s+="\n";
    	return s;
    }

}
