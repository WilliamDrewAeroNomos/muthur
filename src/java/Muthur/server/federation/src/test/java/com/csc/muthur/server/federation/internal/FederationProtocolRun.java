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
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;

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
 * 
 */

public class FederationProtocolRun {

	private static ConfigurationService configurationService;
	private static RouterService routerService;
	private static ModelService modelService;
	private static ObjectService objectService;

	private final static String nexSimFederateName = "NexSim";
	private final static String frascaFederateName = "Frasca";

	private Session session;
	private TemporaryQueue replyToQueue;
	private TemporaryQueue federationEventQueue;
	private TemporaryQueue dataPublicationQueue;
	private TemporaryQueue ownershipEventQueue;

	private String fedExecHandle = UUID.randomUUID().toString();
	private FederationExecutionModel fem;
	private TemporaryQueue timeMgmtEventQueue;
	private MessageConsumer timeMgmtEventQueueConsumer;
	private FileSystemXmlApplicationContext applicationContext;
	private static TemporaryQueue federateRequestEventQueue;

	private static MessageConsumer fedEventConsumer;
	private static MessageConsumer dataPublicationConsumer;

	private static Semaphore responseSem;
	private static Semaphore fedEventSem;
	private static Connection connection;

	/**
	 * 
	 * @param fem
	 */
	public FederationProtocolRun(final ConfigurationService configurationService,
			final Connection connection, final FederationExecutionModel fem) {
		FederationProtocolRun.configurationService = configurationService;
		FederationProtocolRun.connection = connection;
		this.fem = fem;
	}

	/**
	 * 
	 * @throws MuthurException
	 * @throws JMSException
	 */
	public void start() throws MuthurException, JMSException {

		applicationContext =
				new FileSystemXmlApplicationContext(
						"src/main/resources/META-INF/spring/federation-data-event-context.xml");

		// ******************************************

		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		federationEventQueue = session.createTemporaryQueue();

		fedEventConsumer = session.createConsumer(federationEventQueue);

		fedEventConsumer.setMessageListener(new FedEventConsumer());

		dataPublicationQueue = session.createTemporaryQueue();

		dataPublicationConsumer = session.createConsumer(dataPublicationQueue);

		dataPublicationConsumer
				.setMessageListener(new DataPublicationQueueListener());

		// ******************************************

		timeMgmtEventQueue = session.createTemporaryQueue();

		timeMgmtEventQueueConsumer = session.createConsumer(timeMgmtEventQueue);

		timeMgmtEventQueueConsumer.setMessageListener(new TimeEventQueueListener());

		// ******************************************

		replyToQueue = session.createTemporaryQueue();

		MessageConsumer replyQueueConsumer = session.createConsumer(replyToQueue);

		replyQueueConsumer.setMessageListener(new ResponseHandler());

		// ******************************************

		// create a temporary queue and attach a consumer for receiving ownership
		// events

		ownershipEventQueue = session.createTemporaryQueue();

		session.createConsumer(ownershipEventQueue).setMessageListener(
				new OwnershipEventHandler());

		// create a temporary queue for federate requests

		federateRequestEventQueue = session.createTemporaryQueue();

		// ***********

		routerService = new RouterServiceImpl();

		routerService.setConfigurationServer(configurationService);

		routerService.start();

		// ******************************************

		modelService = new ModelServiceImpl();

		modelService.start();

		objectService = new ObjectServiceImpl();

		// set the services into the object service impl

		objectService.setConfigurationService(configurationService);
		objectService.setModelService(modelService);

		// ******************************************

		TimeService ts = new TimeServiceImpl();

		ts.setConfigurationService(configurationService);

		ts.start();

		// ******************************************

		CommonsService commonsService = new CommonsServiceImpl();

		commonsService.start();

		// ******************************************

		fedEventSem = new Semaphore(0);

		FederationService fs = new FederationServiceImpl();
		assertNotNull(fs);

		// set the object service into the federation service
		//
		fs.setObjectService(objectService);

		// set the configuration service into the federation service
		//
		fs.setConfigurationService(configurationService);

		// set the router service into the federation service
		//
		fs.setRouterService(routerService);

		fs.setTimeService(ts);

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

		/**
		 * set the federation data channel server in the federation service
		 */
		fs.setFederationDataChannelServer(federationDataChannelServer);

		try {
			fs.start();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

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

		// re-initialize the semaphore that will be released when the responses from
		// Muthur are returned in the ResponseHandler

		responseSem = new Semaphore(0);

		try {
			fec.joinFederation(fep);
		} catch (MuthurException e3) {
			fail(e3.getLocalizedMessage());
		}

		// block on semaphore until available or timeout
		// releases are in the listener

		try {
			if (!responseSem.tryAcquire(2, 360, TimeUnit.SECONDS)) {
				fail("Timed out before all registration notifications received");
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

		DataSubscriptionRequest dsr = new DataSubscriptionRequest();
		dsr.setSourceOfEvent(nexSimFederateName);
		dsr.setFederationExecutionModelHandle(fem.getFedExecModelUUID());

		try {
			dsr.setSubscriptionQueueName(dataPublicationQueue.getQueueName());
		} catch (JMSException e) {
			fail(e.getLocalizedMessage());
		}

		// add the ownership event queue name
		try {
			dsr.setOwnershipEventQueueName(ownershipEventQueue.getQueueName());
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
					new SubscriptionRegistrationFederationExecutionEntry(fem, dsr,
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

		dsr = new DataSubscriptionRequest();
		dsr.setSourceOfEvent(frascaFederateName);
		dsr.setFederationExecutionModelHandle(fem.getFedExecModelUUID());

		try {
			dsr.setSubscriptionQueueName(dataPublicationQueue.getQueueName());
		} catch (JMSException e) {
			fail(e.getLocalizedMessage());
		}

		// add the ownership event queue name
		try {
			dsr.setOwnershipEventQueueName(ownershipEventQueue.getQueueName());
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
					new SubscriptionRegistrationFederationExecutionEntry(fem, dsr,
							txtMessage);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// re-initialize the semaphore that will be released when the responses from
		// Muthur are returned in the ResponseHandler
		responseSem = new Semaphore(0);

		try {
			fec.registerFederateSubscriptions(fedExecSub);
		} catch (MuthurException e3) {
			fail(e3.getLocalizedMessage());
		}

		// block on semaphore until all responses are received back in the
		// ResponseHandler

		try {
			if (!responseSem.tryAcquire(2, 30, TimeUnit.SECONDS)) {
				fail("Timed out before all subscription responses received");
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
		rrReq.setTimeManagementQueueName(timeMgmtEventQueue.getQueueName());
		rrReq.setFederateRequestQueueName(federateRequestEventQueue.getQueueName());

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
		rrReq.setTimeManagementQueueName(timeMgmtEventQueue.getQueueName());
		rrReq.setFederateRequestQueueName(federateRequestEventQueue.getQueueName());

		try {
			r2r = new ReadyToRunFederationExecutionEntry(fem, rrReq, txtMessage);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// re-initialize the semaphore that will be released when the responses from
		// Muthur are returned in the ResponseHandler
		responseSem = new Semaphore(0);

		// register the Frasca federate as ready to run

		try {
			fec.registerReadyToRun(r2r);
		} catch (MuthurException e1) {
			fail(e1.getLocalizedMessage());
		}

		// block on semaphore until all responses are received back in the
		// ResponseHandler

		try {
			if (!responseSem.tryAcquire(2, 30, TimeUnit.SECONDS)) {
				fail("Timed out before all ready to run responses received");
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
		// Muthur are returned in the FedEventConsumer
		fedEventSem = new Semaphore(0);

		try {
			fec.terminate();
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// block on the semaphore until all responses are received back in the
		// listener FedEventConsumer
		try {
			if (!fedEventSem.tryAcquire(2, 30, TimeUnit.SECONDS)) {
				fail("Timed out before all termination notifications received");
			} else {
				System.out
						.println("Acquired semaphore blocking on receiving termination events");
			}
		} catch (InterruptedException e2) {
			fail(e2.getLocalizedMessage());
		}

	}

	/**
	 * Consumes messages sent by the federation
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
					System.out
							.println("******* Received federation event message **********");

					fedEventSem.release();

				}
			}
		}
	}

	/**
	 * Consumes data published to the federation
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
	 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
	 * @version $Revision$
	 * 
	 */
	static class TimeEventQueueListener implements MessageListener {

		public void onMessage(Message message) {
			if (message != null) {
				TextMessage tm = (TextMessage) message;
				if (tm != null) {
					System.out.println("Time event received");
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

						responseSem.release();

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

}
