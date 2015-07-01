/**
 * 
 */
package com.csc.muthur.server.model.event;

import java.util.Iterator;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.csc.muthur.server.commons.EventTypeEnum;
import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.DataAction;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.event.data.DataObjectFactory;
import com.csc.muthur.server.model.event.data.IBaseDataObject;
import com.csc.muthur.server.model.event.data.flight.Aircraft;
import com.csc.muthur.server.model.event.data.flight.AircraftTaxiIn;
import com.csc.muthur.server.model.event.data.flight.AircraftTaxiOut;
import com.csc.muthur.server.model.event.data.flight.FlightPlan;
import com.csc.muthur.server.model.event.data.flight.FlightPosition;
import com.csc.muthur.server.model.event.request.CreateObjectRequest;
import com.csc.muthur.server.model.event.request.DataSubscriptionRequest;
import com.csc.muthur.server.model.event.request.FederateRegistrationRequest;
import com.csc.muthur.server.model.event.request.JoinFederationRequest;
import com.csc.muthur.server.model.event.request.ListFedExecModelsRequest;
import com.csc.muthur.server.model.event.request.ReadyToRunRequest;
import com.csc.muthur.server.model.event.response.DataSubscriptionResponse;
import com.csc.muthur.server.model.event.response.FederateRegistrationResponse;
import com.csc.muthur.server.model.event.response.JoinFederationResponse;
import com.csc.muthur.server.model.event.response.ListFedExecModelsResponse;
import com.csc.muthur.server.model.event.response.ReadyToRunResponse;

/**
 * @author wdrew
 * 
 */
public class EventFactoryTest extends AbstractModelTest {

	private static String TEST_FEDERATE_NAME = "MyTestFederate";
	private static String TEST_FEM_NAME = "Frasca - NexSim Integration";
	private static String FRASCA_REQUIRED_FEDERATE = "Frasca";
	private static String NEXSIM_REQUIRED_FEDERATE = "NexSim";
	private static int TEST_TTL = 10;

	private String tailNumber = "N481UA";
	private String acid = "DAL333";

	// private String aircraftType = "B774";
	//
	// private String departureAirportCode = "JFK";
	// private String arrivalAirportCode = "ORD";
	// private double latitudeDegrees = 42.65;
	// private double longitudeDegress = -76.72;
	// private double altitudeFeet = 30000;
	// private double groundSpeedKts = 400;
	// private double headingDegrees = 90;
	// private double airSpeedKts = 444;
	// private double pitchDegrees = 0.5;
	// private double rollDegrees = 10;
	// private double yawDegress = 5;
	// private String sectorName = "ZOB48";
	// private String centerName = "ZOB";
	// private String source = "Flight";
	// private double cruiseSpeedKts = 330;
	// private double cruiseAltitudeFeet = 33000;
	// private String route = "IND..ROD.J29.PLB.J595.BGR..BGR";
	// private String departureCenter = "ZID";
	// private String arrivalCenter = "ZBW";
	// private String departureFix = "DEFIX";
	// private String arrivalFix = "ARFIX";
	// private String physicalClass = "J";
	// private String weightClass = "H";
	// private String userClass = "C";
	// private String airborneEquipmentQualifier = "R";

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// new ClassPathXmlApplicationContext(
		// "/META-INF/spring/model-bundle-context.xml",
		// "/META-INF/spring/model-event-context.xml");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link com.csc.muthur.server.model.event.EventFactory#createRequest(java.lang.String)}
	 * .
	 */
	@Test
	public void testFederateRegistrationRequestCreate() {

		FederateRegistrationRequest registrationRequest = null;

		registrationRequest = new FederateRegistrationRequest();
		registrationRequest.setSourceOfEvent(TEST_FEDERATE_NAME);
		registrationRequest.setTimeToLiveSecs(TEST_TTL);
		registrationRequest.setFederateEventQueueName("test-federate-queue-name");
		registrationRequest.setFederateName(NEXSIM_REQUIRED_FEDERATE);

		IEvent event = null;

		try {
			event =
					EventFactory.getInstance().createEvent(
							registrationRequest.serialize());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		if (event instanceof FederateRegistrationRequest) {
			FederateRegistrationRequest fedReg = (FederateRegistrationRequest) event;
			assertEquals("FederateRegistrationRequest", fedReg.getEventName());
		}

		writeToFile(event, false);

	}

	/**
	 * 
	 */
	@Test
	public void testListFedExecModelsRequestCreate() {

		ListFedExecModelsRequest event = null;

		event = new ListFedExecModelsRequest();
		event.setSourceOfEvent(TEST_FEDERATE_NAME);
		event.setTimeToLiveSecs(TEST_TTL);

		IEvent regeneratedEvent = null;

		try {
			regeneratedEvent =
					EventFactory.getInstance().createEvent(event.serialize());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(regeneratedEvent instanceof ListFedExecModelsRequest);

		ListFedExecModelsRequest fedReg =
				(ListFedExecModelsRequest) regeneratedEvent;
		assertEquals(EventTypeEnum.ListFedExecModelsRequest.toString(),
				fedReg.getEventName());
		assertEquals(event.getTimeToLiveSecs(), TEST_TTL);
		assertEquals(event.getSourceOfEvent(), TEST_FEDERATE_NAME);

		writeToFile(event, false);

	}

	/**
	 * 
	 */
	@Test
	public void testJoinFederationRequestCreate() {

		JoinFederationRequest event = null;

		event = new JoinFederationRequest();
		event.setSourceOfEvent(TEST_FEDERATE_NAME);
		event.setTimeToLiveSecs(TEST_TTL);

		FederationExecutionModel federationExecutionModel = null;
		try {
			federationExecutionModel = new FederationExecutionModel(TEST_FEM_NAME);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}
		federationExecutionModel.addRequiredFededrate(FRASCA_REQUIRED_FEDERATE);
		federationExecutionModel.addRequiredFededrate(NEXSIM_REQUIRED_FEDERATE);

		event.setFederationExecutionModel(federationExecutionModel);

		IEvent regeneratedEvent = null;

		try {
			regeneratedEvent =
					EventFactory.getInstance().createEvent(event.serialize());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(regeneratedEvent instanceof JoinFederationRequest);

		JoinFederationRequest req = (JoinFederationRequest) regeneratedEvent;
		assertEquals(EventTypeEnum.JoinFederationRequest.toString(),
				req.getEventName());
		assertEquals(event.getTimeToLiveSecs(), TEST_TTL);
		assertEquals(event.getSourceOfEvent(), TEST_FEDERATE_NAME);
		assertTrue(federationExecutionModel == event.getFederationExecutionModel());

		writeToFile(event, false);

	}

	/**
	 * 
	 */
	@Test
	public void testJoinFederationResponseCreate() {

		JoinFederationResponse event = new JoinFederationResponse();
		event.setSourceOfEvent(TEST_FEDERATE_NAME);
		event.setTimeToLiveSecs(TEST_TTL);

		FederationExecutionModel federationExecutionModel = null;
		try {
			federationExecutionModel = new FederationExecutionModel(TEST_FEM_NAME);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());

		}
		federationExecutionModel.addRequiredFededrate(FRASCA_REQUIRED_FEDERATE);
		federationExecutionModel.addRequiredFededrate(NEXSIM_REQUIRED_FEDERATE);

		IEvent regeneratedEvent = null;

		try {
			regeneratedEvent =
					EventFactory.getInstance().createEvent(event.serialize());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(regeneratedEvent instanceof JoinFederationResponse);

		JoinFederationResponse req = (JoinFederationResponse) regeneratedEvent;
		assertEquals(EventTypeEnum.JoinFederationResponse.toString(),
				req.getEventName());
		assertEquals(event.getTimeToLiveSecs(), TEST_TTL);
		assertEquals(event.getSourceOfEvent(), TEST_FEDERATE_NAME);

		writeToFile(event, false);

	}

	/**
	 * 
	 */
	@Test
	public void testDataSubscriptionRequestCreate() {

		DataSubscriptionRequest event = new DataSubscriptionRequest();

		event.setSourceOfEvent(TEST_FEDERATE_NAME);
		event.setTimeToLiveSecs(TEST_TTL);

		event.addSubscription("SpawnAircraft");

		IEvent regeneratedEvent = null;

		try {
			regeneratedEvent =
					EventFactory.getInstance().createEvent(event.serialize());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(regeneratedEvent instanceof DataSubscriptionRequest);

		DataSubscriptionRequest req = (DataSubscriptionRequest) regeneratedEvent;

		assertEquals(EventTypeEnum.DataSubscriptionRequest.toString(),
				req.getEventName());
		assertEquals(event.getTimeToLiveSecs(), TEST_TTL);
		assertEquals(event.getSourceOfEvent(), TEST_FEDERATE_NAME);

		writeToFile(event, false);
	}

	/**
	 * 
	 */
	@Test
	public void testDataSubscriptionResponseCreate() {

		DataSubscriptionResponse event = new DataSubscriptionResponse();

		event.setSourceOfEvent(TEST_FEDERATE_NAME);
		event.setTimeToLiveSecs(TEST_TTL);

		IEvent regeneratedEvent = null;

		try {
			regeneratedEvent =
					EventFactory.getInstance().createEvent(event.serialize());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(regeneratedEvent instanceof DataSubscriptionResponse);

		DataSubscriptionResponse req = (DataSubscriptionResponse) regeneratedEvent;

		assertEquals(EventTypeEnum.DataSubscriptionResponse.toString(),
				req.getEventName());
		assertEquals(event.getTimeToLiveSecs(), TEST_TTL);
		assertEquals(event.getSourceOfEvent(), TEST_FEDERATE_NAME);

		// with ttl = 0

		event = new DataSubscriptionResponse();

		event.setSourceOfEvent("Muthur");
		event.setTimeToLiveSecs(0);
		event.setSuccess(true);
		event.setFederationExecutionHandle(UUID.randomUUID().toString());

		regeneratedEvent = null;

		try {
			regeneratedEvent =
					EventFactory.getInstance().createEvent(event.serialize());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(regeneratedEvent instanceof DataSubscriptionResponse);

		req = (DataSubscriptionResponse) regeneratedEvent;

		assertEquals(EventTypeEnum.DataSubscriptionResponse.toString(),
				req.getEventName());
		assertEquals(event.getTimeToLiveSecs(), 0);
		assertEquals(event.getSourceOfEvent(), "Muthur");

		writeToFile(event, false);
	}

	/**
	 * 
	 */
	@Test
	public void testReadyToRunRequestCreate() {

		ReadyToRunRequest event = new ReadyToRunRequest();

		event.setSourceOfEvent(TEST_FEDERATE_NAME);
		event.setTimeToLiveSecs(TEST_TTL);

		String federateRegistrationHandle = UUID.randomUUID().toString();
		String federationExecutionHandle = UUID.randomUUID().toString();

		event.setFederateRegistrationHandle(federateRegistrationHandle);
		event.setFederationExecutionHandle(federationExecutionHandle);

		IEvent regeneratedEvent = null;

		try {
			regeneratedEvent =
					EventFactory.getInstance().createEvent(event.serialize());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(regeneratedEvent instanceof ReadyToRunRequest);

		ReadyToRunRequest req = (ReadyToRunRequest) regeneratedEvent;

		assertEquals(EventTypeEnum.ReadyToRunRequest.toString(), req.getEventName());
		assertEquals(event.getTimeToLiveSecs(), TEST_TTL);
		assertEquals(event.getSourceOfEvent(), TEST_FEDERATE_NAME);
		assertEquals(event.getFederateRegistrationHandle(),
				federateRegistrationHandle);
		assertEquals(event.getFederationExecutionHandle(),
				federationExecutionHandle);

		writeToFile(event, false);
	}

	/**
	 * 
	 */
	@Test
	public void testReadyToRunResponseCreate() {

		ReadyToRunResponse event = new ReadyToRunResponse();

		event.setSourceOfEvent(TEST_FEDERATE_NAME);
		event.setTimeToLiveSecs(TEST_TTL);

		String federateRegistrationHandle = UUID.randomUUID().toString();

		event.setFederateRegistrationHandle(federateRegistrationHandle);

		IEvent regeneratedEvent = null;

		try {
			regeneratedEvent =
					EventFactory.getInstance().createEvent(event.serialize());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(regeneratedEvent instanceof ReadyToRunResponse);

		ReadyToRunResponse req = (ReadyToRunResponse) regeneratedEvent;

		assertEquals(EventTypeEnum.ReadyToRunResponse.toString(),
				req.getEventName());
		assertEquals(event.getTimeToLiveSecs(), TEST_TTL);
		assertEquals(event.getSourceOfEvent(), TEST_FEDERATE_NAME);
		assertEquals(event.getFederateRegistrationHandle(),
				federateRegistrationHandle);

		writeToFile(event, false);
	}

	/**
	 * 
	 */
	@Test
	public void testFederateRegistrationResponseCreate() {

		FederateRegistrationResponse event = new FederateRegistrationResponse();

		event.setSourceOfEvent(TEST_FEDERATE_NAME);
		event.setTimeToLiveSecs(TEST_TTL);

		String federateRegistrationHandle = UUID.randomUUID().toString();
		event.setFederateRegistrationHandle(federateRegistrationHandle);

		IEvent regeneratedEvent = null;

		try {
			regeneratedEvent =
					EventFactory.getInstance().createEvent(event.serialize());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(regeneratedEvent instanceof FederateRegistrationResponse);

		FederateRegistrationResponse req =
				(FederateRegistrationResponse) regeneratedEvent;

		assertEquals(EventTypeEnum.FederateRegistrationResponse.toString(),
				req.getEventName());
		assertEquals(event.getTimeToLiveSecs(), TEST_TTL);
		assertEquals(event.getSourceOfEvent(), TEST_FEDERATE_NAME);
		assertEquals(event.getFederateRegistrationHandle(),
				federateRegistrationHandle);

		writeToFile(event, false);
	}

	/**
	 * 
	 */
	@Test
	public void testListFedExecModelsResponseCreate() {

		ListFedExecModelsResponse event = new ListFedExecModelsResponse();

		event.setSourceOfEvent(TEST_FEDERATE_NAME);
		event.setTimeToLiveSecs(TEST_TTL);

		String federateRegistrationHandle = UUID.randomUUID().toString();
		event.setFederateRegistrationHandle(federateRegistrationHandle);

		FederationExecutionModel federationExecutionModel = null;
		try {
			federationExecutionModel = new FederationExecutionModel(TEST_FEM_NAME);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());

		}
		federationExecutionModel.addRequiredFededrate(FRASCA_REQUIRED_FEDERATE);
		federationExecutionModel.addRequiredFededrate(NEXSIM_REQUIRED_FEDERATE);

		event.addFEM(federationExecutionModel);

		IEvent regeneratedEvent = null;

		try {
			regeneratedEvent =
					EventFactory.getInstance().createEvent(event.serialize());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(regeneratedEvent instanceof ListFedExecModelsResponse);

		ListFedExecModelsResponse req =
				(ListFedExecModelsResponse) regeneratedEvent;

		assertEquals(EventTypeEnum.ListFedExecModelsResponse.toString(),
				req.getEventName());

		assertEquals(event.getTimeToLiveSecs(), TEST_TTL);
		assertEquals(event.getSourceOfEvent(), TEST_FEDERATE_NAME);
		assertEquals(event.getFederateRegistrationHandle(),
				federateRegistrationHandle);

		Iterator<FederationExecutionModel> iter = req.getFemList().iterator();

		while (iter.hasNext()) {
			FederationExecutionModel fem = iter.next();
			assertEquals(federationExecutionModel, fem);
		}

		writeToFile(event, false);
	}

	@Test
	public void testCreateObjectRequestWithAircraft() {

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

		// create a CreateObjectRequest

		CreateObjectRequest cor = new CreateObjectRequest();
		assertNotNull(cor);

		cor.setDataObject(sacd);
		cor.setFederationExecutionHandle(UUID.randomUUID().toString());
		cor.setFederateRegistrationHandle(UUID.randomUUID().toString());
		cor.setFederationExecutionModelHandle(UUID.randomUUID().toString());

		IEvent event = null;

		try {
			event = EventFactory.getInstance().createEvent(cor.serialize());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assert (event != null);

		assert (event instanceof CreateObjectRequest);

		CreateObjectRequest corFromEventFactory = (CreateObjectRequest) event;

		assert (corFromEventFactory.serialize() != null);

		IBaseDataObject bdo = corFromEventFactory.getDataObject();

		assert (bdo instanceof Aircraft);

		writeToFile(event, false);

	}

	@Test
	public void testObjectOwnershipRelinquishEvent() {

		Aircraft aircraft = null;

		try {
			aircraft = new Aircraft(tailNumber, acid);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(aircraft);

		assertEquals(aircraft.getCallSign(), acid);
		assertEquals(aircraft.getTailNumber(), tailNumber);
		assertNotNull(aircraft.getDataObjectUUID());
		assertFalse(aircraft.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(aircraft.getObjectCreateTimeMSecs() != 0L);

		// create a ObjectOwnershipRelinquishedEvent

		ObjectOwnershipRelinquishedEvent oore =
				new ObjectOwnershipRelinquishedEvent();
		assertNotNull(oore);

		oore.setFederationExecutionHandle(UUID.randomUUID().toString());
		oore.setFederateRegistrationHandle(UUID.randomUUID().toString());
		oore.setSourceOfEvent("Muthur");
		oore.setTimeToLiveSecs(10);
		oore.setDataObjectUUID(aircraft.getDataObjectUUID());

		IEvent event = null;

		try {
			event = EventFactory.getInstance().createEvent(oore.serialize());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assert (event != null);

		assert (event instanceof ObjectOwnershipRelinquishedEvent);

		ObjectOwnershipRelinquishedEvent ooreEventFactory =
				(ObjectOwnershipRelinquishedEvent) event;

		assert (ooreEventFactory.serialize() != null);

		writeToFile(event, false);
	}

	/**
	 * 
	 */
	@Test
	public void testDataPublicationAircraftData() {

		String tailNumber = "N485UA";
		String acid = "DAL333";

		DataPublicationEvent dp = new DataPublicationEvent();
		assertNotNull(dp);

		dp.setFederationExecutionModelHandle(UUID.randomUUID().toString());

		Aircraft aircraft = null;

		try {
			aircraft = new Aircraft(tailNumber, acid);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(aircraft);

		assertEquals(aircraft.getCallSign(), acid);
		assertEquals(aircraft.getTailNumber(), tailNumber);
		assertNotNull(aircraft.getDataObjectUUID());
		assertFalse(aircraft.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(aircraft.getObjectCreateTimeMSecs() != 0L);
		dp.setDataObject(aircraft);

		dp.setDataAction(DataAction.Add);

		// test the creation of the event from the serialized from the data object
		// event

		testEventFactoryCreateDataPublicationEventAndInitialization(dp.serialize());

	}

	/**
	 * 
	 */
	@Test
	public void testDataPublicationAircraftTaxiOutData() {

		String tailNumber = "N485UA";
		String acid = "DAL333";

		DataPublicationEvent dp = new DataPublicationEvent();
		assertNotNull(dp);

		dp.setFederationExecutionModelHandle(UUID.randomUUID().toString());

		AircraftTaxiOut atod = null;

		try {
			atod = new AircraftTaxiOut(tailNumber, acid);
		} catch (MuthurException e) {
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
		dp.setDataAction(DataAction.Add);

		dp.setDataObject(atod);

		// test the creation of the event from the serialized from the data object
		// event

		testEventFactoryCreateDataPublicationEventAndInitialization(dp.serialize());

	}

	/**
	 * 
	 */
	@Test
	public void testDataPublicationFlightPlanFiledData() {

		String tailNumber = "N485UA";
		String acid = "DAL333";

		DataPublicationEvent dp = new DataPublicationEvent();
		assertNotNull(dp);

		dp.setFederationExecutionModelHandle(UUID.randomUUID().toString());

		FlightPlan fpfd = null;

		try {
			fpfd = new FlightPlan(tailNumber, acid);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(fpfd);

		assertEquals(fpfd.getCallSign(), acid);
		assertEquals(fpfd.getTailNumber(), tailNumber);
		assertNotNull(fpfd.getDataObjectUUID());
		assertFalse(fpfd.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(fpfd.getObjectCreateTimeMSecs() != 0L);

		// flight plan data

		fpfd.setSource("Flight");
		fpfd.setAircraftType("B771");
		fpfd.setPlannedDepartureTimeMSecs(System.currentTimeMillis() + 10);
		fpfd.setPlannedArrivalTimeMSecs(System.currentTimeMillis() + 20);
		fpfd.setCruiseSpeedKts(400);
		fpfd.setCruiseAltitudeFt(330);
		fpfd.setRoutePlan("IND..ROD.J29.PLB.J595.BGR..BGR");
		fpfd.setDepartureCenter("ZID");
		fpfd.setArrivalCenter("ZBW");
		fpfd.setDepartureFix("DEFIX");
		fpfd.setArrivalFix("ARFIX");
		fpfd.setPhysicalAircraftClass("J");
		fpfd.setWeightAircraftClass("H");
		fpfd.setUserAircraftClass("C");
		fpfd.setNumOfAircraft(1);
		fpfd.setAirborneEquipmentQualifier("R");

		// set the data object
		dp.setDataObject(fpfd);

		dp.setDataAction(DataAction.Add);

		// test the creation of the event from the serialized from the data object
		// event

		testEventFactoryCreateDataPublicationEventAndInitialization(dp.serialize());

	}

	/**
	 * 
	 */
	@Test
	public void testDataPublicationAircraftTaxiInData() {

		String tailNumber = "N485UA";
		String acid = "DAL333";

		DataPublicationEvent dp = new DataPublicationEvent();
		assertNotNull(dp);

		dp.setFederationExecutionModelHandle(UUID.randomUUID().toString());

		AircraftTaxiIn aircraftTaxiInData = null;

		try {
			aircraftTaxiInData = new AircraftTaxiIn(tailNumber, acid);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(aircraftTaxiInData);

		assertEquals(aircraftTaxiInData.getCallSign(), acid);
		assertEquals(aircraftTaxiInData.getTailNumber(), tailNumber);
		assertNotNull(aircraftTaxiInData.getDataObjectUUID());
		assertFalse(aircraftTaxiInData.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(aircraftTaxiInData.getObjectCreateTimeMSecs() != 0L);

		// taxi in data

		aircraftTaxiInData.setTaxiInTimeMSecs(System.currentTimeMillis() + 30);
		aircraftTaxiInData.setTaxiInGate("D5");

		// set the data object
		dp.setDataObject(aircraftTaxiInData);

		dp.setDataAction(DataAction.Add);

		// test the creation of the event from the serialized from the data object
		// event

		testEventFactoryCreateDataPublicationEventAndInitialization(dp.serialize());

	}

	/**
	 * 
	 */
	@Test
	public void testDataPublicationFlightPositionData() {

		String tailNumber = "N485UA";
		String acid = "DAL333";

		DataPublicationEvent dp = new DataPublicationEvent();
		assertNotNull(dp);

		dp.setFederationExecutionModelHandle(UUID.randomUUID().toString());

		double verticalspeedKts = 405;
		boolean aircraftOnGround = false;
		int aircraftTransmissionFrequency = 2850;
		String squawkCode = "0363"; // four characters
		boolean ident = true; // true if the A/C has been IDENT'd

		FlightPosition fpd = null;

		try {
			fpd = new FlightPosition(tailNumber, acid);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(fpd);

		assertEquals(fpd.getCallSign(), acid);
		assertEquals(fpd.getTailNumber(), tailNumber);
		assertNotNull(fpd.getDataObjectUUID());
		assertFalse(fpd.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(fpd.getObjectCreateTimeMSecs() != 0L);

		// position data

		fpd.setLatitudeDegrees(42.65);
		fpd.setLongitudeDegrees(-76.72);
		fpd.setAltitudeFt(30000);
		fpd.setGroundspeedKts(400);
		fpd.setHeadingDegrees(90);
		fpd.setAirspeedKts(444);
		fpd.setPitchDegrees(0.5);
		fpd.setRollDegrees(0);
		fpd.setYawDegrees(0);
		fpd.setSector("ZOB48");
		fpd.setCenter("ZOB");

		fpd.setVerticalspeedKts(verticalspeedKts);
		fpd.setAircraftOnGround(aircraftOnGround);
		fpd.setAircraftTransmissionFrequency(aircraftTransmissionFrequency);
		fpd.setSquawkCode(squawkCode);
		fpd.setIdent(ident);

		// set the data object

		dp.setDataObject(fpd);

		dp.setDataAction(DataAction.Add);

		// test the creation of the event from the serialized from the data object
		// event

		testEventFactoryCreateDataPublicationEventAndInitialization(dp.serialize());

	}

	/**
	 * Common code to create the IEvent from the serialized form of the
	 * DataPublicationEvent event using the
	 * {@link DataObjectFactory#getDataObject(com.csc.muthur.server.model.event.data.DataTypeEnum)}
	 * 
	 * 
	 * @param serializedDataPublicationObject
	 *          serialized DataPublicationEvent object
	 */
	private void testEventFactoryCreateDataPublicationEventAndInitialization(
			final String serializedDataPublicationObject) {

		IEvent event = null;

		try {
			event =
					EventFactory.getInstance().createEvent(
							serializedDataPublicationObject);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assert (event != null);

		assert (event instanceof DataPublicationEvent);

		DataPublicationEvent dp = (DataPublicationEvent) event;

		assert (event.serialize() != null);

		IBaseDataObject bdo = DataObjectFactory.getDataObject(dp.getDataType());

		try {
			bdo.initialization(event.serialize());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assert (!bdo.serialize().equals(""));

		writeToFile(dp, false);

	}

}
