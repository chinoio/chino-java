package io.chino.api.schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.chino.api.common.Field;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "description", "structure" })
public class SchemaRequest {

	@JsonProperty("description")
	private String description;
	@JsonProperty("structure")
	private SchemaStructure structure = new SchemaStructure();

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
	public SchemaStructure getStructure() {
		return structure;
	}

	/**
	 * 
	 * @param structure
	 *            The structure
	 */
	@JsonProperty("structure")
	public void setStructure(SchemaStructure structure) {
		this.structure = structure;
	}

	public void addSchemaField(String name, String type) {
		structure.getFields().add(new Field(name, type));
	}

}