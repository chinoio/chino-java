package io.chino.api.consent;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;

/**
 * Contains information about a subject that collects sensitive data.
 * @see Consent
 * @author Andrea Arighi [andrea@chino.io]
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
@JsonPropertyOrder({
        "company",
        "contact",
        "address",
        "email",
        "VAT",
        "on_behalf",
})
public class DataController {
    
    /**
     * Name of the company which performs data collection.
     */
    @JsonProperty("company")
    private String company;
    
    /**
     * Name of the person who is collecting data for the {@link #company company}.
     */
    @JsonProperty("contact")
    private String contact;
    
    /**
     * Address of the subject who is performing data collection.
     */
    @JsonProperty("address")
    private String address;
    
    /**
     * Email address to be used for communications regarding the personal
     * data collection performed by the {@link #company company}.
     */
    @JsonProperty("email")
    private String email;
    
    /**
     * The VAT number of the subject that performs data collection.
     */
    @JsonProperty("VAT")
    private String vat;
    
    /**
     * True if the consent was collected in behalf of this data controller
     * by another data controller.
     */
    @JsonProperty("on_behalf")
    private boolean onBehalf;
    
    
    /**
     * Create and initializes a new {@link DataController}.
     * Check
     * <a href="https://docs.chino.io//#consent-management">Chino.io API documentation</a>
     * to learn more about the parameters of the "data_controller" object
     * in a Consent.
     * @param company
     * @param contact
     * @param address
     * @param email
     * @param VAT
     * @param onBehalf 
     */
    public DataController(String company, String contact, String address,
            String email, String VAT, boolean onBehalf)
    {
        this.company = company;
        this.contact = contact;
        this.address = address;
        this.email = email;
        vat = VAT;
        this.onBehalf = onBehalf;
    }
    
    /**
     * Empty constructor used by {@link ObjectMapper} to create
     * JSON objects from this class.
     */
    private DataController() {
        super();
    }
    
    /**
     * Get the name of the company which performs data collection.
     * @return a {@link String} with the name of the data controller
     */
    public String getCompany() {
        return company;
    }

    /**
     * Get the name of the person who is collecting data for the {@link #company company}
     * @return a {@link String} with the name of the contact person.
     */
    public String getContact() {
        return contact;
    }

    /**
     * Get the address of the subject who is performing data collection.
     * @return a {@link String} with the address of the data controller.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Get the email address to be used for communications regarding the personal
     * data collection.
     * @return an email address as a {@link String}.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Get the VAT number of the subject that performs data collection.
     * @return the VAT number of the data controller.
     */
    public String getVAT() {
        return vat;
    }

    /**
     * Check if the data collection was made on behalf of a data controller.
     * @return true if the data collection was performed by another data controller
     * on behalf of the subject represented here.
     */
    public boolean isOnBehalf() {
        return onBehalf;
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
                + indentation + "\tcompany: " + company + ",\n"
                + indentation + "\tcontact: " + contact + ",\n"
                + indentation + "\taddress: " + address + ",\n"
                + indentation + "\temail: " + email + ",\n"
                + indentation + "\tVAT: " + vat + ",\n"
                + indentation + "\ton_behalf: " + onBehalf + ",\n"
                + indentation + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof DataController))
            return false;
        
        DataController dc = (DataController) obj;
        return this.address.equals(dc.address) &&
                this.company.equals(dc.company) &&
                this.contact.equals(dc.contact)&&
                this.email.equals(dc.email) &&
                this.vat.equals(dc.vat) &&
                (this.onBehalf == dc.onBehalf);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.company);
        hash = 89 * hash + Objects.hashCode(this.contact);
        hash = 89 * hash + Objects.hashCode(this.address);
        hash = 89 * hash + Objects.hashCode(this.email);
        hash = 89 * hash + Objects.hashCode(this.vat);
        hash = 89 * hash + (this.onBehalf ? 1 : 0);
        return hash;
    }
    
    
}
