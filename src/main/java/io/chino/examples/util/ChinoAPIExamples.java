/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.chino.examples.util;

import io.chino.test.util.Constants;
import io.chino.api.common.ChinoApiException;
import io.chino.api.permission.Permission;
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
import io.chino.java.ChinoAPI;
import java.util.LinkedList;

/**
 * Run the Chino.io API examples.<br>
 * Command-line arguments:<br>
 *      <code>--help</code> OR -h               Print help and exit<br>
 *      --run=[example]                         Run specified example; 'example' is the lowercase name of one of the Chino API objects (e.g. --run=users --run=groups etc...)
 *                                              and more '-run' parameters can be used to run all the test
 * @author Andrea
 */
public class ChinoAPIExamples {
    
    public static ChinoAPI chino_admin;
    
    // TODO: remove DEBUG and all the code with "if(DEBUG)"
    private final static boolean DEBUG = true;
    
    
    public static void main(String[] args) {
        // Parse command-line options
        LinkedList<String> opts = new LinkedList<>();
        LinkedList<String> examples = new LinkedList<>();
        for (String s:args) {
            opts.add(s);
            if (s.startsWith("--run=")) {
                String ex = s.replace("--run=", "");
                if(!examples.contains(ex))
                    examples.add(ex);
            }
        }
        // Print help and exit
        if (opts.contains("--help") || opts.contains("-h")) {
            System.out.println("Command-line arguments:\n"
                    + "\t-h    --help\t\tShow this help\n"
                    + "\t--production-api\tUse production API instead of test API. WARNING: use only if you know what you are doing\n"
                    + "\t--run=[example]\t\tRun the specified example; 'example' is the lowercase name of one of the Chino API objects,\n"
                                    + "\t\te.g. --run=users --run=groups etc. Can be used several times to run multiple examples at once.\n"
                    + ""
            );
            return;
        }
        // Run examples
//        if (!DEBUG)
//            chino_admin = new ChinoAPI(Constants.HOST, System.getenv("customer_id"), System.getenv("customer_key"));
//        else
//            chino_admin = new ChinoAPI(Constants.HOST, Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY);

        if(DEBUG) {
            Constants.CUSTOMER_ID = System.getenv("customer_id");
            Constants.CUSTOMER_KEY = System.getenv("customer_key");
            Constants.USERNAME = "mrossi";
            Constants.PASSWORD = "rossimario57";
        }
        
        if (DEBUG)
            

        for(String example:examples) {
            if (DEBUG)
                System.out.println(example);
            try {
                switch(example.toLowerCase()) {
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
                    default:
                        throw new IllegalArgumentException("Wrong parameter '" + example + "'");
                }
            } catch (ChinoApiException apiX) {
                System.err.println("API EXCEPTION caught while running '" + example + "' example.");
                System.err.println(apiX.getLocalizedMessage());
            } catch (Exception e) {
                System.err.println("EXCEPTION caught while running '" + example + "' example.");
                System.err.println(e.getLocalizedMessage());
            }
        }
        
    }
}
