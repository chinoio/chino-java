package io.chino.java;

import io.chino.api.common.ChinoApiException;
import io.chino.api.common.Field;
import io.chino.api.userschema.UserSchema;
import io.chino.api.userschema.UserSchemaRequest;
import io.chino.api.userschema.UserSchemaStructure;
import io.chino.java.testutils.ChinoBaseTest;
import io.chino.java.testutils.TestClassStructure;
import io.chino.java.testutils.TestConstants;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class UserSchemasTest extends ChinoBaseTest {

    private static ChinoAPI chino_admin;
    private static UserSchemas test;

    @BeforeClass
    public static void beforeClass() throws IOException, ChinoApiException {
        ChinoBaseTest.beforeClass();
        chino_admin = new ChinoAPI(TestConstants.HOST, TestConstants.CUSTOMER_ID, TestConstants.CUSTOMER_KEY);
        test = ChinoBaseTest.init(chino_admin.userSchemas);

        ChinoBaseTest.checkResourceIsEmpty(
                chino_admin.userSchemas.list().getUserSchemas().isEmpty(),
                chino_admin.userSchemas
        );
    }

    @Test
    public void test_CRUD_Class() throws IOException, ChinoApiException {
        /* CREATE */
        UserSchema c = test.create("User Schema created for testing purposes"  + " [" + TestConstants.JAVA + "]",
                TestClassStructure.class
        );

        /* READ */
        UserSchema r = test.read(c.getUserSchemaId());
        assertEquals("userSchemas.read() returned an unexpected object: " + r.toString(), c, r);

        /* UPDATE */
        String newDesc = "Updated description for UserSchema"  + " [" + TestConstants.JAVA + "]";

        UserSchema u = test.update(c.getUserSchemaId(), newDesc, TestClassStructure.class);
        assertEquals("Updated userschema has different ID!", r.getUserSchemaId(), u.getUserSchemaId());
        assertNotEquals(r.getDescription(), u.getDescription());
        assertEquals(r.getStructure(), u.getStructure());

        /* DELETE */
        test.delete(c.getUserSchemaId(), true);
        try {
            test.read(c.getUserSchemaId());
            fail("Object was not deleted.");
        } catch (ChinoApiException e) {
            System.out.println("Success");
        }
    }

    @Test
    public void test_CUD_UserSchemaRequest() throws IOException, ChinoApiException {
        LinkedList<Field> fields = new LinkedList<>();
        fields.add(new Field("testMethod", "string", true));
        UserSchemaStructure struct = new UserSchemaStructure(fields);
        UserSchemaRequest req = new UserSchemaRequest(
                "User Schema created for testing purposes"  + " [" + TestConstants.JAVA + "]",
                struct
        );

        /* CREATE */
        UserSchema c = test.create(req);

        /* UPDATE */
        String newDesc = "Updated description for UserSchema"  + " [" + TestConstants.JAVA + "]";
        req.setDescription(newDesc);

        UserSchema u = test.update(c.getUserSchemaId(), req);
        assertEquals("Updated userschema has different ID!", c.getUserSchemaId(), u.getUserSchemaId());
        assertNotEquals(c.getDescription(), u.getDescription());
        assertEquals(c.getStructure(), u.getStructure());

        /* DELETE */
        test.delete(c.getUserSchemaId(), true);
        try {
            test.read(c.getUserSchemaId());
            fail("Object was not deleted.");
        } catch (ChinoApiException e) {
            System.out.println("Success");
        }
    }

    @Test
    public void test_CUD_UserSchemaStructure() throws IOException, ChinoApiException {
        LinkedList<Field> fields = new LinkedList<>();
        fields.add(new Field("testMethod", "string", true));

        /* CREATE */
        UserSchema c = test.create(
                "User Schema created for testing purposes"  + " [" + TestConstants.JAVA + "]",
                new UserSchemaStructure(fields)
        );

        /* UPDATE */
        String newDesc = "Updated description for UserSchema"  + " [" + TestConstants.JAVA + "]";

        UserSchema u = test.update(c.getUserSchemaId(), newDesc, new UserSchemaStructure(fields));
        assertEquals("Updated userschema has different ID!", c.getUserSchemaId(), u.getUserSchemaId());
        assertNotEquals(c.getDescription(), u.getDescription());
        assertEquals(c.getStructure(), u.getStructure());

        /* DELETE */
        test.delete(c.getUserSchemaId(), true);
        try {
            test.read(c.getUserSchemaId());
            fail("Object was not deleted.");
        } catch (ChinoApiException e) {
            System.out.println("Success");
        }
    }

    @Test
    public void test_list() throws IOException, ChinoApiException {
        UserSchema[] userSchemas = new UserSchema[5];

        synchronized (this) {
            for (int i=0; i<5; i++) {
                userSchemas[i] = test.create("UserSchema_list_test_" + (i+1)  + " [" + TestConstants.JAVA + "]", TestClassStructure.class);
                try {
                    wait(3000);
                } catch (InterruptedException ignored) {
                }
            }
        }


        /* LIST (0 args) */

        List<UserSchema> fetchedList = test.list().getUserSchemas();
        assertEquals("Couldn't fetch all the UserSchemas!", userSchemas.length, fetchedList.size());
        for (UserSchema us : userSchemas) {
            assertTrue(us.getDescription() + " is not in the fetched list!", fetchedList.contains(us));
        }

        /* LIST (2 args) */

        int limit = 3;
        fetchedList = test.list(0, limit).getUserSchemas();
        assertEquals("Couldn't fetch all the UserSchemas!", limit, fetchedList.size());

        int offset = 4;
        fetchedList = test.list(offset, 10).getUserSchemas();
        assertEquals("Couldn't fetch all the UserSchemas!", userSchemas.length - offset, fetchedList.size());
    }

    @Test
    public void test_activate() throws IOException, ChinoApiException {
        UserSchema us0 = test.create("test_activation0", TestClassStructure.class);
        UserSchema us1 = test.create("test_activation1", TestClassStructure.class);
        int iteration = -1;
        for (UserSchema us : new UserSchema[] {us0, us1}) {
            String id = us.getUserSchemaId();
            // Set is_active = false
            test.delete(id, false);
            assertFalse("Failed to set inactive", test.read(id).getIsActive());
            // Set is_active = true with different methods
            doActivation(++iteration, id, us.getStructure());
            UserSchema control = test.read(id);
            // Verify update
            assertTrue("Failed to activate", control.getIsActive());
            assertNotEquals("Failed to update string attribute after activation",
                    us.getDescription(),
                    control.getDescription()
            );
            assertTrue("Failed to update integer attribute after activation",
                    control.getDescription().endsWith(":" + iteration)
            );

            test.delete(id, true);
        }
    }

    private void doActivation(int methodId, String userSchemaId, UserSchemaStructure struct) throws IOException, ChinoApiException {
        switch (methodId) {
            case 0:
                UserSchemaRequest request = new UserSchemaRequest("test_activation_updated:" + methodId, struct);
                test.update(true, userSchemaId,
                        request
                );
                break;
            case 1:
                test.update(true, userSchemaId,
                        "test_activation_updated:" + methodId,
                        TestClassStructure.class
                );
                break;
            default:
                throw new IllegalArgumentException("Invalid iteration index: " + methodId);
        }
    }
}