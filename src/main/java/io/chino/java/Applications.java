package io.chino.java;

import io.chino.api.application.*;
import io.chino.api.common.ChinoApiConstants;
import io.chino.api.common.ChinoApiException;
import okhttp3.OkHttpClient;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;

public class Applications extends ChinoBaseAPI {
    public Applications(String hostUrl, OkHttpClient clientInitialized) {
        super(hostUrl, clientInitialized);
    }

    /**
     * Returns the list of Applications
     * @param offset the offset from which it retrieves the Applications
     * @param limit number of results (max {@link io.chino.api.common.ChinoApiConstants#QUERY_DEFAULT_LIMIT})
     * @return GetApplicationsResponse Object that contains the list of the Applications
     * @throws IOException
     * @throws ChinoApiException
     */
    public GetApplicationsResponse list(int offset, int limit) throws IOException, ChinoApiException {
        JsonNode data = getResource("/auth/applications", offset, limit);
        if(data!=null)
            return mapper.convertValue(data, GetApplicationsResponse.class);
        return null;
    }

    /**
     * Returns the list of Applications
     * @return GetApplicationsResponse Object that contains the list of the Applications
     * @throws IOException
     * @throws ChinoApiException
     */
    public GetApplicationsResponse list() throws IOException, ChinoApiException {
        JsonNode data = getResource("/auth/applications", 0 , ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return mapper.convertValue(data, GetApplicationsResponse.class);
        return null;
    }

    /**
     * It retrieves the Application requested
     * @param applicationId the id of the Application
     * @return Application Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public Application read(String applicationId) throws IOException, ChinoApiException{
        checkNotNull(applicationId, "application_id");
        JsonNode data = getResource("/auth/applications/"+applicationId, 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return mapper.convertValue(data, GetApplicationResponse.class).getApplication();

        return null;
    }

    /**
     * Create a new {@link ClientType#CONFIDENTIAL "confidential"} Application
     * @param name the name of the Application
     * @param grantType either "authorization-code" or "password",
     * i.e. the authentication method.
     * @param redirectUrl (used only with the "authorization-code" method)
     * @return an {@link Application} object
     * @throws IOException
     * @throws ChinoApiException
     */
    public Application create(String name, String grantType, String redirectUrl) throws IOException, ChinoApiException {
        CreateApplicationRequest applicationRequest = new CreateApplicationRequest(name, grantType, redirectUrl);
        JsonNode data = postResource("/auth/applications", applicationRequest);
        if(data!=null)
            return mapper.convertValue(data, GetApplicationResponse.class).getApplication();

        return null;
    }
    
    /**
     * Create a new {@link Application}.
     * @param name the name of the Application
     * @param grantType either "authorization-code" or "password",
     * i.e. the authentication method.
     * @param redirectUrl (used only with the "authorization-code" method)
     * @param clientType either {@link ClientType#CONFIDENTIAL} or {@link ClientType#PUBLIC}.
     * See <a href="https://docs.chino.io/#header-client-types">Chino.io API Docs</a>
     * to learn more about cliet types.
     * @return an {@link Application} object
     * @throws IOException
     * @throws ChinoApiException 
     */
    public Application create(String name, String grantType, String redirectUrl, ClientType clientType) throws IOException, ChinoApiException {
        if (clientType == ClientType.CONFIDENTIAL)
            return create(name, grantType, redirectUrl);
        
        CreateApplicationRequest applicationRequest = new CreateApplicationRequest(name, grantType, redirectUrl, clientType);
        JsonNode data = postResource("/auth/applications", applicationRequest);
        if(data!=null)
            return mapper.convertValue(data, GetApplicationResponse.class).getApplication();

        return null;
    }

    /**
     * It updates the Application
     * @param applicationId the id of the Application
     * @param name the new name for the Application
     * @param grantType "authorization-code" or "password", it indicates the method for the authentication
     * @return Application Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public Application update(String applicationId, String name, String grantType, String redirectUrl) throws IOException, ChinoApiException {
        checkNotNull(applicationId, "application_id");
        CreateApplicationRequest applicationRequest = new CreateApplicationRequest(name, grantType, redirectUrl);
        JsonNode data = putResource("/auth/applications/"+applicationId, applicationRequest);
        if(data!=null)
            return mapper.convertValue(data, GetApplicationResponse.class).getApplication();

        return null;
    }

    /**
     * It deletes the Application
     * @param applicationId the id of the Application
     * @param force if true, the resource cannot be restored
     * @return a String with the result of the operation
     * @throws IOException
     * @throws ChinoApiException
     */
    public String delete(String applicationId, boolean force) throws IOException, ChinoApiException {
        checkNotNull(applicationId, "application_id");
        return deleteResource("/auth/applications/"+applicationId, force);
    }
}
