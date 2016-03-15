package io.chino.test;

import io.chino.api.common.ChinoApiException;
import io.chino.client.ChinoAPI;
import io.chino.examples.auth.AuthSamples;
import io.chino.examples.util.Constants;
import io.chino.test.util.DeleteAll;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;

public class T1_Auth {

    ChinoAPI chino;

    @Before
    public void init() throws IOException, ChinoApiException {
        chino = new ChinoAPI(Constants.HOST, Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY);
    }

    @Test
    public void testPermissions() throws IOException, ChinoApiException {
        DeleteAll.testDelete(chino);
        AuthSamples authSamples = new AuthSamples();
        authSamples.testAuth();
    }
}
