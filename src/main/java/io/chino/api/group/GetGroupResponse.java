package io.chino.api.group;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Wraps an {@link Group} returned as a response to an API call
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "group" })
public class GetGroupResponse {

	@JsonProperty("group")
	private Group group;

	/**
	 * 
	 * @return The group
	 */
	@JsonProperty("group")
	public Group getGroup() {
		return group;
	}

	/**
	 * 
	 * @param group
	 *            The group
	 */
	@JsonProperty("group")
	public void setGroup(Group group) {
		this.group = group;
	}

}