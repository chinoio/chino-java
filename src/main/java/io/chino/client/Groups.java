package io.chino.client;

import io.chino.api.common.ChinoApiConstants;
import io.chino.api.common.ChinoApiException;
import io.chino.api.group.CreateGroupRequest;
import io.chino.api.group.GetGroupResponse;
import io.chino.api.group.GetGroupsResponse;
import io.chino.api.group.Group;
import org.codehaus.jackson.JsonNode;

import javax.ws.rs.client.Client;
import java.io.IOException;
import java.util.HashMap;

public class Groups extends ChinoBaseAPI {

    public Groups(String hostUrl, Client clientInitialized){
        super(hostUrl, clientInitialized);
    }

    /**
     * Used to get a list of Groups
     * @param offset the offset
     * @return a GetGroupsResponse Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public GetGroupsResponse list(int offset) throws IOException, ChinoApiException {
        JsonNode data = getResource("/groups", offset, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return mapper.readValue(data, GetGroupsResponse.class);
        return null;
    }

    /**
     * Used to get a specific Group
     * @param groupId the id of the Group
     * @return a Group Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public Group read(String groupId) throws IOException, ChinoApiException{
        JsonNode data = getResource("/groups/"+groupId, 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return mapper.readValue(data, GetGroupResponse.class).getGroup();

        return null;
    }

    /**
     * Used to create a new Group
     * @param groupName the name of the Group
     * @param attributes an HashMap of the attributes
     * @return a Group Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public Group create(String groupName, HashMap attributes) throws IOException, ChinoApiException {
        CreateGroupRequest createGroupRequest=new CreateGroupRequest();
        createGroupRequest.setGroupName(groupName);
        createGroupRequest.setAttributes(attributes);

        System.out.println(mapper.writeValueAsString(createGroupRequest));

        JsonNode data = postResource("/groups", createGroupRequest);
        if(data!=null)
            return mapper.readValue(data, GetGroupResponse.class).getGroup();

        return null;
    }

    /**
     * Used to update a Group
     * @param groupId the id of the Group
     * @param groupName the name of the new Group
     * @param attributes an HashMap of the new attributes
     * @return a Group Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public Group update(String groupId, String groupName, HashMap attributes) throws IOException, ChinoApiException {
        CreateGroupRequest createGroupRequest=new CreateGroupRequest();
        createGroupRequest.setGroupName(groupName);
        createGroupRequest.setAttributes(attributes);

        JsonNode data = putResource("/groups/"+groupId, createGroupRequest);
        if(data!=null)
            return mapper.readValue(data, GetGroupResponse.class).getGroup();

        return null;
    }

    /**
     * Used to delete a Group
     * @param groupId the id of the Group
     * @param force the boolean force
     * @return a String that represents the result of the operation
     * @throws IOException
     * @throws ChinoApiException
     */
    public String delete(String groupId, boolean force) throws IOException , ChinoApiException {
        return deleteResource("/groups/"+groupId, force);
    }

    //--------------------------- Group Membership ----------------------------------

    /**
     * Used to add a User to a Group
     * @param userId the id of the User
     * @param groupId the id of the Group
     * @return a String that represents the result of the operation
     * @throws IOException
     * @throws ChinoApiException
     */
    public String addUserToGroup(String userId, String groupId) throws IOException, ChinoApiException {
        postResource("/groups/"+groupId+"/users/"+userId, null);
        return "success";
    }

    /**
     * Used to remove a User from a Group
     * @param userId the id of the User
     * @param groupId the id of the Group
     * @return a String that represents the result of the operation
     * @throws IOException
     * @throws ChinoApiException
     */
    public String removeUserFromGroup(String userId, String groupId) throws IOException, ChinoApiException {
        deleteResource("/groups/"+groupId+"/users/"+userId, false);
        return "success";
    }

    /**
     * Used to add a UserSchema to a Group
     * @param userSchemaId the id of the UserSchema
     * @param groupId the id of the Group
     * @return a String that represents the result of the operation
     * @throws IOException
     * @throws ChinoApiException
     */
    public String addUserSchemaToGroup(String userSchemaId, String groupId) throws IOException, ChinoApiException {
        postResource("/groups/"+groupId+"/user_schemas/"+userSchemaId, null);
        return "success";
    }

    /**
     * Used to remove a UserSchema from a Group
     * @param userSchemaId the id of the UserSchema
     * @param groupId the id of the Group
     * @return a String that represents the result of the operation
     * @throws IOException
     * @throws ChinoApiException
     */
    public String removeUserSchemaFromGroup(String userSchemaId, String groupId) throws IOException, ChinoApiException {
        deleteResource("/groups/"+groupId+"/user_schemas/"+userSchemaId, false);
        return "success";
    }
}
