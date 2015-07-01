/**
 * 
 */
package com.csc.muthur.server.model;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.TextMessage;

import com.csc.muthur.server.commons.MessageDestination;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.event.request.DataSubscriptionRequest;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class SubscriptionRegistrationFederationExecutionEntry extends
		AbstractFederationExecutionEntry {

	private String dataEventQueueName;
	private Destination dataEventQueue;
	private MessageProducer dataPublicationEventProducer;

	private Destination ownershipEventQueue;
	private MessageProducer ownershipEventProducer;
	private boolean subscribed = false; // false if not yet subscribed

	/**
	 * 
	 * @param federationExecutionModel
	 * @param subscriptionRequestEvent
	 * @param message
	 */
	public SubscriptionRegistrationFederationExecutionEntry(
			final FederationExecutionModel federationExecutionModel,
			final DataSubscriptionRequest subscriptionRequestEvent,
			final Message message) throws MuthurException {
		super();

		// check parameters
		//
		if (federationExecutionModel == null) {
			throw new MuthurException("Error creating JoinFederationExecutionEntry. "
					+ "FederationExecutionModel was null.");
		}

		if (subscriptionRequestEvent == null) {
			throw new MuthurException("Error creating JoinFederationExecutionEntry. "
					+ "DataSubscriptionRequestEvent was null.");
		}
		if (message == null) {
			throw new MuthurException("Error creating JoinFederationExecutionEntry. "
					+ "Message was null.");
		}

		try {
			if (message.getJMSReplyTo() == null) {
				throw new MuthurException(
						"Error creating JoinFederationExecutionEntry. "
								+ "Reply queue was null.");
			}

			TextMessage tm = (TextMessage) message;

			dataEventQueueName =
					tm.getStringProperty(MessageDestination.getDataEventQueuePropName());
		} catch (JMSException e) {
			throw new MuthurException(e);
		}

		if ((dataEventQueueName == null) || ("".equals(dataEventQueueName))) {
			throw new MuthurException("Error creating JoinFederationExecutionEntry. "
					+ "Data event queue name was null or empty.");
		}

		/**
		 * Set each of the AbstractFederationExecutionEntry attributes
		 */
		setFederationExecutionModel(federationExecutionModel);
		setFederateName(subscriptionRequestEvent.getSourceOfEvent());
		setMessage(message);
		setiEvent(subscriptionRequestEvent);
	}

	/**
	 * @return the joined
	 */
	public final boolean isSubscribed() {
		return subscribed;
	}

	/**
	 * @param subscribed
	 *          the joined to set
	 */
	public final void setSubscribed(boolean subscribed) {
		this.subscribed = subscribed;
	}

	/**
	 * @return the datatEventQueueName
	 */
	public final String getDataEventQueueName() {
		return dataEventQueueName;
	}

	/**
	 * @return the dataEventQueue
	 */
	public final Destination getDataEventQueue() {
		return dataEventQueue;
	}

	/**
	 * @param dataEventQueue
	 *          the dataEventQueue to set
	 */
	public final void setDataEventQueue(Destination dataEventQueue) {
		this.dataEventQueue = dataEventQueue;
	}

	/**
	 * @return the dataPublicationEventProducer
	 */
	public final MessageProducer getDataPublicationEventProducer() {
		return dataPublicationEventProducer;
	}

	/**
	 * @param dataPublicationEventProducer
	 *          the dataPublicationEventProducer to set
	 */
	public final void setDataPublicationEventProducer(
			MessageProducer dataPublicationEventProducer) {
		this.dataPublicationEventProducer = dataPublicationEventProducer;
	}

	/**
	 * @param ownershipEventProducer
	 *          the ownershipEventProducer to set
	 */
	public void setOwnershipEventProducer(MessageProducer ownershipEventProducer) {
		this.ownershipEventProducer = ownershipEventProducer;
	}

	/**
	 * @return the ownershipEventProducer
	 */
	public MessageProducer getOwnershipEventProducer() {
		return ownershipEventProducer;
	}

	/**
	 * @param ownershipEventQueue
	 *          the ownershipEventQueue to set
	 */
	public void setOwnershipEventQueue(Destination ownershipEventQueue) {
		this.ownershipEventQueue = ownershipEventQueue;
	}

	/**
	 * @return the ownershipEventQueue
	 */
	public Destination getOwnershipEventQueue() {
		return ownershipEventQueue;
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
						+ ((dataEventQueueName == null) ? 0 : dataEventQueueName.hashCode());
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
		SubscriptionRegistrationFederationExecutionEntry other =
				(SubscriptionRegistrationFederationExecutionEntry) obj;
		if (dataEventQueueName == null) {
			if (other.dataEventQueueName != null) {
				return false;
			}
		} else if (!dataEventQueueName.equals(other.dataEventQueueName)) {
			return false;
		}
		return true;
	}

}
