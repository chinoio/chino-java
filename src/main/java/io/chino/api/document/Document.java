
package io.chino.api.document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.chino.java.ChinoBaseAPI;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

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

    /**
     * Get the content of this {@link Document}. Before using this method, the Document's content must be fetched using
     * {@link io.chino.java.Documents#read(String)}, otherwise an exception will be thrown.
     *
     * @return The content of this {@link Document}
     *
     * @throws IllegalStateException the method is invoked on a {@link Document} before its content has been fetched.
     */
    public JsonNode getContent() {
        if (content != null) {
            return content;
        } else {
            throw new IllegalStateException("Content not present. Use documents.read() to fetch the content of this Document.");
        }
    }

    /**
     * Get this Document's content as an instance of {@link HashMap}.
     * Before using this method, the Document's content must be fetched using
     * {@link io.chino.java.Documents#read(String)}, otherwise an exception will be thrown.
     *
     * @return an {@link HashMap HashMap&lt;String, Object&gt;} with the content of this Document.
     *
     * @throws IllegalStateException the method is invoked on a {@link Document} before its content has been fetched.
     */
    public HashMap<String, Object> getContentAsHashMap(){
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(this.getContent(), HashMap.class);
    }

    @JsonProperty("content")
    private JsonNode getContentForSerialization() {
        return content;
    }

    /**
     * Check that this {@link Document}'s content has been fetched.
     *
     * @return {@code true} if this Document's content is a valid {@link JsonNode}.
     */
    public boolean hasContent() {
        return content != null;
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
     * @param content
     *     The content
     */
    public void setContent(HashMap<? extends String, ?> content) {
        JsonNode jsonContent = new ObjectMapper().valueToTree(content);
        setContent(jsonContent);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Document))
            return false;
        Document other = (Document) obj;
        return other.documentId.equals(documentId)
                && (other.schemaId.equals(schemaId))
                && (other.repositoryId.equals(repositoryId))
                && (other.insertDate.equals(insertDate))
                && (other.lastUpdate.equals(lastUpdate))
                && (other.hasContent() == this.hasContent())
                && (other.getContentAsHashMap().equals(getContentAsHashMap())
        );
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(documentId)
                + Objects.hashCode(schemaId)
                + Objects.hashCode(repositoryId)
                + Objects.hashCode(insertDate)
                + Objects.hashCode(lastUpdate)
                + Objects.hashCode(getContentAsHashMap());
    }

    @Override
    public String toString(){
    	String s="\ndocument_id: "+documentId;
		s+=",\nrepository_id: "+repositoryId;
		s+=",\nschema_id: "+schemaId;
		s+=",\ninsert_date: "+ insertDate.toString();
		s+=",\nlast_update: "+ lastUpdate.toString();
		s+=",\nis_active: "+isActive;

        s+=",\ncontent: ";
    	try {
			s += ChinoBaseAPI.getMapper().writeValueAsString(content);
		} catch(IllegalStateException ise) {
    	    s += "null";
        }catch (Exception e) {
    	    s += "n.d. (error)";
        }
        s+="\n";
    	return s;
    }

}
