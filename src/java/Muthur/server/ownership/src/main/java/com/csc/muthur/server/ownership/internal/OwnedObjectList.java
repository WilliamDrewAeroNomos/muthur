/**
 * 
 */
package com.csc.muthur.server.ownership.internal;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.AccessControl;
import com.csc.muthur.server.model.event.data.IBaseDataObject;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public class OwnedObjectList {

	private static final Logger LOG =
			LoggerFactory.getLogger(OwnedObjectList.class.getName());

	/**
	 * Maps each object by UUID to a {@link ObjectAttributeAccessControl} which
	 * contains each field or attribute for the object and it's access control.
	 */
	private Map<String, ObjectAttributeAccessControl> objUUIDToAttributeAccessCtrlList =
			new ConcurrentHashMap<String, ObjectAttributeAccessControl>();

	/**
	 * 
	 */
	public OwnedObjectList() {
	}

	/**
	 * 
	 * @param objectUUID
	 * @return
	 */
	public IBaseDataObject getOwnedObject(final String objectUUID) {

		IBaseDataObject bdo = null;

		if ((objectUUID != null) && (!"".equalsIgnoreCase(objectUUID))) {

			for (ObjectAttributeAccessControl oacl : objUUIDToAttributeAccessCtrlList
					.values()) {
				if (oacl != null) {
					if (oacl.getObjectUUID().equalsIgnoreCase(objectUUID)) {
						bdo = oacl.getBaseDataObject();
						break;
					}
				}
			}

		}

		return bdo;
	}

	/**
	 * 
	 * @return Set of UUIDs for all owned objects
	 */
	public Set<String> getOwnedObjects() {
		return objUUIDToAttributeAccessCtrlList.keySet();
	}

	/**
	 * 
	 * @param objectUUID
	 * @return
	 */
	public boolean containsObject(final String objectUUID) {
		boolean contains = false;
		if ((objectUUID != null) && (!"".equalsIgnoreCase(objectUUID))) {
			contains = objUUIDToAttributeAccessCtrlList.containsKey(objectUUID);
		}
		return contains;
	}

	/**
	 * 
	 * @param objectUUID
	 * @return
	 */
	public Set<String> getMutableAttributeNames(final String objectUUID) {

		Set<String> mutableAttributeNames = null;

		if ((objectUUID != null) && (!"".equalsIgnoreCase(objectUUID))) {
			ObjectAttributeAccessControl objectAttributeAccessControl =
					objUUIDToAttributeAccessCtrlList.get(objectUUID);
			if (objectAttributeAccessControl != null) {
				mutableAttributeNames =
						objectAttributeAccessControl.getMutableAttributeNames();
			}
		}

		return mutableAttributeNames;
	}

	/**
	 * 
	 * @return
	 */
	public Collection<ObjectAttributeAccessControl> getObjectAttributeAccessControls() {
		return objUUIDToAttributeAccessCtrlList.values();
	}

	/**
	 * 
	 * @param objectUUID
	 * @throws MuthurException
	 */
	public void addObject(final IBaseDataObject baseDataObject)
			throws MuthurException {

		if (baseDataObject == null) {
			throw new IllegalArgumentException("IBaseDataObject was null.");
		}

		if ((baseDataObject.getDataObjectUUID() == null)
				|| ("".equalsIgnoreCase(baseDataObject.getDataObjectUUID()))) {
			throw new IllegalArgumentException("Object UUID was null.");
		}

		ObjectAttributeAccessControl objAttribAccessCtrl =
				new ObjectAttributeAccessControl(baseDataObject);

		objUUIDToAttributeAccessCtrlList.put(baseDataObject.getDataObjectUUID(),
				objAttribAccessCtrl);

		LOG.debug("Added access controls for [" + baseDataObject + "]");

	}

	/**
	 * 
	 * @param objectUUID
	 * @return
	 */
	public ObjectAttributeAccessControl getObjectAttributeAccessControl(
			final String objectUUID) {

		ObjectAttributeAccessControl objectAttributeAccessControl = null;

		if ((objectUUID != null) && (!"".equalsIgnoreCase(objectUUID))) {
			objectAttributeAccessControl =
					objUUIDToAttributeAccessCtrlList.get(objectUUID);
		}

		return objectAttributeAccessControl;
	}

	/**
	 * 
	 * @param objectUUID
	 * @param attributeName
	 * @return
	 */
	public AccessControl getAttributeAccessControl(final String objectUUID,
			final String attributeName) {

		AccessControl accessControl = null;

		if ((objectUUID != null) && (!"".equalsIgnoreCase(objectUUID))
				&& (attributeName != null) && (!"".equalsIgnoreCase(attributeName))) {

			ObjectAttributeAccessControl objectAttributeAccessControl =
					objUUIDToAttributeAccessCtrlList.get(objectUUID);

			if (objectAttributeAccessControl != null) {
				accessControl =
						objectAttributeAccessControl
								.getAttributeAccessControl(attributeName);
			}
		}

		return accessControl;

	}

	/**
	 * Removes the specified object from the list of owned objects.
	 * 
	 * @param objectUUID
	 * @return True if the object was removed from the list or false if the object
	 *         UUID was not found.
	 */
	public ObjectAttributeAccessControl removeObject(final String objectUUID) {

		ObjectAttributeAccessControl objectAttributeAccessControl = null;

		if ((objectUUID != null) && (!"".equalsIgnoreCase(objectUUID))) {

			objectAttributeAccessControl =
					objUUIDToAttributeAccessCtrlList.remove(objectUUID);

			if (objectAttributeAccessControl != null) {
				LOG.debug("Removed ["
						+ objectAttributeAccessControl.getBaseDataObject()
						+ "] from owned objects.");
			}
		}

		return objectAttributeAccessControl;
	}

	/**
	 * Updates the {@link AccessControl} on a list of fields or attributes for the
	 * specified object.
	 * 
	 * @param objectUUID
	 *          Unique ID for an object.
	 * @param fieldNameToAccessControlMap
	 *          Map of fields or attribute names and the {@link AccessControl} to
	 *          which they will be updated or set.
	 * @return
	 */
	public boolean updateAttributeAccessControl(final String objectUUID,
			final Map<String, AccessControl> fieldNameToAccessControlMap) {

		ObjectAttributeAccessControl objectAttributeAccessControlToUpdate = null;

		if ((objectUUID != null)
				&& (!"".equalsIgnoreCase(objectUUID) && (fieldNameToAccessControlMap != null))) {

			objectAttributeAccessControlToUpdate =
					objUUIDToAttributeAccessCtrlList.get(objectUUID);

			if (objectAttributeAccessControlToUpdate != null) {

				objectAttributeAccessControlToUpdate
						.updateAttributeAccessControls(fieldNameToAccessControlMap);

				LOG.debug("Updated access controls for UUID [" + objectUUID + "]");
			}

		}

		return (objectAttributeAccessControlToUpdate != null);
	}

	/**
	 * Clears the list of {@link ObjectAttributeAccessControl}s and the list of
	 * {@link AccessControl}s and data object in each {@link AccessControl}.
	 * 
	 */
	public void clear() {

		if (objUUIDToAttributeAccessCtrlList != null) {

			LOG.debug("Clearing out owned object list...");

			for (ObjectAttributeAccessControl oaac : objUUIDToAttributeAccessCtrlList
					.values()) {
				if (oaac != null) {
					oaac.clear();
					LOG.debug("Cleared out object access controls.");
				}
			}
			objUUIDToAttributeAccessCtrlList.clear();

			LOG.debug("Cleared out owned object list.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OwnedObjectList [objUUIDToAttributeAccessCtrlList="
				+ objUUIDToAttributeAccessCtrlList + "]";
	}
}
