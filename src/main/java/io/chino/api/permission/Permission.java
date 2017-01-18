
package io.chino.api.permission;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "access",
        "parent_id",
        "resource_id",
        "resource_type",
        "permission"
})
public class Permission {
    @JsonProperty("access")
    private String access;
    @JsonProperty("parent_id")
    private String parentId;
    @JsonProperty("resource_id")
    private String resourceId;
    @JsonProperty("resource_type")
    private String resourceType;
    @JsonProperty("permission")
    private HashMap permission;

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public HashMap getPermission() {
        return permission;
    }

    public void setPermission(HashMap permission) {
        this.permission = permission;
    }

    public String toString(){
        String s="Permission -";
        s+="\naccess: "+ getAccess();
        s+=",\nparentId: "+parentId;
        s+=",\nresourceId: "+resourceId;
        s+=",\nresourceType: "+ getResourceType();
        s+=",\npermission: "+ getPermission().toString();
        s+="\n";
        return s;

    }
}
