package io.chino.api.search;

import io.chino.api.search.leaf.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SearchQueryBuilder {

    private final SearchTreeNode treeTop;

    private SearchClient queryExecutor;

    protected SearchQueryBuilder(SearchTreeNode rootNode, SearchClient client) {
        treeTop = rootNode;
        queryExecutor = client;
    }

    protected SearchQueryBuilder(SearchTreeNode query1, SearchCondition cond, SearchTreeNode query2, SearchClient client) {
        treeTop = cond;
        ((SearchCondition) treeTop).addChild(query1);
        ((SearchCondition) treeTop).addChild(query2);
        queryExecutor = client;
    }

    void setClient(SearchClient client) {
        queryExecutor = client;
    }

    /**
     * Get a {@link SearchQueryBuilder} which is equivalent to this instance AND
     * the specified {@link SearchQueryBuilder}
     *
     * @param query a {@link SearchQueryBuilder} which contains another query
     *
     * @return a {@link SearchQueryBuilder} with the new query.
     */
    public SearchQueryBuilder and(SearchQueryBuilder query) {
        if (treeTop instanceof SearchCondition.And) {
            ((SearchCondition.And) treeTop).addChild(query.treeTop);
            return this;
        } else if (query.treeTop instanceof SearchCondition.And) {
            ((SearchCondition.And) query.treeTop).addChild(treeTop);
            query.queryExecutor = queryExecutor;
            return query;
        }

        return new SearchQueryBuilder(
                this.treeTop,
                new SearchCondition.And(),
                query.treeTop,
                queryExecutor
        );
    }

    /**
     * Get a {@link SearchQueryBuilder} which is equivalent to this instance AND
     * the specified search criterion.
     *
     * @param fieldName the name of an indexed field to add as a search criterion
     * @param type the {@link FilterOperator} to be evaluated
     * @param value the expected value of the field
     *
     * @return a {@link SearchQueryBuilder} with the new query.
     */
    public SearchQueryBuilder and(String fieldName, FilterOperator type, int value) {
        return this.and(
                with(fieldName, type, value)
        );
    }

    /**
     * Get a {@link SearchQueryBuilder} which is equivalent to this instance AND
     * the specified search criterion.
     *
     * @param fieldName the name of an indexed field to add as a search criterion
     * @param type the {@link FilterOperator} to be evaluated
     * @param value the expected value of the field
     *
     * @return a {@link SearchQueryBuilder} with the new query.
     */
    public SearchQueryBuilder and(String fieldName, FilterOperator type, float value) {
        return this.and(
                with(fieldName, type, value)
        );
    }

    /**
     * Get a {@link SearchQueryBuilder} which is equivalent to this instance AND
     * the specified search criterion.
     *
     * @param fieldName the name of an indexed field to add as a search criterion
     * @param type the {@link FilterOperator} to be evaluated
     * @param value the expected value of the field
     *
     * @return a {@link SearchQueryBuilder} with the new query.
     */
    public SearchQueryBuilder and(String fieldName, FilterOperator type, boolean value) {
        return this.and(
                with(fieldName, type, value)
        );
    }

    /**
     * Get a {@link SearchQueryBuilder} which is equivalent to this instance AND
     * the specified search criterion.
     *
     * @param fieldName the name of an indexed field to add as a search criterion
     * @param type the {@link FilterOperator} to be evaluated
     * @param value the expected value of the field
     *
     * @return a {@link SearchQueryBuilder} with the new query.
     */
    public SearchQueryBuilder and(String fieldName, FilterOperator type, String value) {
        return this.and(
                with(fieldName, type, value)
        );
    }

    /**
     * Get a {@link SearchQueryBuilder} which is equivalent to this instance AND
     * the specified search criterion.
     *
     * @param fieldName the name of an indexed field to add as a search criterion
     * @param type the {@link FilterOperator} to be evaluated
     * @param value the expected value of the field
     *
     * @return a {@link SearchQueryBuilder} with the new query.
     */
    public SearchQueryBuilder and(String fieldName, FilterOperator type, List value) {
        return this.and(
                with(fieldName, type, value)
        );
    }

    /**
     * Get a {@link SearchQueryBuilder} which is equivalent to this instance OR
     * the specified {@link SearchQueryBuilder}
     *
     * @param query a {@link SearchQueryBuilder} which contains another query
     *
     * @return a {@link SearchQueryBuilder} with the new query.
     */
    public SearchQueryBuilder or(SearchQueryBuilder query) {
        if (treeTop instanceof SearchCondition.Or) {
            ((SearchCondition.Or) treeTop).addChild(query.treeTop);
            return this;
        } else if (query.treeTop instanceof SearchCondition.Or) {
            ((SearchCondition.Or) query.treeTop).addChild(treeTop);
            query.queryExecutor = queryExecutor;
            return query;
        }

        return new SearchQueryBuilder(
                this.treeTop,
                new SearchCondition.Or(),
                query.treeTop,
                queryExecutor
        );
    }

    /**
     * Get a {@link SearchQueryBuilder} which is equivalent to this instance OR
     * the specified search criterion.
     *
     * @param fieldName the name of an indexed field to add as a search criterion
     * @param type the {@link FilterOperator} to be evaluated
     * @param value the expected value of the field
     *
     * @return a {@link SearchQueryBuilder} with the new query.
     */
    public SearchQueryBuilder or(String fieldName, FilterOperator type, int value) {
        return this.or(
                with(fieldName, type, value)
        );
    }

    /**
     * Get a {@link SearchQueryBuilder} which is equivalent to this instance OR
     * the specified search criterion.
     *
     * @param fieldName the name of an indexed field to add as a search criterion
     * @param type the {@link FilterOperator} to be evaluated
     * @param value the expected value of the field
     *
     * @return a {@link SearchQueryBuilder} with the new query.
     */
    public SearchQueryBuilder or(String fieldName, FilterOperator type, float value) {
        return this.or(
                with(fieldName, type, value)
        );
    }

    /**
     * Get a {@link SearchQueryBuilder} which is equivalent to this instance OR
     * the specified search criterion.
     *
     * @param fieldName the name of an indexed field to add as a search criterion
     * @param type the {@link FilterOperator} to be evaluated
     * @param value the expected value of the field
     *
     * @return a {@link SearchQueryBuilder} with the new query.
     */
    public SearchQueryBuilder or(String fieldName, FilterOperator type, boolean value) {
        return this.or(
                with(fieldName, type, value)
        );
    }

    /**
     * Get a {@link SearchQueryBuilder} which is equivalent to this instance OR
     * the specified search criterion.
     *
     * @param fieldName the name of an indexed field to add as a search criterion
     * @param type the {@link FilterOperator} to be evaluated
     * @param value the expected value of the field
     *
     * @return a {@link SearchQueryBuilder} with the new query.
     */
    public SearchQueryBuilder or(String fieldName, FilterOperator type, String value) {
        return this.or(
                with(fieldName, type, value)
        );
    }

    /**
     * Get a {@link SearchQueryBuilder} which is equivalent to this instance OR
     * the specified search criterion.
     *
     * @param fieldName the name of an indexed field to add as a search criterion
     * @param type the {@link FilterOperator} to be evaluated
     * @param value the expected value of the field
     *
     * @return a {@link SearchQueryBuilder} with the new query.
     */
    public SearchQueryBuilder or(String fieldName, FilterOperator type, List value) {
        return this.or(
                with(fieldName, type, value)
        );
    }

    /**
     * Return the specified query, negated (i.e. returns a query which is equivalent to the opposite of the parameter)
     *
     * @param query a {@link SearchQueryBuilder} which contains another query.
     *
     * @return a {@link SearchQueryBuilder} containin the negation of the specified query
     */
    public static SearchQueryBuilder not(SearchQueryBuilder query) {
        if (query.treeTop instanceof SearchCondition.Not) {
            return new SearchQueryBuilder(
                    ((SearchCondition.Not) query.treeTop).getChild(),
                    query.queryExecutor
            );
        }

        return new SearchQueryBuilder(
                new SearchCondition.Not(query.treeTop),
                query.queryExecutor
        );
    }


    /**
     * Return the specified query, negated (i.e. returns a query which is equivalent to the opposite of the parameter)
     *
     * @param fieldName the name of an indexed field to add as a search criterion
     * @param type the {@link FilterOperator} to be evaluated
     * @param value the expected value of the field
     *
     * @return a {@link SearchQueryBuilder} containin the negation of the specified search criterion
     */

    public static SearchQueryBuilder not(String fieldName, FilterOperator type, int value) {
        return not(
                with(fieldName, type, value)
        );
    }

    /**
     * Return the specified query, negated (i.e. returns a query which is equivalent to the opposite of the parameter)
     *
     * @param fieldName the name of an indexed field to add as a search criterion
     * @param type the {@link FilterOperator} to be evaluated
     * @param value the expected value of the field
     *
     * @return a {@link SearchQueryBuilder} containin the negation of the specified search criterion
     */

    public static SearchQueryBuilder not(String fieldName, FilterOperator type, float value) {
        return not(
                with(fieldName, type, value)
        );
    }

    /**
     * Return the specified query, negated (i.e. returns a query which is equivalent to the opposite of the parameter)
     *
     * @param fieldName the name of an indexed field to add as a search criterion
     * @param type the {@link FilterOperator} to be evaluated
     * @param value the expected value of the field
     *
     * @return a {@link SearchQueryBuilder} containin the negation of the specified search criterion
     */

    public static SearchQueryBuilder not(String fieldName, FilterOperator type, boolean value) {
        return not(
                with(fieldName, type, value)
        );
    }

    /**
     * Return the specified query, negated (i.e. returns a query which is equivalent to the opposite of the parameter)
     *
     * @param fieldName the name of an indexed field to add as a search criterion
     * @param type the {@link FilterOperator} to be evaluated
     * @param value the expected value of the field
     *
     * @return a {@link SearchQueryBuilder} containin the negation of the specified search criterion
     */

    public static SearchQueryBuilder not(String fieldName, FilterOperator type, String value) {
        return not(
                with(fieldName, type, value)
        );
    }

    /**
     * Return the specified query, negated (i.e. returns a query which is equivalent to the opposite of the parameter)
     *
     * @param fieldName the name of an indexed field to add as a search criterion
     * @param type the {@link FilterOperator} to be evaluated
     * @param value the expected value of the field
     *
     * @return a {@link SearchQueryBuilder} containin the negation of the specified search criterion
     */

    public static SearchQueryBuilder not(String fieldName, FilterOperator type, List value) {
        return not(
                with(fieldName, type, value)
        );
    }

    /**
     * Saves the current search query in the {@link SearchClient} that created this {@link SearchQueryBuilder}.
     * If the client already contains a query it will be overwritten.
     *
     * @return the original {@link SearchClient} with the updated query.
     */
    public SearchClient buildSearch() {
        return this.queryExecutor.setQuery(treeTop);
    }

    /**
     * Create a new query from an existing {@link SearchQueryBuilder}.
     *
     * @param query an existing {@link SearchQueryBuilder}
     *
     * @return a {@link SearchQueryBuilder} containing the new query.
     */
    public static SearchQueryBuilder with(SearchQueryBuilder query) {
        return query;
    }

    /**
     * Create a new query by specifying a search criterion.
     *
     * @param fieldName the name of an indexed field to add as a search criterion
     * @param type the {@link FilterOperator} to be evaluated
     * @param value the expected value of the field
     *
     * @return a new {@link SearchQueryBuilder} containing the new query.
     */
    public static SearchQueryBuilder with(String fieldName, FilterOperator type, int value) {
        return new SearchQueryBuilder(new IntegerSearchLeaf(fieldName, type, value), null);
    }

    /**
     * Create a new query by specifying a search criterion.
     *
     * @param fieldName the name of an indexed field to add as a search criterion
     * @param type the {@link FilterOperator} to be evaluated
     * @param value the expected value of the field
     *
     * @return a new {@link SearchQueryBuilder} containing the new query.
     */
    public static SearchQueryBuilder with(String fieldName, FilterOperator type, float value) {
        return new SearchQueryBuilder(new FloatSearchLeaf(fieldName, type, value), null);
    }

    /**
     * Create a new query by specifying a search criterion.
     *
     * @param fieldName the name of an indexed field to add as a search criterion
     * @param type the {@link FilterOperator} to be evaluated
     * @param value the expected value of the field
     *
     * @return a new {@link SearchQueryBuilder} containing the new query.
     */
    public static SearchQueryBuilder with(String fieldName, FilterOperator type, boolean value) {
        return new SearchQueryBuilder(new BooleanSearchLeaf(fieldName, type, value), null);
    }

    /**
     * Create a new query by specifying a search criterion.
     *
     * @param fieldName the name of an indexed field to add as a search criterion
     * @param type the {@link FilterOperator} to be evaluated
     * @param value the expected value of the field
     *
     * @return a new {@link SearchQueryBuilder} containing the new query.
     */
    public static SearchQueryBuilder with(String fieldName, FilterOperator type, @Nullable String value) {
        return new SearchQueryBuilder(new StringSearchLeaf(fieldName, type, value), null);
    }

    /**
     * Create a new query by specifying a search criterion.
     *
     * @param fieldName the name of an indexed field to add as a search criterion
     * @param type the {@link FilterOperator} to be evaluated
     * @param value the expected value of the field
     *
     * @return a new {@link SearchQueryBuilder} containing the new query.
     */
    public static SearchQueryBuilder with(String fieldName, FilterOperator type, @NotNull List value) {
        ArraySearchLeaf arraySearchLeaf = SearchClient.getArraySearchLeaf(fieldName, type, value);
        return new SearchQueryBuilder(arraySearchLeaf, null);
    }
}
