package com.csc.muthur.server.time.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.csc.muthur.server.commons.CommonsService;
import com.csc.muthur.server.commons.MessageDestination;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.commons.internal.CommonsServiceImpl;
import com.csc.muthur.server.mocks.MockConfigurationServerImpl;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.ReadyToRunFederationExecutionEntry;
import com.csc.muthur.server.model.event.request.ReadyToRunRequest;
import com.csc.muthur.server.time.TimeService;

public class TimeServiceImplTest {

	private static String nexSimFederateName = "NexSim";
	private static String frascaFederateName = "Frasca";

	private static MockConfigurationServerImpl configurationService;
	private static BrokerService broker;
	private static ActiveMQConnectionFactory connectionFactory;
	private static Connection connection;
	private static Session session;
	private static TemporaryQueue timeMgmtEventQueue;
	private static TemporaryQueue federateRequestEventQueue;
	private String federationExecutionHandle = UUID.randomUUID().toString();

	private String federateRegistrationHandle = UUID.randomUUID().toString();
	private TimeService ts;

	// private static int timeManagementMessageCntReceived;
	// private static Semaphore timeManagementEventsSemaphore = new Semaphore(0);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
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

		timeMgmtEventQueue = session.createTemporaryQueue();

		federateRequestEventQueue = session.createTemporaryQueue();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		broker.stop();
	}

	@Before
	public void setUp() throws Exception {

		ts = new TimeServiceImpl();
		ts.setConfigurationService(configurationService);
		ts.start();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public final void testCreateFederationExecutionTimeManager() {

		FederationExecutionModel fem = new FederationExecutionModel();

		try {
			fem = new FederationExecutionModel("Federation Exec Controller Test");
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}
		fem.addRequiredFededrate(nexSimFederateName);
		fem.addRequiredFededrate(frascaFederateName);
		fem.setDefaultDurationWithinStartupProtocolMSecs(30000);
		fem.setDurationFederationExecutionMSecs(10000);

		ReadyToRunRequest req = new ReadyToRunRequest();
		assertNotNull(req);

		req.setTimeToLiveSecs(10);
		req.setSourceOfEvent(nexSimFederateName);
		try {
			req.setFederateRequestQueueName(federateRequestEventQueue.getQueueName());
			req.setTimeManagementQueueName(timeMgmtEventQueue.getQueueName());
		} catch (JMSException e2) {
			fail(e2.getLocalizedMessage());
		}

		req.setFederateRegistrationHandle(federateRegistrationHandle);
		req.setFederationExecutionHandle(federationExecutionHandle);

		TextMessage txtMessage = null;

		// create map of ReadyToRunFederationExecutionEntrys

		Map<String, ReadyToRunFederationExecutionEntry> federateNameTofedExecReadyToRunEntryMap =
				new ConcurrentHashMap<String, ReadyToRunFederationExecutionEntry>();

		ReadyToRunFederationExecutionEntry readyToRunFederationExecutionEntry =
				null;

		// create the TextMessage for each...

		try {
			txtMessage = session.createTextMessage();

			// ...set the join request as the serialized event

			txtMessage.setText(req.serialize());

			// Set the reply to field to the queue established in setUpBeforeClass()

			txtMessage.setJMSReplyTo(timeMgmtEventQueue);
			txtMessage.setJMSCorrelationID(UUID.randomUUID().toString());

			txtMessage.setStringProperty(
					MessageDestination.getDataEventQueuePropName(),
					timeMgmtEventQueue.getQueueName());

			// create an execution entry for the NexSim federate and call
			// registerParticipant()

			readyToRunFederationExecutionEntry =
					new ReadyToRunFederationExecutionEntry(fem, req, txtMessage);

			// add the ReadyToRunFederationExecutionEntry to the map

			federateNameTofedExecReadyToRunEntryMap.put(
					readyToRunFederationExecutionEntry.getFederateName(),
					readyToRunFederationExecutionEntry);

			ts.createFederateClockManager(fem);

		} catch (JMSException e) {
			fail(e.getLocalizedMessage());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}
	}

	@Test
	public final void testDestroyFederationExecutionManager() {

		FederationExecutionModel fem = new FederationExecutionModel();

		try {
			fem = new FederationExecutionModel("Federation Exec Controller Test");
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}
		fem.addRequiredFededrate(nexSimFederateName);
		fem.addRequiredFededrate(frascaFederateName);
		fem.setDefaultDurationWithinStartupProtocolMSecs(30000);
		fem.setDurationFederationExecutionMSecs(10000);

		ReadyToRunRequest req = new ReadyToRunRequest();
		assertNotNull(req);

		req.setTimeToLiveSecs(10);
		req.setSourceOfEvent(nexSimFederateName);
		try {
			req.setFederateRequestQueueName(federateRequestEventQueue.getQueueName());
			req.setTimeManagementQueueName(timeMgmtEventQueue.getQueueName());
		} catch (JMSException e2) {
			fail(e2.getLocalizedMessage());
		}

		req.setFederateRegistrationHandle(federateRegistrationHandle);
		req.setFederationExecutionHandle(federationExecutionHandle);

		TextMessage txtMessage = null;

		// create map of ReadyToRunFederationExecutionEntrys

		Map<String, ReadyToRunFederationExecutionEntry> federateNameTofedExecReadyToRunEntryMap =
				new ConcurrentHashMap<String, ReadyToRunFederationExecutionEntry>();

		ReadyToRunFederationExecutionEntry readyToRunFederationExecutionEntry =
				null;

		// create the TextMessage for each...

		try {
			txtMessage = session.createTextMessage();

			// ...set the join request as the serialized event

			txtMessage.setText(req.serialize());

			// Set the reply to field to the queue established in setUpBeforeClass()

			txtMessage.setJMSReplyTo(timeMgmtEventQueue);
			txtMessage.setJMSCorrelationID(UUID.randomUUID().toString());

			txtMessage.setStringProperty(
					MessageDestination.getDataEventQueuePropName(),
					timeMgmtEventQueue.getQueueName());

			// create an execution entry for the NexSim federate and call
			// registerParticipant()

			readyToRunFederationExecutionEntry =
					new ReadyToRunFederationExecutionEntry(fem, req, txtMessage);

			// add the ReadyToRunFederationExecutionEntry to the map

			federateNameTofedExecReadyToRunEntryMap.put(
					readyToRunFederationExecutionEntry.getFederateName(),
					readyToRunFederationExecutionEntry);

			ts.createFederateClockManager(fem);

		} catch (JMSException e) {
			fail(e.getLocalizedMessage());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// now destroy the federation execution time manager

		ts.destroyFederateClockManager(fem);

	}

	@Test
	public void testCreateFederationExecutionTimeManagerNullArguments() {

		try {
			ts.createFederateClockManager(null);
			fail("Should have failed with null arguments.");
		} catch (Exception e) {
		}

		FederationExecutionModel fem = new FederationExecutionModel();

		try {
			fem = new FederationExecutionModel("Federation Exec Controller Test");
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		fem.addRequiredFededrate(nexSimFederateName);
		fem.addRequiredFededrate(frascaFederateName);
		fem.setDefaultDurationWithinStartupProtocolMSecs(30000);
		fem.setDurationFederationExecutionMSecs(10000);

		try {
			ts.createFederateClockManager(fem);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}
	}

}
