
package io.chino.api.permission;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "permissions"
})
public class GetPermissionsResponse {

    @JsonProperty("permissions")
    private List<Permission> permissions = new ArrayList<>();

    /**
     *
     * @return
     *     The permissions
     */
    @JsonProperty("permissions")
    public List<Permission> getPermissions() {
        return permissions;
    }

    /**
     *
     * @param permissions
     *     The permissions
     */
    @JsonProperty("permissions")
    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString(){
        String s="Permissions:\n";
        for(Permission p : permissions){
            s+=p;
        }
        return s;
    }
}
