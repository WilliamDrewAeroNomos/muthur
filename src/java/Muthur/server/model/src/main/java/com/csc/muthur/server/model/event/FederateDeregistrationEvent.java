/**
 * 
 */
package com.csc.muthur.server.model.event;

import org.jdom.Element;

import com.csc.muthur.server.commons.EventTypeEnum;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class FederateDeregistrationEvent extends AbstractEvent {

	private String federationExecutionModelHandle;
	private String terminationReason;
	private String federateName;

	/**
	 * 
	 */
	public FederateDeregistrationEvent() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.AbstractEvent#getDataBlockElement()
	 */
	public Element getDataBlockElement() {

		Element dataBlockElement =
				new Element(EventAttributeName.dataBlock.toString());

		Element nextEle =
				new Element(EventAttributeName.federationExecutionModelHandle
						.toString()).setText(federationExecutionModelHandle);

		dataBlockElement.addContent(nextEle);

		nextEle =
				new Element(EventAttributeName.federateRegistrationHandle.toString())
						.setText(getFederateRegistrationHandle());

		dataBlockElement.addContent(nextEle);

		nextEle =
				new Element(EventAttributeName.terminationReason.toString())
						.setText(terminationReason);

		dataBlockElement.addContent(nextEle);

		nextEle =
				new Element(EventAttributeName.federateName.toString())
						.setText(federateName);

		dataBlockElement.addContent(nextEle);

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
					.equalsIgnoreCase(EventAttributeName.federationExecutionModelHandle
							.toString())) {
				federationExecutionModelHandle = elementValue;
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.federateRegistrationHandle
							.toString())) {
				setFederateRegistrationHandle(elementValue);
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.terminationReason.toString())) {
				terminationReason = elementValue;
			} else if (elementName.equalsIgnoreCase(EventAttributeName.federateName
					.toString())) {
				federateName = elementValue;
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
		return EventClass.System.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.IEvent#getEventName()
	 */
	@Override
	public String getEventName() {
		return EventTypeEnum.FederateDeregistrationEvent.getDescription();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.IEvent#getEventType()
	 */
	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.FederateDeregistrationEvent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.AbstractEvent#getHandler()
	 */
	@Override
	public String getHandler() {
		return EventHandlerName.federateDeregistrationEventHandler.toString();
	}

	/**
	 * @return the terminationReason
	 */
	public final String getTerminationReason() {
		return terminationReason;
	}

	/**
	 * @param terminationReason
	 *          the terminationReason to set
	 */
	public final void setTerminationReason(String terminationReason) {
		this.terminationReason = terminationReason;
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
				prime * result + ((federateName == null) ? 0 : federateName.hashCode());
		result =
				prime
						* result
						+ ((federationExecutionModelHandle == null) ? 0
								: federationExecutionModelHandle.hashCode());
		result =
				prime * result
						+ ((terminationReason == null) ? 0 : terminationReason.hashCode());
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
		FederateDeregistrationEvent other = (FederateDeregistrationEvent) obj;
		if (federateName == null) {
			if (other.federateName != null) {
				return false;
			}
		} else if (!federateName.equals(other.federateName)) {
			return false;
		}
		if (federationExecutionModelHandle == null) {
			if (other.federationExecutionModelHandle != null) {
				return false;
			}
		} else if (!federationExecutionModelHandle
				.equals(other.federationExecutionModelHandle)) {
			return false;
		}
		if (terminationReason == null) {
			if (other.terminationReason != null) {
				return false;
			}
		} else if (!terminationReason.equals(other.terminationReason)) {
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
		return "FederateDeregistrationEvent [federateName=" + federateName
				+ ", federationExecutionModelHandle=" + federationExecutionModelHandle
				+ ", terminationReason=" + terminationReason + "]";
	}
}
