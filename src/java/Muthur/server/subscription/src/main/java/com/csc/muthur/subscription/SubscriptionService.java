package com.csc.muthur.subscription;

import com.csc.muthur.commons.exception.MuthurException;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public interface SubscriptionService {

	/**
	 * 
	 * @throws MuthurException
	 */
	public void start() throws MuthurException;

	/**
	 * 
	 * @throws MuthurException
	 */
	public void stop() throws MuthurException;

}