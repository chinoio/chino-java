package io.chino.api.search.leaf;

import io.chino.api.search.FilterOperator;

public class StringSearchLeaf extends SearchLeaf<String> {

    public StringSearchLeaf(String field, FilterOperator type, String value) {
        super(field, type, value);
    }

    @Override
    public String parseJSON(int indentLevel) {
        if (value == null)
            return parseJSONWithValue("null", indentLevel);
        return parseJSONWithValue("\"" + value + "\"", indentLevel);
    }
}
