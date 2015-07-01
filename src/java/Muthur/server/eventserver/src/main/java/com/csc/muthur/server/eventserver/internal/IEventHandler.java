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
import com.csc.muthur.server.model.ModelService;
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
public interface IEventHandler {

	/**
	 * 
	 * @throws Exception
	 */
	public abstract void handle() throws MuthurException;

	/**
	 * 
	 * @param m
	 */
	public abstract void setMessage(final Message m);

	/**
	 * @return Returns the event.
	 */
	public abstract IEvent getEvent();

	/**
	 * @return Returns the message.
	 */
	public abstract Message getMessage();

	/**
	 * 
	 * @param event
	 */
	public abstract void setEvent(IEvent event);

	public abstract void
			returnGeneralErrorResponse(final String errorDescription)
					throws MuthurException;

	public abstract void setFederationExecutionModelService(
			FederationExecutionModelService federationExecutionModelService);

	public abstract void setCommonsService(CommonsService commonsService);

	public abstract void setModelService(ModelService modelService);

	public abstract void
			setFederationService(FederationService federationService);

	public abstract void setOwnershipService(
			final OwnershipService ownershipService);

	public abstract void setObjectService(ObjectService objectService);

	public abstract void setRegistrationServer(
			RegistrationService registrationServer);
}