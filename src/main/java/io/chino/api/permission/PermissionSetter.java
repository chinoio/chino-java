package io.chino.api.permission;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.chino.api.collection.Collection;
import io.chino.java.Permissions;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Utility class that allows to set Permissions over a resource in Chino.io.<br>
 *
 * Usage example:
 * <pre>
 *     new PermissionSetter()
 *          .manage(READ, UPDATE, DELETE)
 *          .authorize(READ, ADMIN)
 *          .manageOnCreatedDocuments(READ, LIST)
 *          .authorizeOnCreatedDocuments(READ, LIST)
 * </pre>
 *
 * Which represents the following JSON object:
 * <pre>
 *     {
 *         "manage" : [ "R", "U", "D" ],
 *         "authorize" : [ "R", "A" ],
 *         "created_document" : {
 *              "manage" : [ "R", "L" ],
 *              "authorize" : [ "R", "L" ]
 *         }
 *     }
 * </pre>
 * For more information about the Permission format, visit the
 * <a href="https://docs.chino.io/#permissions">Permissions</a> page on the Chino.io API docs.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "manage",
        "authorize",
        "created_document"
})
public class PermissionSetter {

    private HashSet<Permissions.Type> manage;
    private HashSet<Permissions.Type> authorize;
    private PermissionRule onCreatedDocuments;

    /**
     * Create a new, empty {@link PermissionSetter}.
     */
    public PermissionSetter() {
        manage = null;
        authorize = null;

        onCreatedDocuments = null;
    }

    /**
     * Create a new {@link PermissionSetter}
     *
     * @param rule a {@link PermissionRule} with the content of the "manage" and "authorize" sets
     */
    public PermissionSetter(@NotNull PermissionRule rule) {
        manage = new HashSet<>();
        authorize = new HashSet<>();
        mapRule(rule, authorize, manage);

        onCreatedDocuments = null;
    }

    /**
     * Create a new {@link PermissionSetter}
     *
     * @param onResource a {@link PermissionRule} with the content of the "manage" and "authorize" sets
     * @param onResource a {@link PermissionRule} with the content of the "created_document.manage"
     *                   and "created_document.authorize" sets
     */
    public PermissionSetter(@NotNull PermissionRule onResource, @NotNull PermissionRule onCreatedDocuments) {
        manage = new HashSet<>();
        authorize = new HashSet<>();
        mapRule(onResource, this.manage, this.authorize);

        this.onCreatedDocuments = new PermissionRule();
        mapRule(onCreatedDocuments, this.onCreatedDocuments.getManageTypes(), this.onCreatedDocuments.getAuthorizeTypes());
    }

    /**
     * Adds the specified permission types to the "manage" set.
     * If the set already contains some values, the new ones will be appended.
     * To reset the values, use {@link #PermissionSetter()} or {@link #setPermissions(PermissionRule)}
     *
     * @param types an array of {@link io.chino.java.Permissions.Type Permissions.Type}.
     *
     * @return this {@link PermissionSetter}
     */
    public PermissionSetter manage(Permissions.Type ... types) {
        if (this.manage == null) {
            this.manage = new HashSet<>();
        }
        this.manage.addAll(
                Arrays.asList(types)
        );
        return this;
    }

    /**
     * Adds the specified permission types to the "authorize" set.
     * If the set already contains some values, the new ones will be appended.
     * To reset the values, use {@link #PermissionSetter()} or {@link #setPermissions(PermissionRule)}
     *
     * @param types an array of {@link io.chino.java.Permissions.Type Permissions.Type}.
     *
     * @return this {@link PermissionSetter}
     */
    public PermissionSetter authorize(Permissions.Type ... types) {
        if (this.authorize == null) {
            this.authorize = new HashSet<>();
        }
        this.authorize.addAll(
                Arrays.asList(types)
        );
        return this;
    }

    /**
     * Adds the specified permission types to the "created_document.manage" set.
     * If the set already contains some values, the new ones will be appended.
     * To reset the values, use {@link #PermissionSetter()} or {@link #setPermissionsOnCreatedDocuments(PermissionRule)}
     *
     * @param types an array of {@link io.chino.java.Permissions.Type Permissions.Type}.
     *
     * @return this {@link PermissionSetter}
     */
    public PermissionSetter manageOnCreatedDocuments(Permissions.Type ... types) {
        onCreatedDocuments.setManage(types);
        return this;
    }

    /**
     * Adds the specified permission types to the "created_document.authorize" set.
     * If the set already contains some values, the new ones will be appended.
     * To reset the values, use {@link #PermissionSetter()} or {@link #setPermissionsOnCreatedDocuments(PermissionRule)}
     *
     * @param types an array of {@link io.chino.java.Permissions.Type Permissions.Type}.
     *
     * @return this {@link PermissionSetter}
     */
    public PermissionSetter authorizeOnCreatedDocuments(Permissions.Type ... types) {
        onCreatedDocuments.setAuthorize(types);
        return this;
    }

    /**
     * Replace the "manage" and "authorize" sets with values from a {@link PermissionRule}
     *
     * @param permissions a {@link PermissionRule} containing the new permissions
     */
    public void setPermissions(PermissionRule permissions) {
        this.manage = new HashSet<>(permissions.getManageTypes());
        this.authorize = new HashSet<>(permissions.getAuthorizeTypes());
    }

    /**
     * Replace the "created_document.manage" and "created_document.authorize" sets with
     * values from a {@link PermissionRule}
     *
     * @param permissions a {@link PermissionRule} containing the new permissions
     */
    public void setPermissionsOnCreatedDocuments(PermissionRule permissions) {
        onCreatedDocuments.manage = new HashSet<>(permissions.getManageTypes());
        onCreatedDocuments.authorize = new HashSet<>(permissions.getAuthorizeTypes());
    }

    public PermissionRule getPermissions() {
        PermissionRule rule = new PermissionRule();
        rule.setManage(
                manage.toArray(new Permissions.Type[0])
        );
        rule.setAuthorize(
                authorize.toArray(new Permissions.Type[0])
        );

        return rule;
    }


    private void mapRule(@NotNull PermissionRule rule, @NotNull HashSet<Permissions.Type> manageSet, @NotNull HashSet<Permissions.Type> authorizeSet) {
        manageSet.addAll(rule.getManageTypes());
        authorizeSet.addAll(rule.getAuthorizeTypes());
    }

    /* SERIALIZATION */

    @JsonProperty("manage")
    List<String> getManage() {
        LinkedList<String> l = new LinkedList<>();
        for (Permissions.Type t : manage) {
            l.add(t.toString());
        }
        return l;
    }

    @JsonProperty("manage")
    void setManage(List<String> manage) {
        for (String s : manage) {
            this.authorize.add(Permissions.Type.fromString(s));
        }
    }

    @JsonProperty("authorize")
    List<String> getAuthorize() {
        LinkedList<String> l = new LinkedList<>();
        for (Permissions.Type t : authorize) {
            l.add(t.toString());
        }
        return l;
    }

    @JsonProperty("authorize")
    void setAuthorize(List<String> authorize) {
        for (String s : authorize) {
            this.authorize.add(Permissions.Type.fromString(s));
        }
    }

    @JsonProperty("created_document")
    PermissionRule getPermissionsOnCreatedDocuments() {
        return onCreatedDocuments;
    }

    @JsonProperty("created_document")
    void setCreatedDocument(PermissionRule createdDocument) {
        this.onCreatedDocuments = createdDocument;
    }
}
