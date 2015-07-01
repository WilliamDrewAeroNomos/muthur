/**
 * 
 */
package com.csc.muthur.server.federation.internal;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public interface BaseTestRequestProcessor {

	void baseFederationRequestProcessor();

	void federationRequestProcessorSingleFederate();

	void federationRequestProcessorRequiredFederateNotRgisteredErrorHandling();

}
