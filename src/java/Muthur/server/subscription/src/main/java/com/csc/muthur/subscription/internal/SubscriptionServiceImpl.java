/**
 * 
 */
package com.csc.muthur.subscription.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.commons.exception.MuthurException;
import com.csc.muthur.configuration.ConfigurationService;
import com.csc.muthur.subscription.SubscriptionService;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class SubscriptionServiceImpl implements SubscriptionService {

	private final static Logger LOG = LoggerFactory
			.getLogger(SubscriptionServiceImpl.class.getName());

	public ConfigurationService configurationService;

	/**
	 * 
	 */
	public SubscriptionServiceImpl() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see subscription.internal.SubscriptionService#start()
	 */
	public void start() throws MuthurException {

		LOG.debug("Starting subscription service...");

		LOG.info("Subscription service started.");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see subscription.internal.SubscriptionService#stop()
	 */
	public void stop() throws MuthurException {

		LOG.debug("Stopping subscription service...");

		LOG.info("Subscription service stopped.");
	}

}
