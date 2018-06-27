package io.chino.java;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.chino.api.application.Application;
import io.chino.api.application.ClientType;
import io.chino.api.common.ChinoApiException;
import io.chino.api.user.User;
import io.chino.api.userschema.UserSchema;
import io.chino.java.testutils.ChinoBaseTest;
import io.chino.java.testutils.DeleteAll;
import io.chino.java.testutils.TestConstants;
import io.chino.java.testutils.UserSchemaStructureSample;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class UsersTest extends ChinoBaseTest {

    private static final String PWD = "defaultPwd";
    private static final int MAX_USERS = 10;
    private static ChinoAPI chino_admin;
    private static Users test;
    private static UserSchema userSchema;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");

    @BeforeClass
    public static void setUpClass() throws Exception {
        ChinoBaseTest.beforeClass();
        chino_admin = new ChinoAPI(TestConstants.HOST, TestConstants.CUSTOMER_ID, TestConstants.CUSTOMER_KEY);
        test = (Users) ChinoBaseTest.init(chino_admin.users);

        ChinoBaseTest.checkResourceIsEmpty(
                chino_admin.userSchemas.list().getUserSchemas().isEmpty(),
                chino_admin.userSchemas
        );

        try {
            userSchema = chino_admin.userSchemas.create("Test UserSchema for class UsersTest", UserSchemaStructureSample.class);
        } catch (Exception ex) {
            fail("failed to set up test for UsersTest (create UserSchema).\n"
                    + ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }
    }

    @Before
    public void setUp() throws IOException, ChinoApiException {
        new DeleteAll().deleteAll(test);
    }

    @Test
    public void checkPasswordTest() throws IOException, ChinoApiException {
        User u = newUser("checkPasswordTest");

        try {
            // try to login without authenticating
            test.checkPassword(TestConstants.PASSWORD);
        } catch (ChinoApiException ex) {
            assertEquals("403", ex.getCode());
            assertTrue(ex.getMessage().contains("Only User can get user info"));
        }

        // verify that login is enabled with new credentials
        Application app = chino_admin.applications.create("test Application for checkPasswordTest", "password", "", ClientType.PUBLIC);
        ChinoAPI client = null;
        String token = null;
        try {
            client = new ChinoAPI(TestConstants.HOST);
            token = client.auth.loginWithPassword(u.getUsername(), TestConstants.PASSWORD, app.getAppId()).getAccessToken();

            assertFalse("Returned TRUE with wrong password", client.users.checkPassword("wrong password!"));
            assertTrue("Returned FALSE with correct password", client.users.checkPassword(TestConstants.PASSWORD));
        } finally {
            if (client != null && token == null)
                client.auth.logout(token, app.getAppId());
            chino_admin.applications.delete(app.getAppId(), true);
        }
    }

    @Test
    public void listTest() throws IOException, ChinoApiException {
        LinkedList<User> localUsers = new LinkedList<>();
        for (int i = 0; i < MAX_USERS; i++) {
                localUsers.add(
                        newUser("listTest_" + i)
                );
        }
        /* LIST 1 params */
        LinkedList<User> fetchedUsers = new LinkedList<>(
                test.list(userSchema.getUserSchemaId()).getUsers()
        );
        // check that MAX_USERS users are retrieved
        assertEquals("unexpected size of the fetched Users list", MAX_USERS, fetchedUsers.size());

        int missing = 0;
        StringBuilder missingUsers = new StringBuilder();
        for (User u : localUsers) {
            if (! fetchedUsers.contains(u)) {
                missing ++;
                missingUsers.append("\t" + (++ missing) + ". ").append(u.getUsername()).append("\n");
            }
        }
        // check that all created users are retrieved
        assertEquals(missing + " Users missing: \n" + missingUsers.toString(), 0, missing);

        /* LIST 3 params - offset */
        int offset = MAX_USERS / 2;
        fetchedUsers = new LinkedList<>(
                test.list(userSchema.getUserSchemaId(), offset, MAX_USERS).getUsers()
        );
        // check that (MAX_USERS - offset) users are retrieved
        assertEquals("unexpected size of the fetched Users list (OFFSET = " + offset + ")", MAX_USERS - offset, fetchedUsers.size());

        missing = 0;
        missingUsers = new StringBuilder();
        for (User u : fetchedUsers) {
            if (! localUsers.contains(u)) {
                missing ++;
                missingUsers.append("\t" + (++ missing) + ". ").append(u.getUsername()).append("\n");
            }
        }
        // check that all fetched users are in the local list
        assertEquals(missing + " Users missing: \n" + missingUsers.toString(), 0, missing);

        /* LIST 3 params - limit */
        int limit = 1;
        fetchedUsers = new LinkedList<>(
                test.list(userSchema.getUserSchemaId(), MAX_USERS - 1, limit).getUsers()
        );
        // check that 1 user was retrieved and that it's in the local list
        assertEquals("unexpected size of the fetched Users list (LIMIT = " + limit + ")", 1, fetchedUsers.size());
        assertTrue(localUsers.contains(fetchedUsers.get(0)));
    }

    @Test
    public void createAndReadTest() throws IOException, ChinoApiException {
        UserSchemaStructureSample attr = new UserSchemaStructureSample("readTest");
        attr.test_integer = 42;
        /* CREATE - attrs passed as JSON String*/
        User local = newUser("createAndReadTest_1 (attributes as String)", attr);

        /* READ 1 params */
        User fetched = test.read(local.getUserId());
        assertEquals("Read user is different from local.", local, fetched);

        /* CREATE - attrs passed as HashMap */
        String testName = "test String - createAndReadTest_2 (attributes as HashMap)";
        HashMap<String, Object> attrMap = new HashMap<>();
        attrMap.put("test_integer", 42);
        attrMap.put("test_boolean", true);
        attrMap.put("test_float", (float) 0.987);
        attrMap.put("test_string", testName);

        attrMap.put("test_date", dateFormat.format(new Date()));

        local = test.create(  "createAndReadTest_2_user@test.chino.io", "defaultPwd", attrMap, userSchema.getUserSchemaId());

        /* READ 2 params */
        UserSchemaStructureSample fetchedAttr = (UserSchemaStructureSample) test.read(local.getUserId(), UserSchemaStructureSample.class);

        assertEquals("Read user has different int attribute from local", attr.test_integer, fetchedAttr.test_integer);
        assertEquals("Read user has different String attribute from local", testName, fetchedAttr.test_string);
    }

    @Test
    public void updatePartial_HashMap() throws IOException, ChinoApiException {
        User old = newUser("updatePartial_HashMap");

        UserSchemaStructureSample oldAttrs = (UserSchemaStructureSample) test.read(old.getUserId(), UserSchemaStructureSample.class);
        UserSchemaStructureSample newAttrs = new UserSchemaStructureSample(oldAttrs);
        newAttrs.test_integer ++;

        String newUsername = "UPDATED_" + old.getUsername(),
                newPassword = "updatedPW";

        HashMap<String, Object> updatedAttributes = new HashMap<>();
        updatedAttributes.put("test_integer", newAttrs.test_integer);

        updatedAttributes.put("username", newUsername);
        updatedAttributes.put("password", newPassword);

        User updated = test.updatePartial(old.getUserId(), updatedAttributes);

        assertEquals(old.getUserId(), updated.getUserId());
        assertEquals("user not updated!", newAttrs.test_integer, updated.getAttributesAsHashMap().get("test_integer"));
        assertNotEquals("user not updated!", old.getUsername(), updated.getUsername());

        // verify that login is enabled with new credentials
        Application app = chino_admin.applications.create("test Application for UsersTest", "password", "", ClientType.PUBLIC);
        try {
            ChinoAPI appClient = new ChinoAPI(TestConstants.HOST);
            appClient.setBearerToken(
                    appClient.auth.loginWithPassword(newUsername, newPassword, app.getAppId()).getAccessToken()
            );
        } finally {
            chino_admin.applications.delete(app.getAppId(), true);
        }
    }

    @Test
    public void updatePartial_String() throws IOException, ChinoApiException {
        User old = newUser("updatePartial_String");
        UserSchemaStructureSample oldAttrs = (UserSchemaStructureSample) test.read(old.getUserId(), UserSchemaStructureSample.class);
        UserSchemaStructureSample newAttrs = new UserSchemaStructureSample(oldAttrs);
        newAttrs.test_integer ++;

        String updatedAttributes = "{" +
                "  \"test_integer\":" + newAttrs.test_integer +
                "}";
        User updated = test.updatePartial(old.getUserId(), updatedAttributes);

        assertNotNull("user not updated!", updated);
        assertEquals("user not updated!", newAttrs.test_integer, updated.getAttributesAsHashMap().get("test_integer"));
    }

    @Test
    public void updateTest_HashMap() throws IOException, ChinoApiException {
        User old = newUser("updateTest_HashMap");
        UserSchemaStructureSample oldAttrs = (UserSchemaStructureSample) test.read(old.getUserId(), UserSchemaStructureSample.class);
        UserSchemaStructureSample newAttrs = new UserSchemaStructureSample(oldAttrs);

        HashMap<String, Object> updatedAttributes = new HashMap<>();
        updatedAttributes.put("test_integer", ++ newAttrs.test_integer);
        updatedAttributes.put("test_boolean", newAttrs.test_boolean);
        updatedAttributes.put("test_date", dateFormat.format(newAttrs.test_date));
        updatedAttributes.put("test_float", newAttrs.test_float);
        updatedAttributes.put("test_string", "updateTest_HashMap_updated");

        User updated = test.update(old.getUserId(), old.getUsername(), TestConstants.PASSWORD, updatedAttributes);

        assertNotNull("user not updated!", updated);
        assertEquals("user not updated!", newAttrs.test_integer, updated.getAttributesAsHashMap().get("test_integer"));

    }

    @Test
    public void updateTest_String() throws IOException, ChinoApiException {
        User old = newUser("updatePartial_String");
        UserSchemaStructureSample oldAttrs = (UserSchemaStructureSample) test.read(old.getUserId(), UserSchemaStructureSample.class);
        UserSchemaStructureSample newAttrs = new UserSchemaStructureSample(oldAttrs);
        newAttrs.test_integer ++;

        String updatedAttributes = new ObjectMapper().writeValueAsString(newAttrs);

        User updated = test.update(old.getUserId(), old.getUsername(), TestConstants.PASSWORD, updatedAttributes);

        assertNotNull("user not updated!", updated);
        assertEquals("user not updated!", newAttrs.test_integer, updated.getAttributesAsHashMap().get("test_integer"));
    }

    @Test
    public void deleteTest() throws IOException, ChinoApiException {
        User user = newUser("deleteTest");

        test.delete(user.getUserId(), false);

        user = test.read(user.getUserId());
        assertFalse("User is active", user.getIsActive());

        test.delete(user.getUserId(), true);

        try {
            user = test.read(user.getUserId());
            fail("The user was not deleted");
        } catch (ChinoApiException e) {
            assertEquals("Unexpected error when attempting to read deleted user: " + e.getCode(), "404", e.getCode());
        }
    }

    /**
     * Utility method to create a new User with custom attributes
     *
     */
    private User newUser(String testName, UserSchemaStructureSample customAttrs) throws IOException, ChinoApiException {
        String attrs = new ObjectMapper().writeValueAsString(customAttrs);
        String username = testName.trim().split(" ")[0];
        return test.create(username + "_user@test.chino.io", TestConstants.PASSWORD, attrs, userSchema.getUserSchemaId());
    }

    /**
     * Utility method to create a new User with default attributes
     *
     */
    private User newUser(String testName) throws IOException, ChinoApiException {
        UserSchemaStructureSample userAttributes = new UserSchemaStructureSample(testName);

        return newUser(testName, userAttributes);
    }
}