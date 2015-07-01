/**
 * 
 */
package com.csc.muthur.server.eventserver.internal;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atcloud.commons.exception.ATCloudException;
import com.atcloud.model.FEM;
import com.atcloud.model.Federate;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.event.request.ListFedExecModelsRequest;
import com.csc.muthur.server.model.event.response.ListFedExecModelsResponse;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class ListFedExecModelsRequestHandler extends AbstractEventHandler {

	private final Logger LOG = LoggerFactory
			.getLogger(ListFedExecModelsRequestHandler.class.getName());

	/**
	 * 
	 */
	public ListFedExecModelsRequestHandler() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.eventserver.internal.IEventHandler#handle()
	 */
	@Override
	public final void handle() throws MuthurException {

		LOG.debug("Handling list federation execution models request...");

		validateServiceReferences();

		/*
		 * Initialize the request
		 */
		ListFedExecModelsRequest request = new ListFedExecModelsRequest();

		request.initialization(getEvent().serialize());

		try {

			FEM fem = null;

			fem =
					federationExecutionModelService.getActiveFEMForGroup(request
							.getGroupName());

			if (fem == null) {
				throw new MuthurException("Unable to find the federation execution "
						+ "model for group [" + request.getGroupName() + "]");
			}

			FederationExecutionModel federationExecutionModel =
					new FederationExecutionModel();

			/*
			 * Copy the FEM to the FederationExecutionModel
			 */

			federationExecutionModel.setName(fem.getName());
			federationExecutionModel.setDescription(fem.getDescription());
			federationExecutionModel.setFedExecModelUUID(fem.getFemID());
			federationExecutionModel.setLogicalStartTimeMSecs(fem
					.getLogicalStrtTimeMSecs());
			federationExecutionModel.setAutoStart(fem.isAutoStart());
			federationExecutionModel.setDefaultDurationWithinStartupProtocolMSecs(fem
					.getDefDurStrtupPrtclMSecs());
			federationExecutionModel.setDurationFederationExecutionMSecs(fem
					.getFederationExecutionMSecs());
			federationExecutionModel.setDurationJoinFederationMSecs(fem
					.getJoinFederationMSecs());
			federationExecutionModel.setDurationRegisterPublicationMSecs(fem
					.getRegisterPublicationMSecs());
			federationExecutionModel.setDurationRegisterSubscriptionMSecs(fem
					.getRegisterSubscriptionMSecs());
			federationExecutionModel.setDurationRegisterToRunMSecs(fem
					.getRegisterToRunMSecs());
			federationExecutionModel
					.setDurationWaitForStartFederationDirectiveMSecs(fem
							.getWaitForStartMSecs());
			federationExecutionModel.setDurationTimeToWaitAfterTerminationMSecs(fem
					.getWaitTimeAfterTermMSecs());

			List<Federate> federates = null;

			/*
			 * Get all federates associated with this FEM and add to the
			 * FederationExecutionModel
			 */

			federates =
					federationExecutionModelService.getFederatesInFEM(fem.getName());

			if ((federates != null) && (federates.size() > 0)) {
				for (Federate nextFederate : federates) {
					if (nextFederate != null) {
						federationExecutionModel.addRequiredFededrate(nextFederate
								.getName());
					}
				}
			}

			/*
			 * Initialize the response to the event
			 */
			ListFedExecModelsResponse rr = new ListFedExecModelsResponse();

			rr.initialization(getEvent().serialize());

			rr.addFEM(federationExecutionModel);

			rr.setStatus("complete");
			rr.setSuccess(true);

			EventMessageResponseDispatcher.sendResponse(rr, getMessage());

		} catch (ATCloudException | MuthurException e) {

			LOG.error("Error listing federation execution models - ["
					+ e.getLocalizedMessage() + "]");

			ListFedExecModelsResponse errorResponse = new ListFedExecModelsResponse();

			errorResponse.initialization(getEvent().serialize());

			errorResponse.setStatus("complete");
			errorResponse.setSuccess(false);
			errorResponse.setErrorDescription(e.getLocalizedMessage());

			EventMessageResponseDispatcher.sendResponse(errorResponse, getMessage());
		}

	}

}
