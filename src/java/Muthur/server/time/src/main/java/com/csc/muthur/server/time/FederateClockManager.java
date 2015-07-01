package com.csc.muthur.server.time;

import java.util.concurrent.Semaphore;

import javax.jms.Session;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.FederationExecutionDirective;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.FederationExecutionState;
import com.csc.muthur.server.model.ReadyToRunFederationExecutionEntry;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public interface FederateClockManager {

	/**
	 * 
	 * @throws MuthurException
	 */
	public abstract void start() throws MuthurException;

	/**
	 * @return the continueRunning
	 */
	public abstract boolean isContinueRunning();

	/**
	 * @param stop
	 *          the running thread and all message event threads
	 * 
	 */
	public abstract void terminate();

	/**
	 * @param session
	 *          the session to set
	 */
	public abstract void setSession(Session session);

	/**
	 * @return the startTimeMSecs
	 */
	public abstract long getStartTimeMSecs();

	/**
	 * 
	 * @return
	 */
	public abstract long getNextIncrement();

	/**
	 * @return the syncSemaphore
	 */
	public abstract Semaphore getSyncSemaphore();

	/**
	 * @return the currentTimeMSecs
	 */
	public abstract long getCurrentTimeMSecs();

	/**
	 * @param currentTimeMSecs
	 *          the currentTimeMSecs to set
	 */
	public void setCurrentTimeMSecs(long currentTimeMSecs);

	/**
	 * @return the federationExecutionDirective
	 */
	public abstract FederationExecutionDirective getFederationExecutionDirective();

	/**
	 * @param federationExecutionDirective
	 *          the federationExecutionDirective to set
	 */
	public abstract void setFederationExecutionDirective(
			FederationExecutionDirective federationExecutionDirective);

	/**
	 * @return the federationExecutionState
	 */
	public abstract FederationExecutionState getFederationExecutionState();

	/**
	 * @param federationExecutionState
	 *          the federationExecutionState to set
	 */
	public abstract void setFederationExecutionState(
			FederationExecutionState federationExecutionState);

	/**
	 * 
	 * @param rrfe
	 * @throws MuthurException
	 */
	public abstract void addFederate(final ReadyToRunFederationExecutionEntry rrfe)
			throws MuthurException;

	/**
	 * 
	 * @param rrfe
	 * @throws MuthurException
	 */
	public abstract void removeFederate(
			final ReadyToRunFederationExecutionEntry rrfe) throws MuthurException;

	void pause();

	void resume();

	public abstract Session getSession();

	public abstract void setIntervalBetweenTimeUpdatesMSecs(
			long intervalBetweenTimeUpdatesMSecs);

	public abstract long getIntervalBetweenTimeUpdatesMSecs();

	public abstract void setSizeOfTimeIncrementMSecs(int sizeOfTimeIncrementMSecs);

	public abstract int getSizeOfTimeIncrementMSecs();

	public abstract void setMaxNumOfConsecutiveFailedAttemptsAllowed(
			int maxNumOfConsecutiveFailedAttemptsAllowed);

	public abstract int getMaxNumOfConsecutiveFailedAttemptsAllowed();

	public FederationExecutionModel getFederationExecutionModel();

	public void setFederationExecutionModel(
			FederationExecutionModel federationExecutionModel);

	/**
	 * Sets the current federation clock to the time specified in milliseconds
	 * using UTC time zone. Each increment moving forward will reflect this new
	 * clock.
	 * 
	 * @param timeMSecs
	 *          New clock time in milliseconds.
	 */
	public void setClock(final long timeMSecs);

}