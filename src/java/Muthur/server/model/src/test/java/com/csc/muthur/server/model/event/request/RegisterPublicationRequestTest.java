/**
 * 
 */
package com.csc.muthur.server.model.event.request;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.event.AbstractModelTest;

/**
 * @author Nexsim
 * 
 */
public class RegisterPublicationRequestTest extends AbstractModelTest {

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
	 * {@link com.csc.muthur.server.model.event.request.RegisterPublicationRequest#RegisterPublicationRequest()}
	 * .
	 */
	public void testRegisterPublicationRequest() {

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

		writeToFile(req, false);

		// test re-constitution

		RegisterPublicationRequest req2 = new RegisterPublicationRequest();

		try {
			req2.initialization(reqAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		String req2FromXML = req2.serialize();

		assertEquals(req2FromXML, reqAsXML);

	}

}
