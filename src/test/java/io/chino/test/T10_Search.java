package io.chino.test;

import io.chino.api.common.ChinoApiException;
import io.chino.client.ChinoAPI;
import io.chino.examples.search.SearchSamples;
import io.chino.examples.util.Constants;
import io.chino.test.util.DeleteAll;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class T10_Search {

    ChinoAPI chino;

    @Before
    public void init(){
        chino = new ChinoAPI(Constants.HOST, Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY);
    }

    @Test
    public void testDocs() throws IOException, ChinoApiException {
        DeleteAll.testDelete(chino);
        SearchSamples searchSamples = new SearchSamples();
        searchSamples.testSearch();
    }
}
