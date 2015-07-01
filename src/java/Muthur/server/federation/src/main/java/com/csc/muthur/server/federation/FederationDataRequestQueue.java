/**
 * 
 */
package com.csc.muthur.server.federation;

import com.csc.muthur.server.commons.IDataEventHandler;
import com.csc.muthur.server.router.RouterService;

/**
 * 
 * @author <a href=mailto:support@atcloud.com>support</a>
 * @version $Revision: $
 */
public interface FederationDataRequestQueue {

	/**
	 * 
	 * @param eventHandler
	 */
	void put(IDataEventHandler eventHandler);

	/**
	 * 
	 */
	void terminate();

	/**
	 * @return the routerService
	 */
	RouterService getRouterService();

	/**
	 * @param routerService
	 *          the routerService to set
	 */
	void setRouterService(RouterService routerService);

	public abstract int getCntEventsProcessed();

}