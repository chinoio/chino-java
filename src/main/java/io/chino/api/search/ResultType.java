package io.chino.api.search;

/**
 * The type of result that will be returned by a search query
 */
public enum ResultType {
    /**
     * Get the full content of each resource (e.g. {@link io.chino.api.document.Document Document})
     * along with metadata
     */
    FULL_CONTENT,
    /**
     * Omit the resource's content and get only the metadata.
     */
    NO_CONTENT,
    /**
     * Get only the resource's ID
     */
    ONLY_ID,
    /**
     * Get only the amount of search results
     */
    COUNT,

//    TODO check why the following don't work
//    EXISTS,
//    USERNAME_EXISTS

}
