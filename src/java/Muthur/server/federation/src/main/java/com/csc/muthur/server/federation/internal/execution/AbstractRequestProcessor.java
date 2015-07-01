package com.csc.muthur.server.federation.internal.execution;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import com.csc.muthur.server.commons.exception.MuthurException;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public abstract class AbstractRequestProcessor implements Runnable,
		RequestProcessor {

	private long timeToLiveMSecs = 0L;

	private Semaphore threadStartedSem;

	private boolean continueProcessing = true;

	private boolean satisfied = false;

	/**
	 * 
	 */
	public AbstractRequestProcessor() {
		super();
		this.threadStartedSem = new Semaphore(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.federation.internal.RequestProcessor#waitTillStarted()
	 */
	public void waitTillStarted() throws MuthurException {
		try {
			if (!threadStartedSem.tryAcquire(30, TimeUnit.SECONDS)) {
				throw new MuthurException(
						"Unable to acquire request processor semaphore.");
			}
		} catch (InterruptedException e) {
			throw new MuthurException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.federation.internal.RequestProcessor#setTimeToLiveMSecs(
	 * long)
	 */
	public void setTimeToLiveMSecs(long timeToLiveMSecs) {
		this.timeToLiveMSecs = timeToLiveMSecs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.federation.internal.RequestProcessor#getTimeToLiveMSecs()
	 */
	public long getTimeToLiveMSecs() {
		return timeToLiveMSecs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.federation.internal.RequestProcessor#getThreadStartedSem()
	 */
	public final Semaphore getThreadStartedSem() {
		return threadStartedSem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.federation.internal.RequestProcessor#setContinueProcessing
	 * (boolean)
	 */
	public void setContinueProcessing(boolean continueProcessing) {
		this.continueProcessing = continueProcessing;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.federation.internal.RequestProcessor#isContinueProcessing()
	 */
	public boolean isContinueProcessing() {
		return continueProcessing;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.federation.internal.RequestProcessor#isProcessorSatisfied()
	 */
	public boolean isSatisfied() {
		return satisfied;
	}

	/**
	 * @param satisfied
	 *          the satisfied to set
	 */
	public void setSatisfied(boolean satisfied) {
		this.satisfied = satisfied;
	}

}
