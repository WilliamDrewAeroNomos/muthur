/**
 * 
 */
package com.csc.muthur.server.federation.internal.handler;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
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
import com.csc.muthur.server.model.event.data.flight.Aircraft;
import com.csc.muthur.server.model.event.request.DeleteObjectRequest;
import com.csc.muthur.server.model.event.response.DeleteObjectResponse;
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
public class DeleteObjectRequestHandler extends AbstractObjectEventHandler {

	private final Logger LOG = LoggerFactory
			.getLogger(DeleteObjectRequestHandler.class.getName());

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
	public DeleteObjectRequestHandler() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.eventserver.internal.IEventHandler#handle()
	 */
	@Override
	public void handle() throws MuthurException {

		LOG.debug("Handling delete object request...");

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

		if (!(getEvent() instanceof DeleteObjectRequest)) {
			throw new MuthurException("Invalid event type encountered. "
					+ "Expected DeleteObjectRequest");
		}

		try {

			DeleteObjectRequest request = (DeleteObjectRequest) getEvent();

			LOG.debug("Initializing delete object request from payload...");

			request.initialization(getEvent().serialize());

			// start handle validations

			// federate registration handle
			if (!registrationService.isFederateRegistered(request
					.getFederateRegistrationHandle())) {
				throw new MuthurException("Received invalid federate registration "
						+ "handle in delete object request.");
			}

			// federation execution model handle
			if (modelService.getModel(request.getFederationExecutionModelHandle()) == null) {
				throw new MuthurException("Received invalid federation execution "
						+ "model handle in delete object request.");
			}

			// federation execution handle
			if (!federationService.isValidFederationExecutionHandle(request
					.getFederationExecutionHandle())) {
				throw new MuthurException(
						"Received invalid federation execution handle parameter "
								+ "in delete object request.");
			}
			// end of handle validations

			FederationExecutionID federationExecutionID =
					commonsService.createFederationExecutionID(
							request.getFederationExecutionHandle(),
							request.getFederationExecutionModelHandle());

			String dataObjectUUID = request.getDataObjectUUID();

			if ((dataObjectUUID == null) || ("".equalsIgnoreCase(dataObjectUUID))) {
				throw new MuthurException(
						"Data object ID in DeleteObjectRequest is null or empty");
			}

			IBaseDataObject dataObject =
					objectService.getDataObject(federationExecutionID, dataObjectUUID);

			if (dataObject == null) {
				throw new MuthurException(
						"Attempted to delete an object that does not exist ["
								+ dataObjectUUID + "]");
			}

			ObjectOwnershipID objectOwnershipID =
					commonsService.createObjectOwnershipID(
							request.getFederateRegistrationHandle(), federationExecutionID);

			if (ownershipService.isObjectOwned(dataObjectUUID)) {
				if (!ownershipService.isOwner(objectOwnershipID,
						dataObject.getDataObjectUUID())) {
					throw new MuthurException(
							"Attempted to delete an object owned by another user.");
				}
			} else {
				LOG.debug("[" + dataObject + "] is not owned.");
			}

			Set<IBaseDataObject> setOfObjectsToBeDeleted = null;

			/**
			 * If it's an Aircraft then perform a cascading delete for all objects
			 * related to this aircraft tail number and call sign
			 */
			if (dataObject instanceof Aircraft) {

				setOfObjectsToBeDeleted =
						objectService.getAircraftObjectGraph(federationExecutionID,
								dataObjectUUID);

			} else {
				/**
				 * ..else simply add this object to the set to be deleted
				 */

				setOfObjectsToBeDeleted =
						Collections.synchronizedSet(new HashSet<IBaseDataObject>());

				setOfObjectsToBeDeleted.add(dataObject);
			}

			Iterator<IBaseDataObject> iter = setOfObjectsToBeDeleted.iterator();

			while (iter.hasNext()) {

				IBaseDataObject objectToBeDeleted = iter.next();

				if (objectToBeDeleted != null) {

					objectService.deleteDataObject(federationExecutionID,
							objectToBeDeleted.getDataObjectUUID());

					LOG.debug("Deleted object [" + objectToBeDeleted + "]");

					ownershipService.removeObjectOwnership(objectOwnershipID,
							objectToBeDeleted.getDataObjectUUID());

					LOG.debug("Removed ownership for [" + objectToBeDeleted + "]");
				}
			}

			/**
			 * Send response back to sender
			 */
			DeleteObjectResponse response = new DeleteObjectResponse();

			response.initialization(request.serialize());

			response.setStatus("complete");
			response.setSuccess(true);

			response.setDataObjectUUID(dataObjectUUID);

			LOG.debug("Sending delete object response to dispatcher...");

			routerService.queue(response.serialize(), getDataChannelControlBlock()
					.getReplyToQueueName(), getDataChannelControlBlock()
					.getCorrelationID());

			LOG.debug("Delete object response sent to [" + request.getSourceOfEvent()
					+ "]");

			/**
			 * publish the object deletions to any subscribers EXCEPT the federate
			 * that deleted the data
			 */

			iter = setOfObjectsToBeDeleted.iterator();

			while (iter.hasNext()) {

				IBaseDataObject dataObjectToPublish = iter.next();

				if (dataObjectToPublish != null) {

					DataPublicationEvent dp = new DataPublicationEvent();

					dp.setFederationExecutionModelHandle(request
							.getFederationExecutionModelHandle());
					dp.setTimeToLiveSecs(10);
					dp.setSourceOfEvent(request.getSourceOfEvent());
					dp.setDataAction(DataAction.Delete);
					dp.setDataObject(dataObjectToPublish);

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

					LOG.debug("Publishing [" + dp.getDataType().toString()
							+ "] deleted event");

					// fedExecCtrlr.getFederationDataPublicationQueue().put(dp);
					
					fedExecCtrlr.publishData(dp);

				}
			}

		} catch (MuthurException me) {

			LOG.error("Exception raised handling delete object request ["
					+ me.getLocalizedMessage() + "]");

			DeleteObjectResponse errorResponse = new DeleteObjectResponse();

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
