/**
 * 
 */
package com.csc.muthur.server.eventserver.internal;

import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.FederationExecutionID;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.FederationExecutionTermination;
import com.csc.muthur.server.model.event.FederationTerminationEvent;
import com.csc.muthur.server.time.TimeService;

/**
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class TerminateFederationEventHandler extends AbstractEventHandler {

	private static final Logger LOG = LoggerFactory
			.getLogger(TerminateFederationEventHandler.class.getName());

	private TimeService timeService;

	/**
	 * 
	 */
	public TerminateFederationEventHandler() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.eventserver.internal.IEventHandler#handle()
	 */
	@Override
	public void handle() throws MuthurException {

		LOG.debug("Processing terminate federation event...");

		validateServiceReferences();

		if (timeService == null) {
			throw new MuthurException("Time service reference was null");
		}

		TextMessage tm = (TextMessage) getMessage();

		FederationTerminationEvent request = new FederationTerminationEvent();

		request.initialization(getEvent().serialize());

		if (request.getFederationExecutionModelHandle() == null) {
			throw new MuthurException(
					"Handle to the federation execution model was null.");
		}

		FederationExecutionModel model = getModelFromHandle(request
				.getFederationExecutionModelHandle());

		if (model == null) {
			throw new MuthurException(
					"Federation termination event was sent with an invalid federation execution model");
		}

		LOG.debug("Received a federation termination event for federation ["
				+ model.getName() + "] from [" + request.getSourceOfEvent() + "]");

		FederationExecutionTermination fedExecTermination = new FederationExecutionTermination(
				model, tm, request);

		federationService.terminateFederationExecution(fedExecTermination);

		LOG.debug("Federation termination event sent from ["
				+ request.getSourceOfEvent() + "] has terminated federation ["
				+ model.getName() + "]");

		FederationExecutionID federationExecutionID = commonsService
				.createFederationExecutionID(request.getFederationExecutionHandle(),
						request.getFederationExecutionModelHandle());

		LOG.debug("Shutting down federation clock...");

		timeService.destroyFederateClockManager(model);

		LOG.debug("Removing all objects contained in the federation ["
				+ model.getName() + "]");

		objectService.deleteObjects(federationExecutionID);

		LOG.debug("Removing all ownership records related to the federation ["
				+ model.getName() + "]");

		ownershipService.removeObjectOwnerships(federationExecutionID);

		LOG.debug("Request to terminate federation [" + model.getName()
				+ "] processed.");
	}

	/**
	 * @param timeService
	 *          the timeService to set
	 */
	public void setTimeService(TimeService timeService) {
		this.timeService = timeService;
	}

}
