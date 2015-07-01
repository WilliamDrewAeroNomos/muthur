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
public class FederateStateEvent extends AbstractEvent {

	/**
	 * dataBlock attributes
	 */
	private Integer federateState;
	private String federateName;

	/**
	 * 
	 */
	public FederateStateEvent() {
		super();
	}

	/**
	 * 
	 */
	public Element getDataBlockElement() {

		Element dataBlockElement =
				new Element(EventAttributeName.dataBlock.toString());

		if (federateState != null) {
			Element nextEle =
					new Element(EventAttributeName.federateState.toString())
							.setText(String.valueOf(getFederateState().intValue()));
			dataBlockElement.addContent(nextEle);
		}

		if ((federateName != null) && (!"".equalsIgnoreCase(federateName))) {
			Element nextEle =
					new Element(EventAttributeName.federateName.toString())
							.setText(federateName);
			dataBlockElement.addContent(nextEle);
		}

		return dataBlockElement;
	}

	/**
	 * 
	 */
	public String getEventTypeDescription() {
		return EventClass.System.toString();
	}

	@Override
	public String getEventName() {
		return EventTypeEnum.FederateStateEvent.toString();
	}

	@Override
	public void newElement(String elementName, String elementValue) {

		if ((elementValue != null) && (elementValue.length() > 0)) {

			if (elementName.equalsIgnoreCase(EventAttributeName.federateState
					.toString())) {
				federateState = Integer.valueOf(elementValue);
			} else if (elementName.equalsIgnoreCase(EventAttributeName.federateName
					.toString())) {
				federateName = elementValue;
			}
		}

	}

	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.FederateStateEvent;
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
	 * @return the federateState
	 */
	public final Integer getFederateState() {
		return federateState;
	}

	/**
	 * @param federateState
	 *          the federateState to set
	 */
	public final void setFederateState(Integer federateState) {
		this.federateState = federateState;
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
				prime * result
						+ ((federateState == null) ? 0 : federateState.hashCode());
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
		FederateStateEvent other = (FederateStateEvent) obj;
		if (federateName == null) {
			if (other.federateName != null) {
				return false;
			}
		} else if (!federateName.equals(other.federateName)) {
			return false;
		}
		if (federateState == null) {
			if (other.federateState != null) {
				return false;
			}
		} else if (!federateState.equals(other.federateState)) {
			return false;
		}
		return true;
	}

}
