package io.chino.client;

import io.chino.api.common.ChinoApiConstants;
import io.chino.api.common.ChinoApiException;
import io.chino.api.user.*;
import org.codehaus.jackson.JsonNode;

import javax.ws.rs.client.Client;
import java.io.IOException;
import java.util.*;

public class Users extends ChinoBaseAPI {
    public Users(String hostUrl, Client clientInitialized){
        super(hostUrl, clientInitialized);
    }

    /**
     * Used to get a list of User
     * @param offset the offset
     * @param userSchemaId the id of the UserSchema
     * @return a GetUsersResponse Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public GetUsersResponse list(int offset, String userSchemaId) throws IOException, ChinoApiException {
        JsonNode data = getResource("/user_schemas/"+userSchemaId+"/users", offset, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null) {
            return mapper.readValue(data, GetUsersResponse.class);
        }

        return null;
    }

    /**
     * Used to get a specific User
     * @param userId the id of the User
     * @return a User Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public User read(String userId) throws IOException, ChinoApiException{
        JsonNode data = getResource("/users/"+userId, 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return mapper.readValue(data, GetUserResponse.class).getUser();

        return null;
    }

    /**
     * Used to get a specific User
     * @param userId the id of the User
     * @param myClass the Class that represents the structure of the User
     * @return an Object of the Class passed
     * @throws IOException
     * @throws ChinoApiException
     */
    public Object read(String userId, Class myClass) throws IOException, ChinoApiException{
        JsonNode data = getResource("/users/"+userId, 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null){
            User user = mapper.readValue(data, GetUserResponse.class).getUser();
            return mapper.readValue(user.getAttributes(), myClass);
        }

        return null;
    }

    /**
     * Used to create a User
     * @param username the username of the User
     * @param password the password of the User
     * @param attributes an HashMap of the attributes
     * @param userSchemaId the id of the UserSchema
     * @return an User Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public User create(String username, String password, HashMap attributes, String userSchemaId) throws IOException, ChinoApiException {
        CreateUserRequest createUserRequest=new CreateUserRequest();
        createUserRequest.setAttributes(attributes);
        createUserRequest.setUsername(username);
        createUserRequest.setPassword(password);

        JsonNode data = postResource("/user_schemas/"+userSchemaId+"/users", createUserRequest);
        if(data!=null)
            return mapper.readValue(data, GetUserResponse.class).getUser();

        return null;
    }

    /**
     * Used to update a User
     * @param userId the id of the User
     * @param username the username of the User
     * @param password the password of the User
     * @param attributes an HashMap of the new attributes
     * @return an User Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public User update(String userId, String username, String password, HashMap attributes) throws IOException, ChinoApiException {
        CreateUserRequest createUserRequest= new CreateUserRequest();
        createUserRequest.setAttributes(attributes);
        createUserRequest.setUsername(username);
        createUserRequest.setPassword(password);

        JsonNode data = putResource("/users/"+userId, createUserRequest);
        if(data!=null)
            return mapper.readValue(data, GetUserResponse.class).getUser();

        return null;
    }

    /**
     * Used to delete a User
     * @param userId the id of the User
     * @param force the boolean force
     * @return a String that represents the result of the operation
     * @throws IOException
     * @throws ChinoApiException
     */
    public String delete(String userId, boolean force) throws IOException, ChinoApiException {
        return deleteResource("/users/"+userId, force);
    }
}
