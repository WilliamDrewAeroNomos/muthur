/**
 * 
 */
package com.csc.muthur.server.federation;

import java.util.Map;

import javax.jms.TemporaryQueue;

import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.federation.internal.execution.FederationDataPublicationQueue;
import com.csc.muthur.server.model.FederationExecutionEntry;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.FederationState;
import com.csc.muthur.server.model.JoinFederationExecutionEntry;
import com.csc.muthur.server.model.ReadyToRunFederationExecutionEntry;
import com.csc.muthur.server.model.SubscriptionRegistrationFederationExecutionEntry;
import com.csc.muthur.server.model.event.DataPublicationEvent;
import com.csc.muthur.server.model.event.data.IBaseDataObject;
import com.csc.muthur.server.model.event.request.RelinquishObjectOwnershipRequest;
import com.csc.muthur.server.object.FederationExecutionDataObjectContainer;
import com.csc.muthur.server.time.FederateClockManager;
import com.csc.muthur.server.time.TimeService;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public interface FederationExecutionController {

	/**
	 * @return the {@link FederationExecutionModel} for the
	 *         {@link FederationExecutionController}
	 */
	FederationExecutionModel getFederationExecutionModel();

	/**
	 * 
	 * @param joinFederationExecutionEntry
	 *          {@link JoinFederationExecutionEntry} for the
	 *          {@link FederationExecutionController}
	 * @throws MuthurException
	 */
	void joinFederation(
			final JoinFederationExecutionEntry joinFederationExecutionEntry)
			throws MuthurException;

	/**
	 * 
	 * @param fedExecSub
	 * @throws MuthurException
	 */
	void registerFederateSubscriptions(
			final SubscriptionRegistrationFederationExecutionEntry fedExecSub)
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
	 * @throws MuthurException
	 * 
	 */
	void startExecution() throws MuthurException;

	/**
	 * 
	 * @throws MuthurException
	 */
	public void pauseExecution() throws MuthurException;

	/**
	 * @throws MuthurException
	 * 
	 */
	void resumeExecution() throws MuthurException;

	/**
	 * 
	 * @throws MuthurException
	 */
	void terminate() throws MuthurException;

	/**
	 * 
	 * @return Whether or not the federation is accepting join events
	 */
	boolean isInJoinableState();

	/**
	 * @return Whether or not the federation is accepting ready to run events
	 */
	boolean IsInAcceptingReadyToRun();

	/**
	 * @return Whether or not the federation is accepting subscription events
	 */
	boolean isInAcceptingSubscriptionsState();

	/**
	 * 
	 * @param dataPublicationEvent
	 * @throws MuthurException
	 */
	void publishData(final DataPublicationEvent dataPublicationEvent)
			throws MuthurException;

	/**
	 * @return the federateNameToFederationExecutionParticipant
	 */
	Map<String, JoinFederationExecutionEntry>
			getFederateNameToFederationExecutionParticipant();

	/**
	 * @param federateNameToFederationExecutionParticipant
	 *          the federateNameToFederationExecutionParticipant to set
	 */
			void
			setFederateNameToFederationExecutionParticipant(
					Map<String, JoinFederationExecutionEntry> federateNameToFederationExecutionParticipant);

	/**
	 * 
	 * @param terminationReason
	 * @throws MuthurException
	 */
	void sendTerminateNotifications(final String terminationReason)
			throws MuthurException;

	/**
	 * Close all of the connections open to the federates in the federation and
	 * all connections opened by the federation controller
	 */
	void cleanUpConnections();

	/**
	 * @return the federationService
	 */
	FederationService getFederationService();

	/**
	 * @return the federationTTLThread
	 */
	FederationTimeToLiveThread getFederationTTLThread();

	/**
	 * @param fee
	 * @param response
	 * @param errMsg
	 * @throws MuthurException
	 */
	void returnErrorResponse(FederationExecutionEntry fee, IEvent response,
			String errMsg) throws MuthurException;

	/**
	 * 
	 * @return A reference to the {@link TimeService} service
	 */
	public TimeService getTimeService();

	/**
	 * 
	 * @param newState
	 */
	void setExecutionState(final FederationState newState);

	/**
	 * @return the current {@link FederationState} for the
	 *         {@link FederationExecutionController}
	 */
	FederationState getExecutionState();

	/**
	 * @return the federationExecutionHandle
	 */
	String getFederationExecutionHandle();

	/**
	 * 
	 * @param currentState
	 * @throws MuthurException
	 */
	void advanceBootProcess(final FederationState currentState)
			throws MuthurException;

	/**
	 * @return the federate clock manager for this federation execution controller
	 */
	FederateClockManager getFederateClockManager();

	/**
	 * @return the federationExecutionDataObjectContainer
	 */
	FederationExecutionDataObjectContainer
			getFederationExecutionDataObjectContainer();

	/**
	 * @param request
	 * @param bdo
	 * @throws MuthurException
	 */
	void publishOwnershipRelinquished(RelinquishObjectOwnershipRequest request,
			IBaseDataObject bdo) throws MuthurException;

	/**
	 * Get the {@link ReadyToRunRequestProcessor} instance for this federation
	 * execution.
	 * 
	 * @return
	 */
	ReadyToRunRequestProcessor getReadyToRunRequestProcessor();

	/**
	 * Set the current federation execution time to federationExecutionTimeMSecs.
	 * 
	 * @param federationExecutionTimeMSecs
	 */
	void updateFederationTime(long federationExecutionTimeMSecs);

	/**
	 * 
	 * @return
	 */
	SimulationExecutionMetricsAggregator getSimulationExecutionMetrics();

	/**
	 * 
	 * @param federateDataQueue
	 */
	void setFederateDataTopic(TemporaryQueue federateDataQueue);

	/**
	 * 
	 * @return
	 */
	TemporaryQueue getFederateDataQueue();

	/**
	 * 
	 * @param federateDataQueueName
	 */
	void setFederateDataQueueName(final String federateDataQueueName);

	/**
	 * 
	 * @return
	 */
	String getFederateDataTopicName();

	/**
	 * 
	 * @param federationDataRequestQueue
	 */
	void setFederationDataRequestQueue(
			FederationDataRequestQueue federationDataRequestQueue);

	/**
	 * 
	 * @return
	 */
	FederationDataRequestQueue getFederationDataRequestQueue();

	/**
	 * 
	 * @return
	 */
	FederationDataPublicationQueue getFederationDataPublicationQueue();

}