package io.chino.examples.applications;

import io.chino.api.application.Application;
import io.chino.api.common.ChinoApiException;
import io.chino.android.ChinoAPI;
import io.chino.examples.util.Constants;

import java.io.IOException;

public class ApplicationSamples {

    ChinoAPI chino;

    public void testApplications() throws IOException, ChinoApiException {
        //You must first initialize your ChinoAPI variable with your customerId and your customerKey
        chino = new ChinoAPI(Constants.HOST, Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY);

        Application application = chino.applications.create("ApplicationTest1", "password", "http://127.0.0.1/");
        System.out.println(application);

        application = chino.applications.update(application.getAppId(), "ApplicationTest2", "password");

        System.out.println(chino.applications.read(application.getAppId()));

        System.out.println(chino.applications.list(0));

        System.out.println(chino.applications.delete(application.getAppId(), true));
    }
}
