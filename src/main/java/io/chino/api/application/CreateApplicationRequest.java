package io.chino.api.application;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "name", "grant_type", "redirect_url" })
public class CreateApplicationRequest {

    @JsonProperty("name")
    private String name;
    @JsonProperty("grant_type")
    private String grantType;
    @JsonProperty("redirect_url")
    private String redirectUrl;

    public CreateApplicationRequest(){
    }

    public CreateApplicationRequest(String name, String grantType, String redirectUrl){
        setName(name);
        setGrantType(grantType);
        setRedirectUrl(redirectUrl);
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
    public void setName(String name) {
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
    public void setGrantType(String grantType) {
        if(grantType==null){
            throw new NullPointerException("grant_type");
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
    public void setRedirectUrl(String redirectUrl) {
        if(redirectUrl==null){
            throw new NullPointerException("redirect_url");
        }
        this.redirectUrl = redirectUrl;
    }
}
