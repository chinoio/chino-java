package io.chino.api.search;

/**
 * Defines how the results of a {@link io.chino.java.Search Search} must be ordered.
 */
public class SortRule {

    public enum Order {
        /**  ascending order */ ASC,
        /** descending order */ DESC;

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

    private static StringBuilder indent(StringBuilder sb, int level) {
        for(int i=0; i<level; i++) {
            sb.append("\t");
        }
        return sb;
    }
}
