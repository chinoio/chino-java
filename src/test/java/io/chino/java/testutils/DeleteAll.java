package io.chino.java.testutils;

import io.chino.api.application.Application;
import io.chino.api.collection.Collection;
import io.chino.api.common.ChinoApiException;
import io.chino.api.consent.Consent;
import io.chino.api.document.Document;
import io.chino.api.group.Group;
import io.chino.api.repository.Repository;
import io.chino.api.schema.Schema;
import io.chino.api.user.User;
import io.chino.api.userschema.UserSchema;
import io.chino.java.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class DeleteAll {

    /**
     * Delete all objects created during this test suite.
     */
    public DeleteAll() {}

    /**
     * Delete ALL the object of a given type, according to the implementation of {@link ChinoBaseAPI}
     * that is passed as a parameter:<br>
     *     <ul>
     *         <li>{@link Applications}:
     *              delete all {@link Application} objects
     *         </li>
     *         <li>{@link Consents}:
     *              delete all the {@link Consent} objects
     *         </li>
     *         <li>{@link Users}:
     *              delete all the {@link User} objects that are stored in every UserSchema, but keeps the UserSchemas
     *         </li>
     *         <li>{@link UserSchemas}:
     *              delete all the User objects that are stored in every {@link UserSchema} and the UserSchemas
     *              themselves
     *         </li>
     *         <li>{@link Documents}:
     *              delete all the {@link Document} objects in every Schema, but keeps the Schemas and the Repositories
     *         </li>
     *         <li>{@link Schemas}:
     *              delete all the Document objects and every {@link Schema}, but keeps the Repositories
     *         </li>
     *         <li>{@link Repositories}:
     *              delete all the Document, every Schema and every {@link Repository} from the account.
     *         </li>
     *     </ul>
     *
     * @param apiClient one of the allowed implementation of {@link ChinoBaseAPI} listed above.
     *                  In order to work, the client must be authenticated: it will only delete resources if it has
     *                  permission to do so.
     *
     * @throws IOException
     * @throws ChinoApiException
     */
    public void deleteAll(ChinoBaseAPI apiClient) throws IOException, ChinoApiException {
        if (apiClient instanceof Applications) {
            Applications applicationsClient = (Applications) apiClient;
            List<Application> items = applicationsClient.list().getApplications();
            while (!items.isEmpty()) {
                for (Application app:items) {
                    applicationsClient.delete(app.getAppId(), true);
                }
                items = applicationsClient.list().getApplications();
            }
        } else if(apiClient instanceof Consents) {
            Consents consentsClient = (Consents) apiClient;
            List<Consent> items = consentsClient.list().getConsents();
            while (!items.isEmpty()) {
                for (Consent consent:items) {
                    consentsClient.delete(consent.getConsentId());
                }
                items = consentsClient.list().getConsents();
            }
        } else if(apiClient instanceof UserSchemas || apiClient instanceof Users || apiClient instanceof Permissions) {
            LinkedList<UserSchema> processedUserSchemas = new LinkedList<>();
            ChinoAPI chino = new ChinoAPI(TestConstants.HOST, TestConstants.CUSTOMER_ID, TestConstants.CUSTOMER_KEY);
            UserSchemas userSchemaClient = chino.userSchemas;
            Users userClient = chino.users;
            List<UserSchema> userSchemasList = userSchemaClient.list().getUserSchemas();
            while (!userSchemasList.isEmpty()) {
                for (UserSchema userSchema:userSchemasList) {
                    if (apiClient instanceof Users) { // only delete users
                        List<User> usersList = userClient.list(userSchema.getUserSchemaId()).getUsers();
                        while (!usersList.isEmpty()) {
                            for (User user:usersList) {
                                userClient.delete(user.getUserId(), true);
                            }
                            usersList = userClient.list(userSchema.getUserSchemaId()).getUsers();
                        }
                        processedUserSchemas.add(userSchema);
                    } else { // delete users & u. schema
                        userSchemaClient.delete(userSchema.getUserSchemaId(), true);
                    }
                }
                userSchemasList = userSchemaClient.list().getUserSchemas();
                userSchemasList.removeAll(processedUserSchemas);
            }
        } else if(apiClient instanceof Repositories || apiClient instanceof Schemas || apiClient instanceof Documents) {
            ChinoAPI chino = new ChinoAPI(TestConstants.HOST, TestConstants.CUSTOMER_ID, TestConstants.CUSTOMER_KEY);
            List<Repository> repositories = chino.repositories.list().getRepositories();
            LinkedList<Repository> processedRepos = new LinkedList<>();
            while (!repositories.isEmpty()) {
                for (Repository r : repositories) {
                    List<Schema> schemas = chino.schemas.list(r.getRepositoryId()).getSchemas();
                    LinkedList<Schema> processedSchemas = new LinkedList<>();
                    while (!schemas.isEmpty()) {
                        for (Schema s : schemas) {
                            List<Document> documents = chino.documents.list(s.getSchemaId()).getDocuments();
                            while (!documents.isEmpty()) {
                                for (Document d : documents) {
                                    chino.documents.delete(d.getDocumentId(), true);
                                }
                                documents = chino.documents.list(s.getSchemaId()).getDocuments();
                            }
                            if (!(apiClient instanceof Documents)) {
                                chino.schemas.delete(s.getSchemaId(), true);
                            } else {
                                processedSchemas.add(s);
                            }
                        }
                        schemas = chino.schemas.list(r.getRepositoryId()).getSchemas();
                        schemas.removeAll(processedSchemas);
                    }
                    if (!(apiClient instanceof Documents) && !(apiClient instanceof Schemas)) {
                        chino.repositories.delete(r.getRepositoryId(), true);
                    } else {
                        processedRepos.add(r);
                    }
                }
                repositories = chino.repositories.list().getRepositories();
                repositories.removeAll(processedRepos);
            }
        } else if(apiClient instanceof Search) {
            ChinoAPI chino = new ChinoAPI(TestConstants.HOST, TestConstants.CUSTOMER_ID, TestConstants.CUSTOMER_KEY);
            deleteAll(chino.repositories);
            deleteAll(chino.userSchemas);
        } else if(apiClient instanceof Blobs) {
            ChinoAPI chino = new ChinoAPI(TestConstants.HOST, TestConstants.CUSTOMER_ID, TestConstants.CUSTOMER_KEY);
            // get a batch of repositories and try to find the
            final int limit = 25;
            int offset = 0;
            List<Repository> repos = chino.repositories.list(offset, limit).getRepositories();
            while (!repos.isEmpty()) {
                for (Repository r : repos) {
                    if (r.getDescription().contains("BlobsTest")) {
                        // delete repository and content
                        List<Schema> schemas = chino.schemas.list(r.getRepositoryId()).getSchemas();
                        while (!schemas.isEmpty()) {
                            for (Schema s : schemas) {
                                List<Document> documents = chino.documents.list(s.getSchemaId()).getDocuments();
                                while (!documents.isEmpty()) {
                                    for (Document d : documents) {
                                        chino.documents.delete(d.getDocumentId(), true);
                                    }
                                    documents = chino.documents.list(s.getSchemaId()).getDocuments();
                                }
                                chino.schemas.delete(s.getSchemaId(), true);
                            }
                            schemas = chino.schemas.list(r.getRepositoryId()).getSchemas();
                        }
                        chino.repositories.delete(r.getRepositoryId(), true);
                    } else {
                        // try next batch
                        offset += limit;
                    }
                }
                repos = chino.repositories.list(offset, limit).getRepositories();
            }
        } else if (apiClient instanceof Collections) {
            ChinoAPI chino = new ChinoAPI(TestConstants.HOST, TestConstants.CUSTOMER_ID, TestConstants.CUSTOMER_KEY);
            deleteAll(chino.repositories);
            List<Collection> collections = chino.collections.list().getCollections();
            while (! collections.isEmpty()) {
                for (Collection c : collections) {
                    chino.collections.delete(c.getCollectionId(), true);
                }
                collections = chino.collections.list().getCollections();
            }
        } else if (apiClient instanceof Groups) {
            ChinoAPI chino = new ChinoAPI(TestConstants.HOST, TestConstants.CUSTOMER_ID, TestConstants.CUSTOMER_KEY);
            List<Group> groups = chino.groups.list().getGroups();
            while (! groups.isEmpty()) {
                for (Group g : groups) {
                    chino.groups.delete(g.getGroupId(), true);
                }
                groups = chino.groups.list().getGroups();
            }
        } else if (apiClient instanceof Auth) {
            // nothing to delete for Auth
        } else {
            if (apiClient != null)
                throw new UnsupportedOperationException("deleteAll(" + apiClient.getClass().getSimpleName() +
                        ") is not supported.");
        }
    }

    /**
     * Delete EVERYTHING on a Chino.io account, as long as the provided {@link ChinoAPI} client
     * has permissions to do so.
     *
     * @param c a {@link ChinoAPI} client that will be used to perform the delete operation
     *
     * @throws IOException
     * @throws ChinoApiException
     */
    public void deleteAll(ChinoAPI c) throws IOException, ChinoApiException {
        deleteAll(c.applications);
        deleteAll(c.userSchemas);
        deleteAll(c.documents);
        deleteAll(c.schemas);
        deleteAll(c.repositories);
        deleteAll(c.groups);
        deleteAll(c.collections);
        deleteAll(c.users);
        deleteAll(c.search);
        deleteAll(c.auth);
        deleteAll(c.permissions);
        deleteAll(c.blobs);
        deleteAll(c.consents);
    }
}
