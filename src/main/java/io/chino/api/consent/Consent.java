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
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
     * A list of {@link Purpose} objects, which represent
     * how the {@link #dataController data controller} was allowed to use the user's data
     * by the user itself.
     */
    @JsonProperty("purposes")
    private List<Purpose> purposes;
    
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
    private Date withdrawnDate = null;
    
    /**
     * Timestamp of creation of this Consent.
     */
    @JsonProperty("inserted_date")
    private Date insertedDate;
    
    /**
     * Brief {@link String} which describes how the consent was collected.
     */
    @JsonProperty("collection_mode")
    private String collectionMode;

    Consent() {
        super();
    }
    
    
    /**
     * Creates a new {@link Consent} and initializes its fields.
     * Check
     * <a href="https://docs.chino.io//#consent-management">Chino.io API documentation</a>
     * to learn more about the parameters of the Consent Object.
     * @param userId
     * @param description
     * @param policyUrl
     * @param policyVersion
     * @param collectionMode
     * @param dataController
     * @param purposes 
     */
    public Consent(String userId, String description, String policyUrl, String policyVersion,
            String collectionMode, DataController dataController,
            List<Purpose> purposes)
    {
        this.userId = userId;
        this.description = description;
        this.policyUrl = policyUrl;
        this.policyVersion = policyVersion;
        this.collectionMode = collectionMode;
        this.dataController = dataController;
        this.purposes = purposes;
        
        // JsonProperty fields with 'null' values will be ignored
        // because of JsonInclude.Include.NON_NULL
        consentId = null;
        insertedDate = null;
        withdrawnDate = null;
    }
    
    /**
     * Creates a new {@link Consent} and initializes its fields like the fields in
     * {@code base}, except for the {@link #dataController dataController} and the
     * {@link #purposes purposes list}. Useful when users give consent to new
     * purposes or grant access to their data to other subjects.
     * @param base the base {@link Consent} object.
     * @param newDataController the new {@link DataController};
     * if {@code null}, the value will be copied from {@code base}.
     * @param newPurposes the new list of {@link Purpose} objects;
     * if {@code null}, the value will be copied from {@code base}.
     */
    public Consent(Consent base, DataController newDataController, List<Purpose> newPurposes) {
        this(base.userId, base.description, base.policyUrl, base.policyVersion, base.collectionMode,
                (newDataController != null) ? newDataController : base.dataController,
                (newPurposes != null) ? newPurposes : base.purposes
        );
    }
    
    /**
     * Creates a new Consent which is identical to {@code base}, except for the
     * {@link #userId userId}. Useful when consent to the same policy
     * is asked to many users.
     * @param base the base {@link Consent} object.
     * @param userId the new {@link #userId userId}. <b>Can not be {@code null}</b>
     */
    public Consent(Consent base, String userId) {
        this(userId,
                base.description, base.policyUrl, base.policyVersion,
                base.collectionMode, base.dataController, base.purposes
        );
    }
    
    /**
     * Check if this Consent is still active, i.e. if its
     * {@link #withdrawnDate withdrawn_date} is null.
     * @return true if the value of {@link #withdrawnDate withdrawn_date}
     * has not been set; false otherwise.
     */
    public boolean isActive() {
        return (withdrawnDate == null);
    }

    @Override
    public String toString() {
        
        String purposesString = "[\n";
        for (Purpose purp:getPurposes()) {
            purposesString += "\t\t" + purp.toString(2) + "\n";
        }
        purposesString += "\t]";
        
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
            + "\tpurposes: " + purposesString + ",\n"
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
        return ChinoBaseAPI.getMapper().convertValue(dataController, DataController.class);
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
     * @return a list of {@link Purpose} objects that describe
     * what the {@link #dataController data controller} is allowed to do with the user's data.
     */
    @JsonProperty("purposes")
    public List<Purpose> getPurposes() {
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
     * otherwise the {@link Date} of the withdrawal
     * of this Consent.
     */
    @JsonProperty("withdrawn_date")
    public Date getWithdrawnDate() {
        return withdrawnDate;
    }

    /**
     * Get the date of creation of this Consent
     * @return the {@link Date} of creation
     * of this Consent.
     */
    @JsonProperty("inserted_date")
    public Date getInsertedDate() {
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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Consent) {
            Consent c = (Consent) obj;
            return (c.consentId == this.consentId && c.insertedDate == this.insertedDate);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.consentId);
        hash = 89 * hash + Objects.hashCode(this.insertedDate);
        return hash;
    }
}
