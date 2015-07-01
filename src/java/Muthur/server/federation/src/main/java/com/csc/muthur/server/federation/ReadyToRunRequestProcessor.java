package com.csc.muthur.server.federation;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.commons.MessageDestination;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.federation.internal.execution.AbstractRequestProcessor;
import com.csc.muthur.server.model.FederationExecutionEntry;
import com.csc.muthur.server.model.FederationState;
import com.csc.muthur.server.model.ReadyToRunFederationExecutionEntry;
import com.csc.muthur.server.model.event.response.ReadyToRunResponse;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class ReadyToRunRequestProcessor extends AbstractRequestProcessor {

	private static final Logger LOG = LoggerFactory
			.getLogger(ReadyToRunRequestProcessor.class.getName());

	private FederationExecutionController federationExecutionController;

	private Map<String, ReadyToRunFederationExecutionEntry> federateNameTofedExecReadyToRunEntry =
			new ConcurrentHashMap<String, ReadyToRunFederationExecutionEntry>();

	private BlockingQueue<ReadyToRunFederationExecutionEntry> notificationQueue =
			new LinkedBlockingQueue<ReadyToRunFederationExecutionEntry>();

	private CountDownLatch readyToRunCountDownLatch;

	/**
	 * 
	 * @param federationExecutionController
	 * @throws MuthurException
	 */
	public ReadyToRunRequestProcessor(
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

		readyToRunCountDownLatch =
				new CountDownLatch(federationExecutionController
						.getFederationExecutionModel().getNamesOfRequiredFederates().size());

		if (federationExecutionController.getFederationExecutionModel()
				.getDurationRegisterToRunMSecs() > 0) {
			setTimeToLiveMSecs(federationExecutionController
					.getFederationExecutionModel().getDurationRegisterToRunMSecs());
		} else {
			setTimeToLiveMSecs(federationExecutionController
					.getFederationExecutionModel()
					.getDefaultDurationWithinStartupProtocolMSecs());
		}

		if (0 == getTimeToLiveMSecs()) {
			LOG.warn("Duration for registering ready to run for ["
					+ federationExecutionController.getFederationExecutionModel()
							.getName() + "] is 0");
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

			federationExecutionController
					.setExecutionState(FederationState.ACCEPTING_READY_TO_RUN);

			// release any processes that called waitTillStarted() on this processor

			getThreadStartedSem().release();

			LOG.debug("Waiting on ["
					+ federationExecutionController.getFederationExecutionModel()
							.getNamesOfRequiredFederates().size()
					+ "] to send in ready to run responses for ["
					+ federationExecutionController.getFederationExecutionModel()
							.getName() + "] federation...");

			if (!readyToRunCountDownLatch.await(getTimeToLiveMSecs(),
					TimeUnit.MILLISECONDS)) {

				String errMsg =
						"Time expired before all required federates "
								+ "registered to run for the ["
								+ federationExecutionController.getFederationExecutionModel()
										.getName() + "] federation.";

				LOG.error(errMsg);

				for (FederationExecutionEntry fee : getFederateNameTofedExecReadyToRunEntry()
						.values()) {

					LOG.debug("Create the error response...");

					IEvent response = new ReadyToRunResponse();

					federationExecutionController.returnErrorResponse(fee, response,
							errMsg);
				}

				federationExecutionController.getFederationTTLThread()
						.setTerminationReason(errMsg);

				federationExecutionController.terminate();

			} else {

				/*
				 * transitions the federation to the next execution state in the boot
				 * process and sets the state of the federation
				 */

				federationExecutionController
						.advanceBootProcess(FederationState.READY_TO_RUN);

				/*
				 * processor has successfully completed
				 */

				setSatisfied(true);

				while (isContinueProcessing()) {

					ReadyToRunFederationExecutionEntry execEntry =
							notificationQueue.take();

					if (execEntry != null) {

						LOG.debug("Create the ready to run response...");

						ReadyToRunResponse response = new ReadyToRunResponse();
						response.initialization(execEntry.getEvent().serialize());

						response.setSourceOfEvent(MessageDestination.MUTHUR);

						response.setStatus("complete");
						response.setSuccess(true);
						response.setFederationHostName(federationExecutionController
								.getFederationService().getDataChannelServerHostName());
						response.setFederationPort(federationExecutionController
								.getFederationService().getDataChannelServerPort());

						LOG.debug("Sending ready to run response to dispatcher...");

						federationExecutionController
								.getFederationService()
								.getRouterService()
								.queue(response.serialize(),
										execEntry.getMessage().getJMSReplyTo(),
										execEntry.getMessage().getJMSCorrelationID());

						LOG.info("Sent ready to run  response to federate ["
								+ execEntry.getFederateName() + "].");

						// add this entry to the federate clock manager

						federationExecutionController.getFederateClockManager()
								.addFederate(execEntry);

					}

				}
			}

		} catch (Exception e) {
			LOG.debug(e.getLocalizedMessage());
		}

	}

	/**
	 * 
	 * @param srfee
	 */
	public void addEntry(final ReadyToRunFederationExecutionEntry srfee) {

		if ((srfee != null) && (srfee.getFederateName() != null)
				&& (!"".equals(srfee.getFederateName()))) {

			// add to the list of entries

			federateNameTofedExecReadyToRunEntry.put(srfee.getFederateName(), srfee);

			// add to the notification queue...

			notificationQueue.add(srfee);

			// decrement the count if it is a required federate...

			if (federationExecutionController.getFederationExecutionModel()
					.isRequired(srfee.getFederateName())) {

				readyToRunCountDownLatch.countDown();

				LOG.debug("Decremented ready to run"
						+ " countdown latch for federation ["
						+ srfee.getFederationExecutionModel().getName() + "]");
			}
		}
	}

	/**
	 * @return the federateNameTofedExecReadyToRunEntry
	 */
	public Map<String, ReadyToRunFederationExecutionEntry>
			getFederateNameTofedExecReadyToRunEntry() {
		return federateNameTofedExecReadyToRunEntry;
	}

}
