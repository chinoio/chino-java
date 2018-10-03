
package io.chino.api.schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.chino.api.common.Field;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Defines the structure of a {@link Schema}, i.e. the name and the data type of each field.
 *
 * @see Field
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "fields"
})
public class SchemaStructure {

    @JsonProperty("fields")
    private List<Field> fields = new ArrayList<>();

    public SchemaStructure(){}

    public SchemaStructure(List<Field> fields){
        setFields(fields);
    }
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
        if(fields == null){
            throw new NullPointerException("fields");
        }
        this.fields = fields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SchemaStructure that = (SchemaStructure) o;
        return Objects.equals(fields, that.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fields);
    }
}
