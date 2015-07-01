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
public class StartFederationResponse extends AbstractEvent {

	private boolean startFederationResponseAck;
	private String federationExecutionModelHandle;

	/**
	 * 
	 */
	public StartFederationResponse() {
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
					EventAttributeName.startFederationResponseAck.toString())
					.setText(Boolean.toString(startFederationResponseAck));

			dataBlockElement.addContent(nextEle);

			nextEle = new Element(
					EventAttributeName.federationExecutionModelHandle.toString())
					.setText(federationExecutionModelHandle);

			dataBlockElement.addContent(nextEle);

		}

		return dataBlockElement;
	}

	@Override
	public void newElement(String elementName, String elementValue) {

		if ((elementValue != null) && (elementValue.length() > 0)) {

			if (elementName
					.equalsIgnoreCase(EventAttributeName.startFederationResponseAck
							.toString())) {
				setStartFederationResponseAck(Boolean.valueOf(elementValue));
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.federationExecutionModelHandle
							.toString())) {
				setFederationExecutionModelHandle(elementValue);
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
		return EventTypeEnum.StartFederationResponse.getDescription();
	}

	/**
	 * 
	 */
	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.StartFederationResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.AbstractEvent#getHandler()
	 */
	@Override
	public String getHandler() {
		return EventHandlerName.startFederationResponseHandler.toString();
	}

	/**
	 * @param startFederationResponseAck
	 *          the startFederationResponseAck to set
	 */
	public void setStartFederationResponseAck(boolean startFederationResponseAck) {
		this.startFederationResponseAck = startFederationResponseAck;
	}

	/**
	 * @return the startFederationResponseAck
	 */
	public boolean isStartFederationResponseAck() {
		return startFederationResponseAck;
	}

}
