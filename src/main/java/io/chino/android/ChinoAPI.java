package io.chino.android;

import okhttp3.*;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.cert.CertificateException;

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

    //There are two constructors for ChinoAPI. The first is needed to initialize a customer, the second one is for the user
    public ChinoAPI(String hostUrl, String customerId, String customerKey){
        initClient(customerId, customerKey, hostUrl);
    }

    public ChinoAPI(String hostUrl) {
        client = new OkHttpClient();
        initObjects(hostUrl);
    }

    public void initClient(final String token, String hostUrl){
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.authenticator(new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                return response.request().newBuilder().header("Authorization", "Bearer ".concat(token)).build();
            }
        });
        client = clientBuilder.build();
        initObjects(hostUrl);
    }

    public void initClient(final String customerId, final String customerKey, String hostUrl){
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.authenticator(new Authenticator() {
            @Override
            public Request authenticate(Route route, Response response) throws IOException {
                String credential = Credentials.basic(customerId, customerKey);
                return response.request().newBuilder().header("Authorization", credential).build();
            }
        });
        client = clientBuilder.build();
        initObjects(hostUrl);
    }

    public void initObjects(String hostUrl){
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
    }

}
