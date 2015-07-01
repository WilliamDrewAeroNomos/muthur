/**
 * 
 */
package com.csc.muthur.server.model.event.data.flight;

import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.model.event.EventAttributeName;
import com.csc.muthur.server.model.event.data.AbstractDataObject;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public abstract class FlightDataObject extends AbstractDataObject {

	private static final Logger LOG = LoggerFactory.getLogger(FlightDataObject.class
			.getName());
	
	protected String tailNumber;
	protected String callSign;

	/**
	 * @param tailNumber
	 * @param callSign
	 */
	public FlightDataObject(String tailNumber, String callSign) {
		super();

		if ((tailNumber == null) || (tailNumber.length() == 0)) {
			throw new IllegalArgumentException("Tail number null or empty");
		}

		if ((callSign == null) || (callSign.length() == 0)) {
			throw new IllegalArgumentException("Call sign null or empty");
		}

		this.tailNumber = tailNumber;
		this.callSign = callSign;
	}

	/**
	 * 
	 */
	public FlightDataObject() {
		super();
	}

	/**
	 * @return the tailNumber
	 */
	public String getTailNumber() {
		return tailNumber;
	}

	/**
	 * @param tailNumber
	 *          the tailNumber to set
	 */
	public void setTailNumber(String tailNumber) {
		this.tailNumber = tailNumber;
	}

	/**
	 * @return the callSign
	 */
	public String getCallSign() {
		return callSign;
	}

	/**
	 * @param callSign
	 *          the callSign to set
	 */
	public void setCallSign(String callSign) {
		this.callSign = callSign;
	}

	/**
	 * The default for data objects is an aircraft element as the ID element. The
	 * ID element is a child to the dataObjectAsXML element
	 */
	public final void getIDElementContents(Element idParentElement) {

		if (idParentElement != null) {

			// standard flight data object ID section

			Element nextEle =
					new Element(EventAttributeName.callSign.toString())
							.setText(getCallSign());
			idParentElement.addContent(nextEle);

			nextEle =
					new Element(EventAttributeName.tailNumber.toString())
							.setText(getTailNumber());
			idParentElement.addContent(nextEle);

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.data.IBaseDataObject#getNaturalKey()
	 */
	@Override
	public String getNaturalKey() {
		return new String(callSign);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.data.IBaseDataObject#validation()
	 */
	@Override
	public boolean validate() {

		if (!super.validate()) {
			return false;
		}

		// call sign
		if (callSign == null) {
			LOG.warn("Validation failed: Call sign was null.");
			return false;
		}
		if (callSign.length() == 0) {
			LOG.warn("Validation failed: Call sign was empty.");
			return false;
		}

		// tail number
		if (tailNumber == null) {
			LOG.warn("Validation failed: Tail number was null.");
			return false;
		}
		if (tailNumber.length() == 0) {
			LOG.warn("Validation failed: Tail number was empty.");
			return false;
		}

		return true;
	}

}