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
package io.chino.java;

import io.chino.api.common.ChinoApiException;
import io.chino.api.consent.Consent;
import io.chino.api.consent.ConsentHistory;
import io.chino.api.consent.ConsentList;
import io.chino.api.consent.DataController;
import io.chino.api.consent.Purpose;
import io.chino.examples.Constants;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.TestName;

/**
 *
 * @author Andrea Arighi [andrea@chino.org]
 */
public class ConsentsTest {
    
    private static ChinoAPI chino_admin;
    private static ArrayList<Consent> createdObjects;
    
    private static String userId1 = "mariorossi@mailmail.com",
            userId2 = "rossimario@mail.ml";
    
    private static DataController dcSample;
    private static Purpose pSample1, pSample2, pSample3;
    private static Consent consentSample1 = null,
            consentSample2 = null;
    
    // control
    private Exception testReadException = null;
    private String testReadMessage = null;
    private static String tearDownMessage = "all objects";
    
    public ConsentsTest() {
    }
   
    @BeforeClass
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
        createdObjects.addAll(chino_admin.consents.list(userId1, 0, 1));
        System.out.println(consentSample1.getConsentId());
        
        purposes.remove(pSample1);
        purposes.remove(pSample2);
        
        // creating consent for another user, "rossimario@mail.ml", with different purposes.
        consentSample2 = new Consent(new Consent(consentSample1, null, purposes), userId2);
        createdObjects.addAll(chino_admin.consents.list(userId2, 0, 1));
    }
    
    @AfterClass
    public static void tearDownClass() {
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
    }

    /**
     * Test of list method, of class Consents.
     */
    @Test
    public void testList_3args() throws Exception {
        System.out.println("list (3 args)");
        
        int newConsents = 4;
        String userId = "userIdList3@mail.ml";
        for (int i = 0; i<newConsents; i++) {
            chino_admin.consents.create(consentSample1, userId);
        }
        
        createdObjects.addAll(
            chino_admin.consents.list(userId, 0, newConsents)
        );
        
        chino_admin.consents.create(consentSample1, "ignoredUserId@mail.ml");
        chino_admin.consents.create(consentSample1, "anotherIgnoredUserId@mail.ml");
        createdObjects.addAll(
            chino_admin.consents.list("ignoredUserId@mail.ml", 0, 1)
            );
        createdObjects.addAll(
            chino_admin.consents.list("anotherIgnoredUserId@mail.ml", 0, 1)
            );
        
        int totalListElements = 0;
        int limit = 2;
        for (int i=0; i < (newConsents + limit - 1)/limit; i++) {
            int offset = i;
            // Call method to be tested
            ConsentList results = chino_admin.consents.list(userId, offset, limit);
            totalListElements += results.size();
        }
        assertEquals(totalListElements, newConsents);
    }

    /**
     * Test of list method, of class Consents.
     */
    @Test
    public void testList_int_int() throws Exception {
        System.out.println("list (2 args)");
        
        int newConsents = 7;
        String userId = "userIdList2@mail.ml";
        for (int i = 0; i<newConsents; i++) {
            chino_admin.consents.create(consentSample1, userId);
        }
        createdObjects.addAll(
                chino_admin.consents.list(userId, 0, newConsents)
        );
        
        int totalListElements = 0;
        int limit = 2;
        for (int i = 0; i < (newConsents + limit - 1)/limit; i++) {
            int offset = i;
            ConsentList results = chino_admin.consents.list(offset, limit);
            totalListElements += results.size();
            if (results.size() < limit) {
                // last page of results was fetched
                assertTrue(chino_admin.consents.list(++ offset, limit).isEmpty());
                break;
            }
        }
        assertEquals(totalListElements, newConsents);
    }

    /**
     * Test of list method, of class Consents.
     */
    @Test
    public void testList_0args() throws Exception {
        System.out.println("list (no args");
        int newConsents = 5;
        String userId = "userIdList0@mail.ml";
        // cleanup all consents
        for (int i = 0; i<newConsents; i++) {
            chino_admin.consents.create(consentSample1, userId);
        }
        createdObjects.addAll(
                chino_admin.consents.list(userId, 0, newConsents)
        );
        
        // Tested method
        ConsentList results = chino_admin.consents.list();
        assertEquals(newConsents, results.size());
    }

    /**
     * Test of create method, of class Consents.
     */
    @Test
    public void testCreate_Consent() throws Exception {
        System.out.println("create");
        String userId = "userIdCreate@mail.ml";
        // Tested method
        Consent local = chino_admin.consents.create(consentSample1);
        assertNotNull(local.getUserId());
        Consent fetched = chino_admin.consents.list(userId, 0, 1).get(0);
        assertNotNull("Couldn't retrieve created object", fetched);
        assertNotNull("Retrieved object has no consentId", fetched.getConsentId());
        assertEquals(fetched.getUserId(), local.getUserId());
    }

    /**
     * Test of create method, of class Consents.
     */
    @Test
    public void testCreate_7args() throws Exception {
        System.out.println("create (7 args)");
        System.out.println("(Tested during setUpClass)");
        
        assertNotNull(consentSample1);
    }

    /**
     * Test of create method, of class Consents.
     */
    @Test
    public void testCreate_3args() throws Exception {
        System.out.println("create (3 args)");
        System.out.println("(Tested during setUpClass)");
        
        assertFalse(consentSample2.getPurposes().isEmpty());
        assertNotNull(consentSample2.getDataController());
        assertNotNull(consentSample2.getInsertedDate());
        assertNotNull(consentSample2.getCollectionMode());
        assertFalse(consentSample2.getCollectionMode().isEmpty());
        assertNotNull(consentSample2.getDescription());
        assertFalse(consentSample2.getDescription().isEmpty());
        assertNotNull(consentSample2.getPolicyUrl());
        assertFalse(consentSample2.getPolicyUrl().isEmpty());
        assertNotNull(consentSample2.getPolicyVersion());
        assertFalse(consentSample2.getPolicyVersion().isEmpty());
    }

    /**
     * Test of create method, of class Consents.
     */
    @Test
    public void testCreate_Consent_String() throws Exception {
        System.out.println("create (2 args)");
        System.out.println("(Tested during setUpClass and 'list' tests)");
        
        assertNotNull(consentSample2.getUserId());
        assertFalse(consentSample2.getUserId().isEmpty());
    }

    /**
     * Test of read method, of class Consents.
     */
    @Test
    public void testRead() throws Exception {
        System.out.println("read");
        
        Consent consent1 = chino_admin.consents.list(userId1, 0, 1).get(0);
        Consent consent2 = chino_admin.consents.list(userId2, 0, 1).get(0);
        
        assertNotNull("failed to read consent 1", chino_admin.consents.read(consent1.getConsentId()));
        assertNotNull("failed to read consent 2", chino_admin.consents.read(consent2.getConsentId()));
    }

    /**
     * Test of update and history methods, of class Consents.
     * Also, test of {@link ConsentHistory#getActiveConsent(java.util.Date) getActiveConsent},
     * of class {@link ConsentHistory}.
     */
    @Test
    public void testUpdate_History() throws Exception {
        System.out.println("update");
        Consent consentOld = chino_admin.consents.list(userId1, 0, 1).get(0);
        DataController updatedDataController = new DataController(dcSample.getCompany(), "new contact", "new address", "new_email@mail.ml", dcSample.getVAT(), true);
        // update consent with a new DataController
        Consent updated = new Consent(consentOld, dcSample, null);
        // Test method (update)
        chino_admin.consents.update(consentOld.getConsentId(), updated);
        Consent consentUpdated = chino_admin.consents.list(userId1, 0, 1).get(0);
        assertEquals(consentOld.getConsentId(), consentUpdated.getConsentId());
        assertEquals(updatedDataController, updated.getDataController());
        
        System.out.println("history");
        // Test method (history)
        ConsentHistory history = chino_admin.consents.history(consentOld.getConsentId());
        assertFalse(history.isEmpty());
        assertNull("Incorrect active consent - 'withdrawn_date' is not null", history.getActiveConsent().getWithdrawnDate());
        assertEquals(consentUpdated, history.getActiveConsent());
        assertEquals(consentOld.getConsentId(), history.getConsentId());
        
        Consent consentOldInHistory = null;
        for (Consent c:history) {
            if (c.isWithdrawn()) {
                consentOldInHistory = c;
                break;
            }
        }
        assertNotNull(
            "Could not find a withdrawn Consent in history"
                    + String.format("\n(consent_id: %s", history.getConsentId()),
            consentOldInHistory
        );
        assertEquals(consentOldInHistory, consentOld);
        
        
        assertEquals(history.getActiveConsent(consentOld.getInsertedDate()), consentOld);
        assertEquals(history.getActiveConsent(consentUpdated.getInsertedDate()), consentUpdated);
        // get the Consent that was active right before consentUpdated (i.e. consentOld)
        assertEquals(history.getActiveConsent(new Date(consentUpdated.getInsertedDate().getTime() - 10000)), consentOld);
        // get the Consent that was active right before consentOld (i.e. null)
        assertNull(history.getActiveConsent(new Date(0)));
        // get the Consent that is active now (i.e. consentUpdated)
        assertEquals(history.getActiveConsent(new Date()), consentUpdated);
    }
    
    /**
     * Test of the Exception that should be thrown by
     * {@link ConsentHistory#getActiveConsent(java.util.Date) getActiveConsent}
     * in class {@link ConsentHistory}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testHistory_getActiveConsent_Exception() throws IOException, ChinoApiException {
        String userId = "userIdhistory_findVersion_Exception@mail.ml";
        chino_admin.consents.create(new Consent(consentSample1, userId));
        Consent created = chino_admin.consents.list(userId, 0, 100).get(0);
        createdObjects.add(created);
        
        ConsentHistory history = chino_admin.consents.history(created.getConsentId());
        // this call should throw an Exception
        history.getActiveConsent(new Date((long)1.5 * System.currentTimeMillis()));
    }

    /**
     * Test of withdraw method, of class Consents.
     */
    @Test
    public void testWithdraw() throws Exception {
        System.out.println("withdraw");
        String consentId = "";
        Consents instance = null;
        String expResult = "";
        String result = instance.withdraw(consentId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of delete method, of class Consents.
     */
    @Test
    public void testDelete() throws Exception {
        System.out.println("delete");
        String consentId = "";
        Consents instance = null;
        String expResult = "";
        String result = instance.delete(consentId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
