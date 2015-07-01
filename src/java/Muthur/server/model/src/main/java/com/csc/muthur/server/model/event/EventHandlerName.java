/**
 * 
 */
package com.csc.muthur.server.model.event;

/**
 * @author <a href=mailto:Nexsim@foo.com>Nexsim</a>
 * @version $Revision$
 */
public enum EventHandlerName {
	registrationRequestHandler,
	listFedExecModelsRequestHandler,
	joinFederationRequestHandler,
	dataSubscriptionRequestHandler,
	readyToRunRequestHandler,
	dataPublicationHandler,
	terminateFederationEventHandler,
	defaultEventHandler,
	startFederateRequestHandler,
	resumeFederateRequestHandler,
	pauseFederateRequestHandler,
	startFederationRequestHandler,
	pauseFederationRequestHandler,
	resumeFederationRequestHandler,
	startFederateResponseHandler,
	resumeFederateResponseHandler,
	pauseFederateResponseHandler,
	startFederationResponseHandler,
	resumeFederationResponseHandler,
	pauseFederationResponseHandler,
	createObjectRequestHandler,
	updateObjectRequestHandler,
	deleteObjectRequestHandler,
	objectOwnershipRequestHandler,
	transferObjectOwnershipRequestHandler,
	transferObjectOwnershipResponseHandler,
	readObjectRequestHandler,
	relinquishObjectOwnershipRequestHandler,
	relinquishObjectOwnershipResponseHandler,
	federateDeregistrationEventHandler,
	updateObjectAccessControlHandler,
	updateObjectAccessControlResponseHandler,
	createOrUpdateObjectRequestHandler,
	runwayAssignmentEventHandler,
	updateFederationExecutionTimeRequestHandler,
	updateFederationExecutionTimeResponseHandler;
}
