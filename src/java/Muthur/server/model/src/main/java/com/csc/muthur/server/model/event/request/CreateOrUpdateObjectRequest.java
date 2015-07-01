/**
 * 
 */
package com.csc.muthur.server.model.event.request;

import org.jdom.Element;

import com.csc.muthur.server.commons.EventTypeEnum;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.event.AbstractEvent;
import com.csc.muthur.server.model.event.EventAttributeName;
import com.csc.muthur.server.model.event.EventClass;
import com.csc.muthur.server.model.event.EventHandlerName;
import com.csc.muthur.server.model.event.data.DataObjectFactory;
import com.csc.muthur.server.model.event.data.DataTypeEnum;
import com.csc.muthur.server.model.event.data.IBaseDataObject;

/**
 * This request type will either create an object if it doesn't already exist or
 * update an existing object.
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class CreateOrUpdateObjectRequest extends AbstractEvent {

	/**
	 * dataBlock attributes
	 */
	private DataTypeEnum dataType;
	private IBaseDataObject dataObject;

	/**
	 * 
	 */
	public CreateOrUpdateObjectRequest() {
		super();
	}

	/**
	 * @param objectAsXML
	 */
	public void initialization(final String objectAsXML) throws MuthurException {
		super.initialization(objectAsXML);

		dataObject = DataObjectFactory.getDataObject(getDataType());

		if (dataObject == null) {
			throw new MuthurException(
					"DataObjectFactory was unable to construct a data object using ["
							+ getDataType().name() + "]");
		}

		dataObject.initialization(objectAsXML);

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

		}

		Element nextEle =
				new Element(EventAttributeName.dataType.toString()).setText(dataType
						.toString());

		dataBlockElement.addContent(nextEle);

		nextEle =
				new Element(
						EventAttributeName.federationExecutionModelHandle.toString())
						.setText(getFederationExecutionModelHandle());

		dataBlockElement.addContent(nextEle);

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

			if (elementName.equalsIgnoreCase(EventAttributeName.dataType.toString())) {
				dataType = DataTypeEnum.valueOf(elementValue);
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.federationExecutionModelHandle
							.toString())) {
				setFederationExecutionModelHandle(elementValue);
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
		return EventTypeEnum.CreateOrUpdateObjectRequest.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.AbstractEvent#getHandler()
	 */
	@Override
	public String getHandler() {
		return EventHandlerName.createOrUpdateObjectRequestHandler.toString();
	}

	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.CreateOrUpdateObjectRequest;
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
				prime * result + ((dataObject == null) ? 0 : dataObject.hashCode());
		result = prime * result + ((dataType == null) ? 0 : dataType.hashCode());
		result =
				prime
						* result
						+ ((getFederationExecutionModelHandle() == null) ? 0
								: getFederationExecutionModelHandle().hashCode());
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
		CreateOrUpdateObjectRequest other = (CreateOrUpdateObjectRequest) obj;
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
		if (getFederationExecutionModelHandle() == null) {
			if (other.getFederationExecutionModelHandle() != null) {
				return false;
			}
		} else if (!getFederationExecutionModelHandle().equals(
				other.getFederationExecutionModelHandle())) {
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
		return "CreateOrUpdateObjectRequest [dataObject=" + dataObject
				+ ", dataType=" + dataType + ", federationExecutionModelHandle="
				+ getFederationExecutionModelHandle() + super.toString() + "]";
	}

}
