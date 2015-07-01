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
import com.csc.muthur.server.model.event.request.StartFederationRequest;

/**
 * @author <a href=mailto:Nexsim@foo.com>Nexsim</a>
 * @version $Revision$
 */
public class StartFederationResponseTest extends AbstractModelTest {

	private String eventSourceName = "NexSim";

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

	public void testStartResponse() {

		StartFederationRequest req = new StartFederationRequest();

		req.setTimeToLiveSecs(10);
		req.setSourceOfEvent(eventSourceName);
		req.setFederationExecutionModelHandle(UUID.randomUUID().toString());

		assertNotNull(req);

		String serializedRequest = req.serialize();

		assertNotNull(serializedRequest);
		assertFalse("".equals(serializedRequest));

		// test the initialization of an StartFederationRequest from serialized
		// object

		StartFederationRequest reqFromXML = new StartFederationRequest();

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

		StartFederationResponse response = new StartFederationResponse();
		response.setStartFederationResponseAck(true);

		try {
			response.initialization(serializedRequest);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		String responseAsXML = response.serialize();

		assertNotNull(responseAsXML);
		assertFalse("".equalsIgnoreCase(responseAsXML));

		StartFederationResponse responseFromXML = new StartFederationResponse();

		try {
			responseFromXML.initialization(responseAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assert (responseFromXML.equals(response));

		writeToFile(response, false);
	}

}
