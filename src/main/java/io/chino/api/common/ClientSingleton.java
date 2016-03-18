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

    public static Client getClient(){
        return client;
    }

}
