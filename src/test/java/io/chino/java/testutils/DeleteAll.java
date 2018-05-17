package io.chino.java.testutils;

import io.chino.java.*;
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

import java.io.IOException;
import java.util.List;

public class DeleteAll {

    /**
     * Delete all the object of a given type, according to the implementation of {@link ChinoBaseAPI}
     * that is passed as a parameter:<br>
     *     <ul>
     *         <li>{@link Applications}: delete all {@link Application} objects</li>
     *         <li>{@link Consents}: delete all the {@link Consent} objects</li>
     *         <li>{@link UserSchemas} OR {@link Users}: delete all the {@link User} objects that are stored in every {@link UserSchema}</li>
     *         <li><i>Other elements to be added soon...</i></li>
     *     </ul>
     * @param apiClient one of the allowed implementation of {@link ChinoBaseAPI} listed above
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
            for(Repository r : repositories){
                List<Schema> schemas = chino.schemas.list(r.getRepositoryId()).getSchemas();
                for(Schema s : schemas){
                    List<Document> documents = chino.documents.list(s.getSchemaId()).getDocuments();
                    for(Document d : documents){
                        chino.documents.delete(d.getDocumentId(), true);
                    }
                    chino.schemas.delete(s.getSchemaId(), true);
                }
                chino.repositories.delete(r.getRepositoryId(), true);
            }
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
        List<Repository> repositories = temp.repositories.list().getRepositories();
        for(Repository r : repositories){
            List<Schema> schemas = temp.schemas.list(r.getRepositoryId()).getSchemas();
            for(Schema s : schemas){
                List<Document> documents = temp.documents.list(s.getSchemaId()).getDocuments();
                for(Document d : documents){
                    temp.documents.delete(d.getDocumentId(), true);
                }
                temp.schemas.delete(s.getSchemaId(), true);
            }
            temp.repositories.delete(r.getRepositoryId(), true);
        }
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
