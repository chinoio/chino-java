
package io.chino.api.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "user"
})
public class LoginResponse {

    @JsonProperty("user")
    private LoggedUser user;

    /**
     * 
     * @return
     *     The user
     */
    @JsonProperty("user")
    public LoggedUser getUser() {
        return user;
    }

    /**
     * 
     * @param user
     *     The user
     */
    @JsonProperty("user")
    public void setUser(LoggedUser user) {
        this.user = user;
    }

    public String toString(){
        String s="";
        s+="user: "+user;
        return s;
    }
}
