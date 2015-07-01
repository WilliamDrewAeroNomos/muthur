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
import com.csc.muthur.server.model.event.request.PauseFederationRequest;

/**
 * @author <a href=mailto:Nexsim@foo.com>Nexsim</a>
 * @version $Revision$
 */
public class PauseFederationResponseTest extends AbstractModelTest {

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

	public void testPauseFederationResponse() {

		PauseFederationRequest req = new PauseFederationRequest();

		req.setFederationExecutionModelHandle(UUID.randomUUID().toString());
		req.setTimeToLiveSecs(10);
		req.setSourceOfEvent(eventSourceName);

		assertNotNull(req);

		String serializedRequest = req.serialize();

		assertNotNull(serializedRequest);
		assertFalse("".equals(serializedRequest));

		// test the initialization of a PauseFederateRequest from serialized object

		PauseFederationRequest reqFromXML = new PauseFederationRequest();

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

		PauseFederationResponse response = new PauseFederationResponse();
		response.setPauseFederationResponseAck(false);

		try {
			response.initialization(serializedRequest);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		String responseAsXML = response.serialize();

		assertNotNull(responseAsXML);
		assertFalse("".equalsIgnoreCase(responseAsXML));

		PauseFederationResponse responseFromXML = new PauseFederationResponse();

		try {
			responseFromXML.initialization(responseAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assert (responseFromXML.equals(response));

		writeToFile(response, false);
	}

}
