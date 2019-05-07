package io.chino.java;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
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

/**
 * Manage you {@link User Users} on Chino.io.
 */
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
     * Check the validity of a {@link User}'s password.<br>
     * <b>WARNING: you must be logged in as a {@link User}</b>,
     * i.e. you must have used one of the loginWith*** methods.
     *
     * @param password the password to verify, as a {@link String}
     *
     * @return {@code true} if the password is valid, {@code false} otherwise
     *
     * @see Auth#loginWithPassword(String, String, String)
     * @see Auth#loginWithPassword(String, String, String, String)
     * @see Auth#loginWithAuthenticationCode(String, String, String, String)
     * @see Auth#loginWithBearerToken(String)
     * @see ChinoAPI#setBearerToken(String)
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public boolean checkPassword(String password) throws IOException, ChinoApiException {
        checkNotNull(password, "password");

        @JsonInclude(JsonInclude.Include.ALWAYS)
        class Password {
            private Password(String p) {
                password = p;
            }

            @JsonProperty("password")
            private final String password;
        }

        JsonNode data = postResource("/users/psw_check", new Password(password));
        HashMap<String, Boolean> value = mapper.convertValue(
                data,
                new TypeReference<HashMap<String, Boolean>>() {}
        );

        return value.get("valid");
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
        return create(username, password, attributes, userSchemaId, false);
    }

    /**
     * Create a new {@link User} on Chino.io under the specified UserSchema
     *
     * @param username the username of the new User
     * @param password the password of the new User
     * @param attributes a {@link HashMap} with the attributes of the new User
     * @param userSchemaId the id of the UserSchema
     * @param consistent setting this flag to {@code true} will make the indexing synchronous with the creation of the user,
     *                   i.e. search operations will be successful right after this method returns.
     *                   However, this operation has a cost and can make the API call last for seconds before answering.
     *                   Use only when it's really needed
     *
     * @return the new {@link User} object
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    @Nullable
    public User create(String username, String password, HashMap attributes, String userSchemaId, boolean consistent) throws IOException, ChinoApiException {
        CreateUserRequest createUserRequest=new CreateUserRequest(username, password, attributes);

        String URL = "/user_schemas/" + userSchemaId + "/users" + ((consistent) ? "?consistent=true" : "");
        JsonNode data = postResource(URL, createUserRequest);
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
        return create(username, password, attributes, userSchemaId, false);
    }

    /**
     * Create a new {@link User} on Chino.io under the specified UserSchema
     *
     * @param username the username of the new User
     * @param password the password of the new User
     * @param attributes a JSON object (as a {@link String}) with the attributes of the new User
     * @param userSchemaId the id of the UserSchema
     * @param consistent setting this flag to {@code true} will make the indexing synchronous with the creation of the user,
     *                   i.e. search operations will be successful right after this method returns.
     *                   However, this operation has a cost and can make the API call last for seconds before answering.
     *                   Use only when it's really needed
     *
     * @return the new {@link User} object
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    @Nullable
    public User create(String username, String password, String attributes, String userSchemaId, boolean consistent) throws IOException, ChinoApiException {
        CreateUserRequest createUserRequest=new CreateUserRequest(username, password, fromStringToHashMap(attributes));

        String URL = "/user_schemas/" + userSchemaId + "/users" + ((consistent) ? "?consistent=true" : "");
        JsonNode data = postResource(URL, createUserRequest);
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
        return updatePartial(false, userId, attributes, false);
    }

    /**
     * Update some fields of a User.
     *
     * @param activateResource if true, the update method will set {@code "is_active": true} in the resource.
     *                         Otherwise, the value of is_active will not be modified.
     * @param userId the User's ID on Chino.io
     * @param attributes a {@link HashMap} with the new values of the User's attributes
     *
     * @return the updated {@link User} object
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    @Nullable
    public User updatePartial(boolean activateResource, String userId, HashMap attributes) throws IOException, ChinoApiException {
        return updatePartial(activateResource, userId, attributes, false);
    }

    /**
     * Update some fields of a User.
     *
     * @param userId the User's ID on Chino.io
     * @param attributes a {@link HashMap} with the new values of the User's attributes
     * @param consistent setting this flag to {@code true} will make the indexing synchronous with the creation of the user,
     *                   i.e. search operations will be successful right after this method returns.
     *                   However, this operation has a cost and can make the API call last for seconds before answering.
     *                   Use only when it's really needed
     *
     * @return the updated {@link User} object
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    @Nullable
    public User updatePartial(String userId, HashMap attributes, boolean consistent) throws IOException, ChinoApiException {
        return updatePartial(false, userId, attributes, consistent);
    }

    /**
     * Update some fields of a User.
     *
     * @param activateResource if true, the update method will set {@code "is_active": true} in the resource.
     *                         Otherwise, the value of is_active will not be modified.
     * @param userId the User's ID on Chino.io
     * @param attributes a {@link HashMap} with the new values of the User's attributes
     * @param consistent setting this flag to {@code true} will make the indexing synchronous with the creation of the user,
     *                   i.e. search operations will be successful right after this method returns.
     *                   However, this operation has a cost and can make the API call last for seconds before answering.
     *                   Use only when it's really needed
     *
     * @return the updated {@link User} object
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    @Nullable
    public User updatePartial(boolean activateResource, String userId, HashMap attributes, boolean consistent) throws IOException, ChinoApiException {
        checkNotNull(userId, "user_id");

        CreateUserRequest createUserRequest= new CreateUserRequest();
        if (attributes.containsKey("username")) {
            createUserRequest.setUsername((String) attributes.remove("username"));
        }
        if (attributes.containsKey("password")) {
            createUserRequest.setPassword((String) attributes.remove("password"));
        }
        createUserRequest.setAttributes(attributes);

        if (activateResource)
            createUserRequest.activateResource();

        String URL = "/users/" + userId + ((consistent) ? "?consistent=true" : "");
        JsonNode data = patchResource(URL, createUserRequest);
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
        return updatePartial(false, userId, attributes, false);
    }


    /**
     * Update some fields of a User.
     *
     * @param activateResource if true, the update method will set {@code "is_active": true} in the resource.
     *                         Otherwise, the value of is_active will not be modified.
     * @param userId the User's ID on Chino.io
     * @param attributes a JSON object (as a {@link String}) with the new values of the User's attributes
     *
     * @return the updated {@link User} object
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    @Nullable
    public User updatePartial(boolean activateResource, String userId, String attributes) throws IOException, ChinoApiException {
        return updatePartial(activateResource, userId, attributes, false);
    }

    /**
     * Update some fields of a User.
     *
     * @param userId the User's ID on Chino.io
     * @param attributes a JSON object (as a {@link String}) with the new values of the User's attributes
     * @param consistent setting this flag to {@code true} will make the indexing synchronous with the creation of the user,
     *                   i.e. search operations will be successful right after this method returns.
     *                   However, this operation has a cost and can make the API call last for seconds before answering.
     *                   Use only when it's really needed
     *
     *
     * @return the updated {@link User} object
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    @Nullable
    public User updatePartial(String userId, String attributes, boolean consistent) throws IOException, ChinoApiException {
        checkNotNull(userId, "user_id");
        HashMap attrMap =  fromStringToHashMap(attributes);

        return updatePartial(false, userId, attrMap, consistent);
    }

    /**
     * Update some fields of a User.
     *
     * @param activateResource if true, the update method will set {@code "is_active": true} in the resource.
     *                         Otherwise, the value of is_active will not be modified.
     * @param userId the User's ID on Chino.io
     * @param attributes a JSON object (as a {@link String}) with the new values of the User's attributes
     * @param consistent setting this flag to {@code true} will make the indexing synchronous with the creation of the user,
     *                   i.e. search operations will be successful right after this method returns.
     *                   However, this operation has a cost and can make the API call last for seconds before answering.
     *                   Use only when it's really needed
     *
     *
     * @return the updated {@link User} object
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    @Nullable
    public User updatePartial(boolean activateResource, String userId, String attributes, boolean consistent) throws IOException, ChinoApiException {
        checkNotNull(userId, "user_id");
        HashMap attrMap =  fromStringToHashMap(attributes);

        return updatePartial(activateResource, userId, attrMap, consistent);
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
        return update(false, userId, username, password, attributes, false);
    }

    /**
     * Update a {@link User} object.
     *
     * @param activateResource if true, the update method will set {@code "is_active": true} in the resource.
     *                         Otherwise, the value of is_active will not be modified.
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
    public User update(boolean activateResource, String userId, String username, String password, HashMap attributes) throws IOException, ChinoApiException {
        return update(activateResource, userId, username, password, attributes, false);
    }

    /**
     * Update a {@link User} object.
     *
     * @param userId the User's ID on Chino.io
     * @param username the username of the User
     * @param password the password of the User
     * @param attributes an HashMap with the new values of the User's attributes.
     *                   You must provide <b><i> all </i></b> of the attributes that are defined for the User.
     * @param consistent setting this flag to {@code true} will make the indexing synchronous with the creation of the user,
     *                   i.e. search operations will be successful right after this method returns.
     *                   However, this operation has a cost and can make the API call last for seconds before answering.
     *                   Use only when it's really needed
     *
     *
     * @return the updated {@link User} object
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    @Nullable
    public User update(String userId, String username, String password, HashMap attributes, boolean consistent) throws IOException, ChinoApiException {
        return update(false, userId, username, password, attributes, consistent);
    }

    /**
     * Update a {@link User} object.
     *
     * @param activateResource if true, the update method will set {@code "is_active": true} in the resource.
     *                         Otherwise, the value of is_active will not be modified.
     * @param userId the User's ID on Chino.io
     * @param username the username of the User
     * @param password the password of the User
     * @param attributes an HashMap with the new values of the User's attributes.
     *                   You must provide <b><i> all </i></b> of the attributes that are defined for the User.
     * @param consistent setting this flag to {@code true} will make the indexing synchronous with the creation of the user,
     *                   i.e. search operations will be successful right after this method returns.
     *                   However, this operation has a cost and can make the API call last for seconds before answering.
     *                   Use only when it's really needed
     *
     *
     * @return the updated {@link User} object
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    @Nullable
    public User update(boolean activateResource, String userId, String username, String password, HashMap attributes, boolean consistent) throws IOException, ChinoApiException {
        checkNotNull(userId, "user_id");
        CreateUserRequest createUserRequest= new CreateUserRequest(username, password, attributes);

        if (activateResource)
            createUserRequest.activateResource();

        String URL = "/users/"+userId + ((consistent) ? "?consistent=true" : "");
        JsonNode data = putResource(URL, createUserRequest);
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
        return update(false, userId, username, password, attributes, false);
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
    public User update(boolean activateResource, String userId, String username, String password, String attributes) throws IOException, ChinoApiException {
        return update(activateResource, userId, username, password, attributes, false);
    }

    /**
     * Update a {@link User} object.
     *
     * @param userId the User's ID on Chino.io
     * @param username the username of the User
     * @param password the password of the User
     * @param attributes a JSON object (as a {@link String}) with the new values of the User's attributes
     * @param consistent setting this flag to {@code true} will make the indexing synchronous with the creation of the
     *                   user,
     *                   i.e. search operations will be successful right after this method returns.
     *                   However, this operation has a cost and can make the API call last for seconds before answering.
     *                   Use only when it's really needed
     *
     * @return the updated {@link User} object
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    @Nullable
    public User update(String userId, String username, String password, String attributes, boolean consistent) throws IOException, ChinoApiException {
        return update(false, userId, username, password, attributes, consistent);
    }

    /**
     * Update a {@link User} object.
     *
     * @param userId the User's ID on Chino.io
     * @param username the username of the User
     * @param password the password of the User
     * @param attributes a JSON object (as a {@link String}) with the new values of the User's attributes
     * @param consistent setting this flag to {@code true} will make the indexing synchronous with the creation of the
     *                   user,
     *                   i.e. search operations will be successful right after this method returns.
     *                   However, this operation has a cost and can make the API call last for seconds before answering.
     *                   Use only when it's really needed
     *
     * @return the updated {@link User} object
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    @Nullable
    public User update(boolean activateResource, String userId, String username, String password, String attributes, boolean consistent) throws IOException, ChinoApiException {
        checkNotNull(userId, "user_id");
        CreateUserRequest createUserRequest= new CreateUserRequest(username, password, fromStringToHashMap(attributes));

        if (activateResource)
            createUserRequest.activateResource();

        String URL = "/users/"+userId + ((consistent) ? "?consistent=true" : "");
        JsonNode data = putResource(URL, createUserRequest);
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
