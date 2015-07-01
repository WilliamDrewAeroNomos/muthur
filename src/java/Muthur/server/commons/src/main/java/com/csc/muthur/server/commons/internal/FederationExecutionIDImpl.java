/**
 * 
 */
package com.csc.muthur.server.commons.internal;

import com.csc.muthur.server.commons.FederationExecutionHandle;
import com.csc.muthur.server.commons.FederationExecutionID;
import com.csc.muthur.server.commons.FederationExecutionModelHandle;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public final class FederationExecutionIDImpl implements FederationExecutionID {

	public FederationExecutionHandle federationExecutionHandle;
	public FederationExecutionModelHandle federationExecutionModelHandle;

	/**
	 * @param federationExecutionHandle
	 * @param federationExecutionModelHandle
	 */
	public FederationExecutionIDImpl(
			FederationExecutionHandle federationExecutionHandle,
			FederationExecutionModelHandle federationExecutionModelHandle) {
		super();
		this.federationExecutionHandle = federationExecutionHandle;
		this.federationExecutionModelHandle = federationExecutionModelHandle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.commons.internal.FederationExecutionID#getID()
	 */
	public final String getID() {
		return federationExecutionHandle.getUuid().toString() + "-"
				+ federationExecutionModelHandle.getUuid().toString();
	}

	/**
	 * @return the federationExecutionHandle
	 */
	public final FederationExecutionHandle getFederationExecutionHandle() {
		return federationExecutionHandle;
	}

	/**
	 * @return the federationExecutionModelHandle
	 */
	public final FederationExecutionModelHandle getFederationExecutionModelHandle() {
		return federationExecutionModelHandle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.commons.internal.FederationExecutionID#toString()
	 */
	@Override
	public String toString() {
		return getID();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.commons.internal.FederationExecutionID#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result =
				prime
						* result
						+ ((federationExecutionHandle == null) ? 0
								: federationExecutionHandle.hashCode());
		result =
				prime
						* result
						+ ((federationExecutionModelHandle == null) ? 0
								: federationExecutionModelHandle.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.commons.internal.FederationExecutionID#equals(java.lang.
	 * Object)
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
		FederationExecutionID other = (FederationExecutionID) obj;
		if (federationExecutionHandle == null) {
			if (other.getFederationExecutionHandle() != null) {
				return false;
			}
		} else if (!federationExecutionHandle.equals(other
				.getFederationExecutionHandle())) {
			return false;
		}
		if (federationExecutionModelHandle == null) {
			if (other.getFederationExecutionModelHandle() != null) {
				return false;
			}
		} else if (!federationExecutionModelHandle.equals(other
				.getFederationExecutionModelHandle())) {
			return false;
		}
		return true;
	}

}
