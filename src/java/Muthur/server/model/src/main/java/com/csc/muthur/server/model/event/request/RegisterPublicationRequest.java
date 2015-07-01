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

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class RegisterPublicationRequest extends AbstractEvent {

	private Set<String> dataPublications = new HashSet<String>();

	/**
	 * 
	 */
	public RegisterPublicationRequest() {
		super();
	}

	/**
	 * Overridden and must call super.initialization()
	 */
	public void initialization() {
		if (dataPublications == null) {
			dataPublications = new HashSet<String>();
		}
		super.initialization();
	}

	/**
	 * 
	 */
	public Element getDataBlockElement() {

		Element dataBlockElement = null;

		if (isSuccess()) {

			dataBlockElement = new Element(EventAttributeName.dataBlock.toString());

			Element nextEle =
					new Element(
							EventAttributeName.federationExecutionModelHandle.toString())
							.setText(getFederationExecutionModelHandle());

			dataBlockElement.addContent(nextEle);

			Element subscriptionsElement =
					new Element(EventAttributeName.subscriptions.toString());

			dataBlockElement.addContent(subscriptionsElement);

			if (dataPublications != null) {

				for (String className : dataPublications) {

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
				dataPublications.add(elementValue);
			}

		}
	}

	@Override
	public String getEventTypeDescription() {
		return EventClass.Request.toString();
	}

	@Override
	public String getEventName() {
		return EventTypeEnum.RegisterPublicationRequest.getDescription();
	}

	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.RegisterPublicationRequest;
	}

	/**
	 * 
	 * @param name
	 */
	public void addPublication(final String className) {

		if (className != null) {

			if (dataPublications == null) {
				dataPublications = new HashSet<String>();
			}
			dataPublications.add(className);
		}
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
						+ ((dataPublications == null) ? 0 : dataPublications.hashCode());
		result =
				prime
						* result
						+ ((getFederationExecutionModelHandle() == null) ? 0
								: getFederationExecutionModelHandle().hashCode());
		return result;
	}

	/**
	 * @return the dataPublications
	 */
	public final Set<String> getDataPublications() {
		return this.dataPublications;
	}

	/**
	 * @param dataPublications
	 *          the dataPublications to set
	 */
	public final void setDataPublications(Set<String> dataPublications) {
		this.dataPublications = dataPublications;
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
		RegisterPublicationRequest other = (RegisterPublicationRequest) obj;
		if (dataPublications == null) {
			if (other.dataPublications != null) {
				return false;
			}
		} else if (!dataPublications.equals(other.dataPublications)) {
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
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RegisterPublicationRequest [dataPublications=" + dataPublications
				+ ", federationExecutionModelHandle="
				+ getFederationExecutionModelHandle() + "]";
	}

}
