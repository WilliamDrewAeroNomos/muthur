/**
 * 
 */
package com.csc.muthur.server.model.event;

import java.util.UUID;

import com.csc.muthur.server.commons.MessageDestination;
import com.csc.muthur.server.commons.exception.MuthurException;

/**
 * @author williamdrew
 * 
 */
public class ObjectOwnershipRelinquishedEventTest extends AbstractModelTest {

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
	 * {@link com.csc.muthur.server.model.event.FederationTerminationEvent#FederationTerminationEvent()}
	 * .
	 */
	public void testFederationTerminationEvent() {

		ObjectOwnershipRelinquishedEvent event =
				new ObjectOwnershipRelinquishedEvent();

		event.setSourceOfEvent(MessageDestination.MUTHUR);
		event.setDataObjectUUID(UUID.randomUUID().toString());
		event.setTimeToLiveSecs(10);
		event.setFederateRegistrationHandle(UUID.randomUUID().toString());
		event.setFederationExecutionHandle(UUID.randomUUID().toString());

		String eventAsXML = event.serialize();

		assertNotNull(eventAsXML);
		assertFalse("".equals(eventAsXML));

		ObjectOwnershipRelinquishedEvent fteFromXML =
				new ObjectOwnershipRelinquishedEvent();

		try {
			fteFromXML.initialization(eventAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(fteFromXML.equals(event));

		writeToFile(event, false);
	}

}
