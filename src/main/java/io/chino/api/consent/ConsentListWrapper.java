package io.chino.api.consent;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that contains the raw results of an API call which returns a list.
 * This is just a container: to actually work with the data use {@link ConsentList}.
 * @author Andrea Arighi [andrea@chino.io]
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "count",
    "total_count",
    "limit",
    "offset",
    "consents"
})
public class ConsentListWrapper {
    @JsonProperty("count")
    Integer count;
    @JsonProperty("total_count")
    Integer totalCount;
    @JsonProperty("limit")
    Integer limit;
    @JsonProperty("offset")
    Integer offset;
    @JsonProperty("consents")
    List<Consent> consents = new ArrayList<>();
    
    public List<Consent> getConsents() {
        return new ArrayList<>(consents);
    }
    
    @Override
    public String toString() {
        return "{\n"
                + "\tcount: " + count +",\n"
                + "\ttotal_count: "+ totalCount + ",\n"
                + "\tlimit: " + limit + ",\n"
                + "\toffset: " + offset + ",\n"
            + "}\n";
    }
    
}
