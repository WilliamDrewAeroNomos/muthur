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
public class UpdateFederationExecutionTimeResponse extends AbstractEvent {

	private boolean updateFederationExecutionTimeResponseAck;

	/**
	 * 
	 */
	public UpdateFederationExecutionTimeResponse() {
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
					.setText(Boolean
							.toString(isUpdateFederationExecutionTimeResponseAck()));

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
				setUpdateFederationExecutionTimeResponseAck(Boolean
						.valueOf(elementValue));
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
		return EventTypeEnum.UpdateFederationExecutionTimeResponse.getDescription();
	}

	/**
	 * 
	 */
	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.UpdateFederationExecutionTimeResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.AbstractEvent#getHandler()
	 */
	@Override
	public String getHandler() {
		return EventHandlerName.updateFederationExecutionTimeResponseHandler
				.toString();
	}

	/**
	 * @param updateFederationExecutionTimeResponseAck
	 *          the updateFederationExecutionTimeResponseAck to set
	 */
	public void setUpdateFederationExecutionTimeResponseAck(
			boolean updateFederationExecutionTimeResponseAck) {
		this.updateFederationExecutionTimeResponseAck = updateFederationExecutionTimeResponseAck;
	}

	/**
	 * @return the updateFederationExecutionTimeResponseAck
	 */
	public boolean isUpdateFederationExecutionTimeResponseAck() {
		return updateFederationExecutionTimeResponseAck;
	}

}
