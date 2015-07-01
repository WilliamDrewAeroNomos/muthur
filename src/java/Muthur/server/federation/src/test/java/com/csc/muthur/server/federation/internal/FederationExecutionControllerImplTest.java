/**
 * 
 */
package com.csc.muthur.server.federation.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.UUID;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.csc.muthur.server.commons.CommonsService;
import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.commons.MessageDestination;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.commons.internal.CommonsServiceImpl;
import com.csc.muthur.server.configuration.ConfigurationService;
import com.csc.muthur.server.federation.FederationDataChannelServer;
import com.csc.muthur.server.federation.FederationExecutionController;
import com.csc.muthur.server.federation.FederationService;
import com.csc.muthur.server.federation.internal.datachannel.DataEventFactoryImpl;
import com.csc.muthur.server.federation.internal.datachannel.DataEventHandlerFactoryImpl;
import com.csc.muthur.server.federation.internal.datachannel.FederationDataChannelListener;
import com.csc.muthur.server.federation.internal.datachannel.FederationDataChannelListenerFactory;
import com.csc.muthur.server.federation.internal.datachannel.FederationDataChannelServerImpl;
import com.csc.muthur.server.federation.internal.execution.FederationExecutionControllerImpl;
import com.csc.muthur.server.federation.internal.execution.FederationServiceImpl;
import com.csc.muthur.server.mocks.MockConfigurationServerImpl;
import com.csc.muthur.server.model.FederationExecutionEntry;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.JoinFederationExecutionEntry;
import com.csc.muthur.server.model.ModelService;
import com.csc.muthur.server.model.ReadyToRunFederationExecutionEntry;
import com.csc.muthur.server.model.SubscriptionRegistrationFederationExecutionEntry;
import com.csc.muthur.server.model.event.EventFactory;
import com.csc.muthur.server.model.event.ObjectOwnershipRelinquishedEvent;
import com.csc.muthur.server.model.event.request.DataSubscriptionRequest;
import com.csc.muthur.server.model.event.request.JoinFederationRequest;
import com.csc.muthur.server.model.event.request.ReadyToRunRequest;
import com.csc.muthur.server.model.internal.ModelServiceImpl;
import com.csc.muthur.server.object.ObjectService;
import com.csc.muthur.server.object.internal.ObjectServiceImpl;
import com.csc.muthur.server.router.RouterService;
import com.csc.muthur.server.router.internal.RouterServiceImpl;
import com.csc.muthur.server.time.TimeService;
import com.csc.muthur.server.time.internal.TimeServiceImpl;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public class FederationExecutionControllerImplTest implements ExceptionListener {

	private final static String nexSimFederateName = "NexSim";
	private final static String frascaFederateName = "Frasca";

	private static BrokerService broker = null;
	private static Connection connection;
	private static Session session;

	private static ActiveMQConnectionFactory connectionFactory;
	private static TemporaryQueue federationEventQueue;
	private static TemporaryQueue dataPublicationQueue;
	private static String fedExecHandle = UUID.randomUUID().toString();

	private static MessageConsumer fedEventConsumer;
	private static MessageConsumer dataPublicationConsumer;

	private static TemporaryQueue timeMgmtEventQueue;
	private static MessageConsumer timeMgmtEventConsumer;

	private static TemporaryQueue ownershipEventQueue;
	private static TemporaryQueue federateRequestEventQueue;

	private static Semaphore sem;
	private static TemporaryQueue replyToQueue;

	private static ConfigurationService configurationService;
	private static RouterService routerService;
	private static TimeService timeService;
	private static ModelService modelService;
	private static ObjectService objectService;
	private static CommonsService commonsService;
	private static FileSystemXmlApplicationContext applicationContext;

	/**
	 * 
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		applicationContext =
				new FileSystemXmlApplicationContext(
						"src/main/resources/META-INF/spring/federation-data-event-context.xml");

		CommonsService cs = new CommonsServiceImpl();

		configurationService = new MockConfigurationServerImpl();

		int availablePort = cs.findAvailablePort(6000, 7000);

		assert (availablePort != -1);

		configurationService.setMessagingPort(availablePort);

		// This message broker is embedded for testing purposes. It emulates
		// having the ActiveMQ bundle running in the OSGi container
		//
		broker = new BrokerService();
		broker.setPersistent(false);
		broker.setUseJmx(false);

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

		federationEventQueue = session.createTemporaryQueue();

		fedEventConsumer = session.createConsumer(federationEventQueue);

		fedEventConsumer.setMessageListener(new FedEventConsumer());

		dataPublicationQueue = session.createTemporaryQueue();

		dataPublicationConsumer = session.createConsumer(dataPublicationQueue);

		dataPublicationConsumer
				.setMessageListener(new DataPublicationQueueListener());

		replyToQueue = session.createTemporaryQueue();

		MessageConsumer replyQueueConsumer = session.createConsumer(replyToQueue);
		replyQueueConsumer.setMessageListener(new ResponseHandler());

		// federate request queue

		federateRequestEventQueue = session.createTemporaryQueue();

		// ***********

		timeMgmtEventQueue = session.createTemporaryQueue();

		timeMgmtEventConsumer = session.createConsumer(timeMgmtEventQueue);

		timeMgmtEventConsumer.setMessageListener(new TimeManagementEventConsumer());

		// ***********

		// create a temporary queue and attach a consumer for receiving ownership
		// events

		ownershipEventQueue = session.createTemporaryQueue();

		session.createConsumer(ownershipEventQueue).setMessageListener(
				new OwnershipEventHandler());

		// ***********

		routerService = new RouterServiceImpl();

		routerService.setConfigurationServer(configurationService);

		routerService.start();

		// ******************************************

		modelService = new ModelServiceImpl();

		modelService.start();

		// ******************************************

		objectService = new ObjectServiceImpl();

		// set the services into the object service impl

		objectService.setConfigurationService(configurationService);
		objectService.setModelService(modelService);

		// ******************************************

		timeService = new TimeServiceImpl();

		timeService.setConfigurationService(configurationService);

		timeService.start();

		// ******************************************

		commonsService = new CommonsServiceImpl();

	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if (session != null) {
			session.close();
		}
		if (connection != null) {
			connection.close();
		}
		if (broker != null) {
			broker.stop();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void setUp() throws Exception {

		try {
			connection.setExceptionListener(this);
		} catch (JMSException e2) {
			// nothing to do here
		}
	}

	// @Test
	public void runAllTests() {
		testCreatingJoinFederationExecutionEntry();
		testJoinFederationExecution();
		testNonRequiredFederateJoinFederationExecution();
		testBasicProtocolFromStartToFinish();

	}

	/**
	 * Tests the creation of a {@link FederationExecutionEntry} object
	 * 
	 */
	@Test
	public void testCreatingJoinFederationExecutionEntry() {

		TextMessage txtMessage = null;

		try {
			txtMessage = session.createTextMessage();
		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		}

		assert (txtMessage != null);

		try {
			txtMessage.setJMSReplyTo(replyToQueue);
			txtMessage.setStringProperty(
					MessageDestination.getFederationEventQueuePropName(),
					replyToQueue.getQueueName());
		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		}

		FederationExecutionModel fem = null;
		try {
			fem = new FederationExecutionModel("Federation Exec Controller Test");
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}
		fem.addRequiredFededrate(nexSimFederateName);
		fem.addRequiredFededrate(frascaFederateName);
		fem.setDefaultDurationWithinStartupProtocolMSecs(30000);
		fem.setDurationFederationExecutionMSecs(10000);

		JoinFederationExecutionEntry fep = null;

		try {

			fep =
					new JoinFederationExecutionEntry(nexSimFederateName, fem, txtMessage,
							new JoinFederationRequest());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assert (fep != null);
		assert (!"".equalsIgnoreCase(fep.getFederateName()));
	}

	/**
	 * 
	 */
	@Test
	public void testJoinFederationExecution() {

		sem = new Semaphore(0);

		FederationService fs = new FederationServiceImpl();
		assertNotNull(fs);

		fs.setConfigurationService(configurationService);
		fs.setRouterService(routerService);
		fs.setTimeService(timeService);
		fs.setObjectService(objectService);
		fs.setCommonsService(commonsService);

		/*
		 * Create event factory
		 */
		DataEventFactoryImpl dataEventFactory = new DataEventFactoryImpl();
		dataEventFactory.setApplicationContext(applicationContext);

		/*
		 * Create event handler factory
		 */
		DataEventHandlerFactoryImpl dataEventHandlerFactory =
				new DataEventHandlerFactoryImpl();
		dataEventHandlerFactory.setApplicationContext(applicationContext);

		/*
		 * Create the data channel listener and set the data event and data event
		 * handler factory
		 */
		FederationDataChannelListener dataChannelListener =
				new FederationDataChannelListener();
		dataChannelListener.setDataEventFactory(dataEventFactory);
		dataChannelListener.setDataEventHandlerFactory(dataEventHandlerFactory);
		dataChannelListener.setFederationService(fs);

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
		fs.setFederationDataChannelServer(federationDataChannelServer);

		try {
			fs.start();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		FederationExecutionModel fem = null;
		try {
			fem = new FederationExecutionModel("Federation Exec Controller Test");
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}
		fem.addRequiredFededrate(nexSimFederateName);
		fem.addRequiredFededrate(frascaFederateName);
		fem.setDefaultDurationWithinStartupProtocolMSecs(300000);
		fem.setDurationFederationExecutionMSecs(60000);

		TextMessage txtMessage = null;
		try {
			txtMessage = session.createTextMessage();
		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		}

		assert (txtMessage != null);

		try {
			txtMessage.setJMSReplyTo(replyToQueue);
			txtMessage.setJMSCorrelationID(UUID.randomUUID().toString());
			txtMessage.setStringProperty(
					MessageDestination.getFederationEventQueuePropName(),
					replyToQueue.getQueueName());
		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		}

		// first NexSim joins the federation
		//
		JoinFederationExecutionEntry jfee = null;

		try {
			jfee =
					new JoinFederationExecutionEntry(nexSimFederateName, fem, txtMessage,
							new JoinFederationRequest());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			fs.joinFederationExecution(fem, jfee);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// then Frasca joins the federation

		try {
			txtMessage.setJMSReplyTo(replyToQueue);
			txtMessage.setJMSCorrelationID(UUID.randomUUID().toString());
			txtMessage.setStringProperty(
					MessageDestination.getFederationEventQueuePropName(),
					replyToQueue.getQueueName());
		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		}

		//
		try {
			jfee =
					new JoinFederationExecutionEntry(frascaFederateName, fem, txtMessage,
							new JoinFederationRequest());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			fs.joinFederationExecution(fem, jfee);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// block on semaphore until available or timeout
		// releases are in the listener

		try {
			if (!sem.tryAcquire(2, 300, TimeUnit.SECONDS)) {
				fail("Timed out before all registration notifications received");
			}
		} catch (InterruptedException e2) {
			fail(e2.getLocalizedMessage());
		}
	}

	/**
	 * 
	 */
	@Test
	public void testNonRequiredFederateJoinFederationExecution() {

		sem = new Semaphore(0);

		FederationService fs = new FederationServiceImpl();
		assertNotNull(fs);

		fs.setConfigurationService(configurationService);
		fs.setRouterService(routerService);
		fs.setTimeService(timeService);
		fs.setObjectService(objectService);
		fs.setCommonsService(commonsService);

		/*
		 * Create event factory
		 */
		DataEventFactoryImpl dataEventFactory = new DataEventFactoryImpl();
		dataEventFactory.setApplicationContext(applicationContext);

		/*
		 * Create event handler factory
		 */
		DataEventHandlerFactoryImpl dataEventHandlerFactory =
				new DataEventHandlerFactoryImpl();
		dataEventHandlerFactory.setApplicationContext(applicationContext);

		/*
		 * Create the data channel listener and set the data event and data event
		 * handler factory
		 */
		FederationDataChannelListener dataChannelListener =
				new FederationDataChannelListener();
		dataChannelListener.setDataEventFactory(dataEventFactory);
		dataChannelListener.setDataEventHandlerFactory(dataEventHandlerFactory);
		dataChannelListener.setFederationService(fs);

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
		fs.setFederationDataChannelServer(federationDataChannelServer);

		try {
			fs.start();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		FederationExecutionModel fem = null;
		try {
			fem = new FederationExecutionModel("Federation Exec Controller Test");
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());

		}
		fem.addRequiredFededrate(nexSimFederateName);
		fem.addRequiredFededrate(frascaFederateName);
		fem.setDefaultDurationWithinStartupProtocolMSecs(30000);
		fem.setDurationFederationExecutionMSecs(10000);

		TextMessage txtMessage = null;
		try {
			txtMessage = session.createTextMessage();
		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		}

		assert (txtMessage != null);

		try {
			txtMessage.setJMSReplyTo(replyToQueue);
			txtMessage.setJMSCorrelationID(UUID.randomUUID().toString());
			txtMessage.setStringProperty(
					MessageDestination.getFederationEventQueuePropName(),
					replyToQueue.getQueueName());
		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		}

		// first joins
		//
		JoinFederationExecutionEntry fep = null;

		try {
			fep =
					new JoinFederationExecutionEntry(nexSimFederateName, fem, txtMessage,
							new JoinFederationRequest());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			fs.joinFederationExecution(fem, fep);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// the second one joins
		//
		try {
			fep =
					new JoinFederationExecutionEntry(frascaFederateName, fem, txtMessage,
							new JoinFederationRequest());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			fs.joinFederationExecution(fem, fep);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// block on semaphore until available or timeout
		// releases are in the listener

		try {
			if (!sem.tryAcquire(2, 10, TimeUnit.SECONDS)) {
				fail("Timed out before all " + "registration notifications received");
			}
		} catch (InterruptedException e2) {
			fail(e2.getLocalizedMessage());
		}

	}

	/**
	 * 
	 */
	@Test
	public void testBasicProtocolFromStartToFinish() {

		FederationService fs = new FederationServiceImpl();
		assertNotNull(fs);

		fs.setConfigurationService(configurationService);
		fs.setRouterService(routerService);
		fs.setTimeService(timeService);
		fs.setObjectService(objectService);
		fs.setCommonsService(commonsService);

		/*
		 * Create event factory
		 */
		DataEventFactoryImpl dataEventFactory = new DataEventFactoryImpl();
		dataEventFactory.setApplicationContext(applicationContext);

		/*
		 * Create event handler factory
		 */
		DataEventHandlerFactoryImpl dataEventHandlerFactory =
				new DataEventHandlerFactoryImpl();
		dataEventHandlerFactory.setApplicationContext(applicationContext);

		/*
		 * Create the data channel listener and set the data event and data event
		 * handler factory
		 */
		FederationDataChannelListener dataChannelListener =
				new FederationDataChannelListener();
		dataChannelListener.setDataEventFactory(dataEventFactory);
		dataChannelListener.setDataEventHandlerFactory(dataEventHandlerFactory);
		dataChannelListener.setFederationService(fs);

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
		fs.setFederationDataChannelServer(federationDataChannelServer);

		try {
			fs.start();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		FederationExecutionModel fem = null;
		try {
			fem = new FederationExecutionModel("Federation Exec Controller Test");
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		fem.setDefaultDurationWithinStartupProtocolMSecs(30000);
		fem.addRequiredFededrate(nexSimFederateName);
		fem.addRequiredFededrate(frascaFederateName);

		fem.setDurationFederationExecutionMSecs(60000);

		FederationExecutionController fec = null;

		try {
			fec = new FederationExecutionControllerImpl(fs, fem);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}
		assert (fec != null);

		// #########################################
		//
		// 1) Join the federates to the federation
		//
		// #########################################

		TextMessage txtMessage = null;
		try {
			txtMessage = session.createTextMessage();
		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		}

		assert (txtMessage != null);

		try {
			txtMessage.setJMSReplyTo(replyToQueue);
			txtMessage.setJMSCorrelationID(UUID.randomUUID().toString());
			txtMessage.setStringProperty(
					MessageDestination.getFederationEventQueuePropName(),
					federationEventQueue.getQueueName());
		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		}

		// re-initialize the semaphore that will be released when the join responses
		// from
		// Muthur are returned in the ResponseHandler

		sem = new Semaphore(0);

		// the first one joins

		JoinFederationExecutionEntry fep = null;

		try {
			fep =
					new JoinFederationExecutionEntry(nexSimFederateName, fem, txtMessage,
							new JoinFederationRequest());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			fec.joinFederation(fep);
		} catch (MuthurException e3) {
			fail(e3.getLocalizedMessage());
		}

		// the second one joins

		try {
			fep =
					new JoinFederationExecutionEntry(frascaFederateName, fem, txtMessage,
							new JoinFederationRequest());
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			fec.joinFederation(fep);
		} catch (MuthurException e3) {
			fail(e3.getLocalizedMessage());
		}

		// block on semaphore until available or timeout
		// releases are in the listener

		try {
			if (!sem.tryAcquire(2, 360, TimeUnit.SECONDS)) {
				fail("Timed out before all " + "join notifications received");
			}
		} catch (InterruptedException e2) {
			fail(e2.getLocalizedMessage());
		}

		// #########################################
		//
		// 2) Register subscriptions
		//
		// #########################################

		// register subscriptions for NexSim

		// re-initialize the semaphore that will be released when the subscription
		// responses from Muthur are returned in the ResponseHandler

		sem = new Semaphore(0);

		DataSubscriptionRequest request = new DataSubscriptionRequest();
		request.setSourceOfEvent(nexSimFederateName);
		request.setFederationExecutionModelHandle(fem.getFedExecModelUUID());

		try {
			request.setSubscriptionQueueName(dataPublicationQueue.getQueueName());
		} catch (JMSException e) {
			fail(e.getLocalizedMessage());
		}

		// add the ownership event queue name
		try {
			request.setOwnershipEventQueueName(ownershipEventQueue.getQueueName());
		} catch (JMSException e) {
			fail(e.getLocalizedMessage());
		}

		txtMessage = null;

		try {
			txtMessage = session.createTextMessage();
		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		}

		assert (txtMessage != null);

		try {
			txtMessage.setJMSReplyTo(replyToQueue);
			txtMessage.setJMSCorrelationID(UUID.randomUUID().toString());
			txtMessage.setStringProperty(
					MessageDestination.getDataEventQueuePropName(),
					dataPublicationQueue.getQueueName());
		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		}

		SubscriptionRegistrationFederationExecutionEntry fedExecSub = null;

		try {
			fedExecSub =
					new SubscriptionRegistrationFederationExecutionEntry(fem, request,
							txtMessage);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			fec.registerFederateSubscriptions(fedExecSub);
		} catch (MuthurException e3) {
			fail(e3.getLocalizedMessage());
		}

		// register subscriptions for Frasca

		request = new DataSubscriptionRequest();
		request.setSourceOfEvent(frascaFederateName);

		try {
			request.setSubscriptionQueueName(dataPublicationQueue.getQueueName());
		} catch (JMSException e) {
			fail(e.getLocalizedMessage());
		}

		// add the ownership event queue name
		try {
			request.setOwnershipEventQueueName(ownershipEventQueue.getQueueName());
		} catch (JMSException e) {
			fail(e.getLocalizedMessage());
		}

		txtMessage = null;

		try {
			txtMessage = session.createTextMessage();
		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		}

		assert (txtMessage != null);

		try {
			txtMessage.setJMSReplyTo(replyToQueue);
			txtMessage.setJMSCorrelationID(UUID.randomUUID().toString());
			txtMessage.setStringProperty(
					MessageDestination.getDataEventQueuePropName(),
					dataPublicationQueue.getQueueName());
		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		}

		try {
			fedExecSub =
					new SubscriptionRegistrationFederationExecutionEntry(fem, request,
							txtMessage);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			fec.registerFederateSubscriptions(fedExecSub);
		} catch (MuthurException e3) {
			fail(e3.getLocalizedMessage());
		}

		// block on semaphore until all responses are received back in the
		// ResponseHandler

		try {
			if (!sem.tryAcquire(2, 360, TimeUnit.SECONDS)) {
				fail("Timed out before all " + "subscription responses received");
			}
		} catch (InterruptedException e2) {
			fail(e2.getLocalizedMessage());
		}

		// #########################################
		//
		// 3) Indicate ready to run
		//
		// #########################################

		// register the federates as ready to run

		// re-initialize the semaphore that will be released when the responses from
		// Muthur are returned in the ResponseHandler
		sem = new Semaphore(0);

		txtMessage = null;

		try {
			txtMessage = session.createTextMessage();
		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		}

		assert (txtMessage != null);

		try {
			txtMessage.setJMSReplyTo(replyToQueue);
			txtMessage.setJMSCorrelationID(UUID.randomUUID().toString());
		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		}

		ReadyToRunRequest rrReq = new ReadyToRunRequest();
		rrReq.setSourceOfEvent(nexSimFederateName);
		rrReq.setFederationExecutionHandle(fedExecHandle);
		try {
			rrReq.setTimeManagementQueueName(timeMgmtEventQueue.getQueueName());
			rrReq.setFederateRequestQueueName(federateRequestEventQueue
					.getQueueName());
		} catch (JMSException e) {
			fail(e.getLocalizedMessage());
		}

		ReadyToRunFederationExecutionEntry r2r = null;
		try {
			r2r = new ReadyToRunFederationExecutionEntry(fem, rrReq, txtMessage);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		try {
			fec.registerReadyToRun(r2r);
		} catch (MuthurException e3) {
			fail(e3.getLocalizedMessage());
		}

		// register the NexSim federate as ready to run

		try {
			txtMessage = session.createTextMessage();
		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		}

		assert (txtMessage != null);

		try {
			txtMessage.setJMSReplyTo(replyToQueue);
			txtMessage.setJMSCorrelationID(UUID.randomUUID().toString());
		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		}

		rrReq = new ReadyToRunRequest();
		rrReq.setSourceOfEvent(frascaFederateName);
		rrReq.setFederationExecutionHandle(fedExecHandle);
		try {
			rrReq.setFederateRequestQueueName(federateRequestEventQueue
					.getQueueName());
			rrReq.setTimeManagementQueueName(timeMgmtEventQueue.getQueueName());
		} catch (JMSException e) {
			fail(e.getLocalizedMessage());
		}

		try {
			r2r = new ReadyToRunFederationExecutionEntry(fem, rrReq, txtMessage);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// register the Frasca federate as ready to run

		try {
			fec.registerReadyToRun(r2r);
		} catch (MuthurException e1) {
			fail(e1.getLocalizedMessage());
		}

		// block on semaphore until all responses are received back in the
		// ResponseHandler

		try {
			if (!sem.tryAcquire(2, 360, TimeUnit.SECONDS)) {
				fail("Timed out before all " + "ready to run responses received");
			}
		} catch (InterruptedException e2) {
			fail(e2.getLocalizedMessage());
		}

		// #########################################
		//
		// 4) Terminate the federation
		//
		// #########################################

		// re-initialize the semaphore that will be released when the responses from
		// Muthur are returned in the ResponseHandler
		sem = new Semaphore(0);

		try {
			fec.terminate();
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// block on the semaphore until all responses are received back in the
		// listener FedEventConsumer
		try {
			if (!sem.tryAcquire(2, 10, TimeUnit.SECONDS)) {
				fail("Timed out before termination notifications received");
			}
		} catch (InterruptedException e2) {
			fail(e2.getLocalizedMessage());
		}

	}

	/**
	 * Consumes messages sent by the federation
	 * 
	 * @author wdrew
	 * 
	 */
	static class FedEventConsumer implements MessageListener {

		public void onMessage(Message message) {
			if (message != null) {
				TextMessage tm = (TextMessage) message;
				if (tm != null) {
					sem.release();

				}
			}
		}
	}

	/**
	 * Consumes time management events
	 * 
	 * @author wdrew
	 * 
	 */
	static class TimeManagementEventConsumer implements MessageListener {

		public void onMessage(Message message) {
			if (message != null) {
				TextMessage tm = (TextMessage) message;
				if (tm != null) {
					sem.release();

				}
			}
		}
	}

	/**
	 * Consumes data published to the federation
	 * 
	 * @author wdrew
	 * 
	 */
	static class DataPublicationQueueListener implements MessageListener {

		public void onMessage(Message message) {
			if (message != null) {
				TextMessage tm = (TextMessage) message;
				if (tm != null) {

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
	static class ResponseHandler implements MessageListener {

		public void onMessage(Message message) {

			if ((message != null) && (message instanceof TextMessage)) {

				TextMessage tm = (TextMessage) message;
				if (tm != null) {
					System.out.println("Reply message received");
					try {
						assertNotNull(tm.getText());

						sem.release();

					} catch (JMSException e) {
						fail(e.getLocalizedMessage());
					}

				}
			}
		}
	}

	/**
	 * 
	 * 
	 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
	 * @version $Revision$
	 */
	private static class OwnershipEventHandler implements MessageListener {

		public void onMessage(Message message) {

			if ((message != null) && (message instanceof TextMessage)) {
				TextMessage txtMsg = (TextMessage) message;

				try {

					String ownershipEventPayload = txtMsg.getText();

					IEvent event =
							EventFactory.getInstance().createEvent(ownershipEventPayload);

					if (event != null) {

						if (event instanceof ObjectOwnershipRelinquishedEvent) {

						}
					}

				} catch (Exception e) {
					fail(e.getLocalizedMessage());
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.jms.ExceptionListener#onException(javax.jms.JMSException)
	 */
	@Override
	public void onException(JMSException arg0) {
		// just eat the exception which is caused by not cleaning up JMS resources
	}
}
