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
package io.chino.examples.consents;

import io.chino.api.common.ChinoApiException;
import io.chino.api.consent.Consent;
import io.chino.api.consent.DataController;
import io.chino.api.consent.Purpose;
import io.chino.java.ChinoAPI;
import io.chino.test.util.Constants;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Andrea Arighi [andrea@chino.org]
 */
public class ConsentSamples {
    
    private ChinoAPI chino_admin;
    
    public void testConsents() throws IOException, ChinoApiException {
        chino_admin = new ChinoAPI(Constants.HOST, Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY);
                        
        DataController dc = new DataController("Chino.io", "example", "42 John Doe St.", "java-example@chino.io", "vat123456789", true);
        Purpose p1 = new Purpose(true, "promo", "Send ads to mail and address"),
                p2 = new Purpose(false, "third-party", "Send data to third party services"),
                p3 = new Purpose(true, "internal", "Required data");
        ArrayList<Purpose> purposes = new ArrayList<>();
        purposes.add(p1);
        purposes.add(p2);
        purposes.add(p3);

        // create
        Consent local = new Consent("mail@mailmail.com", "Chino.io Consent Management example", "https://www.chino.io/legal/privacy-policy", "v1.0", "web-form", dc, purposes);
        System.out.println("Local:");
        System.out.println(local.toString());

        // test if 'equals()' works
        Consent created1 = chino_admin.consents.create(local);
        System.out.println("Created:");
        System.out.println(created1.toString());
        Consent created2 = chino_admin.consents.create("mail@mailmail.com", "Chino.io service agreement", "https://www.chino.io/legal/privacy-policy", "v1.0", "web-form", dc, purposes);
        System.out.println("1. Expected: false. Actual: " + created1.equals(created2));

        // read 'created2'
        Consent read = chino_admin.consents.read(created2.getConsentId());
        if (read.equals(created2)) {
            System.out.println("2. Correctly retrieved consent.");
        } else {
            System.out.println("2. Error in read consent.");
            System.out.println("Original:\n" + created2.toString() + "\n");
            System.out.println("Read:\n" + read.toString() + "\n");
            System.out.flush();
            System.out.println("deleting first consent... " + chino_admin.consents.delete(created1.getConsentId()));
            System.out.println("deleting second consent...  " + chino_admin.consents.delete(created2.getConsentId()));
            System.out.println("exiting...");
            return;
        }

        // update (remove purposes 1 and 2 from 'read')
        ArrayList<Purpose> newPurposes = new ArrayList<>(purposes);
        newPurposes.remove(p1);
        newPurposes.remove(p2);
        Consent updated = chino_admin.consents.update(read.getConsentId(), new Consent(read, null, newPurposes));
        System.out.println("3. Expected: false. Actual: " + updated.equals(read));
        System.out.println("4. Expected: false. Actual: " + updated.getPurposes().contains(p1));
        System.out.println("5. Expected: false. Actual: " + updated.getPurposes().contains(p2));
        System.out.println("6. Expected: true. Actual: " + updated.getPurposes().contains(p3));
        System.out.println();

        // history
        System.out.println("Now there are "
                + chino_admin.consents.history(updated.getConsentId()).size()
                + " elements in the consent history.");

        // withdraw consent1
        System.out.println("withdraw: " + chino_admin.consents.withdraw(created1.getConsentId()));


        // delete
        System.out.println("deleting first consent... " + chino_admin.consents.delete(created1.getConsentId()));
        System.out.println("deleting second consent...  " + chino_admin.consents.delete(created2.getConsentId()));
        System.out.println();
        System.out.println("list:");
        System.out.println(chino_admin.consents.list());
                                
    }
}
