/*
 * The MIT License
 *
 * Copyright 2018 Andrea Arighi <andrea@chino.org>.
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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.chino.api.user.User;
import io.chino.java.ChinoBaseAPI;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represents the consent given by a user to a policy.
 * @author Andrea Arighi [andrea@chino.io]
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "user_id",
        "description",
        "data_controller",
        "consent_id",
        "purposes",
        "policy_url",
        "policy_version",
        "withdrawn_date",
        "inserted_date",
        "collection_mode"
})
public class Consent {
    
    /**
     * A {@link String} that identifies uniquely the user who gave his/her consent.
     * Can be any String, e.g. the email address or the user_id of a Chino.io {@link User}
     */
    @JsonProperty("user_id")
    private String userId;
    
    /**
     * A (potentially long) text description of this consent
     */
    @JsonProperty("description")
    private String description;
    
    /**
     * A {@link DataController} object, which represents the
     * organization that is responsible for the usage of the data.
     */
    @JsonProperty("data_controller")
    private DataController dataController;
    
    /**
     * The id of this Consent.
     */
    @JsonProperty("consent_id")
    private String consentId;
    
    /**
     * A list of {@link DataCollectionPurpose} objects, which represent
     * how the {@link #dataController data controller} was allowed to use the user's data
     * by the user itself.
     */
    @JsonProperty("purposes")
    private List<DataCollectionPurpose> purposes = new LinkedList<>();
    
    /**
     * A valid URL to the policy text
     */
    @JsonProperty("policy_url")
    private String policyUrl;
    
    /**
     * Version number of the policy at {@link #policyUrl policyUrl}
     */
    @JsonProperty("policy_version")
    private String policyVersion;
  
    /**
     * Timestamp of withdrawal of this Consent; if this field is not null,
     * the current Consent was withdrawn by the user. Otherwise, it is active.
     */
    @JsonProperty("withdrawn_date")
    private String withdrawnDate = null;
    
    /**
     * Timestamp of creation of this Consent.
     */
    @JsonProperty("inserted_date")
    private String insertedDate;
    
    /**
     * Brief {@link String} which describes how the consent was collected.
     */
    @JsonProperty("collection_mode")
    private String collectionMode;
    
    
    
    /**
     * Check if this Consent is still active.
     * @return true if the value of {@link #withdrawnDate withdrawn_date}
     * has not been set; false otherwise.
     */
    public boolean isActive() {
        return (withdrawnDate == null);
    }

    @Override
    public String toString() {
        
        String purposeList = "[\n";
        for (DataCollectionPurpose purp:purposes) {
            purposeList += "\t\t" + purp.toString(2) + "\n";
        }
        purposeList += "\t]";
        
        String dataControllerString = "";
        try {
            dataControllerString = ChinoBaseAPI.getMapper().writeValueAsString(dataController);
        } catch (JsonProcessingException ex) {
            System.err.println("Can not map data_controller to JSON.\n"
                    + "Details: " + ex.getMessage());
            dataControllerString = "{n.d.}";
        }
        
        String s = "\tuser_id: " + userId + ",\n"
            + "\tdescription: " + description + ",\n"
            + "\tdata_controller: " + dataControllerString + ",\n"
            + "\tconsent_id: " + consentId + ",\n"
            + "\tpurposes: " + purposeList + ",\n"
            + "\tpolicy_url: " + policyUrl +  ",\n"
            + "\tpolicy_version: " + policyVersion + "\n"
            + "\twithdrawn_date: " + ((withdrawnDate == null) ? "null" : withdrawnDate) + ",\n"
            + "\tinserted_date: " + insertedDate + ",\n"
            + "\tcollection_mode: " + collectionMode + ",\n"
            ;
        return "{\n" + s + "}\n";
    }

    /**
     * Get the id of the user who gave this Consent
     * @return a {@link String} that uniquely identifies a user.
     */
    @JsonProperty("user_id")
    public String getUserId() {
        return userId;
    }

    /**
     * Get the description of the Consent
     * @return a {@link String} with the description of the Consent
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }
    
    /**
     * Get informations about the subject who has been granted access
     * to the user's data
     * @return a {@link DataController} object
     */
    @JsonProperty("data_controller")
    public DataController getDataController() {
        return dataController;
    }

    /**
     * Get the id of this Consent object.
     * @return a {@link String} with this Consent's id
     */
    @JsonProperty("consent_id")
    public String getConsentId() {
        return consentId;
    }

    /**
     * Get informations about the purposes of the data collection.
     * @return a list of {@link DataCollectionPurpose} objects that describe
     * what the {@link #dataController data controller} is allowed to do with the user's data.
     */
    @JsonProperty("purposes")
    public List<DataCollectionPurpose> getPurposes() {
        return purposes;
    }

    /**
     * Get the URL of the policy
     * @return a {@link String} with the policy URL
     */
    @JsonProperty("policy_url")
    public String getPolicyUrl() {
        return policyUrl;
    }

    /**
     * Get the version of the policy
     * @return a {@link String} 
     */
    @JsonProperty("policy_version")
    public String getPolicyVersion() {
        return policyVersion;
    }

    /**
     * Get the date of withdrawal of this Consent
     * @return {@code null} if this Consent is {@link #isActive() active},
     * otherwise a {@link String} with the timestamp of the withdrawal
     * of this Consent.
     */
    @JsonProperty("withdrawn_date")
    public String getWithdrawnDate() {
        return withdrawnDate;
    }

    /**
     * Get the date of creation of this Consent
     * @return a {@link String} with the timestamp of the creation
     * of this Consent.
     */
    @JsonProperty("inserted_date")
    public String getInsertedDate() {
        return insertedDate;
    }

    /**
     * Get the collection mode of this Consent.
     * @return a {@link String} with a short description of the collection mode
     * of this Consent.
     */
    @JsonProperty("collection_mode")
    public String getCollectionMode() {
        return collectionMode;
    }
}
