package io.chino.api.search;


/**
 *  Basic element of a {@link io.chino.java.Search Search} query tree
 */
public interface SearchTreeNode {

    /**
     * Get a String representation of this {@link SearchTreeNode} and all the child nodes (if any)
     *
     * @return a {@link StringBuilder} that contains a String representation of the subtree that starts from this {@link SearchTreeNode}
     */
    StringBuilder getString();

    /**
     * Get the JSON representation of this {@link SearchTreeNode} and its subtree
     *
     * @return the parsed subtree as a JSON {@link String}
     */
    String parseJSON(int indentLevel);
}
