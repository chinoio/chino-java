package io.chino.examples;

import io.chino.java.ChinoAPI;
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
