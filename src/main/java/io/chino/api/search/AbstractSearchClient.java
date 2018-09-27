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

/**
 * Abstract implementation of a API client that can execute search queries on Chino.io
 *
 * @param <ResponseType> the class of the response that will be returned by the search operation.
 */
public abstract class AbstractSearchClient<ResponseType extends Object> {

    private ResultType resultType = ResultType.FULL_CONTENT;
    private SearchTreeNode query;
    private LinkedList<SortRule> sort = null;

    protected final ChinoBaseAPI client;
    protected String resourceID;

    protected static final ObjectMapper mapper = new ObjectMapper();

    protected AbstractSearchClient(ChinoBaseAPI APIclient, String resourceID) {
        client = APIclient;
        this.resourceID = resourceID;
    }

    AbstractSearchClient<ResponseType> setQuery(SearchTreeNode query) {
        this.query = query;
        return this;
    }

    /**
     * Update the result type for the result of this search. Default value is
     * {@link ResultType#FULL_CONTENT FULL_CONTENT}
     *
     * @param resultType the new {@link ResultType}
     *
     * @return a {@link AbstractSearchClient} subclass with the updated {@link ResultType}
     */
    protected  <Client extends AbstractSearchClient<ResponseType>> Client setResultType(ResultType resultType) {
        this.resultType = resultType;
        return null;
    }

    /**
     * Add a new {@link SortRule} for sorting the results of this Search.
     * The new rule will have lower priority compared to the existing ones.
     *
     * @param fieldName the name of the field that will be used to sort the results. The field must be indexed.
     * @param order a value in {@link io.chino.api.search.SortRule.Order SortRule.Order}
     *
     * @return a {@link AbstractSearchClient} subclass with the new {@link SortRule}
     */
    protected <Client extends AbstractSearchClient<ResponseType>> Client addSortRule(String fieldName, SortRule.Order order) {
        if (sort == null) {
            sort = new LinkedList<>();
        }
        sort.add(
                new SortRule(fieldName, order)
        );
        return null;
    }

    /**
     * Add a new {@link SortRule} for sorting the results of this Search.
     * The new rule will be evaluated based on the provided {@code index}, where a low index means a higher priority.
     * E.g. in order to evaluate the new rule, set index to 0.
     *
     * @param fieldName the name of the field that will be used to sort the results. The field must be indexed.
     * @param order a value in {@link io.chino.api.search.SortRule.Order SortRule.Order}
     * @param index a lower value means a higher priority. This value must be equal or higher than 0,
     *             with '0' meaning 'maximum priority'.
     *
     * @see #addSortRule(String, SortRule.Order) How to add rule with minimum priority
     *
     * @return a {@link AbstractSearchClient} subclass with the new {@link SortRule}
     */
    protected <Client extends AbstractSearchClient<ResponseType>> Client addSortRule(String fieldName, SortRule.Order order, int index) {
        if (sort == null) {
            sort = new LinkedList<>();
        }
        if (index > sort.size())
            index = sort.size();
        sort.add(
                index,
                new SortRule(fieldName, order)
        );
        return null;
    }

    /**
     * Start building a query for this client from an existing {@link SearchQueryBuilder}.
     * If another query was set before for this client, it will be overwritten when calling
     * {@link SearchQueryBuilder#buildSearch()}.
     *
     * @param query an existing {@link SearchQueryBuilder}
     *
     * @return a {@link SearchQueryBuilder} that will build this client's query.
     */
    public SearchQueryBuilder with(SearchQueryBuilder query) {
        return query;
    }

    /**
     * Start building a query for this client by specifying a search criterion.
     * If another query was set before for this client, it will be overwritten when calling
     * {@link SearchQueryBuilder#buildSearch()}.
     *
     * @param fieldName the name of an indexed field to add as a search criterion
     * @param type the {@link FilterOperator} to be evaluated
     * @param value the expected value of the field
     *
     * @return a new {@link SearchQueryBuilder} that will build this client's query
     */
    public SearchQueryBuilder with(String fieldName, FilterOperator type, int value) {
        return new SearchQueryBuilder(new IntegerSearchLeaf(fieldName, type, value), this);
    }

    /**
     * Start building a query for this client by specifying a search criterion.
     * If another query was set before for this client, it will be overwritten when calling
     * {@link SearchQueryBuilder#buildSearch()}.
     *
     * @param fieldName the name of an indexed field to add as a search criterion
     * @param type the {@link FilterOperator} to be evaluated
     * @param value the expected value of the field
     *
     * @return a new {@link SearchQueryBuilder} that will build this client's query
     */
    public SearchQueryBuilder with(String fieldName, FilterOperator type, float value) {
        return new SearchQueryBuilder(new FloatSearchLeaf(fieldName, type, value), this);
    }

    /**
     * Start building a query for this client by specifying a search criterion.
     * If another query was set before for this client, it will be overwritten when calling
     * {@link SearchQueryBuilder#buildSearch()}.
     *
     * @param fieldName the name of an indexed field to add as a search criterion
     * @param type the {@link FilterOperator} to be evaluated
     * @param value the expected value of the field
     *
     * @return a new {@link SearchQueryBuilder} that will build this client's query
     */
    public SearchQueryBuilder with(String fieldName, FilterOperator type, boolean value) {
        return new SearchQueryBuilder(new BooleanSearchLeaf(fieldName, type, value), this);
    }

    /**
     * Start building a query for this client by specifying a search criterion.
     * If another query was set before for this client, it will be overwritten when calling
     * {@link SearchQueryBuilder#buildSearch()}.
     *
     * @param fieldName the name of an indexed field to add as a search criterion
     * @param type the {@link FilterOperator} to be evaluated
     * @param value the expected value of the field
     *
     * @return a new {@link SearchQueryBuilder} that will build this client's query
     */
    public SearchQueryBuilder with(String fieldName, FilterOperator type, String value) {
        return new SearchQueryBuilder(new StringSearchLeaf(fieldName, type, value), this);
    }

    /**
     * Start building a query for this client by specifying a search criterion.
     * If another query was set before for this client, it will be overwritten when calling
     * {@link SearchQueryBuilder#buildSearch()}.
     *
     * @param fieldName the name of an indexed field to add as a search criterion
     * @param type the {@link FilterOperator} to be evaluated
     * @param value the expected value of the field
     *
     * @return a new {@link SearchQueryBuilder} that will build this client's query
     */
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

    /**
     * Parse the current query and prints it to {@link System#out} in a human-friendly fashion.
     *
     * @return a String containing the JSON representation of the current query for this client
     */
    public String toJSONString() {
        return parseSearchRequest();
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

    /**
     * Execute the current query that is contained in this {@link AbstractSearchClient}.
     * Calls Chino.io Search API in order to retrieve the objects that match the provided search criteria.
     *
     * @return either a {@link io.chino.api.document.GetDocumentsResponse GetDocumentsResponse} or a
     * {@link io.chino.api.user.GetUsersResponse GetUsersResponse} (depending on the implementation)
     * that contains the search results.
     *
     * @throws IOException
     * @throws ChinoApiException
     */
    public abstract ResponseType execute() throws IOException, ChinoApiException;
}
