/**
 * 
 */
package com.csc.muthur.server.configuration;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public interface ConfigurationService {

	/**
	 * 
	 */
	void start();

	/**
	 * 
	 */
	void stop();

	/**
	 * 
	 * @return Name of the machine that is hosting the messaging service.
	 */
	String getMessagingHost();

	/**
	 * 
	 * @return Port upon which the messaging service is listening for connections.
	 */
	int getMessagingPort();

	/**
	 * 
	 * @return Transport which will be used in the connection to the messaging
	 *         service.
	 */
	String getMessagingTransport();

	/**
	 * 
	 * @return Complete connection URL including the host, port and transport
	 *         values.
	 */
	String getMessagingConnectionUrl();

	/**
	 * 
	 * @return True if heartbeat should be generated for federates or false if no
	 *         heartbeat. The default is true.
	 */
	boolean isGenerateHeartBeat();

	/**
	 * @param messagingHost
	 *          the messagingHost to set
	 */
	void setMessagingHost(String messagingHost);

	/**
	 * @param messagingPort
	 *          the messagingPort to set
	 */
	void setMessagingPort(int messagingPort);

	/**
	 * @param messagingTransport
	 *          the messagingTransport to set
	 */
	void setMessagingTransport(String messagingTransport);

	/**
	 * @param generateHeartBeat
	 *          True will initiate heartbeat support for each registered federate
	 *          and false will turn off federate heartbeats. The default is true.
	 */
	void setGenerateHeartBeat(Boolean generateHeartBeat);

	/**
	 * 
	 * @return
	 */
	Integer getMaxNumOfConsecutiveFailedHeartbeatAttemptsAllowed();

	/**
	 * 
	 * @return
	 */
	Integer getIntervalBetweenHeartBeatsSecs();

	/**
	 * @return
	 */
	int getDataChannelServerPort();

	/**
	 * 
	 * @return
	 */
	String getDataChannelServerHostName();
}