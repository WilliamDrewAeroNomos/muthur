/**
 * 
 */
package com.csc.muthur.server.federation.internal;

import org.junit.Test;

/**
 * @author williamdrew
 *
 */
public interface PublicationRequestProcessor {

	/**
	 * Standard processing for the join step using two federates and default
	 * settings in the FEM
	 */
	@Test
	public abstract void baseFederationRequestProcessor();

	/**
	 * Tests publication requests of a single federate
	 */
	@Test
	public abstract void federationRequestProcessorSingleFederate();

	/**
	 * Tests error handling when a required federate does not register using
	 * default timing settings from the FEM
	 */
	@Test
	public abstract void federationRequestProcessorRequiredFederateNotRgisteredErrorHandling();

}