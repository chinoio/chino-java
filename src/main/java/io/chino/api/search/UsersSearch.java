package io.chino.api.search;

import com.fasterxml.jackson.databind.JsonNode;
import io.chino.api.common.ChinoApiException;
import io.chino.api.user.GetUsersResponse;
import io.chino.java.ChinoBaseAPI;

import java.io.IOException;

public final class UsersSearch extends SearchClient<GetUsersResponse> {

    public UsersSearch(ChinoBaseAPI APIClient, String schemaId) {
        super(APIClient, schemaId);
    }

    @Override
    public GetUsersResponse execute() throws IOException, ChinoApiException {
        String jsonQuery = super.parseSearchRequest();
        JsonNode response = client.postResource(
                "/search/users/" + resourceID,
                mapper.readValue(jsonQuery, JsonNode.class)
        );
        if (response != null)
            return mapper.convertValue(response, GetUsersResponse.class);

        return null;
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
