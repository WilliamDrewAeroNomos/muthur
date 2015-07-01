/**
 * 
 */
package com.csc.muthur.server.model.event.response;

import java.util.UUID;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.event.AbstractModelTest;
import com.csc.muthur.server.model.event.request.UpdateFederationExecutionTimeRequest;

/**
 * @author <a href=mailto:Nexsim@foo.com>Nexsim</a>
 * @version $Revision$
 */
public class UpdateFederationExecutionTimeResponseTest extends
		AbstractModelTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * 
	 */
	public void testUpdateFederationExecutionTimeResponse() {

		UpdateFederationExecutionTimeRequest req = new UpdateFederationExecutionTimeRequest();

		req.setTimeToLiveSecs(10);
		req.setSourceOfEvent("Muthur");
		req.setFederationExecutionModelHandle(UUID.randomUUID().toString());

		// current time
		req.setFederationExecutionTimeMSecs(System.currentTimeMillis());

		assertNotNull(req);

		String serializedRequest = req.serialize();

		assertNotNull(serializedRequest);
		assertFalse("".equals(serializedRequest));

		writeToFile(req, false);

		UpdateFederationExecutionTimeRequest reqFromXML = new UpdateFederationExecutionTimeRequest();

		try {
			reqFromXML.initialization(serializedRequest);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// test that the new object is equal to the previous object using the
		// serialized output of the previous

		assertTrue(reqFromXML.equals(req));

		// create a response event and initialize it from a serialized request
		// object

		UpdateFederationExecutionTimeResponse response = new UpdateFederationExecutionTimeResponse();

		try {
			response.initialization(serializedRequest);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		response.setUpdateFederationExecutionTimeResponseAck(true);

		String serializedResponse = response.serialize();

		assertNotNull(serializedResponse);
		assertFalse("".equalsIgnoreCase(serializedResponse));

		UpdateFederationExecutionTimeResponse responseFromXML = new UpdateFederationExecutionTimeResponse();

		try {
			responseFromXML.initialization(serializedResponse);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assert (responseFromXML.equals(response));

		writeToFile(response, false);
	}

}
