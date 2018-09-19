package io.chino.java;

import com.fasterxml.jackson.databind.JsonNode;
import io.chino.api.common.ChinoApiConstants;
import io.chino.api.common.ChinoApiException;
import io.chino.api.common.Field;
import io.chino.api.schema.*;

import java.io.IOException;
import java.util.List;

public class Schemas extends ChinoBaseAPI {

    /**
     * The default constructor used by all {@link ChinoBaseAPI} subclasses
     *
     * @param baseApiUrl      the base URL of the Chino.io API. For testing, use:<br>
     *                        {@code https://api.test.chino.io/v1/}
     * @param parentApiClient the instance of {@link ChinoAPI} that created this object
     */
    public Schemas(String baseApiUrl, ChinoAPI parentApiClient) {
        super(baseApiUrl, parentApiClient);
    }

    /**
     * Returns a list of Schemas
     * @param repositoryId the id of the Repository
     * @param offset the offset from which it retrieves the Schemas
     * @param limit number of results (max {@link io.chino.api.common.ChinoApiConstants#QUERY_DEFAULT_LIMIT ChinoApiConstants.QUERY_DEFAULT_LIMIT})
     * @return GetSchemasResponse Object which contains the list of Schemas
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public GetSchemasResponse list(String repositoryId, int offset, int limit) throws IOException, ChinoApiException {
        checkNotNull(repositoryId, "repository_id");
        JsonNode data = getResource("/repositories/"+repositoryId+"/schemas", offset, limit);
        if(data!=null)
            return mapper.convertValue(data, GetSchemasResponse.class);
        return null;
    }

    /**
     * Returns a list of Schemas
     * @param repositoryId the id of the Repository
     * @return GetSchemasResponse Object which contains the list of Schemas
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public GetSchemasResponse list(String repositoryId) throws IOException, ChinoApiException {
        checkNotNull(repositoryId, "repository_id");
        JsonNode data = getResource("/repositories/"+repositoryId+"/schemas", 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return mapper.convertValue(data, GetSchemasResponse.class);
        return null;
    }

    /**
     * It retrieves a specific Schema
     * @param schemaId the id of the Schema
     * @return Schema Object
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public Schema read(String schemaId) throws IOException, ChinoApiException{
        checkNotNull(schemaId, "schema_id");
        JsonNode data = getResource("/schemas/"+schemaId, 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return mapper.convertValue(data, GetSchemaResponse.class).getSchema();

        return null;
    }

    /**
     * It creates a new Schema
     * @param repositoryId the id of the Repository
     * @param schemaRequest the SchemaRequest Object
     * @return Schema Object
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public Schema create(String repositoryId, SchemaRequest schemaRequest) throws IOException, ChinoApiException {
        checkNotNull(repositoryId, "repository_id");
        checkNotNull(schemaRequest, "schema_request");
        JsonNode data = postResource("/repositories/"+repositoryId+"/schemas", schemaRequest);
        if(data!=null)
            return mapper.convertValue(data, GetSchemaResponse.class).getSchema();

        return null;
    }

    /**
     * It creates a new Schema based on the variables in the class "myClass"
     * @param repositoryId the id of the Repository
     * @param description the description
     * @param myClass the Class that represents the structure of the Schema
     * @return Schema Object
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public Schema create(String repositoryId, String description, Class myClass) throws IOException, ChinoApiException {
        checkNotNull(repositoryId, "repository_id");
        SchemaRequest schemaRequest = new SchemaRequest();
        schemaRequest.setDescription(description);
        SchemaStructure schemaStructure = new SchemaStructure();
        List<Field> fieldsList = returnFields(myClass);
        schemaStructure.setFields(fieldsList);
        schemaRequest.setStructure(schemaStructure);

        JsonNode data = postResource("/repositories/"+repositoryId+"/schemas", schemaRequest);
        if(data!=null)
            return mapper.convertValue(data, GetSchemaResponse.class).getSchema();

        return null;
    }

    /**
     * It creates a new Schema
     * @param repositoryId the id of the Repository
     * @param description the description
     * @param schemaStructure the SchemaStructure Object
     * @return Schema Object
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public Schema create(String repositoryId, String description, SchemaStructure schemaStructure) throws IOException, ChinoApiException {
        checkNotNull(repositoryId, "repository_id");
        SchemaRequest schemaRequest= new SchemaRequest(description, schemaStructure);

        return create(repositoryId, schemaRequest);
    }

    /**
     * It updates a Schema
     * @param schemaId the id of the Schema
     * @param description the description
     * @param schemaStructure the SchemaStructure Object
     * @return Schema Object
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public Schema update(String schemaId, String description, SchemaStructure schemaStructure) throws IOException, ChinoApiException {
        checkNotNull(schemaId, "schema_id");
        SchemaRequest schemaRequest = new SchemaRequest(description, schemaStructure);

        JsonNode data = putResource("/schemas/"+schemaId, schemaRequest);
        if(data!=null)
            return mapper.convertValue(data, GetSchemaResponse.class).getSchema();

        return null;
    }

    /**
     * It updates a Schema
     * @param schemaId the id of the Schema
     * @param schemaRequest the SchemaRequest Object
     * @return Schema Object
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public Schema update(String schemaId, SchemaRequest schemaRequest) throws IOException, ChinoApiException {
        checkNotNull(schemaId, "schema_id");
        JsonNode data = putResource("/schemas/"+schemaId, schemaRequest);
        if(data!=null)
            return mapper.convertValue(data, GetSchemaResponse.class).getSchema();

        return null;
    }

    /**
     * It updates a Schema based on the variables in the class "myClass"
     * @param schemaId the id of the Schema
     * @param description the description
     * @param myClass the Class that represents the structure of the Schema
     * @return Schema Object
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public Schema update(String schemaId, String description, Class myClass) throws IOException, ChinoApiException {
        checkNotNull(schemaId, "schema_id");
        SchemaRequest schemaRequest = new SchemaRequest();
        schemaRequest.setDescription(description);
        SchemaStructure schemaStructure = new SchemaStructure();
        List<Field> fieldsList = returnFields(myClass);
        schemaStructure.setFields(fieldsList);
        schemaRequest.setStructure(schemaStructure);

        JsonNode data = putResource("/schemas/"+schemaId, schemaRequest);
        if(data!=null)
            return mapper.convertValue(data, GetSchemaResponse.class).getSchema();

        return null;
    }

    /**
     * It deletes a Schema
     * @param schemaId the id of the Schema
     * @param force if true, the resource cannot be restored
     * @return a String with the result of the operation
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public String delete(String schemaId, boolean force) throws IOException, ChinoApiException {
        checkNotNull(schemaId, "schema_id");
        return deleteResource("/schemas/"+schemaId, force);
    }

}
