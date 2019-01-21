package io.chino.api.search.leaf;

import io.chino.api.search.FilterOperator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * {@link SearchLeaf} used to search for values in a JSON Array.
 *
 * <pre><code>
 * {
 *     "field": "field_name",
 *     "type": "in",
 *     "value": [1, 2, 3]
 * }
 * </code></pre>
 *
 * <b>NOTE</b>: You can search for {@code date}, {@code time} and {@code datetime} fields passing an Array of String,
 * e.g.:
 *
 * <pre><code>
 * ["12:02:46.398", "15:43:16.845"]
 *
 * ["2019-01-19T12:02:46.398", "2019-01-17T15:43:16.845"]
 * </code></pre>
 *
 * @param <T> the type of data that are in the array. Supported types are int, float, boolean, {@link java.lang.String}
 *           and {@link Date}
 */
public class ArraySearchLeaf<T> extends SearchLeaf<List<T>> {

    public ArraySearchLeaf(String field, FilterOperator type, List<T> value) {
        super(field, type, value);
    }
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

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

        return super.parseJSONWithValue(valuesString.append("]").toString(), indentLevel);
    }

    @Override
    public StringBuilder getString() {
        if (field == null || type == null) {
            return super.getString();
        } else {
            StringBuilder sb = new StringBuilder("{");
            sb.append(field).append(" ")
                    .append(type.toString()).append(" ")
                    .append("[");

            // parse list elements
            if (!value.isEmpty())
                sb.append(
                        getValueStringEncoding(value.get(0))
                );
            for (T item : value.subList(1, value.size())) {
                sb.append(", ").append(getValueStringEncoding(item));
            }
            sb.append("]");

            return sb.append("}");
        }
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
