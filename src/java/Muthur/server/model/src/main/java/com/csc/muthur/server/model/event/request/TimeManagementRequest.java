/**
 * 
 */
package com.csc.muthur.server.model.event.request;

import org.jdom.Element;

import com.csc.muthur.server.commons.EventTypeEnum;
import com.csc.muthur.server.model.FederationExecutionDirective;
import com.csc.muthur.server.model.FederationExecutionState;
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
public class TimeManagementRequest extends AbstractEvent {

	private long timeIntervalMSecs;
	private FederationExecutionState federationExecutionState = FederationExecutionState.UNDEFINED;
	private FederationExecutionDirective federationExecutionDirective = FederationExecutionDirective.UNDEFINED;

	/**
	 * 
	 */
	public TimeManagementRequest() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.AbstractEvent#initialization()
	 */
	@Override
	public void initialization() {

		if (this.federationExecutionState == null) {
			federationExecutionState = FederationExecutionState.UNDEFINED;
		}

		if (this.federationExecutionDirective == null) {
			federationExecutionDirective = FederationExecutionDirective.UNDEFINED;
		}

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

			Element nextEle = new Element(EventAttributeName.timeIntervalMSecs
					.toString()).setText(Long.toString(timeIntervalMSecs));

			dataBlockElement.addContent(nextEle);

			if (federationExecutionState == null) {
				federationExecutionState = FederationExecutionState.UNDEFINED;
			}

			nextEle = new Element(EventAttributeName.federationExecutionState
					.toString()).setText(Integer.toString(federationExecutionState
					.getIdentifier()));

			dataBlockElement.addContent(nextEle);

			if (federationExecutionDirective == null) {
				federationExecutionDirective = FederationExecutionDirective.UNDEFINED;
			}

			nextEle = new Element(EventAttributeName.federationExecutionDirective
					.toString()).setText(Integer.toString(federationExecutionDirective
					.getIdentifier()));

			dataBlockElement.addContent(nextEle);

		}

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

			if (elementName.equalsIgnoreCase(EventAttributeName.timeIntervalMSecs
					.toString())) {
				timeIntervalMSecs = Long.valueOf(elementValue);
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.federationExecutionState
							.toString())) {
				int tempInt = Integer.valueOf(elementValue);
				federationExecutionState = FederationExecutionState.getType(tempInt);
				if (federationExecutionState == null) {
					federationExecutionState = FederationExecutionState.UNDEFINED;
				}
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.federationExecutionDirective
							.toString())) {
				int tempInt = Integer.valueOf(elementValue);
				federationExecutionDirective = FederationExecutionDirective
						.getType(tempInt);
				if (federationExecutionDirective == null) {
					federationExecutionDirective = FederationExecutionDirective.UNDEFINED;
				}
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
		return EventTypeEnum.TimeManagementRequest.getDescription();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.IEvent#getEventType()
	 */
	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.TimeManagementRequest;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.AbstractEvent#getHandler()
	 */
	@Override
	public String getHandler() {
		return EventHandlerName.defaultEventHandler.toString();
	}

	/**
	 * @return the timeIntervalMSecs
	 */
	public final long getTimeIntervalMSecs() {
		return timeIntervalMSecs;
	}

	/**
	 * @param timeIntervalMSecs
	 *          the timeIntervalMSecs to set
	 */
	public final void setTimeIntervalMSecs(long timeIntervalMSecs) {
		this.timeIntervalMSecs = timeIntervalMSecs;
	}

	/**
	 * @param federationExecutionState
	 *          the federationExecutionState to set
	 */
	public void setFederationExecutionState(
			FederationExecutionState federationExecutionState) {
		this.federationExecutionState = federationExecutionState;
	}

	/**
	 * @return the federationExecutionState
	 */
	public FederationExecutionState getFederationExecutionState() {
		return federationExecutionState;
	}

	/**
	 * @param executionDirective
	 *          the executionDirective to set
	 */
	public void setFederationExecutionDirective(
			FederationExecutionDirective federationExecutionDirective) {
		this.federationExecutionDirective = federationExecutionDirective;
	}

	/**
	 * 
	 * @return
	 */
	public FederationExecutionDirective getFederationExecutionDirective() {
		return federationExecutionDirective;
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
				+ ((federationExecutionDirective == null) ? 0
						: federationExecutionDirective.hashCode());
		result = prime
				* result
				+ ((federationExecutionState == null) ? 0 : federationExecutionState
						.hashCode());
		result = prime * result
				+ (int) (timeIntervalMSecs ^ (timeIntervalMSecs >>> 32));
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
		TimeManagementRequest other = (TimeManagementRequest) obj;
		if (federationExecutionDirective != other.federationExecutionDirective) {
			return false;
		}
		if (federationExecutionState != other.federationExecutionState) {
			return false;
		}
		if (timeIntervalMSecs != other.timeIntervalMSecs) {
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
		return "TimeManagementRequest [timeIntervalMSecs=" + timeIntervalMSecs
				+ ", federationExecutionState=" + federationExecutionState
				+ ", federationExecutionDirective=" + federationExecutionDirective
				+ "]";
	}

}
