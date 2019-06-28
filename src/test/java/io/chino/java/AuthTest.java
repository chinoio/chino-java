package io.chino.java;

import io.chino.api.application.Application;
import io.chino.api.application.ClientType;
import io.chino.api.auth.LoggedUser;
import io.chino.api.common.ChinoApiException;
import io.chino.api.common.Field;
import io.chino.api.permission.PermissionRule;
import io.chino.api.repository.Repository;
import io.chino.api.schema.SchemaStructure;
import io.chino.api.user.User;
import io.chino.api.userschema.UserSchema;
import io.chino.java.testutils.ChinoBaseTest;
import io.chino.java.testutils.DeleteAll;
import io.chino.java.testutils.TestConstants;
import io.chino.java.testutils.UserSchemaStructureSample;
import junit.framework.AssertionFailedError;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.Assert.fail;

public class AuthTest extends ChinoBaseTest {

    private static ChinoAPI chino_admin, testClient;
    private static Auth test;

    private static HashMap<String, String> docContent = new HashMap<>();

    private static String SCHEMA_ID;

    @BeforeClass
    public static void beforeClass() throws IOException, ChinoApiException {
        ChinoBaseTest.runClass(AuthTest.class);
        ChinoBaseTest.beforeClass();
        chino_admin = new ChinoAPI(TestConstants.HOST, TestConstants.CUSTOMER_ID, TestConstants.CUSTOMER_KEY);
        test = ChinoBaseTest.init(new ChinoAPI(TestConstants.HOST).auth);

        // create a userschema and a user
        ChinoBaseTest.checkResourceIsEmpty(
                chino_admin.userSchemas.list().getUserSchemas().isEmpty(),
                chino_admin.userSchemas
        );

        UserSchema schema;
        try {
            schema = chino_admin.userSchemas.create("UserSchema for Auth unit testing"  + " [" + TestConstants.JAVA + "]", UserSchemaStructureSample.class);
        } catch (Exception ex) {
            fail("failed to set up test for AuthTest (create UserSchema).\n"
                    + ex.getClass().getSimpleName() + ": " + ex.getMessage());
            return;
        }

        User user;
        HashMap<String, Object> userData = new HashMap<>();
        userData.put("test_string", "test_string_value");
        userData.put("test_boolean", true);
        userData.put("test_integer", 123);
        userData.put("test_date", "1993-09-08");
        userData.put("test_float", 12.4);
        try {
            user = chino_admin.users.create(TestConstants.USERNAME, TestConstants.PASSWORD, userData, schema.getUserSchemaId());
        } catch (Exception ex) {
            fail("failed to set up test for AuthTest (create User).\n"
                    + ex.getClass().getSimpleName() + ": " + ex.getMessage());
            return;
        }

        // create repository and schema for verifying that login was successful
        ChinoBaseTest.checkResourceIsEmpty(
                chino_admin.repositories.list().getRepositories().isEmpty(),
                chino_admin.repositories
        );

        Repository repo = chino_admin.repositories.create("AuthTest"  + " [" + TestConstants.JAVA + "]");
        LinkedList<Field> fields = new LinkedList<>();
        fields.add(new Field("testMethod", "string"));

        SCHEMA_ID = chino_admin.schemas.create(
                    repo.getRepositoryId(),
                    "this Schema is used to verify that users are logged in and can create Documents.",
                    new SchemaStructure(fields)
            ).getSchemaId();

        // grant 'create' Permission to user
        PermissionRule createPerms = new PermissionRule();
        createPerms.setManage("C");
        chino_admin.permissions.permissionsOnResourceChildren("grant", "schemas", SCHEMA_ID, "documents", "users", user.getUserId(), createPerms);
    }

    @AfterClass
    public static void afterClass() throws IOException, ChinoApiException {
        new DeleteAll().deleteAll(chino_admin);
        ChinoBaseTest.skipDelete();
        ChinoBaseTest.afterClass();
    }

    @Before
    public void before() {
        testClient = new ChinoAPI(TestConstants.HOST);
        test = testClient.auth;
    }

    @Test
    public void testLoginWithPasswordCONFIDENTIAL_logout() throws IOException, ChinoApiException {
        Application app = chino_admin.applications.create(
                TestConstants.APP_NAME + " - AuthTest.testLoginWithPasswordCONFIDENTIAL_logout()",
                "password",
                "",
                ClientType.CONFIDENTIAL
        );;

        // test login
        LoggedUser tokens = testClient.auth.loginWithPassword(
                TestConstants.USERNAME,
                TestConstants.PASSWORD,
                app.getAppId(),
                app.getAppSecret()
        );
        testClient.auth.loginWithBearerToken(tokens.getAccessToken());
        assertLoginSuccessful(testClient, "(in) testLoginWithPasswordCONFIDENTIAL_logout");

        // test logout
        testClient.auth.logout(tokens.getAccessToken(), app.getAppId(), app.getAppSecret());
        assertLogoutSuccessful(testClient, "(out) testLoginWithPasswordCONFIDENTIAL_logout");

        success("Password login (confidential) + logout");
    }

    @Test
    public void testLoginWithPasswordPUBLIC_logout() throws IOException, ChinoApiException {
        Application app = chino_admin.applications.create(
                TestConstants.APP_NAME + " - AuthTest.testLoginWithPasswordCONFIDENTIAL_logout()",
                "password",
                "",
                ClientType.PUBLIC
        );;

        // test login
        LoggedUser tokens = testClient.auth.loginWithPassword(
                TestConstants.USERNAME,
                TestConstants.PASSWORD,
                app.getAppId()
        );
        testClient.auth.loginWithBearerToken(tokens.getAccessToken());
        assertLoginSuccessful(testClient, "(in) testLoginWithPasswordPUBLIC_logout");

        // test logout
        testClient.auth.logout(tokens.getAccessToken(), app.getAppId());
        assertLogoutSuccessful(testClient, "(out) testLoginWithPasswordPUBLIC_logout");

        success("Password login (public) + logout");
    }

    @Test
    public void testTokens() throws IOException, ChinoApiException {
        Application app = chino_admin.applications.create(TestConstants.APP_NAME + " - AuthTest.testLoginWithPasswordCONFIDENTIAL_logout()", "password", "", ClientType.CONFIDENTIAL);;

        // get tokens
        LoggedUser tokens = test.loginWithPassword(
                TestConstants.USERNAME,
                TestConstants.PASSWORD,
                app.getAppId(),
                app.getAppSecret()
        );

        // test login with token
        assertLoginSuccessful(testClient, "(1) testTokens");

        // test refresh token
        LoggedUser newTokens = test.refreshToken(
                tokens.getRefreshToken(),
                app.getAppId(),
                app.getAppSecret()
        );
        assertLoginSuccessful(testClient, "(2) testTokens");

        // test ChinoAPI client constructor with bearer token
        try {
            // check that old tokens are invalid after logout...
            test.logout(tokens.getAccessToken(), app.getAppId(), app.getAppSecret());
            assertLoginSuccessful(new ChinoAPI(TestConstants.HOST, tokens.getAccessToken()), "(4) testTokens");
            fail("Old tokens are valid after logout");
        } catch (AssertionFailedError err) {
            // expected
        }

        // test loginWithBearerToken
        ChinoAPI newClient = new ChinoAPI(TestConstants.HOST);
        newClient.auth.loginWithBearerToken(newTokens.getAccessToken());
        assertLoginSuccessful(newClient, "(3) testTokens");

        success("tokens");
    }

    private static void assertLoginSuccessful(ChinoAPI client, String testMethodName) throws IOException, ChinoApiException {
        try {
            docContent.put("testMethod", testMethodName);
            client.documents.create(SCHEMA_ID, docContent);
        } catch (ChinoApiException | IOException e) {
            if (e instanceof ChinoApiException && ((ChinoApiException) e).getCode().equals("403")) {
                // Somebody forgot to grant 'create' Permissions on the Schema?
                throw e;
            }
            throw new AssertionFailedError("Failed to login. " + e.getClass().getCanonicalName() + ": " + e.getMessage());
        }
    }

    private static void assertLogoutSuccessful(ChinoAPI client, String testMethodName) throws IOException {
        try {
            docContent.put("testMethod", testMethodName);
            client.documents.create(SCHEMA_ID, docContent);
            throw new AssertionFailedError("Failed to logout.");
        } catch (ChinoApiException e) {
            if (e.getCode().equals("401")) {
                return;
            }
            throw new AssertionFailedError("Failed to logout. Expected '401'; server responded '" + e.getMessage() + "'");
        }
    }
}