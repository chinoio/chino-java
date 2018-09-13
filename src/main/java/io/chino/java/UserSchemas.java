package io.chino.java;

import com.fasterxml.jackson.databind.JsonNode;
import io.chino.api.common.ChinoApiConstants;
import io.chino.api.common.ChinoApiException;
import io.chino.api.common.Field;
import io.chino.api.userschema.*;

import java.io.IOException;
import java.util.List;

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
     * Returns a list of UserSchemas
     * @param offset the offset from which it retrieves the UserSchemas
     * @param limit number of results (max {@link io.chino.api.common.ChinoApiConstants#QUERY_DEFAULT_LIMIT})
     * @return GetUserSchemasResponse Object with the list of UserSchemas
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
     * Returns a list of UserSchemas
     * @return GetUserSchemasResponse Object with the list of UserSchemas
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
     * It reads a specific UserSchema
     * @param userSchemaId the id of the UserSchema
     * @return UserSchema Object
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
     * It creates a new UserSchema
     * @param description the description
     * @param userSchemaStructure the UserSchemaStructure Object
     * @return UserSchema Object
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
     * It creates a new UserSchema
     * @param userSchemaRequest the UserSchemaRequest Object
     * @return UserSchema Object
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
     * It creates a new UserSchema based on the variables in the class "myClass"
     * @param description the description
     * @param myClass the Class that represents the structure of the UserSchema. Mark fields that need to be indexed
     *                with the annotation  {@link io.chino.api.common.indexed @indexed}.
     * @return UserSchema Object
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
     * It updates a UserSchema
     * @param userSchemaId the id of the UserSchema
     * @param description the description
     * @param userSchemaStructure the UserSchemaStructure Object
     * @return UserSchema Object
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public UserSchema update(String userSchemaId, String description, UserSchemaStructure userSchemaStructure) throws IOException, ChinoApiException {
        checkNotNull(userSchemaId, "user_schema_id");
        checkNotNull(userSchemaStructure, "user_schema_structure");
        UserSchemaRequest userSchemaRequest= new UserSchemaRequest(description, userSchemaStructure);

        JsonNode data = putResource("/user_schemas/"+userSchemaId, userSchemaRequest);
        if(data!=null)
            return getMapper().convertValue(data, GetUserSchemaResponse.class).getUserSchema();
        return null;
    }

    /**
     * It updates a UserSchema
     * @param userSchemaId the id of the UserSchema
     * @param userSchemaRequest the UserSchemaRequest Object
     * @return UserSchema Object
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
     public UserSchema update(String userSchemaId, UserSchemaRequest userSchemaRequest) throws IOException, ChinoApiException {
         checkNotNull(userSchemaId, "user_schema_id");
         checkNotNull(userSchemaRequest, "user_schema_request");
         JsonNode data = putResource("/user_schemas/"+userSchemaId, userSchemaRequest);
         if(data!=null)
             return getMapper().convertValue(data, GetUserSchemaResponse.class).getUserSchema();
         return null;
    }

    /**
     * It updates a UserSchema based on the variables in the class "myClass"
     * @param userSchemaId the id of the UserSchema
     * @param description the description
     * @param myClass the Class that represents the structure of the UserSchema
     * @return UserSchema Object
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public UserSchema update(String userSchemaId, String description, Class myClass) throws IOException, ChinoApiException {
        checkNotNull(userSchemaId, "user_schema_id");
        List<Field> fieldsList = returnFields(myClass);
        UserSchemaStructure userSchemaStructure = new UserSchemaStructure(fieldsList);
        UserSchemaRequest userSchemaRequest= new UserSchemaRequest(description, userSchemaStructure);

        JsonNode data = putResource("/user_schemas/"+userSchemaId, userSchemaRequest);
        if(data!=null)
            return getMapper().convertValue(data, GetUserSchemaResponse.class).getUserSchema();
        return null;
    }

    /**
     * It deletes a UserSchema
     * @param userSchemaId the id of the UserSchema
     * @param force if true, the resource cannot be restored
     * @return a String with the result of the operation
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public String delete(String userSchemaId, boolean force) throws IOException, ChinoApiException {
        checkNotNull(userSchemaId, "user_schema_id");
        return deleteResource("/user_schemas/"+userSchemaId, force);
    }
}
