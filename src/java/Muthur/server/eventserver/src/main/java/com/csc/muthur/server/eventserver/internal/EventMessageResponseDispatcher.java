/**
 * 
 */
package com.csc.muthur.server.eventserver.internal;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.commons.exception.MuthurException;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public final class EventMessageResponseDispatcher {

	private final static Logger LOG =
			LoggerFactory.getLogger(EventMessageResponseDispatcher.class.getName());

	private static EventMessageResponseDispatcher INSTANCE = null;

	private static Session session;
	private static MessageProducer replyProducer;

	/**
	 * 
	 * @param session
	 * @return
	 * @throws MuthurException
	 */
	public static EventMessageResponseDispatcher getInstance(final Session session)
			throws MuthurException {
		if (INSTANCE == null) {
			INSTANCE = new EventMessageResponseDispatcher(session);
			LOG.debug("Created EventMessageReponseDispatcher instance.");
		}
		return INSTANCE;
	}

	/**
	 * 
	 * @param session
	 * @throws MuthurException
	 */
	private EventMessageResponseDispatcher(final Session session)
			throws MuthurException {

		try {

			EventMessageResponseDispatcher.session = session;
			EventMessageResponseDispatcher.replyProducer =
					session.createProducer(null);
			EventMessageResponseDispatcher.replyProducer
					.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			LOG.debug("Created EventMessageReponseDispatcher message producer.");

		} catch (JMSException e) {
			throw new MuthurException(e.getLocalizedMessage());
		}
	}

	/**
	 * 
	 * @param response
	 * @param message
	 * @throws MuthurException
	 */
	public static void sendResponse(final IEvent response, final Message message)
			throws MuthurException {

		// check session
		if (session == null) {
			throw new MuthurException(
					"JMS session is null in SystemEventRouter.");
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
				throw new MuthurException(
						"Unable to create response text message in SystemEventRouter.");
			}

			LOG.debug("Setting body of message as response payload...");

			replyMessage.setText(responsePayload);

			LOG.debug("Setting [" + message.getJMSCorrelationID()
					+ "] as the request coorelation ID...");

			replyMessage.setJMSCorrelationID(message.getJMSCorrelationID());

			LOG.debug("Sending response to destination [" + message.getJMSReplyTo()
					+ "]");

			replyProducer.send(message.getJMSReplyTo(), replyMessage);

			LOG.debug("Response sent back to requester.");

		} catch (JMSException e) {
			throw new MuthurException(e.getLocalizedMessage());
		}

	}

	/**
	 * do not instantiate
	 */
	private EventMessageResponseDispatcher() {

	}

}
