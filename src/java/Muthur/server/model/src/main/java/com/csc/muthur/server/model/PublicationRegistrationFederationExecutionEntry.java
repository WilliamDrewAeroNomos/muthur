/**
 * 
 */
package com.csc.muthur.server.model;

import java.util.HashSet;
import java.util.Set;

import javax.jms.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.event.request.RegisterPublicationRequest;

/**
 * 
 * @author <a href=mailto:Nexsim@csc.com>Nexsim</a>
 * @version $Revision$
 */
public class PublicationRegistrationFederationExecutionEntry extends
		AbstractFederationExecutionEntry {

	private static final Logger LOG =
			LoggerFactory
					.getLogger(PublicationRegistrationFederationExecutionEntry.class
							.getName());

	private Set<String> dataTypeNames;

	/**
	 * 
	 * @param federationExecutionModel
	 * @param message
	 * @param registerPublicationRequest
	 * @throws MuthurException
	 */
	public PublicationRegistrationFederationExecutionEntry(
			final FederationExecutionModel federationExecutionModel,
			final Message message,
			final RegisterPublicationRequest registerPublicationRequest)
			throws MuthurException {

		LOG.debug("Creating PublicationRegistrationFederationExecutionEntry...");
		LOG.debug("FederationExecutionModel - [" + federationExecutionModel + "]");
		LOG.debug("Message - [" + message + "]");
		LOG.debug("RegisterPublicationRequest - [" + registerPublicationRequest
				+ "]");
		LOG.debug("Message - [" + message + "]");

		if (federationExecutionModel == null) {
			throw new MuthurException(
					"Error creating PublicationRegistrationFederationExecutionEntry. "
							+ "Federation Execution Model was null.");
		}

		if (message == null) {
			throw new MuthurException(
					"Error creating PublicationRegistrationFederationExecutionEntry. "
							+ "Message was null");
		}

		if (registerPublicationRequest == null) {
			throw new MuthurException(
					"Error creating PublicationRegistrationFederationExecutionEntry. "
							+ "RegisterPublicationRequest was null");
		}

		if ((registerPublicationRequest.getSourceOfEvent() == null)
				|| ("".equals(registerPublicationRequest.getSourceOfEvent()))) {
			throw new MuthurException(
					"Error creating PublicationRegistrationFederationExecutionEntry. "
							+ "Federate name was null.");
		}

		dataTypeNames = registerPublicationRequest.getDataPublications();

		if (dataTypeNames == null) {
			this.dataTypeNames = new HashSet<String>();
		}

		/**
		 * Set each of the AbstractFederationExecutionEntry attributes
		 */
		setFederationExecutionModel(federationExecutionModel);
		setFederateName(registerPublicationRequest.getSourceOfEvent());
		setMessage(message);
		setiEvent(registerPublicationRequest);

		LOG.debug("Created PublicationRegistrationFederationExecutionEntry...");
	}

	/**
	 * @return the dataTypeNames
	 */
	public final Set<String> getDataTypeNames() {
		return this.dataTypeNames;
	}

	/**
	 * @param dataTypeNames
	 *          the dataTypeNames to set
	 */
	public final void setDataTypeNames(Set<String> dataTypeNames) {
		this.dataTypeNames = dataTypeNames;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result =
				prime * result
						+ ((dataTypeNames == null) ? 0 : dataTypeNames.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		PublicationRegistrationFederationExecutionEntry other =
				(PublicationRegistrationFederationExecutionEntry) obj;
		if (dataTypeNames == null) {
			if (other.dataTypeNames != null) {
				return false;
			}
		} else if (!dataTypeNames.equals(other.dataTypeNames)) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PublicationRegistrationFederationExecutionEntry [dataTypeNames="
				+ dataTypeNames + ", getiEvent()=" + getiEvent() + "]";
	}

}
