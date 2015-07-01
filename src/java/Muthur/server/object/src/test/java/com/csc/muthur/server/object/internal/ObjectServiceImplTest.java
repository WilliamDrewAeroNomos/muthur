/**
 * 
 */
package com.csc.muthur.server.object.internal;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.csc.muthur.server.commons.FederationExecutionID;
import com.csc.muthur.server.commons.ObjectOwnershipID;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.commons.internal.CommonsServiceImpl;
import com.csc.muthur.server.configuration.ConfigurationService;
import com.csc.muthur.server.mocks.MockConfigurationServerImpl;
import com.csc.muthur.server.model.ModelService;
import com.csc.muthur.server.model.event.EventAttributeName;
import com.csc.muthur.server.model.event.data.IBaseDataObject;
import com.csc.muthur.server.model.event.data.airport.AirportConfiguration;
import com.csc.muthur.server.model.event.data.flight.Aircraft;
import com.csc.muthur.server.model.event.data.flight.AircraftArrival;
import com.csc.muthur.server.model.event.data.flight.AircraftDeparture;
import com.csc.muthur.server.model.event.data.flight.AircraftTaxiIn;
import com.csc.muthur.server.model.event.data.flight.AircraftTaxiOut;
import com.csc.muthur.server.model.event.data.flight.FlightPlan;
import com.csc.muthur.server.model.event.data.flight.FlightPosition;
import com.csc.muthur.server.model.internal.ModelServiceImpl;
import com.csc.muthur.server.object.ObjectService;
import com.csc.muthur.server.ownership.OwnershipService;
import com.csc.muthur.server.ownership.internal.OwnershipServiceImpl;

/**
 * Tests the basic CRUD operations of the ObjectService. A single
 * SpawnAircraftData object is created, read, updated and deleted.
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public class ObjectServiceImplTest {

	private static ConfigurationService configurationService;
	private static ModelService modelService;
	private static ObjectService objectService;
	private static OwnershipService ownershipService;

	private static FederationExecutionID federationExecutionID;

	private static AirportConfiguration airportConfiguration = null;
	private static Aircraft aircraft = null;
	private static FlightPlan flightPlan = null;
	private static FlightPosition flightPosition = null;
	private static AircraftDeparture aircraftDeparture = null;
	private static AircraftArrival aircraftArrival = null;
	private static AircraftTaxiIn aircraftTaxiIn = null;
	private static AircraftTaxiOut aircraftTaxiOut = null;
	private static CommonsServiceImpl commonsService;

	private String tailNumber = "N481UA";
	private String callSign = "DAL333";
	private String aircraftType = "B774";

	// attributes that will be set
	//
	private String departureRunwayName = "8L";
	private String arrivalRunwayName = "7R";
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
	private String departureAirportCode = "KIAD";
	private String arrivalFix = "ARFIX";
	private String arrivalAirportCode = "KORD";
	private String physicalClass = "J";
	private String weightClass = "H";
	private String userClass = "C";
	private String airborneEquipmentQualifier = "R";
	private int aircraftTransmissionFrequency = 2850;
	private String squawkCode = "D013";

	private String taxiInGate = "D27";
	private String taxiOutGate = "C38";

	/**
	 * New values to used in the data object updates
	 */
	private double newCruiseSpeedKts;
	private double newCruiseAltitudeFeet;

	private double newLatitudeDegrees = 43.76;
	private double newLongitudeDegrees = -77.83;
	private double newAltitudeFeet = 40000;

	private String newDepartureAirportCode = "IAD";
	private String newDepartureRunwayName = "9L";

	private String newTaxiInGate = "D27";
	private String newTaxiOutGate = "E38";
	private String airportCode = "KIAD";

	private static ObjectOwnershipID objectOwnershipID = null;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		commonsService = new CommonsServiceImpl();

		commonsService.start();

		configurationService = new MockConfigurationServerImpl();

		int availablePort = commonsService.findAvailablePort(6000, 7000);

		assert (availablePort != -1);

		configurationService.setMessagingPort(availablePort);

		/**
		 * create model service and set it inside the object service impl
		 */

		modelService = new ModelServiceImpl();

		modelService.start();

		objectService = new ObjectServiceImpl();

		ownershipService = new OwnershipServiceImpl();

		ownershipService.setConfigurationService(configurationService);

		ownershipService.start();

		// set the services into the object service impl

		objectService.setConfigurationService(configurationService);
		objectService.setModelService(modelService);
		objectService.setCommonsService(commonsService);

		federationExecutionID = commonsService.createFederationExecutionID(UUID
				.randomUUID().toString(), UUID.randomUUID().toString());

		try {
			objectOwnershipID = commonsService.createObjectOwnershipID(UUID
					.randomUUID().toString(), federationExecutionID);
		} catch (MuthurException e1) {
			fail(e1.getLocalizedMessage());
		}

		assertTrue(objectOwnershipID != null);

		objectService.createFederationDataObjectContainer(federationExecutionID);
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

		testAddAirportObject();
		testGetAirportConfigurationObject();
		testAddAircraftObject();
		testGetAircraftObject();
		testAddAircraftObjectThatAlreadyExists();
		testAddFlightPlanObject();
		testAddFlightPositionObject();
		testAddAircraftDepartureObject();
		testAddAircraftArrivalObject();
		testAddAircraftTaxiInObject();
		testAddAircraftTaxiOutObject();

		testUpateFlightPlanObject();
		testUpateNonExistenceFlightPlanObject();
		testUpateFlightPositionObject();
		testUpateFlightPlanObjectAttributes();
		testUpateFlightPositionObjectAttributes();
		testUpateAircraftDepartureObjectAttributes();
		testUpateAircraftArrivalObjectAttributes();
		testUpateAircraftTaxiInObjectAttributes();
		testUpateAircraftTaxiOutObjectAttributes();
		testUpdateAircraftDepartureObject();

		testGetAircraftObjectGraph();
		testDeleteAircraftObject();
		testGetAircraftObjectGraphWhenAircraftNotExists();

	}

	/**
	 * 
	 */
	public void testAddAirportObject() {

		airportConfiguration = null;

		try {
			airportConfiguration = new AirportConfiguration(airportCode);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		try {
			objectService.addDataObject(federationExecutionID, airportConfiguration);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * 
	 */
	public void testGetAirportConfigurationObject() {

		assertObjectExists(airportConfiguration);

	}

	/**
	 * 
	 */
	public void testAddAircraftObject() {

		aircraft = null;

		try {
			aircraft = new Aircraft(tailNumber, callSign);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		try {
			objectService.addDataObject(federationExecutionID, aircraft);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * 
	 */
	public void testGetAircraftObject() {

		assertObjectExists(aircraft);

	}

	/**
	 * 
	 */
	public void testAddAircraftObjectThatAlreadyExists() {

		try {
			objectService.addDataObject(federationExecutionID, aircraft);
			fail("Should have thrown an exception for "
					+ "attempting to add a duplicate object");
		} catch (MuthurException e) {
			System.out.println(e.getLocalizedMessage());
		}
	}

	/**
	 * 
	 */
	public void testAddFlightPlanObject() {

		// create a flight plan

		flightPlan = null;

		try {
			flightPlan = new FlightPlan(tailNumber, callSign);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		flightPlan.setSource(source);
		flightPlan.setAircraftType(aircraftType);
		flightPlan.setPlannedDepartureTimeMSecs(System.currentTimeMillis());
		flightPlan.setPlannedArrivalTimeMSecs(System.currentTimeMillis() + 30000);
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

		// add the flight plan

		try {
			objectService.addDataObject(federationExecutionID, flightPlan);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertObjectExists(flightPlan);
	}

	/**
	 * 
	 */
	public void testAddFlightPositionObject() {

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

		// add the flight position

		try {
			objectService.addDataObject(federationExecutionID, flightPosition);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertObjectExists(flightPosition);
	}

	/**
	 * 
	 */
	public void testAddAircraftDepartureObject() {

		aircraftDeparture = null;

		try {
			aircraftDeparture = new AircraftDeparture(tailNumber, callSign);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		aircraftDeparture.setActualDepartureTimeMSecs(System.currentTimeMillis());
		aircraftDeparture.setDepartureAirportCode(departureAirportCode);
		aircraftDeparture.setDepartureRunway(departureRunwayName);

		// add the aircraft departure

		try {
			objectService.addDataObject(federationExecutionID, aircraftDeparture);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertObjectExists(aircraftDeparture);
	}

	/**
	 * 
	 */
	public void testAddAircraftArrivalObject() {

		aircraftArrival = null;

		try {
			aircraftArrival = new AircraftArrival(tailNumber, callSign);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		aircraftArrival.setActualArrivalTimeMSecs(System.currentTimeMillis());
		aircraftArrival.setArrivalAirportCode(arrivalAirportCode);
		aircraftArrival.setArrivalRunway(arrivalRunwayName);

		// add the aircraft arrival

		try {
			objectService.addDataObject(federationExecutionID, aircraftArrival);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertObjectExists(aircraftArrival);
	}

	/**
	 * 
	 */
	public void testAddAircraftTaxiInObject() {

		aircraftTaxiIn = null;

		try {
			aircraftTaxiIn = new AircraftTaxiIn(tailNumber, callSign);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		aircraftTaxiIn.setTaxiInTimeMSecs(System.currentTimeMillis());
		aircraftTaxiIn.setTaxiInGate(taxiInGate);

		// add the aircraft taxi in

		try {
			objectService.addDataObject(federationExecutionID, aircraftTaxiIn);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertObjectExists(aircraftTaxiIn);
	}

	/**
	 * 
	 */
	public void testAddAircraftTaxiOutObject() {

		aircraftTaxiOut = null;

		try {
			aircraftTaxiOut = new AircraftTaxiOut(tailNumber, callSign);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		aircraftTaxiOut.setTaxiOutTimeMSecs(System.currentTimeMillis());
		aircraftTaxiOut.setTaxiOutGate(taxiOutGate);

		// add the aircraft taxi in

		try {
			objectService.addDataObject(federationExecutionID, aircraftTaxiOut);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertObjectExists(aircraftTaxiOut);
	}

	/**
	 * 
	 */
	public void testUpateFlightPlanObject() {

		// set the attributes to be updated
		//
		flightPlan.setPlannedDepartureTimeMSecs(System.currentTimeMillis());
		flightPlan.setPlannedArrivalTimeMSecs(System.currentTimeMillis() + 30000);
		flightPlan.setCruiseSpeedKts(newCruiseSpeedKts);
		flightPlan.setCruiseAltitudeFt(newCruiseAltitudeFeet);

		try {
			objectService.updateObject(federationExecutionID, flightPlan);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// will retrieve the updated object from the object service and compare
		// it
		// to the object which should accurately test whether the update was
		// applied

		assertObjectExists(flightPlan);

	}

	/**
	 * 
	 */
	public void testUpateNonExistenceFlightPlanObject() {

		// create a flight plan

		FlightPlan flightPlanNotAdded = null;

		try {
			flightPlanNotAdded = new FlightPlan(tailNumber, callSign);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		flightPlanNotAdded.setSource(source);
		flightPlanNotAdded.setAircraftType(aircraftType);
		flightPlanNotAdded.setPlannedDepartureTimeMSecs(System.currentTimeMillis());
		flightPlanNotAdded
				.setPlannedArrivalTimeMSecs(System.currentTimeMillis() + 30000);
		flightPlanNotAdded.setCruiseSpeedKts(cruiseSpeedKts);
		flightPlanNotAdded.setCruiseAltitudeFt(cruiseAltitudeFeet);
		flightPlanNotAdded.setRoutePlan(route);
		flightPlanNotAdded.setDepartureCenter(departureCenter);
		flightPlanNotAdded.setArrivalCenter(arrivalCenter);
		flightPlanNotAdded.setDepartureFix(departureFix);
		flightPlanNotAdded.setArrivalFix(arrivalFix);
		flightPlanNotAdded.setPhysicalAircraftClass(physicalClass);
		flightPlanNotAdded.setWeightAircraftClass(weightClass);
		flightPlanNotAdded.setUserAircraftClass(userClass);
		flightPlanNotAdded.setNumOfAircraft(1);
		flightPlanNotAdded
				.setAirborneEquipmentQualifier(airborneEquipmentQualifier);

		try {
			objectService.updateObject(federationExecutionID, flightPlanNotAdded);
			fail("Should have thrown an exception trying to update a non-existent object.");
		} catch (MuthurException e) {
		}
	}

	/**
	 * 
	 */
	public void testUpateFlightPositionObject() {

		// set the attributes to be updated
		//
		flightPosition.setLatitudeDegrees(newLatitudeDegrees);
		flightPosition.setLongitudeDegrees(newLongitudeDegrees);
		flightPosition.setAltitudeFt(newAltitudeFeet);

		try {
			objectService.updateObject(federationExecutionID, flightPosition);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// will retrieve the updated object from the object service and compare
		// it
		// to the object which should accurately test whether the update was
		// applied

		assertObjectExists(flightPosition);

	}

	/**
	 * 
	 */
	public void testUpateFlightPlanObjectAttributes() {

		// set the attributes to be updated
		//
		FlightPlan newFlightPlan = new FlightPlan();

		// NOTE: Used only since the unit testing is conducted in the VM.
		// Changing
		// this emulates what would happen if the objects were residing on
		// separate
		// machines and thus address spaces.

		newFlightPlan.setDataObjectUUID(flightPlan.getDataObjectUUID());

		newFlightPlan.setSource(source);
		newFlightPlan.setAircraftType(aircraftType);
		newFlightPlan.setCruiseSpeedKts(cruiseSpeedKts);
		newFlightPlan.setCruiseAltitudeFt(cruiseAltitudeFeet);
		newFlightPlan.setRoutePlan(route);
		newFlightPlan.setPlannedDepartureTimeMSecs(System.currentTimeMillis());
		newFlightPlan.setDepartureCenter(departureCenter);
		newFlightPlan.setDepartureFix(departureFix);
		newFlightPlan.setPlannedArrivalTimeMSecs(System.currentTimeMillis());
		newFlightPlan.setArrivalCenter(arrivalCenter);
		newFlightPlan.setArrivalFix(arrivalFix);
		newFlightPlan.setPhysicalAircraftClass(physicalClass);
		newFlightPlan.setWeightAircraftClass(weightClass);
		newFlightPlan.setUserAircraftClass(userClass);
		newFlightPlan.setNumOfAircraft(1);
		newFlightPlan.setAirborneEquipmentQualifier(airborneEquipmentQualifier);

		Set<String> fieldsToUpdate = new HashSet<String>();
		fieldsToUpdate.add(EventAttributeName.source.toString());
		fieldsToUpdate.add(EventAttributeName.aircraftType.toString());
		fieldsToUpdate.add(EventAttributeName.cruiseSpeedKts.toString());
		fieldsToUpdate.add(EventAttributeName.cruiseAltitudeFt.toString());
		fieldsToUpdate.add(EventAttributeName.routePlan.toString());
		fieldsToUpdate.add(EventAttributeName.plannedDepartureTimeMSecs.toString());
		fieldsToUpdate.add(EventAttributeName.departureCenter.toString());
		fieldsToUpdate.add(EventAttributeName.departureFix.toString());
		fieldsToUpdate.add(EventAttributeName.plannedArrivalTimeMSecs.toString());
		fieldsToUpdate.add(EventAttributeName.arrivalCenter.toString());
		fieldsToUpdate.add(EventAttributeName.arrivalFix.toString());
		fieldsToUpdate.add(EventAttributeName.physicalAircraftClass.toString());
		fieldsToUpdate.add(EventAttributeName.weightAircraftClass.toString());
		fieldsToUpdate.add(EventAttributeName.userAircraftClass.toString());
		fieldsToUpdate.add(EventAttributeName.numOfAircraft.toString());
		fieldsToUpdate
				.add(EventAttributeName.airborneEquipmentQualifier.toString());

		try {
			objectService.updateObjectAttributes(federationExecutionID,
					newFlightPlan, fieldsToUpdate);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// will retrieve the updated object from the object service and compare
		// it
		// to the object which should accurately test whether the update was
		// applied

		assertObjectExists(flightPlan);

	}

	/**
	 * 
	 */
	public void testUpateFlightPositionObjectAttributes() {

		// set the attributes to be updated
		//
		FlightPosition newFlightPosition = new FlightPosition();

		// NOTE: Used only since the unit testing is conducted in the VM.
		// Changing
		// this emulates what would happen if the objects were residing on
		// separate
		// machines and thus address spaces.

		newFlightPosition.setDataObjectUUID(flightPosition.getDataObjectUUID());

		newFlightPosition.setLatitudeDegrees(latitudeDegrees);
		newFlightPosition.setLongitudeDegrees(longitudeDegrees);
		newFlightPosition.setAltitudeFt(altitudeFeet);
		newFlightPosition.setGroundspeedKts(groundSpeedKts);
		newFlightPosition.setHeadingDegrees(headingDegrees);
		newFlightPosition.setAirspeedKts(airSpeedKts);
		newFlightPosition.setPitchDegrees(pitchDegrees);
		newFlightPosition.setRollDegrees(rollDegrees);
		newFlightPosition.setYawDegrees(yawDegrees);
		newFlightPosition.setSector(sectorName);
		newFlightPosition.setCenter(centerName);
		newFlightPosition.setVerticalspeedKts(verticalspeedKts);
		newFlightPosition.setAircraftOnGround(true);
		newFlightPosition
				.setAircraftTransmissionFrequency(aircraftTransmissionFrequency);
		newFlightPosition.setSquawkCode(squawkCode);
		newFlightPosition.setIdent(true);

		Set<String> fieldsToUpdate = new HashSet<String>();
		fieldsToUpdate.add(EventAttributeName.longitudeDegrees.toString());
		fieldsToUpdate.add(EventAttributeName.latitudeDegrees.toString());
		fieldsToUpdate.add(EventAttributeName.altitudeFt.toString());
		fieldsToUpdate.add(EventAttributeName.groundspeedKts.toString());
		fieldsToUpdate.add(EventAttributeName.headingDegrees.toString());
		fieldsToUpdate.add(EventAttributeName.airspeedKts.toString());
		fieldsToUpdate.add(EventAttributeName.pitchDegrees.toString());
		fieldsToUpdate.add(EventAttributeName.rollDegrees.toString());
		fieldsToUpdate.add(EventAttributeName.yawDegrees.toString());
		fieldsToUpdate.add(EventAttributeName.sector.toString());
		fieldsToUpdate.add(EventAttributeName.center.toString());
		fieldsToUpdate.add(EventAttributeName.verticalspeedKts.toString());
		fieldsToUpdate.add(EventAttributeName.aircraftOnGround.toString());
		fieldsToUpdate.add(EventAttributeName.aircraftTransmissionFrequency
				.toString());
		fieldsToUpdate.add(EventAttributeName.squawkCode.toString());
		fieldsToUpdate.add(EventAttributeName.ident.toString());

		try {
			objectService.updateObjectAttributes(federationExecutionID,
					newFlightPosition, fieldsToUpdate);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// will retrieve the updated object from the object service and compare
		// it
		// to the object which should accurately test whether the update was
		// applied

		assertObjectExists(flightPosition);

	}

	/**
	 * 
	 */
	public void testUpateAircraftDepartureObjectAttributes() {

		// set the attributes to be updated
		//
		AircraftDeparture newAircraftDeparture = new AircraftDeparture();

		// NOTE: Used only since the unit testing is conducted in the VM.
		// Changing
		// this emulates what would happen if the objects were residing on
		// separate
		// machines and thus address spaces.

		newAircraftDeparture.setDataObjectUUID(aircraftDeparture
				.getDataObjectUUID());

		newAircraftDeparture.setCallSign(callSign);
		newAircraftDeparture.setTailNumber(tailNumber);
		newAircraftDeparture
				.setActualDepartureTimeMSecs(System.currentTimeMillis());
		newAircraftDeparture.setDepartureRunway(departureRunwayName);
		newAircraftDeparture.setDepartureAirportCode(departureAirportCode);

		Set<String> fieldsToUpdate = new HashSet<String>();
		fieldsToUpdate.add(EventAttributeName.actualDepartureTimeMSecs.toString());
		fieldsToUpdate.add(EventAttributeName.departureRunway.toString());
		fieldsToUpdate.add(EventAttributeName.departureAirportCode.toString());

		try {
			objectService.updateObjectAttributes(federationExecutionID,
					newAircraftDeparture, fieldsToUpdate);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// will retrieve the updated object from the object service and compare
		// it
		// to the object which should accurately test whether the update was
		// applied

		assertObjectExists(aircraftDeparture);

	}

	/**
	 * 
	 */
	public void testUpateAircraftArrivalObjectAttributes() {

		// set the attributes to be updated
		//
		AircraftArrival newAircraftArrival = new AircraftArrival();

		// NOTE: Used only since the unit testing is conducted in the VM.
		// Changing
		// this emulates what would happen if the objects were residing on
		// separate
		// machines and thus address spaces.

		newAircraftArrival.setDataObjectUUID(aircraftArrival.getDataObjectUUID());

		newAircraftArrival.setCallSign(callSign);
		newAircraftArrival.setTailNumber(tailNumber);

		newAircraftArrival.setActualArrivalTimeMSecs(System.currentTimeMillis());
		newAircraftArrival.setArrivalRunway(arrivalRunwayName);
		newAircraftArrival.setArrivalAirportCode(arrivalAirportCode);

		Set<String> fieldsToUpdate = new HashSet<String>();
		fieldsToUpdate.add(EventAttributeName.arrivalRunway.toString());
		fieldsToUpdate.add(EventAttributeName.arrivalAirportCode.toString());

		try {
			objectService.updateObjectAttributes(federationExecutionID,
					newAircraftArrival, fieldsToUpdate);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// will retrieve the updated object from the object service and compare
		// it
		// to the object which should accurately test whether the update was
		// applied

		assertObjectExists(aircraftArrival);

	}

	/**
	 * 
	 */
	public void testUpateAircraftTaxiInObjectAttributes() {

		// set the attributes to be updated
		//
		AircraftTaxiIn newAircraftTaxiIn = new AircraftTaxiIn();

		// NOTE: Used only since the unit testing is conducted in the VM.
		// Changing
		// this emulates what would happen if the objects were residing on
		// separate
		// machines and thus address spaces.

		newAircraftTaxiIn.setDataObjectUUID(aircraftTaxiIn.getDataObjectUUID());

		newAircraftTaxiIn.setCallSign(callSign);
		newAircraftTaxiIn.setTailNumber(tailNumber);

		newAircraftTaxiIn.setTaxiInGate(newTaxiInGate);
		newAircraftTaxiIn.setTaxiInTimeMSecs(System.currentTimeMillis());

		Set<String> fieldsToUpdate = new HashSet<String>();
		fieldsToUpdate.add(EventAttributeName.taxiInGate.toString());
		fieldsToUpdate.add(EventAttributeName.taxiInTimeMSecs.toString());

		try {
			objectService.updateObjectAttributes(federationExecutionID,
					newAircraftTaxiIn, fieldsToUpdate);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// will retrieve the updated object from the object service and compare
		// it
		// to the object which should accurately test whether the update was
		// applied

		assertObjectExists(aircraftTaxiIn);

	}

	/**
	 * 
	 */
	public void testUpateAircraftTaxiOutObjectAttributes() {

		// set the attributes to be updated
		//
		AircraftTaxiOut newAircraftTaxiOut = new AircraftTaxiOut();

		// NOTE: Used only since the unit testing is conducted in the VM.
		// Changing
		// this emulates what would happen if the objects were residing on
		// separate
		// machines and thus address spaces.

		newAircraftTaxiOut.setDataObjectUUID(aircraftTaxiOut.getDataObjectUUID());

		newAircraftTaxiOut.setCallSign(callSign);
		newAircraftTaxiOut.setTailNumber(tailNumber);

		newAircraftTaxiOut.setTaxiOutGate(newTaxiOutGate);
		newAircraftTaxiOut.setTaxiOutTimeMSecs(System.currentTimeMillis());

		Set<String> fieldsToUpdate = new HashSet<String>();
		fieldsToUpdate.add(EventAttributeName.taxiOutGate.toString());
		fieldsToUpdate.add(EventAttributeName.taxiOutTimeMSecs.toString());

		try {
			objectService.updateObjectAttributes(federationExecutionID,
					newAircraftTaxiOut, fieldsToUpdate);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// will retrieve the updated object from the object service and compare
		// it
		// to the object which should accurately test whether the update was
		// applied

		assertObjectExists(aircraftTaxiOut);

	}

	/**
	 * 
	 */
	public void testUpdateAircraftDepartureObject() {

		aircraftDeparture.setActualDepartureTimeMSecs(System.currentTimeMillis());
		aircraftDeparture.setDepartureAirportCode(newDepartureAirportCode);
		aircraftDeparture.setDepartureRunway(newDepartureRunwayName);

		// add the aircraft departure

		try {
			objectService.updateObject(federationExecutionID, aircraftDeparture);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertObjectExists(aircraftDeparture);
	}

	/**
	 * 
	 */
	public void testGetAircraftObjectGraph() {

		Set<IBaseDataObject> aircraftObjectGraph = null;

		try {
			aircraftObjectGraph = objectService.getAircraftObjectGraph(
					federationExecutionID, aircraft.getDataObjectUUID());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(aircraftObjectGraph != null);
		int size = aircraftObjectGraph.size();
		assertTrue(size == 7);

	}

	/**
	 * 
	 */
	public void testDeleteAircraftObject() {

		try {
			objectService.deleteDataObject(federationExecutionID,
					aircraft.getDataObjectUUID());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertObjectDeleted(aircraft);
	}

	/**
	 * 
	 */
	public void testGetAircraftObjectGraphWhenAircraftNotExists() {

		Set<IBaseDataObject> aircraftObjectGraph = null;

		try {
			aircraftObjectGraph = objectService.getAircraftObjectGraph(
					federationExecutionID, aircraft.getDataObjectUUID());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(aircraftObjectGraph != null);
		int size = aircraftObjectGraph.size();
		assertTrue(size == 0);

	}

	/**
	 * Convenience functions
	 */

	/**
	 * Uses the {@link IBaseDataObject#getDataObjectUUID()} from bdo to attempt to
	 * retrieve the object from the {@link ObjectService}. If the object is
	 * retrieved then it is check for null and equality.
	 */
	private void assertObjectExists(final IBaseDataObject bdo) {

		IBaseDataObject retrievedDataObject = null;

		try {
			retrievedDataObject = getObject(bdo.getDataObjectUUID());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assert (retrievedDataObject != null);
		assertTrue(retrievedDataObject.equals(bdo));
	}

	/**
	 * Uses the {@link IBaseDataObject#getDataObjectUUID()} from bdo to attempt to
	 * retrieve the object from the {@link ObjectService}. If the object is
	 * retrieved then it is check for null and equality.
	 */
	private void assertObjectDeleted(final IBaseDataObject bdo) {

		IBaseDataObject retrievedDataObject = null;

		try {
			retrievedDataObject = getObject(bdo.getDataObjectUUID());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assert (retrievedDataObject == null);
	}

	/**
	 * @throws MuthurException
	 * 
	 */
	private IBaseDataObject getObject(final String dataObjectUUID)
			throws MuthurException {

		IBaseDataObject bdo = null;

		bdo = objectService.getDataObject(federationExecutionID, dataObjectUUID);

		return bdo;

	}

}
