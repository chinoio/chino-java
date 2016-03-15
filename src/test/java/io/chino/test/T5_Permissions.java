package io.chino.test;

import io.chino.api.common.ChinoApiException;
import io.chino.client.ChinoAPI;

import java.io.IOException;

import io.chino.examples.permissions.PermissionSamples;
import io.chino.examples.util.Constants;
import io.chino.test.util.DeleteAll;
import org.junit.*;




public class T5_Permissions{

	static ChinoAPI chino;

	@Before
	public void init() throws IOException, ChinoApiException{
	    chino = new ChinoAPI(Constants.HOST, Constants.CUSTOMER_ID, Constants.CUSTOMER_KEY);
	}
	

	@Test
    public void testPermissions() throws IOException, ChinoApiException{
		DeleteAll.testDelete(chino);
		PermissionSamples permissionSamples = new PermissionSamples();
		permissionSamples.testPermissions();
	}
}
