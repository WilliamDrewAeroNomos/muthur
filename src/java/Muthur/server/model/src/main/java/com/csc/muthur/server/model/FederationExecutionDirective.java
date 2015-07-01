/**
 * 
 */
package com.csc.muthur.server.model;

/**
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public enum FederationExecutionDirective {
	UNDEFINED(0, "Undefined"), CREATED(1, "Created"), START(2, "Start"), PAUSE(
			3,
			"Pause"), RESUME(4, "Resume"), RUN(5, "Run");

	private String directiveName;
	private int identifier;

	/**
	 * 
	 * @param identifier
	 * @param directiveName
	 */
	FederationExecutionDirective(int identifier, String directiveName) {
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
	public static FederationExecutionDirective getType(int typeID) {

		for (FederationExecutionDirective type : FederationExecutionDirective
				.values()) {

			if (type.getIdentifier() == typeID) {
				return type;
			}

		}

		return null;
	}
}
