/*
 * The MIT License
 *
 * Copyright (c) 2009-2015 Chino Srls, http://www.chino.io/
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.chino.examples;

import io.chino.java.*;
import io.chino.api.common.ChinoApiConstants;
import io.chino.api.common.ChinoApiException;
import io.chino.api.consent.Consent;
import io.chino.api.consent.ConsentHistory;
import io.chino.api.consent.ConsentList;
import io.chino.api.consent.DataController;
import io.chino.api.consent.Purpose;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
//import org.junit.After;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import static org.junit.Assert.*;

/**
 *
 * // @author Andrea Arighi [andrea@chino.io]
 */
public class ConsentsTestLauncher {
    
    private static ChinoAPI chino_admin;
    private static ArrayList<Consent> createdObjects;
    
    private static String userId1 = "mariorossi@mailmail.com",
            userId2 = "rossimario@mail.ml";
    
    /**
    * User Id value for testDeleted* methods.
    */
    private String deletedUserId = "userIdDelete@mail.ml";
    
    /**
     * consentId of the deleted Consent will be stored here.
     * Used for testDeleted* methods.
     */
    private String deletedConsentId = null;
    
    private static DataController dcSample;
    private static Purpose pSample1, pSample2, pSample3;
    private static Consent consentSample1 = null,
            consentSample2 = null;
    
    // control
    private Exception testReadException = null;
    private String testReadMessage = null;
    private static String tearDownMessage = "all objects";
    
    
    public ConsentsTestLauncher() {
    }
    
    public static void main(String[]s) throws IOException, ChinoApiException, Exception {
        setUpClass();
        ConsentsTestLauncher test = new ConsentsTestLauncher();
        
        test.testUpdate_History();
        test.tearDown();
        
//        // More tests:
//        // <test method here>
//        test.tearDown();
//        // <test method here>
//        test.tearDown();
//        // <test method here>
//        test.tearDown();
    }
   
    // @BeforeClass
    public static void setUpClass() throws IOException, ChinoApiException {
        Constants.init();
        chino_admin = new ChinoAPI(Constants.HOST, Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY);
        createdObjects = new ArrayList<>();
        
        dcSample = new DataController("Chino.io", "example", "42 John Doe St.", "java-example@chino.io", "vat123456789", true);
        
        pSample1 = new Purpose(true, "promo", "Send ads to mail and address");
        pSample2  = new Purpose(false, "third-party", "Send data to third party services");
        pSample3 = new Purpose(true, "internal", "Internal usage");
        ArrayList<Purpose> purposes = new ArrayList<>();
        purposes.add(pSample1);
        purposes.add(pSample2);
        purposes.add(pSample3);
        
        // creating consent for "mariorossi@mailmail.com"
        consentSample1 = new Consent(userId1, "Consent sample created for testing - class ConsentsTest",
                "https://www.chino.io/legal/privacy-policy", "v1.0", "web-form", dcSample, purposes);
        System.out.println(consentSample1.getConsentId());
        
        purposes.remove(pSample1);
        purposes.remove(pSample2);
        
        // creating consent for another user, "rossimario@mail.ml", with different purposes.
        consentSample2 = new Consent(new Consent(consentSample1, null, purposes), userId2);
    }
    
    // @After
    public void tearDown() {
        for (Consent c:createdObjects) {
            try {
                chino_admin.consents.delete(c.getConsentId());
            } catch (ChinoApiException apiX) {
                System.err.println("tearDownClass - server returned following error:");
                System.err.println(apiX.getLocalizedMessage());
                System.err.println("(ChinoAPIException)");
            } catch (IOException e) {
                System.err.println("'tearDownClass - could not reach the server '" + e.getLocalizedMessage() + "'");
                System.err.println("(IOException)");
            }
        }
        
        createdObjects.clear();
    }

    /**
     * Test of list method, of class Consents.
     */
    // @Test
    public void testList_3args() throws Exception {
        System.out.println("list (3 args)");
        
        int newValidConsents = 4;
        String userId = "userIdList3@mail.ml";
        for (int i = 0; i<newValidConsents; i++) {
            createdObjects.add(chino_admin.consents.create(consentSample1, userId));
        }
        
        createdObjects.add(
                chino_admin.consents.create(consentSample1, "ignoredUserId@mail.ml")
        );
        createdObjects.add(
                chino_admin.consents.create(consentSample1, "anotherIgnoredUserId@mail.ml")
        );
        
        int totalListElements = 0;
        int limit = 2;
        for (int i=0; i < (newValidConsents + limit - 1)/limit; i++) {
            int offset = i;
            // Call method to be tested
            ConsentList results = chino_admin.consents.list(userId, offset, limit);
            totalListElements += results.size();
            /*DEBUG*/
            System.out.println(results.getLimit());
            for (Consent c:results) {
                System.out.println(c.getConsentId());
            }
            System.out.println();
            /**/
        }
        //// assertEquals(newValidConsents, totalListElements);
    }

    /**
     * Test of list method, of class Consents.
     */
    // @Test
    public void testList_int_int() throws Exception {
        System.out.println("list (2 args)");
        
        int newConsents = 7;
        String userId = "userIdList2@mail.ml";
        for (int i = 0; i<newConsents; i++) {
            chino_admin.consents.create(consentSample1, userId);
        }
        createdObjects.addAll(
                chino_admin.consents.list(userId, 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT)
        );
        
        int totalListElements = 0;
        int limit = 2;
        for (int i = 0; i < (newConsents + limit - 1)/limit; i++) {
            int offset = i;
            ConsentList results = chino_admin.consents.list(offset, limit);
            totalListElements += results.size();
            if (results.size() < limit) {
                // last page of results was fetched
                // assertTrue(chino_admin.consents.list(++ offset, limit).isEmpty());
                break;
            }
        }
        // assertEquals(newConsents, totalListElements);
    }

    /**
     * Test of list method, of class Consents.
     */
    // @Test
    public void testList_0args() throws Exception {
        System.out.println("list (no args)");
        int newConsents = 5;
        String userId = "userIdList0@mail.ml";
        // cleanup all consents
        for (int i = 0; i<newConsents; i++) {
            chino_admin.consents.create(consentSample1, userId);
        }
        createdObjects.addAll(
                chino_admin.consents.list(userId, 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT)
        );
        
        // Tested method
        ConsentList results = chino_admin.consents.list();
        // assertEquals(createdObjects.size(), results.size());
    }

    /**
     * Test of create method, of class Consents.
     */
    // @Test
    public void testCreate_Consent() throws Exception {
        System.out.println("create");
        String userId = "userIdCreate@mail.ml";
        Consent base = new Consent(consentSample1, userId);
        // Tested method
        Consent local = chino_admin.consents.create(base);
        createdObjects.add(local);
        // assertNotNull(local.getUserId());
        // assertNotNull(local.getConsentId());
        
        Consent fetched = chino_admin.consents.list(userId, 0, 1).get(0);
        // assertNotNull("Couldn't retrieve created object", fetched);
        // assertNotNull("Retrieved object has no consentId", fetched.getConsentId());
        // assertEquals(fetched.getUserId(), local.getUserId());
    }

    /**
     * Test of create method, of class Consents.
     */
    // @Test
    public void testCreate_7args() throws Exception {
        System.out.println("create (7 args)");
        System.out.println("(Tested during setUpClass)");
        
        // assertNotNull(consentSample1);
    }

    /**
     * Test of create method, of class Consents.
     */
    // @Test
    public void testCreate_3args() throws Exception {
        System.out.println("create (3 args)");
        
        String userId = "userIdCreate3Args@mail.ml";
        Consent base = new Consent(consentSample1, userId);
        DataController newDataController = new DataController("new company", "new contact", "new address", "new_email@mail.ml", "new VAT", true);
        LinkedList<Purpose> newPurposes = new LinkedList<>();
        newPurposes.add(pSample3);
        // Test method
        Consent consent = chino_admin.consents.create(consentSample2, newDataController, newPurposes);
        createdObjects.add(consent);
        
        // assertNotNull(consent.getConsentId());
        // assertEquals(1, consent.getPurposes().size());
        // assertNotNull(consent.getDataController());
        // assertNotNull(consent.getInsertedDate());
        // assertNotNull(consent.getCollectionMode());
        // assertFalse(consent.getCollectionMode().isEmpty());
        // assertNotNull(consent.getDescription());
        // assertFalse(consent.getDescription().isEmpty());
        // assertNotNull(consent.getPolicyUrl());
        // assertFalse(consent.getPolicyUrl().isEmpty());
        // assertNotNull(consent.getPolicyVersion());
        // assertFalse(consent.getPolicyVersion().isEmpty());
    }

    /**
     * Test of create method, of class Consents.
     */
    // @Test
    public void testCreate_Consent_String() throws Exception {
        System.out.println("create (2 args)");
        
        String userId = "userIdCreate2Args@mail.ml";
        
        Consent consent1 = chino_admin.consents.create(consentSample2, userId);
        createdObjects.add(consent1);
        
        // assertNotNull(consent1.getUserId());
        // assertFalse(consent1.getUserId().isEmpty());
    }

    /**
     * Test of read method, of class Consents.
     */
    // @Test
    public void testRead() throws Exception {
        System.out.println("read");

        String userId = "userIdRead@mail.ml";
        
        Consent consent1 = chino_admin.consents.create(new Consent(consentSample2, userId));
        createdObjects.add(consent1);
        
        // assertNotNull("failed to read consent 1", chino_admin.consents.read(consent1.getConsentId()));
    }

    /**
     * Test of update and history methods, of class Consents.
     * Also, test of {// @link ConsentHistory#getActiveConsentOnDate(java.util.Date) getActiveConsentOnDate},
 of class {// @link ConsentHistory}.
     */
    // @Test
    public void testUpdate_History() throws Exception {
        System.out.println("update");
        String userId = "userIdCreate@mail.ml";
        Consent base = new Consent(consentSample1, userId);
        
        Consent consentOld = chino_admin.consents.create(base);
/**/        TimeUnit.SECONDS.sleep(1);
        DataController updatedDataController = new DataController(dcSample.getCompany(), "new contact", "new address", "new_email@mail.ml", dcSample.getVAT(), true);
        ArrayList<Purpose> newPurposes = new ArrayList<>();
        newPurposes.add(pSample1);
        // update consent with a new DataController
        Consent updated = new Consent(consentOld, dcSample, newPurposes);
        // Test method (update)
        Consent consentUpdated = chino_admin.consents.update(consentOld.getConsentId(), updated);
        // assertEquals(consentUpdated.getConsentId(), consentOld.getConsentId());
        // assertNotEquals(consentUpdated.getDataController(), updatedDataController);
        // assertNotEquals(consentUpdated.getPurposes(), consentOld.getPurposes());
        
        System.out.println("history");
        // Test method (history)
        ConsentHistory history = chino_admin.consents.history(consentOld.getConsentId());
        // assertFalse(history.isEmpty());
        // assertEquals(consentUpdated, history.getActiveConsentOnDate());
        // assertEquals(consentOld.getConsentId(), history.getConsentId());
        
        Consent consentOldInHistory = null;
        for (Consent c:history) {
            if (c.isWithdrawn()) {
                consentOldInHistory = c;
                break;
            }
        }
        // assertNotNull(
//            "Could not find a withdrawn Consent in history"
//                    + String.format("\n(consent_id: %s", history.getConsentId()),
//            consentOldInHistory
//        );
        // assertEquals(consentOldInHistory, consentOld);
        
        Consent test1 = history.getActiveConsentOnDate(consentOld.getInsertedDate());
        System.out.println("TEST1: inserted " + test1.getInsertedDate() + ", removed: " + test1.getWithdrawnDate());
        System.out.println(history.getActiveConsentOnDate(new Date(0)));
        Consent test2 = history.getActiveConsentOnDate(new Date());
        System.out.println("TEST2: inserted " + test2.getInsertedDate() + ", removed: " + test2.getWithdrawnDate());
        // assertEquals(history.getActiveConsentOnDate(consentOld.getInsertedDate()), consentOld);
        // assertEquals(history.getActiveConsentOnDate(consentUpdated.getInsertedDate()), consentUpdated);
        // get the Consent that was active right before consentUpdated (i.e. consentOld)
        // assertEquals(history.getActiveConsentOnDate(new Date(consentUpdated.getInsertedDate().getTime() - 10000)), consentOld);
        // get the Consent that was active right before consentOld (i.e. null)
        // assertNull(history.getActiveConsentOnDate(new Date(0)));
        // get the Consent that is active now (i.e. consentUpdated)
        // assertEquals(history.getActiveConsentOnDate(new Date()), consentUpdated);
    }
    
    /**
     * Test of the Exception that should be thrown by
 {// @link ConsentHistory#getActiveConsentOnDate(java.util.Date) getActiveConsentOnDate}
 in class {// @link ConsentHistory}.
     */
    // @Test(expected = IllegalArgumentException.class)
    public void testHistory_getActiveConsent_Exception() throws IOException, ChinoApiException {
        String userId = "userIdhistory_findVersion_Exception@mail.ml";
        Consent created = chino_admin.consents.create(consentSample1, userId);
        createdObjects.add(created);
        
        ConsentHistory history = chino_admin.consents.history(created.getConsentId());
        // this call should throw an Exception
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 1);
        Date nextYear = cal.getTime();
        history.getActiveConsentOnDate(nextYear);
    }

    /**
     * Test of withdraw method, of class Consents.
     * Also, test of {// @link ConsentHistory#getActiveConsentOnDate(java.util.Date) getActiveConsentOnDate},
 of class {// @link ConsentHistory}.
     */
    // @Test
    public void testWithdraw() throws Exception {
        System.out.println("withdraw");
        
        String userId = "userIdWithdraw@mail.ml";
        chino_admin.consents.create(consentSample1, userId);
        Consent c = chino_admin.consents.list(userId, 0, 1).get(0);
        createdObjects.add(c);
        
        // Test method
        chino_admin.consents.withdraw(c.getConsentId());
        // assertTrue(chino_admin.consents.read(c.getConsentId()).isWithdrawn());
        
        // Check that c is still returned by history() but that it's not recognized as 'active'
        ConsentHistory h = chino_admin.consents.history(c.getConsentId());
        // assertFalse(h.isEmpty());
        // assertNull(h.getActiveConsentOnDate());
        // assertNull(h.getActiveConsentOnDate(new Date()));
    }

    /**
     * Test of delete method, of class Consents.
     */
    public void deleteInit() throws Exception {
        System.out.println("delete");
        if (deletedConsentId != null)
            return;
        
        
        Consent c = chino_admin.consents.create(consentSample1, deletedUserId);
        createdObjects.add(c);
        deletedConsentId = c.getConsentId();
        
        // Test method
        chino_admin.consents.delete(deletedConsentId);
        createdObjects.remove(c);
    }
    
    // @Test(expected = ChinoApiException.class)
    public void testDeletedRead() throws Exception {
        deleteInit();
        System.out.println("delete (read)");
        chino_admin.consents.read(deletedConsentId);
    }
    
    // @Test(expected = ChinoApiException.class)
    public void testDeletedHistory() throws Exception {
        deleteInit();
        System.out.println("delete (history)");
        chino_admin.consents.history(deletedConsentId);
    }
    
    // @Test
    public void testDeletedList() throws Exception {
        deleteInit();
        System.out.println("delete (list)");
        // assertTrue(chino_admin.consents.list(deletedUserId, 0, 100).isEmpty());
    }
    
}
