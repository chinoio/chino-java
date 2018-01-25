package io.chino.test;

import java.io.IOException;

import io.chino.examples.userschemas.UserSchemaSamples;
import io.chino.examples.util.Constants;
import io.chino.examples.util.DeleteAll;
import org.junit.Before;
import org.junit.Test;

import io.chino.api.common.ChinoApiException;
import io.chino.java.ChinoAPI;



public class T2_UserSchemas{

	static ChinoAPI chino;
	
	@Before
	public void init(){
	    chino = new ChinoAPI(Constants.HOST, Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY);
	}
	
	
    @Test
    public void testSchemas() throws IOException, ChinoApiException{
		UserSchemaSamples userSchemaSamples = new UserSchemaSamples();
		userSchemaSamples.testUserSchemas();
		DeleteAll deleteAll = new DeleteAll();
		deleteAll.deleteAll(chino);
    }
    
}
