package io.chino.java;

import io.chino.api.common.LoggingInterceptor;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

public class ChinoAPI {
    OkHttpClient client;
    public Applications applications;
    public Auth auth;
    public UserSchemas userSchemas;
    public Schemas schemas;
    public Documents documents;
    public Repositories repositories;
    public Groups groups;
    public Collections collections;
    public Users users;
    public Search search;
    public Permissions permissions;
    public Blobs blobs;
    public Consents consents;

    /**
     * Construct an API client which authenticates calls with a {@code (customerID, customerKey)} pair.
     * To be used <b>only in secure clients</b>.
     *
     * @param hostUrl the base URL for the API calls. (will be forced to use 'https://')
     * @param customerId the customer id provided by Chino.io
     * @param customerKey the customer key provided by Chino.io
     */
    public ChinoAPI(String hostUrl, String customerId, String customerKey){
        checkNotNull(hostUrl, "host_url");
        checkNotNull(customerId, "customer_id");
        checkNotNull(customerKey, "customer_key");
        client = getDefaultHttpClient()
                .addNetworkInterceptor(new LoggingInterceptor(customerId, customerKey))
                .build();
        initObjects(hostUrl.replace("http://", "https://"));
    }

    /**
     * Construct an unauthenticated API client.
     * Mainly used for client-side applications.
     * Users will need to authenticate via {@link Auth ChinoAPI.auth} using username and password or an authentication code
     *
     * @param hostUrl the base URL for the API calls
     */
    public ChinoAPI(String hostUrl) {
        checkNotNull(hostUrl, "host_url");
        client = getDefaultHttpClient()
                .addNetworkInterceptor(new LoggingInterceptor())
                .build();
        initObjects(hostUrl);
    }
    
    /**
     * Construct an API client which authenticates calls with a bearer token.
     * Mainly used for server-side applications.
     * @param hostUrl the base URL for the API calls
     * @param bearerToken the bearer token to use for further calls
     */
    public ChinoAPI(String hostUrl, String bearerToken) {
        checkNotNull(hostUrl, "host_url");
        client = getDefaultHttpClient()
                .addNetworkInterceptor(new LoggingInterceptor(bearerToken))
                .build();
        initObjects(hostUrl);
    }
    
    private void initObjects(String hostUrl){
        applications = new Applications(hostUrl, this);
        userSchemas = new UserSchemas(hostUrl, this);
        documents = new Documents(hostUrl, this);
        schemas = new Schemas(hostUrl, this);
        repositories = new Repositories(hostUrl, this);
        groups = new Groups(hostUrl, this);
        collections = new Collections(hostUrl, this);
        users = new Users(hostUrl, this);
        search = new Search(hostUrl, this);
        auth = new Auth(hostUrl, this);
        permissions = new Permissions(hostUrl, this);
        blobs = new Blobs(hostUrl, this);
        consents = new Consents(hostUrl, this);
    }

    private void checkNotNull(Object object, String name){
        if(object == null){
            throw new NullPointerException(name);
        }
    }

    /**
     * Get the default HTTP client
     * @return
     */
    static OkHttpClient.Builder getDefaultHttpClient() {
        return new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS);
    }

    OkHttpClient getHttpClient() {
        return client;
    }

    void updateHttpAuth(LoggingInterceptor authInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS);

        if (authInterceptor == null) {
            this.client = builder.build();
        } else {
            this.client = builder.addNetworkInterceptor(authInterceptor).build();
        }
    }

    /**
     * Change this ChinoAPI authentication method to Bearer token
     * @param token the new bearer token that will be used to authenticate API calls to Chino.io API
     *
     * @return this {@link ChinoAPI} client with the new authentication method
     */
    public ChinoAPI setBearerToken(String token) {
        checkNotNull(token, "token");
        updateHttpAuth(new LoggingInterceptor(token));
        return this;
    }

    /**
     * Change this ChinoAPI authentication method to Basic Auth,
     * using the customer credentials. <br>
     * <br>
     * <b>WARNING: don't use this in public {@link Applications}</b>
     *
     * @param customerId the Chino.io customer ID
     * @param customerKey the Chino.io customer key
     *
     * @return this {@link ChinoAPI} client with the new authentication method
     */
    public ChinoAPI setCustomer(String customerId, String customerKey) {
        checkNotNull(customerId, "customer id");
        checkNotNull(customerKey, "customer key");
        updateHttpAuth(new LoggingInterceptor(customerId, customerKey));
        return this;
    }
}
