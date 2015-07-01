/**
 * 
 */
package com.csc.muthur.server.model.event.response;

import org.jdom.Element;

import com.csc.muthur.server.commons.EventTypeEnum;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.event.AbstractEvent;
import com.csc.muthur.server.model.event.EventAttributeName;
import com.csc.muthur.server.model.event.EventClass;
import com.csc.muthur.server.model.event.data.DataObjectFactory;
import com.csc.muthur.server.model.event.data.DataTypeEnum;
import com.csc.muthur.server.model.event.data.IBaseDataObject;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class ReadObjectResponse extends AbstractEvent {

	/**
	 * dataBlock attributes
	 */
	private DataTypeEnum dataType;
	private IBaseDataObject dataObject;

	/**
	 * 
	 */
	public ReadObjectResponse() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.model.event.AbstractEvent#initialization(java.lang.String)
	 */
	public void initialization(final String objectAsXML) throws MuthurException {
		super.initialization(objectAsXML);

		dataObject = DataObjectFactory.getDataObject(getDataType());

		if (dataObject != null) {
			dataObject.initialization(objectAsXML);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.AbstractEvent#getDataBlockElement()
	 */
	public Element getDataBlockElement() {

		Element dataBlockElement =
				new Element(EventAttributeName.dataBlock.toString());

		if (dataObject != null) {

			Element dataObjectAsXMLElement =
					new Element(EventAttributeName.dataObjectAsXML.toString());

			dataBlockElement.addContent(dataObjectAsXMLElement);

			// let the sub-class add the specific data elements for that class
			//

			dataObject.buildDataObjectAsXMLElement(dataObjectAsXMLElement);

			Element nextEle =
					new Element(EventAttributeName.dataType.toString()).setText(dataType
							.toString());

			dataBlockElement.addContent(nextEle);

		}

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
		return EventTypeEnum.ReadObjectResponse.toString();
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

			if (elementName.equalsIgnoreCase(EventAttributeName.dataType.toString())) {
				dataType = DataTypeEnum.valueOf(elementValue);
			}
		}

	}

	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.ReadObjectResponse;
	}

	/**
	 * @param dataObject
	 *          the dataObject to set
	 */
	public final void setDataObject(IBaseDataObject dataObject) {
		if (dataObject != null) {
			dataType = dataObject.getDataType();
			this.dataObject = dataObject;
		}
	}

	/**
	 * @return the dataObject
	 */
	public final IBaseDataObject getDataObject() {
		return dataObject;
	}

	/**
	 * @return the dataType
	 */
	public final DataTypeEnum getDataType() {
		return dataType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result =
				prime * result + ((dataObject == null) ? 0 : dataObject.hashCode());
		result = prime * result + ((dataType == null) ? 0 : dataType.hashCode());
		return result;
	}

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
		ReadObjectResponse other = (ReadObjectResponse) obj;
		if (dataObject == null) {
			if (other.dataObject != null) {
				return false;
			}
		} else if (!dataObject.equals(other.dataObject)) {
			return false;
		}
		if (dataType == null) {
			if (other.dataType != null) {
				return false;
			}
		} else if (!dataType.equals(other.dataType)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ReadObjectResponse [dataObject=" + dataObject + ", dataType="
				+ dataType + "]";
	}

}