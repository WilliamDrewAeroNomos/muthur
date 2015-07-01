/**
 * 
 */
package com.csc.muthur.server.federation.internal.execution;

import java.util.concurrent.Semaphore;

import com.csc.muthur.server.commons.exception.MuthurException;

/**
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public interface RequestProcessor {

	/**
	 * Blocks until the {@link #run()} method releases the
	 * {@link #threadStartedSem}
	 * 
	 * @throws InterruptedException
	 * @throws MuthurException
	 */
	void waitTillStarted() throws MuthurException;

	/**
	 * @param timeToLiveMSecs
	 *          the timeToLiveMSecs to set
	 */
	void setTimeToLiveMSecs(long timeToLiveMSecs);

	/**
	 * @return the timeToLiveMSecs
	 */
	long getTimeToLiveMSecs();

	/**
	 * @return the threadStartedSem
	 */
	Semaphore getThreadStartedSem();

	/**
	 * 
	 * @param continueProcessing
	 */
	void setContinueProcessing(boolean continueProcessing);

	/**
	 * 
	 * @return
	 */
	boolean isContinueProcessing();

	/**
	 * @return the processorSatisfied
	 */
	boolean isSatisfied();

}