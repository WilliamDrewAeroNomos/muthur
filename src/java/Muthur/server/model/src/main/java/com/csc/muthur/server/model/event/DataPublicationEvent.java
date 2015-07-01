/**
 * 
 */
package com.csc.muthur.server.model.event;

import org.jdom.Element;

import com.csc.muthur.server.commons.EventTypeEnum;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.DataAction;
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
public class DataPublicationEvent extends AbstractEvent {

	/**
	 * dataBlock attributes
	 */
	private DataTypeEnum dataType;
	private DataAction dataAction;
	private String federationExecutionModelHandle;
	private IBaseDataObject dataObject;

	/**
	 * 
	 */
	public DataPublicationEvent() {
		super();
	}

	public void initialization(final String objectAsXML) throws MuthurException {
		super.initialization(objectAsXML);

		dataObject = DataObjectFactory.getDataObject(getDataType());

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

		Element nextEle =
				new Element(EventAttributeName.dataType.toString()).setText(dataType
						.toString());

		dataBlockElement.addContent(nextEle);

		nextEle =
				new Element(EventAttributeName.dataAction.toString())
						.setText(dataAction.toString());

		dataBlockElement.addContent(nextEle);

		nextEle =
				new Element(
						EventAttributeName.federationExecutionModelHandle.toString())
						.setText(federationExecutionModelHandle);

		dataBlockElement.addContent(nextEle);

		if (dataObject != null) {

			Element dataObjectAsXMLElement =
					new Element(EventAttributeName.dataObjectAsXML.toString());

			dataBlockElement.addContent(dataObjectAsXMLElement);

			// let the sub-class add the specific data elements for that class
			//

			dataObject.buildDataObjectAsXMLElement(dataObjectAsXMLElement);

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
		return EventClass.Data.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.IEvent#getEventName()
	 */
	@Override
	public String getEventName() {
		return EventTypeEnum.DataPublication.toString();
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
					.equalsIgnoreCase(EventAttributeName.dataAction.toString())) {
				dataAction = DataAction.valueOf(elementValue);
			} else if (elementName.equalsIgnoreCase(EventAttributeName.dataType
					.toString())) {
				dataType = DataTypeEnum.valueOf(elementValue);
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.federationExecutionModelHandle
							.toString())) {
				federationExecutionModelHandle = elementValue;
			}
		}

	}

	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.DataPublication;
	}

	/**
	 * @return the dataType
	 */
	public final DataTypeEnum getDataType() {
		return dataType;
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
	 * @return the dataAction
	 */
	public final DataAction getDataAction() {
		return dataAction;
	}

	/**
	 * @param dataAction
	 *          the dataAction to set
	 */
	public final void setDataAction(DataAction dataAction) {
		this.dataAction = dataAction;
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
		result = prime * result + ((dataType == null) ? 0 : dataType.hashCode());
		result =
				prime
						* result
						+ ((federationExecutionModelHandle == null) ? 0
								: federationExecutionModelHandle.hashCode());
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
		DataPublicationEvent other = (DataPublicationEvent) obj;
		if (dataType != other.dataType) {
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

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DataPublicationEvent [dataType=" + dataType
				+ ", federationExecutionModelHandle=" + federationExecutionModelHandle
				+ ", dataObject=" + dataObject + "]";
	}

}
