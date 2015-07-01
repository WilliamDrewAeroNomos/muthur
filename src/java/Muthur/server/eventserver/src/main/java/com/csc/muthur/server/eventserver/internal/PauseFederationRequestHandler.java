/**
 * 
 */
package com.csc.muthur.server.eventserver.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.event.request.PauseFederationRequest;
import com.csc.muthur.server.model.event.response.PauseFederationResponse;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class PauseFederationRequestHandler extends AbstractEventHandler {

	private final Logger LOG = LoggerFactory
			.getLogger(PauseFederationRequestHandler.class.getName());

	/**
	 * 
	 */
	public PauseFederationRequestHandler() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.eventserver.internal.IEventHandler#handle()
	 */
	@Override
	public void handle() throws MuthurException {

		LOG.debug("Processing pause federation request...");

		validateServiceReferences();

		try {

			PauseFederationRequest req = new PauseFederationRequest();

			req.initialization(getEvent().serialize());

			if (req.getFederationExecutionModelHandle() == null) {
				throw new MuthurException(
						"Handle to the federation execution model was null.");
			}

			FederationExecutionModel model =
					getModelFromHandle(req.getFederationExecutionModelHandle());

			federationService.pauseFederationExecution(model);

			PauseFederationResponse pfr = new PauseFederationResponse();

			pfr.initialization(req.serialize());
			pfr.setStatus("complete");
			pfr.setSuccess(true);

			EventMessageResponseDispatcher.sendResponse(pfr, getMessage());

			LOG.debug("Pause federation request processed.");

		} catch (MuthurException me) {

			LOG.error("Error handling pause federation event - ["
					+ me.getLocalizedMessage() + "]");

			PauseFederationResponse errorResponse = new PauseFederationResponse();

			errorResponse.initialization(getEvent().serialize());

			errorResponse.setStatus("complete");
			errorResponse.setSuccess(false);
			errorResponse.setErrorDescription(me.getLocalizedMessage());

			EventMessageResponseDispatcher.sendResponse(errorResponse, getMessage());
		}
	}

}
