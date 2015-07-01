package com.csc.muthur.server.time.internal;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;

import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.FederationExecutionDirective;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.FederationExecutionState;
import com.csc.muthur.server.model.ReadyToRunFederationExecutionEntry;
import com.csc.muthur.server.time.FederateClock;
import com.csc.muthur.server.time.FederateClockManager;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class FederateClockManagerImpl implements FederateClockManager {

	public static final Logger LOG = LoggerFactory
			.getLogger(FederateClockImpl.class.getName());

	private boolean continueRunning = true;
	private Semaphore syncSemaphore;
	private Map<String, FederateClockImpl> federateNameTofederateClockMap =
			new ConcurrentHashMap<String, FederateClockImpl>();

	private Session session;
	private long startTimeMSecs;
	private long currentTimeMSecs;

	private Calendar cal;
	private static Map<FederationExecutionDirective, FederationExecutionState> directiveToStateMap =
			new ConcurrentHashMap<FederationExecutionDirective, FederationExecutionState>();
	private FederationExecutionDirective federationExecutionDirective;

	private FederationExecutionState federationExecutionState;
	private Semaphore startFederationTimerSemaphore;
	private Thread federationTimerThread;

	private Format federationTimeFormatted = new SimpleDateFormat(
			"yyyy.MM.dd HH.mm.ss");
	private FederationExecutionModel federationExecutionModel;

	// TODO: Parameterize these from the FEM
	// number of MSecs to move the clock

	private int sizeOfTimeIncrementMSecs = 1000;

	// number of MSecs to wait between updating the time (forward or backward)
	private long intervalBetweenTimeUpdatesMSecs = 1000;

	private int maxNumOfConsecutiveFailedAttemptsAllowed = 0;

	static {

		// initialize federation execution directive to federation execution
		// state map

		directiveToStateMap.put(FederationExecutionDirective.UNDEFINED,
				FederationExecutionState.UNDEFINED);

		directiveToStateMap.put(FederationExecutionDirective.CREATED,
				FederationExecutionState.AWAITING_START_DIRECTIVE);

		directiveToStateMap.put(FederationExecutionDirective.START,
				FederationExecutionState.RUNNING);

		directiveToStateMap.put(FederationExecutionDirective.PAUSE,
				FederationExecutionState.PAUSED);

		directiveToStateMap.put(FederationExecutionDirective.RESUME,
				FederationExecutionState.RUNNING);

		directiveToStateMap.put(FederationExecutionDirective.RUN,
				FederationExecutionState.RUNNING);

	}

	/**
	 * 
	 * @param federationExecutionModel
	 * @param session
	 * @throws MuthurException
	 */
	public FederateClockManagerImpl(
			final FederationExecutionModel federationExecutionModel,
			final Session session) throws MuthurException {

		if (federationExecutionModel == null) {
			throw new IllegalArgumentException(
					"Federation execution model parameter was null.");
		}

		if (session == null) {
			throw new IllegalArgumentException("Session parameter was null.");
		}

		setFederationExecutionModel(federationExecutionModel);

		LOG.debug("Federate clock manager start time (MSecs) ["
				+ getFederationExecutionModel().getLogicalStartTimeMSecs() + "]");

		setSession(session);

		setStartTimeMSecs(federationExecutionModel.getLogicalStartTimeMSecs());

		setCurrentTimeMSecs(federationExecutionModel.getLogicalStartTimeMSecs());

		cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

		cal.setTimeInMillis(federationExecutionModel.getLogicalStartTimeMSecs());

		// semaphore which the timer will block on until released by the start()
		// method

		startFederationTimerSemaphore = new Semaphore(0);

		// semaphore which all federate clocks will synchronize on
		//
		syncSemaphore = new Semaphore(0);

		setFederationExecutionDirective(FederationExecutionDirective.CREATED);

		federationTimerThread = new Thread(new FederationTimer());

		federationTimerThread.start();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.time.internal.FederateClockManager#start()
	 */
	@Override
	public void start() throws MuthurException {

		LOG.debug("Starting the federate clocks...");

		setFederationExecutionDirective(FederationExecutionDirective.RUN);

		// Release the semaphore the federation timer is blocking on. This will
		// start the timer for the federation.
		//
		startFederationTimerSemaphore.release();

		LOG.info("Federate clocks started for federation ["
				+ federationExecutionModel.getName() + "]");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.time.FederationExecutionTimeManager#pause()
	 */
	@Override
	public void pause() {

		setFederationExecutionDirective(FederationExecutionDirective.PAUSE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.time.FederationExecutionTimeManager#resume()
	 */
	@Override
	public void resume() {

		setFederationExecutionDirective(FederationExecutionDirective.RUN);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.time.internal.FederateClockManager#isContinueRunning
	 * ()
	 */
	@Override
	public final boolean isContinueRunning() {
		return continueRunning;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.time.internal.FederateClockManager#terminate()
	 */
	@Override
	public final void terminate() {

		// terminate the federation timer thread which drives the federate
		// clocks
		// then terminate each of the clocks

		LOG.debug("Federation clock manager is terminating...");

		// this will ensure that the thread will stop which will stop the
		// release of
		// the semaphore which will block the clocks from proceeding

		this.continueRunning = false;

		// this will ensure that the thread is interrupted from the sleep period
		// and
		// when it awakes with the continueRunning flag set to false will exit

		federationTimerThread.interrupt();

		LOG.debug("Iterate over the threads and call terminate() to release the run() method.");

		for (FederateClock fci : federateNameTofederateClockMap.values()) {
			if (fci != null) {
				fci.terminate();
			}
		}

		LOG.debug("Releasing [" + federateNameTofederateClockMap.size()
				+ "] permit(s).");

		syncSemaphore.release(federateNameTofederateClockMap.size());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.time.internal.FederateClockManager#setSession(javax
	 * .jms. Session)
	 */
	@Override
	public void setSession(Session session) {
		this.session = session;
	}

	/**
	 * @return the session
	 */
	@Override
	public Session getSession() {
		return session;
	}

	/**
	 * @param startTimeMSecs
	 *          the startTimeMSecs to set
	 */
	public void setStartTimeMSecs(long startTimeMSecs) {
		this.startTimeMSecs = startTimeMSecs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.time.internal.FederateClockManager#getStartTimeMSecs
	 * ()
	 */
	@Override
	public long getStartTimeMSecs() {
		return startTimeMSecs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.time.internal.FederateClockManager#getNextIncrement()
	 */
	@Override
	public long getNextIncrement() {

		// if the clock is running then increment the time

		if (federationExecutionState.equals(FederationExecutionState.RUNNING)) {

			LOG.debug("Proceeding to next time interval.");

			cal.add(Calendar.MILLISECOND, sizeOfTimeIncrementMSecs);

		} else {

			LOG.debug("Federation clock is " + "stopped at ["
					+ federationTimeFormatted.format(cal.getTime()) + "]");
		}

		return cal.getTimeInMillis();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.time.internal.FederateClockManager#getSyncSemaphore()
	 */
	@Override
	public final Semaphore getSyncSemaphore() {
		return syncSemaphore;
	}

	/**
	 * @param syncSemaphore
	 *          the syncSemaphore to set
	 */
	public final void setSyncSemaphore(Semaphore syncSemaphore) {
		this.syncSemaphore = syncSemaphore;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.time.internal.FederateClockManager#getCurrentTimeMSecs
	 * ()
	 */
	@Override
	public final long getCurrentTimeMSecs() {
		return currentTimeMSecs;
	}

	/**
	 * @param currentTimeMSecs
	 *          the currentTimeMSecs to set
	 */
	public final void setCurrentTimeMSecs(long currentTimeMSecs) {
		this.currentTimeMSecs = currentTimeMSecs;
	}

	/**
	 * Sets the current federation clock to the time specified in milliseconds
	 * using UTC time zone. Each increment moving forward will reflect this new
	 * clock.
	 * 
	 * @param timeMSecs
	 *          New clock time in milliseconds.
	 */
	public final void setClock(final long timeMSecs) {
		cal.setTimeInMillis(timeMSecs);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.time.internal.FederateClockManager#
	 * getFederationExecutionDirective()
	 */
	@Override
	public FederationExecutionDirective getFederationExecutionDirective() {
		return federationExecutionDirective;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.time.internal.FederateClockManager#
	 * setFederationExecutionDirective
	 * (com.csc.muthur.model.FederationExecutionDirective)
	 */
	@Override
	public void setFederationExecutionDirective(
			FederationExecutionDirective federationExecutionDirective) {

		setFederationExecutionState(directiveToStateMap
				.get(federationExecutionDirective));

		LOG.debug("Federation directive - [" + federationExecutionDirective + "]");
		LOG.debug("Federation state - [" + getFederationExecutionState() + "]");

		this.federationExecutionDirective = federationExecutionDirective;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.time.internal.FederateClockManager#
	 * getFederationExecutionState ()
	 */
	@Override
	public FederationExecutionState getFederationExecutionState() {
		return federationExecutionState;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.time.internal.FederateClockManager#
	 * setFederationExecutionState (com.csc.muthur.model.FederationExecutionState)
	 */
	@Override
	public void setFederationExecutionState(
			FederationExecutionState federationExecutionState) {
		this.federationExecutionState = federationExecutionState;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.time.internal.FederateClockManager#addFederate(com
	 * .csc .muthur .model.ReadyToRunFederationExecutionEntry)
	 */
	@Override
	public void addFederate(final ReadyToRunFederationExecutionEntry rrfe)
			throws MuthurException {

		if (rrfe != null) {

			LOG.debug("Adding clock for federate [" + rrfe.getFederateName() + "]...");

			FederateClockImpl federateClockImpl = new FederateClockImpl(this, rrfe);

			federateClockImpl
					.setMaxNumOfConsecutiveFailedAttemptsAllowed(getMaxNumOfConsecutiveFailedAttemptsAllowed());

			Thread federateClockThread = new Thread(federateClockImpl);

			if (federateClockThread != null) {

				federateNameTofederateClockMap.put(rrfe.getFederateName(),
						federateClockImpl);

				federateClockThread.start();

				LOG.debug("Clock added for federate [" + rrfe.getFederateName() + "]");
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.time.internal.FederateClockManager#removeFederate
	 * (com.csc .muthur.model.ReadyToRunFederationExecutionEntry)
	 */
	@Override
	public void removeFederate(final ReadyToRunFederationExecutionEntry rrfe)
			throws MuthurException {

		if (rrfe != null) {

			LOG.debug("Removing clock for federate [" + rrfe.getFederateName()
					+ "]...");

			FederateClock fci =
					federateNameTofederateClockMap.get(rrfe.getFederateName());

			if (fci != null) {

				fci.terminate();

				federateNameTofederateClockMap.remove(rrfe.getFederateName());

				LOG.debug("Removed clock for federate [" + rrfe.getFederateName() + "]");
			}

		}
	}

	/**
	 * 
	 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
	 * @version $Revision$
	 * 
	 */
	private class FederationTimer implements Runnable {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {

			LOG.info("Started clock manager for federation ["
					+ getFederationExecutionModel().getName() + "]");

			try {
				startFederationTimerSemaphore.acquire();
			} catch (InterruptedException e1) {
				setContinueRunning(false);
			}

			/**
			 * 
			 */
			while (isContinueRunning()) {

				if (isContinueRunning()) {

					try {

						syncSemaphore.release(federateNameTofederateClockMap.size());

						// TODO: use configuration value for this sleep time
						Thread.sleep(intervalBetweenTimeUpdatesMSecs);

						setCurrentTimeMSecs(getNextIncrement());

					} catch (InterruptedException e) {
						setContinueRunning(false);
					}

				}

			}

			LOG.info("Clock manager for federation ["
					+ getFederationExecutionModel().getName() + "] has terminated.");
		}

	}

	/**
	 * @return the sizeOfTimeIncrementMSecs
	 */
	@Override
	public final int getSizeOfTimeIncrementMSecs() {
		return sizeOfTimeIncrementMSecs;
	}

	/**
	 * @param sizeOfTimeIncrementMSecs
	 *          the sizeOfTimeIncrementMSecs to set
	 */
	@Override
	public final void setSizeOfTimeIncrementMSecs(int sizeOfTimeIncrementMSecs) {
		this.sizeOfTimeIncrementMSecs = sizeOfTimeIncrementMSecs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.time.FederateClockManager#
	 * getIntervalBetweenTimeUpdatesMSecs ()
	 */
	@Override
	public final long getIntervalBetweenTimeUpdatesMSecs() {
		return intervalBetweenTimeUpdatesMSecs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.time.FederateClockManager#
	 * setIntervalBetweenTimeUpdatesMSecs (long)
	 */
	@Override
	public final void setIntervalBetweenTimeUpdatesMSecs(
			long intervalBetweenTimeUpdatesMSecs) {
		this.intervalBetweenTimeUpdatesMSecs = intervalBetweenTimeUpdatesMSecs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.csc.muthur.time.FederateClockManager#
	 * getMaxNumOfConsecutiveFailedAttemptsAllowed()
	 */
	@Override
	public int getMaxNumOfConsecutiveFailedAttemptsAllowed() {
		return maxNumOfConsecutiveFailedAttemptsAllowed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.csc.muthur.time.FederateClockManager#
	 * setMaxNumOfConsecutiveFailedAttemptsAllowed(int)
	 */
	@Override
	public void setMaxNumOfConsecutiveFailedAttemptsAllowed(
			int maxNumOfConsecutiveFailedAttemptsAllowed) {
		this.maxNumOfConsecutiveFailedAttemptsAllowed =
				maxNumOfConsecutiveFailedAttemptsAllowed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.time.FederateClockManager#getFederationExecutionModel
	 * ()
	 */
	public FederationExecutionModel getFederationExecutionModel() {
		return federationExecutionModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.time.FederateClockManager#setFederationExecutionModel
	 * (com .csc.muthur.model.FederationExecutionModel)
	 */
	public void setFederationExecutionModel(
			FederationExecutionModel federationExecutionModel) {
		this.federationExecutionModel = federationExecutionModel;
	}

	/**
	 * @param continueRunning
	 *          the continueRunning to set
	 */
	public void setContinueRunning(boolean continueRunning) {
		this.continueRunning = continueRunning;
	}

}
