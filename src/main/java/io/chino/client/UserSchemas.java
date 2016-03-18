package io.chino.client;

import io.chino.api.common.ChinoApiConstants;
import io.chino.api.common.ChinoApiException;
import io.chino.api.common.ErrorResponse;
import io.chino.api.userschema.*;
import io.chino.api.userschema.Field;
import org.codehaus.jackson.JsonNode;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserSchemas extends ChinoBaseAPI{
    public UserSchemas(String hostUrl, Client client) {
        super(hostUrl, client);
    }

    /**
     * Used to get a list of UserSchemas
     * @param offset the offset
     * @return a GetUserSchemasResponse Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public GetUserSchemasResponse list(int offset) throws IOException, ChinoApiException {
        JsonNode data = getResource("/user_schemas", offset, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return getMapper().readValue(data, GetUserSchemasResponse.class);
        return null;
    }

    /**
     * Used to read a specific UserSchema
     * @param userSchemaId the id of the UserSchema
     * @return a UserSchema Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public UserSchema read(String userSchemaId) throws IOException, ChinoApiException{
        JsonNode data = getResource("/user_schemas/"+userSchemaId, 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return getMapper().readValue(data, GetUserSchemaResponse.class).getUserSchema();
        return null;
    }

    /**
     * Used to create a new UserSchema
     * @param description the description
     * @param userSchemaStructure the UserSchemaStructure Object
     * @return a UserSchema Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public UserSchema create(String description, UserSchemaStructure userSchemaStructure) throws IOException, ChinoApiException {
        UserSchemaRequest userSchemaRequest= new UserSchemaRequest();
        userSchemaRequest.setDescription(description);
        userSchemaRequest.setStructure(userSchemaStructure);

        JsonNode data = postResource("/user_schemas", userSchemaRequest);
        if(data!=null)
            return getMapper().readValue(data, GetUserSchemaResponse.class).getUserSchema();

        return null;
    }

    /**
     * Used to create a new UserSchema
     * @param userSchemaRequest the UserSchemaRequest Object
     * @return a UserSchema Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public UserSchema create(UserSchemaRequest userSchemaRequest) throws IOException, ChinoApiException {
        JsonNode data = postResource("/user_schemas", userSchemaRequest);
        if(data!=null)
            return getMapper().readValue(data, GetUserSchemaResponse.class).getUserSchema();

        return null;
    }

    /**
     * Used to create a new UserSchema
     * @param description the description
     * @param myClass the Class that represents the structure of the UserSchema
     * @return a UserSchema Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public UserSchema create(String description, Class myClass) throws IOException, ChinoApiException {
        UserSchemaRequest schemaRequest= new UserSchemaRequest();
        schemaRequest.setDescription(description);
        List<Field> fieldsList= new ArrayList<Field>();
        java.lang.reflect.Field[] fields = myClass.getDeclaredFields();
        for(java.lang.reflect.Field field : fields){
            fieldsList.add(new Field(field.getName(), checkType(field.getType())));
        }
        UserSchemaStructure schemaStructure = new UserSchemaStructure();
        schemaStructure.setFields(fieldsList);
        schemaRequest.setStructure(schemaStructure);

        JsonNode data = postResource("/user_schemas", schemaRequest);
        if(data!=null)
            return getMapper().readValue(data, GetUserSchemaResponse.class).getUserSchema();

        return null;
    }

    /**
     * Used to update a UserSchema
     * @param userSchemaId the id of the UserSchema
     * @param description the description
     * @param userSchemaStructure the UserSchemaStructure Object
     * @return a UserSchema Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public UserSchema update(String userSchemaId, String description, UserSchemaStructure userSchemaStructure) throws IOException, ChinoApiException {
        UserSchemaRequest schemaRequest= new UserSchemaRequest();
        schemaRequest.setDescription(description);
        schemaRequest.setStructure(userSchemaStructure);

        Response response = client.target(host).path("/user_schemas/"+userSchemaId).request(MediaType.APPLICATION_JSON_TYPE).put(Entity.json(schemaRequest));

        String responseString=response.readEntity(String.class);

        if(response.getStatus()==200){
            return mapper.readValue(mapper.readTree(responseString).get("data"), GetUserSchemaResponse.class).getUserSchema();
        }else{
            throw new ChinoApiException(mapper.readValue(responseString, ErrorResponse.class));
        }
    }

    /**
     * Used to update a UserSchema
     * @param userSchemaId the id of the UserSchema
     * @param userSchemaRequest the UserSchemaRequest Object
     * @return a UserSchema Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public UserSchema update(String userSchemaId, UserSchemaRequest userSchemaRequest) throws IOException, ChinoApiException {
        Response response = client.target(host).path("/user_schemas/"+userSchemaId).request(MediaType.APPLICATION_JSON_TYPE).put(Entity.json(userSchemaRequest));
        String responseString=response.readEntity(String.class);

        if(response.getStatus()==200){
            return mapper.readValue(mapper.readTree(responseString).get("data"), GetUserSchemaResponse.class).getUserSchema();
        }else{
            throw new ChinoApiException(mapper.readValue(responseString, ErrorResponse.class));
        }
    }

    /**
     * Used to update a UserSchema
     * @param userSchemaId the id of the UserSchema
     * @param description the description
     * @param myClass the Class that represents the structure of the UserSchema
     * @return a UserSchema Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public UserSchema update(String userSchemaId, String description, Class myClass) throws IOException, ChinoApiException {
        UserSchemaRequest userSchemaRequest= new UserSchemaRequest();
        userSchemaRequest.setDescription(description);
        List<Field> fieldsList= new ArrayList<Field>();
        java.lang.reflect.Field[] fields = myClass.getFields();
        for(java.lang.reflect.Field field : fields){
            fieldsList.add(new Field(field.getName(), checkType(field.getType())));
        }
        UserSchemaStructure userSchemaStructure = new UserSchemaStructure();
        userSchemaStructure.setFields(fieldsList);
        userSchemaRequest.setStructure(userSchemaStructure);

        Response response = client.target(host).path("/user_schemas/"+userSchemaId).request(MediaType.APPLICATION_JSON_TYPE).put(Entity.json(userSchemaRequest));
        String responseString=response.readEntity(String.class);

        if(response.getStatus()==200){
            return mapper.readValue(mapper.readTree(responseString).get("data"), GetUserSchemaResponse.class).getUserSchema();
        }else{
            throw new ChinoApiException(mapper.readValue(responseString, ErrorResponse.class));
        }
    }

    /**
     * Used to delete a UserSchema
     * @param userSchemaId the id of the UserSchema
     * @param force the boolean force
     * @return a String that represents the result of the operation
     * @throws IOException
     * @throws ChinoApiException
     */
    public String delete(String userSchemaId, boolean force) throws IOException, ChinoApiException {
        return deleteResource("/user_schemas/"+userSchemaId, force);
    }
}
