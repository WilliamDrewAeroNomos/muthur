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
public class ReadyToRunRequest extends AbstractEvent {

	private String timeManagementQueueName;
	private String federateRequestQueueName;

	/**
	 * 
	 * @param timeToLiveSecs
	 */
	public ReadyToRunRequest() {
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
							EventAttributeName.federationExecutionModelHandle.toString())
							.setText(getFederationExecutionModelHandle());

			dataBlockElement.addContent(nextEle);

			nextEle =
					new Element(EventAttributeName.timeManagementQueueName.toString())
							.setText(timeManagementQueueName);

			dataBlockElement.addContent(nextEle);

			nextEle =
					new Element(EventAttributeName.federateRequestQueueName.toString())
							.setText(federateRequestQueueName);

			dataBlockElement.addContent(nextEle);

		}

		return dataBlockElement;
	}

	@Override
	public void newElement(String elementName, String elementValue) {

		if ((elementValue != null) && (elementValue.length() > 0)) {

			if (elementName
					.equalsIgnoreCase(EventAttributeName.federationExecutionModelHandle
							.toString())) {
				setFederationExecutionModelHandle(elementValue);
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.timeManagementQueueName
							.toString())) {
				timeManagementQueueName = elementValue;
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.federateRequestQueueName
							.toString())) {
				federateRequestQueueName = elementValue;
			}

		}
	}

	@Override
	public String getEventTypeDescription() {
		return EventClass.Request.toString();
	}

	@Override
	public String getEventName() {
		return EventTypeEnum.ReadyToRunRequest.getDescription();
	}

	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.ReadyToRunRequest;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.AbstractEvent#getHandler()
	 */
	@Override
	public String getHandler() {
		return EventHandlerName.readyToRunRequestHandler.toString();
	}

	/**
	 * 
	 * @return
	 */
	public String getTimeManagementQueueName() {
		return timeManagementQueueName;
	}

	/**
	 * 
	 * @param timeManagementQueueName
	 */
	public void setTimeManagementQueueName(String timeManagementQueueName) {
		this.timeManagementQueueName = timeManagementQueueName;
	}

	/**
	 * @return the federateRequestQueueName
	 */
	public String getFederateRequestQueueName() {
		return federateRequestQueueName;
	}

	/**
	 * @param federateRequestQueueName
	 *          the federateRequestQueueName to set
	 */
	public void setFederateRequestQueueName(String federateRequestQueueName) {
		this.federateRequestQueueName = federateRequestQueueName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result =
				prime
						* result
						+ ((federateRequestQueueName == null) ? 0
								: federateRequestQueueName.hashCode());
		result =
				prime
						* result
						+ ((getFederationExecutionModelHandle() == null) ? 0
								: getFederationExecutionModelHandle().hashCode());
		result =
				prime
						* result
						+ ((timeManagementQueueName == null) ? 0 : timeManagementQueueName
								.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ReadyToRunRequest other = (ReadyToRunRequest) obj;
		if (federateRequestQueueName == null) {
			if (other.federateRequestQueueName != null) {
				return false;
			}
		} else if (!federateRequestQueueName.equals(other.federateRequestQueueName)) {
			return false;
		}
		if (getFederationExecutionModelHandle() == null) {
			if (other.getFederationExecutionModelHandle() != null) {
				return false;
			}
		} else if (!getFederationExecutionModelHandle().equals(
				other.getFederationExecutionModelHandle())) {
			return false;
		}
		if (timeManagementQueueName == null) {
			if (other.timeManagementQueueName != null) {
				return false;
			}
		} else if (!timeManagementQueueName.equals(other.timeManagementQueueName)) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ReadyToRunRequest [federationExecutionModelHandle=")
				.append(getFederationExecutionModelHandle())
				.append(", timeManagementQueueName=").append(timeManagementQueueName)
				.append(", federateRequestQueueName=").append(federateRequestQueueName)
				.append("]");
		return builder.toString();
	}

}
