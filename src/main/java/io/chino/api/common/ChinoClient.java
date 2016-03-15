package io.chino.api.common;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class ChinoClient{

    private Client client;
    private CheckRequestFilter filter = new CheckRequestFilter();

    public ChinoClient(){
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        TrustManager[] trustAllCerts = {new InsecureTrustManager()};
        try {
            if (sc != null) {
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
            }
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        HostnameVerifier allHostsValid = new InsecureHostnameVerifier();

        client = ClientBuilder.newBuilder().sslContext(sc).hostnameVerifier(allHostsValid).build();
        client.register(filter);
    }

    public Client getClient(){
        return client;
    }

    public CheckRequestFilter getFilter(){
        return filter;
    }

    public void setAuth(String customerId, String customerKey){
        filter.setAuth(customerId, customerKey);
    }

    public void setAuth(String accessToken){
        filter.setAuth(accessToken);
    }
}
