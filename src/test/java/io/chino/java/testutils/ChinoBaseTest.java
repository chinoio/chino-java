package io.chino.java.testutils;

import io.chino.api.common.ChinoApiException;
import io.chino.java.ChinoAPITest;
import io.chino.java.ChinoBaseAPI;
import okhttp3.OkHttpClient;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Base test class for all tests.
 *
 */
public class ChinoBaseTest {

    private static String USERNAME = "testusrname";
    private static String PASSWORD = "testpword32";

    private static ChinoBaseAPI test = null;

    private static boolean continueTests = true;
    /**
     * This flag is set to false after the first run,
     * it is used to print out the test heading with the Java version
     * in such a way that it is only displayed once.
     */
    private static boolean firstRun = true;
    /**
     * This flag can be set to true by subclasses
     * that handle the deletion of test objects.
     */
    private static boolean skipDelete = false;
    private static String errorMsg = "init() method not called";

    /**
     * The name of the current test class, always a subclass of {@link ChinoBaseTest}.
     * The value is set inside {@link #init(ChinoBaseAPI)}.
     */
    private static String className = null;

    /**
     * Set the current class name in {@link ChinoBaseTest}.
     * The value will be reset to {@code null} by method {@link #afterClass()}
     *
     * @param testClass a subclass of {@link ChinoBaseTest}
     */
    protected static void runClass(Class<? extends ChinoBaseTest> testClass) {
        className = testClass.getSimpleName();
    }

    /**
     * Use this method to run the tests in {@link ChinoAPITest}.
     */
    public static void runChinoApiTest() throws IOException, ChinoApiException {
        className = ChinoAPITest.class.getSimpleName();
        ChinoBaseTest.beforeClass();
        // override username and password used by this test
        TestConstants.init(TestConstants.USERNAME, TestConstants.PASSWORD);
    }


    /**
     * Init this base test class.
     *
     * @param testedAPIClient the {@link ChinoBaseAPI} that will be used to perform the tests.
     * @return the API client that has been set for this instance
     */
    public static <APIClient extends ChinoBaseAPI, TestClass extends Class<? extends ChinoBaseTest>>
        APIClient init(APIClient testedAPIClient)
    {

        errorMsg = "no errors";
        test = testedAPIClient;

        return (APIClient) test;
    }

    @BeforeClass
    public static void beforeClass() throws IOException, ChinoApiException {
        TestConstants.init(USERNAME, PASSWORD);
        String automatedTests = null;
        if (TestConstants.testProperties != null) {
            automatedTests = TestConstants.testProperties.getProperty("chino.test.automated", null);
        }
        if (automatedTests == null || automatedTests.isEmpty()) {
            automatedTests = System.getenv("automated_test");
        }
        if (Objects.equals(automatedTests, "allow")) // null-safe 'equals()'
            TestConstants.FORCE_DELETE_ALL_ON_TESTS = true;
        // if specified, update host
        String host = TestConstants.testProperties.getProperty("chino.test.host", null);
        if (host == null || host.isEmpty()) {
            host = System.getenv("host");
        }
        if (host != null) {
            TestConstants.HOST = host;
        }
        System.out.println();
        if (firstRun) {
            String isProduction = TestConstants.PRODUCTION_ENV ? " [PRODUCTION ENVIRONMENT]" : "";
            String automatedTestStatus = TestConstants.FORCE_DELETE_ALL_ON_TESTS
                    ? "ALLOWED - the account will be cleaned up."
                    : "NOT ALLOWED - In order to run the test, set automated_test=allow in the environment.";
            System.out.println(
                    hr("CHINO.IO JAVA SDK TEST") + "\n" +
                    " ~ Java version       : " + TestConstants.JAVA + "\n" +
                    " ~ Chino.io host      : " + TestConstants.HOST + isProduction + "\n" +
                    " ~ SDK version        : " + TestConstants.SDK_VERSION + "\n" +
                    " ~ Delete all objects : " + automatedTestStatus + "\n" +
                    hr());
            firstRun = false;
        } else {
            System.out.flush();
            System.out.println(hr(className));
            System.out.println();
        }

        System.out.flush();
    }

    @Before
    public void before() {
        if (errorMsg.equals("init() method not called"))
            continueTests = false;

        if (!continueTests) {
            System.err.println(errorMsg);
            System.exit(1);
        }
    }

    @After
    public void after() throws IOException, ChinoApiException {
        try {
            synchronized (this) {wait(3000);}
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void afterClass() throws IOException, ChinoApiException {
        // reset static flags
        errorMsg =  "init() method not called";
        continueTests = true;
        className = null;
        // delete test objects
        try {
            if (! skipDelete) {
                System.out.println();
                System.out.print("Cleaning up test account... ");
                new DeleteAll().deleteAll(test);
            } else {
                // skip delete operation, but reset static field for the next class.
                System.out.print("Terminating test... ");
                skipDelete = false;
            }
        } finally {
            test = null;
        }
        System.gc();
        System.out.println("Done.");
        System.out.println(hr());
        System.out.println();
    }


    /**
     * Handles test interruption when there are instances of a Chino.io resource
     * on the customer's Chino.io API account. If {@link TestConstants#FORCE_DELETE_ALL_ON_TESTS} is
     * set to {@code true}, the test won't be interrupted and all the objects on the Chino.io account
     * will be deleted.<br>
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
    protected static void checkResourceIsEmpty(boolean resourceIsEmpty, ChinoBaseAPI resourceAPIClient)
            throws IOException, ChinoApiException
    {
        String resourceName = resourceAPIClient.getClass().getSimpleName();

        Logger.getLogger(OkHttpClient.class.getName()).setLevel(Level.FINE);

        if (! resourceIsEmpty) {
            if (! TestConstants.FORCE_DELETE_ALL_ON_TESTS) {
                System.err.println("WARNING: this account has " + resourceName + " stored. " +
                        "If you run the tests they will be deleted.");
            System.err.println("To hide this message, set 'automated_test' in your environment variables " +
                    "and re-run the tests.");
            } else {
                new DeleteAll().deleteAll(resourceAPIClient);
                continueTests = true;
                System.out.flush();
                return;
            }
        }

        continueTests = resourceIsEmpty;

        if (!continueTests) {
            errorMsg = "Unable to delete " + resourceName + ". Test session will be" +
                    " stopped.";
        } else {
            errorMsg = "no errors";
        }
    }

    protected static void success(String testName) {
        System.out.println(testName + " test OK");
    }

    /**
     * Call this method <b>before</b> calling {@link #afterClass() ChinoBaseTest.afterClass()}.<br>
     * When this method is called, {@link #afterClass()} will behave as if the subclass
     * already deleted the objects used for the test.
     */
    public static void skipDelete() {
        ChinoBaseTest.skipDelete = true;
    }

    /**
     * Used in print statements to generate a separator line, with max length of
     * {@link TestConstants#OUTPUT_MAX_LENGTH OUTPUT_MAX_LENGTH} and an optional title.
     *
     * @param text an optional title for the separator
     *
     * @return a String (without '\n')
     */
    public static String hr(String text) {
        String title = "";
        if (text != null && !text.isEmpty()) {
            title = String.format(" %s ", text);
        }
        StringBuilder separator = new StringBuilder("---" + title);
        while (separator.length() < TestConstants.OUTPUT_MAX_LENGTH) {
            separator.append("-");
        }
        return separator.toString();
    }

    /**
     * Used in print statements to generate a separator line,
     * with length of {@link TestConstants#OUTPUT_MAX_LENGTH OUTPUT_MAX_LENGTH}.
     *
     * @return a String (without '\n')
     */
    public static String hr() {
        return hr(null);
    }
}
