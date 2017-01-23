
package io.chino.api.blob;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "document_id",
    "field",
    "file_name"
})
public class CreateBlobUploadRequest {

    
    @JsonProperty("document_id")
    private String documentId;
    
    @JsonProperty("field")
    private String field;
    
    @JsonProperty("file_name")
    private String fileName;
    
    public CreateBlobUploadRequest(){

	}

	public CreateBlobUploadRequest(String documentId, String field, String fileName){
		setDocumentId(documentId);
		setField(field);
		setFileName(fileName);
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		if(documentId==null){
			throw new NullPointerException("document_id");
		}
		this.documentId = documentId;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		if(field==null){
			throw new NullPointerException("field");
		}
		this.field = field;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		if(fileName==null){
			throw new NullPointerException("file_name");
		}
		this.fileName = fileName;
	}
    

    


}
