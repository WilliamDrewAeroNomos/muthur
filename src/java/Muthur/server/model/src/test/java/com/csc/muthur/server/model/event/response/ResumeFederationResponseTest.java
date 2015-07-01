/**
 * 
 */
package com.csc.muthur.server.model.event.response;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.event.AbstractModelTest;
import com.csc.muthur.server.model.event.request.ResumeFederationRequest;

/**
 * @author <a href=mailto:Nexsim@foo.com>Nexsim</a>
 * @version $Revision$
 */
public class ResumeFederationResponseTest extends AbstractModelTest {

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

	@Test
	public void testStopFederationResponse() {

		ResumeFederationRequest req = new ResumeFederationRequest();

		req.setTimeToLiveSecs(10);
		req.setSourceOfEvent(eventSourceName);

		assertNotNull(req);

		String serializedRequest = req.serialize();

		assertNotNull(serializedRequest);
		assertFalse("".equals(serializedRequest));

		// test creating a response event and initializing it from the serialized
		// request object

		ResumeFederationResponse response = new ResumeFederationResponse();
		response.setResumeFederationResponseAck(true);

		try {
			response.initialization(serializedRequest);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// serialize that response

		String responseAsXML = response.serialize();

		// test it's validity

		assertNotNull(responseAsXML);
		assertFalse("".equalsIgnoreCase(responseAsXML));

		// create a new one from the serialized version

		ResumeFederationResponse responseFromXML = new ResumeFederationResponse();

		// initialize the new response from a serialized version

		try {
			responseFromXML.initialization(responseAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// test that they are indeed equal

		assert (responseFromXML.equals(response));

		writeToFile(response, false);
	}

}
