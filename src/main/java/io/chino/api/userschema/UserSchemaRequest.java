package io.chino.api.userschema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "description", "structure" })
public class UserSchemaRequest {

	@JsonProperty("description")
	private String description;
	@JsonProperty("structure")
	private UserSchemaStructure structure;

	/**
	 * 
	 * @return The description
	 */
	@JsonProperty("description")
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 *            The description
	 */
	@JsonProperty("description")
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * @return The structure
	 */
	@JsonProperty("structure")
	public UserSchemaStructure getStructure() {
		return structure;
	}

	/**
	 * 
	 * @param structure
	 *            The structure
	 */
	@JsonProperty("structure")
	public void setStructure(UserSchemaStructure structure) {
		this.structure = structure;
	}

}