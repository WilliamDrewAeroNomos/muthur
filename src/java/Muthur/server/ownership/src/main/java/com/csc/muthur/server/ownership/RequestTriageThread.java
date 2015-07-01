/**
 * 
 */
package com.csc.muthur.server.ownership;

import com.csc.muthur.server.commons.DataChannelControlBlock;
import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.ownership.internal.RequestCallback;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public interface RequestTriageThread extends Runnable {

	/**
	 * 
	 * @param correlationID
	 * @return A {@link RequestCallback} from the {@link #requestToCallbackMap}
	 *         which has the correlationId as the key or null if the entry is not
	 *         found.
	 * @throws InterruptedException
	 */
	RequestCallback getCallback(final String correlationID)
			throws InterruptedException;

	/**
	 * 
	 * @param event
	 * @param cb
	 * @param dccBlock
	 * @return
	 * @throws MuthurException
	 */
	boolean addRequest(final IEvent event, final RequestCallback cb,
			final DataChannelControlBlock dccBlock) throws MuthurException;

	/**
	 * 
	 * @param continueRunning
	 *          the continueRunning to set
	 */
	void setContinueRunning(boolean continueRunning);

	/**
	 * 
	 * @return the continueRunning
	 */
	boolean isContinueRunning();

	/**
	 * @return the pauseBeforeBeginTriaging
	 */
	long getPauseBeforeBeginTriaging();

	/**
	 * @param pauseBeforeBeginTriaging
	 *          the pauseBeforeBeginTriaging to set
	 */
	void setPauseBeforeBeginTriaging(long pauseBeforeBeginTriaging);

	/**
	 * @return the intervalBetweenTriages
	 */
	long getIntervalBetweenTriages();

	/**
	 * @param intervalBetweenTriages
	 *          the intervalBetweenTriages to set
	 */
	void setIntervalBetweenTriages(long intervalBetweenTriages);

	/**
	 * 
	 * @param event
	 * @return
	 */

	PendingFederateRequestEntry removeRequest(final IEvent event);

	/**
	 * 
	 * @param event
	 * @return
	 */
	PendingFederateRequestEntry getRequest(final IEvent event);

}