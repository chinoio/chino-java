package io.chino.api.search;

/**
 * The type of result that will be returned by a search query.
 */
public enum ResultType {
    /**
     * Get the full content of each result along with the metadata
     */
    FULL_CONTENT,
    /**
     * Omit the content of the results and get only the metadata.
     */
    NO_CONTENT,
    /**
     * Get only the IDs of the search results
     */
    ONLY_ID,
    /**
     * Get only the amount of search results that match the query
     */
    COUNT,

    /**
     * Return an empty response containing only the field 'exists'. The value is {@code true}
     * if the search results for the given query contain at least one value, {@code false} otherwise.
     */
    EXISTS,
//    USERNAME_EXISTS

}
