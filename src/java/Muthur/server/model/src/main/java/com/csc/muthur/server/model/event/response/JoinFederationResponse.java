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
public class JoinFederationResponse extends AbstractEvent {

	/**
	 * 
	 */
	public JoinFederationResponse() {
		super();
	}

	public String getEventTypeDescription() {
		return EventClass.Response.toString();
	}

	@Override
	public String getEventName() {
		return EventTypeEnum.JoinFederationResponse.getDescription();
	}

	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.JoinFederationResponse;
	}

}
