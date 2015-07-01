/**
 * 
 */
package com.csc.muthur.server.model.event;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.event.data.RunwayFlow;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>NexSim</a>
 * @version $Revision$
 * 
 */
public class RunwayAssignmentEventTest extends AbstractModelTest {

	private static final String eventSourceName = "TestFederate";

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

	/*
	 * 
	 */
	@Test
	public void testRunwayAssignmentEventStandardCreation() {

		RunwayAssignmentEvent runwayAssignmentEvent = new RunwayAssignmentEvent();
		runwayAssignmentEvent.setSourceOfEvent(eventSourceName);

		runwayAssignmentEvent.setRunwayFlow(RunwayFlow.ARRIVAL);
		runwayAssignmentEvent.setRunwayName("01R");

		String xml = runwayAssignmentEvent.serialize();

		assertNotNull(xml);
		assertFalse("".equals(xml));

		RunwayAssignmentEvent runwayAssignmentEventFromXML =
				new RunwayAssignmentEvent();

		try {
			runwayAssignmentEventFromXML.initialization(xml);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(runwayAssignmentEventFromXML.equals(runwayAssignmentEvent));

		writeToFile(runwayAssignmentEvent, false);
	}

	/*
	 * 
	 */
	@Test
	public void testRunwayAssignmentEventTestCreationWithDefaults() {

		RunwayAssignmentEvent runwayAssignmentEvent = new RunwayAssignmentEvent();
		runwayAssignmentEvent.setSourceOfEvent(eventSourceName);

		String xml = runwayAssignmentEvent.serialize();

		assertNotNull(xml);
		assertFalse("".equals(xml));

		RunwayAssignmentEvent runwayAssignmentEventFromXML =
				new RunwayAssignmentEvent();

		try {
			runwayAssignmentEventFromXML.initialization(xml);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(runwayAssignmentEventFromXML.equals(runwayAssignmentEvent));

	}

	/*
	 * 
	 */
	@Test
	public void testRunwayAssignmentEventTestDeparture() {

		RunwayAssignmentEvent runwayAssignmentEvent = new RunwayAssignmentEvent();
		runwayAssignmentEvent.setSourceOfEvent(eventSourceName);

		runwayAssignmentEvent.setRunwayFlow(RunwayFlow.DEPARTURE);
		runwayAssignmentEvent.setRunwayName("18L");

		String xml = runwayAssignmentEvent.serialize();

		assertNotNull(xml);
		assertFalse("".equals(xml));

		RunwayAssignmentEvent runwayAssignmentEventFromXML =
				new RunwayAssignmentEvent();

		try {
			runwayAssignmentEventFromXML.initialization(xml);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(runwayAssignmentEventFromXML.equals(runwayAssignmentEvent));

	}

	/*
	 * 
	 */
	@Test
	public void testRunwayAssignmentEventTestArrival() {

		RunwayAssignmentEvent runwayAssignmentEvent = new RunwayAssignmentEvent();
		runwayAssignmentEvent.setSourceOfEvent(eventSourceName);

		runwayAssignmentEvent.setRunwayFlow(RunwayFlow.ARRIVAL);
		runwayAssignmentEvent.setRunwayName("9C");

		String xml = runwayAssignmentEvent.serialize();

		assertNotNull(xml);
		assertFalse("".equals(xml));

		RunwayAssignmentEvent runwayAssignmentEventFromXML =
				new RunwayAssignmentEvent();

		try {
			runwayAssignmentEventFromXML.initialization(xml);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(runwayAssignmentEventFromXML.equals(runwayAssignmentEvent));

	}

}
