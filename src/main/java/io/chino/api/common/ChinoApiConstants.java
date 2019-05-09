
package io.chino.api.common;


/**
 * Definition of constants that can be used to replace constant values in API calls
 */
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

    /**
     * Default value of the User-Agent header
     */
	public static final String USER_AGENT = String.format("okhttp/3 chino-java/%s", BuildConfig.VERSION);
	
	/**
     * Query standard limit, default value is 100.
     */
	public static final Integer QUERY_DEFAULT_LIMIT = 100;

    /**
     * The default number of search results that are read from Chino.
     */
    public static final int SEARCH_RESULTS_LIMIT = 10;
	
	/** 
     * Server response 200
     */
	public static final Integer RESPONSE_OK = 200;

    /**
     * Server response 201
     */
	public static final Integer RESPONSE_OK_CREATED = 201;

    /**
     * Server response 400. The server cannot or will not process the request
     * due to something that is perceived to be a client error
     * (e.g., malformed request syntax, invalid request message framing,
     * or deceptive request routing).
     */
	public static final Integer RESPONSE_CLIENT_ERROR_BAD_REQUEST = 400;

    /**
     * Server response 401. Similar to {@link #RESPONSE_CLIENT_ERROR_FORBIDDEN 403 Forbidden}, but specifically for
     * use when authentication is required and has failed or has not
     * yet been provided. The response must include a WWW-Authenticate
     * header field containing a challenge applicable to the requested resource.
     * See Basic access authentication and Digest access authentication.
     */
	public static final Integer RESPONSE_CLIENT_ERROR_NOT_AUTHORIZED = 401;

	/**
     * Server response 403. The request was a valid request, but the server
     * is refusing to respond to it. Unlike a
     * {@link #RESPONSE_CLIENT_ERROR_NOT_AUTHORIZED 401 Unauthorized}
     * response, authenticating will make no difference
     */
	public static final Integer RESPONSE_CLIENT_ERROR_FORBIDDEN = 403;

    /**
     * Server response 405. Method Not Allowed
     */
	public static final Integer RESPONSE_CLIENT_ERROR_METHOD_NOT_ALLOWED = 405;

    /**
     * Server response 404. The requested resource could not be found
     * but may be available again in the future. Subsequent requests by the
     * client are permissible.
     */
	public static final Integer RESPONSE_CLIENT_ERROR_NOT_FOUND = 404;

    /**
     * Server response 500
     */
	public static final Integer RESPONSE_SERVER_ERROR_INTERNAL_ERROR = 500;

    /**
     * Server response 503
     */
	public static final Integer RESPONSE_SERVER_ERROR_TEMPORARY_UNAVAILABLE = 503;
	
	// Search filters and operators - DEPRECATED! these values refer to the deprecated Search API
    @Deprecated
    public static final String FILTER_OPERATOR_EQUAL = "eq";
    @Deprecated
    public static final String FILTER_OPERATOR_NOT = "not";
    @Deprecated
    public static final String FILTER_OPERATOR_IN = "in";
    @Deprecated
    public static final String FILTER_OPERATOR_NOT_IN = "not in";
    @Deprecated
    public static final String FILTER_OPERATOR_WILDCARD = "wildcard";
    @Deprecated
    public static final String FILTER_OPERATOR_GREATER_THAN = "gt";
    @Deprecated
    public static final String FILTER_OPERATOR_LOWER_THAN = "lt";
    @Deprecated
    public static final String FILTER_OPERATOR_GREATER_OR_EQUAL= "gte";
    @Deprecated
    public static final String FILTER_OPERATOR_LOWER_OR_EQUAL= "lte";

    @Deprecated
    public static final String FILTER_TYPE_AND = "and";
    @Deprecated
    public static final String FILTER_TYPE_OR = "or";

    @Deprecated
	public static final String SORT_OPTION_ASC="asc";
	@Deprecated
	public static final String SORT_OPTION_DESC="desc";
}
