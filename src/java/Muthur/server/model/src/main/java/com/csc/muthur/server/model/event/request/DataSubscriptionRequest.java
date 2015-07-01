/**
 * 
 */
package com.csc.muthur.server.model.event.request;

import java.util.HashSet;
import java.util.Set;

import org.jdom.Element;

import com.csc.muthur.server.commons.EventTypeEnum;
import com.csc.muthur.server.model.event.AbstractEvent;
import com.csc.muthur.server.model.event.EventAttributeName;
import com.csc.muthur.server.model.event.EventClass;
import com.csc.muthur.server.model.event.EventHandlerName;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class DataSubscriptionRequest extends AbstractEvent {

	private Set<String> dataSubscriptions = new HashSet<String>();

	// name of queue where object events (creates/updates/deletes) will be sent
	// back to the federate based on subscriptions
	private String subscriptionQueueName;

	// name of queue where ownership events will be sent back to the federate
	// based on subscriptions
	private String ownershipEventQueueName;

	/**
	 * 
	 */
	public DataSubscriptionRequest() {
		super();
	}

	/**
	 * Overridden and must call super.initialization()
	 */
	public void initialization() {
		if (dataSubscriptions == null) {
			dataSubscriptions = new HashSet<String>();
		}

		dataSubscriptions.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.AbstractEvent#getDataBlockElement()
	 */
	public Element getDataBlockElement() {

		Element dataBlockElement = null;

		if (isSuccess()) {

			dataBlockElement = new Element(EventAttributeName.dataBlock.toString());

			Element nextEle =
					new Element(EventAttributeName.ownershipEventQueueName.toString())
							.setText(ownershipEventQueueName);

			dataBlockElement.addContent(nextEle);

			nextEle =
					new Element(EventAttributeName.subscriptionQueueName.toString())
							.setText(subscriptionQueueName);

			dataBlockElement.addContent(nextEle);

			nextEle =
					new Element(
							EventAttributeName.federationExecutionModelHandle.toString())
							.setText(getFederationExecutionModelHandle());

			dataBlockElement.addContent(nextEle);

			Element subscriptionsElement =
					new Element(EventAttributeName.subscriptions.toString());

			dataBlockElement.addContent(subscriptionsElement);

			if (dataSubscriptions != null) {

				for (String className : dataSubscriptions) {

					if (className != null) {

						Element classNameElement =
								new Element(EventAttributeName.className.toString())
										.setText(className);

						subscriptionsElement.addContent(classNameElement);
					}
				}
			}
		}

		return dataBlockElement;
	}

	@Override
	public void newElement(String elementName, String elementValue) {

		if ((elementValue != null) && (elementValue.length() > 0)) {

			if (elementName
					.equalsIgnoreCase(EventAttributeName.federationExecutionModelHandle
							.toString())) {
				setFederationExecutionModelHandle(elementValue);
			} else if (elementName.equalsIgnoreCase(EventAttributeName.className
					.toString())) {
				dataSubscriptions.add(elementValue);
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.subscriptionQueueName.toString())) {
				this.subscriptionQueueName = elementValue;
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.ownershipEventQueueName
							.toString())) {
				ownershipEventQueueName = elementValue;
			}

		}
	}

	@Override
	public String getEventTypeDescription() {
		return EventClass.Request.toString();
	}

	@Override
	public String getEventName() {
		return EventTypeEnum.DataSubscriptionRequest.getDescription();
	}

	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.DataSubscriptionRequest;
	}

	/**
	 * 
	 * @param name
	 */
	public void addSubscription(final String className) {

		if (className != null) {

			if (dataSubscriptions == null) {
				dataSubscriptions = new HashSet<String>();
			}

			dataSubscriptions.add(className);
		}
	}

	/**
	 * @return the dataSubscriptions
	 */
	public final Set<String> getDataSubscriptions() {
		return dataSubscriptions;
	}

	/**
	 * @param dataSubscriptions
	 *          the dataSubscriptions to set
	 */
	public final void setDataSubscriptions(Set<String> dataSubscriptions) {
		if (dataSubscriptions != null) {
			this.dataSubscriptions = dataSubscriptions;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.AbstractEvent#getHandler()
	 */
	@Override
	public String getHandler() {
		return EventHandlerName.dataSubscriptionRequestHandler.toString();
	}

	/**
	 * @return the subscriptionQueueName
	 */
	public final String getSubscriptionQueueName() {
		return subscriptionQueueName;
	}

	/**
	 * @param subscriptionQueueName
	 *          the subscriptionQueueName to set
	 */
	public final void setSubscriptionQueueName(String subscriptionQueueName) {
		this.subscriptionQueueName = subscriptionQueueName;
	}

	/**
	 * @param ownershipEventQueueName
	 *          the ownershipEventQueueName to set
	 */
	public void setOwnershipEventQueueName(String ownershipEventQueueName) {
		this.ownershipEventQueueName = ownershipEventQueueName;
	}

	/**
	 * @return the ownershipEventQueueName
	 */
	public String getOwnershipEventQueueName() {
		return ownershipEventQueueName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DataSubscriptionRequest [dataSubscriptions=" + dataSubscriptions
				+ ", federationExecutionModelHandle="
				+ getFederationExecutionModelHandle() + ", ownershipEventQueueName="
				+ ownershipEventQueueName + ", subscriptionQueueName="
				+ subscriptionQueueName + "]";
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
				prime * result
						+ ((dataSubscriptions == null) ? 0 : dataSubscriptions.hashCode());
		result =
				prime
						* result
						+ ((getFederationExecutionModelHandle() == null) ? 0
								: getFederationExecutionModelHandle().hashCode());
		result =
				prime
						* result
						+ ((ownershipEventQueueName == null) ? 0 : ownershipEventQueueName
								.hashCode());
		result =
				prime
						* result
						+ ((subscriptionQueueName == null) ? 0 : subscriptionQueueName
								.hashCode());
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
		DataSubscriptionRequest other = (DataSubscriptionRequest) obj;
		if (dataSubscriptions == null) {
			if (other.dataSubscriptions != null) {
				return false;
			}
		} else if (!dataSubscriptions.equals(other.dataSubscriptions)) {
			return false;
		}
		if (getFederationExecutionModelHandle() == null) {
			if (other.getFederationExecutionModelHandle() != null) {
				return false;
			}
		} else if (!getFederationExecutionModelHandle().equals(
				other.getFederationExecutionModelHandle())) {
			return false;
		}
		if (ownershipEventQueueName == null) {
			if (other.ownershipEventQueueName != null) {
				return false;
			}
		} else if (!ownershipEventQueueName.equals(other.ownershipEventQueueName)) {
			return false;
		}
		if (subscriptionQueueName == null) {
			if (other.subscriptionQueueName != null) {
				return false;
			}
		} else if (!subscriptionQueueName.equals(other.subscriptionQueueName)) {
			return false;
		}
		return true;
	}

}
