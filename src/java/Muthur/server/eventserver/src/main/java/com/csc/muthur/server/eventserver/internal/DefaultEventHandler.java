/**
 * 
 */
package com.csc.muthur.server.eventserver.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.exception.MuthurException;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class DefaultEventHandler extends AbstractEventHandler {

	private static final Logger LOG =
			LoggerFactory.getLogger(DefaultEventHandler.class.getName());

	/**
	 * 
	 */
	public DefaultEventHandler() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.eventserver.internal.IEventHandler#handle()
	 */
	public void handle() throws MuthurException {

		LOG.warn("Default event handler invoked.");

		if (getEvent() == null) {
			LOG.warn("Received [" + getEvent().getEventName() + "] event");
		}
	}
}
