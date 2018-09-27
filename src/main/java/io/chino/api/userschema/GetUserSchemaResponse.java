
package io.chino.api.userschema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Wraps a {@link UserSchema} returned as a response to an API call
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "result",
    "user_schema"
})
public class GetUserSchemaResponse {

    @JsonProperty("result")
    private String result;
    @JsonProperty("user_schema")
    private UserSchema userSchema;

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

	public UserSchema getUserSchema() {
		return userSchema;
	}

	public void setUserSchema(UserSchema userSchema) {
		this.userSchema = userSchema;
	}

   

}
