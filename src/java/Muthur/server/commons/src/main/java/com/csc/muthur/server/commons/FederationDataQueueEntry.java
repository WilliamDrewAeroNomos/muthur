/**
 * 
 */
package com.csc.muthur.server.commons;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public class FederationDataQueueEntry {

	private DataChannelControlBlock dataChannelControlBlock;
	private String payLoad;

	/**
	 * @param dataChannelControlBlock
	 * @param payLoad
	 */
	public FederationDataQueueEntry(
			DataChannelControlBlock dataChannelControlBlock, String payLoad) {
		super();
		this.setDataChannelControlBlock(dataChannelControlBlock);
		this.setPayLoad(payLoad);
	}

	/**
	 * @return the dataChannelControlBlock
	 */
	public DataChannelControlBlock getDataChannelControlBlock() {
		return dataChannelControlBlock;
	}

	/**
	 * @param dataChannelControlBlock
	 *          the dataChannelControlBlock to set
	 */
	public void setDataChannelControlBlock(
			DataChannelControlBlock dataChannelControlBlock) {
		this.dataChannelControlBlock = dataChannelControlBlock;
	}

	/**
	 * @return the payLoad
	 */
	public String getPayLoad() {
		return payLoad;
	}

	/**
	 * @param payLoad
	 *          the payLoad to set
	 */
	public void setPayLoad(String payLoad) {
		this.payLoad = payLoad;
	}

}
