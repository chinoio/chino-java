package io.chino.java.testutils;

import io.chino.api.common.indexed;

import java.util.HashMap;

public class BasicUser {
    @indexed
    String name;

    public BasicUser(String name) {this.name = name;}

    public HashMap<String, Object> toMap() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        return map;
    }
}
