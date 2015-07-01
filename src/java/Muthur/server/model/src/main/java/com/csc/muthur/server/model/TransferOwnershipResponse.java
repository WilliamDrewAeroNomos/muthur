/**
 * 
 */
package com.csc.muthur.server.model;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public enum TransferOwnershipResponse {
	UNKNOWN(-1),
	DENIED(0),
	GRANTED(1);

	private int stateId;

	TransferOwnershipResponse(int stateId) {
		this.stateId = stateId;
	}

	/**
	 * @return the stateId
	 */
	public int getStateId() {
		return stateId;
	}

	/**
	 * @param stateId
	 *          the stateId to set
	 */
	public void setStateId(int stateId) {
		this.stateId = stateId;
	}
}
