package io.chino.test;

import io.chino.api.common.ChinoApiException;
import io.chino.java.ChinoAPI;
import io.chino.examples.auth.AuthSamples;
import io.chino.test.util.Constants;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;

public class T0_Auth {

    ChinoAPI chino;

    @Before
    public void init() throws IOException, ChinoApiException {
        chino = new ChinoAPI(Constants.HOST, Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY);
    }

    @Test
    public void testAuthAndroid() throws IOException, ChinoApiException {
        AuthSamples authSamples = new AuthSamples();
        authSamples.testAuth();
    }
}
