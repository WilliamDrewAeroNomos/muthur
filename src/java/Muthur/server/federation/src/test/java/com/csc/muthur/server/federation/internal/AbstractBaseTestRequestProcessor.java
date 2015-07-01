/**
 * 
 */
package com.csc.muthur.server.federation.internal;

import static org.junit.Assert.fail;

import java.util.UUID;
import java.util.concurrent.Semaphore;

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

import com.csc.muthur.server.commons.CommonsService;
import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.commons.internal.CommonsServiceImpl;
import com.csc.muthur.server.federation.FederationExecutionController;
import com.csc.muthur.server.federation.FederationService;
import com.csc.muthur.server.federation.internal.execution.FederationServiceImpl;
import com.csc.muthur.server.mocks.MockConfigurationServerImpl;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.ModelService;
import com.csc.muthur.server.model.event.EventFactory;
import com.csc.muthur.server.model.event.request.TimeManagementRequest;
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
public abstract class AbstractBaseTestRequestProcessor implements
		ExceptionListener {

	protected static MockConfigurationServerImpl configurationService;
	protected static String nexSimFederateName = "NexSim";
	protected static String frascaFederateName = "Frasca";
	
	protected static FederationExecutionController federationExecutionController = null;
	protected static FederationExecutionModel fem;
	protected static BrokerService broker;
	
	protected static ActiveMQConnectionFactory connectionFactory;
	protected static Connection connection;
	protected static Session session;
	
	protected static TemporaryQueue federationEventQueue;
	protected static MessageConsumer fedEventConsumer;
	protected static TemporaryQueue dataPublicationQueue;
	
	protected static MessageConsumer dataPublicationConsumer;
	protected static TemporaryQueue timeMgmtEventQueue;
	protected static MessageConsumer timeMgmtEventConsumer;

	protected static TemporaryQueue replyToQueue;
	protected static Semaphore sem;
	protected static FederationService federationService;
	
	protected static RouterService routerService;
	protected static TimeService timeService;
	protected static ModelService modelService;
	
	protected static ObjectService objectService;
	protected static TemporaryQueue federateRequestEventQueue;
	protected static Semaphore semTimeMgmtEventHandler;

	protected static long previousTimeInterval = 0L;
	protected static int pauseResponsesReceived = 0;
	protected static final int maxNumOfPausedResponses = 5;
	protected static int runningResponsesReceived = 0;
	
	protected static final int maxNumOfRunningResponses = 5;
	protected String eventSourceName = "NexSim";
	protected String nexSimName = "NexSim";
	
	protected String FrascaName = "Frasca";
	protected String name = "Test Model";
	protected String description = "This is a test model";
	protected int femDurationMSecs = 60;
	
	protected int reqDurationMSecs = 10;
	protected String federationExecutionHandle = UUID.randomUUID().toString();
	protected String federateRegistrationHandle = UUID.randomUUID().toString();
	
	protected String fedExecModelUUID = UUID.randomUUID().toString();

	/**
	 * 
	 */
	public AbstractBaseTestRequestProcessor() {
		super();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		CommonsService commonsService = new CommonsServiceImpl();

		configurationService = new MockConfigurationServerImpl();

		int availablePort = commonsService.findAvailablePort(6000, 7000);

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

		// time management queue

		timeMgmtEventQueue = session.createTemporaryQueue();

		timeMgmtEventConsumer = session.createConsumer(timeMgmtEventQueue);

		timeMgmtEventConsumer.setMessageListener(new FedEventConsumer());

		// create the services for the federation service

		routerService = new RouterServiceImpl();

		routerService.setConfigurationServer(configurationService);

		routerService.start();

		timeService = new TimeServiceImpl();

		timeService.setConfigurationService(configurationService);

		timeService.start();

		federationService = new FederationServiceImpl();

		federationService.setConfigurationService(configurationService);

		federationService.setRouterService(routerService);

		federationService.setTimeService(timeService);

		federationService.setCommonsService(commonsService);

		/**
		 * create model service and set it inside the object service impl
		 */

		modelService = new ModelServiceImpl();

		modelService.start();

		objectService = new ObjectServiceImpl();

		// set the services into the object service impl

		objectService.setConfigurationService(configurationService);
		objectService.setModelService(modelService);

		// set the object service into the federation service
		//
		federationService.setObjectService(objectService);

		federateRequestEventQueue = session.createTemporaryQueue();
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
	 * Consumes messages sent by the federation execution controller
	 * 
	 * @author wdrew
	 * 
	 */
	protected static class FedEventConsumer implements MessageListener {

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
							sem.release();
						}
					} catch (Exception e) {
						fail(e.getLocalizedMessage());
					}

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
	protected static class DataPublicationQueueListener implements
			MessageListener {

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
	protected static class ResponseHandler implements MessageListener {

		public void onMessage(Message message) {

			if ((message != null) && (message instanceof TextMessage)) {

				TextMessage tm = (TextMessage) message;
				if (tm != null) {
					System.out.println("Reply message received");
					try {
						String msgBody = tm.getText();
						System.out.println("Message [" + msgBody + "]");

						sem.release();

					} catch (JMSException e) {
						fail(e.getLocalizedMessage());
					}

				}
			}
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
	protected static class FederationTimeManagementEventHandler implements
			MessageListener {

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

								if (previousTimeInterval == 0L) {
									previousTimeInterval = tmr.getTimeIntervalMSecs();
								}

								else {
									// if the federation is paused then the
									// previous and current
									// time should be the same
									switch (tmr.getFederationExecutionState()) {

									case PAUSED:

										System.out.println("**** PAUSED ****");
										System.out.println("Previous time = ["
												+ previousTimeInterval + "]");
										System.out.println("Current time  = ["
												+ tmr.getTimeIntervalMSecs() + "]");

										pauseResponsesReceived++;

										if ((pauseResponsesReceived > 1)
												&& (pauseResponsesReceived <= maxNumOfPausedResponses)) {
											assert (previousTimeInterval == tmr
													.getTimeIntervalMSecs());
										}
										if (pauseResponsesReceived >= maxNumOfPausedResponses) {
											semTimeMgmtEventHandler.release();
										}

										break;

									case RUNNING:

										System.err.println("**** RUNNING ****");
										System.err.println("Previous time = ["
												+ previousTimeInterval + "]");
										System.err.println("Current time  = ["
												+ tmr.getTimeIntervalMSecs() + "]");

										runningResponsesReceived++;

										if ((runningResponsesReceived > 1)
												&& (runningResponsesReceived <= maxNumOfRunningResponses)) {
											assert (previousTimeInterval != tmr
													.getTimeIntervalMSecs());
										}

										if (runningResponsesReceived >= maxNumOfRunningResponses) {
											semTimeMgmtEventHandler.release();
										}

										break;

									case AWAITING_START_DIRECTIVE:
									case UNDEFINED:
										break;
									}
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
