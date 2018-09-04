package io.chino.api.search.leaf;

import io.chino.api.search.FilterOperator;

public final class IntegerSearchLeaf extends SearchLeaf<Integer> {

    public IntegerSearchLeaf(String field, FilterOperator type, Integer value) {
        super(field, type, value);
    }

    @Override
    public String parseJSON(int indentLevel) {
        return super.parseJSONWithValue(value + "", indentLevel);
    }
}
