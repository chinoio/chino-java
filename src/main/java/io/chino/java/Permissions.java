package io.chino.java;

import com.fasterxml.jackson.databind.JsonNode;
import io.chino.api.common.ChinoApiConstants;
import io.chino.api.common.ChinoApiException;
import io.chino.api.document.Document;
import io.chino.api.group.Group;
import io.chino.api.permission.*;
import io.chino.api.user.User;
import io.chino.api.common.Pair;

import java.io.IOException;

/**
 * This class is used to manage access Permissions over Chino.io resources.
 * For instructions about how to use the Permissions system, visit the
 * <a href="https://docs.chino.io/#permissions">Permissions</a> page on
 * the Chino.io API docs.
 *
 * @see PermissionsRequest
 * @see PermissionsRequestBuilder
 * @see PermissionSetter
 */
public class Permissions extends ChinoBaseAPI {

    /**
     * The default constructor used by all {@link ChinoBaseAPI} subclasses
     *
     * @param baseApiUrl      the base URL of the Chino.io API. For testing, use:<br>
     *                        {@code https://api.test.chino.io/v1/}
     * @param parentApiClient the instance of {@link ChinoAPI} that created this object
     */
    public Permissions(String baseApiUrl, ChinoAPI parentApiClient) {
        super(baseApiUrl, parentApiClient);
    }


    /* NEW PERMISSIONS INTERFACE */

    /**
     * Create a new {@link PermissionsRequest} that will grant new Permissions over resources in Chino.io.
     *
     * @return a {@link PermissionsRequestBuilder} that can be used to specify the subject, the target resource(s)
     * and the new Permissions that will be granted.
     */
    public PermissionsRequestBuilder grant() {
        return new PermissionsRequestBuilder(Action.GRANT, this);
    }

    /**
     * Create a new {@link PermissionsRequest} that will revoke existing Permissions over resources in Chino.io.
     *
     * @return a {@link PermissionsRequestBuilder} that can be used to specify the subject, the target resource(s)
     * and the new Permissions that will be revoked.
     */
    public PermissionsRequestBuilder revoke() {
        return new PermissionsRequestBuilder(Action.REVOKE, this);
    }

    /**
     * Execute a {@link PermissionsRequest}
     *
     * @param request a {@link PermissionsRequest}
     *
     * @return a String with the result of the operation
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public String executeRequest(PermissionsRequest request) throws IOException, ChinoApiException {
        postResource(request.getUrlPath(), request.getBody());
        return SUCCESS_MSG;
    }

    /**
     * Read all the Permissions set on every resource belonging to a logged {@link User User}.
     * Doesn't work if invoked by a {@link ChinoAPI#ChinoAPI(String, String, String) customer client}
     * (i.e. authenticated with customerId and customerKey)
     *
     * @return A {@link GetPermissionsResponse} that wraps a list of {@link io.chino.api.permission.Permission Permissions}
     *
     * @throws IOException data processing error
     * @throws ChinoApiException either a server error or this method was invoked by a customer client
     */
    public GetPermissionsResponse readPermissions() throws IOException, ChinoApiException {
        JsonNode data = getResource("/perms", 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return mapper.convertValue(data, GetPermissionsResponse.class);

        return null;
    }

    /**
     * Reads all Permissions of a given {@link User}. In the response are omitted all
     * the Permissions on Documents, because they would generate too many results.<br>
     * <br>
     * Customers can always perform this call, while {@link User Users} can only
     * see their own Permissions.
     *
     * @param user a {@link User}
     *
     * @return A {@link GetPermissionsResponse} that wraps a list of {@link io.chino.api.permission.Permission Permissions}
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error, or the User is unauthorized to see these permissions.
     */
    public GetPermissionsResponse readPermissions(User user) throws IOException, ChinoApiException {
        return readPermissionsOfaUser(user.getUserId());
    }

    /**
     * Reads all Permissions of a given {@link Group}. In the response are omitted all
     * the Permissions on Documents, because they would generate too many results.<br>
     * <br>
     * Customers can always perform this call, while {@link User Users} can only
     * specify the ID of a {@link Group} they belong to.
     *
     * @param group a Chino.io {@link Group}
     *
     * @return A {@link GetPermissionsResponse} that wraps a list of {@link io.chino.api.permission.Permission Permissions}
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error, or the User doesn't belong to the specified Group.
     */
    public GetPermissionsResponse readPermissions(Group group) throws IOException, ChinoApiException {
        return readPermissionsOfaGroup(group.getGroupId());
    }

    /**
     * Reads Permissions that are set on a single {@link io.chino.api.document.Document Document}.
     * This call only returns Permissions that were specifically set on the
     * {@link io.chino.api.document.Document Document} using
     * {@link #permissionsOnaResource(String, String, String, String, String, PermissionRule) permissionsOnaResource(...)}<br>
     * <br>
     * Customers can always perform this call, while {@link User Users} must have
     * {@code R} Permission on the document.
     *
     * @param document a Chino.io {@link io.chino.api.document.Document Document}
     *
     * @return A {@link GetPermissionsResponse} that wraps a list of {@link io.chino.api.permission.Permission Permissions}
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public GetPermissionsResponse readPermissionsOn(Document document) throws IOException, ChinoApiException {
        return readPermissionsOnaDocument(document.getDocumentId());
    }

    /**
     * Reads Permissions that are set on a single {@link io.chino.api.document.Document Document}.
     * This call only returns Permissions that were specifically set on the
     * {@link io.chino.api.document.Document Document} using
     * {@link #permissionsOnaResource(String, String, String, String, String, PermissionRule) permissionsOnaResource(...)}<br>
     * <br>
     * Customers can always perform this call, while {@link User Users} must have
     * {@code R} Permission on the document.
     *
     * @param documentId the id of a {@link io.chino.api.document.Document Document} on Chino.io
     *
     * @return A {@link GetPermissionsResponse} that wraps a list of {@link io.chino.api.permission.Permission Permissions}
     *
     * @see #readPermissionsOn(Document)
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public GetPermissionsResponse readPermissionsOnaDocument(String documentId) throws IOException, ChinoApiException {
        checkNotNull(documentId, "document_id");
        JsonNode data = getResource("/perms/documents/"+documentId, 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null) {
            return mapper.convertValue(data, GetPermissionsResponse.class);
        }
        return null;
    }


    /**
     * Reads all Permissions of a given {@link User User}. In the response are omitted all
     * the Permissions on Documents, because they would generate too many results.<br>
     * <br>
     * Customers can always perform this call, while {@link User Users} can only
     * see their own Permissions.
     *
     * @param userId the id of a {@link User User} on Chino.io
     *
     * @return A {@link GetPermissionsResponse} that wraps a list of {@link io.chino.api.permission.Permission Permissions}
     *
     * @see #readPermissions(User)
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error, or the User is unauthorized to see these permissions.
     */
    public GetPermissionsResponse readPermissionsOfaUser(String userId) throws IOException, ChinoApiException{
        checkNotNull(userId, "user_id");
        JsonNode data = getResource("/perms/users/"+userId, 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return mapper.convertValue(data, GetPermissionsResponse.class);

        return null;
    }


    /**
     * Reads all Permissions of a given {@link Group Group}. In the response are omitted all
     * the Permissions on Documents, because they would generate too many results.<br>
     * <br>
     * Customers can always perform this call, while {@link User Users} can only
     * specify the ID of a {@link Group Group} they belong to.
     *
     * @param groupId the ID of a {@link Group Group} on Chino.io
     *
     * @return A {@link GetPermissionsResponse} that wraps a list of {@link io.chino.api.permission.Permission Permissions}
     *
     * @see #readPermissions(Group)
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error, or the User doesn't belong to the specified Group.
     */
    public GetPermissionsResponse readPermissionsOfaGroup(String groupId) throws IOException, ChinoApiException{
        checkNotNull(groupId, "group_id");
        JsonNode data = getResource("/perms/groups/"+groupId, 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return mapper.convertValue(data, GetPermissionsResponse.class);

        return null;
    }

    /* MACROS */

    /**
     *  The types of Permission that can be granted/revoked on a resource.
     */
    public enum Type {
        /** Create resources - only applies to resource types (e.g. all documents) */
        CREATE,

        /** Read resources - applies to a specific resource or to a type (e.g. all documents) */
        READ,

        /** Update resources - applies to a specific resource or to a type (e.g. all documents) */
        UPDATE,

        /** Delete resources - applies to a specific resource or to a type (e.g. all documents) */
        DELETE,

        /** List resources - only applies to resource types (e.g. all documents) */
        LIST,

        /**
         * Administer permissions - can only be set in the "authorize" Permissions list of a user.
         * Enables the user to add/remove permissions he can "manage" from/to other users' "authorize" list.
         */
        ADMIN,

        /**
         * Search content - only applies to {@link io.chino.api.schema.Schema Schemas} and
         * {@link io.chino.api.userschema.UserSchema UserSchemas}, with the following rules: (click on the constant to see full Javadoc)<br>
         * <ul>
         *     <li>
         *         On a Schema, allows to search <b>all the {@link io.chino.api.document.Document Documents}
         *         he has {@link #READ} permission over</b>.
         *     </li>
         *     <li>
         *         On a UserSchema, allows to search <b>every user</b> in the schema.
         *     </li>
         * </ul>
         */
        SEARCH;

        @Override
        public String toString() {
            return name().substring(0, 1).toUpperCase();
        }

        public static Type fromString(String type) {
            String stringValue = type;
            for (Type value : values()) {
                if (value.toString().equals(stringValue)) {
                    return value;
                }
            }
            throw new IllegalArgumentException(type + " is not a valid permission type string.");
        }
    }

    /**
     *  Actions that modify Permissions of a {@link Subject} over one or multiple resources.
     */
    public enum Action {
        /** {@code action}: grant permission */
        GRANT,
        /** {@code action}: revoke permission */
        REVOKE;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

    /**
     * The resources of Chino.io. Access to resources is regulated with Permissions.
     */
    public enum ResourceType {
        /** {@code resource}: apply Permissions changes to a {@link io.chino.api.repository.Repository Repository} */
        REPOSITORY,
        /** {@code resource}: apply Permissions changes to a {@link io.chino.api.schema.Schema Schema} */
        SCHEMA,
        /** {@code resource}: apply Permissions changes to a {@link io.chino.api.document.Document Document} */
        DOCUMENT,
        /** {@code resource}: apply Permissions changes to a {@link io.chino.api.userschema.UserSchema UserSchema} */
        USER_SCHEMA,
        /** {@code resource}: apply Permissions changes to a {@link User}'s data */
        USER,
        /** {@code resource}: apply Permissions changes to all members of {@link Group} */
        GROUP;

        @Override
        public String toString() {
            if (this == REPOSITORY)
                return "repositories";
            return this.name().toLowerCase() + "s";
        }

        public static ResourceType fromString(String string) {
            string = string.toUpperCase();
            for (ResourceType type : values()) {
                String singular = string.toUpperCase()
                        .replace("REPOSITORIES", "REPOSITORY")
                        .replaceAll("[A-Z]*S$", "");
                if (string.equals(type.name()) || singular.equals(type.name())) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Invalid Resource Type: " + string);
        }
    }

    /**
     * Entities that can be granted or revoked Permissions.
     */
    public enum Subject {
        /** {@code subject}: change the Permissions that a {@link User} has on the specified resource. */
        USER,
        /**
         * {@code subject}: change the Permissions that all members of {@link Group} have
         *                  on the specified resource.
         */
        GROUP;

        @Override
        public String toString() {
            return this.name().toLowerCase() + "s";
        }
    }





    /* OLD PERMISSIONS INTERFACE */

    /**
     * Read all the Permissions set on every resource belonging to a logged {@link User User}.
     * Doesn't work if invoked by a {@link ChinoAPI#ChinoAPI(String, String, String) customer client}
     * (i.e. authenticated with customerId and customerKey).<br>
     *     <br>
     * WARNING: this method will be removed in a future version. Please use {@link #readPermissions()} instead.
     *
     * @param offset page offset of the results.
     * @param limit the max amount of results to be returned
     *
     * @return A {@link GetPermissionsResponse} that wraps a list of {@link io.chino.api.permission.Permission Permissions}
     *
     * @throws IOException data processing error
     * @throws ChinoApiException either a server error or this method was invoked by a customer client
     */
    @Deprecated
    public GetPermissionsResponse readPermissions(int offset, int limit) throws IOException, ChinoApiException {
        JsonNode data = getResource("/perms", offset, limit);
        if(data!=null)
            return mapper.convertValue(data, GetPermissionsResponse.class);

        return null;
    }

    /**
     * Reads Permissions that are set on a single {@link io.chino.api.document.Document Document}.<br>
     * <br>
     * Customers can always perform this call, while {@link User Users} must have
     * {@code R} Permission on the document.<br>
     *     <br>
     * WARNING: this method will be removed in a future version. Please use {@link #readPermissionsOn(Document)}
     * or {@link #readPermissionsOnaDocument(String)} instead.
     *
     * @param documentId the id of a {@link io.chino.api.document.Document} on Chino.io
     * @param offset page offset of the results. - <b>DEPRECATED</b>: this parameter is ignored by Chino.io API for this call
     * @param limit the max amount of results to be returned - <b>DEPRECATED</b>: this parameter is ignored by Chino.io API for this call
     *
     * @return A {@link GetPermissionsResponse} that wraps a list of {@link io.chino.api.permission.Permission Permissions}
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    @Deprecated
    public GetPermissionsResponse readPermissionsOnaDocument(String documentId, int offset, int limit) throws IOException, ChinoApiException {
        checkNotNull(documentId, "document_id");
        JsonNode data = getResource("/perms/documents/"+documentId, offset, limit);
        if(data!=null) {
            return mapper.convertValue(data, GetPermissionsResponse.class);
        }
        return null;
    }

    /**
     * Reads all Permissions of a given {@link User User}. In the response are omitted all
     * the Permissions on Documents, because they would generate too many results.<br>
     * <br>
     * Customers can always perform this call, while {@link User Users} can only
     * see their own Permissions.<br>
     *     <br>
     * WARNING: this method will be removed in a future version. Please use {@link #readPermissions(User)} or
     * {@link #readPermissionsOfaUser(String)} instead.
     *
     * @param userId the id of a {@link User User} on Chino.io
     * @param offset page offset of the results - <b>DEPRECATED</b>: this parameter is ignored by Chino.io API for this call
     * @param limit the max amount of results to be returned - <b>DEPRECATED</b>: this parameter is ignored by Chino.io API for this call
     *
     * @return A {@link GetPermissionsResponse} that wraps a list of {@link io.chino.api.permission.Permission Permissions}
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error, or the User is unauthorized to see these permissions.
     */
    @Deprecated
    public GetPermissionsResponse readPermissionsOfaUser(String userId, int offset, int limit) throws IOException, ChinoApiException{
        checkNotNull(userId, "user_id");
        JsonNode data = getResource("/perms/users/"+userId, offset, limit);
        if(data!=null)
            return mapper.convertValue(data, GetPermissionsResponse.class);

        return null;
    }

    /**
     * Reads all Permissions of a given {@link Group Group}. In the response are omitted all
     * the Permissions on Documents, because they would generate too many results.<br>
     * <br>
     * Customers can always perform this call, while {@link User Users} can only
     * specify the ID of a {@link Group Group} they belong to.<br>
     *     <br>
     * WARNING: this method will be removed in a future version. Please use {@link #readPermissions(Group)} or
     * {@link #readPermissionsOfaGroup(String)} instead.
     *
     * @param groupId the ID of a {@link Group Group} on Chino.io
     * @param offset page offset of the results. - <b>DEPRECATED</b>: this parameter is ignored by Chino.io API for this call
     * @param limit the max amount of results to be returned - <b>DEPRECATED</b>: this parameter is ignored by Chino.io API for this call
     *
     * @return A {@link GetPermissionsResponse} that wraps a list of {@link io.chino.api.permission.Permission Permissions}
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error, or the User doesn't belong to the specified Group.
     */
    @Deprecated
    public GetPermissionsResponse readPermissionsOfaGroup(String groupId, int offset, int limit) throws IOException, ChinoApiException{
        checkNotNull(groupId, "group_id");
        JsonNode data = getResource("/perms/groups/"+groupId, offset, limit);
        if(data!=null)
            return mapper.convertValue(data, GetPermissionsResponse.class);

        return null;
    }

    /**
     * Set {@link io.chino.api.permission.Permission Permissions} on all the resources of a specified type.<br>
     * Available resource types for this method are "GROUPS", "USER_SCHEMAS" and "REPOSITORIES".
     *
     * @param action either "GRANT" or "REVOKE"
     * @param resourceType the type of Resource where the new Permissions will be set.
     *                     Can be one of "GROUPS", "USER_SCHEMAS" or "REPOSITORIES"
     * @param subjectType the type of Subject that will get the new Permissions.
     * @param subjectId the id of the Subject on Chino.io
     * @param permissionRule a {@link PermissionRule} that specifies the new Permissions for the Subject
     *
     * @return a String with the result of the operation
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    @Deprecated
    public String permissionsOnResources(String action, String resourceType, String subjectType, String subjectId, PermissionRule permissionRule) throws IOException, ChinoApiException {
        checkNotNull(action, "action");
        checkNotNull(resourceType, "resource_type");
        checkNotNull(subjectType, "subject_type");
        checkNotNull(subjectId, "subject_id");
        checkNotNull(permissionRule, "permission_rule");
        postResource("/perms/"+action+"/"+resourceType+"/"+subjectType+"/"+subjectId, permissionRule);
        return SUCCESS_MSG;
    }

    /**
     * Set {@link io.chino.api.permission.Permission Permissions} on all the resources of a specified type.<br>
     * Available resource types for this method are "GROUPS", "USER_SCHEMAS" and "REPOSITORIES".
     *
     * @param action either "GRANT" or "REVOKE"
     * @param resourceType the type of Resource where the new Permissions will be set.
     * @param resourceId the id of the Resource on Chino.io
     * @param subjectType the type of Subject that will get the new Permissions.
     * @param subjectId the id of the Subject on Chino.io
     * @param permissionRule a {@link PermissionRule} that specifies the new Permissions for the Subject
     *
     * @return a String with the result of the operation
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    @Deprecated
    public String permissionsOnaResource(String action, String resourceType, String resourceId, String subjectType, String subjectId, PermissionRule permissionRule) throws IOException, ChinoApiException {
        checkNotNull(action, "action");
        checkNotNull(resourceType, "resource_type");
        checkNotNull(resourceId, "resource_id");
        checkNotNull(subjectType, "subject_type");
        checkNotNull(subjectId, "subject_id");
        checkNotNull(permissionRule, "permission_rule");
        postResource("/perms/"+action+"/"+resourceType+"/"+resourceId+"/"+subjectType+"/"+subjectId, permissionRule);
        return SUCCESS_MSG;
    }

    /**
     * Set {@link io.chino.api.permission.Permission Permissions} on all the children of a resource type.<br>
     * Only "USER_SCHEMAS", "SCHEMAS" or "REPOSITORIES" have children: <br>
     *
     * <ul>
     *     <li><b>UserSchemas</b> -&gt; Users</li>
     *     <li><b>Schemas</b> -&gt; Documents</li>
     *     <li><b>Repositories</b> -&gt; Schemas</li>
     * </ul>
     *
     * @param action either "GRANT" or "REVOKE"
     * @param resourceType One of "USER_SCHEMAS", "SCHEMAS" or "REPOSITORIES"
     * @param resourceId the id of the Resource on Chino.io
     * @param resourceChildren the child Resource of the specified Resource type.
     * @param subjectType the type of Subject that will get the new Permissions.
     * @param subjectId the id of the Subject on Chino.io
     * @param permissionRule a {@link PermissionRule} that specifies the new Permissions for the Subject
     *
     * @return a String with the result of the operation
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    @Deprecated
    public String permissionsOnResourceChildren(String action, String resourceType, String resourceId, String resourceChildren, String subjectType, String subjectId, PermissionRule permissionRule) throws IOException, ChinoApiException {
        checkNotNull(
                new Pair<>(action, "action"),
                new Pair<>(resourceType, "resource_type"),
                new Pair<>(resourceId, "resource_id"),
                new Pair<>(resourceChildren, "resource_children"),
                new Pair<>(subjectType, "subject_type"),
                new Pair<>(subjectId, "subject_id"),
                new Pair<>(permissionRule, "permission_rule_created_document")
        );
        checkNotNull(permissionRule, "permission_rule");
        postResource("/perms/"+action+"/"+resourceType+"/"+resourceId+"/"+resourceChildren+"/"+subjectType+"/"+subjectId, permissionRule);
        return SUCCESS_MSG;
    }

    /**
     * This method is deprecated and will be removed in the next release of the SDK. Please use the new Permissions
     * interface
     */
    @Deprecated
    public String permissionsOnResourceChildren(String action, String resourceType, String resourceId, String resourceChildren, String subjectType, String subjectId, PermissionRuleCreatedDocument permissionRule) throws IOException, ChinoApiException {
        checkNotNull(
                new Pair<>(action, "action"),
                new Pair<>(resourceType, "resource_type"),
                new Pair<>(resourceId, "resource_id"),
                new Pair<>(resourceChildren, "resource_children"),
                new Pair<>(subjectType, "subject_type"),
                new Pair<>(subjectId, "subject_id"),
                new Pair<>(permissionRule, "permission_rule_created_document")
        );
        postResource("/perms/"+action+"/"+resourceType+"/"+resourceId+"/"+resourceChildren+"/"+subjectType+"/"+subjectId, permissionRule);
        return SUCCESS_MSG;
    }

}
