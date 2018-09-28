
package io.chino.api.permission;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.chino.java.Permissions.Type;

import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "authorize",
        "manage"
})
public class PermissionRule {

    @JsonProperty("manage")
    HashSet<Type> manage = new HashSet<>();

    @JsonProperty("authorize")
    HashSet<Type> authorize = new HashSet<>();

    public PermissionRule() {
        super();
    }

    public PermissionRule(List<String> manage, List<String> authorize) {
        this();
        if (manage != null)
            this.manage = typeHashSet(manage);
        else
            this.manage = new HashSet<>();

        if (authorize != null)
            this.authorize = typeHashSet(authorize);
        else
            this.authorize = new HashSet<>();
    }

    public PermissionRule(String[] manage, String[] authorize) {
        this(
                (manage == null) ? null : Arrays.asList(manage),
                (authorize == null) ? null : Arrays.asList(authorize)
        );
    }

    @JsonProperty("manage")
    public List<String> getManage() {
        return stringList(manage);
    }

    public HashSet<Type> getManageTypes() {
        return manage;
    }

    /**
     * This method will be removed in the next SDK version.
     * Please use {@link PermissionSetter#manage(Type...)} instead
     */
    @Deprecated
    public void setManage(String... strings) {
        manage = typeHashSet(
                Arrays.asList(strings)
        );
    }

    void setManage(Type ... types) {
        manage = new HashSet<>(
                Arrays.asList(types)
        );
    }

    @JsonProperty("manage")
    public List<String> getAuthorize() {
        return stringList(authorize);
    }

    public HashSet<Type> getAuthorizeTypes() {
        return authorize;
    }

    /**
     * This method will be removed in the next SDK version.
     * Please use {@link PermissionSetter#authorize(Type...)} instead
     */
    @Deprecated
    public void setAuthorize(String... strings) {
        authorize = typeHashSet(
                Arrays.asList(strings)
        );
    }

    void setAuthorize(Type ... types) {
        authorize = new HashSet<>(
                Arrays.asList(types)
        );
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

    private static HashSet<Type> typeHashSet(List<String> stringList) {
        HashSet<Type> l = new HashSet<>();
        for (String s : stringList)
            l.add(Type.fromString(s));

        return l;
    }

    private static List<String> stringList(HashSet<Type> typeList) {
        LinkedList<String> l = new LinkedList<>();
        for (Type t : typeList)
            l.add(t.toString());

        return l;
    }
}
