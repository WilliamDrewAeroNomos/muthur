/**
 * 
 */
package com.csc.muthur.server.federation.internal.handler;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
import com.csc.muthur.server.model.event.request.RelinquishObjectOwnershipRequest;
import com.csc.muthur.server.model.event.response.RelinquishObjectOwnershipResponse;
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
public class RelinquishObjectOwnershipRequestHandler extends
		AbstractObjectEventHandler {

	private final Logger LOG = LoggerFactory
			.getLogger(RelinquishObjectOwnershipRequestHandler.class.getName());

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
	public RelinquishObjectOwnershipRequestHandler() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.eventserver.internal.IEventHandler#handle()
	 */
	@Override
	public void handle() throws MuthurException {

		LOG.debug("Handling relinquish object ownership request...");

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

		if (!(getEvent() instanceof RelinquishObjectOwnershipRequest)) {
			throw new MuthurException("Invalid event type encountered. "
					+ "Expected RelinquishObjectOwnershipRequest");
		}

		try {

			RelinquishObjectOwnershipRequest request =
					(RelinquishObjectOwnershipRequest) getEvent();

			LOG.debug("Initializing relinquish object ownership request from payload...");

			request.initialization(getEvent().serialize());

			LOG.debug("Getting federation execution ID...");

			FederationExecutionID federationExecutionID =
					commonsService.createFederationExecutionID(
							request.getFederationExecutionHandle(),
							request.getFederationExecutionModelHandle());

			String dataObjectUUID = request.getDataObjectUUID();

			if ((dataObjectUUID == null) || ("".equalsIgnoreCase(dataObjectUUID))) {
				throw new MuthurException(
						"Data object ID in RelinquishObjectOwnershipRequest is null or empty");
			}

			String relinquishingFederateName =
					registrationService.getFederateName(request
							.getFederateRegistrationHandle());

			IBaseDataObject dataObjectToRelinquish =
					objectService.getDataObject(federationExecutionID, dataObjectUUID);

			if (dataObjectToRelinquish == null) {
				throw new MuthurException(
						"["
								+ relinquishingFederateName
								+ "] attempted to relinquish ownership of an object that does not exist");
			}

			LOG.debug("[" + relinquishingFederateName
					+ "] is relinquishing ownership on ["
					+ dataObjectToRelinquish.getDataType().toString() + "]-["
					+ dataObjectToRelinquish.getNaturalKey() + "]...");

			/**
			 * Set of objects that are subject to have their ownership removed.
			 */
			Set<IBaseDataObject> setOfObjectsToRelinquish = null;

			/**
			 * If it's an Aircraft then perform a cascading action for all objects
			 * related to this aircraft tail number and call sign
			 */
			if (dataObjectToRelinquish instanceof Aircraft) {

				setOfObjectsToRelinquish =
						objectService.getAircraftObjectGraph(federationExecutionID,
								dataObjectUUID);

			} else {
				/**
				 * ..else simply add this single object to the set to be relinquished
				 */

				setOfObjectsToRelinquish =
						Collections.synchronizedSet(new HashSet<IBaseDataObject>());

				setOfObjectsToRelinquish.add(dataObjectToRelinquish);
			}

			LOG.debug("Getting object ownership ID...");

			ObjectOwnershipID objectOwnershipID =
					commonsService.createObjectOwnershipID(
							request.getFederateRegistrationHandle(), federationExecutionID);

			Map<String, IBaseDataObject> uuidToObjectsToBeRelinquishedMap =
					new ConcurrentHashMap<String, IBaseDataObject>();

			Iterator<IBaseDataObject> iter = setOfObjectsToRelinquish.iterator();

			while (iter.hasNext()) {

				IBaseDataObject nextObject = iter.next();

				/*
				 * check if the object is owned
				 */
				if (ownershipService.isObjectOwned(nextObject.getDataObjectUUID())) {

					uuidToObjectsToBeRelinquishedMap.put(nextObject.getDataObjectUUID(),
							nextObject);

					LOG.debug("Add [" + nextObject + "] to publication list.");

				} else {
					/*
					 * if the object is NOT owned then just log it and DON'T add it to the
					 * collection of objects whose ownership will be removed
					 */
					LOG.debug("Object [" + nextObject + "] was not owned.");
				}

			}

			/**
			 * Now go over the collection of owned objects and remove the ownership
			 * for each
			 */
			for (IBaseDataObject removeOwnershipObject : uuidToObjectsToBeRelinquishedMap
					.values()) {

				ownershipService.removeObjectOwnership(objectOwnershipID,
						removeOwnershipObject.getDataObjectUUID());

				LOG.debug("Removed [" + relinquishingFederateName + "] as owner of ["
						+ removeOwnershipObject.getDataType().toString() + "]-["
						+ removeOwnershipObject.getNaturalKey() + "]");
			}

			/**
			 * Send response back to requestor
			 * 
			 */
			RelinquishObjectOwnershipResponse response =
					new RelinquishObjectOwnershipResponse();

			response.initialization(request.serialize());

			response.setStatus("complete");
			response.setSuccess(true);

			response.setDataObjectUUID(dataObjectToRelinquish.getDataObjectUUID());

			LOG.debug("Sending relinquish object ownership response to ["
					+ request.getSourceOfEvent() + "]");

			routerService.queue(response.serialize(), getDataChannelControlBlock()
					.getReplyToQueueName(), getDataChannelControlBlock()
					.getCorrelationID());

			LOG.debug("[" + relinquishingFederateName
					+ "] relinquished ownership of ["
					+ dataObjectToRelinquish.getDataType().toString() + "]-["
					+ dataObjectToRelinquish.getNaturalKey() + "].");

			/**
			 * publish the ObjectOwnershipRelinquishedEvent to all subscribers to that
			 * data type
			 */

			for (IBaseDataObject dataObjectToPublish : uuidToObjectsToBeRelinquishedMap
					.values()) {

				if (dataObjectToPublish != null) {

					DataPublicationEvent dp = new DataPublicationEvent();

					dp.setFederationExecutionModelHandle(request
							.getFederationExecutionModelHandle());
					dp.setTimeToLiveSecs(10);
					dp.setSourceOfEvent(request.getSourceOfEvent());
					dp.setDataAction(DataAction.Relinquished);
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

					LOG.debug("Publishing ownership relinquishment by ["
							+ relinquishingFederateName + "] of ["
							+ dataObjectToPublish.getDataType().toString() + "]-["
							+ dataObjectToPublish.getNaturalKey() + "].");

					fedExecCtrlr.publishOwnershipRelinquished(request,
							dataObjectToPublish);

				}
			}

		} catch (MuthurException me) {

			LOG.error("Exception raised handling relinquish object ownership request ["
					+ me.getLocalizedMessage() + "]");

			RelinquishObjectOwnershipResponse errorResponse =
					new RelinquishObjectOwnershipResponse();

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
