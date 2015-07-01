/**
 * 
 */
package com.csc.muthur.server.model;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import com.atcloud.fem.FederationExecutionModelService;
import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.commons.exception.MuthurException;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public interface ModelService {

	/**
	 * 
	 */
	public abstract void start();

	/**
	 * 
	 */
	public abstract void stop();

	/**
	 * 
	 * @param fem
	 */
	public abstract void addFederationExecutionModelToCache(
			final FederationExecutionModel fem);

	/**
	 * 
	 * @return
	 */
	public abstract Collection<FederationExecutionModel>
			getFederationExecutionModels();

	/**
	 * 
	 * @param femToRemove
	 */
	public abstract void removeFEM(final FederationExecutionModel femToRemove);

	/**
	 * 
	 * @param eventAsXML
	 * @return
	 * @throws MuthurException
	 */
	public IEvent createEvent(final String eventAsXML) throws MuthurException;

	/**
	 * Returns all the {@link Field}s in the class hierarchy for a particular
	 * object starting at the class represented by object.
	 * 
	 * @param object
	 *          Outermost class in the hierarchy
	 * @return List of {@link Field}s for all classes in the hierarchy.
	 * @throws ClassNotFoundException
	 */
	public List<Field> getAllFieldsInHierarchy(final Object object)
			throws ClassNotFoundException;

	public abstract FederationExecutionModel getModel(final String uuid);

	public abstract void setFederationExecutionModelService(
			FederationExecutionModelService federationExecutionModelService);

	public abstract FederationExecutionModelService
			getFederationExecutionModelService();

}