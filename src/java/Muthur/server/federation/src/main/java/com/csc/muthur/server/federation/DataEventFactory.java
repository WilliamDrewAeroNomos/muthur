/**
 * 
 */
package com.csc.muthur.server.federation;

import com.csc.muthur.server.commons.IEvent;

/**
 * 
 * @author <a href=mailto:support@atcloud.com>support</a>
 * @version $Revision: $
 */
public interface DataEventFactory {

	IEvent createEvent(final String eventName);

}
