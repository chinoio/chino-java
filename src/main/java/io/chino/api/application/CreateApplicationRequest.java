package io.chino.api.application;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "name",
    "grant_type",
    "redirect_url",
    "client_type"
})
public class CreateApplicationRequest {

    /**
     * The new Application's name.
     */
    @JsonProperty("name")
    private String name;
    /**
     * The new Application's grant_type - either "password" or "authentication_code".
     */
    @JsonProperty("grant_type")
    private String grantType;
    /**
     * The new Application's redirect_url.
     */
    @JsonProperty("redirect_url")
    private String redirectUrl;
    /**
     * The type of Application, either "public" or "confidential".
     */
    @JsonProperty("client_types")
    private String clientType;

    /**
     * Empty constructor used by {@link ObjectMapper} to create/map
     * JSON objects from/to this class.
     */
    private CreateApplicationRequest(){
    }

    /**
     * Create a new {@link ClientType#CONFIDENTIAL "confidential"} Application,
     * which can hold securely an "app_secret".
     * @param name the new Application's name
     * @param grantType The new Application's grant_type - either "password" or "authentication_code".
     * @param redirectUrl The new Application's redirect_url.
     */
    public CreateApplicationRequest(String name, String grantType, String redirectUrl){
        this(name, grantType, redirectUrl, ClientType.CONFIDENTIAL);
    }
    
    /**
     * Create a new Application.
     * @param name the new Application's name
     * @param grantType The new Application's grant_type - either "password" or "authentication_code".
     * @param redirectUrl The new Application's redirect_url.
     * @param clientType 
     */
    public CreateApplicationRequest(String name, String grantType, String redirectUrl, ClientType clientType) {
        setName(name);
        setGrantType(grantType);
        setRedirectUrl(redirectUrl);
        this.clientType = (clientType == ClientType.PUBLIC) ? "public" : "confidential";
    }

    /**
     *
     * @return The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     *            The name
     */
    @JsonProperty("name")
    public final void setName(String name) {
        if(name==null){
            throw new NullPointerException("name");
        }
        this.name = name;
    }

    /**
     *
     * @return The grantType
     */
    @JsonProperty("grant_type")
    public String getGrantType() {
        return grantType;
    }

    /**
     *
     * @param grantType
     *            The grantType
     */
    @JsonProperty("grant_type")
    public final void setGrantType(String grantType) {
        if(grantType==null){
            throw new NullPointerException("grant_type");
        } else if (!grantType.equals("password") && !grantType.equalsIgnoreCase("authentication_code")) {
            throw new IllegalArgumentException(("'grantType' must be either \"password\" or \"authentication_code\"."));
        }
        
        this.grantType = grantType;
    }

    /**
     *
     * @return The redirectUrl
     */
    @JsonProperty("redirect_url")
    public String getRedirectUrl() {
        return redirectUrl;
    }

    /**
     *
     * @param redirectUrl
     *            The redirectUrl
     */
    @JsonProperty("redirect_url")
    public final void setRedirectUrl(String redirectUrl) {
        if(redirectUrl==null){
            throw new NullPointerException("redirect_url");
        }
        this.redirectUrl = redirectUrl;
    }
}
