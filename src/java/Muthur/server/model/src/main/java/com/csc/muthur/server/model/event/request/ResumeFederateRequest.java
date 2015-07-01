/**
 * 
 */
package com.csc.muthur.server.model.event.request;

import com.csc.muthur.server.commons.EventTypeEnum;
import com.csc.muthur.server.model.event.AbstractEvent;
import com.csc.muthur.server.model.event.EventClass;
import com.csc.muthur.server.model.event.EventHandlerName;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class ResumeFederateRequest extends AbstractEvent {

	/**
	 * 
	 */
	public ResumeFederateRequest() {
		super();
	}

	@Override
	public String getEventTypeDescription() {
		return EventClass.Request.toString();
	}

	@Override
	public String getEventName() {
		return EventTypeEnum.ResumeFederateRequest.getDescription();
	}

	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.ResumeFederateRequest;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.AbstractEvent#getHandler()
	 */
	@Override
	public String getHandler() {
		return EventHandlerName.resumeFederateRequestHandler.toString();
	}

}
