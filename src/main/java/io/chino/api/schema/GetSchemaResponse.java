
package io.chino.api.schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Wraps an {@link Schema} returned as a response to an API call
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "result",
    "schema"
})
public class GetSchemaResponse {

    @JsonProperty("result")
    private String result;
    @JsonProperty("schema")
    private Schema schema;

    /**
     * 
     * @return
     *     The result
     */
    @JsonProperty("result")
    public String getResult() {
        return result;
    }

    /**
     * 
     * @param result
     *     The result
     */
    @JsonProperty("result")
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * 
     * @return
     *     The schema
     */
    @JsonProperty("schema")
    public Schema getSchema() {
        return schema;
    }

    /**
     * 
     * @param schema
     *     The schema
     */
    @JsonProperty("schema")
    public void setSchema(Schema schema) {
        this.schema = schema;
    }

}
