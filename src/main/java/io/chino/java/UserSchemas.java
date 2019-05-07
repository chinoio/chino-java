package io.chino.java;

import com.fasterxml.jackson.databind.JsonNode;
import io.chino.api.common.ChinoApiConstants;
import io.chino.api.common.ChinoApiException;
import io.chino.api.common.Field;
import io.chino.api.userschema.*;

import java.io.IOException;
import java.util.List;

/**
 * Manage the {@link UserSchema UserSchemas} that define attributes stored for your {@link io.chino.api.user.User Users}.
 */
public class UserSchemas extends ChinoBaseAPI {

    /**
     * The default constructor used by all {@link ChinoBaseAPI} subclasses
     *
     * @param baseApiUrl      the base URL of the Chino.io API. For testing, use:<br>
     *                        {@code https://api.test.chino.io/v1/}
     * @param parentApiClient the instance of {@link ChinoAPI} that created this object
     */
    public UserSchemas(String baseApiUrl, ChinoAPI parentApiClient) {
        super(baseApiUrl, parentApiClient);
    }

    /**
     * List all the {@link UserSchema UserSchemas} in the account
     *
     * @param offset page offset of the results.
     * @param limit the max amount of results to be returned.
     *
     * @return a {@link GetUserSchemasResponse} that wraps a {@link List} of {@link UserSchema UserSchemas}
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public GetUserSchemasResponse list(int offset, int limit) throws IOException, ChinoApiException {
        JsonNode data = getResource("/user_schemas", offset, limit);
        if(data!=null)
            return getMapper().convertValue(data, GetUserSchemasResponse.class);
        return null;
    }

    /**
     * List all the {@link UserSchema UserSchemas} in the account
     *
     * @return a {@link GetUserSchemasResponse} that wraps a {@link List} of {@link UserSchema UserSchemas}
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public GetUserSchemasResponse list() throws IOException, ChinoApiException {
        JsonNode data = getResource("/user_schemas", 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return getMapper().convertValue(data, GetUserSchemasResponse.class);
        return null;
    }

    /**
     * Read a specific {@link UserSchema}
     *
     * @param userSchemaId the id of the {@link UserSchema} on Chino.io
     *
     * @return the requested {@link UserSchema}
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public UserSchema read(String userSchemaId) throws IOException, ChinoApiException{
        checkNotNull(userSchemaId, "user_schema_id");
        JsonNode data = getResource("/user_schemas/"+userSchemaId, 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return getMapper().convertValue(data, GetUserSchemaResponse.class).getUserSchema();
        return null;
    }

    /**
     * Create a new {@link UserSchema} on Chino.io
     *
     * @param description a description of the new {@link UserSchema}
     * @param userSchemaStructure a {@link UserSchemaStructure} object which describes the structure of the new UserSchema.
     *
     * @return the new {@link UserSchema}
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public UserSchema create(String description, UserSchemaStructure userSchemaStructure) throws IOException, ChinoApiException {
        checkNotNull(userSchemaStructure, "user_schema_structure");
        UserSchemaRequest userSchemaRequest= new UserSchemaRequest(description, userSchemaStructure);

        JsonNode data = postResource("/user_schemas", userSchemaRequest);
        if(data!=null)
            return getMapper().convertValue(data, GetUserSchemaResponse.class).getUserSchema();

        return null;
    }

    /**
     * Create a new {@link UserSchema} on Chino.io
     *
     * @param userSchemaRequest a {@link UserSchemaRequest} Object which contains a description and the
     *                          UserSchema's fields
     *
     * @return the new {@link UserSchema}
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public UserSchema create(UserSchemaRequest userSchemaRequest) throws IOException, ChinoApiException {
        checkNotNull(userSchemaRequest, "user_schema_request");
        JsonNode data = postResource("/user_schemas", userSchemaRequest);
        if(data!=null)
            return getMapper().convertValue(data, GetUserSchemaResponse.class).getUserSchema();

        return null;
    }

    /**
     * Create a new {@link UserSchema} on Chino.io. The attributes of the schema are inferred from the fields
     * of class "myClass".
     *
     * @param description a description of the new {@link UserSchema}
     * @param myClass a Java {@link Class} that represents the structure of the new UserSchema.
     *                Mark fields that need to be indexed with the annotation
     *                {@link io.chino.api.common.indexed @indexed}.
     *
     * @return the new {@link UserSchema}
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
     public UserSchema create(String description, Class myClass) throws IOException, ChinoApiException {
        List<Field> fieldsList = returnFields(myClass);
        UserSchemaStructure userSchemaStructure = new UserSchemaStructure(fieldsList);
        UserSchemaRequest schemaRequest= new UserSchemaRequest(description, userSchemaStructure);

        JsonNode data = postResource("/user_schemas", schemaRequest);
        if(data!=null)
            return getMapper().convertValue(data, GetUserSchemaResponse.class).getUserSchema();

        return null;
    }

    /**
     * Update the specified {@link UserSchema}
     *
     * @param activateResource if true, the update method will set {@code "is_active": true} in the resource.
     *                         Otherwise, the value of is_active will not be modified.
     * @param userSchemaId the id of the {@link UserSchema} on Chino.io
     * @param userSchemaRequest a {@link UserSchemaRequest} Object which contains the new description and the
     *                          new structure of the UserSchema's fields
     *
     * @return the updated {@link UserSchema}
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public UserSchema update(boolean activateResource, String userSchemaId, UserSchemaRequest userSchemaRequest) throws IOException, ChinoApiException {
        checkNotNull(userSchemaId, "user_schema_id");
        checkNotNull(userSchemaRequest, "user_schema_request");
        if (activateResource)
            userSchemaRequest.activateResource();
        JsonNode data = putResource("/user_schemas/"+userSchemaId, userSchemaRequest);
        if(data!=null)
            return getMapper().convertValue(data, GetUserSchemaResponse.class).getUserSchema();
        return null;
    }

    /**
     * Update the specified {@link UserSchema}
     *
     * @param userSchemaId the id of the {@link UserSchema} on Chino.io
     * @param userSchemaRequest a {@link UserSchemaRequest} Object which contains the new description and the
     *                          new structure of the UserSchema's fields
     *
     * @return the updated {@link UserSchema}
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public UserSchema update(String userSchemaId, UserSchemaRequest userSchemaRequest) throws IOException, ChinoApiException {
        return update(false, userSchemaId, userSchemaRequest);
    }

    /**
     * Update the specified {@link UserSchema}
     *
     * @param userSchemaId the id of the {@link UserSchema} on Chino.io
     * @param description the new description for the UserSchema
     * @param userSchemaStructure a {@link UserSchemaStructure} object which describes the new structure
     *                            of the UserSchema.
     *
     * @return the updated {@link UserSchema}
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public UserSchema update(String userSchemaId, String description, UserSchemaStructure userSchemaStructure) throws IOException, ChinoApiException {
        return update(false, userSchemaId, new UserSchemaRequest(description, userSchemaStructure));
    }

    /**
     * Update the specified {@link UserSchema}
     *
     * @param activateResource if true, the update method will set {@code "is_active": true} in the resource.
     *                         Otherwise, the value of is_active will not be modified.
     * @param userSchemaId the id of the {@link UserSchema} on Chino.io
     * @param description the new description for the UserSchema
     * @param userSchemaStructure a {@link UserSchemaStructure} object which describes the new structure
     *                            of the UserSchema.
     *
     * @return the updated {@link UserSchema}
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public UserSchema update(boolean activateResource, String userSchemaId, String description, UserSchemaStructure userSchemaStructure) throws IOException, ChinoApiException {
        checkNotNull(userSchemaId, "user_schema_id");
        checkNotNull(userSchemaStructure, "user_schema_structure");

        return update(activateResource, userSchemaId, new UserSchemaRequest(description, userSchemaStructure));
    }

    /**
     * Update the specified {@link UserSchema}
     *
     * @param userSchemaId the id of the {@link UserSchema} on Chino.io
     * @param description the new description of the {@link UserSchema}
     * @param myClass a Java {@link Class} that represents the new structure of the UserSchema.
     *                Mark fields that need to be indexed with the annotation
     *                {@link io.chino.api.common.indexed @indexed}.
     *
     * @return the updated {@link UserSchema}
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public UserSchema update(String userSchemaId, String description, Class myClass) throws IOException, ChinoApiException {
        return update(false, userSchemaId, description, myClass);
    }

    /**
     * Update the specified {@link UserSchema}
     *
     * @param activateResource if true, the update method will set {@code "is_active": true} in the resource.
     *                         Otherwise, the value of is_active will not be modified.
     * @param userSchemaId the id of the {@link UserSchema} on Chino.io
     * @param description the new description of the {@link UserSchema}
     * @param myClass a Java {@link Class} that represents the new structure of the UserSchema.
     *                Mark fields that need to be indexed with the annotation
     *                {@link io.chino.api.common.indexed @indexed}.
     *
     * @return the updated {@link UserSchema}
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public UserSchema update(boolean activateResource, String userSchemaId, String description, Class myClass) throws IOException, ChinoApiException {
        checkNotNull(userSchemaId, "user_schema_id");
        List<Field> fieldsList = returnFields(myClass);
        UserSchemaStructure userSchemaStructure = new UserSchemaStructure(fieldsList);
        UserSchemaRequest userSchemaRequest= new UserSchemaRequest(description, userSchemaStructure);

        return update(activateResource, userSchemaId, userSchemaRequest);
    }

    /**
     * Delete a {@link UserSchema} from Chino.io
     *
     * @param userSchemaId the id of the {@link UserSchema}
     * @param force if {@code true}, the resource will be deleted forever. Otherwise, it will only be deactivated.
     *
     * @return a String with the result of the operation
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public String delete(String userSchemaId, boolean force) throws IOException, ChinoApiException {
        checkNotNull(userSchemaId, "user_schema_id");
        return deleteResource("/user_schemas/"+userSchemaId, force);
    }
}
