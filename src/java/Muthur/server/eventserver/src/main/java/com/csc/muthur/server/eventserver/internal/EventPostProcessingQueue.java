/**
 * 
 */
package com.csc.muthur.server.eventserver.internal;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.IEvent;

/**
 * 
 * @author <a href=mailto:support@atcloud.com>support</a>
 * @version $Revision: $
 */
public class EventPostProcessingQueue {

	private static final Logger LOG = LoggerFactory
			.getLogger(EventPostProcessingQueue.class.getName());

	private BlockingQueue<IEvent> processedEventQueue =
			new LinkedBlockingQueue<IEvent>();

	private EventPostProcessor eventPostProcessor;
	private Thread eventPostProcessorThread;
	private boolean continueProcessing;

	/**
	 * 
	 */
	public EventPostProcessingQueue() {

	}

	/**
	 * 
	 */
	public void start() {

		continueProcessing = true;

		eventPostProcessor = new EventPostProcessor();

		eventPostProcessorThread = new Thread(eventPostProcessor);

		if (eventPostProcessorThread == null) {

			LOG.warn("Unable to create EventPostProcessor thread. "
					+ "Event post processing will not be conducted.");
		} else {

			eventPostProcessorThread.start();
		}
	}

	public void stop() {

		if (eventPostProcessorThread != null) {
			setContinueProcessing(false);
			eventPostProcessorThread.interrupt();
			eventPostProcessorThread = null;
		}
	}

	/**
	 * 
	 * @param event
	 */
	public void put(final IEvent event) {
		if (event != null) {
			processedEventQueue.add(event);
		}
	}

	/**
	 * 
	 * 
	 * @author <a href=mailto:support@atcloud.com>support</a>
	 * @version $Revision: $
	 */
	private class EventPostProcessor implements Runnable {

		/**
		 * 
		 */
		public EventPostProcessor() {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {

			LOG.info("Event post processor thread started.");

			while (continueProcessing) {

				try {

					IEvent event = processedEventQueue.take();

					if (event != null) {

						LOG.debug("Post processing [" + event.getEventName() + "] event.");

						// send to the Registration service to update current federate
						// heartbeats

						// send to the Metric service for processing
					}

				} catch (InterruptedException e) {
					LOG.info("Event post processor thread has been interrupted");
					setContinueProcessing(false);
				}

			}

			LOG.info("Event post processor thread exiting.");
		}

	}

	/**
	 * @return the eventPostProcessorThread
	 */
	public Thread getEventPostProcessorThread() {
		return eventPostProcessorThread;
	}

	/**
	 * @return the continueProcessing
	 */
	public boolean isContinueProcessing() {
		return continueProcessing;
	}

	/**
	 * @param continueProcessing
	 *          the continueProcessing to set
	 */
	public void setContinueProcessing(boolean continueProcessing) {
		this.continueProcessing = continueProcessing;
	}
}
