/**
 * 
 */
package com.csc.muthur.server.commons.internal;

import com.csc.muthur.server.commons.FederateRegistrationHandle;
import com.csc.muthur.server.commons.FederationExecutionID;
import com.csc.muthur.server.commons.ObjectOwnershipID;
import com.csc.muthur.server.commons.exception.MuthurException;

/**
 * Used to identify those objects owned by a specific registered user within an
 * executing federation.
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public class ObjectOwnershipIDImpl implements ObjectOwnershipID {

	private FederationExecutionID federationExecutionID;
	private FederateRegistrationHandle federateReqistrationHandle;

	/**
	 * @param federationExecutionID
	 * @param federateReqistrationHandle
	 * @throws MuthurException
	 */
	public ObjectOwnershipIDImpl(
			final FederationExecutionID federationExecutionID,
			final FederateRegistrationHandle federateReqistrationHandle)
			throws MuthurException {
		super();

		if (federationExecutionID == null) {
			throw new MuthurException("FederationExecutionID is null");
		}

		if (federateReqistrationHandle == null) {
			throw new MuthurException("FederateRegistrationHandle is null");
		}

		this.federationExecutionID = federationExecutionID;
		this.federateReqistrationHandle = federateReqistrationHandle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.commons.internal.ObjectOwnershipID#getFederationExecutionID
	 * ()
	 */
	public FederationExecutionID getFederationExecutionID() {
		return federationExecutionID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.commons.internal.ObjectOwnershipID#getFederateReqistrationHandle
	 * ()
	 */
	public FederateRegistrationHandle getFederateReqistrationHandle() {
		return federateReqistrationHandle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result =
				prime
						* result
						+ ((federateReqistrationHandle == null) ? 0
								: federateReqistrationHandle.hashCode());
		result =
				prime
						* result
						+ ((federationExecutionID == null) ? 0 : federationExecutionID
								.hashCode());
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
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ObjectOwnershipID other = (ObjectOwnershipID) obj;
		if (federateReqistrationHandle == null) {
			if (other.getFederateReqistrationHandle() != null) {
				return false;
			}
		} else if (!federateReqistrationHandle.equals(other
				.getFederateReqistrationHandle())) {
			return false;
		}
		if (federationExecutionID == null) {
			if (other.getFederationExecutionID() != null) {
				return false;
			}
		} else if (!federationExecutionID.equals(other.getFederationExecutionID())) {
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
		return "ObjectOwnershipID [federationExecutionID=" + federationExecutionID
				+ ", federateReqistrationHandle=" + federateReqistrationHandle + "]";
	}
}
