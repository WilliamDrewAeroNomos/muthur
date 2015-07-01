/**
 * 
 */
package com.csc.muthur.server.federation;

import com.atcloud.model.Simulation;
import com.atcloud.model.SimulationMetrics;
import com.csc.muthur.server.model.event.DataPublicationEvent;

/**
 * 
 * @author <a href=mailto:support@atcloud.com>support</a>
 * @version $Revision: $
 */
public interface SimulationExecutionMetricsAggregator {

	/**
	 * 
	 */
	void stop();

	/**
	 * 
	 */
	void start();

	/**
	 * 
	 * @return
	 */
	SimulationMetrics getSimulationMetrics();

	/**
	 * 
	 * @param dataPublicationEvent
	 */
	void addDataEvent(final DataPublicationEvent dataPublicationEvent);

	/**
	 * 
	 * @param dataPublicationEvent
	 */
	void addRoutedDataEvent(final DataPublicationEvent dataPublicationEvent);

	/**
	 * 
	 * @return
	 */
	Simulation getSimulation();

}