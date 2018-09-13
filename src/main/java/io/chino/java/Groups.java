package io.chino.java;

import com.fasterxml.jackson.databind.JsonNode;
import io.chino.api.common.ChinoApiConstants;
import io.chino.api.common.ChinoApiException;
import io.chino.api.group.CreateGroupRequest;
import io.chino.api.group.GetGroupResponse;
import io.chino.api.group.GetGroupsResponse;
import io.chino.api.group.Group;

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
     * Returns a list of Groups
     * @param offset the offset from which it retrieves the Groups
     * @param limit number of results (max {@link io.chino.api.common.ChinoApiConstants#QUERY_DEFAULT_LIMIT})
     * @return GetGroupsResponse Object which contains the list of Groups
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
     * Returns a list of Groups
     * @return GetGroupsResponse Object which contains the list of Groups
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
     * It retrieves a specific Group
     * @param groupId the id of the Group
     * @return Group Object
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
     * It creates a new Group
     * @param groupName the name of the Group
     * @param attributes an HashMap of the attributes
     * @return Group Object
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public Group create(String groupName, HashMap attributes) throws IOException, ChinoApiException {
        CreateGroupRequest createGroupRequest=new CreateGroupRequest(groupName, attributes);

        JsonNode data = postResource("/groups", createGroupRequest);
        if(data!=null)
            return mapper.convertValue(data, GetGroupResponse.class).getGroup();

        return null;
    }

    /**
     * It updates a Group
     * @param groupId the id of the Group
     * @param groupName the name of the new Group
     * @param attributes an HashMap of the new attributes
     * @return Group Object
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
     * It deletes a Group
     * @param groupId the id of the Group
     * @param force if true, the resource cannot be restored
     * @return a String with the result of the operation
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public String delete(String groupId, boolean force) throws IOException , ChinoApiException {
        checkNotNull(groupId, "group_id");
        return deleteResource("/groups/"+groupId, force);
    }

    //--------------------------- Group Membership ----------------------------------

    /**
     * It adds a User to a Group
     * @param userId the id of the User
     * @param groupId the id of the Group
     * @return a String with the result of the operation
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
     * It removes a User from a Group
     * @param userId the id of the User
     * @param groupId the id of the Group
     * @return a String with the result of the operation
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
     * It adds a UserSchema to a Group
     * @param userSchemaId the id of the UserSchema
     * @param groupId the id of the Group
     * @return a String with the result of the operation
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
     * It removes a UserSchema from a Group
     * @param userSchemaId the id of the UserSchema
     * @param groupId the id of the Group
     * @return a String with the result of the operation
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
