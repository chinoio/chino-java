package io.chino.api.application;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "app_secret",
        "grant_type",
        "app_name",
        "redirect_url",
        "app_id"
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

    public String toString(){
        String s="\n";
        s+="app_secret: " + appSecret;
        s+=",\ngrant_type: " + grantType;
        s+=",\napp_name: "+appName;
        s+=",\nredirect_url: " + redirectUrl;
        s+=",\napp_id: " + appId;
        s+="\n";
        return s;
    }
}
