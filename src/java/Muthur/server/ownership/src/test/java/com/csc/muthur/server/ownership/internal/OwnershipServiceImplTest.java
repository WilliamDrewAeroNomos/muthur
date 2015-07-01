/**
 * 
 */
package com.csc.muthur.server.ownership.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.csc.muthur.server.commons.CommonsService;
import com.csc.muthur.server.commons.FederationExecutionID;
import com.csc.muthur.server.commons.ObjectOwnershipID;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.commons.internal.CommonsServiceImpl;
import com.csc.muthur.server.configuration.ConfigurationService;
import com.csc.muthur.server.mocks.MockConfigurationServerImpl;
import com.csc.muthur.server.model.AccessControl;
import com.csc.muthur.server.model.event.EventAttributeName;
import com.csc.muthur.server.model.event.data.GeoPoint;
import com.csc.muthur.server.model.event.data.Runway;
import com.csc.muthur.server.model.event.data.RunwayFlow;
import com.csc.muthur.server.model.event.data.airport.AirportConfiguration;
import com.csc.muthur.server.model.event.data.flight.Aircraft;
import com.csc.muthur.server.model.event.data.flight.AircraftArrival;
import com.csc.muthur.server.model.event.data.flight.AircraftDeparture;
import com.csc.muthur.server.model.event.data.flight.AircraftTaxiIn;
import com.csc.muthur.server.model.event.data.flight.AircraftTaxiOut;
import com.csc.muthur.server.model.event.data.flight.FlightPlan;
import com.csc.muthur.server.model.event.data.flight.FlightPosition;
import com.csc.muthur.server.ownership.OwnershipService;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public class OwnershipServiceImplTest {

	private static OwnershipService ownershipService;
	private static ConfigurationService configurationService;
	private static CommonsService commonsService;

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
	private String airportCodeKIAD = "KIAD";
	private String airportCodeKORD = "KORD";
	private static AirportConfiguration airportConfigurationKIAD;
	private static AirportConfiguration airportConfigurationKORD;

	private static ObjectOwnershipID xferObjectOwnershipID = null;
	private static FederationExecutionID xferFederationExecutionID;

	private static FederationExecutionID federationExecutionID1 = null;
	private static ObjectOwnershipID objectOwnershipID1 = null;

	private static FederationExecutionID federationExecutionID2 = null;
	private static ObjectOwnershipID objectOwnershipID2 = null;

	// private static FederateRegistrationHandle federateReqistrationHandle;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		configurationService = new MockConfigurationServerImpl();

		ownershipService = new OwnershipServiceImpl();

		ownershipService.setConfigurationService(configurationService);

		ownershipService.start();

		commonsService = new CommonsServiceImpl();

		commonsService.start();

		/**
		 * Set up the object ownership ID
		 */

		federationExecutionID1 = commonsService.createFederationExecutionID(UUID
				.randomUUID().toString(), UUID.randomUUID().toString());

		try {
			objectOwnershipID1 = commonsService.createObjectOwnershipID(UUID
					.randomUUID().toString(), federationExecutionID1);
		} catch (MuthurException e1) {
			fail(e1.getLocalizedMessage());
		}

		/**
		 * Set up the object ownership ID to which objects will be transferred
		 */

		xferFederationExecutionID = commonsService.createFederationExecutionID(UUID
				.randomUUID().toString(), UUID.randomUUID().toString());

		try {
			xferObjectOwnershipID = commonsService.createObjectOwnershipID(UUID
					.randomUUID().toString(), xferFederationExecutionID);
		} catch (MuthurException e1) {
			fail(e1.getLocalizedMessage());
		}

		/**
		 * Set up a second object ownership ID to test ownership functions
		 */

		federationExecutionID2 = commonsService.createFederationExecutionID(UUID
				.randomUUID().toString(), UUID.randomUUID().toString());

		try {
			objectOwnershipID2 = commonsService.createObjectOwnershipID(UUID
					.randomUUID().toString(), federationExecutionID2);
		} catch (MuthurException e1) {
			fail(e1.getLocalizedMessage());
		}

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

	/**
	 * 
	 */
	@Test
	public void runAllTests() {
		testAddObjectOwnershipWithAirportConfigurationforOwnershipID1();
		testAddObjectOwnershipWithAirportConfigurationforOwnershipID2();
		testDetermineObjectOwnership();
		testAddObjectOwnershipWithAircraft();
		testAddObjectOwnershipWithFlightPlan();
		testAddObjectOwnershipWithFlightPosition();
		testAddObjectOwnershipWithAircraftArrival();
		testAddObjectOwnershipWithAircraftDeparture();
		testAddObjectOwnershipWithAircraftTaxiIn();
		testAddObjectOwnershipWithAircraftTaxiOut();
		testUpdateAttributeAccessControl();
		testTransferObjectOwnership();
		testRemoveObjectOwnershipWithObjectOwnershipID();
		testDetermineObjectOwnershipAfterOwnershipRemoved();
		testRemoveObjectOwnershipWithFederationExecutionID();
	}

	/**
	 * Test method for
	 * {@link com.csc.muthur.server.ownership.internal.OwnershipServiceImpl#addObjectOwnership(com.csc.muthur.server.commons.ObjectOwnershipID, com.csc.muthur.model.event.data.IBaseDataObject)}
	 * .
	 */
	public void testAddObjectOwnershipWithAirportConfigurationforOwnershipID1() {

		airportConfigurationKIAD = null;

		try {
			airportConfigurationKIAD = new AirportConfiguration(airportCodeKIAD);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// runway 01L
		Runway runway = new Runway();
		runway.setName("01L");
		runway.setRunwayFlow(RunwayFlow.ARRIVAL);
		GeoPoint gp = new GeoPoint();
		gp.setLatitude(38.944966);
		gp.setLongitude(-77.474822);
		gp.setAltitude(382.0);
		runway.setUpperLeft(gp);
		gp = new GeoPoint(38.970765, -77.474483, 382.0);
		runway.setLowerRight(gp);

		airportConfigurationKIAD.addRunway(runway);

		// runway 01C
		runway = new Runway("01C");
		runway.setRunwayFlow(RunwayFlow.ARRIVAL);
		gp = new GeoPoint(38.944966, -77.474822, 382.0);
		runway.setUpperLeft(gp);
		gp = new GeoPoint(38.944966, -77.474822, 382.0);
		runway.setLowerRight(gp);

		airportConfigurationKIAD.addRunway(runway);

		// runway 01R
		runway = new Runway("01R");
		runway.setRunwayFlow(RunwayFlow.DEPARTURE);
		gp = new GeoPoint(38.939058, -77.459801, 382.0);
		runway.setUpperLeft(gp);
		gp = new GeoPoint(38.970628, -77.459354, 382.0);
		runway.setLowerRight(gp);

		airportConfigurationKIAD.addRunway(runway);

		// runway 30
		runway = new Runway("30");
		runway.setRunwayFlow(RunwayFlow.DEPARTURE);
		gp = new GeoPoint(38.933622, -77.455911, 382.0);
		runway.setUpperLeft(gp);
		gp = new GeoPoint(38.943778, -77.490448, 382.0);
		runway.setLowerRight(gp);

		airportConfigurationKIAD.addRunway(runway);

		assertNotNull(airportConfigurationKIAD.getDataObjectUUID());
		assertFalse(airportConfigurationKIAD.getDataObjectUUID().equalsIgnoreCase(
				""));
		assertTrue(airportConfigurationKIAD.getObjectCreateTimeMSecs() != 0L);

		assertNotNull(airportConfigurationKIAD);

		assertEquals(airportConfigurationKIAD.getAirportCode(), airportCodeKIAD);

		Runway retrievedRunway = airportConfigurationKIAD.getRunway("30");

		assertNotNull(retrievedRunway);

		retrievedRunway = airportConfigurationKIAD.getRunway("30L");

		assertNull(retrievedRunway);

		try {
			ownershipService.addObjectOwnership(objectOwnershipID1,
					airportConfigurationKIAD);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(ownershipService.isObjectOwned(airportConfigurationKIAD
				.getDataObjectUUID()));
		assertTrue(ownershipService.isOwner(objectOwnershipID1,
				airportConfigurationKIAD.getDataObjectUUID()));
	}

	/**
	 * Test method for
	 * {@link com.csc.muthur.server.ownership.internal.OwnershipServiceImpl#addObjectOwnership(com.csc.muthur.server.commons.ObjectOwnershipID, com.csc.muthur.model.event.data.IBaseDataObject)}
	 * .
	 */
	public void testAddObjectOwnershipWithAirportConfigurationforOwnershipID2() {

		airportConfigurationKORD = null;

		try {
			airportConfigurationKORD = new AirportConfiguration(airportCodeKORD);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// runway 01L
		Runway runway = new Runway();
		runway.setName("01L");
		runway.setRunwayFlow(RunwayFlow.ARRIVAL);
		GeoPoint gp = new GeoPoint();
		gp.setLatitude(38.944966);
		gp.setLongitude(-77.474822);
		gp.setAltitude(382.0);
		runway.setUpperLeft(gp);
		gp = new GeoPoint(38.970765, -77.474483, 382.0);
		runway.setLowerRight(gp);

		airportConfigurationKORD.addRunway(runway);

		// runway 01C
		runway = new Runway("01C");
		runway.setRunwayFlow(RunwayFlow.ARRIVAL);
		gp = new GeoPoint(38.944966, -77.474822, 382.0);
		runway.setUpperLeft(gp);
		gp = new GeoPoint(38.944966, -77.474822, 382.0);
		runway.setLowerRight(gp);

		airportConfigurationKORD.addRunway(runway);

		// runway 01R
		runway = new Runway("01R");
		runway.setRunwayFlow(RunwayFlow.DEPARTURE);
		gp = new GeoPoint(38.939058, -77.459801, 382.0);
		runway.setUpperLeft(gp);
		gp = new GeoPoint(38.970628, -77.459354, 382.0);
		runway.setLowerRight(gp);

		airportConfigurationKORD.addRunway(runway);

		// runway 30
		runway = new Runway("30");
		runway.setRunwayFlow(RunwayFlow.DEPARTURE);
		gp = new GeoPoint(38.933622, -77.455911, 382.0);
		runway.setUpperLeft(gp);
		gp = new GeoPoint(38.943778, -77.490448, 382.0);
		runway.setLowerRight(gp);

		airportConfigurationKORD.addRunway(runway);

		assertNotNull(airportConfigurationKORD.getDataObjectUUID());
		assertFalse(airportConfigurationKORD.getDataObjectUUID().equalsIgnoreCase(
				""));
		assertTrue(airportConfigurationKORD.getObjectCreateTimeMSecs() != 0L);

		assertNotNull(airportConfigurationKORD);

		assertEquals(airportConfigurationKORD.getAirportCode(), airportCodeKORD);

		Runway retrievedRunway = airportConfigurationKORD.getRunway("30");

		assertNotNull(retrievedRunway);

		retrievedRunway = airportConfigurationKORD.getRunway("30L");

		assertNull(retrievedRunway);

		try {
			ownershipService.addObjectOwnership(objectOwnershipID2,
					airportConfigurationKORD);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(ownershipService.isObjectOwned(airportConfigurationKORD
				.getDataObjectUUID()));
		assertTrue(ownershipService.isOwner(objectOwnershipID2,
				airportConfigurationKORD.getDataObjectUUID()));
	}

	/**
	 * 
	 */
	public void testDetermineObjectOwnership() {

		ObjectOwnershipID objectOwnershipFound = ownershipService
				.getObjectOwner(airportConfigurationKIAD);

		assertTrue(objectOwnershipFound.equals(objectOwnershipID1));

		objectOwnershipFound = ownershipService
				.getObjectOwner(airportConfigurationKORD);

		assertTrue(objectOwnershipFound.equals(objectOwnershipID2));

		assertFalse(objectOwnershipFound.equals(objectOwnershipID1));

	}

	/**
	 * Test method for
	 * {@link com.csc.muthur.server.ownership.internal.OwnershipServiceImpl#addObjectOwnership(com.csc.muthur.server.commons.ObjectOwnershipID, com.csc.muthur.model.event.data.IBaseDataObject)}
	 * .
	 */
	public void testAddObjectOwnershipWithAircraft() {

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

		try {
			ownershipService.addObjectOwnership(objectOwnershipID1, aircraft);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(ownershipService.isObjectOwned(aircraft.getDataObjectUUID()));
		assertTrue(ownershipService.isOwner(objectOwnershipID1,
				aircraft.getDataObjectUUID()));
	}

	/**
	 * 
	 */
	public void testAddObjectOwnershipWithFlightPlan() {

		// create a flight plan

		flightPlan = null;

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

		try {
			ownershipService.addObjectOwnership(objectOwnershipID1, flightPlan);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(ownershipService.isObjectOwned(flightPlan.getDataObjectUUID()));
		assertTrue(ownershipService.isOwner(objectOwnershipID1,
				flightPlan.getDataObjectUUID()));
	}

	/**
	 * 
	 */
	public void testAddObjectOwnershipWithFlightPosition() {

		flightPosition = null;

		try {
			flightPosition = new FlightPosition(tailNumber, callSign);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

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

		try {
			ownershipService.addObjectOwnership(objectOwnershipID1, flightPosition);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}
		assertTrue(ownershipService.isObjectOwned(flightPosition
				.getDataObjectUUID()));
		assertTrue(ownershipService.isOwner(objectOwnershipID1,
				flightPosition.getDataObjectUUID()));
	}

	/**
	 * 
	 */
	public void testAddObjectOwnershipWithAircraftArrival() {

		aircraftArrival = null;

		try {
			aircraftArrival = new AircraftArrival(tailNumber, callSign);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		aircraftArrival.setActualArrivalTimeMSecs(System.currentTimeMillis());
		aircraftArrival.setArrivalAirportCode(arrivalAirportCode);
		aircraftArrival.setArrivalRunway(arrivalRunwayName);

		try {
			ownershipService.addObjectOwnership(objectOwnershipID1, aircraftArrival);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * 
	 */
	public void testAddObjectOwnershipWithAircraftDeparture() {

		aircraftDeparture = null;

		try {
			aircraftDeparture = new AircraftDeparture(tailNumber, callSign);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		aircraftDeparture.setActualDepartureTimeMSecs(System.currentTimeMillis());
		aircraftDeparture.setDepartureAirportCode(departureAirportCode);
		aircraftDeparture.setDepartureRunway(departureRunwayName);

		try {
			ownershipService
					.addObjectOwnership(objectOwnershipID1, aircraftDeparture);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * 
	 */
	public void testAddObjectOwnershipWithAircraftTaxiIn() {

		aircraftTaxiIn = null;

		try {
			aircraftTaxiIn = new AircraftTaxiIn(tailNumber, callSign);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		aircraftTaxiIn.setTaxiInTimeMSecs(System.currentTimeMillis());
		aircraftTaxiIn.setTaxiInGate(taxiInGate);

		try {
			ownershipService.addObjectOwnership(objectOwnershipID1, aircraftTaxiIn);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * 
	 */
	public void testAddObjectOwnershipWithAircraftTaxiOut() {

		aircraftTaxiOut = null;

		try {
			aircraftTaxiOut = new AircraftTaxiOut(tailNumber, callSign);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		aircraftTaxiOut.setTaxiOutTimeMSecs(System.currentTimeMillis());
		aircraftTaxiOut.setTaxiOutGate(taxiInGate);

		try {
			ownershipService.addObjectOwnership(objectOwnershipID1, aircraftTaxiOut);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * 
	 */
	public void testUpdateAttributeAccessControl() {

		Map<String, AccessControl> fieldNameToAccessControlMap = new ConcurrentHashMap<String, AccessControl>();

		fieldNameToAccessControlMap
				.put(EventAttributeName.latitudeDegrees.toString(),
						AccessControl.READ_WRITE);
		fieldNameToAccessControlMap.put(
				EventAttributeName.longitudeDegrees.toString(),
				AccessControl.READ_WRITE);
		fieldNameToAccessControlMap.put(EventAttributeName.altitudeFt.toString(),
				AccessControl.READ_WRITE);

		boolean status = false;

		try {
			status = ownershipService.updateAttributeAccessControl(
					objectOwnershipID1, flightPosition.getDataObjectUUID(),
					fieldNameToAccessControlMap);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(status);

	}

	/**
	 * 
	 */
	public void testTransferObjectOwnership() {

		boolean status = false;

		// check that the object is owned by the owner that will transfer the
		// ownership
		OwnedObjectList ownedObjectList = ownershipService
				.getOwnerObjects(objectOwnershipID1);

		assertNotNull(ownedObjectList);

		assertTrue(ownedObjectList.getOwnedObjects().contains(
				flightPlan.getDataObjectUUID()));

		// transfer the ownership

		try {
			status = ownershipService.transferObjectOwnership(objectOwnershipID1,
					xferObjectOwnershipID, flightPlan.getDataObjectUUID());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(status);

		// check that the object is NOT in the previous owner's list

		ownedObjectList = ownershipService.getOwnerObjects(objectOwnershipID1);

		assertNotNull(ownedObjectList);

		assertFalse(ownedObjectList.getOwnedObjects().contains(
				flightPlan.getDataObjectUUID()));

		// check that the object is in the new owner's list

		ownedObjectList = ownershipService.getOwnerObjects(xferObjectOwnershipID);

		assertNotNull(ownedObjectList);

		assertTrue(ownedObjectList.getOwnedObjects().contains(
				flightPlan.getDataObjectUUID()));

		System.out.println(ownedObjectList.toString());

	}

	/**
	 * 
	 */
	public void testRemoveObjectOwnershipWithObjectOwnershipID() {

		boolean status = false;

		// check that the object is owned by the owner that will transfer the
		// ownership

		OwnedObjectList ownedObjectList = ownershipService
				.getOwnerObjects(objectOwnershipID1);

		assertNotNull(ownedObjectList);

		assertTrue(ownedObjectList.getOwnedObjects().contains(
				flightPosition.getDataObjectUUID()));

		// remove the ownership

		try {
			status = ownershipService.removeObjectOwnership(objectOwnershipID1,
					flightPosition.getDataObjectUUID());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(status);

		// check that the object is NOT in the previous owner's list

		ownedObjectList = ownershipService.getOwnerObjects(objectOwnershipID1);

		assertNotNull(ownedObjectList);

		assertFalse(ownedObjectList.getOwnedObjects().contains(
				flightPosition.getDataObjectUUID()));
	}

	/**
	 * 
	 */
	public void testDetermineObjectOwnershipAfterOwnershipRemoved() {

		// object is owned so this is NOT null

		assertNotNull(ownershipService.getObjectOwner(airportConfigurationKIAD));

		// remove the ownership

		assertTrue(ownershipService.removeObjectOwnership(objectOwnershipID1,
				airportConfigurationKIAD.getDataObjectUUID()));

		// object is not owned so this should now be null

		assertNull(ownershipService.getObjectOwner(airportConfigurationKIAD));
	}

	/**
	 * 
	 */
	public void testRemoveObjectOwnershipWithFederationExecutionID() {

		// check that the object is owned by the owner that will remove the
		// ownership

		OwnedObjectList ownedObjectList = ownershipService
				.getOwnerObjects(xferObjectOwnershipID);

		assertNotNull(ownedObjectList);

		assertTrue(ownedObjectList.getOwnedObjects().contains(
				flightPlan.getDataObjectUUID()));

		// remove the ownership

		try {
			ownershipService.removeObjectOwnerships(xferFederationExecutionID);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// check that the object is NOT in the previous owner's list

		ownedObjectList = ownershipService.getOwnerObjects(xferObjectOwnershipID);

		assertNull(ownedObjectList);
	}

}
