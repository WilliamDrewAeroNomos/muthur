/**
 * 
 */
package com.csc.muthur.server.eventserver.internal;

import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.MessageDestination;
import com.csc.muthur.server.commons.ServiceName;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.federation.FederationExecutionController;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.event.FederateDeregistrationEvent;
import com.csc.muthur.server.model.event.FederationTerminationEvent;
import com.csc.muthur.server.router.RouterService;

/**
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class FederateDeregistrationEventHandler extends AbstractEventHandler {

	private static final Logger LOG = LoggerFactory
			.getLogger(FederateDeregistrationEventHandler.class.getName());

	public RouterService routerService;

	/**
	 * 
	 */
	public FederateDeregistrationEventHandler() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.eventserver.internal.IEventHandler#handle()
	 */
	@Override
	public void handle() throws MuthurException {

		validateServiceReferences();

		if (routerService == null) {
			throw new MuthurException(
					"Unable to process federate deregistration event. Router service was null.");
		}

		FederateDeregistrationEvent request = new FederateDeregistrationEvent();

		request.initialization(getEvent().serialize());

		String federateName = request.getFederateName();

		if ((federateName == null) || (federateName.length() == 0)) {
			throw new MuthurException(
					"Unable to process federate deregistration event. Federate name was null or empty.");
		}

		LOG.debug("Processing deregistration event for federate [" + federateName
				+ "] sent by [" + request.getSourceOfEvent() + "]");

		getRegistrationServer().deregisterFederate(request.getFederateName());

		/*
		 * Send a termination message to each running federation that has this
		 * federate name as a required federate
		 */
		Map<FederationExecutionModel, FederationExecutionController> modelToFedExecCtrlMap =
				federationService.getFedExecModelToFedExecCtrlrMap();

		if (modelToFedExecCtrlMap != null) {

			Collection<FederationExecutionController> fedExecCtrlrs =
					modelToFedExecCtrlMap.values();

			if (fedExecCtrlrs != null) {

				/**
				 * for each federation that the federate is required send a termination
				 * event to the event service which will shutdown the federation
				 * execution
				 */
				for (FederationExecutionController fedExecCtrl : fedExecCtrlrs) {

					if (fedExecCtrl != null) {

						if (fedExecCtrl.getFederationExecutionModel().isRequired(
								request.getFederateName())) {

							String terminationReason =
									"Federation ["
											+ fedExecCtrl.getFederationExecutionModel().getName()
											+ "] being terminated due to the required federate ["
											+ request.getFederateName() + "] terminating.";

							LOG.debug(terminationReason);

							FederationTerminationEvent fte = new FederationTerminationEvent();

							fte.setFederationExecutionHandle(fedExecCtrl
									.getFederationExecutionHandle());
							fte.setFederationExecutionModelHandle(fedExecCtrl
									.getFederationExecutionModel().getFedExecModelUUID());

							fte.setSourceOfEvent(MessageDestination.MUTHUR);
							fte.setTerminationReason(terminationReason);

							try {
								routerService.postEvent(fte.serialize(), ServiceName.EVENT);
							} catch (Throwable t) {
								LOG.error(t.getLocalizedMessage());
							}
						}
					}
				}
			}
		}

	}

	/**
	 * @param routerService
	 *          the routerService to set
	 */
	public final void setRouterService(RouterService routerService) {
		this.routerService = routerService;
	}

}
