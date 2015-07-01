/**
 * 
 */
package com.csc.muthur.server.router.internal;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.MessageDestination;
import com.csc.muthur.server.commons.exception.MuthurException;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class DispatchMessageQueue {

	private static final Logger LOG = LoggerFactory
			.getLogger(DispatchMessageQueue.class.getName());

	private Session session;
	private MessageProducer replyToQueueProducer;
	private MessageProducer systemEventProducer;

	/**
	 * Sends a payload represented as a String to the named destination
	 * jmsReplyTo. The jmsCorrelationID is used to set the correlation ID of the
	 * message.
	 * 
	 * This is used internally to route or send messages to queues created by
	 * federates. These temporary destinations are created by federates for
	 * receiving responses to requests or for events sent by Muthur. These queues
	 * are created and destroyed by each federate appropriately during it's life
	 * cycle.
	 * 
	 * @param replyToDestination
	 * @param correlationID
	 * @throws MuthurException
	 */
	public void put(String payload, Destination replyToDestination,
			String correlationID) throws MuthurException {

		if ((payload != null) && (replyToDestination != null)
				&& (correlationID != null) && (replyToQueueProducer != null)) {

			try {

				LOG.debug("Creating text message for dispatch queue...");

				TextMessage dispatchMessage = session.createTextMessage();

				LOG.debug("Set the body as the serialized message");

				dispatchMessage.setText(payload);

				LOG.debug("Set the reply to field to [" + replyToDestination
						+ "] provided by original sender");

				dispatchMessage.setJMSReplyTo(replyToDestination);

				LOG.debug("Set correlation ID [" + correlationID + "] for this message");

				dispatchMessage.setJMSCorrelationID(correlationID);

				LOG.debug("Queueing message for dispatch...");

				replyToQueueProducer.send(dispatchMessage);

				LOG.debug("Message queued for dispatch.");

			} catch (Exception e) {
				throw new MuthurException(e);
			}
		}
	}

	/**
	 * 
	 * @param payload
	 * @param serviceName
	 * @throws MuthurException
	 */

	public void put(final String payload, final MessageProducer messageProducer)
			throws MuthurException {

		if (payload == null) {
			throw new IllegalArgumentException("Payload was null or empty.");
		}

		if (messageProducer == null) {
			throw new IllegalArgumentException("Message producer was null.");
		}

		try {

			LOG.debug("Creating text message...");

			TextMessage dispatchMessage = session.createTextMessage();

			// ...set the body as the serialized message

			dispatchMessage.setText(payload);

			LOG.debug("Queuing message for service...");

			messageProducer.send(dispatchMessage);

			LOG.debug("Message queued for dispatch.");

		} catch (Exception e) {
			throw new MuthurException(e);
		}

	}

	/**
	 * Sends a payload represented as a String to a named destination. This is
	 * used internally to route or send messages to queues created by federates.
	 * These temporary destinations are created by the federate for several
	 * reasons including registration, joining a federation, receiving data
	 * publications, etc. These queues is created by the federate and destroyed by
	 * the federate appropriately during the life cycle of the federate.
	 * 
	 * @param payload
	 * @param destinationName
	 * @throws MuthurException
	 */
	public void put(String payload, String destinationName)
			throws MuthurException {

		if ((payload != null) && (destinationName != null)
				&& (!"".equalsIgnoreCase(destinationName))) {

			try {

				LOG.debug("Creating text message for dispatch queue...");

				TextMessage dispatchMessage = session.createTextMessage();

				// ...set the body as the serialized request

				dispatchMessage.setText(payload);

				LOG.debug("Queueing message for dispatch...");

				Destination destination = session.createQueue(destinationName);

				systemEventProducer.send(destination, dispatchMessage);

				LOG.debug("Message queued for dispatch.");

			} catch (Exception e) {
				throw new MuthurException(e);
			}
		}
	}

	/**
	 * 
	 * @param session
	 * @throws MuthurException
	 */
	public DispatchMessageQueue(final Session session) throws MuthurException {

		try {

			this.session = session;

			/**
			 * Create the dispatch queue which will hold all the messages that should
			 * be dispatched to federates
			 */
			Destination dispatchQueue =
					session.createQueue(MessageDestination.getMuthurDispatchQueueName());

			LOG.debug("Creating dispatch producer...");

			replyToQueueProducer = session.createProducer(dispatchQueue);

			replyToQueueProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			systemEventProducer = session.createProducer(null);

			systemEventProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

		} catch (Exception e) {
			throw new MuthurException(e);
		}
	}
}
