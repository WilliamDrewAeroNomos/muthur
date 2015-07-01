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
public class PauseFederateRequest extends AbstractEvent {

	private long lengthOfPauseMSecs = 0L;

	/**
	 * 
	 */
	public PauseFederateRequest() {
		super();
	}

	/**
	 * 
	 */
	public Element getDataBlockElement() {

		Element dataBlockElement = null;

		if (isSuccess()) {

			dataBlockElement = new Element(EventAttributeName.dataBlock.toString());

			Element nextEle =
					new Element(EventAttributeName.lengthOfPauseMSecs.toString())
							.setText(Long.toString(lengthOfPauseMSecs));

			dataBlockElement.addContent(nextEle);

			nextEle =
					new Element(
							EventAttributeName.federationExecutionModelHandle.toString())
							.setText(getFederationExecutionModelHandle());

			dataBlockElement.addContent(nextEle);

		}

		return dataBlockElement;
	}

	@Override
	public void newElement(String elementName, String elementValue) {

		if ((elementValue != null) && (elementValue.length() > 0)) {

			if (elementName.equalsIgnoreCase(EventAttributeName.lengthOfPauseMSecs
					.toString())) {
				setLengthOfPauseMSecs(Long.valueOf(elementValue));
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.federationExecutionModelHandle
							.toString())) {
				setFederationExecutionModelHandle(elementValue);
			}
		}
	}

	@Override
	public String getEventTypeDescription() {
		return EventClass.Request.toString();
	}

	@Override
	public String getEventName() {
		return EventTypeEnum.PauseFederateRequest.getDescription();
	}

	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.PauseFederateRequest;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.AbstractEvent#getHandler()
	 */
	@Override
	public String getHandler() {
		return EventHandlerName.pauseFederateRequestHandler.toString();
	}

	/**
	 * @param lengthOfPauseMSecs
	 *          the lengthOfPauseMSecs to set
	 */
	public void setLengthOfPauseMSecs(long lengthOfPauseMSecs) {
		this.lengthOfPauseMSecs = lengthOfPauseMSecs;
	}

	/**
	 * @return the lengthOfPauseMSecs
	 */
	public long getLengthOfPauseMSecs() {
		return lengthOfPauseMSecs;
	}

}
