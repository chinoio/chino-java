package io.chino.java;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.chino.api.common.ChinoApiException;
import io.chino.api.group.Group;
import io.chino.api.user.User;
import io.chino.java.testutils.TestClassStructure;
import io.chino.java.testutils.ChinoBaseTest;
import io.chino.java.testutils.TestConstants;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class GroupsTest extends ChinoBaseTest {

    private static ChinoAPI chino_admin;
    private static Groups test;

    private static String USER_SCHEMA_ID;

    @BeforeClass
    public static void beforeClass() throws IOException, ChinoApiException {
        ChinoBaseTest.beforeClass();
        chino_admin = new ChinoAPI(TestConstants.HOST, TestConstants.CUSTOMER_ID, TestConstants.CUSTOMER_KEY);
        test = ChinoBaseTest.init(chino_admin.groups);

        // cleanup groups
        ChinoBaseTest.checkResourceIsEmpty(
                chino_admin.groups.list().getGroups().isEmpty(),
                chino_admin.groups
        );

        // create a UserSchema
        ChinoBaseTest.checkResourceIsEmpty(
                chino_admin.userSchemas.list().getUserSchemas().isEmpty(),
                chino_admin.userSchemas
        );

        USER_SCHEMA_ID = chino_admin.userSchemas.create("GroupsTest", TestClassStructure.class)
                            .getUserSchemaId();
    }

    @Test
    public void test_CRUD() throws IOException, ChinoApiException {
        /* CREATE */
        String groupName = "Group for testing Chino.io SDK";
        Group c = test.create(groupName, new HashMap());
        assertNotNull("Group was not created! (null reference)", c);

        /* READ */
        Group r = test.read(c.getGroupId());
        assertEquals("Read object is different from original", c, r);

        /* UPDATE */
        HashMap<String, Object> attrs = new HashMap<>();
        attrs.put("attr", "");
        Group u = test.update(c.getGroupId(), "UPDATED", attrs);

        assertNotEquals("Object was not updated", c, u);
        assertEquals("Update failed", "UPDATED", u.getGroupName());
        HashMap<String, Object> control = new ObjectMapper().convertValue(u.getAttributes(),
                new TypeReference<HashMap<String, Object>>() {}
        );
        assertFalse("Group attributes weren't updated", control.isEmpty());
        assertTrue(control.containsKey("attr"));

        /* DELETE */
        test.delete(u.getGroupId(), true);

        try {
            test.read(c.getGroupId());
            fail("Object was not deleted.");
        } catch (ChinoApiException e) {
            System.out.println("Success");
        }
    }

    @Test
    public void test_list() throws IOException, ChinoApiException {
        Group[] groups = {
                test.create("test_list_group1", new HashMap()),
                test.create("test_list_group2", new HashMap()),
                test.create("test_list_group3", new HashMap()),
                test.create("test_list_group4", new HashMap()),
                test.create("test_list_group5", new HashMap())
        };
        /* LIST (no args) */
        assertEquals( "Missing groups in list",
                groups.length,
                test.list().getGroups().size()
        );

        /* LIST (2 args) */
        int offset = 0;
        int limit = 2;
        assertEquals( "Wrong list size (1)",
                limit,
                test.list(offset, limit).getGroups().size()
        );

        offset = groups.length - 1;
        limit = groups.length;
        assertEquals( "Wrong list size (2)",
                1,
                test.list(offset, limit).getGroups().size()
        );

        offset = 2;
        limit = groups.length;
        assertEquals( "Wrong list size (3)",
                limit - offset,
                test.list(offset, limit).getGroups().size()
        );
    }

    @Test
    public void test_users() throws IOException, ChinoApiException {
        User[] users = {
                makeUser("test_users_usr1"),
                makeUser("test_users_usr2"),
                makeUser("test_users_usr3"),
                makeUser("test_users_usr4"),
                makeUser("test_users_usr5")
        };
        Group group = test.create("GroupsTest.test_users", new HashMap());

        /* ADD */
        for (User u : users) {
            test.addUserToGroup(u.getUserId(), group.getGroupId());
            assertUserIsIn(u, group);
        }

        /* REMOVE */
        for (User u : users) {
            test.removeUserFromGroup(u.getUserId(), group.getGroupId());
            assertUserIsNotIn(u, group);
        }

        // Skipping these tests because they don't work at the moment
        // TODO uncomment all as soon as the addition of UserSchemas to a Group works correctly
//        /* ADD USER SCHEMA */
//        test.addUserSchemaToGroup(USER_SCHEMA_ID, group.getGroupId());
//        assertUserIsIn(users[0], group);
//        assertUserIsIn(
//                makeUser("test_user_new1"),
//                group
//        );
//
//        /* REMOVE USER SCHEMA */
//        test.removeUserSchemaFromGroup(USER_SCHEMA_ID, group.getGroupId());
//        assertUserIsNotIn(users[0], group);
//        assertUserIsNotIn(
//                makeUser("test_user_new2"),
//                group
//        );
    }

    private static void assertUserIsIn(User user, Group group) throws IOException, ChinoApiException {
        User check = chino_admin.users.read(user.getUserId());
        List groups = check.getGroups();

        assertTrue(
            "User " + user.getUsername() + " is not in group " + group.getGroupName(),
            groups.contains(group.getGroupId())
        );
    }

    private static void assertUserIsNotIn(User user, Group group) throws IOException, ChinoApiException {
        User check = chino_admin.users.read(user.getUserId());
        List groups = check.getGroups();

        assertFalse(
            "User " + user.getUsername() + " is still in group " + group.getGroupName(),
            groups.contains(group.getGroupId())
        );
    }

    private User makeUser(String name) throws IOException, ChinoApiException {
        return chino_admin.users.create(name, "1234567890", new TestClassStructure(name).toMap(), USER_SCHEMA_ID);
    }

    @AfterClass
    public static void afterClass() throws IOException, ChinoApiException {
        ChinoBaseTest.afterClass();
    }
}
