/**
 * 
 */
package com.csc.muthur.server.model.event.response;

import org.jdom.Element;

import com.csc.muthur.server.commons.EventTypeEnum;
import com.csc.muthur.server.model.event.AbstractEvent;
import com.csc.muthur.server.model.event.EventAttributeName;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class DataSubscriptionResponse extends AbstractEvent {

	/**
	 * 
	 */
	public DataSubscriptionResponse() {
		super();
	}

	/**
	 * 
	 */
	public Element getDataBlockElement() {

		Element dataBlockElement = null;

		if (isSuccess()) {

			dataBlockElement = new Element(EventAttributeName.dataBlock.toString());
		}

		return dataBlockElement;
	}

	public String getEventTypeDescription() {
		return "Response";
	}

	@Override
	public String getEventName() {
		return EventTypeEnum.DataSubscriptionResponse.getDescription();
	}

	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.DataSubscriptionResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.AbstractEvent#initialization()
	 */
	@Override
	public void initialization() {
		super.initialization();
	}

}
