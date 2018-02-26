/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.chino.java;

import io.chino.api.auth.LoggedUser;
import io.chino.api.common.ChinoApiException;
import io.chino.api.permission.PermissionRule;
import io.chino.api.repository.Repository;
import io.chino.api.user.User;
import io.chino.api.userschema.GetUserSchemasResponse;
import io.chino.api.userschema.UserSchema;
import io.chino.examples.userschemas.UserSchemaStructureSample;
import io.chino.test.util.Constants;
import java.io.IOException;
import java.util.HashMap;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 * Test for class {@link ChinoAPI io.chino.java.ChinoAPI}
 * You need to set the Chino.io customerId and customerKey in method {@link #setCodes() }.
 * You also need to create a Chino.io application and to paste the
 * applicationId and applicationSecret in method {@link #setCodes() setCodes()}.
 * @author Andrea
 */
public class ChinoAPITest {
    
    private static final String URL = Constants.HOST;
    private static String CID = null;
    private static String CKEY = null;
    private static final String USERNAME = "testusrname";
    private static String PASSWORD = "testpword32";
    
    private static String USER_ID;
    private static String USER_SCHEMA_ID;
    private static String PWORD_APP_ID = null;
    private static String PWORD_APP_SECRET = null;
    private static String AUTHCODE_APP_ID = null;
    private static String AUTHCODE_APP_SECRET = null;
    
    /**
     * The customer console
     */
    private static ChinoAPI chino_admin;
    
    /**
     * This method will contain the codes that are needed in order to successfully
     * complete the tests. See {@link ChinoAPITest class javadoc} for more instructions.
     */
    private static void setCodes() {
        // paste here your customerID/customerKey pair to run tests
        CID = // customerId
                ""
        ;
        CKEY = // customerKey
                ""
        ;
        
        // You will need to create an application to run tests, which must have
        // a 'password' grant type. After creating it, paste here the
        // id and secret of the application you will use to run tests.
        // You can find them in Chino.io API console (https://console.test.chino.io)
        PWORD_APP_ID = // app ID
                ""
        ;
        PWORD_APP_SECRET = // app secret
                ""
        ;
        
        // You will also need a second application which uses 'authentication code'
        // as grant type. The redirect URL can be any valid URL.
        // After creating it manually, paste here the app id and app secret.
        AUTHCODE_APP_ID = // app id
                ""
        ;
        AUTHCODE_APP_SECRET = // app secret
                ""
        ;
    }
    
    @BeforeClass
    public static void setUpClass() {
        setCodes();
        if (CID == null || CID.isEmpty() || CKEY == null || CKEY.isEmpty()) {
            fail("Please setup tests: customerId or customerKey missing. Check javadoc for ChinoAPITest.");
        }
        if (PWORD_APP_ID == null | PWORD_APP_ID.isEmpty() || PWORD_APP_SECRET == null || PWORD_APP_SECRET.isEmpty()) {
            fail("Please setup password application: applicationId or applicationSecret missing. Check javadoc for ChinoAPITest.");
        }
        if (AUTHCODE_APP_ID == null | AUTHCODE_APP_ID.isEmpty() || AUTHCODE_APP_SECRET == null || AUTHCODE_APP_SECRET.isEmpty()) {
            fail("Please setup auth code application: applicationId or applicationSecret missing. Check javadoc for ChinoAPITest.");
        }
        
        chino_admin = new ChinoAPI(URL, CID, CKEY);
        
        UserSchema userSchema = null;
        
        try {
            GetUserSchemasResponse ls = chino_admin.userSchemas.list();
            while (! ls.getCount().equals(0)){
                String usid = ls.getUserSchemas().get(0).getUserSchemaId();
                chino_admin.userSchemas.delete(usid, true);
                ls = chino_admin.userSchemas.list();
            }
        } catch (Exception ex) {
            tearDownClass();
            fail("failed to delete objects for ChinoAPITest. Please do it by hand.\n"
                    + ex.getClass().getName() + ": " + ex.getLocalizedMessage());
        }
        
        try {
            // create a new user
            userSchema = chino_admin.userSchemas.create("test_user_schema", UserSchemaStructureSample.class);
            USER_SCHEMA_ID = userSchema.getUserSchemaId();
            HashMap<String, Object> attributes = new HashMap<String, Object>();
            attributes.put("test_string", "test_string_value");
            attributes.put("test_boolean", true);
            attributes.put("test_integer", 123);
            attributes.put("test_date", "1993-09-08");
            attributes.put("test_float", 12.4);
            User user = chino_admin.users.create(USERNAME, PASSWORD, attributes, USER_SCHEMA_ID);
            USER_ID = user.getUserId();
        } catch (Exception ex) {
            fail("failed to set up test for ChinoAPITest.\n"
                    + ex.getClass().getName() + ": " + ex.getLocalizedMessage());
        }
    }
    
    @AfterClass
    public static void tearDownClass() {
        try {
            if (!chino_admin.userSchemas.list().getCount().equals(0))
                chino_admin.userSchemas.delete(USER_SCHEMA_ID, true);
            chino_admin = null;
        } catch (Exception ex) {
            fail("failed to delete objects for ChinoAPITest. Please do it by hand.\n"
                    + ex.getClass().getName() + ": " + ex.getLocalizedMessage());
        }
    }

    @Test
    public void testConstructor_String() {
        ChinoAPI apiClient = new ChinoAPI(URL);
        assertClientWasCreated(apiClient);
    }

    @Test
    public void testConstructor_String_String() {
        // authenticate user
        String step = "initialize";
        ChinoAPI chino_user = new ChinoAPI(URL);
        String accessToken = null,
                refreshToken = null;
        LoggedUser user = null;
        try {
            step = "authenticate user with username '" + USERNAME + "' and password '" + PASSWORD + "'";
            user = chino_user.auth.loginWithPassword(USERNAME, PASSWORD, PWORD_APP_ID, PWORD_APP_SECRET);
            accessToken = user.getAccessToken();
            System.out.println("1st access token: " + accessToken);
            refreshToken = user.getRefreshToken();
            System.out.println("1st refresh token: " + refreshToken);
        } catch (ChinoApiException ex) {
            fail("Thrown ChinoApiException. Failed to " + step + ". \n" + ex.getMessage());
        } catch (IOException ex) {
            tearDownClass();
            fail("Thrown IOException. Reason: " + ex.getMessage());
        }
        // Do some operations with the bearer token client
        ChinoAPI apiClient = new ChinoAPI(URL, accessToken);
        assertClientWasCreated(apiClient);
        try {
            // give the user permission to CRUD and List repositories
            step = "grant perms on repositories";
            PermissionRule repo_grant = new PermissionRule();
            repo_grant.setManage("C", "R", "U", "D", "L");
            chino_admin.permissions.permissionsOnResources("grant", "repositories", "users", USER_ID, repo_grant);
            
            // use the access token to create a new repo
            step = "create repository";
            Repository rep = apiClient.repositories.create("test_repo");
            assertNotNull(apiClient.repositories.read(rep.getRepositoryId()));

            // refresh token - access token is automatically updated in apiClient
            step = "refresh token";
            user = apiClient.auth.refreshToken(refreshToken, PWORD_APP_ID, PWORD_APP_SECRET);
            accessToken = user.getAccessToken();
            System.out.println("2nd access token: " + accessToken);
            refreshToken = user.getRefreshToken();
            System.out.println("2nd refresh token: " + refreshToken);
            
            // use the new access token to delete the repository
            step = "delete repository";
            String repId = rep.getRepositoryId();
            apiClient.repositories.delete(repId, true);
            boolean deleted = false;
            try {
                apiClient.repositories.read(repId);
            } catch (ChinoApiException x) {
                if (x.getCode().equals("404"))
                    deleted = true;
            } finally {
                assertTrue(deleted);
            }
            
            // log out from the api client
            step = "logout";
            apiClient.auth.logout(accessToken, PWORD_APP_ID, PWORD_APP_SECRET);
        } catch (ChinoApiException ex) {
            fail("Thrown ChinoApiException. Failed to " + step + ". \n" + ex.getMessage());
        } catch (IOException ex) {
            tearDownClass();
            fail("Thrown IOException. Reason: " + ex.getMessage());
        }
    }

    @Test
    public void testConstructor_String_String_String() {
        String step = "initialize";
        ChinoAPI apiClient = new ChinoAPI(URL, CID, CKEY);
        assertClientWasCreated(apiClient);
        try {
            // create a repository using costomer credentials
            step = "create repository";
            Repository rep = apiClient.repositories.create("test_repo for ChinoAPITest");
            assertNotNull(rep);
            
            // delete the repository
            step = "delete repository";
            apiClient.repositories.delete(rep.getRepositoryId(), true);
            boolean deleted = false;
            try {
                apiClient.repositories.read(rep.getRepositoryId());
            } catch (ChinoApiException x) {
                if (x.getCode().equals("404"))
                    deleted = true;
            } finally {
                assertTrue(deleted);
            }
        } catch (ChinoApiException ex) {
            fail("Thrown ChinoApiException. Failed to " + step + ". \n" + ex.getMessage());
        } catch (IOException ex) {
            tearDownClass();
            fail("Thrown IOException. Reason: " + ex.getMessage());
        }
    }

    private void assertClientWasCreated(ChinoAPI c) {
        assertNotNull(c);
        assertNotNull(c.applications);
        assertNotNull(c.auth);
        assertNotNull(c.blobs);
        assertNotNull(c.client);
        assertNotNull(c.collections);
        assertNotNull(c.documents);
        assertNotNull(c.groups);
        assertNotNull(c.permissions);
        assertNotNull(c.repositories);
        assertNotNull(c.schemas);
        assertNotNull(c.search);
        assertNotNull(c.userSchemas);
        assertNotNull(c.users);
    }
    
}
