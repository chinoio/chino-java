package io.chino.api.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.chino.api.collection.Collection;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
        {
                "collection"
        }
)
public class GetCollectionResponse {

    @JsonProperty("collection")
    private Collection collection;

    /**
     *
     * @return The collection
     */
    @JsonProperty("collection")
    public Collection getCollection() {
        return collection;
    }

    /**
     *
     * @param collection
     * The collection
     */
    @JsonProperty("collection")
    public void setCollection(Collection collection) {
        this.collection = collection;
    }
}
