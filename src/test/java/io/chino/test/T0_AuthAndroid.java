package io.chino.test;

import io.chino.api.application.Application;
import io.chino.api.auth.LoggedUser;
import io.chino.api.common.ChinoApiException;
import io.chino.api.user.User;
import io.chino.api.userschema.UserSchema;
import io.chino.examples.userschemas.UserSchemaStructureSample;
import io.chino.java.ChinoAPI;
import io.chino.examples.auth.AuthSamples;
import io.chino.examples.util.Constants;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.HashMap;

public class T0_AuthAndroid {

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
