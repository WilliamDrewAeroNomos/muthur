/**
 * 
 */
package com.csc.muthur.server.federation.internal.datachannel;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.federation.DataChannelListener;
import com.csc.muthur.server.federation.DataChannelListenerFactory;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public class FederationDataChannelListenerFactory implements
		DataChannelListenerFactory {

	private DataChannelListener dataChannelListener;

	/**
	 * 
	 * @param federationService
	 * @throws MuthurException
	 */
	public FederationDataChannelListenerFactory() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.federation.DataChannelListenerFactory#createListener
	 * ()
	 */
	@Override
	public DataChannelListener createListener() throws MuthurException {
		return dataChannelListener;
	}

	/**
	 * @return the dataChannelListener
	 */
	public DataChannelListener getDataChannelListener() {
		return dataChannelListener;
	}

	/**
	 * @param dataChannelListener
	 *          the dataChannelListener to set
	 */
	public void setDataChannelListener(DataChannelListener dataChannelListener) {
		this.dataChannelListener = dataChannelListener;
	}

}
