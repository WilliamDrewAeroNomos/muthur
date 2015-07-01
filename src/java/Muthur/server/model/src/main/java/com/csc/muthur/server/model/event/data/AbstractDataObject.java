/**
 * 
 */
package com.csc.muthur.server.model.event.data;

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

import com.csc.muthur.server.commons.XMLSchemaConstants;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.event.EventAttributeName;
import com.csc.muthur.server.model.event.EventClass;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public abstract class AbstractDataObject implements IBaseDataObject {

	private static final Logger LOG = LoggerFactory
			.getLogger(AbstractDataObject.class.getName());

	private Namespace xmlSchema;
	private Document myDocument;

	/**
	 * creation time for the data object
	 */
	long objectCreateTimeMSecs;

	/**
	 * object ID
	 */
	String dataObjectUUID;

	/**
	 * 
	 */
	public AbstractDataObject() {
		super();
		this.dataObjectUUID = UUID.randomUUID().toString();
		this.objectCreateTimeMSecs = System.currentTimeMillis();
	}

	/**
	 * @return the objectUUID
	 */
	public final String getDataObjectUUID() {
		return dataObjectUUID;
	}

	/**
	 * @param dataObjectUUID
	 *          the dataObjectUUID to set
	 */
	public final void setDataObjectUUID(String dataObjectUUID) {
		this.dataObjectUUID = dataObjectUUID;
	}

	/**
	 * @return the getObjectCreateTimeMSecs
	 */
	public final long getObjectCreateTimeMSecs() {
		return objectCreateTimeMSecs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.IBaseDataObject#serialize()
	 */
	public String serialize() {

		Document eventDoc = createDocument();

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
	 * @return Document
	 */
	private Document createDocument() {

		// add name spaces

		xmlSchema =
				Namespace.getNamespace(XMLSchemaConstants.XSI_PREFIX,
						XMLSchemaConstants.XML_SCHEMA_LOCATION);

		// root element
		Element rootEventElement = new Element(EventAttributeName.event.toString());

		rootEventElement.addNamespaceDeclaration(xmlSchema);

		// add type attribute

		rootEventElement.setAttribute(EventAttributeName.type.toString(),
				getEventName());

		// add name attribute

		rootEventElement.setAttribute(EventAttributeName.name.toString(),
				EventClass.Data.toString());

		// create the document from the root element

		myDocument = new Document(rootEventElement);

		// Add the dataBlock section in the sub-class

		Element dataBlockElement =
				new Element(EventAttributeName.dataBlock.toString());

		Element dataObjectAsXMLElement =
				new Element(EventAttributeName.dataObjectAsXML.toString());

		dataBlockElement.addContent(dataObjectAsXMLElement);

		// build out the dataObjectAsXML element

		buildDataObjectAsXMLElement(dataObjectAsXMLElement);

		// add the dataBlock element to the root element

		rootEventElement.addContent(dataBlockElement);

		return myDocument;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.data.IBaseDataObject#
	 * buildDataObjectAsXMLElement (org.jdom.Element)
	 */
	@Override
	public void buildDataObjectAsXMLElement(Element dataObjectAsXMLElement) {

		Element nextEle =
				new Element(EventAttributeName.objectCreateTimeMSecs.toString())
						.setText(String.valueOf(getObjectCreateTimeMSecs()));

		dataObjectAsXMLElement.addContent(nextEle);

		nextEle =
				new Element(EventAttributeName.dataObjectUUID.toString())
						.setText(String.valueOf(getDataObjectUUID()));

		dataObjectAsXMLElement.addContent(nextEle);

		// add the aircraft ID block and elements required for all classes
		// standard aircraft ID section

		Element idParentElement =
				new Element(EventAttributeName.naturalKey.toString());

		getIDElementContents(idParentElement);

		dataObjectAsXMLElement.addContent(idParentElement);

		// let the sub-class add the specific elements for that class

		addDataBlockElements(dataObjectAsXMLElement);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.model.event.data.IBaseDataObject#addDataBlockElements
	 * (org .jdom.Element)
	 */
	public void addDataBlockElements(Element dataBlockElement) {
		// empty default
	}

	/**
	 * @throws MuthurException
	 * 
	 */
	public void initialization(final String objectAsXML) throws MuthurException {

		if (!("".equals(objectAsXML))) {

			// parse the objectAsXML and populate the object attributes

			try {

				SAXParserFactory factory = SAXParserFactory.newInstance();

				SAXParser saxParser = factory.newSAXParser();

				saxParser.parse(new InputSource(new StringReader(objectAsXML)),
						new DataObjectPayloadParser());

			} catch (Exception e) {
				throw new MuthurException(e);
			}

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.data.IBaseDataObject#getEventName()
	 */
	@Override
	public String getEventName() {
		return getDataType().toString();
	}

	/**
	 * 
	 * @author <a href=mailto:wdrew@csc.com>wdrew</a>
	 * @version $Revision$
	 */
	private class DataObjectPayloadParser extends DefaultHandler {

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
				startOfElement(currentElementName);
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
		public void endElement(String namespaceURI, // uri
				String sName, // simple name
				String qName // qualified name
				) throws SAXException {

			if ((textBuffer != null) && (textBuffer.length() > 0)) {

				LOG.debug(currentElementName + " = [" + textBuffer.toString() + "]");

				// controlBlock
				if (currentElementName
						.equalsIgnoreCase(EventAttributeName.dataObjectUUID.toString())) {
					dataObjectUUID = textBuffer.toString();
				} else if (currentElementName
						.equalsIgnoreCase(EventAttributeName.objectCreateTimeMSecs
								.toString())) {
					objectCreateTimeMSecs = Long.valueOf(textBuffer.toString());
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

	@Override
	public void startOfElement(String currentElementName) {
		// default is no-op
	}

	@Override
	public void endOfElement(String currentElementName) {
		// default is no-op
	}

	@Override
	public void newElement(String elementName, String elementValue) {
		// default is no-op
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.data.IBaseDataObject#validate()
	 */
	@Override
	public boolean validate() {

		// uuid
		if (dataObjectUUID == null) {
			LOG.warn("Validation failed: UUID was null.");
			return false;
		}
		if (dataObjectUUID.length() == 0) {
			LOG.warn("Validation failed: UUID was empty.");
			return false;
		}

		return true;
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
		result =
				prime * result
						+ ((dataObjectUUID == null) ? 0 : dataObjectUUID.hashCode());
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
		AbstractDataObject other = (AbstractDataObject) obj;
		if (dataObjectUUID == null) {
			if (other.dataObjectUUID != null) {
				return false;
			}
		} else if (!dataObjectUUID.equals(other.dataObjectUUID)) {
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
		return "AbstractDataObject [dataObjectUUID=" + dataObjectUUID + "]";
	}
}