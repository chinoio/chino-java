package io.chino.api.user;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "result", "count", "total_count", "limit",
		"offset" })
public class GetUsersResponse {

	@JsonProperty("exists")
	private Boolean exists;
	@JsonProperty("count")
	private Integer count;
	@JsonProperty("total_count")
	private Integer totalCount;
	@JsonProperty("limit")
	private Integer limit;
	@JsonProperty("offset")
	private Integer offset;
	@JsonProperty("users")
	private List<User> users = new ArrayList<User>();

	/**
	 * 
	 * @return The users
	 */
	@JsonProperty("users")
	public List<User> getUsers() {
		return users;
	}

	/**
	 * 
	 * @param users
	 *            The users
	 */
	@JsonProperty("users")
	public void setUsers(List<User> users) {
		this.users = users;
	}

	/**
	 * 
	 * @return The count
	 */
	@JsonProperty("count")
	public Integer getCount() {
		return count;
	}

	/**
	 * 
	 * @param count
	 *            The count
	 */
	@JsonProperty("count")
	public void setCount(Integer count) {
		this.count = count;
	}

	/**
	 * 
	 * @return The totalCount
	 */
	@JsonProperty("total_count")
	public Integer getTotalCount() {
		return totalCount;
	}

	/**
	 * 
	 * @param totalCount
	 *            The total_count
	 */
	@JsonProperty("total_count")
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 
	 * @return The limit
	 */
	@JsonProperty("limit")
	public Integer getLimit() {
		return limit;
	}

	/**
	 * 
	 * @param limit
	 *            The limit
	 */
	@JsonProperty("limit")
	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	/**
	 * 
	 * @return The offset
	 */
	@JsonProperty("offset")
	public Integer getOffset() {
		return offset;
	}

	/**
	 * 
	 * @param offset
	 *            The offset
	 */
	@JsonProperty("offset")
	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public String toString(){
		String s = "";
		s+="count: "+count;
		s+=",\ntotal_count: "+totalCount;
		s+=",\nlimit: "+limit;
		s+=",\noffset: "+offset;
		s+=",\nusers: "+users;
		s+=",\nexists: "+exists;
		return s;
	}

	public Boolean getExists() {
		return exists;
	}

	public void setExists(Boolean exists) {
		this.exists = exists;
	}
}
