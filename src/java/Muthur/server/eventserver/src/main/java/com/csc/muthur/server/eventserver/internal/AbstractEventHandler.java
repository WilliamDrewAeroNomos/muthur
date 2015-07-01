/**
 * 
 */
package com.csc.muthur.server.eventserver.internal;

import javax.jms.Message;

import com.atcloud.fem.FederationExecutionModelService;
import com.csc.muthur.server.commons.CommonsService;
import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.federation.FederationService;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.ModelService;
import com.csc.muthur.server.model.event.response.GeneralErrorResponse;
import com.csc.muthur.server.object.ObjectService;
import com.csc.muthur.server.ownership.OwnershipService;
import com.csc.muthur.server.registration.RegistrationService;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public abstract class AbstractEventHandler implements IEventHandler {

	private IEvent event;

	private Message message;

	private String errorDescription;

	protected RegistrationService registrationServer;
	protected ObjectService objectService;
	protected OwnershipService ownershipService;
	protected FederationService federationService;
	protected ModelService modelService;
	protected CommonsService commonsService;
	protected FederationExecutionModelService federationExecutionModelService;

	/**
	 * 
	 */
	public AbstractEventHandler() {
	}

	/**
	 * Helper function that validates each of the references to the services being
	 * used by this handler.
	 * 
	 * @throws MuthurException
	 */
	protected void validateServiceReferences() throws MuthurException {

		// check reference to registration service
		if (registrationServer == null) {
			throw new MuthurException(
					"RegistrationService service reference was null");
		}
		// check reference to object service
		if (objectService == null) {
			throw new MuthurException("Object service reference was null");
		}
		// check reference to ownership service
		if (ownershipService == null) {
			throw new MuthurException("Ownership service reference was null");
		}
		// check reference to federation service
		if (federationService == null) {
			throw new MuthurException("FederationService service reference was null");
		}
		// check reference to model service
		if (modelService == null) {
			throw new MuthurException("ModelService service reference was null");
		}
		// check reference to common service
		if (commonsService == null) {
			throw new MuthurException("CommonsService service reference was null.");
		}
		// check reference to federation execution model service
		if (federationExecutionModelService == null) {
			throw new MuthurException(
					"FederationExecutionModelService service reference was null.");
		}
	}

	/**
	 * 
	 * @param handle
	 * @return
	 * @throws MuthurException
	 */
	protected FederationExecutionModel getModelFromHandle(final String handle)
			throws MuthurException {

		return modelService.getModel(handle);

	}

	/**
	 * @return the registrationService
	 */
	public RegistrationService getRegistrationServer() {
		return registrationServer;
	}

	/**
	 * @param registrationService
	 *          the registrationService to set
	 */
	@Override
	public void setRegistrationServer(RegistrationService registrationServer) {
		this.registrationServer = registrationServer;
	}

	/**
	 * @param objectService
	 *          the objectService to set
	 */
	@Override
	public void setObjectService(ObjectService objectService) {
		this.objectService = objectService;
	}

	/**
	 * 
	 */
	@Override
	public void setOwnershipService(final OwnershipService ownershipService) {
		this.ownershipService = ownershipService;
	}

	/**
	 * @param federationService
	 *          the federationService to set
	 */
	@Override
	public void setFederationService(FederationService federationService) {
		this.federationService = federationService;
	}

	/**
	 * @param modelServer
	 *          the modelServer to set
	 */
	@Override
	public void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}

	/**
	 * @param commonsService
	 *          the commonsService to set
	 */
	@Override
	public void setCommonsService(CommonsService commonsService) {
		this.commonsService = commonsService;
	}

	/**
	 * @return the federationExecutionModelService
	 */
	public FederationExecutionModelService getFederationExecutionModelService() {
		return federationExecutionModelService;
	}

	/**
	 * @param federationExecutionModelService
	 *          the federationExecutionModelService to set
	 */
	@Override
	public void setFederationExecutionModelService(
			FederationExecutionModelService federationExecutionModelService) {
		this.federationExecutionModelService = federationExecutionModelService;
	}

	@Override
	public void setMessage(final Message m) {
		this.message = m;
	}

	/**
	 * @return Returns the event.
	 */
	public IEvent getEvent() {
		return this.event;
	}

	/**
	 * @return Returns the message.
	 */
	public Message getMessage() {
		return this.message;
	}

	/**
	 * @param event
	 *          the event to set
	 */
	public final void setEvent(IEvent event) {
		this.event = event;
	}

	/**
	 * @param errorDescription
	 *          the errorDescription to set
	 */
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	/**
	 * @return the errorDescription
	 */
	public String getErrorDescription() {
		return errorDescription;
	}

	/**
	 * @throws MuthurException
	 */
	@Override
	public void returnGeneralErrorResponse(final String errorDescription)
			throws MuthurException {
		GeneralErrorResponse ger = new GeneralErrorResponse();
		ger.setSuccess(false);
		ger.setErrorDescription(errorDescription);
		EventMessageResponseDispatcher.sendResponse(ger, getMessage());
	}

}
