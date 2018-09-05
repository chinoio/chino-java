package io.chino.api.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.chino.api.common.ChinoApiException;
import io.chino.api.search.leaf.*;
import io.chino.java.ChinoBaseAPI;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class SearchClient<ResponseType extends Object> {

    private ResultType resultType = ResultType.FULL_CONTENT;
    private SearchTreeNode query;
    private LinkedList<SortRule> sort = null;

    protected final ChinoBaseAPI client;
    protected String resourceID;

    protected static final ObjectMapper mapper = new ObjectMapper();

    protected SearchClient (ChinoBaseAPI APIclient, String resourceID) {
        client = APIclient;
        this.resourceID = resourceID;
    }

    SearchClient<ResponseType> setQuery(SearchTreeNode query) {
        this.query = query;
        return this;
    }

    public SearchClient<ResponseType> setResultType(ResultType resultType) {
        this.resultType = resultType;
        return this;
    }

    public SearchClient<ResponseType> addSortRule(String fieldName, SortRule.Order order) {
        if (sort == null) {
            sort = new LinkedList<>();
        }
        sort.add(
                new SortRule(fieldName, order)
        );
        return this;
    }

    public SearchQueryBuilder with(SearchQueryBuilder query) {
        return query;
    }

    public SearchQueryBuilder with(String fieldName, FilterOperator type, int value) {
        return new SearchQueryBuilder(new IntegerSearchLeaf(fieldName, type, value), this);
    }

    public SearchQueryBuilder with(String fieldName, FilterOperator type, float value) {
        return new SearchQueryBuilder(new FloatSearchLeaf(fieldName, type, value), this);
    }

    public SearchQueryBuilder with(String fieldName, FilterOperator type, boolean value) {
        return new SearchQueryBuilder(new BooleanSearchLeaf(fieldName, type, value), this);
    }

    public SearchQueryBuilder with(String fieldName, FilterOperator type, String value) {
        return new SearchQueryBuilder(new StringSearchLeaf(fieldName, type, value), this);
    }

    public SearchQueryBuilder with(String fieldName, FilterOperator type, List value) {
        if (value == null) {
            return with(fieldName, type, (String) null);
        }

        ArraySearchLeaf arraySearchLeaf = getArraySearchLeaf(fieldName, type, value);

        return new SearchQueryBuilder(arraySearchLeaf, this);
    }

    @NotNull
    static ArraySearchLeaf getArraySearchLeaf(String fieldName, FilterOperator type, @NotNull List value) {
        if (value.isEmpty())
            return new ArraySearchLeaf<Void>(fieldName, type, value);
        Object element = value.get(0);

        ArraySearchLeaf arraySearchLeaf;
        if(element instanceof Integer) {
            arraySearchLeaf = new ArraySearchLeaf<Integer>(fieldName, type, value);
        } else if(element instanceof Float) {
            arraySearchLeaf = new ArraySearchLeaf<Float>(fieldName, type, value);
        } else if(element instanceof Boolean) {
            arraySearchLeaf = new ArraySearchLeaf<Boolean>(fieldName, type, value);
        } else if(element instanceof String) {
            arraySearchLeaf = new ArraySearchLeaf<String>(fieldName, type, value);
        } else {
            throw new IllegalArgumentException(
                    "Unsupported element in list of type '" +
                            element.getClass().getCanonicalName() +
                            "'. Supported types are int, float, boolean and java.lang.String."
            );
        }
        return arraySearchLeaf;
    }

    @Override
    public String toString() {
        return query.getString().toString();
    }

    protected String parseSearchRequest() {
        String queryJSON = query.parseJSON(2);

        /* indented by 0 */
        StringBuilder sb = new StringBuilder("{\n");

        /* indented by 1 */
        // write resultType field in JSON
        sb.append("\t").append("\"result_type\": \"").append(resultType.toString()).append("\"").append(",\n");
        // write list of SortRules in JSON
        if (sort != null && !sort.isEmpty()) {
            sb.append("\t").append("\"sort\": ").append("[\n");
            Iterator<SortRule> it = sort.iterator();
            while (it.hasNext()) {
                sb.append(it.next().toJSONString(2));
                if(it.hasNext()) {
                    sb.append(",");
                }
                sb.append("\n");
            }
            sb.append("\t").append("],\n");
        }
        // write parsed query JSON
        sb.append("\t").append("\"query\": ").append(queryJSON);
        return sb.append("}\n").toString();
    }

    public abstract ResponseType execute() throws IOException, ChinoApiException;

    public String toJSONString() {
        return parseSearchRequest();
    }
}
