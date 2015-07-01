package com.csc.muthur.server.eventserver.internal;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.event.EventFactory;
import com.csc.muthur.server.model.event.response.GeneralErrorResponse;
import com.csc.muthur.server.router.RouterService;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
class EventMessageConsumer implements MessageListener {

	private static final Logger LOG = LoggerFactory
			.getLogger(EventMessageConsumer.class.getName());

	private RouterService routerService;

	/**
	 * 
	 * @param session
	 * @throws MuthurException
	 */
	public EventMessageConsumer(final Session session,
			final RouterService routerService) throws MuthurException {
		if (session == null) {
			throw new MuthurException("Null session sent to EventMessageConsumer");
		}
		if (routerService == null) {
			throw new MuthurException(
					"Null reference to router service sent to EventMessageConsumer");
		}

		this.routerService = routerService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
	 */
	@Override
	public void onMessage(Message message) {

		IEvent event = null;

		try {

			LOG.debug("Event message received");

			if (message == null) {
				throw new MuthurException("Message was null.");
			}

			if (!(message instanceof TextMessage)) {
				throw new MuthurException("Invalid message type encountered. "
						+ "Expecting TextMessage.");
			}

			TextMessage tm = (TextMessage) message;

			String xmlContent = tm.getText();

			LOG.debug("Event - [" + xmlContent + "]");

			event = EventFactory.getInstance().createEvent(xmlContent);

			if (event == null) {
				throw new MuthurException("Unable to create event from message payload");
			}

			LOG.debug("Received [" + event.getEventName() + "] event from ["
					+ event.getSourceOfEvent() + "]");

			IEventHandler eventHandler =
					EventHandlerFactory.getInstance().getEventHandler(event);

			if (eventHandler == null) {
				throw new MuthurException("Unable to create event handler for event ["
						+ event.getEventTypeDescription() + "]");
			}

			LOG.debug("Setting message into the event handler...");

			eventHandler.setMessage(tm);

			LOG.debug("Calling handle() for [" + event.getEventName() + "] event...");

			eventHandler.handle();

		} catch (Exception e) {

			// return error back to sender

			try {
				
				if (message == null) {

					// not sure what else I can do here
					LOG.error(
							"Critical error: Message received in EventMessageConsumer was null",
							e);

				} else {

					if (event == null) {
						String errorMessage =
								"Critical error: Event could not be constructed "
										+ "from message received in EventMessageConsumer.";
						LOG.error(errorMessage);

					} else {

						GeneralErrorResponse ger = new GeneralErrorResponse();

						ger.initialization(event.serialize());

						// set to failed
						ger.setSuccess(false);

						// set error description from exception
						ger.setErrorDescription(e.getLocalizedMessage());

						// return to sender
						if (routerService != null) {
							routerService.queue(ger.serialize(), message.getJMSReplyTo(),
									message.getJMSCorrelationID());
						}

					}
				}

			} catch (Throwable t) {

				try {
					LOG.error("Unrecoverable error encountered ["
							+ t.getLocalizedMessage()
							+ "] attempting to return exception to federate ["
							+ event.getSourceOfEvent() + "]; Message content ["
							+ event.serialize() + "]");

				} catch (Throwable t2) {
					LOG.error("Exception raised attempting to serialize event.", t2);
				}

			}

		}
	}
}
