
package io.chino.api.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
})
public class CreateCollectionRequest {

    @JsonProperty("name")
    private String name;

    public CreateCollectionRequest(){

    }

    public CreateCollectionRequest(String name){
        setName(name);
    }

    /**
     *
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public final void setName(String name) {
        if(name == null){
            throw new NullPointerException("name");
        }
        this.name = name;
    }

}
