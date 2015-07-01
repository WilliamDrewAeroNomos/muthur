package com.csc.muthur.server.federation.internal.execution;

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
import com.csc.muthur.server.model.SubscriptionRegistrationFederationExecutionEntry;
import com.csc.muthur.server.model.event.response.DataSubscriptionResponse;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class SubscriptionRequestProcessor extends AbstractRequestProcessor {

	private static final Logger LOG =
			LoggerFactory.getLogger(SubscriptionRequestProcessor.class.getName());

	private FederationExecutionController federationExecutionController;

	private Map<String, SubscriptionRegistrationFederationExecutionEntry> federateNameToFederationExecutionSubscription =
			new ConcurrentHashMap<String, SubscriptionRegistrationFederationExecutionEntry>();

	private BlockingQueue<SubscriptionRegistrationFederationExecutionEntry> notificationQueue =
			new LinkedBlockingQueue<SubscriptionRegistrationFederationExecutionEntry>();

	private CountDownLatch subscriptionCountDownLatch;

	/**
	 * 
	 * @param federationExecutionController
	 * @throws MuthurException
	 */
	public SubscriptionRequestProcessor(
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

		subscriptionCountDownLatch =
				new CountDownLatch(federationExecutionController
						.getFederationExecutionModel().getNamesOfRequiredFederates().size());

		if (federationExecutionController.getFederationExecutionModel()
				.getDurationRegisterPublicationMSecs() > 0) {
			setTimeToLiveMSecs(federationExecutionController
					.getFederationExecutionModel().getDurationRegisterSubscriptionMSecs());
		} else {
			setTimeToLiveMSecs(federationExecutionController
					.getFederationExecutionModel()
					.getDefaultDurationWithinStartupProtocolMSecs());
		}

		if (0 == getTimeToLiveMSecs()) {
			LOG.warn("Duration for registering subscriptions for ["
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
					.setExecutionState(FederationState.ACCEPTING_SUBSCRIPTIONS);

			// release any processes that called waitTillStarted() on this processor

			getThreadStartedSem().release();

			LOG.debug("Waiting on ["
					+ federationExecutionController.getFederationExecutionModel()
							.getNamesOfRequiredFederates().size()
					+ "] to send subscriptions for the ["
					+ federationExecutionController.getFederationExecutionModel()
							.getName() + "] federation...");

			if (!subscriptionCountDownLatch.await(getTimeToLiveMSecs(),
					TimeUnit.MILLISECONDS)) {

				String errMsg =
						"Time expired before all required federates "
								+ "registered subscriptions for the ["
								+ federationExecutionController.getFederationExecutionModel()
										.getName() + "] federation.";

				LOG.error(errMsg);

				for (FederationExecutionEntry fee : federateNameToFederationExecutionSubscription
						.values()) {

					LOG.debug("Create the error response...");

					IEvent response = new DataSubscriptionResponse();

					federationExecutionController.returnErrorResponse(fee, response,
							errMsg);
				}

				federationExecutionController.getFederationTTLThread()
						.setTerminationReason(errMsg);

				federationExecutionController.terminate();

			} else {

				// transitions the federation to the next execution state in the boot
				// process and sets the state of the federation
				//
				federationExecutionController
						.advanceBootProcess(FederationState.SUBSCRIBED);

				// processor as completed
				
				setSatisfied(true);

				while (isContinueProcessing()) {

					SubscriptionRegistrationFederationExecutionEntry fedExecSub =
							notificationQueue.take();

					if (fedExecSub != null) {

						LOG.debug("Create the subscription response...");

						DataSubscriptionResponse response = new DataSubscriptionResponse();
						response.initialization(fedExecSub.getEvent().serialize());

						LOG.debug("Setting the federation execution handle in response...");

						response.setFederationExecutionHandle(federationExecutionController
								.getFederationExecutionHandle());

						response.setSourceOfEvent(MessageDestination.MUTHUR);

						response.setStatus("complete");
						response.setSuccess(true);

						LOG.debug("Sending subscription response to federate ["
								+ fedExecSub.getFederateName() + "]...");

						federationExecutionController.getFederationService()
								.getRouterService().queue(response.serialize(),
										fedExecSub.getMessage().getJMSReplyTo(),
										fedExecSub.getMessage().getJMSCorrelationID());

						LOG.info("Sent subscription response to federate ["
								+ fedExecSub.getFederateName() + "].");

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
	public void addEntry(
			final SubscriptionRegistrationFederationExecutionEntry srfee) {

		if ((srfee != null) && (srfee.getFederateName() != null)
				&& (!"".equals(srfee.getFederateName()))) {

			// add to the list of entries

			federateNameToFederationExecutionSubscription.put(
					srfee.getFederateName(), srfee);

			// add to the notification queue...

			notificationQueue.add(srfee);

			// decrement the count if it is a required federate...

			if (federationExecutionController.getFederationExecutionModel()
					.isRequired(srfee.getFederateName())) {

				subscriptionCountDownLatch.countDown();

				LOG.debug("Decremented subscription registration"
						+ " countdown latch for federation ["
						+ srfee.getFederationExecutionModel().getName() + "]");
			}
		}
	}

	/**
	 * @return the federateNameToFederationExecutionSubscription
	 */
	public Map<String, SubscriptionRegistrationFederationExecutionEntry> getFederateNameToFederationExecutionSubscription() {
		return federateNameToFederationExecutionSubscription;
	}

}
