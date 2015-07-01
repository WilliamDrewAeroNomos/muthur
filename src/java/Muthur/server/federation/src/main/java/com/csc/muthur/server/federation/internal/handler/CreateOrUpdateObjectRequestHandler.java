/**
 * 
 */
package com.csc.muthur.server.federation.internal.handler;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.CommonsService;
import com.csc.muthur.server.commons.FederationExecutionID;
import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.commons.ObjectOwnershipID;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.federation.FederationExecutionController;
import com.csc.muthur.server.federation.FederationService;
import com.csc.muthur.server.model.DataAction;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.ModelService;
import com.csc.muthur.server.model.event.DataPublicationEvent;
import com.csc.muthur.server.model.event.data.IBaseDataObject;
import com.csc.muthur.server.model.event.request.CreateOrUpdateObjectRequest;
import com.csc.muthur.server.model.event.response.CreateObjectResponse;
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
public class CreateOrUpdateObjectRequestHandler extends
		AbstractObjectEventHandler {

	private final Logger LOG = LoggerFactory
			.getLogger(CreateOrUpdateObjectRequestHandler.class.getName());

	private RegistrationService registrationService;

	private ModelService modelService;

	private FederationService federationService;

	private ObjectService objectService;

	private OwnershipService ownershipService;

	private CommonsService commonsService;

	private RouterService routerService;

	/**
	 * 
	 */
	public CreateOrUpdateObjectRequestHandler() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.eventserver.internal.IEventHandler#handle()
	 */
	@Override
	public void handle() throws MuthurException {

		LOG.debug("Handling create or update object request...");

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

		DataAction dataAction = null;
		CreateOrUpdateObjectRequest request = null;

		if (!(getEvent() instanceof CreateOrUpdateObjectRequest)) {
			throw new MuthurException("Invalid event type encountered. "
					+ "Expected CreateOrUpdateObjectRequest");
		}

		try {

			request = (CreateOrUpdateObjectRequest) getEvent();

			LOG.debug("Initializing create or update object request from payload...");

			request.initialization(getEvent().serialize());

			// start handle validations

			// federate registration handle
			if (!registrationService.isFederateRegistered(request
					.getFederateRegistrationHandle())) {
				throw new MuthurException("Received invalid federate registration "
						+ "handle in create or update object request.");
			}

			// federation execution model handle
			if (modelService.getModel(request.getFederationExecutionModelHandle()) == null) {
				throw new MuthurException("Received invalid federation execution "
						+ "model handle in create or update object request.");
			}

			// federation execution handle
			if (!federationService.isValidFederationExecutionHandle(request
					.getFederationExecutionHandle())) {
				throw new MuthurException(
						"Received invalid federation execution handle parameter "
								+ "in create or update object request.");
			}
			// end of handle validations

			String federateName =
					registrationService.getFederateName(request
							.getFederateRegistrationHandle());

			LOG.debug("Create or updating object for federate [" + federateName + "]");

			LOG.debug("Getting federation execution ID...");

			FederationExecutionID federationExecutionID =
					commonsService.createFederationExecutionID(
							request.getFederationExecutionHandle(),
							request.getFederationExecutionModelHandle());

			LOG.debug("Getting object ownership ID...");

			ObjectOwnershipID objectOwnershipID =
					commonsService.createObjectOwnershipID(
							request.getFederateRegistrationHandle(), federationExecutionID);

			IBaseDataObject bdo = request.getDataObject();

			if (bdo == null) {
				throw new MuthurException(
						"Object to create in CreateOrUpdateObjectRequest is null");
			}

			if (objectService.objectExists(federationExecutionID, bdo)) {

				dataAction = DataAction.Update;

				// object already exists

				LOG.debug("Updating [" + bdo.getDataType() + "]; Key = "
						+ bdo.getNaturalKey());

				// if this object is not owned or owned by this user then we can simply
				// update without checking attribute access control
				//

				if (!ownershipService.isObjectOwned(bdo.getDataObjectUUID())
						|| (ownershipService.isOwner(objectOwnershipID,
								bdo.getDataObjectUUID()))) {

					objectService.updateObject(federationExecutionID, bdo);

					LOG.debug("Updated [" + bdo.getDataType() + "]; Key = "
							+ bdo.getNaturalKey() + " - [" + bdo + "]");

				} else {

					// else we need to see which attributes are READ_WRITE and then
					// apply
					// updates to those attributes, if any exist

					Set<String> mutableAttributeNames =
							ownershipService
									.getMutableAttributeNames(bdo.getDataObjectUUID());

					if ((mutableAttributeNames != null)
							&& (!mutableAttributeNames.isEmpty())) {

						objectService.updateObjectAttributes(federationExecutionID, bdo,
								mutableAttributeNames);
					} else {
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

			} else {

				dataAction = DataAction.Add;

				// object does not exist so create and set the ownership

				LOG.debug("Creating new [" + bdo.getDataType() + "]; Key = "
						+ bdo.getNaturalKey());

				objectService.addDataObject(federationExecutionID, bdo);

				LOG.debug("Added [" + bdo.toString() + "] data object");

				ownershipService.addObjectOwnership(objectOwnershipID, bdo);

				LOG.debug("Set ownership and default access control for [" + bdo + "]");

			}

			/**
			 * Send response back to sender
			 */
			if (dataAction.equals(DataAction.Add)) {

				CreateObjectResponse response = new CreateObjectResponse();

				response.initialization(request.serialize());

				response.setStatus("complete");
				response.setSuccess(true);

				response.setDataObjectUUID(bdo.getDataObjectUUID());

				LOG.debug("Sending create object response to ["
						+ request.getSourceOfEvent() + "]...");

				routerService.queue(response.serialize(), getDataChannelControlBlock()
						.getReplyToQueueName(), getDataChannelControlBlock()
						.getCorrelationID());

			} else {

				UpdateObjectResponse response = new UpdateObjectResponse();

				response.initialization(request.serialize());

				response.setSourceOfEvent("Muthur");
				response.setStatus("complete");
				response.setSuccess(true);

				LOG.debug("Sending update object response to ["
						+ request.getSourceOfEvent() + "]...");

				routerService.queue(response.serialize(), getDataChannelControlBlock()
						.getReplyToQueueName(), getDataChannelControlBlock()
						.getCorrelationID());

			}

			/**
			 * publish the object addition or update to any subscribers
			 */

			DataPublicationEvent dp = new DataPublicationEvent();

			dp.setFederationExecutionModelHandle(request
					.getFederationExecutionModelHandle());
			dp.setTimeToLiveSecs(10);
			dp.setSourceOfEvent(request.getSourceOfEvent());
			dp.setDataAction(dataAction);
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

			// fedExecCtrlr.getFederationDataPublicationQueue().put(dp);
			
			fedExecCtrlr.publishData(dp);

			if (dataAction.equals(DataAction.Update)) {
				LOG.debug("Publishing updated [" + dp.getDataType().toString() + "]");
			} else {
				LOG.debug("Publishing new [" + dp.getDataType().toString() + "]");
			}

		} catch (MuthurException me) {

			IEvent errorResponse = null;

			if (dataAction.equals(DataAction.Add)) {
				errorResponse = new CreateObjectResponse();
			} else {
				errorResponse = new UpdateObjectResponse();
			}
			LOG.error("Exception raised handling createOrUpdate object request ["
					+ me.getLocalizedMessage() + "]");

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

}
