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

import com.fasterxml.jackson.databind.JsonNode;
import io.chino.api.common.ChinoApiConstants;
import io.chino.api.common.ChinoApiException;
import io.chino.api.consent.Consent;
import io.chino.api.consent.ConsentHistory;
import io.chino.api.consent.ConsentList;
import io.chino.api.consent.ConsentListWrapper;
import io.chino.api.consent.Purpose;
import io.chino.api.consent.DataController;
import java.io.IOException;
import java.util.List;
import okhttp3.OkHttpClient;

/**
 * Base API client for Consent Management.
 * @see Consent
 * @author Andrea Arighi [andrea@chino.org]
 */
public class Consents extends ChinoBaseAPI {
    
    public Consents(String hostUrl, OkHttpClient clientInitialized) {
        super(hostUrl, clientInitialized);
    }
    
    /**
     * List all the available {@link Consent consents}, filtering by the
     * specified user_id. The results are paginated
     * @param userId a {@link String} which uniquely identifies a user,
     * as in {@link Consent#userId}.
     * @param offset page offset of the results.
     * @param limit the number of results
     * (max {@link io.chino.api.common.ChinoApiConstants#QUERY_DEFAULT_LIMIT})
     * @return a {@link ConsentList} of objects that match
     * the specified {@code userId}
     * @throws java.io.IOException request could not be executed
     * (but it might have arrived to the server).
     * @throws io.chino.api.common.ChinoApiException Chino.io
     * server responded with error code.
     */
    public ConsentList list(String userId, int offset, int limit) throws IOException, ChinoApiException {
        String uidParameter;
        if (userId == null || userId.equals("") || userId.endsWith("user_id=")) {
            // no user_id specified
            uidParameter = "";
        } else {
             uidParameter = "?user_id=" + userId;
        }
        
        int offsetValue = (offset < 0) ? 0 : offset,
                limitValue = (limit < 0) ? 0 : limit;
        
        JsonNode data = getResource("/consents" + uidParameter, offsetValue, limitValue);
        
        if (data != null) {
            ConsentListWrapper wrapper = mapper.convertValue(data, ConsentListWrapper.class);
            return new ConsentList(wrapper);
        } else {
            return null;
        }
    }
    
    /**
     * List all the available {@link Consent consents}. The results are paginated.
     * @param offset page offset of the results.
     * @param limit the number of results
     * (max {@link io.chino.api.common.ChinoApiConstants#QUERY_DEFAULT_LIMIT})
     * @return a {@link ConsentList} of objects that match
     * the specified {@code userId}
     * @throws java.io.IOException request could not be executed
     * (but it might have arrived to the server).
     * @throws io.chino.api.common.ChinoApiException Chino.io
     * server responded with error code.
     */
    public ConsentList list(int offset, int limit) throws IOException, ChinoApiException {
        return list(null, offset, limit);
    }

    /**
     * List all the available {@link Consent consents}. Results are paginated
     * and this method only fetches the first page, with a
     * {@link ChinoApiConstants#QUERY_DEFAULT_LIMIT default limit} 100.
     * Use {@link #list(int, int) list(offset, limit)} to navigate
     * or to fetch less results.
     * @return a {@link ConsentList} of objects that match
     * the specified {@code userId}
     * @throws java.io.IOException request could not be executed
     * (but it might have arrived to the server).
     * @throws io.chino.api.common.ChinoApiException Chino.io
     * server responded with error code.
     */
    public ConsentList list() throws IOException, ChinoApiException {
        return list(null, 0, ChinoApiConstants.QUERY_DEFAULT_LIMIT);
    }
    
    /**
     * Create a new {@link Consent} on Chino.io, passing a local {@link Consent}
     * object.
     * @param consentData
     * @return the data sent by Chino in the API call response,
     * mapped to a {@link Consent} object.
     */
    public Consent create(Consent consentData) throws IOException, ChinoApiException {
        checkNotNull(consentData, "consent data");
        JsonNode data = postResource("/consents", consentData);
        if (data != null)
            return mapper.convertValue(data.get("consent"), Consent.class);
        else
            return null;
    }
    
    /**
     * Create a new {@link Consent} on Chino.io with the specified data and
     * maps the API response to a new {@link Consent} object.<br>
     * <br>
     * Check
     * <a href="https://docs.chino.io//#consent-management">Chino.io API documentation</a>
     * to learn more about the parameters of the Consent Object.
     * 
     * @see Consent#Consent(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, io.chino.api.consent.DataController, java.util.List) Consent() full constructor
     * 
     * @param userId
     * @param description
     * @param policyUrl
     * @param policyVersion
     * @param collectionMode
     * @param dataController
     * @param purposes
     * @return the {@link Consent} object that was just created on Chino.io.
     * @throws java.io.IOException request could not be executed
     * (but it might have arrived to the server).
     * @throws io.chino.api.common.ChinoApiException Chino.io
     * server responded with error code.
     */
    public Consent create(String userId, String description, String policyUrl,
            String policyVersion, String collectionMode, DataController dataController,
            List<Purpose> purposes) throws IOException, ChinoApiException
    {
        checkNotNull(userId, "user_id");
        checkNotNull(description, "description");
        checkNotNull(policyUrl, "policy_url");
        checkNotNull(policyVersion, "policy_version");
        checkNotNull(collectionMode, "collection_mode");
        checkNotNull(dataController, "data_controller");
        checkNotNull(purposes, "purposes");
        
        return create(
                new Consent(userId, description, policyUrl, policyVersion, collectionMode, dataController, purposes)
        );
        
    }
    
    /**
     * Create a new {@link Consent} on Chino.io with the specified data and
     * maps the API response to a new {@link Consent} object.<br>
     * <br>
     * Check
     * <a href="https://docs.chino.io//#consent-management">Chino.io API documentation</a>
     * to learn more about the parameters of the Consent Object.
     * 
     * @see Consent#Consent(io.chino.api.consent.Consent, io.chino.api.consent.DataController, java.util.List) Consent constructor by data_controller/purposes
     * 
     * @param base the base {@link Consent} object
     * @param newDataController the new {@link DataController};
     * if {@code null}, the value will be copied from {@code base}.
     * @param newPurposes the new list of {@link Purpose} objects;
     * if {@code null}, the value will be copied from {@code base}.
     * @return the {@link Consent} object that was just created on Chino.io.
     * @throws java.io.IOException request could not be executed
     * (but it might have arrived to the server).
     * @throws io.chino.api.common.ChinoApiException Chino.io
     * server responded with error code.
     */
    public Consent create(Consent base, DataController newDataController, List<Purpose> newPurposes) throws IOException, ChinoApiException {
        checkNotNull(base, "old consent data");
        checkNotNull(newDataController, "new data_controller");
        checkNotNull(newPurposes, "new purposes");
        return create(
                new Consent(base, newDataController, newPurposes)
        );
    }
    
    /**
     * Create a new {@link Consent} on Chino.io with the specified data and
     * maps the API response to a new {@link Consent} object.<br>
     * <br>
     * Check
     * <a href="https://docs.chino.io//#consent-management">Chino.io API documentation</a>
     * to learn more about the parameters of the Consent Object.
     * 
     * @see Consent#Consent(io.chino.api.consent.Consent, java.lang.String)  Consent  constructor by user_id
     * 
     * @param base the base {@link Consent} object
     * @param userId the new {@link #userId userId}. <b>Cannot be {@code null}</b>
     * @return the {@link Consent} object that was just created on Chino.io.
     * @throws java.io.IOException request could not be executed
     * (but it might have arrived to the server).
     * @throws io.chino.api.common.ChinoApiException Chino.io
     * server responded with error code.
     */
    public Consent create(Consent base, String userId) throws IOException, ChinoApiException {
        checkNotNull(base, "old consent data");
        checkNotNull(userId, "user_id");
        return create(
                new Consent(base, userId)
        );
    }
    
    /**
     * Fetch the consent with the specified {@code consent_id}.
     * If there is a history for this {@link Consent} object, only the active
     * consent is fetched (see {@link Consent#isActive()})
     * @param consentId the {@link Consent#consentId consent_id} of the Consent to read
     * @return the {@link Consent} whose id matches {@code consent_id}, if exists, otherwise {@code null}.
     * @throws java.io.IOException request could not be executed
     * (but it might have arrived to the server).
     * @throws io.chino.api.common.ChinoApiException Chino.io
     * server responded with error code.
     */
    public Consent read(String consentId) throws IOException, ChinoApiException {
        checkNotNull(consentId, "consent_id");
        JsonNode data = getResource("/consents/" + consentId);
        if (data != null)
            return mapper.convertValue(data.get("consent"), Consent.class);
        else {
            return null;
        }
    }
    
    /**
     * Updates the {@link Consent} with matching {@code consentId};
     * the old version is kept in the Consent's history for future reference
     * and its {@link Consent#withdrawnDate withdrawn_date} field is set to a
     * non-null value.
     * 
     * @see #history(java.lang.String) history()
     * @param consentId the consent_id of the {@link Consent} to update
     * @param consentData an instance of {@link Consent} containing the updated values
     * @return the updated {@link Consent} object returned by Chino.io in the
     * API call response's body
     * @throws java.io.IOException request could not be executed
     * (but it might have arrived to the server).
     * @throws io.chino.api.common.ChinoApiException Chino.io
     * server responded with error code.
     */
    public Consent update(String consentId, Consent consentData) throws IOException, ChinoApiException {
        checkNotNull(consentId, "consent_id");
        checkNotNull(consentData, "consent data");
        JsonNode data = putResource("/consents/" + consentId, consentData);
        if (data != null)
            return mapper.convertValue(data.get("consent"), Consent.class);
        else
            return null;
    }
    
    
    /**
     * Fetch the history of the {@link Consent} whith the specified {@code consentId}.
     * @param consentId the id of the consents in the consent history
     * @return the {@link ConsentHistory} of the consent whose id matches
     * {@code consentId}.
     * @throws java.io.IOException request could not be executed
     * (but it might have arrived to the server).
     * @throws io.chino.api.common.ChinoApiException Chino.io
     * server responded with error code.
     */
    public ConsentHistory history(String consentId) throws IOException, ChinoApiException {
        checkNotNull(consentId, "consent_id");
        JsonNode rawData = getResource("/consents/" + consentId + "/history");
        
        if (rawData != null) {
            return new ConsentHistory(
                mapper.convertValue(rawData, ConsentListWrapper.class),
                read(consentId)
            );
        } else {
            return null;
        }
    }
    
    /**
     * Withdraw the {@link Consent} with the specified {@code consentId}.
     * The consent's {@link Consent#withdrawnDate withdrawn_date} will be set
     * to a non-null value and the object will no longer be active. 
     * Inactive Consents cannot be updated, but their history is available
     * on the server as a proof.
     * @param consentId id of the {@link Consent} to be withdrawn
     * @return the result {@link String}, as returned by the server.
     * @throws java.io.IOException request could not be executed
     * (but it might have arrived to the server).
     * @throws io.chino.api.common.ChinoApiException Chino.io
     * server responded with error code.
     */
    public String withdraw(String consentId) throws IOException, ChinoApiException {
        checkNotNull(consentId, "consent_id");
        return deleteResource("/consents/" + consentId, false);
    }
    
    /**
     * Deletes the {@link Consent} with the specified {@code consentId} from
     * Chino.io servers. Consents are no more available after deletion.
     * <b>This feature only works with the test API at
     * <code>api.test.chino.io</code> and is not available in production.</b>
     * @param consentId id of the {@link Consent} to be deleted
     * @return the result {@link String}, as returned by the server.
     * @throws java.io.IOException request could not be executed
     * (but it might have arrived to the server).
     * @throws io.chino.api.common.ChinoApiException Chino.io
     * server responded with error code.
     */
    public String delete(String consentId) throws IOException, ChinoApiException {
        checkNotNull(consentId, "consent_id");
        return deleteResource("/consents/" + consentId, true);
    }
}
