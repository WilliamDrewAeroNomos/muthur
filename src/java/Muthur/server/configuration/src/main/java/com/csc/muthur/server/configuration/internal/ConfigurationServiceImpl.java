/**
 * 
 */
package com.csc.muthur.server.configuration.internal;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.osgi.context.BundleContextAware;

import com.csc.muthur.server.commons.MessagingConfigurator;
import com.csc.muthur.server.configuration.ConfigurationService;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class ConfigurationServiceImpl implements ConfigurationService,
		BundleContextAware {

	private static final Logger LOG = LoggerFactory
			.getLogger(ConfigurationServiceImpl.class.getName());

	private BundleContext bundleContext;
	private ServiceRegistration ppcService;

	private String serviceMessagingPID = "com.csc.muthur.messaging";

	private MessagingConfigurator mc;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.configuration.ConfigurationService#start()
	 */
	public void start() {

		LOG.debug("Starting configuration service...");

		if (bundleContext != null) {

			// register managed service for messaging

			Dictionary<String, String> props = new Hashtable<String, String>();

			props.put("service.pid", serviceMessagingPID);

			mc = new MessagingConfigurator();

			ppcService =
					bundleContext.registerService(ManagedService.class.getName(), mc,
							props);
		} else {
			LOG.warn("Bundle context was null.");
		}

		LOG.info("Configuration service started.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.configuration.ConfigurationService#stop()
	 */
	public void stop() {

		LOG.debug("Stopping configuration service...");

		if (ppcService != null) {
			ppcService.unregister();
			ppcService = null;
		}

		LOG.info("Configuration service stopped.");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.springframework.osgi.context.BundleContextAware#setBundleContext(org
	 * .osgi.framework.BundleContext)
	 */
	public void setBundleContext(BundleContext context) {
		bundleContext = context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.configuration.ConfigurationService#getMessagingHost
	 * ()
	 */
	public String getMessagingHost() {
		return mc.getMessagingHost();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.configuration.ConfigurationService#getMessagingPort
	 * ()
	 */
	public int getMessagingPort() {
		return mc.getMessagingPort();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.configuration.ConfigurationService#
	 * getMessagingTransport()
	 */
	public String getMessagingTransport() {
		return mc.getMessagingTransport();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.configuration.ConfigurationService#
	 * getMessagingConnectionUrl ()
	 */
	public String getMessagingConnectionUrl() {
		return mc.getConnectionUrl();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.configuration.ConfigurationService#
	 * getIntervalBetweenHeartBeatsSecs()
	 */
	public Integer getIntervalBetweenHeartBeatsSecs() {
		return mc.getIntervalBetweenHeartBeatsSecs();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.configuration.ConfigurationService#
	 * getMaxNumOfConsecutiveFailedHeartbeatAttemptsAllowed()
	 */
	public Integer getMaxNumOfConsecutiveFailedHeartbeatAttemptsAllowed() {
		return mc.getMaxNumOfConsecutiveFailedHeartbeatAttemptsAllowed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.configuration.ConfigurationService#setMessagingHost
	 * (java .lang.String)
	 */
	public void setMessagingHost(String messagingHost) {
		updateProperty("messagingHost", messagingHost);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.configuration.ConfigurationService#setMessagingPort
	 * (int)
	 */
	public void setMessagingPort(int messagingPort) {
		updateProperty("messagingPort", String.valueOf(messagingPort));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.configuration.ConfigurationService#isGenerateHeartBeat
	 * ()
	 */
	public boolean isGenerateHeartBeat() {
		return mc.getGenerateHeartBeat();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.configuration.ConfigurationService#setGenerateHeartBeat
	 * ( java.lang.Boolean)
	 */
	public void setGenerateHeartBeat(Boolean generateHeartBeat) {
		updateProperty("generateHeartBeat", String.valueOf(generateHeartBeat));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.configuration.ConfigurationService#
	 * setMessagingTransport (java.lang.String)
	 */
	public void setMessagingTransport(String messagingTransport) {
		updateProperty("messagingTransport", messagingTransport);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.configuration.ConfigurationService#
	 * getDataChannelServerPort()
	 */
	@Override
	public int getDataChannelServerPort() {
		return mc.getDataChannelServerPort();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.configuration.ConfigurationService#
	 * getDataChannelServerHostName()
	 */
	@Override
	public String getDataChannelServerHostName() {
		return mc.getDataChannelServerHostName();
	}

	/**
	 * 
	 * @param propName
	 * @param propValue
	 */
	@SuppressWarnings("unchecked")
	private void updateProperty(final String propName, final String propValue) {

		ServiceReference configAdminServiceRef =
				bundleContext.getServiceReference(ConfigurationAdmin.class.getName());

		if (configAdminServiceRef != null) {

			ConfigurationAdmin configAdmin =
					(ConfigurationAdmin) bundleContext.getService(configAdminServiceRef);

			if (configAdmin != null) {

				try {
					Configuration config =
							configAdmin.getConfiguration("com.csc.muthur.messaging");

					Dictionary<String, String> properties = config.getProperties();

					if (properties == null) {
						properties = new Hashtable<String, String>();
					}
					properties.put(propName, propValue);
					config.update(properties);

				} catch (IOException e) {
					LOG.warn("Error updating messaging property value : ["
							+ e.getLocalizedMessage() + "]");
				}

			}
		}
	}

}
