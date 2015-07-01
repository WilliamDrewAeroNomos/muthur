/**
 * 
 */
package com.csc.muthur.server.eventserver.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.SubscriptionRegistrationFederationExecutionEntry;
import com.csc.muthur.server.model.event.request.DataSubscriptionRequest;
import com.csc.muthur.server.model.event.response.DataSubscriptionResponse;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class DataSubscriptionRequestHandler extends AbstractEventHandler {

	private final static Logger LOG = LoggerFactory
			.getLogger(DataSubscriptionRequestHandler.class.getName());

	/**
	 * 
	 */
	public DataSubscriptionRequestHandler() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.eventserver.internal.IEventHandler#handle()
	 */
	@Override
	public final void handle() throws MuthurException {

		LOG.debug("Handling data subscription request...");

		validateServiceReferences();

		DataSubscriptionRequest request = new DataSubscriptionRequest();

		request.initialization(getEvent().serialize());

		try {

			String fedExecModelHandle = request.getFederationExecutionModelHandle();

			if (fedExecModelHandle == null) {
				throw new MuthurException("Federation execution model handle is null");
			}

			FederationExecutionModel model = getModelFromHandle(fedExecModelHandle);

			if (model == null) {
				throw new MuthurException("Model service is null");
			}

			SubscriptionRegistrationFederationExecutionEntry fedExecSub =
					new SubscriptionRegistrationFederationExecutionEntry(model, request,
							getMessage());

			LOG.debug("Registering subscriptions " + request.getDataSubscriptions()
					+ " for federate [" + fedExecSub.getFederateName()
					+ "] for federation [" + model.getName() + "]...");

			federationService.registerSubscriptions(fedExecSub);

			LOG.debug("Registered subscriptions " + request.getDataSubscriptions()
					+ " for federate [" + fedExecSub.getFederateName()
					+ "] in federation [" + model.getName() + "]");

			LOG.debug("Successfully registered subscriptions for ["
					+ fedExecSub.getFederateName() + "] for federation ["
					+ model.getName() + "]");

		} catch (MuthurException me) {

			LOG.error("Error handling data subscription request ["
					+ me.getLocalizedMessage() + "]");

			DataSubscriptionResponse errorResponse = new DataSubscriptionResponse();

			errorResponse.initialization(getEvent().serialize());

			errorResponse.setStatus("complete");
			errorResponse.setSuccess(false);
			errorResponse.setErrorDescription(me.getLocalizedMessage());

			EventMessageResponseDispatcher.sendResponse(errorResponse, getMessage());
		}
	}

}
