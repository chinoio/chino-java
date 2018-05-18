package io.chino.api.common;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public final class LoggingInterceptor implements Interceptor {

    private String credentials;
    private Boolean authenticate;


    public LoggingInterceptor(){
        authenticate = false;
    }

    public LoggingInterceptor(String customerId, String customerKey){
        authenticate = true;
        credentials = Credentials.basic(customerId, customerKey);
    }

    public LoggingInterceptor(String token){
        authenticate = true;
        credentials = "Bearer ".concat(token);
    }

    @Override public Response intercept(Interceptor.Chain chain) throws IOException {
        if (chain != null) {
            Request request = chain.request();
            Request newRequest;
            if (authenticate) {
                newRequest = request.newBuilder()
                        .addHeader("Authorization", credentials)
                        .build();
            } else {
                newRequest = request.newBuilder().build();
            }
            return chain.proceed(newRequest);
        }
        return null;
    }

    public String getAuthorization() {
        return credentials;
    }

    /**
     *  Removes any authentication method from this Interceptor
     */
    public void noCredentials(){
        authenticate = false;
    }

}
