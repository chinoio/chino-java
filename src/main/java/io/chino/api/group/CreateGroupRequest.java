package io.chino.api.group;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "group_name", "attributes" })
public class CreateGroupRequest {

	@JsonProperty("group_name")
	private String groupName;
	@JsonProperty("attributes")
	private HashMap attributes;

	/**
	 * 
	 * @return The groupname
	 */
	@JsonProperty("group_name")
	public String getGroupName() {
		return groupName;
	}

	/**
	 * 
	 * @param groupName
	 *            The groupName
	 */
	@JsonProperty("group_name")
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * 
	 * @return The attributes
	 */
	@JsonProperty("attributes")
	public HashMap getAttributes() {
		return attributes;
	}

	/**
	 * 
	 * @param attributes
	 *            The attributes
	 */
	@JsonProperty("attributes")
	public void setAttributes(HashMap attributes) {
		this.attributes = attributes;
	}

}