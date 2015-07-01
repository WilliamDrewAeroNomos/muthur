/**
 * 
 */
package com.csc.muthur.server.federation.internal.datachannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.DataChannelControlBlock;
import com.csc.muthur.server.commons.IDataEventHandler;
import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.commons.MessageDestination;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.federation.DataChannelListener;
import com.csc.muthur.server.federation.DataEventFactory;
import com.csc.muthur.server.federation.DataEventHandlerFactory;
import com.csc.muthur.server.federation.FederationExecutionController;
import com.csc.muthur.server.federation.FederationService;
import com.csc.muthur.server.model.FederationExecutionModel;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public class FederationDataChannelListener implements DataChannelListener {

	private static final Logger LOG = LoggerFactory
			.getLogger(FederationDataChannelListener.class.getName());

	private DataEventHandlerFactory dataEventHandlerFactory;
	private DataEventFactory dataEventFactory;
	private FederationService federationService;

	/**
	 * 
	 */
	public FederationDataChannelListener() {
	}

	/**
	 * 
	 * @param dccBlock
	 * @param payload
	 */
	@Override
	public void messageReceived(final DataChannelControlBlock dccBlock,
			Object objectLoad) {

		IEvent event = null;
		String payLoad = null;

		try {

			if (dccBlock == null) {
				throw new MuthurException("DataChannelControlBlock was null.");
			}

			LOG.debug("Data event [" + dccBlock.getEventName() + "] received");

			if (objectLoad == null) {
				throw new MuthurException("Payload was null.");
			}

			if (!(objectLoad instanceof String)) {
				throw new MuthurException("Payload was not an instance of String.");
			}

			/*
			 * Cast to String to avoid multiple casts below
			 */
			payLoad = (String) objectLoad;

			LOG.debug("Data event payload [" + payLoad + "]");

			event = dataEventFactory.createEvent(dccBlock.getEventName());

			if (event == null) {
				throw new MuthurException(
						"Unable to create event from data message payload");
			}

			event.initialization(payLoad);

			LOG.debug("Received [" + event.getEventName() + "] event from ["
					+ event.getSourceOfEvent() + "]");

			/*
			 * Get the event handler and set the event in the handler
			 */
			IDataEventHandler eventHandler =
					getDataEventHandlerFactory().getEventHandler(event);

			if (eventHandler == null) {
				throw new MuthurException("Unable to create event handler for event ["
						+ event.getEventTypeDescription() + "]");
			}

			eventHandler.setDataChannelControlBlock(dccBlock);

			LOG.debug("Calling handle() for [" + dccBlock.getEventName()
					+ "] event...");

			FederationExecutionModel fem =
					federationService.getModelService().getModel(
							event.getFederationExecutionModelHandle());
			FederationExecutionController fec =
					federationService.getFederationExecutionController(fem);

			fec.getFederationDataRequestQueue().put(eventHandler);

		} catch (Exception e) {

			// return error back to sender

			try {

				if (event == null) {

					LOG.error("Event could not be constructed from "
							+ "message received in FederationDataChannelListener.");

				} else {

					// set to failed
					event.setSuccess(false);

					// set error description from exception
					event.setErrorDescription(e.getLocalizedMessage());

					if (MessageDestination.getMuthurEventQueueName() == dccBlock
							.getReplyToQueueName()) {
						LOG.error("Unable to return error to sender using the main event queue ["
								+ MessageDestination.getMuthurEventQueueName()
								+ "]. "
								+ "The reply queue name must be set to a queue created on the client side.");

					} else {

						LOG.debug("Returning error to sender using reply queue ["
								+ dccBlock.getReplyToQueueName() + "] provided by sender.");

						if (federationService.getRouterService() != null) {
							federationService.getRouterService().queue(payLoad,
									dccBlock.getReplyToQueueName(), dccBlock.getCorrelationID());
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
					LOG.error("Exception raised attempting to serialize event which caused exception. "
							+ "Exception message - [" + t2.getLocalizedMessage() + "]");
				}

			}

		}

	}

	/**
	 * @return the dataEventHandlerFactory
	 */
	public DataEventHandlerFactory getDataEventHandlerFactory() {
		return dataEventHandlerFactory;
	}

	/**
	 * @param dataEventHandlerFactory
	 *          the dataEventHandlerFactory to set
	 */
	public void setDataEventHandlerFactory(
			DataEventHandlerFactory dataEventHandlerFactory) {
		this.dataEventHandlerFactory = dataEventHandlerFactory;
	}

	/**
	 * @return the dataEventFactory
	 */
	public DataEventFactory getDataEventFactory() {
		return dataEventFactory;
	}

	/**
	 * @param dataEventFactory
	 *          the dataEventFactory to set
	 */
	public void setDataEventFactory(DataEventFactory dataEventFactory) {
		this.dataEventFactory = dataEventFactory;
	}

	/**
	 * @return the federationService
	 */
	public FederationService getFederationService() {
		return federationService;
	}

	/**
	 * @param federationService
	 *          the federationService to set
	 */
	public void setFederationService(FederationService federationService) {
		this.federationService = federationService;
	}
}
