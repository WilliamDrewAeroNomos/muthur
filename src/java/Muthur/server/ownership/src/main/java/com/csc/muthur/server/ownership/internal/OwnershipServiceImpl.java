/**
 * 
 */
package com.csc.muthur.server.ownership.internal;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.jms.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.commons.DataChannelControlBlock;
import com.csc.muthur.server.commons.FederationExecutionID;
import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.commons.MessageDestination;
import com.csc.muthur.server.commons.ObjectOwnershipID;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.configuration.ConfigurationService;
import com.csc.muthur.server.model.AccessControl;
import com.csc.muthur.server.model.event.data.IBaseDataObject;
import com.csc.muthur.server.model.event.request.TransferObjectOwnershipRequest;
import com.csc.muthur.server.model.event.response.TransferObjectOwnershipResponse;
import com.csc.muthur.server.ownership.OwnershipService;
import com.csc.muthur.server.ownership.PendingFederateRequestEntry;
import com.csc.muthur.server.ownership.RequestTriageThread;
import com.csc.muthur.server.router.RouterService;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public class OwnershipServiceImpl implements OwnershipService {

	public static final Logger LOG = LoggerFactory
			.getLogger(OwnershipServiceImpl.class.getName());

	private ConfigurationService configurationService = null;

	private RouterService routerService = null;

	private RequestTriageThread requestTriageThread = null;

	private RequestCallback pendingRequestCallback = null;

	/**
	 * 
	 */
	public Map<ObjectOwnershipID, OwnedObjectList> objOwnershipIDToOwnedObjectListMap =
			new ConcurrentHashMap<ObjectOwnershipID, OwnedObjectList>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.ownership.OwnershipService#start()
	 */
	public void start() throws Exception {

		LOG.info("Ownership service starting...");

		pendingRequestCallback = new TransferObjectOwnershipRequestCallback(this);

		requestTriageThread = new RequestTriageThreadImpl();

		new Thread(requestTriageThread).start();

		LOG.info("Ownership service started.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.ownership.OwnershipService#stop()
	 */
	public void stop() throws Exception {

		LOG.info("Ownership service stopping...");

		if (requestTriageThread != null) {
			requestTriageThread.setContinueRunning(false);
		}

		LOG.info("Ownership service stopped.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.ownership.OwnershipService#queuePendingOwnershipRequest
	 * ( com.csc.muthur.model.event.IEvent, javax.jms.Message)
	 */
	public void queuePendingOwnershipRequest(final IEvent request,
			final DataChannelControlBlock dccBlock) throws MuthurException {

		requestTriageThread.addRequest(request, pendingRequestCallback, dccBlock);
	}

	/**
	 * 
	 * @param event
	 * @return
	 */
	@Override
	public PendingFederateRequestEntry removePendingOwnershipReqeust(
			final IEvent event) {

		PendingFederateRequestEntry pendingFederateRequestEntry = null;

		if (event != null) {
			pendingFederateRequestEntry = requestTriageThread.removeRequest(event);
		}

		return pendingFederateRequestEntry;
	}

	/**
	 * Given only the {@link FederationExecutionID} removes all
	 * {@link ObjectOwnershipID}s for this {@link FederationExecutionID} including
	 * {@link ObjectAttributeAccessControl}s and referenced objects.
	 * 
	 * It iterators over the map of ownership IDs and for each that has a
	 * federation execution ID that matches the federationExectionID parameter it
	 * retrieves the {@link OwnedObjectList}. The {@link OwnedObjectList#clear()}
	 * is then called to remove the {@link ObjectAttributeAccessControl}s, all
	 * {@link AccessControl}s and the related {@link IBaseDataObject}.
	 * 
	 * @param federationExecutionID
	 */
	public void removeObjectOwnerships(
			final FederationExecutionID federationExecutionID) {

		/**
		 * Check parameters
		 */
		if (federationExecutionID == null) {
			throw new IllegalArgumentException("FederationExecutionID was null.");
		}

		Set<ObjectOwnershipID> objectOwnershipSet =
				objOwnershipIDToOwnedObjectListMap.keySet();

		if (objectOwnershipSet != null) {

			Iterator<ObjectOwnershipID> iter = objectOwnershipSet.iterator();

			if (iter != null) {

				while (iter.hasNext()) {

					ObjectOwnershipID objectOwnershipID = iter.next();

					if (objectOwnershipID != null) {

						FederationExecutionID nextFedExecID =
								objectOwnershipID.getFederationExecutionID();

						if ((nextFedExecID != null)
								&& (nextFedExecID.equals(federationExecutionID))) {

							OwnedObjectList thisOwnObjList =
									objOwnershipIDToOwnedObjectListMap.remove(objectOwnershipID);

							if (thisOwnObjList != null) {

								thisOwnObjList.clear();

								LOG.debug("Removed all objects for ownership ID ["
										+ objectOwnershipID.toString() + "]");
							}
						}
					}
				}
			}
		}

	}

	/**
	 * Add an object to the list of objects owned by the registered user
	 * designated by the {@link ObjectOwnershipID} objectOwnershipID
	 * 
	 * @param objectOwnershipID
	 * @param baseDataObject
	 * @throws MuthurException
	 */
	public void addObjectOwnership(final ObjectOwnershipID objectOwnershipID,
			final IBaseDataObject baseDataObject) throws MuthurException {

		/**
		 * Check parameters
		 */
		if (objectOwnershipID == null) {
			throw new IllegalArgumentException("ObjectOwnershipID was null.");
		}
		if (baseDataObject == null) {
			throw new IllegalArgumentException("IBaseDataObject was null.");
		}

		OwnedObjectList ownedObjectList =
				objOwnershipIDToOwnedObjectListMap.get(objectOwnershipID);

		// check if this owner has added any objects before...

		if (ownedObjectList == null) {

			// if not then create a new list and then it to the map
			//
			ownedObjectList = new OwnedObjectList();

			objOwnershipIDToOwnedObjectListMap
					.put(objectOwnershipID, ownedObjectList);
		}

		// add the object to the list for this owner
		//
		ownedObjectList.addObject(baseDataObject);

		LOG.debug("Added [" + baseDataObject + "] to owned object list for ["
				+ objectOwnershipID + "]");
		LOG.debug(" [" + ownedObjectList.getOwnedObjects().size()
				+ "] objects owned by [" + objectOwnershipID + "]");
	}

	/**
	 * Updates the {@link AccessControl} on one or more attributes for a
	 * particular object owned by a registered user represented by a
	 * {@link ObjectOwnershipID}.
	 * 
	 * @param objectOwnershipID
	 *          IDs a registered user running in a executing federation
	 * @param objectUUID
	 *          Unique object identifier
	 * @param fieldNameToAccessControlMap
	 *          Map of fields or attribute names and the {@link AccessControl} to
	 *          which they will be updated or set.
	 * @return
	 */
	public boolean updateAttributeAccessControl(
			final ObjectOwnershipID objectOwnershipID, final String objectUUID,
			final Map<String, AccessControl> fieldNameToAccessControlMap) {

		/**
		 * Check parameters
		 */
		if (objectOwnershipID == null) {
			throw new IllegalArgumentException("ObjectOwnershipID was null.");
		}

		if ((objectUUID == null) || ("".equalsIgnoreCase(objectUUID))) {
			throw new IllegalArgumentException("Object UUID was null.");
		}

		if (fieldNameToAccessControlMap == null) {
			throw new IllegalArgumentException(
					"Field name to access control map was null.");
		}

		boolean status = false;

		OwnedObjectList ownedObjectList =
				objOwnershipIDToOwnedObjectListMap.get(objectOwnershipID);

		if (ownedObjectList != null) {
			status =
					ownedObjectList.updateAttributeAccessControl(objectUUID,
							fieldNameToAccessControlMap);
		}

		return status;
	}

	/**
	 * 
	 * @param fromObjectOwnershipID
	 * @param toObjectOwnershipID
	 * @param objectUUID
	 * @return True if the object was transferred or false if the object was not
	 *         owned by the user.
	 * @throws MuthurException
	 */
	public boolean transferObjectOwnership(
			final ObjectOwnershipID fromObjectOwnershipID,
			final ObjectOwnershipID toObjectOwnershipID, final String objectUUID)
			throws MuthurException {

		boolean status = false;

		/**
		 * Check parameters
		 */
		if (fromObjectOwnershipID == null) {
			throw new IllegalArgumentException("From ObjectOwnershipID was null.");
		}

		if (toObjectOwnershipID == null) {
			throw new IllegalArgumentException("To ObjectOwnershipID was null.");
		}

		if ((objectUUID == null) || ("".equalsIgnoreCase(objectUUID))) {
			throw new IllegalArgumentException("Object UUID was null.");
		}

		OwnedObjectList ownedObjectList =
				objOwnershipIDToOwnedObjectListMap.get(fromObjectOwnershipID);

		if (ownedObjectList != null) {

			ObjectAttributeAccessControl accessControl =
					ownedObjectList.removeObject(objectUUID);

			if (accessControl != null) {

				IBaseDataObject bdo = accessControl.getBaseDataObject();

				if (bdo != null) {

					addObjectOwnership(toObjectOwnershipID, bdo);

					status = true;

					LOG.debug("Transferred [" + bdo + "] from [" + fromObjectOwnershipID
							+ " to [" + toObjectOwnershipID + "]");
				}
			}
		}

		return status;
	}

	/**
	 * Removes the object from the list of objects owned by the specified
	 * {@link ObjectOwnershipID}
	 * 
	 * @param objectOwnershipID
	 * @param objectUUID
	 * @return True if the object was found and removed else false if the object
	 *         was not owned by the specified user.
	 */
	public boolean removeObjectOwnership(
			final ObjectOwnershipID objectOwnershipID, final String objectUUID) {

		boolean status = false;

		/**
		 * Check parameters
		 */
		if (objectOwnershipID == null) {
			throw new IllegalArgumentException("ObjectOwnershipID was null.");
		}

		if ((objectUUID == null) || ("".equalsIgnoreCase(objectUUID))) {
			throw new IllegalArgumentException("Object UUID was null.");
		}

		/**
		 * Get the list of objects owned by this ObjectOwnershipID
		 */
		OwnedObjectList ownedObjectList =
				objOwnershipIDToOwnedObjectListMap.get(objectOwnershipID);

		if (ownedObjectList != null) {

			ObjectAttributeAccessControl accessControl =
					ownedObjectList.removeObject(objectUUID);

			if (accessControl != null) {
				LOG.debug("Removing ownership of [" + accessControl.getBaseDataObject()
						+ "]");
				accessControl.clear();
				status = true;
			}
		}

		return status;
	}

	/**
	 * Returns the objects owned by a user in a {@link OwnedObjectList}.
	 * 
	 * @param objectOwnershipID
	 * @return {@link OwnedObjectList} containing all the objects owned by the
	 *         user or null if the user does not own any objects.
	 */
	public OwnedObjectList getOwnerObjects(
			final ObjectOwnershipID objectOwnershipID) {

		/**
		 * Check parameters
		 */
		if (objectOwnershipID == null) {
			throw new IllegalArgumentException("ObjectOwnershipID was null.");
		}

		return objOwnershipIDToOwnedObjectListMap.get(objectOwnershipID);
	}

	/**
	 * 
	 * @param ownedObject
	 * @return
	 */
	@Override
	public ObjectOwnershipID getObjectOwner(final IBaseDataObject ownedObject) {

		ObjectOwnershipID objectOwnershipID = null;

		if ((ownedObject != null) && (ownedObject.getDataObjectUUID() != null)
				&& (ownedObject.getDataObjectUUID().length() > 0)) {

			Set<ObjectOwnershipID> objectOwnershipSet =
					objOwnershipIDToOwnedObjectListMap.keySet();

			if (objectOwnershipSet != null) {

				Iterator<ObjectOwnershipID> iter = objectOwnershipSet.iterator();

				if (iter != null) {

					while (iter.hasNext()) {

						ObjectOwnershipID nextObjectOwnershipID = iter.next();

						if (nextObjectOwnershipID != null) {

							OwnedObjectList ownedObjectList =
									objOwnershipIDToOwnedObjectListMap.get(nextObjectOwnershipID);

							if (ownedObjectList != null) {

								if (ownedObjectList.containsObject(ownedObject
										.getDataObjectUUID())) {

									objectOwnershipID = nextObjectOwnershipID;

									break;
								}
							}
						}
					}
				}
			}
		}

		return objectOwnershipID;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.ownership.OwnershipService#isOwner(com.csc.muthur
	 * .server.commons .FederationExecutionID, java.lang.String)
	 */
	@Override
	public boolean isOwner(ObjectOwnershipID objectOwnershipID,
			String dataObjectUUID) {

		boolean isOwner = false;

		if ((objectOwnershipID != null) && (dataObjectUUID != null)
				&& (!("".equalsIgnoreCase(dataObjectUUID)))) {

			OwnedObjectList ownObjectList = getOwnerObjects(objectOwnershipID);

			if (ownObjectList != null) {
				isOwner = ownObjectList.getOwnedObjects().contains(dataObjectUUID);
			}
		}
		return isOwner;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.ownership.OwnershipService#isObjectOwned(java.lang
	 * .String)
	 */
	@Override
	public boolean isObjectOwned(String dataObjectUUID) {

		boolean isObjectOwned = false;

		if ((dataObjectUUID != null) && (!("".equalsIgnoreCase(dataObjectUUID)))) {

			for (OwnedObjectList ownObjectList : objOwnershipIDToOwnedObjectListMap
					.values()) {

				if (ownObjectList != null) {
					isObjectOwned = ownObjectList.containsObject(dataObjectUUID);
					if (isObjectOwned == true) {
						break;
					}
				}
			}

		}
		return isObjectOwned;
	}

	/**
	 * 
	 * @param dataObjectUUID
	 * @return
	 */
	@Override
	public IBaseDataObject getOwnedObject(String dataObjectUUID) {

		IBaseDataObject bdo = null;

		if ((dataObjectUUID != null) && (!("".equalsIgnoreCase(dataObjectUUID)))) {

			for (OwnedObjectList ownObjectList : objOwnershipIDToOwnedObjectListMap
					.values()) {

				if (ownObjectList != null) {
					bdo = ownObjectList.getOwnedObject(dataObjectUUID);
					if (bdo != null) {
						break;
					}
				}
			}
		}
		return bdo;
	}

	/**
	 * 
	 * @param objectOwnershipID
	 * @param dataObjectUUID
	 * @return
	 */
	public Set<String> getMutableAttributeNames(
			final ObjectOwnershipID objectOwnershipID, final String dataObjectUUID) {

		Set<String> mutableAttributeNames = null;

		if ((objectOwnershipID != null) && (dataObjectUUID != null)
				&& (!("".equalsIgnoreCase(dataObjectUUID)))) {

			OwnedObjectList ownedObjectList =
					objOwnershipIDToOwnedObjectListMap.get(objectOwnershipID);
			if (ownedObjectList != null) {
				mutableAttributeNames =
						ownedObjectList.getMutableAttributeNames(dataObjectUUID);
			}
		}

		return mutableAttributeNames;
	}

	/**
	 * 
	 * @param dataObjectUUID
	 * @return
	 */
	public Set<String> getMutableAttributeNames(final String dataObjectUUID) {

		Set<String> mutableAttributeNames = null;

		if ((dataObjectUUID != null) && (!("".equalsIgnoreCase(dataObjectUUID)))) {

			for (OwnedObjectList ool : objOwnershipIDToOwnedObjectListMap.values()) {

				if (ool != null) {

					if (ool.containsObject(dataObjectUUID)) {

						mutableAttributeNames =
								ool.getMutableAttributeNames(dataObjectUUID);
					}
				}
			}
		}

		return mutableAttributeNames;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.object.internal.ObjectService#getConfigurationService()
	 */
	public ConfigurationService getConfigurationService() {
		return configurationService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.object.internal.ObjectService#setConfigurationService(com
	 * .csc.muthur.configuration.ConfigurationService)
	 */
	public void
			setConfigurationService(ConfigurationService configurationService) {
		this.configurationService = configurationService;
	}

	/**
	 * @return the requestTriageThread
	 */
	public final RequestTriageThread getRequestTriageThread() {
		return requestTriageThread;
	}

	/**
	 * 
	 * @param request
	 * @param message
	 * @throws MuthurException
	 */
	public final void returnTransferOwnershipResponse(
			final TransferObjectOwnershipRequest request, final Message message)
			throws MuthurException {

		TransferObjectOwnershipResponse response =
				new TransferObjectOwnershipResponse();

		response.initialization(request.serialize());

		response.setSourceOfEvent(MessageDestination.MUTHUR);
		response.setStatus("complete");
		response.setSuccess(true);

		response.setDataObjectUUID(request.getDataObjectUUID());

		LOG.debug("Sending transfer object ownership response to dispatcher...");

		routerService.sendResponse(response, message);

		LOG.info("Transfer object ownership response sent to ["
				+ request.getSourceOfEvent() + "]");

	}

	/**
	 * @param routerService
	 *          the routerService to set
	 */
	public final void setRouterService(RouterService routerService) {
		this.routerService = routerService;
	}

	/**
	 * @return the routerService
	 */
	public final RouterService getRouterService() {
		return routerService;
	}
}
