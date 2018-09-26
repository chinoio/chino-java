package io.chino.api.document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Wraps an {@link Document} returned as a response to an API call
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"document"
})
public class GetDocumentResponse {

	@JsonProperty("document")
	private Document document;

	/**
	 * 
	 * @return The document
	 */
	@JsonProperty("document")
	public Document getDocument() {
		return document;
	}

	/**
	 * 
	 * @param document
	 *            The document
	 */
	@JsonProperty("document")
	public void setDocument(Document document) {
		this.document = document;
	}
}