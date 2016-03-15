package io.chino.api.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "data", "message", "result", "result_code" })
public class ErrorResponse {

	@JsonProperty("data")
	private Object data;
	@JsonProperty("message")
	private String message;
	@JsonProperty("result")
	private String result;
	@JsonProperty("result_code")
	private Integer resultCode;

	/**
	 * 
	 * @return The data
	 */
	@JsonProperty("data")
	public Object getData() {
		return data;
	}

	/**
	 * 
	 * @param data
	 *            The data
	 */
	@JsonProperty("data")
	public void setData(Object data) {
		this.data = data;
	}

	/**
	 * 
	 * @return The messages
	 */
	@JsonProperty("message")
	public String getMessage() {
		return message;
	}

	/**
	 * 
	 * @param messages
	 *            The messages
	 */
	@JsonProperty("message")
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 
	 * @return The result
	 */
	@JsonProperty("result")
	public String getResult() {
		return result;
	}

	/**
	 * 
	 * @param result
	 *            The result
	 */
	@JsonProperty("result")
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * 
	 * @return The resultCode
	 */
	@JsonProperty("result_code")
	public Integer getResultCode() {
		return resultCode;
	}

	/**
	 * 
	 * @param resultCode
	 *            The result_code
	 */
	@JsonProperty("result_code")
	public void setResultCode(Integer resultCode) {
		this.resultCode = resultCode;
	}
	
	@Override
	public String toString(){
		return this.result+", "+this.resultCode+", "+this.message;
	}

}