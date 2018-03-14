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
package io.chino.api.consent;

import java.util.Date;

/**
 * A special {@link ConsentList} that help navigating through the history of a Consent.
 * @author Andrea Arighi [andrea@chino.org]
 */
public class ConsentHistory extends ConsentList {
    
    private Consent active = null;
    
    public ConsentHistory(ConsentListWrapper wrapper) {
        super(wrapper);
        
        for (Consent c:this) {
            if (!c.isWithdrawn()) {
                active = c;
                break;
            }
        }
    }
    
    /**
     * Get the {@link Consent#consentId consent_id} of the Consents in this
     * history.
     * @return a {@link String} containing the consent_id, or {@code null}
     * if the history is empty.
     */
    public String getConsentId() {
        return (this.isEmpty()) ? null : this.get(0).getConsentId();
    }
        
    /**
     * Get the currently active version of this Consent.
     * @return the active {@link Consent} in this history
     * if there is one, otherwise {@code null}.
     */
    public Consent getActiveConsent() {
        return active;
    }
    
    /**
     * Get the Consent version that was active at the specified Date.
     * @param insertedDate a {@link Date} object.
     * @return the {@link Consent} which was active at that date. Returns
     * {@code null} in two cases:<br>
     * 1) if {@code insertedDate} preceeds every Consent in this history<br>
     * 2) if the first Consent before {@code insertedDate} was withdrawn before that date
     * @throws IllegalArgumentException insertedDate is a Date in the future
     */
    public Consent getActiveConsent(Date insertedDate) throws IllegalArgumentException {
        Date now = new Date();
        if (insertedDate.after(now)) {
            throw new IllegalArgumentException("'insertedDate' cannot be a date in the future.\n"
                    + "Illegal value:" + insertedDate);
        }
        
        Date nearestDate = null;
        Consent previousInsertedConsent = null;
        for (Consent c:this) {
            if (!c.getInsertedDate().before(insertedDate)) {
                if (nearestDate == null || c.getInsertedDate().before(nearestDate)) {
                    nearestDate = c.getInsertedDate();
                    previousInsertedConsent = c;
                }
            }
        }
        
        if (!previousInsertedConsent.getWithdrawnDate().after(insertedDate))
            return null;
        
        return previousInsertedConsent;
    }
}
