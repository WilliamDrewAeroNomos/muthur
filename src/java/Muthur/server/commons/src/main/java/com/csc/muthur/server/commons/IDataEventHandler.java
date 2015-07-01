/**
 * 
 */
package com.csc.muthur.server.commons;

import com.csc.muthur.server.commons.exception.MuthurException;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public interface IDataEventHandler {

	/**
	 * 
	 * @throws Exception
	 */
	void handle() throws MuthurException;

	void setDataChannelControlBlock(final DataChannelControlBlock dccb);

	DataChannelControlBlock getDataChannelControlBlock();

	/**
	 * @return Returns the event.
	 */
	IEvent getEvent();

	/**
	 * 
	 * @param event
	 */
	void setEvent(IEvent event);

}