/**
 * 
 */
package com.csc.muthur.server.model.event.data.airport;

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
public abstract class AiportDataObject extends AbstractDataObject {

	private static final Logger LOG = LoggerFactory
			.getLogger(AiportDataObject.class.getName());

	protected String airportCode;

	/**
	 * @param tailNumber
	 * @param callSign
	 */
	public AiportDataObject(final String airportCode) {
		super();

		if ((airportCode == null) || (airportCode.length() == 0)) {
			throw new IllegalArgumentException("Airport code was null or empty");
		}

		this.airportCode = airportCode;
	}

	/**
	 * 
	 */
	public AiportDataObject() {
		super();
	}

	/**
	 * @return the airportCode
	 */
	public final String getAirportCode() {
		return airportCode;
	}

	/**
	 * @param airportCode
	 *          the airportCode to set
	 */
	public final void setAirportCode(String airportCode) {
		this.airportCode = airportCode;
	}

	/**
	 * The default for data objects is an aircraft element as the ID element. The
	 * ID element is a child to the dataObjectAsXML element
	 */
	public final void getIDElementContents(Element idParentElement) {

		if (idParentElement != null) {

			// add the airport code as the natural key

			Element nextEle =
					new Element(EventAttributeName.airportCode.toString())
							.setText(getAirportCode());
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
		return new String(airportCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.data.AbstractDataObject#validate()
	 */
	public boolean validate() {

		// airportCode
		if (airportCode == null) {
			LOG.warn("Validation failed: UUID was null.");
			return false;
		}
		if (airportCode.length() == 0) {
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
		int result = super.hashCode();
		result =
				prime * result + ((airportCode == null) ? 0 : airportCode.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AiportDataObject other = (AiportDataObject) obj;
		if (airportCode == null) {
			if (other.airportCode != null)
				return false;
		} else if (!airportCode.equals(other.airportCode))
			return false;
		return true;
	}

}