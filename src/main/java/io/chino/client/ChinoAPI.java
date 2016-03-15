package io.chino.client;

import javax.ws.rs.client.Client;

import io.chino.api.common.*;

public class ChinoAPI{

	//Those two variables are needed to initialize the client, which is passed to all the Objects below when they are created
	private ChinoClient chinoClient;
	public Client client = null;

	public Users users;
	public UserSchemas userSchemas;
	public Auth auth;
	public Documents documents;
	public Groups groups;
	public Blobs blobs;
	public Permissions permissions;
	public Repositories repositories;
	public Schemas schemas;
	public Search search;
	public Collections collections;

	//There are two constructors for ChinoAPI. The first is needed to initialize a customer, the second one is for the user
	public ChinoAPI(String hostUrl, String customerId, String customerKey){
		initClient(customerId, customerKey);
		initializeObjects(hostUrl);
	}
	/*
		In this case we initialize a client that has no authentication method. To login as a user the developer needs to
		call the function loginUser(...) in Auth class
	*/
	public ChinoAPI(String hostUrl) {
		initClient();
		initializeObjects(hostUrl);
	}

	//This calls the function setAuth in ChinoClient which sets the basic auth as <customerId>:<customerKey>
	public void initClient(String customerId, String customerKey){
		chinoClient = new ChinoClient();
		chinoClient.setAuth(customerId, customerKey);
		client = chinoClient.getClient();

	}
	//This calls no auth function, so the client has no authentication method
	public void initClient(){
		chinoClient = new ChinoClient();
		client = chinoClient.getClient();
	}
	//This function initialize all the variables that are needed to call the functions inside them
	private void initializeObjects(String hostUrl){
			users = new Users(hostUrl, client);
			userSchemas = new UserSchemas(hostUrl, client);
			auth = new Auth(hostUrl, client, chinoClient.getFilter());
			documents = new Documents(hostUrl, client);
			groups = new Groups(hostUrl, client);
			permissions = new Permissions(hostUrl, client);
			repositories = new Repositories(hostUrl, client);
			schemas = new Schemas(hostUrl, client);
			search = new Search(hostUrl, client);
			blobs = new Blobs(hostUrl, client);
			collections = new Collections(hostUrl, client);
	}
	
}
