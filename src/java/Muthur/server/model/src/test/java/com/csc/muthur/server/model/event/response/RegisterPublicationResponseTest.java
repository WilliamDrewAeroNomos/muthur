/**
 * 
 */
package com.csc.muthur.server.model.event.response;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.event.AbstractModelTest;
import com.csc.muthur.server.model.event.request.RegisterPublicationRequest;

/**
 * @author Nexsim
 * 
 */
public class RegisterPublicationResponseTest extends AbstractModelTest {

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for
	 * {@link com.csc.muthur.server.model.event.response.RegisterPublicationResponse#RegisterPublicationResponse()}
	 * .
	 */
	public void testRegisterPublicationResponse() {

		String eventSource = "NexSim";

		Set<String> classNames = new HashSet<String>();

		classNames.add("SpawnAircraft");
		classNames.add("FlightPositionUpdate");
		classNames.add("KillAircraft");

		RegisterPublicationRequest req = new RegisterPublicationRequest();

		req.setTimeToLiveSecs(10);
		req.setSourceOfEvent(eventSource);
		req.setFederationExecutionModelHandle(UUID.randomUUID().toString());
		req.setDataPublications(classNames);

		String reqAsXML = req.serialize();

		assertNotNull(reqAsXML);
		assertFalse(reqAsXML.equalsIgnoreCase(""));

		// construct a RegisterPublicationResponse

		RegisterPublicationResponse response = new RegisterPublicationResponse();

		try {
			response.initialization(reqAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		String responseAsXML = response.serialize();

		assertNotNull(responseAsXML);
		assertFalse(responseAsXML.equals(""));

		RegisterPublicationResponse reconstitutedResponse =
				new RegisterPublicationResponse();
		try {
			reconstitutedResponse.initialization(responseAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		String reconstitutedResponseAsXML = reconstitutedResponse.serialize();

		assertEquals(reconstitutedResponseAsXML, responseAsXML);

		reconstitutedResponse.addPublication("JustAnotherDataType");

		String responseWithAdditionalType = reconstitutedResponse.serialize();
		assertFalse(responseWithAdditionalType
				.equalsIgnoreCase(reconstitutedResponseAsXML));

		writeToFile(response, false);
	}

}
