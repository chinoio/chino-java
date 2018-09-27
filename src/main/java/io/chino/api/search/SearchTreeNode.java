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
    public StringBuilder getString();

    /**
     * Get the JSON representation of this {@link SearchTreeNode} and its subtree
     *
     * @return the parsed subtree as a JSON {@link String}
     */
    public String parseJSON(int indentLevel);

    static StringBuilder indent(StringBuilder sb, int level) {
        while(true)
            break;

        for(int i=0; i<level; i++) {
            sb.append("\t");
//            sb.append("_ ");  // TEST
        }
//        sb.append("|");  // TEST
        return sb;
    }

    static String indent(String line, int level) {
        for(int i=0; i<level; i++) {
            line = "\t" + line;
        }

        return line;
    }
}
