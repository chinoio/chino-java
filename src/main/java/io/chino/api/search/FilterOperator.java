package io.chino.api.search;

/**
 * The operators that can be used to compare field values in {@link io.chino.java.Search Search}. <br>
 * Values:
 * <ul>
 *     <li>{@link #EQUALS}</li>
 *     <li>{@link #GREATER_EQUAL}</li>
 *     <li>{@link #GREATER_THAN}</li>
 *     <li>{@link #IN}</li>
 *     <li>{@link #IS}</li>
 *     <li>{@link #LIKE}</li>
 *     <li>{@link #LOWER_EQUAL}</li>
 *     <li>{@link #LOWER_THAN}</li>
 * </ul>
 *
 * @see SearchQueryBuilder
 */
public enum FilterOperator {
    /** a == b */
    EQUALS,

    /** a >= b */
    GREATER_EQUAL,

    /** a > b */
    GREATER_THAN,

    /** a is in [a, b] */
    IN,

    /** same as {@link #EQUALS} but only for {@code boolean} values */
    IS,

    /**
     * same as {@link #EQUALS}, but only for {@link String} values. Allows to use the following wildcards:
     * <ul>
     *     <li>{@code ?} -> matches any character</li>
     *     <li>{@code *} -> matches any String, including the empty one</li>
     * </ul>
     */
    LIKE,

    /** a <= b */
    LOWER_EQUAL,

    /** a < b */
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
