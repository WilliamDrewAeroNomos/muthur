/**
 * 
 */
package com.csc.muthur.server.eventserver.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.event.request.UpdateFederationExecutionTimeRequest;
import com.csc.muthur.server.model.event.response.UpdateFederationExecutionTimeResponse;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class UpdateFederationExecutionTimeRequestHandler extends
		AbstractEventHandler {

	private final Logger LOG = LoggerFactory
			.getLogger(UpdateFederationExecutionTimeRequestHandler.class.getName());

	/**
	 * 
	 */
	public UpdateFederationExecutionTimeRequestHandler() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.eventserver.internal.IEventHandler#handle()
	 */
	@Override
	public void handle() throws MuthurException {

		LOG.debug("Processing update federation execution time request...");

		validateServiceReferences();

		UpdateFederationExecutionTimeRequest req =
				new UpdateFederationExecutionTimeRequest();

		req.initialization(getEvent().serialize());

		try {

			if (req.getFederationExecutionModelHandle() == null) {
				throw new MuthurException(
						"Handle to the federation execution model was null.");
			}

			FederationExecutionModel model =
					getModelFromHandle(req.getFederationExecutionModelHandle());

			federationService.updateFederationExecutionTime(model,
					req.getFederationExecutionTimeMSecs());

			UpdateFederationExecutionTimeResponse pfr =
					new UpdateFederationExecutionTimeResponse();

			pfr.initialization(req.serialize());
			pfr.setStatus("complete");
			pfr.setSuccess(true);

			EventMessageResponseDispatcher.sendResponse(pfr, getMessage());

			LOG.debug("Update federation execution time request processed.");

		} catch (MuthurException me) {

			LOG.error("Error handling update federation execution time request - ["
					+ me.getLocalizedMessage() + "]");

			UpdateFederationExecutionTimeResponse errorResponse =
					new UpdateFederationExecutionTimeResponse();

			errorResponse.initialization(getEvent().serialize());

			errorResponse.setStatus("complete");
			errorResponse.setSuccess(false);
			errorResponse.setErrorDescription(me.getLocalizedMessage());

			EventMessageResponseDispatcher.sendResponse(errorResponse, getMessage());
		}
	}
}
