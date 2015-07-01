/**
 * 
 */
package com.csc.muthur.server.commons;

import com.csc.muthur.server.commons.exception.MuthurException;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */

public interface CommonsService {

	public abstract void start();

	public abstract void stop();

	/**
	 * Checks if a particular port is available
	 * 
	 * @param port
	 * @return true if the port is available
	 */
	public boolean portAvailable(int port);

	/**
	 * Finds an available port between the minimum and maximum port numbers
	 * provided. -1 is returned if a port is not available in the given range.
	 * 
	 * @param minPort
	 * @param maxPort
	 * @return
	 */
	public int findAvailablePort(final int minPort, final int maxPort)
			throws IllegalArgumentException;

	/**
	 * 
	 * @param federateRegistrationHandle
	 * @param federationExecutionHandle
	 * @param federationExecutionModelHandle
	 * @return
	 * @throws MuthurException
	 */
	public ObjectOwnershipID createObjectOwnershipID(
			final String federateRegistrationHandle,
			final String federationExecutionHandle,
			final String federationExecutionModelHandle) throws MuthurException;

	/**
	 * 
	 * @param federationExecutionHandle
	 * @param federationExecutionModelHandle
	 * @return
	 * @throws MuthurException
	 */
	public FederationExecutionID createFederationExecutionID(
			final String federationExecutionHandle,
			final String federationExecutionModelHandle) throws MuthurException;

	/**
	 * Create a {@link ObjectOwnershipID} with federate registration handle
	 * represented as a {@link String} and a {@link FederationExecutionID}.
	 * 
	 * @param federateRegistrationHandle
	 * @param federationExecutionID
	 * @return
	 * @throws MuthurException
	 */
	public ObjectOwnershipID createObjectOwnershipID(
			final String federateRegistrationHandle,
			final FederationExecutionID federationExecutionID) throws MuthurException;

}