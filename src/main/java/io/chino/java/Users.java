package io.chino.java;

import com.fasterxml.jackson.databind.JsonNode;
import io.chino.api.common.ChinoApiConstants;
import io.chino.api.common.ChinoApiException;
import io.chino.api.user.CreateUserRequest;
import io.chino.api.user.GetUserResponse;
import io.chino.api.user.GetUsersResponse;
import io.chino.api.user.User;
import okhttp3.OkHttpClient;
import java.io.IOException;
import java.util.HashMap;

public class Users extends ChinoBaseAPI {
    public Users(String hostUrl, OkHttpClient clientInitialized){
        super(hostUrl, clientInitialized);
    }

    /**
     * Returns a list of Users
     * @param offset the offset from which it retrieves the Users
     * @param limit number of results (max {@link io.chino.api.common.ChinoApiConstants#QUERY_DEFAULT_LIMIT})
     * @param userSchemaId the id of the UserSchema
     * @return GetUsersResponse Object which contains the list of Users
     * @throws IOException
     * @throws ChinoApiException
     */
    public GetUsersResponse list(String userSchemaId, int offset, int limit) throws IOException, ChinoApiException {
        JsonNode data = getResource("/user_schemas/"+userSchemaId+"/users", offset, limit);
        if(data!=null) {
            return mapper.convertValue(data, GetUsersResponse.class);
        }

        return null;
    }

    /**
     * Returns a list of Users
     * @param userSchemaId the id of the UserSchema
     * @return GetUsersResponse Object which contains the list of Users
     * @throws IOException
     * @throws ChinoApiException
     */
    public GetUsersResponse list(String userSchemaId) throws IOException, ChinoApiException {
        JsonNode data = getResource("/user_schemas/"+userSchemaId+"/users", 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null) {
            return mapper.convertValue(data, GetUsersResponse.class);
        }

        return null;
    }

    /**
     * It retrieves a specific User
     * @param userId the id of the User
     * @return User Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public User read(String userId) throws IOException, ChinoApiException{
        JsonNode data = getResource("/users/"+userId, 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return mapper.convertValue(data, GetUserResponse.class).getUser();

        return null;
    }

    /**
     * It retrieves a specific User creating an Object of the class "myClass"
     * @param userId the id of the User
     * @param myClass the Class that represents the structure of the User
     * @return Object of the Class passed as argument
     * @throws IOException
     * @throws ChinoApiException
     */
    public Object read(String userId, Class myClass) throws IOException, ChinoApiException{
        JsonNode data = getResource("/users/"+userId, 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null){
            User user = mapper.convertValue(data, GetUserResponse.class).getUser();
            return mapper.convertValue(user.getAttributes(), myClass);
        }

        return null;
    }

    /**
     * It creates a User
     * @param username the username of the User
     * @param password the password of the User
     * @param attributes an HashMap of the attributes
     * @param userSchemaId the id of the UserSchema
     * @return User Object
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
            return mapper.convertValue(data, GetUserResponse.class).getUser();

        return null;
    }

    /**
     * It creates a User
     * @param username the username of the User
     * @param password the password of the User
     * @param attributes a String that represents a json of the attributes
     * @param userSchemaId the id of the UserSchema
     * @return User Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public User create(String username, String password, String attributes, String userSchemaId) throws IOException, ChinoApiException {
        CreateUserRequest createUserRequest=new CreateUserRequest();
        createUserRequest.setAttributes(fromStringToHashMap(attributes));
        createUserRequest.setUsername(username);
        createUserRequest.setPassword(password);

        JsonNode data = postResource("/user_schemas/"+userSchemaId+"/users", createUserRequest);
        if(data!=null)
            return mapper.convertValue(data, GetUserResponse.class).getUser();

        return null;
    }

    /**
     * It updates the User
     * @param userId the id of the User
     * @param attributes an HashMap with the attributes of the user
     * @return User Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public User update(String userId, HashMap attributes) throws IOException, ChinoApiException {
        CreateUserRequest createUserRequest= new CreateUserRequest();
        createUserRequest.setAttributes(attributes);
        JsonNode data = patchResource("/users/"+userId, createUserRequest);
        if(data!=null)
            return mapper.convertValue(data, GetUserResponse.class).getUser();

        return null;
    }

    /**
     * It updates a User
     * @param userId the id of the User
     * @param username the username of the User
     * @param password the password of the User
     * @param attributes an HashMap of the new attributes
     * @return User Object
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
            return mapper.convertValue(data, GetUserResponse.class).getUser();

        return null;
    }

    /**
     * It updates a User
     * @param userId the id of the User
     * @param username the username of the User
     * @param password the password of the User
     * @param attributes a String that represents a json of the attributes
     * @return User Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public User update(String userId, String username, String password, String attributes) throws IOException, ChinoApiException {
        CreateUserRequest createUserRequest= new CreateUserRequest();
        createUserRequest.setAttributes(fromStringToHashMap(attributes));
        createUserRequest.setUsername(username);
        createUserRequest.setPassword(password);

        JsonNode data = putResource("/users/"+userId, createUserRequest);
        if(data!=null)
            return mapper.convertValue(data, GetUserResponse.class).getUser();

        return null;
    }

    /**
     * It deletes a User
     * @param userId the id of the User
     * @param force if true, the resource cannot be restored
     * @return a String with the result of the operation
     * @throws IOException
     * @throws ChinoApiException
     */
    public String delete(String userId, boolean force) throws IOException, ChinoApiException {
        return deleteResource("/users/"+userId, force);
    }
}
