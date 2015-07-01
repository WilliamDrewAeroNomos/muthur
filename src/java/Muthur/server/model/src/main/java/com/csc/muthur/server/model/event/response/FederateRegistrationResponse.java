/**
 * 
 */
package com.csc.muthur.server.model.event.response;

import org.jdom.Element;

import com.csc.muthur.server.commons.EventTypeEnum;
import com.csc.muthur.server.model.event.AbstractEvent;
import com.csc.muthur.server.model.event.EventAttributeName;
import com.csc.muthur.server.model.event.EventClass;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class FederateRegistrationResponse extends AbstractEvent {

	/**
	 * dataBlock attributes
	 */
	private String federateName;
	private boolean bHeartBeanEnabled;

	/**
	 * 
	 */
	public FederateRegistrationResponse() {
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

			Element nextEle = new Element(EventAttributeName.federateName.toString())
					.setText(getFederateName());

			dataBlockElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.heartBeatEnabled.toString())
					.setText(Boolean.toString(bHeartBeanEnabled));

			dataBlockElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.registrationID.toString())
					.setText(getFederateRegistrationHandle());

			dataBlockElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.status.toString())
					.setText(getStatus());

			dataBlockElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.success.toString())
					.setText(Boolean.toString(isSuccess()));

			dataBlockElement.addContent(nextEle);
		}

		return dataBlockElement;
	}

	@Override
	public void newElement(String elementName, String elementValue) {

		if ((elementValue != null) && (elementValue.length() > 0)) {
			if (elementName.equalsIgnoreCase(EventAttributeName.registrationID
					.toString())) {
				setFederateRegistrationHandle(elementValue);
			} else if (elementName.equalsIgnoreCase(EventAttributeName.federateName
					.toString())) {
				federateName = elementValue;
			} else if (elementName.equalsIgnoreCase(EventAttributeName.success
					.toString())) {
				setSuccess(Boolean.getBoolean(elementValue));
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.heartBeatEnabled.toString())) {
				setSuccess(Boolean.getBoolean(elementValue));
			} else if (elementName.equalsIgnoreCase(EventAttributeName.status
					.toString())) {
				setStatus(elementValue);
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
		return EventTypeEnum.FederateRegistrationResponse.toString();
	}

	/**
	 * @return Returns the federateName.
	 */
	public final String getFederateName() {
		return this.federateName;
	}

	/**
	 * @param federateName
	 *          The federateName to set.
	 */
	public final void setFederateName(String federateName) {
		this.federateName = federateName;
	}

	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.FederateRegistrationResponse;
	}

	/**
	 * @return the bHeartBeanEnabled
	 */
	public boolean isbHeartBeanEnabled() {
		return bHeartBeanEnabled;
	}

	/**
	 * @param bHeartBeanEnabled
	 *          the bHeartBeanEnabled to set
	 */
	public void setbHeartBeanEnabled(boolean bHeartBeanEnabled) {
		this.bHeartBeanEnabled = bHeartBeanEnabled;
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
		result = prime * result + (bHeartBeanEnabled ? 1231 : 1237);
		result = prime * result
				+ ((federateName == null) ? 0 : federateName.hashCode());
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
		FederateRegistrationResponse other = (FederateRegistrationResponse) obj;
		if (bHeartBeanEnabled != other.bHeartBeanEnabled) {
			return false;
		}
		if (federateName == null) {
			if (other.federateName != null) {
				return false;
			}
		} else if (!federateName.equals(other.federateName)) {
			return false;
		}
		return true;
	}

}
