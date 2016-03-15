
package io.chino.api.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "logout"
})
public class LogoutResponse {

    @JsonProperty("logout")
    private Logout logout;

    /**
     * 
     * @return
     *     The logout
     */
    @JsonProperty("logout")
    public Logout getLogout() {
        return logout;
    }

    /**
     * 
     * @param logout
     *     The logout
     */
    @JsonProperty("logout")
    public void setLogout(Logout logout) {
        this.logout = logout;
    }

    public String toString(){
        String s="";
        s+="Logout: "+logout;
        return s;
    }
}
