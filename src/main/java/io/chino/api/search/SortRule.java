package io.chino.api.search;

import static io.chino.api.search.SearchTreeNode.indent;

public class SortRule {

    public enum Order {
        ASC,
        DESC;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

    private String fieldName;
    private Order order;

    SortRule(String field, Order order) {
        this.fieldName = field;
        this.order = order;
    }

    public StringBuilder toJSONString(int indentLevel) {
        StringBuilder sb = indent(new StringBuilder(), indentLevel).append("{\n");
        indent(sb, indentLevel)
                .append("\t")
                .append("\"field\": ").append("\"").append(fieldName).append("\",\n");
        indent(sb, indentLevel)
                .append("\t")
                .append("\"order\": ").append("\"").append(order.toString()).append("\"\n");
        indent(sb, indentLevel)
                .append("}");

        return sb;
    }
}
