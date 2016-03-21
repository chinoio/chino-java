package io.chino.examples.groups;

import io.chino.api.common.ChinoApiException;
import io.chino.api.group.Group;
import io.chino.api.user.User;
import io.chino.api.userschema.UserSchema;
import io.chino.client.ChinoAPI;
import io.chino.examples.userschemas.UserSchemaStructureSample;
import io.chino.examples.util.Constants;
import java.io.IOException;
import java.util.HashMap;

public class GroupSamples {

    ChinoAPI chino;
    String GROUP_ID = "";
    String USER_ID = "";
    String USER_SCHEMA_ID = "";

    public void testGroups() throws IOException, ChinoApiException {

        //You must first initialize your ChinoAPI variable with your customerId and your customerKey
        chino = new ChinoAPI(Constants.HOST, Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY);

        //We create a group
        HashMap<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("test_attribute", "test_attribute_value");
        Group group = chino.groups.create("test_group", attributes);
        GROUP_ID = group.getGroupId();

        //Now we create a User and then we add him to the Group
        UserSchema userSchema = chino.userSchemas.create("test_description", UserSchemaStructureSample.class);
        USER_SCHEMA_ID = userSchema.getUserSchemaId();

        attributes = new HashMap<String, Object>();
        attributes.put("test_string", "test_string_value");
        attributes.put("test_boolean", true);
        attributes.put("test_integer", 123);
        attributes.put("test_date", "1992-12-11");
        attributes.put("test_float", 12.4);
        User user = chino.users.create(Constants.USERNAME, Constants.PASSWORD, attributes, USER_SCHEMA_ID);
        USER_ID = user.getUserId();

        System.out.println(chino.groups.addUserToGroup(USER_ID, GROUP_ID));

        //Now we create a new User and we add him to the Group too
        attributes = new HashMap<String, Object>();
        attributes.put("test_string", "test_string_value");
        attributes.put("test_boolean", true);
        attributes.put("test_integer", 123);
        attributes.put("test_date", "1989-03-06");
        attributes.put("test_float", 12.4);
        user = chino.users.create(Constants.USERNAME+"1", Constants.PASSWORD, attributes, USER_SCHEMA_ID);
        USER_ID = user.getUserId();

        System.out.println(chino.groups.addUserToGroup(USER_ID, GROUP_ID));

        //Check if the User is a member of the Group
        System.out.println(chino.users.read(USER_ID));

        //Now try to remove that User from the Group
        System.out.println(chino.groups.removeUserFromGroup(USER_ID, GROUP_ID));

        //Check the User again
        System.out.println(chino.users.read(USER_ID));

        //We repeat the operations above with a UserSchema
        System.out.println(chino.groups.addUserSchemaToGroup(USER_SCHEMA_ID, GROUP_ID));

        //We create a new User with that UserSchema and we check if the new User is a member of the Group
        attributes = new HashMap<String, Object>();
        attributes.put("test_string", "test_string_value");
        attributes.put("test_boolean", true);
        attributes.put("test_integer", 123);
        attributes.put("test_date", "1991-05-07");
        attributes.put("test_float", 12.4);
        user = chino.users.create(Constants.USERNAME+"2", Constants.PASSWORD, attributes, USER_SCHEMA_ID);
        USER_ID = user.getUserId();
        System.out.println(chino.users.read(USER_ID));

        //Now we remove the UserSchema from the Group
        System.out.println(chino.groups.removeUserSchemaFromGroup(USER_SCHEMA_ID, GROUP_ID));

    }
}
