package io.chino.test;

import io.chino.api.common.ChinoApiException;
import io.chino.java.ChinoAPI;
import io.chino.examples.documents.DocumentSamples;
import io.chino.examples.Constants;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class T8_Documents {

    ChinoAPI chino;

    @Before
    public void init() throws IOException, ChinoApiException {
        chino = new ChinoAPI(Constants.HOST, Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY);
    }

    @Test
    public void testPermissions() throws IOException, ChinoApiException {
        DocumentSamples documentSamples = new DocumentSamples();
        documentSamples.testDocuments();
    }
}
