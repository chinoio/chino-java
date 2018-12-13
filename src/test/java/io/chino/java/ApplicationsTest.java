package io.chino.java;

import io.chino.api.application.Application;
import io.chino.api.application.ClientType;
import io.chino.api.common.ChinoApiException;
import io.chino.java.testutils.ChinoBaseTest;
import io.chino.java.testutils.TestConstants;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class ApplicationsTest extends ChinoBaseTest {

    private static Applications test;

    private static LinkedList<Application> localApps;

    @BeforeClass
    public static void setUpClass() throws Exception {
        ChinoBaseTest.beforeClass();
        ChinoAPI chino_customer = new ChinoAPI(TestConstants.HOST, TestConstants.CUSTOMER_ID, TestConstants.CUSTOMER_KEY);
        test = ChinoBaseTest.init(chino_customer.applications);
        ChinoBaseTest.checkResourceIsEmpty(test.list().getApplications().isEmpty(), test);

        localApps = new LinkedList<>();
    }

    @Test
    public void testCRUD_List0args() throws IOException, ChinoApiException {
        // create
        Application app = test.create(TestConstants.APP_NAME + " - authcode", "password", "");
        assertNotNull("Create - 1st app null", app);
        localApps.add(app);

        app = test.create(TestConstants.APP_NAME + " - password, public", "password", "", ClientType.PUBLIC);
        assertNotNull("Create - 2nd app null", app);
        localApps.add(app);

        app = test.create(TestConstants.APP_NAME + " - password, confidential", "password", "", ClientType.CONFIDENTIAL);
        assertNotNull( "Create - 3rd app null", app);
        localApps.add(app);

        // list (0 args)
        LinkedList<Application> fetchedApps = new LinkedList<>(test.list().getApplications());
        assertEquals(localApps.size(), fetchedApps.size());

        // update
        String appId = app.getAppId();
        Application updatedApp = test.update(app.getAppId(), TestConstants.APP_NAME + " - updated", "password", "");
        assertNotNull(updatedApp);
        assertEquals(updatedApp.getAppId(), appId);
        assertNotEquals(updatedApp.getAppName(), app.getAppName());

        // read
        Application readApp = test.read(appId);
        assertEquals(readApp.getAppId(), appId);
        assertEquals(readApp.getAppName(), updatedApp.getAppName());
        assertEquals(readApp.getGrantType(), updatedApp.getGrantType());
        assertEquals(readApp.getRedirectUrl(), updatedApp.getRedirectUrl());

        // delete
        if (test.delete(readApp.getAppId(), true).equals("success")) {
            localApps.remove(readApp);
        }

        try {
            test.read(readApp.getAppId());
            fail("Application " + readApp.getAppId() + " should have been deleted");
        } catch (ChinoApiException e) {
            success("Applications CRUDL");
        }
    }

    @Test
    public void testList2Args() throws IOException, ChinoApiException {
        int limit = 1;

        Application app = test.create(TestConstants.APP_NAME + " - list navigation (1/2)", "password", "");
        localApps.add(app);
        app = test.create(TestConstants.APP_NAME + " - list navigation (2/2)", "password", "");
        localApps.add(app);

        LinkedList<Application> fetchedApps = new LinkedList<>(test.list(0,limit).getApplications());

        assertNotNull(fetchedApps);
        assertEquals(fetchedApps.size(), limit);

        success("Applications list(int, int)");
    }
}