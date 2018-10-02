package io.chino.api.search.leaf;

import io.chino.api.search.FilterOperator;

import java.util.Iterator;
import java.util.List;

/**
 * {@link SearchLeaf} used to search for values in Arrays.
 *
 * @param <T> the type of data that are in the array. Supported types are int, float, boolean and {@link java.lang.String}
 */
public class ArraySearchLeaf<T extends Object> extends SearchLeaf<List<T>> {

    public ArraySearchLeaf(String field, FilterOperator type, List<T> value) {
        super(field, type, value);
    }

    @Override
    public String parseJSON(int indentLevel) {
        StringBuilder valuesString = new StringBuilder("[");

        Iterator<T> it = value.iterator();
        while (it.hasNext()) {
            valuesString.append(
                    getValueStringEncoding(
                            it.next()
                    )
            );
            if (it.hasNext()) {
                valuesString.append(",");
            }
        }

        return valuesString.append("]").toString();
    }

    private String getValueStringEncoding(T element) {
        if (element == null)
            return "null";
        else if (element instanceof Number)
            return "" + element;
        else if (element instanceof String)
            return "\"" + element + "\"";
        else if (element instanceof Boolean)
            return ((Boolean) element) ? "true" : "false";
        else
            return element.toString();
    }
}
