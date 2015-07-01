/**
 * 
 */
package com.csc.muthur.server.eventserver.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.csc.muthur.server.commons.IEvent;

/**
 * 
 * @author <a href=mailto:support@atcloud.com>support</a>
 * @version $Revision: $
 */
public final class EventHandlerFactory {

	private static final Logger LOG = LoggerFactory
			.getLogger(EventHandlerFactory.class.getName());

	/**
	 * only instance of this class
	 */
	private static EventHandlerFactory INSTANCE = null;

	/**
	 * Spring application context retrieved once in {@link #getInstance()}
	 */
	private static ApplicationContext applicationContext;

	/**
	 * 
	 * @return Single instance of the event factory
	 */
	public static EventHandlerFactory getInstance() {
		if (INSTANCE == null) {

			INSTANCE = new EventHandlerFactory();
			applicationContext = EventServiceImpl.getApplicationContext();

		}
		return INSTANCE;
	}

	/**
	 * 
	 * @param event
	 *          {@link IEvent} to be handled or processed
	 * @return {@link IDataEventHandler} configured in the Spring application context
	 *         for the {@link IEvent} parameter.
	 */
	public IEventHandler getEventHandler(final IEvent event) {

		IEventHandler eventHandler =
				(IEventHandler) applicationContext.getBean(event.getHandler());

		eventHandler.setEvent(event);

		LOG.debug("Created [" + event.getEventName() + "] event handler.");

		return eventHandler;
	}

	/**
	 * do not instantiate
	 */
	private EventHandlerFactory() {

	}

}
