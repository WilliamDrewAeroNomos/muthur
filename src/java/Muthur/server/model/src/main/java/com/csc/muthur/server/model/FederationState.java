/**
 * 
 */
package com.csc.muthur.server.model;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public enum FederationState {
	INSTANTIATING,
	INSTANTIATED,
	ACCEPTING_JOINS,
	JOINED,
	ACCEPTING_SUBSCRIPTIONS,
	SUBSCRIBED,
	ACCEPTING_READY_TO_RUN,
	READY_TO_RUN,
	RUNNING,
	PAUSING,
	PAUSED,
	RESUMING,
	END_CONDITION,
	TERMINATION,
	TERMINATING,
	TERMINATED,
	AWAITING_CLEANUP,
	FATAL_ERROR,
	STARTED,
	STARTING,
	UNDEFINED
}
