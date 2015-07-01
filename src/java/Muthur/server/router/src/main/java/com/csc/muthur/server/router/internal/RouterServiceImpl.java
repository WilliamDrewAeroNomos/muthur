/**
 * 
 */
package com.csc.muthur.server.router.internal;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.commons.MessageDestination;
import com.csc.muthur.server.commons.ServiceName;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.configuration.ConfigurationService;
import com.csc.muthur.server.model.event.EventFactory;
import com.csc.muthur.server.router.RouterService;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class RouterServiceImpl implements RouterService {

	private static final Logger LOG = LoggerFactory
			.getLogger(RouterServiceImpl.class.getName());

	private Connection connection;
	private Session session;
	private MessageProducer replyProducer;
	private static Destination dispatchQueue;
	private static MessageConsumer dispatchQueueConsumer;

	public ConfigurationService configurationServer;

	public DispatchMessageQueue dispatchMessageQueue;

	public Map<ServiceName, MessageProducer> serviceNameToMessageProducer =
			new ConcurrentHashMap<ServiceName, MessageProducer>();

	/**
	 * 
	 */
	public RouterServiceImpl() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.router.internal.RouterService#start()
	 */
	public void start() throws MuthurException {

		LOG.debug("Starting routing service...");

		try {

			// assumes that the ActiveMQ bundle is already started
			//
			ActiveMQConnectionFactory connectionFactory =
					new ActiveMQConnectionFactory(
							configurationServer.getMessagingConnectionUrl());

			connection = connectionFactory.createConnection();

			connection.start();

			LOG.debug("Established connection to message broker at ["
					+ configurationServer.getMessagingConnectionUrl() + "]");

			LOG.debug("Creating session...");

			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			LOG.debug("Creating routing producer...");

			replyProducer = session.createProducer(null);
			replyProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			/**
			 * Create the dispatch queue which will hold all the messages that should
			 * be dispatched to federates
			 */
			dispatchQueue =
					session.createQueue(MessageDestination.getMuthurDispatchQueueName());

			startConsumer();

			// create the dispatch message queue which

			dispatchMessageQueue = new DispatchMessageQueue(session);

			/**
			 * Create the producer to send messages to the event service
			 */
			Destination eventServiceQueue =
					session.createQueue(MessageDestination.getMuthurEventQueueName());

			MessageProducer eventServiceEventProducer =
					session.createProducer(eventServiceQueue);
			eventServiceEventProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			serviceNameToMessageProducer.put(ServiceName.EVENT,
					eventServiceEventProducer);

		} catch (Exception e) {
			throw new MuthurException(e);
		}

		LOG.info("Routing service started.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.router.internal.RouterService#stop()
	 */
	public void stop() throws MuthurException {

		LOG.debug("Stopping routing service...");

		// clean up the JMS resources
		//
		try {

			if (session != null) {
				session.close();
			}
			if (connection != null) {
				connection.close();
			}

		} catch (JMSException e) {
			throw new MuthurException(e);
		}

		LOG.info("Routing service stopped.");
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void startConsumer() throws Exception {

		LOG.debug("Starting consumer...");

		// create consumer on the event queue that will send back the
		// appropriate response to the originating federate

		dispatchQueueConsumer = session.createConsumer(dispatchQueue);

		dispatchQueueConsumer
				.setMessageListener(new DispatchQueueConsumer(session));

		LOG.info("Consumer started.");
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void stopConsumer() throws Exception {

		LOG.debug("Stopping consumer...");

		if (dispatchQueueConsumer != null) {
			dispatchQueueConsumer.close();
		}

		LOG.info("Consumer stopped.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.router.RouterService#put(java.lang.String,
	 * javax.jms.Destination, java.lang.String)
	 */
	public void queue(String payload, Destination jmsReplyTo,
			String jmsCorrelationID) throws MuthurException {

		if (dispatchMessageQueue != null) {
			dispatchMessageQueue.put(payload, jmsReplyTo, jmsCorrelationID);
		}

	}

	/**
	 * 
	 * @param payload
	 * @param service
	 * @throws MuthurException
	 */
	public void postEvent(String payload, ServiceName service)
			throws MuthurException {

		if (payload == null) {
			throw new IllegalArgumentException("Payload was null or empty.");
		}

		if (service == null) {
			throw new IllegalArgumentException("Service name was null or empty.");
		}

		MessageProducer producer = serviceNameToMessageProducer.get(service);

		if (producer == null) {
			throw new MuthurException("Could not find a message producer for ["
					+ service + "] service.");
		}

		if (dispatchMessageQueue != null) {

			LOG.debug("Sending message [" + payload + "] to [" + service
					+ "] service...");

			dispatchMessageQueue.put(payload, producer);

			LOG.debug("Message sent to [" + service + "] service");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.router.RouterService#put(java.lang.String,
	 * java.lang.String)
	 */
	public void queue(String payload, String destinationName)
			throws MuthurException {

		if (dispatchMessageQueue != null) {
			dispatchMessageQueue.put(payload, destinationName);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.router.internal.RouterService#sendResponse(com.
	 * csc.muthur .model.event.IEvent, javax.jms.Message)
	 */
	public void sendResponse(final IEvent response, final Message message)
			throws MuthurException {

		// check session
		if (session == null) {
			throw new MuthurException("JMS session is null in RouterService.");
		}

		// check producer
		if (replyProducer == null) {
			throw new MuthurException(
					"Message producer is null in SystemEventRouter.");
		}

		// check arguments
		if (response == null) {
			throw new MuthurException("Illegal argument. Response was null.");
		}
		if (message == null) {
			throw new MuthurException("Illegal argument. Message was null.");
		}

		LOG.debug("Getting response payload...");

		String responsePayload = response.serialize();

		LOG.debug("Response payload [" + responsePayload + "]");

		TextMessage replyMessage;

		try {

			// Create text message from session to send response payload

			replyMessage = session.createTextMessage();

			if (replyMessage == null) {
				throw new MuthurException("Unable to create response text message.");
			}

			LOG.debug("Setting body of message as response payload...");

			replyMessage.setText(responsePayload);

			LOG.debug("Setting [" + message.getJMSCorrelationID()
					+ "] as the request correlation ID...");

			replyMessage.setJMSCorrelationID(message.getJMSCorrelationID());

			LOG.debug("Sending response to destination [" + message.getJMSReplyTo()
					+ "]");

			replyProducer.send(message.getJMSReplyTo(), replyMessage);

			LOG.debug("Response sent back to requester.");

		} catch (JMSException e) {
			throw new MuthurException(e.getLocalizedMessage());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.router.RouterService#getConfigurationServer()
	 */
	public ConfigurationService getConfigurationServer() {
		return configurationServer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.router.RouterService#setConfigurationServer(com
	 * .csc.muthur .configuration.ConfigurationService)
	 */
	public void setConfigurationServer(ConfigurationService configurationServer) {
		this.configurationServer = configurationServer;
	}

	/**
	 * 
	 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
	 * @version $Revision$
	 * @author wdrew
	 * 
	 */
	class DispatchQueueConsumer implements MessageListener {

		private final Logger LOG = LoggerFactory
				.getLogger(DispatchQueueConsumer.class.getName());

		/**
		 * 
		 * @param session
		 * @throws MuthurException
		 */
		public DispatchQueueConsumer(final Session session) throws MuthurException {
			if (session == null) {
				throw new MuthurException("Null session sent to DispatchQueueConsumer");
			}
		}

		/**
	 * 
	 */
		@Override
		public void onMessage(Message message) {

			LOG.debug("Dispatch message received");

			if (message instanceof TextMessage) {

				TextMessage tm = (TextMessage) message;

				String xmlContent = null;

				try {

					xmlContent = tm.getText();

					LOG.debug("Serialized payload - [" + xmlContent + "]");

					IEvent event;

					try {

						LOG.debug("Creating event from serialized form...");

						event = EventFactory.getInstance().createEvent(xmlContent);

						try {

							LOG.debug("Sending response message...");
							
							sendResponse(event, message);

						} catch (MuthurException e) {
							LOG.error("Error sending response. Serialized payload ["
									+ xmlContent + "]", e);
						}

					} catch (MuthurException e) {
						LOG.error("Error creating event. Serialized payload [" + xmlContent
								+ "]", e);
					}

				} catch (JMSException e) {
					LOG.error("Error getting serialized payload from TextMessage", e);
				} catch (Throwable t) {
					LOG.error("Unrecoverable error returning response", t);
				}

			}

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.router.RouterService#queue(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void queue(String payload, String replyToQueueName,
			String correlationID) throws MuthurException {

		Destination jmsReplyTo;
		try {
			jmsReplyTo = session.createQueue(replyToQueueName);
		} catch (JMSException e) {
			throw new MuthurException("Unable to send message to ["
					+ replyToQueueName + "]", e);
		}

		if (dispatchMessageQueue != null) {
			LOG.debug("Putting payload on dispatch queue...");
			dispatchMessageQueue.put(payload, jmsReplyTo, correlationID);
			LOG.debug("Put payload on dispatch queue.");
		}

	}

}
