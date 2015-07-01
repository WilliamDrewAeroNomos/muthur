/**
 * 
 */
package com.csc.muthur.server.commons;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public final class MessageDestination {

	// event source name

	public static final String MUTHUR = "Muthur";

	// name of destinations
	//
	private final static String MUTHER_EVENT_QUEUE_NAME = "MUTHUR.EVENT.QUEUE";
	private final static String MUTHUR_SYSTEM_EVENT_TOPIC_NAME =
			"MUTHUR.SYSTEM.EVENT.TOPIC";
	private final static String MUTHUR_DISPATCH_QUEUE_NAME =
			"MUTHUR.DISPATCH.QUEUE";

	// names of message properties
	//
	private final static String DATA_EVENT_QUEUE_PROP_NAME =
			"DATA.EVENT.QUEUE.NAME";

	private final static String FEDERATION_EVENT_QUEUE_PROP_NAME =
			"FEDERATION.EVENT.QUEUE.NAME";

	/**
	 * Used by other services to send events to the federation service
	 */
	private final static String FEDERATION_SERVICE_QUEUE_NAME =
			"FEDERATION.SERVICE.QUEUE";

	/**
	 * Subscription service message queue
	 */
	private static final String SUBSCRIPTION_SERVICE_QUEUE_NAME =
			"SUBSCRIPTION.SERVICE.QUEUE";

	/**
	 * @return the mutherEventQueueName
	 */
	public final static String getMuthurEventQueueName() {
		return MUTHER_EVENT_QUEUE_NAME;
	}

	/**
	 * @return the muthurSystemEventTopicName
	 */
	public final static String getMuthurSystemEventTopicName() {
		return MUTHUR_SYSTEM_EVENT_TOPIC_NAME;
	}

	/**
	 * @return the dataEventQueuePropName
	 */
	public static final String getDataEventQueuePropName() {
		return DATA_EVENT_QUEUE_PROP_NAME;
	}

	/**
	 * @return the federationEventQueuePropName
	 */
	public static final String getFederationEventQueuePropName() {
		return FEDERATION_EVENT_QUEUE_PROP_NAME;
	}

	/**
	 * @return the MUTHUR_DISPATCH_QUEUE_NAME
	 */
	public static final String getMuthurDispatchQueueName() {
		return MUTHUR_DISPATCH_QUEUE_NAME;
	}

	/**
	 * 
	 * @return
	 */
	public static final String getFederationServiceQueueName() {
		return FEDERATION_SERVICE_QUEUE_NAME;
	}

	/**
	 * @return
	 */
	public static String getFederateStateQueueName() {
		return "MUTHUR.FEDERATE.STATE.QUEUE.NAME";
	}

	/**
	 * @return
	 */
	public static String getSubscriptionServiceQueueName() {
		return SUBSCRIPTION_SERVICE_QUEUE_NAME;
	}

	/**
	 * do not instantiate
	 */
	private MessageDestination() {

	}

}
