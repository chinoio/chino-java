package io.chino.java;

import com.fasterxml.jackson.databind.JsonNode;
import io.chino.api.common.ChinoApiConstants;
import io.chino.api.common.ChinoApiException;
import io.chino.api.group.Group;
import io.chino.api.permission.GetPermissionsResponse;
import io.chino.api.permission.PermissionRule;
import io.chino.api.permission.PermissionRuleCreatedDocument;
import io.chino.api.user.User;
import javafx.util.Pair;

import java.io.IOException;

// TODO rewrite the Permissions interface (reduce/remove Strings from method parameters):
// * use enums instead of String constants
// * add overloaded methods that read IDs from Java objects instead of Strings
// * check that one can not recursively add PermissionRuleCreatedDocument
// * in general, fix this class and objects in io.chino.api.permission.*
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

    /**
     * Read all the Permissions set on every resource belonging to a logged {@link User User}.
     * Doesn't work if invoked by a {@link ChinoAPI#ChinoAPI(String, String, String) customer client}
     * (i.e. authenticated with customerId and customerKey).
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
     * Reads Permissions that are set on a single {@link io.chino.api.document.Document Document}.<br>
     * <br>
     * Customers can always perform this call, while {@link User Users} must have
     * {@code R} Permission on the document.
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

    /**
     * Set {@link io.chino.api.permission.Permission Permissions} on all the resources of a specified type.<br>
     * Available resource types for this method are {@link #GROUPS}, {@link #USER_SCHEMAS} and {@link #REPOSITORIES}.
     *
     * @param action either {@link #GRANT} or {@link #REVOKE}
     * @param resourceType the type of Resource where the new Permissions will be set.
     *                     Can be one of {@link #GROUPS}, {@link #USER_SCHEMAS} or {@link #REPOSITORIES}
     * @param subjectType the type of Subject that will get the new Permissions.
     *                    One of the values in {@link #subjects}
     * @param subjectId the id of the Subject on Chino.io
     * @param permissionRule a {@link PermissionRule} that specifies the new Permissions for the Subject
     *
     * @return a String with the result of the operation
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public String permissionsOnResources(String action, String resourceType, String subjectType, String subjectId, PermissionRule permissionRule) throws IOException, ChinoApiException {
        checkNotNull(action, "action");
        checkNotNull(resourceType, "resource_type");
        checkNotNull(subjectType, "subject_type");
        checkNotNull(subjectId, "subject_id");
        checkNotNull(permissionRule, "permission_rule");
        postResource("/perms/"+action+"/"+resourceType+"/"+subjectType+"/"+subjectId, permissionRule);
        return "success";
    }

    /**
     * Set {@link io.chino.api.permission.Permission Permissions} on all the resources of a specified type.<br>
     * Available resource types for this method are {@link #GROUPS}, {@link #USER_SCHEMAS} and {@link #REPOSITORIES}.
     *
     * @param action either {@link #GRANT} or {@link #REVOKE}
     * @param resourceType the type of Resource where the new Permissions will be set.
     *                     One of the values in {@link #resources}
     * @param resourceId the id of the Resource on Chino.io
     * @param subjectType the type of Subject that will get the new Permissions.
     *                    One of the values in {@link #subjects}
     * @param subjectId the id of the Subject on Chino.io
     * @param permissionRule a {@link PermissionRule} that specifies the new Permissions for the Subject
     *
     * @return a String with the result of the operation
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public String permissionsOnaResource(String action, String resourceType, String resourceId, String subjectType, String subjectId, PermissionRule permissionRule) throws IOException, ChinoApiException {
        checkNotNull(action, "action");
        checkNotNull(resourceType, "resource_type");
        checkNotNull(resourceId, "resource_id");
        checkNotNull(subjectType, "subject_type");
        checkNotNull(subjectId, "subject_id");
        checkNotNull(permissionRule, "permission_rule");
        postResource("/perms/"+action+"/"+resourceType+"/"+resourceId+"/"+subjectType+"/"+subjectId, permissionRule);
        return "success";
    }

    /**
     * Set {@link io.chino.api.permission.Permission Permissions} on all the children of a resource type.<br>
     * Only {@link #USER_SCHEMAS}, {@link #SCHEMAS} or {@link #REPOSITORIES} have children: <br>
     *
     * <ul>
     *     <li><b>UserSchemas</b> -&gt; Users</li>
     *     <li><b>Schemas</b> -&gt; Documents</li>
     *     <li><b>Repositories</b> -&gt; Schemas</li>
     * </ul>
     *
     * @param action either {@link #GRANT} or {@link #REVOKE}
     * @param resourceType One of {@link #USER_SCHEMAS}, {@link #SCHEMAS} or {@link #REPOSITORIES}
     * @param resourceId the id of the Resource on Chino.io
     * @param resourceChildren the child Resource of the specified Resource type.
     * @param subjectType the type of Subject that will get the new Permissions.
     *                    One of the values in {@link #subjects}
     * @param subjectId the id of the Subject on Chino.io
     * @param permissionRule a {@link PermissionRule} that specifies the new Permissions for the Subject
     *
     * @return a String with the result of the operation
     *
     * @see #childrenOf(String)
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
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
        return "success";
    }

    /**
     * Set {@link io.chino.api.permission.Permission Permissions} on all the children of a resource type.<br>
     * Only {@link #USER_SCHEMAS}, {@link #SCHEMAS} or {@link #REPOSITORIES} have children: <br>
     *
     * <ul>
     *     <li><b>UserSchemas</b> -&gt; Users</li>
     *     <li><b>Schemas</b> -&gt; Documents</li>
     *     <li><b>Repositories</b> -&gt; Schemas</li>
     * </ul>
     *
     * @param action either {@link #GRANT} or {@link #REVOKE}
     * @param resourceType One of {@link #USER_SCHEMAS}, {@link #SCHEMAS} or {@link #REPOSITORIES}
     * @param resourceId the id of the Resource on Chino.io
     * @param resourceChildren the child Resource of the specified Resource type.
     * @param subjectType the type of Subject that will get the new Permissions.
     *                    One of the values in {@link #subjects}
     * @param subjectId the id of the Subject on Chino.io
     * @param permissionRule a {@link PermissionRule} that specifies the new Permissions for the Subject
     *
     * @return a String with the result of the operation
     *
     * @see #childrenOf(String)
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
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
        return "success";
    }

    /* MACROS */

    /* actions */
    /** {@code action}: grant permission on the specified resource */
    public static final String GRANT = "grant",

    /** {@code action}: revoke permission on the specified resource */
            REVOKE = "revoke";

    /* resource types */
    /** {@code resource}: apply Permissions changes to a {@link io.chino.api.repository.Repository Repository} */
    public static final String REPOSITORIES = "repositories",

    /** {@code resource}: apply Permissions changes to a {@link io.chino.api.schema.Schema Schema} */
            SCHEMAS = "schemas",

    /** {@code resource}: apply Permissions changes to a {@link io.chino.api.document.Document Document} */
            DOCUMENTS = "documents",

    /** {@code resource}: apply Permissions changes to a {@link io.chino.api.userschema.UserSchema UserSchema} */
            USER_SCHEMAS = "user_schemas",

    /**
     * {@code resource}: apply Permissions changes to a {@link User}'s data
     * {@code subject}: change the Permissions that a {@link User} has on the specified resource.
     */
            USERS = "users",

    /**
     * {@code resource}: apply Permissions changes to all members of {@link Group}
     * {@code subject}: change the Permissions that all members of {@link Group} have
     *                  on the specified resource.
     */
            GROUPS = "groups";

    /**
     * Values that are meant to be passed as the {@code subject} argument of methods that set Permissions<br>
     *      {@link #USERS}  <br>
     *      {@link #GROUPS}
     */
    public final static String [] subjects = {
            USERS,
            GROUPS
    };

    /**
     * Values that are meant to be passed as the {@code resource} argument of methods that set Permissions:<br>
     *      {@link #REPOSITORIES}   <br>
     *      {@link #SCHEMAS}        <br>
     *      {@link #DOCUMENTS}      <br>
     *      {@link #USER_SCHEMAS}   <br>
     *      {@link #USERS}          <br>
     *      {@link #GROUPS}
     */
    public final static String [] resources = {
            REPOSITORIES,
            SCHEMAS,
            DOCUMENTS,
            USER_SCHEMAS,
            USERS,
            GROUPS
    };

    /**
     * Get the children resource type of the specified resource.
     *
     * @param resource One of {@link #REPOSITORIES}, {@link #SCHEMAS} or {@link #USER_SCHEMAS}
     *
     * @return the child {@link #resources resource} of the specified resource, according to the
     *          <a href="https://docs.chino.io/#header-resource-structure">
     *              Chino.io Resources structure
     *          </a>
     */
    public static String childrenOf(String resource) {
        switch (resource) {
            case REPOSITORIES:
                return SCHEMAS;
            case SCHEMAS:
                return DOCUMENTS;
            case USER_SCHEMAS:
                return USERS;
            default:
                throw new IllegalArgumentException(resource + " don't have child resources.");
        }
    }
}
