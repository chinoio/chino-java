package io.chino.java.testutils;

import io.chino.api.common.ChinoApiException;
import io.chino.java.ChinoBaseAPI;
import org.junit.AfterClass;
import org.junit.Before;

import java.io.IOException;
import java.util.Scanner;

public class ChinoBaseTest {

    public static final String URL = TestConstants.HOST;
    public static String USERNAME = "testusrname";
    public static String PASSWORD = "testpword32";

    private static ChinoBaseAPI test = null;

    static boolean continueTests = true;
    static String errorMsg = "init() method not called";

    static {
        TestConstants.init(USERNAME, PASSWORD);
    }


    /**
     * Init this base test class.
     *
     * @param testedAPIClient the {@link ChinoBaseAPI} that will be used to perform the tests.
     * @return the API client that has been set for this instance
     */
    public static ChinoBaseAPI init(ChinoBaseAPI testedAPIClient) {
        errorMsg = "no errors";
        test = testedAPIClient;
        return test;
    }

    @Before
    public void before() {
        if (errorMsg.equals("init() method not called"))
            continueTests = false;

        if (!continueTests) {
            System.err.println(errorMsg);
            System.exit(-1);
        }
    }

    @AfterClass
    public static void afterClass() throws IOException, ChinoApiException {
        new DeleteAll().deleteAll(test);
        errorMsg =  "init() method not called";
        continueTests = true;
    }


    /**
     * Handles test interruption when there are instances of a Chino.io resource
     * on the user account
     *
     * @param resourceIsEmpty
     * @param resourceType
     */
    protected static final void resourceIsEmpty(boolean resourceIsEmpty, String resourceType) {
        if (! resourceIsEmpty) {
            if (! TestConstants.FORCE_DELETE_ALL_ON_TESTS) {
            Scanner scanner = new Scanner(System.in);
            System.err.println("WARNING: this account has" + resourceType + "stored. If you run the tests they will be deleted.");
            System.err.println("To hide this message, set the constant TestConstants.FORCE_DELETE_ALL_ON_TESTS to 'true' and re-run the tests.");
            } else {
                System.out.println("TestConstants.FORCE_DELETE_ALL_ON_TESTS = true");
                System.out.println("Every objenct will be deleted.");
                continueTests = true;
                return;
            }
        }

        continueTests = resourceIsEmpty;

        if (!continueTests) {
            errorMsg = "If you don't want to delete all your " + resourceType + ", consider using another account for testing.";
        } else {
            errorMsg = "no errors";
        }
    }
}
