/**
 * 
 */
package com.csc.muthur.server.federation;

import com.csc.muthur.server.commons.DataChannelControlBlock;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public interface DataChannelListener {

	public abstract void messageReceived(DataChannelControlBlock fdccBlock, Object payload);

}
