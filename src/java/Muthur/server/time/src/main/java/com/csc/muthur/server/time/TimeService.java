/**
 * 
 */
package com.csc.muthur.server.time;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.configuration.ConfigurationService;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.ModelService;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public interface TimeService {

	final Map<FederationExecutionModel, FederateClockManager> FEM_TO_FEDERATE_CLOCKMANAGER_MAP = new ConcurrentHashMap<FederationExecutionModel, FederateClockManager>();

	/**
	 * @throws MuthurException
	 * 
	 */
	void start() throws MuthurException;

	/**
	 * 
	 */
	void stop();

	/**
	 * 
	 * @param configurationService
	 */
	void setConfigurationService(ConfigurationService configurationService);

	/**
	 * 
	 * @return
	 */
	ConfigurationService getConfigurationService();

	/**
	 * 
	 * @return
	 */
	ModelService getModelService();

	/**
	 * 
	 * @param modelService
	 */
	void setModelService(ModelService modelService);

	/**
	 * 
	 * @param federationExecutionModel
	 */
	public void destroyFederateClockManager(
			final FederationExecutionModel federationExecutionModel);

	public abstract void setMaxNumOfConsecutiveFailedAttemptsAllowed(
			Integer maxNumOfConsecutiveFailedAttemptsAllowed);

	public abstract Integer getMaxNumOfConsecutiveFailedAttemptsAllowed();

	public abstract void setIntervalBetweenTimeUpdatesMSecs(
			Integer intervalBetweenTimeUpdatesMSecs);

	public abstract Integer getIntervalBetweenTimeUpdatesMSecs();

	public abstract void setSizeOfTimeIncrementMSecs(
			Integer sizeOfTimeIncrementMSecs);

	public abstract Integer getSizeOfTimeIncrementMSecs();

	/**
	 * @param federationExecutionModel
	 * @return
	 * @throws MuthurException
	 */
	FederateClockManager createFederateClockManager(
			FederationExecutionModel federationExecutionModel) throws MuthurException;

}