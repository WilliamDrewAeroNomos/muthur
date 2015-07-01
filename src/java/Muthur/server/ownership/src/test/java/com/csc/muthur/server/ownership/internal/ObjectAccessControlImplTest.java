/**
 * 
 */
package com.csc.muthur.server.ownership.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.AccessControl;
import com.csc.muthur.server.model.event.EventAttributeName;
import com.csc.muthur.server.model.event.data.airport.AirportConfiguration;
import com.csc.muthur.server.model.event.data.flight.Aircraft;
import com.csc.muthur.server.model.event.data.flight.AircraftArrival;
import com.csc.muthur.server.model.event.data.flight.AircraftDeparture;
import com.csc.muthur.server.model.event.data.flight.AircraftTaxiIn;
import com.csc.muthur.server.model.event.data.flight.AircraftTaxiOut;
import com.csc.muthur.server.model.event.data.flight.FlightPlan;
import com.csc.muthur.server.model.event.data.flight.FlightPosition;

/**
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class ObjectAccessControlImplTest {

	// objects

	private static Aircraft aircraft = null;
	private static FlightPlan flightPlan = null;
	private static FlightPosition flightPosition = null;
	private static AircraftDeparture aircraftDeparture = null;
	private static AircraftArrival aircraftArrival = null;
	private static AircraftTaxiIn aircraftTaxiIn = null;
	private static AircraftTaxiOut aircraftTaxiOut = null;

	private String tailNumber = "N481UA";
	private String callSign = "DAL333";

	// attributes that will be set
	//
	private String aircraftType = "B774";
	private String departureAirportCode = "JFK";
	private String departureRunwayName = "8L";
	private String arrivalRunwayName = "7R";
	private String arrivalAirportCode = "ORD";
	private double latitudeDegrees = 42.65;
	private double longitudeDegrees = -76.72;
	private double altitudeFeet = 30000;
	private double groundSpeedKts = 400;
	private double headingDegrees = 90;
	private double airSpeedKts = 444;
	private double pitchDegrees = 0.5;
	private double rollDegrees = 10;
	private double yawDegrees = 5;
	private String sectorName = "ZOB48";
	private String centerName = "ZOB";
	private double verticalspeedKts = 300;
	private String source = "Flight";
	private double cruiseSpeedKts = 330;
	private double cruiseAltitudeFeet = 33000;
	private String route = "IND..ROD.J29.PLB.J595.BGR..BGR";
	private String departureCenter = "ZID";
	private String arrivalCenter = "ZBW";
	private String departureFix = "DEFIX";
	private String arrivalFix = "ARFIX";
	private String physicalClass = "J";
	private String weightClass = "H";
	private String userClass = "C";
	private String airborneEquipmentQualifier = "R";
	private int aircraftTransmissionFrequency = 2850;
	private String squawkCode = "D013";

	private String taxiInGate = "D27";
	private AirportConfiguration airportConfiguration;
	private String airportCode = "KIAD";

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

	@Test
	public void testObjectAccessControlImplWithAirportConfiguration() {

		airportConfiguration = null;

		try {
			airportConfiguration = new AirportConfiguration(airportCode);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(airportConfiguration);

		assertEquals(airportConfiguration.getAirportCode(), airportCode);

		ObjectAttributeAccessControl oac = null;

		try {
			oac = new ObjectAttributeAccessControl(airportConfiguration);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(oac);
	}

	@Test
	public void testObjectAccessControlImplWithAircraft() {

		aircraft = null;

		try {
			aircraft = new Aircraft(tailNumber, callSign);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(aircraft);

		assertEquals(aircraft.getCallSign(), callSign);
		assertEquals(aircraft.getTailNumber(), tailNumber);
		assertNotNull(aircraft.getDataObjectUUID());
		assertFalse(aircraft.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(aircraft.getObjectCreateTimeMSecs() != 0L);

		ObjectAttributeAccessControl oac = null;

		try {
			oac = new ObjectAttributeAccessControl(aircraft);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(oac);
	}

	@Test
	public void testObjectAccessControlImplWithFlightPlan() {

		flightPlan = null;

		try {
			flightPlan = new FlightPlan(tailNumber, callSign);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(flightPlan);

		assertEquals(flightPlan.getCallSign(), callSign);
		assertEquals(flightPlan.getTailNumber(), tailNumber);
		assertNotNull(flightPlan.getDataObjectUUID());
		assertFalse(flightPlan.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(flightPlan.getObjectCreateTimeMSecs() != 0L);

		try {
			flightPlan = new FlightPlan(tailNumber, callSign);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		flightPlan.setSource(source);
		flightPlan.setAircraftType(aircraftType);
		flightPlan.setPlannedDepartureTimeMSecs(System.currentTimeMillis() + 10);
		flightPlan.setPlannedArrivalTimeMSecs(System.currentTimeMillis() + 20);
		flightPlan.setCruiseSpeedKts(cruiseSpeedKts);
		flightPlan.setCruiseAltitudeFt(cruiseAltitudeFeet);
		flightPlan.setRoutePlan(route);
		flightPlan.setDepartureCenter(departureCenter);
		flightPlan.setArrivalCenter(arrivalCenter);
		flightPlan.setDepartureFix(departureFix);
		flightPlan.setArrivalFix(arrivalFix);
		flightPlan.setPhysicalAircraftClass(physicalClass);
		flightPlan.setWeightAircraftClass(weightClass);
		flightPlan.setUserAircraftClass(userClass);
		flightPlan.setNumOfAircraft(1);
		flightPlan.setAirborneEquipmentQualifier(airborneEquipmentQualifier);

		ObjectAttributeAccessControl oac = null;

		try {
			oac = new ObjectAttributeAccessControl(flightPlan);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(oac);

	}

	/**
	 * 
	 */
	@Test
	public void testObjectAccessControlImplWithFlightPosition() {

		flightPosition = null;

		try {
			flightPosition = new FlightPosition(tailNumber, callSign);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(flightPosition);

		assertEquals(flightPosition.getCallSign(), callSign);
		assertEquals(flightPosition.getTailNumber(), tailNumber);
		assertNotNull(flightPosition.getDataObjectUUID());
		assertFalse(flightPosition.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(flightPosition.getObjectCreateTimeMSecs() != 0L);

		flightPosition.setLatitudeDegrees(latitudeDegrees);
		flightPosition.setLongitudeDegrees(longitudeDegrees);
		flightPosition.setAltitudeFt(altitudeFeet);
		flightPosition.setGroundspeedKts(groundSpeedKts);
		flightPosition.setHeadingDegrees(headingDegrees);
		flightPosition.setAirspeedKts(airSpeedKts);
		flightPosition.setPitchDegrees(pitchDegrees);
		flightPosition.setRollDegrees(rollDegrees);
		flightPosition.setYawDegrees(yawDegrees);
		flightPosition.setSector(sectorName);
		flightPosition.setCenter(centerName);
		flightPosition.setVerticalspeedKts(verticalspeedKts);
		flightPosition.setAircraftOnGround(true);
		flightPosition
				.setAircraftTransmissionFrequency(aircraftTransmissionFrequency);
		flightPosition.setSquawkCode(squawkCode);
		flightPosition.setIdent(true);

		ObjectAttributeAccessControl oac = null;

		try {
			oac = new ObjectAttributeAccessControl(flightPosition);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(oac);
	}

	/**
	 * 
	 */
	@Test
	public void testObjectAccessControlImplWithAircraftDeparture() {

		aircraftDeparture = null;

		try {
			aircraftDeparture = new AircraftDeparture(tailNumber, callSign);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(aircraftDeparture);

		assertEquals(aircraftDeparture.getCallSign(), callSign);
		assertEquals(aircraftDeparture.getTailNumber(), tailNumber);
		assertNotNull(aircraftDeparture.getDataObjectUUID());
		assertFalse(aircraftDeparture.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(aircraftDeparture.getObjectCreateTimeMSecs() != 0L);

		aircraftDeparture.setActualDepartureTimeMSecs(System.currentTimeMillis());
		aircraftDeparture.setDepartureAirportCode(departureAirportCode);
		aircraftDeparture.setDepartureRunway(departureRunwayName);

		ObjectAttributeAccessControl oac = null;

		try {
			oac = new ObjectAttributeAccessControl(aircraftDeparture);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(oac);
	}

	/**
	 * 
	 */
	@Test
	public void testObjectAccessControlImplWithAircraftArrival() {

		aircraftArrival = null;

		try {
			aircraftArrival = new AircraftArrival(tailNumber, callSign);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(aircraftArrival);

		assertEquals(aircraftArrival.getCallSign(), callSign);
		assertEquals(aircraftArrival.getTailNumber(), tailNumber);
		assertNotNull(aircraftArrival.getDataObjectUUID());
		assertFalse(aircraftArrival.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(aircraftArrival.getObjectCreateTimeMSecs() != 0L);

		aircraftArrival.setActualArrivalTimeMSecs(System.currentTimeMillis());
		aircraftArrival.setArrivalAirportCode(arrivalAirportCode);
		aircraftArrival.setArrivalRunway(arrivalRunwayName);

		ObjectAttributeAccessControl oac = null;

		try {
			oac = new ObjectAttributeAccessControl(aircraftArrival);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(oac);
	}

	/**
	 * 
	 */
	@Test
	public void testObjectAccessControlImplWithAircraftTaxiIn() {

		aircraftTaxiIn = null;

		try {
			aircraftTaxiIn = new AircraftTaxiIn(tailNumber, callSign);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(aircraftTaxiIn);

		assertEquals(aircraftTaxiIn.getCallSign(), callSign);
		assertEquals(aircraftTaxiIn.getTailNumber(), tailNumber);
		assertNotNull(aircraftTaxiIn.getDataObjectUUID());
		assertFalse(aircraftTaxiIn.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(aircraftTaxiIn.getObjectCreateTimeMSecs() != 0L);

		aircraftTaxiIn.setTaxiInTimeMSecs(System.currentTimeMillis());
		aircraftTaxiIn.setTaxiInGate(taxiInGate);

		ObjectAttributeAccessControl oac = null;

		try {
			oac = new ObjectAttributeAccessControl(aircraftTaxiIn);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(oac);
	}

	/**
	 * 
	 */
	@Test
	public void testObjectAccessControlImplWithAircraftTaxiOut() {

		aircraftTaxiOut = null;

		try {
			aircraftTaxiOut = new AircraftTaxiOut(tailNumber, callSign);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(aircraftTaxiOut);

		assertEquals(aircraftTaxiOut.getCallSign(), callSign);
		assertEquals(aircraftTaxiOut.getTailNumber(), tailNumber);
		assertNotNull(aircraftTaxiOut.getDataObjectUUID());
		assertFalse(aircraftTaxiOut.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(aircraftTaxiOut.getObjectCreateTimeMSecs() != 0L);

		aircraftTaxiOut.setTaxiOutTimeMSecs(System.currentTimeMillis());
		aircraftTaxiOut.setTaxiOutGate(taxiInGate);

		ObjectAttributeAccessControl oac = null;

		try {
			oac = new ObjectAttributeAccessControl(aircraftTaxiOut);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(oac);
	}

	/**
	 * UPDATE Access controls
	 * 
	 */

	@Test
	public void testObjectAccessControlImplUpdateACsFlightPlan() {

		flightPlan = null;

		try {
			flightPlan = new FlightPlan(tailNumber, callSign);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(flightPlan);

		assertEquals(flightPlan.getCallSign(), callSign);
		assertEquals(flightPlan.getTailNumber(), tailNumber);
		assertNotNull(flightPlan.getDataObjectUUID());
		assertFalse(flightPlan.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(flightPlan.getObjectCreateTimeMSecs() != 0L);

		try {
			flightPlan = new FlightPlan(tailNumber, callSign);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		flightPlan.setSource(source);
		flightPlan.setAircraftType(aircraftType);
		flightPlan.setPlannedDepartureTimeMSecs(System.currentTimeMillis() + 10);
		flightPlan.setPlannedArrivalTimeMSecs(System.currentTimeMillis() + 20);
		flightPlan.setCruiseSpeedKts(cruiseSpeedKts);
		flightPlan.setCruiseAltitudeFt(cruiseAltitudeFeet);
		flightPlan.setRoutePlan(route);
		flightPlan.setDepartureCenter(departureCenter);
		flightPlan.setArrivalCenter(arrivalCenter);
		flightPlan.setDepartureFix(departureFix);
		flightPlan.setArrivalFix(arrivalFix);
		flightPlan.setPhysicalAircraftClass(physicalClass);
		flightPlan.setWeightAircraftClass(weightClass);
		flightPlan.setUserAircraftClass(userClass);
		flightPlan.setNumOfAircraft(1);
		flightPlan.setAirborneEquipmentQualifier(airborneEquipmentQualifier);

		ObjectAttributeAccessControl oac = null;

		try {
			oac = new ObjectAttributeAccessControl(flightPlan);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(oac);

		Map<String, AccessControl> attributeNameToAccessControlMap =
				new ConcurrentHashMap<String, AccessControl>();

		// update the access controls

		attributeNameToAccessControlMap.put(EventAttributeName.source.toString(),
				AccessControl.READ_WRITE);
		attributeNameToAccessControlMap.put(
				EventAttributeName.aircraftType.toString(), AccessControl.READ_WRITE);
		attributeNameToAccessControlMap.put(
				EventAttributeName.routePlan.toString(), AccessControl.READ_WRITE);
		attributeNameToAccessControlMap.put(
				EventAttributeName.plannedDepartureTimeMSecs.toString(),
				AccessControl.READ_WRITE);

		oac.updateAttributeAccessControls(attributeNameToAccessControlMap);

		assertTrue(oac.isReadWrite(EventAttributeName.source.toString()));
		assertTrue(oac.isReadWrite(EventAttributeName.aircraftType.toString()));
		assertTrue(oac.isReadWrite(EventAttributeName.routePlan.toString()));
		assertTrue(oac.isReadWrite(EventAttributeName.plannedDepartureTimeMSecs
				.toString()));

		// test using fields that don't exist for flight plan mixed with valid
		// fields
		//

		attributeNameToAccessControlMap.clear();

		attributeNameToAccessControlMap.put(
				EventAttributeName.plannedDepartureTimeMSecs.toString(),
				AccessControl.READ_WRITE);
		attributeNameToAccessControlMap
				.put(EventAttributeName.departureCenter.toString(),
						AccessControl.READ_WRITE);
		attributeNameToAccessControlMap.put(
				EventAttributeName.aircraftOnGround.toString(),
				AccessControl.READ_WRITE);

		oac.updateAttributeAccessControls(attributeNameToAccessControlMap);

		assertTrue(oac.isReadWrite(EventAttributeName.plannedDepartureTimeMSecs
				.toString()));
		assertTrue(oac.isReadWrite(EventAttributeName.departureCenter.toString()));

		// this is NOT a valid field so should return false
		assertFalse(oac.isReadWrite(EventAttributeName.aircraftOnGround.toString()));
	}
}
