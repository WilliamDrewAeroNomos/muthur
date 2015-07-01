package com.csc.muthur.server.federation;

import java.text.SimpleDateFormat;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.FederationState;

/**
 * Created when the {@link FederationExecutionController} is instantiated. It
 * blocks on a semaphore for the time period
 * {@link FederationExecutionModel#getFederationExecutionMSesc()}. Once the time
 * period expires the thread calls
 * {@link FederationExecutionController#sendTerminateNotifications()} to start
 * the termination process.
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public class FederationTimeToLiveThread implements Runnable {

	private static final Logger LOG =
			LoggerFactory.getLogger(FederationTimeToLiveThread.class.getName());

	private FederationExecutionModel federationExecutionModel;
	private long terminationTimeMSecs;

	private String terminationReason;

	private long durationOfFederationExecutionMSecs;
	private long durationTimeToWaitAfterTermination;

	private Semaphore federationTimeToLiveThreadSem = new Semaphore(0);

	private FederationExecutionController federationExecutionController;
	private FederationService federationService;

	/**
	 * @throws MuthurException
	 * 
	 */
	public FederationTimeToLiveThread(final FederationService federationService,
			final FederationExecutionController federationExecutionController)
			throws MuthurException {

		if (federationService == null) {
			throw new MuthurException(
					"Federation service parameter to FederationTimeToLiveThread was null.");
		}

		this.federationService = federationService;

		if (federationExecutionController == null) {
			throw new MuthurException(
					"Federation execution controller parameter to FederationTimeToLiveThread was null.");
		}

		this.federationExecutionController = federationExecutionController;

		// get the FEM from the federation controller

		this.federationExecutionModel =
				federationExecutionController.getFederationExecutionModel();

		if (federationExecutionModel == null) {
			throw new MuthurException(
					"Federation execution model retrieved by FederationTimeToLiveThread was null.");
		}

		// determine the duration of the federation
		//
		if (federationExecutionModel.getDurationFederationExecutionMSecs() > 0) {
			durationOfFederationExecutionMSecs =
					federationExecutionModel.getDurationFederationExecutionMSecs();
		}

		if (0 == federationExecutionModel.getDurationFederationExecutionMSecs()) {
			LOG
					.warn("Execution duration time for federation ["
							+ federationExecutionModel.getName()
							+ "] is set to 0. "
							+ " Termination process will be initiated immediately after startup.");
		}

		terminationTimeMSecs =
				System.currentTimeMillis()
						+ federationExecutionModel.getDurationFederationExecutionMSecs();

		durationTimeToWaitAfterTermination =
				federationExecutionModel.getDurationTimeToWaitAfterTerminationMSecs();

		if (0 == durationTimeToWaitAfterTermination) {
			durationTimeToWaitAfterTermination =
					federationExecutionModel
							.getDefaultDurationWithinStartupProtocolMSecs();
		}

		if (0 == durationTimeToWaitAfterTermination) {
			LOG
					.warn("Wait time for final termination for federation ["
							+ federationExecutionModel.getName()
							+ "] is set to 0. "
							+ " Termination process will proceed immediately after notification.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		LOG.debug("Started time to live thread.");

		LOG
				.debug("Federation execution controller for federation execution model ["
						+ federationExecutionModel.getName()
						+ "] will expire on ["
						+ new SimpleDateFormat("MM.dd.yyyy").format(terminationTimeMSecs)
						+ "] @ ["
						+ new SimpleDateFormat("HH:mm.ss").format(terminationTimeMSecs)
						+ "]");

		try {

			federationTimeToLiveThreadSem.tryAcquire(
					durationOfFederationExecutionMSecs, TimeUnit.MILLISECONDS);

			federationExecutionController
					.setExecutionState(FederationState.TERMINATING);

			LOG
					.debug("Federation execution controller for federation execution model ["
							+ federationExecutionModel.getName() + "] is terminating...");

			try {

				federationExecutionController
						.sendTerminateNotifications(terminationReason);

			} catch (MuthurException me) {
				// log and proceed
				LOG.error(me.getLocalizedMessage());
			}

			federationExecutionController
					.setExecutionState(FederationState.TERMINATED);

			LOG
					.info("Federation execution controller for federation execution model ["
							+ federationExecutionModel.getName()
							+ "] is waiting ["
							+ durationTimeToWaitAfterTermination
							+ "] msec(s) before terminating");

			try {
				Thread.sleep(durationTimeToWaitAfterTermination);
			} catch (InterruptedException ie) {
				// nothing to do here
			}

			// close down connections

			federationExecutionController.cleanUpConnections();

			// set to FederationState.AWAITING_CLEANUP in order to be removed from the
			// list of controllers

			federationExecutionController
					.setExecutionState(FederationState.AWAITING_CLEANUP);

			// remove this instance from the list of active controllers

			federationService
					.removeFederationControllerInstance(federationExecutionModel);

		} catch (InterruptedException e) {

			LOG.debug("Time to live thread was interrupted and is exiting");

		} finally {

			// this will ensure that it gets removed from the list of active
			// controllers

			federationExecutionController
					.setExecutionState(FederationState.AWAITING_CLEANUP);

		}

	}

	/**
	 * 
	 */
	public void release() {
		federationTimeToLiveThreadSem.release();
	}

	/**
	 * @param terminationReason
	 *          the terminationReason to set
	 */
	public void setTerminationReason(String terminationReason) {
		this.terminationReason = terminationReason;
	}
}