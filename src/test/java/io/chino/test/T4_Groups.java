package io.chino.test;

import io.chino.api.common.ChinoApiException;
import io.chino.java.ChinoAPI;
import io.chino.examples.groups.GroupSamples;
import io.chino.examples.Constants;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class T4_Groups {

    ChinoAPI chino;

    // @Before
    public void init() throws IOException, ChinoApiException {
        chino = new ChinoAPI(Constants.HOST, Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY);
    }

    // @Test
    public void testPermissions() throws IOException, ChinoApiException {
        GroupSamples groupSamples = new GroupSamples();
        groupSamples.testGroups();
    }
}
