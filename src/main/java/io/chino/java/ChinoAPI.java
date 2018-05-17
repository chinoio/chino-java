package io.chino.java;

import io.chino.api.common.LoggingInterceptor;
import okhttp3.*;

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
     * @param hostUrl the base URL for the API calls
     * @param customerId the customer id provided by Chino.io
     * @param customerKey the customer key provided by Chino.io
     */
    public ChinoAPI(String hostUrl, String customerId, String customerKey){
        checkNotNull(hostUrl, "host_url");
        checkNotNull(customerId, "customer_id");
        checkNotNull(customerKey, "customer_key");
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addNetworkInterceptor(new LoggingInterceptor(customerId, customerKey))
                .build();
//        LoggingInterceptor.getInstance().setCustomer(customerId, customerKey);
        initObjects(hostUrl);
    }

    /**
     * Construct an unauthenticated API client.
     * Mainly used for client-side applications.
     * Users will need to authenticate via {@link Auth ChinoAPI.auth} using username and password or an authentication code
     * @param hostUrl the base URL for the API calls
     */
    public ChinoAPI(String hostUrl) {
        checkNotNull(hostUrl, "host_url");
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
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
        client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addNetworkInterceptor(new LoggingInterceptor(bearerToken))
                .build();
        initObjects(hostUrl);
    }
    
    private void initObjects(String hostUrl){
        applications = new Applications(hostUrl, client);
        userSchemas = new UserSchemas(hostUrl, client);
        documents = new Documents(hostUrl, client);
        schemas = new Schemas(hostUrl, client);
        repositories = new Repositories(hostUrl, client);
        groups = new Groups(hostUrl, client);
        collections = new Collections(hostUrl, client);
        users = new Users(hostUrl, client);
        search = new Search(hostUrl, client);
        auth = new Auth(hostUrl, client);
        permissions = new Permissions(hostUrl, client);
        blobs = new Blobs(hostUrl, client);
        consents = new Consents(hostUrl, client);
    }

    private void checkNotNull(Object object, String name){
        if(object == null){
            throw new NullPointerException(name);
        }
    }
}
