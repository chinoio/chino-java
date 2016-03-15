package io.chino.test;

import io.chino.api.common.ChinoApiException;
import io.chino.client.ChinoAPI;
import io.chino.examples.repositories.RepositorySamples;
import io.chino.examples.util.Constants;
import io.chino.test.util.DeleteAll;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class T6_Repositories {

    static ChinoAPI chino;

    @Before
    public void init(){
        chino = new ChinoAPI(Constants.HOST, Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY);
    }


    @Test
    public void testSchemas() throws IOException, ChinoApiException {
        DeleteAll.testDelete(chino);
        RepositorySamples repositorySamples = new RepositorySamples();
        repositorySamples.testRepositories();
    }
}
