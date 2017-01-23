package io.chino.api.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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
    public void setType(String type) {
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
    public void setName(String name) {
        if(name == null){
            throw new NullPointerException("name");
        }
        this.name = name;
    }

    public Boolean getIndexed() {
        return indexed;
    }

    public void setIndexed(Boolean indexed) {
        if(indexed == null){
            throw new NullPointerException("indexed");
        }
        this.indexed = indexed;
    }
}
