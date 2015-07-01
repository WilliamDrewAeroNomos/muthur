package com.csc.muthur.server.ownership.internal;

import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.ownership.PendingFederateRequestEntry;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public interface RequestCallback {

	/**
	 * 
	 * @param response
	 */
	void onSuccess(IEvent response);

	/**
	 * 
	 * @param entry
	 * @param e
	 */
	void onError(PendingFederateRequestEntry entry, Exception e);

}