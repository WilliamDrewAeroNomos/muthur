/**
 * 
 */
package com.csc.muthur.server.federation.internal.execution;

import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atcloud.model.Simulation;
import com.atcloud.model.SimulationMetrics;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.federation.SimulationExecutionMetricsAggregator;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.event.DataPublicationEvent;
import com.csc.muthur.server.model.event.data.DataTypeEnum;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public class SimulationExecutionMetricsAggregatorImpl implements
		SimulationExecutionMetricsAggregator {

	public static final Logger LOG = LoggerFactory
			.getLogger(SimulationExecutionMetricsAggregatorImpl.class.getName());

	private Simulation simulation;
	private SimulationMetrics simulationMetrics;

	/**
	 * 
	 */
	private BlockingQueue<ProcessDataPublicationEventEntry> dataPublicationQueue =
			new LinkedBlockingQueue<ProcessDataPublicationEventEntry>();

	private DataEntryQueueProcessor dataEntryQueueProcessor;

	private Thread queueProcessorThread;

	/**
	 * 
	 * 
	 * @author <a href=mailto:support@atcloud.com>support</a>
	 * @version $Revision: $
	 */
	private enum ProcessDataPublicationEventType {
		ROUTED_DATA_ENTRY,
		DATA_ACTION_ENTRY
	}

	/**
	 * @throws MuthurException
	 * 
	 */
	public SimulationExecutionMetricsAggregatorImpl(
			final FederationExecutionModel fem) throws MuthurException {

		if (fem == null) {
			throw new MuthurException("Federation execution model was null");
		}

		Calendar c = Calendar.getInstance();

		simulation = new Simulation();

		simulation.setTimestamp(c);
		simulation.setSimulationID(UUID.randomUUID().toString());

		simulationMetrics = new SimulationMetrics();

		simulationMetrics.setTimestamp(c);
		simulationMetrics.setSimulationMetricsID(UUID.randomUUID().toString());
	}

	/**
	 * 
	 * 
	 * @author <a href=mailto:support@atcloud.com>support</a>
	 * @version $Revision: $
	 */
	private class ProcessDataPublicationEventEntry {

		private ProcessDataPublicationEventType type;
		private DataPublicationEvent event;

		/**
		 * 
		 * @param type
		 * @param dataPublicationEvent
		 */
		private ProcessDataPublicationEventEntry(
				ProcessDataPublicationEventType type,
				DataPublicationEvent dataPublicationEvent) {
			this.type = type;
			this.event = dataPublicationEvent;
		}

		/**
		 * @return the event
		 */
		public DataPublicationEvent getEvent() {
			return event;
		}

		/**
		 * @return the type
		 */
		public ProcessDataPublicationEventType getType() {
			return type;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.federation.SimulationExecutionMetricsAggregator#start
	 * ()
	 */
	@Override
	public void start() {

		dataEntryQueueProcessor = new DataEntryQueueProcessor();

		if (dataEntryQueueProcessor != null) {
			queueProcessorThread = new Thread(dataEntryQueueProcessor);
			if (queueProcessorThread != null) {
				queueProcessorThread.start();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.federation.SimulationExecutionMetricsAggregator#stop
	 * ()
	 */
	@Override
	public void stop() {

		if (dataEntryQueueProcessor != null) {

			dataEntryQueueProcessor.setContinueProcessing(false);

			if (queueProcessorThread != null) {
				queueProcessorThread.interrupt();
				queueProcessorThread = null;
			}

			dataEntryQueueProcessor = null;
		}
	}

	/**
	 * 
	 * 
	 * @author <a href=mailto:support@atcloud.com>support</a>
	 * @version $Revision: $
	 */
	private class DataEntryQueueProcessor implements Runnable {

		private boolean continueProcessing = true;

		public DataEntryQueueProcessor() {
			LOG.debug("DataEntryQueueProcessor ctor");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		@Override
		public void run() {

			while (isContinueProcessing()) {

				try {
					ProcessDataPublicationEventEntry pdpeEntry =
							dataPublicationQueue.take();

					if (pdpeEntry != null) {
						DataPublicationEvent event = pdpeEntry.getEvent();

						if (event != null) {

							switch (pdpeEntry.getType()) {

							case DATA_ACTION_ENTRY:
								processDataAction(event);
								break;

							case ROUTED_DATA_ENTRY:
								processRoutedObject(event);
								break;

							default:
								break;
							}
						}
					}

				} catch (InterruptedException e) {
					setContinueProcessing(false);
					LOG.info("DataEntryQueueProcessor was interrupted and is exiting");
				}
			}

			LOG.info("DataEntryQueueProcessor is exiting");
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.federation.SimulationExecutionMetricsAggregator#
	 * addRoutedDataEvent(com.csc.muthur.server.model.event.DataPublicationEvent)
	 */
	public void
			addRoutedDataEvent(final DataPublicationEvent dataPublicationEvent) {

		if (dataPublicationEvent != null) {

			ProcessDataPublicationEventEntry pdpeEntry =
					new ProcessDataPublicationEventEntry(
							ProcessDataPublicationEventType.ROUTED_DATA_ENTRY,
							dataPublicationEvent);

			dataPublicationQueue.add(pdpeEntry);

		}

	}

	/**
	 * 
	 * @param dataPublicationEvent
	 */
	private void processRoutedObject(
			final DataPublicationEvent dataPublicationEvent) {

		if (dataPublicationEvent != null) {

			simulationMetrics.setNumberOfObjectsRouted(simulationMetrics
					.getNumberOfObjectsRouted() + 1);

			String s = dataPublicationEvent.serialize();

			if (s != null) {
				simulationMetrics.setNumberOfBytesRouted(simulationMetrics
						.getNumberOfBytesRouted() + s.length());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.federation.SimulationExecutionMetricsAggregator#
	 * addDataEventObject(com.csc.muthur.server.model.event.DataPublicationEvent)
	 */
	@Override
	public void addDataEvent(final DataPublicationEvent dataPublicationEvent) {

		if (dataPublicationEvent != null) {

			ProcessDataPublicationEventEntry pdpeEntry =
					new ProcessDataPublicationEventEntry(
							ProcessDataPublicationEventType.DATA_ACTION_ENTRY,
							dataPublicationEvent);

			dataPublicationQueue.add(pdpeEntry);

		}

	}

	/**
	 * 
	 * @param dataPublicationEvent
	 */
	private void processDataAction(DataPublicationEvent dataPublicationEvent) {

		if (dataPublicationEvent != null) {

			switch (dataPublicationEvent.getDataAction()) {

			case Add:

				incrObjectCreated(dataPublicationEvent);

				break;

			case Delete:
				break;

			case Relinquished:
				break;

			case Transferred:
				break;

			case Update:

				incrObjectsUpdated(dataPublicationEvent);

				break;

			default:
				break;
			}
		}

	}

	/**
	 * @param dataPublicationEvent
	 */
	private void incrObjectsUpdated(
			final DataPublicationEvent dataPublicationEvent) {

		if (dataPublicationEvent != null) {

			switch (dataPublicationEvent.getDataType()) {

			case Aircraft:
				simulationMetrics.setCntAircraftUpdates(simulationMetrics
						.getCntAircraftUpdates() + 1);
				simulationMetrics.setTotalObjectUpdates(simulationMetrics
						.getTotalObjectUpdates() + 1);
				break;

			case AircraftArrival:
				simulationMetrics.setCntAircraftArrivalUpdates(simulationMetrics
						.getCntAircraftArrivalUpdates() + 1);
				simulationMetrics.setTotalObjectUpdates(simulationMetrics
						.getTotalObjectUpdates() + 1);
				break;

			case AircraftDeparture:
				simulationMetrics.setCntAircraftDepartureUpdates(simulationMetrics
						.getCntAircraftDepartureUpdates() + 1);
				simulationMetrics.setTotalObjectUpdates(simulationMetrics
						.getTotalObjectUpdates() + 1);
				break;

			case AircraftTaxiIn:
				simulationMetrics.setCntAircraftTaxiInUpdates(simulationMetrics
						.getCntAircraftTaxiInUpdates() + 1);
				simulationMetrics.setTotalObjectUpdates(simulationMetrics
						.getTotalObjectUpdates() + 1);
				break;

			case AircraftTaxiOut:
				simulationMetrics.setCntAircraftTaxiOutUpdates(simulationMetrics
						.getCntAircraftTaxiOutUpdates() + 1);
				simulationMetrics.setTotalObjectUpdates(simulationMetrics
						.getTotalObjectUpdates() + 1);
				break;

			case AirportConfiguration:
				simulationMetrics.setCntAirportConfigurationUpdates(simulationMetrics
						.getCntAirportConfigurationUpdates() + 1);
				simulationMetrics.setTotalObjectUpdates(simulationMetrics
						.getTotalObjectUpdates() + 1);
				break;

			case FlightPlan:
				simulationMetrics.setCntFlightPlanUpdates(simulationMetrics
						.getCntFlightPlanUpdates() + 1);
				simulationMetrics.setTotalObjectUpdates(simulationMetrics
						.getTotalObjectUpdates() + 1);
				break;

			case FlightPosition:
				simulationMetrics.setCntFlightPositionUpdates(simulationMetrics
						.getCntFlightPositionUpdates() + 1);
				simulationMetrics.setTotalObjectUpdates(simulationMetrics
						.getTotalObjectUpdates() + 1);
				break;

			case Vehicle:
				simulationMetrics.setCntVehicleUpdates(simulationMetrics
						.getCntVehicleUpdates() + 1);
				simulationMetrics.setTotalObjectUpdates(simulationMetrics
						.getTotalObjectUpdates() + 1);
				break;

			case Weather:
				simulationMetrics.setCntWeatherUpdates(simulationMetrics
						.getCntWeatherUpdates() + 1);
				simulationMetrics.setTotalObjectUpdates(simulationMetrics
						.getTotalObjectUpdates() + 1);
				break;

			default:
				LOG.warn("Encountered unknown object.");
				break;

			}

		}

	}

	/**
	 * 
	 * @param dp
	 */
	private void incrObjectCreated(final DataPublicationEvent dp) {

		if (dp != null) {

			DataTypeEnum dte = dp.getDataType();

			switch (dte) {
			case Aircraft:
				simulationMetrics
						.setCntAircraft(simulationMetrics.getCntAircraft() + 1);
				simulationMetrics.setTotalObjectsCreated(simulationMetrics
						.getTotalObjectsCreated() + 1);
				break;

			case AircraftArrival:
				simulationMetrics.setCntAircraftArrival(simulationMetrics
						.getCntAircraftArrival() + 1);
				simulationMetrics.setTotalObjectsCreated(simulationMetrics
						.getTotalObjectsCreated() + 1);

				break;

			case AircraftDeparture:
				simulationMetrics.setCntAircraftDeparture(simulationMetrics
						.getCntAircraftDeparture() + 1);
				simulationMetrics.setTotalObjectsCreated(simulationMetrics
						.getTotalObjectsCreated() + 1);

				break;

			case AircraftTaxiIn:
				simulationMetrics.setCntAircraftTaxiIn(simulationMetrics
						.getCntAircraftTaxiIn() + 1);
				simulationMetrics.setTotalObjectsCreated(simulationMetrics
						.getTotalObjectsCreated() + 1);

				break;

			case AircraftTaxiOut:
				simulationMetrics.setCntAircraftTaxiOut(simulationMetrics
						.getCntAircraftTaxiOut() + 1);
				simulationMetrics.setTotalObjectsCreated(simulationMetrics
						.getTotalObjectsCreated() + 1);

				break;

			case AirportConfiguration:
				simulationMetrics.setCntAirportConfiguration(simulationMetrics
						.getCntAirportConfiguration() + 1);
				simulationMetrics.setTotalObjectsCreated(simulationMetrics
						.getTotalObjectsCreated() + 1);

				break;

			case FlightPlan:
				simulationMetrics
						.setCntFlightPlan(simulationMetrics.getCntFlightPlan() + 1);
				simulationMetrics.setTotalObjectsCreated(simulationMetrics
						.getTotalObjectsCreated() + 1);

				break;

			case FlightPosition:
				simulationMetrics.setCntFlightPosition(simulationMetrics
						.getCntFlightPosition() + 1);
				simulationMetrics.setTotalObjectsCreated(simulationMetrics
						.getTotalObjectsCreated() + 1);

				break;

			case Vehicle:
				simulationMetrics.setCntVehicle(simulationMetrics.getCntVehicle() + 1);
				simulationMetrics.setTotalObjectsCreated(simulationMetrics
						.getTotalObjectsCreated() + 1);

				break;

			case Weather:
				simulationMetrics.setCntWeather(simulationMetrics.getCntWeather() + 1);
				simulationMetrics.setTotalObjectsCreated(simulationMetrics
						.getTotalObjectsCreated() + 1);

				break;

			default:
				LOG.warn("Encountered unknown object.");
				break;

			}

		}
	}

	/**
	 * @return the simulationMetrics
	 */
	@Override
	public SimulationMetrics getSimulationMetrics() {
		return simulationMetrics;
	}

	/**
	 * @return the simulation
	 */
	@Override
	public Simulation getSimulation() {
		return simulation;
	}

}
