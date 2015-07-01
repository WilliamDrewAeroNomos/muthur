/**
 * 
 */
package com.csc.muthur.server.federation.internal.execution;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.federation.FederationExecutionController;
import com.csc.muthur.server.model.event.DataPublicationEvent;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public class FederationDataPublicationQueueImpl implements
		FederationDataPublicationQueue {

	private static final Logger LOG = LoggerFactory
			.getLogger(FederationDataPublicationQueueImpl.class.getName());

	private FederationDataPublicationQueueProcessor federationDataPublicationQueueProcessor;

	private Thread federationDataPublicationQueueProcessorThread;

	private BlockingQueue<DataPublicationEvent> dataPublicationQueue =
			new LinkedBlockingQueue<DataPublicationEvent>();

	private FederationExecutionController federationExecutionController;

	/**
	 * @throws MuthurException
	 * 
	 */
	public FederationDataPublicationQueueImpl(
			final FederationExecutionController federationExecutionController)
			throws MuthurException {

		if (federationExecutionController == null) {
			throw new MuthurException(
					"Federation execution controller reference pass to the FederationDataPublicationQueue was null");
		}

		this.federationExecutionController = federationExecutionController;

		federationDataPublicationQueueProcessor =
				new FederationDataPublicationQueueProcessor();

		federationDataPublicationQueueProcessorThread =
				new Thread(federationDataPublicationQueueProcessor);

		federationDataPublicationQueueProcessorThread.start();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.federation.internal.execution.
	 * FederationDataPublicationQueue#terminate()
	 */
	@Override
	public void terminate() {

		if (federationDataPublicationQueueProcessor != null) {
			federationDataPublicationQueueProcessor.setStillRunning(false);
		}

		if (federationDataPublicationQueueProcessorThread != null) {
			federationDataPublicationQueueProcessorThread.interrupt();
		}

	}

	/**
	 * 
	 * @param dataPublicationEvent
	 */
	@Override
	public void put(final DataPublicationEvent dataPublicationEvent) {

		if (dataPublicationEvent != null) {

			try {

				if (dataPublicationQueue.offer(dataPublicationEvent, 5,
						TimeUnit.SECONDS)) {
					LOG.debug("Added [" + dataPublicationEvent.getEventName()
							+ "] to federation publication queue");
				} else {
					LOG.error("Time expired attempting to add ["
							+ dataPublicationEvent.getEventName()
							+ "] to federation publication queue");
				}

			} catch (InterruptedException e) {
				LOG.warn("Attempt to add [" + dataPublicationEvent.getEventName()
						+ "] to federation publication queue was interrupted");
			}

		}
	}

	/**
	 * 
	 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
	 * @version $Revision$
	 */
	private class FederationDataPublicationQueueProcessor implements Runnable {

		private boolean stillRunning = true;

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {

			while (isStillRunning()) {

				try {

					DataPublicationEvent dp = dataPublicationQueue.take();

					if ((dp != null) && (federationExecutionController != null)) {

						try {

							federationExecutionController.publishData(dp);

						} catch (MuthurException e) {
							LOG.error("Exception raised publishing data", e);
						}

					}

				} catch (InterruptedException e) {

					LOG.debug("Federation data publication queue processor was interrupted.");

					setStillRunning(false);

					if (dataPublicationQueue.size() > 0) {
						LOG.info("["
								+ dataPublicationQueue.size()
								+ "] entries remaining on the federation data publication queue when terminated.");
					}
				}

			}

			LOG.info("Federation data request queue is terminating.");

		}

		/**
		 * 
		 * @return
		 */
		public boolean isStillRunning() {
			return stillRunning;
		}

		/**
		 * 
		 * @param stillRunning
		 */
		public void setStillRunning(boolean stillRunning) {
			this.stillRunning = stillRunning;
		}

	}

}
