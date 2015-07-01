package com.csc.muthur.server.federation.internal.execution;

import java.util.Iterator;
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
import com.csc.muthur.server.federation.FederationExecutionController;
import com.csc.muthur.server.model.FederationExecutionEntry;
import com.csc.muthur.server.model.FederationState;
import com.csc.muthur.server.model.JoinFederationExecutionEntry;
import com.csc.muthur.server.model.event.response.JoinFederationResponse;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class JoinFederationRequestProcessor extends AbstractRequestProcessor {

	private static final Logger LOG = LoggerFactory
			.getLogger(JoinFederationRequestProcessor.class.getName());

	private FederationExecutionController federationExecutionController;

	private Map<String, JoinFederationExecutionEntry> federateNameToJoinFederationExecutionEntry =
			new ConcurrentHashMap<String, JoinFederationExecutionEntry>();

	private BlockingQueue<JoinFederationExecutionEntry> notificationQueue =
			new LinkedBlockingQueue<JoinFederationExecutionEntry>();

	private CountDownLatch joinFederationCountDownLatch;

	/**
	 * 
	 * @param federationExecutionController
	 * @throws MuthurException
	 */
	public JoinFederationRequestProcessor(
			final FederationExecutionController federationExecutionController)
			throws MuthurException {
		super();

		if (federationExecutionController == null) {
			throw new MuthurException("Invalid argument. "
					+ "Federation execution controller was null");
		}

		this.federationExecutionController = federationExecutionController;

		if (federationExecutionController.getFederationExecutionModel() == null) {
			throw new MuthurException("Invalid argument. "
					+ "Federation execution model was null");
		}

		joinFederationCountDownLatch =
				new CountDownLatch(federationExecutionController
						.getFederationExecutionModel().getNamesOfRequiredFederates().size());

		if (federationExecutionController.getFederationExecutionModel()
				.getDurationJoinFederationMSecs() > 0) {
			setTimeToLiveMSecs(federationExecutionController
					.getFederationExecutionModel().getDurationJoinFederationMSecs());
		} else {
			setTimeToLiveMSecs(federationExecutionController
					.getFederationExecutionModel()
					.getDefaultDurationWithinStartupProtocolMSecs());
		}

		if (0 == getTimeToLiveMSecs()) {
			LOG.warn("Duration for joining the fedearation for ["
					+ federationExecutionController.getFederationExecutionModel()
							.getName() + "] is 0");
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		try {

			// set the initial execution state for this processor

			federationExecutionController
					.setExecutionState(FederationState.ACCEPTING_JOINS);

			// release any processes that called waitTillStarted() on this processor

			getThreadStartedSem().release();

			LOG.info("Waiting ["
					+ getTimeToLiveMSecs()
					+ "] msec(s) for ["
					+ federationExecutionController.getFederationExecutionModel()
							.getNamesOfRequiredFederates().size()
					+ "] federate(s) to join the ["
					+ federationExecutionController.getFederationExecutionModel()
							.getName()
					+ "] federation. Required federates - ["
					+ federationExecutionController.getFederationExecutionModel()
							.getNamesOfRequiredFederates() + "]");

			// wait for all the required federates to join before the required time
			// period expires
			//
			if (!joinFederationCountDownLatch.await(getTimeToLiveMSecs(),
					TimeUnit.MILLISECONDS)) {

				StringBuffer bufferOfJoinedFederates = new StringBuffer();

				if (notificationQueue.isEmpty()) {

					bufferOfJoinedFederates.append("No federates joined the federation.");

				} else {

					bufferOfJoinedFederates.append("Joined federate(s) - [");

					Iterator<JoinFederationExecutionEntry> iter =
							notificationQueue.iterator();

					if (iter != null) {

						// add the initial without appending a ", "
						if (iter.hasNext()) {
							JoinFederationExecutionEntry jfeeJoinedToDate = iter.next();
							bufferOfJoinedFederates
									.append(jfeeJoinedToDate.getFederateName());
						}

						while (iter.hasNext()) {
							JoinFederationExecutionEntry jfeeJoinedToDate = iter.next();
							bufferOfJoinedFederates.append(", ");
							bufferOfJoinedFederates
									.append(jfeeJoinedToDate.getFederateName());
						}
					}

					bufferOfJoinedFederates.append("]. Required federates - ["
							+ federationExecutionController.getFederationExecutionModel()
									.getNamesOfRequiredFederates() + "]");

				}

				String errMsg =
						"Time expired before all required federates joined the ["
								+ federationExecutionController.getFederationExecutionModel()
										.getName() + "] federation. "
								+ bufferOfJoinedFederates.toString();

				LOG.error(errMsg);

				for (FederationExecutionEntry fee : federationExecutionController
						.getFederateNameToFederationExecutionParticipant().values()) {

					LOG.debug("Create the error join federation response...");

					IEvent response = new JoinFederationResponse();

					federationExecutionController.returnErrorResponse(fee, response,
							errMsg);
				}

				federationExecutionController.getFederationTTLThread()
						.setTerminationReason(errMsg);

				federationExecutionController.terminate();

			} else {

				LOG.info("All required federates "
						+ federationExecutionController.getFederationExecutionModel()
								.getNamesOfRequiredFederates()
						+ " have joined the ["
						+ federationExecutionController.getFederationExecutionModel()
								.getName() + "] federation.");

				// Transition the state which will not return until the
				// FederationFlowController has been transitioned to the next state and
				// can receive requests from the required federates in the next state.
				//

				federationExecutionController
						.advanceBootProcess(FederationState.JOINED);

				// processor as completed

				setSatisfied(true);

				// now we can tell the federates that we are now ready
				// to receive requests to transition to the next state

				while (isContinueProcessing()) {

					JoinFederationExecutionEntry fep = notificationQueue.take();

					if (fep != null) {

						LOG.debug("Create the join federation response...");

						JoinFederationResponse response = new JoinFederationResponse();
						response.initialization(fep.getEvent().serialize());

						LOG.debug("Setting the federation execution handle in response...");

						response.setFederationExecutionHandle(federationExecutionController
								.getFederationExecutionHandle());

						response.setSourceOfEvent(MessageDestination.MUTHUR);

						response.setStatus("complete");
						response.setSuccess(true);

						LOG.debug("Sending join federation response to ["
								+ fep.getFederateName() + "]..");

						federationExecutionController
								.getFederationService()
								.getRouterService()
								.queue(response.serialize(), fep.getMessage().getJMSReplyTo(),
										fep.getMessage().getJMSCorrelationID());

						LOG.info("Sent join ["
								+ federationExecutionController.getFederationExecutionModel()
										.getName() + "] federation response to ["
								+ fep.getFederateName() + "].");
					}

				}

			}

		} catch (Exception e) {
			LOG.error(e.getLocalizedMessage());
		}

	}

	/**
	 * 
	 * @param joinFederationCountDownLatch
	 */
	public void setJoinFederationCountDownLatch(
			CountDownLatch joinFederationCountDownLatch) {
		this.joinFederationCountDownLatch = joinFederationCountDownLatch;
	}

	/**
	 * 
	 * @return
	 */
	public CountDownLatch getJoinFederationCountDownLatch() {
		return joinFederationCountDownLatch;
	}

	/**
	 * 
	 * @param jfee
	 */
	public void addEntry(final JoinFederationExecutionEntry jfee) {

		if ((jfee != null) && (jfee.getFederateName() != null)
				&& (!"".equals(jfee.getFederateName()))) {

			LOG.info("Federate [" + jfee.getFederateName() + "] joined ["
					+ jfee.getFederationExecutionModel().getName() + "]");

			federateNameToJoinFederationExecutionEntry.put(jfee.getFederateName(),
					jfee);

			// add to the notification queue...

			notificationQueue.add(jfee);

			// decrement the count if it a required federate...

			if (federationExecutionController.getFederationExecutionModel()
					.isRequired(jfee.getFederateName())) {

				LOG.debug("Decremented join countdown latch for + ["
						+ jfee.getFederationExecutionModel().getName()
						+ "] after required federate [" + jfee.getFederateName()
						+ "] joined.");

				joinFederationCountDownLatch.countDown();
			}
		}
	}

	/**
	 * 
	 * 
	 * @return federateNameToJoinFederationExecutionEntry
	 */
	public Map<String, JoinFederationExecutionEntry> getFederateNameToFederationExecutionParticipant() {
		return federateNameToJoinFederationExecutionEntry;
	}
}
