package com.csc.muthur.server.model.event;

import java.io.StringReader;
import java.util.UUID;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.commons.XMLSchemaConstants;
import com.csc.muthur.server.commons.exception.MuthurException;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public abstract class AbstractEvent implements IEvent {

	private static final Logger LOG =
			LoggerFactory.getLogger(AbstractEvent.class.getName());

	protected String handlerName =
			EventHandlerName.defaultEventHandler.toString();

	private Document myDocument;
	private Namespace xmlSchema;
	private EventPayloadParser eventPayloadParser;

	/**
	 * controlBlock
	 */
	private String eventType;
	private String eventName;
	private long createEventTimeMSecs;
	private String sourceOfEvent;
	private int timeToLiveSecs;
	private String eventUUID;
	private String status;
	private boolean success;
	private String errorDescription;
	private String registrationHandle;
	private String federationExecutionHandle;
	private String federationExecutionModelHandle;

	/**
	 * 
	 */
	public AbstractEvent() {
		super();
		baseInitialization();
	}

	/**
	 * 
	 */
	private void baseInitialization() {

		this.createEventTimeMSecs = System.currentTimeMillis();
		this.eventUUID = UUID.randomUUID().toString();
		this.eventType = getEventTypeDescription();
		this.eventName = getEventName();

		status = "";
		success = true;

		LOG.debug("Created " + getEventName() + " request");
	}

	/**
	 * Default so that all child classes don't have to provide one.
	 */
	public void initialization() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.IEvent#initialization(java.lang.String)
	 */
	public void initialization(final String objectAsXML) throws MuthurException {
		initialization();
		ingest(objectAsXML);
	}

	/**
	 * @return Document
	 */
	public Document getEventDocument() {

		// add name spaces

		xmlSchema =
				Namespace.getNamespace(XMLSchemaConstants.XSI_PREFIX,
						XMLSchemaConstants.XML_SCHEMA_LOCATION);

		// root element
		Element rootEventElement = new Element(EventAttributeName.event.toString());

		rootEventElement.addNamespaceDeclaration(xmlSchema);

		// add attributes

		rootEventElement.setAttribute(EventAttributeName.type.toString(),
				getEventTypeDescription());

		// create the document from the root element

		myDocument = new Document(rootEventElement);

		// Add the control block element

		Element controlBlockElement =
				new Element(EventAttributeName.controlBlock.toString());

		rootEventElement.addContent(controlBlockElement);

		Element nextEle =
				new Element(EventAttributeName.createEventTimeMSecs.toString())
						.setText(String.valueOf(getCreateTimeMilliSecs()));

		controlBlockElement.addContent(nextEle);

		nextEle =
				new Element(EventAttributeName.success.toString()).setText(Boolean
						.toString(isSuccess()));

		controlBlockElement.addContent(nextEle);

		nextEle =
				new Element(EventAttributeName.eventSource.toString())
						.setText(getSourceOfEvent());

		controlBlockElement.addContent(nextEle);

		nextEle =
				new Element(EventAttributeName.eventName.toString())
						.setText(getEventName());

		controlBlockElement.addContent(nextEle);

		nextEle =
				new Element(EventAttributeName.timeToLiveSecs.toString())
						.setText(String.valueOf(getTimeToLiveSecs()));

		controlBlockElement.addContent(nextEle);

		nextEle =
				new Element(EventAttributeName.eventUUID.toString())
						.setText(getEventUUID());

		controlBlockElement.addContent(nextEle);

		if ((registrationHandle != null) && (!"".equals(registrationHandle))) {
			nextEle =
					new Element(EventAttributeName.registrationHandle.toString())
							.setText(getFederateRegistrationHandle());

			controlBlockElement.addContent(nextEle);
		}

		if ((federationExecutionHandle != null)
				&& (!"".equals(federationExecutionHandle))) {
			nextEle =
					new Element(EventAttributeName.federationExecutionHandle.toString())
							.setText(federationExecutionHandle);

			controlBlockElement.addContent(nextEle);
		}

		if (isSuccess()) {

			// Add the dataBlock section

			Element dataBlockElement = getDataBlockElement();

			if (dataBlockElement != null) {
				rootEventElement.addContent(dataBlockElement);
			}

		} else {

			// or the error block

			Element errorBlockElement = getErrorBlockElement();

			if (errorBlockElement != null) {
				rootEventElement.addContent(errorBlockElement);
			}

		}

		return myDocument;

	}

	@Override
	public Element getDataBlockElement() {
		// default is return null
		return null;
	}

	@Override
	public void newElement(String elementName, String elementValue) {
		// default is no-op
	}

	@Override
	public Element getErrorBlockElement() {

		Element errorBlockElement = null;

		errorBlockElement = new Element(EventAttributeName.errorBlock.toString());

		Element nextEle =
				new Element(EventAttributeName.errorDescription.toString())
						.setText(getErrorDescription());

		errorBlockElement.addContent(nextEle);

		return errorBlockElement;
	}

	@Override
	public void ingest(final String objectAsXML) throws MuthurException {

		if (!("".equals(objectAsXML))) {

			// parse the objectAsXML and populate the object attributes

			try {

				SAXParserFactory factory = SAXParserFactory.newInstance();

				SAXParser saxParser = factory.newSAXParser();

				eventPayloadParser = new EventPayloadParser();

				saxParser.parse(new InputSource(new StringReader(objectAsXML)),
						eventPayloadParser);

			} catch (Exception e) {
				throw new MuthurException(e);
			}

		}
	}

	/**
	 * @return the createTimeMilliSecs
	 */
	public final long getCreateTimeMilliSecs() {
		return createEventTimeMSecs;
	}

	/**
	 * @param createTimeMilliSecs
	 *          the createTimeMilliSecs to set
	 */
	public final void setCreateTimeMilliSecs(long createTimeMilliSecs) {
		this.createEventTimeMSecs = createTimeMilliSecs;
	}

	/**
	 * @return the sourceOfEvent
	 */
	public final String getSourceOfEvent() {
		return sourceOfEvent;
	}

	/**
	 * @param sourceOfEvent
	 *          the sourceOfEvent to set
	 */
	public final void setSourceOfEvent(String sourceOfEvent) {
		this.sourceOfEvent = sourceOfEvent;
	}

	/**
	 * @return the timeToLiveSecs
	 */
	public final int getTimeToLiveSecs() {
		return timeToLiveSecs;
	}

	/**
	 * @param timeToLiveSecs
	 *          the timeToLiveSecs to set
	 */
	public final void setTimeToLiveSecs(int timeToLiveSecs) {
		this.timeToLiveSecs = timeToLiveSecs;
	}

	/**
	 * @return the eventUUID
	 */
	public final String getEventUUID() {
		return eventUUID;
	}

	/**
	 * @param eventUUID
	 *          the eventUUID to set
	 */
	public final void setEventUUID(String eventUUID) {
		this.eventUUID = eventUUID;
	}

	@Override
	public String serialize() {

		Document eventDoc = getEventDocument();

		String eventAsXML = null;

		if (eventDoc != null) {

			XMLOutputter output = new XMLOutputter();
			output.setFormat(Format.getPrettyFormat());

			eventAsXML = output.outputString(eventDoc);

			LOG.debug(eventAsXML);
		}
		return eventAsXML;
	}

	/**
	 * @return the expirationTimeMilliSecs
	 */
	public final long getExpirationTimeMilliSecs() {

		long expirationTimeMilliSecs = -1;

		if (timeToLiveSecs < 0) {
			expirationTimeMilliSecs = -1;
		} else {
			// calculate the expiration date
			expirationTimeMilliSecs = createEventTimeMSecs + timeToLiveSecs * 1000;
		}
		return expirationTimeMilliSecs;
	}

	/**
	 * @return Returns the status.
	 */
	public final String getStatus() {
		return this.status;
	}

	/**
	 * @param status
	 *          The status to set.
	 */
	public final void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return Returns the success.
	 */
	public final boolean isSuccess() {
		return this.success;
	}

	/**
	 * @param success
	 *          The success to set.
	 */
	public final void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * @return Returns the errorDescription.
	 */
	public final String getErrorDescription() {
		return this.errorDescription;
	}

	/**
	 * @param errorDescription
	 *          The errorDescription to set.
	 */
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	/**
	 * 
	 * @author <a href=mailto:wdrew@csc.com>wdrew</a>
	 * @version $Revision$
	 */
	private class EventPayloadParser extends DefaultHandler {

		private String currentElementName;

		private StringBuffer textBuffer;

		/**
     * 
     */
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {

			currentElementName = qName;

			if ("".equals(currentElementName)) {
				currentElementName = localName;
			}

			if ((currentElementName != null) && (!("".equals(currentElementName)))) {
				try {
					startOfElement(currentElementName);
				} catch (MuthurException e) {
					LOG.error(e.getLocalizedMessage());
				}
			}
		}

		/**
     * 
     */
		@Override
		public void characters(char ch[], int start, int length)
				throws SAXException {

			String s = new String(ch, start, length).trim();

			if (s != null) {
				String trimmedString = s.trim();
				if (textBuffer == null) {
					textBuffer = new StringBuffer(trimmedString);
				} else {
					textBuffer.append(trimmedString);
				}
			}
		}

		/*
     * 
     */
		@Override
		public void endElement(String namespaceURI, String sName, // simple name
				String qName // qualified name
		) throws SAXException {

			if ((textBuffer != null) && (textBuffer.length() > 0)) {

				LOG.debug(currentElementName + " = [" + textBuffer.toString() + "]");

				// controlBlock
				if (currentElementName.equalsIgnoreCase(EventAttributeName.eventType
						.toString())) {
					eventType = textBuffer.toString();
				} else if (currentElementName
						.equalsIgnoreCase(EventAttributeName.success.toString())) {
					success = Boolean.valueOf(textBuffer.toString());
				} else if (currentElementName
						.equalsIgnoreCase(EventAttributeName.createEventTimeMSecs
								.toString())) {
					createEventTimeMSecs = Long.valueOf(textBuffer.toString());
				} else if (currentElementName
						.equalsIgnoreCase(EventAttributeName.timeToLiveSecs.toString())) {
					setTimeToLiveSecs(Integer.valueOf(textBuffer.toString()));
				} else if (currentElementName
						.equalsIgnoreCase(EventAttributeName.eventName.toString())) {
					eventName = textBuffer.toString();
				} else if (currentElementName
						.equalsIgnoreCase(EventAttributeName.eventSource.toString())) {
					sourceOfEvent = textBuffer.toString();
				} else if (currentElementName
						.equalsIgnoreCase(EventAttributeName.eventUUID.toString())) {
					eventUUID = textBuffer.toString();
				} else if (currentElementName
						.equalsIgnoreCase(EventAttributeName.registrationHandle.toString())) {
					registrationHandle = textBuffer.toString();
				} else if (currentElementName
						.equalsIgnoreCase(EventAttributeName.federationExecutionHandle
								.toString())) {
					federationExecutionHandle = textBuffer.toString();
				} else if (currentElementName
						.equalsIgnoreCase(EventAttributeName.errorDescription.toString())) {
					errorDescription = textBuffer.toString();
				} else {
					newElement(currentElementName, textBuffer.toString());
				}
			}

			endOfElement(qName);

			textBuffer = null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.xml.sax.helpers.DefaultHandler#ignorableWhitespace(char[], int,
		 * int)
		 */
		@Override
		public void ignorableWhitespace(char[] ch, int start, int length)
				throws SAXException {
		}

		/**
		 * 
		 */
		@Override
		public void endDocument() throws SAXException {
			super.endDocument();
		}

	}

	/**
	 * @throws MuthurException
	 * 
	 */
	public void startOfElement(String currentElementName) throws MuthurException {

	}

	/**
	 * 
	 * @param currentElementName
	 */
	public void endOfElement(String currentElementName) {

	}

	/**
	 * @return the registrationHandle
	 */
	public final String getFederateRegistrationHandle() {
		return registrationHandle;
	}

	/**
	 * 
	 * @param federateRegistrationHandle
	 */
	public final void setFederateRegistrationHandle(
			String federateRegistrationHandle) {
		this.registrationHandle = federateRegistrationHandle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.IEvent#getHandler()
	 */
	@Override
	public String getHandler() {
		return handlerName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.IEvent#setHandler(java.lang.String)
	 */
	@Override
	public void setHandler(String handlerName) {
		this.handlerName = handlerName;
	}

	/**
	 * @return the federationExecutionHandle
	 */
	@Override
	public String getFederationExecutionHandle() {
		return federationExecutionHandle;
	}

	/**
	 * @param federationExecutionHandle
	 *          the federationExecutionHandle to set
	 */
	@Override
	public final void setFederationExecutionHandle(
			String federationExecutionHandle) {
		this.federationExecutionHandle = federationExecutionHandle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AbstractEvent [createTimeMilliSecs=" + createEventTimeMSecs
				+ ", errorDescription=" + errorDescription + ", eventName=" + eventName
				+ ", eventType=" + eventType + ", eventUUID=" + eventUUID
				+ ", handlerName=" + handlerName + ", registrationHandle="
				+ registrationHandle + "federationExecutionHandle="
				+ federationExecutionHandle + ", sourceOfEvent=" + sourceOfEvent
				+ ", status=" + status + ", success=" + success + ", timeToLiveSecs="
				+ timeToLiveSecs + "]";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((eventUUID == null) ? 0 : eventUUID.hashCode());
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
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AbstractEvent other = (AbstractEvent) obj;
		if (eventUUID == null) {
			if (other.eventUUID != null) {
				return false;
			}
		} else if (!eventUUID.equals(other.eventUUID)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the federationExecutionModelHandle
	 */
	@Override
	public final String getFederationExecutionModelHandle() {
		return federationExecutionModelHandle;
	}

	/**
	 * @param federationExecutionModelHandle
	 *            the federationExecutionModelHandle to set
	 */
	@Override
	public final void setFederationExecutionModelHandle(String federationExecutionModelHandle) {
		this.federationExecutionModelHandle = federationExecutionModelHandle;
	}

}