package io.chino.api.repository;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Wraps an {@link Repository} returned as a response to an API call
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"repository" })
public class GetRepositoryResponse {

	@JsonProperty("repository")
	private Repository repository;

	/**
	 * 
	 * @return The repository
	 */
	@JsonProperty("repository")
	public Repository getRepository() {
		return repository;
	}

	/**
	 * 
	 * @param repository
	 *            The repository
	 */
	@JsonProperty("repository")
	public void setRepository(Repository repository) {
		this.repository = repository;
	}

}