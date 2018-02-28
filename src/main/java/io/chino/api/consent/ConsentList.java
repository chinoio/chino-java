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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;

/**
 * A list of {@link Consent} objects returned by an API call.
 * @author Andrea Arighi [andrea@chino.org]
 */
public class ConsentList extends ArrayList<Consent> {
    
    private int count, offset, limit, total;

    /**
     * Extracts and organizes the {@link Consent Consents}
     * and pagination data from a {@link ConsentListWrapper}.
     * @param wrapper the raw data (mapped using an {@link ObjectMapper})
     * from an API call results.
     */
    public ConsentList(ConsentListWrapper wrapper) {
        super(wrapper.getConsents());
        count = wrapper.count;
        offset = wrapper.offset;
        limit = wrapper.limit;
        total = wrapper.totalCount;
    }
    
    /**
     * Get the number of elements returned by the API call.
     * @return the {@code int} number of elements in this page.
     */
    public int getCount() {
        return count;
    }

    /**
     * Get the total number of results.
     * @return the number of result that can be found in all the result pages.
     */
    public int getTotalCount() {
        return total;
    }

    /**
     * Get the results limit.
     * @return the maximum number of results that could have been
     * returned by the API call.
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Get the page offset (first page is <b>0</b>)
     * @return the offset of the current page relative
     * to the first page of results.
     */
    public int getOffset() {
        return offset;
    }
    
    /**
     * Get the objects in this ConsentList as a {@link List}.
     * This method exists to adapt the interface of this class to the rest of this SDK.
     * However it is possible (and best practice) to use
     * {@link List} methods of the {@link ConsentList} class directly instead
     * of using method getConsents.
     * @return a new {@link List} containing all the objects of this {@link ConsentList}.
     */
    public List<Consent> getConsents() {
        return new ArrayList<>(this);
    }
    
    /**
     * Add to this list the data from the {@code wrapper} and overwrites the
     * pagination informations with the data contained in the wrapper
     * @param wrapper 
     */
    public void addAll(ConsentListWrapper wrapper) {
        this.addAll(new ConsentList(wrapper));
        count = wrapper.count;
        offset = wrapper.offset;
        limit = wrapper.limit;
        total = wrapper.totalCount;
    }
}
