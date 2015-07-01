/**
 * 
 */
package com.csc.muthur.server.commons;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public final class DataChannelControlBlock {

	private String version;
	private String eventName;
	private String correlationID;
	private String replyToQueueName;
	private int payLoadLength;

	/**
	 * 
	 */
	public DataChannelControlBlock() {

	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version
	 *          the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * @return the eventName
	 */
	public String getEventName() {
		return eventName;
	}

	/**
	 * @param eventName
	 *          the eventName to set
	 */
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	/**
	 * @return the correlationID
	 */
	public String getCorrelationID() {
		return correlationID;
	}

	/**
	 * @param correlationID
	 *          the correlationID to set
	 */
	public void setCorrelationID(String correlationID) {
		this.correlationID = correlationID;
	}

	/**
	 * @return the replyToQueueName
	 */
	public String getReplyToQueueName() {
		return replyToQueueName;
	}

	/**
	 * @param replyToQueueName
	 *          the replyToQueueName to set
	 */
	public void setReplyToQueueName(String replyToQueueName) {
		this.replyToQueueName = replyToQueueName;
	}

	/**
	 * @return the payLoadLength
	 */
	public int getPayLoadLength() {
		return payLoadLength;
	}

	/**
	 * @param payLoadLength
	 *          the payLoadLength to set
	 */
	public void setPayLoadLength(int payLoadLength) {
		this.payLoadLength = payLoadLength;
	}

}
