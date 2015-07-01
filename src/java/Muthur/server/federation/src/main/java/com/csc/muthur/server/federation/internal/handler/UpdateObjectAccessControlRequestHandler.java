/**
 * 
 */
package com.csc.muthur.server.federation.internal.handler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.CommonsService;
import com.csc.muthur.server.commons.FederationExecutionID;
import com.csc.muthur.server.commons.MessageDestination;
import com.csc.muthur.server.commons.ObjectOwnershipID;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.federation.FederationService;
import com.csc.muthur.server.model.AccessControl;
import com.csc.muthur.server.model.ModelService;
import com.csc.muthur.server.model.event.data.IBaseDataObject;
import com.csc.muthur.server.model.event.request.UpdateObjectAccessControlRequest;
import com.csc.muthur.server.model.event.response.UpdateObjectAccessControlResponse;
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
public class UpdateObjectAccessControlRequestHandler extends
		AbstractObjectEventHandler {

	private final Logger LOG = LoggerFactory
			.getLogger(UpdateObjectAccessControlRequestHandler.class.getName());

	private RegistrationService registrationService;
	private ModelService modelService;
	private FederationService federationService;
	private OwnershipService ownershipService;
	private ObjectService objectService;
	private CommonsService commonsService;
	private RouterService routerService;

	/**
	 * 
	 */
	public UpdateObjectAccessControlRequestHandler() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.eventserver.internal.IEventHandler#handle()
	 */
	@Override
	public void handle() throws MuthurException {

		LOG.debug("Handling updated object access controls request...");

		if (!(getEvent() instanceof UpdateObjectAccessControlRequest)) {
			throw new MuthurException("Invalid event type encountered. "
					+ "Expected UpdateObjectAccessControlRequest");
		}

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

		try {

			UpdateObjectAccessControlRequest request =
					(UpdateObjectAccessControlRequest) getEvent();

			LOG.debug("Initializing update object access control request from payload...");

			request.initialization(getEvent().serialize());

			// start handle validations

			// federate registration handle
			//
			if (!registrationService.isFederateRegistered(request
					.getFederateRegistrationHandle())) {
				throw new MuthurException("Received invalid federate registration "
						+ "handle in update object access control request.");
			}

			// federation execution model handle
			//
			if (modelService.getModel(request.getFederationExecutionModelHandle()) == null) {
				throw new MuthurException("Received invalid federation execution "
						+ "model handle in update object access control request.");
			}

			// federation execution handle
			//
			if (!federationService.isValidFederationExecutionHandle(request
					.getFederationExecutionHandle())) {
				throw new MuthurException(
						"Received invalid federation execution handle parameter "
								+ "in update object access control request.");
			}
			// end of handle validations

			LOG.debug("Getting federation execution ID...");

			FederationExecutionID federationExecutionID =
					commonsService.createFederationExecutionID(
							request.getFederationExecutionHandle(),
							request.getFederationExecutionModelHandle());

			if ((request.getDataObjectUUID() == null)
					|| ("".equalsIgnoreCase(request.getDataObjectUUID()))) {
				throw new MuthurException(
						"Data object ID in UpdateObjectAccessControlRequest is null or empty");
			}

			LOG.debug("Getting object ownership ID...");

			ObjectOwnershipID objectOwnershipID =
					commonsService.createObjectOwnershipID(
							request.getFederateRegistrationHandle(), federationExecutionID);

			IBaseDataObject dataObjectToUpdate =
					objectService.getDataObject(federationExecutionID,
							request.getDataObjectUUID());

			if (dataObjectToUpdate == null) {
				throw new MuthurException(
						"Data object was not found using supplied UUID ["
								+ request.getDataObjectUUID() + "]");
			}

			LOG.debug("[" + request.getSourceOfEvent()
					+ "] updating attribute access controls for ["
					+ dataObjectToUpdate.getDataType() + "] - ["
					+ dataObjectToUpdate.getNaturalKey() + "]");

			// check if the object is NOT owned OR is owned by the requester
			//
			if (!ownershipService.isObjectOwned(dataObjectToUpdate
					.getDataObjectUUID())
					|| (ownershipService.isOwner(objectOwnershipID,
							dataObjectToUpdate.getDataObjectUUID()))) {

				Map<String, AccessControl> fieldNameToAccessControlMap =
						request.getAttributeNameToAccessControlMap();

				ownershipService.updateAttributeAccessControl(objectOwnershipID,
						request.getDataObjectUUID(), fieldNameToAccessControlMap);

				LOG.debug("Updated attributes for [" + dataObjectToUpdate.getDataType()
						+ "]; Key = [" + dataObjectToUpdate.getNaturalKey() + "] - ["
						+ dataObjectToUpdate + "]");

			}

			/**
			 * Send response back to sender
			 */
			UpdateObjectAccessControlResponse response =
					new UpdateObjectAccessControlResponse();

			response.initialization(request.serialize());

			response.setSourceOfEvent(MessageDestination.MUTHUR);
			response.setStatus("complete");
			response.setSuccess(true);

			response.setDataObjectUUID(request.getDataObjectUUID());

			LOG.debug("Sending update object access control response to request...");

			routerService.queue(response.serialize(), getDataChannelControlBlock()
					.getReplyToQueueName(), getDataChannelControlBlock()
					.getCorrelationID());

			LOG.debug("Update object access control response sent to ["
					+ request.getSourceOfEvent() + "]");

		} catch (MuthurException me) {

			LOG.error("Exception raised handling update object access control request ["
					+ me.getLocalizedMessage() + "]");

			UpdateObjectAccessControlResponse errorResponse =
					new UpdateObjectAccessControlResponse();

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
