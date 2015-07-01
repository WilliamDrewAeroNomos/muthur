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
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.ModelService;
import com.csc.muthur.server.model.ReadyToRunFederationExecutionEntry;
import com.csc.muthur.server.model.event.data.IBaseDataObject;
import com.csc.muthur.server.model.event.data.flight.Aircraft;
import com.csc.muthur.server.model.event.request.TransferObjectOwnershipRequest;
import com.csc.muthur.server.model.event.response.TransferObjectOwnershipResponse;
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
public class TransferObjectOwnershipRequestHandler extends
		AbstractObjectEventHandler {

	private final Logger LOG = LoggerFactory
			.getLogger(TransferObjectOwnershipRequestHandler.class.getName());

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
	public TransferObjectOwnershipRequestHandler() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.eventserver.internal.IEventHandler#handle()
	 */
	@Override
	public void handle() throws MuthurException {

		LOG.debug("Handling transfer object ownership request...");

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

		if (!(getEvent() instanceof TransferObjectOwnershipRequest)) {
			throw new MuthurException("Invalid event type encountered. "
					+ "Expected TransferObjectOwnershipRequest");
		}

		try {

			TransferObjectOwnershipRequest request =
					(TransferObjectOwnershipRequest) getEvent();

			LOG.debug("Initializing transfer object ownership request from payload...");

			request.initialization(getEvent().serialize());

			// start handle validations

			// federate registration handle
			if (!registrationService.isFederateRegistered(request
					.getFederateRegistrationHandle())) {
				throw new MuthurException("Received invalid federate registration "
						+ "handle in transfer object ownership request.");
			}

			// federation execution model handle
			if (modelService.getModel(request.getFederationExecutionModelHandle()) == null) {
				throw new MuthurException("Received invalid federation execution "
						+ "model handle in transfer object ownership request.");
			}

			// federation execution handle
			if (!federationService.isValidFederationExecutionHandle(request
					.getFederationExecutionHandle())) {
				throw new MuthurException(
						"Received invalid federation execution handle parameter "
								+ "in transfer object ownership request.");
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
						"Data object ID in TransferObjectOwnershipRequest is null or empty");
			}

			LOG.debug("Getting object ownership ID...");

			ObjectOwnershipID toObjectOwnershipID =
					commonsService.createObjectOwnershipID(
							request.getFederateRegistrationHandle(), federationExecutionID);

			IBaseDataObject dataObjectToTransfer =
					objectService.getDataObject(federationExecutionID,
							request.getDataObjectUUID());

			if (dataObjectToTransfer == null) {
				throw new MuthurException("Data object that was targeted "
						+ "for transfer was not found.");
			}

			LOG.debug("[" + request.getSourceOfEvent()
					+ "] is requesting ownership of [" + dataObjectToTransfer + "]");

			ObjectOwnershipID objectOwnershipID =
					ownershipService.getObjectOwner(dataObjectToTransfer);

			String currentOwnerName = null;

			if (objectOwnershipID != null) {

				String registrationHandle =
						objectOwnershipID.getFederateReqistrationHandle()
								.getRegistrationHandle();

				currentOwnerName =
						registrationService.getFederateName(registrationHandle);
			}

			// if the object is owned but NOT by the requesting owner

			if (ownershipService.isObjectOwned(dataObjectToTransfer
					.getDataObjectUUID())
					&& (!ownershipService.isOwner(toObjectOwnershipID,
							dataObjectToTransfer.getDataObjectUUID()))) {

				// send the request to the current owner to relinquish ownership of the
				// object

				LOG.debug("[" + dataObjectToTransfer.getDataType() + "]-["
						+ dataObjectToTransfer.getNaturalKey()
						+ "] object is currently owned by [" + currentOwnerName
						+ "]. Sending request to [" + currentOwnerName
						+ "] to relinquish ownership to [" + request.getSourceOfEvent()
						+ "]");

				// queue up the request to relinquish ownership and send a request to
				// the
				// owner to relinquish ownership

				ownershipService.queuePendingOwnershipRequest(getEvent(),
						getDataChannelControlBlock());

				FederationExecutionModel fedExecModel =
						modelService.getModel(request.getFederationExecutionModelHandle());

				if (fedExecModel == null) {
					throw new MuthurException("Federation execution model was null. "
							+ "Unable to complete request to transfer control.");
				}

				FederationExecutionController federationExecutionController =
						federationService.getFederationExecutionController(fedExecModel);

				if (federationExecutionController == null) {
					throw new MuthurException(
							"Federation execution controller handle was null. "
									+ "Unable to complete request to transfer control.");
				}

				// get the federate request queue name for the current owner

				ReadyToRunFederationExecutionEntry readyToRunFederationExecEntry =
						federationExecutionController.getReadyToRunRequestProcessor()
								.getFederateNameTofedExecReadyToRunEntry()
								.get(currentOwnerName);

				if (readyToRunFederationExecEntry == null) {
					throw new MuthurException(
							"Unable to find ReadyToRunFederationExecutionEntry "
									+ " for federate [");
				}

				// this is the queue upon which the owning federate receives transfer
				// requests

				String federateRequestQueueName =
						readyToRunFederationExecEntry.getFederateRequestQueueName();

				if ((federateRequestQueueName == null)
						|| ((federateRequestQueueName.length() == 0))) {
					throw new MuthurException("Unable to locate queue in order to send "
							+ "transfer control request to owning federate.");
				}

				// send the request to the owning federate

				routerService.queue(request.serialize(), federateRequestQueueName);

				LOG.debug("Queued request to transfer control of ["
						+ dataObjectToTransfer.getDataType() + "]-["
						+ dataObjectToTransfer.getNaturalKey() + "] from ["
						+ currentOwnerName + "] to [" + request.getSourceOfEvent() + "].");

				// simply return since the response will be received when the current
				// owner responds to the transfer ownership request

			} else { // else the object is not owned

				LOG.debug("[" + dataObjectToTransfer
						+ "] is currently not owned. Setting ownership to ["
						+ request.getSourceOfEvent() + "]");

				Set<IBaseDataObject> setOfObjectsToTransfer = null;

				/**
				 * If it's an Aircraft then perform a cascading action for all objects
				 * related to this aircraft tail number and call sign
				 */
				if (dataObjectToTransfer instanceof Aircraft) {

					setOfObjectsToTransfer =
							objectService.getAircraftObjectGraph(federationExecutionID,
									dataObjectToTransfer.getDataObjectUUID());

				} else {
					/**
					 * ..else simply add this object to the set to be transferred
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

					ownershipService.addObjectOwnership(toObjectOwnershipID, nextObject);
				}

				/**
				 * Send response directly back to requester
				 */

				TransferObjectOwnershipResponse response =
						new TransferObjectOwnershipResponse();

				response.initialization(request.serialize());

				response.setSourceOfEvent("Muthur");
				response.setStatus("complete");
				response.setSuccess(true);

				response.setDataObjectUUID(request.getDataObjectUUID());

				LOG.debug("Sending transfer object ownership response to requestor...");

				routerService.queue(response.serialize(), getDataChannelControlBlock()
						.getReplyToQueueName(), getDataChannelControlBlock()
						.getReplyToQueueName());

				LOG.debug("Transfer object ownership response sent to ["
						+ request.getSourceOfEvent() + "]");

			}

		} catch (MuthurException me) {

			LOG.error("Exception raised handling transfer object ownership request ["
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
