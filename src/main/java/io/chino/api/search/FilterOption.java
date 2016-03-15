
package io.chino.api.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "field",
    "type",
    "value",
    "case_sensitive"
})
public class FilterOption {

	@JsonProperty("field")
    private String field;
    @JsonProperty("type")
    private String type;
    @JsonProperty("value")
    private Object value;
    @JsonProperty("case_sensitive")
    private Boolean caseSensitive;
    
    
    public FilterOption() {
	}
    
    public FilterOption(String field, String type, Object value, Boolean caseSensitive){
    	this.field=field;
    	this.type=type;
    	this.value=value;
    	this.caseSensitive=caseSensitive;
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

    /**
     * 
     * @return
     *     The caseSensitive
     */
    @JsonProperty("case_sensitive")
    public Boolean getCaseSensitive() {
        return caseSensitive;
    }

    /**
     * 
     * @param caseSensitive
     *     The case_sensitive
     */
    @JsonProperty("case_sensitive")
    public void setCaseSensitive(Boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

}
