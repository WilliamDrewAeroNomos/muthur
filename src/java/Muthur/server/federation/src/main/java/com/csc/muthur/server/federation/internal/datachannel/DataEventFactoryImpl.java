/**
 * 
 */
package com.csc.muthur.server.federation.internal.datachannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.federation.DataEventFactory;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public class DataEventFactoryImpl implements DataEventFactory,
		ApplicationContextAware {

	private final static Logger LOG = LoggerFactory
			.getLogger(DataEventFactoryImpl.class.getName());

	private ApplicationContext applicationContext = null;

	/**
	 * @throws MuthurException
	 * 
	 */
	public DataEventFactoryImpl(){
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.federation.DataEventFactory#createEvent(java.lang
	 * .String)
	 */
	@Override
	public IEvent createEvent(String eventName) {

		IEvent event = null;

		if (getApplicationContext() != null) {
			
			event = (IEvent) getApplicationContext().getBean(eventName);

			if (event != null) {
				LOG.debug("Created [" + event.getEventName() + "] event.");
			} else {
				LOG.error("Unable to create data event [" + eventName
						+ "] in DataEventFactoryImpl.");
			}
			
		} else {
			LOG.error("Application context in DataEventFactoryImpl was null. "
					+ "Unable to create event [" + eventName + "]");
		}

		return event;
	}

	/**
	 * @return the applicationContext
	 */
	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * @param applicationContext
	 *          the applicationContext to set
	 */
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

}
