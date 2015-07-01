/**
 * 
 */
package com.csc.muthur.server.model;

import javax.jms.Message;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.event.FederationTerminationEvent;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class FederationExecutionTermination {

	private FederationExecutionModel fedExecModel;
	private Message message;
	private FederationTerminationEvent event;

	public FederationExecutionTermination(
			final FederationExecutionModel fedExecModel, final Message message,
			final FederationTerminationEvent event) throws MuthurException {

		if (fedExecModel == null) {
			throw new MuthurException(
					"Error creating FederationExecutionTermination object. "
							+ "FederationExecutionModel was null");
		}

		if (message == null) {
			throw new MuthurException(
					"Error creating FederationExecutionTermination object. "
							+ "Message was null");
		}

		if (event == null) {
			throw new MuthurException(
					"Error creating FederationExecutionTermination object. "
							+ "FederationTerminationEvent was null");
		}

		this.fedExecModel = fedExecModel;
		this.message = message;
		this.event = event;

	}

	/**
	 * @return the fedExecModel
	 */
	public final FederationExecutionModel getFedExecModel() {
		return fedExecModel;
	}

	/**
	 * @return the message
	 */
	public final Message getMessage() {
		return message;
	}

	/**
	 * @return the event
	 */
	public final FederationTerminationEvent getEvent() {
		return event;
	}
}
