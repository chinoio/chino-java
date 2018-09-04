package io.chino.api.search;

public enum FilterOperator {
    EQUALS,
    GREATER_EQUAL,
    GREATER_THAN,
    IN,
    IS,
    LIKE,
    LOWER_EQUAL,
    LOWER_THAN;


    public String toJSON() {
        switch (this) {
            case EQUALS:
                return "eq";
            case LOWER_THAN:
                return "lt";
            case LOWER_EQUAL:
                return "lte";
            case GREATER_THAN:
                return "gt";
            case GREATER_EQUAL:
                return "gte";
            case IS:
            case IN:
            case LIKE:
                return this.name().toLowerCase();
        }

        throw new UnsupportedOperationException(this.name() + " is not a valid FilterOperator");
    }

    public String toString() {
        switch (this) {
            case EQUALS:
                return "=";
            case LOWER_THAN:
                return "<";
            case LOWER_EQUAL:
                return "<=";
            case GREATER_THAN:
                return ">";
            case GREATER_EQUAL:
                return ">=";
            case LIKE:
                return "matches";
            case IS:
            case IN:
                return this.name().toLowerCase();
        }

        throw new UnsupportedOperationException(this.name() + " is not a valid FilterOperator");
    }
}
