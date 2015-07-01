/**
 * 
 */
package com.csc.muthur.server.model.event;

import java.util.UUID;

import com.csc.muthur.server.commons.exception.MuthurException;

/**
 * @author williamdrew
 * 
 */
public class FederateDeregistrationEventTest extends AbstractModelTest {

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

	/*
	 * 
	 */
	public void testFederateDeregistrationEvent() {

		String terminationReason = "Normal termination";
		String federationExecutionHandle = UUID.randomUUID().toString();
		String federateRegistrationHandle = UUID.randomUUID().toString();
		String federationExecutionModelHandle = UUID.randomUUID().toString();

		FederateDeregistrationEvent event = new FederateDeregistrationEvent();

		event.setSourceOfEvent("NexSim");

		event.setFederateRegistrationHandle(federateRegistrationHandle);
		event.setFederationExecutionModelHandle(federationExecutionModelHandle);
		event.setFederationExecutionHandle(federationExecutionHandle);
		event.setFederateName("NexSim");

		event.setTerminationReason(terminationReason);

		String xml = event.serialize();

		assertNotNull(xml);
		assertFalse("".equals(xml));

		FederateDeregistrationEvent eventFromXML =
				new FederateDeregistrationEvent();

		try {
			eventFromXML.initialization(xml);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(eventFromXML.equals(event));

		writeToFile(eventFromXML, false);
	}

}
