package com.csc.muthur.server.ownership;

import com.csc.muthur.server.commons.DataChannelControlBlock;
import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.ownership.internal.RequestCallback;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class PendingFederateRequestEntry {

	private IEvent event;
	private RequestCallback callback;
	private DataChannelControlBlock dccBlock;

	/**
	 * 
	 * @param event
	 * @param callback
	 * @param message
	 */
	public PendingFederateRequestEntry(final IEvent event,
			final RequestCallback callback, final DataChannelControlBlock dccBlock) {

		if (event == null) {
			throw new IllegalArgumentException("Event was null.");
		}
		if (callback == null) {
			throw new IllegalArgumentException("Callback was null.");
		}
		if (dccBlock == null) {
			throw new IllegalArgumentException("DataChannelControlBlock was null.");
		}

		this.event = event;
		this.callback = callback;
		this.dccBlock = dccBlock;
	}

	/**
	 * @return the pending request
	 */
	public IEvent getEvent() {
		return event;
	}

	/**
	 * @return the callback for this pending request
	 */
	public RequestCallback getCallback() {
		return callback;
	}

	/**
	 * @return the dccBlock
	 */
	public DataChannelControlBlock getDccBlock() {
		return dccBlock;
	}

	/**
	 * @param dccBlock
	 *          the dccBlock to set
	 */
	public void setDccBlock(DataChannelControlBlock dccBlock) {
		this.dccBlock = dccBlock;
	}
}
