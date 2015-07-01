/**
 * 
 */
package com.csc.muthur.server.model.event.request;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.event.AbstractModelTest;
import com.csc.muthur.server.model.event.data.IBaseDataObject;
import com.csc.muthur.server.model.event.data.flight.Aircraft;
import com.csc.muthur.server.model.event.data.flight.AircraftArrival;
import com.csc.muthur.server.model.event.data.flight.AircraftDeparture;
import com.csc.muthur.server.model.event.data.flight.AircraftTaxiIn;
import com.csc.muthur.server.model.event.data.flight.AircraftTaxiOut;
import com.csc.muthur.server.model.event.data.flight.FlightPlan;
import com.csc.muthur.server.model.event.data.flight.FlightPlanStatus;
import com.csc.muthur.server.model.event.data.flight.FlightPosition;

/**
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class CreateOrUpdateObjectRequestTest extends AbstractModelTest {

	private static final String tailNumber = "N485UA";
	private static final String acid = "DAL333";

	private double verticalspeedKts = 405;
	private boolean aircraftOnGround = false;
	private int aircraftTransmissionFrequency = 2850;

	private String squawkCode = "0363"; // four characters
	private boolean ident = true; // true if the A/C has been IDENT'd

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
	public void testCreateOrUpdateObjectRequestWithAirCraftObject() {

		Aircraft sacd = null;

		try {
			sacd = new Aircraft(tailNumber, acid);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(sacd);

		assertEquals(sacd.getCallSign(), acid);
		assertEquals(sacd.getTailNumber(), tailNumber);
		assertNotNull(sacd.getDataObjectUUID());
		assertFalse(sacd.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(sacd.getObjectCreateTimeMSecs() != 0L);

		// create a CreateOrUpdateObjectRequest

		CreateOrUpdateObjectRequest cor = new CreateOrUpdateObjectRequest();
		assertNotNull(cor);

		cor.setFederationExecutionHandle(UUID.randomUUID().toString());
		cor.setFederateRegistrationHandle(UUID.randomUUID().toString());
		cor.setFederationExecutionModelHandle(UUID.randomUUID().toString());
		cor.setSourceOfEvent("NexSim");
		cor.setDataObject(sacd);

		// serialize the object

		String corAsXML = cor.serialize();

		// do standard validation

		assertNotNull(corAsXML);
		assertFalse(corAsXML.equals(""));

		writeToFile(cor, false);

		// create another object from previous object's serialized form

		CreateOrUpdateObjectRequest objectFromXML =
				new CreateOrUpdateObjectRequest();

		try {
			objectFromXML.initialization(corAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// ensure that the two objects are equal

		assertTrue(objectFromXML.equals(cor));

		// get the serialized version of the new object

		String newObjSerializedAsXML = objectFromXML.serialize();

		assertNotNull(newObjSerializedAsXML);
		assertFalse(newObjSerializedAsXML.equals(""));

		IBaseDataObject bdo = objectFromXML.getDataObject();

		assertNotNull(bdo);
		assertTrue(bdo instanceof Aircraft);

		/**
		 * Initialize the internal data object from the serialized containing object
		 */
		try {
			bdo.initialization(newObjSerializedAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// check the serialized form of the data object
		String bdoSerialized = bdo.serialize();
		assertNotNull(bdoSerialized);

	}

	/**
	 * 
	 */
	@Test
	public void testCreateOrUpdateObjectRequestWithFlightPlanObject() {

		FlightPlan flightPlan = null;

		try {
			flightPlan = new FlightPlan(tailNumber, acid);
		} catch (MuthurException e) {
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
		flightPlan.setDepartureFix("DEFIX");
		flightPlan.setDepartureCenter("ZID");

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

		// create a CreateOrUpdateObjectRequest

		CreateOrUpdateObjectRequest cor = new CreateOrUpdateObjectRequest();
		assertNotNull(cor);

		cor.setFederationExecutionHandle(UUID.randomUUID().toString());
		cor.setFederateRegistrationHandle(UUID.randomUUID().toString());
		cor.setFederationExecutionModelHandle(UUID.randomUUID().toString());
		cor.setSourceOfEvent("NexSim");
		cor.setDataObject(flightPlan);

		// serialize the object

		String corAsXML = cor.serialize();

		writeToFile(cor, false);

		// do standard validation

		assertNotNull(corAsXML);
		assertFalse(corAsXML.equals(""));

		// create another object from previous object's serialized form

		CreateOrUpdateObjectRequest objectFromXML =
				new CreateOrUpdateObjectRequest();

		try {
			objectFromXML.initialization(corAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// ensure that the two objects are equal

		assertTrue(objectFromXML.equals(cor));

		// get the serialized version of the new object

		String newObjSerializedAsXML = objectFromXML.serialize();

		assertNotNull(newObjSerializedAsXML);
		assertFalse(newObjSerializedAsXML.equals(""));

		IBaseDataObject bdo = objectFromXML.getDataObject();

		assertNotNull(bdo);
		assertTrue(bdo instanceof FlightPlan);

		/**
		 * Initialize the internal data object from the serialized containing object
		 */
		try {
			bdo.initialization(newObjSerializedAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// check the serialized form of the data object
		String bdoSerialized = bdo.serialize();
		assertNotNull(bdoSerialized);

	}

	/**
	 * 
	 */
	@Test
	public void testCreateOrUpdateObjectRequestWithFlightPositionObject() {

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

		// create a CreateOrUpdateObjectRequest

		CreateOrUpdateObjectRequest cor = new CreateOrUpdateObjectRequest();
		assertNotNull(cor);

		cor.setFederationExecutionHandle(UUID.randomUUID().toString());
		cor.setFederateRegistrationHandle(UUID.randomUUID().toString());
		cor.setFederationExecutionModelHandle(UUID.randomUUID().toString());
		cor.setSourceOfEvent("NexSim");
		cor.setDataObject(flightPosition);

		// serialize the object

		String corAsXML = cor.serialize();

		// do standard validation

		assertNotNull(corAsXML);
		assertFalse(corAsXML.equals(""));

		// create another object from previous object's serialized form

		CreateOrUpdateObjectRequest objectFromXML =
				new CreateOrUpdateObjectRequest();

		try {
			objectFromXML.initialization(corAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// ensure that the two objects are equal

		assertTrue(objectFromXML.equals(cor));

		// get the serialized version of the new object

		String newObjSerializedAsXML = objectFromXML.serialize();

		assertNotNull(newObjSerializedAsXML);
		assertFalse(newObjSerializedAsXML.equals(""));

		IBaseDataObject bdo = objectFromXML.getDataObject();

		assertNotNull(bdo);
		assertTrue(bdo instanceof FlightPosition);

		/**
		 * Initialize the internal data object from the serialized containing object
		 */
		try {
			bdo.initialization(newObjSerializedAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// check the serialized form of the data object
		String bdoSerialized = bdo.serialize();
		assertNotNull(bdoSerialized);

	}

	/**
	 * 
	 */
	@Test
	public void testCreateOrUpdateObjectRequestWithAircraftDepartureObject() {

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

		// create a CreateOrUpdateObjectRequest

		CreateOrUpdateObjectRequest cor = new CreateOrUpdateObjectRequest();
		assertNotNull(cor);

		cor.setFederationExecutionHandle(UUID.randomUUID().toString());
		cor.setFederateRegistrationHandle(UUID.randomUUID().toString());
		cor.setFederationExecutionModelHandle(UUID.randomUUID().toString());
		cor.setSourceOfEvent("NexSim");
		cor.setDataObject(aircraftDeparture);

		// serialize the object

		String corAsXML = cor.serialize();

		// do standard validation

		assertNotNull(corAsXML);
		assertFalse(corAsXML.equals(""));

		// create another object from previous object's serialized form

		CreateOrUpdateObjectRequest objectFromXML =
				new CreateOrUpdateObjectRequest();

		try {
			objectFromXML.initialization(corAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// ensure that the two objects are equal

		assertTrue(objectFromXML.equals(cor));

		// get the serialized version of the new object

		String newObjSerializedAsXML = objectFromXML.serialize();

		assertNotNull(newObjSerializedAsXML);
		assertFalse(newObjSerializedAsXML.equals(""));

		IBaseDataObject bdo = objectFromXML.getDataObject();

		assertNotNull(bdo);
		assertTrue(bdo instanceof AircraftDeparture);

		/**
		 * Initialize the internal data object from the serialized containing object
		 */
		try {
			bdo.initialization(newObjSerializedAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// check the serialized form of the data object
		String bdoSerialized = bdo.serialize();
		assertNotNull(bdoSerialized);

	}

	/**
	 * 
	 */
	@Test
	public void testCreateOrUpdateObjectRequestWithAircraftArrivalObject() {

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

		// create a CreateObjectRequest

		CreateOrUpdateObjectRequest cor = new CreateOrUpdateObjectRequest();
		assertNotNull(cor);

		cor.setFederationExecutionHandle(UUID.randomUUID().toString());
		cor.setFederateRegistrationHandle(UUID.randomUUID().toString());
		cor.setFederationExecutionModelHandle(UUID.randomUUID().toString());
		cor.setSourceOfEvent("NexSim");
		cor.setDataObject(acad);

		// serialize the object

		String corAsXML = cor.serialize();

		// do standard validation

		assertNotNull(corAsXML);
		assertFalse(corAsXML.equals(""));

		// create another object from previous object's serialized form

		CreateOrUpdateObjectRequest objectFromXML =
				new CreateOrUpdateObjectRequest();

		try {
			objectFromXML.initialization(corAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// ensure that the two objects are equal

		assertTrue(objectFromXML.equals(cor));

		// get the serialized version of the new object

		String newObjSerializedAsXML = objectFromXML.serialize();

		assertNotNull(newObjSerializedAsXML);
		assertFalse(newObjSerializedAsXML.equals(""));

		IBaseDataObject bdo = objectFromXML.getDataObject();

		assertNotNull(bdo);
		assertTrue(bdo instanceof AircraftArrival);

		/**
		 * Initialize the internal data object from the serialized containing object
		 */
		try {
			bdo.initialization(newObjSerializedAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// check the serialized form of the data object
		String bdoSerialized = bdo.serialize();
		assertNotNull(bdoSerialized);

	}

	/**
	 * 
	 */
	@Test
	public void testCreateOrUpdateObjectRequestWithAircraftTaxInObject() {

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

		// create a CreateOrUpdateObjectRequest

		CreateOrUpdateObjectRequest cor = new CreateOrUpdateObjectRequest();
		assertNotNull(cor);

		cor.setFederationExecutionHandle(UUID.randomUUID().toString());
		cor.setFederateRegistrationHandle(UUID.randomUUID().toString());
		cor.setFederationExecutionModelHandle(UUID.randomUUID().toString());
		cor.setSourceOfEvent("NexSim");
		cor.setDataObject(aircraftTaxiInData);

		// serialize the object

		String corAsXML = cor.serialize();

		// do standard validation

		assertNotNull(corAsXML);
		assertFalse(corAsXML.equals(""));

		// create another object from previous object's serialized form

		CreateOrUpdateObjectRequest objectFromXML =
				new CreateOrUpdateObjectRequest();

		try {
			objectFromXML.initialization(corAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// ensure that the two objects are equal

		assertTrue(objectFromXML.equals(cor));

		// get the serialized version of the new object

		String newObjSerializedAsXML = objectFromXML.serialize();

		assertNotNull(newObjSerializedAsXML);
		assertFalse(newObjSerializedAsXML.equals(""));

		IBaseDataObject bdo = objectFromXML.getDataObject();

		assertNotNull(bdo);
		assertTrue(bdo instanceof AircraftTaxiIn);

		/**
		 * Initialize the internal data object from the serialized containing object
		 */
		try {
			bdo.initialization(newObjSerializedAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// check the serialized form of the data object
		String bdoSerialized = bdo.serialize();
		assertNotNull(bdoSerialized);

	}

	/**
	 * 
	 */
	@Test
	public void testCreateOrUpdateObjectRequestWithAircraftTaxOutObject() {

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

		// create a CreateOrUpdateObjectRequest

		CreateOrUpdateObjectRequest cor = new CreateOrUpdateObjectRequest();
		assertNotNull(cor);

		cor.setFederationExecutionHandle(UUID.randomUUID().toString());
		cor.setFederateRegistrationHandle(UUID.randomUUID().toString());
		cor.setFederationExecutionModelHandle(UUID.randomUUID().toString());
		cor.setSourceOfEvent("NexSim");
		cor.setDataObject(atod);

		// serialize the object

		String corAsXML = cor.serialize();

		// do standard validation

		assertNotNull(corAsXML);
		assertFalse(corAsXML.equals(""));

		// create another object from previous object's serialized form

		CreateOrUpdateObjectRequest objectFromXML =
				new CreateOrUpdateObjectRequest();

		try {
			objectFromXML.initialization(corAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// ensure that the two objects are equal

		assertTrue(objectFromXML.equals(cor));

		// get the serialized version of the new object

		String newObjSerializedAsXML = objectFromXML.serialize();

		assertNotNull(newObjSerializedAsXML);
		assertFalse(newObjSerializedAsXML.equals(""));

		IBaseDataObject bdo = objectFromXML.getDataObject();

		assertNotNull(bdo);
		assertTrue(bdo instanceof AircraftTaxiOut);

		/**
		 * Initialize the internal data object from the serialized containing object
		 */
		try {
			bdo.initialization(newObjSerializedAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// check the serialized form of the data object
		String bdoSerialized = bdo.serialize();
		assertNotNull(bdoSerialized);

	}
}
