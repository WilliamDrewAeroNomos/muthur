package com.csc.muthur.server.federation.internal.execution;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.federation.FederationExecutionController;
import com.csc.muthur.server.model.FederationExecutionEntry;
import com.csc.muthur.server.model.FederationState;
import com.csc.muthur.server.model.event.response.JoinFederationResponse;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class StartFederationRequestProcessor extends AbstractRequestProcessor {

	private static final Logger LOG =
			LoggerFactory.getLogger(StartFederationRequestProcessor.class.getName());

	private FederationExecutionController federationExecutionController;

	private CountDownLatch processorCountDownLatch;

	/**
	 * 
	 * @param federationExecutionController
	 * @throws MuthurException
	 */
	public StartFederationRequestProcessor(
			final FederationExecutionController federationExecutionController)
			throws MuthurException {
		super();

		if (federationExecutionController.getFederationExecutionModel() == null) {
			throw new MuthurException("Invalid argument. "
					+ "Federation execution model was null");
		}

		this.federationExecutionController = federationExecutionController;

		if (federationExecutionController.getFederationExecutionModel() == null) {
			throw new MuthurException("Invalid argument. "
					+ "Federation execution model was null");
		}

		// if auto start we can simply set the count down latch to 0

		if (federationExecutionController.getFederationExecutionModel()
				.isAutoStart()) {

			processorCountDownLatch = new CountDownLatch(0);

			setTimeToLiveMSecs(0);

			LOG.debug("Duration for waiting for start federation directive for ["
					+ federationExecutionController.getFederationExecutionModel()
							.getName()
					+ "] is 0 since autoStart is ["
					+ federationExecutionController.getFederationExecutionModel()
							.isAutoStart() + "]");

		} else {

			// else if the autoStart is true then set it to 1 then wait for a single
			// federation directive

			processorCountDownLatch = new CountDownLatch(1);

			if (federationExecutionController.getFederationExecutionModel()
					.getDurationWaitForStartFederationDirectiveMSecs() > 0) {

				setTimeToLiveMSecs(federationExecutionController
						.getFederationExecutionModel()
						.getDurationWaitForStartFederationDirectiveMSecs());

			} else {

				setTimeToLiveMSecs(federationExecutionController
						.getFederationExecutionModel()
						.getDefaultDurationWithinStartupProtocolMSecs());

			}

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {

		try {

			// set the initial execution state for this processor

			federationExecutionController.setExecutionState(FederationState.STARTED);

			// release any processes that called waitTillStarted() on this processor

			getThreadStartedSem().release();

			if (!federationExecutionController.getFederationExecutionModel()
					.isAutoStart()) {

				LOG.debug("Waiting on ["
						+ federationExecutionController.getFederationExecutionModel()
								.getNamesOfRequiredFederates().size()
						+ "] to receive a start federation direcitive for ["
						+ federationExecutionController.getFederationExecutionModel()
								.getName() + "] federation...");
			}

			if (!processorCountDownLatch.await(getTimeToLiveMSecs(),
					TimeUnit.MILLISECONDS)) {

				String errMsg =
						"Time expired before receiving a  "
								+ "start federation directive for ["
								+ federationExecutionController.getFederationExecutionModel()
										.getName()
								+ "] federation. Check your federation execution model for "
								+ "the autoStart attribute value.";

				LOG.error(errMsg);

				for (FederationExecutionEntry fee : federationExecutionController
						.getFederateNameToFederationExecutionParticipant().values()) {

					LOG
							.debug("Create the error messages from the join federation response list...");

					IEvent response = new JoinFederationResponse();

					federationExecutionController.returnErrorResponse(fee, response,
							errMsg);
				}

				federationExecutionController.getFederationTTLThread()
						.setTerminationReason(errMsg);

				federationExecutionController.terminate();

			} else {

				// processor as completed

				setSatisfied(true);

				// Start the execution controller which will start the time management
				// and set the execution state to RUNNING
				//
				federationExecutionController.startExecution();

			}

		} catch (Exception e) {
			LOG.error(e.getLocalizedMessage());
		}

	}

	/**
	 * 
	 * @param rrfe
	 * @throws MuthurException
	 */
	public void release() {

		processorCountDownLatch.countDown();
	}

}
