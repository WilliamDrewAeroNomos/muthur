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
public class FederationTerminationEvent extends AbstractEvent {

	private String terminationReason;

	/**
	 * 
	 */
	public FederationTerminationEvent() {
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
					new Element(EventAttributeName.terminationReason.toString())
							.setText(terminationReason);

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
					.equalsIgnoreCase(EventAttributeName.federationExecutionModelHandle
							.toString())) {
				setFederationExecutionModelHandle(elementValue);
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.terminationReason.toString())) {
				terminationReason = elementValue;
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
		return EventTypeEnum.FederationTermination.getDescription();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.IEvent#getEventType()
	 */
	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.FederationTermination;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.AbstractEvent#getHandler()
	 */
	@Override
	public String getHandler() {
		return EventHandlerName.terminateFederationEventHandler.toString();
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
						+ ((getFederationExecutionModelHandle() == null) ? 0
								: getFederationExecutionModelHandle().hashCode());
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
		FederationTerminationEvent other = (FederationTerminationEvent) obj;
		if (getFederationExecutionModelHandle() == null) {
			if (other.getFederationExecutionModelHandle() != null) {
				return false;
			}
		} else if (!getFederationExecutionModelHandle().equals(
				other.getFederationExecutionModelHandle())) {
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

}
