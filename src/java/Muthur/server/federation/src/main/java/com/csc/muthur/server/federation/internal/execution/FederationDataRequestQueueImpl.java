/**
 * 
 */
package com.csc.muthur.server.federation.internal.execution;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.IDataEventHandler;
import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.federation.FederationDataRequestQueue;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.event.response.GeneralErrorResponse;
import com.csc.muthur.server.router.RouterService;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public class FederationDataRequestQueueImpl implements
		FederationDataRequestQueue {

	private static final Logger LOG = LoggerFactory
			.getLogger(FederationDataRequestQueueImpl.class.getName());

	private BlockingQueue<IDataEventHandler> dataRequestEntryQueue =
			new LinkedBlockingQueue<IDataEventHandler>();

	private FederationDataRequestQueueProcessor federationDataRequestQueueProcessor;
	private Thread federationDataRequestQueueProcessorThread;
	private RouterService routerService;

	private FederationExecutionModel federationExecutionModel;
	private int cntEventsProcessed = 0;

	/**
	 * 
	 */
	public FederationDataRequestQueueImpl() {
		super();

		federationDataRequestQueueProcessor =
				new FederationDataRequestQueueProcessor();
		federationDataRequestQueueProcessorThread =
				new Thread(federationDataRequestQueueProcessor);
		federationDataRequestQueueProcessorThread.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.federation.internal.execution.FederationDataRequestQueue
	 * #put(com.csc.muthur.server.commons.IDataEventHandler)
	 */
	@Override
	public void put(IDataEventHandler eventHandler) {

		if ((eventHandler != null)
				&& (eventHandler.getDataChannelControlBlock() != null)
				&& (eventHandler.getEvent() != null)) {

			try {

				if (dataRequestEntryQueue.offer(eventHandler, 5, TimeUnit.SECONDS)) {
					LOG.debug("Added [" + eventHandler.getEvent().getEventName()
							+ "] to federation data request queue");
				} else {
					LOG.error("Time expired attempting to add ["
							+ eventHandler.getEvent().getEventName()
							+ "] to federation data request queue");
				}

			} catch (InterruptedException e) {
				LOG.warn("Attempt to add [" + eventHandler.getEvent().getEventName()
						+ "] to federation data request queue was interrupted");
				e.printStackTrace();
			}

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.federation.internal.execution.FederationDataRequestQueue
	 * #terminate()
	 */
	@Override
	public void terminate() {

		if (federationDataRequestQueueProcessor != null) {
			federationDataRequestQueueProcessor.setStillRunning(false);
		}
		if (federationDataRequestQueueProcessorThread != null) {
			federationDataRequestQueueProcessorThread.interrupt();
		}
	}

	/**
	 * 
	 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
	 * @version $Revision$
	 */
	private class FederationDataRequestQueueProcessor implements Runnable {

		private boolean stillRunning = true;

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {

			LOG.info("Federation data request queue processing initiated.");

			while (isStillRunning()) {

				try {

					IDataEventHandler eventHandler = dataRequestEntryQueue.take();

					IEvent event = null;

					try {

						eventHandler.handle();

						cntEventsProcessed++;

					} catch (Exception e) {

						// return error back to sender

						try {

							event = eventHandler.getEvent();

							if (event == null) {
								LOG.error("Critical error: Event could not be constructed "
										+ "from message received in FederationDataRequestQueueProcessor.");

							} else {

								LOG.debug("Error handling event type [" + event.getEventType()
										+ "]. Return error to sender");

								GeneralErrorResponse ger = new GeneralErrorResponse();

								ger.initialization(event.serialize());

								// set to failed
								ger.setSuccess(false);

								// set error description from exception
								ger.setErrorDescription(e.getLocalizedMessage());

								if (routerService != null) {
									routerService.queue(ger.serialize(), eventHandler
											.getDataChannelControlBlock().getReplyToQueueName(),
											eventHandler.getDataChannelControlBlock()
													.getCorrelationID());
								}

							}

						} catch (Throwable t) {

							try {
								LOG.error("Unrecoverable error encountered ["
										+ t.getLocalizedMessage()
										+ "] attempting to return exception to federate ["
										+ event.getSourceOfEvent() + "]; Message content ["
										+ event.serialize() + "]");

							} catch (Throwable t2) {
								LOG.error("Exception raised attempting to serialize event which caused exception."
										+ " Exception message = [" + t2.getLocalizedMessage() + "]");
							}

						}
					}

				} catch (InterruptedException e) {

					LOG.debug("Federation data request queue processor was interrupted.");

					setStillRunning(false);

					if (dataRequestEntryQueue.size() > 0) {
						LOG.info("["
								+ dataRequestEntryQueue.size()
								+ "] entries remaining on the federation data request queue when terminated.");
					}
				}
			}

			LOG.info("Federation data request queue is terminating. " + "Processed ["
					+ cntEventsProcessed + "] requests.");
		}

		/**
		 * @return the stillRunning
		 */
		public boolean isStillRunning() {
			return stillRunning;
		}

		/**
		 * @param stillRunning
		 *          the stillRunning to set
		 */
		public void setStillRunning(boolean stillRunning) {
			this.stillRunning = stillRunning;
		}

	}

	/**
	 * @return the cntEventsProcessed
	 */
	@Override
	public int getCntEventsProcessed() {
		return cntEventsProcessed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.federation.internal.execution.FederationDataRequestQueue
	 * #getRouterService()
	 */
	@Override
	public RouterService getRouterService() {
		return routerService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.federation.internal.execution.FederationDataRequestQueue
	 * #setRouterService(com.csc.muthur.server.router.RouterService)
	 */
	@Override
	public void setRouterService(RouterService routerService) {
		this.routerService = routerService;
	}

	/**
	 * @return the federationExecutionModel
	 */
	public FederationExecutionModel getFederationExecutionModel() {
		return federationExecutionModel;
	}

	/**
	 * @param federationExecutionModel
	 *          the federationExecutionModel to set
	 */
	public void setFederationExecutionModel(
			FederationExecutionModel federationExecutionModel) {
		this.federationExecutionModel = federationExecutionModel;
	}

}
