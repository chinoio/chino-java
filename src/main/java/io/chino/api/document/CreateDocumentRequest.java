
package io.chino.api.document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.HashMap;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "content"
})
public class CreateDocumentRequest {

    @JsonProperty("content")
    private HashMap content;

    /**
     * 
     * @return
     *     The content
     */
    @JsonProperty("content")
    public HashMap getContent() {
        return content;
    }

    /**
     * 
     * @param content
     *     The content
     */
    @JsonProperty("content")
    public void setContent(HashMap content) {
        this.content = content;
    }

}
