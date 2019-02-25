package io.chino.java.testutils;

import io.chino.api.common.ChinoApiException;
import io.chino.java.ChinoAPI;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class ResetAccount {

    private static String USERNAME = "testusrname";
    private static String PASSWORD = "testpword32";

    public static void main(String[] s) throws IOException, ChinoApiException {
        TestConstants.init(USERNAME, PASSWORD);

        String response;

        if (Arrays.asList(s).contains("--yes")) {
            response = "yes";
        } else {
            Scanner in = new Scanner(System.in);
            System.out.println("--- YOUR ACCOUNT ----------------------" + "\n" +
                               " ~ Chino.io host: " + TestConstants.HOST + "\n" +
                               " ~ Customer ID: " + TestConstants.CUSTOMER_ID.substring(0, 5) + "...\n" +
                               " ~ Customer key: ..." + TestConstants.CUSTOMER_KEY.substring(TestConstants.CUSTOMER_KEY.length() - 5) + "\n" +
                               "---------------------------------------");
            System.out.println("Are you sure you want to delete EVERYTHING on your account?");
            System.out.print("This operation cannot be undone. [yes/NO]> ");
            response = in.nextLine().toLowerCase();
        }

        if (response.equals("yes")) {
            ChinoAPI chino_admin = new ChinoAPI(TestConstants.HOST, TestConstants.CUSTOMER_ID, TestConstants.CUSTOMER_KEY);
            new DeleteAll("").deleteAll(chino_admin);
            System.out.println("Done.");
        } else {
            System.out.println("Aborted.");
        }
    }
}
