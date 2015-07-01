/**
 * 
 */
package com.csc.muthur.server.federation.internal.execution;

import com.csc.muthur.server.model.event.DataPublicationEvent;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public interface FederationDataPublicationQueue {

	/**
	 * 
	 */
	void terminate();

	/**
	 * 
	 * @param dataPublicationEvent
	 */
	void put(final DataPublicationEvent dataPublicationEvent);

}