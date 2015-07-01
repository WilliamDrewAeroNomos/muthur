/**
 * 
 */
package com.csc.muthur.server.federation.internal.execution;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atcloud.fem.FederationExecutionModelService;
import com.atcloud.metrics.MetricsService;
import com.csc.muthur.server.commons.CommonsService;
import com.csc.muthur.server.commons.MessageDestination;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.configuration.ConfigurationService;
import com.csc.muthur.server.federation.FederationDataChannelServer;
import com.csc.muthur.server.federation.FederationExecutionController;
import com.csc.muthur.server.federation.FederationService;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.FederationExecutionTermination;
import com.csc.muthur.server.model.FederationState;
import com.csc.muthur.server.model.JoinFederationExecutionEntry;
import com.csc.muthur.server.model.ModelService;
import com.csc.muthur.server.model.ReadyToRunFederationExecutionEntry;
import com.csc.muthur.server.model.SubscriptionRegistrationFederationExecutionEntry;
import com.csc.muthur.server.object.ObjectService;
import com.csc.muthur.server.registration.RegistrationService;
import com.csc.muthur.server.router.RouterService;
import com.csc.muthur.server.time.TimeService;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class FederationServiceImpl implements FederationService {

	private static final Logger LOG = LoggerFactory
			.getLogger(FederationServiceImpl.class.getName());

	private Map<FederationExecutionModel, FederationExecutionController> fedExecModelToFedExecCtrlrMap =
			new ConcurrentHashMap<FederationExecutionModel, FederationExecutionController>();

	private RegistrationService registrationService;
	private ConfigurationService configurationService;
	private RouterService routerService;

	private ModelService modelService;
	private TimeService timeService;
	private ObjectService objectService;

	private CommonsService commonsService;
	private MetricsService metricsService;
	private FederationExecutionModelService federationExecutionModelService;

	private Thread federationExecutionReaperThread;
	private FederationExecutionReaper federationExecutionReaper;
	private Semaphore federationExecutionControllerMapSem = new Semaphore(1);

	@SuppressWarnings("unused")
	private Destination eventQueue = null;
	private static Session session = null;
	private Connection connection = null;

	private static Destination federateStateQueue = null;
	private MessageConsumer federateStateQueueConsumer;
	private ExecutorService federationDataChanneServerlPool = Executors
			.newCachedThreadPool();

	private FederationDataChannelServer federationDataChannelServer;
	private int dataChannelServerPort = 44444;
	private String dataChannelServerHostName = "localhost";

	/**
	 * 
	 */
	public FederationServiceImpl() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.federation.FederationService#start()
	 */
	@Override
	public void start() throws Exception {

		LOG.info("Starting federation service...");

		federationExecutionReaper = new FederationExecutionReaper();

		federationExecutionReaperThread = new Thread(federationExecutionReaper);

		federationExecutionReaperThread.start();

		LOG.debug("Connecting to the event queue on Muthur...");

		String configurationUrl = configurationService.getMessagingConnectionUrl();

		// assumes that the ActiveMQ bundle is already started
		//
		ActiveMQConnectionFactory connectionFactory =
				new ActiveMQConnectionFactory(configurationUrl);

		connection = connectionFactory.createConnection();

		connection.start();

		LOG.debug("Established connection to message broker at ["
				+ configurationUrl + "]");

		// create session
		//
		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		eventQueue =
				session.createQueue(MessageDestination
						.getFederationEventQueuePropName());

		// create the federate state queue

		federateStateQueue =
				session.createQueue(MessageDestination.getFederateStateQueueName());

		// create the consumer for the federate state queue

		LOG.debug("Starting Federate state queue consumer...");

		// create consumer on the event queue that will send back the
		// appropriate response to the originating federate

		federateStateQueueConsumer = session.createConsumer(federateStateQueue);

		federateStateQueueConsumer
				.setMessageListener(new FederateStateQueueListener(this));

		LOG.info("Federate state queue consumer started.");

		LOG.debug("Starting federation data channel server...");

		if (federationDataChanneServerlPool == null) {
			throw new MuthurException("Federation thread pool was null.");
		}

		if (federationDataChannelServer == null) {
			throw new MuthurException("Federation data channel server was null.");
		}

		LOG.debug("Starting federation data channel server...");

		/*
		 * Get the host name from the configuration service which will be returned
		 * in the ReadyToRunResponse message to each federate
		 */
		dataChannelServerHostName =
				configurationService.getDataChannelServerHostName();
		dataChannelServerPort = configurationService.getDataChannelServerPort();

		LOG.info("Setting federation data channel server listening port to ["
				+ dataChannelServerPort + "]");

		federationDataChannelServer.setPortNumber(dataChannelServerPort);

		federationDataChanneServerlPool.execute(federationDataChannelServer);

		LOG.info("Started federation data channel server.");

		LOG.info("Federation service started.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.federation.FederationService#stop()
	 */
	@Override
	public void stop() throws Exception {

		LOG.info("Stopping federation service...");

		if ((fedExecModelToFedExecCtrlrMap != null)
				&& (!fedExecModelToFedExecCtrlrMap.isEmpty())) {

			fedExecModelToFedExecCtrlrMap.clear();

			// TODO:
			// This should be handled with messages to any *registered*
			// federates that the federation server has just expired/stopped.
		}

		/*
		 * Shut down the federation execution reaper
		 */
		if (federationExecutionReaper != null) {
			federationExecutionReaper.setContinueRunning(false);
		}

		if (federationExecutionReaperThread != null) {
			federationExecutionReaperThread.interrupt();
		}

		/*
		 * Shutdown the federation data channel server
		 */
		if (federationDataChannelServer != null) {
			federationDataChannelServer.stop();
		}

		LOG.info("Federation service stopped.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.federation.FederationService#joinFederationExecution
	 * (com .csc.muthur.model.fem.FederationExecutionModel,
	 * com.csc.muthur.server.federation.FederationExecutionParticipant)
	 */
	@Override
	public void joinFederationExecution(final FederationExecutionModel fem,
			final JoinFederationExecutionEntry fep) throws MuthurException {

		// check the parms

		if (fem == null) {
			throw new MuthurException("FederationExecutionModel was null");
		}

		if ((fem.getName() == null) || (fem.getName().length() == 0)) {
			throw new MuthurException(
					"Federation execution model name was null or empty.");
		}

		if (fep == null) {
			throw new MuthurException("JoinFederationExecutionEntry was null.");
		}

		if ((fep.getFederateName() == null) || ("".equals(fep.getFederateName()))) {
			throw new MuthurException("Federate name was null or empty.");
		}

		FederationExecutionController fec = fedExecModelToFedExecCtrlrMap.get(fem);

		if (fec == null) {

			// then this federation is not started yet so...
			// create a new one and...

			fec = new FederationExecutionControllerImpl(this, fem);

			LOG.info("Created new execution controller for federation ["
					+ fec.getFederationExecutionModel().getName() + "]");

			// add it to the map of controllers

			fedExecModelToFedExecCtrlrMap.put(fem, fec);

		}

		if (!fec.isInJoinableState()) {

			throw new MuthurException(
					"Attempted to join a federation that is in the ["
							+ fec.getExecutionState().toString() + "] process");
		}

		fec.joinFederation(fep);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.federation.FederationService#registerSubscriptions
	 * (java. lang.String, java.lang.String)
	 */
	@Override
	public void registerSubscriptions(
			final SubscriptionRegistrationFederationExecutionEntry fedExecSub)
			throws MuthurException {

		if (fedExecSub == null) {
			LOG.debug("SubscriptionRegistrationFederationExecutionEntry passed "
					+ "to registerSubscriptions() was null");
			throw new MuthurException(
					"SubscriptionRegistrationFederationExecutionEntry passed "
							+ "to registerSubscriptions() was null");
		}

		if (fedExecSub.getFederationExecutionModel() == null) {
			LOG.debug("FEM retrieved from "
					+ "SubscriptionRegistrationFederationExecutionEntry was null");
			throw new MuthurException("FEM retrieved from "
					+ "SubscriptionRegistrationFederationExecutionEntry was null");
		}

		FederationExecutionController fec =
				fedExecModelToFedExecCtrlrMap.get(fedExecSub
						.getFederationExecutionModel());

		if (fec == null) {

			throw new MuthurException(
					"Attempted to register subscriptions for federation "
							+ "which is not active.");
		}

		if (!fec.isInAcceptingSubscriptionsState()) {
			throw new MuthurException(
					"Attempted to register subscriptions for a federation that is in the ["
							+ fec.getExecutionState().toString() + "] process");
		}

		fec.registerFederateSubscriptions(fedExecSub);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.federation.FederationService#readyToRun(com.csc.muthur
	 * .model .FederationExecutionSubscription)
	 */
	@Override
	public void registerReadyToRun(ReadyToRunFederationExecutionEntry rrfee)
			throws MuthurException {

		if (rrfee == null) {
			LOG.debug("ReadyToRunFederationExecutionEntry passed to was null.");
			throw new MuthurException(
					"ReadyToRunFederationExecutionEntry passed to was null.");
		}

		FederationExecutionModel federationExecutionModel =
				rrfee.getFederationExecutionModel();

		if (federationExecutionModel == null) {
			LOG.debug("FederationExecutionModel retrieved from "
					+ "ReadyToRunFederationExecutionEntry was null.");
			throw new MuthurException("FederationExecutionModel retrieved from "
					+ "ReadyToRunFederationExecutionEntry was null.");

		}

		FederationExecutionController fec =
				fedExecModelToFedExecCtrlrMap.get(federationExecutionModel);

		if (fec == null) {

			throw new MuthurException(
					"Attempted to register ready to run for federation ["
							+ federationExecutionModel.getName() + "] which is not active.");
		}

		if (!fec.IsInAcceptingReadyToRun()) {

			throw new MuthurException("Attempted to register ready to run for "
					+ "a federation that is in the ["
					+ fec.getExecutionState().toString() + "] process");

		}

		fec.registerReadyToRun(rrfee);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.federation.FederationService#pauseFederationExecution
	 * (com .csc.muthur.model.FederationExecutionModel)
	 */
	public void startFederationExecution(
			FederationExecutionModel federationExecutionModel) throws MuthurException {

		if (federationExecutionModel == null) {
			throw new MuthurException("Unable to start federation execution. "
					+ "Federation execution model was null.");
		}

		FederationExecutionController fec =
				getFederationExecutionController(federationExecutionModel);

		if (fec != null) {
			fec.startExecution();
		} else {
			throw new MuthurException("Unable to start federation ["
					+ federationExecutionModel.getName() + "] since it was not active.");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.federation.FederationService#pauseFederationExecution
	 * (com .csc.muthur.model.FederationExecutionModel)
	 */
	public void pauseFederationExecution(
			FederationExecutionModel federationExecutionModel) throws MuthurException {

		if (federationExecutionModel == null) {
			throw new MuthurException("Unable to pause federation execution. "
					+ "Federation execution model was null.");
		}

		FederationExecutionController fec =
				getFederationExecutionController(federationExecutionModel);

		if (fec != null) {
			fec.pauseExecution();
		} else {
			throw new MuthurException("Unable to pause federation ["
					+ federationExecutionModel.getName() + "] since it was not active.");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.federation.FederationService#
	 * updateFederationExecutionTime
	 * (com.csc.muthur.model.FederationExecutionModel, long)
	 */
	@Override
	public void updateFederationExecutionTime(
			FederationExecutionModel federationExecutionModel,
			long federationExecutionTimeMSecs) throws MuthurException {

		if (federationExecutionModel == null) {
			throw new MuthurException("Unable to update federation execution time. "
					+ "Federation execution model was null.");
		}

		FederationExecutionController fec =
				getFederationExecutionController(federationExecutionModel);

		if (fec != null) {
			fec.updateFederationTime(federationExecutionTimeMSecs);
		} else {
			throw new MuthurException("Unable to update federation execution ["
					+ federationExecutionModel.getName() + "] since it was not active.");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.federation.FederationService#resumeFederationExecution
	 * (com .csc.muthur.model.FederationExecutionModel)
	 */
	public void resumeFederationExecution(
			FederationExecutionModel federationExecutionModel) throws MuthurException {

		if (federationExecutionModel == null) {

			throw new MuthurException("Unable to resume federation execution. "
					+ "Federation execution model was null.");
		}

		FederationExecutionController fec =
				getFederationExecutionController(federationExecutionModel);

		if (fec != null) {
			fec.resumeExecution();
		} else {
			throw new MuthurException("Unable to resume federation ["
					+ federationExecutionModel.getName() + "] since it was not active.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.federation.FederationService#terminateFederation(
	 * com.csc .muthur.model.FederationExecutionTermination)
	 */
	@Override
	public void terminateFederationExecution(
			FederationExecutionTermination fedExecTermination) throws MuthurException {

		if (fedExecTermination != null) {

			FederationExecutionModel federationExecutionModel =
					fedExecTermination.getFedExecModel();

			if (federationExecutionModel != null) {

				FederationExecutionController fec =
						fedExecModelToFedExecCtrlrMap.get(federationExecutionModel);

				if (fec == null) {

					throw new MuthurException("Attempted to terminate a federation ["
							+ federationExecutionModel.getName() + "] which is not active.");
				} else {

					fec.terminate();

				}

			}
		}

	}

	/**
	 * 
	 */
	public void removeFederationControllerInstance(
			final FederationExecutionModel fedExecModel) {

		try {

			if (federationExecutionControllerMapSem.tryAcquire(5, TimeUnit.SECONDS)) {

				fedExecModelToFedExecCtrlrMap.remove(fedExecModel);

				LOG.debug("Removed federation [" + fedExecModel.getName()
						+ "] from execution controller list.");

			} else {
				LOG.warn("Unable to get access to map of federation execution controllers"
						+ " to remove execution controller for federation ["
						+ fedExecModel.getName() + "]");
			}

		} catch (InterruptedException e) {
			LOG.warn("Unable to get access to map of federation execution controllers"
					+ " to remove controller for federation ["
					+ fedExecModel.getName()
					+ "]");
		} finally {
			// in case we have acquired this semaphore
			//
			federationExecutionControllerMapSem.release();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.federation.FederationService#
	 * getFederationExecutionController
	 * (com.csc.muthur.model.FederationExecutionModel)
	 */
	public FederationExecutionController getFederationExecutionController(
			final FederationExecutionModel federationExecutionModel) {

		FederationExecutionController federationExecutionController = null;

		if ((fedExecModelToFedExecCtrlrMap != null)
				&& (federationExecutionModel != null)) {
			federationExecutionController =
					fedExecModelToFedExecCtrlrMap.get(federationExecutionModel);
		}
		return federationExecutionController;
	}

	/**
	 * 
	 * @param federationExecutionHandle
	 * @return
	 */
	@Override
	public boolean isValidFederationExecutionHandle(
			final String federationExecutionHandle) {

		boolean isHandleValid = false;

		for (FederationExecutionController federationExecutionController : fedExecModelToFedExecCtrlrMap
				.values()) {

			if (federationExecutionController != null) {
				if (federationExecutionController.getFederationExecutionHandle()
						.equalsIgnoreCase(federationExecutionHandle)) {
					isHandleValid = true;
					break;
				}
			}
		}
		return isHandleValid;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.federation.FederationService#removeFederationExecution
	 * (java .lang.String)
	 */
	public void removeFederationExecution(final String fedExecHandle) {
		if ((fedExecHandle != null) && (!"".equals(fedExecHandle))) {
			fedExecModelToFedExecCtrlrMap.remove(fedExecHandle);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.federation.FederationService#
	 * getFedExecModelToFedExecCtrlrMap ()
	 */
	@Override
	public final Map<FederationExecutionModel, FederationExecutionController>
			getFedExecModelToFedExecCtrlrMap() {
		return fedExecModelToFedExecCtrlrMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.federation.FederationService#
	 * setFedExecModelToFedExecCtrlrMap (java.util.Map)
	 */
	@Override
	public final
			void
			setFedExecModelToFedExecCtrlrMap(
					Map<FederationExecutionModel, FederationExecutionController> fedExecModelToFedExecCtrlrMap) {
		this.fedExecModelToFedExecCtrlrMap = fedExecModelToFedExecCtrlrMap;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.federation.FederationService#getModelService()
	 */
	@Override
	public final ModelService getModelService() {
		return modelService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.federation.FederationService#setModelService(com.
	 * csc.muthur .model.ModelService)
	 */
	@Override
	public final void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.federation.FederationService#setTimeService(com.csc
	 * .muthur .model.TimeService)
	 */
	@Override
	public void setTimeService(TimeService timeService) {
		this.timeService = timeService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.federation.FederationService#getTimeService()
	 */
	@Override
	public TimeService getTimeService() {
		return timeService;
	}

	/**
	 * @param commonsService
	 *          the commonsService to set
	 */
	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	/**
	 * @return the commonsService
	 */
	public CommonsService getCommonsService() {
		return commonsService;
	}

	/**
	 * 
	 * 
	 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
	 * @version $Revision$
	 * 
	 */
	private class FederationExecutionReaper implements Runnable {

		private boolean continueRunning = true;
		private Semaphore sem = new Semaphore(0);

		/**
		 * 
		 */
		public FederationExecutionReaper() {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {

			LOG.debug("Started federation execution reaper.");

			while (isContinueRunning()) {

				try {
					sem.tryAcquire(10, TimeUnit.SECONDS);
				} catch (InterruptedException e) {
					setContinueRunning(false);
				}

				if (isContinueRunning()) {

					for (FederationExecutionModel fedExecModel : fedExecModelToFedExecCtrlrMap
							.keySet()) {

						FederationExecutionController fedExecCtrlr =
								fedExecModelToFedExecCtrlrMap.get(fedExecModel);

						if (fedExecCtrlr.getExecutionState() == FederationState.AWAITING_CLEANUP) {
							removeFederationControllerInstance(fedExecModel);
						}
					}
				}

			}

			LOG.debug("Exiting federation execution reaper.");

		}

		/**
		 * @return the continueRunning
		 */
		public final boolean isContinueRunning() {
			return continueRunning;
		}

		/**
		 * @param continueRunning
		 *          the continueRunning to set
		 */
		public final void setContinueRunning(boolean continueRunning) {
			this.continueRunning = continueRunning;
		}

	}

	/**
	 * @return the federateStateQueue
	 */
	public static Destination getFederateStateQueue() {
		return federateStateQueue;
	}

	/**
	 * @param federateStateQueue
	 *          the federateStateQueue to set
	 */
	public static void setFederateStateQueue(Destination federateStateQueue) {
		FederationServiceImpl.federateStateQueue = federateStateQueue;
	}

	/**
	 * @return the metricsService
	 */
	@Override
	public MetricsService getMetricsService() {
		return metricsService;
	}

	/**
	 * @param metricsService
	 *          the metricsService to set
	 */
	@Override
	public void setMetricsService(MetricsService metricsService) {
		this.metricsService = metricsService;
	}

	/**
	 * @return the federationExecutionModelService
	 */
	@Override
	public FederationExecutionModelService getFederationExecutionModelService() {
		return federationExecutionModelService;
	}

	/**
	 * @param federationExecutionModelService
	 *          the federationExecutionModelService to set
	 */
	@Override
	public void setFederationExecutionModelService(
			FederationExecutionModelService federationExecutionModelService) {
		this.federationExecutionModelService = federationExecutionModelService;
	}

	/**
	 * @return the registrationService
	 */
	public RegistrationService getRegistrationService() {
		LOG.debug("getRegistrationServer() called.");
		return registrationService;
	}

	/**
	 * @param registrationService
	 *          Called by the Spring framework to set the reference to the
	 *          RegistrationService
	 */
	public void setRegistrationService(RegistrationService registrationService) {
		LOG.debug("setRegistrationServer() called.");
		this.registrationService = registrationService;
	}

	/**
	 * @return the configurationServer
	 */
	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	/**
	 * @param configurationService
	 *          the configurationServer to set
	 */
	public void
			setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	/**
	 * @return the routerService
	 */
	public RouterService getRouterService() {
		return routerService;
	}

	/**
	 * @param routerService
	 *          the routerService to set
	 */
	public void setRouterService(RouterService routerService) {
		this.routerService = routerService;
	}

	/**
	 * @param objectService
	 *          the objectService to set
	 */
	public void setObjectService(ObjectService objectService) {
		this.objectService = objectService;
	}

	/**
	 * @return the objectService
	 */
	public ObjectService getObjectService() {
		return objectService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.federation.FederationService#
	 * getFederationDataChannelServer()
	 */
	@Override
	public FederationDataChannelServer getFederationDataChannelServer() {
		return federationDataChannelServer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.federation.FederationService#
	 * setFederationDataChannelServer
	 * (com.csc.muthur.server.federation.FederationDataChannelServer)
	 */
	@Override
	public void setFederationDataChannelServer(
			FederationDataChannelServer federationDataChannelServer) {
		this.federationDataChannelServer = federationDataChannelServer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.federation.FederationService#setPortNumber(int)
	 */
	@Override
	public void setDataChannelServerPort(int dataChannelServerPort) {
		this.dataChannelServerPort = dataChannelServerPort;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.federation.FederationService#getPortNumber()
	 */
	@Override
	public int getDataChannelServerPort() {
		return dataChannelServerPort;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.federation.FederationService#getHostName()
	 */
	@Override
	public String getDataChannelServerHostName() {
		return dataChannelServerHostName;
	}

	/*
	 * 
	 */
	@Override
	public void setDataChannelServerHostName(String dataChannelServerHostName) {
		this.dataChannelServerHostName = dataChannelServerHostName;
	}
}
