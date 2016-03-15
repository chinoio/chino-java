
package io.chino.api.blob;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "blob"
})
public class CreateBlobUploadResponse {
    @JsonProperty("blob")
    private CreateBlobUploadResponseContent blob;

	public CreateBlobUploadResponseContent getBlob() {
		return blob;
	}

	public void setBlob(CreateBlobUploadResponseContent blob) {
		this.blob = blob;
	}
    
    
    
    
}