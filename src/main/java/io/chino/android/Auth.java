package io.chino.android;

import com.fasterxml.jackson.databind.JsonNode;
import io.chino.api.auth.*;
import io.chino.api.common.ChinoApiException;
import io.chino.api.common.ErrorResponse;
import io.chino.api.user.GetUserResponse;
import io.chino.api.user.User;
import okhttp3.*;
import java.io.IOException;

public class Auth extends ChinoBaseAPI {

    public Auth(String hostUrl, OkHttpClient client){
        super(hostUrl, client);
    }


    public LoggedUser login(String username, String password, final String applicationId, final String applicationSecret) throws IOException, ChinoApiException {
        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", "password")
                .add("username", username)
                .add("password", password)
                .build();
        Request request = new Request.Builder()
                .url(hostUrl+"/auth/token/")
                .post(formBody)
                .build();
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.authenticator(new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                String credential = Credentials.basic(applicationId, applicationSecret);
                return response.request().newBuilder().header("Authorization", credential).build();
            }
        });
        client = clientBuilder.build();
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        if (response.code() == 200) {
            JsonNode data = mapper.readTree(body).get("data");
            if(data!=null) {
                return mapper.convertValue(data, LoggedUser.class);
            }
            return null;
        } else {
            throw new ChinoApiException(mapper.readValue(body, ErrorResponse.class));
        }
    }

    /**
     * Used to check the User logged status
     * @return the status of the User logged as a User Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public User checkUserStatus() throws IOException, ChinoApiException {
        JsonNode data = getResource("/users/me");
        if(data!=null)
            return mapper.convertValue(data, GetUserResponse.class).getUser();
        return null;
    }

}


