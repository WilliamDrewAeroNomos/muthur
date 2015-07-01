/**
 * 
 */
package com.csc.muthur.server.time.internal;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.osgi.context.BundleContextAware;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.configuration.ConfigurationService;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.ModelService;
import com.csc.muthur.server.time.FederateClockManager;
import com.csc.muthur.server.time.TimeService;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class TimeServiceImpl implements TimeService, ApplicationContextAware,
		BundleContextAware, ManagedService {

	private static final Logger LOG = LoggerFactory
			.getLogger(TimeServiceImpl.class.getName());

	private BundleContext bundleContext;
	private static ApplicationContext appContext;

	private String serviceMessagingPID = "com.csc.muthur.server.time";
	private ServiceRegistration ppcService;

	private ConfigurationService configurationService;
	private ModelService modelService;

	private ActiveMQConnectionFactory factory;
	private Connection connection;
	private Session session;

	private Format federationTimeFormatted = new SimpleDateFormat(
			"yyyy.MM.dd HH.mm.ss");

	private Integer sizeOfTimeIncrementMSecs = new Integer(1000);
	private Integer intervalBetweenTimeUpdatesMSecs = new Integer(1000);
	private Integer maxNumOfConsecutiveFailedAttemptsAllowed = new Integer(0);

	/**
	 * 
	 */
	public TimeServiceImpl() {
		super();

	}

	/**
	 * 
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return appContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.time.TimeService#start()
	 */
	public void start() throws MuthurException {

		LOG.debug("Starting Time service...");

		if (bundleContext != null) {

			LOG.debug("Registering time service as a managed service...");

			Dictionary<String, String> props = new Hashtable<String, String>();

			props.put("service.pid", serviceMessagingPID);

			ppcService =
					bundleContext.registerService(ManagedService.class.getName(), this,
							props);

			LOG.info("Time service registered as a managed service...");

		} else {
			LOG.warn("Bundle context was null.");
		}

		if (configurationService == null) {
			throw new MuthurException("Configuration service was null.");
		}

		try {

			factory =
					new ActiveMQConnectionFactory(
							configurationService.getMessagingConnectionUrl());

			connection = factory.createConnection();

			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		} catch (Exception e) {
			LOG.error("Error creating TimeServiceImpl.");
			throw new MuthurException(e);
		}

		LOG.info("Time service started.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.time.TimeService#stop()
	 */
	public void stop() {

		LOG.debug("Stopping Time service...");

		if (ppcService != null) {
			ppcService.unregister();
			ppcService = null;
		}

		if (session != null) {
			try {
				session.close();
			} catch (JMSException e) {
				// nothing to do here
			}
		}

		if (connection != null) {
			try {
				connection.close();
			} catch (JMSException e) {
				// nothing to do here
			}
		}

		LOG.info("Time service stopped.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.context.ApplicationContextAware#setApplicationContext
	 * (org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext appContext)
			throws BeansException {
		TimeServiceImpl.appContext = appContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.osgi.context.BundleContextAware#setBundleContext(org
	 * .osgi.framework.BundleContext)
	 */
	@Override
	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.osgi.service.cm.ManagedService#updated(java.util.Dictionary)
	 */
	@Override
	public void updated(@SuppressWarnings("rawtypes") Dictionary props)
			throws ConfigurationException {

		if (props == null) {

			LOG.debug("No configuration from configuration admin or "
					+ "old configuration has been deleted");

		} else {

			LOG.debug("Time service properties updated - [" + props + "] from ["
					+ props.get("felix.fileinstall.filename") + "]");

			if (props.get("sizeOfTimeIncrementMSecs") != null) {
				sizeOfTimeIncrementMSecs =
						Integer.valueOf((String) props.get("sizeOfTimeIncrementMSecs"));
			}

			if (props.get("intervalBetweenTimeUpdatesMSecs") != null) {
				intervalBetweenTimeUpdatesMSecs =
						Integer.valueOf((String) props
								.get("intervalBetweenTimeUpdatesMSecs"));
			}

			if (props.get("maxNumOfConsecutiveFailedAttemptsAllowed") != null) {
				maxNumOfConsecutiveFailedAttemptsAllowed =
						Integer.valueOf((String) props
								.get("maxNumOfConsecutiveFailedAttemptsAllowed"));
			}

			LOG.debug("Properties [" + props + "]");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.time.TimeService#setConfigurationService(com.csc.
	 * muthur. configuration.ConfigurationService)
	 */
	public void
			setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.time.TimeService#getConfigurationService()
	 */
	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.time.TimeService#setModelService(com.csc.muthur.model
	 * . ModelService)
	 */
	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.time.TimeService#getModelService()
	 */
	public ModelService getModelService() {
		return modelService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.time.TimeService#createFederationExecutionTimeManager
	 * (com .csc.muthur.model.FederationExecutionModel)
	 */
	@Override
	public FederateClockManager createFederateClockManager(
			final FederationExecutionModel federationExecutionModel)
			throws MuthurException {

		if (federationExecutionModel == null) {
			throw new MuthurException(
					"Error creating federate clock manager. FederationExecutionModel was null.");
		}

		if (session == null) {
			throw new MuthurException(
					"Error creating federate clock manager. Session was null.");
		}

		LOG.debug("Creating the time manager for federation execution model ["
				+ federationExecutionModel.getName() + "]");

		FederateClockManager federateClockManager =
				new FederateClockManagerImpl(federationExecutionModel, session);

		LOG.info("Created the clock manager for federation ["
				+ federationExecutionModel.getName()
				+ "] with start time of ["
				+ federationTimeFormatted.format(federateClockManager
						.getStartTimeMSecs()) + "]");

		federateClockManager
				.setIntervalBetweenTimeUpdatesMSecs(intervalBetweenTimeUpdatesMSecs);
		federateClockManager.setSizeOfTimeIncrementMSecs(sizeOfTimeIncrementMSecs);
		federateClockManager
				.setMaxNumOfConsecutiveFailedAttemptsAllowed(maxNumOfConsecutiveFailedAttemptsAllowed);

		// put it in the map

		FEM_TO_FEDERATE_CLOCKMANAGER_MAP.put(federationExecutionModel,
				federateClockManager);

		LOG.debug("Created federate clock manager for federation ["
				+ federationExecutionModel.getName() + "]");

		return federateClockManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.time.TimeService#destroyFederationExecutionManager
	 * (com.csc .muthur.model.FederationExecutionModel)
	 */
	public void destroyFederateClockManager(
			final FederationExecutionModel federationExecutionModel) {

		FederateClockManager federateClockManager = null;

		if ((federationExecutionModel != null)
				&& (FEM_TO_FEDERATE_CLOCKMANAGER_MAP
						.containsKey(federationExecutionModel))) {

			federateClockManager =
					FEM_TO_FEDERATE_CLOCKMANAGER_MAP.remove(federationExecutionModel);

			if (federateClockManager != null) {

				LOG.debug("Destroying federate clock manager for federation ["
						+ federationExecutionModel.getName() + "]...");

				federateClockManager.terminate();

				federateClockManager = null;

				LOG.debug("Destroyed federate clock manager for federation ["
						+ federationExecutionModel.getName() + "]");
			}

		}
	}

	/**
	 * @return the sizeOfTimeIncrementMSecs
	 */
	@Override
	public Integer getSizeOfTimeIncrementMSecs() {
		return sizeOfTimeIncrementMSecs;
	}

	/**
	 * @param sizeOfTimeIncrementMSecs
	 *          the sizeOfTimeIncrementMSecs to set
	 */
	@Override
	public void setSizeOfTimeIncrementMSecs(Integer sizeOfTimeIncrementMSecs) {
		this.sizeOfTimeIncrementMSecs = sizeOfTimeIncrementMSecs;
	}

	/**
	 * @return the intervalBetweenTimeUpdatesMSecs
	 */
	@Override
	public Integer getIntervalBetweenTimeUpdatesMSecs() {
		return intervalBetweenTimeUpdatesMSecs;
	}

	/**
	 * @param intervalBetweenTimeUpdatesMSecs
	 *          the intervalBetweenTimeUpdatesMSecs to set
	 */
	@Override
	public void setIntervalBetweenTimeUpdatesMSecs(
			Integer intervalBetweenTimeUpdatesMSecs) {
		this.intervalBetweenTimeUpdatesMSecs = intervalBetweenTimeUpdatesMSecs;
	}

	/**
	 * @return the maxNumOfConsecutiveFailedAttemptsAllowed
	 */
	@Override
	public Integer getMaxNumOfConsecutiveFailedAttemptsAllowed() {
		return maxNumOfConsecutiveFailedAttemptsAllowed;
	}

	/**
	 * @param maxNumOfConsecutiveFailedAttemptsAllowed
	 *          the maxNumOfConsecutiveFailedAttemptsAllowed to set
	 */
	@Override
	public void setMaxNumOfConsecutiveFailedAttemptsAllowed(
			Integer maxNumOfConsecutiveFailedAttemptsAllowed) {
		this.maxNumOfConsecutiveFailedAttemptsAllowed =
				maxNumOfConsecutiveFailedAttemptsAllowed;
	}
}
