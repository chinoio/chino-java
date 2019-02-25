package io.chino.java.testutils;

import io.chino.api.common.ChinoApiException;
import io.chino.java.ChinoAPI;

import java.io.IOException;
import java.util.Scanner;

public class ResetAccount {

    private static String USERNAME = "testusrname";
    private static String PASSWORD = "testpword32";

    public static void main(String[] s) throws IOException, ChinoApiException {
        TestConstants.init(USERNAME, PASSWORD);

        Scanner in = new Scanner(System.in);
        System.out.println("Are you sure you want to delete EVERYTHING on your account?");
        System.out.print("This operation cannot be undone. [yes/NO]> ");
        String response = in.nextLine().toLowerCase();

        if (response.equals("yes")) {
            ChinoAPI chino_admin = new ChinoAPI(TestConstants.HOST, TestConstants.CUSTOMER_ID, TestConstants.CUSTOMER_KEY);
            new DeleteAll("").deleteAll(chino_admin);
            System.out.println("Done.");
        } else {
            System.out.println("Aborted.");
        }
    }
}
