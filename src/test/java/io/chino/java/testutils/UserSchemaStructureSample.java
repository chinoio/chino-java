package io.chino.java.testutils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserSchemaStructureSample {

    @JsonProperty("test_integer")
    public int test_integer;
    @JsonProperty("test_string")
    public String test_string;
    @JsonProperty("test_boolean")
    public boolean test_boolean;
    @JsonProperty("test_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY-MM-dd")
    public Date test_date;
    @JsonProperty("test_float")
    public float test_float;

    private UserSchemaStructureSample() {
    }

    public UserSchemaStructureSample(String testName) {
        String suffix = (testName == null || testName.isEmpty()) ? "" : " - " + testName;

        this.test_boolean = true;
        this.test_date = new Date();
        this.test_float = (float) 0.987;
        this.test_integer = 1234;
        this.test_string = "test String" + suffix;
    }

    public UserSchemaStructureSample(UserSchemaStructureSample copyFrom) {
        this.test_boolean = copyFrom.test_boolean;
        this.test_date = copyFrom.test_date;
        this.test_float = copyFrom.test_float;
        this.test_integer = copyFrom.test_integer;
        this.test_string = copyFrom.test_string;
    }
}
