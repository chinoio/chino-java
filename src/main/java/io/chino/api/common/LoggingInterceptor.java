package io.chino.api.common;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class LoggingInterceptor implements Interceptor {

    private String credentials;
    private Boolean authenticate;

    @Override public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest;
        if(authenticate){
            newRequest = request.newBuilder()
                    .addHeader("Authorization", credentials)
                    .build();
        } else {
            newRequest = request.newBuilder().build();
        }

        return chain.proceed(newRequest);
    }

    public void setCustomer(String customerId, String customerKey){
        authenticate = true;
        credentials = Credentials.basic(customerId, customerKey);
    }

    public void setUser(String token){
        authenticate = true;
        credentials = "Bearer ".concat(token);
    }

    public void logout(){
        authenticate = false;
    }

}
