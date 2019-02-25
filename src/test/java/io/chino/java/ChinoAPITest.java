package io.chino.java;

import io.chino.api.application.Application;
import io.chino.api.auth.LoggedUser;
import io.chino.api.common.ChinoApiException;
import io.chino.api.permission.PermissionRule;
import io.chino.api.repository.Repository;
import io.chino.api.user.User;
import io.chino.api.userschema.UserSchema;
import io.chino.java.testutils.ChinoBaseTest;
import io.chino.java.testutils.DeleteAll;
import io.chino.java.testutils.TestConstants;
import io.chino.java.testutils.UserSchemaStructureSample;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static io.chino.java.testutils.TestConstants.JAVA;
import static org.junit.Assert.*;

/**
 * Test for class {@link ChinoAPI io.chino.java.ChinoAPI}:
 * you need to add two environment variables "customer_id" and "customer_key"
 * before running the tests.
 * @author Andrea
 */
public class ChinoAPITest {

    private static String USER_ID;
    private static String USER_SCHEMA_ID;
    private static String APP_ID = null;
    private static String APP_SECRET = null;

    /**
     * The customer console
     */
    private static ChinoAPI chino_customer;

    /**
     * This method will contain the codes that are needed in order to successfully
     * complete the tests. See {@link ChinoAPITest class javadoc} for more instructions.
     */
    private static void setUpApplication() throws IOException, ChinoApiException {

        if (APP_ID != null && APP_SECRET != null) {
            return;
        }

        ArrayList<Application> apps = (ArrayList<Application>) chino_customer.applications.list().getApplications();

        for (Application app:apps)
            if (app.getAppName().equals(TestConstants.APP_NAME)){
                chino_customer.applications.delete(app.getAppId(), true);
            }

        Application app = chino_customer.applications.create(TestConstants.APP_NAME, "password", TestConstants.HOST);
        APP_ID = app.getAppId();
        APP_SECRET = app.getAppSecret();
    }

    @BeforeClass
    public static void setUpClass() throws IOException, ChinoApiException {
        ChinoBaseTest.beforeClass();
        TestConstants.init(TestConstants.USERNAME, TestConstants.PASSWORD); // instruction needed, because this is not subclass of ChinoBaseTest
        chino_customer = new ChinoAPI(TestConstants.HOST, TestConstants.CUSTOMER_ID, TestConstants.CUSTOMER_KEY);

        // delete existing users from account
        new DeleteAll().deleteAll(chino_customer.userSchemas);

        // init data of test application
        setUpApplication();

        UserSchema userSchema = null;

        try {
            ArrayList<UserSchema> ls = (ArrayList<UserSchema>) chino_customer.userSchemas.list().getUserSchemas();
            for (UserSchema us:ls) {
                ArrayList<User> users = (ArrayList<User>) chino_customer.users.list(us.getUserSchemaId()).getUsers();
                for (User user:users){
                    chino_customer.users.delete(user.getUserId(), true);
                }
                chino_customer.userSchemas.delete(us.getUserSchemaId(), true);
            }
        } catch (Exception ex) {
            tearDownClass();
            fail("failed to delete objects for ChinoAPITest. Please do it by hand.\n"
                    + ex.getClass().getSimpleName() + ": " + ex.getLocalizedMessage());
        }

        String step = "create user schema";
        try {
            // create a new user
            userSchema = chino_customer.userSchemas.create("test_user_schema"  + " [" + JAVA + "]", UserSchemaStructureSample.class);
            USER_SCHEMA_ID = userSchema.getUserSchemaId();
        } catch (Exception ex) {
            fail("failed to set up test for ChinoAPITest (" + step + ").\n"
                    + ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }

        step = "create user";
        HashMap<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("test_string", "test_string_value");
        attributes.put("test_boolean", true);
        attributes.put("test_integer", 123);
        attributes.put("test_date", "1993-09-08");
        attributes.put("test_float", 12.4);

        try {
            User user = chino_customer.users.create(TestConstants.USERNAME, TestConstants.PASSWORD, attributes, USER_SCHEMA_ID);
            USER_ID = user.getUserId();
        } catch (Exception ex) {
            fail("failed to set up test for ChinoAPITest (" + step + ").\n"
                    + ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
    }

    @AfterClass
    public static void tearDownClass() {
        try {
            if (!chino_customer.userSchemas.list().getCount().equals(0))
                chino_customer.userSchemas.delete(USER_SCHEMA_ID, true);
        } catch (Exception ex) {
            fail("failed to delete objects for ChinoAPITest. Please do it by hand.\n"
                    + ex.getClass().getSimpleName() + ": " + ex.getLocalizedMessage());
        }
    }

    @Test
    public void testUserClient() {
        ChinoAPI apiClient = new ChinoAPI(TestConstants.HOST);
        assertClientWasCreated(apiClient);
    }

    @Test
    public void testAccessTokenClient() {
        // authenticate user
        String step = "initialize";
        ChinoAPI chino_user = new ChinoAPI(TestConstants.HOST);
        String accessToken = null,
                refreshToken = null;
        LoggedUser user = null;
        try {
            step = "authenticate user with username '" + TestConstants.USERNAME + "' and password '" + TestConstants.PASSWORD + "'";
            user = chino_user.auth.loginWithPassword(TestConstants.USERNAME, TestConstants.PASSWORD, APP_ID, APP_SECRET);
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
        ChinoAPI apiClient = new ChinoAPI(TestConstants.HOST, accessToken);
        assertClientWasCreated(apiClient);
        try {
            // give the user permission to CRUD and List repositories
            step = "grant perms on repositories";
            PermissionRule repo_grant = new PermissionRule();
            repo_grant.setManage("C", "R", "U", "D", "L");
            chino_customer.permissions.permissionsOnResources("grant", "repositories", "users", USER_ID, repo_grant);

            // use the access token to create a new repo
            step = "create repository";
            Repository rep = apiClient.repositories.create("test_repo"  + " [" + TestConstants.JAVA + "]");
            assertNotNull(apiClient.repositories.read(rep.getRepositoryId()));

            // refresh token - access token is automatically updated in apiClient
            step = "refresh token";
            user = apiClient.auth.refreshToken(refreshToken, APP_ID, APP_SECRET);
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
            apiClient.auth.logout(accessToken, APP_ID, APP_SECRET);
        } catch (ChinoApiException ex) {
            fail("Thrown ChinoApiException. Failed to " + step + ". \n" + ex.getMessage());
        } catch (IOException ex) {
            tearDownClass();
            fail("Thrown IOException. Reason: " + ex.getMessage());
        }
    }

    @Test
    public void testCustomerClient() {
        String step = "initialize";
        ChinoAPI apiClient = new ChinoAPI(TestConstants.HOST, TestConstants.CUSTOMER_ID, TestConstants.CUSTOMER_KEY);
        assertClientWasCreated(apiClient);
        try {
            // create a repository using customer credentials
            step = "create repository";
            Repository rep = apiClient.repositories.create("test_repo for ChinoAPITest"  + " [" + TestConstants.JAVA + "]");
            assertNotNull(rep);

            // delete the repository
            step = "delete repository";
            apiClient.repositories.delete(rep.getRepositoryId(), true);

            assertRepositoryWasDeleted(apiClient, rep);
        } catch (ChinoApiException ex) {
            fail("Thrown ChinoApiException. Failed to " + step + ". \n" + ex.getMessage());
        } catch (IOException ex) {
            tearDownClass();
            fail("Thrown IOException. Reason: " + ex.getMessage());
        }
    }

    @Test
    public void testHostNormalization() throws IOException, ChinoApiException {
        if (TestConstants.HOST.endsWith("/")) {
            fail("The specified host URL has a trailing slash. Please set your Chino API URL to " +
                    TestConstants.HOST.substring(0, TestConstants.HOST.length() - 1));
        }

        String[] hosts = {
                TestConstants.HOST, // fine
                TestConstants.HOST.replace("https:", "http:"), // no HTTPS
                TestConstants.HOST + "/" // trailing slash
        };
        for (String hostName : hosts) {
            // test constructor 1 (customer auth)
            ChinoAPI c = new ChinoAPI(hostName, TestConstants.CUSTOMER_ID, TestConstants.CUSTOMER_KEY);
            assertUrlIsNormalized(c, hostName);
            // test constructor 2 (no auth)
            c = new ChinoAPI(hostName);
            c.setCustomer(TestConstants.CUSTOMER_ID, TestConstants.CUSTOMER_KEY);
            assertUrlIsNormalized(c, hostName);
            // test constructor 3 (bearer auth)
            LoggedUser user = c.auth.loginWithPassword(TestConstants.USERNAME, TestConstants.PASSWORD, APP_ID, APP_SECRET);
            c = new ChinoAPI(hostName, user.getAccessToken());
            assertUrlIsNormalized(c, hostName);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHostWithoutVersionCode_withSlash() {
        String errorHost = TestConstants.HOST.replace(ChinoAPI.API_VERSION, "");  // no version code (with slash)
        if (!errorHost.endsWith("/")) {
            errorHost += "/";
        }
        ChinoAPI c = new ChinoAPI(errorHost, TestConstants.CUSTOMER_ID, TestConstants.CUSTOMER_KEY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testHostWithoutVersionCode_withoutSlash() {
        String errorHost = TestConstants.HOST.replace(ChinoAPI.API_VERSION, "");  // no version code (without slash)
        if (errorHost.endsWith("/")) {
            errorHost = errorHost.substring(0, errorHost.length() - 1);
        }
        ChinoAPI c = new ChinoAPI(errorHost, TestConstants.CUSTOMER_ID, TestConstants.CUSTOMER_KEY);
    }

    @Test
    public void testSetCustomer() {
        String step = "initialize";
        ChinoAPI apiClient = new ChinoAPI(TestConstants.HOST);
        assertClientWasCreated(apiClient);
        try {
            // create a repository using costomer credentials
            step = "create repository";
            Repository rep = apiClient.setCustomer(TestConstants.CUSTOMER_ID, TestConstants.CUSTOMER_KEY) // set credentials in client
                    .repositories.create("test_repo for ChinoAPITest"  + " [" + TestConstants.JAVA + "]");
            assertNotNull(rep);

            // delete the repository
            step = "delete repository";
            apiClient.repositories.delete(rep.getRepositoryId(), true); // client should keep the credentials saved.
            assertRepositoryWasDeleted(apiClient, rep);
        } catch (ChinoApiException ex) {
            fail("Thrown ChinoApiException. Failed to " + step + ". \n" + ex.getMessage());
        } catch (IOException ex) {
            tearDownClass();
            fail("Thrown IOException. Reason: " + ex.getMessage());
        }
    }

    @Test
    public void testSetBearerToken() {
        // authenticate user
        String step = "initialize";
        ChinoAPI chino_user = new ChinoAPI(TestConstants.HOST);
        String accessToken = null,
                refreshToken = null;
        LoggedUser user = null;
        try {
            step = "authenticate user with username '" + TestConstants.USERNAME + "' and password '" + TestConstants.PASSWORD + "'";
            user = chino_user.auth.loginWithPassword(TestConstants.USERNAME, TestConstants.PASSWORD, APP_ID, APP_SECRET);
            accessToken = user.getAccessToken();
            System.out.println("1st access token: " + accessToken);
            refreshToken = user.getRefreshToken();
            System.out.println("1st refresh token: " + refreshToken);

            // Do some operations with the bearer token client
            ChinoAPI apiClient = new ChinoAPI(TestConstants.HOST);
            assertClientWasCreated(apiClient);
            // give the user permission to CRUD and List repositories
            step = "grant perms on repositories";
            PermissionRule repo_grant = new PermissionRule();
            repo_grant.setManage("C", "R", "U", "D", "L");
            chino_customer.permissions.permissionsOnResources("grant", "repositories", "users", USER_ID, repo_grant);

            // use the access token to create a new repo
            step = "create repository";
            Repository rep = apiClient.setBearerToken(accessToken)              // set token in API client
                    .repositories.create("test_repo"  + " [" + TestConstants.JAVA + "]");
            assertNotNull(apiClient.repositories.read(rep.getRepositoryId()));  // client should keep the token saved

            // refresh token - access token is automatically updated in apiClient
            step = "refresh token";
            user = apiClient.auth.refreshToken(refreshToken, APP_ID, APP_SECRET);
            accessToken = user.getAccessToken();
            System.out.println("2nd access token: " + accessToken);
            refreshToken = user.getRefreshToken();
            System.out.println("2nd refresh token: " + refreshToken);

            // use the new access token to delete the repository
            step = "delete repository";
            String repId = rep.getRepositoryId();
            apiClient.setBearerToken(accessToken)  // set new token in API client
                    .repositories.delete(repId, true);

            assertRepositoryWasDeleted(apiClient, rep);

            // log out from the api client
            step = "logout";
            apiClient.auth.logout(accessToken, APP_ID, APP_SECRET);
        } catch (ChinoApiException ex) {
            fail("Thrown ChinoApiException. Failed to " + step + ". \n" + ex.getMessage());
        } catch (IOException ex) {
            tearDownClass();
            fail("Thrown IOException. Reason: " + ex.getMessage());
        }
    }

    private static void assertRepositoryWasDeleted(ChinoAPI apiClient, Repository repository) throws IOException {
        boolean deleted = false;
        try {
            apiClient.repositories.read(repository.getRepositoryId());
        } catch (ChinoApiException x) {
            if (x.getCode().equals("404"))
                deleted = true;
        } finally {
            assertTrue(deleted);
        }
    }

    private static void assertUrlIsNormalized(ChinoAPI c, String url) throws IOException, ChinoApiException {
        assertClientWasCreated(c);
        Repository r = null;
        try {
            r = c.repositories.create("testHostNormalization"  + " [" + TestConstants.JAVA + "]");
        } catch (IOException e) {
            e.printStackTrace(System.err);
            System.err.println("I/O error: " + e.getMessage());
        } catch (ChinoApiException e) {
            System.err.println("FAILED - Chino.io API responded:\n" + e.getErrorResponse());
        } finally {
            // try to clean up the Repository anyway
            assertNotNull("Host normalization failed for '" + url + "'", r);
            try {
                c.repositories.delete(r.getRepositoryId(), true);
                assertRepositoryWasDeleted(c, r);
            } catch (IOException | ChinoApiException ignored) {
                System.err.println("Failed to delete Repository: " + r.getRepositoryId());
            }
        }
    }

    private static void assertClientWasCreated(ChinoAPI c) {
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
