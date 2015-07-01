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
public class ReadyToRunResponse extends AbstractEvent {

	/**
	 * dataBlock attributes
	 */

	private String federateRegistrationHandle;
	private String federationHostName;
	private int federationPort;

	/**
	 * 
	 */
	public ReadyToRunResponse() {
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

			/*
			 * Host name
			 */
			Element nextEle =
					new Element(EventAttributeName.federationHostName.toString())
			.setText(getFederationHostName());
			
			dataBlockElement.addContent(nextEle);
			
			/*
			 * Port
			 */
			nextEle =
					new Element(EventAttributeName.federationPort.toString())
			.setText(Integer.toString(getFederationPort()));
			
			dataBlockElement.addContent(nextEle);

		}

		return dataBlockElement;
	}

	@Override
	public void newElement(String elementName, String elementValue) {

		if ((elementValue != null) && (elementValue.length() > 0)) {
			if (elementName
					.equalsIgnoreCase(EventAttributeName.federationHostName
							.toString())) {
				setFederationHostName(elementValue);
			}else if (elementName
					.equalsIgnoreCase(EventAttributeName.federationPort
							.toString())) {
				setFederationPort(Integer.valueOf(elementValue));
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
		return EventTypeEnum.ReadyToRunResponse.getDescription();
	}

	/**
	 * 
	 */
	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.ReadyToRunResponse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.AbstractEvent#initialization()
	 */
	@Override
	public void initialization() {
		if ((this.federateRegistrationHandle != null)
				&& (!this.federateRegistrationHandle.equals(""))) {
			setFederateRegistrationHandle(this.federateRegistrationHandle);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ReadyToRunResponse [federateRegistrationHandle=")
				.append(federateRegistrationHandle).append(", federationHostName=")
				.append(federationHostName).append(", federationPort=")
				.append(federationPort).append("]");
		return builder.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result =
				prime
						* result
						+ ((federateRegistrationHandle == null) ? 0
								: federateRegistrationHandle.hashCode());
		result =
				prime
						* result
						+ ((federationHostName == null) ? 0 : federationHostName.hashCode());
		result = prime * result + federationPort;
		return result;
	}

	/* (non-Javadoc)
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
		ReadyToRunResponse other = (ReadyToRunResponse) obj;
		if (federateRegistrationHandle == null) {
			if (other.federateRegistrationHandle != null) {
				return false;
			}
		} else if (!federateRegistrationHandle
				.equals(other.federateRegistrationHandle)) {
			return false;
		}
		if (federationHostName == null) {
			if (other.federationHostName != null) {
				return false;
			}
		} else if (!federationHostName.equals(other.federationHostName)) {
			return false;
		}
		if (federationPort != other.federationPort) {
			return false;
		}
		return true;
	}

	/**
	 * @return the federationHostName
	 */
	public String getFederationHostName() {
		return federationHostName;
	}

	/**
	 * @param federationHostName the federationHostName to set
	 */
	public void setFederationHostName(String federationHostName) {
		this.federationHostName = federationHostName;
	}

	/**
	 * @return the federationPort
	 */
	public int getFederationPort() {
		return federationPort;
	}

	/**
	 * @param federationPort the federationPort to set
	 */
	public void setFederationPort(int federationPort) {
		this.federationPort = federationPort;
	}

}
