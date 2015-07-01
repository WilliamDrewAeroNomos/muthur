/**
 * 
 */
package com.csc.muthur.server.federation;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public interface FederationDataChannelServer extends Runnable {

	void setListening(boolean listening);

	boolean isListening();

	void stop();

	public abstract void setDataChannelListenerFactory(DataChannelListenerFactory dataChannelListenerFactory);

	public abstract DataChannelListenerFactory getDataChannelListenerFactory();

	public abstract void setPortNumber(int portNumber);

	public abstract int getPortNumber();

}
