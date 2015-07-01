/**
 * 
 */
package com.csc.muthur.server.model.event.data.flight;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.csc.muthur.server.model.event.AbstractModelTest;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class AircraftDepartureTest extends AbstractModelTest {

	private static final String tailNumber = "N485UA";
	private static final String acid = "DAL333";

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
	 * {@link com.csc.muthur.server.model.event.data.flight.AircraftDeparture#addDataElements()}
	 * .
	 */
	@Test
	public void testGetDataBlockElement() {

		AircraftDeparture aircraftDeparture = null;

		try {
			aircraftDeparture = new AircraftDeparture(tailNumber, acid);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(aircraftDeparture);

		assertEquals(aircraftDeparture.getCallSign(), acid);
		assertEquals(aircraftDeparture.getTailNumber(), tailNumber);
		assertNotNull(aircraftDeparture.getDataObjectUUID());
		assertFalse(aircraftDeparture.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(aircraftDeparture.getObjectCreateTimeMSecs() != 0L);

		// departure runway

		aircraftDeparture.setDepartureRunway("8L");

		// departure AP

		aircraftDeparture.setDepartureAirportCode("JFK");

		// departure time

		aircraftDeparture
				.setActualDepartureTimeMSecs(System.currentTimeMillis() + 40);

		String dataAsXml = aircraftDeparture.serialize();
		assertNotNull(dataAsXml);
		assertFalse("".equalsIgnoreCase(dataAsXml));

		AircraftDeparture acddFromXML = new AircraftDeparture();
		try {
			acddFromXML.initialization(dataAsXml);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(aircraftDeparture);

		assertTrue(acddFromXML.equals(aircraftDeparture));

		writeToFile(acddFromXML, false);
	}

	/**
	 * Test method for
	 * {@link com.csc.muthur.server.model.event.data.flight.AircraftDeparture#AircraftDepartureData(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testAircraftDepartureDataStringString() {

		AircraftDeparture sacd = null;
		try {
			sacd = new AircraftDeparture(tailNumber, acid);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(sacd);

		assertEquals(sacd.getCallSign(), acid);
		assertEquals(sacd.getTailNumber(), tailNumber);
		assertNotNull(sacd.getDataObjectUUID());
		assertFalse(sacd.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(sacd.getObjectCreateTimeMSecs() != 0L);

		try {
			sacd = new AircraftDeparture(tailNumber, null);
			fail("Should have thrown exception for null call sign");
		} catch (Exception e) {
		}

		try {
			sacd = new AircraftDeparture(null, acid);
			fail("Should have thrown exception for null tail number");
		} catch (Exception e) {
		}
	}

	/**
	 * Test method for
	 * {@link com.csc.muthur.server.model.event.data.flight.AircraftDeparture#AircraftDepartureData()}
	 * .
	 */
	@Test
	public void testAircraftDepartureData() {
		assertNotNull(new AircraftDeparture());
	}

}
