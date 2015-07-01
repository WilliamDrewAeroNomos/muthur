/**
 * 
 */
package com.csc.muthur.server.model.event;

import java.util.UUID;

import com.csc.muthur.server.commons.MessageDestination;
import com.csc.muthur.server.commons.exception.MuthurException;

/**
 * @author <a href=mailto:Nexsim-support@csc.com>NexSim</a>
 * @version $Revision$
 * 
 */
public class FederationTerminationEventTest extends AbstractModelTest {

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

		String terminationReason =
				"Federation execution terminated due to time out";
		String federationExecutionHandle = UUID.randomUUID().toString();

		FederationTerminationEvent fte = new FederationTerminationEvent();

		fte.setSourceOfEvent(MessageDestination.MUTHUR);

		fte.setTerminationReason(terminationReason);

		fte.setFederationExecutionHandle(federationExecutionHandle.toString());

		String xml = fte.serialize();

		assertNotNull(xml);
		assertFalse("".equals(xml));

		FederationTerminationEvent fteFromXML = new FederationTerminationEvent();

		try {
			fteFromXML.initialization(xml);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(fteFromXML.equals(fte));

		writeToFile(fteFromXML, false);
	}

}
