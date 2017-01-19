package io.chino.examples.userschemas;

import io.chino.api.common.ChinoApiException;
import io.chino.api.common.Field;
import io.chino.api.userschema.*;
import io.chino.android.ChinoAPI;
import io.chino.examples.util.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserSchemaSamples {

    String USER_SCHEMA_ID1 = "";
    String USER_SCHEMA_ID2 = "";
    String USER_SCHEMA_ID3 = "";
    ChinoAPI chino;

    public void testUserSchemas() throws IOException, ChinoApiException {

        //You must first initialize your ChinoAPI variable with your customerId and your customerKey
        chino = new ChinoAPI(Constants.HOST, Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY);

        //Check CREATE UerSchema with all three methods

        //First method, passing a UserSchemaRequest
        UserSchemaRequest userSchemaRequest = new UserSchemaRequest();
        userSchemaRequest.setDescription("user_schema_description1");
        UserSchemaStructure userSchemaStructure = new UserSchemaStructure();
        List<Field> fields = new ArrayList<Field>();
        fields.add(new Field("test_string1", "string"));
        fields.add(new Field("test_integer1", "integer"));
        fields.add(new Field("test_date1", "date"));
        userSchemaStructure.setFields(fields);
        userSchemaRequest.setStructure(userSchemaStructure);
        UserSchema userSchema1 = chino.userSchemas.create(userSchemaRequest);
        USER_SCHEMA_ID1 = userSchema1.getUserSchemaId();
        System.out.println(userSchema1);

        //Second method, passing a description and a UserSchemaStructure
        userSchemaStructure = new UserSchemaStructure();
        fields = new ArrayList<Field>();
        fields.add(new Field("test_string2", "string"));
        fields.add(new Field("test_integer2", "integer"));
        fields.add(new Field("test_date2", "date"));
        userSchemaStructure.setFields(fields);
        UserSchema userSchema2 = chino.userSchemas.create("user_schema_description2", userSchemaStructure);
        USER_SCHEMA_ID2 = userSchema2.getUserSchemaId();
        System.out.println(userSchema2);

        //Third method, passing a description and a Class with the variables we want as Fields
        UserSchema userSchema = chino.userSchemas.create("user_schema_description3", UserSchemaStructureSample.class);
        USER_SCHEMA_ID3 = userSchema.getUserSchemaId();
        System.out.println(userSchema);

        //Check UPDATE Schema first method
        userSchemaRequest = new UserSchemaRequest();
        userSchemaRequest.setDescription("schema_description1_updated");
        userSchemaStructure = new UserSchemaStructure();
        fields = new ArrayList<Field>();
        fields.add(new Field("test_string1", "string"));
        fields.add(new Field("test_integer1", "integer"));
        fields.add(new Field("test_date1", "date"));
        fields.add(new Field("test_boolean", "boolean"));
        userSchemaStructure.setFields(fields);
        userSchemaRequest.setStructure(userSchemaStructure);
        System.out.println(chino.userSchemas.update(USER_SCHEMA_ID1, userSchemaRequest));

        //Check UPDATE Schema second method
        userSchemaStructure = new UserSchemaStructure();
        fields = new ArrayList<Field>();
        fields.add(new Field("test_string2", "string"));
        fields.add(new Field("test_integer2", "integer"));
        fields.add(new Field("test_date2", "date"));
        fields.add(new Field("test_boolean", "boolean"));
        userSchemaStructure.setFields(fields);
        System.out.println(chino.userSchemas.update(USER_SCHEMA_ID2, "user_schema_description2_updated", userSchemaStructure));

        //Check UPDATE UserSchema third method
        System.out.println(chino.userSchemas.update(USER_SCHEMA_ID3, "user_schema_description3_updated", UserSchemaStructureSampleUpdated.class));

        //Check LIST UserSchemas
        System.out.println(chino.userSchemas.list());

        //Check DELETE UserSchemas
        System.out.println(chino.userSchemas.delete(USER_SCHEMA_ID1, true));
        System.out.println(chino.userSchemas.delete(USER_SCHEMA_ID2, true));
        System.out.println(chino.userSchemas.delete(USER_SCHEMA_ID3, true));
    }
}
