/**
 * 
 */
package com.csc.muthur.server.registration.internal;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.MessageDestination;
import com.csc.muthur.server.commons.ServiceName;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.event.FederateDeregistrationEvent;
import com.csc.muthur.server.model.event.FederateHeartbeatEvent;
import com.csc.muthur.server.model.event.request.FederateRegistrationRequest;
import com.csc.muthur.server.registration.RegistrationService;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>NexSim</a>
 * @version $Revision$
 */
public class FederateHeartBeat implements Runnable {

	private static final Logger LOG = LoggerFactory
			.getLogger(FederateHeartBeat.class.getName());

	private FederateRegistrationRequest federateRegistrationRequest;
	private Destination federateHeartBeatQueue;
	private MessageProducer heartbeatProducer;

	private Session session;
	private boolean stillAlive = true;
	private int intervalBetweenHeartBeatsSecs = 5;

	private int maxNumOfConsecutiveFailedHeartbeatAttemptsAllowed = 1;
	private int numOfConsecutiveFailedAttempts = 0;
	private Semaphore sem = new Semaphore(-1);

	private RegistrationService registrationService;

	/**
	 * 
	 * @param registrationService
	 * @param federateRegistrationRequest
	 * @param session
	 * @throws MuthurException
	 */
	public FederateHeartBeat(final RegistrationService registrationService,
			final FederateRegistrationRequest federateRegistrationRequest,
			final Session session) throws MuthurException {

		if (registrationService == null) {
			throw new MuthurException(
					"RegistrationService passed to FederateHeartBeat ctor was null");
		}

		if (federateRegistrationRequest == null) {
			throw new MuthurException(
					"FederateRegistrationRequest passed to FederateHeartBeat ctor was null");
		}

		LOG.debug("Creating heartbeat thread ctor for federate ["
				+ federateRegistrationRequest.getFederateName() + "]...");

		if (federateRegistrationRequest.getFederateEventQueueName() == null) {
			throw new MuthurException(
					"Federate event queue name passed to FederateHeartBeat ctor was null");
		}

		if (session == null) {
			throw new MuthurException("Session passed to FederateHeartBeat was null");
		}

		this.registrationService = registrationService;

		if (registrationService.getConfigurationService() != null) {

			// set the max number of consecutive failed attempts from the
			// configuration service
			setMaxNumOfConsecutiveFailedHeartbeatAttemptsAllowed(registrationService
					.getConfigurationService()
					.getMaxNumOfConsecutiveFailedHeartbeatAttemptsAllowed());

			// set the interval between heartbeat attempts from the configuration
			// service
			setIntervalBetweenHeartBeatsSecs(registrationService
					.getConfigurationService().getIntervalBetweenHeartBeatsSecs());
		}

		this.federateRegistrationRequest = federateRegistrationRequest;
		this.session = session;

		try {

			LOG.debug("Creating heart beat queue ["
					+ federateRegistrationRequest.getFederateEventQueueName() + " for "
					+ federateRegistrationRequest.getFederateName() + "]...");

			federateHeartBeatQueue =
					session.createQueue(federateRegistrationRequest
							.getFederateEventQueueName());

			LOG.debug("Creating producer for heart beat queue ["
					+ federateRegistrationRequest.getFederateEventQueueName() + "]...");

			heartbeatProducer = session.createProducer(federateHeartBeatQueue);

			heartbeatProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			LOG.debug("Created heartbeat thread for federate ["
					+ federateRegistrationRequest.getFederateName() + "]...");

		} catch (JMSException e) {
			throw new MuthurException(e);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		LOG.debug("Started heartbeat thread for federate ["
				+ federateRegistrationRequest.getFederateName() + "]");

		while (isStillAlive()) {

			try {

				/*
				 * We will simply use the semaphore to block for configuration interval.
				 */
				sem.tryAcquire(intervalBetweenHeartBeatsSecs, TimeUnit.SECONDS);

				sendHeartbeat();

				// reset
				numOfConsecutiveFailedAttempts = 0;

			} catch (InterruptedException e) {

				LOG.info("Heart beat thread for federate ["
						+ federateRegistrationRequest.getFederateName()
						+ "] interrupted - [" + e.getLocalizedMessage() + "]");
				setStillAlive(false);

			} catch (JMSException e) {

				LOG.warn("Unable to send heart beat message to ["
						+ federateRegistrationRequest.getFederateName() + "] - ["
						+ e.getLocalizedMessage() + "]");
				numOfConsecutiveFailedAttempts++;

			} finally {

				if (isStillAlive()) {

					if (numOfConsecutiveFailedAttempts > maxNumOfConsecutiveFailedHeartbeatAttemptsAllowed) {
						LOG.warn("The number of consecutive failures ["
								+ numOfConsecutiveFailedAttempts
								+ "] sending a heart beat to federate ["
								+ federateRegistrationRequest.getFederateName()
								+ "] has exceeded the maximum allowed ["
								+ maxNumOfConsecutiveFailedHeartbeatAttemptsAllowed + "].");
						setStillAlive(false);
					}

				}

			}
		}

		LOG.debug("Posting message to [" + ServiceName.EVENT
				+ "] service to deregister ["
				+ federateRegistrationRequest.getFederateName() + "]");

		FederateDeregistrationEvent event = new FederateDeregistrationEvent();

		event.setSourceOfEvent(MessageDestination.MUTHUR);

		event.setFederateRegistrationHandle(federateRegistrationRequest
				.getFederateRegistrationHandle());
		event.setFederateName(federateRegistrationRequest.getFederateName());

		try {
			registrationService.getRouterService().postEvent(event.serialize(),
					ServiceName.EVENT);
		} catch (MuthurException e) {
			LOG.warn("Problem posting event to deregister federate ["
					+ federateRegistrationRequest.getFederateName() + "]");
		}

		LOG.info("Heart beat thread for ["
				+ federateRegistrationRequest.getFederateName() + "] is exiting.");
	}

	/**
	 * 
	 * @throws JMSException
	 */
	private void sendHeartbeat() throws JMSException {

		FederateHeartbeatEvent fhbe = new FederateHeartbeatEvent();
		fhbe.setSourceOfEvent(MessageDestination.MUTHUR);

		TextMessage tm = session.createTextMessage(fhbe.serialize());

		if (tm != null) {

			LOG.debug("Setting heart beat message payload...");

			if (heartbeatProducer != null) {

				LOG.debug("Sending heart beat to federate ["
						+ federateRegistrationRequest.getFederateName() + "]...");

				heartbeatProducer.send(tm);

				LOG.debug("Heart beat sent to federate ["
						+ federateRegistrationRequest.getFederateName() + "]");
			}
		}
	}

	/**
	 * @param intervalBetweenHeartBeatsSecs
	 *          the intervalBetweenHeartBeatsSecs to set
	 */
	public void
			setIntervalBetweenHeartBeatsSecs(int intervalBetweenHeartBeatsSecs) {
		this.intervalBetweenHeartBeatsSecs = intervalBetweenHeartBeatsSecs;
	}

	/**
	 * @return the intervalBetweenHeartBeats
	 */
	public int getIntervalBetweenHeartBeatsSecs() {
		return intervalBetweenHeartBeatsSecs;
	}

	/**
	 * @return the stillAlive
	 */
	public final boolean isStillAlive() {
		return stillAlive;
	}

	/**
	 * @param stillAlive
	 *          the stillAlive to set
	 */
	public final void setStillAlive(boolean stillAlive) {
		this.stillAlive = stillAlive;
	}

	/**
	 * @return the federateHeartBeatQueue
	 */
	public final Destination getFederateHeartBeatQueue() {
		return federateHeartBeatQueue;
	}

	/**
	 * @return the maxNumOfConsecutiveFailedHeartbeatAttemptsAllowed
	 */
	public int getMaxNumOfConsecutiveFailedHeartbeatAttemptsAllowed() {
		return maxNumOfConsecutiveFailedHeartbeatAttemptsAllowed;
	}

	/**
	 * @param maxNumOfConsecutiveFailedHeartbeatAttemptsAllowed
	 *          the maxNumOfConsecutiveFailedHeartbeatAttemptsAllowed to set
	 */
	public void setMaxNumOfConsecutiveFailedHeartbeatAttemptsAllowed(
			int maxNumOfConsecutiveFailedHeartbeatAttemptsAllowed) {
		this.maxNumOfConsecutiveFailedHeartbeatAttemptsAllowed =
				maxNumOfConsecutiveFailedHeartbeatAttemptsAllowed;
	}

}
