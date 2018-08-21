
package io.chino.api.search;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Objects;

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
		setField(field);
		setOrder(order);
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
        if(field == null){
            throw new NullPointerException("field");
        }
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
        if(order == null){
            throw new NullPointerException("order");
        }
        this.order = order;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof SortOption) {
            return false;
        }
        SortOption other = (SortOption) obj;
        return other.field.equals(this.field)
                && other.order.equals(this.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(field, order);
    }
}
