#  CHINO.io Java client #
*Official* Java wrapper for **CHINO.io** API,

Docs are available [here](http://docs.chino.io)

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
	<version>1.1</version>
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
    compile 'com.github.chinoio:chino-java:1.1'
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

you have to create a new LoggedUser variable and then you can access the ACCESS_TOKEN

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
    
### Auth
Class to manage authentication, `chino.auth`

- `loginWithPassword(<username>, <password>, <application_id>, <application_secret>)`
    log in as a User with the "password" method
- `loginWithAuthenticationCode(<code>, <redirect_url>, <application_id>, <application_secret>)`
    log in as a User with the "authentication-code" method
- `refreshToken(<refresh_token>, <application_id>, <application_secret>)`
- `checkUserStatus()`
- `logout(<token>, <application_id>, <application_secret>)`

### Application
Class to manage applications, `chino.applications`

- `list()`
- `list(<offset>, <limit>)`
- `read(<application_id>)`
- `create(<name>, <grant_type>, <redirect_url>)`
    `grant_type` can be "password" or "authentication-code" and defines the authentication method for the users
- `update(<application_id>, <name>, <grant_type>, <redirect_url>)`
- `delete(<application_id>, <force>)`
    `force` is a boolean and if it's true, the resource cannot be restored

### User
Class to manage users, `chino.users`

- `list(<user_schema_id>, <offset>, <limit>)`
- `list(<user_schema_id>)`
- `read(<user_id>)`
- `create(<username>, <password>, <attributes>, <user_schema_id>)`
- `update(<user_id>, <username>, <password>, <attributes>)`
- `update(<user_id>, <attributes>)`
    This is for a partial update (for example one attribute)
- `delete(<user_id>, <force>)`
    `force` is a boolean and if it's true, the resource cannot be restored

### Group
Class to mange groups, `chino.groups`

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

### Permission
Class to mange permissions, `chino.permissions`

- `readPermissions()`
- `readPermissions(<offset>, <limit>)`
- `readPermissionsOnaDocument(<document_id>)`
- `readPermissionsOfaUser(<user_id>)`
- `readPermissionsOfaGroup(<group_id>)`
- `permissionsOnResources(<action>, <resource_type>, <subject_type>, <subject_id>, <permission_rules>)`
- `permissionsOnaResource(<action>, <resource_type>, <resource_id>, <subject_type>, <subject_id>, <permission_rules>)`
- `permissionsOnResourceChildren(<action>, <resource_type>, <resource_id>, <resource_children>, <subject_type>, <subject_id>, <permission_rules>)`

### Repository
Class to mange repositories, `chino.repositories`

- `list()`
- `list(<offset>, <limit>)`
- `read(<repository_id>)`
- `create(<description>)`
- `update(<repository_id>, <description>)`
- `delete(<repository_id>, <force>)`
    `force` is a boolean and if it's true, the resource cannot be restored

### Schemas
Class to mange schemas, `chino.schemas`

- `list(<repository_id>)`
- `list(<repository_id>, <offset>, <limit>)`
- `read(<schema_id>)`
- `create(<repository_id>, <schema_request>)`
- `update(<schema_id>, <schema_request>)`
- `delete(<schema_id>, <force>)`
    `force` is a boolean and if it's true, the resource cannot be restored

### Document
Class to mange documents, `chino.documents`

- `list(<schema_id>)`
- `list(<schema_id>, <offset>, <limit>)`
- `read(<document_id>)`
- `create(<schema_id>, <content>)`
- `update(<document_id>, <content>)`
- `delete(<document_id>, <force>)`
    `force` is a boolean and if it's true, the resource cannot be restored

### BLOB
Class to mange blobs, `chino.blobs`

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

### SEARCH
Class to mange searches, `chino.searches`

- `searchDocuments(<schema_id>, <result_type>, <filter_type>, <sort_options_list>, <filter_option_list>, <offset>, <limit>)`

All the functions below are used in the following form

Example:
```
Documents docs = chino.search.where("test_integer").gt(123).and("test_date").eq("1994-02-04").sortAscBy("test_string").search(SCHEMA_ID);
```

For a better explanation see the file `SearchSamples` in the `io.chino.example.search` folder

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

##Support
use issues of github 
