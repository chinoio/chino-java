package io.chino.java;

import com.fasterxml.jackson.databind.JsonNode;
import io.chino.api.common.ChinoApiConstants;
import io.chino.api.common.ChinoApiException;
import io.chino.api.repository.GetRepositoriesResponse;
import io.chino.api.repository.GetRepositoryResponse;
import io.chino.api.repository.Repository;

import java.io.IOException;

/**
 * Manage {@link Repository Repositories}, the top-level container for {@link io.chino.api.document.Document Documents}
 * on Chino.io
 */
public class Repositories extends ChinoBaseAPI {

    /**
     * The default constructor used by all {@link ChinoBaseAPI} subclasses
     *
     * @param baseApiUrl      the base URL of the Chino.io API. For testing, use:<br>
     *                        {@code https://api.test.chino.io/v1/}
     * @param parentApiClient the instance of {@link ChinoAPI} that created this object
     */
    public Repositories(String baseApiUrl, ChinoAPI parentApiClient) {
        super(baseApiUrl, parentApiClient);
    }

    /**
     * List all the existing {@link Repository Repositories}
     *
     * @param offset page offset of the results.
     * @param limit the max amount of results to be returned
     *
     * @return A {@link GetRepositoriesResponse} that wraps a list of {@link Repository Repositories}
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public GetRepositoriesResponse list(int offset, int limit) throws IOException, ChinoApiException {
        JsonNode data = getResource("/repositories", offset, limit);
        if(data!=null)
            return mapper.convertValue(data, GetRepositoriesResponse.class);

        return null;
    }

    /**
     * List all the existing {@link Repository Repositories}
     *
     * @return A {@link GetRepositoriesResponse} that wraps a list of {@link Repository Repositories}
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public GetRepositoriesResponse list() throws IOException, ChinoApiException {
        JsonNode data = getResource("/repositories", 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return mapper.convertValue(data, GetRepositoriesResponse.class);

        return null;
    }

    /**
     * Read information about a specific {@link Repository}
     *
     * @param repositoryId the id of the {@link Repository} on Chino.io
     *
     * @return the specified {@link Repository}
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public Repository read(String repositoryId) throws IOException, ChinoApiException {
        checkNotNull(repositoryId, "repository_id");
        JsonNode data = getResource("/repositories/"+repositoryId, 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return mapper.convertValue(data, GetRepositoryResponse.class).getRepository();

        return null;
    }

    /**
     * Create a new {@link Repository} on Chino.io
     *
     * @param description a brief description of the new Repository
     *
     * @return the new {@link Repository}
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public Repository create(String description) throws IOException, ChinoApiException {
        checkNotNull(description, "description");
        String createRepoRequest="{\"description\": \""+description+"\"}";
        JsonNode createRepoRequestNode= mapper.readValue(createRepoRequest, JsonNode.class);
        JsonNode data = postResource("/repositories", createRepoRequestNode);
        if(data!=null)
            return mapper.convertValue(data, GetRepositoryResponse.class).getRepository();

        return null;
    }

    /**
     * Update an existing {@link Repository}
     *
     * @param repositoryId the id of the {@link Repository} on Chino.io
     * @param description the new description of the Repository
     *
     * @return the updated {@link Repository}
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public Repository update(String repositoryId, String description) throws IOException, ChinoApiException {
        return update(false, repositoryId, description);
    }

    /**
     * Update an existing {@link Repository}<br>
     * Use this method with {@code activateResource=true} to make sure that the resource is active when you update it.
     * NOTE: this method can NOT be used to set the resource inactive: use {@link #delete(String, boolean)} instead.
     *
     * @param activateResource if true, the update method will set {@code "is_active": true} in the resource.
     *                         Otherwise, the value will not be modified.
     * @param repositoryId the id of the {@link Repository} on Chino.io
     * @param description the new description of the Repository
     *
     * @return the updated {@link Repository}
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public Repository update(boolean activateResource, String repositoryId, String description) throws IOException, ChinoApiException {
        checkNotNull(repositoryId, "repository_id");
        checkNotNull(description, "description");
        String descriptionField = String.format("\"description\": \"%s\"", description);
        String isActiveField = activateResource
                ? ",\n\"is_active\": true"
                : "";
        String createRepoRequest= "{\n" + descriptionField + isActiveField +"\n}";
        JsonNode createRepoRequestNode= mapper.readValue(createRepoRequest, JsonNode.class);

        JsonNode data = putResource("/repositories/"+repositoryId, createRepoRequestNode);
        if(data!=null)
            return mapper.convertValue(data, GetRepositoryResponse.class).getRepository();
        return null;
    }

    /**
     * Delete a {@link Repository} from Chino.io
     *
     * @param repositoryId the id of the {@link Repository} on Chino.io
     * @param force if true, the resource cannot be restored. Otherwise, it will only be deactivated.
     *
     * @return a String with the result of the operation
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public String delete(String repositoryId, boolean force) throws IOException, ChinoApiException {
        checkNotNull(repositoryId, "repository_id");
        return deleteResource("/repositories/"+repositoryId, force);
    }
}
