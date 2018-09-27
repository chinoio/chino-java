package io.chino.api.search;

import com.sun.javafx.UnmodifiableArrayList;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import static io.chino.api.search.SearchTreeNode.indent;

/**
 * Implementation of {@link SearchTreeNode} that expresses a condition (AND, OR, NOT)
 * between nodes of a {@link io.chino.java.Search Search} query tree
 *
 * @see And
 * @see Or
 * @see Not
 */
abstract class SearchCondition implements SearchTreeNode {

    protected final LinkedList<SearchTreeNode> childTreeNodes;
    protected final String operator;

    protected SearchCondition(String operatorText) {
        childTreeNodes = new LinkedList<>();
        operator = operatorText;
    }

    protected SearchCondition(String operatorText, Collection<SearchTreeNode> clauses) {
        this(operatorText);

        if (clauses.size() == 0)
            return;

        Iterator<SearchTreeNode> it = clauses.iterator();
        while (it.hasNext()) {
            addChild(it.next());
        }
    }

    @Override
    public StringBuilder getString() {
        StringBuilder sb = new StringBuilder("(");
        ListIterator<SearchTreeNode> it = childTreeNodes.listIterator();
        if (it.hasNext()) {
            // first element
            sb.append(it.next().getString());
            while (it.hasNext()) {
                SearchTreeNode childNode = it.next();
                sb.append(" ")
                        .append(operator.toUpperCase())
                        .append(" ")
                        .append(childNode.getString());
            }
        }
        return sb.append(")");
    }

    @Override
    public String parseJSON(int indentLevel) {
        StringBuilder sb = new StringBuilder("{\n");
        indent(sb, indentLevel + 1).append("\"").append(operator).append("\" : [\n");
        ListIterator<SearchTreeNode> it = childTreeNodes.listIterator();
        if (it.hasNext()) {
            // first element
            indent(sb, indentLevel + 2).append(it.next().parseJSON(indentLevel + 3));
            while (it.hasNext()) {
                SearchTreeNode childNode = it.next();
                indent(sb, indentLevel + 2).append(",\n");
                indent(sb, indentLevel + 2).append(childNode.parseJSON(indentLevel + 3));
            }
        }
        indent(sb, indentLevel + 1).append("]\n");
        return
                indent(sb, indentLevel).append("}\n").toString();
    }

    public UnmodifiableArrayList<SearchTreeNode> getChildren() {
        int childrenCount = childTreeNodes.size();
        SearchTreeNode[] nodes = new SearchTreeNode[childrenCount];
        return new UnmodifiableArrayList<>(childTreeNodes.toArray(nodes), childrenCount);
    }

    abstract void addChild(SearchTreeNode newChild);

    abstract void removeChild(SearchTreeNode child);

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        }

        if (!(obj instanceof SearchCondition)) {
            return false;
        }

        SearchCondition other = (SearchCondition) obj;
        if (!other.operator.equals(this.operator))
            return false;
        else {
            for (SearchTreeNode node : other.childTreeNodes) {
                if(! this.childTreeNodes.contains(node)) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public int hashCode() {
        String childrenCount = childTreeNodes.size() + "";
        String operatorHash = (operator.hashCode() % 1000) + "";
        String childrenHash = "";

        int tmpHash = 0;
        for (SearchTreeNode node : childTreeNodes) {
            tmpHash += node.hashCode() % 100;
        }
        childrenHash += tmpHash;

        return Integer.parseInt(childrenCount + operatorHash + childrenHash);
    }



    /*                                                                                  *
     * Implementations are written inside class SearchCondition for better rendering    *
     * of class names: SearchCondition.And, SearchCondition.Or, SearchCondition.Not     *
     *                                                                                  */

    /**
     * Represents an AND operation between 2 or more {@link SearchTreeNode SearchTreeNodes}
     *
     * @see SearchCondition
     */
    public static final class And extends SearchCondition {

        And() {
            super("and");
        }

        /**
         * Create a new AND condition between the specified clauses
         *
         * @param clauses a {@link Collection} of {@link SearchTreeNode} that will be evaluated in conjunction (AND)
         */
        public And(Collection<SearchTreeNode> clauses) {
            super("and", clauses);
        }

        @Override
        public void addChild(SearchTreeNode newChild) {
            if (newChild instanceof And) {
                childTreeNodes.addAll(((And) newChild).childTreeNodes);
            } else {
                childTreeNodes.addLast(newChild);
            }
        }

        @Override
        public void removeChild(SearchTreeNode child) {
        childTreeNodes.remove(child);
    }
    }


    /**
     * Represents an OR operation between 2 or more {@link SearchTreeNode SearchTreeNodes}
     *
     * @see SearchCondition
     */
    public static final class Or extends SearchCondition {

        Or() {
            super("or");
        }

        public Or(Collection<SearchTreeNode> clauses) {
            super("or", clauses);
        }

        @Override
        public void addChild(SearchTreeNode newChild)  {
            if (newChild instanceof Or) {
                childTreeNodes.addAll(((Or) newChild).childTreeNodes);
            } else {
                childTreeNodes.addLast(newChild);
            }
        }

        @Override
        public void removeChild(SearchTreeNode child) {
            childTreeNodes.remove(child);
        }
    }


    /**
     * Represents a NOT operation applied on a {@link SearchTreeNode}
     *
     * @see SearchCondition
     */
    public static final class Not extends SearchCondition {

        Not() {
            super("not");
        }

        public Not(SearchTreeNode element) {
            this();
            this.setChild(element);
        }

        // use setChild instead
        @Override
        void addChild(SearchTreeNode newChild) {
            if (childTreeNodes.size() == 0)
                childTreeNodes.add(newChild);
        }

        // use setChild instead
        @Override
        void removeChild(SearchTreeNode child) {
            childTreeNodes.clear();
        }

        /**
         * Sets the specified {@link SearchTreeNode} as this node's child. If another child is present, it will be removed.
         *
         * @param child the new child of this {@link Not} node. If child is {@code null}, this
         *              method will only remove any existing child of this node.
         */
        public void setChild(SearchTreeNode child) {
            if (! childTreeNodes.isEmpty())
                removeChild(child);
            if (child != null)
                addChild(child);
        }

        /**
         * Get this node's child, or {@code null} if it doesn't have any.
         *
         * @return the child {@link SearchTreeNode} of this element, if exists; otherwise {@code null}.
         */
        public SearchTreeNode getChild() {
            if (childTreeNodes.isEmpty())
                return null;
            return childTreeNodes.getFirst();
        }

        @Override
        public StringBuilder getString() {
            StringBuilder sb = new StringBuilder("(");
            SearchTreeNode child = getChild();
            if (child != null) {
                sb.append(operator.toUpperCase())
                        .append(" ")
                        .append(child.getString());
            } else {
                sb.append("<ERROR! 'NOT' can't be applied to 'null'>");
            }
            return sb.append(")");
        }

        @Override
        public String parseJSON(int indentLevel) {
            StringBuilder sb = new StringBuilder("{\n");
            indent(sb, indentLevel).append("\"").append(operator).append("\" : ");
            SearchTreeNode node = getChild();
            if (node != null)
                sb.append(
                        node.parseJSON(indentLevel)
                );
            return indent(sb, indentLevel-1).append("}\n").toString();
        }
    }
}
