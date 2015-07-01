/**
 * 
 */
package com.csc.muthur.server.model.event.data.flight;

import com.csc.muthur.server.model.event.AbstractModelTest;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class AircraftArrivalTest extends AbstractModelTest {

	private static final String tailNumber = "N485UA";
	private static final String acid = "DAL333";

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
	 * {@link com.csc.muthur.server.model.event.data.AircraftArrival#addDataElements()}
	 * .
	 */
	public void testGetDataBlockElement() {

		AircraftArrival acad = null;

		try {
			acad = new AircraftArrival(tailNumber, acid);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(acad);

		assertEquals(acad.getCallSign(), acid);
		assertEquals(acad.getTailNumber(), tailNumber);
		assertNotNull(acad.getDataObjectUUID());
		assertFalse(acad.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(acad.getObjectCreateTimeMSecs() != 0L);

		// arrival time

		acad.setActualArrivalTimeMSecs(System.currentTimeMillis() + 40);

		// arrival APs

		acad.setArrivalAirportCode("ORD");

		// set runway

		acad.setArrivalRunway("7R");

		// serialize the object and validate

		String dataAsXml = acad.serialize();
		assertNotNull(dataAsXml);
		assertFalse("".equalsIgnoreCase(dataAsXml));

		writeToFile(acad, false);

		// create an empty object to be used to initialize from the serialized form
		// of the previous object

		AircraftArrival acadFromXML = new AircraftArrival();

		// initialize the empty object from the serialized output of the first

		try {
			acadFromXML.initialization(dataAsXml);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(acad);

		// second object should equal the first

		assertTrue(acadFromXML.equals(acad));
	}

	/**
	 * Test method for
	 * {@link com.csc.muthur.server.model.event.data.AircraftArrival#AircraftArrivalData(java.lang.String, java.lang.String)}
	 * .
	 */
	public void testAircraftArrivalDataStringString() {

		AircraftArrival acad = null;

		try {
			acad = new AircraftArrival(tailNumber, acid);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(acad);

		assertEquals(acad.getCallSign(), acid);
		assertEquals(acad.getTailNumber(), tailNumber);
		assertNotNull(acad.getDataObjectUUID());
		assertFalse(acad.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(acad.getObjectCreateTimeMSecs() != 0L);

		try {
			acad = new AircraftArrival(tailNumber, null);
			fail("Should have thrown exception for null call sign");
		} catch (Exception e) {
		}

		try {
			acad = new AircraftArrival(null, acid);
			fail("Should have thrown exception for null tail number");
		} catch (Exception e) {
		}
	}

	/**
	 * Test method for
	 * {@link com.csc.muthur.server.model.event.data.AircraftArrival#AircraftArrivalData()}
	 * .
	 */
	public void testAircraftArrivalData() {
		assertNotNull(new AircraftArrival());
	}

}
