package io.chino.client;

import io.chino.api.auth.LoggedUser;
import io.chino.api.auth.LoginRequest;
import io.chino.api.auth.LoginResponse;
import io.chino.api.auth.LogoutResponse;
import io.chino.api.common.*;
import io.chino.api.user.GetUserResponse;
import io.chino.api.user.User;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

public class Auth extends ChinoBaseAPI{

    //This filter is needed to check all the requests made to the Server and in case, switching from customer to user or from user to customer
    CheckRequestFilter filter;

    public Auth(String hostUrl, Client client, CheckRequestFilter filter){
        super(hostUrl, client);
        this.filter = filter;
    }

    /**
     * Used to login as a User
     * @param username the username of the User
     * @param password the password of the User
     * @param customerId the customerId of the Customer
     * @return The LoggedUser Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public LoggedUser loginUser(String username, String password, String customerId) throws IOException, ChinoApiException {
        LoginRequest loginRequest=new LoginRequest();
        loginRequest.setUsername(username);
        loginRequest.setPassword(password);
        loginRequest.setCustomerId(customerId);

        Response response = client.target(host).path("/auth/login").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(mapper.writeValueAsString(loginRequest)));
        String responseString=response.readEntity(String.class);

        if(response.getStatus()==200){
            LoggedUser user = mapper.readValue(mapper.readTree(responseString).get("data"), LoginResponse.class).getUser();
            //Here we call this function to switch from customer to user in the filter, setting the token used to authenticate
            setUser(user.getAccessToken());
            return user;
        }else{
            throw new ChinoApiException(mapper.readValue(responseString, ErrorResponse.class));
        }
    }

    //Function used to set the token in the filter, to authenticate a specific user, called in loginUser function
    private void setUser(String token){
        filter.setAuth(token);
    }

    //Function used to set the customerId and the customerKey in the filter, to authenticate the customer
    public void setCustomer(String customerId, String customerKey){
        filter.setAuth(customerId, customerKey);
    }

    /**
     * Used to check the User logged status
     * @return the status of the User logged as a User Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public User checkUserStatus() throws IOException, ChinoApiException {
        Response response = client.target(host).path("/auth/info").request().get();
        String responseString=response.readEntity(String.class);

        if(response.getStatus()==200){
            return mapper.readValue(mapper.readTree(responseString).get("data"), GetUserResponse.class).getUser();
        }else{
            throw new ChinoApiException(mapper.readValue(responseString, ErrorResponse.class));
        }
    }

    /**
     * Used to logout a User
     * @return The LogoutResponse Object
     * @throws IOException
     * @throws ChinoApiException
     */
    public LogoutResponse logoutUser() throws IOException, ChinoApiException {
        Response response = client.target(host).path("/auth/logout").request(MediaType.APPLICATION_JSON_TYPE).post(null);
        String responseString=response.readEntity(String.class);

        if(response.getStatus()==200){
            return mapper.readValue(mapper.readTree(responseString).get("data"), LogoutResponse.class);
        }else{
            throw new ChinoApiException(mapper.readValue(responseString, ErrorResponse.class));
        }
    }
}
