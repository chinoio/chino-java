package io.chino.examples.applications;

import io.chino.api.application.Application;
import io.chino.api.common.ChinoApiException;
import io.chino.android.ChinoAPI;
import io.chino.examples.util.Constants;

import java.io.IOException;

public class ApplicationSamples {

    private String APPLICATION_ID_1 = "";
    private String APPLICATION_ID_2 = "";

    ChinoAPI chino;

    public void testApplications() throws IOException, ChinoApiException {
        //You must first initialize your ChinoAPI variable with your customerId and your customerKey
        chino = new ChinoAPI(Constants.HOST, Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY);

        /*  Then you create your first Application object. Let's create one with "password" method for the authentication.
            Since we're using the "password" method the third parameter (the redirection_url) is useless, therefore we pass to
            the function the value "http://127.0.0.1/"
         */
        Application application = chino.applications.create("ApplicationTest1", "password", "http://127.0.0.1/");
        APPLICATION_ID_1 = application.getAppId();

        //We print the resulting Object
        System.out.println(application);

        //We try to update the Application already created
        chino.applications.update(APPLICATION_ID_1, "ApplicationTest2", "password");

        //We try to read the application updated and print it in the console
        System.out.println(chino.applications.read(APPLICATION_ID_1));

        //Let's create another Application and then print the result of the "list" operation
        application = chino.applications.create("ApplicationTest2", "password", "http://127.0.0.1/");
        APPLICATION_ID_2 = application.getAppId();
        System.out.println(chino.applications.list());

        //Finally we delete the Applications we created
        System.out.println(chino.applications.delete(APPLICATION_ID_1, true));
        System.out.println(chino.applications.delete(APPLICATION_ID_2, true));
    }
}
