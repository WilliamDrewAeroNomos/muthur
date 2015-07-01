/**
 * 
 */
package com.csc.muthur.server.model;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public enum FederationExecutionState {
	UNDEFINED(0, "Undefined"),
	AWAITING_START_DIRECTIVE(1, "Awaiting start"),
	RUNNING(2, "Running"),
	PAUSED(3, "Paused");

	private String directiveName;
	private int identifier;

	/**
	 * 
	 * @param identifier
	 * @param directiveName
	 */
	FederationExecutionState(int identifier, String directiveName) {
		this.setIdentifier(identifier);
		this.setDirectiveName(directiveName);
	}

	/**
	 * @param directiveName
	 *          the directiveName to set
	 */
	public void setDirectiveName(String directiveName) {
		this.directiveName = directiveName;
	}

	/**
	 * @return the directiveName
	 */
	public String getDirectiveName() {
		return directiveName;
	}

	/**
	 * @param identifier
	 *          the identifier to set
	 */
	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return the identifier
	 */
	public int getIdentifier() {
		return identifier;
	}

	/**
	 * 
	 * @param typeID
	 * @return
	 */
	public static FederationExecutionState getType(int typeID) {

		for (FederationExecutionState type : FederationExecutionState.values()) {

			if (type.getIdentifier() == typeID) {
				return type;
			}

		}

		return null;
	}
}
