package io.chino.test;

import io.chino.api.common.ChinoApiException;
import io.chino.java.ChinoAPI;
import io.chino.examples.repositories.RepositorySamples;
import io.chino.examples.Constants;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class T6_Repositories {

    ChinoAPI chino;

    @Before
    public void init(){
        chino = new ChinoAPI(Constants.HOST, Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY);
    }


    @Test
    public void testSchemas() throws IOException, ChinoApiException {
        RepositorySamples repositorySamples = new RepositorySamples();
        repositorySamples.testRepositories();
    }
}
