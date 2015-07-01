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
public class ObjectOwnershipRelinquishedEvent extends AbstractEvent {

	/**
	 * dataBlock attributes
	 */
	private String dataObjectUUID;

	/**
	 * 
	 */
	public ObjectOwnershipRelinquishedEvent() {
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
				new Element(EventAttributeName.dataObjectUUID.toString())
						.setText(dataObjectUUID);

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

			if (elementName.equalsIgnoreCase(EventAttributeName.dataObjectUUID
					.toString())) {
				dataObjectUUID = elementValue;
			}
		}

	}

	/**
	 * 
	 * @return
	 */
	public String getDataObjectUUID() {
		return dataObjectUUID;
	}

	/**
	 * 
	 * @param dataObjectUUID
	 */
	public void setDataObjectUUID(String dataObjectUUID) {
		this.dataObjectUUID = dataObjectUUID;
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
		return EventTypeEnum.ObjectOwnershipRelinquishedEvent.getDescription();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.IEvent#getEventType()
	 */
	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.ObjectOwnershipRelinquishedEvent;
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
				prime * result
						+ ((dataObjectUUID == null) ? 0 : dataObjectUUID.hashCode());
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
		ObjectOwnershipRelinquishedEvent other =
				(ObjectOwnershipRelinquishedEvent) obj;
		if (dataObjectUUID == null) {
			if (other.dataObjectUUID != null) {
				return false;
			}
		} else if (!dataObjectUUID.equals(other.dataObjectUUID)) {
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
		return "ObjectOwnershipRelinquishedEvent [dataObjectUUID=" + dataObjectUUID
				+ super.toString() + "]";
	}

}
