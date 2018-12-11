package io.chino.api.application;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Wraps an {@link Application} returned as a response to an API call
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
        {
                "application"
        }
)
public class GetApplicationResponse {

    @JsonProperty("application")
    private Application application;

    /**
     *
     * @return The metadata of the {@link Application}
     */
    @JsonProperty("application")
    public Application getApplication() {
        return application;
    }

    /**
     * init the content of this object with a new {@link Application}.
     *
     * @param application
     */
    @JsonProperty("application")
    public void setApplication(Application application) {
        this.application = application;
    }
}
