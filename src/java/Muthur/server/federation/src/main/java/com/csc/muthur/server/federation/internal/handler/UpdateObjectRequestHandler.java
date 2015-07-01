/**
 * 
 */
package com.csc.muthur.server.federation.internal.handler;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.CommonsService;
import com.csc.muthur.server.commons.FederationExecutionID;
import com.csc.muthur.server.commons.ObjectOwnershipID;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.federation.FederationExecutionController;
import com.csc.muthur.server.federation.FederationService;
import com.csc.muthur.server.model.DataAction;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.ModelService;
import com.csc.muthur.server.model.event.DataPublicationEvent;
import com.csc.muthur.server.model.event.data.IBaseDataObject;
import com.csc.muthur.server.model.event.request.UpdateObjectRequest;
import com.csc.muthur.server.model.event.response.UpdateObjectResponse;
import com.csc.muthur.server.object.ObjectService;
import com.csc.muthur.server.ownership.OwnershipService;
import com.csc.muthur.server.registration.RegistrationService;
import com.csc.muthur.server.router.RouterService;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class UpdateObjectRequestHandler extends AbstractObjectEventHandler {

	private final Logger LOG = LoggerFactory
			.getLogger(UpdateObjectRequestHandler.class.getName());

	private RegistrationService registrationService;
	private ModelService modelService;
	private FederationService federationService;
	private CommonsService commonsService;
	private OwnershipService ownershipService;
	private ObjectService objectService;
	private RouterService routerService;

	/**
	 * 
	 */
	public UpdateObjectRequestHandler() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.eventserver.internal.IEventHandler#handle()
	 */
	@Override
	public void handle() throws MuthurException {

		LOG.debug("Handling update object request...");

		// check reference to registration service
		if (registrationService == null) {
			throw new MuthurException(
					"RegistrationService service reference was null");
		}

		// check reference to model service
		if (modelService == null) {
			throw new MuthurException("ModelService service reference was null");
		}

		// check reference to federation service
		if (federationService == null) {
			throw new MuthurException("FederationService service reference was null");
		}

		// check reference to object service
		if (objectService == null) {
			throw new MuthurException("Object service reference was null");
		}

		// check reference to common service
		if (commonsService == null) {
			throw new MuthurException("CommonsService service reference was null.");
		}

		// check reference to ownership service
		if (ownershipService == null) {
			throw new MuthurException("Ownership service reference was null");
		}

		// check reference to router service
		if (routerService == null) {
			throw new MuthurException("Router service reference was null");
		}

		if (!(getEvent() instanceof UpdateObjectRequest)) {
			throw new MuthurException("Invalid event type encountered. "
					+ "Expected UpdateObjectRequest");
		}

		UpdateObjectRequest request = null;

		try {

			request = (UpdateObjectRequest) getEvent();

			LOG.debug("Initializing update object request from payload...");

			request.initialization(getEvent().serialize());

			// start handle validations

			// federate registration handle
			if (!registrationService.isFederateRegistered(request
					.getFederateRegistrationHandle())) {
				throw new MuthurException("Received invalid federate registration "
						+ "handle in update object request.");
			}

			// federation execution model handle
			if (modelService.getModel(request.getFederationExecutionModelHandle()) == null) {
				throw new MuthurException("Received invalid federation execution "
						+ "model handle in update object request.");
			}

			// federation execution handle
			if (!federationService.isValidFederationExecutionHandle(request
					.getFederationExecutionHandle())) {
				throw new MuthurException(
						"Received invalid federation execution handle parameter "
								+ "in update object request.");
			}
			// end of handle validations

			// retrieve registered federate name for diagnostic purposes

			String federateName =
					registrationService.getFederateName(request
							.getFederateRegistrationHandle());

			LOG.debug("Updating object for federate [" + federateName + "]");

			LOG.debug("Getting federation execution ID...");

			FederationExecutionID federationExecutionID =
					commonsService.createFederationExecutionID(
							request.getFederationExecutionHandle(),
							request.getFederationExecutionModelHandle());

			LOG.debug("Getting object ownership ID...");

			ObjectOwnershipID objectOwnershipID =
					commonsService.createObjectOwnershipID(
							request.getFederateRegistrationHandle(), federationExecutionID);

			LOG.debug("Getting data object...");

			IBaseDataObject bdo = request.getDataObject();

			if (bdo == null) {
				throw new MuthurException(
						"Object passed in UpdateObjectRequest is null");
			}

			LOG.debug("Updating [" + bdo.getDataType() + "] - ["
					+ bdo.getNaturalKey() + "]");

			// if this object is not owned or owned by this user then we can simply
			// update without checking attribute access control
			//

			if (!ownershipService.isObjectOwned(bdo.getDataObjectUUID())
					|| (ownershipService.isOwner(objectOwnershipID,
							bdo.getDataObjectUUID()))) {

				LOG.debug("Federate [" + federateName + "] is updating ["
						+ bdo.getDataType() + "] - [" + bdo.getNaturalKey() + "]");

				objectService.updateObject(federationExecutionID, bdo);

				LOG.debug("Federate [" + federateName + "] updated ["
						+ bdo.getDataType() + "] - [" + bdo.getNaturalKey() + "] - [" + bdo
						+ "]");

			} else {

				// else we need to see which attributes are READ_WRITE and then
				// apply
				// updates to those attributes, if any exist

				Set<String> mutableAttributeNames =
						ownershipService.getMutableAttributeNames(bdo.getDataObjectUUID());

				if ((mutableAttributeNames != null)
						&& (!mutableAttributeNames.isEmpty())) {

					objectService.updateObjectAttributes(federationExecutionID, bdo,
							mutableAttributeNames);

				} else {
					LOG.warn("Federate [" + federateName
							+ "] attempted to update an object it did not own");

					throw new MuthurException(
							"Federate ["
									+ federateName
									+ "] attempted to update ["
									+ bdo.getDataType()
									+ "]; Key = "
									+ bdo.getNaturalKey()
									+ "] - [ which it did not own nor did the object have any mutable attributes");
				}

			}

			LOG.debug("Sending response back to [" + federateName + "]...");

			UpdateObjectResponse response = new UpdateObjectResponse();

			LOG.debug("Initializing response from request...");

			response.initialization(request.serialize());

			response.setSourceOfEvent("Muthur");
			response.setStatus("complete");
			response.setSuccess(true);

			LOG.debug("Queueing update object response for federate [" + federateName
					+ "] on reply queue ["
					+ getDataChannelControlBlock().getReplyToQueueName() + "]");

			routerService.queue(response.serialize(), getDataChannelControlBlock()
					.getReplyToQueueName(), getDataChannelControlBlock()
					.getCorrelationID());

			LOG.debug("Queued update object response on reply queue ["
					+ getDataChannelControlBlock().getReplyToQueueName()
					+ "] for federate [" + federateName + "]");

			LOG.debug("Publish the object update to any subscribers...");

			DataPublicationEvent dp = new DataPublicationEvent();

			dp.setFederationExecutionModelHandle(request
					.getFederationExecutionModelHandle());
			dp.setTimeToLiveSecs(10);
			dp.setSourceOfEvent(request.getSourceOfEvent());
			dp.setDataAction(DataAction.Update);
			dp.setDataObject(bdo);

			FederationExecutionModel model =
					modelService.getModel(dp.getFederationExecutionModelHandle());

			if (model == null) {
				throw new MuthurException(
						"Unable to retrieve the FederationExecutionModel "
								+ "object using the supplied federation execution model handle.");
			}

			FederationExecutionController fedExecCtrlr =
					federationService.getFederationExecutionController(model);

			if (fedExecCtrlr == null) {
				throw new MuthurException(
						"Unable to retrieve federation execution controller.");
			}

			LOG.debug("Publish the update of [" + bdo.getDataType() + "]-["
					+ bdo.getNaturalKey() + "] to subscribers...");

			// fedExecCtrlr.getFederationDataPublicationQueue().put(dp);

			fedExecCtrlr.publishData(dp);

			LOG.debug("Published the update of [" + bdo.getDataType() + "]-["
					+ bdo.getNaturalKey() + "] to subscribers.");

			fedExecCtrlr.getSimulationExecutionMetrics().addDataEvent(dp);

		} catch (MuthurException me) {

			LOG.error("Exception raised handling update object request ["
					+ me.getLocalizedMessage() + "]");

			UpdateObjectResponse errorResponse = new UpdateObjectResponse();

			errorResponse.initialization(getEvent().serialize());

			errorResponse.setStatus("complete");
			errorResponse.setSuccess(false);
			errorResponse.setErrorDescription(me.getLocalizedMessage());

			getRouterService().queue(errorResponse.serialize(),
					getDataChannelControlBlock().getReplyToQueueName(),
					getDataChannelControlBlock().getCorrelationID());
		}
	}

	/**
	 * @return the registrationService
	 */
	public RegistrationService getRegistrationService() {
		return registrationService;
	}

	/**
	 * @param registrationService
	 *          the registrationService to set
	 */
	public void setRegistrationService(RegistrationService registrationService) {
		this.registrationService = registrationService;
	}

	/**
	 * @return the routerService
	 */
	public RouterService getRouterService() {
		return routerService;
	}

	/**
	 * @param routerService
	 *          the routerService to set
	 */
	public void setRouterService(RouterService routerService) {
		this.routerService = routerService;
	}

	/**
	 * @return the modelService
	 */
	public ModelService getModelService() {
		return modelService;
	}

	/**
	 * @param modelService
	 *          the modelService to set
	 */
	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}

	/**
	 * @return the federationService
	 */
	public FederationService getFederationService() {
		return federationService;
	}

	/**
	 * @param federationService
	 *          the federationService to set
	 */
	public void setFederationService(FederationService federationService) {
		this.federationService = federationService;
	}

	/**
	 * @return the commonsService
	 */
	public CommonsService getCommonsService() {
		return commonsService;
	}

	/**
	 * @param commonsService
	 *          the commonsService to set
	 */
	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	/**
	 * @return the ownershipService
	 */
	public OwnershipService getOwnershipService() {
		return ownershipService;
	}

	/**
	 * @param ownershipService
	 *          the ownershipService to set
	 */
	public void setOwnershipService(OwnershipService ownershipService) {
		this.ownershipService = ownershipService;
	}

	/**
	 * @return the objectService
	 */
	public ObjectService getObjectService() {
		return objectService;
	}

	/**
	 * @param objectService
	 *          the objectService to set
	 */
	public void setObjectService(ObjectService objectService) {
		this.objectService = objectService;
	}
}
