/**
 * 
 */
package com.csc.muthur.server.federation;

import java.util.Map;

import com.atcloud.fem.FederationExecutionModelService;
import com.atcloud.metrics.MetricsService;
import com.csc.muthur.server.commons.CommonsService;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.configuration.ConfigurationService;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.FederationExecutionTermination;
import com.csc.muthur.server.model.JoinFederationExecutionEntry;
import com.csc.muthur.server.model.ModelService;
import com.csc.muthur.server.model.ReadyToRunFederationExecutionEntry;
import com.csc.muthur.server.model.SubscriptionRegistrationFederationExecutionEntry;
import com.csc.muthur.server.object.ObjectService;
import com.csc.muthur.server.router.RouterService;
import com.csc.muthur.server.time.TimeService;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public interface FederationService {

	/**
	 * 
	 * @throws Exception
	 */
	void start() throws Exception;

	/**
	 * 
	 * @throws Exception
	 */
	void stop() throws Exception;

	/**
	 * 
	 * @param federationExecutionModel
	 * @param joinFederationExecutionEntry
	 * @throws MuthurException
	 */
	void joinFederationExecution(
			final FederationExecutionModel federationExecutionModel,
			final JoinFederationExecutionEntry joinFederationExecutionEntry)
			throws MuthurException;

	/**
	 * 
	 * @param federationExecutionModel
	 * @return FederationExecutionController
	 */
	FederationExecutionController getFederationExecutionController(
			final FederationExecutionModel federationExecutionModel);

	/**
	 * 
	 * @param fedExecHandle
	 */
	void removeFederationExecution(final String fedExecHandle);

	/**
	 * 
	 * @param federationExecutionSubscription
	 * @throws MuthurException
	 */
			void
			registerSubscriptions(
					final SubscriptionRegistrationFederationExecutionEntry federationExecutionSubscription)
					throws MuthurException;

	/**
	 * 
	 * @param readyToRunFederationExecutionEntry
	 * @throws MuthurException
	 */
			void
			registerReadyToRun(
					final ReadyToRunFederationExecutionEntry readyToRunFederationExecutionEntry)
					throws MuthurException;

	/**
	 * 
	 * @param federationExecutionTermination
	 * @throws MuthurException
	 */
	void terminateFederationExecution(
			final FederationExecutionTermination federationExecutionTermination)
			throws MuthurException;

	/**
	 * 
	 */
	void removeFederationControllerInstance(
			final FederationExecutionModel federationExecutionModel);

	/**
	 * @return the configurationServer
	 */
	ConfigurationService getConfigurationService();

	/**
	 * @param configurationServer
	 *          the configurationServer to set
	 */
	void setConfigurationService(ConfigurationService configurationService);

	/**
	 * @return the routerService
	 */
	RouterService getRouterService();

	/**
	 * @param routerService
	 *          the routerService to set
	 */
	void setRouterService(RouterService routerService);

	/**
	 * @return the fedExecModelToFedExecCtrlrMap
	 */
	Map<FederationExecutionModel, FederationExecutionController>
			getFedExecModelToFedExecCtrlrMap();

	/**
	 * @param fedExecModelToFedExecCtrlrMap
	 *          the fedExecModelToFedExecCtrlrMap to set
	 */
			void
			setFedExecModelToFedExecCtrlrMap(
					Map<FederationExecutionModel, FederationExecutionController> fedExecModelToFedExecCtrlrMap);

	abstract void setModelService(ModelService modelService);

	abstract ModelService getModelService();

	/**
	 * @param timeService
	 *          the timeService to set
	 */
	void setTimeService(TimeService timeService);

	/**
	 * @return the timeService
	 */
	TimeService getTimeService();

	void pauseFederationExecution(
			FederationExecutionModel federationExecutionModel) throws MuthurException;

	/**
	 * 
	 * @param federationExecutionModel
	 * @throws MuthurException
	 */
	void resumeFederationExecution(
			FederationExecutionModel federationExecutionModel) throws MuthurException;

	void startFederationExecution(
			FederationExecutionModel federationExecutionModel) throws MuthurException;

	/**
	 * @param objectService
	 *          the objectService to set
	 */
	void setObjectService(ObjectService objectService);

	/**
	 * @return the objectService
	 */
	ObjectService getObjectService();

	/**
	 * @param commonsService
	 *          the commonsService to set
	 */
	void setCommonsService(CommonsService commonsService);

	/**
	 * @return the commonsService
	 */
	CommonsService getCommonsService();

	/*
	 * 
	 * return true if the federation execution handle is valid and current or
	 * false if the handle can not be found in the list of federation executions.
	 */
	boolean isValidFederationExecutionHandle(
			final String federationExecutionHandle);

	/**
	 * Updates a federation's current execution time setting it to the
	 * federationExecutionTimeMSecs
	 * 
	 * @param model
	 * @param federationExecutionTimeMSecs
	 * @throws MuthurException
	 */
	void updateFederationExecutionTime(FederationExecutionModel model,
			long federationExecutionTimeMSecs) throws MuthurException;

	/**
	 * 
	 * @param metricsService
	 */
	void setMetricsService(MetricsService metricsService);

	/**
	 * 
	 * @return
	 */
	MetricsService getMetricsService();

	/**
	 * 
	 * @param federationExecutionModelService
	 */
	void setFederationExecutionModelService(
			FederationExecutionModelService federationExecutionModelService);

	/**
	 * 
	 * @return
	 */
	FederationExecutionModelService getFederationExecutionModelService();

	/**
	 * 
	 * @param portNumber
	 */
	void setDataChannelServerPort(int portNumber);

	/**
	 * 
	 * @return
	 */
	int getDataChannelServerPort();

	/**
	 * @return
	 */
	String getDataChannelServerHostName();

	/**
	 * 
	 * @param dataChannelServerHostName
	 */
	void setDataChannelServerHostName(String dataChannelServerHostName);

	/**
	 * 
	 * @param federationDataChannelServer
	 */
	void setFederationDataChannelServer(
			FederationDataChannelServer federationDataChannelServer);

	/**
	 * 
	 * @return
	 */
	FederationDataChannelServer getFederationDataChannelServer();
}