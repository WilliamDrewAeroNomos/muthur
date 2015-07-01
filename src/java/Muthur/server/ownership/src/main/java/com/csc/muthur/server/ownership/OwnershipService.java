/**
 * 
 */
package com.csc.muthur.server.ownership;

import java.util.Map;
import java.util.Set;

import javax.jms.Message;

import com.csc.muthur.server.commons.DataChannelControlBlock;
import com.csc.muthur.server.commons.FederationExecutionID;
import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.commons.ObjectOwnershipID;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.configuration.ConfigurationService;
import com.csc.muthur.server.model.AccessControl;
import com.csc.muthur.server.model.event.data.IBaseDataObject;
import com.csc.muthur.server.model.event.request.TransferObjectOwnershipRequest;
import com.csc.muthur.server.ownership.internal.ObjectAttributeAccessControl;
import com.csc.muthur.server.ownership.internal.OwnedObjectList;
import com.csc.muthur.server.router.RouterService;

/**
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public interface OwnershipService {

	void start() throws Exception;

	void stop() throws Exception;

	ConfigurationService getConfigurationService();

	void setConfigurationService(ConfigurationService configurationService);

	/**
	 * Add an object to the list owned by the registered user designated by the
	 * {@link ObjectOwnershipID} objectOwnershipID
	 * 
	 * @param objectOwnershipID
	 * @param baseDataObject
	 * @throws MuthurException
	 */
	void addObjectOwnership(final ObjectOwnershipID objectOwnershipID,
			final IBaseDataObject baseDataObject) throws MuthurException;

	/**
	 * 
	 * @param objectOwnershipID
	 * @param objectUUID
	 * @param fieldNameToAccessControlMap
	 * @return
	 */
	boolean updateAttributeAccessControl(
			final ObjectOwnershipID objectOwnershipID, final String objectUUID,
			final Map<String, AccessControl> fieldNameToAccessControlMap);

	/**
	 * 
	 * @param fromObjectOwnershipID
	 * @param toObjectOwnershipID
	 * @param objectUUID
	 * @return True if the object was transferred or false if the object was not
	 *         owned by the user.
	 * @throws MuthurException
	 */
	boolean transferObjectOwnership(
			final ObjectOwnershipID fromObjectOwnershipID,
			final ObjectOwnershipID toObjectOwnershipID, final String objectUUID)
			throws MuthurException;

	/**
	 * Removes the object from the list of objects owned by the specified
	 * {@link ObjectOwnershipID}
	 * 
	 * @param objectOwnershipID
	 * @param objectUUID
	 * @return True if the object was found and removed else false if the object
	 *         was not owned by the specified user.
	 */
	boolean removeObjectOwnership(final ObjectOwnershipID objectOwnershipID,
			final String objectUUID);

	/**
	 * Returns the objects owned by a user in a {@link OwnedObjectList}.
	 * 
	 * @param objectOwnershipID
	 * @return {@link OwnedObjectList} containing all the objects owned by the
	 *         user or null if the user does not own any objects.
	 */
	OwnedObjectList getOwnerObjects(final ObjectOwnershipID objectOwnershipID);

	/**
	 * 
	 * @param objectOwnershipID
	 * @param dataObjectUUID
	 */
	boolean isOwner(ObjectOwnershipID objectOwnershipID, String dataObjectUUID);

	/**
	 * 
	 * @param objectOwnershipID
	 * @param dataObjectUUID
	 * @return
	 */
	Set<String> getMutableAttributeNames(ObjectOwnershipID objectOwnershipID,
			String dataObjectUUID);

	/**
	 * 
	 * @param dataObjectUUID
	 * @return
	 */
	Set<String> getMutableAttributeNames(final String dataObjectUUID);

	/**
	 * 
	 * @param dataObjectUUID
	 * @return
	 */
	abstract boolean isObjectOwned(String dataObjectUUID);

	/**
	 * 
	 * @param dataObjectUUID
	 * @return
	 */
	public abstract IBaseDataObject getOwnedObject(String dataObjectUUID);

	/**
	 * Removes all objects for this {@link FederationExecutionID} including
	 * {@link ObjectAttributeAccessControl}s and referenced objects.
	 * 
	 * @param federationExecutionID
	 */
	void
			removeObjectOwnerships(final FederationExecutionID federationExecutionID);

	/**
	 * @return the requestTriageThread
	 */
	RequestTriageThread getRequestTriageThread();

	/**
	 * 
	 * @param request
	 * @param message
	 * @throws MuthurException
	 */
	void returnTransferOwnershipResponse(
			final TransferObjectOwnershipRequest request, final Message message)
			throws MuthurException;

	/**
	 * @return the routerService
	 */
	RouterService getRouterService();

	/**
	 * @param routerService
	 *          the routerService to set
	 */
	void setRouterService(RouterService routerService);

	/**
	 * 
	 * @param request
	 * @param dccBlock
	 * @throws MuthurException
	 */
	void queuePendingOwnershipRequest(final IEvent request,
			final DataChannelControlBlock dccBlock) throws MuthurException;

	/**
	 * 
	 * @param ownedObject
	 * @return
	 */
	ObjectOwnershipID getObjectOwner(final IBaseDataObject ownedObject);

	/**
	 * 
	 * @param event
	 * @return
	 */
	PendingFederateRequestEntry removePendingOwnershipReqeust(final IEvent event);

}