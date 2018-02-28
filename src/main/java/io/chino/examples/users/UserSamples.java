package io.chino.examples.users;

import io.chino.api.common.ChinoApiException;
import io.chino.api.common.Field;
import io.chino.api.user.User;
import io.chino.api.userschema.UserSchema;
import io.chino.api.userschema.UserSchemaStructure;
import io.chino.examples.DeleteAll;
import io.chino.java.ChinoAPI;
import io.chino.examples.userschemas.UserSchemaStructureSample;
import io.chino.examples.Constants;
import java.io.IOException;
import java.util.*;

public class UserSamples {

    String USER_SCHEMA_ID = "";
    String USER_ID = "";
    ChinoAPI chino;
    User user=null;


    public void testUsers() throws IOException, ChinoApiException {

        //You must first initialize your ChinoAPI variable with your customerId and your customerKey
        chino = new ChinoAPI(Constants.HOST, Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY);

        //We create a UserSchema to try this example
        UserSchemaStructure userSchemaStructure = new UserSchemaStructure();

        List<Field> fields = new ArrayList<Field>();
        fields.add(new Field("test_string", "string"));
        fields.add(new Field("test_int", "integer"));
        userSchemaStructure.setFields(fields);

        UserSchema userSchema = chino.userSchemas.create("userSchemaSample", userSchemaStructure);
        USER_SCHEMA_ID = userSchema.getUserSchemaId();

        //Now we try to create a new UserSchema using a Class with some Fields;
        userSchema = chino.userSchemas.create("test_description", UserSchemaStructureSample.class);
        USER_SCHEMA_ID = userSchema.getUserSchemaId();

        //This is a method for creating a User passing a String that represents a json of the attributes
        String attributesStringJson = "\"test_integer\":123, \"test_string\":\"string_value\", \"test_boolean\":true, \"test_date\":\"1992-01-03\", \"test_float\" : 23.2";
        user = chino.users.create(Constants.USERNAME+"1", Constants.PASSWORD, attributesStringJson, USER_SCHEMA_ID);
        System.out.println(user);

        /*
            Let's try to create an User. You must pass the username, password and attributes of the user you want to create
            in the function ChinoAPI.users.create(String username, String password, HashMap attributes, String userSchemaId).
            You have to specify the userSchemaId of the UserSchema you want to use.
            For the attributes you have to create an HashMap. See the example below for the structure.
         */

        HashMap<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("test_string", "test_string_value");
        attributes.put("test_boolean", true);
        attributes.put("test_integer", 123);
        attributes.put("test_date", "1990-12-19");
        attributes.put("test_float", 12.4);

        //Check CREATE user
        user = chino.users.create(Constants.USERNAME, Constants.PASSWORD, attributes, USER_SCHEMA_ID);
        USER_ID = user.getUserId();
        System.out.println(user.toString());

        //Check READ user
        user = chino.users.read(USER_ID);
        System.out.println(user.toString());

        //Let's try to read using MyUser class which extends UserSchemaStructureSample
        MyUser myUser = (MyUser)chino.users.read(USER_ID, MyUser.class);
        myUser.USER_ID=USER_ID;
        System.out.println("MyUser.USER_ID: "+myUser.USER_ID);
        System.out.println("MyUser.test_boolean: "+myUser.test_boolean);
        System.out.println("MyUser.test_integer: "+myUser.test_integer);
        System.out.println("MyUser.test_string: "+myUser.test_string);
        System.out.println("MyUser.test_date: "+myUser.test_date);
        System.out.println("MyUser.test_float: "+myUser.test_float);

        //Check UPDATE user
        attributes.put("test_string", "changedVersion");
        user = chino.users.update(USER_ID, Constants.USERNAME, Constants.PASSWORD, attributes);
        System.out.println(user.toString());

        //Check DELETE user
        String deleteResult = chino.users.delete(USER_ID, true);
        System.out.println(deleteResult);

        //Finally we delete everything we created
        List<User> users = chino.users.list(USER_SCHEMA_ID).getUsers();
        for(User u : users){
            System.out.println(chino.users.delete(u.getUserId(), true));
        }
        System.out.println(chino.userSchemas.delete(USER_SCHEMA_ID, true));
    }
}
