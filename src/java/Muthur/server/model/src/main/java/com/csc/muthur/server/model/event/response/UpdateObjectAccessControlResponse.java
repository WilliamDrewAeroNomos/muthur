/**
 * 
 */
package com.csc.muthur.server.model.event.response;

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
public class UpdateObjectAccessControlResponse extends AbstractEvent {

	/**
	 * dataBlock attributes
	 */
	private String dataObjectUUID; // id of the affected object

	/**
	 * 
	 */
	public UpdateObjectAccessControlResponse() {
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
	 * @see com.csc.muthur.server.model.event.IEvent#getEventTypeDescription()
	 */
	@Override
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
		return EventTypeEnum.UpdateObjectAccessControlResponse.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.AbstractEvent#getHandler()
	 */
	@Override
	public String getHandler() {
		return EventHandlerName.updateObjectAccessControlResponseHandler.toString();
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

	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.UpdateObjectAccessControlResponse;
	}

	/**
	 * @return the dataObjectUUID
	 */
	public String getDataObjectUUID() {
		return dataObjectUUID;
	}

	/**
	 * @param dataObjectUUID
	 *          the dataObjectUUID to set
	 */
	public void setDataObjectUUID(String dataObjectUUID) {
		this.dataObjectUUID = dataObjectUUID;
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
		UpdateObjectAccessControlResponse other =
				(UpdateObjectAccessControlResponse) obj;
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
		return "UpdateObjectAccessControlResponse [dataObjectUUID="
				+ dataObjectUUID + "]";
	}

}
