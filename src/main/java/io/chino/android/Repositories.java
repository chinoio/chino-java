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
     * Used to get a list of Repositories
     * @param offset the offset
     * @return a GetRepositoriesResponse Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public GetRepositoriesResponse list(int offset) throws IOException, ChinoApiException {
        JsonNode data = getResource("/repositories", offset, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return mapper.convertValue(data, GetRepositoriesResponse.class);

        return null;
    }

    /**
     * Used to read a Repository
     * @param repositoryId the id of the Repository
     * @return a Repository Object
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
     * Used to create a Repository
     * @param description the description of the Repository
     * @return a Repository Object
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
     * Used to update a Repository
     * @param repositoryId the id of the Repository
     * @param description the description of the new Repository
     * @return a Repository Object
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
     * Used to delete a Repository
     * @param repositoryId the id o the Repository
     * @param force the boolean force
     * @return a String that represents the result of the operation
     * @throws IOException
     * @throws ChinoApiException
     */
    public String delete(String repositoryId, boolean force) throws IOException, ChinoApiException {
        return deleteResource("/repositories/"+repositoryId, force);
    }
}
