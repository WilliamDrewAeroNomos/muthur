/**
 * 
 */
package com.csc.muthur.server.model.event.response;

import org.jdom.Element;

import com.csc.muthur.server.commons.EventTypeEnum;
import com.csc.muthur.server.model.event.AbstractEvent;
import com.csc.muthur.server.model.event.EventAttributeName;
import com.csc.muthur.server.model.event.EventClass;
import com.csc.muthur.server.model.event.EventHandlerName;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class PauseFederateResponse extends AbstractEvent {

	private boolean pauseFederateResponseAck;

	/**
	 * 
	 */
	public PauseFederateResponse() {
		super();

	}

	/**
	 * 
	 */
	public Element getDataBlockElement() {

		Element dataBlockElement = null;

		if (isSuccess()) {

			dataBlockElement = new Element(EventAttributeName.dataBlock.toString());

			Element nextEle = new Element(EventAttributeName.pauseFederateResponseAck
					.toString()).setText(Boolean.toString(isPauseFederateResponseAck()));

			dataBlockElement.addContent(nextEle);

		}

		return dataBlockElement;
	}

	@Override
	public void newElement(String elementName, String elementValue) {

		if ((elementValue != null) && (elementValue.length() > 0)) {

			if (elementName
					.equalsIgnoreCase(EventAttributeName.pauseFederateResponseAck
							.toString())) {
				setPauseFederateResponseAck(Boolean.valueOf(elementValue));
			}
		}
	}

	/**
	 * 
	 */
	public String getEventTypeDescription() {
		return EventClass.Response.toString();
	}

	/**
	 * 
	 */
	@Override
	public String getEventName() {
		return EventTypeEnum.PauseFederateResponse.getDescription();
	}

	/**
	 * 
	 */
	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.PauseFederateResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.AbstractEvent#getHandler()
	 */
	@Override
	public String getHandler() {
		return EventHandlerName.pauseFederateResponseHandler.toString();
	}

	/**
	 * @param pauseFederateResponseAck
	 *          the pauseFederateResponseAck to set
	 */
	public void setPauseFederateResponseAck(boolean pauseFederateResponseAck) {
		this.pauseFederateResponseAck = pauseFederateResponseAck;
	}

	/**
	 * @return the pauseFederateResponseAck
	 */
	public boolean isPauseFederateResponseAck() {
		return pauseFederateResponseAck;
	}

}
