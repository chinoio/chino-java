
package io.chino.api.blob;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "blob"
})
public class CommitBlobUploadResponse {

    @JsonProperty("blob")
    private CommitBlobUploadResponseContent blob;

	public CommitBlobUploadResponseContent getBlob() {
		return blob;
	}

	public void setBlob(CommitBlobUploadResponseContent blob) {
		this.blob = blob;
	}

    @Override
	public String toString(){
		return blob.toString();
	}
    
    
}