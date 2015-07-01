/**
 * 
 */
package com.csc.muthur.server.eventserver.internal;

import com.csc.muthur.server.commons.DataChannelControlBlock;
import com.csc.muthur.server.federation.DataChannelListener;

/**
 * 
 * @author <a href=mailto:support@atcloud.com>support</a>
 * @version $Revision: $
 */
public class MockDataChannelListener implements DataChannelListener {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.federation.DataChannelListener#messageReceived(com
	 * .csc.muthur.server.commons.DataChannelControlBlock, java.lang.Object)
	 */
	@Override
	public void messageReceived(DataChannelControlBlock dccBlock, Object payload) {

		if (dccBlock == null) {
			System.err.println("DataChannelControlBlock was null.");
		}

		System.out.println("Data event [" + dccBlock.getEventName() + "] received");

		if (payload == null) {
			System.out.println("Payload was null.");
		}

		if (!(payload instanceof String)) {
			System.out.println("Payload was not an instance of String.");
		}

		String payLoad = (String) payload;

		System.out.println("Data event payload [" + payLoad + "]");
	}

}
