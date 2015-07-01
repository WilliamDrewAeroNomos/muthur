/**
 * 
 */
package com.csc.muthur.server.eventserver;

import javax.jms.Destination;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.configuration.ConfigurationService;
import com.csc.muthur.server.registration.RegistrationService;
import com.csc.muthur.server.router.RouterService;

/**
 * @author <a href=mailto:Nexsim@foo.com>Nexsim</a>
 * @version $Revision$
 */
public interface EventService {

	/**
	 * 
	 * @throws MuthurException
	 */
	void start() throws Exception;

	/**
	 * 
	 * @throws MuthurException
	 */
	void stop() throws Exception;

	/**
	 * 
	 * @throws Exception
	 */
	public abstract void startConsumer() throws Exception;

	/**
	 * 
	 * @throws Exception
	 */
	void stopConsumer() throws Exception;

	void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException;

	void setConfigurationService(ConfigurationService configurationService);

	void setRegistrationService(RegistrationService registrationService);

	void setRouterService(RouterService routerService);

	Destination getEventQueue();

}
