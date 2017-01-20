package io.chino.examples.repositories;

import io.chino.api.common.ChinoApiException;
import io.chino.api.repository.Repository;
import io.chino.android.ChinoAPI;
import io.chino.examples.util.Constants;

import java.io.IOException;

public class RepositorySamples {

    String REPOSITORY_ID = "";
    String REPOSITORY_ID_2 = "";
    ChinoAPI chino;

    public void testRepositories() throws IOException, ChinoApiException {
        //We initialize the ChinoAPI variable with the customerId and customerKey
        chino = new ChinoAPI(Constants.HOST, Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY);

        //Check CREATE Repository
        Repository repository = chino.repositories.create("test_repository");
        REPOSITORY_ID = repository.getRepositoryId();
        System.out.println(repository);

        //Check UPDATE Repository
        System.out.println(chino.repositories.update(REPOSITORY_ID, "test_repository_updated"));

        //Check LIST Repository
        REPOSITORY_ID_2 = chino.repositories.create("new_test_repository").getRepositoryId();
        System.out.println(chino.repositories.list());

        //Check DELETE Repository
        System.out.println(chino.repositories.delete(REPOSITORY_ID, true));
        System.out.println(chino.repositories.delete(REPOSITORY_ID_2, true));
    }
}
