
package io.chino.api.userschema;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.chino.api.common.Field;

/**
 * Defines the structure of a {@link UserSchema}, i.e. the name and the data type of each field.
 *
 * @see Field
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "fields"
})
public class UserSchemaStructure {

    @JsonProperty("fields")
    private List<Field> fields = new ArrayList<>();

    public UserSchemaStructure(){

    }

    public UserSchemaStructure(List<Field> fields){
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
        UserSchemaStructure that = (UserSchemaStructure) o;
        return Objects.equals(fields, that.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fields);
    }
}
