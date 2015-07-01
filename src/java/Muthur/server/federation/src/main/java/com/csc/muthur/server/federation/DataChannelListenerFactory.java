/**
 * 
 */
package com.csc.muthur.server.federation;

import com.csc.muthur.server.commons.exception.MuthurException;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public interface DataChannelListenerFactory {

	DataChannelListener createListener() throws MuthurException;

}
