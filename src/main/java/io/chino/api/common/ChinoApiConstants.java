
package io.chino.api.common;



public class ChinoApiConstants {

	// schema and document field types
	public static final String DATA_TYPE_BOOLEAN="boolean";
	public static final String DATA_TYPE_INTEGER="integer";
	public static final String DATA_TYPE_FLOAT="float";
	public static final String DATA_TYPE_STRING="string";
	public static final String DATA_TYPE_DATE="date";
	public static final String DATA_TYPE_TIME="time";
	public static final String DATA_TYPE_DATETIME="datetime";
	public static final String DATA_TYPE_BASE64ENCODED="base64";
	public static final String DATA_TYPE_BLOB="blob";
	
	// Query standard limit (also if not specified it's 100)
	public static final Integer QUERY_DEFAULT_LIMIT=100;
	
	// Response codes
	public static final Integer RESPONSE_OK = 200;
	public static final Integer RESPONSE_OK_CREATED = 201;
	public static final Integer RESPONSE_CLIENT_ERROR_BAD_REQUEST = 400; //The server cannot or will not process the request due to something that is perceived to be a client error (e.g., malformed request syntax, invalid request message framing, or deceptive request routing).[14]
	public static final Integer RESPONSE_CLIENT_ERROR_NOT_AUTHORIZED = 401; //Similar to 403 Forbidden, but specifically for use when authentication is required and has failed or has not yet been provided. The response must include a WWW-Authenticate header field containing a challenge applicable to the requested resource. See Basic access authentication and Digest access authentication.
	public static final Integer RESPONSE_CLIENT_ERROR_FORBIDDEN = 403; //The request was a valid request, but the server is refusing to respond to it. Unlike a 401 Unauthorized response, authenticating will make no difference
	public static final Integer RESPONSE_CLIENT_ERROR_METHOD_NOT_ALLOWED = 405; // Method Not Allowed
	public static final Integer RESPONSE_CLIENT_ERROR_NOT_FOUND = 404; //The requested resource could not be found but may be available again in the future. Subsequent requests by the client are permissible.
	public static final Integer RESPONSE_SERVER_ERROR_INTERNAL_ERROR = 500;
	public static final Integer RESPONSE_SERVER_ERROR_TEMPORARY_UNAVAILABLE = 503;
	
	// Search filters and operators
    public static final String FILTER_OPERATOR_EQUAL = "eq";
    public static final String FILTER_OPERATOR_NOT = "not";
    public static final String FILTER_OPERATOR_IN = "in";
    public static final String FILTER_OPERATOR_NOT_IN = "not in";
    public static final String FILTER_OPERATOR_WILDCARD = "wildcard";
    public static final String FILTER_OPERATOR_GREATER_THAN = "gt";
    public static final String FILTER_OPERATOR_LOWER_THAN = "lt";
    public static final String FILTER_OPERATOR_GREATER_OR_EQUAL= "gte";
    public static final String FILTER_OPERATOR_LOWER_OR_EQUAL= "lte";
    
    public static final String FILTER_TYPE_AND = "and";
    public static final String FILTER_TYPE_OR = "or";
    
	public static final String SORT_OPTION_ASC="asc";
	public static final String SORT_OPTION_DESC="desc";
	

}
