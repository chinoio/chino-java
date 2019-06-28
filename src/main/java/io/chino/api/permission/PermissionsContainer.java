package io.chino.api.permission;

import io.chino.java.Permissions;

import java.util.List;

/**
 * This interface is implemented by classes that hold information about Permissions on Chino.io,
 * which use the following structure:
 * <pre>
 *     {
 *         "manage" : [ permissions ... ],
 *         "authorize" : [ permissions ... ],
 *         "created_document" : {
 *              "manage" : [ permissions ... ],
 *              "authorize" : [ permissions ... ]
 *         }
 *     }
 * </pre>
 *
 * For more information about the Permission format, visit the
 * <a href="https://console.test.chino.io/docs/v1#permissions">Permissions</a> page on the Chino.io API docs.
 *
 * @see Permissions.Type
 */
public interface PermissionsContainer {
    /**
     * Get the "manage" list of Permissions
     *
     * @return a {@link List} of {@link io.chino.java.Permissions.Type}
     */
    List<Permissions.Type> getManagePermissions();

    /**
     * Get the "authorize" list of Permissions
     *
     * @return a {@link List} of {@link io.chino.java.Permissions.Type}
     */
    List<Permissions.Type> getAuthorizePermissions();

    /**
     * Get the "created_document.manage" list of Permissions.
     * <b>Note:</b> the "created_document" list is only available for requests that set Permissions
     * on children of a Schema. For any other request, implementations of this method must return {@code null}.
     *
     * @return a {@link List} of {@link io.chino.java.Permissions.Type}
     */
    List<Permissions.Type> getManagePermissionsOnCreatedDocuments();

    /**
     * Get the "created_document.authorize" list of Permissions.
     * <b>Note:</b> the "created_document" list is only available for requests that set Permissions
     * on children of a Schema. For any other request, implementations of this method must return {@code null}.
     *
     * @return a {@link List} of {@link io.chino.java.Permissions.Type}
     */
    List<Permissions.Type> getAuthorizePermissionsOnCreatedDocuments();
}
