package io.chino.api.common;

import org.apache.commons.codec.binary.Base64;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class CheckRequestFilter implements ClientRequestFilter {

    private String authToken;

    @Override
    public void filter(ClientRequestContext requestContext)
            throws IOException {
        requestContext.getStringHeaders().remove("Authorization");
        requestContext.getHeaders().add("Authorization", "Basic "+authToken);
    }

    public void setAuth(String token){
        String tot = "ACCESS_TOKEN:"+token;
        byte[] bytesEncoded = Base64.encodeBase64(tot.getBytes());
        authToken = new String(bytesEncoded);
    }
    public void setAuth(String customerId, String customerKey){
        String tot = customerId+":"+customerKey;
        byte[] bytesEncoded = Base64.encodeBase64(tot.getBytes());
        authToken = new String(bytesEncoded);
    }
}
