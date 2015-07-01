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
public class PauseFederationResponse extends AbstractEvent {

	private boolean pauseFederationResponseAck;

	/**
	 * 
	 */
	public PauseFederationResponse() {
		super();

	}

	/**
	 * 
	 */
	public Element getDataBlockElement() {

		Element dataBlockElement = null;

		if (isSuccess()) {

			dataBlockElement = new Element(EventAttributeName.dataBlock.toString());

			Element nextEle = new Element(
					EventAttributeName.pauseFederationResponseAck.toString())
					.setText(Boolean.toString(isPauseFederationResponseAck()));

			dataBlockElement.addContent(nextEle);

		}

		return dataBlockElement;
	}

	@Override
	public void newElement(String elementName, String elementValue) {

		if ((elementValue != null) && (elementValue.length() > 0)) {

			if (elementName
					.equalsIgnoreCase(EventAttributeName.pauseFederationResponseAck
							.toString())) {
				setPauseFederationResponseAck(Boolean.valueOf(elementValue));
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
		return EventTypeEnum.PauseFederationResponse.getDescription();
	}

	/**
	 * 
	 */
	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.PauseFederationResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.AbstractEvent#getHandler()
	 */
	@Override
	public String getHandler() {
		return EventHandlerName.pauseFederationResponseHandler.toString();
	}

	/**
	 * @param pauseFederationResponseAck
	 *          the pauseFederationResponseAck to set
	 */
	public void setPauseFederationResponseAck(boolean pauseFederateResponseAck) {
		this.pauseFederationResponseAck = pauseFederateResponseAck;
	}

	/**
	 * @return the pauseFederationResponseAck
	 */
	public boolean isPauseFederationResponseAck() {
		return pauseFederationResponseAck;
	}

}
