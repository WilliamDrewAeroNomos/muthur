/**
 * 
 */
package com.csc.muthur.server.eventserver.internal;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.csc.muthur.server.commons.MessageDestination;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.configuration.ConfigurationService;
import com.csc.muthur.server.eventserver.EventService;
import com.csc.muthur.server.registration.RegistrationService;
import com.csc.muthur.server.router.RouterService;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class EventServiceImpl implements EventService, ApplicationContextAware {

	private static final Logger LOG = LoggerFactory
			.getLogger(EventServiceImpl.class.getName());

	private static Connection connection = null;
	private static Session session = null;
	private static Destination eventQueue = null;

	private static MessageConsumer eventQueueConsumer;

	public RegistrationService registrationService;

	public ConfigurationService configurationService;

	public RouterService routerService;

	private EventMessageConsumer eventMessageConsumer;

	private static ApplicationContext applicationContext;

	/**
	 * 
	 * @throws MuthurException
	 */
	public EventServiceImpl() throws MuthurException {
	}

	/**
	 * @return the routerService
	 */
	public final RouterService getRouterService() {
		return routerService;
	}

	/**
	 * @param routerService
	 *          the routerService to set
	 */
	@Override
	public final void setRouterService(RouterService routerService) {
		this.routerService = routerService;
	}

	/**
	 * @return the registrationService
	 */
	public RegistrationService getRegistrationService() {
		LOG.debug("getRegistrationService() called.");
		return registrationService;
	}

	/**
	 * @param registrationService
	 *          the registrationService to set
	 */
	@Override
	public void setRegistrationService(RegistrationService registrationService) {
		LOG.debug("setRegistrationService() called.");
		this.registrationService = registrationService;
	}

	/**
	 * @return the configurationService
	 */
	public final ConfigurationService getConfigurationService() {
		return configurationService;
	}

	/**
	 * @param configurationService
	 *          the configurationService to set
	 */
	@Override
	public final void setConfigurationService(
			ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	/**
	 * 
	 * @return
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * @return the connection
	 */
	public static final Connection getConnection() {
		return connection;
	}

	@Override
	public void start() throws MuthurException {

		LOG.debug("Starting event service...");

		try {

			if (configurationService == null) {
				throw new MuthurException("Configuration service reference is null");
			}

			String configurationUrl =
					configurationService.getMessagingConnectionUrl();

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
					session.createQueue(MessageDestination.getMuthurEventQueueName());

			// initiate the event response dispatcher which will create
			// a producer which will be used to publish messages to the reply
			// queue included in each message
			//
			EventMessageResponseDispatcher.getInstance(session);

			// create consumer on the event queue that will send back the
			// appropriate response to the originating federate

			startConsumer();

		} catch (Exception e) {
			throw new MuthurException(e);
		}

		LOG.info("Event service started.");
	}

	@Override
	public void stop() throws Exception {

		LOG.debug("Stopping event service...");

		// clean up the JMS resources
		//
		try {

			stopConsumer();

			if (session != null) {
				session.close();
			}
			if (connection != null) {
				connection.close();
			}

		} catch (JMSException e) {
			throw new MuthurException(e);
		}

		LOG.info("Event service stopped.");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.eventserver.EventService#startConsumer()
	 */
	@Override
	public void startConsumer() throws Exception {

		LOG.debug("Starting consumer...");

		// create consumer on the event queue that will handle all events sent into
		// Muthur

		eventQueueConsumer = session.createConsumer(eventQueue);

		eventMessageConsumer = new EventMessageConsumer(session, routerService);

		eventQueueConsumer.setMessageListener(eventMessageConsumer);

		LOG.info("Consumer started.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.eventserver.EventService#stopConsumer()
	 */
	@Override
	public void stopConsumer() throws Exception {

		LOG.debug("Stopping consumer...");

		// if (eventMessageConsumer != null) {
		//
		// EventPostProcessingQueue ppq = eventMessageConsumer
		// .getEventPostProcessingQueue();
		//
		// if (ppq != null) {
		// ppq.stop();
		// }
		// }

		if (eventQueueConsumer != null) {
			eventQueueConsumer.close();
		}

		LOG.info("Consumer stopped.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.context.ApplicationContextAware#setApplicationContext
	 * (org.springframework.context.ApplicationContext)
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		EventServiceImpl.applicationContext = applicationContext;
	}

	/**
	 * @return the eventQueue
	 */
	@Override
	public Destination getEventQueue() {
		return eventQueue;
	}

}
