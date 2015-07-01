/**
 * 
 */
package com.csc.muthur.server.eventserver.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.event.request.ResumeFederationRequest;
import com.csc.muthur.server.model.event.response.ResumeFederationResponse;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class ResumeFederationRequestHandler extends AbstractEventHandler {

	private final Logger LOG = LoggerFactory
			.getLogger(PauseFederationRequestHandler.class.getName());

	/**
	 * 
	 */
	public ResumeFederationRequestHandler() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.eventserver.internal.IEventHandler#handle()
	 */
	@Override
	public void handle() throws MuthurException {

		LOG.debug("Handling resume federation request...");

		validateServiceReferences();

		ResumeFederationRequest req = new ResumeFederationRequest();

		req.initialization(getEvent().serialize());

		try {

			if (req.getFederationExecutionModelHandle() == null) {
				throw new MuthurException(
						"Handle to the federation execution model was null.");
			}

			FederationExecutionModel model =
					getModelFromHandle(req.getFederationExecutionModelHandle());

			federationService.resumeFederationExecution(model);

			ResumeFederationResponse rfresponse = new ResumeFederationResponse();

			rfresponse.initialization(req.serialize());
			rfresponse.setStatus("complete");
			rfresponse.setSuccess(true);

			EventMessageResponseDispatcher.sendResponse(rfresponse, getMessage());

			LOG.debug("Resume federation request processed.");

		} catch (MuthurException me) {

			LOG.error("Error handling resume federation event - ["
					+ me.getLocalizedMessage() + "]");

			ResumeFederationResponse errorResponse = new ResumeFederationResponse();

			errorResponse.initialization(getEvent().serialize());

			errorResponse.setStatus("complete");
			errorResponse.setSuccess(false);
			errorResponse.setErrorDescription(me.getLocalizedMessage());

			EventMessageResponseDispatcher.sendResponse(errorResponse, getMessage());
		}
	}

}
