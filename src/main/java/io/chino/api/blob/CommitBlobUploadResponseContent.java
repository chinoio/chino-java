package io.chino.api.blob;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
	{ 
		"bytes",
		"blob_id",
		"sha1",
		"document_id",
		"md5"
	}
)
public class CommitBlobUploadResponseContent {

	@JsonProperty("bytes")
	private int bytes;

	@JsonProperty("blob_id")
	private String blobId;
	
	@JsonProperty("sha1")
	private String sha1;
	
	@JsonProperty("document_id")
	private String documentId;
	
	@JsonProperty("md5")
	private String md5;

	public int getBytes() {
		return bytes;
	}

	public void setBytes(int bytes) {
		this.bytes = bytes;
	}

	public String getBlobId() {
		return blobId;
	}

	public void setBlobId(String blobId) {
		this.blobId = blobId;
	}

	public String getSha1() {
		return sha1;
	}

	public void setSha1(String sha1) {
		this.sha1 = sha1;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

        @Override
	public String toString(){
		String s="";
		s+="bytes: "+bytes;
		s+=", blob_id: "+blobId;
		s+=", sha1: "+sha1;
		s+=", md5: "+md5;
		s+=", document_id: "+documentId;
		return s;
	}
	

}