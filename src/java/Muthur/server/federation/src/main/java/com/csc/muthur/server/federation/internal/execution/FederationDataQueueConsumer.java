package com.csc.muthur.server.federation.internal.execution;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.IDataEventHandler;
import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.federation.DataEventHandlerFactory;
import com.csc.muthur.server.federation.FederationService;
import com.csc.muthur.server.model.event.EventFactory;
import com.csc.muthur.server.router.RouterService;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
@Deprecated
public class FederationDataQueueConsumer implements MessageListener {

	public static final Logger LOG = LoggerFactory
			.getLogger(FederationDataQueueConsumer.class.getName());

	private DataEventHandlerFactory federationDataEventHandlerFactoryImpl;
	private RouterService routerService;

	/**
	 * 
	 * @param federationService
	 * @throws MuthurException
	 */
	public FederationDataQueueConsumer(final FederationService federationService)
			throws MuthurException {

		if (federationService == null) {
			throw new MuthurException("Reference to federation service was null");
		}

		routerService = federationService.getRouterService();

		if (routerService == null) {
			throw new MuthurException(
					"Reference to router service in federation service was null");
		}

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
				LOG.warn("Invalid message type encountered on data event queue in ["
						+ "Expecting TextMessage.");
			}

			TextMessage tm = (TextMessage) message;

			String xmlContent = tm.getText();

			LOG.debug("Event - [" + xmlContent + "]");

			event = EventFactory.getInstance().createEvent(xmlContent);

			if (event == null) {
				throw new MuthurException("Unable to create event from message payload");
			}

			LOG.info("Received [" + event.getEventName() + "] event from ["
					+ event.getSourceOfEvent() + "]");

			IDataEventHandler eventHandler =
					federationDataEventHandlerFactoryImpl.getEventHandler(event);

			if (eventHandler == null) {
				throw new MuthurException("Unable to create event handler for event ["
						+ event.getEventTypeDescription() + "]");
			}

			LOG.debug("Calling handle() for [" + event.getEventName() + "] event...");

			eventHandler.handle();

		} catch (Exception e) {

			// return error back to sender

			try {
				if (message == null) {

					// not sure what else I can do here
					LOG.error("Critical error: Message received in EventMessageConsumer was null");
					LOG.error(e.getLocalizedMessage());

				} else {

					if (event == null) {
						String errorMessage =
								"Event could not be constructed "
										+ "from message received in EventMessageConsumer.";
						LOG.error(errorMessage);

					} else {

						// set to failed
						event.setSuccess(false);

						// set error description from exception
						event.setErrorDescription(e.getLocalizedMessage());

						// return to sender
						if (routerService != null) {
							routerService.queue(event.serialize(), message.getJMSReplyTo(),
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
					LOG.error("Exception raised attempting to serialize event which caused exception. Exception message = ["
							+ t2.getLocalizedMessage() + "]");
				}

			}

		}

	}

}
