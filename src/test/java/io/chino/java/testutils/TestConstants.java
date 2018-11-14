package io.chino.java.testutils;

import io.chino.api.user.User;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Holds constants for testing with JUnit.
 *
 * Remember to add to your environment two variables named
 * "customer_id" and "customer_key" with your Chino.io
 * customer data.
 * FOR NO REASON YOU SHOULD WRITE THEM IN THE CODE.
 *
 * @see #init(String, String)
 */
public class TestConstants {

    /**
     * The base URL of the Chino.io test API
     */
    public static String HOST = "https://api.test.chino.io/v1";

    /**
     * Your customer ID - DO NOT WRITE IT HERE IN THE CODE.
     * @see TestConstants
     */
    public static String CUSTOMER_ID = null;
    
    /**
     * Your customer key - DO NOT WRITE IT HERE IN THE CODE.
     * @see TestConstants
     */
    public static String CUSTOMER_KEY = null;
    
    /**
     * Sample username for User log-in - if null, this value is set automatically.
     * with {@link #init(String, String)}.
     *
     */
    public static String USERNAME= null;

    /**
     * Sample password for User log-in - if null, this value is set automatically
     * with {@link #init(String, String)}.
     */
    public static String PASSWORD= null;

    /**
     * When {@code true}, allows the test suite to force deletion of every object
     * in the customer account.
     *
     * If you want to avoid this, you should use a separate account to perform tests.
     */
    public static boolean FORCE_DELETE_ALL_ON_TESTS = false;
    static Properties testProperties = null;

    /**
     * Initializes values in {@link TestConstants} with the customer information,
     * which are loaded from system environment variables. Then sets default
     * username and password values for the {@link User Users} which will be created
     * automatically during tests or examples. If you want to set custom values,
     * use {@link #init(String, String) init(String, String)}
     */
    public static void init() {
        init(null, null);
    }
    
    /**
     * Initializes values in {@link TestConstants} with the customer information and
     * username/password values for the {@link User Users} which will be created
     * automatically during tests and examples.
     *
     * Before running this method you have to set your "customer_id" and "customer_key"
     * environment variables, as explained {@link TestConstants here}.
     *
     * @param defaultUserUsername the default username for test/example Users.
     * If {@code null}, the value will be set to a default String.
     * @param defaultUserPassword the default password for test/example Users.
     * If {@code null}, the value will be set to a default String.
     */
    public static void init(String defaultUserUsername, String defaultUserPassword) {
        try {
            // attempt to load Properties file
            if (testProperties == null) {
                testProperties = new Properties();
                testProperties.load(
                        new FileReader("src/test/res/test.properties")
                );
            }
        } catch (IOException e) {
            System.err.println("Failed to load 'src/test/res/test.properties'. Reason:");
            e.printStackTrace(System.err);
            System.err.flush();
            System.exit(1);
        }
        // attempt to load values from Properties
        CUSTOMER_ID = testProperties.getProperty("chino.test.customer_id", null);
        CUSTOMER_KEY = testProperties.getProperty("chino.test.customer_key", null);
        // load missing values from env variables
        if (CUSTOMER_ID == null || CUSTOMER_ID.isEmpty()) {
            CUSTOMER_ID = System.getenv("customer_id");
        }
        if (CUSTOMER_KEY == null || CUSTOMER_KEY.isEmpty()) {
            CUSTOMER_KEY = System.getenv("customer_key");
        }
        // error - no variables set
        if (CUSTOMER_ID == null || CUSTOMER_KEY == null) {
            System.err.println("To test the SDK, you need to obtain your Chino.io customer id and customer key.\n"
                    + "Once you have the required credentials, write them in 'src/test/res/test.properties' as CUSTOMER_ID/CUSTOMER_KEY\n"
                    + "or create the system environment variables 'customer_id'/'customer_key'.\n"
                    + "ChinoAPIExample will read the values from there (in this order) and authenticate the API calls with your credentials.\n");

            System.exit(2);
        }
        
        // sample values; you can edit those two values at will (either here or in class 'TestConstants').
        USERNAME = (defaultUserUsername == null) ? "mrossi" : defaultUserUsername;
        PASSWORD = (defaultUserPassword == null) ? "rossimario57" : defaultUserPassword;
    }

    /* Other constant values used throughout the test classes */
    public final static String APP_NAME = "chino Java SDK test";
}
