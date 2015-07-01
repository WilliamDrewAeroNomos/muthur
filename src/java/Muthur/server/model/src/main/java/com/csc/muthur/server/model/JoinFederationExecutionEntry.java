/**
 * 
 */
package com.csc.muthur.server.model;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import com.csc.muthur.server.commons.MessageDestination;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.event.request.JoinFederationRequest;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class JoinFederationExecutionEntry extends
		AbstractFederationExecutionEntry {

	private String federationEventQueueName;
	private boolean joined = false; // false if not joined yet and true once

	/**
	 * 
	 * @param federateName
	 * @param federationExecutionModel
	 * @param message
	 * @param joinFederationRequest
	 * @throws MuthurException
	 */
	public JoinFederationExecutionEntry(final String federateName,
			final FederationExecutionModel federationExecutionModel,
			final Message message, final JoinFederationRequest joinFederationRequest)
			throws MuthurException {
		super();

		String errPrefix = "Error creating JoinFederationExecutionEntry. ";

		// check parameters
		//
		if ((federateName == null) || ("".equals(federateName))) {
			throw new MuthurException(errPrefix + "Federate name was null.");
		}
		if (message == null) {
			throw new MuthurException(errPrefix + "Message was null.");
		}
		try {
			if (message.getJMSReplyTo() == null) {
				throw new MuthurException(errPrefix + "Reply queue was null.");
			}
		} catch (JMSException e1) {
			throw new MuthurException(e1);
		}

		if (joinFederationRequest == null) {
			throw new MuthurException(errPrefix + "JoinFederationRequest was null.");
		}

		//
		// Get the federation event queue from the message
		//

		TextMessage tm = (TextMessage) message;

		try {
			setFederationEventQueueName(tm.getStringProperty(MessageDestination
					.getFederationEventQueuePropName()));
		} catch (JMSException e) {
			throw new MuthurException(e);
		}

		if ((federationEventQueueName == null)
				|| ("".equalsIgnoreCase(federationEventQueueName))) {
			throw new MuthurException(errPrefix
					+ "Federation event queue name was null.");
		}

		/**
		 * Set each of the AbstractFederationExecutionEntry attributes
		 */
		setFederationExecutionModel(federationExecutionModel);
		setFederateName(federateName);
		setMessage(message);
		setiEvent(joinFederationRequest);
	}

	/**
	 * @return the joined
	 */
	public final boolean isJoined() {
		return joined;
	}

	/**
	 * @param joined
	 *          the joined to set
	 */
	public final void setJoined(boolean joined) {
		this.joined = joined;
	}

	/**
	 * @param federationEventQueueName
	 *          the federationEventQueueName to set
	 */
	public final void setFederationEventQueueName(String federationEventQueueName) {
		this.federationEventQueueName = federationEventQueueName;
	}

	/**
	 * @return the federationEventQueueName
	 */
	public final String getFederationEventQueueName() {
		return federationEventQueueName;
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
		result =
				prime
						* result
						+ ((federationEventQueueName == null) ? 0
								: federationEventQueueName.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		JoinFederationExecutionEntry other = (JoinFederationExecutionEntry) obj;
		if (federationEventQueueName == null) {
			if (other.federationEventQueueName != null) {
				return false;
			}
		} else if (!federationEventQueueName.equals(other.federationEventQueueName)) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("JoinFederationExecutionEntry [federationEventQueueName=");
		builder.append(federationEventQueueName);
		builder.append(", Federate=");
		builder.append(getFederateName());
		builder.append(", joined=");
		builder.append(joined);
		builder.append("]");
		return builder.toString();
	}

}
