package com.csc.muthur.server.ownership.internal;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.DataChannelControlBlock;
import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.ownership.PendingFederateRequestEntry;
import com.csc.muthur.server.ownership.RequestTriageThread;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public class RequestTriageThreadImpl implements RequestTriageThread {

	private static final Logger LOG = LoggerFactory
			.getLogger(RequestTriageThreadImpl.class.getName());

	private Map<String, PendingFederateRequestEntry> requestToCallbackMap =
			new ConcurrentHashMap<String, PendingFederateRequestEntry>();

	private Semaphore threadWaitSem = new Semaphore(0);
	private Semaphore mapAccessSem = new Semaphore(1);

	private boolean continueRunning = true;
	private long intervalBetweenTriages = 5;
	private long pauseBeforeBeginTriaging = 3;

	private Calendar c = Calendar.getInstance();
	private Format f = new SimpleDateFormat("yyyy.MM.dd HH.mm.ss");

	/**
	 * 
	 */
	public RequestTriageThreadImpl() {
		super();
	}

	/**
	 * 
	 */
	public void run() {

		LOG.debug("RequestTriage thread is running.");

		while (isContinueRunning()) {

			try {

				threadWaitSem.tryAcquire(pauseBeforeBeginTriaging, TimeUnit.SECONDS);

				if (mapAccessSem.tryAcquire(intervalBetweenTriages, TimeUnit.SECONDS)) {

					try {

						if (requestToCallbackMap.size() > 0) {

							LOG.debug("Reviewing [" + requestToCallbackMap.size()
									+ "] pending ownership transfer request(s) for triage");

							for (String correlationID : requestToCallbackMap.keySet()) {

								if (correlationID != null) {

									PendingFederateRequestEntry frcbEntry =
											requestToCallbackMap.get(correlationID);

									if (frcbEntry != null) {

										IEvent event = frcbEntry.getEvent();

										if (event != null) {

											long currTimeSecs = System.currentTimeMillis();
											long expirationTimeSecs =
													event.getExpirationTimeMilliSecs();

											if (currTimeSecs > expirationTimeSecs) {

												// remove it from the map
												requestToCallbackMap.remove(correlationID);

												// format the message
												String msgString =
														formatMessage(event, currTimeSecs,
																expirationTimeSecs);

												LOG.debug(msgString);

												// send the error back to the caller
												frcbEntry.getCallback().onError(frcbEntry,
														new MuthurException(msgString));
											}
										}
									}
								}
							}
						}
					} finally {
						mapAccessSem.release();
					}

				} else {
					LOG.info("Unable to acquire access to the request map to remove old requests");
				}

			} catch (InterruptedException e) {
				setContinueRunning(false);
			}
		}

		LOG.debug("Request triage thread is terminating...");
	}

	/**
	 * 
	 */
	private String formatMessage(final IEvent event, long currTimeSecs,
			long expirationTimeSecs) {

		String msgString = null;

		c.setTimeInMillis(currTimeSecs);
		Date currDate = c.getTime();
		c.setTimeInMillis(expirationTimeSecs);
		Date expirationDate = c.getTime();

		Date createDate = new Date(event.getCreateTimeMilliSecs());
		msgString =
				"Request type [" + event.getEventName() + "] created on ["
						+ f.format(createDate) + "] expired. Request expiration ["
						+ f.format(expirationDate) + "] exceeded current time of ["
						+ f.format(currDate) + "].";

		return msgString;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.ownership.internal.RequestTriageThread#getCallback
	 * (java. lang.String)
	 */
	public RequestCallback getCallback(final String correlationID)
			throws InterruptedException {

		RequestCallback cb = null;

		mapAccessSem.tryAcquire(intervalBetweenTriages, TimeUnit.SECONDS);

		PendingFederateRequestEntry frcb = null;
		frcb = requestToCallbackMap.remove(correlationID);

		if (frcb != null) {
			cb = frcb.getCallback();
		}

		mapAccessSem.release();

		return cb;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.ownership.internal.RequestTriageThread#addRequest
	 * (com.csc .muthur.model.event.IEvent,
	 * com.csc.muthur.server.ownership.internal.RequestCallback)
	 */
	public boolean addRequest(final IEvent event,
			final RequestCallback requestCallback,
			final DataChannelControlBlock dccBlock) throws MuthurException {

		boolean addedRequest = false;

		try {
			if (mapAccessSem.tryAcquire(intervalBetweenTriages, TimeUnit.SECONDS)) {

				try {

					PendingFederateRequestEntry pendingFederateRequestEntry =
							new PendingFederateRequestEntry(event, requestCallback, dccBlock);

					requestToCallbackMap.put(event.getEventUUID(),
							pendingFederateRequestEntry);

					addedRequest = true;

				} finally {
					mapAccessSem.release();
				}
			}
		} catch (InterruptedException e) {
			throw new MuthurException("Unable to acquire access to request callback "
					+ "map to add pending request to transfer ownership.");
		}

		return addedRequest;
	}

	/**
	 * 
	 * @param event
	 * @return
	 */
	@Override
	public PendingFederateRequestEntry getRequest(final IEvent event) {

		PendingFederateRequestEntry pendingFederateRequestEntry = null;

		if ((event != null) && (event.getEventUUID() != null)
				&& (event.getEventUUID().length() > 0)) {

			pendingFederateRequestEntry =
					requestToCallbackMap.get(event.getEventUUID());

		}

		return pendingFederateRequestEntry;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.ownership.internal.RequestTriageThread#setContinueRunning
	 * (boolean)
	 */
	public void setContinueRunning(boolean continueRunning) {
		this.continueRunning = continueRunning;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.ownership.internal.RequestTriageThread#isContinueRunning
	 * ()
	 */
	public boolean isContinueRunning() {
		return continueRunning;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.csc.muthur.ownership.internal.RequestTriageThread#
	 * getPauseBeforeBeginTriaging()
	 */
	public final long getPauseBeforeBeginTriaging() {
		return pauseBeforeBeginTriaging;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.csc.muthur.ownership.internal.RequestTriageThread#
	 * setPauseBeforeBeginTriaging(long)
	 */
	public final void setPauseBeforeBeginTriaging(long pauseBeforeBeginTriaging) {
		this.pauseBeforeBeginTriaging = pauseBeforeBeginTriaging;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.ownership.internal.RequestTriageThread#
	 * getIntervalBetweenTriages ()
	 */
	public final long getIntervalBetweenTriages() {
		return intervalBetweenTriages;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.ownership.internal.RequestTriageThread#
	 * setIntervalBetweenTriages (long)
	 */
	public final void setIntervalBetweenTriages(long intervalBetweenTriages) {
		this.intervalBetweenTriages = intervalBetweenTriages;
	}

	@Override
	public PendingFederateRequestEntry removeRequest(IEvent event) {

		PendingFederateRequestEntry pendingFederateRequestEntry = null;

		if ((event != null) && (event.getEventUUID() != null)
				&& (event.getEventUUID().length() > 0)) {

			pendingFederateRequestEntry =
					requestToCallbackMap.remove(event.getEventUUID());

		}

		return pendingFederateRequestEntry;
	}
}
