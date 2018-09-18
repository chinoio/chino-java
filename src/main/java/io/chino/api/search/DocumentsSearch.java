package io.chino.api.search;

import com.fasterxml.jackson.databind.JsonNode;
import io.chino.api.common.ChinoApiException;
import io.chino.api.document.GetDocumentsResponse;
import io.chino.java.ChinoBaseAPI;

import java.io.IOException;

public final class DocumentsSearch extends SearchClient<GetDocumentsResponse> {

    public DocumentsSearch(ChinoBaseAPI APIClient, String schemaId) {
        super(APIClient, schemaId);
    }

    @Override
    public GetDocumentsResponse execute() throws IOException, ChinoApiException {
        String JSONquery = super.parseSearchRequest();

        JsonNode response = client.postResource(
                "/search/documents/" + resourceID,
                mapper.readValue(JSONquery, JsonNode.class)
        );
        if (response != null)
            return mapper.convertValue(response, GetDocumentsResponse.class);

        return null;
    }

    @Override
    public DocumentsSearch setResultType(ResultType resultType) {
        super.setResultType(resultType);
        return this;
    }

    @Override
    public DocumentsSearch addSortRule(String fieldName, SortRule.Order order) {
        super.addSortRule(fieldName, order);
        return this;
    }

    @Override
    public DocumentsSearch addSortRule(String fieldName, SortRule.Order order, int index) {
        super.addSortRule(fieldName, order, index);
        return this;
    }
}
