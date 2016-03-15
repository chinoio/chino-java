package io.chino.test;


import java.io.IOException;

import io.chino.client.ChinoAPI;
import io.chino.examples.blobs.BlobSamples;
import io.chino.examples.util.Constants;
import io.chino.test.util.DeleteAll;
import org.junit.Before;
import org.junit.Test;

import io.chino.api.common.ChinoApiException;



public class T9_BLOBs{

    static ChinoAPI chino;

	
	@Before
	public void init(){
        chino = new ChinoAPI(Constants.HOST, Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY);
	}
	
    @Test 
    public void testDocs() throws IOException, ChinoApiException{
        DeleteAll.testDelete(chino);
        BlobSamples blobSamples = new BlobSamples();
        blobSamples.testBlobs();
    }
   
}
