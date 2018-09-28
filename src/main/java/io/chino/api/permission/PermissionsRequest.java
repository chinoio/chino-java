package io.chino.api.permission;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.chino.api.common.ChinoApiException;
import io.chino.java.Permissions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import static io.chino.java.Permissions.*;
import static io.chino.java.Permissions.ResourceType.*;

/**
 * Class that wraps an API call to Chino.io Permissions API.
 * This request can be executed using {@link Permissions#executeRequest(PermissionsRequest) Permissions.executeRequest} <br>
 *     <br>
 * Values in this class are final and <b>can not be modified</b>.
 * Use {@link Permissions#grant()} and {@link Permissions#revoke()} or a
 * {@link PermissionsRequestBuilder} to create a new instance of {@link PermissionsRequest}.<br>
 *
 */
public final class PermissionsRequest {
    // action info
    private final Action action;

    // subject info
    private final Subject subject;
    private final String subjectId;

    // target resource info
    private final ResourceType target;
    private final String targetId;
    private final boolean onTargetChildren;

    // permissions
    private final JsonNode perms;


    PermissionsRequest(@NotNull PermissionsRequestBuilder builder) {
        this.action = builder.action;
        subject = builder.subject;
        subjectId = builder.subjectId;
        target = builder.target;
        targetId = builder.targetId;
        onTargetChildren = builder.onTargetChildren;

        perms = new ObjectMapper().valueToTree(builder.permissions);
    }

    public String getUrlPath() {
        // Perms on top level resources
        PathBuilder path = new PathBuilder("/perms")
                .append(action.toString())
                .append(target.toString());

        if (onTargetChildren && getChildOf(target) != null) {
            // Perms on resource children
            path.append(targetId)
                .append(getChildOf(target).toString());
        } else if (targetId != null) {
            // Perms on single resource
            path.append(targetId);
        }

        // add subject information
        path.append(subject.toString())
                .append(subjectId);

        return path.toString();
    }

    public JsonNode getBody() {
        return perms;
    }

    @Nullable
    static ResourceType getChildOf(ResourceType type) {
        switch (type) {
            case REPOSITORY:
                return SCHEMA;
            case SCHEMA:
                return DOCUMENT;
            case USER_SCHEMA:
                return USER;
            default:
                return null;
        }
    }


    private class PathBuilder {
        private String path;

        public PathBuilder(String basePath) {
            path = basePath;
        }

        public PathBuilder append(String s) {
            path += "/" + s;
            return this;
        }

        @Override
        public String toString() {
            return path;
        }
    }
}
