/**
 * 
 */
package com.csc.muthur.server.model.event.request;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jdom.Element;

import com.csc.muthur.server.commons.EventTypeEnum;
import com.csc.muthur.server.model.AccessControl;
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
public class UpdateObjectAccessControlRequest extends AbstractEvent {

	private Map<String, AccessControl> attributeNameToAccessControlMap =
			new ConcurrentHashMap<String, AccessControl>();

	private String dataObjectUUID; // id of the targeted object

	@SuppressWarnings("unused")
	// used to signal start of attribute access control element processing
	private boolean processingAttributeAccessControlUpdateElement;
	private String nextAttributeName;
	private AccessControl nextAccessControl;

	/**
	 * 
	 */
	public UpdateObjectAccessControlRequest() {
		super();
	}

	/**
	 * Overridden and must call super.initialization()
	 */
	public void initialization() {
		super.initialization();

		if (attributeNameToAccessControlMap == null) {
			attributeNameToAccessControlMap =
					new ConcurrentHashMap<String, AccessControl>();
		}

		attributeNameToAccessControlMap.clear();

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
					new Element(EventAttributeName.dataObjectUUID.toString())
							.setText(dataObjectUUID);

			dataBlockElement.addContent(nextEle);

			nextEle =
					new Element(EventAttributeName.federationExecutionModelHandle
							.toString()).setText(getFederationExecutionModelHandle());

			dataBlockElement.addContent(nextEle);

			Element attributeAccessControlUpdatesElement =
					new Element(EventAttributeName.attributeAccessControlUpdates
							.toString());

			dataBlockElement.addContent(attributeAccessControlUpdatesElement);

			if (attributeNameToAccessControlMap != null) {

				for (String attributeName : attributeNameToAccessControlMap.keySet()) {

					if (attributeName != null) {

						Element attributeAccessControlUpdateElement =
								new Element(EventAttributeName.attributeAccessControlUpdate
										.toString());

						attributeAccessControlUpdatesElement
								.addContent(attributeAccessControlUpdateElement);

						Element nextElement =
								new Element(EventAttributeName.attributeName.toString())
										.setText(attributeName);

						attributeAccessControlUpdateElement.addContent(nextElement);

						AccessControl accessControl =
								attributeNameToAccessControlMap.get(attributeName);

						// add the access control element
						//
						if (accessControl != null) {

							nextElement =
									new Element(EventAttributeName.accessControl.toString())
											.setText(accessControl.toString());

							attributeAccessControlUpdateElement.addContent(nextElement);
						}
					}
				}
			}
		}

		return dataBlockElement;
	}

	@Override
	public void newElement(String elementName, String elementValue) {

		if ((elementValue != null) && (elementValue.length() > 0)) {

			if (elementName
					.equalsIgnoreCase(EventAttributeName.federationExecutionModelHandle
							.toString())) {
				setFederationExecutionModelHandle(elementValue);
			} else if (elementName.equalsIgnoreCase(EventAttributeName.dataObjectUUID
					.toString())) {
				this.dataObjectUUID = elementValue;
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.attributeAccessControlUpdate
							.toString())) {
				processingAttributeAccessControlUpdateElement = true;
			} else if (elementName.equalsIgnoreCase(EventAttributeName.attributeName
					.toString())) {
				nextAttributeName = elementValue;
			} else if (elementName.equalsIgnoreCase(EventAttributeName.accessControl
					.toString())) {
				nextAccessControl = AccessControl.valueOf(elementValue);
			}

		}
	}

	@Override
	public String getEventTypeDescription() {
		return EventClass.Request.toString();
	}

	@Override
	public String getEventName() {
		return EventTypeEnum.UpdateObjectAccessControlRequest.toString();
	}

	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.UpdateObjectAccessControlRequest;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.AbstractEvent#getHandler()
	 */
	@Override
	public String getHandler() {
		return EventHandlerName.updateObjectAccessControlHandler.toString();
	}

	/**
	 * @return the dataObjectUUID
	 */
	public final String getDataObjectUUID() {
		return dataObjectUUID;
	}

	/**
	 * @param dataObjectUUID
	 *          the dataObjectUUID to set
	 */
	public final void setDataObjectUUID(String dataObjectUUID) {
		this.dataObjectUUID = dataObjectUUID;
	}

	/**
	 * @param attributeNameToAccessControlMap
	 *          the attributeNameToAccessControlMap to set
	 */
	public final void setAttributeNameToAccessControlMap(
			Map<String, AccessControl> attributeNameToAccessControlMap) {
		this.attributeNameToAccessControlMap = attributeNameToAccessControlMap;
	}

	/**
	 * @return the attributeNameToAccessControlMap
	 */
	public final Map<String, AccessControl> getAttributeNameToAccessControlMap() {
		return attributeNameToAccessControlMap;
	}

	/**
	 * Adds an entry to the map of attribute name and {@link AccessControl} that
	 * will be applied against a currently owned object.
	 * 
	 * @param attributeName
	 * @param accessControlUpdate
	 */
	public final void addAttributeAccessControlUpdate(final String attributeName,
			final AccessControl accessControlUpdate) {

		if ((attributeNameToAccessControlMap != null) && (attributeName != null)
				&& (attributeName.length() > 0) && (accessControlUpdate != null)) {
			attributeNameToAccessControlMap.put(attributeName, accessControlUpdate);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.model.event.AbstractEvent#endOfElement(java.lang.String)
	 */
	@Override
	public void endOfElement(String currentElementName) {

		if ((currentElementName != null) && (currentElementName.length() > 0)) {

			if (currentElementName
					.equalsIgnoreCase(EventAttributeName.attributeAccessControlUpdate
							.toString())) {
				attributeNameToAccessControlMap.put(nextAttributeName,
						nextAccessControl);
				processingAttributeAccessControlUpdateElement = false;
			}
		}
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
						+ ((attributeNameToAccessControlMap == null) ? 0
								: attributeNameToAccessControlMap.hashCode());
		result =
				prime * result
						+ ((dataObjectUUID == null) ? 0 : dataObjectUUID.hashCode());
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
		UpdateObjectAccessControlRequest other =
				(UpdateObjectAccessControlRequest) obj;
		if (attributeNameToAccessControlMap == null) {
			if (other.attributeNameToAccessControlMap != null) {
				return false;
			}
		} else if (!attributeNameToAccessControlMap
				.equals(other.attributeNameToAccessControlMap)) {
			return false;
		}
		if (dataObjectUUID == null) {
			if (other.dataObjectUUID != null) {
				return false;
			}
		} else if (!dataObjectUUID.equals(other.dataObjectUUID)) {
			return false;
		}
		if (getFederationExecutionModelHandle() == null) {
			if (other.getFederationExecutionModelHandle() != null) {
				return false;
			}
		} else if (!getFederationExecutionModelHandle()
				.equals(other.getFederationExecutionModelHandle())) {
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
		return "UpdateObjectAccessControlRequest [attributeNameToAccessControlMap="
				+ attributeNameToAccessControlMap + ", dataObjectUUID="
				+ dataObjectUUID + ", federationExecutionModelHandle="
				+ getFederationExecutionModelHandle() + "]";
	}

}
