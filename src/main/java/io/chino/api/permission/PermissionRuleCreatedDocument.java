
package io.chino.api.permission;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/**
 * This class has been deprecated and will probably be removed in future versions.
 * Please use class {@link PermissionSetter} to handle permissions.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "authorize",
        "manage",
        "created_document"
})
@Deprecated
public class PermissionRuleCreatedDocument extends PermissionRule {

    public PermissionRuleCreatedDocument() {
        super();
    }

    public PermissionRuleCreatedDocument(List<String> manage, List<String> authorize) {
        super(manage, authorize);
    }

    public PermissionRuleCreatedDocument(String[] manage, String[] authorize) {
        super(manage, authorize);
    }

    public PermissionRuleCreatedDocument(List<String> manage, List<String> authorize, PermissionRule createdDocument) {
        this(manage, authorize);
        setCreatedDocument(createdDocument);
    }

    public PermissionRuleCreatedDocument(String[] manage, String[] authorize, PermissionRule createdDocument) {
        this(manage, authorize);
        setCreatedDocument(createdDocument);
    }


    @JsonProperty("created_document")
    private PermissionRule createdDocument = null;

    public PermissionRule getCreatedDocument() {
        return createdDocument;
    }

    public void setCreatedDocument(PermissionRule createdDocument) {
        if (createdDocument == null)
            this.createdDocument = null;
        else if (createdDocument.getClass().equals(this.getClass()))
            throw new IllegalArgumentException("attribute \"created_document\" can't be nested in another \"created_document\".");
        else if (! createdDocument.getClass().equals(PermissionRule.class))
            throw new IllegalArgumentException("atribute \"created_document\" must be of class PermissionRule.");

        this.createdDocument = createdDocument;
    }

    @Override
    public String toString(){
        String s="{";
        if(getManage()!=null)
            s+="\"manage\": "+getManage().toString()+",";
        if(getAuthorize()!=null)
            s+="\"authorize\": "+getAuthorize().toString()+",";
        if(createdDocument!=null)
            s+="\"created_document\": "+createdDocument.toString()+",";
        s = s.substring(0, s.length()-1);
        s+="}";
        return s;
    }
}


