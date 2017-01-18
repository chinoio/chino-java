
package io.chino.api.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "field",
    "type",
    "value"
})
public class FilterOption {

	@JsonProperty("field")
    private String field;
    @JsonProperty("type")
    private String type;
    @JsonProperty("value")
    private Object value;
    
    
    public FilterOption() {
	}
    
    public FilterOption(String field, String type, Object value){
    	this.field=field;
    	this.type=type;
    	this.value=value;
    }

    /**
     * 
     * @return
     *     The field
     */
    @JsonProperty("field")
    public String getField() {
        return field;
    }

    /**
     * 
     * @param field
     *     The field
     */
    @JsonProperty("field")
    public void setField(String field) {
        this.field = field;
    }

    /**
     * 
     * @return
     *     The type
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The value
     */
    @JsonProperty("value")
    public Object getValue() {
        return value;
    }

    /**
     * 
     * @param value
     *     The value
     */
    @JsonProperty("value")
    public void setValue(Object value) {
        this.value = value;
    }

}
