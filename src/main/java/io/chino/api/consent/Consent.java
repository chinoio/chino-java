package io.chino.api.consent;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This class represents a user's consent to a policy.
 * @author Andrea Arighi andrea@chino.io
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

    public Consent() {
        throw new UnsupportedOperationException("To be implemented");
    }
}
