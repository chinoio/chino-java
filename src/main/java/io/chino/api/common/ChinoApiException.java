package io.chino.api.common;

import java.util.StringTokenizer;

public class ChinoApiException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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