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
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class AircraftTaxiInTest extends AbstractModelTest {

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

	/*
	 * 
	 */
	@Test
	public void testGetDataBlockElement() {

		AircraftTaxiIn aircraftTaxiInData = null;

		try {
			aircraftTaxiInData = new AircraftTaxiIn(tailNumber, acid);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(aircraftTaxiInData);

		assertEquals(aircraftTaxiInData.getCallSign(), acid);
		assertEquals(aircraftTaxiInData.getTailNumber(), tailNumber);
		assertNotNull(aircraftTaxiInData.getDataObjectUUID());
		assertFalse(aircraftTaxiInData.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(aircraftTaxiInData.getObjectCreateTimeMSecs() != 0L);

		// gate taxiing to

		aircraftTaxiInData.setTaxiInGate("D5");

		// taxi in data

		aircraftTaxiInData.setTaxiInTimeMSecs(System.currentTimeMillis() + 30);

		String dataAsXml = aircraftTaxiInData.serialize();
		assertNotNull(dataAsXml);
		assertFalse("".equalsIgnoreCase(dataAsXml));

		AircraftTaxiIn aircraftTaxiInFromXML = new AircraftTaxiIn();

		try {
			aircraftTaxiInFromXML.initialization(dataAsXml);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(aircraftTaxiInData);

		assertTrue(aircraftTaxiInFromXML.equals(aircraftTaxiInData));

		writeToFile(aircraftTaxiInFromXML, false);
	}

	/*
	 * 
	 */
	@Test
	public void testAircraftTaxiOutDataStringString() {

		AircraftTaxiIn aircraftTaxiInData = null;

		try {
			aircraftTaxiInData = new AircraftTaxiIn(tailNumber, acid);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(aircraftTaxiInData);

		assertEquals(aircraftTaxiInData.getCallSign(), acid);
		assertEquals(aircraftTaxiInData.getTailNumber(), tailNumber);
		assertNotNull(aircraftTaxiInData.getDataObjectUUID());
		assertFalse(aircraftTaxiInData.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(aircraftTaxiInData.getObjectCreateTimeMSecs() != 0L);

		try {
			aircraftTaxiInData = new AircraftTaxiIn(tailNumber, null);
			fail("Should have thrown exception for null call sign");
		} catch (Exception e) {
		}

		try {
			aircraftTaxiInData = new AircraftTaxiIn(null, acid);
			fail("Should have thrown exception for null tail number");
		} catch (Exception e) {
		}

	}

	/**
	 * 
	 */
	@Test
	public void testAircraftTaxiOutData() {
		assertNotNull(new AircraftTaxiOut());
	}

}
