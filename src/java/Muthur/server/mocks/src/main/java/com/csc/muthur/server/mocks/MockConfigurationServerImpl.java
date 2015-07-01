/**
 * 
 */
package com.csc.muthur.server.mocks;

import com.csc.muthur.server.configuration.ConfigurationService;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class MockConfigurationServerImpl implements ConfigurationService {

	private Integer dataChannelServerPort = 54545;
	private String dataChannelServerHostName = "localhost";
	private String messagingHost = "localhost";
	private int messagingPort = 61616;
	private String messagingTransport = "tcp";
	private boolean generateHeartBeat = true;
	private int maxNumOfConsecutiveFailedHeartbeatAttemptsAllowed = 1;
	private int intervalBetweenHeartBeatsSecs = 5;

	/**
	 * @param messagingHost
	 *          the messagingHost to set
	 */
	public final void setMessagingHost(String messagingHost) {
		this.messagingHost = messagingHost;
	}

	/**
	 * @param messagingPort
	 *          the messagingPort to set
	 */
	public final void setMessagingPort(int messagingPort) {
		this.messagingPort = messagingPort;
	}

	/**
	 * @param messagingTransport
	 *          the messagingTransport to set
	 */
	public final void setMessagingTransport(String messagingTransport) {
		this.messagingTransport = messagingTransport;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.configuration.ConfigurationService#
	 * getMessagingConnectionUrl ()
	 */
	@Override
	public final String getMessagingConnectionUrl() {
		return getMessagingTransport() + "://" + getMessagingHost() + ":"
				+ Integer.toString(getMessagingPort());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.configuration.ConfigurationService#getMessagingHost()
	 */
	@Override
	public final String getMessagingHost() {

		return messagingHost;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.configuration.ConfigurationService#getMessagingPort()
	 */
	@Override
	public final int getMessagingPort() {

		return messagingPort;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.configuration.ConfigurationService#getMessagingTransport
	 * ()
	 */
	@Override
	public final String getMessagingTransport() {

		return messagingTransport;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.configuration.ConfigurationService#start()
	 */
	@Override
	public void start() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.configuration.ConfigurationService#stop()
	 */
	@Override
	public void stop() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.configuration.ConfigurationService#isGenerateHeartBeat
	 * ()
	 */
	@Override
	public final boolean isGenerateHeartBeat() {
		return generateHeartBeat;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.configuration.ConfigurationService#setGenerateHeartBeat
	 * ( java.lang.Boolean)
	 */
	@Override
	public final void setGenerateHeartBeat(Boolean generateHeartBeat) {
		this.generateHeartBeat = generateHeartBeat;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.configuration.ConfigurationService#
	 * getMaxNumOfConsecutiveFailedHeartbeatAttemptsAllowed()
	 */
	public Integer getMaxNumOfConsecutiveFailedHeartbeatAttemptsAllowed() {
		return maxNumOfConsecutiveFailedHeartbeatAttemptsAllowed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.configuration.ConfigurationService#
	 * getIntervalBetweenHeartBeatsSecs()
	 */
	public Integer getIntervalBetweenHeartBeatsSecs() {
		return intervalBetweenHeartBeatsSecs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.configuration.ConfigurationService#
	 * getDataChannelServerPort()
	 */
	@Override
	public int getDataChannelServerPort() {
		return dataChannelServerPort;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.configuration.ConfigurationService#
	 * getDataChannelServerHostName()
	 */
	@Override
	public String getDataChannelServerHostName() {
		return dataChannelServerHostName;
	}

}
