package com.csc.muthur.server.time;

/**
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 *
 */
public interface FederateClock extends Runnable {

	/**
	 * @return the continueRunning
	 */
	public abstract boolean isContinueRunning();

	/**
	 * 
	 */
	public abstract void terminate();

	/**
	 * @return the maxNumOfConsecutiveFailedAttemptsAllowed
	 */
	public abstract int getMaxNumOfConsecutiveFailedAttemptsAllowed();

	/**
	 * @param maxNumOfConsecutiveFailedAttemptsAllowed
	 *          the maxNumOfConsecutiveFailedAttemptsAllowed to set
	 */
	public abstract void setMaxNumOfConsecutiveFailedAttemptsAllowed(
			int maxNumOfConsecutiveFailedAttemptsAllowed);

}