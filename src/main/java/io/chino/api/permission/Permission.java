
package io.chino.api.permission;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.chino.java.Permissions;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Contains information about permissions that are active on a Chino.io resource.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "access",
        "parent_id",
        "resource_id",
        "resource_type",
        "permission"
})
public class Permission implements PermissionsContainer {
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

    /**
     * Get the access level this {@link Permission} refers to.
     * May be "Structure" or "Data".
     *
     * @return either "Structure" (permission is applied on the resource's metadata)
     *      or "Data" (permission is applied on the resource's content)
     */
    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    /**
     * Get the ID on Chino.io of the parent resource of the object of this {@link Permission}
     *
     * @see #getResourceId() Get this resource's ID instead
     *
     * @return the Chino.io ID of the parent object. If the resource has no parent, returns {@code null}
     */
    @Nullable
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    /**
     * Get the ID on Chino.io of the resource of this {@link Permission}
     *
     * @return the Chino.io ID of the object this {@link Permission} applies to.
     */

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * Get the Chino.io resource type of the object of this {@link Permission}
     *
     * @return the name of a Chino.io resource (e.g. Schema, Document, Group...) as a {@link String}
     */
    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    /**
     * Get the permissions that are active on this object.
     *
     *
     * @return a {@link HashMap} that may contain up to three lists of Permissions:
     *  <ul>
     *      <li>
     *          <code>"manage"</code> Permissions, i.e. permissions that are active for the user itself.
     *          Cast to a {@link java.util.List List&lt;String&gt;}.
     *      </li>
     *      <li>
     *          <code>"authorize"</code> Permissions, i.e. permissions the user can grant to third party users.
     *          Cast to a {@link java.util.List List&lt;String&gt;}.
     *      </li>
     *      <li>
     *          <code>"created_document"</code> Permissions, which defines the default permissions on children
     *          resources in Repositories, Schemas and UserSchemas. Contains a <code>"manage"</code> and a
     *          <code>"authorize"</code> list itself.
     *      </li>
     *  </ul>
     */
    public HashMap getPermission() {
        return permission;
    }

    @Override
    public List<Permissions.Type> getManagePermissions() {
        LinkedList<Permissions.Type> perms = new LinkedList<>();
        List<String> stringPerms;
        if (permission.containsKey("Manage")) {
            stringPerms = (List<String>) permission.get("Manage");
            for (String s : stringPerms) {
                perms.add(
                        Permissions.Type.fromString(s)
                );
            }
        }
        return perms;
    }

    @Override
    public List<Permissions.Type> getAuthorizePermissions() {
        LinkedList<Permissions.Type> perms = new LinkedList<>();
        List<String> stringPerms;
        if (permission.containsKey("Authorize")) {
            stringPerms = (List<String>) permission.get("Authorize");
            for (String s : stringPerms) {
                perms.add(
                        Permissions.Type.fromString(s)
                );
            }
        }
        return perms;
    }

    @Override
    public List<Permissions.Type> getManagePermissionsOnCreatedDocuments() {
        LinkedList<Permissions.Type> perms = new LinkedList<>();
        HashMap ocd;
        LinkedList<String> stringPerms;
        if (permission.containsKey("created_document")) {
            ocd = (HashMap) permission.get("created_document");
            if (ocd.containsKey("Manage")) {
                stringPerms = (LinkedList<String>) ocd.get("Manage");
                for (String s : stringPerms) {
                    perms.add(
                            Permissions.Type.fromString(s)
                    );
                }
            }
        }
        return perms;
    }

    @Override
    public List<Permissions.Type> getAuthorizePermissionsOnCreatedDocuments() {
        LinkedList<Permissions.Type> perms = new LinkedList<>();
        HashMap ocd;
        LinkedList<String> stringPerms;
        if (permission.containsKey("created_document")) {
            ocd = (HashMap) permission.get("created_document");
            if (ocd.containsKey("Authorize")) {
                stringPerms = (LinkedList<String>) ocd.get("Authorize");
                for (String s : stringPerms) {
                    perms.add(
                            Permissions.Type.fromString(s)
                    );
                }
            }
        }
        return perms;
    }

    public void setPermission(HashMap permission) {
        this.permission = permission;
    }

    @Override
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
