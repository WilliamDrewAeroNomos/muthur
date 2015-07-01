/**
 * 
 */
package com.csc.muthur.server.object;

import java.util.Set;

import com.csc.muthur.server.commons.CommonsService;
import com.csc.muthur.server.commons.FederationExecutionID;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.configuration.ConfigurationService;
import com.csc.muthur.server.model.ModelService;
import com.csc.muthur.server.model.event.data.IBaseDataObject;
import com.csc.muthur.server.ownership.OwnershipService;

/**
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public interface ObjectService {

	void start() throws Exception;

	void stop() throws Exception;

	ModelService getModelService();

	void setModelService(ModelService modelService);

	ConfigurationService getConfigurationService();

	void setConfigurationService(ConfigurationService configurationService);

	/**
	 * 
	 * @param federationExecutionID
	 * @param baseDataObject
	 * @throws MuthurException
	 */
	void addDataObject(final FederationExecutionID federationExecutionID,
			final IBaseDataObject baseDataObject) throws MuthurException;

	/**
	 * 
	 * @param federationExecutionID
	 * @param dataObjectUUID
	 * @return
	 * @throws MuthurException
	 */
	IBaseDataObject getDataObject(
			final FederationExecutionID federationExecutionID,
			final String dataObjectUUID) throws MuthurException;

	/**
	 * 
	 * @param federationExecutionID
	 * @param dataObjectUUID
	 * @throws MuthurException
	 */
	IBaseDataObject deleteDataObject(
			final FederationExecutionID federationExecutionID,
			final String dataObjectUUID) throws MuthurException;

	/**
	 * 
	 * @param federationExecutionID
	 * @param bdo
	 * @throws MuthurException
	 */
	abstract void updateObject(final FederationExecutionID federationExecutionID,
			IBaseDataObject bdo) throws MuthurException;

	/**
	 * 
	 * @param federationExecutionID
	 * @return
	 */
	FederationExecutionDataObjectContainer createFederationDataObjectContainer(
			final FederationExecutionID federationExecutionID);

	/**
	 * 
	 * @param federationExecutionID
	 */
	void removeFederationDataObjectContainer(
			final FederationExecutionID federationExecutionID);

	/**
	 * 
	 * @param ownershipService
	 */
	void setOwnershipService(final OwnershipService ownershipService);

	abstract OwnershipService getObjectService();

	/**
	 * 
	 * @param federationExecutionID
	 * @param baseDataObject
	 * @param fieldsToUpdate
	 * @throws MuthurException
	 */
	void updateObjectAttributes(
			final FederationExecutionID federationExecutionID,
			final IBaseDataObject baseDataObject, Set<String> fieldsToUpdate)
			throws MuthurException;

	/**
	 * @param commonsService
	 *          the commonsService to set
	 */
	public void setCommonsService(CommonsService commonsService);

	/**
	 * Removes the data object container for the specified federation execution ID
	 * and all the objects within that container.
	 * 
	 * @param federationExecutionID
	 * @throws MuthurException
	 */
	public void deleteObjects(final FederationExecutionID federationExecutionID)
			throws MuthurException;

	/**
	 * 
	 * @param federationExecutionID
	 * @param dataObjectUUID
	 * @return
	 * @throws MuthurException
	 */
	public Set<IBaseDataObject> getAircraftObjectGraph(
			final FederationExecutionID federationExecutionID,
			final String dataObjectUUID) throws MuthurException;

	public abstract boolean objectExists(final FederationExecutionID federationExecutionID, IBaseDataObject bdo)
			throws MuthurException;

}