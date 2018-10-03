
package io.chino.api.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "username",
    "password",
    "attributes"
})
public class CreateUserRequest {

    @JsonProperty("username")
    private String username;
    @JsonProperty("password")
    private String password;
    @JsonProperty("attributes")
    private HashMap attributes;

    public CreateUserRequest(){

    }

    public CreateUserRequest(String username, String password, HashMap attributes){
        setUsername(username);
        setPassword(password);
        setAttributes(attributes);
    }

    /**
     * 
     * @return
     *     The username
     */
    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    /**
     * 
     * @param username
     *     The username
     */
    @JsonProperty("username")
    public void setUsername(String username) {
        if(username == null){
            throw new NullPointerException("username");
        }
        this.username = username;
    }

    /**
     * 
     * @return
     *     The password
     */
    @JsonProperty("password")
    public String getPassword() {
        return password;
    }

    /**
     * 
     * @param password
     *     The password
     */
    @JsonProperty("password")
    public void setPassword(String password) {
        if(password == null){
            throw new NullPointerException("password");
        }
        this.password = password;
    }

    /**
     * 
     * @return
     *     The attributes
     */
    @JsonProperty("attributes")
    public HashMap getAttributes() {
        return attributes;
    }

    /**
     * 
     * @param attributes
     *     The attributes
     */
    @JsonProperty("attributes")
    public void setAttributes(HashMap attributes) {
        if(attributes == null){
            throw new NullPointerException("attributes");
        }
        this.attributes = attributes;
    }

}
