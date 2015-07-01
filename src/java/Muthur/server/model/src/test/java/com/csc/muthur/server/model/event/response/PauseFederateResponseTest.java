/**
 * 
 */
package com.csc.muthur.server.model.event.response;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.event.AbstractModelTest;
import com.csc.muthur.server.model.event.request.PauseFederateRequest;

/**
 * @author <a href=mailto:Nexsim@foo.com>Nexsim</a>
 * @version $Revision$
 */
public class PauseFederateResponseTest extends AbstractModelTest {

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

		PauseFederateRequest req = new PauseFederateRequest();

		req.setTimeToLiveSecs(10);
		req.setSourceOfEvent(eventSourceName);

		assertNotNull(req);

		String serializedRequest = req.serialize();

		assertNotNull(serializedRequest);
		assertFalse("".equals(serializedRequest));

		// test the initialization of an PauseFederateRequest from serialized object

		PauseFederateRequest reqFromXML = new PauseFederateRequest();

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

		PauseFederateResponse federateResponse = new PauseFederateResponse();
		federateResponse.setPauseFederateResponseAck(false);

		try {
			federateResponse.initialization(serializedRequest);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		String responseAsXML = federateResponse.serialize();

		assertNotNull(responseAsXML);
		assertFalse("".equalsIgnoreCase(responseAsXML));

		PauseFederateResponse responseFromXML = new PauseFederateResponse();

		try {
			responseFromXML.initialization(responseAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assert (responseFromXML.equals(federateResponse));

		writeToFile(federateResponse, false);
	}

}
