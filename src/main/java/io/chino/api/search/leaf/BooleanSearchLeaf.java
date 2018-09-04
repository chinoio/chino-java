package io.chino.api.search.leaf;

import io.chino.api.search.FilterOperator;

public class BooleanSearchLeaf extends SearchLeaf<Boolean> {

    public BooleanSearchLeaf(String field, FilterOperator type, Boolean value) {
        super(field, type, value);
    }

    @Override
    public String parseJSON(int indentLevel) {
        return super.parseJSONWithValue(
                (value) ? "true" : "false",
                indentLevel
        );
    }
}
