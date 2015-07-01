package com.csc.muthur.server.ownership.internal;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
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
 * 
 */
public class ObjectAttributeAccessControl {

	private static final Logger LOG = LoggerFactory
			.getLogger(ObjectAttributeAccessControl.class.getName());

	private String objectUUID;
	private IBaseDataObject baseDataObject;

	/**
	 * Maps a field name from the {@link #baseDataObject} to an AccessControl
	 */
	private Map<String, AccessControl> fieldNameToAccessControlMap = new ConcurrentHashMap<String, AccessControl>();

	/**
	 * @param baseDataObject
	 * @throws MuthurException
	 */
	public ObjectAttributeAccessControl(final IBaseDataObject baseDataObject)
			throws MuthurException {
		super();

		// check parms
		//
		if (baseDataObject == null) {
			throw new IllegalArgumentException("IBaseDataObject was null");
		}

		if (baseDataObject.getDataObjectUUID() == null) {
			throw new IllegalArgumentException("Data object UUID was null");
		}

		this.baseDataObject = baseDataObject;
		this.objectUUID = baseDataObject.getDataObjectUUID();

		// get the class

		Class<?> clazz = null;

		try {
			clazz = Class.forName(baseDataObject.getClass().getName());
		} catch (ClassNotFoundException e) {
			throw new MuthurException(e);
		}

		if (clazz != null) {

			// will contain the list of all Fields in the entire object graph

			List<Field> ll = new LinkedList<Field>();

			getAllFields(ll, clazz);

			Iterator<Field> iter = ll.iterator();

			// add each Field to the map defaulted to AccessControl.READ

			while (iter.hasNext()) {
				Field f = iter.next();

				if (f != null) {
					AccessControl ac = null;
					// TODO: THIS IS A HACK TO GET AROUND USING THE UPDATE ATTRIBUTE
					// ACCESS
					// CONTROL API
					if (f.getName().equalsIgnoreCase("routePlan")) {
						ac = AccessControl.READ_WRITE;
					} else {
						ac = AccessControl.READ_ONLY;
					}
					fieldNameToAccessControlMap.put(f.getName(), ac);
					LOG.debug("Field [" + f.getName() + "] set to [" + ac + "]");
				}
			}
		}

	}

	/**
	 * 
	 * @param updatefieldNameToAccessControlMap
	 */
	public void updateAttributeAccessControls(
			final Map<String, AccessControl> updatefieldNameToAccessControlMap) {

		for (String fieldNameToUpdate : updatefieldNameToAccessControlMap.keySet()) {

			if ((fieldNameToUpdate != null)
					&& (!"".equalsIgnoreCase(fieldNameToUpdate))) {

				if (fieldNameToAccessControlMap.containsKey(fieldNameToUpdate)) {

					AccessControl newAccessControl = updatefieldNameToAccessControlMap
							.get(fieldNameToUpdate);

					if (newAccessControl != null) {

						// TODO: THIS IS A HACK TO GET AROUND USING THE UPDATE ATTRIBUTE
						// ACCESS CONTROL API
						if (fieldNameToUpdate.equalsIgnoreCase("routePlan")) {
							newAccessControl = AccessControl.READ_WRITE;
						}

						fieldNameToAccessControlMap
								.put(fieldNameToUpdate, newAccessControl);

						LOG.debug("Updated [" + fieldNameToUpdate + "] to ["
								+ newAccessControl + "]");
					}
				}
			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public Set<String> getMutableAttributeNames() {

		Set<String> mutableAttributeNames = Collections
				.synchronizedSet(new HashSet<String>());

		for (String fieldName : fieldNameToAccessControlMap.keySet()) {

			if ((fieldName != null) && (!"".equalsIgnoreCase(fieldName))) {

				AccessControl ac = fieldNameToAccessControlMap.get(fieldName);

				if (ac != null) {

					if (ac == AccessControl.READ_WRITE) {

						mutableAttributeNames.add(fieldName);
					}
				}
			}
		}
		return mutableAttributeNames;
	}

	/**
	 * Retrieves the {@link AccessControl} for the specified attribute or field.
	 * 
	 * @param fieldName
	 * @return
	 */
	public AccessControl getAttributeAccessControl(final String fieldName) {

		AccessControl accessControl = null;

		if ((fieldName != null) && (!"".equalsIgnoreCase(fieldName))) {
			accessControl = fieldNameToAccessControlMap.get(fieldName);
		}

		return accessControl;
	}

	/**
	 * 
	 * @param fieldName
	 * @return
	 */
	public boolean isReadWrite(final String fieldName) {
		boolean isReadWrite = false;
		AccessControl accessControl = getAttributeAccessControl(fieldName);
		if (accessControl != null) {
			isReadWrite = getAttributeAccessControl(fieldName).equals(
					AccessControl.READ_WRITE);
		}
		return isReadWrite;
	}

	/**
	 * 
	 * @param fieldName
	 * @return
	 */
	public boolean isReadOnly(final String fieldName) {
		boolean isReadWrite = false;
		AccessControl accessControl = getAttributeAccessControl(fieldName);
		if (accessControl != null) {
			isReadWrite = getAttributeAccessControl(fieldName).equals(
					AccessControl.READ_ONLY);
		}
		return isReadWrite;
	}

	/**
	 * Recursive method that traverses the object graph to retrieve all Fields.
	 * 
	 * @param fields
	 *          Will contain all the Fields in the class hierarchy upon return
	 * @param type
	 *          Outermost sub-class
	 * @return
	 */
	private static List<Field> getAllFields(List<Field> fields, Class<?> type) {
		for (Field field : type.getDeclaredFields()) {
			fields.add(field);
		}

		if (type.getSuperclass() != null) {
			fields = getAllFields(fields, type.getSuperclass());
		}

		return fields;
	}

	/**
	 * @return the objectUUID
	 */
	public String getObjectUUID() {
		return objectUUID;
	}

	/**
	 * @return the baseDataObject
	 */
	public IBaseDataObject getBaseDataObject() {
		return baseDataObject;
	}

	/**
	 * 
	 */
	public void clear() {
		if (fieldNameToAccessControlMap != null) {
			fieldNameToAccessControlMap.clear();
		}
		if (baseDataObject != null) {
			baseDataObject = null;
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
		int result = 1;
		result = prime * result
				+ ((objectUUID == null) ? 0 : objectUUID.hashCode());
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
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ObjectAttributeAccessControl other = (ObjectAttributeAccessControl) obj;
		if (objectUUID == null) {
			if (other.objectUUID != null) {
				return false;
			}
		} else if (!objectUUID.equals(other.objectUUID)) {
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
		return "ObjectAttributeAccessControl [baseDataObject=" + baseDataObject
				+ ", fieldNameToAccessControlMap=" + fieldNameToAccessControlMap
				+ ", objectUUID=" + objectUUID + "]";
	}

}
