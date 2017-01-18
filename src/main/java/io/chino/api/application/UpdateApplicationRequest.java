package io.chino.api.application;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "name", "grant_type"})

public class UpdateApplicationRequest {
    @JsonProperty("name")
    private String name;
    @JsonProperty("grant_type")
    private String grantType;

    /**
     *
     * @return The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     *            The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return The grantType
     */
    @JsonProperty("grant_type")
    public String getGrantType() {
        return grantType;
    }

    /**
     *
     * @param grantType
     *            The grantType
     */
    @JsonProperty("grant_type")
    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }
}
