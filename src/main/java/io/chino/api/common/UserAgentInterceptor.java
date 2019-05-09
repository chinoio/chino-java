package io.chino.api.common;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Set the User-Agent header of the SDK.
 */
public class UserAgentInterceptor implements Interceptor {

    private final String clientName;

    /**
     * Set the User-Agent header of the SDK.
     */
    public UserAgentInterceptor() {
        this.clientName = null;
    }

    /**
     * Set the User-Agent header of the SDK.
     * @param clientName a String describing the client, which will be appended
     *                   to the default User-Agent header
     */
    public UserAgentInterceptor(String clientName) {
        this.clientName = clientName;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .header("User-Agent", getUserAgent())
                .build();

        return chain.proceed(request);
    }

    private String getUserAgent() {
        String base = ChinoApiConstants.USER_AGENT;
        if (clientName == null) return base;
        return base + String.format("(%s)", clientName);
    }
}
