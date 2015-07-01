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
public class FederateClockManagerMultipleFederateWithTimeSetAtStartTest
		implements ExceptionListener {

	private static MockConfigurationServerImpl configurationService;
	private static BrokerService broker;
	private static ActiveMQConnectionFactory connectionFactory;
	private static Connection connection;
	private static Session session;

	// NexSim queue, consumer, semaphore and message variables
	private static String nexSimFederateName = "NexSim";
	private static TemporaryQueue nexSimTimeMgmtEventQueue;
	private static MessageConsumer nexSimTimeMgmtEventConsumer;

	private static int nexSimTimeManagementRunEventCntReceived;
	private static int nexSimTimeManagementRunEventsExpected;
	private static int nexSimTimeManagementStartEventsExpected;
	private static int nexSimTimeManagementPauseEventsExpected;

	private static Semaphore nexSimTimeManagementRunEventsSemaphore;
	public static Semaphore nexSimTimeManagementStartEventsSemaphore;
	private static Semaphore nexSimTimeManagementPauseEventsSemaphore;

	// Frasca queue, consumer, semaphore and message variables
	private static String frascaFederateName = "Frasca";
	private static TemporaryQueue frascaTimeMgmtEventQueue;
	private static MessageConsumer frascaTimeMgmtEventConsumer;

	private static int frascaTimeManagementRunEventCntReceived;
	private static int frascaTimeManagementRunEventsExpected;
	private static int frascaTimeManagementStartEventsExpected;
	private static int frascaTimeManagementPauseEventsExpected;

	private static Semaphore frascaTimeManagementRunEventsSemaphore;
	private static Semaphore frascaTimeManagementStartEventsSemaphore;
	private static Semaphore frascaTimeManagementPauseEventsSemaphore;

	private String federationExecutionHandle = UUID.randomUUID().toString();
	private String federateRegistrationHandle = UUID.randomUUID().toString();
	private static TemporaryQueue federateRequestEventQueue;

	// start time variables

	private static Calendar updatedCal = null;

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
				ActiveMQConnection.DEFAULT_USER,
				ActiveMQConnection.DEFAULT_PASSWORD, connUrl);

		connection = connectionFactory.createConnection();

		connection.start();

		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		// create queue, consumer and listener for NexSim federate

		nexSimTimeMgmtEventQueue = session.createTemporaryQueue();

		nexSimTimeMgmtEventConsumer = session
				.createConsumer(nexSimTimeMgmtEventQueue);

		nexSimTimeMgmtEventConsumer
				.setMessageListener(new NexSimTimeEventConsumer());

		// create queue, consumer and listener for Frasca federate

		frascaTimeMgmtEventQueue = session.createTemporaryQueue();

		frascaTimeMgmtEventConsumer = session
				.createConsumer(frascaTimeMgmtEventQueue);

		frascaTimeMgmtEventConsumer
				.setMessageListener(new FrascaTimeEventConsumer());

		// federate request queue

		federateRequestEventQueue = session.createTemporaryQueue();

	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if (connection != null) {
			connection.close();
		}
		if (session != null) {
			session.close();
		}
		if (broker != null) {
			broker.stop();
		}
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
	 * Test for two federate clocks being added to the federate clock manager.
	 * 
	 */
	@Test
	public void multiFederateClockManagerImplTest() {

		FederationExecutionModel fem = null;

		try {
			fem = new FederationExecutionModel(
					"Multiple Federate Clock Manager Test");
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// add the multiple federates

		fem.addRequiredFededrate(nexSimFederateName);
		fem.addRequiredFededrate(frascaFederateName);

		// set the logical time

		Calendar cal = Calendar.getInstance();

		cal.setTimeInMillis(System.currentTimeMillis());
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.HOUR, 12);
		cal.add(Calendar.MINUTE, 30);

		fem.setLogicalStartTimeMSecs(cal.getTimeInMillis());
		fem.setDefaultDurationWithinStartupProtocolMSecs(30000);
		fem.setDurationFederationExecutionMSecs(10000);

		// create the NexSim request

		ReadyToRunRequest nexSimRequest = new ReadyToRunRequest();
		assertNotNull(nexSimRequest);

		nexSimRequest.setTimeToLiveSecs(10);
		nexSimRequest.setSourceOfEvent(nexSimFederateName);
		try {
			nexSimRequest.setFederateRequestQueueName(federateRequestEventQueue
					.getQueueName());
			nexSimRequest.setTimeManagementQueueName(nexSimTimeMgmtEventQueue
					.getQueueName());
		} catch (JMSException e2) {
			fail(e2.getLocalizedMessage());
		}

		nexSimRequest.setFederateRegistrationHandle(federateRegistrationHandle);
		nexSimRequest.setFederationExecutionHandle(federationExecutionHandle);

		// set up the reply-to queue for NexSim federate to receive the time
		// messages

		TextMessage txtMessage = null;
		ReadyToRunFederationExecutionEntry nexSimReadyToRunFederationExecutionEntry = null;

		try {

			// create the TextMessage for each...

			txtMessage = session.createTextMessage();

			// ...set the join request as the serialized event

			txtMessage.setText(nexSimRequest.serialize());

			// Set the reply to field to the queue established in
			// setUpBeforeClass()

			txtMessage.setJMSReplyTo(nexSimTimeMgmtEventQueue);
			txtMessage.setJMSCorrelationID(UUID.randomUUID().toString());

			txtMessage.setStringProperty(
					MessageDestination.getDataEventQueuePropName(),
					nexSimTimeMgmtEventQueue.getQueueName());

			// create an execution entry that will satisfy the R2R request
			// processor
			// and
			// which will be added as a federate clock to the federate time
			// manager

			nexSimReadyToRunFederationExecutionEntry = new ReadyToRunFederationExecutionEntry(
					fem, nexSimRequest, txtMessage);

		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// create the Frasca request

		ReadyToRunRequest frascaRequest = new ReadyToRunRequest();
		assertNotNull(frascaRequest);

		frascaRequest.setTimeToLiveSecs(10);
		frascaRequest.setSourceOfEvent(frascaFederateName);
		try {
			frascaRequest.setFederateRequestQueueName(federateRequestEventQueue
					.getQueueName());
			frascaRequest.setTimeManagementQueueName(frascaTimeMgmtEventQueue
					.getQueueName());
		} catch (JMSException e2) {
			fail(e2.getLocalizedMessage());
		}

		frascaRequest.setFederateRegistrationHandle(federateRegistrationHandle);
		frascaRequest.setFederationExecutionHandle(federationExecutionHandle);

		// set up the reply-to queue for NexSim federate to receive the time
		// messages

		txtMessage = null;
		ReadyToRunFederationExecutionEntry frascaReadyToRunFederationExecutionEntry = null;

		try {

			// create the TextMessage for each...

			txtMessage = session.createTextMessage();

			// ...set the join request as the serialized event

			txtMessage.setText(frascaRequest.serialize());

			// Set the reply to field to the queue established in
			// setUpBeforeClass()

			txtMessage.setJMSReplyTo(frascaTimeMgmtEventQueue);
			txtMessage.setJMSCorrelationID(UUID.randomUUID().toString());

			txtMessage.setStringProperty(
					MessageDestination.getDataEventQueuePropName(),
					frascaTimeMgmtEventQueue.getQueueName());

			// create an execution entry that will satisfy the R2R request
			// processor
			// and
			// which will be added as a federate clock to the federate time
			// manager

			frascaReadyToRunFederationExecutionEntry = new ReadyToRunFederationExecutionEntry(
					fem, frascaRequest, txtMessage);

		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// initialize time management events semaphores and the variables for
		// messages received and expected for NexSim

		nexSimTimeManagementStartEventsSemaphore = new Semaphore(0);
		nexSimTimeManagementRunEventsSemaphore = new Semaphore(0);
		nexSimTimeManagementPauseEventsSemaphore = new Semaphore(0);

		nexSimTimeManagementRunEventCntReceived = 0;
		nexSimTimeManagementStartEventsExpected = 1;
		nexSimTimeManagementRunEventsExpected = 4;

		nexSimTimeManagementPauseEventsExpected = 1;

		// initialize time management event semaphores and the variables for
		// messages received and expected for Frasca

		frascaTimeManagementStartEventsSemaphore = new Semaphore(0);
		frascaTimeManagementRunEventsSemaphore = new Semaphore(0);
		frascaTimeManagementPauseEventsSemaphore = new Semaphore(0);

		frascaTimeManagementRunEventCntReceived = 0;

		frascaTimeManagementStartEventsExpected = 1;
		frascaTimeManagementRunEventsExpected = 4;

		frascaTimeManagementPauseEventsExpected = 1;

		// create federate clock manager

		FederateClockManager fcmi = null;

		try {

			fcmi = new FederateClockManagerImpl(fem, session);

			fcmi.addFederate(nexSimReadyToRunFederationExecutionEntry);

			fcmi.addFederate(frascaReadyToRunFederationExecutionEntry);

			// set the logical time

			updatedCal = Calendar.getInstance();

			updatedCal.setTimeInMillis(System.currentTimeMillis());

			updatedCal.add(Calendar.MONTH, 12);
			updatedCal.add(Calendar.HOUR, 25);
			updatedCal.add(Calendar.MINUTE, 30);

			// start the federate clock manager

			fcmi.setCurrentTimeMSecs(updatedCal.getTimeInMillis());

			// start the federate clock manager

			fcmi.start();

		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// wait till both federates have received the start directive

		try {
			if (!nexSimTimeManagementStartEventsSemaphore.tryAcquire(
					nexSimTimeManagementStartEventsExpected, 30,
					TimeUnit.SECONDS)) {
				fail("Failed to receive expected number of time management events "
						+ "for the NexSim federate.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

		try {
			if (!frascaTimeManagementRunEventsSemaphore.tryAcquire(
					frascaTimeManagementStartEventsExpected, 30,
					TimeUnit.SECONDS)) {
				fail("Failed to receive expected number of time management events "
						+ "for the Frasca federate.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

		// wait till both federates have received the expected number of run
		// directives

		try {
			if (!nexSimTimeManagementRunEventsSemaphore
					.tryAcquire(nexSimTimeManagementRunEventsExpected, 30,
							TimeUnit.SECONDS)) {
				fail("Failed to receive expected number of time management events "
						+ "for the NexSim federate.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

		try {
			if (!frascaTimeManagementRunEventsSemaphore
					.tryAcquire(frascaTimeManagementRunEventsExpected, 30,
							TimeUnit.SECONDS)) {
				fail("Failed to receive expected number of time management events "
						+ "for the Frasca federate.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

		// initialize the pause semaphores

		nexSimTimeManagementPauseEventsSemaphore = new Semaphore(0);
		frascaTimeManagementPauseEventsSemaphore = new Semaphore(0);

		// check if we've received at least the number of expected run
		// directives

		assertTrue(nexSimTimeManagementRunEventCntReceived >= nexSimTimeManagementRunEventsExpected);
		assertTrue(frascaTimeManagementRunEventCntReceived >= frascaTimeManagementRunEventsExpected);

		fcmi.pause();

		// wait till both federates have received at least one pause directive

		try {
			if (!nexSimTimeManagementPauseEventsSemaphore.tryAcquire(1, 30,
					TimeUnit.SECONDS)) {
				fail("Failed to receive expected number of time management events "
						+ "for the NexSim federate.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

		try {
			if (!frascaTimeManagementPauseEventsSemaphore.tryAcquire(1, 30,
					TimeUnit.SECONDS)) {
				fail("Failed to receive expected number of time management events "
						+ "for the Frasca federate.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

		// ensure that we got at least one pause for NexSim and...
		assertTrue(1 == nexSimTimeManagementPauseEventsExpected);
		// ...and Frasca
		assertTrue(1 == frascaTimeManagementPauseEventsExpected);

		// re-initialize time management events semaphores and the variables for
		// messages received and expected for NexSim for the resume processing

		nexSimTimeManagementRunEventsSemaphore = new Semaphore(0);
		nexSimTimeManagementRunEventCntReceived = 0;
		nexSimTimeManagementRunEventsExpected = 4;

		// initialize time management event semaphores and the variables for
		// messages received and expected for Frasca for the resume processing

		frascaTimeManagementRunEventsSemaphore = new Semaphore(0);
		frascaTimeManagementRunEventCntReceived = 0;
		frascaTimeManagementRunEventsExpected = 4;

		// resume the federation

		fcmi.resume();

		// wait till both federates have received the expected number of run
		// directives

		try {
			if (!nexSimTimeManagementRunEventsSemaphore
					.tryAcquire(nexSimTimeManagementRunEventsExpected, 30,
							TimeUnit.SECONDS)) {
				fail("Failed to receive expected number of time management events "
						+ "for the NexSim federate.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

		try {
			if (!frascaTimeManagementRunEventsSemaphore
					.tryAcquire(frascaTimeManagementRunEventsExpected, 30,
							TimeUnit.SECONDS)) {
				fail("Failed to receive expected number of time management events "
						+ "for the Frasca federate.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

		// tests if the expected number of time management events were received
		// in
		// NexSimTimeEventConsumer.onMessage() and
		// FrascaTimeEventConsumer.onMessage()

		System.out.println("NexSim time events ["
				+ nexSimTimeManagementRunEventCntReceived + "]");
		System.out.println("Frasca time events ["
				+ frascaTimeManagementRunEventCntReceived + "]");

		// the reasoning behind the >= rather than simply = is that the time
		// between
		// the semaphores being released and the termination being completed
		// several
		// additional messages could have been received.
		//
		assertTrue(nexSimTimeManagementRunEventCntReceived >= nexSimTimeManagementRunEventsExpected);
		assertTrue(frascaTimeManagementRunEventCntReceived >= frascaTimeManagementRunEventsExpected);

		// terminate the federation

		fcmi.terminate();

	}

	/**
	 * 
	 * Receives messages sent by the federation time manager.
	 * nexSimTimeManagementMessageCntReceived will be incremented in the
	 * onMessage() for each time management event received. This will eventually
	 * release the nexSimTimeManagementEventsSemaphore each time until
	 * nexSimTimeManagementMessagesExpected permits have been released.
	 * 
	 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
	 * @version $Revision$
	 * 
	 */
	private static class NexSimTimeEventConsumer implements MessageListener {

		public void onMessage(Message message) {
			if (message != null) {
				TextMessage tm = (TextMessage) message;
				if (tm != null) {
					try {
						String payLoad = tm.getText();

						IEvent e = EventFactory.getInstance().createEvent(
								payLoad);

						if (e.isSuccess()) {

							if (e.getEventType().equals(
									EventTypeEnum.TimeManagementRequest)) {
								TimeManagementRequest tmr = (TimeManagementRequest) e;

								Format f = new SimpleDateFormat(
										"yyyy.MM.dd HH.mm.ss");
								Date createDate = new Date(
										e.getCreateTimeMilliSecs());
								Date timeStepDate = new Date(
										tmr.getTimeIntervalMSecs());
								System.out
										.println("Time event message created at ["
												+ f.format(createDate)
												+ "] with a time step of ["
												+ f.format(timeStepDate)
												+ "] received in consumer.");

								if (tmr.getFederationExecutionDirective()
										.equals(FederationExecutionDirective.START)) {

									/*
									 * releases the semaphore that each test is
									 * using to block until all responses are
									 * received
									 */
									nexSimTimeManagementStartEventsSemaphore
											.release();

								} else if (tmr
										.getFederationExecutionDirective()
										.equals(FederationExecutionDirective.RUN)) {

									nexSimTimeManagementRunEventCntReceived++;
									/*
									 * releases the semaphore that each test is
									 * using to block until all responses are
									 * received
									 */
									nexSimTimeManagementRunEventsSemaphore
											.release();

								} else if (tmr
										.getFederationExecutionDirective()
										.equals(FederationExecutionDirective.PAUSE)) {

									/*
									 * releases the semaphore that each test is
									 * using to block until all responses are
									 * received
									 */
									nexSimTimeManagementPauseEventsSemaphore
											.release();
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
	 * Receives messages sent by the federation time manager.
	 * nexSimTimeManagementMessageCntReceived will be incremented in the
	 * onMessage() for each time management event received. This will eventually
	 * release the nexSimTimeManagementEventsSemaphore each time until
	 * nexSimTimeManagementMessagesExpected permits have been released.
	 * 
	 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
	 * @version $Revision$
	 * 
	 */
	private static class FrascaTimeEventConsumer implements MessageListener {

		public void onMessage(Message message) {
			if (message != null) {
				TextMessage tm = (TextMessage) message;
				if (tm != null) {
					try {
						String payLoad = tm.getText();

						IEvent e = EventFactory.getInstance().createEvent(
								payLoad);

						if (e.isSuccess()) {

							if (e.getEventType().equals(
									EventTypeEnum.TimeManagementRequest)) {
								TimeManagementRequest tmr = (TimeManagementRequest) e;

								Format f = new SimpleDateFormat(
										"yyyy.MM.dd HH.mm.ss");
								Date createDate = new Date(
										e.getCreateTimeMilliSecs());
								Date timeStepDate = new Date(
										tmr.getTimeIntervalMSecs());
								System.out
										.println("Time event message created at ["
												+ f.format(createDate)
												+ "] with a time step of ["
												+ f.format(timeStepDate)
												+ "] received in consumer.");

								if (tmr.getFederationExecutionDirective()
										.equals(FederationExecutionDirective.START)) {

									// check if the time was successfully set

									assertTrue(tmr.getTimeIntervalMSecs() == updatedCal
											.getTimeInMillis());

									/*
									 * releases the semaphore that each test is
									 * using to block until all responses are
									 * received
									 */
									frascaTimeManagementStartEventsSemaphore
											.release();

								} else if (tmr
										.getFederationExecutionDirective()
										.equals(FederationExecutionDirective.RUN)) {

									frascaTimeManagementRunEventCntReceived++;

									/*
									 * releases the semaphore that each test is
									 * using to block until all responses are
									 * received
									 */
									frascaTimeManagementRunEventsSemaphore
											.release();

								} else if (tmr
										.getFederationExecutionDirective()
										.equals(FederationExecutionDirective.PAUSE)) {

									/*
									 * releases the semaphore that each test is
									 * using to block until all responses are
									 * received
									 */
									frascaTimeManagementPauseEventsSemaphore
											.release();
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
