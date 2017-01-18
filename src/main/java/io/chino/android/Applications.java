package io.chino.android;

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

    public GetApplicationsResponse list(int offset) throws IOException, ChinoApiException {
        JsonNode data = getResource("/auth/applications", offset, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return mapper.convertValue(data, GetApplicationsResponse.class);
        return null;
    }

    public Application read(String applicationId) throws IOException, ChinoApiException{
        JsonNode data = getResource("/auth/applications/"+applicationId, 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
        if(data!=null)
            return mapper.convertValue(data, GetApplicationResponse.class).getApplication();

        return null;
    }

    public Application create(String name, String grantType, String redirectUrl) throws IOException, ChinoApiException {
        CreateApplicationRequest applicationRequest = new CreateApplicationRequest();
        applicationRequest.setName(name);
        applicationRequest.setGrantType(grantType);
        applicationRequest.setRedirectUrl(redirectUrl);
        JsonNode data = postResource("/auth/applications", applicationRequest);
        if(data!=null)
            return mapper.convertValue(data, GetApplicationResponse.class).getApplication();

        return null;
    }

    public Application update(String applicationId, String name, String grantType) throws IOException, ChinoApiException {
        UpdateApplicationRequest applicationRequest = new UpdateApplicationRequest();
        applicationRequest.setName(name);
        applicationRequest.setGrantType(grantType);
        JsonNode data = putResource("/auth/applications/"+applicationId, applicationRequest);
        if(data!=null)
            return mapper.convertValue(data, GetApplicationResponse.class).getApplication();

        return null;
    }

    public String delete(String applicationId, boolean force) throws IOException, ChinoApiException {
        return deleteResource("/auth/applications/"+applicationId, force);
    }
}
