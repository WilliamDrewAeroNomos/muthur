/**
 * 
 */
package com.csc.muthur.server.commons;

/**
 * @author williamdrew
 *
 */
public interface FederationExecutionID {

	/**
	 * 
	 * @return
	 */
	String getID();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	String toString();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	int hashCode();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	boolean equals(Object obj);

	/**
	 * @return the federationExecutionHandle
	 */
	public FederationExecutionHandle getFederationExecutionHandle();

	/**
	 * @return the federationExecutionModelHandle
	 */
	public FederationExecutionModelHandle getFederationExecutionModelHandle();

}