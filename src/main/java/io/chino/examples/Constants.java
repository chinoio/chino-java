package io.chino.examples;

import io.chino.api.user.User;

public class Constants {

    /**
     * The base URL of the Chino.io test API
     */
    public final static String HOST = "https://api.test.chino.io/v1";
    
    /**
     * Your customer ID
     */
    public static String CUSTOMER_ID = null;
    
    /**
     * Your customer key
     */
    public static String CUSTOMER_KEY = null;
    
    /**
     * Sample username for User log-in
     */
    public static String USERNAME= null;

    /**
     * Sample passwrod for User log-in
     */
    public static String PASSWORD= null;
    
    /**
     * Initializes values in {@link Constants} with the customer informations,
     * which are loaded from system environment variables. Then sets default
     * username and password values for the {@link User Users} which will be created
     * automatically during tests or examples. If you want to set custom values,
     * use {@link #init(java.lang.String, java.lang.String) init(String, String)}
     */
    public static void init() {
        init(null, null);
    }
    
    /**
     * Initializes values in {@link Constants} with the customer informations and
     * username/password values for the {@link User Users} which will be created
     * automatically during tests and examples.
     * @param defaultUserUsername the default username for test/example Users.
     * If {@code null}, the value will be set to a default String.
     * @param defaultUserPassword the default password for test/example Users.
     * If {@code null}, the value will be set to a default String.
     */
    public static void init(String defaultUserUsername, String defaultUserPassword) {
        CUSTOMER_ID = System.getenv("customer_id");
        CUSTOMER_KEY = System.getenv("customer_key");
        if (CUSTOMER_ID == null || CUSTOMER_KEY == null) {
            System.err.println("customer_id or customer_key not found.\n"
                    + "Be sure to get them from Chino.io API console and to add the system environment variables 'customer_id' and 'customer_key' before running the examples.");
        }
        
        // sample values; you can edit those two values at will (either here or in class 'Constants').
        USERNAME = (defaultUserUsername == null) ? "mrossi" : defaultUserUsername;
        PASSWORD = (defaultUserPassword == null) ? "rossimario57" : defaultUserPassword;
    }
}
