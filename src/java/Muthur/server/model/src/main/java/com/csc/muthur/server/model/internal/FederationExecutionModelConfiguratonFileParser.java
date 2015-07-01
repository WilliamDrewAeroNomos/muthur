package com.csc.muthur.server.model.internal;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.event.EventAttributeName;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class FederationExecutionModelConfiguratonFileParser extends
		DefaultHandler {

	private static final Logger LOG = LoggerFactory
			.getLogger(FederationExecutionModelConfiguratonFileParser.class.getName());

	public Map<String, FederationExecutionModel> uuidToFederationExecutionModel = new ConcurrentHashMap<String, FederationExecutionModel>();

	private String currentElementName;

	private StringBuffer textBuffer;

	private FederationExecutionModel currentFEM;

	private static DateFormat formatter = new SimpleDateFormat(
			"MMM dd HH:mm:ss z yyyy");

	private boolean validParse = true;

	/**
	 * 
	 * @param configFileFullPathName
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public FederationExecutionModelConfiguratonFileParser(
			final String configFileFullPathName) throws ParserConfigurationException,
			SAXException, IOException {

		SAXParserFactory factory = SAXParserFactory.newInstance();

		SAXParser saxParser;

		saxParser = factory.newSAXParser();

		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

		saxParser.parse(configFileFullPathName, this);
	}

	/**
   * 
   */
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		currentElementName = qName;

		if (!"".equals(currentElementName)) {

			if (qName.equalsIgnoreCase(EventAttributeName.fedExecModel.toString())) {

				// create new FEM
				currentFEM = new FederationExecutionModel();

			}
		}

	}

	/**
   * 
   */
	@Override
	public void characters(char ch[], int start, int length) throws SAXException {

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

		if (qName.equalsIgnoreCase(EventAttributeName.fedExecModel.toString())) {

			// add the new FEM to the list
			//
			uuidToFederationExecutionModel.put(currentFEM.getFedExecModelUUID(),
					currentFEM);

		} else if ((textBuffer != null) && (textBuffer.length() > 0)) {

			LOG.debug(currentElementName + " = [" + textBuffer.toString() + "]");

			if (currentElementName.equalsIgnoreCase(EventAttributeName.femName
					.toString())) {

				currentFEM.setName(textBuffer.toString());

			} else if (currentElementName
					.equalsIgnoreCase(EventAttributeName.description.toString())) {

				currentFEM.setDescription(textBuffer.toString());

			} else if (currentElementName
					.equalsIgnoreCase(EventAttributeName.fedExecModelUUID.toString())) {

				currentFEM.setFedExecModelUUID(textBuffer.toString());

			} else if (currentElementName
					.equalsIgnoreCase(EventAttributeName.durationFederationExecutionMSecs
							.toString())) {

				currentFEM.setDurationFederationExecutionMSecs(Long.valueOf(textBuffer
						.toString()));

			} else if (currentElementName
					.equalsIgnoreCase(EventAttributeName.defaultDurationWithinStartupProtocolMSecs
							.toString())) {

				currentFEM.setDefaultDurationWithinStartupProtocolMSecs(Long
						.valueOf(textBuffer.toString()));

			} else if (currentElementName
					.equalsIgnoreCase(EventAttributeName.durationJoinFederationMSecs
							.toString())) {

				currentFEM.setDurationJoinFederationMSecs(Long.valueOf(textBuffer
						.toString()));

			} else if (currentElementName
					.equalsIgnoreCase(EventAttributeName.durationRegisterPublicationMSecs
							.toString())) {

				currentFEM.setDurationRegisterPublicationMSecs(Long.valueOf(textBuffer
						.toString()));

			} else if (currentElementName
					.equalsIgnoreCase(EventAttributeName.durationRegisterToRunMSecs
							.toString())) {

				currentFEM.setDurationRegisterToRunMSecs(Long.valueOf(textBuffer
						.toString()));

			} else if (currentElementName
					.equalsIgnoreCase(EventAttributeName.durationTimeToWaitAfterTermination
							.toString())) {

				currentFEM.setDurationTimeToWaitAfterTerminationMSecs(Long
						.valueOf(textBuffer.toString()));

			} else if (currentElementName
					.equalsIgnoreCase(EventAttributeName.fedExecModelStartDate.toString())) {

				try {

					Date startDate = formatter.parse(textBuffer.toString());

					Calendar federationCalendar = Calendar.getInstance(TimeZone
							.getTimeZone("GMT"));

					federationCalendar.setTime(startDate);

					currentFEM.setLogicalStartTimeMSecs(startDate.getTime());

					LOG.debug("****** Start time for the federation = ["
							+ formatter.format(startDate.getTime()) + "] using MSec value ["
							+ startDate.getTime() + "]");

				} catch (ParseException e) {
					LOG.error("Error parsing date format [" + e.getLocalizedMessage()
							+ "]");
					setValidParse(false);
				}

			} else if (currentElementName
					.equalsIgnoreCase(EventAttributeName.requiredFederate.toString())) {

				currentFEM.addRequiredFededrate(textBuffer.toString());

			} else if (currentElementName
					.equalsIgnoreCase(EventAttributeName.autoStart.toString())) {

				currentFEM.setAutoStart(Boolean.valueOf(textBuffer.toString()));
			}

			textBuffer.setLength(0);
		}
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

	/**
	 * @return the federationExecutionModels
	 */
	public final Map<String, FederationExecutionModel>
			getFederationExecutionModels() {
		return uuidToFederationExecutionModel;
	}

	/**
	 * @param validParse
	 *          the validParse to set
	 */
	public void setValidParse(boolean validParse) {
		this.validParse = validParse;
	}

	/**
	 * @return the validParse
	 */
	public boolean isValidParse() {
		return validParse;
	}

}