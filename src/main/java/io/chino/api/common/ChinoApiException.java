package io.chino.api.common;

import java.util.StringTokenizer;

/**
 * Checked exception that is thrown whenever an error is returned by Chino.io
 * as a response to an API call
 */
public class ChinoApiException extends Exception {

	private ErrorResponse errorResponse;

	// Parameterless Constructor
	public ChinoApiException() {
	}

	// Constructor that accepts a message
	public ChinoApiException(String message) {
		super(message);
	}

	public ChinoApiException(ErrorResponse error) {
		super(error.toString());
		this.errorResponse=error;
	}
	
	public String getCode(){
		StringTokenizer st=new StringTokenizer(this.getMessage()," ,");
		st.nextElement();
		return (String)st.nextElement();
	}
	
	public ErrorResponse getErrorResponse(){
		return errorResponse;
	}
	
}