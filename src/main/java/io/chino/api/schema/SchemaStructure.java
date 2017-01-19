
package io.chino.api.schema;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.chino.api.common.Field;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "fields"
})
public class SchemaStructure {

    @JsonProperty("fields")
    private List<Field> fields = new ArrayList<Field>();
   
    /**
     * 
     * @return
     *     The fields
     */
    @JsonProperty("fields")
    public List<Field> getFields() {
        return fields;
    }

    /**
     * 
     * @param fields
     *     The fields
     */
    @JsonProperty("fields")
    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

}
