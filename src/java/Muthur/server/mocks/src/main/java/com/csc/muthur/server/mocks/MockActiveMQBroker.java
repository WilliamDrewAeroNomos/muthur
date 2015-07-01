package com.csc.muthur.server.mocks;

import org.apache.activemq.broker.BrokerService;

import com.csc.muthur.server.configuration.ConfigurationService;

public class MockActiveMQBroker {

	private BrokerService broker = null;
	private ConfigurationService configurationService;

	/**
	 * 
	 */
	public MockActiveMQBroker() {
		super();
	}

	/**
	 * @throws Exception
	 * 
	 */
	public void start() throws Exception {

		if (configurationService == null) {
			configurationService = new MockConfigurationServerImpl();
		}

		broker = new BrokerService();
		broker.setPersistent(false);
		broker.setUseJmx(false);

		broker.addConnector(configurationService.getMessagingConnectionUrl());

		broker.start();
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void stop() throws Exception {

		if (broker != null) {
			broker.stop();
			broker = null;
		}
	}

	/**
	 * @return the configurationService
	 */
	public final ConfigurationService getConfigurationService() {
		return configurationService;
	}

	/**
	 * @param configurationService
	 *          the configurationService to set
	 */
	public final void setConfigurationService(
			ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

}
