
package io.chino.api.permission;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "authorize",
        "manage"
})
public class PermissionRule {

    @JsonProperty("manage")
    private List<String> manage = new ArrayList<String>();
    @JsonProperty("authorize")
    private List<String> authorize = new ArrayList<String>();

    public List<String> getManage() {
        return manage;
    }

    public void setManage(String... strings) {
        if(strings == null){
            throw new NullPointerException("manage");
        }
        manage = new ArrayList<String>();
        Collections.addAll(manage, strings);
    }

    public List<String> getAuthorize() {
        return authorize;
    }

    public void setAuthorize(String... strings) {
        if(strings == null){
            throw new NullPointerException("authorize");
        }
        authorize = new ArrayList<String>();
        Collections.addAll(authorize, strings);
    }

    public String toString(){
        String s="{";
        if(manage!=null)
            s+="\"manage\": "+manage.toString()+",";
        if(authorize!=null)
            s+="\"authorize\": "+authorize.toString()+",";
        s = s.substring(0, s.length()-1);
        s+="}";
        return s;
    }
}
