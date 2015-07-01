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
public class FlightPlanTest extends AbstractModelTest {

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
	 * {@link com.csc.muthur.server.model.event.data.flight.FlightPlan#addDataElements()}
	 * .
	 */
	public void testGetDataBlockElement() {

		FlightPlan flightPlan = null;

		try {
			flightPlan = new FlightPlan(tailNumber, acid);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(flightPlan);

		assertEquals(flightPlan.getCallSign(), acid);
		assertEquals(flightPlan.getTailNumber(), tailNumber);
		assertNotNull(flightPlan.getDataObjectUUID());
		assertFalse(flightPlan.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(flightPlan.getObjectCreateTimeMSecs() != 0L);

		// flight plan data

		flightPlan.setSource("Flight");
		flightPlan.setAircraftType("B771");

		flightPlan.setPlannedDepartureTimeMSecs(System.currentTimeMillis() + 10);
		flightPlan.setPlannedDepartureRunway("1R");
		flightPlan.setPlannedTaxiOutGate("D31");
		flightPlan.setDepartureCenter("ZID");
		flightPlan.setDepartureFix("DEFIX");

		flightPlan.setPlannedArrivalTimeMSecs(System.currentTimeMillis() + 20);
		flightPlan.setPlannedArrivalRunway("8L");
		flightPlan.setPlannedTaxiInGate("C11");
		flightPlan.setArrivalCenter("ZBW");
		flightPlan.setArrivalFix("ARFIX");

		flightPlan.setCruiseSpeedKts(400);
		flightPlan.setCruiseAltitudeFt(330);
		flightPlan.setRoutePlan("IND..ROD.J29.PLB.J595.BGR..BGR");
		flightPlan.setPhysicalAircraftClass("J");
		flightPlan.setWeightAircraftClass("H");
		flightPlan.setUserAircraftClass("C");
		flightPlan.setNumOfAircraft(1);
		flightPlan.setAirborneEquipmentQualifier("R");
		flightPlan.setFlightPlanStatus(FlightPlanStatus.CANCELLED);

		String dataAsXml = flightPlan.serialize();
		assertNotNull(dataAsXml);
		assertFalse("".equalsIgnoreCase(dataAsXml));

		FlightPlan fpfdFromXML = new FlightPlan();
		try {
			fpfdFromXML.initialization(dataAsXml);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(flightPlan);

		assertTrue(fpfdFromXML.equals(flightPlan));

		writeToFile(flightPlan, false);
	}

	/**
	 * Test method for
	 * {@link com.csc.muthur.server.model.event.data.flight.FlightPlan#FlightPlanFiledData(java.lang.String, java.lang.String)}
	 * .
	 */
	public void testFlightPlanFiledDataStringString() {

		FlightPlan fpfd = null;
		try {
			fpfd = new FlightPlan(tailNumber, acid);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(fpfd);

		assertEquals(fpfd.getCallSign(), acid);
		assertEquals(fpfd.getTailNumber(), tailNumber);
		assertNotNull(fpfd.getDataObjectUUID());
		assertFalse(fpfd.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(fpfd.getObjectCreateTimeMSecs() != 0L);

		try {
			fpfd = new FlightPlan(tailNumber, null);
			fail("Should have thrown exception for null call sign");
		} catch (Exception e) {
		}

		try {
			fpfd = new FlightPlan(null, acid);
			fail("Should have thrown exception for null tail number");
		} catch (Exception e) {
		}

	}

	/**
	 * Test method for
	 * {@link com.csc.muthur.server.model.event.data.flight.FlightPlan#FlightPlanFiledData()}
	 * .
	 */
	public void testFlightPlanFiledData() {
		FlightPlan fpfd = new FlightPlan();
		assertNotNull(fpfd);
	}

	/**
	 * 
	 */
	public void testFlightPlanValidationSuccess() {

		FlightPlan flightPlan = null;

		try {
			flightPlan = new FlightPlan(tailNumber, acid);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(flightPlan);

		// flight plan data

		flightPlan.setSource("Flight");
		flightPlan.setAircraftType("B771");
		flightPlan.setDepartureAirportCode("ORD");
		flightPlan.setArrivalAirportCode("IAD");

		flightPlan.setPlannedDepartureTimeMSecs(System.currentTimeMillis() + 10);
		flightPlan.setPlannedDepartureRunway("1R");
		flightPlan.setPlannedTaxiOutGate("D31");
		flightPlan.setDepartureCenter("ZID");
		flightPlan.setDepartureFix("DEFIX");

		flightPlan.setPlannedArrivalTimeMSecs(System.currentTimeMillis() + 20);
		flightPlan.setPlannedArrivalRunway("8L");
		flightPlan.setPlannedTaxiInGate("C11");
		flightPlan.setArrivalCenter("ZBW");
		flightPlan.setArrivalFix("ARFIX");

		flightPlan.setCruiseSpeedKts(400);
		flightPlan.setCruiseAltitudeFt(330);
		flightPlan.setRoutePlan("IND..ROD.J29.PLB.J595.BGR..BGR");
		flightPlan.setPhysicalAircraftClass("J");
		flightPlan.setWeightAircraftClass("H");
		flightPlan.setUserAircraftClass("C");
		flightPlan.setNumOfAircraft(1);
		flightPlan.setAirborneEquipmentQualifier("R");
		flightPlan.setFlightPlanStatus(FlightPlanStatus.CANCELLED);

		assertTrue(flightPlan.validate());
	}

	/**
	 * 
	 */
	public void testFlightPlanValidationFailure() {

		FlightPlan flightPlan = null;

		try {
			flightPlan = new FlightPlan(tailNumber, acid);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(flightPlan);

		// flight plan data

		flightPlan.setSource("Flight");
		flightPlan.setAircraftType("B771");
		flightPlan.setDepartureAirportCode("ORD");
		// flightPlan.setArrivalAirportCode("IAD");

		flightPlan.setPlannedDepartureTimeMSecs(System.currentTimeMillis() + 10);
		flightPlan.setPlannedDepartureRunway("1R");
		flightPlan.setPlannedTaxiOutGate("D31");
		flightPlan.setDepartureCenter("ZID");
		flightPlan.setDepartureFix("DEFIX");

		flightPlan.setPlannedArrivalTimeMSecs(System.currentTimeMillis() + 20);
		flightPlan.setPlannedArrivalRunway("8L");
		flightPlan.setPlannedTaxiInGate("C11");
		flightPlan.setArrivalCenter("ZBW");
		flightPlan.setArrivalFix("ARFIX");

		flightPlan.setCruiseSpeedKts(400);
		flightPlan.setCruiseAltitudeFt(330);
		flightPlan.setRoutePlan("IND..ROD.J29.PLB.J595.BGR..BGR");
		flightPlan.setPhysicalAircraftClass("J");
		flightPlan.setWeightAircraftClass("H");
		flightPlan.setUserAircraftClass("C");
		flightPlan.setNumOfAircraft(1);
		flightPlan.setAirborneEquipmentQualifier("R");
		flightPlan.setFlightPlanStatus(FlightPlanStatus.CANCELLED);

		assertFalse(flightPlan.validate());

		flightPlan = null;

		try {
			flightPlan = new FlightPlan(tailNumber, acid);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(flightPlan);

		// flight plan data

		flightPlan.setSource("Flight");
		flightPlan.setAircraftType("B771");
		// flightPlan.setDepartureAirportCode("ORD");
		flightPlan.setArrivalAirportCode("IAD");

		flightPlan.setPlannedDepartureTimeMSecs(System.currentTimeMillis() + 10);
		flightPlan.setPlannedDepartureRunway("1R");
		flightPlan.setPlannedTaxiOutGate("D31");
		flightPlan.setDepartureCenter("ZID");
		flightPlan.setDepartureFix("DEFIX");

		flightPlan.setPlannedArrivalTimeMSecs(System.currentTimeMillis() + 20);
		flightPlan.setPlannedArrivalRunway("8L");
		flightPlan.setPlannedTaxiInGate("C11");
		flightPlan.setArrivalCenter("ZBW");
		flightPlan.setArrivalFix("ARFIX");

		flightPlan.setCruiseSpeedKts(400);
		flightPlan.setCruiseAltitudeFt(330);
		flightPlan.setRoutePlan("IND..ROD.J29.PLB.J595.BGR..BGR");
		flightPlan.setPhysicalAircraftClass("J");
		flightPlan.setWeightAircraftClass("H");
		flightPlan.setUserAircraftClass("C");
		flightPlan.setNumOfAircraft(1);
		flightPlan.setAirborneEquipmentQualifier("R");
		flightPlan.setFlightPlanStatus(FlightPlanStatus.CANCELLED);

		assertFalse(flightPlan.validate());
	}
}
