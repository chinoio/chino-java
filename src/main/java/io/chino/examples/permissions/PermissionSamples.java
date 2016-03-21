package io.chino.examples.permissions;

import io.chino.api.auth.LoggedUser;
import io.chino.api.common.ChinoApiException;
import io.chino.api.document.Document;
import io.chino.api.group.Group;
import io.chino.api.permission.*;
import io.chino.api.repository.Repository;
import io.chino.api.schema.Schema;
import io.chino.api.user.User;
import io.chino.api.userschema.UserSchema;
import io.chino.client.ChinoAPI;
import io.chino.examples.schemas.SchemaStructureSample;
import io.chino.examples.userschemas.UserSchemaStructureSample;
import io.chino.examples.util.Constants;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class PermissionSamples {

    String REPOSITORY_ID = "";
    String SCHEMA_ID = "";
    String DOCUMENT_ID = "";
    String USER_SCHEMA_ID = "";
    String USER_ID = "";
    String GROUP_ID = "";
    String TOKEN = "";
    String USERNAME = "Giovanni";
    String PASSWORD = "password";
    ChinoAPI chino;

    public void testPermissions() throws IOException, ChinoApiException {

        //You must first initialize your ChinoAPI variable with your customerId and your customerKey
        chino = new ChinoAPI(Constants.HOST, Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY);

        //We try first with a Repository and a User to apply the Permissions
        Repository repository = chino.repositories.create("test_repository");
        REPOSITORY_ID = repository.getRepositoryId();

        UserSchema userSchema = chino.userSchemas.create("test_description", UserSchemaStructureSample.class);
        USER_SCHEMA_ID = userSchema.getUserSchemaId();

        HashMap<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("test_string", "test_string_value");
        attributes.put("test_boolean", true);
        attributes.put("test_integer", 123);
        attributes.put("test_date", "1993-09-08");
        attributes.put("test_float", 12.4);
        User user = chino.users.create(USERNAME, PASSWORD, attributes, USER_SCHEMA_ID);
        USER_ID = user.getUserId();

        PermissionRule rule = new PermissionRule();
        rule.setAuthorize(PermissionValues.READ);
        rule.setManage(PermissionValues.READ, PermissionValues.UPDATE, PermissionValues.DELETE);
        System.out.println(chino.permissions.permissionsOnaResource(PermissionValues.GRANT, PermissionValues.REPOSITORIES, REPOSITORY_ID, PermissionValues.USERS, USER_ID, rule));

        /*
            And now we try to add permission to all Documents created by a certain User on a certain Schema. In this case
            we need to create permissions on Resource Children
        */

        Schema schema = chino.schemas.create(REPOSITORY_ID, "sample_description", SchemaStructureSample.class);
        SCHEMA_ID = schema.getSchemaId();

        //Now we create a rule with the field "created_document"
        PermissionRuleCreatedDocument permissionRuleCreatedDocument = new PermissionRuleCreatedDocument();
        //You can even use this method to set the Array of Strings for authorize and manage
        permissionRuleCreatedDocument.setAuthorize("R", "C", "U");
        permissionRuleCreatedDocument.setManage("R", "C", "U", "D");
        rule = new PermissionRule();
        rule.setAuthorize("R", "U");
        rule.setManage("R", "U", "D");
        permissionRuleCreatedDocument.setCreatedDocument(rule);
        System.out.println(chino.permissions.permissionsOnResourceChildren(PermissionValues.GRANT, PermissionValues.SCHEMAS, SCHEMA_ID, PermissionValues.DOCUMENTS, PermissionValues.USERS, USER_ID, permissionRuleCreatedDocument));

        /*
            We try to create a Document using the Schema with permissions and try to read the permissions on that Document.
            We need to do this using the User who has the permissions on "created_document"
         */

        LoggedUser loggedUser = chino.auth.loginUser(USERNAME, PASSWORD, Constants.CUSTOMER_ID);
        System.out.println(TOKEN = loggedUser.getAccessToken());

        //Let's try to read the status of the User
        System.out.println(chino.auth.checkUserStatus());

        HashMap<String, Object> content = new HashMap<String, Object>();
        content.put("test_string", "test_string_value");
        content.put("test_integer", 123);
        content.put("test_boolean", true);
        content.put("test_date", "1994-02-03");

        Document document = chino.documents.create(SCHEMA_ID, content);
        DOCUMENT_ID = document.getDocumentId();

        //Let's read the permissions of the User
        System.out.println("Permissions of the User:");
        System.out.println(chino.permissions.readPermissionsOfaUser(USER_ID, 0));

        //And now let's read the permissions on the Document
        System.out.println("Permissions of the Document:");
        GetPermissionsResponse permissionsResponse = chino.permissions.readPermissionsOnaDocument(DOCUMENT_ID, 0);
        System.out.println(permissionsResponse);
        for(Permission permission : permissionsResponse.getPermissions()){
            List<?> authorizeList = (List<?>)permission.getPermission().get("Authorize");
            List<?> manageList = (List<?>)permission.getPermission().get("Manage");
            System.out.println("Authorize: "+authorizeList);
            System.out.println("Manage: "+manageList);
        }

        //Let's try to read all permissions
        System.out.println("");
        System.out.println(chino.permissions.readPermissions(0));

        System.out.println(chino.auth.logoutUser());
        chino.auth.setCustomer(Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY);

        //Now we create a Group and we'll add and read permissions on it
        attributes = new HashMap<String, Object>();
        attributes.put("test_attribute", "test_attribute_value");
        Group group = chino.groups.create("test_group", attributes);
        GROUP_ID = group.getGroupId();

        rule = new PermissionRule();
        rule.setAuthorize(PermissionValues.READ, PermissionValues.UPDATE);
        rule.setManage(PermissionValues.READ, PermissionValues.UPDATE, PermissionValues.CREATE);
        chino.permissions.permissionsOnResources(PermissionValues.GRANT, PermissionValues.REPOSITORIES, PermissionValues.GROUPS, GROUP_ID, rule);
        chino.permissions.permissionsOnResourceChildren(PermissionValues.GRANT, PermissionValues.REPOSITORIES, REPOSITORY_ID, PermissionValues.SCHEMAS, PermissionValues.GROUPS, GROUP_ID, rule);

        System.out.println("");
        System.out.println(chino.permissions.readPermissionsOfaGroup(GROUP_ID, 0));
    }
}
