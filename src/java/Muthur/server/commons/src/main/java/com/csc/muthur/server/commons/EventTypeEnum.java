/**
 * 
 */
package com.csc.muthur.server.commons;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public enum EventTypeEnum {
	FederateRegistrationRequest(
			"FederateRegistrationRequest"),
	FederateRegistrationResponse(
			"FederateRegistrationResponse"),
	ListFedExecModelsRequest(
			"ListFedExecModelsRequest"),
	ListFedExecModelsResponse(
			"ListFedExecModelsResponse"),
	JoinFederationRequest(
			"JoinFederationRequest"),
	JoinFederationResponse(
			"JoinFederationResponse"),
	DataSubscriptionRequest(
			"DataSubscriptionRequest"),
	DataSubscriptionResponse(
			"DataSubscriptionResponse"),
	DataPublication(
			"DataPublicationEvent"),
	ReadyToRunRequest(
			"ReadyToRunRequest"),
	ReadyToRunResponse(
			"ReadyToRunResponse"),
	FederationTermination(
			"FederationTerminationEvent"),
	DataPublicationRequest(
			"DataPublicationRequest"),
	RegisterPublicationRequest(
			"RegisterPublicationRequest"),
	RegisterPublicationResponse(
			"RegisterPublicationResponse"),
	ExceptionEvent(
			"ExceptionEvent"),
	FederateHeartbeatEvent(
			"FederateHeartbeatEvent"),
	FederateStateEvent(
			"FederateStateEvent"),
	TimeManagementRequest(
			"TimeManagementRequest"),
	StartFederateRequest(
			"StartFederateRequest"),
	StartFederateResponse(
			"StartFederateResponse"),
	ResumeFederateRequest(
			"ResumeFederateRequest"),
	ResumeFederateResponse(
			"ResumeFederateResponse"),
	PauseFederateRequest(
			"PauseFederateRequest"),
	PauseFederateResponse(
			"PauseFederateResponse"),
	StartFederationRequest(
			"StartFederationRequest"),
	StartFederationResponse(
			"StartFederationResponse"),
	ResumeFederationRequest(
			"ResumeFederationRequest"),
	ResumeFederationResponse(
			"ResumeFederationResponse"),
	PauseFederationRequest(
			"PauseFederationRequest"),
	PauseFederationResponse(
			"PauseFederationResponse"),
	CreateObjectRequest(
			"CreateObjectRequest"),
	CreateObjectResponse(
			"CreateObjectResponse"),
	UpdateObjectRequest(
			"UpdateObjectRequest"),
	UpdateObjectResponse(
			"UpdateObjectResponse"),
	DeleteObjectRequest(
			"DeleteObjectRequest"),
	DeleteObjectResponse(
			"DeleteObjectResponse"),
	ObjectOwnershipRequest(
			"ObjectOwnershipRequest"),
	ObjectOwnershipResponse(
			"ObjectOwnershipResponse"),
	TransferObjectOwnershipRequest(
			"TransferObjectOwnershipRequest"),
	TransferObjectOwnershipResponse(
			"TransferObjectOwnershipResponse"),
	ReadObjectRequest(
			"ReadObjectRequest"),
	ReadObjectResponse(
			"ReadObjectResponse"),
	GeneralErrorResponse(
			"GeneralErrorResponse"),
	ObjectOwnershipRelinquishedEvent(
			"ObjectOwnershipRelinquishedEvent"),
	RelinquishObjectOwnershipRequest(
			"RelinquishObjectOwnershipRequest"),
	RelinquishObjectOwnershipResponse(
			"RelinquishObjectOwnershipResponse"),
	FederateDeregistrationEvent(
			"FederateDeregistrationEvent"),
	UpdateObjectAccessControlRequest(
			"UpdateObjectAccessControlRequest"),
	UpdateObjectAccessControlResponse(
			"UpdateObjectAccessControlResponse"),
	CreateOrUpdateObjectRequest(
			"CreateOrUpdateObjectRequest"),
	CreateOrUpdateObjectResponse(
			"CreateOrUpdateObjectResponse"),
	RunwayAssignmentEvent(
			"RunwayAssignmentEvent"),
	UpdateFederationExecutionTimeRequest(
			"UpdateFederationExecutionTimeRequest"),
	UpdateFederationExecutionTimeResponse(
			"UpdateFederationExecutionTimeResponse");

	private String description;

	/**
	 * 
	 * @param description
	 * @param id
	 */
	EventTypeEnum(final String description) {
		this.description = description;
	}

	/**
	 * 
	 * @return short text description of the event
	 */
	public String getDescription() {
		return description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return description;
	}

}
