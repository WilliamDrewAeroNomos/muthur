/**
 * 
 */
package com.csc.muthur.server.commons;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public class FederateRegistrationHandle {

	private String registrationHandle;

	/**
	 * @param registrationHandle
	 */
	public FederateRegistrationHandle(final String registrationHandle) {
		super();
		this.registrationHandle = registrationHandle;
	}

	/**
	 * @return the registrationHandle
	 */
	public String getRegistrationHandle() {
		return registrationHandle;
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
		result = prime * result
				+ ((registrationHandle == null) ? 0 : registrationHandle.hashCode());
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
		FederateRegistrationHandle other = (FederateRegistrationHandle) obj;
		if (registrationHandle == null) {
			if (other.registrationHandle != null) {
				return false;
			}
		} else if (!registrationHandle.equals(other.registrationHandle)) {
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
		return "FederateRegistrationHandle [registrationHandle="
				+ registrationHandle + "]";
	}
}
