/**
 * 
 */
package com.csc.muthur.server.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.mocks.MockMessage;
import com.csc.muthur.server.model.event.request.RegisterPublicationRequest;

/**
 * @author Nexsim
 * 
 */
public class FederationExecutionPublicationTest extends TestCase {

	private int timeOutSecs = 10;
	private String federateName = "NexSim";
	List<String> dataTypes = new Vector<String>();

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
	 * Test method for
	 * {@link com.csc.muthur.server.model.PublicationRegistrationFederationExecutionEntry#FederationExecutionPublication(com.csc.muthur.server.model.FederationExecutionModel, javax.jms.Message, com.csc.muthur.server.model.event.request.RegisterPublicationRequest)}
	 * .
	 */
	@Test
	public void testFederationExecutionPublication() {

		// test creating a valid object that should pass all tests
		Set<String> dataTypes = new HashSet<String>();

		dataTypes.add("SpawnAircraft");
		dataTypes.add("FlightPositionUpdate");
		dataTypes.add("KillAircraft");

		FederationExecutionModel fem = null;
		try {
			fem = new FederationExecutionModel("Testing");
		} catch (MuthurException e1) {
			fail(e1.getLocalizedMessage());
		}
		MockMessage mm = new MockMessage();

		RegisterPublicationRequest request = new RegisterPublicationRequest();

		request.setSourceOfEvent(federateName);
		request.setTimeToLiveSecs(timeOutSecs);
		request.setFederationExecutionModelHandle(fem.getFedExecModelUUID());

		if (dataTypes != null) {
			for (String name : dataTypes) {
				request.addPublication(name);
			}
		}

		PublicationRegistrationFederationExecutionEntry fep = null;

		try {
			fep = new PublicationRegistrationFederationExecutionEntry(fem, mm,
					request);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertEquals(fep.getFederateName(), federateName);
		assertEquals(fep.getDataTypeNames(), dataTypes);
		assertEquals(fep.getDataTypeNames().size(), dataTypes.size());
		assertEquals(fep.getFederationExecutionModel(), fem);
		assertEquals(fep.getEvent(), request);

		// test exception paths

		// null FEM
		request = new RegisterPublicationRequest();

		request.setSourceOfEvent(federateName);
		request.setTimeToLiveSecs(timeOutSecs);
		request.setFederationExecutionModelHandle(fem.getFedExecModelUUID());

		if (dataTypes != null) {
			for (String name : dataTypes) {
				request.addPublication(name);
			}
		}

		try {
			fep = new PublicationRegistrationFederationExecutionEntry(null, mm,
					request);
			fail("Should have thrown an exception");
		} catch (MuthurException e) {
		}

		// null Message

		try {
			fep = new PublicationRegistrationFederationExecutionEntry(fem, null,
					request);
			fail("Should have thrown an exception");
		} catch (MuthurException e) {
		}

		// empty federate name

		request = new RegisterPublicationRequest();

		request.setSourceOfEvent("");
		request.setTimeToLiveSecs(timeOutSecs);
		request.setFederationExecutionModelHandle(fem.getFedExecModelUUID());

		if (dataTypes != null) {
			for (String name : dataTypes) {
				request.addPublication(name);
			}
		}

		try {
			fep = new PublicationRegistrationFederationExecutionEntry(fem, mm,
					request);
			fail("Should have thrown an exception");
		} catch (MuthurException e) {
		}

		// empty list of data type names

		request = new RegisterPublicationRequest();

		request.setSourceOfEvent(federateName);
		request.setTimeToLiveSecs(timeOutSecs);
		request.setFederationExecutionModelHandle(fem.getFedExecModelUUID());

		try {
			fep = new PublicationRegistrationFederationExecutionEntry(fem, mm,
					request);
			// exception here since a Vector<String> is created
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

	}

}
