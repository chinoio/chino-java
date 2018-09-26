package io.chino.api.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Objects;

/**
 * This class represents a field of a {@link io.chino.api.userschema.UserSchema UserSchema}
 * or a {@link io.chino.api.schema.Schema Schema}. It is used to create or update the structure
 * of those objects
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "type",
        "name",
        "indexed"
})
public class Field {

    @JsonProperty("type")
    private String type;
    @JsonProperty("name")
    private String name;
    @JsonProperty("indexed")
    private Boolean indexed;

    public Field(){
    }

    public Field(String name, String type){
        setName(name);
        setType(type);
    }

    public Field(String name, String type, Boolean indexed){
        setName(name);
        setType(type);
        setIndexed(indexed);
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
    public final void setType(String type) {
        if(type == null){
            throw new NullPointerException("type");
        }
        this.type = type;
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

    public Boolean getIndexed() {
        return indexed;
    }

    public final void setIndexed(Boolean indexed) {
        if(indexed == null){
            throw new NullPointerException("indexed");
        }
        this.indexed = indexed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return Objects.equals(type, field.type) &&
                Objects.equals(name, field.name) &&
                Objects.equals(indexed, field.indexed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, name, indexed);
    }
}
