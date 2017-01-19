package io.chino.android;

import io.chino.api.common.LoggingInterceptor;
import okhttp3.*;

public class ChinoAPI {
    private LoggingInterceptor interceptor;
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

    /**
     * The constructor for the developer
     * @param hostUrl the url of the server
     * @param customerId the id of the customer
     * @param customerKey the key of the customer
     */
    public ChinoAPI(String hostUrl, String customerId, String customerKey){
        interceptor = new LoggingInterceptor();
        client = new OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor).build();
        interceptor.setCustomer(customerId, customerKey);
        initObjects(hostUrl);
    }

    /**
     * The constructor for the user
     * @param hostUrl the url of the server
     */
    public ChinoAPI(String hostUrl) {
        client = new OkHttpClient();
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
        auth = new Auth(hostUrl, client, interceptor);
        permissions = new Permissions(hostUrl, client);
        blobs = new Blobs(hostUrl, client);
    }

}
