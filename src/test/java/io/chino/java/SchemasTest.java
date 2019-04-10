package io.chino.java;

import io.chino.api.common.ChinoApiException;
import io.chino.api.common.Field;
import io.chino.api.schema.Schema;
import io.chino.api.schema.SchemaRequest;
import io.chino.api.schema.SchemaStructure;
import io.chino.java.testutils.ChinoBaseTest;
import io.chino.java.testutils.TestClassStructure;
import io.chino.java.testutils.TestConstants;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class SchemasTest extends ChinoBaseTest {

    private static ChinoAPI chino_admin;
    private static Schemas test;

    private static String REPO_ID;

    @BeforeClass
    public static void beforeClass() throws IOException, ChinoApiException {
        ChinoBaseTest.beforeClass();
        chino_admin = new ChinoAPI(TestConstants.HOST, TestConstants.CUSTOMER_ID, TestConstants.CUSTOMER_KEY);
        test = ChinoBaseTest.init(chino_admin.schemas);

        ChinoBaseTest.checkResourceIsEmpty(
                chino_admin.repositories.list().getRepositories().isEmpty(),
                chino_admin.repositories
        ); // cleans repositories too

        REPO_ID = chino_admin.repositories.create("SchemasTest")
                .getRepositoryId();
    }

    @Test
    public void test_CRUD() throws IOException, ChinoApiException {
        /* CREATE */
        String desc = "Schema created for SchemaTest in Java SDK";
        List<Field> fields = new LinkedList<>();
        fields.add(new Field ("test_update", "string"));
        SchemaStructure struc = new SchemaStructure(fields);
        SchemaRequest req = new SchemaRequest("SchemaTest", struc);

        Schema c_class = test.create(REPO_ID, desc, TestClassStructure.class);
        Schema c_struct = test.create(REPO_ID, desc, struc);
        Schema c_req = test.create(REPO_ID, req);

        /* READ */
        Schema r = test.read(c_class.getSchemaId());
        assertEquals("schemas.read returned an unexpected object: " + r.toString(), c_class, r);

        /* UPDATE */
        String newDesc = "Updated Schema";

        List<Field> newFields = new LinkedList<>();
        newFields.add(new Field ("test_update", "string"));
        struc = new SchemaStructure(newFields);
        req = new SchemaRequest("SchemaTest", struc);

        // test Schema update with the same ID. Empty Schemas can be updated and fields can be removed.
        Schema u_class = test.update(c_class.getSchemaId(), newDesc, TestClassStructure.class);
        Schema u_struct = test.update(c_class.getSchemaId(), newDesc, struc);
        Schema u_req = test.update(c_class.getSchemaId(), req);

        assertNotEquals(c_class, u_class);
        assertNotEquals(c_struct, u_struct);
        assertNotEquals(c_req, u_req);

        /* DELETE */
        test.delete(c_class.getSchemaId(), true);
        try {
            test.read(c_class.getSchemaId());
            fail("Object was not deleted.");
        } catch (ChinoApiException e) {
            System.out.println("Success");
        }

        test.delete(c_req.getSchemaId(), true);
        try {
            test.read(c_req.getSchemaId());
            fail("Object was not deleted.");
        } catch (ChinoApiException e) {
            System.out.println("Success");
        }

        test.delete(c_struct.getSchemaId(), true);
        try {
            test.read(c_struct.getSchemaId());
            fail("Object was not deleted.");
        } catch (ChinoApiException e) {
            System.out.println("Success");
        }
    }


    @Test
    public void test_list() throws IOException, ChinoApiException {
        Schema[] schemas = new Schema[5];
        for (int i=0; i<5; i++) {
                schemas[i] = makeSchema("test_list", i);
        }

        /* LIST (1 arg) */
        List<Schema> list = test.list(REPO_ID).getSchemas();
        int index = 0;
        for (Schema schema : schemas) {
            assertTrue(
                    "schema" + ++index + " wasn't in the list.",
                    list.contains(schema)
            );
        }

        list.clear();
        /* LIST (3 args) */
        int offset = 0;
        int limit = 2;
        assertEquals( "Wrong list size (1)",
                limit,
                test.list(REPO_ID, offset, limit).getSchemas().size()
        );

        offset = schemas.length - 1;
        limit = schemas.length;
        assertEquals( "Wrong list size (2)",
                1,
                test.list(REPO_ID, offset, limit).getSchemas().size()
        );

        offset = 2;
        limit = schemas.length;
        assertEquals( "Wrong list size (3)",
                limit - offset,
                test.list(REPO_ID, offset, limit).getSchemas().size()
        );
    }

    private synchronized Schema makeSchema(String method, int instanceNumber) throws IOException, ChinoApiException {
        String desc = method + "_" + instanceNumber;

        Schema res = test.create(REPO_ID, desc, TestClassStructure.class);
        try {
            wait(3000);
        } catch (InterruptedException ignored) {}
        return res;
    }

}