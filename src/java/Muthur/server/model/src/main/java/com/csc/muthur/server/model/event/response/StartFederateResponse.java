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
public class StartFederateResponse extends AbstractEvent {

	private boolean startFederateResponseAck;

	/**
	 * 
	 */
	public StartFederateResponse() {
		super();

	}

	/**
	 * 
	 */
	public Element getDataBlockElement() {

		Element dataBlockElement = null;

		if (isSuccess()) {

			dataBlockElement = new Element(EventAttributeName.dataBlock.toString());

			Element nextEle = new Element(EventAttributeName.startFederateResponseAck
					.toString()).setText(Boolean.toString(startFederateResponseAck));

			dataBlockElement.addContent(nextEle);

		}

		return dataBlockElement;
	}

	@Override
	public void newElement(String elementName, String elementValue) {

		if ((elementValue != null) && (elementValue.length() > 0)) {

			if (elementName
					.equalsIgnoreCase(EventAttributeName.startFederateResponseAck
							.toString())) {
				setStartRequestAck(Boolean.valueOf(elementValue));
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
		return EventTypeEnum.StartFederateResponse.getDescription();
	}

	/**
	 * 
	 */
	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.StartFederateResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.AbstractEvent#getHandler()
	 */
	@Override
	public String getHandler() {
		return EventHandlerName.startFederateResponseHandler.toString();
	}

	/**
	 * @param startFederateResponseAck
	 *          the startFederateResponseAck to set
	 */
	public void setStartRequestAck(boolean startFederateRequestAck) {
		this.startFederateResponseAck = startFederateRequestAck;
	}

	/**
	 * @return the startFederateResponseAck
	 */
	public boolean isStartRequestAck() {
		return startFederateResponseAck;
	}

}
