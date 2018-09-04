package io.chino.api.search.leaf;


import io.chino.api.search.FilterOperator;
import io.chino.api.search.SearchTreeNode;

import static io.chino.api.search.SearchTreeNode.indent;

/**
 * Abstract class that represents a leaf in the search tree. Leaves contain
 * a Field name, a {@link FilterOperator} and a Value; the Value will be compared
 * to the one contained in the Field using the specified operator during the search operation on Chino.io.
 *
 * @param <ValueType> the class of the Value. There is a subclass of {@link SearchLeaf} for all the Field types
 *                   accepted by Chino.io.
 */
public abstract class SearchLeaf<ValueType extends Object> implements SearchTreeNode {

    protected String field;
    protected FilterOperator type;
    protected ValueType value;

    private boolean supportListOperators = false;

    SearchLeaf (String field, FilterOperator type, ValueType value) {
        this.field = field;
        this.type = type;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public FilterOperator getType() {
        return type;
    }

    public void setType(FilterOperator type) {
        this.type = type;
    }

    public ValueType getValue() {
        return value;
    }

    public void setValue(ValueType value) {
        this.value = value;
    }

    @Override
    public StringBuilder getString() {
        StringBuilder sb = new StringBuilder("{");

        if (field == null || type == null) {
            sb.append("ERROR! ")
            .append(
                    (field == null) ? "'field'"
                    : "'type'"
            )
            .append(" can't be 'null'");
        } else {
            sb.append(field).append(" ")
                    .append(type.toString()).append(" ")
                    .append(value.toString());
        }

        return sb.append("}");
    }

    protected String parseJSONWithValue(String valueString, int indentLevel) {
        StringBuilder sb = new StringBuilder().append("{\n");

        indent(sb, indentLevel).append("\"field\": ").append("\"").append(field).append("\",\n");
        indent(sb, indentLevel).append("\"type\": ").append("\"").append(type.toJSON()).append("\",\n");

        // write custom value
        indent(sb, indentLevel).append("\"value\": ")
                .append(valueString)
                .append("\n");

        return indent(sb, indentLevel - 1).append("}\n").toString();
    }
}
