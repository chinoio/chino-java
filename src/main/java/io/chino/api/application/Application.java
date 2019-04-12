package io.chino.api.application;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Objects;

/**
 * An Application of Chino.io
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "app_secret",
        "grant_type",
        "app_name",
        "redirect_url",
        "app_id",
        "client_type"
})
public class Application {

    @JsonProperty("app_secret")
    private String appSecret;
    @JsonProperty("grant_type")
    private String grantType;
    @JsonProperty("app_name")
    private String appName;
    @JsonProperty("redirect_url")
    private String redirectUrl;
    @JsonProperty("app_id")
    private String appId;
    @JsonProperty("client_type")
    private String clientType;
    

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }
    
    @Override
    public String toString(){
        String s="\n";
        s+=",\napp_name: "+appName;
        s+=",\ngrant_type: " + grantType;
        s+=",\napp_id: " + appId;
        s+="app_secret: " + appSecret;
        s+=",\nredirect_url: " + redirectUrl;
        s+="\n";
        return s;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this ||
                obj instanceof Application
                && Objects.equals(((Application) obj).appId, this.appId)
                && Objects.equals(((Application) obj).grantType, this.grantType)
                && Objects.equals(((Application) obj).appName, this.appName)
                && Objects.equals(((Application) obj).redirectUrl, this.redirectUrl)
                && Objects.equals(((Application) obj).clientType, this.clientType);
    }
}
