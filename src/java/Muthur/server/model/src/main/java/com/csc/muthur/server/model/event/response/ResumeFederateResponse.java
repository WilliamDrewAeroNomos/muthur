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
public class ResumeFederateResponse extends AbstractEvent {

	private boolean resumeFederateResponseAck;

	/**
	 * 
	 */
	public ResumeFederateResponse() {
		super();

	}

	/**
	 * 
	 */
	public Element getDataBlockElement() {

		Element dataBlockElement = null;

		if (isSuccess()) {

			dataBlockElement = new Element(EventAttributeName.dataBlock.toString());

			Element nextEle = new Element(EventAttributeName.resumeFederateResponseAck
					.toString()).setText(Boolean.toString(resumeFederateResponseAck));

			dataBlockElement.addContent(nextEle);

		}

		return dataBlockElement;
	}

	@Override
	public void newElement(String elementName, String elementValue) {

		if ((elementValue != null) && (elementValue.length() > 0)) {

			if (elementName
					.equalsIgnoreCase(EventAttributeName.resumeFederateResponseAck
							.toString())) {
				setResumeFederateResponseAck(Boolean.valueOf(elementValue));
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
		return EventTypeEnum.ResumeFederateResponse.getDescription();
	}

	/**
	 * 
	 */
	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.ResumeFederateResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.AbstractEvent#getHandler()
	 */
	@Override
	public String getHandler() {
		return EventHandlerName.resumeFederateResponseHandler.toString();
	}

	/**
	 * @return the resumeFederateResponseAck
	 */
	public boolean isResumeFederateResponseAck() {
		return resumeFederateResponseAck;
	}

	/**
	 * @param resumeFederateResponseAck the resumeFederateResponseAck to set
	 */
	public void setResumeFederateResponseAck(boolean resumeFederateResponseAck) {
		this.resumeFederateResponseAck = resumeFederateResponseAck;
	}


}
