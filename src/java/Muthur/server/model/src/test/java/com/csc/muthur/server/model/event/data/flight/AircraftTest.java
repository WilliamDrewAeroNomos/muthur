/**
 * 
 */
package com.csc.muthur.server.model.event.data.flight;

import com.csc.muthur.server.model.event.AbstractModelTest;

/**
 * @author williamdrew
 * 
 */
public class AircraftTest extends AbstractModelTest {

	String tailNumber = "N481UA";
	String acid = "DAL333";
	String aircraftType = "B774";

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
	 * {@link com.csc.muthur.server.model.event.data.flight.Aircraft#SpawnAircraftData(java.lang.String, java.lang.String)}
	 * .
	 */
	public void testSpawnAircraftDataStringString() {

		Aircraft aircraft = null;
		try {
			aircraft = new Aircraft(tailNumber, acid);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(aircraft);

		assertEquals(aircraft.getCallSign(), acid);
		assertEquals(aircraft.getTailNumber(), tailNumber);
		assertNotNull(aircraft.getDataObjectUUID());
		assertFalse(aircraft.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(aircraft.getObjectCreateTimeMSecs() != 0L);

		try {
			aircraft = new Aircraft(tailNumber, null);
			fail("Should have thrown exception for null call sign");
		} catch (Exception e) {
		}

		try {
			aircraft = new Aircraft(null, acid);
			fail("Should have thrown exception for null tail number");
		} catch (Exception e) {
		}

		writeToFile(aircraft, false);
	}

	/**
	 * Test method for
	 * {@link com.csc.muthur.server.model.event.data.flight.Aircraft#SpawnAircraftData(java.lang.String, java.lang.String)}
	 * .
	 */
	public void testSpawnAircraftData() {

		Aircraft sacd = new Aircraft();
		assertNotNull(sacd);
	}

	public void testCreateAircraftDataGetDocument() {

		Aircraft sacd = null;

		try {
			sacd = new Aircraft(tailNumber, acid);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(sacd);

		assertEquals(sacd.getCallSign(), acid);
		assertEquals(sacd.getTailNumber(), tailNumber);
		assertNotNull(sacd.getDataObjectUUID());
		assertFalse(sacd.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(sacd.getObjectCreateTimeMSecs() != 0L);

		String dataAsXml = sacd.serialize();
		assertNotNull(dataAsXml);
		assertFalse("".equalsIgnoreCase(dataAsXml));

		Aircraft sacdFromXML = new Aircraft();
		try {
			sacdFromXML.initialization(dataAsXml);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(sacd);

		assertTrue(sacdFromXML.equals(sacd));
	}
}
