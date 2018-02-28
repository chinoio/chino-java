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
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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
    private static Consent consentSample1, consentSample2;
    
    public ConsentsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        Constants.init();
        chino_admin = new ChinoAPI(Constants.HOST, Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY);
        createdObjects = new ArrayList<>();
        
        dcSample = new DataController("Chino.io", "example", "42 John Doe St.", "java-example@chino.io", "vat123456789", true);
        
        pSample1 = new Purpose(true, "promo", "Send ads to mail and address");
        pSample2  = new Purpose(false, "third-party", "Send data to third party services");
        pSample3 = new Purpose(true, "internal", "Required data");
        ArrayList<Purpose> purps = new ArrayList<>();
        purps.add(pSample1);
        purps.add(pSample2);
        purps.add(pSample3);
        
        consentSample1 = new Consent(userId1, "Consent sample created for testing - class ConsentsTest",
                "https://www.chino.io/legal/privacy-policy", "v1.0", "web-form", dcSample, purps);
        createdObjects.add(consentSample1);
        
        purps.remove(pSample1);
        purps.remove(pSample2);
        
        // creating another consent for user "rossimario@mail.ml", with different purposes
        consentSample2 = new Consent(new Consent(consentSample1, null, purps), userId2);
        createdObjects.add(consentSample2);
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
        String userId = "userId3@mail.ml";
        for (int i = 0; i<newConsents; i++) {
            createdObjects.add(
                    chino_admin.consents.create(consentSample1, userId)
            );
        }
        
        int totalListElements = 0;
        int limit = 2;
        for (int i=0; i < newConsents/limit; i++) {
            int offset = i;
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
        System.out.println("list");
        int offset = 0;
        int limit = 0;
        Consents instance = null;
        ConsentList expResult = null;
        ConsentList result = instance.list(offset, limit);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of list method, of class Consents.
     */
    @Test
    public void testList_0args() throws Exception {
        System.out.println("list");
        Consents instance = null;
        ConsentList expResult = null;
        ConsentList result = instance.list();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of create method, of class Consents.
     */
    @Test
    public void testCreate_Consent() throws Exception {
        System.out.println("create");
        Consent consentData = null;
        Consents instance = null;
        Consent expResult = null;
        Consent result = instance.create(consentData);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of create method, of class Consents.
     */
    @Test
    public void testCreate_7args() throws Exception {
        System.out.println("create");
        String userId = "";
        String description = "";
        String policyUrl = "";
        String policyVersion = "";
        String collectionMode = "";
        DataController dataController = null;
        List<Purpose> purposes = null;
        Consents instance = null;
        Consent expResult = null;
        Consent result = instance.create(userId, description, policyUrl, policyVersion, collectionMode, dataController, purposes);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of create method, of class Consents.
     */
    @Test
    public void testCreate_3args() throws Exception {
        System.out.println("create");
        Consent base = null;
        DataController newDataController = null;
        List<Purpose> newPurposes = null;
        Consents instance = null;
        Consent expResult = null;
        Consent result = instance.create(base, newDataController, newPurposes);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of create method, of class Consents.
     */
    @Test
    public void testCreate_Consent_String() throws Exception {
        System.out.println("create");
        Consent base = null;
        String userId = "";
        Consents instance = null;
        Consent expResult = null;
        Consent result = instance.create(base, userId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of read method, of class Consents.
     */
    @Test
    public void testRead() throws Exception {
        System.out.println("read");
        String consentId = "";
        Consents instance = null;
        Consent expResult = null;
        Consent result = instance.read(consentId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of update method, of class Consents.
     */
    @Test
    public void testUpdate() throws Exception {
        System.out.println("update");
        String consentId = "";
        Consent consentData = null;
        Consents instance = null;
        Consent expResult = null;
        Consent result = instance.update(consentId, consentData);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of history method, of class Consents.
     */
    @Test
    public void testHistory() throws Exception {
        System.out.println("history");
        String consentId = "";
        Consents instance = null;
        ConsentHistory expResult = null;
        ConsentHistory result = instance.history(consentId);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
