/**
 * 
 */
package com.csc.muthur.server.eventserver.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.event.request.StartFederationRequest;
import com.csc.muthur.server.model.event.response.StartFederationResponse;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class StartFederationRequestHandler extends AbstractEventHandler {

	private static final Logger LOG = LoggerFactory
			.getLogger(StartFederationRequestHandler.class.getName());

	/**
	 * 
	 */
	public StartFederationRequestHandler() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.eventserver.internal.IEventHandler#handle()
	 */
	@Override
	public void handle() throws MuthurException {

		LOG.debug("Processing start federation request...");

		validateServiceReferences();

		try {

			StartFederationRequest request = new StartFederationRequest();

			request.initialization(getEvent().serialize());

			if (request.getFederationExecutionModelHandle() == null) {
				throw new MuthurException(
						"Handle to the federation execution model was null.");
			}

			FederationExecutionModel model =
					getModelFromHandle(request.getFederationExecutionModelHandle());

			federationService.startFederationExecution(model);

			StartFederationResponse response = new StartFederationResponse();

			response.initialization(request.serialize());
			response.setStatus("complete");
			response.setSuccess(true);

			EventMessageResponseDispatcher.sendResponse(response, getMessage());

			LOG.debug("Start federation request processed.");

		} catch (MuthurException me) {

			LOG.error("Error handling start federation event - ["
					+ me.getLocalizedMessage() + "]");

			StartFederationResponse errorResponse = new StartFederationResponse();

			errorResponse.initialization(getEvent().serialize());

			errorResponse.setStatus("complete");
			errorResponse.setSuccess(false);
			errorResponse.setErrorDescription(me.getLocalizedMessage());

			EventMessageResponseDispatcher.sendResponse(errorResponse, getMessage());
		}
	}

}
