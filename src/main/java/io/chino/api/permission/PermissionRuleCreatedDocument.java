
package io.chino.api.permission;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "authorize",
        "manage",
        "created_document"
})
public class PermissionRuleCreatedDocument extends PermissionRule{

    @JsonProperty("created_document")
    private PermissionRule createdDocument = null;

    public PermissionRule getCreatedDocument() {
        return createdDocument;
    }

    public void setCreatedDocument(PermissionRule createdDocument) {
        if(createdDocument == null){
            throw new NullPointerException("created_document");
        }
        this.createdDocument = createdDocument;
    }

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
