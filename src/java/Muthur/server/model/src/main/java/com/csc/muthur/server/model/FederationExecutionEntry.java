/**
 * 
 */
package com.csc.muthur.server.model;

import javax.jms.Message;

import com.csc.muthur.server.commons.IEvent;

/**
 * @author williamdrew
 * 
 */
public interface FederationExecutionEntry {

	/**
	 * @return the federateName
	 */
	public String getFederateName();

	/**
	 * @return the iEvent
	 */
	public IEvent getEvent();

	/**
	 * @return the message
	 */
	public Message getMessage();

	/**
	 * 
	 * @return {@link FederationExecutionModel} used for this federation execution
	 */
	public FederationExecutionModel getFederationExecutionModel();

}