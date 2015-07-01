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
public class ResumeFederationResponse extends AbstractEvent {

	private boolean resumeFederationResponseAck;

	/**
	 * 
	 */
	public ResumeFederationResponse() {
		super();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.AbstractEvent#getDataBlockElement()
	 */
	public Element getDataBlockElement() {

		Element dataBlockElement = null;

		if (isSuccess()) {

			dataBlockElement = new Element(EventAttributeName.dataBlock.toString());

			Element nextEle = new Element(
					EventAttributeName.resumeFederationResponseAck.toString())
					.setText(Boolean.toString(resumeFederationResponseAck));

			dataBlockElement.addContent(nextEle);

		}

		return dataBlockElement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.AbstractEvent#newElement(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void newElement(String elementName, String elementValue) {

		if ((elementValue != null) && (elementValue.length() > 0)) {

			if (elementName
					.equalsIgnoreCase(EventAttributeName.resumeFederationResponseAck
							.toString())) {
				setResumeFederationResponseAck(Boolean.valueOf(elementValue));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.IEvent#getEventTypeDescription()
	 */
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
		return EventTypeEnum.ResumeFederationResponse.getDescription();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.IEvent#getEventType()
	 */
	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.ResumeFederationResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.AbstractEvent#getHandler()
	 */
	@Override
	public String getHandler() {
		return EventHandlerName.resumeFederationResponseHandler.toString();
	}

	/**
	 * @return the resumeFederationResponseAck
	 */
	public boolean isResumeFederationResponseAck() {
		return resumeFederationResponseAck;
	}

	/**
	 * @param resumeFederationResponseAck
	 *          the resumeFederationResponseAck to set
	 */
	public void setResumeFederationResponseAck(boolean resumeFederationResponseAck) {
		this.resumeFederationResponseAck = resumeFederationResponseAck;
	}

}
