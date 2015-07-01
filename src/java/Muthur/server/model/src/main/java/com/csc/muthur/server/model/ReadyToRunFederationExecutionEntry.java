/**
 * 
 */
package com.csc.muthur.server.model;

import javax.jms.JMSException;
import javax.jms.Message;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.event.request.ReadyToRunRequest;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class ReadyToRunFederationExecutionEntry extends
		AbstractFederationExecutionEntry {

	private boolean indicatedReadyToRun = false;
	private String federationExecutionHandle;
	private String timeManagementQueueName;
	private String ownershipEventQueueName;
	private String federateRequestQueueName;

	private static final String exceptionMessagePrefix = "Error creating ReadyToRunFederationExecutionEntry. ";

	/**
	 * 
	 * @param federationExecutionModel
	 * @param readyToRunRequest
	 * @param message
	 * @throws Exception
	 */
	public ReadyToRunFederationExecutionEntry(
			final FederationExecutionModel federationExecutionModel,
			final ReadyToRunRequest readyToRunRequest, final Message message)
			throws MuthurException {
		super();

		// check parameters
		//
		if (readyToRunRequest == null) {
			throw new MuthurException(exceptionMessagePrefix
					+ "ReadyToRunRequest was null.");
		}

		if (message == null) {
			throw new MuthurException(exceptionMessagePrefix + "Message was null.");
		}

		try {
			if (message.getJMSReplyTo() == null) {
				throw new MuthurException(exceptionMessagePrefix
						+ "Reply queue was null.");
			}
		} catch (JMSException e) {
			throw new MuthurException(e);
		}

		// validate federation execution model

		if (federationExecutionModel == null) {
			throw new MuthurException(exceptionMessagePrefix
					+ "FederationExecutionModel was null.");
		}

		setFederationExecutionModel(federationExecutionModel);

		// validate federation execution handle

		if (readyToRunRequest.getFederationExecutionHandle() == null) {
			throw new MuthurException(exceptionMessagePrefix
					+ "FederationExecutionHandle was null.");
		}

		federationExecutionHandle = readyToRunRequest
				.getFederationExecutionHandle();

		// validate event source name

		if ((readyToRunRequest.getSourceOfEvent() == null)
				|| (readyToRunRequest.getSourceOfEvent().length() == 0)) {
			throw new MuthurException(exceptionMessagePrefix
					+ "Source of event was null or empty.");
		}

		setFederateName(readyToRunRequest.getSourceOfEvent());

		// validate time management queue name

		if ((readyToRunRequest.getTimeManagementQueueName() == null)
				|| (readyToRunRequest.getTimeManagementQueueName().length() == 0)) {
			throw new MuthurException(exceptionMessagePrefix
					+ "Time management queue name was null or empty.");
		}

		setTimeManagementQueueName(readyToRunRequest.getTimeManagementQueueName());

		// validate federate request queue name

		if ((readyToRunRequest.getFederateRequestQueueName() == null)
				|| (readyToRunRequest.getFederateRequestQueueName().length() == 0)) {
			throw new MuthurException(exceptionMessagePrefix
					+ "Federate request queue name was null or empty.");
		}

		setFederateRequestQueueName(readyToRunRequest.getFederateRequestQueueName());

		setMessage(message);

		setiEvent(readyToRunRequest);
	}

	/**
	 * @return the federationExecutionHandle
	 */
	public final String getFederationExecutionHandle() {
		return federationExecutionHandle;
	}

	/**
	 * @param timeManagementQueueName
	 *          the timeManagementQueueName to set
	 */
	public void setTimeManagementQueueName(String timeManagementQueueName) {
		this.timeManagementQueueName = timeManagementQueueName;
	}

	/**
	 * @return the timeManagementQueueName
	 */
	public String getTimeManagementQueueName() {
		return timeManagementQueueName;
	}

	/**
	 * @return the ownershipEventQueueName
	 */
	public final String getOwnershipEventQueueName() {
		return ownershipEventQueueName;
	}

	/**
	 * @param ownershipEventQueueName
	 *          the ownershipEventQueueName to set
	 */
	public final void setOwnershipEventQueueName(String ownershipEventQueueName) {
		this.ownershipEventQueueName = ownershipEventQueueName;
	}

	/**
	 * @return the federateRequestQueueName
	 */
	public String getFederateRequestQueueName() {
		return federateRequestQueueName;
	}

	/**
	 * @param federateRequestQueueName
	 *          the federateRequestQueueName to set
	 */
	public void setFederateRequestQueueName(String federateRequestQueueName) {
		this.federateRequestQueueName = federateRequestQueueName;
	}

	/**
	 * @param indicatedReadyToRun
	 *          the indicatedReadyToRun to set
	 */
	public final void setIndicatedReadyToRun(boolean indicatedReadyToRun) {
		this.indicatedReadyToRun = indicatedReadyToRun;
	}

	/**
	 * @return the indicatedReadyToRun
	 */
	public final boolean isIndicatedReadyToRun() {
		return indicatedReadyToRun;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime
				* result
				+ ((federationExecutionHandle == null) ? 0 : federationExecutionHandle
						.hashCode());
		result = prime * result + (indicatedReadyToRun ? 1231 : 1237);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReadyToRunFederationExecutionEntry other = (ReadyToRunFederationExecutionEntry) obj;
		if (federationExecutionHandle == null) {
			if (other.federationExecutionHandle != null)
				return false;
		} else if (!federationExecutionHandle
				.equals(other.federationExecutionHandle))
			return false;
		if (indicatedReadyToRun != other.indicatedReadyToRun)
			return false;
		return true;
	}

}
