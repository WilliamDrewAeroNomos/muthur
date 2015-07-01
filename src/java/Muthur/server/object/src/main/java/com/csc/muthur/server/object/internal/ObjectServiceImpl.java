/**
 * 
 */
package com.csc.muthur.server.object.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.CommonsService;
import com.csc.muthur.server.commons.FederationExecutionID;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.configuration.ConfigurationService;
import com.csc.muthur.server.model.ModelService;
import com.csc.muthur.server.model.event.data.IBaseDataObject;
import com.csc.muthur.server.model.event.data.flight.Aircraft;
import com.csc.muthur.server.object.FederationExecutionDataObjectContainer;
import com.csc.muthur.server.object.ObjectService;
import com.csc.muthur.server.ownership.OwnershipService;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class ObjectServiceImpl implements ObjectService {

	public static final Logger LOG = LoggerFactory
			.getLogger(ObjectServiceImpl.class.getName());

	// maps the federation execution ID to the container of objects created for
	// that federation execution
	//
	public Map<FederationExecutionID, FederationExecutionDataObjectContainer> federationExecIDToFederationObjectsContainerMap = new ConcurrentHashMap<FederationExecutionID, FederationExecutionDataObjectContainer>();

	private ConfigurationService configurationService;
	private ModelService modelService;
	private OwnershipService ownershipService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.object.ObjectService#start()
	 */
	public void start() throws Exception {

		LOG.info("Object service starting...");

		LOG.info("Object service started...");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.object.ObjectService#stop()
	 */
	public void stop() throws Exception {

		LOG.info("Object service stopping...");

		// clear out each of the object containers
		//
		for (FederationExecutionDataObjectContainer fedoc : federationExecIDToFederationObjectsContainerMap
				.values()) {
			if (fedoc != null) {
				fedoc.clear();
			}
		}

		LOG.info("Object service stopped...");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see (com.csc.muthur.server.commons.FederationExecutionID)
	 * com.csc.muthur.
	 * server.object.ObjectService#createFederationDataObjectContainer
	 */
	public FederationExecutionDataObjectContainer createFederationDataObjectContainer(
			final FederationExecutionID federationExecutionID) {

		FederationExecutionDataObjectContainer federationExecutionDataObjectContainer = null;

		if (federationExecutionID != null) {

			federationExecutionDataObjectContainer = federationExecIDToFederationObjectsContainerMap
					.get(federationExecutionID);

			if (federationExecutionDataObjectContainer == null) {

				// didn't already exist so create one
				//
				federationExecutionDataObjectContainer = new FederationExecutionDataObjectContainerImpl(
						federationExecutionID);

				federationExecIDToFederationObjectsContainerMap.put(
						federationExecutionID,
						federationExecutionDataObjectContainer);

				LOG.debug("Created object container for federation execution ID ["
						+ federationExecutionID + "]");

			}

		}

		return federationExecutionDataObjectContainer;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.object.ObjectService#
	 * removeFederationDataObjectContainer
	 * (com.csc.muthur.server.commons.FederationExecutionID)
	 */
	public void removeFederationDataObjectContainer(
			final FederationExecutionID federationExecutionID) {

		if (federationExecutionID != null) {

			if (!federationExecIDToFederationObjectsContainerMap
					.containsKey(federationExecutionID)) {

				FederationExecutionDataObjectContainer federationExecutionDataObjectContainer = federationExecIDToFederationObjectsContainerMap
						.get(federationExecutionID);

				if (federationExecutionDataObjectContainer != null) {

					federationExecutionDataObjectContainer.clear();
					LOG.debug("Removed all objects for federation execution ID ["
							+ federationExecutionID + "]");
				}
			}

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.object.ObjectService#addDataObject(com.csc.muthur
	 * .server.model. event.data.IBaseDataObject)
	 */
	public void addDataObject(
			final FederationExecutionID federationExecutionID,
			final IBaseDataObject baseDataObject) throws MuthurException {

		if (baseDataObject == null) {
			throw new IllegalArgumentException("IBaseDataObject parm null.");
		}

		if (federationExecutionID == null) {
			throw new IllegalArgumentException(
					"FederationExecutionID parm null.");
		}

		LOG.debug("Adding data object...");

		// get the container of data objects for the federation execution

		FederationExecutionDataObjectContainer fedExecDataObjContainer = getFederationExecutionDataObjectContainer(federationExecutionID);

		if (fedExecDataObjContainer == null) {
			throw new MuthurException(
					"Unable to find the FederationExecutionDataObjectContainerImpl for ["
							+ baseDataObject.getDataType() + "] data object.");
		}

		// add the object to the federation execution container
		//
		fedExecDataObjContainer.addDataObject(baseDataObject);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.object.ObjectService#updateDataObject(com.csc.muthur
	 * .server.model .event.data.IBaseDataObject)
	 */
	public void updateObject(final FederationExecutionID federationExecutionID,
			final IBaseDataObject baseDataObject) throws MuthurException {

		if (baseDataObject == null) {
			throw new IllegalArgumentException("IBaseDataObject parm null.");
		}

		if (federationExecutionID == null) {
			throw new IllegalArgumentException(
					"FederationExecutionID parm null.");
		}

		// get the container of data objects for the federation execution

		FederationExecutionDataObjectContainer fedExecDataObjContainer = getFederationExecutionDataObjectContainer(federationExecutionID);

		if (fedExecDataObjContainer == null) {
			throw new MuthurException(
					"["
							+ baseDataObject
							+ "] object was not updated. "
							+ "Unable to find object container for federation execution ID.");
		}

		// update the object in the federation execution container
		//
		fedExecDataObjContainer.updateDataObject(baseDataObject);

	}

	/**
	 * 
	 * @param federationExecutionID
	 * @param newDataObject
	 * @param fieldsToUpdate
	 * @throws MuthurException
	 */
	public void updateObjectAttributes(
			final FederationExecutionID federationExecutionID,
			final IBaseDataObject sourceObject, Set<String> fieldsToUpdate)
			throws MuthurException {

		if (sourceObject == null) {
			throw new IllegalArgumentException("IBaseDataObject was null.");
		}

		if (federationExecutionID == null) {
			throw new IllegalArgumentException(
					"FederationExecutionID was null.");
		}

		if (fieldsToUpdate == null) {
			throw new IllegalArgumentException(
					"List of fields to update was null.");
		}

		// get the container of data objects for the federation execution

		FederationExecutionDataObjectContainer fedExecDataObjContainer = getFederationExecutionDataObjectContainer(federationExecutionID);

		if (fedExecDataObjContainer == null) {
			throw new MuthurException(
					"["
							+ sourceObject
							+ "] object was not updated. "
							+ "Unable to find object container for federation execution ID.");
		}

		// retrieve the object from the federation execution container
		//
		IBaseDataObject targetObject = fedExecDataObjContainer
				.getObject(sourceObject.getDataObjectUUID());

		List<Field> fieldList = null;

		try {
			fieldList = modelService.getAllFieldsInHierarchy(sourceObject);
		} catch (ClassNotFoundException e) {
			throw new MuthurException(e);
		}

		Iterator<String> iter = fieldsToUpdate.iterator();

		Class<?> clazz = null;

		try {
			clazz = Class.forName(sourceObject.getClass().getName());

			// iterate over the list of attribute names that will be udpated

			while (iter.hasNext()) {

				String attributeName = iter.next();

				StringBuffer methodName = new StringBuffer();

				// get the get* method for this attribute

				methodName.append("get");
				methodName.append(attributeName.substring(0, 1).toUpperCase());
				methodName.append(attributeName.substring(1,
						(attributeName.length())));

				Method method = null;

				method = clazz.getMethod(methodName.toString());

				String mname = method.getName();

				LOG.debug("Getting [" + attributeName + "] with [" + mname
						+ "]");

				method.setAccessible(true);

				Object newValue = method.invoke(sourceObject, new Object[0]);

				methodName = new StringBuffer();
				methodName.append("set");
				methodName.append(attributeName.substring(0, 1).toUpperCase());
				methodName.append(attributeName.substring(1,
						(attributeName.length())));

				Iterator<Field> i = fieldList.iterator();
				Field nextField = null;
				while (i.hasNext()) {
					nextField = i.next();
					if (nextField.getName().equals(attributeName)) {
						break;
					}
				}

				Class<?> fieldType = nextField.getType();

				method = clazz.getMethod(methodName.toString(), fieldType);

				mname = method.getName();

				LOG.debug("Setting [" + attributeName + "] to [" + newValue
						+ "] with [" + mname + "]");

				method.setAccessible(true);

				method.invoke(targetObject, newValue);

				LOG.debug("[" + attributeName + "] set to [" + newValue + "]");

			}
		} catch (Exception e) {
			throw new MuthurException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.object.ObjectService#getDataObject(java.lang.String
	 * )
	 */
	public IBaseDataObject getDataObject(
			final FederationExecutionID federationExecutionID,
			final String dataObjectUUID) throws MuthurException {

		IBaseDataObject baseDataObject = null;

		if (dataObjectUUID == null) {
			throw new IllegalArgumentException("Object ID was null");
		}

		if (federationExecutionID == null) {
			throw new IllegalArgumentException(
					"Federation execution ID was null");
		}

		// get the container of data objects for the federation execution

		FederationExecutionDataObjectContainer fedExecDataObjContainer = getFederationExecutionDataObjectContainer(federationExecutionID);

		if (fedExecDataObjContainer == null) {
			throw new MuthurException(
					"Error retrieving data object with ID ["
							+ dataObjectUUID
							+ "]."
							+ " Unable to find object container for federation execution ID.");
		}

		// retrieve the object from the federation execution object container
		//
		baseDataObject = fedExecDataObjContainer.getObject(dataObjectUUID);

		return baseDataObject;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.object.ObjectService#objectExist(com.csc.muthur
	 * .server.commons. FederationExecutionID,
	 * com.csc.muthur.server.model.event.data.IBaseDataObject)
	 */
	public boolean objectExists(
			final FederationExecutionID federationExecutionID,
			IBaseDataObject bdo) throws MuthurException {

		if (bdo == null) {
			throw new IllegalArgumentException(
					"Object was null when checking if it currently exists.");
		}
		if (federationExecutionID == null) {
			throw new IllegalArgumentException(
					"Federation execution ID was null");
		}

		// get the container of data objects for the federation execution

		FederationExecutionDataObjectContainer fedExecDataObjContainer = getFederationExecutionDataObjectContainer(federationExecutionID);

		if (fedExecDataObjContainer == null) {
			throw new MuthurException(
					"Error checking for existence of data object ["
							+ bdo
							+ "]."
							+ " Unable to find object container for federation execution ID.");
		}

		// check if the object exists in the federation execution data container
		//
		return fedExecDataObjContainer.objectExists(bdo.getDataType(),
				bdo.getNaturalKey());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.object.ObjectService#getAircraftObjectGraph(com
	 * .csc.muthur .commons.FederationExecutionID, java.lang.String)
	 */
	public Set<IBaseDataObject> getAircraftObjectGraph(
			final FederationExecutionID federationExecutionID,
			final String dataObjectUUID) throws MuthurException {

		Set<IBaseDataObject> aircraftObjectGraph = Collections
				.synchronizedSet(new HashSet<IBaseDataObject>());

		IBaseDataObject baseDataObject = null;

		if (dataObjectUUID == null) {
			throw new IllegalArgumentException("Object ID was null");
		}

		if (federationExecutionID == null) {
			throw new IllegalArgumentException(
					"Federation execution ID was null");
		}

		// get the container of data objects for the federation execution

		FederationExecutionDataObjectContainer fedExecDataObjContainer = getFederationExecutionDataObjectContainer(federationExecutionID);

		if (fedExecDataObjContainer == null) {
			throw new MuthurException(
					"Error retrieving data object with ID ["
							+ dataObjectUUID
							+ "]."
							+ " Unable to find object container for federation execution ID.");
		}

		// retrieve the object from the federation execution object container
		//
		baseDataObject = fedExecDataObjContainer.getObject(dataObjectUUID);

		// see if there is even an Aircraft object with the requested UUID

		if (baseDataObject instanceof Aircraft) {

			aircraftObjectGraph.add(baseDataObject);

			LOG.debug("Added aircraft [" + baseDataObject
					+ "] to the object graph.");

			FederationExecutionDataObjectContainer federationExecutionDataObjectContainer = getFederationExecutionDataObjectContainer(federationExecutionID);

			if (federationExecutionDataObjectContainer != null) {

				for (IBaseDataObject bdo : federationExecutionDataObjectContainer
						.getAllObjects()) {

					// if not null and it's not the aircraft object that itself

					if ((bdo != null) && (!bdo.equals(baseDataObject))) {

						// could be from another aircraft graph

						if ((bdo.getNaturalKey()
								.equalsIgnoreCase(baseDataObject
										.getNaturalKey()))) {

							aircraftObjectGraph.add(bdo);

							LOG.debug("Added [" + bdo
									+ "] to the object graph.");
						}
					}
				}
			}
		}

		return aircraftObjectGraph;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.object.ObjectService#deleteDataObject(com.csc.muthur
	 * .server.commons .FederationExecutionID, java.lang.String)
	 */
	public IBaseDataObject deleteDataObject(
			final FederationExecutionID federationExecutionID,
			final String dataObjectUUID) throws MuthurException {

		if (dataObjectUUID == null) {
			throw new IllegalArgumentException("Object ID was null");
		}

		if (federationExecutionID == null) {
			throw new IllegalArgumentException(
					"FederationExecutionID parm null.");
		}

		// get the container of data objects for the federation execution

		FederationExecutionDataObjectContainer fedExecDataObjContainer = getFederationExecutionDataObjectContainer(federationExecutionID);

		if (fedExecDataObjContainer == null) {
			throw new MuthurException(
					"Error deleting data object with ID ["
							+ dataObjectUUID
							+ "]."
							+ " Unable to find object container for federation execution ID.");
		}

		// delete the object in the federation execution container
		//
		return fedExecDataObjContainer.deleteObject(dataObjectUUID);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.object.ObjectService#deleteObjects(com.csc.muthur
	 * .server.commons .FederationExecutionID)
	 */
	public void deleteObjects(final FederationExecutionID federationExecutionID)
			throws MuthurException {

		if (federationExecutionID == null) {
			throw new IllegalArgumentException(
					"FederationExecutionID parm null.");
		}

		// get the container of data objects for the federation execution

		FederationExecutionDataObjectContainer fedExecDataObjContainer = getFederationExecutionDataObjectContainer(federationExecutionID);

		if (fedExecDataObjContainer != null) {

			// delete the all objects in the federation data object container
			//
			fedExecDataObjContainer.clear();

			// remove the data object container from the map

			federationExecIDToFederationObjectsContainerMap
					.remove(federationExecutionID);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.object.ObjectService#getModelService()
	 */
	public final ModelService getModelService() {
		return modelService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.object.ObjectService#setModelService(com.csc.muthur
	 * .server.model .ModelService)
	 */
	public final void setModelService(ModelService modelService) {
		this.modelService = modelService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.object.ObjectService#getConfigurationService()
	 */
	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.object.ObjectService#setConfigurationService(com
	 * .csc.muthur .configuration.ConfigurationService)
	 */
	public void setConfigurationService(
			ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	/**
	 * 
	 * @param federationExecutionID
	 * @return
	 */
	public FederationExecutionDataObjectContainer getFederationExecutionDataObjectContainer(
			final FederationExecutionID federationExecutionID) {

		return federationExecIDToFederationObjectsContainerMap
				.get(federationExecutionID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.object.ObjectService#getObjectService()
	 */
	public OwnershipService getObjectService() {
		return ownershipService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.object.ObjectService#setOwnershipService(com.csc
	 * .muthur. ownership.OwnershipService)
	 */
	public void setOwnershipService(final OwnershipService ownershipService) {
		this.ownershipService = ownershipService;
	}

	/**
	 * @param commonsService
	 *            the commonsService to set
	 */
	public void setCommonsService(CommonsService commonsService) {
	}

}
