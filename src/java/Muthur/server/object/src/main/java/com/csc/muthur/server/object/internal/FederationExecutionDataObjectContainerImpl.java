/**
 * 
 */
package com.csc.muthur.server.object.internal;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.FederationExecutionID;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.event.data.DataTypeEnum;
import com.csc.muthur.server.model.event.data.IBaseDataObject;
import com.csc.muthur.server.object.FederationExecutionDataObjectContainer;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class FederationExecutionDataObjectContainerImpl implements
		FederationExecutionDataObjectContainer {

	private static final Logger LOG = LoggerFactory
			.getLogger(FederationExecutionDataObjectContainerImpl.class
					.getName());

	private FederationExecutionID federationExectionID;

	/**
	 * Maps dataObject UUID to data object
	 */
	private Map<String, IBaseDataObject> dataObjectUUIDToDataObjectMap = new ConcurrentHashMap<String, IBaseDataObject>();

	/**
	 * 
	 * @param federationExectionID
	 */
	public FederationExecutionDataObjectContainerImpl(
			final FederationExecutionID federationExectionID) {
		this.setFederationExectionID(federationExectionID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.csc.muthur.object.internal.FederationExecutionDataObjectContainer#
	 * setFederationExectionID
	 * (com.csc.muthur.server.commons.FederationExecutionID)
	 */
	public void setFederationExectionID(
			FederationExecutionID federationExectionID) {
		this.federationExectionID = federationExectionID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.csc.muthur.object.internal.FederationExecutionDataObjectContainer#
	 * getFederationExectionID()
	 */
	public FederationExecutionID getFederationExectionID() {
		return federationExectionID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.csc.muthur.object.internal.FederationExecutionDataObjectContainer#
	 * addDataObject(com.csc.muthur.server.model.event.data.IBaseDataObject)
	 */
	public void addDataObject(final IBaseDataObject baseDataObject)
			throws MuthurException {

		if (baseDataObject == null) {
			throw new IllegalArgumentException("Data object was null");
		}

		String dataObjectUUID = baseDataObject.getDataObjectUUID();

		if ((dataObjectUUID == null) || ("".equalsIgnoreCase(dataObjectUUID))) {
			throw new MuthurException(
					"Unable to add data object. ID was null or empty for ["
							+ baseDataObject + "] data object.");
		}

		if (objectExists(baseDataObject)) {
			throw new MuthurException(
					"Attempted to add duplicate object with key ["
							+ baseDataObject.getNaturalKey() + "] - ["
							+ baseDataObject + "]");
		}

		LOG.debug("Adding [" + baseDataObject + "] data object...");

		dataObjectUUIDToDataObjectMap.put(dataObjectUUID, baseDataObject);

		LOG.debug("Added data object [" + baseDataObject + "].");

		LOG.debug("[" + dataObjectUUIDToDataObjectMap.size()
				+ "] objects in map.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.csc.muthur.object.internal.FederationExecutionDataObjectContainer#
	 * updateDataObject(com.csc.muthur.server.model.event.data.IBaseDataObject)
	 */
	public void updateDataObject(IBaseDataObject baseDataObject)
			throws MuthurException {

		if (baseDataObject == null) {
			throw new IllegalArgumentException("Data object was null");
		}

		LOG.debug("Updating data object with key ["
				+ baseDataObject.getNaturalKey() + "]; UUID ["
				+ baseDataObject.getDataObjectUUID() + "]");

		String dataObjectUUID = baseDataObject.getDataObjectUUID();

		if ((dataObjectUUID == null) || ("".equalsIgnoreCase(dataObjectUUID))) {
			throw new MuthurException(
					"Unable to update data object. ID was null or empty for ["
							+ baseDataObject + "] data object.");
		}

		if (!dataObjectUUIDToDataObjectMap.containsKey(dataObjectUUID)) {
			throw new MuthurException(
					"Unable to update data object ["
							+ baseDataObject
							+ "]. A data object with UUID ["
							+ baseDataObject.getDataObjectUUID()
							+ "] was not found in the current list of federation objects.");
		}

		if (!objectExists(baseDataObject)) {
			throw new MuthurException("Unable to update data object. ["
					+ baseDataObject + "] data object was not found in "
					+ "the current list of federation objects with the key ["
					+ baseDataObject.getNaturalKey() + "];UUID ["
					+ baseDataObject.getDataObjectUUID() + "]");
		}

		LOG.debug("Updating data object [" + baseDataObject.getNaturalKey()
				+ "];UUID [" + baseDataObject.getDataObjectUUID() + "]["
				+ baseDataObject + "]...");

		dataObjectUUIDToDataObjectMap.put(dataObjectUUID, baseDataObject);

		LOG.debug("Data object [" + baseDataObject.getNaturalKey() + "];UUID ["
				+ baseDataObject.getDataObjectUUID() + "][" + baseDataObject
				+ "] updated");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seecom.csc.muthur.object.internal.FederationExecutionDataObjectContainer#
	 * deleteObject(java.lang.String)
	 */
	public IBaseDataObject deleteObject(String dataObjectUUID)
			throws MuthurException {

		IBaseDataObject bdo = null;

		if ((dataObjectUUID == null) || ("".equalsIgnoreCase(dataObjectUUID))) {
			throw new IllegalArgumentException(
					"Unable to delete data object. ID was null or empty");
		}

		if (!dataObjectUUIDToDataObjectMap.containsKey(dataObjectUUID)) {
			throw new MuthurException("Unable to delete data object."
					+ " Data object with ID [" + dataObjectUUID + "] was not "
					+ "found in the current list of federation objects.");
		}

		LOG.debug("Deleting data object...");

		bdo = dataObjectUUIDToDataObjectMap.remove(dataObjectUUID);

		if (bdo == null) {
			throw new MuthurException("Unable to delete data object."
					+ " Data object with ID [" + dataObjectUUID + "] was not "
					+ "found in the current map of federation objects.");
		}
		LOG.debug("Deleted [" + bdo + "] data object.");

		LOG.debug("[" + dataObjectUUIDToDataObjectMap.size()
				+ "] objects in map.");

		return bdo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.object.internal.FederationExecutionDataObjectContainer
	 * #getObject (java.lang.String)
	 */
	public IBaseDataObject getObject(String dataObjectUUID) {

		IBaseDataObject queriedOject = null;

		if ((dataObjectUUID == null) || ("".equalsIgnoreCase(dataObjectUUID))) {
			throw new IllegalArgumentException(
					"Unable to query for data object. ID was null or empty");
		}

		queriedOject = dataObjectUUIDToDataObjectMap.get(dataObjectUUID);

		return queriedOject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.object.FederationExecutionDataObjectContainer#
	 * objectExists (com.csc.muthur.server.model.event.data.IBaseDataObject)
	 */
	public boolean objectExists(final IBaseDataObject objectToCheckFor) {

		boolean objectExists = false;

		if (objectToCheckFor == null) {
			throw new IllegalArgumentException(
					"Unable to determine if data object exists. IBaseDataObject was null");
		}
		if (objectToCheckFor.getNaturalKey() == null) {
			throw new IllegalArgumentException(
					"Unable to determine if data object exists. Natural key is null.");
		}

		if (objectToCheckFor.getDataType() == null) {
			throw new IllegalArgumentException(
					"Unable to determine if data object exists. Data type value is null");
		}

		// use the natural key to check for the existence of the object

		for (IBaseDataObject bdo : getAllObjects()) {
			if (bdo != null) {
				if ((bdo.getNaturalKey().equalsIgnoreCase(
						objectToCheckFor.getNaturalKey()) && (bdo.getDataType()
						.equals(objectToCheckFor.getDataType())))) {
					objectExists = true;
					break;
				}
			}
		}

		return objectExists;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.object.FederationExecutionDataObjectContainer#
	 * objectExists (com.csc.muthur.server.model.event.data.DataTypeEnum,
	 * java.lang.String, java.lang.String)
	 */
	public boolean objectExists(final DataTypeEnum dataType,
			final String naturalKey) {

		boolean objectExists = false;

		if (dataType == null) {
			throw new IllegalArgumentException(
					"Unable to query for data object. Data type was null");
		}

		if ((naturalKey == null) || (naturalKey.length() == 0)) {
			throw new IllegalArgumentException(
					"Unable to query for data object. Natural key was null or empty");
		}

		// use the natural key to check for the existence of the object

		for (IBaseDataObject bdo : getAllObjects()) {
			if (bdo != null) {
				if ((bdo.getNaturalKey().equalsIgnoreCase(naturalKey) && (bdo
						.getDataType().equals(dataType)))) {
					objectExists = true;
					break;
				}
			}
		}

		return objectExists;
	}

	/**
	 * 
	 * @return
	 */
	public Collection<IBaseDataObject> getAllObjects() {
		return dataObjectUUIDToDataObjectMap.values();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.object.internal.FederationExecutionDataObjectContainer
	 * #clear ()
	 */
	public void clear() {
		if (dataObjectUUIDToDataObjectMap != null) {
			dataObjectUUIDToDataObjectMap.clear();
		}
	}

}
