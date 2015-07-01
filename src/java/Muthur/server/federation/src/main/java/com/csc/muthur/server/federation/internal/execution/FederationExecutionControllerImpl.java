/**
 * 
 */
package com.csc.muthur.server.federation.internal.execution;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.FederationExecutionID;
import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.commons.MessageDestination;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.federation.FederationDataRequestQueue;
import com.csc.muthur.server.federation.FederationExecutionController;
import com.csc.muthur.server.federation.FederationService;
import com.csc.muthur.server.federation.FederationTimeToLiveThread;
import com.csc.muthur.server.federation.ReadyToRunRequestProcessor;
import com.csc.muthur.server.federation.SimulationExecutionMetricsAggregator;
import com.csc.muthur.server.model.FederationExecutionEntry;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.FederationState;
import com.csc.muthur.server.model.JoinFederationExecutionEntry;
import com.csc.muthur.server.model.ReadyToRunFederationExecutionEntry;
import com.csc.muthur.server.model.SubscriptionRegistrationFederationExecutionEntry;
import com.csc.muthur.server.model.event.DataPublicationEvent;
import com.csc.muthur.server.model.event.FederationTerminationEvent;
import com.csc.muthur.server.model.event.ObjectOwnershipRelinquishedEvent;
import com.csc.muthur.server.model.event.data.IBaseDataObject;
import com.csc.muthur.server.model.event.request.DataSubscriptionRequest;
import com.csc.muthur.server.model.event.request.RelinquishObjectOwnershipRequest;
import com.csc.muthur.server.object.FederationExecutionDataObjectContainer;
import com.csc.muthur.server.object.ObjectService;
import com.csc.muthur.server.time.FederateClockManager;
import com.csc.muthur.server.time.TimeService;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class FederationExecutionControllerImpl implements
		FederationExecutionController {

	public static final Logger LOG = LoggerFactory
			.getLogger(FederationExecutionControllerImpl.class.getName());

	private FederationState federationState = FederationState.INSTANTIATING;
	private Set<String> namesOfRequiredFederates = null;

	private UUID federationExecutionHandle;
	private FederationExecutionModel federationExecutionModel;

	private Thread joinFederationRequestProcessorInstance;
	private JoinFederationRequestProcessor joinFederationRequestProcessor;

	private Thread subscriptionRequestProcessorInstance;
	private SubscriptionRequestProcessor subscriptionRequestProcessor;

	private Thread readyToRunRequestProcessorInstance;
	private ReadyToRunRequestProcessor readyToRunRequestProcessor;

	private Thread startFederationRequestProcessorInstance;
	private StartFederationRequestProcessor startFederationRequestProcessor;

	private FederationTimeToLiveThread federationTTLThread;
	private ActiveMQConnectionFactory factory;

	private FederationService federationService;
	private Connection connection;

	private Session jmsSession;
	public FederateClockManager federateClockManager;

	private FederationExecutionDataObjectContainer federationExecutionDataObjectContainer;
	public SimulationExecutionMetricsAggregator simulationExecutionMetricsAggregator;

	private Map<FederationState, FederationState> launchProtocol =
			new ConcurrentHashMap<FederationState, FederationState>() {

				private static final long serialVersionUID = -4769497426996683262L;

				{
					put(FederationState.INSTANTIATED, FederationState.ACCEPTING_JOINS);
					put(FederationState.JOINED, FederationState.ACCEPTING_SUBSCRIPTIONS);
					put(FederationState.SUBSCRIBED,
							FederationState.ACCEPTING_READY_TO_RUN);
					put(FederationState.READY_TO_RUN, FederationState.STARTING);
				}
			};

	private String federateDataQueueName;
	private TemporaryQueue federateDataQueue;

	private FederationDataRequestQueue federationDataRequestQueue;
	private FederationDataPublicationQueue federationDataPublicationQueue;

	/**
	 * 
	 * @param federationService
	 * @param fem
	 * @throws MuthurException
	 */
	public FederationExecutionControllerImpl(
			final FederationService federationService,
			final FederationExecutionModel fem) throws MuthurException {
		super();

		if (fem == null) {
			throw new MuthurException("Federation Execution Model parameter was"
					+ " null in FederationExecutionControllerImpl ctor");
		}
		if (federationService == null) {
			throw new MuthurException("FederationService parameter was"
					+ " null in FederationExecutionControllerImpl ctor");
		}

		this.federationExecutionModel = fem;
		this.federationService = federationService;

		try {
			factory =
					new ActiveMQConnectionFactory(federationService
							.getConfigurationService().getMessagingConnectionUrl());

			connection = factory.createConnection();

			connection.start();

			jmsSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		} catch (Exception e) {
			LOG.error("Error creating FederationExecutionControlImpl");
			throw new MuthurException(e);
		}

		// get the parameters from the federation execution model

		namesOfRequiredFederates = fem.getNamesOfRequiredFederates();

		int numberOfRequiredFederates = fem.getNamesOfRequiredFederates().size();

		LOG.debug("[" + numberOfRequiredFederates
				+ "] federates are required for [" + fem.getName() + "] federation.");

		federationExecutionHandle = UUID.randomUUID();

		// create the time manager

		TimeService ts = getTimeService();

		if (ts == null) {
			throw new MuthurException(
					"Time service in federation execution controller"
							+ " was null. Unable to provide essential time management function");
		}

		federateClockManager =
				ts.createFederateClockManager(getFederationExecutionModel());

		// create the ttl thread to ensure the federation lives only as long as
		// designated in the FEM

		federationTTLThread =
				new FederationTimeToLiveThread(federationService, this);

		new Thread(federationTTLThread).start();

		ObjectService objectService = federationService.getObjectService();

		if (objectService == null) {
			throw new MuthurException(
					"Object service in federation execution controller"
							+ " was null. Unable to provide essential object management functionality");
		}

		if (federationService.getCommonsService() == null) {
			throw new MuthurException(
					"Commons service in federation execution controller was null.");
		}

		FederationExecutionID federationExecutionID =
				federationService.getCommonsService().createFederationExecutionID(
						federationExecutionHandle.toString(),
						getFederationExecutionModel().getFedExecModelUUID());

		federationExecutionDataObjectContainer =
				objectService
						.createFederationDataObjectContainer(federationExecutionID);

		if (federationExecutionDataObjectContainer == null) {
			throw new MuthurException(
					"Unable to create data object container in the DataObject service. "
							+ "Unable to provide essential object management functionality");
		}

		simulationExecutionMetricsAggregator =
				new SimulationExecutionMetricsAggregatorImpl(federationExecutionModel);

		simulationExecutionMetricsAggregator.start();

		federationDataRequestQueue = new FederationDataRequestQueueImpl();
		federationDataRequestQueue.setRouterService(federationService
				.getRouterService());

		federationDataPublicationQueue =
				new FederationDataPublicationQueueImpl(this);

		// initiate the join request processor to begin receiving join requests

		startJoinRequestProcessor();
	}

	/**
	 * @throws MuthurException
	 * 
	 */
	private void startJoinRequestProcessor() throws MuthurException {

		joinFederationRequestProcessor = new JoinFederationRequestProcessor(this);

		Thread joinFederationRequestProcessorInstance =
				new Thread(joinFederationRequestProcessor);

		joinFederationRequestProcessorInstance.start();

		joinFederationRequestProcessor.waitTillStarted();

	}

	/**
	 * 
	 * @throws MuthurException
	 */
	public void startSubscriptionRequestProcessor() throws MuthurException {

		subscriptionRequestProcessor = new SubscriptionRequestProcessor(this);

		Thread subscriptionRequestProcessorInstance =
				new Thread(subscriptionRequestProcessor);

		subscriptionRequestProcessorInstance.start();

		subscriptionRequestProcessor.waitTillStarted();

	}

	/**
	 * 
	 * @throws MuthurException
	 */
	private void startReadyToRunRequestProcessor() throws MuthurException {

		readyToRunRequestProcessor = new ReadyToRunRequestProcessor(this);

		Thread readyToRunRequestProcessorInstance =
				new Thread(readyToRunRequestProcessor);

		readyToRunRequestProcessorInstance.start();

		readyToRunRequestProcessor.waitTillStarted();
	}

	/**
	 * 
	 * @throws MuthurException
	 */
	private void startStartFederationRequestProcessor() throws MuthurException {

		startFederationRequestProcessor = new StartFederationRequestProcessor(this);

		startFederationRequestProcessorInstance =
				new Thread(startFederationRequestProcessor);

		startFederationRequestProcessorInstance.start();

		startFederationRequestProcessor.waitTillStarted();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.federation.internal.Bogus#registerParticipant(com
	 * .csc. muthur .federation.FederationExecutionParticipant)
	 */
	public void joinFederation(final JoinFederationExecutionEntry jfee)
			throws MuthurException {

		/*
		 * Validate the JoinFederationExexcutionEntry
		 */
		if (jfee == null) {
			throw new MuthurException("JoinFederationExecutionEntry is null");
		}
		if (jfee.getFederateName() == null) {
			throw new MuthurException("JoinFederationExecutionEntry name is null");
		}
		if ("".equals(jfee.getFederateName())) {
			throw new MuthurException("JoinFederationExecutionEntry name is empty");
		}

		/**
		 * Check if the federate has already joined the federation.
		 */
		if (joinFederationRequestProcessor
				.getFederateNameToFederationExecutionParticipant().containsKey(
						jfee.getFederateName())) {
			throw new MuthurException("Federate [" + jfee.getFederateName()
					+ "] attempted to join the federation ["
					+ federationExecutionModel.getName()
					+ "] which it has already joined");
		}

		/**
		 * Check that the federation is in a state in which it could be joined.
		 */
		if (!isInJoinableState()) {
			throw new MuthurException("Federate [" + jfee.getFederateName()
					+ "] attempted to join a federation ["
					+ federationExecutionModel.getName() + "] in ["
					+ getExecutionState().toString() + "]");
		}

		LOG.debug("Joining federate [" + jfee.getFederateName()
				+ "] to federation [" + federationExecutionModel.getName() + "]");

		joinFederationRequestProcessor.addEntry(jfee);

		LOG.info("Federate [" + jfee.getFederateName() + "] joined federation ["
				+ federationExecutionModel.getName() + "]");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.csc.muthur.federation.FederationExecutionController#
	 * registerFederateSubscriptions
	 * (com.csc.muthur.model.SubscriptionRegistrationFederationExecutionEntry)
	 */
	public void registerFederateSubscriptions(
			final SubscriptionRegistrationFederationExecutionEntry fedExecSub)
			throws MuthurException {

		String errorTextPrefix = "Error registering federate subscriptions. ";

		// if this federate is participating and the state of the federation
		// execution is accepting subscriptions then...
		// add the federate to the name of required subscribers and decrement
		// the count down latch for the required number of federates registering
		// subscriptions

		if (fedExecSub == null) {
			throw new MuthurException(errorTextPrefix
					+ "FederateExecutionSubscription was null");
		}

		if ("".equals(fedExecSub.getFederateName())) {
			throw new MuthurException(errorTextPrefix + "Federate name was null");
		}

		// check if the federation is accepting subscriptions
		//

		if (!isInAcceptingSubscriptionsState()) {

			throw new MuthurException("Federate [" + fedExecSub.getFederateName()
					+ "] sent a register subscriptions request to the federation ["
					+ federationExecutionModel.getName() + "] which is ["
					+ getExecutionState().toString() + "]");
		}

		LOG.debug("Registering subscriptions for federate ["
				+ fedExecSub.getFederateName() + "] for federation ["
				+ federationExecutionModel.getName() + "]...");

		String federateName = fedExecSub.getFederateName();

		DataSubscriptionRequest dataSubscriptionRequest =
				(DataSubscriptionRequest) fedExecSub.getEvent();

		Set<String> dataTypeNames = dataSubscriptionRequest.getDataSubscriptions();

		if (dataTypeNames == null) {
			LOG.warn("List of data types was null when registering subscriptions for federate ["
					+ federateName + "]");
		} else {
			LOG.debug("Federate [" + federateName + "] subscribed to "
					+ dataTypeNames);
		}

		/**
		 * Create the producer for the the data publication event queue
		 */

		String dataEventQueueName = fedExecSub.getDataEventQueueName();

		if ((dataEventQueueName == null)
				|| ("".equalsIgnoreCase(dataEventQueueName))) {
			throw new MuthurException("Data event queue name is null or empty.");
		}

		LOG.debug("Creating data event queue [" + dataEventQueueName + "] for ["
				+ federateName + "]");

		Destination dataEventQueue;

		MessageProducer publisher = null;

		try {

			dataEventQueue = jmsSession.createQueue(dataEventQueueName);

			publisher = jmsSession.createProducer(dataEventQueue);

			publisher.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

		} catch (JMSException e) {
			throw new MuthurException(e);
		}

		fedExecSub.setDataEventQueue(dataEventQueue);
		fedExecSub.setDataPublicationEventProducer(publisher);

		/**
		 * Create the producer for the the ownership event queue
		 */

		String ownershipEventQueueName =
				dataSubscriptionRequest.getOwnershipEventQueueName();

		if ((ownershipEventQueueName == null)
				|| ("".equalsIgnoreCase(ownershipEventQueueName))) {
			throw new MuthurException("Ownership event queue name is null or empty.");
		}

		LOG.debug("Creating ownership event queue [" + ownershipEventQueueName
				+ "] for [" + federateName + "]");

		Destination ownershipEventQueue = null;

		publisher = null;

		try {

			ownershipEventQueue = jmsSession.createQueue(ownershipEventQueueName);

			publisher = jmsSession.createProducer(ownershipEventQueue);

			publisher.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

		} catch (JMSException e) {
			throw new MuthurException(e);
		}

		fedExecSub.setOwnershipEventQueue(ownershipEventQueue);
		fedExecSub.setOwnershipEventProducer(publisher);

		LOG.debug("Registering subscriptions " + dataTypeNames + " for federate ["
				+ fedExecSub.getFederateName() + "] for federation ["
				+ federationExecutionModel.getName() + "]...");

		subscriptionRequestProcessor.addEntry(fedExecSub);

		LOG.info("Registered subscriptions " + dataTypeNames + " for federate ["
				+ fedExecSub.getFederateName() + "] in federation ["
				+ federationExecutionModel.getName() + "].");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.federation.FederationExecutionController#
	 * registerReadyToRun
	 * (com.csc.muthur.model.ReadyToRunFederationExecutionEntry)
	 */
	public void registerReadyToRun(
			final ReadyToRunFederationExecutionEntry fedExecReadyToRun)
			throws MuthurException {

		if (fedExecReadyToRun == null) {
			throw new MuthurException(
					"ReadyToRunFederationExecutionEntry object was null. "
							+ "Unable to register ready to run status.");
		}

		String federateName = fedExecReadyToRun.getFederateName();

		if (federateName == null) {
			throw new MuthurException(
					"Federate name in ReadyToRunFederationExecutionEntry was null. "
							+ "Unable to register ready to run status.");
		}

		if (!IsInAcceptingReadyToRun()) {

			throw new MuthurException("Federate ["
					+ fedExecReadyToRun.getFederateName()
					+ "] sent a ready to run request to a federation ["
					+ federationExecutionModel.getName() + "] in ["
					+ getExecutionState().toString() + "]");

		}

		LOG.debug("Federate [" + fedExecReadyToRun.getFederateName()
				+ "] has indicated ready to run state for federation ["
				+ federationExecutionModel.getName() + "]");

		readyToRunRequestProcessor.addEntry(fedExecReadyToRun);

		LOG.info("Federate [" + fedExecReadyToRun.getFederateName()
				+ "] registered as ready to run for federation ["
				+ federationExecutionModel.getName() + "]");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.federation.FederationExecutionController#startExecution
	 * ()
	 */
	public void startExecution() throws MuthurException {

		if (federateClockManager != null) {

			LOG.debug("Starting execution of federation ["
					+ getFederationExecutionModel().getName() + "].");

			federateClockManager.start();

			setExecutionState(FederationState.RUNNING);

		} else {

			LOG.warn("Federate clock manager was null for ["
					+ getFederationExecutionModel().getName()
					+ "] federation. Unable to provide time management to federates.");
		}

		if (startFederationRequestProcessor != null) {
			startFederationRequestProcessor.release();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.federation.FederationExecutionController#pauseExecution
	 * ()
	 */
	public void pauseExecution() throws MuthurException {

		if (federateClockManager != null) {

			LOG.info("Pausing execution of federation ["
					+ getFederationExecutionModel().getName() + "].");

			federateClockManager.pause();

			setExecutionState(FederationState.PAUSED);

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.federation.FederationExecutionController#
	 * updateFederationTime (long)
	 */
	@Override
	public void updateFederationTime(long federationExecutionTimeMSecs) {

		if (federateClockManager != null) {

			LOG.info("Setting federation [" + getFederationExecutionModel().getName()
					+ "] execution time to [" + federationExecutionTimeMSecs + "]");

			federateClockManager.setClock(federationExecutionTimeMSecs);

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.federation.FederationExecutionController#resumeExecution
	 * ()
	 */
	@Override
	public void resumeExecution() throws MuthurException {

		if (federateClockManager != null) {

			LOG.debug("Resuming execution of federation ["
					+ getFederationExecutionModel().getName() + "].");

			federateClockManager.resume();

			setExecutionState(FederationState.RUNNING);

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.federation.FederationExecutionController#publishData
	 * (java .lang.String, java.lang.String)
	 */
	public void publishData(final DataPublicationEvent dataPublicationEvent)
			throws MuthurException {

		if ((dataPublicationEvent.getEventType() != null)
				&& (dataPublicationEvent.serialize() != null)) {

			LOG.debug("Received [" + dataPublicationEvent.getDataAction()
					+ "] data event [" + dataPublicationEvent.getDataType().toString()
					+ "] by federate [" + dataPublicationEvent.getSourceOfEvent() + "]");

			if (subscriptionRequestProcessor == null) {
				throw new MuthurException("Subscription processor is null in the "
						+ "federation execution controller of federation ["
						+ getFederationExecutionModel().getName() + "]");
			}

			for (SubscriptionRegistrationFederationExecutionEntry fedExecSub : subscriptionRequestProcessor
					.getFederateNameToFederationExecutionSubscription().values()) {

				if (fedExecSub != null) {

					LOG.debug("Checking subscriptions for ["
							+ fedExecSub.getFederateName() + "] to ["
							+ dataPublicationEvent.getDataType().toString()
							+ "] published by [" + dataPublicationEvent.getSourceOfEvent()
							+ "]...");

					// don't publish to the creator of the object

					if (fedExecSub.getFederateName().equalsIgnoreCase(
							dataPublicationEvent.getSourceOfEvent())) {

						LOG.debug("Not sending ["
								+ dataPublicationEvent.getDataType().toString() + "] back to ["
								+ dataPublicationEvent.getSourceOfEvent()
								+ "] since data event initiated by ["
								+ dataPublicationEvent.getSourceOfEvent() + "]");

					} else {

						// iterate over the data type names in the subscription request and
						// determine if there's a match on the data type name

						IEvent iEvent = fedExecSub.getEvent();

						if ((iEvent != null) && (iEvent instanceof DataSubscriptionRequest)) {

							DataSubscriptionRequest dataSubscriptionRequest =
									(DataSubscriptionRequest) iEvent;

							String subscriber = dataSubscriptionRequest.getSourceOfEvent();
							Set<String> dataTypes =
									dataSubscriptionRequest.getDataSubscriptions();

							LOG.debug("Data event initiator ["
									+ dataPublicationEvent.getSourceOfEvent() + "]; subscriber ["
									+ subscriber + "]; subscriptions " + dataTypes);

							for (String dataSubscription : dataTypes) {

								if (dataPublicationEvent.getDataType().toString()
										.equalsIgnoreCase(dataSubscription)) {

									LOG.debug("Match found for subscription [" + dataSubscription
											+ "] to ["
											+ dataPublicationEvent.getDataType().toString()
											+ "] for federate [" + fedExecSub.getFederateName() + "]");

									TextMessage tm = null;

									try {
										tm = jmsSession.createTextMessage();

										LOG.debug("Serializing data publication event...");

										tm.setText(dataPublicationEvent.serialize());

										LOG.debug("Sending [" + dataSubscription + "] to ["
												+ subscriber + "]...");

										fedExecSub.getDataPublicationEventProducer().send(tm);

									} catch (JMSException e) {
										throw new MuthurException(e);
									}

									LOG.debug("Sent [" + dataSubscription + "] to [" + subscriber
											+ "]");

									simulationExecutionMetricsAggregator
											.addRoutedDataEvent(dataPublicationEvent);

								}
							}
						}

						LOG.debug("Completed check of subscriptions for ["
								+ fedExecSub.getFederateName() + "] to ["
								+ dataPublicationEvent.getDataType().toString()
								+ "] published by [" + dataPublicationEvent.getSourceOfEvent()
								+ "]");

					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.csc.muthur.federation.FederationExecutionController#
	 * publishOwnershipRelinquished
	 * (com.csc.muthur.model.event.data.IBaseDataObject)
	 */
	@Override
	public void
			publishOwnershipRelinquished(
					final RelinquishObjectOwnershipRequest request,
					final IBaseDataObject bdo) throws MuthurException {

		if (bdo != null) {

			LOG.debug("Publishing owner relinquished event of ["
					+ bdo.getDataType().toString() + "] by federate ["
					+ request.getSourceOfEvent() + "]");

			if (subscriptionRequestProcessor == null) {
				throw new MuthurException("Subscription processor is null in the "
						+ "federation execution controller of federation ["
						+ getFederationExecutionModel().getName() + "]");
			}

			for (SubscriptionRegistrationFederationExecutionEntry fedExecSub : subscriptionRequestProcessor
					.getFederateNameToFederationExecutionSubscription().values()) {

				if (fedExecSub != null) {

					LOG.debug("Checking subscriptions for ["
							+ fedExecSub.getFederateName() + "] to ["
							+ bdo.getDataType().toString() + "] published by ["
							+ request.getSourceOfEvent() + "]...");

					// don't publish to the creator of the object

					if (fedExecSub.getFederateName().equalsIgnoreCase(
							request.getSourceOfEvent())) {

						LOG.debug("Not publishing to initiator of the ownership event ["
								+ request.getSourceOfEvent() + "]");

					} else {

						// iterate over the data type names in the subscription request and
						// determine if there's a match on the data type name

						IEvent iEvent = fedExecSub.getEvent();

						if ((iEvent != null) && (iEvent instanceof DataSubscriptionRequest)) {

							DataSubscriptionRequest dataSubscriptionRequest =
									(DataSubscriptionRequest) iEvent;

							String subscriber = dataSubscriptionRequest.getSourceOfEvent();
							Set<String> dataTypes =
									dataSubscriptionRequest.getDataSubscriptions();

							LOG.debug("Data event initiator [" + request.getSourceOfEvent()
									+ "]; subscriber [" + subscriber + "]; subscriptions "
									+ dataTypes);

							for (String dataSubscription : dataTypes) {

								if (bdo.getDataType().toString()
										.equalsIgnoreCase(dataSubscription)) {

									LOG.debug("Match found for subscription [" + dataSubscription
											+ "] to [" + bdo.getDataType().toString()
											+ "] for federate [" + fedExecSub.getFederateName() + "]");

									TextMessage tm = null;

									try {

										ObjectOwnershipRelinquishedEvent objectOwnershipRelinquishedEvent =
												new ObjectOwnershipRelinquishedEvent();

										objectOwnershipRelinquishedEvent.initialization(request
												.serialize());

										objectOwnershipRelinquishedEvent.setDataObjectUUID(bdo
												.getDataObjectUUID());

										tm = jmsSession.createTextMessage();

										tm.setText(objectOwnershipRelinquishedEvent.serialize());

										fedExecSub.getOwnershipEventProducer().send(tm);

									} catch (JMSException e) {
										throw new MuthurException(e);
									}

									LOG.debug("Sent [" + dataSubscription + "] to [" + subscriber
											+ "]");
								}
							}
						}

						LOG.debug("Completed check of subscriptions for ["
								+ fedExecSub.getFederateName() + "] to ["
								+ bdo.getDataType().toString() + "] published by ["
								+ request.getSourceOfEvent() + "]");

					}
				}
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.federation.FederationExecutionController#terminate()
	 */
	@Override
	public void terminate() throws MuthurException {

		if (federationDataRequestQueue != null) {
			federationDataRequestQueue.terminate();
		}

		if (federationDataPublicationQueue != null) {
			federationDataPublicationQueue.terminate();
		}

		TimeService ts = getTimeService();

		if (null != ts) {
			ts.destroyFederateClockManager(getFederationExecutionModel());
		}

		// get the time to live thread and release it to send out the
		// notifications to the participating federates
		//

		if (null != federationTTLThread) {
			federationTTLThread.release();
		}

		if (simulationExecutionMetricsAggregator != null) {
			LOG.debug("Metrics for execution of federation using model ["
					+ getFederationExecutionModel().getName()
					+ "] - "
					+ simulationExecutionMetricsAggregator.getSimulationMetrics()
							.toString() + "]");
			simulationExecutionMetricsAggregator.stop();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.federation.FederationExecutionController#
	 * sendTerminateNotifications(java.lang.String)
	 */
	public void sendTerminateNotifications(final String terminationReason)
			throws MuthurException {

		LOG.debug("Terminating federation [" + federationExecutionModel.getName()
				+ "]...");

		Iterator<String> iter = namesOfRequiredFederates.iterator();

		if (iter != null) {

			while (iter.hasNext()) {

				String federateName = iter.next();

				JoinFederationExecutionEntry fedExecParticipant =
						joinFederationRequestProcessor
								.getFederateNameToFederationExecutionParticipant().get(
										federateName);

				if (fedExecParticipant != null) {

					String federationEventQueueName =
							fedExecParticipant.getFederationEventQueueName();

					if ((federationEventQueueName == null)
							|| ("".equalsIgnoreCase(federationEventQueueName))) {

						LOG.warn("Federate event queue name null for federate ["
								+ fedExecParticipant.getFederateName() + "]");
						LOG.warn("Unable to send termination to federate ["
								+ fedExecParticipant.getFederateName() + "] for federation["
								+ federationExecutionModel.getName() + "]");

					} else {

						LOG.debug("Sending termination notification to federate ["
								+ fedExecParticipant.getFederateName() + "]...");

						FederationTerminationEvent fte = new FederationTerminationEvent();

						fte.setSourceOfEvent(MessageDestination.MUTHUR);

						fte.setTerminationReason(terminationReason);

						fte.setFederationExecutionHandle(federationExecutionHandle
								.toString());

						try {

							federationService.getRouterService().queue(fte.serialize(),
									federationEventQueueName);
							LOG.info("Termination notification sent to federate ["
									+ fedExecParticipant.getFederateName() + "]");

						} catch (MuthurException je) {
							LOG.debug(je.getLocalizedMessage());
						}

					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.federation.internal.Bogus#getFederationExecutionHandle
	 * ()
	 */
	public final String getFederationExecutionHandle() {
		return federationExecutionHandle.toString();
	}

	/**
	 * 
	 * @param newState
	 * @return
	 */
	public void setExecutionState(final FederationState newState) {
		federationState = newState;
	}

	/**
	 * 
	 * INSTANTIATED -> ACCEPTING_JOINS
	 * 
	 * JOINED -> ACCEPTING_SUBSCRIPTIONS
	 * 
	 * SUBSCRIBED -> ACCEPTING_READY_TO_RUN
	 * 
	 * READY_TO_RUN -> STARTED
	 * 
	 * @param currentState
	 * @throws MuthurException
	 */
	public void advanceBootProcess(final FederationState currentState)
			throws MuthurException {

		boolean bootProcessTransitioned = false;

		FederationState nextState = getLaunchProtocol().get(currentState);

		if (nextState != null) {

			switch (nextState) {

			case ACCEPTING_SUBSCRIPTIONS:

				startSubscriptionRequestProcessor();

				bootProcessTransitioned = true;

				break;

			case ACCEPTING_READY_TO_RUN:

				startReadyToRunRequestProcessor();

				bootProcessTransitioned = true;

				break;

			case STARTING:

				startStartFederationRequestProcessor();

				bootProcessTransitioned = true;

				break;

			default:
				LOG.warn("Unknown state transition specified");
			}

			if (bootProcessTransitioned) {
				LOG.info("Transitioned boot process for ["
						+ getFederationExecutionModel().getName() + "] to ["
						+ nextState.toString() + "].");

				// set the new state of the federation execution

				federationState = nextState;

			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.federation.internal.Bogus#getExecutionState()
	 */
	public final FederationState getExecutionState() {
		return federationState;
	}

	/**
	 * 
	 */
	public final boolean isInJoinableState() {
		boolean joinable = false;

		if (federationIsRunning()) {
			joinable = true;
		} else if ((joinFederationRequestProcessor != null)
				&& (federationState.equals(FederationState.ACCEPTING_JOINS))) {
			joinable = true;
		}

		return joinable;
	}

	/**
	 * 
	 * @return
	 */
	public final boolean joinComplete() {

		boolean status = false;

		if ((joinFederationRequestProcessor != null)
				&& (joinFederationRequestProcessor.isSatisfied())) {
			status = true;
		}

		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.csc.muthur.federation.FederationExecutionController#
	 * isInAcceptingSubscriptionsState()
	 */
	@Override
	public boolean isInAcceptingSubscriptionsState() {

		boolean isInAcceptingSubState = false;

		if (federationIsRunning()) {
			isInAcceptingSubState = true;
		} else if (joinComplete()
				&& (federationState.equals(FederationState.ACCEPTING_SUBSCRIPTIONS))) {
			isInAcceptingSubState = true;
		}

		return isInAcceptingSubState;
	}

	/**
	 * 
	 * @return
	 */
	public final boolean registeringSubscriptionsComplete() {

		boolean status = false;

		if ((subscriptionRequestProcessor != null)
				&& (subscriptionRequestProcessor.isSatisfied())) {
			status = true;
		}

		return status;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.csc.muthur.federation.FederationExecutionController#
	 * isInAcceptingReadyToRun ()
	 */
	@Override
	public boolean IsInAcceptingReadyToRun() {

		boolean isInReadyToRunState = false;

		if (federationIsRunning()) {
			isInReadyToRunState = true;
		} else if (joinComplete() && registeringSubscriptionsComplete()
				&& (federationState.equals(FederationState.ACCEPTING_READY_TO_RUN))) {
			isInReadyToRunState = true;
		}

		return isInReadyToRunState;
	}

	/**
	 * 
	 * @return
	 */
	public final boolean acceptingReadyToRunComplete() {

		boolean status = false;

		if ((readyToRunRequestProcessor != null)
				&& (readyToRunRequestProcessor.isSatisfied())) {
			status = true;
		}

		return status;
	}

	/**
	 * 
	 * @return
	 */
	public boolean federationIsRunning() {
		return (federationState.equals(FederationState.STARTING)
				|| federationState.equals(FederationState.STARTED)
				|| federationState.equals(FederationState.PAUSING)
				|| federationState.equals(FederationState.PAUSED) || federationState
					.equals(FederationState.RUNNING));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.federation.FederationExecutionController#
	 * cleanUpConnections ()
	 */
	public void cleanUpConnections() {

		LOG.debug("Closing connections for federation ["
				+ federationExecutionModel.getName() + "]...");

		if (jmsSession != null) {
			try {
				jmsSession.close();
			} catch (JMSException e) {
				LOG.warn("Error closing jmsSession for federation ["
						+ federationExecutionModel.getName() + "]");
				LOG.warn(e.getLocalizedMessage());
			}
		}

		if (connection != null) {
			try {
				connection.stop();
			} catch (JMSException e) {
				LOG.warn("Error stopping connection for federation ["
						+ federationExecutionModel.getName() + "]");
				LOG.warn(e.getLocalizedMessage());
			}

			try {
				connection.close();
			} catch (JMSException e) {
				LOG.warn("Error closing connection for federation ["
						+ federationExecutionModel.getName() + "]");
				LOG.warn(e.getLocalizedMessage());
			}

		}

		LOG.info("Connections closed for federation ["
				+ federationExecutionModel.getName() + "].");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.federation.FederationExecutionController#
	 * getFederateNameToFederationExecutionParticipant()
	 */
	public final Map<String, JoinFederationExecutionEntry>
			getFederateNameToFederationExecutionParticipant() {
		return joinFederationRequestProcessor
				.getFederateNameToFederationExecutionParticipant();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.federation.FederationExecutionController#
	 * setFederateNameToFederationExecutionParticipant(java.util.Map)
	 */
	public final
			void
			setFederateNameToFederationExecutionParticipant(
					Map<String, JoinFederationExecutionEntry> federateNameToFederationExecutionParticipant) {
		joinFederationRequestProcessor
				.getFederateNameToFederationExecutionParticipant();
	}

	/**
	 * @return the federationService
	 */
	public final FederationService getFederationService() {
		return federationService;
	}

	/**
	 * @return the federationTTLThread
	 */
	public final FederationTimeToLiveThread getFederationTTLThread() {
		return federationTTLThread;
	}

	/**
	 * @param fee
	 * @param response
	 * @param errMsg
	 * @throws MuthurException
	 */
	public void returnErrorResponse(FederationExecutionEntry fee,
			IEvent response, String errMsg) throws MuthurException {

		response.initialization(fee.getEvent().serialize());
		response.setSourceOfEvent(MessageDestination.MUTHUR);

		response.setSuccess(false);
		response.setErrorDescription(errMsg);

		LOG.debug("Sending [" + response.getEventName() + "] to ["
				+ fee.getFederateName() + "]..");

		try {
			federationService.getRouterService().queue(response.serialize(),
					fee.getMessage().getJMSReplyTo(),
					fee.getMessage().getJMSCorrelationID());
		} catch (JMSException e) {
			throw new MuthurException(e);
		}

		LOG.info("Error on event type [" + response.getEventName()
				+ "] returned to [" + fee.getFederateName() + "].");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.federation.FederationExecutionController#getTimeService
	 * ()
	 */
	@Override
	public final TimeService getTimeService() {

		TimeService ts = null;

		FederationService fs = getFederationService();

		if (fs != null) {
			ts = fs.getTimeService();
		}

		return ts;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.csc.muthur.federation.FederationExecutionController#
	 * getFederationExecutionModel()
	 */
	public final FederationExecutionModel getFederationExecutionModel() {
		return federationExecutionModel;
	}

	/**
	 * @return the joinFederationRequestProcessorInstance
	 */
	public final Thread getJoinFederationRequestProcessorInstance() {
		return joinFederationRequestProcessorInstance;
	}

	/**
	 * @return the subscriptionRequestProcessorInstance
	 */
	public final Thread getSubscriptionRequestProcessorInstance() {
		return subscriptionRequestProcessorInstance;
	}

	/**
	 * @return the readyToRunRequestProcessorInstance
	 */
	public final Thread getReadyToRunRequestProcessorInstance() {
		return readyToRunRequestProcessorInstance;
	}

	/**
	 * @return the readyToRunRequestProcessor
	 */
	@Override
	public ReadyToRunRequestProcessor getReadyToRunRequestProcessor() {
		return readyToRunRequestProcessor;
	}

	/**
	 * @return the launchProtocol
	 */
	public Map<FederationState, FederationState> getLaunchProtocol() {
		return launchProtocol;
	}

	public FederateClockManager getFederateClockManager() {
		return federateClockManager;
	}

	/**
	 * @return the federationExecutionDataObjectContainer
	 */
	public final FederationExecutionDataObjectContainer
			getFederationExecutionDataObjectContainer() {
		return federationExecutionDataObjectContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.federation.FederationExecutionController#
	 * getSimulationExecutionMetrics()
	 */
	@Override
	public SimulationExecutionMetricsAggregator getSimulationExecutionMetrics() {
		return simulationExecutionMetricsAggregator;
	}

	/**
	 * @return the connection
	 */
	public Connection getConnection() {
		return connection;
	}

	@Override
	public String getFederateDataTopicName() {
		return federateDataQueueName;
	}

	@Override
	public void setFederateDataQueueName(final String federateDataQueueName) {
		this.federateDataQueueName = federateDataQueueName;
	}

	@Override
	public TemporaryQueue getFederateDataQueue() {
		return federateDataQueue;
	}

	/**
	 * @param federateDataQueue
	 *          the federateDataQueue to set
	 */
	@Override
	public void setFederateDataTopic(TemporaryQueue federateDataQueue) {
		this.federateDataQueue = federateDataQueue;
	}

	/**
	 * @return the federationDataRequestQueue
	 */
	@Override
	public FederationDataRequestQueue getFederationDataRequestQueue() {
		return federationDataRequestQueue;
	}

	/**
	 * @param federationDataRequestQueue
	 *          the federationDataRequestQueue to set
	 */
	@Override
	public void setFederationDataRequestQueue(
			FederationDataRequestQueue federationDataRequestQueue) {
		this.federationDataRequestQueue = federationDataRequestQueue;
	}

	/**
	 * @return the federationDataPublicationQueue
	 */
	@Override
	public FederationDataPublicationQueue getFederationDataPublicationQueue() {
		return federationDataPublicationQueue;
	}

	/**
	 * @param federationDataPublicationQueue
	 *          the federationDataPublicationQueue to set
	 */
	public void setFederationDataPublicationQueue(
			FederationDataPublicationQueue federationDataPublicationQueue) {
		this.federationDataPublicationQueue = federationDataPublicationQueue;
	}

}
