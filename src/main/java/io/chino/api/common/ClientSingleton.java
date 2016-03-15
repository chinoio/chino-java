package io.chino.api.common;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class ClientSingleton {
    static Client client;
    static boolean isCustomer;
    static HttpAuthenticationFeature auth;
    static CheckRequestFilter filter = new CheckRequestFilter();
    private static ClientSingleton instance = null;
    protected ClientSingleton() {
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        TrustManager[] trustAllCerts = {new InsecureTrustManager()};
        try {
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HostnameVerifier allHostsValid = new InsecureHostnameVerifier();

        client = ClientBuilder.newBuilder().sslContext(sc).hostnameVerifier(allHostsValid).build();
        client.register(filter);
    }
    public static ClientSingleton getInstance() {
        if(instance == null) {
            instance = new ClientSingleton();
        }
        return instance;
    }

    public static void setClient(String customerId, String customerKey){
        isCustomer=true;
        /*boolean register = false;
        if(auth==null)
            register=true;
        auth=HttpAuthenticationFeature.basic(customerId, customerKey);
        if (register)
            client.register(auth);
        */
        //filter.setCustomerId(customerId);
        //filter.setCustomerKey(customerKey);
    }

    public static void setClient(String accessToken){
        isCustomer=false;
        /*boolean register = false;
        if(auth==null)
            register=true;
        auth=HttpAuthenticationFeature.basic("ACCESS_TOKEN", accessToken);
        instance = new ClientSingleton();
        client.register(auth);
        if (register)
            client.register(auth);
        */

        //filter.setToken(accessToken);
    }
    public static Client getClient(){
        return client;
    }

}
