package io.chino.java.testutils;

import io.chino.api.common.ChinoApiException;
import io.chino.java.ChinoBaseAPI;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * Base test class for all tests.
 *
 */
public class ChinoBaseTest {

    public static final String URL = TestConstants.HOST;
    public static String USERNAME = "testusrname";
    public static String PASSWORD = "testpword32";

    private static ChinoBaseAPI test = null;

    protected static boolean continueTests = true;
    static String errorMsg = "init() method not called";


    /**
     * Init this base test class.
     *
     * @param testedAPIClient the {@link ChinoBaseAPI} that will be used to perform the tests.
     * @return the API client that has been set for this instance
     */
    public static <APIClient extends ChinoBaseAPI> APIClient init(APIClient testedAPIClient) {

        errorMsg = "no errors";
        test = testedAPIClient;

        return (APIClient) test;
    }

    @BeforeClass
    public static void beforeClass() throws IOException, ChinoApiException {
        TestConstants.init(USERNAME, PASSWORD);
        if (Objects.equals(System.getenv("automated_test"), "allow")) // null-safe 'equals()'
            TestConstants.FORCE_DELETE_ALL_ON_TESTS = true;
        if (System.getenv("host") != null) {
            TestConstants.HOST = System.getenv("host");
        }
        System.out.println("USING CHINO.IO HOST: " + TestConstants.HOST);
    }

    @Before
    public void before() {
        if (errorMsg.equals("init() method not called"))
            continueTests = false;

        if (!continueTests) {
            System.err.println(errorMsg);
            throw new RuntimeException(errorMsg);
        }
    }

    @After
    public void after() throws IOException, ChinoApiException {}

    @AfterClass
    public static void afterClass() throws IOException, ChinoApiException {
        new DeleteAll().deleteAll(test);
        errorMsg =  "init() method not called";
        continueTests = true;
        test = null;
        System.gc();
    }


    /**
     * Handles test interruption when there are instances of a Chino.io resource
     * on the customer's Chino.io API account. If {@link TestConstants#FORCE_DELETE_ALL_ON_TESTS} is
     * set to {@code true}, the test won't be interrupted and all the objects on the Chino.io account will be deleted.<br>
     * <br>
     * Example:<br><br>
     * <code>
     *     ChinoAPI chinoApi = new ChinoAPI(HOST, CUSTOMER_ID, CUSTOMER_KEY);<br>
     *     checkResourceIsEmpty(chinoApi.userSchemas.list().getUserSchemas.isEmpty(), chinoApi.userSchemas);
     * </code>
     *
     * @param resourceIsEmpty the result of {@link List#isEmpty()} or another value, which should be {@code true}
     *                        only if there are no resources of the desired type stored on Chino.io.
     * @param resourceAPIClient the API client that will be eventually used to delete all the objects of that kind if
     * {@link TestConstants#FORCE_DELETE_ALL_ON_TESTS} is set to {@code true}.
     */
    protected static void checkResourceIsEmpty(boolean resourceIsEmpty, ChinoBaseAPI resourceAPIClient) throws IOException, ChinoApiException {
        String resourceName = resourceAPIClient.getClass().getSimpleName();

        if (! resourceIsEmpty) {
            if (! TestConstants.FORCE_DELETE_ALL_ON_TESTS) {
            Scanner scanner = new Scanner(System.in);
            System.err.println("WARNING: this account has " + resourceName + " stored. If you run the tests they will be deleted.");
            System.err.println("To hide this message, set the constant TestConstants.FORCE_DELETE_ALL_ON_TESTS to 'true' and re-run the tests.");
            } else {
                System.out.println();
                System.out.println("TestConstants.FORCE_DELETE_ALL_ON_TESTS = true");
                System.out.println("Every object will be deleted.");
                new DeleteAll().deleteAll(resourceAPIClient);
                continueTests = true;
                return;
            }
        }

        continueTests = resourceIsEmpty;

        if (!continueTests) {
            errorMsg = "If you don't want to delete all your " + resourceName + ", consider using another account for testing.";
        } else {
            errorMsg = "no errors";
        }
    }

    protected static void success(String testName) {
        System.out.println(testName + " test OK");
    }
}
