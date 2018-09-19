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
     * List all the {@link Schema Schemas} inside a {@link io.chino.api.repository.Repository Repository}.
     *
     * @param repositoryId the ID of the {@link io.chino.api.repository.Repository Repository}
     * @param offset page offset of the results.
     * @param limit the max amount of results to be returned.
     *
     * @return A {@link GetSchemasResponse} that wraps a list of {@link Schema Schemas}
     *
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
     * List all the {@link Schema Schemas} inside a {@link io.chino.api.repository.Repository Repository}.
     *
     * @param repositoryId the ID of the {@link io.chino.api.repository.Repository Repository}
     *
     * @return A {@link GetSchemasResponse} that wraps a list of {@link Schema Schemas}
     *
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
     * Read a specific Schema from Chino.io
     *
     * @param schemaId the ID of the {@link Schema}
     *
     * @return the requested {@link Schema}
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
     * Create a new {@link Schema} on Chino.io
     *
     * @param repositoryId the id of the {@link io.chino.api.repository.Repository Repository}
     * @param schemaRequest a {@link SchemaRequest} which contains the new {@link Schema}'s structure
     *
     * @return the new {@link Schema}
     *
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
     * Create a new {@link Schema} on Chino.io, based on the fields of a Java class
     *
     * @param repositoryId the ID of the {@link io.chino.api.repository.Repository}
     * @param description the description of the {@link Schema}
     * @param myClass a {@link Class} that represents the structure of the new {@link Schema}
     *
     * @return the new {@link Schema}
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     *
     * @see io.chino.api.common.indexed @indexed
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
     * Create a new {@link Schema} on Chino.io
     *
     * @param repositoryId the id of the {@link io.chino.api.repository.Repository Repository}
     * @param schemaStructure a {@link SchemaStructure} which contains the new {@link Schema}'s structure
     *
     * @return the new {@link Schema}
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public Schema create(String repositoryId, String description, SchemaStructure schemaStructure) throws IOException, ChinoApiException {
        checkNotNull(repositoryId, "repository_id");
        SchemaRequest schemaRequest= new SchemaRequest(description, schemaStructure);

        return create(repositoryId, schemaRequest);
    }

    /**
     * Update an existing {@link Schema}
     *
     * @param schemaId the ID of the Schema to update
     * @param description the new description of the Schema
     * @param schemaStructure a {@link SchemaStructure} object that contins the new structure of the Schema
     *
     * @return the updated {@link Schema}
     *
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
     * Update an existing {@link Schema}
     *
     * @param schemaId the ID of the Schema to update
     * @param schemaRequest a {@link SchemaStructure} object that contains the new description
     *                      and structure of the Schema
     *
     * @return the updated {@link Schema}
     *
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
     * Update an existing {@link Schema}
     *
     * @param schemaId the ID of the Schema to update
     * @param description the new description of the Schema
     * @param myClass {@link Class} that represents the new structure of the {@link Schema}
     *
     * @return the updated {@link Schema}
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     *
     * @see io.chino.api.common.indexed @indexed
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
     * Delete a {@link Schema} from Chino.io
     *
     * @param schemaId the ID of the Schema
     * @param force if true, the resource cannot be restored. Otherwise, the Schema is just deactivated
     *
     * @return a String with the result of the operation
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public String delete(String schemaId, boolean force) throws IOException, ChinoApiException {
        checkNotNull(schemaId, "schema_id");
        return deleteResource("/schemas/"+schemaId, force);
    }

}
