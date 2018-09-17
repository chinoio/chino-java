package io.chino.java.testutils;

import io.chino.api.common.ChinoApiException;
import io.chino.api.permission.GetPermissionsResponse;
import io.chino.java.ChinoAPI;
import io.chino.java.Permissions;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;

public class PermissionsTest extends ChinoBaseTest {

    private static ChinoAPI chino_admin;
    private static Permissions test;

    private static String REPO_ID, SCHEMA_ID;

    @BeforeClass
    public static void beforeClass() throws IOException, ChinoApiException {
        ChinoBaseTest.beforeClass();
        chino_admin = new ChinoAPI(TestConstants.HOST, TestConstants.CUSTOMER_ID, TestConstants.CUSTOMER_KEY);
        test = ChinoBaseTest.init(chino_admin.permissions);
    }

        @Test
    public void testPerms() throws IOException, ChinoApiException {
        GetPermissionsResponse asd = test.readPermissions();
        System.out.println(asd.getPermissions());
    }
}
