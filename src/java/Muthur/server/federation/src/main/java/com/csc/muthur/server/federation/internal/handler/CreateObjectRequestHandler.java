/**
 * 
 */
package com.csc.muthur.server.federation.internal.handler;

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
import com.csc.muthur.server.model.event.request.CreateObjectRequest;
import com.csc.muthur.server.model.event.response.CreateObjectResponse;
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
public class CreateObjectRequestHandler extends AbstractObjectEventHandler {

	private final Logger LOG = LoggerFactory
			.getLogger(CreateObjectRequestHandler.class.getName());

	private RegistrationService registrationService;
	private ModelService modelService;
	private FederationService federationService;
	private ObjectService objectService;
	private CommonsService commonsService;
	private OwnershipService ownershipService;
	private RouterService routerService;

	/**
	 * 
	 */
	public CreateObjectRequestHandler() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.eventserver.internal.IEventHandler#handle()
	 */
	@Override
	public void handle() throws MuthurException {

		LOG.debug("Handling create object request...");

		if (!(getEvent() instanceof CreateObjectRequest)) {
			throw new MuthurException("Invalid event type encountered. "
					+ "Expected CreateObjectRequest");
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

		CreateObjectRequest request = null;

		try {

			request = (CreateObjectRequest) getEvent();

			LOG.debug("Initializing create object request from payload...");

			request.initialization(getEvent().serialize());

			// start handle validations

			// federate registration handle
			if (!getRegistrationService().isFederateRegistered(
					request.getFederateRegistrationHandle())) {
				throw new MuthurException("Received invalid federate registration "
						+ "handle in create object request.");
			}

			// federation execution model handle
			if (modelService.getModel(request.getFederationExecutionModelHandle()) == null) {
				throw new MuthurException("Received invalid federation execution "
						+ "model handle in create object request.");
			}

			// federation execution handle
			if (!federationService.isValidFederationExecutionHandle(request
					.getFederationExecutionHandle())) {
				throw new MuthurException(
						"Received invalid federation execution handle parameter "
								+ "in create object request.");
			}
			// end of handle validations

			String federateName =
					getRegistrationService().getFederateName(
							request.getFederateRegistrationHandle());

			LOG.debug("Adding object for federate [" + federateName + "]");

			LOG.debug("Getting federation execution ID...");

			FederationExecutionID federationExecutionID =
					commonsService.createFederationExecutionID(
							request.getFederationExecutionHandle(),
							request.getFederationExecutionModelHandle());

			LOG.debug("Getting data object...");

			IBaseDataObject bdo = request.getDataObject();

			if (bdo == null) {
				throw new MuthurException(
						"Object to create in CreateObjectRequest is null");
			}

			LOG.debug("Checking if [" + bdo.getDataType() + "] already exists...");

			if (objectService.objectExists(federationExecutionID, bdo)) {
				throw new MuthurException(bdo.getDataType() + " with key ["
						+ bdo.getNaturalKey() + "] already exists");
			}

			LOG.debug("Adding [" + bdo.toString() + "] data object...");

			objectService.addDataObject(federationExecutionID, bdo);

			LOG.debug("Getting object ownership ID...");

			ObjectOwnershipID objectOwnershipID =
					commonsService.createObjectOwnershipID(
							request.getFederateRegistrationHandle(), federationExecutionID);

			LOG.debug("Setting [" + federateName + "] as owner of ["
					+ bdo.getDataType() + "]-[" + bdo.getNaturalKey()
					+ "] data object...");

			ownershipService.addObjectOwnership(objectOwnershipID, bdo);

			LOG.debug("Set ownership and default access control for ["
					+ bdo.getDataType() + "]-[" + bdo.getNaturalKey() + "]");

			LOG.debug("Send response back to sender...");

			CreateObjectResponse response = new CreateObjectResponse();

			LOG.debug("Initialize response from request...");

			response.initialization(request.serialize());

			response.setStatus("complete");
			response.setSuccess(true);

			LOG.debug("Setting data object UUID...");

			response.setDataObjectUUID(bdo.getDataObjectUUID());

			LOG.debug("Queueing create object response for federate [" + federateName
					+ "] on reply queue ["
					+ getDataChannelControlBlock().getReplyToQueueName() + "]");

			routerService.queue(response.serialize(), getDataChannelControlBlock()
					.getReplyToQueueName(), getDataChannelControlBlock()
					.getCorrelationID());

			LOG.debug("Queued create object response on reply queue ["
					+ getDataChannelControlBlock().getReplyToQueueName()
					+ "] for federate [" + federateName + "]");

			LOG.debug("Publish the addition of [" + bdo.getDataType() + "]-["
					+ bdo.getNaturalKey() + "] to subscribers...");

			DataPublicationEvent dp = new DataPublicationEvent();

			dp.setFederationExecutionModelHandle(request
					.getFederationExecutionModelHandle());
			dp.setTimeToLiveSecs(10);
			dp.setSourceOfEvent(request.getSourceOfEvent());
			dp.setDataAction(DataAction.Add);
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

			LOG.debug("Publishing [" + dp.getDataType().toString()
					+ "] created event...");

			// fedExecCtrlr.getFederationDataPublicationQueue().put(dp);

			fedExecCtrlr.publishData(dp);
			
			LOG.debug("Published the addition of [" + bdo.getDataType() + "]-["
					+ bdo.getNaturalKey() + "] to subscribers.");

			fedExecCtrlr.getSimulationExecutionMetrics().addDataEvent(dp);

			LOG.debug("Completed CreateObjectRequestHandler processing of ["
					+ bdo.getDataType() + "]-[" + bdo.getNaturalKey() + "]");

		} catch (MuthurException me) {

			LOG.error("Exception raised handling create object request ["
					+ me.getLocalizedMessage() + "]");

			CreateObjectResponse errorResponse = new CreateObjectResponse();

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
}
