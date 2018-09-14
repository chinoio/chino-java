package io.chino.java;

import com.fasterxml.jackson.databind.JsonNode;
import io.chino.api.common.ChinoApiConstants;
import io.chino.api.common.ChinoApiException;
import io.chino.api.group.CreateGroupRequest;
import io.chino.api.group.GetGroupResponse;
import io.chino.api.group.GetGroupsResponse;
import io.chino.api.group.Group;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;

public class Groups extends ChinoBaseAPI {

    /**
     * The default constructor used by all {@link ChinoBaseAPI} subclasses
     *
     * @param baseApiUrl      the base URL of the Chino.io API. For testing, use:<br>
     *                        {@code https://api.test.chino.io/v1/}
     * @param parentApiClient the instance of {@link ChinoAPI} that created this object
     */
    public Groups(String baseApiUrl, ChinoAPI parentApiClient) {
        super(baseApiUrl, parentApiClient);
    }

    /**
     * Get the list of existing {@link Group Groups}
     *
     * @param offset the list offset (how many are skipped)
     * @param limit maximum number of results (must be below
     *              {@link io.chino.api.common.ChinoApiConstants#QUERY_DEFAULT_LIMIT ChinoApiConstants.QUERY_DEFAULT_LIMIT})
     *
     * @return a {@link GetGroupsResponse} which wraps a {@link java.util.List} of {@link Group Groups}
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public GetGroupsResponse list(int offset, int limit) throws IOException, ChinoApiException {
        JsonNode data = getResource("/groups", offset, limit);
        if(data!=null)
            return mapper.convertValue(data, GetGroupsResponse.class);
        return null;
    }

    /**
     * Get the list of existing {@link Group Groups}
     *
     * @return a {@link GetGroupsResponse} which wraps a {@link java.util.List} of {@link Group Groups}
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public GetGroupsResponse list() throws IOException, ChinoApiException {
        JsonNode data = getResource("/groups", 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return mapper.convertValue(data, GetGroupsResponse.class);
        return null;
    }

    /**
     * Get information about a specific {@link Group}
     *
     * @param groupId the id of the {@link Group}
     *
     * @return the {@link Group} with the specified ID
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public Group read(String groupId) throws IOException, ChinoApiException{
        checkNotNull(groupId, "group_id");
        JsonNode data = getResource("/groups/"+groupId, 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return mapper.convertValue(data, GetGroupResponse.class).getGroup();

        return null;
    }

    /**
     * Create a new {@link Group} on Chino.io
     *
     * @param groupName the name of the new {@link Group}
     * @param attributes an {@link HashMap} containing the Group's attributes.
     *                   If you don't want to set attributes, you have to pass an empty map.<br>
     *                   <b>Do not pass a {@code null} reference</b> or you will get an exception.
     *
     * @return the new {@link Group}
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public Group create(String groupName, @NotNull HashMap attributes) throws IOException, ChinoApiException {
        CreateGroupRequest createGroupRequest = new CreateGroupRequest(groupName, attributes);

        JsonNode data = postResource("/groups", createGroupRequest);
        if(data!=null)
            return mapper.convertValue(data, GetGroupResponse.class).getGroup();

        return null;
    }

    /**
     * Update an existing {@link Group}
     *
     * @param groupId the id of the Group
     * @param groupName the name of the new Group
     * @param attributes an HashMap of the new attributes
     *
     * @return the updated {@link Group}
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public Group update(String groupId, String groupName, HashMap attributes) throws IOException, ChinoApiException {
        checkNotNull(groupId, "group_id");
        CreateGroupRequest createGroupRequest=new CreateGroupRequest(groupName, attributes);

        JsonNode data = putResource("/groups/"+groupId, createGroupRequest);
        if(data!=null)
            return mapper.convertValue(data, GetGroupResponse.class).getGroup();

        return null;
    }

    /**
     * Delete a {@link Group} from Chino.io
     *
     * @param groupId the id of the Group to delete
     * @param force if set to {@code true}, the {@link Group} cannot be restored.
     *              Otherwise it will only get deactivated.
     *
     * @return a String with the result of the operation
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public String delete(String groupId, boolean force) throws IOException , ChinoApiException {
        checkNotNull(groupId, "group_id");
        return deleteResource("/groups/"+groupId, force);
    }

    //--------------------------- Group Membership ----------------------------------//

    /**
     * Add a {@link io.chino.api.user.User} to a {@link Group}
     *
     * @param userId the id of the {@link io.chino.api.user.User} to add
     * @param groupId the id of the {@link Group}
     *
     * @return a String with the result of the operation
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public String addUserToGroup(String userId, String groupId) throws IOException, ChinoApiException {
        checkNotNull(groupId, "group_id");
        checkNotNull(userId, "user_id");
        postResource("/groups/"+groupId+"/users/"+userId, null);
        return "success";
    }

    /**
     * Remove a {@link io.chino.api.user.User} from a {@link Group}
     *
     * @param userId the id of the {@link io.chino.api.user.User} to remove
     * @param groupId the id of the {@link Group}
     *
     * @return a String with the result of the operation
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public String removeUserFromGroup(String userId, String groupId) throws IOException, ChinoApiException {
        checkNotNull(groupId, "group_id");
        checkNotNull(userId, "user_id");
        deleteResource("/groups/"+groupId+"/users/"+userId, false);
        return "success";
    }

    /**
     * Add all {@link io.chino.api.user.User Users} of a {@link io.chino.api.userschema.UserSchema} to a {@link Group}.
     * The new Group membership is applied to all users of the UserSchema, even if created later than this call.
     *
     * @param userSchemaId the id of the {@link io.chino.api.userschema.UserSchema}
     * @param groupId the id of the {@link Group}
     *
     * @return a String with the result of the operation
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public String addUserSchemaToGroup(String userSchemaId, String groupId) throws IOException, ChinoApiException {
        checkNotNull(groupId, "group_id");
        checkNotNull(userSchemaId, "user_schema_id");
        postResource("/groups/"+groupId+"/user_schemas/"+userSchemaId, null);
        return "success";
    }

    /**
     * Remove all {@link io.chino.api.user.User Users} of a {@link io.chino.api.userschema.UserSchema} from a {@link Group}.
     * The new Group membership is applied to all users of the UserSchema, even if created later than this call.
     *
     * @param userSchemaId the id of the {@link io.chino.api.userschema.UserSchema}
     * @param groupId the id of the {@link Group}
     *
     * @return a String with the result of the operation
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public String removeUserSchemaFromGroup(String userSchemaId, String groupId) throws IOException, ChinoApiException {
        checkNotNull(groupId, "group_id");
        checkNotNull(userSchemaId, "user_schema_id");
        deleteResource("/groups/"+groupId+"/user_schemas/"+userSchemaId, false);
        return "success";
    }
}
