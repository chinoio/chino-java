/*
 * The MIT License
 *
 * Copyright 2018 Andrea Arighi [andrea@chino.org].
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.chino.api.consent;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that contains the raw results of an API call which returns a list.
 * This is just a container: to actually work with the data use {@link ConsentList}.
 * @author Andrea Arighi [andrea@chino.org]
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
