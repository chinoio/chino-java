package io.chino.api.search.leaf;

import io.chino.api.search.FilterOperator;

public class FloatSearchLeaf extends SearchLeaf<Float> {

    public FloatSearchLeaf(String field, FilterOperator type, Float value) {
        super(field, type, value);
    }

    @Override
    public String parseJSON(int indentLevel) {
        return super.parseJSONWithValue(getValue() + "", indentLevel);
    }
}
