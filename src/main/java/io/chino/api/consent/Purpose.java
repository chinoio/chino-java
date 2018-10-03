package io.chino.api.consent;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;

/**
 * Contains information about the purpose of personal data collection.
 * @see Consent#purposes
 * @author Andrea Arighi [andrea@chino.io]
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
    
    /**
     * Empty constructor used by {@link ObjectMapper} to create
     * JSON objects from this class.
     */
    private Purpose() {
        super();
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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Purpose))
            return false;
        
        Purpose p = (Purpose) obj;
        return (this.authorized == p.authorized) &&
                this.description.equals(p.description) &&
                this.purpose.equals(p.purpose);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + (this.authorized ? 1 : 0);
        hash = 37 * hash + Objects.hashCode(this.purpose);
        hash = 37 * hash + Objects.hashCode(this.description);
        return hash;
    }
}
