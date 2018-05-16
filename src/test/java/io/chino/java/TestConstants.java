package io.chino.java;

import io.chino.api.user.User;

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
    public final static String HOST = "https://api.test.chino.io/v1";
    
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
     * Initializes values in {@link TestConstants} with the customer informations,
     * which are loaded from system environment variables. Then sets default
     * username and password values for the {@link User Users} which will be created
     * automatically during tests or examples. If you want to set custom values,
     * use {@link #init(String, String) init(String, String)}
     */
    public static void init() {
        init(null, null);
    }
    
    /**
     * Initializes values in {@link TestConstants} with the customer informations and
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
        CUSTOMER_ID = System.getenv("customer_id");
        CUSTOMER_KEY = System.getenv("customer_key");
        if (CUSTOMER_ID == null || CUSTOMER_KEY == null) {
            System.err.println("To use this class, you need to obtain your Chino.io customer id and customer key.\n"
                    + "Once you have the required credentials, you need to create two system environment variables: 'customer_id' and 'customer_key'.\n"
                    + "ChinoAPIExample will read the values from there and authenticate the API calls with your credentials.\n");
        }
        
        // sample values; you can edit those two values at will (either here or in class 'TestConstants').
        USERNAME = (defaultUserUsername == null) ? "mrossi" : defaultUserUsername;
        PASSWORD = (defaultUserPassword == null) ? "rossimario57" : defaultUserPassword;
    }
}
