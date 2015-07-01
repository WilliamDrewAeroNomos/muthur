/**
 * 
 */
package com.csc.muthur.server.model.event.data.weather;

import org.jdom.Element;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.event.EventAttributeName;
import com.csc.muthur.server.model.event.data.AbstractDataObject;
import com.csc.muthur.server.model.event.data.DataTypeEnum;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class WeatherDataObject extends AbstractDataObject {

	private String airportCode;
	private String atisCode;
	private double centerFieldWindSpeedKts;

	private String centerFieldWind;
	// TODO: this needs to be parsed into it's individual fields
	private String metarString;
	private double altimeterReading;
	private String atisMessage;

	/**
	 * @throws MuthurException
	 */
	public WeatherDataObject(final String airportCode) throws MuthurException {
		super();

		if ((airportCode == null) || (airportCode.length() == 0)) {
			throw new MuthurException("Airport code was null or empty");
		}
		this.airportCode = airportCode;
	}

	/**
	 */
	public WeatherDataObject() {
		super();
	}

	/**
	 * @return the airportCode
	 */
	public String getAirportCode() {
		return airportCode;
	}

	/**
	 * @return the altimeterReading
	 */
	public double getAltimeterReading() {
		return altimeterReading;
	}

	/**
	 * @param altimeterReading
	 *          the altimeterReading to set
	 */
	public void setAltimeterReading(double altimeterReading) {
		this.altimeterReading = altimeterReading;
	}

	/**
	 * @param airportCode
	 *          the airportCode to set
	 */
	public void setAirportCode(String airportCode) {
		this.airportCode = airportCode;
	}

	/**
	 * @return the atisCode
	 */
	public String getAtisCode() {
		return atisCode;
	}

	/**
	 * @param atisCode
	 *          the atisCode to set
	 */
	public void setAtisCode(String atisCode) {
		this.atisCode = atisCode;
	}

	/**
	 * @return the centerFieldWindSpeedKts
	 */
	public double getCenterFieldWindSpeedKts() {
		return centerFieldWindSpeedKts;
	}

	/**
	 * @param centerFieldWindSpeedKts
	 *          the centerFieldWindSpeed to set
	 */
	public void setCenterFieldWindSpeedKts(double centerFieldWindSpeedKts) {
		this.centerFieldWindSpeedKts = centerFieldWindSpeedKts;
	}

	/**
	 * @return the centerFieldWind
	 */
	public String getCenterFieldWind() {
		return centerFieldWind;
	}

	/**
	 * @param centerFieldWind
	 *          the centerFieldWind to set
	 */
	public void setCenterFieldWind(String centerFieldWind) {
		this.centerFieldWind = centerFieldWind;
	}

	/**
	 * @return the metarString
	 */
	public String getMetarString() {
		return metarString;
	}

	/**
	 * @param metarString
	 *          the metarString to set
	 */
	public void setMetarString(String metarString) {
		this.metarString = metarString;
	}

	/**
	 * @return the atisMessage
	 */
	public String getAtisMessage() {
		return atisMessage;
	}

	/**
	 * @param atisMessage
	 *          the atisMessage to set
	 */
	public void setAtisMessage(String atisMessage) {
		this.atisMessage = atisMessage;
	}

	/**
	 * Construct the natural key for this class
	 */
	public final void getIDElementContents(Element idParentElement) {

		if (idParentElement != null) {

			// airport code

			Element nextEle =
					new Element(EventAttributeName.airportCode.toString())
							.setText(getAirportCode());
			idParentElement.addContent(nextEle);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.IBaseDataObject#getDataBlockElement()
	 */
	@Override
	public void addDataBlockElements(Element dataBlockElement) {

		if (dataBlockElement != null) {

			// weather data section

			Element weatherDataElement =
					new Element(EventAttributeName.weatherData.toString());

			Element nextEle =
					new Element(EventAttributeName.atisCode.toString())
							.setText(getAtisCode());
			weatherDataElement.addContent(nextEle);

			nextEle =
					new Element(EventAttributeName.metarString.toString())
							.setText(getMetarString());
			weatherDataElement.addContent(nextEle);

			nextEle =
					new Element(EventAttributeName.centerFieldWind.toString())
							.setText(getCenterFieldWind());
			weatherDataElement.addContent(nextEle);

			nextEle =
					new Element(EventAttributeName.centerFieldWindSpeedKts.toString())
							.setText(Double.toString(getCenterFieldWindSpeedKts()));
			weatherDataElement.addContent(nextEle);

			nextEle =
					new Element(EventAttributeName.altimeterReading.toString())
							.setText(Double.toString(getAltimeterReading()));
			weatherDataElement.addContent(nextEle);

			nextEle =
					new Element(EventAttributeName.atisMessage.toString())
							.setText(getAtisMessage());
			weatherDataElement.addContent(nextEle);

			// add the weather data elements to the data block element

			dataBlockElement.addContent(weatherDataElement);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.csc.muthur.server.model.event.data.AbstractDataObject#newElement(java.lang
	 * .String, java.lang.String)
	 */
	@Override
	public void newElement(String elementName, String elementValue) {

		if ((elementValue != null) && (elementValue.length() > 0)) {

			// flight plan data

			if (elementName.equalsIgnoreCase(EventAttributeName.airportCode
					.toString())) {
				airportCode = elementValue;
			} else if (elementName.equalsIgnoreCase(EventAttributeName.atisCode
					.toString())) {
				atisCode = elementValue;
			} else if (elementName.equalsIgnoreCase(EventAttributeName.metarString
					.toString())) {
				metarString = elementValue;
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.centerFieldWindSpeedKts
							.toString())) {
				centerFieldWindSpeedKts = Double.valueOf(elementValue);
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.centerFieldWind.toString())) {
				centerFieldWind = elementValue;
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.altimeterReading.toString())) {
				altimeterReading = Double.valueOf(elementValue);
			} else if (elementName.equalsIgnoreCase(EventAttributeName.atisMessage
					.toString())) {
				atisMessage = elementValue;
			}
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
	 * @see com.csc.muthur.server.model.event.data.IBaseDataObject#getDataType()
	 */
	@Override
	public DataTypeEnum getDataType() {
		return DataTypeEnum.Weather;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.data.IBaseDataObject#validate()
	 */
	@Override
	public boolean validate() {
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
		result = prime * result + ((atisCode == null) ? 0 : atisCode.hashCode());
		result =
				prime * result
						+ ((centerFieldWind == null) ? 0 : centerFieldWind.hashCode());
		long temp;
		temp = Double.doubleToLongBits(centerFieldWindSpeedKts);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result =
				prime * result + ((metarString == null) ? 0 : metarString.hashCode());
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
		WeatherDataObject other = (WeatherDataObject) obj;
		if (airportCode == null) {
			if (other.airportCode != null)
				return false;
		} else if (!airportCode.equals(other.airportCode))
			return false;
		if (atisCode == null) {
			if (other.atisCode != null)
				return false;
		} else if (!atisCode.equals(other.atisCode))
			return false;
		if (centerFieldWind == null) {
			if (other.centerFieldWind != null)
				return false;
		} else if (!centerFieldWind.equals(other.centerFieldWind))
			return false;
		if (Double.doubleToLongBits(centerFieldWindSpeedKts) != Double
				.doubleToLongBits(other.centerFieldWindSpeedKts))
			return false;
		if (metarString == null) {
			if (other.metarString != null)
				return false;
		} else if (!metarString.equals(other.metarString))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "WeatherDataObject [airportCode=" + airportCode + ", atisCode="
				+ atisCode + ", centerFieldWindSpeedKts=" + centerFieldWindSpeedKts
				+ ", centerFieldWind=" + centerFieldWind + ", metarString="
				+ metarString + ", altimeterReading=" + altimeterReading
				+ ", atisMessage=" + atisMessage + "]";
	}

}