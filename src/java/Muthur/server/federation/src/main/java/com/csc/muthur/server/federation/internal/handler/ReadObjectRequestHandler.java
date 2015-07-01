/**
 * 
 */
package com.csc.muthur.server.federation.internal.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.CommonsService;
import com.csc.muthur.server.commons.FederationExecutionID;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.event.data.IBaseDataObject;
import com.csc.muthur.server.model.event.request.ReadObjectRequest;
import com.csc.muthur.server.model.event.response.ReadObjectResponse;
import com.csc.muthur.server.object.ObjectService;
import com.csc.muthur.server.router.RouterService;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class ReadObjectRequestHandler extends AbstractObjectEventHandler {

	private final Logger LOG = LoggerFactory
			.getLogger(ReadObjectRequestHandler.class.getName());

	private ObjectService objectService;
	private CommonsService commonsService;
	private RouterService routerService;

	/**
	 * 
	 */
	public ReadObjectRequestHandler() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.eventserver.internal.IEventHandler#handle()
	 */
	@Override
	public void handle() throws MuthurException {

		LOG.debug("Handling read object request...");

		// check reference to object service
		if (objectService == null) {
			throw new MuthurException("Object service reference was null");
		}

		// check reference to common service
		if (commonsService == null) {
			throw new MuthurException("CommonsService service reference was null.");
		}

		// check reference to router service
		if (routerService == null) {
			throw new MuthurException("Router service reference was null");
		}

		if (!(getEvent() instanceof ReadObjectRequest)) {
			throw new MuthurException("Invalid event type encountered. "
					+ "Expected ReadObjectRequest");
		}

		try {

			ReadObjectRequest request = (ReadObjectRequest) getEvent();

			LOG.debug("Initializing read object request from payload...");

			request.initialization(getEvent().serialize());

			LOG.debug("Getting registration handle from request...");

			String registrationHandle = request.getFederateRegistrationHandle();

			if (registrationHandle == null) {
				throw new MuthurException("Federate registration handle is null");
			}

			String dataObjectUUID = request.getDataObjectUUID();

			if (dataObjectUUID == null) {
				throw new MuthurException("Object ID in ReadObjectRequest is null");
			}

			if (objectService == null) {
				throw new MuthurException("Object service reference was null");
			}

			LOG.debug("Getting federation execution ID...");

			FederationExecutionID federationExecutionID =
					commonsService.createFederationExecutionID(
							request.getFederationExecutionHandle(),
							request.getFederationExecutionModelHandle());

			IBaseDataObject dataObject =
					objectService.getDataObject(federationExecutionID, dataObjectUUID);

			/**
			 * Send response back to sender
			 */
			ReadObjectResponse response = new ReadObjectResponse();

			response.initialization(request.serialize());

			response.setSourceOfEvent("Muthur");
			response.setStatus("complete");

			response.setSuccess(true);
			response.setDataObject(dataObject);

			LOG.debug("Sending read object response to dispatcher...");

			routerService.queue(response.serialize(), getDataChannelControlBlock()
					.getReplyToQueueName(), getDataChannelControlBlock()
					.getCorrelationID());

			LOG.debug("Read object response sent to [" + request.getSourceOfEvent()
					+ "]");

		} catch (MuthurException me) {

			LOG.error("Exception raised handling read object request ["
					+ me.getLocalizedMessage() + "]");

			ReadObjectResponse errorResponse = new ReadObjectResponse();

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

}
