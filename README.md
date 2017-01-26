#  CHINO.io Java client #
*Official* Java wrapper for **CHINO.io** API,

Docs are available [here](http://docs.chino.io)

###Setup
If you're using Maven, then edit your project's "pom.xml" and add this to the `<dependencies>` section:

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
	<version>0.9</version>
</dependency>
```

If you are using Gradle, then edit your project's "build.gradle" and add this to the `dependencies` section:

```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}

dependencies {
    // ...
    compile 'com.github.chinoio:chino-java:0.9'
}
```

You can also download the Java SDK JAR and and its required dependencies directly from the [latest release page](https://github.com/chinoio/chino-java/releases/latest).

## Building from source

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

##How to use it
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

you have to create a new LoggedUser variable and then you can access the ACCESS_TOKEN

###HelloWorldDocument
To create a simple Document follow these steps:

-Create a ChinoAPI variable with your  `customer_id` and `customer_key`
`ChinoAPI chino = new ChinoAPI(<host_url>, <customer_id>, <customer_key>)`

-Create a Repository (which is the container for Schemas)   
 `chino.repositories.create(<repository_description>)`

-Create a Schema (which is the container for Documents)
 `chino.schemas.create(<repository_id>, <schema_description>, <SchemaStructure variable>)`

-Finally create a Document
 `chino.documents.create(<schema_id>, <HashMap or String of the content>)`

See io.chino.examples.documents.HelloWorldDocument for full documentation example
    
### Auth
Class that manages the auth, `chino.auth`

- `loginUser` to login as user
- `setCustomer` to set the auth as admin
- `checkUserStatus` to check the status of the user logged
- `logoutUser` to logoutRequest as user

### User
Class to manage the user, `chino.users`

- `list`
- `read`
- `create`
- `update`
- `delete`

### Group
`chino.groups`

- `list`
- `read`
- `create`
- `update`
- `delete`
- `addUserToGroup`
- `removeUserFromGroup`
- `addUserSchemaToGroup`
- `removeUserSchemaFromGroup`

### Permission
`chino.permissions`

- `readPermissions`
- `readPermissionsOnaDocument`
- `readPermissionsOfaUser`
- `readPermissionsOfaGroup`
- `permissionsOnResources`
- `permissionsOnaResource`
- `permissionsOnResourceChildren`

### Repository
`chino.repotiories`

- `list`
- `create`
- `read`
- `update`
- `delete`

### Schemas
`chino.schemas`

- `list`
- `create`
- `read`
- `update`
- `delete`

### Document
`chino.documents`

- `list`
- `create`
- `read`
- `update`
- `delete`


### BLOB
`chino.blobs`

- `uploadBlob`
- `get`
- `initUpload`
- `uploadChunk`
- `commitUpload`
- `delete`

### SEARCH
`chino.searches`

- `searchDocuments`

All the functions below are used in this form
Example:
`Documents docs = chino.search.where("test_integer").gt(123).and("test_date").eq("1994-02-04").sortAscBy("test_string").search(SCHEMA_ID);`

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

### UserSchemas
`chino.userSchemas`

- `list`
- `create`
- `read`
- `update`
- `delete`

### Collections
`chino.collections`

- `list`
- `create`
- `read`
- `update`
- `delete`
- `listDocuments`
- `addDocument`
- `removeDocument`

##Support
use issues of github 
