package io.chino.java;

import com.fasterxml.jackson.databind.JsonNode;
import io.chino.api.auth.*;
import io.chino.api.common.ChinoApiException;
import io.chino.api.common.ErrorResponse;
import io.chino.api.common.LoggingInterceptor;
import io.chino.api.user.GetUserResponse;
import io.chino.api.user.User;
import okhttp3.*;
import java.io.IOException;

public class Auth extends ChinoBaseAPI {

    public Auth(String hostUrl, OkHttpClient client, LoggingInterceptor interceptor){
        super(hostUrl, client, interceptor);
    }

    /**
     * Login with password and username for users
     * @param username the username of the user
     * @param password the password of the user
     * @param applicationId the id of the Application
     * @param applicationSecret the Application secret
     * @return LoggedUser Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public LoggedUser loginWithPassword(String username, String password, final String applicationId, final String applicationSecret) throws IOException, ChinoApiException {
        checkNotNull(username, "username");
        checkNotNull(password, "password");
        checkNotNull(applicationId, "application_id");
        checkNotNull(applicationSecret, "application_secret");
        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", "password")
                .add("username", username)
                .add("password", password)
                .build();
        Request request = new Request.Builder()
                .url(hostUrl+"/auth/token/")
                .post(formBody)
                .build();
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().addInterceptor(interceptor);
        clientBuilder.authenticator(new Authenticator() {
            private int mCounter = 0;
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                if (mCounter++ > 0) {
                    return null;
                }
                String credential = Credentials.basic(applicationId, applicationSecret);
                return response.request().newBuilder().header("Authorization", credential).build();
            }
        });
        client = clientBuilder.build();
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        return auxFunction(response, body);
    }

    /**
     * Login with authentication code for users
     * @param code the code retrieved from the app server
     * @param redirectUrl the redirect_url of the app server
     * @param applicationId the id of the Application
     * @param applicationSecret the Application secret
     * @return LoggedUser Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public LoggedUser loginWithAuthenticationCode(String code, String redirectUrl, final String applicationId, final String applicationSecret) throws IOException, ChinoApiException {
        checkNotNull(code, "code");
        checkNotNull(redirectUrl, "redirect_url");
        checkNotNull(applicationId, "application_id");
        checkNotNull(applicationSecret, "application_secret");
        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", "authorization_code")
                .add("code", code)
                .add("redirect_uri", redirectUrl)
                .add("client_id", applicationId)
                .add("client_secret", applicationSecret)
                .add("scope", "read write")
                .build();
        Request request = new Request.Builder()
                .url(hostUrl+"/auth/token/")
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        return auxFunction(response, body);
    }

    private LoggedUser auxFunction(Response response, String body) throws IOException, ChinoApiException{
        if (response.code() == 200) {
            JsonNode data = mapper.readTree(body).get("data");
            if(data!=null) {
                LoggedUser loggedUser = mapper.convertValue(data, LoggedUser.class);
                interceptor.setUser(loggedUser.getAccessToken());
                return loggedUser;
            }
            return null;
        } else {
            throw new ChinoApiException(mapper.readValue(body, ErrorResponse.class));
        }
    }

    /**
     * It refreshes the token for the logged user
     * @param refreshToken the refresh_token in the attributes of the logged user
     * @param applicationId the id of the Application
     * @param applicationSecret the Application secret
     * @return LoggedUser Object updated
     * @throws IOException
     * @throws ChinoApiException
     */
    public LoggedUser refreshToken(String refreshToken, final String applicationId, final String applicationSecret) throws IOException, ChinoApiException {
        checkNotNull(refreshToken, "refresh_token");
        checkNotNull(applicationId, "application_id");
        checkNotNull(applicationSecret, "application_secret");
        RequestBody formBody = new FormBody.Builder()
                .add("grant_type", "refresh_token")
                .add("refresh_token", refreshToken)
                .add("client_id", applicationId)
                .add("client_secret", applicationSecret)
                .build();
        Request request = new Request.Builder()
                .url(hostUrl+"/auth/token/")
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        return auxFunction(response, body);
    }

    /**
     * It checks the logged user status
     * @return User Object that contains the status of the logged user
     * @throws IOException
     * @throws ChinoApiException
     */
    public User checkUserStatus() throws IOException, ChinoApiException {
        JsonNode data = getResource("/users/me");
        if(data!=null){
            return mapper.convertValue(data, GetUserResponse.class).getUser();
        }
        return null;
    }

    /**
     * It logs out
     * @param token the token of the logged user
     * @param applicationId the id of the Application
     * @param applicationSecret the Application secret
     * @return a String with the result of the operation
     * @throws IOException
     * @throws ChinoApiException
     */
    public String logout(String token, String applicationId, String applicationSecret) throws IOException, ChinoApiException {
        checkNotNull(token, "token");
        checkNotNull(applicationId, "application_id");
        checkNotNull(applicationSecret, "application_secret");
        RequestBody formBody = new FormBody.Builder()
                .add("token", token)
                .add("client_id", applicationId)
                .add("client_secret", applicationSecret)
                .build();
        Request request = new Request.Builder()
                .url(hostUrl+"/auth/revoke_token/")
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        if (response.code() == 200) {
            interceptor.logout();
            return "success";
        } else {
            throw new ChinoApiException(mapper.readValue(body, ErrorResponse.class));
        }
    }
}


