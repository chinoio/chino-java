
package io.chino.api.permission;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "authorize",
        "manage"
})
public class PermissionRule {

    @JsonProperty("manage")
    private List<String> manage = new LinkedList<>();

    @JsonProperty("authorize")
    private List<String> authorize = new LinkedList<>();

    public PermissionRule() {
        super();
    }

    public PermissionRule(List<String> manage, List<String> authorize) {
        this();
        if (manage != null)
            this.manage = new LinkedList<>(manage);
        else
            this.manage = new LinkedList<>();

        if (authorize != null)
            this.authorize = new LinkedList<>(authorize);
        else
            this.authorize = new LinkedList<>();
    }

    public PermissionRule(String[] manage, String[] authorize) {
        this(
                (manage == null) ? null : Arrays.asList(manage),
                (authorize == null) ? null : Arrays.asList(authorize)
        );
    }

    public List<String> getManage() {
        return manage;
    }

    public void setManage(String... strings) {
        manage = new LinkedList<>();
        if(strings != null) {
            Collections.addAll(manage, strings);
        }
    }

    public List<String> getAuthorize() {
        return authorize;
    }

    public void setAuthorize(String... strings) {
        authorize = new LinkedList<>();
        if(strings != null){
            Collections.addAll(authorize, strings);
        }
    }

    @Override
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
