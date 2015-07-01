/**
 * 
 */
package com.csc.muthur.server.time.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.csc.muthur.server.commons.CommonsService;
import com.csc.muthur.server.commons.EventTypeEnum;
import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.commons.MessageDestination;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.commons.internal.CommonsServiceImpl;
import com.csc.muthur.server.mocks.MockConfigurationServerImpl;
import com.csc.muthur.server.model.FederationExecutionDirective;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.ReadyToRunFederationExecutionEntry;
import com.csc.muthur.server.model.event.EventFactory;
import com.csc.muthur.server.model.event.request.ReadyToRunRequest;
import com.csc.muthur.server.model.event.request.TimeManagementRequest;
import com.csc.muthur.server.time.FederateClockManager;

/**
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class FederateClockManagerSingleFederateTest implements
		ExceptionListener {

	private static MockConfigurationServerImpl configurationService;
	private static BrokerService broker;
	private static ActiveMQConnectionFactory connectionFactory;
	private static Connection connection;
	private static Session session;
	private static TemporaryQueue timeMgmtEventQueue;
	private static MessageConsumer timeMgmtEventConsumer;

	private static String nexSimFederateName = "NexSim";

	private String federationExecutionHandle = UUID.randomUUID().toString();
	private String federateRegistrationHandle = UUID.randomUUID().toString();
	private static TemporaryQueue federateRequestEventQueue;

	private static Format f = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss.SSS");

	// Federate queue, consumer, semaphore and message variables

	private static int timeManagementRunMessageCntReceived;

	private static int timeManagementStartMessagesExpected;
	private static int timeManagementRunMessagesExpected;

	private static Semaphore timeManagementStartEventsSemaphore;
	private static Semaphore timeManagementRunEventsSemaphore;
	private static Semaphore timeManagementPauseEventsSemaphore;

	private static long originalStartingTime;

	/**
	 * @throws java.lang.Exception
	 */
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

		connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD,
				connUrl);

		connection = connectionFactory.createConnection();

		connection.start();

		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		timeMgmtEventQueue = session.createTemporaryQueue();

		timeMgmtEventConsumer = session.createConsumer(timeMgmtEventQueue);

		timeMgmtEventConsumer.setMessageListener(new FedEventConsumer());

		federateRequestEventQueue = session.createTemporaryQueue();

	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		broker.stop();
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
	 * Test for a single federate clock being added to the federate clock manager.
	 * The
	 */
	@Test
	public void singleFederateClockManagerImplTest() {

		FederationExecutionModel fem = null;

		try {
			fem = new FederationExecutionModel("Single Federate lock Manager Test");
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// add the single federate

		fem.addRequiredFededrate(nexSimFederateName);

		// set the logical time

		Calendar cal = Calendar.getInstance();

		cal.setTimeInMillis(System.currentTimeMillis());
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.HOUR, 12);
		cal.add(Calendar.MINUTE, 30);

		fem.setLogicalStartTimeMSecs(cal.getTimeInMillis());
		fem.setDefaultDurationWithinStartupProtocolMSecs(30000);
		fem.setDurationFederationExecutionMSecs(10000);

		originalStartingTime = cal.getTimeInMillis();

		// create a R2R request

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

		// set up the reply-to queue for this federate to receive the time messages

		TextMessage txtMessage = null;
		ReadyToRunFederationExecutionEntry readyToRunFederationExecutionEntry = null;

		try {

			// create the TextMessage for each...

			txtMessage = session.createTextMessage();

			// ...set the join request as the serialized event

			txtMessage.setText(req.serialize());

			// Set the reply to field to the queue established in
			// setUpBeforeClass()

			txtMessage.setJMSReplyTo(timeMgmtEventQueue);
			txtMessage.setJMSCorrelationID(UUID.randomUUID().toString());

			txtMessage.setStringProperty(MessageDestination
					.getDataEventQueuePropName(), timeMgmtEventQueue.getQueueName());

			// create an execution entry that will satisfy the R2R request processor
			// and which will be added as a federate clock to the federate time
			// manager

			readyToRunFederationExecutionEntry = new ReadyToRunFederationExecutionEntry(
					fem, req, txtMessage);

		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// create a ReadyToRunFederationExecutionEntry entry for the federate clock
		// manager

		Map<String, ReadyToRunFederationExecutionEntry> federateNameTofedExecReadyToRunEntryMap = new ConcurrentHashMap<String, ReadyToRunFederationExecutionEntry>();

		federateNameTofedExecReadyToRunEntryMap.put(
				readyToRunFederationExecutionEntry.getFederateName(),
				readyToRunFederationExecutionEntry);

		// initialize time management events semaphore and the variables for
		// messages received and expected

		timeManagementRunEventsSemaphore = new Semaphore(0);
		timeManagementStartEventsSemaphore = new Semaphore(0);
		timeManagementPauseEventsSemaphore = new Semaphore(0);

		timeManagementRunMessageCntReceived = 0;

		timeManagementStartMessagesExpected = 1;
		timeManagementRunMessagesExpected = 4;

		// create federate clock manager

		FederateClockManager fcmi = null;

		try {

			fcmi = new FederateClockManagerImpl(fem, session);

			fcmi.addFederate(readyToRunFederationExecutionEntry);

			// start the federate clock manager

			fcmi.start();

		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// wait to get the start time event

		try {
			if (!timeManagementStartEventsSemaphore.tryAcquire(
					timeManagementStartMessagesExpected, 30, TimeUnit.SECONDS)) {
				fail("Failed to acquire start time messages semaphore.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

		// wait to get 4 run time events

		try {
			if (!timeManagementRunEventsSemaphore.tryAcquire(
					timeManagementRunMessagesExpected, 30, TimeUnit.SECONDS)) {
				fail("Failed to acquire run time messages semaphore.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

		// tests if all time management events were received in
		// FedEventConsumer.onMessage()

		assertTrue(timeManagementRunMessageCntReceived == timeManagementRunMessagesExpected);

		fcmi.pause();

		try {
			if (!timeManagementPauseEventsSemaphore.tryAcquire(1, 30,
					TimeUnit.SECONDS)) {
				fail("Failed to acquire run time messages semaphore during pause.");
			}

		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

		timeManagementRunEventsSemaphore = new Semaphore(0);
		timeManagementRunMessageCntReceived = 0;
		timeManagementRunMessagesExpected = 5;

		fcmi.resume();

		// wait till get run time events

		try {
			if (!timeManagementRunEventsSemaphore.tryAcquire(
					timeManagementRunMessagesExpected, 30, TimeUnit.SECONDS)) {
				fail("Failed to acquire run time messages semaphore.");
			}

		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

		// tests if all run time management events were received in
		// FedEventConsumer.onMessage()

		assertTrue(timeManagementRunMessageCntReceived == timeManagementRunMessagesExpected);

		// current time
		Calendar adjustTimeBack = Calendar.getInstance();

		adjustTimeBack.setTimeInMillis(System.currentTimeMillis());

		adjustTimeBack.add(Calendar.MONTH, -5);
		adjustTimeBack.add(Calendar.YEAR, -5);

		fcmi.setCurrentTimeMSecs(adjustTimeBack.getTimeInMillis());

		try {
			Thread.currentThread();
			Thread.sleep(10000);
		} catch (InterruptedException e) {
		}

		// terminate the federation

		fcmi.terminate();

	}

	/**
	 * 
	 * Receives messages sent by the federation time manager.
	 * timeManagementRunMessageCntReceived will be incremented in the onMessage()
	 * for each time management event received. This will eventually release the
	 * timeManagementRunEventsSemaphore each time until
	 * timeManagementRunMessagesExpected permits have been released.
	 * 
	 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
	 * @version $Revision$
	 * 
	 */
	private static class FedEventConsumer implements MessageListener {

		public void onMessage(Message message) {
			if (message != null) {
				TextMessage tm = (TextMessage) message;
				if (tm != null) {
					try {
						String payLoad = tm.getText();

						IEvent e = EventFactory.getInstance().createEvent(payLoad);

						if (e.isSuccess()) {

							if (e.getEventType().equals(EventTypeEnum.TimeManagementRequest)) {

								TimeManagementRequest tmr = (TimeManagementRequest) e;

								Date createDate = new Date(e.getCreateTimeMilliSecs());
								Date timeStepDate = new Date(tmr.getTimeIntervalMSecs());
								System.out.println("Time event message created at ["
										+ f.format(createDate) + "] with a time step of ["
										+ f.format(timeStepDate) + "] received in consumer.");

								if (tmr.getFederationExecutionDirective().equals(
										FederationExecutionDirective.START)) {

									assertTrue(originalStartingTime == tmr.getTimeIntervalMSecs());

									/*
									 * releases the semaphore that each test is using to block
									 * until all responses are received
									 */
									timeManagementStartEventsSemaphore.release();
								}

								if (tmr.getFederationExecutionDirective().equals(
										FederationExecutionDirective.RUN)) {

									timeManagementRunMessageCntReceived++;
									/*
									 * releases the semaphore that each test is using to block
									 * until all responses are received
									 */
									timeManagementRunEventsSemaphore.release();

								} else if (tmr.getFederationExecutionDirective().equals(
										FederationExecutionDirective.PAUSE)) {

									/*
									 * releases the semaphore that each test is using to block
									 * until all responses are received
									 */
									timeManagementPauseEventsSemaphore.release();

								}
							}

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
	 * 
	 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
	 * @version $Revision$
	 * 
	 */
	static class DataPublicationQueueListener implements MessageListener {

		public void onMessage(Message message) {
			if (message != null) {
				TextMessage tm = (TextMessage) message;
				if (tm != null) {
					System.out.println("Data publication message");
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
						String msgBody = tm.getText();
						System.out.println("Message [" + msgBody + "]");

						timeManagementRunEventsSemaphore.release();

					} catch (JMSException e) {
						fail(e.getLocalizedMessage());
					}

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
		// just eat the exception which is caused by not cleaning up JMS
		// resources
	}

}
