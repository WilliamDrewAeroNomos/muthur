/**
 * 
 */
package com.csc.muthur.server.federation.internal;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.configuration.ConfigurationService;
import com.csc.muthur.server.federation.internal.execution.FederationServiceImpl;
import com.csc.muthur.server.router.RouterService;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */

public class MockFederationServiceImpl extends FederationServiceImpl {

	private static ConfigurationService cs;
	private static RouterService rs;

	/**
	 * @param configurationService
	 * @throws MuthurException
	 */
	public MockFederationServiceImpl(ConfigurationService configurationService)
			throws MuthurException {
		cs = configurationService;
		setConfigurationService(cs);
		rs = new MockRouterServiceImpl(cs);
		setRouterService(rs);
		rs.start();
	}

}
