/**
 * 
 */
package com.csc.muthur.server.federation.datachannel.internal;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.federation.DataChannelListener;
import com.csc.muthur.server.federation.DataChannelListenerFactory;

/**
 * 
 * @author <a href=mailto:support@atcloud.com>support</a>
 * @version $Revision: $
 */
public class MockDataChannelListenerFactory implements
		DataChannelListenerFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.federation.DataChannelListenerFactory#createListener
	 * ()
	 */
	@Override
	public DataChannelListener createListener() throws MuthurException {
		return new MockDataChannelListener();
	}

}
