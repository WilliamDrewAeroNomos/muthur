/**
 * 
 */
package com.csc.muthur.server.model;

import java.util.UUID;

import com.csc.muthur.server.commons.exception.MuthurException;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class FederateRegistration {

	private String federateName;
	private String federationUUID;

	/**
	 * 
	 * @param federateName
	 * @throws MuthurException
	 */
	public FederateRegistration(final String federateName) throws MuthurException {
		super();

		// check parameters
		//
		if (federateName == null) {
			throw new MuthurException(
					"Error creating FederateRegistration. Federate name was null.");
		}
		if ("".equals(federateName)) {
			throw new MuthurException(
					"Error creating FederateRegistration. Federate name was empty.");
		}

		this.federateName = federateName;
		this.federationUUID = UUID.randomUUID().toString();
	}

	/**
	 * @return the federateName
	 */
	public final String getFederateName() {
		return federateName;
	}

	/**
	 * @return the federationUUID
	 */
	public final String getFederationUUID() {
		return federationUUID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result =
				prime * result + ((federateName == null) ? 0 : federateName.hashCode());
		result =
				prime * result
						+ ((federationUUID == null) ? 0 : federationUUID.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		FederateRegistration other = (FederateRegistration) obj;
		if (federateName == null) {
			if (other.federateName != null) {
				return false;
			}
		} else if (!federateName.equals(other.federateName)) {
			return false;
		}
		if (federationUUID == null) {
			if (other.federationUUID != null) {
				return false;
			}
		} else if (!federationUUID.equals(other.federationUUID)) {
			return false;
		}
		return true;
	}

}
