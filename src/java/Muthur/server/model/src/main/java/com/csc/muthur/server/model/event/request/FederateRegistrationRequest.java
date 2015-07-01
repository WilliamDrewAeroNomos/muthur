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
public class FederateRegistrationRequest extends AbstractEvent {

	/**
	 * dataBlock attributes
	 */
	private String federateName;
	private String federateEventQueueName;
	private Integer federateHeartBeatIntervalSecs;
	private String licenseKey;

	/**
	 * 
	 */
	public FederateRegistrationRequest() {
		super();
	}

	/**
	 * 
	 */
	public Element getDataBlockElement() {

		Element dataBlockElement = null;

		if (isSuccess()) {

			dataBlockElement = new Element(EventAttributeName.dataBlock.toString());

			Element nextEle = new Element(EventAttributeName.federateName.toString())
					.setText(getFederateName());

			dataBlockElement.addContent(nextEle);

			nextEle = new Element(
					EventAttributeName.federateEventQueueName.toString())
					.setText(getFederateEventQueueName());

			dataBlockElement.addContent(nextEle);

			if (federateHeartBeatIntervalSecs != null) {
				nextEle = new Element(
						EventAttributeName.federateHeartBeatIntervalSecs.toString())
						.setText(String.valueOf(getFederateHeartBeatIntervalSecs()
								.intValue()));
				dataBlockElement.addContent(nextEle);
			}

			nextEle = new Element(EventAttributeName.licenseKey.toString())
					.setText(getLicenseKey());

			dataBlockElement.addContent(nextEle);

		}

		return dataBlockElement;
	}

	/**
	 * 
	 */
	public String getEventTypeDescription() {
		return EventClass.Request.toString();
	}

	@Override
	public String getEventName() {
		return EventTypeEnum.FederateRegistrationRequest.toString();
	}

	@Override
	public void newElement(String elementName, String elementValue) {

		if ((elementValue != null) && (elementValue.length() > 0)) {

			if (elementName.equalsIgnoreCase(EventAttributeName.federateName
					.toString())) {
				federateName = elementValue;
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.federateEventQueueName
							.toString())) {
				federateEventQueueName = elementValue;
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.federateHeartBeatIntervalSecs
							.toString())) {
				federateHeartBeatIntervalSecs = Integer.valueOf(elementValue);
			} else if (elementName.equalsIgnoreCase(EventAttributeName.licenseKey
					.toString())) {
				licenseKey = elementValue;
			}
		}

	}

	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.FederateRegistrationRequest;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.AbstractEvent#getHandler()
	 */
	@Override
	public String getHandler() {
		return EventHandlerName.registrationRequestHandler.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FederateRegistrationRequest [federateName=")
				.append(federateName).append(", federateEventQueueName=")
				.append(federateEventQueueName)
				.append(", federateHeartBeatIntervalSecs=")
				.append(federateHeartBeatIntervalSecs).append(", licenseKey=")
				.append(licenseKey).append("]");
		return builder.toString();
	}

	/**
	 * @return the federateName
	 */
	public final String getFederateName() {
		return federateName;
	}

	/**
	 * @param federateName
	 *          the federateName to set
	 */
	public final void setFederateName(String federateName) {
		this.federateName = federateName;
	}

	/**
	 * @param federateEventQueueName
	 *          the federateEventQueueName to set
	 */
	public void setFederateEventQueueName(String federateEventQueueName) {
		this.federateEventQueueName = federateEventQueueName;
	}

	/**
	 * @return the federateEventQueueName
	 */
	public String getFederateEventQueueName() {
		return federateEventQueueName;
	}

	/**
	 * @param federateHeartBeatIntervalSecs
	 *          the federateHeartBeatIntervalSecs to set
	 */
	public void setFederateHeartBeatIntervalSecs(
			Integer federateHeartBeatIntervalSecs) {
		this.federateHeartBeatIntervalSecs = federateHeartBeatIntervalSecs;
	}

	/**
	 * @return the federateHeartBeatIntervalSecs
	 */
	public Integer getFederateHeartBeatIntervalSecs() {
		return federateHeartBeatIntervalSecs;
	}

	/**
	 * @return the licenseKey
	 */
	public String getLicenseKey() {
		return licenseKey;
	}

	/**
	 * @param licenseKey
	 *          the licenseKey to set
	 */
	public void setLicenseKey(String licenseKey) {
		this.licenseKey = licenseKey;
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
		result = prime
				* result
				+ ((federateEventQueueName == null) ? 0 : federateEventQueueName
						.hashCode());
		result = prime
				* result
				+ ((federateHeartBeatIntervalSecs == null) ? 0
						: federateHeartBeatIntervalSecs.hashCode());
		result = prime * result
				+ ((federateName == null) ? 0 : federateName.hashCode());
		result = prime * result
				+ ((licenseKey == null) ? 0 : licenseKey.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		FederateRegistrationRequest other = (FederateRegistrationRequest) obj;
		if (federateEventQueueName == null) {
			if (other.federateEventQueueName != null)
				return false;
		} else if (!federateEventQueueName.equals(other.federateEventQueueName))
			return false;
		if (federateHeartBeatIntervalSecs == null) {
			if (other.federateHeartBeatIntervalSecs != null)
				return false;
		} else if (!federateHeartBeatIntervalSecs
				.equals(other.federateHeartBeatIntervalSecs))
			return false;
		if (federateName == null) {
			if (other.federateName != null)
				return false;
		} else if (!federateName.equals(other.federateName))
			return false;
		if (licenseKey == null) {
			if (other.licenseKey != null)
				return false;
		} else if (!licenseKey.equals(other.licenseKey))
			return false;
		return true;
	}

}
