/**
 * 
 */
package com.csc.muthur.server.model.event.request;

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
public class UpdateFederationExecutionTimeRequest extends AbstractEvent {

	/**
	 * 
	 */
	private long federationExecutionTimeMSecs = 0L;

	/**
	 * 
	 */
	public UpdateFederationExecutionTimeRequest() {
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

			Element nextEle =
					new Element(
							EventAttributeName.federationExecutionTimeMSecs.toString())
							.setText(Long.toString(federationExecutionTimeMSecs));

			dataBlockElement.addContent(nextEle);

			nextEle =
					new Element(
							EventAttributeName.federationExecutionModelHandle.toString())
							.setText(getFederationExecutionModelHandle());

			dataBlockElement.addContent(nextEle);

		}

		return dataBlockElement;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.model.event.AbstractEvent#newElement(java.lang.String
	 * , java.lang.String)
	 */

	@Override
	public void newElement(String elementName, String elementValue) {

		if ((elementValue != null) && (elementValue.length() > 0)) {

			if (elementName
					.equalsIgnoreCase(EventAttributeName.federationExecutionTimeMSecs
							.toString())) {
				setFederationExecutionTimeMSecs(Long.valueOf(elementValue));
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.federationExecutionModelHandle
							.toString())) {
				setFederationExecutionModelHandle(elementValue);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.IEvent#getEventTypeDescription()
	 */
	@Override
	public String getEventTypeDescription() {
		return EventClass.Request.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.IEvent#getEventName()
	 */
	@Override
	public String getEventName() {
		return EventTypeEnum.UpdateFederationExecutionTimeRequest.getDescription();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.IEvent#getEventType()
	 */
	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.UpdateFederationExecutionTimeRequest;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.AbstractEvent#getHandler()
	 */
	@Override
	public String getHandler() {
		return EventHandlerName.updateFederationExecutionTimeRequestHandler
				.toString();
	}

	/**
	 * @return the federationExecutionTimeMSecs
	 */
	public long getFederationExecutionTimeMSecs() {
		return federationExecutionTimeMSecs;
	}

	/**
	 * @param federationExecutionTimeMSecs
	 *          the federationExecutionTimeMSecs to set
	 */
	public void setFederationExecutionTimeMSecs(
			final long federationExecutionTimeMSecs) {
		this.federationExecutionTimeMSecs = federationExecutionTimeMSecs;
	}

}
