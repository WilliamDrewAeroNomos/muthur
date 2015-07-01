/**
 * 
 */
package com.csc.muthur.server.eventserver.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.commons.MessageDestination;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.event.request.FederateRegistrationRequest;
import com.csc.muthur.server.model.event.response.FederateRegistrationResponse;

/**
 * @author <a href=mailto:Nexsim@foo.com>Nexsim</a>
 * @version $Revision$
 */
public class RegistrationRequestEventHandler extends AbstractEventHandler {

	private final Logger LOG = LoggerFactory
			.getLogger(RegistrationRequestEventHandler.class.getName());

	/**
	 * 
	 */
	public RegistrationRequestEventHandler() {
	}

	@Override
	public void handle() throws MuthurException {

		LOG.debug("Handling registration request...");

		IEvent event = getEvent();

		if (event == null) {
			throw new MuthurException(
					"Event received in RegistrationRequestEventHandler was null");
		}

		if (!(event instanceof FederateRegistrationRequest)) {
			throw new MuthurException(
					"RegistrationRequestEventHandler received unexpected event type. "
							+ "Expected FederateRegistrationRequest but received ["
							+ event.getClass().getName() + "]");
		}

		FederateRegistrationRequest fr = (FederateRegistrationRequest) event;

		try {

			if (registrationServer == null) {
				throw new MuthurException(
						"Registration service reference is null. Unable to register federate ["
								+ fr.getFederateName() + "]", fr);
			}

			LOG.debug("Registering [" + fr.getFederateName() + "]...");

			registrationServer.registerFederate(fr);

			FederateRegistrationResponse rr = new FederateRegistrationResponse();

			rr.initialization(fr.serialize());

			rr.setSourceOfEvent(MessageDestination.MUTHUR);
			rr.setTimeToLiveSecs(30);

			rr.setStatus("complete");
			rr.setSuccess(true);
			rr.setFederateRegistrationHandle(fr.getEventUUID());
			rr.setbHeartBeanEnabled(getRegistrationServer().getConfigurationService()
					.isGenerateHeartBeat());

			LOG.debug("Sending registration response to dispatcher...");

			EventMessageResponseDispatcher.sendResponse(rr, getMessage());

			LOG.debug("Successfully registered federate [" + fr.getFederateName()
					+ "]");

		} catch (MuthurException me) {

			LOG.error("Error registering federate [" + fr.getFederateName() + "]");

			FederateRegistrationResponse errorResponse =
					new FederateRegistrationResponse();

			errorResponse.initialization(fr.serialize());

			errorResponse.setSourceOfEvent(MessageDestination.MUTHUR);
			errorResponse.setTimeToLiveSecs(30);

			errorResponse.setStatus("complete");
			errorResponse.setSuccess(false);
			errorResponse.setErrorDescription(me.getLocalizedMessage());

			EventMessageResponseDispatcher.sendResponse(errorResponse, getMessage());
		}

	}

}
