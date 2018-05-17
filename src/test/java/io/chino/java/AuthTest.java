package io.chino.java;

import io.chino.api.application.Application;
import io.chino.api.application.ClientType;
import io.chino.api.auth.LoggedUser;
import io.chino.api.common.ChinoApiException;
import io.chino.api.common.Field;
import io.chino.api.repository.Repository;
import io.chino.api.schema.SchemaStructure;
import io.chino.api.userschema.UserSchema;
import io.chino.java.testutils.ChinoBaseTest;
import io.chino.java.testutils.DeleteAll;
import io.chino.java.testutils.TestConstants;
import io.chino.java.testutils.UserSchemaStructureSample;
import junit.framework.AssertionFailedError;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class AuthTest extends ChinoBaseTest {

    private static ChinoAPI chino_admin;
    private static Auth test;

    private static String SCHEMA_ID;

    @BeforeClass
    public static void beforeClass() throws IOException, ChinoApiException {
        ChinoBaseTest.beforeClass();
        chino_admin = new ChinoAPI(TestConstants.HOST);
        test = (Auth) ChinoBaseTest.init(chino_admin.auth);

        // create a userschema and a user
        ChinoBaseTest.checkResourceIsEmpty(
                chino_admin.userSchemas.list().getUserSchemas().isEmpty(),
                chino_admin.userSchemas
        );

        UserSchema schema;
        try {
            schema = chino_admin.userSchemas.create("UserSchema for Auth unit testing", UserSchemaStructureSample.class);
        } catch (Exception ex) {
            fail("failed to set up test for AuthTest (create UserSchema).\n"
                    + ex.getClass().getSimpleName() + ": " + ex.getMessage());
            return;
        }

        HashMap<String, Object> userData = new HashMap<>();
        userData.put("test_string", "test_string_value");
        userData.put("test_boolean", true);
        userData.put("test_integer", 123);
        userData.put("test_date", "1993-09-08");
        userData.put("test_float", 12.4);
        try {
            chino_admin.users.create(TestConstants.USERNAME, TestConstants.PASSWORD, userData, schema.getUserSchemaId());
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

        Repository repo = chino_admin.repositories.create("AuthTest");
        LinkedList<Field> fields = new LinkedList<>();
        fields.add(new Field("testMethod", "string"));

        SCHEMA_ID = chino_admin.schemas.create(
                    repo.getRepositoryId(),
                    "this Schema is used to verify that users are logged in and can create Documents.",
                    new SchemaStructure(fields)
            ).getSchemaId();
    }

    @AfterClass
    public static void afterClass() throws IOException, ChinoApiException {
        ChinoBaseTest.afterClass();

        new DeleteAll().deleteAll(chino_admin);
    }

    @Test
    public void testLoginWithPasswordCONFIDENTIAL_logout() throws IOException, ChinoApiException {
        Application app = chino_admin.applications.create(TestConstants.APP_NAME + " - AuthTest.testLoginWithPasswordCONFIDENTIAL_logout()", "password", "", ClientType.CONFIDENTIAL);;

        // test login
        LoggedUser tokens = test.loginWithPassword(
                TestConstants.USERNAME,
                TestConstants.PASSWORD,
                app.getAppId(),
                app.getAppSecret()
        );
        assertLoginSuccessful(chino_admin, "(in) testLoginWithPasswordCONFIDENTIAL_logout");

        // test logout
        test.logout(tokens.getAccessToken(), app.getAppId(), app.getAppSecret());
        assertLogoutSuccessful(chino_admin, "(out) testLoginWithPasswordCONFIDENTIAL_logout");

        success("Password login (confidential) + logout");
    }

    @Test
    public void testLoginWithPasswordPUBLIC_logout() throws IOException, ChinoApiException {
        Application app = chino_admin.applications.create(TestConstants.APP_NAME + " - AuthTest.testLoginWithPasswordCONFIDENTIAL_logout()", "password", "", ClientType.CONFIDENTIAL);;

        // test login
        LoggedUser tokens = test.loginWithPassword(
                TestConstants.USERNAME,
                TestConstants.PASSWORD,
                app.getAppId()
        );
        assertLoginSuccessful(chino_admin, "(in) testLoginWithPasswordPUBLIC_logout");

        // test logout
        test.logout(tokens.getAccessToken(), app.getAppId());
        assertLogoutSuccessful(chino_admin, "(out) testLoginWithPasswordPUBLIC_logout");

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
        ChinoAPI bearerTokenClient = new ChinoAPI(TestConstants.HOST);
        bearerTokenClient.auth.loginWithBearerToken(
                tokens.getAccessToken()
        );
        assertLoginSuccessful(bearerTokenClient, "(1) testTokens");

        // test refresh token
        LoggedUser newTokens = bearerTokenClient.auth.refreshToken(
                tokens.getRefreshToken(),
                app.getAppId(),
                app.getAppSecret()
        );
        assertLoginSuccessful(bearerTokenClient, "(2) testTokens");

        // test ChinoAPI client constructor with bearer token
        assertLoginSuccessful(new ChinoAPI(TestConstants.HOST, newTokens.getAccessToken()), "(3) testTokens");

        // check that old tokens are invalid after refresh
        try {
            assertLoginSuccessful(new ChinoAPI(TestConstants.HOST, tokens.getAccessToken()), "(4) testTokens");
            fail("Old tokens are valid after refreshs");
        } catch (AssertionFailedError err) {
            success("tokens");
        }

    }

    private static HashMap<String, String> docContent = new HashMap<>();

    private static void assertLoginSuccessful(ChinoAPI client, String testMethodName) throws IOException, ChinoApiException {
        docContent.put("testMethod", testMethodName);
        client.documents.create(SCHEMA_ID, docContent);
    }

    private static void assertLogoutSuccessful(ChinoAPI client, String testMethodName) throws IOException {
        try {
            docContent.put("testMethod", testMethodName);
            client.documents.create(SCHEMA_ID, docContent);
            fail("You did not log out.");
        } catch (ChinoApiException e) {
            return;
        }
    }
}