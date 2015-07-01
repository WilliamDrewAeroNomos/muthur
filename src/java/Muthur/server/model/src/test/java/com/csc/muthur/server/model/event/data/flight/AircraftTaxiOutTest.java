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
public class AircraftTaxiOutTest extends AbstractModelTest {

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
	 * {@link com.csc.muthur.server.model.event.data.flight.AircraftTaxiOut#addDataElements()} .
	 */
	@Test
	public void testGetDataBlockElement() {

		AircraftTaxiOut atod = null;

		try {
			atod = new AircraftTaxiOut(tailNumber, acid);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(atod);

		assertEquals(atod.getCallSign(), acid);
		assertEquals(atod.getTailNumber(), tailNumber);
		assertNotNull(atod.getDataObjectUUID());
		assertFalse(atod.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(atod.getObjectCreateTimeMSecs() != 0L);

		// gate taxiing from

		atod.setTaxiOutGate("C17");

		// taxi out data

		atod.setTaxiOutTimeMSecs(System.currentTimeMillis() + 30);

		String dataAsXml = atod.serialize();
		assertNotNull(dataAsXml);
		assertFalse("".equalsIgnoreCase(dataAsXml));

		AircraftTaxiOut atodFromXML = new AircraftTaxiOut();

		try {
			atodFromXML.initialization(dataAsXml);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(atod);

		assertTrue(atodFromXML.equals(atod));

		writeToFile(atodFromXML, false);
	}

	/**
	 * Test method for
	 * {@link com.csc.muthur.server.model.event.data.flight.AircraftTaxiOut#AircraftTaxiOutData(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testAircraftTaxiOutDataStringString() {

		AircraftTaxiOut atod = null;

		try {
			atod = new AircraftTaxiOut(tailNumber, acid);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(atod);

		assertEquals(atod.getCallSign(), acid);
		assertEquals(atod.getTailNumber(), tailNumber);
		assertNotNull(atod.getDataObjectUUID());
		assertFalse(atod.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(atod.getObjectCreateTimeMSecs() != 0L);

		try {
			atod = new AircraftTaxiOut(tailNumber, null);
			fail("Should have thrown exception for null call sign");
		} catch (Exception e) {
		}

		try {
			atod = new AircraftTaxiOut(null, acid);
			fail("Should have thrown exception for null tail number");
		} catch (Exception e) {
		}

	}

	/**
	 * Test method for
	 * {@link com.csc.muthur.server.model.event.data.flight.AircraftTaxiOut#AircraftTaxiOutData()}
	 * .
	 */
	@Test
	public void testAircraftTaxiOutData() {
		assertNotNull(new AircraftTaxiOut());
	}

}
