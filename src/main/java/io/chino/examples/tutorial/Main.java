package io.chino.examples.tutorial;

import io.chino.java.ChinoAPI;

public class Main {

    private static final String CUSTOMER_ID = "<YOUR_CUSTOMER_ID>";
    private static final String CUSTOMER_KEY = "<YOUR_CUSTOMER_KEY>";
    private static final String HOST_URL = "<SERVER_HOST_URL>";

    public static void main(String args[]){
        //First you need to create a ChinoApi variable
        ChinoAPI chino = new ChinoAPI(HOST_URL, CUSTOMER_ID, CUSTOMER_KEY);
    }
}
