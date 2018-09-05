#  CHINO.io Java SDK [ [![Build Status](https://travis-ci.org/chinoio/chino-java.svg?branch=master)](https://travis-ci.org/chinoio/chino-java) [![](https://jitpack.io/v/chinoio/chino-java.svg)](https://jitpack.io/#chinoio/chino-java) ]
Official Java wrapper for **CHINO.io** API

Full docs are available [here](http://docs.chino.io).

#### What's new - *version 1.2.4-alpha2*

* **Upgraded minimum JDK version**:
    
    Since official support for Java 7 will be dropped in December 2018, the minimum SDK version for the Chino.io SDK
    has been upgraded to Java 8.   

* **Fixed Search API**:

    the Search API used to produce strange requests, which now have been fixed.

* **New Search system**:

    **the old Search API have been deprecated**. They will be removed in a future version (1.2.5 or 1.3).
    
    A new Search interface has been implemented: see the *Search* section below to learn more.
    We strongly suggest to migrate to the new Search API as soon as possible to preserve compatibility with new versions 
    of our SDK.
    
* **Wait for Document indexing**:

    Now it is possible to wait for new Documents to be fully indexed, so that they will appear in the Search results
    right after the creation. See the *Documents* section below to learn more

## Setup
If you're using Maven, then edit your project's "pom.xml" and add this:

```xml
<repositories>
	<repository>
	    <id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>

<dependency>
	<groupId>com.github.chinoio</groupId>
	    <artifactId>chino-java</artifactId>
	<version>1.2.3</version>
</dependency>
```

If you are using Gradle, then edit your project's "build.gradle" and add this:

```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}

dependencies {
    // ...
    compile 'com.github.chinoio:chino-java:1.2.3'
}
```

Due to a bug in gradle, if you're developing in android you have to add even the following code
to the "build.gradle" file

```groovy
android {
    ...
    packagingOptions {
        exclude 'META-INF/NOTICE'
        exclude 'META-INF/LICENSE'
    }
}
```

### Building from source

```
git clone https://github.com/chinoio/chino-java.git
cd chino-java
./gradlew build
```

The output will be in "build/".

And if you want a "fat jar" with all the dependencies inside use the following commands

```
git clone https://github.com/chinoio/chino-java.git
cd chino-java
./gradlew build shadowJar
```

### How to use it
First create a variable from the `ChinoAPI` class

`ChinoAPI chino = new ChinoAPI(<host_url>, <customer_id>, <customer_key>)`

passing the `host_url` and your `customer_id` and `customer_key`

or if you want to init as a User

`ChinoAPI chino = new ChinoAPI(<host_url>)`

passing only the `host_url`, then you have to call the function

```
LoggedUser loggedUser = chino.auth.loginUser(<username>, <password>, <customer_id>);
String ACCESS_TOKEN = loggedUser.getAccessToken();
```

you have to create a new LoggedUser variable and then you can access the ACCESS_TOKEN. More on user login in the [auth](#auth) section.

If you already have a valid ACCESS_TOKEN, you can create a `ChinoAPI` object and pass the token directly

`ChinoAPI chino = new ChinoAPI(<hosturl>, <access_token>);`

Check out [ChinoAPITest.java](https://github.com/chinoio/chino-java/blob/dev-andrea/src/test/java/io/chino/java/ChinoAPITest.java) to see some practical examples about the use of the `ChinoAPI` class.

### HelloWorldDocument
To create a simple Document follow these steps:

-Create a ChinoAPI variable with your `customer_id` and `customer_key`
```
ChinoAPI chino = new ChinoAPI(<host_url>, <customer_id>, <customer_key>)
```

-Create a Repository (which is the container for Schemas)

```
chino.repositories.create(<repository_description>)
```

-Create a Schema (which is the container for Documents)

```
chino.schemas.create(<repository_id>, <schema_description>, <SchemaStructure variable>)
```

-Finally create a Document

```
chino.documents.create(<schema_id>, <HashMap or String of the content>)
```

See io.chino.examples.documents.HelloWorldDocument for full documentation example

### ChinoAPI
Base client for sending API calls to [Chino.io](https://chino.io).

You can create an authenticated client, which uses the specified auth method for every API call:

- `new ChinoAPI(<host_url>, <customer_id>, <customer_key>)` 
    authenticated with **customer credentials**
- `new ChinoAPI(<host_url>, <bearer_token>)` 
    authenticated with **bearer token**
- `new ChinoAPI(<host_url>`
    **non authenticated** - you will need to login.
    
You can change the auth method with:

- `setBearerToken(<bearer_token>)`
- `setCustomer(<customer_id>, <customer_key>)`

Example:
```Java
    ChinoAPI chino = new ChinoAPI("<host_url>", "<customer_id>", "<customer_key>");

    chino.documents.read("<document_id>");     // using customer credentials:
    
    chino.setBearerToken("<bearer_token>").documents.read("<document_id>"); // using bearer token:
    chino.documents.read("<document_id>"); // using bearer token:
```   
    
### Auth
Class to manage authentication, `chino.auth`

To set an authentication method of an API client use one of the following:

- `loginWithPassword(<username>, <password>, <application_id>, <application_secret>)`
    log in as a User with the "password" method
- `loginWithPassword(<username>, <password>, <application_id>)`
    log into a "public" Application as a User, with the "password" method
- `loginWithAuthenticationCode(<code>, <redirect_url>, <application_id>, <application_secret>)`
    log in as a User with the "authentication-code" method
- `loginWithBearerToken(<access_token>)`
    log in as a User using a previously stored access token

The Auth class also provides the following utility methods: 
- `refreshToken(<refresh_token>, <application_id>, <application_secret>)`
    use a refresh token to get new tokens 
- `checkUserStatus()`
    get current User from Chino.io
- `logout(<token>, <application_id>, <application_secret>)`
    revoke access to token

### Applications
Class to manage applications, `chino.applications`

- `list()`
- `list(<offset>, <limit>)`
- `read(<application_id>)`
- `create(<name>, <grant_type>, <redirect_url>, <client_type)`
    - `grant_type` can be either "password" or "authentication-code" and defines the authentication method for the users
    - `client_type` is "public" or "confidential" and describes the type of application (see [Client types](https://docs.chino.io/#header-client-types))
- `create(<name>, <grant_type>, <redirect_url>)`
    - see above; `client_type` is set to "confidential".
- `update(<application_id>, <name>, <grant_type>, <redirect_url>)`
- `delete(<application_id>, <force>)`
    `force` is a boolean and if it's true, the resource cannot be restored

### Users
Class to manage users, `chino.users`

- `list(<user_schema_id>, <offset>, <limit>)`
- `list(<user_schema_id>)`
- `read(<user_id>)`
- `create(<username>, <password>, <attributes>, <user_schema_id>)`
- `update(<user_id>, <username>, <password>, <attributes>)`
- `update(<user_id>, <attributes>)`
    ***WARNING: Deprecated in version 1.2.3*** - This is for a partial update
    (for example one attribute).
- `updatePartial(<user_id>, <username>, <password>, <attributes>)`
    **New in version 1.2.3** update some of the attributes of a User
- `updatePartial(<user_id>, <attributes>)` **New in version 1.2.3**
- `delete(<user_id>, <force>)`
    `force` is a boolean and if it's true, the resource cannot be restored

### Groups
Class to manage groups, `chino.groups`

- `list()`
- `list(<offset>, <limit>)`
- `read(<group_id>)`
- `create(<group_name>, <attributes>)`
- `update(<group_id>, <group_name>, <attributes>)`
- `delete(<group_id>, <force>)`
    `force` is a boolean and if it's true, the resource cannot be restored
- `addUserToGroup(<user_id>, <group_id>)`
- `removeUserFromGroup(<user_id>, <group_id>)`
- `addUserSchemaToGroup(<user_schema_id>, <group_id>)`
- `removeUserSchemaFromGroup(<user_schema_id>, <group_id>)`

### Permissions
Class to manage permissions, `chino.permissions`

- `readPermissions()`
- `readPermissions(<offset>, <limit>)`
- `readPermissionsOnaDocument(<document_id>)`
- `readPermissionsOfaUser(<user_id>)`
- `readPermissionsOfaGroup(<group_id>)`
- `permissionsOnResources(<action>, <resource_type>, <subject_type>, <subject_id>, <permission_rules>)`
- `permissionsOnaResource(<action>, <resource_type>, <resource_id>, <subject_type>, <subject_id>, <permission_rules>)`
- `permissionsOnResourceChildren(<action>, <resource_type>, <resource_id>, <resource_children>, <subject_type>, <subject_id>, <permission_rules>)`

### Repositories
Class to manage repositories, `chino.repositories`

- `list()`
- `list(<offset>, <limit>)`
- `read(<repository_id>)`
- `create(<description>)`
- `update(<repository_id>, <description>)`
- `delete(<repository_id>, <force>)`
    `force` is a boolean and if it's true, the resource cannot be restored

### Schemas
Class to manage schemas, `chino.schemas`

- `list(<repository_id>)`
- `list(<repository_id>, <offset>, <limit>)`
- `read(<schema_id>)`
- `create(<repository_id>, <schema_request>)`
- `update(<schema_id>, <schema_request>)`
- `delete(<schema_id>, <force>)`
    `force` is a boolean and if it's true, the resource cannot be restored

### Documents
Class to manage documents, `chino.documents`

- `list(<schema_id>)`
- `list(<schema_id>, <offset>, <limit>)`
- `read(<document_id>)`
- `create(<schema_id>, <content>)`
- `update(<document_id>, <content>)`
- `delete(<document_id>, <force>)`
    `force` is a boolean and if it's true, the resource cannot be restored

### BLOBs
Class to manage BLOBs, `chino.blobs`

- `uploadBlob(<path>, <document_id>, <field>, <file_name>)`
    this is the main function which calls the following functions for the upload of a Blob
    for a better explanation of the usage see the file `BlobSamples` in the `io.chino.example.blobs` folder
    `path` the path of the file named `file_name` to upload to the Document with the id `document_id`, in the field `field`
- `get(<blob_id>, <destination>)`
    `destination` is the path where to save the blob read
- `initUpload(<document_id>, <field>, <file_name>)`
- `uploadChunk(<upload_id>, <chunk_data>, <offset>, <length>)`
- `commitUpload(<upload_id>)`
- `delete(<blob_id>, <force>)`
    `force` is a boolean and if it's true, the resource cannot be restored

### Search
Class to manage searches, `chino.search`

#### New Search interface *(New in v. 1.2.4-alpha2)*

We have updated our Search API, implementing:
 
* a more dev-friendly interface
* support for complex queries - now supporting multiple conditional operators (AND, OR, NOT) in one query.
* a `SearchQueryBuilder` class, that makes queries repeatable and thread-safe 

The new Search requests must contain the following parameters:
* the Search domain, either a `UserSchema` or a `Schema`
* the `ResultType`, which can be one of `FULL_CONTENT`, `NO_CONTENT`, `ONLY_ID` and `COUNT`
* any amount (even 0) of `SortRule` objects
* a **query** describing the conditions that the search results must match:
    * name of an indexed **field**
    * an **operator** from class FilterOperator, i.e. one of `EQUALS`, `GREATER_EQUAL`, `GREATER_THAN`, `IN`, `IS`, 
    `LIKE`, `LOWER_EQUAL`, `LOWER_THAN`
    * the expected **value** for the comparison

Then the query must be built using the `buildSearch()` method.

```java
    UserSearch search = (UserSearch) chino.search
        // search domain
    .users(<user-schema-id>)
        // result type
    .setResultType(ResultType.FULL_CONTENT)
        // sort rules (0 or more)
    .addSortRule("first_name", SortRule.Order.ASC)
        // query starts here:
            .with("last_name", FilterOperator.EQUALS, "Rossi")
            .and("age", FilterOperator.GREATER_THAN, 59)  
        // return a search client to perform the query            
    .buildSearch();
    ;
``` 

The returned object is a subclass of `SearchClient` that can perform that query. By calling
```java
    search.execute();
```
you will send the API call, just like in the old search, and obtain a GetDocumentResponse.

More complex queries can be made, e.g. nested queries.

#### Example 1 - `last_name = "Smith" OR last_name = "Snow"`
simple condition (taken from the [official docs](https://chino.io/))
```java
    chino.search.users(<user-schema-id>).setResultType(ResultType.FULL_CONTENT).addSortRule("first_name", SortRule.Order.ASC)
         .with("last_name", FilterOperator.EQUALS, "Smith")
         .or("last_name", FilterOperator.EQUALS, "Snow")
         .buildSearch().execute();
```

----------------------------------------------------------

#### Example 2 - `last_name="smith" AND (age>60 OR (NOT age >= 20)))`
nested queries (taken from the [official docs](https://chino.io/) and improved)
```java
    chino.search.users(<user-schema-id>).setResultType(ResultType.FULL_CONTENT).addSortRule("first_name", SortRule.Order.ASC)
             .with("last_name", FilterOperator.EQUALS, "Smith")
             .and(
                     // create new query: use the following static method
                     SearchQueryBuilder.with("age", FilterOperator.GREATER_THAN, 60)
                     .or(
                             // create negated query: another static method
                             SearchQueryBuilder.not("age", FilterOperator.GREATER_EQUAL, 20)
                     )
             )
             .buildSearch().execute();
```

----------------------------------------------------------

#### Example 3 - `(NOT document_title = "empty") AND page_count <= 30`
use of **static imports** to improve code readability
```java
    import static SearchQueryBuilder.with;
    import static SearchQueryBuilder.not;
    import static FilterOperator.*;
    
    . . .
    
    chino.search.users(<user-schema-id>).setResultType(ResultType.FULL_CONTENT).addSortRule("first_name", SortRule.Order.ASC)
             .with(
                     not("document_title", EQUALS, "empty")
             )
             .and(
                     with("page_count", LOWER_EQUAL, 30)
             )
             .buildSearch().execute();
```

#### API overview:

`io.chino.java.Search`:
- `users(String userSchemaId)`: get a new `UsersSearch` client.
- `documents(String schemaId)`: get a new `DocumentsSearch` client.

`io.chino.api.search.UsersSearch` & `*.DocumentsSearch` (implementations of `*.SearchClient`):
- `setResultType(ResultType type)`: overwrite the type of the results returned by this search
- `addSortRule(String field, SortRule.Order order)`: add a sort rule to this search
- `with(String field, FilterOperator type, ? value)`: get a new `SearchQueryBuilder` 
with the search condition in the parameters.
- `with(SearchQueryBuilder query)`: get a new `SearchQueryBuilder` from the `query` parameter.

`io.chino.api.search.SearchQueryBuilder`:
- (static) `with(String field, FilterOperator type, ? value)`: negate the search condition in the parameters.
- (static) `with(SearchQueryBuilder query)`: return the `query` parameter.
- (static) `not(String field, FilterOperator type, ? value)`: negate the search condition in the parameters
 and return it as a new query.
- (static) `not(SearchQueryBuilder query)`: negate the `query` parameter and return it as a new query.
- `and(String field, FilterOperator type, ? value)`: get a new query that is equivalent to the original query AND the 
search condition in the parameters 
- `and(SearchQueryBuilder query)`: get a new query that is equivalent to the original query AND the `query` parameter
- `or(String field, FilterOperator type, ? value)`: get a new query that is equivalent to the original query OR the 
search condition in the parameters
- `or(SearchQueryBuilder query)`: get a new query that is equivalent to the original query OR the `query` parameter


#### Old search API
***WARNING:*** this Search system is deprecated and will be removed soon.
Please consider migrating to the new Search API.

- `searchDocuments(<schema_id>, <result_type>, <filter_type>, <sort_options_list>, <filter_option_list>, <offset>, <limit>)`

All the functions below are used in the following form

Example:
```
Documents docs = chino.search.where("test_integer").gt(123).and("test_date").eq("1994-02-04").sortAscBy("test_string").search(SCHEMA_ID);
```

- `sortAscBy`
- `sortDescBy`
- `resultType`
- `withoutIndex`
- `where`
- `search`
- `and`
- `or`
- `eq`
- `lt`
- `gt`
- `lte`
- `gte`
- `isCaseSensitive`

### Collections
`chino.collections`

- `list()`
- `list(<offset>, <limit>)`
- `read(<collection_id>)`
- `create(<name>)`
- `update(<collection_id>, <name>)`
- `delete(<collection_id>, <force>)`
    `force` is a boolean and if it's true, the resource cannot be restored
- `listDocuments(<collection_id>)`
- `addDocument(<collection_id>, <document_id>)`
- `removeDocument(<collection_id>, <document_id>)`

## Testing
With the SDK are included some JUnit tests, that are used for continuous integration.
If you want (for some reason) to run these tests by yourself, the best thing to do is to run them in
an account *ad hoc*.
In fact, after each test **every object on the account is deleted**, in order to preserve the correctness of tests.

If you know what you are doing, open `io.chino.java.TestConstants` in the test folder, then:
1. set the constant `TestConstants.FORCE_DELETE_ALL_ON_TESTS` to `true`.
As an alternative, you can also set `automated_test=allow` in your environment variables. 
2. set the required environment variables;
3. run the tests.
    
After every test, all the related object will be deleted.
(E.g. after running the `ApplicationsTest` test class, every existing *Application* on the account will be lost forever.)

Testing is made with JUnit 4. Tests are implemented for the following classes:
- `io.chino.api`:    
    * `Applications`
    * `Auth`
    * `ChinoAPI`
    * `Consents`

##Support
Report problems and ask for support using Github issues.

If you want to learn more about Chino.io, visit the [official site](https://chino.io) or email us at [info@chino.io](mailto:info@chino.io).
