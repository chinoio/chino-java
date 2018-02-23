package io.chino.test;

import io.chino.api.common.ChinoApiException;
import io.chino.java.ChinoAPI;
import io.chino.examples.users.UserSamples;
import java.io.IOException;

import io.chino.test.util.Constants;
import org.junit.*;




public class T3_User{

	static ChinoAPI chino;
	
	@Before
	public void init(){
		chino = new ChinoAPI(Constants.HOST, Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY);
	}

	
    @Test
    public void testUsers() throws IOException, ChinoApiException{
		UserSamples userSamples = new UserSamples();
		userSamples.testUsers();
    }

}
