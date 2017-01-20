package io.chino.examples.auth;

import io.chino.api.application.Application;
import io.chino.api.auth.LoggedUser;
import io.chino.api.common.ChinoApiException;
import io.chino.api.user.User;
import io.chino.api.userschema.UserSchema;
import io.chino.android.ChinoAPI;
import io.chino.examples.userschemas.UserSchemaStructureSample;
import io.chino.examples.util.Constants;

import java.io.IOException;
import java.util.HashMap;

public class AuthSamples {

    public String APPLICATION_ID = "";
    public String USER_SCHEMA_ID = "";
    public String USER_ID = "";
    public ChinoAPI chino;

    public void testAuth() throws IOException, ChinoApiException {

        //We initialize the ChinoAPI variable with the customerId and customerKey
        chino = new ChinoAPI(Constants.HOST, Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY);

        //First of all we need a UserSchema in which we create the User
        UserSchema userSchema = chino.userSchemas.create("test_description", UserSchemaStructureSample.class);
        USER_SCHEMA_ID = userSchema.getUserSchemaId();

        //Now we create the User under the UserSchema newly created
        HashMap<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("test_string", "test_string_value");
        attributes.put("test_boolean", true);
        attributes.put("test_integer", 123);
        attributes.put("test_date", "1993-09-08");
        attributes.put("test_float", 12.4);
        User user = chino.users.create(Constants.USERNAME, Constants.PASSWORD, attributes, USER_SCHEMA_ID);
        USER_ID = user.getUserId();

        //Let's approach with the authentication of the User

        //First of all we need to create an Application with the "password" method for the authentication
        Application application = chino.applications.create("ApplicationTest1", "password", "http://127.0.0.1/");
        APPLICATION_ID = application.getAppId();

        //Now we log in with the username and password of the User created
        LoggedUser loggedUser = chino.auth.loginWithPassword(Constants.USERNAME, Constants.PASSWORD, application.getAppId(), application.getAppSecret());
        System.out.println(loggedUser);

        //Let's try to read the User status
        user = chino.auth.checkUserStatus();
        System.out.println(user);

        //We also try to refresh the token for the authentication and we print in the console the user with updated fields
        loggedUser = chino.auth.refreshToken(loggedUser.getRefreshToken(), application.getAppId(), application.getAppSecret());
        System.out.println(loggedUser);

        //Finally we log out, we create a new ChinoAPI Object with the customerId and customerKey and we delete everything we created
        System.out.println(chino.auth.logout(loggedUser.getAccessToken(), application.getAppId(), application.getAppSecret()));
        chino = new ChinoAPI(Constants.HOST, Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY);

        System.out.println(chino.users.delete(USER_ID, true));
        System.out.println(chino.userSchemas.delete(USER_SCHEMA_ID, true));
        System.out.println(chino.applications.delete(APPLICATION_ID, true));
    }
}
