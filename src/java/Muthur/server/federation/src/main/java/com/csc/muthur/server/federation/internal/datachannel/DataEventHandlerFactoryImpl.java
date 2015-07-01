/**
 * 
 */
package com.csc.muthur.server.federation.internal.datachannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.csc.muthur.server.commons.IDataEventHandler;
import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.federation.DataEventHandlerFactory;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public final class DataEventHandlerFactoryImpl implements
		DataEventHandlerFactory, ApplicationContextAware {

	private static final Logger LOG = LoggerFactory
			.getLogger(DataEventHandlerFactoryImpl.class.getName());

	/**
	 * Spring application context
	 */
	private ApplicationContext applicationContext;

	/**
	 * 
	 * @param applicationContext
	 * @throws MuthurException
	 */
	public DataEventHandlerFactoryImpl() {
		LOG.debug("Created DataEventHandlerFactoryImpl.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.federation.internal.DataEventHandlerFactory#
	 * getEventHandler(com.csc.muthur.server.model.event.IEvent)
	 */
	@Override
	public IDataEventHandler getEventHandler(final IEvent event) {

		IDataEventHandler dataEventHandler = null;

		if (event == null) {
			LOG.error("Event passed to DataEventHandlerFactoryImpl.getEventHandler() was null. "
					+ "Unable to create event handler");
			return null;
		}

		if (getApplicationContext() == null) {
			LOG.error("Application context in DataEventHandlerFactoryImpl was null. "
					+ "Unable to create event handler for event [" + event.getEventName()
					+ "]");
			return null;
		}

		dataEventHandler =
				(IDataEventHandler) applicationContext.getBean(event.getHandler());

		if (dataEventHandler == null) {
			LOG.error("Unable to create event handler for event ["
					+ event.getEventName() + "]");
			return null;
		}

		dataEventHandler.setEvent(event);

		LOG.debug("Created handler [" + dataEventHandler.toString()
				+ "] for event [" + event.getEventName() + "]");

		return dataEventHandler;
	}

	/**
	 * @return the applicationContext
	 */
	@Override
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * @param applicationContext
	 *          the applicationContext to set
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

}
