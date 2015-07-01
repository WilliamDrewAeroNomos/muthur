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
public class ListFedExecModelsRequest extends AbstractEvent {

	private String federateRegistrationHandle;
	private String groupName;

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName
	 *          the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * 
	 * @param timeToLiveSecs
	 */
	public ListFedExecModelsRequest() {
		super();
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
				+ ((federateRegistrationHandle == null) ? 0
						: federateRegistrationHandle.hashCode());
		result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
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
		ListFedExecModelsRequest other = (ListFedExecModelsRequest) obj;
		if (federateRegistrationHandle == null) {
			if (other.federateRegistrationHandle != null)
				return false;
		} else if (!federateRegistrationHandle
				.equals(other.federateRegistrationHandle))
			return false;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		return true;
	}

	/**
	 * 
	 */
	public Element getDataBlockElement() {

		Element dataBlockElement = null;

		if (isSuccess()) {

			dataBlockElement = new Element(EventAttributeName.dataBlock.toString());

			Element nextEle = new Element(EventAttributeName.groupName.toString())
					.setText(getGroupName());

			dataBlockElement.addContent(nextEle);
		}

		return dataBlockElement;
	}

	@Override
	public String getEventTypeDescription() {
		return EventClass.Request.toString();
	}

	@Override
	public String getEventName() {
		return EventTypeEnum.ListFedExecModelsRequest.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ListFedExecModelsRequest [federateRegistrationHandle=")
				.append(federateRegistrationHandle).append(", groupName=")
				.append(groupName).append("]");
		return builder.toString();
	}

	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.ListFedExecModelsRequest;
	}

	@Override
	public void newElement(String elementName, String elementValue) {

		if ((elementValue != null) && (elementValue.length() > 0)) {

			if (elementName.equalsIgnoreCase(EventAttributeName.groupName.toString())) {
				groupName = elementValue;
			}

		}

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.AbstractEvent#getHandler()
	 */
	@Override
	public String getHandler() {
		return EventHandlerName.listFedExecModelsRequestHandler.toString();
	}
}
