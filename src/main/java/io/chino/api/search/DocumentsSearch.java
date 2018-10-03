package io.chino.api.search;

import com.fasterxml.jackson.databind.JsonNode;
import io.chino.api.common.ChinoApiException;
import io.chino.api.document.GetDocumentsResponse;
import io.chino.java.ChinoBaseAPI;

import java.io.IOException;

/**
 * Implementation of a {@link AbstractSearchClient} that executes search queries on
 * {@link io.chino.api.document.Document Documents}.
 *
 * @see io.chino.java.Search#documents(String)
 */
public final class DocumentsSearch extends AbstractSearchClient<GetDocumentsResponse> {

    public DocumentsSearch(ChinoBaseAPI APIClient, String schemaId) {
        super(APIClient, schemaId);
    }

    @Override
    public GetDocumentsResponse execute() throws IOException, ChinoApiException {
        String JSONQuery = super.parseSearchRequest();

        JsonNode response = client.postResource(
                "/search/documents/" + resourceID,
                mapper.readValue(JSONQuery, JsonNode.class)
        );
        return mapper.convertValue(response, GetDocumentsResponse.class);
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
