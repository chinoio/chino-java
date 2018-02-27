/*
 * The MIT License
 *
 * Copyright 2018 Andrea Arighi [andrea@chino.org].
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

import io.chino.api.consent.Consent;
import io.chino.api.consent.ConsentHistory;
import io.chino.api.consent.ConsentList;
import io.chino.api.consent.DataController;
import io.chino.api.consent.Purpose;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrea Arighi [andrea@chino.org]
 */
public class ConsentsTest {
    
    public ConsentsTest() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of list method, of class Consents.
     */
    @Test
    public void testList_3args() throws Exception {
        System.out.println("list");
        String userId = "";
        int offset = 0;
        int limit = 0;
        Consents instance = null;
        ConsentList expResult = null;
        ConsentList result = instance.list(userId, offset, limit);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
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
