/*
 * The MIT License
 *
 * Copyright 2018 Andrea Arighi <andrea@chino.org>.
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

/**
 * Contains informations about the purpose of personal data collection.
 * @see Consent#purposes
 * @author Andrea Arighi [andrea@chino.org]
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
    "authorized",
    "purpose",
    "description"
})
public class Purpose {
    
    /**
     * Indicates whether or not the user gave the authorization to
     * this purpose of data collection.
     */
    @JsonProperty("authorized")
    private boolean authorized;
    
    /**
     * A short {@link String} that describes a purpose for the data collection.
     */
    @JsonProperty("purpose")
    private String purpose;
    
    
    /**
     * A text description which further explain the {@link #purpose purpose}
     * of the data collection.
     */
    @JsonProperty("description")
    private String description;

    public Purpose(boolean authorized, String purpose, String description) {
        this.authorized = authorized;
        this.purpose = purpose;
        this.description = description;
    }
    

    @Override
    public String toString() {
        return toString(0);
    }
    
    /**
     * Gat an indented {@link String}.
     * @param indentLevel the indentation level, e.g. the nr of "tab" characters
     * to be inserted before each line.
     * @return the same result given by {@link #toString() toString()},
     * indented by the specified quantity. <b>The first line has no indentation</b>.
     */
    public String toString(int indentLevel) {
        if (indentLevel < 0) {
            throw new IllegalArgumentException("Indentation level can't be negative.");
        }
        
        String indentation = "";
        for (int i = 0; i < indentLevel; i++)
            indentation += "\t";
        
        return "{\n"
                + indentation + "\tauthorized: " + authorized + ",\n"
                + indentation + "\tpurpose: " + purpose + ",\n"
                + indentation + "\tdescription: " + description + ",\n"
                + indentation + "}";
    }

    /**
     * Check whether or not this purpose was authorized by a user.
     * @return {@code true} if the user authorized the collection of
     * their data for this purpose, otherwise {@code false}.
     */
    public boolean isAuthorized() {
        return authorized;
    }

    /**
     * Get the name of the purpose
     * @return a short {@link String} that describes a purpose for
     * the usage of personal data.
     */
    public String getPurpose() {
        return purpose;
    }

    /**
     * Get the purpose description
     * @return a {@link String} that further explains how the personal
     * data will be used.
     */
    public String getDescription() {
        return description;
    }
    
    
}
