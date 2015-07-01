/**
 * 
 */
package com.csc.muthur.server.router;

import javax.jms.Destination;
import javax.jms.Message;

import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.commons.ServiceName;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.configuration.ConfigurationService;

/**
 * @author <a href=mailto:Nexsim@foo.com>Nexsim</a>
 * @version $Revision$
 */
public interface RouterService {

	/**
	 * @throws MuthurException
	 * 
	 */
	void start() throws MuthurException;

	/**
	 * @throws MuthurException
	 * 
	 */
	void stop() throws MuthurException;

	/**
	 * 
	 * @param payload
	 * @param jmsReplyTo
	 * @param jmsCorrelationID
	 * @throws MuthurException
	 */
	void queue(String payload, Destination jmsReplyTo, String jmsCorrelationID)
			throws MuthurException;

	/**
	 * 
	 * @param payload
	 * @param destinationName
	 * @throws MuthurException
	 */
	void queue(String payload, String destinationName) throws MuthurException;

	/**
	 * 
	 * @param response
	 * @param message
	 * @throws MuthurException
	 */
	void sendResponse(final IEvent response, final Message message)
			throws MuthurException;

	/**
	 * @return the configurationServer
	 */
	ConfigurationService getConfigurationServer();

	/**
	 * @param configurationServer
	 *          the configurationServer to set
	 */
	void setConfigurationServer(ConfigurationService configurationServer);

	/**
	 * 
	 * @param payload
	 * @param service
	 * @throws MuthurException
	 */
	void postEvent(String payload, ServiceName service) throws MuthurException;

	/**
	 * @param serialize
	 * @param replyToQueueName
	 * @param correlationID
	 * @throws MuthurException
	 */
	void queue(String serialize, String replyToQueueName, String correlationID)
			throws MuthurException;
}