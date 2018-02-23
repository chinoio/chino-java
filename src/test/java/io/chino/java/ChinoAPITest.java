/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.chino.java;

import io.chino.api.application.Application;
import io.chino.api.auth.LoggedUser;
import io.chino.api.common.ChinoApiException;
import io.chino.api.repository.Repository;
import io.chino.api.user.User;
import io.chino.api.userschema.UserSchema;
import io.chino.examples.userschemas.UserSchemaStructureSample;
import io.chino.examples.util.DeleteAll;
import io.chino.test.util.Constants;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * Test for class {@link ChinoAPI io.chino.java.ChinoAPI}
 * @author Andrea
 */
public class ChinoAPITest {
    
    private static final String URL = Constants.HOST;
    private static String CID = null;
    private static String CKEY = null;
    private static String USERNAME = "testusrname",
            PASSWORD = "testpword32";
    
    private static String USER_ID;
    private static String USER_SCHEMA_ID;
    private static String APP_ID;
    private static String APP_SECRET;
    
    // TODO remove
    public static void setCustomer() {
        // set here your customerID/customerKey pair to run tests
        CID = // customerId
                ""
        ;
        CKEY = // customerKey
                ""
        ;
    }
    
    @BeforeClass
    public static void setUpClass() {
        // TODO remove
        setCustomer();
        
        ChinoAPI chino_admin = new ChinoAPI(URL, CID, CKEY);
        
        UserSchema userSchema;
        
        try {
            if (chino_admin.userSchemas.list().getCount().equals(0))
                new DeleteAll().deleteAll(new ChinoAPI(URL, CID, CKEY));
        } catch (Exception ex) {
            fail("failed to delete objects for ChinoAPITest. Please do it by hand.\n"
                    + ex.getClass().getName() + ": " + ex.getLocalizedMessage());
        } finally {
            tearDownClass();
        }
        
        // create a new user for testing
        try {
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
            
            Application app = chino_admin.applications.create("test_app", "password", "");
            APP_ID = app.getAppId();
            APP_SECRET = app.getAppSecret();
        } catch (Exception ex) {
            fail("failed to set up test for ChinoAPITest.\n"
                    + ex.getClass().getName() + ": " + ex.getLocalizedMessage());
        } finally {
            tearDownClass();
        }
    }
    
    @AfterClass
    public static void tearDownClass() {
        try {
            new DeleteAll().deleteAll(new ChinoAPI(URL, CID, CKEY));
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
        ChinoAPI chino_user = new ChinoAPI(URL);
        String accessToken = null,
                refreshToken = null;
        LoggedUser user = null;
        try {
            user = chino_user.auth.loginWithPassword(USERNAME, PASSWORD, APP_ID, APP_SECRET);
            accessToken = user.getAccessToken();
            refreshToken = user.getRefreshToken();
        } catch (ChinoApiException ex) {
            fail("Thrown ChinoApiException. Reason: " + ex.getMessage());
        } catch (IOException ex) {
            fail("Thrown IOException. Reason: " + ex.getMessage());
        } finally {
            tearDownClass();
        }
        // Do some operations with the bearer token client
        ChinoAPI apiClient = new ChinoAPI(URL, accessToken);
        assertClientWasCreated(apiClient);
        try {
            // use the access token to create a new repo
            Repository rep = apiClient.repositories.create("test_repo");
            assertNotNull(apiClient.repositories.read(rep.getRepositoryId()));
            
            // refresh token
            user = apiClient.auth.refreshToken(refreshToken, APP_ID, APP_SECRET);
            // use the new access token to delete the repository
            apiClient.repositories.delete(rep.getRepositoryId(), true);
            assertNull(apiClient.repositories.read(rep.getRepositoryId()));
            
            apiClient.auth.logout(accessToken, APP_ID, APP_SECRET);
        } catch (ChinoApiException ex) {
            fail("Thrown ChinoApiException. Reason: " + ex.getMessage());
        } catch (IOException ex) {
            fail("Thrown IOException. Reason: " + ex.getMessage());
        } finally {
            tearDownClass();
        }
    }

    @Test
    public void testConstructor_String_String_String() {
        ChinoAPI apiClient = new ChinoAPI(URL, CID, CKEY);
        assertClientWasCreated(apiClient);
        // test if login was successful
        try {
            Repository rep = apiClient.repositories.create("test_repo for ChinoAPITest");
            assertNotNull(rep);
            apiClient.repositories.delete(rep.getRepositoryId(), true);
            assertNull(rep);
        } catch (ChinoApiException ex) {
            fail("Thrown ChinoApiException. Reason: " + ex.getMessage());
        } catch (IOException ex) {
            fail("Thrown IOException. Reason: " + ex.getMessage());
        } finally {
            tearDownClass();
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
