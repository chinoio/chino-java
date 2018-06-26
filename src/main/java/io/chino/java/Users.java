package io.chino.java;

import com.fasterxml.jackson.databind.JsonNode;
import io.chino.api.common.ChinoApiConstants;
import io.chino.api.common.ChinoApiException;
import io.chino.api.user.CreateUserRequest;
import io.chino.api.user.GetUserResponse;
import io.chino.api.user.GetUsersResponse;
import io.chino.api.user.User;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.HashMap;

public class Users extends ChinoBaseAPI {

    /**
     * The default constructor used by all {@link ChinoBaseAPI} subclasses
     *
     * @param baseApiUrl      the base URL of the Chino.io API. For testing, use:<br>
     *                        {@code https://api.test.chino.io/v1/}
     * @param parentApiClient the instance of {@link ChinoAPI} that created this object
     */
    public Users(String baseApiUrl, ChinoAPI parentApiClient) {
        super(baseApiUrl, parentApiClient);
    }

    /**
     * Get the list of Users in a UserSchema
     *
     * @param offset the list offset (how many are skipped)
     * @param limit maximum number of results (must be below {@link io.chino.api.common.ChinoApiConstants#QUERY_DEFAULT_LIMIT ChinoApiConstants.QUERY_DEFAULT_LIMIT})
     * @param userSchemaId the id of the UserSchema
     *
     * @return GetUsersResponse Object which contains the list of Users. The list can be retrieved with {@link GetUsersResponse#getUsers()}
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    @Nullable
    public GetUsersResponse list(String userSchemaId, int offset, int limit) throws IOException, ChinoApiException {
        JsonNode data = getResource("/user_schemas/"+userSchemaId+"/users", offset, limit);
        if(data!=null) {
            return mapper.convertValue(data, GetUsersResponse.class);
        }

        return null;
    }

    /**
     * Get the list of {@link User Users} in a {@link io.chino.api.userschema.UserSchema UserSchema}. Return the top 100 Users of the list.
     *
     * @param userSchemaId the id of the UserSchema
     *
     * @return GetUsersResponse Object which contains the list of Users. The list can be retrieved with {@link GetUsersResponse#getUsers()}
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     *
     * @see #list(String, int, int)
     * @see ChinoApiConstants#QUERY_DEFAULT_LIMIT
     */
    @Nullable
    public GetUsersResponse list(String userSchemaId) throws IOException, ChinoApiException {
        JsonNode data = getResource("/user_schemas/"+userSchemaId+"/users", 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null) {
            return mapper.convertValue(data, GetUsersResponse.class);
        }

        return null;
    }

    /**
     * Get the {@link User} object with the matching userId
     *
     * @param userId the id of the User
     *
     * @return the requested User object
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    @Nullable
    public User read(String userId) throws IOException, ChinoApiException{
        JsonNode data = getResource("/users/"+userId, 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return mapper.convertValue(data, GetUserResponse.class).getUser();

        return null;
    }

    /**
     * Get the {@link User} object with the matching userId and map its attributes to a new instance of {@code myClass}.
     * All of the attributes of the User object must be declared in the source of {@code myClass}.
     *
     * @param userId the id of the User
     * @param myClass the Class that represents the attributes of the User
     *
     * @return an instance of {@code myClass} containing the User's attributes.
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    @Nullable
    public Object read(String userId, Class myClass) throws IOException, ChinoApiException{
        JsonNode data = getResource("/users/"+userId, 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null){
            User user = mapper.convertValue(data, GetUserResponse.class).getUser();
            return mapper.convertValue(user.getAttributes(), myClass);
        }

        return null;
    }

    /**
     * Create a new {@link User} on Chino.io under the specified UserSchema
     *
     * @param username the username of the new User
     * @param password the password of the new User
     * @param attributes a {@link HashMap} with the attributes of the new User
     * @param userSchemaId the id of the UserSchema
     *
     * @return the new {@link User} object
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    @Nullable
    public User create(String username, String password, HashMap attributes, String userSchemaId) throws IOException, ChinoApiException {
        CreateUserRequest createUserRequest=new CreateUserRequest(username, password, attributes);

        JsonNode data = postResource("/user_schemas/"+userSchemaId+"/users", createUserRequest);
        if(data!=null)
            return mapper.convertValue(data, GetUserResponse.class).getUser();

        return null;
    }

    /**
     * Create a new {@link User} on Chino.io under the specified UserSchema
     *
     * @param username the username of the new User
     * @param password the password of the new User
     * @param attributes a JSON object (as a {@link String}) with the attributes of the new User
     * @param userSchemaId the id of the UserSchema
     *
     * @return the new {@link User} object
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    @Nullable
    public User create(String username, String password, String attributes, String userSchemaId) throws IOException, ChinoApiException {
        CreateUserRequest createUserRequest=new CreateUserRequest(username, password, fromStringToHashMap(attributes));

        JsonNode data = postResource("/user_schemas/"+userSchemaId+"/users", createUserRequest);
        if(data!=null)
            return mapper.convertValue(data, GetUserResponse.class).getUser();

        return null;
    }

    /**
     * Update some fields of a User.
     *
     * @param userId the User's ID on Chino.io
     * @param attributes a {@link HashMap} with the new values of the User's attributes
     *
     * @return the updated {@link User} object
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    @Nullable
    public User updatePartial(String userId, HashMap attributes) throws IOException, ChinoApiException {
        checkNotNull(userId, "user_id");

        CreateUserRequest createUserRequest= new CreateUserRequest();
        if (attributes.containsKey("username")) {
            createUserRequest.setUsername((String) attributes.remove("username"));
        }
        if (attributes.containsKey("password")) {
            createUserRequest.setPassword((String) attributes.remove("password"));
        }
        createUserRequest.setAttributes(attributes);

        JsonNode data = patchResource("/users/"+userId, createUserRequest);
        if(data!=null)
            return mapper.convertValue(data, GetUserResponse.class).getUser();

        return null;
    }

    /**
     * Update some fields of a User.
     *
     * @param userId the User's ID on Chino.io
     * @param attributes a JSON object (as a {@link String}) with the new values of the User's attributes
     *
     * @return the updated {@link User} object
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    @Nullable
    public User updatePartial(String userId, String attributes) throws IOException, ChinoApiException {
        checkNotNull(userId, "user_id");
        HashMap attrMap =  fromStringToHashMap(attributes);

        return updatePartial(userId, attrMap);
    }

    /**
     * Update some fields of a User.<br>
     * <br>
     * <b>WARNING: this method is Deprecated since SDK version 1.2.3</b> and will be removed in a future release.<br>
     * Please use {@link #updatePartial(String, HashMap)} instead.
     *
     * @param userId the User's ID on Chino.io
     * @param attributes a {@link HashMap} with the new values of the attributes
     *
     * @return User Object
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    @Deprecated
    @Nullable
    public User update(String userId, HashMap attributes) throws IOException, ChinoApiException {
        return updatePartial(userId, attributes);
    }

    /**
     * Update a {@link User} object.
     *
     * @param userId the User's ID on Chino.io
     * @param username the username of the User
     * @param password the password of the User
     * @param attributes an HashMap with the new values of the User's attributes.
     *                   You must provide <b><i> all </i></b> of the attributes that are defined for the User.
     *
     * @return the updated {@link User} object
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    @Nullable
    public User update(String userId, String username, String password, HashMap attributes) throws IOException, ChinoApiException {
        checkNotNull(userId, "user_id");
        CreateUserRequest createUserRequest= new CreateUserRequest(username, password, attributes);

        JsonNode data = putResource("/users/"+userId, createUserRequest);
        if(data!=null)
            return mapper.convertValue(data, GetUserResponse.class).getUser();

        return null;
    }

    /**
     * Update a {@link User} object.
     *
     * @param userId the User's ID on Chino.io
     * @param username the username of the User
     * @param password the password of the User
     * @param attributes a JSON object (as a {@link String}) with the new values of the User's attributes
     *
     * @return the updated {@link User} object
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    @Nullable
    public User update(String userId, String username, String password, String attributes) throws IOException, ChinoApiException {
        checkNotNull(userId, "user_id");
        CreateUserRequest createUserRequest= new CreateUserRequest(username, password, fromStringToHashMap(attributes));

        JsonNode data = putResource("/users/"+userId, createUserRequest);
        if(data!=null)
            return mapper.convertValue(data, GetUserResponse.class).getUser();

        return null;
    }

    /**
     * Delete a {@link User} object
     *
     * @param userId the User's ID on Chino.io
     * @param force set to {@code false} to deactivate the User; set to {@code true} to delete it.
     *              Deleted objects are lost forever.
     *
     * @return a String with the result of the operation (either {@code "success"} or an error message)
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public String delete(String userId, boolean force) throws IOException, ChinoApiException {
        checkNotNull(userId, "user_id");
        return deleteResource("/users/"+userId, force);
    }
}
