/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.chino.examples;

import io.chino.api.common.ChinoApiException;
import io.chino.examples.applications.ApplicationSamples;
import io.chino.examples.auth.AuthSamples;
import io.chino.examples.blobs.BlobSamples;
import io.chino.examples.collections.CollectionSamples;
import io.chino.examples.documents.DocumentSamples;
import io.chino.examples.groups.GroupSamples;
import io.chino.examples.permissions.PermissionSamples;
import io.chino.examples.repositories.RepositorySamples;
import io.chino.examples.schemas.SchemaSamples;
import io.chino.examples.search.SearchSamples;
import io.chino.examples.users.UserSamples;
import io.chino.examples.userschemas.UserSchemaSamples;
import io.chino.examples.consents.ConsentSamples;
import io.chino.java.ChinoAPI;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Run the Chino.io API examples.<br>
 * Command-line arguments:<br>
 * <ul>
 * <li><code>--help</code> OR -h        :       Print help and exit.</li>
 * <li>--run=[example]                  :       Run specified example; 'example' is the lowercase name of one of the Chino API objects.</br>
 *                                              Many '-run' parameters can be used at the same time to run all the examples of the specified objects in order. E.g.: <code>--run=users --run=groups</code></li>
 * <li><code>--clean</code> OR -c       :       Delete all the test objects from Chino.io server. Can be used together with 'run' parameters. E.g.: <code>-run=users -run=consents -c</code></li>
 * </ul>
 * @author Andrea
 */
public class ChinoAPIExamples {
    
    public static void main(String[] args) {
        // Parse command-line arguments
        LinkedList<String> arguments = new LinkedList<>();
        LinkedList<String> operations = new LinkedList<>();
        for (String s:args) {
            arguments.add(s);
            if (s.startsWith("--run=")) {
                String ex = s.replace("--run=", "");
                if(!operations.contains(ex))
                    operations.add(ex);
            } else if (s.equals("-c") || s.equals("--clean")) {
                operations.add("clean");
            }
        }
        // Print help and exit
        if (arguments.contains("--help") || arguments.contains("-h")) {
            System.out.println("Chino.io API Examples\n"
                    + "To use this class, you need to obtain your Chino.io customer id and customer key.\n"
                    + "Once you have the required credentials, you'll need to add two system environment variables: 'customer_id' and 'customer_key'."
                    + "ChinoAPIExample will read the values from there and authenticate the API calls with your credentials."
                    + "Command-line arguments:\n"
                    + "\t-h    --help\t\tShow this help\n"
                    + "\t--production-api\tUse production API instead of test API. WARNING: use only if you know what you are doing\n"
                    + "\t--run=[example]\t\tRun the specified example; 'example' is the lowercase name of one of the Chino API objects,\n"
                                    + "\t\t\t\te.g. --run=users --run=groups etc. Can be used several times to run multiple examples at once.\n"
                    + "\t-c    --clean\t\tDelete all the test objects from Chino.io server. Can be used together with 'run' parameters.\n"
                                    + "\t\t\t\tE.g.: -run=users -run=consents -c"
            );
            return;
        }
        
        // set customer_key and customer_id
        Constants.init();

        for(String example:operations) {
            if (example.equals("clean"))
                System.out.println("***cleaning***");
            else
                System.out.println("***Starting " + example + " example***");
            try {
                switch(example.toLowerCase()) {
                    case "clean":
                        // delete all test objects
                        new DeleteAll().deleteAll(new ChinoAPI(Constants.HOST, Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY));
                        System.out.println("Done.\n");
                        break;
                    case "application":
                    case "applications":
                        new ApplicationSamples().testApplications();
                        break;
                    case "auth":
                        new AuthSamples().testAuth();
                        break;
                    case "blob":
                    case "blobs":
                        new BlobSamples().testBlobs();
                        break;
                    case "collection":
                    case "collections":
                        new CollectionSamples().testCollections();
                        break;
                    case "document":
                    case "documents":
                        new DocumentSamples().testDocuments();
                        break;
                    case "group":
                    case "groups":
                        new GroupSamples().testGroups();
                        break;
                    case "permission":
                    case "permissions":
                        new PermissionSamples().testPermissions();
                        break;
                    case "repository":
                    case "repositories":
                        new RepositorySamples().testRepositories();
                        break;
                    case "schema":
                    case "schemas":
                        new SchemaSamples().testSchemas();
                        break;
                    case "search":
                    case "searches":
                        new SearchSamples().testSearch();
                        break;
                    case "user":
                    case "users":
                        new UserSamples().testUsers();
                        break;
                    case "userschema":
                    case "userschemas":
                        new UserSchemaSamples().testUserSchemas();
                        break;
                    case "consent":
                    case "consents":
                        new ConsentSamples().testConsents();
                        break;
                    default:
                        throw new IllegalArgumentException("Wrong parameter '" + example + "'");
                }
            } catch (ChinoApiException apiX) {
                System.err.println("'" + example + "' API example - server returned following error:");
                System.err.println(apiX.getLocalizedMessage());
                System.err.println("(ChinoAPIException)");
            } catch (IOException e) {
                System.err.println("'" + example + "' API example - could not reach the server '" + e.getLocalizedMessage() + "'");
                System.err.println("(IOException)");
            } catch (InterruptedException ex) {
                System.err.println("INTERRUPT EXCEPTION caught while running '" + example + "' example.");
                System.err.println(ex.getLocalizedMessage());
            }
        }
        
    }
}
