/**
 * 
 */
package com.csc.muthur.server.model.event.data.flight;

import com.csc.muthur.server.model.event.AbstractModelTest;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class FlightPositionTest extends AbstractModelTest {

	private static final String tailNumber = "N485UA";
	private static final String acid = "DAL333";

	private double verticalspeedKts = 405;
	private boolean aircraftOnGround = false;
	private int aircraftTransmissionFrequency = 2850;

	private String squawkCode = "0363"; // four characters
	private boolean ident = true; // true if the A/C has been IDENT'd

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
	 * {@link com.csc.muthur.server.model.event.data.flight.FlightPosition#FlightPositionData(java.lang.String, java.lang.String)}
	 * .
	 */
	public void testFlightPositionDataStringString() {

		FlightPosition fpd = null;
		try {
			fpd = new FlightPosition(tailNumber, acid);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(fpd);

		assertEquals(fpd.getCallSign(), acid);
		assertEquals(fpd.getTailNumber(), tailNumber);
		assertNotNull(fpd.getDataObjectUUID());
		assertFalse(fpd.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(fpd.getObjectCreateTimeMSecs() != 0L);

		try {
			fpd = new FlightPosition(tailNumber, null);
			fail("Should have thrown exception for null call sign");
		} catch (Exception e) {
		}

		try {
			fpd = new FlightPosition(null, acid);
			fail("Should have thrown exception for null tail number");
		} catch (Exception e) {
		}

	}

	/**
	 * Test method for
	 * {@link com.csc.muthur.server.model.event.data.flight.FlightPosition#FlightPositionData()}
	 * .
	 */
	public void testFlightPositionData() {

		FlightPosition fpd = new FlightPosition();
		assertNotNull(fpd);

	}

	public void testFlightPositionDataGetDocument() {

		FlightPosition flightPosition = null;

		try {
			flightPosition = new FlightPosition(tailNumber, acid);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(flightPosition);

		assertEquals(flightPosition.getCallSign(), acid);
		assertEquals(flightPosition.getTailNumber(), tailNumber);
		assertNotNull(flightPosition.getDataObjectUUID());
		assertFalse(flightPosition.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(flightPosition.getObjectCreateTimeMSecs() != 0L);

		// position data

		flightPosition.setLatitudeDegrees(42.65);
		flightPosition.setLongitudeDegrees(-76.72);
		flightPosition.setAltitudeFt(30000);
		flightPosition.setGroundspeedKts(400);
		flightPosition.setHeadingDegrees(90);
		flightPosition.setAirspeedKts(444);
		flightPosition.setPitchDegrees(0.5);
		flightPosition.setRollDegrees(0);
		flightPosition.setYawDegrees(0);
		flightPosition.setSector("ZOB48");
		flightPosition.setCenter("ZOB");

		flightPosition.setVerticalspeedKts(verticalspeedKts);
		flightPosition.setAircraftOnGround(aircraftOnGround);
		flightPosition
				.setAircraftTransmissionFrequency(aircraftTransmissionFrequency);
		flightPosition.setSquawkCode(squawkCode);
		flightPosition.setIdent(ident);

		// serialize and validate

		String dataAsXml = flightPosition.serialize();
		assertNotNull(dataAsXml);
		assertFalse("".equalsIgnoreCase(dataAsXml));

		writeToFile(flightPosition, false);

		// create a new object and initialize it from the serialized XML

		FlightPosition pfdFromXML = new FlightPosition();
		try {
			pfdFromXML.initialization(dataAsXml);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(flightPosition);

		// check that the objects are equal after initialization from the XML

		assertTrue(pfdFromXML.equals(flightPosition));

		assertEquals(pfdFromXML.getCallSign(), flightPosition.getCallSign());
		assertEquals(pfdFromXML.getTailNumber(), flightPosition.getTailNumber());
		assertEquals(pfdFromXML.getLatitudeDegrees(),
				flightPosition.getLatitudeDegrees());

		assertNotNull(pfdFromXML.getDataObjectUUID());
		assertFalse(pfdFromXML.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(pfdFromXML.getObjectCreateTimeMSecs() != 0L);

	}

	/**
	 * 
	 */
	public void testFlightPlanValidationSuccess() {

		FlightPosition flightPosition = null;

		try {
			flightPosition = new FlightPosition(tailNumber, acid);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(flightPosition);

		// position data

		flightPosition.setLatitudeDegrees(42.65);
		flightPosition.setLongitudeDegrees(-76.72);
		flightPosition.setAltitudeFt(30000);
		flightPosition.setGroundspeedKts(400);
		flightPosition.setHeadingDegrees(90);
		flightPosition.setAirspeedKts(444);
		flightPosition.setPitchDegrees(0.5);
		flightPosition.setRollDegrees(0);
		flightPosition.setYawDegrees(0);
		flightPosition.setSector("ZOB48");
		flightPosition.setCenter("ZOB");

		flightPosition.setVerticalspeedKts(verticalspeedKts);
		flightPosition.setAircraftOnGround(aircraftOnGround);
		flightPosition
				.setAircraftTransmissionFrequency(aircraftTransmissionFrequency);
		flightPosition.setSquawkCode(squawkCode);
		flightPosition.setIdent(ident);

		assertTrue(flightPosition.validate());

	}

}
