/**
 * 
 */
package com.csc.muthur.server.federation.internal;

import com.csc.muthur.server.configuration.ConfigurationService;
import com.csc.muthur.server.router.internal.RouterServiceImpl;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */

public class MockRouterServiceImpl extends RouterServiceImpl {

	/**
	 * 
	 */
	public MockRouterServiceImpl(ConfigurationService configurationServer) {
		super();

		super.setConfigurationServer(configurationServer);
	}

}
