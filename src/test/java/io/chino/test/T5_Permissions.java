package io.chino.test;

import io.chino.api.common.ChinoApiException;
import io.chino.java.ChinoAPI;

import java.io.IOException;

import io.chino.examples.permissions.PermissionSamples;
import io.chino.examples.Constants;
import org.junit.*;




public class T5_Permissions{

	static ChinoAPI chino;

	// @Before
	public void init() throws IOException, ChinoApiException{
	    chino = new ChinoAPI(Constants.HOST, Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY);
	}
	

	// @Test
    public void testPermissions() throws IOException, ChinoApiException{
		PermissionSamples permissionSamples = new PermissionSamples();
		permissionSamples.testPermissions();
	}
}
