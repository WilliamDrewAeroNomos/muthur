/**
 * 
 */
package com.csc.muthur.server.model.event;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.csc.muthur.server.model.DataAction;
import com.csc.muthur.server.model.event.data.IBaseDataObject;
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
public class DataPublicationEventTest extends AbstractModelTest {

	private static final String tailNumber = "N485UA";
	private static final String acid = "DAL333";

	private static double latitudeDegrees = 42.65;
	private static double longitudeDegrees = -76.72;
	private static double altitudeFt = 30000;
	private static double groundspeedKts = 400;
	private static double headingDegrees = 90;
	private static double airspeedKts = 444;
	private static double pitchDegrees = 0.5;
	private static double rollDegrees = 0;
	private static double yawDegrees = 0;
	private static String sector = "ZOB48";
	private static String center = "ZOB";

	private static String source = "Flight";
	private static String aircraftType = "B777";

	// private static double plannedDepartureTimeMSecs;
	// private static double plannedArrivalTimeMSecs;

	private static double cruiseSpeedKts = 400;
	private static double cruiseAltitudeFt = 330;
	private static String routePlan = "IND..ROD.J29.PLB.J595.BGR..BGR";
	private static String departureCenter = "ZID";
	private static String arrivalCenter = "ZBW";
	private static String departureFix = "DEFIX";
	private static String arrivalFix = "ARFIX";
	private static String physicalAircraftClass = "J";
	private static String weightAircraftClass = "H";
	private static String userAircraftClass = "C";
	private static int numberOfAircraft = 1;
	private static String airborneEquipmentQualifier = "R";

	// private static double taxiOutTime;
	// private static double actualDeptTimeMSecs;

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
	public void testDataPublicationAircraftData() {

		Aircraft sacd = null;

		try {
			sacd = new Aircraft(tailNumber, acid);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(sacd);

		assertEquals(sacd.getCallSign(), acid);
		assertEquals(sacd.getTailNumber(), tailNumber);
		assertNotNull(sacd.getDataObjectUUID());
		assertFalse(sacd.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(sacd.getObjectCreateTimeMSecs() != 0L);

		// create a DataPublicationEvent object to hold the data object

		DataPublicationEvent dp = new DataPublicationEvent();
		assertNotNull(dp);

		dp.setFederationExecutionModelHandle(UUID.randomUUID().toString());
		dp.setSourceOfEvent("Muthur");
		dp.setTimeToLiveSecs(10);
		dp.setDataAction(DataAction.Add);
		dp.setDataObject(sacd);

		// serialize the DP object
		//
		String dpAsXML = dp.serialize();

		// ...and do standard validity check
		//
		assertNotNull(dpAsXML);
		assertFalse(dpAsXML.equals(""));

		// create another DP object in order to create that from the serialized

		DataPublicationEvent dpFromXML = new DataPublicationEvent();

		// initialize new object from previous object's serialized form
		try {
			dpFromXML.initialization(dpAsXML);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}

		// check that the two objects are now equal
		//
		assertTrue(dpFromXML.equals(dp));

		// get the serialized version of the new DP object
		//
		String newDPSerializedAsXML = dpFromXML.serialize();

		writeToFile(dp, false);

		assertNotNull(newDPSerializedAsXML);
		assertFalse(newDPSerializedAsXML.equals(""));

		IBaseDataObject bdo = dpFromXML.getDataObject();

		assertNotNull(bdo);
		assertTrue(bdo instanceof Aircraft);

		/**
		 * Initialize the data object from the serialized DataPublication object
		 */
		try {
			bdo.initialization(newDPSerializedAsXML);
		} catch (Exception e) {
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
	public void testDataPublicationAddFlightPlanData() {

		FlightPlan flightPlanFiledData = null;

		try {
			flightPlanFiledData = new FlightPlan(tailNumber, acid);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(flightPlanFiledData);

		assertEquals(flightPlanFiledData.getCallSign(), acid);
		assertEquals(flightPlanFiledData.getTailNumber(), tailNumber);
		assertNotNull(flightPlanFiledData.getDataObjectUUID());
		assertFalse(flightPlanFiledData.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(flightPlanFiledData.getObjectCreateTimeMSecs() != 0L);

		flightPlanFiledData.setSource(source);
		flightPlanFiledData.setAircraftType(aircraftType);
		flightPlanFiledData.setCruiseSpeedKts(cruiseSpeedKts);
		flightPlanFiledData.setCruiseAltitudeFt(cruiseAltitudeFt);
		flightPlanFiledData.setRoutePlan(routePlan);

		flightPlanFiledData
				.setPlannedDepartureTimeMSecs(System.currentTimeMillis() + 10);
		flightPlanFiledData.setPlannedDepartureRunway("1R");
		flightPlanFiledData.setPlannedTaxiOutGate("D31");
		flightPlanFiledData.setDepartureCenter(departureCenter);
		flightPlanFiledData.setDepartureFix(departureFix);

		flightPlanFiledData
				.setPlannedArrivalTimeMSecs(System.currentTimeMillis() + 20);
		flightPlanFiledData.setPlannedArrivalRunway("8L");
		flightPlanFiledData.setPlannedTaxiInGate("C11");
		flightPlanFiledData.setArrivalCenter(arrivalCenter);
		flightPlanFiledData.setArrivalFix(arrivalFix);

		flightPlanFiledData.setPhysicalAircraftClass(physicalAircraftClass);
		flightPlanFiledData.setWeightAircraftClass(weightAircraftClass);
		flightPlanFiledData.setUserAircraftClass(userAircraftClass);
		flightPlanFiledData.setNumOfAircraft(numberOfAircraft);
		flightPlanFiledData
				.setAirborneEquipmentQualifier(airborneEquipmentQualifier);

		// create a DataPublicationEvent object to hold the data object

		DataPublicationEvent dp = new DataPublicationEvent();
		assertNotNull(dp);

		dp.setFederationExecutionModelHandle(UUID.randomUUID().toString());
		dp.setSourceOfEvent("Muthur");
		dp.setTimeToLiveSecs(10);
		dp.setDataAction(DataAction.Add);
		dp.setDataObject(flightPlanFiledData);

		// serialize the DP object
		//
		String dpAsXML = dp.serialize();

		writeToFile(dp, false);

		// ...and do standard validity check
		//
		assertNotNull(dpAsXML);
		assertFalse(dpAsXML.equals(""));

		// create another DP object in order to create that from the serialized

		DataPublicationEvent dpFromXML = new DataPublicationEvent();

		// initialize new object from previous object's serialized form
		try {
			dpFromXML.initialization(dpAsXML);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}

		// check that the two objects are now equal
		//
		assertTrue(dpFromXML.equals(dp));

		// get the serialized version of the new DP object
		//
		String newDPSerializedAsXML = dpFromXML.serialize();

		assertNotNull(newDPSerializedAsXML);
		assertFalse(newDPSerializedAsXML.equals(""));

		IBaseDataObject bdo = dpFromXML.getDataObject();

		assertNotNull(bdo);
		assertTrue(bdo instanceof FlightPlan);

		/**
		 * Initialize the data object from the serialized DataPublication object
		 */
		try {
			bdo.initialization(newDPSerializedAsXML);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// check the serialized form of the data object
		String bdoSerialized = bdo.serialize();
		assertNotNull(bdoSerialized);

		// exception testing

		try {
			new FlightPlan(null, acid);
			fail("Should have thrown an exception due to null tail number");
		} catch (Exception e) {
			// should be here
		}

		try {
			new FlightPlan(tailNumber, null);
			fail("Should have thrown an exception due to null aircraft ID");
		} catch (Exception e) {
			// should be here
		}
	}

	/**
	 * 
	 */
	@Test
	public void testDataPublicationUpdateFlightPlanData() {

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

		flightPlan.setSource(source);
		flightPlan.setAircraftType(aircraftType);
		flightPlan.setPlannedDepartureTimeMSecs(System.currentTimeMillis() + 10);
		flightPlan.setPlannedArrivalTimeMSecs(System.currentTimeMillis() + 20);
		flightPlan.setCruiseSpeedKts(cruiseSpeedKts);
		flightPlan.setCruiseAltitudeFt(cruiseAltitudeFt);
		flightPlan.setRoutePlan(routePlan);
		flightPlan.setDepartureCenter(departureCenter);
		flightPlan.setArrivalCenter(arrivalCenter);
		flightPlan.setDepartureFix(departureFix);
		flightPlan.setArrivalFix(arrivalFix);
		flightPlan.setPhysicalAircraftClass(physicalAircraftClass);
		flightPlan.setWeightAircraftClass(weightAircraftClass);
		flightPlan.setUserAircraftClass(userAircraftClass);
		flightPlan.setNumOfAircraft(numberOfAircraft);
		flightPlan.setAirborneEquipmentQualifier(airborneEquipmentQualifier);

		// create a DataPublicationEvent object to hold the data object

		DataPublicationEvent dp = new DataPublicationEvent();
		assertNotNull(dp);

		dp.setFederationExecutionModelHandle(UUID.randomUUID().toString());
		dp.setSourceOfEvent("Muthur");
		dp.setTimeToLiveSecs(10);
		dp.setDataAction(DataAction.Update);
		dp.setDataObject(flightPlan);

		// serialize the DP object
		//
		String dpAsXML = dp.serialize();

		// writeToFile(dp, false);

		// ...and do standard validity check
		//
		assertNotNull(dpAsXML);
		assertFalse(dpAsXML.equals(""));

		// create another DP object in order to create that from the serialized

		DataPublicationEvent dpFromXML = new DataPublicationEvent();

		// initialize new object from previous object's serialized form
		try {
			dpFromXML.initialization(dpAsXML);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}

		// check that the two objects are now equal
		//
		assertTrue(dpFromXML.equals(dp));

		// get the serialized version of the new DP object
		//
		String newDPSerializedAsXML = dpFromXML.serialize();

		assertNotNull(newDPSerializedAsXML);
		assertFalse(newDPSerializedAsXML.equals(""));

		IBaseDataObject bdo = dpFromXML.getDataObject();

		assertNotNull(bdo);
		assertTrue(bdo instanceof FlightPlan);

		/**
		 * Initialize the data object from the serialized DataPublication object
		 */
		try {
			bdo.initialization(newDPSerializedAsXML);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// check the serialized form of the data object
		String bdoSerialized = bdo.serialize();
		assertNotNull(bdoSerialized);

		// exception testing

		try {
			new FlightPlan(null, acid);
			fail("Should have thrown an exception due to null tail number");
		} catch (Exception e) {
			// should be here
		}

		try {
			new FlightPlan(tailNumber, null);
			fail("Should have thrown an exception due to null aircraft ID");
		} catch (Exception e) {
			// should be here
		}
	}

	/**
	 * 
	 */
	@Test
	public void testDataPublicationDeleteFlightPlanData() {

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

		flightPlan.setSource(source);
		flightPlan.setAircraftType(aircraftType);
		flightPlan.setPlannedDepartureTimeMSecs(System.currentTimeMillis() + 10);
		flightPlan.setPlannedArrivalTimeMSecs(System.currentTimeMillis() + 20);
		flightPlan.setCruiseSpeedKts(cruiseSpeedKts);
		flightPlan.setCruiseAltitudeFt(cruiseAltitudeFt);
		flightPlan.setRoutePlan(routePlan);
		flightPlan.setDepartureCenter(departureCenter);
		flightPlan.setArrivalCenter(arrivalCenter);
		flightPlan.setDepartureFix(departureFix);
		flightPlan.setArrivalFix(arrivalFix);
		flightPlan.setPhysicalAircraftClass(physicalAircraftClass);
		flightPlan.setWeightAircraftClass(weightAircraftClass);
		flightPlan.setUserAircraftClass(userAircraftClass);
		flightPlan.setNumOfAircraft(numberOfAircraft);
		flightPlan.setAirborneEquipmentQualifier(airborneEquipmentQualifier);

		// create a DataPublicationEvent object to hold the data object

		DataPublicationEvent dp = new DataPublicationEvent();
		assertNotNull(dp);

		dp.setFederationExecutionModelHandle(UUID.randomUUID().toString());
		dp.setSourceOfEvent("Muthur");
		dp.setTimeToLiveSecs(10);
		dp.setDataAction(DataAction.Delete);
		dp.setDataObject(flightPlan);

		// serialize the DP object
		//
		String dpAsXML = dp.serialize();

		// writeToFile(dp, false);

		// ...and do standard validity check
		//
		assertNotNull(dpAsXML);
		assertFalse(dpAsXML.equals(""));

		// create another DP object in order to create that from the serialized

		DataPublicationEvent dpFromXML = new DataPublicationEvent();

		// initialize new object from previous object's serialized form
		try {
			dpFromXML.initialization(dpAsXML);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}

		// check that the two objects are now equal
		//
		assertTrue(dpFromXML.equals(dp));

		// get the serialized version of the new DP object
		//
		String newDPSerializedAsXML = dpFromXML.serialize();

		assertNotNull(newDPSerializedAsXML);
		assertFalse(newDPSerializedAsXML.equals(""));

		IBaseDataObject bdo = dpFromXML.getDataObject();

		assertNotNull(bdo);
		assertTrue(bdo instanceof FlightPlan);

		/**
		 * Initialize the data object from the serialized DataPublication object
		 */
		try {
			bdo.initialization(newDPSerializedAsXML);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// check the serialized form of the data object
		String bdoSerialized = bdo.serialize();
		assertNotNull(bdoSerialized);

		// exception testing

		try {
			new FlightPlan(null, acid);
			fail("Should have thrown an exception due to null tail number");
		} catch (Exception e) {
			// should be here
		}

		try {
			new FlightPlan(tailNumber, null);
			fail("Should have thrown an exception due to null aircraft ID");
		} catch (Exception e) {
			// should be here
		}
	}

	/**
	 * 
	 */
	@Test
	public void testDataPublicationFlightPositionData() {

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

		fpd.setLatitudeDegrees(latitudeDegrees);
		fpd.setLongitudeDegrees(longitudeDegrees);
		fpd.setAltitudeFt(altitudeFt);
		fpd.setGroundspeedKts(groundspeedKts);
		fpd.setHeadingDegrees(headingDegrees);
		fpd.setAirspeedKts(airspeedKts);
		fpd.setPitchDegrees(pitchDegrees);
		fpd.setRollDegrees(rollDegrees);
		fpd.setYawDegrees(yawDegrees);
		fpd.setSector(sector);
		fpd.setCenter(center);

		// create a DataPublicationEvent object to hold the data object

		DataPublicationEvent dp = new DataPublicationEvent();
		assertNotNull(dp);

		dp.setFederationExecutionModelHandle(UUID.randomUUID().toString());
		dp.setSourceOfEvent("Muthur");
		dp.setTimeToLiveSecs(10);
		dp.setDataAction(DataAction.Add);
		dp.setDataObject(fpd);

		// serialize the DP object
		//
		String dpAsXML = dp.serialize();

		writeToFile(dp, false);

		// ...and do standard validity check
		//
		assertNotNull(dpAsXML);
		assertFalse(dpAsXML.equals(""));

		// create another DP object in order to create that from the serialized

		DataPublicationEvent dpFromXML = new DataPublicationEvent();

		// initialize new object from previous object's serialized form
		try {
			dpFromXML.initialization(dpAsXML);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}

		// check that the two objects are now equal
		//
		assertTrue(dpFromXML.equals(dp));

		// get the serialized version of the new DP object
		//
		String newDPSerializedAsXML = dpFromXML.serialize();

		assertNotNull(newDPSerializedAsXML);
		assertFalse(newDPSerializedAsXML.equals(""));

		IBaseDataObject bdo = dpFromXML.getDataObject();

		assertNotNull(bdo);
		assertTrue(bdo instanceof FlightPosition);

		/**
		 * Initialize the data object from the serialized DataPublication object
		 */
		try {
			bdo.initialization(newDPSerializedAsXML);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// check the serialized form of the data object
		String bdoSerialized = bdo.serialize();
		assertNotNull(bdoSerialized);

		// exception testing

		try {
			new FlightPosition(null, acid);
			fail("Should have thrown an exception due to null tail number");
		} catch (Exception e) {
			// should be here
		}

		try {
			new FlightPosition(tailNumber, null);
			fail("Should have thrown an exception due to null aircraft ID");
		} catch (Exception e) {
			// should be here
		}

	}

	/**
	 * 
	 */
	@Test
	public void testDataPublicationAircraftDepartureData() {

		AircraftDeparture aircraftDepartureData = null;

		try {
			aircraftDepartureData = new AircraftDeparture(tailNumber, acid);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(aircraftDepartureData);

		assertEquals(aircraftDepartureData.getCallSign(), acid);
		assertEquals(aircraftDepartureData.getTailNumber(), tailNumber);
		assertNotNull(aircraftDepartureData.getDataObjectUUID());
		assertFalse(aircraftDepartureData.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(aircraftDepartureData.getObjectCreateTimeMSecs() != 0L);

		aircraftDepartureData.setDepartureRunway("8R");

		aircraftDepartureData.setActualDepartureTimeMSecs(System
				.currentTimeMillis() + 100);

		// create a DataPublicationEvent object to hold the data object

		DataPublicationEvent dp = new DataPublicationEvent();
		assertNotNull(dp);

		dp.setFederationExecutionModelHandle(UUID.randomUUID().toString());
		dp.setSourceOfEvent("Muthur");
		dp.setTimeToLiveSecs(10);
		dp.setDataAction(DataAction.Add);
		dp.setDataObject(aircraftDepartureData);

		// serialize the DP object
		//
		String dpAsXML = dp.serialize();

		writeToFile(dp, false);

		// ...and do standard validity check
		//
		assertNotNull(dpAsXML);
		assertFalse(dpAsXML.equals(""));

		// create another DP object in order to create that from the serialized

		DataPublicationEvent dpFromXML = new DataPublicationEvent();

		// initialize new object from previous object's serialized form
		try {
			dpFromXML.initialization(dpAsXML);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}

		// check that the two objects are now equal
		//
		assertTrue(dpFromXML.equals(dp));

		// get the serialized version of the new DP object
		//
		String newDPSerializedAsXML = dpFromXML.serialize();

		assertNotNull(newDPSerializedAsXML);
		assertFalse(newDPSerializedAsXML.equals(""));

		IBaseDataObject bdo = dpFromXML.getDataObject();

		assertNotNull(bdo);
		assertTrue(bdo instanceof AircraftDeparture);

		/**
		 * Initialize the data object from the serialized DataPublication object
		 */
		try {
			bdo.initialization(newDPSerializedAsXML);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// check the serialized form of the data object
		String bdoSerialized = bdo.serialize();
		assertNotNull(bdoSerialized);

		// exception testing

		try {
			new AircraftDeparture(null, acid);
			fail("Should have thrown an exception due to null tail number");
		} catch (Exception e) {
			// should be here
		}

		try {
			new AircraftDeparture(tailNumber, null);
			fail("Should have thrown an exception due to null aircraft ID");
		} catch (Exception e) {
			// should be here
		}
	}

	/**
	 * 
	 */
	@Test
	public void testDataPublicationAircraftArrivalData() {

		AircraftArrival aircraftArrivalData = null;

		try {
			aircraftArrivalData = new AircraftArrival(tailNumber, acid);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(aircraftArrivalData);

		assertEquals(aircraftArrivalData.getCallSign(), acid);
		assertEquals(aircraftArrivalData.getTailNumber(), tailNumber);
		assertNotNull(aircraftArrivalData.getDataObjectUUID());
		assertFalse(aircraftArrivalData.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(aircraftArrivalData.getObjectCreateTimeMSecs() != 0L);
		aircraftArrivalData.setArrivalRunway("8L");
		aircraftArrivalData.setActualArrivalTimeMSecs(System.currentTimeMillis());

		// create a DataPublicationEvent object to hold the data object

		DataPublicationEvent dp = new DataPublicationEvent();
		assertNotNull(dp);

		dp.setFederationExecutionModelHandle(UUID.randomUUID().toString());
		dp.setSourceOfEvent("Muthur");
		dp.setTimeToLiveSecs(10);
		dp.setDataAction(DataAction.Add);
		dp.setDataObject(aircraftArrivalData);

		// serialize the DP object
		//
		String dpAsXML = dp.serialize();

		writeToFile(dp, false);

		// ...and do standard validity check
		//
		assertNotNull(dpAsXML);
		assertFalse(dpAsXML.equals(""));

		// create another DP object in order to create that from the serialized

		DataPublicationEvent dpFromXML = new DataPublicationEvent();

		// initialize new object from previous object's serialized form
		try {
			dpFromXML.initialization(dpAsXML);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}

		// check that the two objects are now equal
		//
		assertTrue(dpFromXML.equals(dp));

		// get the serialized version of the new DP object
		//
		String newDPSerializedAsXML = dpFromXML.serialize();

		assertNotNull(newDPSerializedAsXML);
		assertFalse(newDPSerializedAsXML.equals(""));

		IBaseDataObject bdo = dpFromXML.getDataObject();

		assertNotNull(bdo);
		assertTrue(bdo instanceof AircraftArrival);

		/**
		 * Initialize the data object from the serialized DataPublication object
		 */
		try {
			bdo.initialization(newDPSerializedAsXML);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// check the serialized form of the data object
		String bdoSerialized = bdo.serialize();
		assertNotNull(bdoSerialized);

		// exception testing

		try {
			new AircraftArrival(null, acid);
			fail("Should have thrown an exception due to null tail number");
		} catch (Exception e) {
			// should be here
		}

		try {
			new AircraftArrival(tailNumber, null);
			fail("Should have thrown an exception due to null aircraft ID");
		} catch (Exception e) {
			// should be here
		}

	}

	/**
	 * 
	 */
	@Test
	public void testDataPublicationAircraftTaxiInData() {

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

		aircraftTaxiInData.setTaxiInTimeMSecs(System.currentTimeMillis());
		aircraftTaxiInData.setTaxiInGate("D36");

		// create a DataPublicationEvent object to hold the data object

		DataPublicationEvent dp = new DataPublicationEvent();
		assertNotNull(dp);

		dp.setFederationExecutionModelHandle(UUID.randomUUID().toString());
		dp.setSourceOfEvent("Muthur");
		dp.setTimeToLiveSecs(10);
		dp.setDataAction(DataAction.Add);
		dp.setDataObject(aircraftTaxiInData);

		// serialize the DP object
		//
		String dpAsXML = dp.serialize();

		// ...and do standard validity check
		//
		assertNotNull(dpAsXML);
		assertFalse(dpAsXML.equals(""));

		// create another DP object in order to create that from the serialized

		DataPublicationEvent dpFromXML = new DataPublicationEvent();

		// initialize new object from previous object's serialized form
		try {
			dpFromXML.initialization(dpAsXML);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}

		// check that the two objects are now equal
		//
		assertTrue(dpFromXML.equals(dp));

		// get the serialized version of the new DP object
		//
		String newDPSerializedAsXML = dpFromXML.serialize();

		assertNotNull(newDPSerializedAsXML);
		assertFalse(newDPSerializedAsXML.equals(""));

		writeToFile(dp, false);

		IBaseDataObject bdo = dpFromXML.getDataObject();

		assertNotNull(bdo);
		assertTrue(bdo instanceof AircraftTaxiIn);

		/**
		 * Initialize the data object from the serialized DataPublication object
		 */
		try {
			bdo.initialization(newDPSerializedAsXML);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// check the serialized form of the data object
		String bdoSerialized = bdo.serialize();
		assertNotNull(bdoSerialized);

		// exception testing

		try {
			new AircraftTaxiOut(null, acid);
			fail("Should have thrown an exception due to null tail number");
		} catch (Exception e) {
			// should be here
		}

		try {
			new AircraftTaxiOut(tailNumber, null);
			fail("Should have thrown an exception due to null aircraft ID");
		} catch (Exception e) {
			// should be here
		}
	}

	/**
	 * 
	 */
	@Test
	public void testDataPublicationAircraftTaxiOutData() {

		AircraftTaxiOut aircraftTaxiOutData = null;

		try {
			aircraftTaxiOutData = new AircraftTaxiOut(tailNumber, acid);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(aircraftTaxiOutData);

		assertEquals(aircraftTaxiOutData.getCallSign(), acid);
		assertEquals(aircraftTaxiOutData.getTailNumber(), tailNumber);
		assertNotNull(aircraftTaxiOutData.getDataObjectUUID());
		assertFalse(aircraftTaxiOutData.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(aircraftTaxiOutData.getObjectCreateTimeMSecs() != 0L);

		aircraftTaxiOutData.setTaxiOutTimeMSecs(System.currentTimeMillis());
		aircraftTaxiOutData.setTaxiOutGate("D38");

		// create a DataPublicationEvent object to hold the data object

		DataPublicationEvent dp = new DataPublicationEvent();
		assertNotNull(dp);

		dp.setFederationExecutionModelHandle(UUID.randomUUID().toString());
		dp.setSourceOfEvent("Muthur");
		dp.setTimeToLiveSecs(10);
		dp.setDataAction(DataAction.Add);
		dp.setDataObject(aircraftTaxiOutData);

		// serialize the DP object
		//
		String dpAsXML = dp.serialize();

		// ...and do standard validity check
		//
		assertNotNull(dpAsXML);
		assertFalse(dpAsXML.equals(""));

		writeToFile(dp, false);

		// create another DP object in order to create that from the serialized

		DataPublicationEvent dpFromXML = new DataPublicationEvent();

		// initialize new object from previous object's serialized form
		try {
			dpFromXML.initialization(dpAsXML);
		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}

		// check that the two objects are now equal
		//
		assertTrue(dpFromXML.equals(dp));

		// get the serialized version of the new DP object
		//
		String newDPSerializedAsXML = dpFromXML.serialize();

		assertNotNull(newDPSerializedAsXML);
		assertFalse(newDPSerializedAsXML.equals(""));

		IBaseDataObject bdo = dpFromXML.getDataObject();

		assertNotNull(bdo);
		assertTrue(bdo instanceof AircraftTaxiOut);

		/**
		 * Initialize the data object from the serialized DataPublication object
		 */
		try {
			bdo.initialization(newDPSerializedAsXML);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// check the serialized form of the data object
		String bdoSerialized = bdo.serialize();
		assertNotNull(bdoSerialized);

		writeToFile(dp, false);

		// exception testing

		try {
			new AircraftTaxiOut(null, acid);
			fail("Should have thrown an exception due to null tail number");
		} catch (Exception e) {
			// should be here
		}

		try {
			new AircraftTaxiOut(tailNumber, null);
			fail("Should have thrown an exception due to null aircraft ID");
		} catch (Exception e) {
			// should be here
		}
	}
}
