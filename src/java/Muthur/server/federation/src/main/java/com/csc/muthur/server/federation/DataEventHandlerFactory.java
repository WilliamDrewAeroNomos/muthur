/**
 * 
 */
package com.csc.muthur.server.federation;

import org.springframework.context.ApplicationContext;

import com.csc.muthur.server.commons.IDataEventHandler;
import com.csc.muthur.server.commons.IEvent;

/**
 * 
 * @author <a href=mailto:support@atcloud.com>support</a>
 * @version $Revision: $
 */
public interface DataEventHandlerFactory {

	/**
	 * 
	 * @param event
	 *          {@link IEvent} to be handled or processed
	 * @return {@link IDataEventHandler} configured in the Spring application
	 *         context for the {@link IEvent} parameter.
	 */
	public abstract IDataEventHandler getEventHandler(final IEvent event);

	public abstract void setApplicationContext(ApplicationContext applicationContext);

	public abstract ApplicationContext getApplicationContext();


}