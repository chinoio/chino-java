package io.chino.test;

import io.chino.api.common.ChinoApiException;
import io.chino.java.ChinoAPI;
import io.chino.examples.collections.CollectionSamples;
import io.chino.examples.Constants;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class T11_Collections {

    ChinoAPI chino;

    @Before
    public void init() throws IOException, ChinoApiException{
        chino = new ChinoAPI(Constants.HOST, Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY);
    }

    @Test
    public void testPermissions() throws IOException, ChinoApiException {
        CollectionSamples collectionSamples = new CollectionSamples();
        collectionSamples.testCollections();
    }
}
