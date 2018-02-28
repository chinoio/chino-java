package io.chino.examples.auth;

import io.chino.api.application.Application;
import io.chino.api.auth.LoggedUser;
import io.chino.api.common.ChinoApiException;
import io.chino.api.common.Field;
import io.chino.api.repository.Repository;
import io.chino.api.schema.Schema;
import io.chino.api.schema.SchemaRequest;
import io.chino.api.schema.SchemaStructure;
import io.chino.api.user.User;
import io.chino.api.userschema.UserSchema;
import io.chino.examples.DeleteAll;
import io.chino.java.ChinoAPI;
import io.chino.examples.userschemas.UserSchemaStructureSample;
import io.chino.examples.Constants;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

public class AuthSamples {

    public String APPLICATION_ID = null;
    public String USER_SCHEMA_ID = null;
    public String USER_ID = null;
    public ChinoAPI chino_admin;

    public void testAuth() throws IOException, ChinoApiException {
        
        // *** PREPARATION ***

        // Initialize the ChinoAPI client with the customerId and customerKey
        chino_admin = new ChinoAPI(Constants.HOST, Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY);

        // TODO: keep here or move at the end? Or delete?
//        DeleteAll deleteAll = new DeleteAll();
//        deleteAll.deleteAll(chino_admin);

        // Create a UserSchema with the user data we want to record
        UserSchema userSchema = chino_admin.userSchemas.create("test_description", UserSchemaStructureSample.class);
        USER_SCHEMA_ID = userSchema.getUserSchemaId();

        // Create a new User under the UserSchema we just created
        HashMap<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("test_string", "test_string_value");
        attributes.put("test_boolean", true);
        attributes.put("test_integer", 123);
        attributes.put("test_date", "1993-09-08");
        attributes.put("test_float", 12.4);
        User user = chino_admin.users.create(Constants.USERNAME, Constants.PASSWORD, attributes, USER_SCHEMA_ID);
        USER_ID = user.getUserId();

        // *** USER AUTHENTICATION ***

        // Create an Application that authenticates users with the 'password' grant type
        Application application = chino_admin.applications.create("ApplicationTest1", "password", "http://127.0.0.1/");
        APPLICATION_ID = application.getAppId();

        // TODO: create a new attribute? es.: chino_admin and chino_user
        ChinoAPI chino_user = new ChinoAPI(Constants.HOST);

        // The User logs in using their username and password
        LoggedUser loggedUser = chino_user.auth.loginWithPassword(Constants.USERNAME, Constants.PASSWORD, application.getAppId(), application.getAppSecret());
        System.out.println(loggedUser);

        // Read and print the User's status
        user = chino_user.auth.checkUserStatus();
        System.out.println(user);

        // Refresh the auth token and print the updated User's fields
        loggedUser = chino_user.auth.refreshToken(loggedUser.getRefreshToken(), application.getAppId(), application.getAppSecret());
        System.out.println(loggedUser);
        
        // User refreshes their Bearer Token
        System.out.println(loggedUser.getAccessToken());
        Application external_app = chino_admin.applications.create("ApplicationTest2", "authorization-code", "http://127.0.0.1/");
        User u = chino_user.auth.loginWithBearerToken(loggedUser.getAccessToken(), external_app.getAppId(), external_app.getAppSecret());
        System.out.println(u);

        // User logs out
        System.out.println(chino_user.auth.logout(loggedUser.getAccessToken(), application.getAppId(), application.getAppSecret()));

        // Delete test User infos
        chino_admin = new ChinoAPI(Constants.HOST, Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY);
        System.out.println(chino_admin.users.delete(USER_ID, true));
        System.out.println(chino_admin.userSchemas.delete(USER_SCHEMA_ID, true));
        System.out.println(chino_admin.applications.delete(APPLICATION_ID, true));
    }
}
