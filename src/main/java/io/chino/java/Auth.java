package io.chino.java;

import com.fasterxml.jackson.databind.JsonNode;
import io.chino.api.application.Application;
import io.chino.api.application.ClientType;
import io.chino.api.auth.*;
import io.chino.api.common.ChinoApiException;
import io.chino.api.common.ErrorResponse;
import io.chino.api.common.LoggingInterceptor;
import io.chino.api.user.GetUserResponse;
import io.chino.api.user.User;
import okhttp3.*;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Auth extends ChinoBaseAPI {

    public Auth(String hostUrl, OkHttpClient client){
        super(hostUrl, client);
    }

    /**
     * Login with username/password to an {@link Application}.
     * @param username the "username" of the user
     * @param password the "password" of the user
     * @param applicationId the "application_id"
     * @param applicationSecret the application_secret - if authenticating from
     * a {@link ClientType#PUBLIC "public"} application, pass an empty String: {@code ""}
     * @return a {@link LoggedUser} object
     * @throws IOException
     * @throws ChinoApiException
     */
    public LoggedUser loginWithPassword(String username, String password, final String applicationId, final String applicationSecret) throws IOException, ChinoApiException {
        checkNotNull(username, "username");
        checkNotNull(password, "password");
        checkNotNull(applicationId, "application_id");
        checkNotNull(applicationSecret, "application_secret");
        RequestBody formBody;
        if (applicationSecret.equals("")) {
            formBody = new FormBody.Builder()
                    .add("grant_type", "password")
                    .add("username", username)
                    .add("password", password)
                    .add("client_id", applicationId)
                    .build();
        } else {
            formBody = new FormBody.Builder()
                    .add("grant_type", "password")
                    .add("username", username)
                    .add("password", password)
                    .add("client_id", applicationId)
                    .add("client_secret", applicationSecret)
                    .build();
        }
        Request request = new Request.Builder()
                .url(hostUrl+"/auth/token/")
                .post(formBody)
                .build();
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        Response response = client.newCall(request).execute();
        String body = null;
        if (response != null){
            body = response.body().string();
        }
        return auxFunction(response, body);
    }
    
    /**
     * Login with username/password to a {@link ClientType#PUBLIC "public"}
     * Application. No need to know the {@link Application}'s "application_secret".
     * @param username the "username" of the user
     * @param password the "password" of the user
     * @param applicationId the "application_id"
     * @return a {@link LoggedUser} object
     * @throws IOException
     * @throws ChinoApiException 
     */
    public LoggedUser loginWithPassword (String username, String password, final String applicationId) throws IOException, ChinoApiException {
        return loginWithPassword(username, password, applicationId, "");
    }

    /**
     * Save the {@code token} in a new {@link #client OkHttpClient} to be used in future calls.<br>
     * <br>
     * <b>This method has been deprecated since API version 1.2 and might be removed at any time.</b>
     * It's strongly suggested to use {@link #loginWithBearerToken(String)} instead.
     *
     * @param token the new token to be used in the API calls
     * @param applicationId the id of the application that interacts with Chino API
     * @param applicationSecret the secret code of the application that interacts with Chino API
     * @return information about the current {@link User}'s status (see also {@link #checkUserStatus() checkUserStatus()})
     * @throws IOException the User can not be found on server. Returned by {@link Call#execute() okhttp3.Call}
     * @throws ChinoApiException server responded with error
     */
    @Deprecated
    public User loginWithBearerToken(String token, final String applicationId, final String applicationSecret) throws IOException, ChinoApiException {
        checkNotNull(applicationId, "application_id");
        checkNotNull(applicationSecret, "application_secret");

        return loginWithBearerToken(token);
    }

    /**
     * Save the {@code token} in a new {@link #client OkHttpClient} to be used in future calls
     * @param token the new token to be used in the API calls
     * @return information about the current {@link User}'s status (see also {@link #checkUserStatus() checkUserStatus()})
     * @throws IOException the User can not be found on server. Thrown by {@link Call#execute() okhttp3.Call}
     * @throws ChinoApiException server responded with error
     */
    public User loginWithBearerToken(String token) throws IOException, ChinoApiException {
        checkNotNull(token, "token");
        // saves the new token in the HTTP client
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addNetworkInterceptor(new LoggingInterceptor(token))
                .build();
        User u = checkUserStatus();
        return u;
    }

    /**
     * Login with authentication code for users
     * @param code the code retrieved from the app server
     * @param redirectUrl the redirect_url of the app server
     * @param applicationId the id of the Application
     * @param applicationSecret the Application secret (pass an empty string if you login from a "public application")
     * @return LoggedUser Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public LoggedUser loginWithAuthenticationCode(String code, String redirectUrl, final String applicationId, final String applicationSecret) throws IOException, ChinoApiException {
        checkNotNull(code, "code");
        checkNotNull(redirectUrl, "redirect_url");
        checkNotNull(applicationId, "application_id");
        checkNotNull(applicationSecret, "application_secret");
        RequestBody formBody;
        if (applicationSecret.equals("")) {
            formBody = new FormBody.Builder()
                    .add("grant_type", "authorization_code")
                    .add("code", code)
                    .add("redirect_uri", redirectUrl)
                    .add("client_id", applicationId)
                    .add("scope", "read write")
                    .build();
        } else {
            formBody = new FormBody.Builder()
                    .add("grant_type", "authorization_code")
                    .add("code", code)
                    .add("redirect_uri", redirectUrl)
                    .add("client_id", applicationId)
                    .add("client_secret", applicationSecret)
                    .add("scope", "read write")
                    .build();
        }
        Request request = new Request.Builder()
                .url(hostUrl+"/auth/token/")
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        String body = null;
        if (response != null){
            body = response.body().string();
        }
        return auxFunction(response, body);
    }

    private LoggedUser auxFunction(Response response, String body) throws IOException, ChinoApiException{
        checkNotNull(response, "response");
        checkNotNull(body, "body");
        if (response.code() == 200) {
            JsonNode data = mapper.readTree(body).get("data");
            if(data!=null) {
                LoggedUser loggedUser = mapper.convertValue(data, LoggedUser.class);
                client = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .addNetworkInterceptor(new LoggingInterceptor(loggedUser.getAccessToken()))
                        .build();
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
                .url(hostUrl + "/auth/token/")
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        String body = null;
        if (response != null){
            body = response.body().string();
        }
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
     * Log out from a  {@link Application} using the user's access token.
     * Use this method for {@link ClientType#CONFIDENTIAL CONFIDENTIAL} clients
     * or when you are unsure about the client type.
     *
     * @param token the token of the logged user
     * @return a String with the result of the operation
     * @throws IOException
     * @throws ChinoApiException
     */
    public String logout(String token, final String applicationId, final String applicationSecret) throws IOException, ChinoApiException {
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
            return "success";
        } else {
            throw new ChinoApiException(mapper.readValue(body, ErrorResponse.class));
        }
    }

    /**
     * Log out from a {@link Application} using the user's access token.
     * This method works only for {@link ClientType#PUBLIC PUBLIC} clients.
     * If you are not sure, use {@link #logout(String, String, String)}.
     *
     * @param token the token of the logged user
     * @return a String with the result of the operation
     * @throws IOException
     * @throws ChinoApiException
     */
    public String logout(String token, final String applicationId) throws IOException, ChinoApiException {
        return logout(token, applicationId, "");
    }
}


