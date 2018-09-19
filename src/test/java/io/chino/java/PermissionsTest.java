package io.chino.java;

import io.chino.api.application.Application;
import io.chino.api.application.ClientType;
import io.chino.api.common.ChinoApiException;
import io.chino.api.common.Field;
import io.chino.api.document.Document;
import io.chino.api.group.Group;
import io.chino.api.permission.Permission;
import io.chino.api.permission.PermissionRule;
import io.chino.api.permission.PermissionRuleCreatedDocument;
import io.chino.api.schema.SchemaStructure;
import io.chino.api.user.User;
import io.chino.java.testutils.ChinoBaseTest;
import io.chino.java.testutils.DeleteAll;
import io.chino.java.testutils.TestConstants;
import io.chino.java.testutils.UserSchemaStructureSample;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static io.chino.java.Permissions.*;
import static org.junit.Assert.*;

public class PermissionsTest extends ChinoBaseTest {

    private static ChinoAPI chino_admin;
    private static Permissions test;

    private static String REPO_ID, SCHEMA_ID, USER_SCHEMA_ID;

    @BeforeClass
    public static void beforeClass() throws IOException, ChinoApiException {
        ChinoBaseTest.beforeClass();
        chino_admin = new ChinoAPI(TestConstants.HOST, TestConstants.CUSTOMER_ID, TestConstants.CUSTOMER_KEY);
        test = ChinoBaseTest.init(chino_admin.permissions);


        ChinoBaseTest.checkResourceIsEmpty(
                chino_admin.userSchemas.list().getUserSchemas().isEmpty(),
                chino_admin.userSchemas
        );

        ChinoBaseTest.checkResourceIsEmpty(
                chino_admin.repositories.list().getRepositories().isEmpty(),
                chino_admin.repositories
        );

        ChinoBaseTest.checkResourceIsEmpty(
                chino_admin.applications.list().getApplications().isEmpty(),
                chino_admin.applications
        );

        ChinoBaseTest.checkResourceIsEmpty(
                chino_admin.groups.list().getGroups().isEmpty(),
                chino_admin.groups
        );


        // create a userschema and a user
        try {
            USER_SCHEMA_ID = chino_admin.userSchemas.create("UserSchema for Permissions unit testing", UserSchemaStructureSample.class)
                .getUserSchemaId();
        } catch (Exception ex) {
            fail("failed to set up test for PermissionsTest (create UserSchema).\n"
                    + ex.getClass().getSimpleName() + ": " + ex.getMessage());
            return;
        }

        // create repository and schema to set permissions on
        REPO_ID = chino_admin.repositories.create("PermissionsTest").getRepositoryId();
        LinkedList<Field> fields = new LinkedList<>();
        fields.add(new Field("testMethod", "string"));

        SCHEMA_ID = chino_admin.schemas.create(
                REPO_ID,
                "this Schema is used for PermissionsTest.",
                new SchemaStructure(fields)
        ).getSchemaId();
    }

    @Test
    public void test_GrantOnResourceChildren_readPermsUser() throws IOException, ChinoApiException {
        User user = makeUser("test_GrantOnResourceChildren_readPermsUser");

        // grant 'create' Permission to user
        LinkedList<String> permissionSetter = new LinkedList<>();
        permissionSetter.add("C");
        permissionSetter.add("R");
        PermissionRule create = new PermissionRule(permissionSetter, null);
        test.permissionsOnResourceChildren(GRANT, SCHEMAS, SCHEMA_ID, childrenOf(SCHEMAS), USERS, user.getUserId(), create);

        // read Permissions
        List<Permission> perms = test.readPermissionsOfaUser(user.getUserId()).getPermissions();
        StringBuilder list = toString(perms);
        assertEquals("Permission list has wrong size.\n" + list.toString(),1, perms.size());

        Permission r = perms.get(0);
        assertEquals(SCHEMA_ID, r.getResourceId());
        HashMap permissions = r.getPermission();

        List<String> manage = (List<String>) permissions.get("Manage");
        assertEquals("'manage' Permissions over Schema has the wrong size",2, manage.size());
        assertTrue("READ permission wasn't set.\n\n\"manage\":\n" + manage.toString(), manage.contains("R"));
    }

    private static StringBuilder toString(List<?> list) {
        StringBuilder builder = new StringBuilder("[\n");
        list.forEach(
                p -> builder.append(p.toString()).append(",\n")
        );
        return builder.append("]\n");
    }

    @Test
    public void test_GrantOnResource_createdDocument_readPermsUser() throws IOException, ChinoApiException {
        User user = makeUser("test_GrantOnResourceChildren_createdDocument_readPermsUser");

        // grant 'create' & 'read' Permission to user
        PermissionRule onCreatedDocuments = new PermissionRule(
                new String[] {"R", "U", "D"},
                new String[] {"R"}
        );
        PermissionRuleCreatedDocument createRead = new PermissionRuleCreatedDocument(
                new String[] {"R", "D"},
                null,
                onCreatedDocuments
        );

        test.permissionsOnResourceChildren(GRANT, SCHEMAS, SCHEMA_ID, childrenOf(SCHEMAS),  USERS, user.getUserId(), createRead);

        // read Permissions
        List<Permission> perms = test.readPermissionsOfaUser(user.getUserId()).getPermissions();
        StringBuilder list = toString(perms);
        assertEquals("Permission list has wrong size.\n" + list.toString(),1, perms.size());

        Permission r = perms.get(0);
        assertEquals(SCHEMA_ID, r.getResourceId());
        HashMap permissions = r.getPermission();

        List<String> manage = (List<String>) permissions.get("Manage");
        assertTrue(SCHEMA_ID, manage.contains("R"));
        assertEquals("'manage' Permissions over Schema has the wrong size",2, manage.size());
    }

    @Test
    public void test_GrantOnResource_readPermsGroup() throws IOException, ChinoApiException {
        Group group = makeGroup(5, "test_GrantOnResource_readPermsGroup");

        // grant 'create' Permission to group
        PermissionRule create = new PermissionRule();
        create.setManage("R", "U");
        test.permissionsOnaResource(GRANT, USER_SCHEMAS, USER_SCHEMA_ID, GROUPS, group.getGroupId(), create);

        // read Permissions
        List<Permission> perms = test.readPermissionsOfaGroup(group.getGroupId()).getPermissions();
        StringBuilder list = toString(perms);
        assertEquals("Permission list has wrong size.\n" + list.toString(),1, perms.size());

        Permission r = perms.get(0);
        assertEquals(USER_SCHEMA_ID, r.getResourceId());
        HashMap permissions = r.getPermission();

        List<String> manage = (List<String>) permissions.get("Manage");
        assertTrue(SCHEMA_ID, manage.contains("R"));
        assertTrue(SCHEMA_ID, manage.contains("U"));
    }

    @Test
    public void testGrantRevokeOnResources() throws IOException, ChinoApiException {
        User u = makeUser("testGrantRevokeOnResources");

        PermissionRule readRepositories = new PermissionRule(
                new String[] {"R", "C"},
                new String[] {"R"}
        );

        test.permissionsOnResources(GRANT, REPOSITORIES, USERS, u.getUserId(), readRepositories);

        readRepositories = new PermissionRule(
                new String[] {"C"},
                new String[] {"R"});
        test.permissionsOnResources(REVOKE, REPOSITORIES, USERS, u.getUserId(), readRepositories);
    }

    @Test
    public void test_readPerms_readPermsOnDoc() throws IOException, ChinoApiException {
        String password = "test_readPerms";
        User u = makeUser(password);

        HashMap<String, Object> docContent = new HashMap<>();
        docContent.put("testMethod", password);
        chino_admin.permissions.permissionsOnResourceChildren(GRANT, SCHEMAS, SCHEMA_ID, childrenOf(SCHEMAS), USERS, u.getUserId(),
                new PermissionRuleCreatedDocument(new String[]{"R", "D"}, new String[]{"R"},
                        new PermissionRule(new String[]{"R"}, new String[]{})
                )
        );
        Document doc = chino_admin.documents.create(SCHEMA_ID, docContent);

        ChinoAPI chino_user = new ChinoAPI(TestConstants.HOST);
        Application app = chino_admin.applications.create("test_readPerms_readPermsOnDoc", "password", "", ClientType.PUBLIC);
        chino_user.auth.loginWithPassword(u.getUsername(), password, app.getAppId());

        /* READ PERMISSIONS */

        List<Permission> perms = chino_user.permissions.readPermissions().getPermissions();

        assertEquals(1, perms.size());
        Permission onSchema = perms.get(0);
        assertEquals(SCHEMA_ID, onSchema.getResourceId());

        chino_admin.permissions.permissionsOnaResource(GRANT, DOCUMENTS, doc.getDocumentId(), USERS, u.getUserId(),
                new PermissionRule(new String[]{"U"}, new String[]{})
        );
        perms = chino_user.permissions.readPermissionsOnaDocument(doc.getDocumentId()).getPermissions();
        assertEquals(1, perms.size());
        Permission onDoc = perms.get(0);
        List<String> canManage = (List<String>) onDoc.getPermission().get("Manage");

        assertTrue(canManage.contains("U"));
    }

    @Test
    public void test_childrenOf() {
        assertEquals(SCHEMAS, childrenOf(REPOSITORIES));
        assertEquals(DOCUMENTS, childrenOf(SCHEMAS));
        assertEquals(USERS, childrenOf(USER_SCHEMAS));
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_exception_childrenOf() {
        System.out.println(
                childrenOf(DOCUMENTS)
        );
    }

    @After
    public void afterTest() throws IOException, ChinoApiException {
        new DeleteAll().deleteAll(chino_admin.users);
        new DeleteAll().deleteAll(chino_admin.documents);
    }

    private User makeUser(String testName) {
        if (testName.length() > 36)
            testName = testName.substring(0, 36);

        User user = null;

        HashMap<String, Object> userData = new HashMap<>();
        userData.put("test_string", "test_string_value");
        userData.put("test_boolean", true);
        userData.put("test_integer", 123);
        userData.put("test_date", "1993-09-08");
        userData.put("test_float", 12.4);

        String name = testName.replace("test_", "user_"),
            password = testName;

        try {
            user = chino_admin.users.create(name, password, userData, USER_SCHEMA_ID);
        } catch (Exception ex) {
            fail("failed to create user for PermissionsTest." + testName + "().\n"
                    + ex.getClass().getSimpleName() + ": " + ex.getMessage());
        }

        return user;
    }

    private Group makeGroup(int n, String testName) throws IOException, ChinoApiException {
        if (testName.length() > 32)
            testName = testName.substring(0, 32);
        Group g = chino_admin.groups.create(testName, new HashMap());
        for (int i=0; i < n; i++) {
            User u = makeUser((i + 1) + "_" + testName);
            chino_admin.groups.addUserToGroup(u.getUserId(), g.getGroupId());
        }
        return g;
    }
}
