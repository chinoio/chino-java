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

    /**
     * Check that the username exists in the {@link io.chino.api.userschema.UserSchema} specified in
     * {@link Search#users(String) users()}.
     *
     * @param username a String containing the username to look for
     * 
     * @return {@code true} if a User exists with that name in the specified UserSchema.
     *
     * @throws IOException data processing error
     * @throws ChinoApiException server error
     */
    public boolean usernameExists(String username) throws IOException, ChinoApiException {
        // invalidate any current query
        this.setQuery(null);
        // check if username exist and return result
        GetUsersResponse response = (GetUsersResponse) this.setResultType(USERNAME_EXISTS)
               .with("username", EQUALS, username)
               .buildSearch().execute();
        return response.getExists();
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
