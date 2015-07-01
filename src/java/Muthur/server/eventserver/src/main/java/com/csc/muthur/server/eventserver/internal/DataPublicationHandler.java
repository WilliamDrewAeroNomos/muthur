/**
 * 
 */
package com.csc.muthur.server.eventserver.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.federation.FederationExecutionController;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.FederationState;
import com.csc.muthur.server.model.event.DataPublicationEvent;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class DataPublicationHandler extends AbstractEventHandler {

	private static final Logger LOG = LoggerFactory
			.getLogger(DataPublicationHandler.class.getName());

	/**
	 * 
	 */
	public DataPublicationHandler() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.eventserver.internal.IEventHandler#handle()
	 */
	@Override
	public final void handle() throws MuthurException {

		LOG.debug("Handling data publication event...");

		validateServiceReferences();

		if (getEvent() == null) {
			throw new MuthurException("Event was null.");
		}

		if (getMessage() == null) {
			throw new MuthurException("Message was null.");
		}

		LOG.debug("[" + getEvent().getEventName() + "]");

		IEvent event = getEvent();

		if (!(event instanceof DataPublicationEvent)) {
			throw new MuthurException("Invalid event type encountered. "
					+ "Expecting a DataPublicationEvent event.");
		}

		DataPublicationEvent dp = (DataPublicationEvent) event;

		LOG.debug("Payload [" + dp.serialize() + "]");

		FederationExecutionModel model =
				getModelFromHandle(dp.getFederationExecutionModelHandle());

		if (model == null) {
			throw new MuthurException(
					"Unable to retrieve the FederationExecutionModel "
							+ "object using the supplied federation execution model handle.");
		}

		FederationExecutionController fedExecCtrlr =
				federationService.getFederationExecutionController(model);

		if (fedExecCtrlr == null) {
			throw new MuthurException(
					"Unable to retrieve federation execution controller.");
		}

		// publish it only if the federation is running
		//
		if (fedExecCtrlr.getExecutionState() == FederationState.RUNNING) {

			LOG.debug("Publishing [" + dp.getDataType().toString() + "]");

			fedExecCtrlr.publishData(dp);

		} else {

			throw new MuthurException("Unable to publish data for federation ["
					+ model.getName() + "] whose state is ["
					+ fedExecCtrlr.getExecutionState() + "]");
		}

	}

}
