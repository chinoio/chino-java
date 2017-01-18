
package io.chino.api.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "field",
    "order"
})
public class SortOption {

    @JsonProperty("field")
    private String field;
    @JsonProperty("order")
    private String order;
    
    public SortOption(){
    	
    }
    
	public SortOption(String field, String order){
		this.field=field;
		this.order=order;
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
     *     The order
     */
    @JsonProperty("order")
    public String getOrder() {
        return order;
    }

    /**
     * 
     * @param order
     *     The order
     */
    @JsonProperty("order")
    public void setOrder(String order) {
        this.order = order;
    }
}
