/**
 * 
 */
package com.csc.muthur.server.eventserver.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.ReadyToRunFederationExecutionEntry;
import com.csc.muthur.server.model.event.request.ReadyToRunRequest;
import com.csc.muthur.server.model.event.response.ReadyToRunResponse;

/**
 * @author <a href=mailto:Nexsim@foo.com>Nexsim</a>
 * @version $Revision$
 */
public class ReadyToRunRequestHandler extends AbstractEventHandler {

	private static final Logger LOG = LoggerFactory
			.getLogger(ReadyToRunRequestHandler.class.getName());

	/**
	 * 
	 */
	public ReadyToRunRequestHandler() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.eventserver.internal.IEventHandler#handle()
	 */
	@Override
	public void handle() throws MuthurException {

		LOG.debug("Processing ready to run request...");

		validateServiceReferences();

		ReadyToRunRequest request = new ReadyToRunRequest();

		request.initialization(getEvent().serialize());

		try {
			
			FederationExecutionModel model =
					getModelFromHandle(request.getFederationExecutionModelHandle());

			if ((request.getFederationExecutionModelHandle() == null)
					|| ("".equalsIgnoreCase(request.getFederationExecutionModelHandle()))) {
				throw new MuthurException(
						"Handle to the federation execution model was null or empty.");
			}

			ReadyToRunFederationExecutionEntry fedExecReadyToRun =
					new ReadyToRunFederationExecutionEntry(model, request, getMessage());

			federationService.registerReadyToRun(fedExecReadyToRun);

		} catch (MuthurException me) {

			LOG.error("Error handling ready to run request ["
					+ me.getLocalizedMessage() + "]");

			ReadyToRunResponse errorResponse = new ReadyToRunResponse();

			errorResponse.initialization(getEvent().serialize());

			errorResponse.setStatus("complete");
			errorResponse.setSuccess(false);
			errorResponse.setErrorDescription(me.getLocalizedMessage());

			EventMessageResponseDispatcher.sendResponse(errorResponse, getMessage());
		}

	}

}
