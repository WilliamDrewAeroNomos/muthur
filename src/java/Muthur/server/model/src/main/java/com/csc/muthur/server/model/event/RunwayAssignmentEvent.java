/**
 * 
 */
package com.csc.muthur.server.model.event;

import org.jdom.Element;

import com.csc.muthur.server.commons.EventTypeEnum;
import com.csc.muthur.server.model.event.data.RunwayFlow;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class RunwayAssignmentEvent extends AbstractEvent {

	private String runwayName;
	private RunwayFlow runwayFlow = RunwayFlow.UNDEFINED;

	/**
	 * 
	 */
	public RunwayAssignmentEvent() {
		super();
	}

	/**
	 * @return the runwayName
	 */
	public String getRunwayName() {
		return runwayName;
	}

	/**
	 * @param runwayName
	 *          the runwayName to set
	 */
	public void setRunwayName(String runwayName) {
		this.runwayName = runwayName;
	}

	/**
	 * @return the runwayFlow
	 */
	public RunwayFlow getRunwayFlow() {
		return runwayFlow;
	}

	/**
	 * @param runwayFlow
	 *          the runwayFlow to set
	 */
	public void setRunwayFlow(RunwayFlow runwayFlow) {
		this.runwayFlow = runwayFlow;
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
					new Element(EventAttributeName.runwayName.toString())
							.setText(runwayName);

			dataBlockElement.addContent(nextEle);

			nextEle =
					new Element(EventAttributeName.runwayFlow.toString())
							.setText(runwayFlow.toString());

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

			if (elementName
					.equalsIgnoreCase(EventAttributeName.runwayName.toString())) {
				runwayName = elementValue;
			} else if (elementName.equalsIgnoreCase(EventAttributeName.runwayFlow
					.toString())) {
				runwayFlow = RunwayFlow.valueOf(elementValue);
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
		return EventTypeEnum.RunwayAssignmentEvent.getDescription();
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
		return EventHandlerName.runwayAssignmentEventHandler.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result =
				prime * result + ((runwayFlow == null) ? 0 : runwayFlow.hashCode());
		result =
				prime * result + ((runwayName == null) ? 0 : runwayName.hashCode());
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
		RunwayAssignmentEvent other = (RunwayAssignmentEvent) obj;
		if (runwayFlow != other.runwayFlow) {
			return false;
		}
		if (runwayName == null) {
			if (other.runwayName != null) {
				return false;
			}
		} else if (!runwayName.equals(other.runwayName)) {
			return false;
		}
		return true;
	}

}
