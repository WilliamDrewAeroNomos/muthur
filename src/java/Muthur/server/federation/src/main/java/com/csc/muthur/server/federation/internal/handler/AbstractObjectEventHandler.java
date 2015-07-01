/**
 * 
 */
package com.csc.muthur.server.federation.internal.handler;

import com.csc.muthur.server.commons.DataChannelControlBlock;
import com.csc.muthur.server.commons.IDataEventHandler;
import com.csc.muthur.server.commons.IEvent;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public abstract class AbstractObjectEventHandler implements IDataEventHandler {

	private IEvent event;

	private String errorDescription;

	private DataChannelControlBlock dataChannelControlBlock;

	/**
	 * 
	 */
	public AbstractObjectEventHandler() {
	}

	/**
	 * @return Returns the event.
	 */
	public IEvent getEvent() {
		return this.event;
	}

	/**
	 * @param event
	 *          the event to set
	 */
	public final void setEvent(IEvent event) {
		this.event = event;
	}

	/**
	 * @param errorDescription
	 *          the errorDescription to set
	 */
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	/**
	 * @return the errorDescription
	 */
	public String getErrorDescription() {
		return errorDescription;
	}

	/**
	 * @return the dataChannelControlBlock
	 */
	public DataChannelControlBlock getDataChannelControlBlock() {
		return dataChannelControlBlock;
	}

	/**
	 * @param dataChannelControlBlock
	 *          the dataChannelControlBlock to set
	 */
	public void setDataChannelControlBlock(
			DataChannelControlBlock dataChannelControlBlock) {
		this.dataChannelControlBlock = dataChannelControlBlock;
	}

}
