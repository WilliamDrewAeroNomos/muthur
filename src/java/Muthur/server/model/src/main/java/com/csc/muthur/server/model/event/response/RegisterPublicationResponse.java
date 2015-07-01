/**
 * 
 */
package com.csc.muthur.server.model.event.response;

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
public class RegisterPublicationResponse extends AbstractEvent {

	private String federationExecutionModelHandle;
	/**
	 * {@link #federationDataPublications} represents the list of publications
	 * that will be available to each federate.
	 */
	private Set<String> federationDataPublications = new HashSet<String>();

	/**
	 * 
	 */
	public RegisterPublicationResponse() {
		super();
	}

	/**
	 * Overridden and must call super.initialization()
	 */
	public void initialization() {
		if (federationDataPublications == null) {
			federationDataPublications = new HashSet<String>();
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
					new Element(EventAttributeName.federationExecutionModelHandle
							.toString()).setText(federationExecutionModelHandle);

			dataBlockElement.addContent(nextEle);

			Element subscriptionsElement =
					new Element(EventAttributeName.subscriptions.toString());

			dataBlockElement.addContent(subscriptionsElement);

			if (federationDataPublications != null) {

				for (String className : federationDataPublications) {

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
				federationExecutionModelHandle = elementValue;
			} else if (elementName.equalsIgnoreCase(EventAttributeName.className
					.toString())) {
				federationDataPublications.add(elementValue);
			}

		}
	}

	@Override
	public String getEventTypeDescription() {
		return EventClass.Response.toString();
	}

	@Override
	public String getEventName() {
		return EventTypeEnum.RegisterPublicationResponse.getDescription();
	}

	@Override
	public EventTypeEnum getEventType() {
		return EventTypeEnum.RegisterPublicationResponse;
	}

	/**
	 * 
	 * @param name
	 */
	public void addPublication(final String className) {

		if (className != null) {

			if (federationDataPublications == null) {
				federationDataPublications = new HashSet<String>();
			}
			federationDataPublications.add(className);
		}
	}

	/**
	 * @return the federationDataPublications
	 */
	public final Set<String> getFederationDataPublications() {
		return this.federationDataPublications;
	}

	/**
	 * @param federationDataPublications
	 *          the federationDataPublications to set
	 */
	public final void setFederationDataPublications(
			Set<String> federationDataPublications) {
		this.federationDataPublications = federationDataPublications;
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
						+ ((federationDataPublications == null) ? 0
								: federationDataPublications.hashCode());
		result =
				prime
						* result
						+ ((federationExecutionModelHandle == null) ? 0
								: federationExecutionModelHandle.hashCode());
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
		RegisterPublicationResponse other = (RegisterPublicationResponse) obj;
		if (federationDataPublications == null) {
			if (other.federationDataPublications != null) {
				return false;
			}
		} else if (!federationDataPublications
				.equals(other.federationDataPublications)) {
			return false;
		}
		if (federationExecutionModelHandle == null) {
			if (other.federationExecutionModelHandle != null) {
				return false;
			}
		} else if (!federationExecutionModelHandle
				.equals(other.federationExecutionModelHandle)) {
			return false;
		}
		return true;
	}
}
