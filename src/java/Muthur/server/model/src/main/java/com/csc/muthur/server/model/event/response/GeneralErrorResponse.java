/**
 * 
 */
package com.csc.muthur.server.model.event.response;

import com.csc.muthur.server.commons.EventTypeEnum;
import com.csc.muthur.server.model.event.AbstractEvent;
import com.csc.muthur.server.model.event.EventClass;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class GeneralErrorResponse extends AbstractEvent {

	/**
	 * 
	 */
	public GeneralErrorResponse() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.IEvent#getEventTypeDescription()
	 */
	@Override
	public String getEventTypeDescription() {
		return EventClass.Response.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.IEvent#getEventName()
	 */
	@Override
	public String getEventName() {
		return EventTypeEnum.GeneralErrorResponse.toString();
	}

	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.GeneralErrorResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GeneralErrorResponse [getEventName()=" + getEventName()
				+ ", getEventType()=" + getEventType() + ", getEventTypeDescription()="
				+ getEventTypeDescription() + super.toString() + "]";
	}

}
