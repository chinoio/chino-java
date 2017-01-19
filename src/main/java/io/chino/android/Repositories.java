package io.chino.android;

import com.fasterxml.jackson.databind.JsonNode;
import io.chino.api.common.ChinoApiConstants;
import io.chino.api.common.ChinoApiException;
import io.chino.api.repository.GetRepositoriesResponse;
import io.chino.api.repository.GetRepositoryResponse;
import io.chino.api.repository.Repository;
import okhttp3.OkHttpClient;
import java.io.IOException;

public class Repositories extends ChinoBaseAPI {

    public Repositories(String hostUrl, OkHttpClient clientInitialized){
        super(hostUrl, clientInitialized);
    }

    /**
     * Returns a list of Repositories
     * @param offset the offset from which it retrieves the Repositories
     * @param limit number of results (max {@link io.chino.api.common.ChinoApiConstants#QUERY_DEFAULT_LIMIT})
     * @return GetRepositoriesResponse Object which contains the list of Repositories
     * @throws IOException
     * @throws ChinoApiException
     */
    public GetRepositoriesResponse list(int offset, int limit) throws IOException, ChinoApiException {
        JsonNode data = getResource("/repositories", offset, limit);
        if(data!=null)
            return mapper.convertValue(data, GetRepositoriesResponse.class);

        return null;
    }

    /**
     * Returns a list of Repositories
     * @return GetRepositoriesResponse Object which contains the list of Repositories
     * @throws IOException
     * @throws ChinoApiException
     */
    public GetRepositoriesResponse list() throws IOException, ChinoApiException {
        JsonNode data = getResource("/repositories", 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return mapper.convertValue(data, GetRepositoriesResponse.class);

        return null;
    }

    /**
     * It retrieves a Repository
     * @param repositoryId the id of the Repository
     * @return Repository Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public Repository read(String repositoryId) throws IOException, ChinoApiException{
        JsonNode data = getResource("/repositories/"+repositoryId, 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return mapper.convertValue(data, GetRepositoryResponse.class).getRepository();

        return null;
    }

    /**
     * It creates a Repository
     * @param description the description of the Repository
     * @return Repository Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public Repository create(String description) throws IOException, ChinoApiException {
        String createRepoRequest="{\"description\": \""+description+"\"}";
        JsonNode createRepoRequestNode= mapper.readValue(createRepoRequest, JsonNode.class);
        JsonNode data = postResource("/repositories", createRepoRequestNode);
        if(data!=null)
            return mapper.convertValue(data, GetRepositoryResponse.class).getRepository();

        return null;
    }

    /**
     * It updates a Repository
     * @param repositoryId the id of the Repository
     * @param description the description of the new Repository
     * @return Repository Object updated
     * @throws IOException
     * @throws ChinoApiException
     */
    public Repository update(String repositoryId, String description) throws IOException, ChinoApiException {
        String createRepoRequest="{\"description\": \""+description+"\"}";
        JsonNode createRepoRequestNode= mapper.readValue(createRepoRequest, JsonNode.class);

        JsonNode data = putResource("/repositories/"+repositoryId, createRepoRequestNode);
        if(data!=null)
            return mapper.convertValue(data, GetRepositoryResponse.class).getRepository();
        return null;
    }

    /**
     * It deletes a Repository
     * @param repositoryId the id o the Repository
     * @param force if true, the resource cannot be restored
     * @return a String with the result of the operation
     * @throws IOException
     * @throws ChinoApiException
     */
    public String delete(String repositoryId, boolean force) throws IOException, ChinoApiException {
        return deleteResource("/repositories/"+repositoryId, force);
    }
}
