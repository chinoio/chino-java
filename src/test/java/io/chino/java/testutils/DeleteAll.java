package io.chino.java.testutils;

import io.chino.api.application.Application;
import io.chino.api.collection.Collection;
import io.chino.api.common.ChinoApiException;
import io.chino.api.consent.Consent;
import io.chino.api.consent.ConsentList;
import io.chino.api.document.Document;
import io.chino.api.group.Group;
import io.chino.api.repository.Repository;
import io.chino.api.schema.Schema;
import io.chino.api.user.User;
import io.chino.api.userschema.UserSchema;
import io.chino.java.*;

import java.io.IOException;
import java.util.List;

public class DeleteAll {

    /**
     * Delete all the object of a given type, according to the implementation of {@link ChinoBaseAPI}
     * that is passed as a parameter:<br>
     *     <ul>
     *         <li>{@link Applications}: delete all {@link Application} objects</li>
     *         <li>{@link Consents}: delete all the {@link Consent} objects</li>
     *         <li>{@link Users}: delete all the {@link User} objects that are stored in every UserSchema, but keeps the UserSchemas</li>
     *         <li>{@link UserSchemas}: delete all the User objects that are stored in every {@link UserSchema} and the UserSchemas themselves</li>
     *         <li>{@link Documents}: delete all the {@link Document} objects in every Schema, but keeps the Schemas and the Repositories</li>
     *         <li>{@link Schemas}: delete all the Document objects and every {@link Schema}, but keeps the Repositories</li>
     *         <li>{@link Repositories}: delete all the Document, every Schema and every {@link Repository} from the account.</li>
     *     </ul>
     *
     * @param apiClient one of the allowed implementation of {@link ChinoBaseAPI} listed above
     *
     * @throws IOException
     * @throws ChinoApiException
     */
    public void deleteAll(ChinoBaseAPI apiClient) throws IOException, ChinoApiException {
        if (apiClient instanceof Applications) {
            Applications applicationsClient = (Applications) apiClient;
            List<Application> items = applicationsClient.list().getApplications();
            for (Application app:items) {
                applicationsClient.delete(app.getAppId(), true);
            }
        } else if(apiClient instanceof Consents) {
            Consents consentsClient = (Consents) apiClient;
            List<Consent> items = consentsClient.list().getConsents();
            for (Consent consent:items) {
                consentsClient.delete(consent.getConsentId());
            }
        } else if(apiClient instanceof UserSchemas || apiClient instanceof Users) {
            ChinoAPI chino = new ChinoAPI(TestConstants.HOST, TestConstants.CUSTOMER_ID, TestConstants.CUSTOMER_KEY);
            UserSchemas userSchemaClient = chino.userSchemas;
            Users userClient = chino.users;
            List<UserSchema> schemasList = userSchemaClient.list().getUserSchemas();
            for (UserSchema userSchema:schemasList) {
                List<User> usersList = userClient.list(userSchema.getUserSchemaId()).getUsers();
                for (User user:usersList) {
                    userClient.delete(user.getUserId(), true);
                }
            }
        } else if(apiClient instanceof Repositories || apiClient instanceof Schemas || apiClient instanceof Documents) {
            ChinoAPI chino = new ChinoAPI(TestConstants.HOST, TestConstants.CUSTOMER_ID, TestConstants.CUSTOMER_KEY);
            List<Repository> repositories = chino.repositories.list().getRepositories();
            for (Repository r : repositories) {
                List<Schema> schemas = chino.schemas.list(r.getRepositoryId()).getSchemas();
                for (Schema s : schemas) {
                    List<Document> documents = chino.documents.list(s.getSchemaId()).getDocuments();
                    for (Document d : documents) {
                        chino.documents.delete(d.getDocumentId(), true);
                    }
                    if (!(apiClient instanceof Documents)) {
                        chino.schemas.delete(s.getSchemaId(), true);
                    }
                }
                if (!(apiClient instanceof Documents) && !(apiClient instanceof Schemas)) {
                    chino.repositories.delete(r.getRepositoryId(), true);
                }
            }
        } else if(apiClient instanceof Search) {
            ChinoAPI chino = new ChinoAPI(TestConstants.HOST, TestConstants.CUSTOMER_ID, TestConstants.CUSTOMER_KEY);
            deleteAll(chino.repositories);
            deleteAll(chino.userSchemas);
        } else if(apiClient instanceof Blobs) {
            ChinoAPI chino = new ChinoAPI(TestConstants.HOST, TestConstants.CUSTOMER_ID, TestConstants.CUSTOMER_KEY);
            for (Repository r : chino.repositories.list().getRepositories()) {
                if (r.getDescription().contains("BlobsTest")) {
                    List<Schema> schemas = chino.schemas.list(r.getRepositoryId()).getSchemas();
                    for (Schema s : schemas) {
                        List<Document> documents = chino.documents.list(s.getSchemaId()).getDocuments();
                        for (Document d : documents) {
                            chino.documents.delete(d.getDocumentId(), true);
                        }
                        chino.schemas.delete(s.getSchemaId(), true);
                    }
                }
                chino.repositories.delete(r.getRepositoryId(), true);
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
        } else if (apiClient instanceof Auth) {
            // do nothing
        } else {
            throw new UnsupportedOperationException("deleteAll(" + apiClient.getClass().getSimpleName() + ") is not supported.");
        }
    }

    public void deleteAll(ChinoAPI temp) throws IOException, ChinoApiException {
        List<Group> groups = temp.groups.list().getGroups();
        for(Group g : groups){
            temp.groups.delete(g.getGroupId(), true);
        }
        List<Collection> collections = temp.collections.list().getCollections();
        for(Collection c : collections){
            temp.collections.delete(c.getCollectionId(), true);
        }
        List<Application> applications = temp.applications.list().getApplications();
        for(Application a : applications){
            temp.applications.delete(a.getAppId(), true);
        }
        deleteAll(temp.repositories);
        List<UserSchema> userSchemas = temp.userSchemas.list().getUserSchemas();
        for(UserSchema u : userSchemas){
            List<User> users = temp.users.list(u.getUserSchemaId()).getUsers();
            for(User user : users){
                temp.users.delete(user.getUserId(), true);
            }
            temp.userSchemas.delete(u.getUserSchemaId(), true);
        }
        ConsentList consents = temp.consents.list(); // ConsentList consents = temp.consents.list().getConsents(); // gives the same result
        for (Consent c:consents) {
            temp.consents.delete(c.getConsentId());
        }
    }
}
