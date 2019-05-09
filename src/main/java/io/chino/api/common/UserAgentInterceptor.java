package io.chino.api.common;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Set the User-Agent header of the SDK.
 */
public class UserAgentInterceptor implements Interceptor {

    private String clientName;

    /**
     * Set the User-Agent header of the SDK.
     */
    public UserAgentInterceptor() {
        this.clientName = null;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .header("User-Agent", getUserAgent())
                .build();

        return chain.proceed(request);
    }

    public String getUserAgent() {
        String base = ChinoApiConstants.USER_AGENT;
        if (clientName == null) return base;
        return base + String.format("(%s)", clientName);
    }

    /**
     * Get the current client name.
     *
     * @return the client name, or an empty String if the current name is {@code null}
     */
    public String getClientName() {
        if (clientName == null)
            return "";
        return clientName;
    }

    /**
     * Update the client name in this {@link UserAgentInterceptor}.
     *
     * @param name the new client name. If null, the client name value will be removed from the "User-Agent" header.
     */
    public void updateClientName(String name) {
        this.clientName = name;
    }
}
