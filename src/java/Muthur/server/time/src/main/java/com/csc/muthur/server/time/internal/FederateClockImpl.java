package com.csc.muthur.server.time.internal;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TemporaryQueue;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.FederationExecutionDirective;
import com.csc.muthur.server.model.ReadyToRunFederationExecutionEntry;
import com.csc.muthur.server.model.event.request.ReadyToRunRequest;
import com.csc.muthur.server.model.event.request.TimeManagementRequest;
import com.csc.muthur.server.time.FederateClock;
import com.csc.muthur.server.time.FederateClockManager;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class FederateClockImpl implements FederateClock {

	public static final Logger LOG = LoggerFactory
			.getLogger(FederateClockImpl.class.getName());

	private ReadyToRunFederationExecutionEntry federateNameTofedExecReadyToRunEntry;
	private boolean continueRunning;
	private String timeMgmtQueueName;

	private Destination timeMgmtSendQueue;
	private MessageProducer publisher;
	private Session session;

	private TemporaryQueue jmsReplyTo;
	private FederateClockManager federateClockManager;
	private boolean bStartDirectiveSent = false;

	private Format timeFormat = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss.SSS");
	private Calendar calendar = Calendar.getInstance();
	private int numOfConsecutiveFailedAttempts;

	private int maxNumOfConsecutiveFailedAttemptsAllowed = 0;

	/**
	 * 
	 * @param federateClockManager
	 * @param readyToRunFederationExecutionEntry
	 * @throws MuthurException
	 */
	public FederateClockImpl(
			final FederateClockManager federateClockManager,
			final ReadyToRunFederationExecutionEntry readyToRunFederationExecutionEntry)
			throws MuthurException {

		if (federateClockManager == null) {
			throw new MuthurException("FederationClockManager parameter "
					+ "passed to FederateClockImpl ctor was null.");
		}

		this.federateClockManager = federateClockManager;

		if (readyToRunFederationExecutionEntry == null) {
			throw new MuthurException("ReadyToRunFederationExecutionEntry parameter "
					+ "passed to FederateClockImpl ctor was null.");
		}

		this.federateNameTofedExecReadyToRunEntry =
				readyToRunFederationExecutionEntry;

		this.session = federateClockManager.getSession();

		if (session == null) {
			throw new MuthurException("Session parameter "
					+ "passed to FederateClockImpl ctor was null.");
		}

		ReadyToRunRequest readyToRunRequest =
				(ReadyToRunRequest) readyToRunFederationExecutionEntry.getEvent();

		timeMgmtQueueName = readyToRunRequest.getTimeManagementQueueName();

		LOG.debug("Creating federate time queue [" + timeMgmtQueueName + "] for ["
				+ readyToRunFederationExecutionEntry.getFederateName() + "]");

		try {

			timeMgmtSendQueue = session.createQueue(timeMgmtQueueName);

			publisher = session.createProducer(timeMgmtSendQueue);

			publisher.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

			jmsReplyTo = session.createTemporaryQueue();

		} catch (JMSException e) {
			throw new MuthurException(e);
		}

		continueRunning = true;

		numOfConsecutiveFailedAttempts = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {

		LOG.debug("Initiated clock for federate ["
				+ federateNameTofedExecReadyToRunEntry.getFederateName() + "].");

		while (isContinueRunning()) {

			try {

				if (federateClockManager.getSyncSemaphore().tryAcquire(1, 60000,
						TimeUnit.MILLISECONDS)) {

					if (isContinueRunning()) {

						sendTimeEvent();

						// reset
						numOfConsecutiveFailedAttempts = 0;
					}

				} else {

					LOG.warn("Unable to acquire semaphore for sending time to federate ["
							+ federateNameTofedExecReadyToRunEntry.getFederateName() + "]");
				}

			} catch (InterruptedException e) {
				LOG.warn("Clock for ["
						+ federateNameTofedExecReadyToRunEntry.getFederateName()
						+ "] has been interrupted and will be terminated.");
				continueRunning = false;
			} catch (JMSException e) {
				LOG.warn("Unable to send time event to federate ["
						+ federateNameTofedExecReadyToRunEntry.getFederateName() + "] - ["
						+ e.getLocalizedMessage() + "]");
				numOfConsecutiveFailedAttempts++;
			} finally {

				if (numOfConsecutiveFailedAttempts > maxNumOfConsecutiveFailedAttemptsAllowed) {
					LOG.warn("The number of consecutive failures ["
							+ numOfConsecutiveFailedAttempts
							+ "] sending a time event to federate ["
							+ federateNameTofedExecReadyToRunEntry.getFederateName()
							+ "] has exceeded the maximum allowed ["
							+ maxNumOfConsecutiveFailedAttemptsAllowed + "].");
					terminate();
				}

			}

		}

		LOG.info("Clock for federate ["
				+ federateNameTofedExecReadyToRunEntry.getFederateName()
				+ "] has terminated.");
	}

	/**
	 * @return
	 * @throws JMSException
	 */
	private TextMessage sendTimeEvent() throws JMSException {

		// set the body as the serialized time management event

		TimeManagementRequest timeManagementRequest = new TimeManagementRequest();

		timeManagementRequest.setTimeToLiveSecs(10);
		timeManagementRequest.setSourceOfEvent("Muthur");

		// check if the one and only start directive has been sent yet
		if (bStartDirectiveSent == false) {

			// if not then we'll override the return value
			// and set the flag so we'll only do this once
			bStartDirectiveSent = true;
			timeManagementRequest
					.setFederationExecutionDirective(FederationExecutionDirective.START);
		} else {

			// else send the current directive
			timeManagementRequest
					.setFederationExecutionDirective(federateClockManager
							.getFederationExecutionDirective());
		}

		timeManagementRequest.setFederationExecutionState(federateClockManager
				.getFederationExecutionState());

		// get the current time from the federate clock manager which
		// started this thread

		timeManagementRequest.setTimeIntervalMSecs(federateClockManager
				.getCurrentTimeMSecs());

		TextMessage timeEventMessage = session.createTextMessage();

		timeEventMessage.setText(timeManagementRequest.serialize());

		// Set the reply to field to the temporary queue created for this
		// federate clock

		timeEventMessage.setJMSReplyTo(jmsReplyTo);

		publisher.send(timeEventMessage);

		calendar.setTimeInMillis(timeManagementRequest.getTimeIntervalMSecs());

		LOG.debug("[" + timeManagementRequest.getFederationExecutionDirective()
				+ "] event sent to ["
				+ federateNameTofedExecReadyToRunEntry.getFederateName()
				+ "] - federation time [" + timeFormat.format(calendar.getTime()) + "]");

		return timeEventMessage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.time.internal.FederateClock#isContinueRunning()
	 */
	@Override
	public final boolean isContinueRunning() {
		return continueRunning;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.time.internal.FederateClock#terminate()
	 */
	@Override
	public final void terminate() {
		LOG.debug("Clock has been set to terminate for federate ["
				+ federateNameTofedExecReadyToRunEntry.getFederateName() + "]");
		continueRunning = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.time.internal.FederateClock#
	 * getMaxNumOfConsecutiveFailedAttemptsAllowed()
	 */
	@Override
	public int getMaxNumOfConsecutiveFailedAttemptsAllowed() {
		return maxNumOfConsecutiveFailedAttemptsAllowed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.time.internal.FederateClock#
	 * setMaxNumOfConsecutiveFailedAttemptsAllowed(int)
	 */
	@Override
	public void setMaxNumOfConsecutiveFailedAttemptsAllowed(
			int maxNumOfConsecutiveFailedAttemptsAllowed) {
		this.maxNumOfConsecutiveFailedAttemptsAllowed =
				maxNumOfConsecutiveFailedAttemptsAllowed;
	}
}
