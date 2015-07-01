/**
 * 
 */
package com.csc.muthur.server.model.event;

import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.internal.ModelServiceImpl;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public final class EventFactory {

	private final static Logger LOG = LoggerFactory.getLogger(EventFactory.class
			.getName());

	private static ApplicationContext applicationContext = null;
	private static EventFactory INSTANCE = null;
	private static SAXParserFactory SAX_PARSER_FACTORY_INSTANCE = null;

	/**
	 * 
	 * @return {@link #INSTANCE} as the single instance of the
	 *         {@link EventFactory}
	 */
	public static EventFactory getInstance() {

		if (INSTANCE == null) {
			INSTANCE = new EventFactory();
		}

		return INSTANCE;

	}

	/**
	 * 
	 * @return {@link #applicationContext} as the application context for the
	 *         federation data model.
	 * @throws MuthurException
	 */
	private static ApplicationContext getApplicationContext()
			throws MuthurException {

		if (applicationContext == null) {

			applicationContext = ModelServiceImpl.getApplicationContext();

			if (applicationContext == null) {
				applicationContext =
						new ClassPathXmlApplicationContext(
								"/META-INF/spring/model-event-context.xml");
			}

			if (applicationContext != null) {
				LOG.debug("Loaded model application context.");
			} else {
				LOG.error("Unable to load model application context.");
				throw new MuthurException(
						"Unable to load model application context in EventFactory.");
			}
		}
		return applicationContext;
	}

	/**
	 * 
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 */
	private static SAXParserFactory getSAXParserFactory() {

		if (SAX_PARSER_FACTORY_INSTANCE == null) {
			SAX_PARSER_FACTORY_INSTANCE = SAXParserFactory.newInstance();
		}

		return SAX_PARSER_FACTORY_INSTANCE;
	}

	/**
	 * Takes a serialized {@link IEvent} object as a string and returns the
	 * {@link IEvent} which it represents. The {@link EventParser} class parses
	 * the input string which is assumed to be a valid XML document for the value
	 * of the element {@link EventAttributeName#eventName}. Once the parsing has
	 * been completed {@link EventParser#isEventNameFound()} is called to
	 * determine if an event name value was found in the XML. If one has been
	 * found then {@link EventParser#getEventName()} is called to retrieve the
	 * value of the event name. This name is used to retrieve the associated
	 * object from the Spring context via a call to
	 * {@link ApplicationContext#getBean(String)}. If a valid bean is found int
	 * the context then {@link IEvent#initialization(String)} is called passing in
	 * the original eventAsXML parameter. The eventAsXML populates the object and
	 * returns a fully formed {@link IEvent} object.
	 * 
	 * @param eventAsXML
	 *          An {@link IEvent} serialized as an XML document.
	 * @return The {@link IEvent} which the eventAsXML parameter the serialized
	 *         event represents.
	 * @throws MuthurException
	 */
	public IEvent createEvent(final String eventAsXML) throws MuthurException {

		IEvent event = null;
		EventParser eventParser = null;

		try {

			SAXParser saxParser = getSAXParserFactory().newSAXParser();

			eventParser = new EventParser();

			saxParser.parse(new InputSource(new StringReader(eventAsXML)),
					eventParser);

			if (!eventParser.isEventNameFound()) {
				throw new MuthurException(
						"Unable to parse an event from the serialized event [" + eventAsXML
								+ "].");
			}

		} catch (Exception e) {
			LOG.error("XML event [" + eventAsXML + "]");
			throw new MuthurException(e);
		}

		event =
				(IEvent) getApplicationContext().getBean(eventParser.getEventName());

		if (event != null) {
			event.initialization(eventAsXML);
			LOG.debug("Created [" + event.getEventName() + "] event.");
		}

		return event;
	}

	/**
	 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
	 * @version $Revision$
	 * 
	 */
	private class EventParser extends DefaultHandler {

		private boolean eventNameFound = false;
		private StringBuffer eventNameBuffer = null;

		/**
		 * 
		 */
		public EventParser() {

		}

		/**
		 * 
		 */
		@Override
		public void startDocument() throws SAXException {
			eventNameBuffer = null;
			eventNameFound = false;
		}

		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {

			if ((qName.equalsIgnoreCase(EventAttributeName.eventName.toString()) || localName
					.equalsIgnoreCase(EventAttributeName.eventName.toString()))) {
				eventNameFound = true;
			}
		}

		/**
		 * 
		 */
		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {

			if (eventNameFound) {
				String s = new String(ch, start, length).trim();

				if (s != null) {
					String trimmedString = s.trim();
					if (eventNameBuffer == null) {
						eventNameBuffer = new StringBuffer(trimmedString);
					} else {
						eventNameBuffer.append(trimmedString);
					}
				}
			}
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			eventNameFound = false;
		}

		/**
		 * @return the eventNameFound
		 */
		public boolean isEventNameFound() {
			return (eventNameBuffer.length() > 0);
		}

		/**
		 * @return the event name parsed from the serialized XML
		 */
		public String getEventName() {
			return eventNameBuffer.toString();
		}
	}

	/**
	 * no instantiation
	 */
	private EventFactory() {

	}
}
