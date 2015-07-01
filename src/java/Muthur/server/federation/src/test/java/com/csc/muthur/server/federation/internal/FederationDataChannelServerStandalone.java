/**
 * 
 */
package com.csc.muthur.server.federation.internal;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.csc.muthur.server.federation.FederationDataChannelServer;
import com.csc.muthur.server.federation.datachannel.internal.MockDataChannelListenerFactory;
import com.csc.muthur.server.federation.internal.datachannel.FederationDataChannelServerImpl;

/**
 * 
 * @author <a href=mailto:support@atcloud.com>support</a>
 * @version $Revision: $
 */
public class FederationDataChannelServerStandalone {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		ExecutorService federationDataChanneServerlPool =
				Executors.newCachedThreadPool();

		FederationDataChannelServer fdcs = new FederationDataChannelServerImpl();
		fdcs.setDataChannelListenerFactory(new MockDataChannelListenerFactory());
		fdcs.setPortNumber(42424);

		try {
			federationDataChanneServerlPool.execute(fdcs);
		} catch (Exception e) {
			System.err.println(e);
		}

	}

}
