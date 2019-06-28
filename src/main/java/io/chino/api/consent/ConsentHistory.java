package io.chino.api.consent;

import java.util.Date;

/**
 * A special {@link ConsentList} that help navigating through the history of a Consent.
 * @author Andrea Arighi [andrea@chino.io]
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
     * Get the {@link Consent#getConsentId() consent_id} of the Consents in this
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
     * @param date a {@link Date} object.
     * @return the {@link Consent} which was active at that date. Returns
     * {@code null} in two cases:<br>
     * 1) if {@code insertedDate} precedes every Consent in this history<br>
     * 2) if the first Consent before {@code insertedDate} was withdrawn before that date
     * @throws IllegalArgumentException {@code insertedDate} is a {@link Date} in the future
     */
    public Consent getActiveConsentOnDate(Date date) throws IllegalArgumentException {
        Date now = new Date();
        if (date.after(now)) {
            throw new IllegalArgumentException("'insertedDate' cannot be a date in the future.\n"
                    + "\tCurrent time:" + now + "\n"
                    + "\tIllegal value:" + date);
        }
        
        Date nearestDate = null;
        Consent previousConsent = null;
        for (Consent c:this) {
            if (! c.getInsertedDate().after(date)) { // the consent needs to have been inserted before the date passed as a parameter
                if (nearestDate == null || c.getInsertedDate().after(nearestDate)) {
                    nearestDate = c.getInsertedDate();
                    previousConsent = c;
                }
            }
        }
        
        if (previousConsent == null)
            return null;
        
        if (previousConsent.getWithdrawnDate() != null && previousConsent.getWithdrawnDate().before(date))
            // the previous consent must still be active on the date passed as a parameter
            return null;
        
        return previousConsent;
    }    
    
}
