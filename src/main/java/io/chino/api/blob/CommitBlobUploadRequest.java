package io.chino.api.blob;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
	{ 
		"upload_id"		
	}
)
public class CommitBlobUploadRequest {

	@JsonProperty("upload_id")
	private String uploadId;

	public CommitBlobUploadRequest(){

	}

	public CommitBlobUploadRequest(String uploadId){
		setUploadId(uploadId);
	}

	public String getUploadId() {
		return uploadId;
	}

	public final void setUploadId(String uploadId) {
		if(uploadId == null){
			throw new NullPointerException("upload_id");
		}
		this.uploadId = uploadId;
	}
	
	

}