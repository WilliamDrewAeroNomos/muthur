/**
 * 
 */
package com.csc.muthur.server.federation.internal.execution;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.federation.FederationExecutionController;
import com.csc.muthur.server.model.FederationExecutionModel;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public final class FederationExecutionControllerMap {

	/**
	 * Maps a {@link FederationExecutionModel} to an active
	 * {@link FederationExecutionController}
	 */
	private Map<FederationExecutionModel, FederationExecutionController> fedExecModelToFedExecCtrlrMap =
			new ConcurrentHashMap<FederationExecutionModel, FederationExecutionController>();

	/**
	 * 
	 */
	public FederationExecutionControllerMap() {
	}

	/**
	 * 
	 * @param federationExecutionModel
	 * @return
	 */
	public FederationExecutionController getController(
			final FederationExecutionModel federationExecutionModel) {
		return fedExecModelToFedExecCtrlrMap.get(federationExecutionModel);
	}

	/**
	 * Adds a federation execution controller to the internal mapping it to the
	 * federation execution model.
	 * 
	 * @param federationExecutionController
	 * @return
	 */
	public void addController(
			final FederationExecutionController federationExecutionController)
			throws MuthurException {

		// validate parameters

		if (federationExecutionController == null) {
			throw new MuthurException("Error adding controller to internal map."
					+ " Federation execution controller parameter was null");
		}

		if (federationExecutionController.getFederationExecutionModel() == null) {
			throw new MuthurException("Error adding controller to internal map."
					+ " Federation execution model parameter was null");
		}

		// add the federation execution controller to the internal map

		fedExecModelToFedExecCtrlrMap.put(federationExecutionController
				.getFederationExecutionModel(), federationExecutionController);

	}

	/**
	 * Removes the {@link FederationExecutionController} using it's
	 * {@link FederationExecutionModel} as the key from the internal map.
	 * 
	 * @param federationExecutionController
	 */
	public void removeController(
			final FederationExecutionController federationExecutionController) {

		/*
		 * Remove the federation execution controller from the map using the
		 * federation execution model contained in the FederationExecutionController
		 */
		fedExecModelToFedExecCtrlrMap.remove(federationExecutionController
				.getFederationExecutionModel());
	}

	/**
	 * 
	 */
	public void clear() {
		fedExecModelToFedExecCtrlrMap.clear();
	}

	/**
	 * @return the getControllers
	 */
	public Collection<FederationExecutionController> getControllers() {
		return fedExecModelToFedExecCtrlrMap.values();
	}
}
