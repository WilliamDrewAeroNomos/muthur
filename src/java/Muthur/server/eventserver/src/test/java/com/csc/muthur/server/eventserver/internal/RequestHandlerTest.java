package com.csc.muthur.server.eventserver.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.spi.InitialContextFactoryBuilder;
import javax.naming.spi.NamingManager;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.atcloud.commons.exception.ATCloudException;
import com.atcloud.dcache.DistributedCacheService;
import com.atcloud.dcache.internal.DistributedCacheServiceImpl;
import com.atcloud.fem.FederationExecutionModelService;
import com.atcloud.fem.internal.FederationExecutionModelServiceImpl;
import com.atcloud.license.internal.LicenseServiceImpl;
import com.atcloud.model.ATCloudDataModelException;
import com.atcloud.model.FEM;
import com.atcloud.model.Group;
import com.atcloud.model.License;
import com.atcloud.model.Organization;
import com.atcloud.model.SchemaFactorySourceLocator;
import com.atcloud.persistence.PersistenceService;
import com.atcloud.persistence.internal.PersistenceServiceImpl;
import com.atcloud.user.UserService;
import com.atcloud.user.internal.UserServiceImpl;
import com.csc.muthur.server.commons.CommonsService;
import com.csc.muthur.server.commons.DataChannelControlBlock;
import com.csc.muthur.server.commons.FederationExecutionID;
import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.commons.MessageDestination;
import com.csc.muthur.server.commons.ObjectOwnershipID;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.commons.internal.CommonsServiceImpl;
import com.csc.muthur.server.federation.FederationDataChannelServer;
import com.csc.muthur.server.federation.FederationService;
import com.csc.muthur.server.federation.internal.datachannel.DataEventFactoryImpl;
import com.csc.muthur.server.federation.internal.datachannel.DataEventHandlerFactoryImpl;
import com.csc.muthur.server.federation.internal.datachannel.FederationDataChannelListener;
import com.csc.muthur.server.federation.internal.datachannel.FederationDataChannelListenerFactory;
import com.csc.muthur.server.federation.internal.datachannel.FederationDataChannelServerImpl;
import com.csc.muthur.server.federation.internal.execution.FederationServiceImpl;
import com.csc.muthur.server.federation.internal.handler.CreateObjectRequestHandler;
import com.csc.muthur.server.federation.internal.handler.CreateOrUpdateObjectRequestHandler;
import com.csc.muthur.server.federation.internal.handler.DeleteObjectRequestHandler;
import com.csc.muthur.server.federation.internal.handler.ReadObjectRequestHandler;
import com.csc.muthur.server.federation.internal.handler.RelinquishObjectOwnershipRequestHandler;
import com.csc.muthur.server.federation.internal.handler.TransferObjectOwnershipRequestHandler;
import com.csc.muthur.server.federation.internal.handler.TransferObjectOwnershipResponseHandler;
import com.csc.muthur.server.federation.internal.handler.UpdateObjectAccessControlRequestHandler;
import com.csc.muthur.server.federation.internal.handler.UpdateObjectRequestHandler;
import com.csc.muthur.server.mocks.MockConfigurationServerImpl;
import com.csc.muthur.server.model.AccessControl;
import com.csc.muthur.server.model.FederationExecutionDirective;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.ModelService;
import com.csc.muthur.server.model.TransferOwnershipResponse;
import com.csc.muthur.server.model.event.DataPublicationEvent;
import com.csc.muthur.server.model.event.EventAttributeName;
import com.csc.muthur.server.model.event.EventFactory;
import com.csc.muthur.server.model.event.FederationTerminationEvent;
import com.csc.muthur.server.model.event.ObjectOwnershipRelinquishedEvent;
import com.csc.muthur.server.model.event.data.DataTypeEnum;
import com.csc.muthur.server.model.event.data.GeoPoint;
import com.csc.muthur.server.model.event.data.IBaseDataObject;
import com.csc.muthur.server.model.event.data.Runway;
import com.csc.muthur.server.model.event.data.RunwayFlow;
import com.csc.muthur.server.model.event.data.RunwayName;
import com.csc.muthur.server.model.event.data.airport.AirportConfiguration;
import com.csc.muthur.server.model.event.data.flight.Aircraft;
import com.csc.muthur.server.model.event.data.flight.AircraftDeparture;
import com.csc.muthur.server.model.event.data.flight.FlightPlan;
import com.csc.muthur.server.model.event.data.flight.FlightPosition;
import com.csc.muthur.server.model.event.data.vehicle.VehicleDataObject;
import com.csc.muthur.server.model.event.data.weather.WeatherDataObject;
import com.csc.muthur.server.model.event.request.CreateObjectRequest;
import com.csc.muthur.server.model.event.request.CreateOrUpdateObjectRequest;
import com.csc.muthur.server.model.event.request.DataSubscriptionRequest;
import com.csc.muthur.server.model.event.request.DeleteObjectRequest;
import com.csc.muthur.server.model.event.request.FederateRegistrationRequest;
import com.csc.muthur.server.model.event.request.JoinFederationRequest;
import com.csc.muthur.server.model.event.request.ListFedExecModelsRequest;
import com.csc.muthur.server.model.event.request.PauseFederationRequest;
import com.csc.muthur.server.model.event.request.ReadObjectRequest;
import com.csc.muthur.server.model.event.request.ReadyToRunRequest;
import com.csc.muthur.server.model.event.request.RelinquishObjectOwnershipRequest;
import com.csc.muthur.server.model.event.request.ResumeFederationRequest;
import com.csc.muthur.server.model.event.request.StartFederationRequest;
import com.csc.muthur.server.model.event.request.TimeManagementRequest;
import com.csc.muthur.server.model.event.request.TransferObjectOwnershipRequest;
import com.csc.muthur.server.model.event.request.UpdateFederationExecutionTimeRequest;
import com.csc.muthur.server.model.event.request.UpdateObjectAccessControlRequest;
import com.csc.muthur.server.model.event.request.UpdateObjectRequest;
import com.csc.muthur.server.model.event.response.CreateObjectResponse;
import com.csc.muthur.server.model.event.response.DataSubscriptionResponse;
import com.csc.muthur.server.model.event.response.DeleteObjectResponse;
import com.csc.muthur.server.model.event.response.FederateRegistrationResponse;
import com.csc.muthur.server.model.event.response.JoinFederationResponse;
import com.csc.muthur.server.model.event.response.ListFedExecModelsResponse;
import com.csc.muthur.server.model.event.response.PauseFederationResponse;
import com.csc.muthur.server.model.event.response.ReadObjectResponse;
import com.csc.muthur.server.model.event.response.ReadyToRunResponse;
import com.csc.muthur.server.model.event.response.RegisterPublicationResponse;
import com.csc.muthur.server.model.event.response.RelinquishObjectOwnershipResponse;
import com.csc.muthur.server.model.event.response.TransferObjectOwnershipResponse;
import com.csc.muthur.server.model.event.response.UpdateFederationExecutionTimeResponse;
import com.csc.muthur.server.model.event.response.UpdateObjectAccessControlResponse;
import com.csc.muthur.server.model.event.response.UpdateObjectResponse;
import com.csc.muthur.server.model.internal.ModelServiceImpl;
import com.csc.muthur.server.object.ObjectService;
import com.csc.muthur.server.object.internal.ObjectServiceImpl;
import com.csc.muthur.server.ownership.OwnershipService;
import com.csc.muthur.server.ownership.internal.OwnedObjectList;
import com.csc.muthur.server.ownership.internal.OwnershipServiceImpl;
import com.csc.muthur.server.registration.RegistrationService;
import com.csc.muthur.server.registration.internal.RegistrationServiceImpl;
import com.csc.muthur.server.router.internal.RouterServiceImpl;
import com.csc.muthur.server.time.TimeService;
import com.csc.muthur.server.time.internal.TimeServiceImpl;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class RequestHandlerTest {

	// ActiveMQ

	private static BrokerService broker;
	private static ActiveMQConnectionFactory connectionFactory;
	private static Connection connection;
	private static Session session;

	// queues for sending and/or receiving messages

	private static TemporaryQueue federationEventQueue;
	private static TemporaryQueue dataPublicationQueueNexSim;
	private static TemporaryQueue replyToQueueNexsim;

	private static TemporaryQueue federationTimeManagementQueueNexSim;
	private static TemporaryQueue federateRequestEventQueueNexSim;

	private static TemporaryQueue federationTimeManagementQueueFrasca;
	private static TemporaryQueue federateRequestEventQueueFrasca;
	private static TemporaryQueue replyToQueueFrasca;

	private static TemporaryQueue dataPublicationQueueFrasca;

	private static MessageConsumer fedEventConsumer;

	// event/response handlers

	private static ResponseHandler nexSimFederateResponseHandler;
	private static ResponseHandler frascaFederateResponseHandler;
	private static FederationTimeManagementEventHandler federationTimeMgmtEventHandlerNexSim;
	private static FederationTimeManagementEventHandler federationTimeMgmtEventHandlerFrasca;

	//
	// semaphores

	private static Semaphore semResponseHandler;
	private static Semaphore semFederateRequestHandler;
	private static Semaphore semTimeMgmtEventHandler;
	private static Semaphore semTermination = new Semaphore(0);
	private static Semaphore semDataPublication = new Semaphore(0);
	private static Semaphore semDataDeletionPublication = new Semaphore(0);

	// service references that will be set in each handler as required

	private static MockConfigurationServerImpl configurationService;
	private static RouterServiceImpl routerService;
	private static FederationService federationService;
	private static RegistrationService registrationService;
	private static ObjectService objectService;
	private static OwnershipService ownershipService;
	private static ModelService modelService;
	private static CommonsService commonsService;
	private static TimeService timeService;

	private static ObjectOwnershipID objectOwnershipID = null;
	private static ObjectOwnershipID transferToObjectOwnershipID = null;
	private static FederateRegistrationRequest federateRegistrationRequest;

	private String atisCode = "VALID-VALUE-REQUIRED-HERE";
	private String metarString = "CY#AS18000 - this is a test remark - NE12";

	private String centerFieldWindDirection = "NE";
	private double centerFieldWindSpeedKts = 25.345;
	private String beaconCode = "636";

	private double groundspeedKts = 230.0;
	private double latitudeDegrees = 38.948004;
	private double longitudeDegrees = -77.44148;

	private double altitudeFt = 3500;
	private double headingDegrees = 180.40;
	private int vehicleTransmissionFrequency = 2300;

	// objects used to test the create/update/delete request handlers
	//
	private static Aircraft aircraft = null;
	private static FlightPlan flightPlan = null;
	private static WeatherDataObject weatherDataObject = null;
	private static FlightPosition flightPosition = null;
	private static VehicleDataObject vehicleDataObject = null;

	// object used to test the createOrUpdate request handler

	private static Aircraft aircraftForCreateOrUpdate = null;

	// attributes for the Aircraft object

	private String tailNumber = "N481UA";
	private String callSign = "DAL333";
	private String aircraftType = "B774";

	// attributes for the Aircraft object created with CreateOrUpdateObject
	// call
	private String tailNumberForCreateOrUpdate = "M592MD";
	private String callSignForCreateOrUpdate = "UAL567";

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
	private double verticalspeedKts = 405;
	private boolean aircraftOnGround = false;
	private int aircraftTransmissionFrequency = 2850;

	private String squawkCode = "0363"; // four characters
	private boolean ident = true; // true if the A/C has been IDENT'd
	private String dullesAirportCode = "KIAD";

	// federate names

	private static String federateNexSim = "NexSim";
	private static String federateFrascaFTD = "FrascaFTD";

	// federation execution model

	private static FederationExecutionModel federationExecutionModel;

	private static Set<String> namesOfRequiredFederates = new TreeSet<String>();
	private int timeToLiveSecs = 60000;
	private static int terminationWaitMSecs = 5000;
	private static String name = "Test Model";
	private static String description = "This is a test model";
	private int reqDurationMSecs = 10000;

	// registration, fem and ownership handles

	private static FederationExecutionID federationExecutionID = null;

	private static long startingFederationTime = 0L;

	// License service attributes

	private static UserService userService;
	private static PersistenceService persistenceService;
	private static License licenseOne;
	private static com.atcloud.model.ModelService atCloudModelService;

	private static EntityManager em;
	private static FederationExecutionModelService federationExecutionModelService;
	private static Group newGroup;
	private static LicenseServiceImpl licenseService;
	private static License licenseTwo;

	/**
	 * 
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		// This message broker is embedded for testing purposes. It emulates
		// having the ActiveMQ bundle running in the OSGi container
		//
		broker = new BrokerService();
		broker.setPersistent(false);
		broker.setUseJmx(false);

		/**
		 * Create the FEM
		 */

		namesOfRequiredFederates = new TreeSet<String>();
		namesOfRequiredFederates.add(federateNexSim);
		namesOfRequiredFederates.add(federateFrascaFTD);

		federationExecutionModel = new FederationExecutionModel(name);

		federationExecutionModel.setName(name);
		federationExecutionModel.setDescription(description);
		federationExecutionModel.setDurationFederationExecutionMSecs(60000);
		federationExecutionModel.setFedExecModelUUID(UUID.randomUUID().toString());
		federationExecutionModel.setLogicalStartTimeMSecs(System
				.currentTimeMillis());
		federationExecutionModel
				.setNamesOfRequiredFederates(namesOfRequiredFederates);
		federationExecutionModel
				.setDefaultDurationWithinStartupProtocolMSecs(60000);
		federationExecutionModel
				.setDurationTimeToWaitAfterTerminationMSecs(terminationWaitMSecs);
		federationExecutionModel.setAutoStart(false);

		/**
		 * Configuration service
		 */

		commonsService = new CommonsServiceImpl();

		configurationService = new MockConfigurationServerImpl();

		int availablePort = commonsService.findAvailablePort(6000, 7000);

		assert (availablePort != -1);

		configurationService.setMessagingPort(availablePort);
		configurationService.setGenerateHeartBeat(false);

		String connUrl = configurationService.getMessagingConnectionUrl();

		broker.addConnector(connUrl);
		broker.start();

		// connect to JMS server

		connectionFactory =
				new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
						ActiveMQConnection.DEFAULT_PASSWORD, connUrl);

		connection = connectionFactory.createConnection();

		connection.start();

		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		// create the federation level event queue and consumer

		federationEventQueue = session.createTemporaryQueue();

		fedEventConsumer = session.createConsumer(federationEventQueue);

		fedEventConsumer.setMessageListener(new FedEventConsumer());

		// create a temporary reply-to queue for replies to NexSim federate
		// requests

		replyToQueueNexsim = session.createTemporaryQueue();

		MessageConsumer replyQueueConsumerNexSim =
				session.createConsumer(replyToQueueNexsim);

		nexSimFederateResponseHandler = new ResponseHandler();

		replyQueueConsumerNexSim.setMessageListener(nexSimFederateResponseHandler);

		// create a temporary reply-to queue for replies to Frasca FTD federate
		// requests

		replyToQueueFrasca = session.createTemporaryQueue();

		MessageConsumer replyQueueConsumerFrasca =
				session.createConsumer(replyToQueueFrasca);

		frascaFederateResponseHandler = new ResponseHandler();

		replyQueueConsumerFrasca.setMessageListener(frascaFederateResponseHandler);

		// Data publication queue - all satisfied subscriptions are received
		// here

		dataPublicationQueueNexSim = session.createTemporaryQueue();

		session.createConsumer(dataPublicationQueueNexSim).setMessageListener(
				new DataPublicationHandler());

		dataPublicationQueueFrasca = session.createTemporaryQueue();

		session.createConsumer(dataPublicationQueueFrasca).setMessageListener(
				new DataPublicationHandler());

		// Creating queues and consumers to receive federation time management
		// messages
		//

		federationTimeManagementQueueNexSim = session.createTemporaryQueue();

		federationTimeMgmtEventHandlerNexSim =
				new FederationTimeManagementEventHandler();

		session.createConsumer(federationTimeManagementQueueNexSim)
				.setMessageListener(federationTimeMgmtEventHandlerNexSim);

		federationTimeManagementQueueFrasca = session.createTemporaryQueue();

		federationTimeMgmtEventHandlerFrasca =
				new FederationTimeManagementEventHandler();

		session.createConsumer(federationTimeManagementQueueFrasca)
				.setMessageListener(federationTimeMgmtEventHandlerFrasca);

		// create temporary queues and attach consumers for receiving transfer
		// ownership requests

		federateRequestEventQueueNexSim = session.createTemporaryQueue();

		FederateRequestHandler transferObjectOwnershipHandlerNexSim =
				new FederateRequestHandler(federateNexSim,
						nexSimFederateResponseHandler);

		transferObjectOwnershipHandlerNexSim
				.setAcceptTransferOwnershipRequests(true);

		session.createConsumer(federateRequestEventQueueNexSim).setMessageListener(
				transferObjectOwnershipHandlerNexSim);

		federateRequestEventQueueFrasca = session.createTemporaryQueue();

		FederateRequestHandler transferObjectOwnershipHandlerFrasca =
				new FederateRequestHandler(federateFrascaFTD,
						frascaFederateResponseHandler);

		transferObjectOwnershipHandlerFrasca
				.setAcceptTransferOwnershipRequests(true);

		session.createConsumer(federateRequestEventQueueFrasca).setMessageListener(
				transferObjectOwnershipHandlerFrasca);

		// initiate the event response dispatcher which will create
		// a producer which will be used to publish messages to the reply
		// queue included in each message
		//
		EventMessageResponseDispatcher.getInstance(session);

		com.atcloud.commons.CommonsService atCloudCommonsService =
				new com.atcloud.commons.internal.CommonsServiceImpl();

		/*
		 * Initialize the embedded DB
		 */

		try {

			InitialContextFactoryBuilder icfb =
					atCloudCommonsService.getInitialContextFactoryBuilder();

			assertNotNull(icfb);

			NamingManager.setInitialContextFactoryBuilder(icfb);

			InitialContext ic = new InitialContext();
			EmbeddedDataSource ds = new EmbeddedDataSource();
			ds.setDatabaseName("target/test");
			ds.setCreateDatabase("create");
			ic.bind(
					"osgi:service/javax.sql.DataSource/(osgi.jndi.service.name=jdbc/derbyds)",
					ds);

			EntityManagerFactory emf =
					Persistence.createEntityManagerFactory("userTest",
							System.getProperties());

			em = emf.createEntityManager();

		} catch (Throwable t) {
			fail(t.getLocalizedMessage());
		}

		/*
		 * Set up License service and all it's required services
		 */

		userService = new UserServiceImpl();

		commonsService.start();

		/*
		 * ATCloud model service
		 */
		atCloudModelService = new com.atcloud.model.internal.ModelServiceImpl();

		atCloudModelService
				.setSchemaFactorySourceLocator(new SchemaFactorySourceLocator() {

					@Override
					public String getURL(String sourceName)
							throws ATCloudDataModelException {
						return System.getenv("ATCLOUD_SCHEMA_DIR") + "/" + sourceName;

					}

				});

		atCloudModelService.start();

		/*
		 * cache service
		 */
		DistributedCacheService distributeCacheService =
				new DistributedCacheServiceImpl();

		distributeCacheService.start();

		/*
		 * User service
		 */
		userService.setDcacheService(distributeCacheService);

		userService.setCommonsService(atCloudCommonsService);

		persistenceService = new PersistenceServiceImpl();

		/*
		 * Setup and start FederationExecutionModelService
		 */

		persistenceService = new PersistenceServiceImpl();
		persistenceService.setEntityManager(em);

		federationExecutionModelService = new FederationExecutionModelServiceImpl();

		federationExecutionModelService.setModelService(atCloudModelService);
		federationExecutionModelService.setCommonsService(atCloudCommonsService);
		federationExecutionModelService.setDcacheService(distributeCacheService);
		federationExecutionModelService.setPersistenceService(persistenceService);

		federationExecutionModelService.start();

		/**
		 * Model service
		 */

		modelService = new ModelServiceImpl();

		modelService.start();

		/**
		 * Router service
		 */

		routerService = new RouterServiceImpl();

		routerService.setConfigurationServer(configurationService);

		routerService.start();

		/**
		 * Time service
		 */

		timeService = new TimeServiceImpl();

		timeService.setConfigurationService(configurationService);
		timeService.setModelService(modelService);

		timeService.start();

		/**
		 * Registration service
		 */

		registrationService = new RegistrationServiceImpl();

		registrationService.setConfigurationService(configurationService);
		registrationService.setRouterService(routerService);

		registrationService.start();

		/**
		 * Object service
		 */

		objectService = new ObjectServiceImpl();

		objectService.setConfigurationService(configurationService);
		objectService.setModelService(modelService);
		objectService.setCommonsService(commonsService);

		objectService.start();

		/**
		 * Federation service
		 */

		federationService = new FederationServiceImpl();

		federationService.setConfigurationService(configurationService);
		federationService.setRouterService(routerService);
		federationService.setCommonsService(commonsService);
		federationService.setObjectService(objectService);
		federationService.setTimeService(timeService);

		/*
		 * Create event factory
		 */
		DataEventFactoryImpl dataEventFactory = new DataEventFactoryImpl();
		dataEventFactory.setApplicationContext(null);

		/*
		 * Create event handler factory
		 */
		DataEventHandlerFactoryImpl dataEventHandlerFactory =
				new DataEventHandlerFactoryImpl();
		dataEventHandlerFactory.setApplicationContext(null);

		/*
		 * Create the data channel listener and set the data event and data event
		 * handler factory
		 */
		FederationDataChannelListener dataChannelListener =
				new FederationDataChannelListener();
		dataChannelListener.setDataEventFactory(dataEventFactory);
		dataChannelListener.setDataEventHandlerFactory(dataEventHandlerFactory);
		dataChannelListener.setFederationService(federationService);

		/*
		 * Create channel lister factory and set the data event factory and data
		 * event handler factory
		 */
		FederationDataChannelListenerFactory dataChannelListenerFactory =
				new FederationDataChannelListenerFactory();
		dataChannelListenerFactory.setDataChannelListener(dataChannelListener);

		FederationDataChannelServer federationDataChannelServer =
				new FederationDataChannelServerImpl();

		federationDataChannelServer
				.setDataChannelListenerFactory(dataChannelListenerFactory);
		federationDataChannelServer.setPortNumber(54545);

		/*
		 * set the federation data channel server in the federation service
		 */
		federationService
				.setFederationDataChannelServer(federationDataChannelServer);

		federationService.start();

		/**
		 * Ownership service
		 */

		ownershipService = new OwnershipServiceImpl();

		ownershipService.setConfigurationService(configurationService);
		ownershipService.setRouterService(routerService);

		ownershipService.start();

		/*
		 * User persistence service
		 */
		persistenceService.setEntityManager(em);

		persistenceService.start();

		userService.setPersistenceService(persistenceService);

		em.getTransaction().begin();

		userService.start();

		em.getTransaction().commit();

		persistenceService.setModelService(atCloudModelService);

		licenseService = new LicenseServiceImpl();

		licenseService.setModelService(atCloudModelService);

		licenseService.setPersistenceService(persistenceService);

		licenseService.start();

		registrationService.setLicenseService(licenseService);

		modelService
				.setFederationExecutionModelService(federationExecutionModelService);

		/*
		 * Clean out database of FEMs and Groups
		 */

		em.getTransaction().begin();

		persistenceService.deleteAllFEMs();
		persistenceService.deleteAllFederates();
		persistenceService.deleteAllGroups();

		em.getTransaction().commit();

		/*
		 * Add the FEM to the database
		 */
		FEM fem = new FEM();

		fem.setName(federationExecutionModel.getName());
		fem.setDescription(federationExecutionModel.getDescription());
		fem.setFemID(federationExecutionModel.getFedExecModelUUID());

		fem.setLogicalStrtTimeMSecs(federationExecutionModel
				.getLogicalStartTimeMSecs());

		fem.setAutoStart(federationExecutionModel.isAutoStart());

		fem.setDefDurStrtupPrtclMSecs(federationExecutionModel
				.getDefaultDurationWithinStartupProtocolMSecs());

		fem.setFederationExecutionMSecs(federationExecutionModel
				.getDurationFederationExecutionMSecs());

		fem.setJoinFederationMSecs(federationExecutionModel
				.getDurationJoinFederationMSecs());

		fem.setRegisterPublicationMSecs(federationExecutionModel
				.getDurationRegisterPublicationMSecs());

		fem.setRegisterPublicationMSecs(federationExecutionModel
				.getDurationRegisterSubscriptionMSecs());

		fem.setRegisterToRunMSecs(federationExecutionModel
				.getDurationRegisterToRunMSecs());

		fem.setWaitForStartMSecs(federationExecutionModel
				.getDurationWaitForStartFederationDirectiveMSecs());

		fem.setWaitForStartMSecs(federationExecutionModel
				.getDurationTimeToWaitAfterTerminationMSecs());

		em.getTransaction().begin();

		federationExecutionModelService.addFEM(fem);

		// add the federates to the FEM

		int descCtr = 1;

		for (String fedName : namesOfRequiredFederates) {
			federationExecutionModelService.addFederate(fedName,
					"Description federate #" + descCtr);
			federationExecutionModelService.addFederateToFEM(fedName, fem.getName());
		}

		em.getTransaction().commit();

		addLicenses();

		addGroupAndFEM();

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

		if (connection != null) {
			connection.stop();
		}
		if (session != null) {
			session.close();
		}
		if (broker.isStarted()) {
			broker.stop();
		}

		broker = null;
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * 
	 */
	@Test
	public void runAllTests() {

		testRegistrationRequestEventHandler();
		testListFedExecModelsRequestHandler();

		testJoinFederationRequestHandler();
		testRegisterSubscriptionsRequestHandler();

		testReadyToRunRequestHandler();
		testUpdateFederationTimeRequestHandler();

		// TODO: Need to fix this
		// testStartFederationRequestHandler();
		testCreateObjectRequestHandlerWithAircraft();

		testCreateObjectRequestHandlerWithFlightPlan();
		testCreateObjectRequestHandlerWithFlightPosition();

		testCreateObjectRequestHandlerWithAircraftDepartureObject();
		testCreateObjectRequestHandlerWithAirportConfigurationObject();

		testCreateObjectRequestHandlerWithWeather();
		testCreateObjectRequestHandlerWithVehicle();

		testCreateOrUpdateObjectRequestHandlerWithAddAircraft();
		testCreateOrUpdateObjectRequestHandlerWithUpdateAircraft();

		testReadObjectRequestHandler();
		testUpdateObjectRequestHandler();

		testUpdateObjectAttributeControlRequestHandler();
		testUpdateTransferredFlightPlanObjectRequestHandler();

		testRelinquishObjectOwnershipRequestHandlerWithAircraft();
		testTransferObjectOwnershipRequestHandlerWithAircraft();

		testTransferObjectOwnershipRequestHandlerTransferFromNexSimToFrascaFTD();

		// testDeleteObjectRequestHandlerFlightPlan();
		testDeleteObjectRequestHandlerAircraft();

		// testPauseFederationRequestHandler();
		// testResumeFederationRequestHandler();

		testTerminateFederationEventHandler();

	}

	/**
	 * Tests the registration handler by registering the NexSim and Frasca
	 * federates.
	 * 
	 */
	public void testRegistrationRequestEventHandler() {

		semResponseHandler = new Semaphore(0);

		registerFederate(federateNexSim, federateRequestEventQueueNexSim,
				replyToQueueNexsim, licenseOne.getLicenseID());

		// register the second participant

		registerFederate(federateFrascaFTD, federateRequestEventQueueFrasca,
				replyToQueueFrasca, licenseTwo.getLicenseID());

		// try to acquire 2 permits on the semaphore - 1 for each participant

		try {
			if (!semResponseHandler.tryAcquire(2, 60, TimeUnit.SECONDS)) {
				fail("Timed out waiting for response from registration.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

	}

	/**
	 * Helper function to register a federate
	 * 
	 * @param federateName
	 */
	private void registerFederate(final String federateName,
			final TemporaryQueue federationEventQueue,
			final TemporaryQueue replyToQueue, final String licenseID) {

		// create a FederateRegistrationRequest

		federateRegistrationRequest = new FederateRegistrationRequest();

		federateRegistrationRequest.setSourceOfEvent(federateName);
		federateRegistrationRequest.setFederateName(federateName);
		federateRegistrationRequest.setTimeToLiveSecs(timeToLiveSecs);
		federateRegistrationRequest.setFederateHeartBeatIntervalSecs(30); // seems
		// good
		federateRegistrationRequest.setLicenseKey(licenseID);

		try {
			federateRegistrationRequest
					.setFederateEventQueueName(federationEventQueue.getQueueName());
		} catch (JMSException e) {
			fail(e.getLocalizedMessage());
		}

		// create the TextMessage and...

		TextMessage txtMessage = null;

		try {

			txtMessage = session.createTextMessage();

			// ...set the body as the serialized event

			txtMessage.setText(federateRegistrationRequest.serialize());

			// Set the reply to field to the reply queue on which the server
			// will
			// respond

			txtMessage.setJMSReplyTo(replyToQueue);

			// Set a correlation ID from the event

			txtMessage
					.setJMSCorrelationID(federateRegistrationRequest.getEventUUID());

			txtMessage.setStringProperty(
					MessageDestination.getFederationEventQueuePropName(),
					federationEventQueue.getQueueName());

		} catch (JMSException e) {
			fail(e.getLocalizedMessage());
		}

		RegistrationRequestEventHandler rrh = new RegistrationRequestEventHandler();

		// do what the event factory does...

		rrh.setMessage(txtMessage);

		// do what the event handler factory does...

		rrh.setEvent(federateRegistrationRequest);

		// emulate what the container and Spring will do with IoC...

		setServiceReferences(rrh);

		try {
			em.getTransaction().begin();
			rrh.handle();
			em.getTransaction().commit();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Tests the list federation execution model handler
	 */
	public void testListFedExecModelsRequestHandler() {

		ListFedExecModelsRequest event = new ListFedExecModelsRequest();

		event.setSourceOfEvent(federateNexSim);
		event.setFederateRegistrationHandle(nexSimFederateResponseHandler
				.getFederateRegistrationHandle());
		event.setTimeToLiveSecs(reqDurationMSecs);
		event.setGroupName(newGroup.getName());

		assertNotNull(event);

		// create the TextMessage and...

		TextMessage txtMessage = null;

		try {

			txtMessage = session.createTextMessage();

			// ...set the body as the serialized event

			txtMessage.setText(event.serialize());

			// Set the reply to field to the reply queue on which the server
			// will
			// respond

			txtMessage.setJMSReplyTo(replyToQueueNexsim);

			// Set a correlation ID from the event

			txtMessage.setJMSCorrelationID(event.getEventUUID());

		} catch (JMSException e) {
			fail(e.getLocalizedMessage());
		}

		ListFedExecModelsRequestHandler lfemrh =
				new ListFedExecModelsRequestHandler();

		// do what the event factory does...

		lfemrh.setMessage(txtMessage);

		// do what the event handler factory does...

		lfemrh.setEvent(event);

		// emulate what the container and Spring will do with IoC...

		setServiceReferences(lfemrh);

		semResponseHandler = new Semaphore(0);

		try {
			lfemrh.handle();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			if (!semResponseHandler.tryAcquire(1, 60, TimeUnit.SECONDS)) {
				fail("Timed out waiting for response "
						+ "from list federation execution models request.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

	}

	/**
	 * Tests the join federation handler by joining the NexSim and Frasca
	 * federates into the federation execution model
	 * {@link #federationExecutionModel}.
	 * 
	 */
	public void testJoinFederationRequestHandler() {

		semResponseHandler = new Semaphore(0);

		sendJoinFederationRequest(federateNexSim, nexSimFederateResponseHandler,
				replyToQueueNexsim);

		sendJoinFederationRequest(federateFrascaFTD, frascaFederateResponseHandler,
				replyToQueueFrasca);

		try {
			if (!semResponseHandler.tryAcquire(2, 60, TimeUnit.SECONDS)) {
				fail("Timed out waiting for response from join request.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

	}

	/**
	 * 
	 * @param federateName
	 */
	private void sendJoinFederationRequest(final String federateName,
			final ResponseHandler responseHandler, final TemporaryQueue replyToQueue) {
		JoinFederationRequest req = new JoinFederationRequest();

		assertNotNull(req);

		req.setTimeToLiveSecs(reqDurationMSecs);
		req.setSourceOfEvent(federateName);
		req.setFederateRegistrationHandle(responseHandler
				.getFederateRegistrationHandle());
		req.setFederationExecutionModel(federationExecutionModel);
		req.setFederationExecutionHandle(responseHandler
				.getFederationExecutionHandle());

		// create the TextMessage and...

		TextMessage txtMessage = null;

		try {

			txtMessage = session.createTextMessage();

			// ...set the body as the serialized event

			txtMessage.setText(req.serialize());

			// Set the reply to field to the reply queue on which the server
			// will
			// respond

			txtMessage.setJMSReplyTo(replyToQueue);

			// Set a correlation ID from the event

			txtMessage.setJMSCorrelationID(req.getEventUUID());

			txtMessage.setStringProperty(
					MessageDestination.getFederationEventQueuePropName(),
					federationEventQueue.getQueueName());

		} catch (JMSException e) {
			fail(e.getLocalizedMessage());
		}

		JoinFederationRequestHandler jfrh = new JoinFederationRequestHandler();

		// do what the event factory does...

		jfrh.setMessage(txtMessage);

		// do what the event handler factory does...

		jfrh.setEvent(req);

		// emulate what the container and Spring will do with IoC...

		setServiceReferences(jfrh);

		try {
			jfrh.handle();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Tests the subscription handler for NexSim and Frasca federates.
	 * 
	 */
	public void testRegisterSubscriptionsRequestHandler() {

		semResponseHandler = new Semaphore(0);

		// create data subscription request

		sendRegisterFedereateSubscriptionRequest(federateNexSim,
				dataPublicationQueueNexSim, replyToQueueNexsim,
				nexSimFederateResponseHandler, replyToQueueNexsim);

		sendRegisterFedereateSubscriptionRequest(federateFrascaFTD,
				dataPublicationQueueFrasca, replyToQueueFrasca,
				frascaFederateResponseHandler, replyToQueueFrasca);

		try {
			if (!semResponseHandler.tryAcquire(2, 60, TimeUnit.SECONDS)) {
				fail("Timed out waiting for response from subscription registration.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

	}

	/**
	 * 
	 * @param federateName
	 */
	private void sendRegisterFedereateSubscriptionRequest(
			final String federateName, final TemporaryQueue dataPubQueue,
			final TemporaryQueue ownershipQueue,
			final ResponseHandler responseHandler, final TemporaryQueue replyToQueue) {

		DataSubscriptionRequest request = new DataSubscriptionRequest();

		request.setSourceOfEvent(federateName);
		request.setTimeToLiveSecs(timeToLiveSecs);
		request.setFederationExecutionModelHandle(federationExecutionModel
				.getFedExecModelUUID());
		try {
			request.setSubscriptionQueueName(dataPubQueue.getQueueName());
		} catch (JMSException e) {
			fail(e.getLocalizedMessage());
		}

		// add the ownership event queue name
		try {
			request.setOwnershipEventQueueName(ownershipQueue.getQueueName());
		} catch (JMSException e) {
			fail(e.getLocalizedMessage());
		}

		request.setFederateRegistrationHandle(responseHandler
				.getFederateRegistrationHandle());
		request.setFederationExecutionHandle(responseHandler
				.getFederationExecutionHandle());

		// add the data type names

		request.addSubscription(DataTypeEnum.Aircraft.toString());
		request.addSubscription(DataTypeEnum.FlightPlan.toString());
		request.addSubscription(DataTypeEnum.FlightPosition.toString());

		// create the TextMessage and...

		TextMessage txtMessage = null;

		try {

			txtMessage = session.createTextMessage();

			// ...set the body as the serialized event

			txtMessage.setText(request.serialize());

			// ...set the reply to field to the reply queue on which the server
			// will
			// respond

			txtMessage.setJMSReplyTo(replyToQueue);

			// ...set a correlation ID from the event

			txtMessage.setJMSCorrelationID(request.getEventUUID());

			// ...set the data event queue property to data event queue name

			txtMessage.setStringProperty(
					MessageDestination.getDataEventQueuePropName(),
					dataPublicationQueueNexSim.getQueueName());

		} catch (JMSException e) {
			fail(e.getLocalizedMessage());
		}

		DataSubscriptionRequestHandler dsrh = new DataSubscriptionRequestHandler();

		// do what the event factory does...

		dsrh.setMessage(txtMessage);

		// do what the event handler factory does...

		dsrh.setEvent(request);

		// emulate what the container and Spring will do with IoC...

		setServiceReferences(dsrh);

		try {
			dsrh.handle();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Tests the ready to run handler for NexSim and Frasca federates.
	 * 
	 */
	public void testReadyToRunRequestHandler() {

		sendReadyToRunFederateRequest(federateNexSim,
				federateRequestEventQueueNexSim, federationTimeManagementQueueNexSim,
				replyToQueueNexsim, nexSimFederateResponseHandler,
				dataPublicationQueueNexSim);

		sendReadyToRunFederateRequest(federateFrascaFTD,
				federateRequestEventQueueFrasca, federationTimeManagementQueueFrasca,
				replyToQueueFrasca, frascaFederateResponseHandler,
				dataPublicationQueueFrasca);

		try {
			if (!semResponseHandler.tryAcquire(2, 60, TimeUnit.SECONDS)) {
				fail("Timed out waiting for response from ready to run.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

	}

	/**
	 * 
	 * @param federateName
	 */
	private void sendReadyToRunFederateRequest(final String federateName,
			final TemporaryQueue federateRequestEventQueue,
			final TemporaryQueue federationTimeMgmtQueue,
			final TemporaryQueue replyToQueue, final ResponseHandler responseHandler,
			final TemporaryQueue dataPubQueue) {

		// create ready to run request

		ReadyToRunRequest event = new ReadyToRunRequest();

		event.setSourceOfEvent(federateName);
		event.setFederationExecutionModelHandle(federationExecutionModel
				.getFedExecModelUUID());
		event.setFederationExecutionHandle(responseHandler
				.getFederationExecutionHandle());
		event.setTimeToLiveSecs(timeToLiveSecs);

		// add the time management and federate request queue names
		try {
			event.setFederateRequestQueueName(federateRequestEventQueue
					.getQueueName());
			event.setTimeManagementQueueName(federationTimeMgmtQueue.getQueueName());
		} catch (JMSException e) {
			fail(e.getLocalizedMessage());
		}

		// create the TextMessage and...

		TextMessage txtMessage = null;

		try {

			txtMessage = session.createTextMessage();

			// ...set the body as the serialized event

			txtMessage.setText(event.serialize());

			// Set the reply to field to the reply queue on which the server
			// will
			// respond

			txtMessage.setJMSReplyTo(replyToQueue);

			// Set a correlation ID from the event

			txtMessage.setJMSCorrelationID(event.getEventUUID());

			txtMessage.setStringProperty(
					MessageDestination.getDataEventQueuePropName(),
					dataPubQueue.getQueueName());

		} catch (JMSException e) {
			fail(e.getLocalizedMessage());
		}

		ReadyToRunRequestHandler rrrh = new ReadyToRunRequestHandler();

		// do what the event factory does...

		rrrh.setMessage(txtMessage);

		// do what the event handler factory does...

		rrrh.setEvent(event);

		// emulate what the container and Spring will do with IoC...

		setServiceReferences(rrrh);

		semResponseHandler = new Semaphore(0);

		try {
			rrrh.handle();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Tests the update federation time request handler for the federation.
	 * 
	 */
	public void testUpdateFederationTimeRequestHandler() {

		// create update federation time request

		UpdateFederationExecutionTimeRequest req =
				new UpdateFederationExecutionTimeRequest();

		req.setSourceOfEvent(federateNexSim);
		req.setFederationExecutionModelHandle(federationExecutionModel
				.getFedExecModelUUID());
		req.setTimeToLiveSecs(10);

		// current time
		Calendar adjustTimeBack = Calendar.getInstance();

		adjustTimeBack.setTimeInMillis(System.currentTimeMillis());

		adjustTimeBack.add(Calendar.MONTH, -5);

		req.setFederationExecutionTimeMSecs(adjustTimeBack.getTimeInMillis());

		startingFederationTime = req.getFederationExecutionTimeMSecs();

		assertNotNull(req);

		// create the TextMessage and...

		TextMessage txtMessage = null;

		try {

			txtMessage = session.createTextMessage();

			// ...set the body as the serialized event

			txtMessage.setText(req.serialize());

			// Set the reply to field to the reply queue on which the server
			// will
			// respond

			txtMessage.setJMSReplyTo(replyToQueueNexsim);

			// Set a correlation ID from the event

			txtMessage.setJMSCorrelationID(req.getEventUUID());

			txtMessage.setStringProperty(
					MessageDestination.getFederationEventQueuePropName(),
					federationEventQueue.getQueueName());

		} catch (JMSException e) {
			fail(e.getLocalizedMessage());
		}

		UpdateFederationExecutionTimeRequestHandler rh =
				new UpdateFederationExecutionTimeRequestHandler();

		// do what the event factory does...

		rh.setMessage(txtMessage);

		// do what the event handler factory does...

		rh.setEvent(req);

		// emulate what the container and Spring will do with IoC...

		setServiceReferences(rh);

		semResponseHandler = new Semaphore(0);

		// runningResponsesReceived = 0;

		try {
			rh.handle();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			if (!semResponseHandler.tryAcquire(60, TimeUnit.SECONDS)) {
				fail("Timed out waiting for response from federation execution time update.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

	}

	/**
	 * Tests the start federation handler for the federation.
	 * 
	 */
	public void testStartFederationRequestHandler() {

		// create start federation request

		StartFederationRequest req = new StartFederationRequest();

		req.setSourceOfEvent(federateNexSim);
		req.setFederationExecutionModelHandle(federationExecutionModel
				.getFedExecModelUUID());
		req.setFederationExecutionHandle(nexSimFederateResponseHandler
				.getFederationExecutionHandle());
		req.setTimeToLiveSecs(timeToLiveSecs);

		// create the TextMessage and...

		TextMessage txtMessage = null;

		try {

			txtMessage = session.createTextMessage();

			// ...set the body as the serialized event

			txtMessage.setText(req.serialize());

			// Set the reply to field to the reply queue on which the server
			// will
			// respond

			txtMessage.setJMSReplyTo(replyToQueueNexsim);

			// Set a correlation ID from the event

			txtMessage.setJMSCorrelationID(req.getEventUUID());

			txtMessage.setStringProperty(
					MessageDestination.getFederationEventQueuePropName(),
					federationEventQueue.getQueueName());

		} catch (JMSException e) {
			fail(e.getLocalizedMessage());
		}

		StartFederationRequestHandler rh = new StartFederationRequestHandler();

		// do what the event factory does...

		rh.setMessage(txtMessage);

		// do what the event handler factory does...

		rh.setEvent(req);

		// emulate what the container and Spring will do with IoC...

		setServiceReferences(rh);

		semTimeMgmtEventHandler = new Semaphore(0);

		try {
			rh.handle();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			if (!semTimeMgmtEventHandler.tryAcquire(30, TimeUnit.SECONDS)) {
				fail("Timed out waiting for response from start federation request.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

	}

	/**
	 * Tests the create object request handler
	 * 
	 */
	public void testCreateObjectRequestHandlerWithAircraft() {

		// create an object to send to the create object handler
		//

		try {
			aircraft = new Aircraft(tailNumber, callSign);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(aircraft);

		assertEquals(aircraft.getCallSign(), callSign);
		assertEquals(aircraft.getTailNumber(), tailNumber);
		assertNotNull(aircraft.getDataObjectUUID());
		assertFalse(aircraft.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(aircraft.getObjectCreateTimeMSecs() != 0L);

		// create create object request

		sendCreateObjectRequestMessage(aircraft);

		try {
			if (!semResponseHandler.tryAcquire(1, 30, TimeUnit.SECONDS)) {
				fail("Timed out waiting for response from create object request.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

	}

	/**
	 * 
	 * @param dataObject
	 */
	private void sendCreateObjectRequestMessage(IBaseDataObject dataObject) {

		CreateObjectRequest req = new CreateObjectRequest();

		req.setDataObject(dataObject);

		req.setSourceOfEvent(federateNexSim);
		req.setFederateRegistrationHandle(nexSimFederateResponseHandler
				.getFederateRegistrationHandle());
		req.setFederationExecutionHandle(nexSimFederateResponseHandler
				.getFederationExecutionHandle());
		req.setFederationExecutionModelHandle(federationExecutionModel
				.getFedExecModelUUID());
		req.setTimeToLiveSecs(timeToLiveSecs);

		CreateObjectRequestHandler rh = new CreateObjectRequestHandler();

		// do what the event handler factory does...

		rh.setEvent(req);

		// emulate what the container and Spring will do with DI...

		rh.setRegistrationService(registrationService);

		rh.setObjectService(objectService);

		rh.setOwnershipService(ownershipService);

		rh.setModelService(modelService);

		rh.setFederationService(federationService);

		rh.setCommonsService(commonsService);

		rh.setRouterService(routerService);

		DataChannelControlBlock dccBlock = new DataChannelControlBlock();

		try {
			dccBlock.setReplyToQueueName(replyToQueueNexsim.getQueueName());
		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		}
		dccBlock.setCorrelationID(req.getEventUUID());
		dccBlock.setEventName(req.getEventName());
		dccBlock.setPayLoadLength(req.serialize().length());

		rh.setDataChannelControlBlock(dccBlock);

		semResponseHandler = new Semaphore(0);

		try {
			rh.handle();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * @param rh
	 */
	private void setServiceReferences(IEventHandler rh) {

		rh.setRegistrationServer(registrationService);

		rh.setObjectService(objectService);

		rh.setOwnershipService(ownershipService);

		rh.setModelService(modelService);

		rh.setFederationService(federationService);

		rh.setCommonsService(commonsService);

		rh.setFederationExecutionModelService(federationExecutionModelService);
	}

	/**
	 * Tests the create object request handler for adding a flight plan
	 * 
	 */
	public void testCreateObjectRequestHandlerWithFlightPlan() {

		// create an object to send to the create object handler
		//

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

		assertNotNull(flightPlan);

		assertEquals(flightPlan.getCallSign(), callSign);
		assertEquals(flightPlan.getTailNumber(), tailNumber);
		assertNotNull(flightPlan.getDataObjectUUID());
		assertFalse(flightPlan.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(flightPlan.getObjectCreateTimeMSecs() != 0L);

		// create create object request

		sendCreateObjectRequestMessage(flightPlan);

		try {
			if (!semResponseHandler.tryAcquire(1, 30, TimeUnit.SECONDS)) {
				fail("Timed out waiting for response from create object request.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

	}

	/**
	 * Tests the create object request handler for adding a flight plan
	 * 
	 */
	public void testCreateObjectRequestHandlerWithFlightPosition() {

		// create an object to send to the create object handler
		//

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

		assertNotNull(flightPosition);

		assertEquals(flightPosition.getCallSign(), callSign);
		assertEquals(flightPosition.getTailNumber(), tailNumber);
		assertNotNull(flightPosition.getDataObjectUUID());
		assertFalse(flightPosition.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(flightPosition.getObjectCreateTimeMSecs() != 0L);

		// create create object request

		sendCreateObjectRequestMessage(flightPosition);

		try {
			if (!semResponseHandler.tryAcquire(1, 30, TimeUnit.SECONDS)) {
				fail("Timed out waiting for response from create object request.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

	}

	/**
	 * Tests the create object request handler for adding a flight plan
	 * 
	 */
	public void testCreateObjectRequestHandlerWithAircraftDepartureObject() {

		// create an object to send to the create object handler
		//

		AircraftDeparture aircraftDeparture = null;

		try {
			aircraftDeparture = new AircraftDeparture(tailNumber, callSign);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(aircraftDeparture);

		assertEquals(aircraftDeparture.getCallSign(), callSign);
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

		assertNotNull(aircraftDeparture);

		assertEquals(aircraftDeparture.getCallSign(), callSign);
		assertEquals(aircraftDeparture.getTailNumber(), tailNumber);
		assertNotNull(aircraftDeparture.getDataObjectUUID());
		assertFalse(aircraftDeparture.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(aircraftDeparture.getObjectCreateTimeMSecs() != 0L);

		// create create object request

		sendCreateObjectRequestMessage(aircraftDeparture);

		try {
			if (!semResponseHandler.tryAcquire(1, 30, TimeUnit.SECONDS)) {
				fail("Timed out waiting for response from create object request.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

	}

	/**
	 * Tests the create object request handler for adding a flight plan
	 * 
	 */
	public void testCreateObjectRequestHandlerWithAirportConfigurationObject() {

		// create an object to send to the create object handler
		//

		AirportConfiguration airportConfiguration = null;

		try {
			airportConfiguration = new AirportConfiguration(dullesAirportCode);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(airportConfiguration);

		assertEquals(airportConfiguration.getAirportCode(), dullesAirportCode);
		assertNotNull(airportConfiguration.getDataObjectUUID());
		assertFalse(airportConfiguration.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(airportConfiguration.getObjectCreateTimeMSecs() != 0L);

		// set the runways

		Map<RunwayName, Runway> runways =
				new ConcurrentHashMap<RunwayName, Runway>();

		Runway runway = new Runway();
		runway.setName("1R");

		GeoPoint gp = new GeoPoint();
		gp.setAltitude(312.0);
		gp.setLatitude(38.9474444);
		gp.setLongitude(-77.4599444);
		runway.setLowerRight(gp);

		gp = new GeoPoint();
		gp.setAltitude(312.0);
		gp.setLatitude(39.9475555);
		gp.setLongitude(-78.4599555);
		runway.setUpperLeft(gp);

		runway.setRunwayFlow(RunwayFlow.ARRIVAL);
		runways.put(new RunwayName(runway.getName().getName()), runway);

		airportConfiguration.setRunways(runways);

		// create create object request

		sendCreateObjectRequestMessage(airportConfiguration);

		try {
			if (!semResponseHandler.tryAcquire(1, 30, TimeUnit.SECONDS)) {
				fail("Timed out waiting for response from create object request.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

	}

	/**
	 * Tests the create object request handler for adding a weather object
	 * 
	 */
	public void testCreateObjectRequestHandlerWithWeather() {

		// create an object to send to the create object handler
		//

		weatherDataObject = null;

		try {
			weatherDataObject = new WeatherDataObject(dullesAirportCode);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		weatherDataObject.setAirportCode(dullesAirportCode);
		weatherDataObject.setAtisCode(atisCode);
		weatherDataObject.setCenterFieldWind(centerFieldWindDirection);
		weatherDataObject.setCenterFieldWindSpeedKts(centerFieldWindSpeedKts);
		weatherDataObject.setMetarString(metarString);

		assertNotNull(weatherDataObject);

		assertFalse(weatherDataObject.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(weatherDataObject.getObjectCreateTimeMSecs() != 0L);

		assertTrue(weatherDataObject.getAirportCode().equals(dullesAirportCode));
		assertTrue(weatherDataObject.getAtisCode().equals(atisCode));
		assertTrue(weatherDataObject.getCenterFieldWind().equals(
				centerFieldWindDirection));
		assertTrue(weatherDataObject.getCenterFieldWindSpeedKts() == centerFieldWindSpeedKts);
		assertTrue(weatherDataObject.getMetarString().equals(metarString));

		// create create object request

		sendCreateObjectRequestMessage(weatherDataObject);

		try {
			if (!semResponseHandler.tryAcquire(1, 30, TimeUnit.SECONDS)) {
				fail("Timed out waiting for response from create weather object request.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

	}

	/**
	 * Tests the create object request handler for adding a vehicle object
	 * 
	 */
	public void testCreateObjectRequestHandlerWithVehicle() {

		vehicleDataObject = null;

		try {
			vehicleDataObject = new VehicleDataObject();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(vehicleDataObject);

		vehicleDataObject.setAirportCode(dullesAirportCode);
		vehicleDataObject.setBeaconCode(beaconCode);
		vehicleDataObject.setGroundspeedKts(groundspeedKts);
		vehicleDataObject.setLatitudeDegrees(latitudeDegrees);
		vehicleDataObject.setLongitudeDegrees(longitudeDegrees);
		vehicleDataObject.setAltitudeFt(altitudeFt);
		vehicleDataObject.setHeadingDegrees(headingDegrees);
		vehicleDataObject
				.setVehicleTransmissionFrequency(vehicleTransmissionFrequency);
		assertNotNull(vehicleDataObject);

		assertFalse(vehicleDataObject.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(vehicleDataObject.getObjectCreateTimeMSecs() != 0L);

		assertTrue(vehicleDataObject.getAirportCode().equals(dullesAirportCode));
		assertTrue(vehicleDataObject.getBeaconCode().equals(beaconCode));
		assertTrue(vehicleDataObject.getGroundspeedKts() == groundspeedKts);
		assertTrue(vehicleDataObject.getLatitudeDegrees() == latitudeDegrees);
		assertTrue(vehicleDataObject.getLongitudeDegrees() == longitudeDegrees);
		assertTrue(vehicleDataObject.getAltitudeFt() == altitudeFt);
		assertTrue(vehicleDataObject.getHeadingDegrees() == headingDegrees);
		assertTrue(vehicleDataObject.getVehicleTransmissionFrequency() == vehicleTransmissionFrequency);

		// create create object request

		sendCreateObjectRequestMessage(vehicleDataObject);

		try {
			if (!semResponseHandler.tryAcquire(1, 30, TimeUnit.SECONDS)) {
				fail("Timed out waiting for response from create vehicle object request.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

	}

	/**
	 * Tests the create or update object request handler for an aircraft
	 * 
	 */
	public void testCreateOrUpdateObjectRequestHandlerWithAddAircraft() {

		// create an object to send to the create object handler
		//

		try {
			aircraftForCreateOrUpdate =
					new Aircraft(tailNumberForCreateOrUpdate, callSignForCreateOrUpdate);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(aircraftForCreateOrUpdate);

		assertEquals(aircraftForCreateOrUpdate.getCallSign(),
				callSignForCreateOrUpdate);
		assertEquals(aircraftForCreateOrUpdate.getTailNumber(),
				tailNumberForCreateOrUpdate);
		assertNotNull(aircraftForCreateOrUpdate.getDataObjectUUID());
		assertFalse(aircraftForCreateOrUpdate.getDataObjectUUID().equalsIgnoreCase(
				""));
		assertTrue(aircraftForCreateOrUpdate.getObjectCreateTimeMSecs() != 0L);

		// create createOrUpdateObject request

		CreateOrUpdateObjectRequest req = new CreateOrUpdateObjectRequest();

		req.setDataObject(aircraftForCreateOrUpdate);

		req.setSourceOfEvent(federateNexSim);
		req.setFederateRegistrationHandle(nexSimFederateResponseHandler
				.getFederateRegistrationHandle());
		req.setFederationExecutionHandle(nexSimFederateResponseHandler
				.getFederationExecutionHandle());
		req.setFederationExecutionModelHandle(federationExecutionModel
				.getFedExecModelUUID());
		req.setTimeToLiveSecs(timeToLiveSecs);

		CreateOrUpdateObjectRequestHandler rh =
				new CreateOrUpdateObjectRequestHandler();

		// do what the event handler factory does...

		rh.setEvent(req);

		// emulate what the container and Spring will do with DI...

		rh.setRegistrationService(registrationService);

		rh.setObjectService(objectService);

		rh.setOwnershipService(ownershipService);

		rh.setModelService(modelService);

		rh.setFederationService(federationService);

		rh.setCommonsService(commonsService);

		rh.setRouterService(routerService);

		DataChannelControlBlock dccBlock = new DataChannelControlBlock();

		try {
			dccBlock.setReplyToQueueName(replyToQueueNexsim.getQueueName());
		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		}
		dccBlock.setCorrelationID(req.getEventUUID());
		dccBlock.setEventName(req.getEventName());
		dccBlock.setPayLoadLength(req.serialize().length());

		rh.setDataChannelControlBlock(dccBlock);

		semResponseHandler = new Semaphore(0);

		try {
			rh.handle();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			if (!semResponseHandler.tryAcquire(1, 30, TimeUnit.SECONDS)) {
				fail("Timed out waiting for response from "
						+ "create or update object request.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

	}

	/**
	 * Tests the create or update object request handler for an aircraft
	 * 
	 */
	public void testCreateOrUpdateObjectRequestHandlerWithUpdateAircraft() {

		assertNotNull(aircraftForCreateOrUpdate);

		assertEquals(aircraftForCreateOrUpdate.getCallSign(),
				callSignForCreateOrUpdate);
		assertEquals(aircraftForCreateOrUpdate.getTailNumber(),
				tailNumberForCreateOrUpdate);
		assertNotNull(aircraftForCreateOrUpdate.getDataObjectUUID());
		assertFalse(aircraftForCreateOrUpdate.getDataObjectUUID().equalsIgnoreCase(
				""));
		assertTrue(aircraftForCreateOrUpdate.getObjectCreateTimeMSecs() != 0L);

		// create createOrUpdateObject request

		CreateOrUpdateObjectRequest req = new CreateOrUpdateObjectRequest();

		req.setDataObject(aircraftForCreateOrUpdate);

		req.setSourceOfEvent(federateNexSim);
		req.setFederateRegistrationHandle(nexSimFederateResponseHandler
				.getFederateRegistrationHandle());
		req.setFederationExecutionHandle(nexSimFederateResponseHandler
				.getFederationExecutionHandle());
		req.setFederationExecutionModelHandle(federationExecutionModel
				.getFedExecModelUUID());
		req.setTimeToLiveSecs(timeToLiveSecs);

		CreateOrUpdateObjectRequestHandler rh =
				new CreateOrUpdateObjectRequestHandler();

		// do what the event handler factory does...

		rh.setEvent(req);

		// emulate what the container and Spring will do with DI...

		rh.setRegistrationService(registrationService);

		rh.setObjectService(objectService);

		rh.setOwnershipService(ownershipService);

		rh.setModelService(modelService);

		rh.setFederationService(federationService);

		rh.setCommonsService(commonsService);

		rh.setRouterService(routerService);

		DataChannelControlBlock dccBlock = new DataChannelControlBlock();

		try {
			dccBlock.setReplyToQueueName(replyToQueueNexsim.getQueueName());
		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		}
		dccBlock.setCorrelationID(req.getEventUUID());
		dccBlock.setEventName(req.getEventName());
		dccBlock.setPayLoadLength(req.serialize().length());

		rh.setDataChannelControlBlock(dccBlock);

		semResponseHandler = new Semaphore(0);

		try {
			rh.handle();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			if (!semResponseHandler.tryAcquire(1, 30, TimeUnit.SECONDS)) {
				fail("Timed out waiting for response from "
						+ "create or update object request.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

	}

	/**
	 * Tests the read object request handler
	 * 
	 */
	public void testReadObjectRequestHandler() {

		// create create object request

		ReadObjectRequest req = new ReadObjectRequest();

		req.setDataObjectUUID(aircraft.getDataObjectUUID());

		req.setSourceOfEvent(federateNexSim);
		req.setFederateRegistrationHandle(nexSimFederateResponseHandler
				.getFederateRegistrationHandle());
		req.setFederationExecutionHandle(nexSimFederateResponseHandler
				.getFederationExecutionHandle());
		req.setFederationExecutionModelHandle(federationExecutionModel
				.getFedExecModelUUID());
		req.setTimeToLiveSecs(timeToLiveSecs);

		ReadObjectRequestHandler rh = new ReadObjectRequestHandler();

		// do what the event handler factory does...

		rh.setEvent(req);

		// emulate what the container and Spring will do with IoC...

		rh.setCommonsService(commonsService);

		rh.setRouterService(routerService);

		rh.setObjectService(objectService);

		DataChannelControlBlock dccBlock = new DataChannelControlBlock();

		try {
			dccBlock.setReplyToQueueName(replyToQueueNexsim.getQueueName());
		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		}
		dccBlock.setCorrelationID(req.getEventUUID());
		dccBlock.setEventName(req.getEventName());
		dccBlock.setPayLoadLength(req.serialize().length());

		rh.setDataChannelControlBlock(dccBlock);

		semResponseHandler = new Semaphore(0);

		try {
			rh.handle();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			if (!semResponseHandler.tryAcquire(1, 30, TimeUnit.SECONDS)) {
				fail("Timed out waiting for response from create object request.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

	}

	/**
	 * Tests the update object request handler
	 * 
	 */
	public void testUpdateObjectRequestHandler() {

		Map<String, AccessControl> fieldNameToAccessControlMap =
				new ConcurrentHashMap<String, AccessControl>();

		fieldNameToAccessControlMap.put(
				EventAttributeName.cruiseAltitudeFt.toString(),
				AccessControl.READ_WRITE);
		fieldNameToAccessControlMap.put(EventAttributeName.routePlan.toString(),
				AccessControl.READ_WRITE);

		boolean status = false;

		/**
		 * Create the federation execution ID from the execution handle and model
		 */
		try {
			federationExecutionID =
					commonsService.createFederationExecutionID(
							nexSimFederateResponseHandler.getFederationExecutionHandle(),
							federationExecutionModel.getFedExecModelUUID());
		} catch (MuthurException e1) {
			fail(e1.getLocalizedMessage());
		}

		/**
		 * Create the ownership ID from the federation execution ID and the
		 * registration ID
		 */
		try {
			objectOwnershipID =
					commonsService.createObjectOwnershipID(
							nexSimFederateResponseHandler.getFederateRegistrationHandle(),
							federationExecutionID);
		} catch (MuthurException e1) {
			fail(e1.getLocalizedMessage());
		}

		/**
		 * Update the AC for the set of attributes
		 */

		try {
			status =
					ownershipService.updateAttributeAccessControl(objectOwnershipID,
							flightPlan.getDataObjectUUID(), fieldNameToAccessControlMap);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(status);

		// create update object request

		UpdateObjectRequest req = new UpdateObjectRequest();

		req.setDataObject(flightPlan);

		req.setSourceOfEvent(federateNexSim);
		req.setFederateRegistrationHandle(nexSimFederateResponseHandler
				.getFederateRegistrationHandle());
		req.setFederationExecutionHandle(nexSimFederateResponseHandler
				.getFederationExecutionHandle());
		req.setFederationExecutionModelHandle(federationExecutionModel
				.getFedExecModelUUID());
		req.setTimeToLiveSecs(timeToLiveSecs);

		UpdateObjectRequestHandler rh = new UpdateObjectRequestHandler();

		// do what the event handler factory does...

		rh.setEvent(req);

		// emulate what the container and Spring will do with DI...

		rh.setRegistrationService(registrationService);

		rh.setObjectService(objectService);

		rh.setOwnershipService(ownershipService);

		rh.setModelService(modelService);

		rh.setFederationService(federationService);

		rh.setCommonsService(commonsService);

		rh.setRouterService(routerService);

		DataChannelControlBlock dccBlock = new DataChannelControlBlock();

		try {
			dccBlock.setReplyToQueueName(replyToQueueNexsim.getQueueName());
		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		}
		dccBlock.setCorrelationID(req.getEventUUID());
		dccBlock.setEventName(req.getEventName());
		dccBlock.setPayLoadLength(req.serialize().length());

		rh.setDataChannelControlBlock(dccBlock);

		semResponseHandler = new Semaphore(0);

		try {
			rh.handle();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			if (!semResponseHandler.tryAcquire(1, 30, TimeUnit.SECONDS)) {
				fail("Timed out waiting for response from update object request.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

	}

	/**
	 * Tests the update object attribute access control request handler
	 * 
	 */
	public void testUpdateObjectAttributeControlRequestHandler() {

		Map<String, AccessControl> fieldNameToAccessControlMap =
				new ConcurrentHashMap<String, AccessControl>();

		fieldNameToAccessControlMap.put(
				EventAttributeName.cruiseAltitudeFt.toString(),
				AccessControl.READ_WRITE);
		fieldNameToAccessControlMap.put(EventAttributeName.routePlan.toString(),
				AccessControl.READ_WRITE);

		// create

		UpdateObjectAccessControlRequest req =
				new UpdateObjectAccessControlRequest();

		req.setDataObjectUUID(flightPlan.getDataObjectUUID());
		req.setAttributeNameToAccessControlMap(fieldNameToAccessControlMap);

		req.setSourceOfEvent(federateNexSim);
		req.setFederateRegistrationHandle(nexSimFederateResponseHandler
				.getFederateRegistrationHandle());
		req.setFederationExecutionHandle(nexSimFederateResponseHandler
				.getFederationExecutionHandle());
		req.setFederationExecutionModelHandle(federationExecutionModel
				.getFedExecModelUUID());
		req.setTimeToLiveSecs(timeToLiveSecs);

		UpdateObjectAccessControlRequestHandler rh =
				new UpdateObjectAccessControlRequestHandler();

		// do what the event handler factory does...

		rh.setEvent(req);

		// emulate what the container and Spring will do with DI...

		rh.setRegistrationService(registrationService);

		rh.setObjectService(objectService);

		rh.setOwnershipService(ownershipService);

		rh.setModelService(modelService);

		rh.setFederationService(federationService);

		rh.setCommonsService(commonsService);

		rh.setRouterService(routerService);

		DataChannelControlBlock dccBlock = new DataChannelControlBlock();

		try {
			dccBlock.setReplyToQueueName(replyToQueueNexsim.getQueueName());
		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		}
		dccBlock.setCorrelationID(req.getEventUUID());
		dccBlock.setEventName(req.getEventName());
		dccBlock.setPayLoadLength(req.serialize().length());

		rh.setDataChannelControlBlock(dccBlock);

		semResponseHandler = new Semaphore(0);

		try {
			rh.handle();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			if (!semResponseHandler.tryAcquire(1, 30, TimeUnit.SECONDS)) {
				fail("Timed out waiting for response from update object access control request.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

	}

	/**
	 * Tests attempting to update an transferred flight plan object
	 * 
	 */
	public void testUpdateTransferredFlightPlanObjectRequestHandler() {

		/**
		 * Create a new user with ownership ID from the federation execution ID and
		 * a new registration ID to transfer ownership of an object
		 */
		try {
			transferToObjectOwnershipID =
					commonsService.createObjectOwnershipID(UUID.randomUUID().toString(),
							federationExecutionID);
		} catch (MuthurException e1) {
			fail(e1.getLocalizedMessage());
		}

		// check that the object is owned by the owner that will transfer the
		// ownership
		OwnedObjectList ownedObjectList =
				ownershipService.getOwnerObjects(objectOwnershipID);

		assertNotNull(ownedObjectList);

		assertTrue(ownedObjectList.getOwnedObjects().contains(
				flightPlan.getDataObjectUUID()));

		// transfer the ownership to this new user

		boolean status = false;

		try {
			status =
					ownershipService.transferObjectOwnership(objectOwnershipID,
							transferToObjectOwnershipID, flightPlan.getDataObjectUUID());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(status);

		// set the access to READ_WRITE for several attributes to be updated by
		// other users including the one that just transferred ownership
		//
		Map<String, AccessControl> fieldNameToAccessControlMap =
				new ConcurrentHashMap<String, AccessControl>();

		fieldNameToAccessControlMap.put(
				EventAttributeName.cruiseAltitudeFt.toString(),
				AccessControl.READ_WRITE);
		fieldNameToAccessControlMap.put(EventAttributeName.routePlan.toString(),
				AccessControl.READ_WRITE);

		status = false;

		try {
			status =
					ownershipService.updateAttributeAccessControl(
							transferToObjectOwnershipID, flightPlan.getDataObjectUUID(),
							fieldNameToAccessControlMap);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(status);

		// create update object request for the original user

		UpdateObjectRequest req = new UpdateObjectRequest();

		req.setDataObject(flightPlan);

		req.setSourceOfEvent(federateNexSim);
		req.setFederateRegistrationHandle(nexSimFederateResponseHandler
				.getFederateRegistrationHandle());
		req.setFederationExecutionHandle(nexSimFederateResponseHandler
				.getFederationExecutionHandle());
		req.setFederationExecutionModelHandle(federationExecutionModel
				.getFedExecModelUUID());
		req.setTimeToLiveSecs(timeToLiveSecs);

		UpdateObjectRequestHandler rh = new UpdateObjectRequestHandler();

		// do what the event handler factory does...

		rh.setEvent(req);

		// emulate what the container and Spring will do with DI...

		rh.setRegistrationService(registrationService);

		rh.setObjectService(objectService);

		rh.setOwnershipService(ownershipService);

		rh.setModelService(modelService);

		rh.setFederationService(federationService);

		rh.setCommonsService(commonsService);

		rh.setRouterService(routerService);

		DataChannelControlBlock dccBlock = new DataChannelControlBlock();

		try {
			dccBlock.setReplyToQueueName(replyToQueueNexsim.getQueueName());
		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		}
		dccBlock.setCorrelationID(req.getEventUUID());
		dccBlock.setEventName(req.getEventName());
		dccBlock.setPayLoadLength(req.serialize().length());

		rh.setDataChannelControlBlock(dccBlock);

		semResponseHandler = new Semaphore(0);

		try {
			rh.handle();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			if (!semResponseHandler.tryAcquire(1, 30, TimeUnit.SECONDS)) {
				fail("Timed out waiting for response from update object request.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

	}

	/**
	 * Tests the relinquish object ownership request handler with an aircraft.
	 * 
	 * Note: Relinquishing the ownership at the aircraft level does so for the
	 * entire object graph.
	 * 
	 */
	public void testRelinquishObjectOwnershipRequestHandlerWithAircraft() {

		// create create object request

		RelinquishObjectOwnershipRequest req =
				new RelinquishObjectOwnershipRequest();

		req.setSourceOfEvent(federateNexSim);
		req.setFederateRegistrationHandle(nexSimFederateResponseHandler
				.getFederateRegistrationHandle());
		req.setFederationExecutionHandle(nexSimFederateResponseHandler
				.getFederationExecutionHandle());
		req.setFederationExecutionModelHandle(federationExecutionModel
				.getFedExecModelUUID());
		req.setTimeToLiveSecs(timeToLiveSecs);

		req.setDataObjectUUID(aircraft.getDataObjectUUID());

		RelinquishObjectOwnershipRequestHandler rh =
				new RelinquishObjectOwnershipRequestHandler();

		// do what the event handler factory does...

		rh.setEvent(req);

		// emulate what the container and Spring will do with DI...

		rh.setRegistrationService(registrationService);

		rh.setObjectService(objectService);

		rh.setOwnershipService(ownershipService);

		rh.setModelService(modelService);

		rh.setFederationService(federationService);

		rh.setCommonsService(commonsService);

		rh.setRouterService(routerService);

		DataChannelControlBlock dccBlock = new DataChannelControlBlock();

		try {
			dccBlock.setReplyToQueueName(replyToQueueNexsim.getQueueName());
		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		}
		dccBlock.setCorrelationID(req.getEventUUID());
		dccBlock.setEventName(req.getEventName());
		dccBlock.setPayLoadLength(req.serialize().length());

		rh.setDataChannelControlBlock(dccBlock);

		semResponseHandler = new Semaphore(0);

		try {
			rh.handle();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			if (!semResponseHandler.tryAcquire(1, 60, TimeUnit.SECONDS)) {
				fail("Timed out waiting for response from "
						+ "relinquish aircraft object ownership request.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

	}

	/**
	 * Tests the transfer object ownership request handler. Transfers the object
	 * ownership for the aircraft and the related objects back to the original
	 * owner.
	 * 
	 */
	public void testTransferObjectOwnershipRequestHandlerWithAircraft() {

		// transfer the ownership
		//

		TransferObjectOwnershipRequest req = new TransferObjectOwnershipRequest();

		req.setDataObjectUUID(aircraft.getDataObjectUUID());

		req.setSourceOfEvent(federateNexSim);
		req.setFederateRegistrationHandle(nexSimFederateResponseHandler
				.getFederateRegistrationHandle());
		req.setFederationExecutionHandle(nexSimFederateResponseHandler
				.getFederationExecutionHandle());
		req.setFederationExecutionModelHandle(federationExecutionModel
				.getFedExecModelUUID());
		req.setTimeToLiveSecs(timeToLiveSecs);

		TransferObjectOwnershipRequestHandler rh =
				new TransferObjectOwnershipRequestHandler();

		// do what the event handler factory does...

		rh.setEvent(req);

		// emulate what the container and Spring will do with DI...

		rh.setRegistrationService(registrationService);

		rh.setObjectService(objectService);

		rh.setOwnershipService(ownershipService);

		rh.setModelService(modelService);

		rh.setFederationService(federationService);

		rh.setCommonsService(commonsService);

		rh.setRouterService(routerService);

		DataChannelControlBlock dccBlock = new DataChannelControlBlock();

		try {
			dccBlock.setReplyToQueueName(replyToQueueNexsim.getQueueName());
		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		}
		dccBlock.setCorrelationID(req.getEventUUID());
		dccBlock.setEventName(req.getEventName());
		dccBlock.setPayLoadLength(req.serialize().length());

		rh.setDataChannelControlBlock(dccBlock);

		semResponseHandler = new Semaphore(0);

		try {
			rh.handle();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			if (!semResponseHandler.tryAcquire(1, 30, TimeUnit.SECONDS)) {
				fail("Timed out waiting for response from create object request.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

	}

	/**
	 * A request will be made for ownership of an object that another federate
	 * owns. The request will be queued up and a request to relinquish the object
	 * will be sent to the owning federate. The owning federate will (in this
	 * case) response that the object can be relinquished to the requesting
	 * federate. The response will be handled in
	 * {@link FederateRequestHandler#onMessage(Message)} which will call the
	 * TransferObjectOwnershipResponseHandler.handle() which emulates the owning
	 * federate responding to the ownership transfer request.
	 * 
	 */
	public void
			testTransferObjectOwnershipRequestHandlerTransferFromNexSimToFrascaFTD() {

		// create transfer object ownership request
		//

		TransferObjectOwnershipRequest req = new TransferObjectOwnershipRequest();

		req.setDataObjectUUID(aircraft.getDataObjectUUID());

		req.setSourceOfEvent(federateFrascaFTD);
		req.setFederateRegistrationHandle(frascaFederateResponseHandler
				.getFederateRegistrationHandle());
		req.setFederationExecutionHandle(frascaFederateResponseHandler
				.getFederationExecutionHandle());
		req.setFederationExecutionModelHandle(federationExecutionModel
				.getFedExecModelUUID());
		req.setTimeToLiveSecs(timeToLiveSecs);

		TransferObjectOwnershipRequestHandler rh =
				new TransferObjectOwnershipRequestHandler();

		// do what the event handler factory does...

		rh.setEvent(req);

		// emulate what the container and Spring will do with DI...

		rh.setRegistrationService(registrationService);

		rh.setObjectService(objectService);

		rh.setOwnershipService(ownershipService);

		rh.setModelService(modelService);

		rh.setFederationService(federationService);

		rh.setCommonsService(commonsService);

		rh.setRouterService(routerService);

		DataChannelControlBlock dccBlock = new DataChannelControlBlock();

		try {
			dccBlock.setReplyToQueueName(replyToQueueNexsim.getQueueName());
		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		}
		dccBlock.setCorrelationID(req.getEventUUID());
		dccBlock.setEventName(req.getEventName());
		dccBlock.setPayLoadLength(req.serialize().length());

		rh.setDataChannelControlBlock(dccBlock);

		semFederateRequestHandler = new Semaphore(0);
		semResponseHandler = new Semaphore(0);

		try {
			rh.handle();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		/**
		 * this semaphore is released by the receipt of the
		 * TransferObjectOwnershipRequest in onMessage() in FederateRequestHandler.
		 */
		try {
			if (!semFederateRequestHandler.tryAcquire(30, TimeUnit.SECONDS)) {
				fail("Timed out waiting for response from transfer ownership request.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

		/**
		 * this semaphore is released by the receipt of the
		 * TransferObjectOwnershipResponse in onMessage() in ResponseHandler which
		 * is sent by the TransferObjectOwnershipResponseHandler
		 */
		try {
			if (!semResponseHandler.tryAcquire(30, TimeUnit.SECONDS)) {
				fail("Timed out waiting for receipt of the TransferObjectOwnershipResponse message"
						+ " sent by TransferObjectOwnershipResponseHandler.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * A request will be made for ownership of an object that another federate
	 * owns. The request will be queued up and a request to relinquish the object
	 * will be sent to the owning federate. The owning federate will (in this
	 * case) response that the object can be relinquished to the requesting
	 * federate. The response will be handled in
	 * {@link FederateRequestHandler#onMessage(Message)} which will call the
	 * TransferObjectOwnershipResponseHandler.handle() which emulates the owning
	 * federate responding to the ownership transfer request.
	 * 
	 */
	// @Test
	public void
			testTransferObjectOwnershipRequestHandlerTransferFromFrascaFTDToNexSim() {

		// create transfer object ownership request
		//

		TransferObjectOwnershipRequest req = new TransferObjectOwnershipRequest();

		req.setDataObjectUUID(aircraft.getDataObjectUUID());

		req.setSourceOfEvent(federateNexSim);
		req.setFederateRegistrationHandle(nexSimFederateResponseHandler
				.getFederateRegistrationHandle());
		req.setFederationExecutionHandle(nexSimFederateResponseHandler
				.getFederationExecutionHandle());
		req.setFederationExecutionModelHandle(federationExecutionModel
				.getFedExecModelUUID());
		req.setTimeToLiveSecs(timeToLiveSecs);

		// create the TextMessage and...

		TextMessage txtMessage = null;

		try {

			txtMessage = session.createTextMessage();

			// ...set the body as the serialized event

			txtMessage.setText(req.serialize());

			// Set the reply to field to the reply queue on which the server
			// will
			// respond

			txtMessage.setJMSReplyTo(replyToQueueNexsim);

			// Set a correlation ID from the event

			txtMessage.setJMSCorrelationID(req.getEventUUID());

			txtMessage.setStringProperty(
					MessageDestination.getFederationEventQueuePropName(),
					federationEventQueue.getQueueName());

		} catch (JMSException e) {
			fail(e.getLocalizedMessage());
		}

		TransferObjectOwnershipRequestHandler rh =
				new TransferObjectOwnershipRequestHandler();

		// do what the event handler factory does...

		rh.setEvent(req);

		// emulate what the container and Spring will do with DI...

		rh.setRegistrationService(registrationService);

		rh.setObjectService(objectService);

		rh.setOwnershipService(ownershipService);

		rh.setModelService(modelService);

		rh.setFederationService(federationService);

		rh.setCommonsService(commonsService);

		rh.setRouterService(routerService);

		semFederateRequestHandler = new Semaphore(0);
		semResponseHandler = new Semaphore(0);

		try {
			rh.handle();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		/**
		 * this semaphore is released by the receipt of the
		 * TransferObjectOwnershipRequest in onMessage() in FederateRequestHandler.
		 */
		try {
			if (!semFederateRequestHandler.tryAcquire(30, TimeUnit.SECONDS)) {
				fail("Timed out waiting for response from transfer ownership request.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

		/**
		 * this semaphore is released by the receipt of the
		 * TransferObjectOwnershipResponse in onMessage() in ResponseHandler which
		 * is sent by the TransferObjectOwnershipResponseHandler
		 */
		try {
			if (!semResponseHandler.tryAcquire(30, TimeUnit.SECONDS)) {
				fail("Timed out waiting for receipt of the TransferObjectOwnershipResponse message"
						+ " sent by TransferObjectOwnershipResponseHandler.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * 
	 * Test the {@link DeleteObjectRequestHandler} by deleting the
	 * {@link FlightPlan} object originally created by the NexSim federate. Note
	 * that the ownership of the FlightPlan still remains with the original owner
	 * NexSim despite the associated Aircraft object being transferred to the
	 * FrascaFTD federate.
	 * 
	 */
	public void testDeleteObjectRequestHandlerFlightPlan() {

		// create delete object request

		DeleteObjectRequest req = new DeleteObjectRequest();

		req.setDataObjectUUID(flightPlan.getDataObjectUUID());

		req.setSourceOfEvent(federateNexSim);
		req.setFederateRegistrationHandle(nexSimFederateResponseHandler
				.getFederateRegistrationHandle());
		req.setFederationExecutionHandle(nexSimFederateResponseHandler
				.getFederationExecutionHandle());
		req.setFederationExecutionModelHandle(federationExecutionModel
				.getFedExecModelUUID());
		req.setTimeToLiveSecs(timeToLiveSecs);

		// create the TextMessage and...

		TextMessage txtMessage = null;

		try {

			txtMessage = session.createTextMessage();

			// ...set the body as the serialized event

			txtMessage.setText(req.serialize());

			// Set the reply to field to the reply queue on which the server
			// will
			// respond

			txtMessage.setJMSReplyTo(replyToQueueNexsim);

			// Set a correlation ID from the event

			txtMessage.setJMSCorrelationID(req.getEventUUID());

			txtMessage.setStringProperty(
					MessageDestination.getFederationEventQueuePropName(),
					federationEventQueue.getQueueName());

		} catch (JMSException e) {
			fail(e.getLocalizedMessage());
		}

		DeleteObjectRequestHandler rh = new DeleteObjectRequestHandler();

		// do what the event handler factory does...

		rh.setEvent(req);

		// emulate what the container and Spring will do with DI...

		rh.setRegistrationService(registrationService);

		rh.setObjectService(objectService);

		rh.setOwnershipService(ownershipService);

		rh.setModelService(modelService);

		rh.setFederationService(federationService);

		rh.setCommonsService(commonsService);

		rh.setRouterService(routerService);

		semResponseHandler = new Semaphore(0);

		try {
			rh.handle();
			// fail("Should have failed since it was transferred in earlier test");
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			if (!semResponseHandler.tryAcquire(5, TimeUnit.SECONDS)) {
				fail("Timed out waiting for response from delete flight plan object request.");
				// fail("Should have timed out. Failed in handle() method.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

	}

	/**
	 * Test the {@link DeleteObjectRequestHandler} by deleting the
	 * {@link Aircraft} object originally created by the NexSim federate.
	 * Ownership of the Aircraft was subsequently transferred to the FrascaFTD
	 * federate which allows it to delete the object.
	 * 
	 */
	public void testDeleteObjectRequestHandlerAircraft() {

		// create delete object request

		DeleteObjectRequest req = new DeleteObjectRequest();

		req.setDataObjectUUID(aircraft.getDataObjectUUID());

		req.setSourceOfEvent(federateFrascaFTD);
		req.setFederateRegistrationHandle(frascaFederateResponseHandler
				.getFederateRegistrationHandle());
		req.setFederationExecutionHandle(frascaFederateResponseHandler
				.getFederationExecutionHandle());
		req.setFederationExecutionModelHandle(federationExecutionModel
				.getFedExecModelUUID());
		req.setTimeToLiveSecs(timeToLiveSecs);

		DeleteObjectRequestHandler rh = new DeleteObjectRequestHandler();

		// do what the event handler factory does...

		rh.setEvent(req);

		// emulate what the container and Spring will do with DI...

		rh.setRegistrationService(registrationService);

		rh.setObjectService(objectService);

		rh.setOwnershipService(ownershipService);

		rh.setModelService(modelService);

		rh.setFederationService(federationService);

		rh.setCommonsService(commonsService);

		rh.setRouterService(routerService);

		DataChannelControlBlock dccBlock = new DataChannelControlBlock();

		try {
			dccBlock.setReplyToQueueName(replyToQueueNexsim.getQueueName());
		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		}
		dccBlock.setCorrelationID(req.getEventUUID());
		dccBlock.setEventName(req.getEventName());
		dccBlock.setPayLoadLength(req.serialize().length());

		rh.setDataChannelControlBlock(dccBlock);

		semDataDeletionPublication = new Semaphore(0);
		semResponseHandler = new Semaphore(0);

		try {
			rh.handle();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			if (!semResponseHandler.tryAcquire(1, 15, TimeUnit.SECONDS)) {
				fail("Timed out waiting for response from delete aircraft object request.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

		try {
			// NOTE: this is waiting on 2 releases since we only subscribed to 3
			// of the 4 types of data types that will be returned and only 2 of
			// those
			// 3 types are left

			if (!semDataDeletionPublication.tryAcquire(2, 15, TimeUnit.SECONDS)) {
				fail("Timed out waiting for all data publications from delete object request.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

	}

	/**
	 * Tests the pause federation handler for the federation.
	 * 
	 */
	public void testPauseFederationRequestHandler() {

		// create pause federation request

		PauseFederationRequest req = new PauseFederationRequest();

		req.setLengthOfPauseMSecs(10000); // 10 secs

		req.setSourceOfEvent(federateNexSim);
		req.setFederationExecutionModelHandle(federationExecutionModel
				.getFedExecModelUUID());
		req.setFederationExecutionHandle(nexSimFederateResponseHandler
				.getFederationExecutionHandle());
		req.setTimeToLiveSecs(timeToLiveSecs);

		// create the TextMessage and...

		TextMessage txtMessage = null;

		try {

			txtMessage = session.createTextMessage();

			// ...set the body as the serialized event

			txtMessage.setText(req.serialize());

			// Set the reply to field to the reply queue on which the server
			// will
			// respond

			txtMessage.setJMSReplyTo(replyToQueueNexsim);

			// Set a correlation ID from the event

			txtMessage.setJMSCorrelationID(req.getEventUUID());

			txtMessage.setStringProperty(
					MessageDestination.getFederationEventQueuePropName(),
					federationEventQueue.getQueueName());

		} catch (JMSException e) {
			fail(e.getLocalizedMessage());
		}

		PauseFederationRequestHandler rh = new PauseFederationRequestHandler();

		// do what the event factory does...

		rh.setMessage(txtMessage);

		// do what the event handler factory does...

		rh.setEvent(req);

		// emulate what the container and Spring will do with IoC...

		setServiceReferences(rh);

		semTimeMgmtEventHandler = new Semaphore(0);

		try {
			rh.handle();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			if (!semTimeMgmtEventHandler.tryAcquire(15, TimeUnit.SECONDS)) {
				fail("Timed out waiting for response from ready to run.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

	}

	/**
	 * Tests the resume federation handler for the federation.
	 * 
	 */
	public void testResumeFederationRequestHandler() {

		// create resume federation request

		ResumeFederationRequest req = new ResumeFederationRequest();

		req.setSourceOfEvent(federateNexSim);
		req.setFederationExecutionModelHandle(federationExecutionModel
				.getFedExecModelUUID());
		req.setFederationExecutionHandle(nexSimFederateResponseHandler
				.getFederationExecutionHandle());
		req.setTimeToLiveSecs(timeToLiveSecs);

		// create the TextMessage and...

		TextMessage txtMessage = null;

		try {

			txtMessage = session.createTextMessage();

			// ...set the body as the serialized event

			txtMessage.setText(req.serialize());

			// Set the reply to field to the reply queue on which the server
			// will
			// respond

			txtMessage.setJMSReplyTo(replyToQueueNexsim);

			// Set a correlation ID from the event

			txtMessage.setJMSCorrelationID(req.getEventUUID());

			txtMessage.setStringProperty(
					MessageDestination.getFederationEventQueuePropName(),
					federationEventQueue.getQueueName());

		} catch (JMSException e) {
			fail(e.getLocalizedMessage());
		}

		ResumeFederationRequestHandler rh = new ResumeFederationRequestHandler();

		// do what the event factory does...

		rh.setMessage(txtMessage);

		// do what the event handler factory does...

		rh.setEvent(req);

		// emulate what the container and Spring will do with IoC...

		setServiceReferences(rh);

		semTimeMgmtEventHandler = new Semaphore(0);

		try {
			rh.handle();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			if (!semTimeMgmtEventHandler.tryAcquire(60, TimeUnit.SECONDS)) {
				fail("Timed out waiting for resume response.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

	}

	/**
	 * Tests the termination federation handler for the federation.
	 * 
	 */
	public void testTerminateFederationEventHandler() {

		// create terminate federation request

		FederationTerminationEvent event = null;

		event = new FederationTerminationEvent();

		event.setSourceOfEvent(federateNexSim);
		event.setFederationExecutionHandle(nexSimFederateResponseHandler
				.getFederationExecutionHandle());
		event.setFederationExecutionModelHandle(federationExecutionModel
				.getFedExecModelUUID());

		// create the TextMessage and...

		TextMessage txtMessage = null;

		try {

			txtMessage = session.createTextMessage();

			// ...set the body as the serialized event

			txtMessage.setText(event.serialize());

			// Set the reply to field to the reply queue on which the server
			// will
			// respond

			txtMessage.setJMSReplyTo(replyToQueueNexsim);

			// Set a correlation ID from the event

			txtMessage.setJMSCorrelationID(event.getEventUUID());

			txtMessage.setStringProperty(
					MessageDestination.getDataEventQueuePropName(),
					dataPublicationQueueNexSim.getQueueName());

		} catch (JMSException e) {
			fail(e.getLocalizedMessage());
		}

		TerminateFederationEventHandler rh = new TerminateFederationEventHandler();

		// do what the event factory does...

		rh.setMessage(txtMessage);

		// do what the event handler factory does...

		rh.setEvent(event);

		// emulate what the container and Spring will do with IoC...

		setServiceReferences(rh);

		rh.setTimeService(timeService);

		semTermination = new Semaphore(0);

		try {
			rh.handle();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			if (!semTermination.tryAcquire((terminationWaitMSecs * 2),
					TimeUnit.SECONDS)) {
				fail("Timed out waiting for response from termination.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

	}

	/**
	 * Consumes messages sent by the federation execution controller
	 * 
	 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
	 * @version $Revision$
	 * 
	 */
	static class FedEventConsumer implements MessageListener {

		public void onMessage(Message message) {
			if (message != null) {
				TextMessage tm = (TextMessage) message;
				if (tm != null) {
					try {
						String payLoad = tm.getText();

						IEvent e = EventFactory.getInstance().createEvent(payLoad);

						if (e.isSuccess()) {

							/*
							 * releases the semaphore that each test is using to block until
							 * all responses are received
							 */
							semTermination.release();
						}
					} catch (Exception e) {
						fail(e.getLocalizedMessage());
					}

				}
			}
		}
	}

	/**
	 * 
	 * Receives responses from Muthur to requests from this unit test. As
	 * responses are received the semResponseHandler semaphore is released which
	 * each handler test will block on after calling the handle() method for the
	 * appropriate handler.
	 * 
	 */
	private static class ResponseHandler implements MessageListener {

		private String federateRegistrationHandle;
		private String federationExecutionHandle;

		/**
		 * 
		 */
		public ResponseHandler() {

		}

		/**
		 * 
		 */
		public void onMessage(Message message) {

			if ((message != null) && (message instanceof TextMessage)) {

				TextMessage tm = (TextMessage) message;
				if (tm != null) {
					try {
						String payLoad = tm.getText();

						IEvent e = EventFactory.getInstance().createEvent(payLoad);

						if (e.isSuccess()) {

							if (e instanceof ListFedExecModelsResponse) {

								/*
								 * releases the semaphore that each test is using to block until
								 * all responses are received
								 */
								semResponseHandler.release();

							} else if (e instanceof FederateRegistrationResponse) {

								setFederateRegistrationHandle(((FederateRegistrationResponse) e)
										.getFederateRegistrationHandle());
								/*
								 * releases the semaphore that each test is using to block until
								 * all responses are received
								 */
								semResponseHandler.release();

							} else if (e instanceof JoinFederationResponse) {

								setFederationExecutionHandle(((JoinFederationResponse) e)
										.getFederationExecutionHandle());
								/*
								 * releases the semaphore that each test is using to block until
								 * all responses are received
								 */
								semResponseHandler.release();

							} else if (e instanceof RegisterPublicationResponse) {

								/*
								 * releases the semaphore that each test is using to block until
								 * all responses are received
								 */
								semResponseHandler.release();

							} else if (e instanceof DataSubscriptionResponse) {

								/*
								 * releases the semaphore that each test is using to block until
								 * all responses are received
								 */
								semResponseHandler.release();

							} else if (e instanceof ReadyToRunResponse) {

								/*
								 * releases the semaphore that each test is using to block until
								 * all responses are received
								 */
								semResponseHandler.release();

							} else if (e instanceof PauseFederationResponse) {

								/*
								 * releases the semaphore that each test is using to block until
								 * all responses are received
								 */
								semResponseHandler.release();

							} else if (e instanceof CreateObjectResponse) {

								String objectUUID =
										((CreateObjectResponse) e).getDataObjectUUID();
								assert (objectUUID != null);
								assertTrue(!objectUUID.equalsIgnoreCase(""));

								/*
								 * releases the semaphore that each test is using to block until
								 * all responses are received
								 */
								semResponseHandler.release();

							} else if (e instanceof RelinquishObjectOwnershipResponse) {

								String objectUUID =
										((RelinquishObjectOwnershipResponse) e).getDataObjectUUID();
								assert (objectUUID != null);
								assertTrue(!objectUUID.equalsIgnoreCase(""));

								/*
								 * releases the semaphore that each test is using to block until
								 * all responses are received
								 */
								semResponseHandler.release();

							} else if (e instanceof ObjectOwnershipRelinquishedEvent) {

							} else if (e instanceof TransferObjectOwnershipResponse) {

								TransferObjectOwnershipResponse transferObjectOwnershipResponse =
										(TransferObjectOwnershipResponse) e;
								String objectUUID =
										transferObjectOwnershipResponse.getDataObjectUUID();
								assert (objectUUID != null);
								assertTrue(!objectUUID.equalsIgnoreCase(""));

								/*
								 * releases the semaphore that each test is using to block until
								 * all responses are received
								 */
								semResponseHandler.release();

							} else if (e instanceof ReadObjectResponse) {

								IBaseDataObject bdo = ((ReadObjectResponse) e).getDataObject();
								if (bdo instanceof Aircraft) {
									Aircraft readSacd = (Aircraft) bdo;
									assertTrue(readSacd.getDataObjectUUID().equals(
											aircraft.getDataObjectUUID()));

									/*
									 * releases the semaphore that each test is using to block
									 * until all responses are received
									 */
									semResponseHandler.release();
								}

							} else if (e instanceof UpdateObjectResponse) {

								IBaseDataObject bdo =
										((UpdateObjectResponse) e).getDataObject();

								if (bdo instanceof FlightPlan) {

									assertTrue(bdo.getDataObjectUUID().equals(
											flightPlan.getDataObjectUUID()));

									/*
									 * releases the semaphore that each test is using to block
									 * until all responses are received
									 */
									semResponseHandler.release();

								} else if (bdo instanceof Aircraft) {

									assertTrue(bdo.getDataObjectUUID().equals(
											aircraftForCreateOrUpdate.getDataObjectUUID()));

									/*
									 * releases the semaphore that each test is using to block
									 * until all responses are received
									 */
									semResponseHandler.release();
								}

							} else if (e instanceof UpdateObjectAccessControlResponse) {

								/*
								 * releases the semaphore that each test is using to block until
								 * all responses are received
								 */
								semResponseHandler.release();

							} else if (e instanceof DeleteObjectResponse) {

								String deletedDataObjectUUID =
										((DeleteObjectResponse) e).getDataObjectUUID();

								assert (deletedDataObjectUUID != null);
								assertTrue(!deletedDataObjectUUID.equalsIgnoreCase(""));
								assert (deletedDataObjectUUID.equalsIgnoreCase(aircraft
										.getDataObjectUUID()));

								/*
								 * releases the semaphore that each test is using to block until
								 * all responses are received
								 */
								semResponseHandler.release();

							} else if (e instanceof UpdateFederationExecutionTimeResponse) {

								/*
								 * releases the semaphore that each test is using to block until
								 * all responses are received
								 */
								semResponseHandler.release();

							}

						}

					} catch (Exception e) {
						fail(e.getLocalizedMessage());
					}

				}
			}
		}

		public String getFederateRegistrationHandle() {
			return federateRegistrationHandle;
		}

		public void
				setFederateRegistrationHandle(String federateRegistrationHandle) {
			this.federateRegistrationHandle = federateRegistrationHandle;
		}

		public String getFederationExecutionHandle() {
			return federationExecutionHandle;
		}

		public void setFederationExecutionHandle(String federationExecutionHandle) {
			this.federationExecutionHandle = federationExecutionHandle;
		}
	}

	/**
	 * 
	 * Receives requests from other federates to transfer ownership or control of
	 * an object to the requesting federate.
	 * 
	 */
	private static class FederateRequestHandler implements MessageListener {

		private String name;
		private boolean acceptTransferOwnershipRequests = true;
		private ResponseHandler responseHandler;

		public FederateRequestHandler(final String name,
				final ResponseHandler responseHandler) {
			this.name = name;
			this.responseHandler = responseHandler;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
		 */
		public void onMessage(Message message) {

			if ((message != null) && (message instanceof TextMessage)) {

				TextMessage tm = (TextMessage) message;

				if (tm != null) {

					try {

						String payLoad = tm.getText();

						IEvent e = EventFactory.getInstance().createEvent(payLoad);

						if (e.isSuccess()) {

							if (e instanceof TransferObjectOwnershipRequest) {

								TransferObjectOwnershipRequest transferObjectOwnershipRequest =
										(TransferObjectOwnershipRequest) e;

								TransferObjectOwnershipResponse response =
										new TransferObjectOwnershipResponse();

								if (acceptTransferOwnershipRequests) {
									response
											.setTransferOwnershipResponse(TransferOwnershipResponse.GRANTED);
								} else {
									response
											.setTransferOwnershipResponse(TransferOwnershipResponse.DENIED);
								}

								try {
									response.initialization(transferObjectOwnershipRequest
											.serialize());
								} catch (MuthurException ex) {
									fail(ex.getLocalizedMessage());
								}

								response.setSourceOfEvent(name);
								response.setFederateRegistrationHandle(responseHandler
										.getFederateRegistrationHandle());

								// create the TextMessage and...

								TextMessage txtMessage = null;

								try {

									txtMessage = session.createTextMessage();

									// ...set the body as the serialized event

									txtMessage.setText(response.serialize());

									// Set the reply to field to the reply queue
									// on which the
									// server will respond

									txtMessage.setJMSReplyTo(replyToQueueNexsim);

									// Set a correlation ID from the event

									txtMessage.setJMSCorrelationID(response.getEventUUID());

									txtMessage.setStringProperty(
											MessageDestination.getFederationEventQueuePropName(),
											federationEventQueue.getQueueName());

								} catch (JMSException ex) {
									fail(ex.getLocalizedMessage());
								}

								TransferObjectOwnershipResponseHandler rh =
										new TransferObjectOwnershipResponseHandler();

								// do what the event handler factory does...

								rh.setEvent(response);

								// emulate what the container and Spring will do
								// with DI...

								rh.setRegistrationService(registrationService);

								rh.setObjectService(objectService);

								rh.setOwnershipService(ownershipService);

								rh.setModelService(modelService);

								rh.setFederationService(federationService);

								rh.setCommonsService(commonsService);

								rh.setRouterService(routerService);

								try {
									rh.handle();

									semFederateRequestHandler.release();

								} catch (Exception ex) {
									fail(ex.getLocalizedMessage());
								}
							}
						}

					} catch (Exception e) {
						fail(e.getLocalizedMessage());
					}

				}
			}
		}

		/**
		 * @return the name
		 */
		@SuppressWarnings("unused")
		public Object getName() {
			return name;
		}

		/**
		 * @return the acceptTransferOwnershipRequests
		 */
		@SuppressWarnings("unused")
		public boolean isAcceptTransferOwnershipRequests() {
			return acceptTransferOwnershipRequests;
		}

		/**
		 * @param acceptTransferOwnershipRequests
		 *          the acceptTransferOwnershipRequests to set
		 */
		public void setAcceptTransferOwnershipRequests(
				boolean acceptTransferOwnershipRequests) {
			this.acceptTransferOwnershipRequests = acceptTransferOwnershipRequests;
		}
	}

	/**
	 * 
	 * Consumes heartbeat events
	 * 
	 */
	@SuppressWarnings("unused")
	static private class HeartbeatHandler implements MessageListener {

		public void onMessage(Message message) {

			// Received message on heartbeat queue

		}
	}

	/**
	 * Consumes federation time management events and performs the same function
	 * as the ResponseHandler method. On receipt of a message it releases the
	 * semaphore which the unit test is block on. In the case of the pause, there
	 * will be several messages received in order to release the number of permits
	 * requested from the unit test.
	 * 
	 */
	private static class FederationTimeManagementEventHandler implements
			MessageListener {

		private long currentTimeInterval = 0L;
		private long previousTimeInterval = 0L;

		private int runningResponsesReceived = 0;
		private final int maxNumOfRunningResponses = 5;

		private static int pauseResponsesReceived;
		private static final int maxNumOfPausedResponses = 5;

		public void onMessage(Message message) {

			if ((message != null) && (message instanceof TextMessage)) {

				try {

					TextMessage tm = (TextMessage) message;

					if (tm != null) {

						String payLoad = tm.getText();

						IEvent e = EventFactory.getInstance().createEvent(payLoad);

						if (e.isSuccess()) {

							if (e instanceof TimeManagementRequest) {

								TimeManagementRequest tmr = (TimeManagementRequest) e;
								if (tmr.getFederationExecutionDirective() == FederationExecutionDirective.START) {
									assertTrue(tmr.getTimeIntervalMSecs() == startingFederationTime);
								}

								currentTimeInterval = tmr.getTimeIntervalMSecs();

								// if the federation is paused then the previous
								// and current
								// time should be the same
								switch (tmr.getFederationExecutionState()) {

								case PAUSED:

									pauseResponsesReceived++;

									if ((pauseResponsesReceived > 1)
											&& (pauseResponsesReceived <= maxNumOfPausedResponses)) {
										if (previousTimeInterval != currentTimeInterval) {
											System.err.println("Previous and current should be "
													+ "the same since time is paused.");
										}
									}
									if (pauseResponsesReceived >= maxNumOfPausedResponses) {
										semTimeMgmtEventHandler.release();
									}

									break;

								case RUNNING:

									runningResponsesReceived++;

									if ((runningResponsesReceived > 1)
											&& (runningResponsesReceived <= maxNumOfRunningResponses)) {

										if (previousTimeInterval == currentTimeInterval) {
											System.err.println("Previous and current should NOT be "
													+ "the same with the time moving forward.");
										}
									}

									if (runningResponsesReceived >= maxNumOfRunningResponses) {
										semTimeMgmtEventHandler.release();
									}

									break;
								case AWAITING_START_DIRECTIVE:
								case UNDEFINED:
									break;
								}

								// save the previous time
								//
								previousTimeInterval = tmr.getTimeIntervalMSecs();
							}

						}

					}
				} catch (Exception e) {
					fail(e.getLocalizedMessage());
				}
			}

		}
	}

	/**
	 * 
	 * 
	 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
	 * @version $Revision$
	 * 
	 */
	private static class DataPublicationHandler implements MessageListener {

		public void onMessage(Message message) {

			if ((message != null) && (message instanceof TextMessage)) {
				TextMessage txtMsg = (TextMessage) message;

				try {

					String dataPublicationPayload = txtMsg.getText();

					IEvent event =
							EventFactory.getInstance().createEvent(dataPublicationPayload);

					if (event != null) {

						if (event instanceof DataPublicationEvent) {

							DataPublicationEvent dp = (DataPublicationEvent) event;

							IBaseDataObject bdo = dp.getDataObject();

							assertNotNull(bdo);

							switch (dp.getDataAction()) {
							case Delete:
								semDataDeletionPublication.release();
								break;
							case Add:
								semDataPublication.release();
								break;
							case Relinquished:
							case Transferred:
							case Update:
							default:
								break;
							}

						}
					} else {
						fail("Null event returned by EventFactory.");
					}

				} catch (Exception e) {
					fail("Error handling system event [" + e.getLocalizedMessage() + "]");
				}
			}
		}
	}

	/**
	 * 
	 * Creates and adds the licenses for the federate to use during registration
	 */
	static private void addLicenses() {
		/*
		 * Create the Organization
		 */

		Organization org = atCloudModelService.createOrganization();

		/*
		 * Add the Organization
		 */
		em.getTransaction().begin();

		persistenceService.add(org);

		em.getTransaction().commit();

		/*
		 * Create licenseOne
		 */

		licenseOne = atCloudModelService.createLicense();

		/*
		 * Add the License
		 */
		em.getTransaction().begin();

		persistenceService.add(licenseOne);

		em.getTransaction().commit();

		/*
		 * Add licenseOne to the organization
		 */

		em.getTransaction().begin();

		persistenceService.assignLicenseToOrganization(licenseOne.getLicenseID(),
				org.getOrgID());

		em.getTransaction().commit();

		/*
		 * Create licenseTwo
		 */

		licenseTwo = atCloudModelService.createLicense();

		/*
		 * Add the License
		 */
		em.getTransaction().begin();

		persistenceService.add(licenseTwo);

		em.getTransaction().commit();

		/*
		 * Add licenseOne to the organization
		 */

		em.getTransaction().begin();

		persistenceService.assignLicenseToOrganization(licenseTwo.getLicenseID(),
				org.getOrgID());

		em.getTransaction().commit();

		/*
		 * Get the licenses for the organization
		 */

		List<License> licenses = persistenceService.getLicenses(org);

		assertNotNull(licenses);

		assertTrue(licenses.size() == 2);

		Iterator<License> licenseIter = licenses.iterator();

		assertNotNull(licenseIter);

		/*
		 * This should contain the licenses that we added to the organization
		 */
		while (licenseIter.hasNext()) {
			License nextLicense = licenseIter.next();
			assertNotNull(nextLicense);
			assertTrue(nextLicense.equals(licenseOne)
					|| nextLicense.equals(licenseTwo));
		}

		em.getTransaction().begin();
		try {
			licenseService.activateLicense(licenseOne.getLicenseID());
			licenseService.activateLicense(licenseTwo.getLicenseID());
		} catch (ATCloudException e) {
			fail(e.getLocalizedMessage());
		}
		em.getTransaction().commit();

	}

	/**
	 * Add a group and FEM
	 */
	static private void addGroupAndFEM() {

		/*
		 * Add a group and FEM and add the FEM to the group
		 */

		FEM fem = atCloudModelService.createFEM("MTSU Focus Lab - Spring 2014");

		try {

			/*
			 * Add the FEM
			 */
			em.getTransaction().begin();

			persistenceService.add(fem);

			em.getTransaction().commit();

			/*
			 * Add the group
			 */
			em.getTransaction().begin();

			newGroup =
					userService
							.addGroup("MTSU FOCUS", "FOCUS lab at MTSU", fem.getName());

			em.getTransaction().commit();

			/*
			 * Add the FEM to the Group
			 */

			em.getTransaction().begin();

			persistenceService.addFEMToGroup(fem.getName(), newGroup.getName());

			em.getTransaction().commit();

		} catch (ATCloudException e) {
			fail(e.getLocalizedMessage());
		}
	}
}
