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
import com.csc.muthur.server.commons.DataChannelControlBlock;
import com.csc.muthur.server.commons.FederationExecutionID;
import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.commons.ObjectOwnershipID;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.federation.FederationService;
import com.csc.muthur.server.model.ModelService;
import com.csc.muthur.server.model.TransferOwnershipResponse;
import com.csc.muthur.server.model.event.data.IBaseDataObject;
import com.csc.muthur.server.model.event.data.flight.Aircraft;
import com.csc.muthur.server.model.event.request.TransferObjectOwnershipRequest;
import com.csc.muthur.server.model.event.response.TransferObjectOwnershipResponse;
import com.csc.muthur.server.object.ObjectService;
import com.csc.muthur.server.ownership.OwnershipService;
import com.csc.muthur.server.ownership.PendingFederateRequestEntry;
import com.csc.muthur.server.registration.RegistrationService;
import com.csc.muthur.server.router.RouterService;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class TransferObjectOwnershipResponseHandler extends
		AbstractObjectEventHandler {

	private final Logger LOG = LoggerFactory
			.getLogger(TransferObjectOwnershipResponseHandler.class.getName());

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
	public TransferObjectOwnershipResponseHandler() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.eventserver.internal.IEventHandler#handle()
	 */
	@Override
	public void handle() throws MuthurException {

		LOG.debug("Handling transfer object ownership response...");

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

		if (!(getEvent() instanceof TransferObjectOwnershipResponse)) {
			throw new MuthurException("Invalid event type encountered. "
					+ "Expected TransferObjectOwnershipResponse");
		}

		try {

			TransferObjectOwnershipResponse responseFromOwner =
					(TransferObjectOwnershipResponse) getEvent();

			LOG.debug("Initializing transfer object ownership response from payload...");

			responseFromOwner.initialization(getEvent().serialize());

			// start handle validations

			// federate registration handle
			if (!registrationService.isFederateRegistered(responseFromOwner
					.getFederateRegistrationHandle())) {
				throw new MuthurException("Received invalid federate registration "
						+ "handle in transfer object ownership response.");
			}

			// federation execution model handle
			if (modelService.getModel(responseFromOwner
					.getFederationExecutionModelHandle()) == null) {
				throw new MuthurException("Received invalid federation execution "
						+ "model handle in transfer object ownership response.");
			}

			// federation execution handle
			if (!federationService.isValidFederationExecutionHandle(responseFromOwner
					.getFederationExecutionHandle())) {
				throw new MuthurException(
						"Received invalid federation execution handle parameter "
								+ "in transfer object ownership response.");
			}
			// end of handle validations

			// Get federation execution ID for the original owner...

			FederationExecutionID originalOwnerfederationExecutionID =
					commonsService.createFederationExecutionID(
							responseFromOwner.getFederationExecutionHandle(),
							responseFromOwner.getFederationExecutionModelHandle());

			// Get owner ID for the original owner...

			ObjectOwnershipID originalOwnerOwnershipID =
					commonsService.createObjectOwnershipID(
							responseFromOwner.getFederateRegistrationHandle(),
							originalOwnerfederationExecutionID);

			if ((responseFromOwner.getDataObjectUUID() == null)
					|| ("".equalsIgnoreCase(responseFromOwner.getDataObjectUUID()))) {
				throw new MuthurException(
						"Data object ID in TransferObjectOwnershipResponse is null or empty");
			}

			String originalOwnerName =
					registrationService.getFederateName(responseFromOwner
							.getFederateRegistrationHandle());

			// get the data object to transfer and ensure that still exists

			IBaseDataObject dataObjectToTransfer =
					objectService.getDataObject(originalOwnerfederationExecutionID,
							responseFromOwner.getDataObjectUUID());

			if (dataObjectToTransfer == null) {
				throw new MuthurException("Data object that was targeted "
						+ "for transfer was not found.");
			}

			/*
			 * Get the pending entry which will be the original request
			 */
			PendingFederateRequestEntry pendingFederateRequestEntry =
					ownershipService.removePendingOwnershipReqeust(responseFromOwner);

			if (pendingFederateRequestEntry == null) {
				throw new MuthurException(
						"PendingFederateRequestEntry for transfer response was not found.");
			}

			IEvent pendingEvent = pendingFederateRequestEntry.getEvent();

			if (pendingEvent == null) {
				throw new MuthurException(
						"Matching request for transfer response was not found.");
			}

			TransferObjectOwnershipRequest originalTransferRequest =
					(TransferObjectOwnershipRequest) pendingEvent;

			// get the federation execution ID for the requestor

			FederationExecutionID requestorFederationExecutionID =
					commonsService.createFederationExecutionID(
							originalTransferRequest.getFederationExecutionHandle(),
							originalTransferRequest.getFederationExecutionModelHandle());

			// get the requestor's ownership ID

			ObjectOwnershipID requestorObjectOwnershipID =
					commonsService.createObjectOwnershipID(
							originalTransferRequest.getFederateRegistrationHandle(),
							requestorFederationExecutionID);

			String requestorName =
					registrationService.getFederateName(originalTransferRequest
							.getFederateRegistrationHandle());

			LOG.debug("Retrieved pending request to transfer ["
					+ dataObjectToTransfer + "] to [" + requestorName + "]");

			if (responseFromOwner.getTransferOwnershipResponse() == TransferOwnershipResponse.GRANTED) {

				LOG.debug("Request to transfer ownership of [" + dataObjectToTransfer
						+ "] from [" + originalOwnerName + "] to [" + requestorName
						+ "] was granted.");

				// if the object is no longer owned then simply make the requestor the
				// new
				// owner

				if (!ownershipService.isObjectOwned(dataObjectToTransfer
						.getDataObjectUUID())) {

					addOwnership(originalOwnerfederationExecutionID, originalOwnerName,
							dataObjectToTransfer, requestorObjectOwnershipID, requestorName);

					LOG.debug("[" + dataObjectToTransfer + "] is no longer owned by ["
							+ originalOwnerName + "]. Ownership was given to ["
							+ requestorName + "]");

				} else { // ..if the object is still owned

					// if the requestor is the owner then there's nothing to do

					if (ownershipService.isOwner(requestorObjectOwnershipID,
							dataObjectToTransfer.getDataObjectUUID())) {

						LOG.debug("[" + dataObjectToTransfer + "] is already owned by ["
								+ requestorName
								+ "]. No action required for ownership transfer request.");

					} else {

						// ...transfer to the requesting entity

						ownershipService.transferObjectOwnership(originalOwnerOwnershipID,
								requestorObjectOwnershipID,
								dataObjectToTransfer.getDataObjectUUID());

						LOG.debug("Ownership of [" + dataObjectToTransfer
								+ "] was transferred from [" + originalOwnerName + "] to ["
								+ requestorName + "]");
					}

				}
			} else {

				LOG.debug("Request to transfer ownership of [" + dataObjectToTransfer
						+ "] from [" + originalOwnerName + "] to [" + requestorName
						+ "] was denied.");

			}

			/**
			 * Send a response back to the federate that originated the transfer of
			 * ownership request
			 */

			IEvent originalEvent = pendingFederateRequestEntry.getEvent();

			if (originalEvent == null) {
				throw new MuthurException(
						"Unable to locate the original transfer ownership request.");
			}

			LOG.debug("Sending transfer object ownership response to ["
					+ originalEvent.getSourceOfEvent() + "]...");

			TransferObjectOwnershipResponse transferResponseToOriginalRequest =
					new TransferObjectOwnershipResponse();

			transferResponseToOriginalRequest.initialization(originalEvent
					.serialize());

			DataChannelControlBlock dccBlock =
					pendingFederateRequestEntry.getDccBlock();

			if (dccBlock == null) {
				throw new MuthurException(
						"DataChannelControlBlock from original transfer ownership request was null.");
			}

			routerService.queue(transferResponseToOriginalRequest.serialize(),
					dccBlock.getReplyToQueueName(), dccBlock.getCorrelationID());

			LOG.debug("Transfer object ownership response returned to ["
					+ originalTransferRequest.getSourceOfEvent() + "]");

		} catch (MuthurException me) {

			LOG.error("Exception raised handling transfer object ownership response ["
					+ me.getLocalizedMessage() + "]");

			TransferObjectOwnershipResponse errorResponse =
					new TransferObjectOwnershipResponse();

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
	 * @param originalOwnerfederationExecutionID
	 * @param originalOwnerName
	 * @param dataObjectToTransfer
	 * @param requestorObjectOwnershipID
	 * @param requestorName
	 * @throws MuthurException
	 */
	private void addOwnership(
			FederationExecutionID originalOwnerfederationExecutionID,
			String originalOwnerName, IBaseDataObject dataObjectToTransfer,
			ObjectOwnershipID requestorObjectOwnershipID, String requestorName)
			throws MuthurException {
		LOG.debug("Transferring [" + dataObjectToTransfer + "] from ["
				+ originalOwnerName + "] to [" + requestorName + "]");

		Set<IBaseDataObject> setOfObjectsToTransfer = null;

		/**
		 * If it's an Aircraft then perform a cascading action for all objects
		 * related to this aircraft tail number and call sign
		 */
		if (dataObjectToTransfer instanceof Aircraft) {

			setOfObjectsToTransfer =
					objectService.getAircraftObjectGraph(
							originalOwnerfederationExecutionID,
							dataObjectToTransfer.getDataObjectUUID());

		} else {
			/**
			 * ..else simply add this object to the set to be transfered
			 */

			setOfObjectsToTransfer =
					Collections.synchronizedSet(new HashSet<IBaseDataObject>());

			setOfObjectsToTransfer.add(dataObjectToTransfer);
		}

		/*
		 * iterate over the set of objects and transfer the ownership to the new
		 * owner
		 */
		Iterator<IBaseDataObject> iter = setOfObjectsToTransfer.iterator();

		while (iter.hasNext()) {

			IBaseDataObject nextObject = iter.next();

			ownershipService.addObjectOwnership(requestorObjectOwnershipID,
					nextObject);
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
