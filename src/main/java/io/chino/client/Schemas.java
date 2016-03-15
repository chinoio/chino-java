package io.chino.client;

import io.chino.api.common.ChinoApiConstants;
import io.chino.api.common.ChinoApiException;
import io.chino.api.common.ErrorResponse;
import io.chino.api.schema.*;
import org.codehaus.jackson.JsonNode;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Schemas extends ChinoBaseAPI{

    public Schemas(String hostUrl, Client clientInitialized){
        super(hostUrl, clientInitialized);
    }

    /**
     * Used to get a list of Schemas
     * @param repositoryId the id of the Repository
     * @param offset the offset
     * @return a GetSchemasResponse
     * @throws IOException
     * @throws ChinoApiException
     */
    public GetSchemasResponse list(String repositoryId, int offset) throws IOException, ChinoApiException {
        JsonNode data = getResource("/repositories/"+repositoryId+"/schemas", offset, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return mapper.readValue(data, GetSchemasResponse.class);
        return null;
    }

    /**
     * Used to get a specific Schema
     * @param schemaId the id of the Schema
     * @return a Schema Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public Schema read(String schemaId) throws IOException, ChinoApiException{
        JsonNode data = getResource("/schemas/"+schemaId, 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return mapper.readValue(data, GetSchemaResponse.class).getSchema();

        return null;
    }

    /**
     * Used to create a new Schema
     * @param repositoryId the id of the Repository
     * @param schemaRequest the SchemaRequest Object
     * @return a Schema Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public Schema create(String repositoryId, SchemaRequest schemaRequest) throws IOException, ChinoApiException {
        JsonNode data = postResource("/repositories/"+repositoryId+"/schemas", schemaRequest);
        if(data!=null)
            return mapper.readValue(data, GetSchemaResponse.class).getSchema();

        return null;
    }

    /**
     * Used to create a new Schema
     * @param repositoryId the id of the Repository
     * @param description the description
     * @param myClass the Class that represents the structure of the Schema
     * @return a Schema Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public Schema create(String repositoryId, String description, Class myClass) throws IOException, ChinoApiException {
        SchemaRequest schemaRequest = new SchemaRequest();
        schemaRequest.setDescription(description);
        SchemaStructure schemaStructure = new SchemaStructure();
        List<Field> fieldsList= new ArrayList<Field>();
        java.lang.reflect.Field[] fields = myClass.getFields();
        for(java.lang.reflect.Field field : fields){
            fieldsList.add(new Field(field.getName(), checkType(field.getType())));
        }
        schemaStructure.setFields(fieldsList);
        schemaRequest.setStructure(schemaStructure);

        JsonNode data = postResource("/repositories/"+repositoryId+"/schemas", schemaRequest);
        if(data!=null)
            return mapper.readValue(data, GetSchemaResponse.class).getSchema();

        return null;
    }

    /**
     * Used to create a new Schema
     * @param repositoryId the id of the Repository
     * @param description the description
     * @param schemaStructure the SchemaStructure Object
     * @return a Schema Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public Schema create(String repositoryId, String description, SchemaStructure schemaStructure) throws IOException, ChinoApiException {
        SchemaRequest schemaRequest= new SchemaRequest();
        schemaRequest.setDescription(description);
        schemaRequest.setStructure(schemaStructure);

        return create(repositoryId, schemaRequest);
    }

    /**
     * Used to update a Schema
     * @param schemaId the id of the Schema
     * @param description the description
     * @param schemaStructure the SchemaStructure Object
     * @return a Schema Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public Schema update(String schemaId, String description, SchemaStructure schemaStructure) throws IOException, ChinoApiException {
        SchemaRequest schemaRequest = new SchemaRequest();
        schemaRequest.setDescription(description);
        schemaRequest.setStructure(schemaStructure);

        Response response = client.target(host).path("/schemas/" + schemaId).request(MediaType.APPLICATION_JSON_TYPE).put(Entity.json(schemaRequest));

        String responseString = response.readEntity(String.class);

        if (response.getStatus() == 200) {
            return mapper.readValue(mapper.readTree(responseString).get("data"), GetSchemaResponse.class).getSchema();
        } else {
            throw new ChinoApiException(mapper.readValue(responseString, ErrorResponse.class));
        }
    }

    /**
     * Used to update a Schema
     * @param schemaId the id of the Schema
     * @param schemaRequest the SchemaRequest Object
     * @return a Schema Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public Schema update(String schemaId, SchemaRequest schemaRequest) throws IOException, ChinoApiException {
        Response response = client.target(host).path("/schemas/" + schemaId).request(MediaType.APPLICATION_JSON_TYPE).put(Entity.json(schemaRequest));

        String responseString = response.readEntity(String.class);

        if (response.getStatus() == 200) {
            return mapper.readValue(mapper.readTree(responseString).get("data"), GetSchemaResponse.class).getSchema();
        } else {
            throw new ChinoApiException(mapper.readValue(responseString, ErrorResponse.class));
        }
    }

    /**
     * Used to update a Schema
     * @param schemaId the id of the Schema
     * @param description the description
     * @param myClass the Class that represents the structure of the Schema
     * @return a Schema Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public Schema update(String schemaId, String description, Class myClass) throws IOException, ChinoApiException {
        SchemaRequest schemaRequest = new SchemaRequest();
        schemaRequest.setDescription(description);
        SchemaStructure schemaStructure = new SchemaStructure();
        List<Field> fieldsList= new ArrayList<Field>();
        java.lang.reflect.Field[] fields = myClass.getFields();
        for(java.lang.reflect.Field field : fields){
            fieldsList.add(new Field(field.getName(), checkType(field.getType())));
        }
        schemaStructure.setFields(fieldsList);
        schemaRequest.setStructure(schemaStructure);

        Response response = client.target(host).path("/schemas/" + schemaId).request(MediaType.APPLICATION_JSON_TYPE).put(Entity.json(schemaRequest));

        String responseString = response.readEntity(String.class);

        if (response.getStatus() == 200) {
            return mapper.readValue(mapper.readTree(responseString).get("data"), GetSchemaResponse.class).getSchema();
        } else {
            throw new ChinoApiException(mapper.readValue(responseString, ErrorResponse.class));
        }
    }

    /**
     * Used to delete a Schema
     * @param schemaId the id of the Schema
     * @param force the boolean force
     * @return a String that represents the result of the operation
     * @throws IOException
     * @throws ChinoApiException
     */
    public String delete(String schemaId, boolean force) throws IOException, ChinoApiException {
        return deleteResource("/schemas/"+schemaId, force);
    }

}
