/**
 * 
 */
package com.csc.muthur.server.model.event;

import com.csc.muthur.server.commons.EventTypeEnum;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class ExceptionEvent extends AbstractEvent {

	/**
	 * 
	 */
	public ExceptionEvent() {
		super();
	}

	@Override
	public String getEventTypeDescription() {
		return EventClass.System.toString();
	}

	@Override
	public String getEventName() {
		return EventTypeEnum.ExceptionEvent.getDescription();
	}

	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.ExceptionEvent;
	}

}
