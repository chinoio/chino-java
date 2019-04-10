package io.chino.api.permission;

import io.chino.api.common.ChinoApiException;
import io.chino.api.group.Group;
import io.chino.api.user.User;
import io.chino.java.Permissions;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

import static io.chino.java.Permissions.*;
import static io.chino.java.Permissions.ResourceType.*;

/**
 * Utility class that is used to build {@link PermissionsRequest PermissionsRequests} that will change Permissions of a
 * {@link io.chino.api.user.User User} or {@link io.chino.api.group.Group Group} over a certain resource.
 * See a usage example below:
 * <pre>
 *     chino.permissions.grant()            // Returns a {@link PermissionsRequestBuilder}
 *          .toUser("bf31d259-423f-4b9a-bea1-63270c9e5770")
 *          .onChildrenOf(SCHEMA, "9a3b7b5d-d437-4386-b61e-2e65bde6341d")
 *          .permissions(
 *                  new {@link PermissionSetter}()....
 *          ).buildRequest();               // Returns a {@link PermissionsRequest}
 * </pre>
 */
public class PermissionsRequestBuilder implements PermissionsContainer {

    // action info
    Action action;

    // subject info
    Subject subject;
    String subjectId;

    // target resource info
    ResourceType target;
    String targetId;
    boolean onTargetChildren;

    // actual permissions
    PermissionSetter permissions;

    // API client
    Permissions client;

    /**
     * Create a new {@link PermissionsRequestBuilder} which will perform the specified action
     */
    public PermissionsRequestBuilder(@NotNull Action action, Permissions caller) {
        this.action = action;
        this.client = caller;

        // init fields
        subject = null;
        subjectId = null;
        target = null;
        targetId = null;
        onTargetChildren = false;
    }

    /* SET SUBJECT */
    /**
     * Modify Permissions for a single {@link io.chino.api.user.User User}.
     * Call either this method or {@link #toGroup(String)} in order to set the subject of the {@link PermissionsRequest}.
     * <br>
     * Subsequent calls to {@link #toUser(String)} or {@link #toGroup(String)} will overwrite the previous value.
     *
     * @param userId the ID of a {@link io.chino.api.user.User User} on Chino.io
     *
     * @return this {@link PermissionsRequestBuilder}
     *
     * @throws ChinoApiException invalid parameter: {@code userId} is not a valid UUID
     * (verified with {@link UUID#fromString(String)} )
     */
    public PermissionsRequestBuilder toUser(String userId) throws ChinoApiException {
        validateID(userId, "user");
        subject = Subject.USER;
        subjectId = userId;

        return this;
    }

    /**
     * Modify Permissions for all Users in a {@link io.chino.api.group.Group Group}.
     * Call either this method or {@link #toUser(String)} in order to set the subject of the {@link PermissionsRequest}.
     * <br>
     * Subsequent calls to {@link #toUser(String)} or {@link #toGroup(String)} will overwrite the previous value.
     *
     * @param groupId the ID of a {@link io.chino.api.group.Group Group} on Chino.io
     *
     * @return this {@link PermissionsRequestBuilder}
     *
     * @throws ChinoApiException invalid parameter: {@code groupId} is not a valid UUID
     * (verified with {@link UUID#fromString(String)} )
     */
    public PermissionsRequestBuilder toGroup(String groupId) throws ChinoApiException {
        validateID(groupId, "group");
        subject = Subject.GROUP;
        subjectId = groupId;

        return this;
    }

    /**
     * Modify Permissions for a single {@link io.chino.api.user.User User}.
     * Call either this method or {@link #toGroup(String)} in order to set the subject of the {@link PermissionsRequest}.
     * <br>
     * Subsequent calls to {@link #toUser(String)} or {@link #toGroup(String)} will overwrite the previous value.
     *
     * @param user an instance of {@link io.chino.api.user.User User}
     *
     * @return this {@link PermissionsRequestBuilder}
     *
     * @throws ChinoApiException invalid parameter: {@code user} doesn't have a valid ID
     * (verified with {@link UUID#fromString(String)} )
     */
    public PermissionsRequestBuilder to(@NotNull User user) throws ChinoApiException {
        return toUser(user.getUserId());
    }

    /**
     * Modify Permissions for all Users in a {@link io.chino.api.group.Group Group}.
     * Call either this method or {@link #toUser(String)} in order to set the subject of the {@link PermissionsRequest}.
     * <br>
     * Subsequent calls to {@link #toUser(String)} or {@link #toGroup(String)} will overwrite the previous value.
     *
     * @param group an instance of {@link io.chino.api.group.Group Group}
     *
     * @return this {@link PermissionsRequestBuilder}
     *
     * @throws ChinoApiException invalid parameter: {@code group} doesn't have a valid ID
     * (verified with {@link UUID#fromString(String)} )
     */
    public PermissionsRequestBuilder to(@NotNull Group group) throws ChinoApiException {
        return toGroup(group.getGroupId());
    }


    /* SET TARGET RESOURCE */

    /**
     * Apply Permissions on a single resource.
     * <br>
     * Subsequent calls to {@link #on(ResourceType, String)} and {@link #onChildrenOf(ResourceType, String)} and on {@link #onEvery(ResourceType)}
     * will overwrite the previous value.
     *
     * @param resourceType the {@link ResourceType type} of the resource
     * @param resourceId the ID of the resource on Chino.io
     *
     * @return this {@link PermissionsRequestBuilder}
     *
     * @throws ChinoApiException invalid parameter: {@code resourceId} is not a valid UUID
     * (verified with {@link UUID#fromString(String)} )
     */
    public PermissionsRequestBuilder on(@NotNull ResourceType resourceType, @NotNull String resourceId) throws ChinoApiException {
        validateID(resourceId, resourceType.toString());
        target = resourceType;
        targetId = resourceId;
        onTargetChildren = false;

        return this;
    }

    /**
     * Apply Permissions on the children of the specified resource. Only the following resources have children:
     *
     * <ul>
     *     <li><b>Repositories</b> -&gt; Schemas</li>
     *     <li><b>Schemas</b> -&gt; Documents</li>
     *     <li><b>UserSchemas</b> -&gt; Users</li>
     * </ul>
     * <br>
     * Subsequent calls to {@link #on(ResourceType, String)} and {@link #onChildrenOf(ResourceType, String)} and on {@link #onEvery(ResourceType)}
     * will overwrite the previous value.
     *
     * @param resourceType the parent resource. Only  {@link ResourceType#REPOSITORY}, {@link ResourceType#SCHEMA}
     *                     and {@link ResourceType#USER_SCHEMA} are allowed, since other resources don't have children.
     * @param resourceId the resource ID on Chino.io of the parent resource.
     *
     * @return this {@link PermissionsRequestBuilder}
     *
     * @throws ChinoApiException invalid parameter: {@code resourceId} is not a valid UUID
     * (verified with {@link UUID#fromString(String)} )
     * @throws IllegalArgumentException the specified {@link ResourceType} does not have children
     */
    public PermissionsRequestBuilder onChildrenOf(@NotNull ResourceType resourceType, @NotNull String resourceId) throws ChinoApiException, IllegalArgumentException {
        validateID(resourceId, resourceType.toString());
        if (PermissionsRequest.getChildOf(resourceType) == null) {
            throw new IllegalArgumentException("Resource '" + resourceType + "' does not have children.");
        }

        target = resourceType;
        targetId = resourceId;
        onTargetChildren = true;

        return this;
    }

    /**
     * Apply Permissions on every resource. Can only be called for <b>top-level resources</b>.
     * <br>
     * Subsequent calls to {@link #on(ResourceType, String)} and {@link #onChildrenOf(ResourceType, String)} and on {@link #onEvery(ResourceType)}
     * will overwrite the previous value.
     *
     * @param resourceType one of the top-level resources, i.e. {@link ResourceType#REPOSITORY},
     *                      {@link ResourceType#GROUP} or {@link ResourceType#USER_SCHEMA}
     *
     * @return this {@link PermissionsRequestBuilder}
     *
     * @throws IllegalArgumentException invalid {@link ResourceType} - not a top-level resource
     */
    public PermissionsRequestBuilder onEvery(@NotNull ResourceType resourceType) throws IllegalArgumentException {
        if (resourceType != REPOSITORY && resourceType !=GROUP && resourceType !=USER_SCHEMA) {
            throw new IllegalArgumentException("'" + resourceType + "' is not a valid resource type. Allowed values are REPOSITORY, GROUP and USER_SCHEMA.");
        }
        target = resourceType;
        targetId = null;
        onTargetChildren = false;

        return this;
    }

    /* SET PERMISSION RULES */

    /**
     * Store the {@link Type Permission Types} that are granted / revoked with this request. <br>
     * In order for the permissions to be applied, you need to call also
     * {@link #buildRequest() .buildRequest()}{@link PermissionsRequest#execute() .execute()}<br>
     * <br>
     * <b>Note:</b> the "created_document" list can only be set on children of Schemas:
     * <pre>
     *     {@link #onChildrenOf(ResourceType, String) .onChildrenOf}({@link ResourceType#SCHEMA SCHEMA}, &lt;schema_id&gt;)
     * </pre>
     * Trying to set this value for other resources will produce an exception.
     *
     * @param permissions a {@link PermissionSetter} with the {@link Type Permission Types} to set with this request
     *
     * @return this {@link PermissionsRequestBuilder}
     *
     * @throws IllegalArgumentException permissions over "created_document" can only be set on children of Schemas
     */
    public PermissionsRequestBuilder permissions(PermissionSetter permissions) {
        if (!(target == SCHEMA && onTargetChildren)) {
            // JSON parameter "created_document" can not be added
            boolean hasCreatedDocument = permissions.getManagePermissionsOnCreatedDocuments() != null
                                        && permissions.getAuthorizePermissionsOnCreatedDocuments() != null;

            if (hasCreatedDocument)
                throw new IllegalArgumentException("Field \"created_document\" can only be used on children of a Schema.");
        }
        this.permissions = permissions;
        return this;
    }

    /* BUILD REQUEST */

    /**
     * Freeze this builder into a {@link PermissionsRequest} that can be executed.
     *
     * @return a {@link PermissionsRequest} with the parameter values that are set in this builder at this moment
     *
     * @throws ChinoApiException this {@link PermissionsRequestBuilder} lacks a parameter or contains an invalid ID
     *
     * @see PermissionsRequest#execute()
     */
    public PermissionsRequest buildRequest() throws ChinoApiException {
        validateRequest();
        return new PermissionsRequest(this);
    }

    @Override
    public List<Type> getManagePermissions() {
        return permissions.getManagePermissions();
    }

    @Override
    public List<Type> getAuthorizePermissions() {
        return permissions.getAuthorizePermissions();
    }

    @Override
    public List<Type> getManagePermissionsOnCreatedDocuments() {
        return permissions.getManagePermissionsOnCreatedDocuments();
    }

    @Override
    public List<Type> getAuthorizePermissionsOnCreatedDocuments() {
        return permissions.getAuthorizePermissionsOnCreatedDocuments();
    }


    private void validateID(String id, String ownerType) throws ChinoApiException {
        try {
            UUID.fromString(id);
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new ChinoApiException("Malformed " + ownerType + " Id: wrong format, not a valid UUID");
        }

    }

    private void validateRequest() throws ChinoApiException {
        try {
            if(action == null)
                throw new NullPointerException("action");
            if(subject == null)
                throw new NullPointerException("subject");
            if(target == null)
                throw new NullPointerException("resource type");
            if(permissions == null)
                throw new NullPointerException("permissions");
        } catch (NullPointerException np) {
            throw new ChinoApiException("Permissions request is missing parameter '" + np.getMessage() + "'");
        }

        validateID(subjectId, "subject");
        if (targetId != null)
            validateID(targetId, "target");
    }
}
