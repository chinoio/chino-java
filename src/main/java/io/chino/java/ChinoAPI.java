package io.chino.java;

import io.chino.api.common.LoggingInterceptor;
import io.chino.api.common.UserAgentInterceptor;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Main API client. Initializes and coordinates all the clients based on {@link ChinoBaseAPI}.
 */
public class ChinoAPI {

    /**
     * the version code of Chino.io API used by this SDK
     */
    public final static String API_VERSION = "v1";

    static Interceptor userAgent = new UserAgentInterceptor();

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
    public ChinoAPI(String hostUrl, String customerId, String customerKey) {
        checkNotNull(hostUrl, "host_url");
        checkNotNull(customerId, "customer_id");
        checkNotNull(customerKey, "customer_key");
        client = getDefaultHttpClient()
                .addNetworkInterceptor(new LoggingInterceptor(customerId, customerKey))
                .build();
        initObjects(hostUrl);
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
        hostUrl = normalizeApiUrl(hostUrl);
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
     * Check format of the API host url and append the {@link #API_VERSION} code
     * if required.
     *
     * @param hostUrl the url to Chino.io API
     * @return the polished URL with {@link #API_VERSION} code and without trailing '/'
     */
    private static String normalizeApiUrl(String hostUrl) {
        // force https
        if (hostUrl.startsWith("http://")) {
            if (hostUrl.contains(".chino.io")) {
                hostUrl = hostUrl.replace("http://", "https://");
            } else {
                System.err.println(
                        ">> WARNING:\n" +
                        ">> You are using Chino API over HTTP.\n" +
                        ">> The API will work as usual, but HTTPS is strongly recommended."
                );
                System.err.flush();
            }
        }

        // check version is specified
        if (hostUrl.contains(API_VERSION)) {
            while (hostUrl.endsWith("/")) {
                // remove trailing '/' (if any)
                hostUrl = hostUrl.replaceFirst("/$", "");
            }
        } else {
            String errString = "\"" + hostUrl + "\": Chino API version not specified. Allowed values: %s";
            StringBuilder versions = new StringBuilder("[");
            for (String v : getAvailableVersions()) {
                versions.append("\"").append(v).append("\"");
            }
            versions.append("]");
            throw new IllegalArgumentException(
                    String.format(errString, versions.toString())
            );
        }

        return hostUrl;
    }

    /**
     * Get the default HTTP client
     * @return
     */
    static OkHttpClient.Builder getDefaultHttpClient() {
        return new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(userAgent);
    }

    OkHttpClient getHttpClient() {
        return client;
    }

    void updateHttpAuth(LoggingInterceptor authInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(userAgent);

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

    /**
     * Get a {@link List} of all versions of Chino.io API supported by this SDK
     * (now only "v1")
     *
     * @return a {@link List List&lt;String&gt;} with the supported version codes
     */
    public static List<String> getAvailableVersions() {
        return java.util.Collections.singletonList(API_VERSION);
    }
}
