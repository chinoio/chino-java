package io.chino.java.testutils;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.chino.api.common.indexed;

public class MappedDocument {
    @JsonProperty("testMethod")
    @indexed
    public String testMethod;
}
