/**
 * 
 */
package com.csc.muthur.server.object;

import java.util.Collection;

import com.csc.muthur.server.commons.FederationExecutionID;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.event.data.DataTypeEnum;
import com.csc.muthur.server.model.event.data.IBaseDataObject;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public interface FederationExecutionDataObjectContainer {

	/**
	 * 
	 * @param federationExectionID
	 *          the federationExectionID to set
	 */
	void setFederationExectionID(FederationExecutionID federationExectionID);

	/**
	 * 
	 * @return the federationExectionID
	 */
	FederationExecutionID getFederationExectionID();

	/**
	 * 
	 * @param baseDataObject
	 * @throws MuthurException
	 */
	void addDataObject(final IBaseDataObject baseDataObject)
			throws MuthurException;

	/**
	 * 
	 * @param baseDataObject
	 * @throws MuthurException
	 */
	void updateDataObject(IBaseDataObject baseDataObject) throws MuthurException;

	/**
	 * 
	 * @param dataObjectUUID
	 * @return
	 * @throws MuthurException
	 */
	IBaseDataObject deleteObject(String dataObjectUUID) throws MuthurException;

	/**
	 * 
	 * @param dataObjectUUID
	 * @return
	 */
	IBaseDataObject getObject(String dataObjectUUID);

	/**
	 * 
	 */
	void clear();

	/**
	 * 
	 * @return
	 */
	public Collection<IBaseDataObject> getAllObjects();

	public boolean objectExists(final DataTypeEnum dataType,
			final String naturalKey);

	public abstract boolean objectExists(final IBaseDataObject objectToCheckFor);

}