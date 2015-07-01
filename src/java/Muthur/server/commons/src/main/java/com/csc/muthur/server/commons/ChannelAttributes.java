/**
 * 
 */
package com.csc.muthur.server.commons;

/**
 * 
 * Contains the common attributes and their values used between classes using
 * channels
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public final class ChannelAttributes {

	/**
	 * Port the gateway is listening on for UDP messages.
	 */
	public static final int GATEWAY_CHANNEL_BROKER_PORT = 9876;

	/**
	 * 
	 */
	public static final int CHANNEL_REQUEST_HANDLER_WAIT_TIME_SECS = 5;

	/**
	 * Time to wait for channel information from the gateway before timing out
	 */
	public static final int CHANNEL_REQUEST_TIMEOUT = 5000;

	/*
	 * Sizes of the individual fields in the control block Note: this is also
	 * their order in the block
	 */

	public static final int VERSION_SIZE = 16;
	public static final int EVENT_NAME_SIZE = 32;
	public static final int CORRELATION_ID_SIZE = 64;
	public static final int REPLY_TO_QUEUE_NAME_SIZE = 64;
	public static final int PAYLOAD_LENGTH_SIZE = 8;

	public static int controlBlockSize() {
		return VERSION_SIZE + CORRELATION_ID_SIZE + EVENT_NAME_SIZE
				+ PAYLOAD_LENGTH_SIZE + REPLY_TO_QUEUE_NAME_SIZE;
	}

	public static int getVersionOffset() {
		return 0;
	}

	public static int getEventNameOffset() {
		return VERSION_SIZE;
	}

	public static int getCorrelationIdOffset() {
		return VERSION_SIZE + EVENT_NAME_SIZE;
	}

	public static int getReplyToQueueNameOffset() {
		return VERSION_SIZE + EVENT_NAME_SIZE + CORRELATION_ID_SIZE;
	}

	public static int getPayloadLengthOffset() {
		return VERSION_SIZE + EVENT_NAME_SIZE + CORRELATION_ID_SIZE
				+ REPLY_TO_QUEUE_NAME_SIZE;
	}

	/**
	 * Private constructor - DO NOT instantiate
	 */
	private ChannelAttributes() {
	}

}
