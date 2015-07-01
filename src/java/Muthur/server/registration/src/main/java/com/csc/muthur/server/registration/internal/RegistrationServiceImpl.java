/**
 * 
 */
package com.csc.muthur.server.registration.internal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atcloud.commons.exception.ATCloudException;
import com.atcloud.license.LicenseService;
import com.csc.muthur.server.commons.MessageDestination;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.configuration.ConfigurationService;
import com.csc.muthur.server.model.event.request.FederateRegistrationRequest;
import com.csc.muthur.server.registration.RegistrationService;
import com.csc.muthur.server.router.RouterService;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class RegistrationServiceImpl implements RegistrationService {

	private static final Logger LOG = LoggerFactory
			.getLogger(RegistrationServiceImpl.class.getName());

	private Map<String, FederateRegistrationRequest> nameToFederateRegistration =
			new ConcurrentHashMap<String, FederateRegistrationRequest>();
	private Map<String, FederateHeartBeat> federateNameToHeartbeat =
			new ConcurrentHashMap<String, FederateHeartBeat>();

	private ConfigurationService configurationService;
	private RouterService routerService;
	private LicenseService licenseService;

	private Connection connection;
	private Session session;
	private Destination federateStateQueue = null;
	private MessageProducer federateStateQueueProducer;

	/**
	 * 
	 */
	public RegistrationServiceImpl() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.registration.RegistrationService#start()
	 */
	@Override
	public void start() throws MuthurException {

		LOG.debug("Starting registration service...");

		// assumes that the ActiveMQ bundle is already started
		//
		try {

			ActiveMQConnectionFactory connectionFactory =
					new ActiveMQConnectionFactory(
							configurationService.getMessagingConnectionUrl());

			connection = connectionFactory.createConnection();

			connection.start();

			LOG.debug("Established connection to message broker at ["
					+ configurationService.getMessagingConnectionUrl() + "]");

			LOG.debug("Creating session...");

			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			LOG.debug("Creating federate state queue ["
					+ MessageDestination.getFederateStateQueueName() + " ]...");

			federateStateQueue =
					session.createQueue(MessageDestination.getFederateStateQueueName());

			LOG.debug("Federate state queue created.");

			LOG.debug("Creating producer for federate state queue ["
					+ MessageDestination.getFederateStateQueueName() + "]...");

			federateStateQueueProducer = session.createProducer(federateStateQueue);

			federateStateQueueProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			LOG.debug("Federate state queue producer created.");

		} catch (JMSException e) {
			throw new MuthurException(e);
		}
		LOG.info("Registration service started.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.registration.RegistrationService#stop()
	 */
	@Override
	public void stop() {
		LOG.debug("Stopping registration service...");

		if (nameToFederateRegistration != null) {
			nameToFederateRegistration.clear();
		}
		if (federateNameToHeartbeat != null) {
			federateNameToHeartbeat.clear();
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

		LOG.info("Registration service stopped.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.registration.internal.RegistrationService#addFederate
	 * (java .lang.String)
	 */
	public final void registerFederate(
			final FederateRegistrationRequest federateRegistrationRequest)
			throws MuthurException {

		if (federateRegistrationRequest == null) {
			throw new MuthurException("FederateRegistrationRequest was null or "
					+ "empty when registering federate.");
		}

		if (federateRegistrationRequest.getFederateName() == null) {
			throw new MuthurException(
					"Federate name was null or empty when registering.");
		}

		/*
		 * check if federate is already registered
		 */

		if (nameToFederateRegistration.containsKey(federateRegistrationRequest
				.getFederateName())) {
			throw new MuthurException("Federate ["
					+ federateRegistrationRequest.getFederateName()
					+ "] is already registered.");
		}

		/*
		 * Validate the supplied license key
		 */
		try {
			if (!licenseService.isValidLicense(federateRegistrationRequest
					.getLicenseKey())) {
				throw new MuthurException(
						"License key used for registeration of federate ["
								+ federateRegistrationRequest.getFederateName()
								+ "] was invalid.");
			}
		} catch (ATCloudException e1) {
			throw new MuthurException(e1.getLocalizedMessage(), e1);
		}

		/*
		 * Check if the license is in use
		 */
		if (licenseService.isLicenseInUse(federateRegistrationRequest
				.getLicenseKey())) {

			throw new MuthurException("License key ["
					+ federateRegistrationRequest.getLicenseKey()
					+ "] used for registeration of federate ["
					+ federateRegistrationRequest.getFederateName()
					+ "] is currently in use.");
		}

		/*
		 * set the license as "in use"
		 */
		try {

			licenseService.setLicenseUnavailable(federateRegistrationRequest
					.getLicenseKey());

		} catch (ATCloudException e) {
			LOG.error("Unable to set exclusive lock on license key ["
					+ federateRegistrationRequest.getLicenseKey() + "]", e);
			throw new MuthurException(e);
		}

		LOG.debug("Adding federate ["
				+ federateRegistrationRequest.getFederateName() + "] to map...");

		nameToFederateRegistration.put(
				federateRegistrationRequest.getFederateName(),
				federateRegistrationRequest);

		LOG.debug("nameToFederateRegistration map contents ["
				+ nameToFederateRegistration + "]");

		if (configurationService.isGenerateHeartBeat()) {

			LOG.debug("Creating heart beat thread for ["
					+ federateRegistrationRequest.getFederateName() + "]...");

			FederateHeartBeat fhb =
					new FederateHeartBeat(this, federateRegistrationRequest, session);

			Thread t = new Thread(fhb);

			federateNameToHeartbeat.put(
					federateRegistrationRequest.getFederateName(), fhb);

			t.start();

			LOG.debug("Heart beat thread started for ["
					+ federateRegistrationRequest.getFederateName() + "].");

		} else {
			LOG.debug("Heart beat generation parameter = ["
					+ configurationService.isGenerateHeartBeat()
					+ "]. No heartbeat will be generated for ["
					+ federateRegistrationRequest.getFederateName() + "]");
		}

		LOG.info("Federate [" + federateRegistrationRequest.getFederateName()
				+ "] has been registered.");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.registration.RegistrationService#deregisterFederate
	 * (java.lang.String)
	 */
	public void deregisterFederate(final String federateName)
			throws MuthurException {

		if ((federateName != null) && (federateName.length() != 0)) {

			FederateRegistrationRequest removedFederateRegistration =
					nameToFederateRegistration.remove(federateName);

			if (removedFederateRegistration == null) {
				LOG.debug("Attempted to deregister federate [" + federateName
						+ "] which was not registered or was previously deregistered.");
			} else {

				LOG.debug("Deregistering federate [" + federateName + "]...");

				FederateHeartBeat fhb = federateNameToHeartbeat.remove(federateName);

				if (fhb != null) {
					fhb.setStillAlive(false);
				} else {
					LOG.warn("Unable to find heart beat thread for federate ["
							+ federateName + "]");
				}

				/*
				 * Make the license available again.
				 */
				try {
					licenseService.setLicenseAvailable(removedFederateRegistration
							.getLicenseKey());
				} catch (ATCloudException e) {
					LOG.error("Error attempting to make license key ["
							+ removedFederateRegistration.getLicenseKey() + "] available ["
							+ e.getLocalizedMessage() + "]", e);
				}

				// LOG.debug("Sending federation termination message to ["
				// + ServiceName.EVENT + "] service to terminate all "
				// + "federation executions where [" + federateName
				// + "] is a required federate.");
				//
				// FederateDeregistrationEvent event = new
				// FederateDeregistrationEvent();
				//
				// event.setSourceOfEvent(MessageDestination.MUTHUR);
				//
				// event.setFederateRegistrationHandle(removedFederateRegistration
				// .getFederateRegistrationHandle());
				// event.setFederateName(federateName);
				//
				// getRouterService().postEvent(event.serialize(), ServiceName.EVENT);

				LOG.info("Federate [" + federateName + "] has been deregistered.");
			}
		}
	}

	/**
	 * 
	 * @param federateRegistrationHandle
	 * @return
	 */
	@Override
	public boolean isFederateRegistered(final String federateRegistrationHandle) {

		boolean federateRegistered = false;

		if (federateRegistrationHandle != null) {

			for (FederateRegistrationRequest frr : nameToFederateRegistration
					.values()) {

				if (frr != null) {

					if (frr.getEventUUID() != null) {

						if (frr.getEventUUID().equalsIgnoreCase(federateRegistrationHandle)) {

							federateRegistered = true;
							break;
						}
					}
				}
			}
		}

		return federateRegistered;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.registration.RegistrationService#getFederateRegistration
	 * (java.lang.String)
	 */
	public FederateRegistrationRequest getFederateRegistrationRequest(
			final String federateName) {

		FederateRegistrationRequest federationRegistrationRequest = null;

		if ((federateName != null) && (!("".equals(federateName)))) {
			federationRegistrationRequest =
					nameToFederateRegistration.get(federateName);
			if (federationRegistrationRequest == null) {
				LOG.debug("Federate [" + federateName + "] was not registered.");
			}
		}
		return federationRegistrationRequest;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.registration.RegistrationService#getFederateName(
	 * java. lang.String)
	 */
	public String getFederateName(final String registrationHandle) {

		String federateName = null;

		if (registrationHandle != null) {

			for (String registeredName : this.nameToFederateRegistration.keySet()) {

				FederateRegistrationRequest fedRegistrationRequest =
						nameToFederateRegistration.get(registeredName);

				if (fedRegistrationRequest.getEventUUID().equalsIgnoreCase(
						registrationHandle)) {
					federateName = registeredName;
					break;
				}
			}

		}
		return federateName;
	}

	/**
	 * @return the configurationService
	 */
	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	/**
	 * @param configurationService
	 *          the configurationService to set
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

	@Override
	public LicenseService getLicenseService() {
		return licenseService;
	}

	@Override
	public void setLicenseService(LicenseService licenseService) {
		this.licenseService = licenseService;
	}

	/**
	 * @return the nameToFederateRegistration
	 */
	@Override
	public Map<String, FederateRegistrationRequest>
			getNameToFederateRegistration() {
		return nameToFederateRegistration;
	}

	/**
	 * @param nameToFederateRegistration
	 *          the nameToFederateRegistration to set
	 */
	@Override
	public void setNameToFederateRegistration(
			Map<String, FederateRegistrationRequest> nameToFederateRegistration) {
		this.nameToFederateRegistration = nameToFederateRegistration;
	}
}
