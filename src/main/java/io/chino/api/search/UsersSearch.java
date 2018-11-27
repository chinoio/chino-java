package io.chino.api.search;

import com.fasterxml.jackson.databind.JsonNode;
import io.chino.api.common.ChinoApiException;
import io.chino.api.user.GetUsersResponse;
import io.chino.java.ChinoBaseAPI;
import io.chino.java.Search;

import java.io.IOException;

import static io.chino.api.search.FilterOperator.EQUALS;
import static io.chino.api.search.ResultType.USERNAME_EXISTS;

/**
 * Implementation of a {@link AbstractSearchClient} that executes search queries on
 * {@link io.chino.api.user.User Users}.
 *
 * @see io.chino.java.Search#users(String)
 */
public final class UsersSearch extends AbstractSearchClient<GetUsersResponse> {

    public UsersSearch(ChinoBaseAPI APIClient, String schemaId) {
        super(APIClient, schemaId);
    }


    @Override
    public GetUsersResponse execute(int offset, int limit) throws IOException, ChinoApiException {
        // build JSON from query
        String jsonQuery = super.parseSearchRequest();
        // search on chino
        JsonNode response = client.postResource(
                "/search/users/" + resourceID,
                mapper.readValue(jsonQuery, JsonNode.class),
                offset,
                limit
        );
        return mapper.convertValue(response, GetUsersResponse.class);
    }

    @Override
    public UsersSearch setResultType(ResultType resultType) {
        super.setResultType(resultType);
        return this;
    }

    @Override
    public UsersSearch addSortRule(String fieldName, SortRule.Order order) {
        super.addSortRule(fieldName, order);
        return this;
    }

    @Override
    public UsersSearch addSortRule(String fieldName, SortRule.Order order, int index) {
        super.addSortRule(fieldName, order, index);
        return this;
    }
}
