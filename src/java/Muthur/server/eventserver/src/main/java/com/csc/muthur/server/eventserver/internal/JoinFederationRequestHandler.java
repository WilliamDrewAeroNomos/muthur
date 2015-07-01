/**
 * 
 */
package com.csc.muthur.server.eventserver.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.JoinFederationExecutionEntry;
import com.csc.muthur.server.model.event.request.JoinFederationRequest;
import com.csc.muthur.server.model.event.response.JoinFederationResponse;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class JoinFederationRequestHandler extends AbstractEventHandler {

	private final Logger LOG = LoggerFactory
			.getLogger(JoinFederationRequestHandler.class.getName());

	/**
	 * 
	 */
	public JoinFederationRequestHandler() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.eventserver.internal.IEventHandler#handle()
	 */
	@Override
	public void handle() throws MuthurException {

		LOG.debug("Handling join federation request...");

		validateServiceReferences();
		
		if (!(getEvent() instanceof JoinFederationRequest)) {
			throw new MuthurException("Invalid event type encountered. "
					+ "Expected JoinFederationRequest", getEvent());
		}

		JoinFederationRequest joinRequest = (JoinFederationRequest) getEvent();

		try {

			LOG.debug("Initializing join request from payload...");

			joinRequest.initialization(getEvent().serialize());

			LOG.debug("Getting registration handle from request...");

			String registrationHandle = joinRequest.getFederateRegistrationHandle();

			if (registrationHandle == null) {
				throw new MuthurException("Federate registration handle is null");
			}

			LOG.debug("Getting federate name from registration service...");

			String federateName =
					registrationServer.getFederateName(registrationHandle);

			if ((federateName == null) || (federateName.length() == 0)) {
				throw new MuthurException(
						"Federation name in join federation request was null or empty.");
			}

			LOG.debug("Received join federation request from federate ["
					+ federateName + "]");

			if (joinRequest.getFederationExecutionModel() == null) {
				throw new MuthurException(
						"Federation execution model in join federation request was null.");
			}

			/**
			 * we use the federation execution model passed in to retrieve the
			 * federation execution model from the model service to ensure that it has
			 * not been modified by the client.
			 */

			FederationExecutionModel femInternal =
					joinRequest.getFederationExecutionModel();

			if (femInternal == null) {
				throw new MuthurException(
						"Federation execution model was not found in the JoinFederationRequest");
			}

			JoinFederationExecutionEntry joinFedExecEntry =
					new JoinFederationExecutionEntry(federateName, femInternal,
							getMessage(), joinRequest);

			LOG.debug("Calling federation server to join [" + federateName
					+ "] to the [" + femInternal.getName() + "] federation ["
					+ femInternal + "]");

			federationService.joinFederationExecution(femInternal, joinFedExecEntry);

			LOG.info("Federate [" + joinFedExecEntry.getFederateName()
					+ "] successfully joined federation [" + femInternal.getName() + "]");

		} catch (MuthurException me) {

			LOG.error("Error joining federation [" + me.getLocalizedMessage() + "]");

			JoinFederationResponse errorResponse = new JoinFederationResponse();

			errorResponse.initialization(getEvent().serialize());

			errorResponse.setStatus("complete");
			errorResponse.setSuccess(false);
			errorResponse.setErrorDescription(me.getLocalizedMessage());

			EventMessageResponseDispatcher.sendResponse(errorResponse, getMessage());
		}
	}

}
