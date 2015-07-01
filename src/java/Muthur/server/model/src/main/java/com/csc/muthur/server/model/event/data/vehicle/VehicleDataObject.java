/**
 * 
 */
package com.csc.muthur.server.model.event.data.vehicle;

import org.jdom.Element;

import com.csc.muthur.server.model.event.EventAttributeName;
import com.csc.muthur.server.model.event.data.AbstractDataObject;
import com.csc.muthur.server.model.event.data.DataTypeEnum;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class VehicleDataObject extends AbstractDataObject {

	private String airportCode;
	private String beaconCode;

	private double latitudeDegrees;
	private double longitudeDegrees;
	private double altitudeFt;

	private double groundspeedKts;
	private double headingDegrees;

	private int vehicleTransmissionFrequency;

	/**
	 */
	public VehicleDataObject() {
		super();
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

			// beacon code

			nextEle =
					new Element(EventAttributeName.beaconCode.toString())
							.setText(getBeaconCode());
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
		return new String(airportCode + "-" + beaconCode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.data.IBaseDataObject#getDataType()
	 */
	@Override
	public DataTypeEnum getDataType() {
		return DataTypeEnum.Vehicle;
	}

	/**
	 * @return the airportCode
	 */
	public String getAirportCode() {
		return airportCode;
	}

	/**
	 * @param airportCode
	 *          the airportCode to set
	 */
	public void setAirportCode(String airportCode) {
		this.airportCode = airportCode;
	}

	/**
	 * @return the beaconCode
	 */
	public String getBeaconCode() {
		return beaconCode;
	}

	/**
	 * @param beaconCode
	 *          the beaconCode to set
	 */
	public void setBeaconCode(String beaconCode) {
		this.beaconCode = beaconCode;
	}

	/**
	 * @return the latitudeDegrees
	 */
	public double getLatitudeDegrees() {
		return latitudeDegrees;
	}

	/**
	 * @param latitudeDegrees
	 *          the latitudeDegrees to set
	 */
	public void setLatitudeDegrees(double latitudeDegrees) {
		this.latitudeDegrees = latitudeDegrees;
	}

	/**
	 * @return the longitudeDegrees
	 */
	public double getLongitudeDegrees() {
		return longitudeDegrees;
	}

	/**
	 * @param longitudeDegrees
	 *          the longitudeDegrees to set
	 */
	public void setLongitudeDegrees(double longitudeDegrees) {
		this.longitudeDegrees = longitudeDegrees;
	}

	/**
	 * @return the altitudeFt
	 */
	public double getAltitudeFt() {
		return altitudeFt;
	}

	/**
	 * @param altitudeFt
	 *          the altitudeFt to set
	 */
	public void setAltitudeFt(double altitudeFt) {
		this.altitudeFt = altitudeFt;
	}

	/**
	 * @return the groundspeedKts
	 */
	public double getGroundspeedKts() {
		return groundspeedKts;
	}

	/**
	 * @param groundspeedKts
	 *          the groundspeedKts to set
	 */
	public void setGroundspeedKts(double groundspeedKts) {
		this.groundspeedKts = groundspeedKts;
	}

	/**
	 * @return the headingDegrees
	 */
	public double getHeadingDegrees() {
		return headingDegrees;
	}

	/**
	 * @param headingDegrees
	 *          the headingDegrees to set
	 */
	public void setHeadingDegrees(double headingDegrees) {
		this.headingDegrees = headingDegrees;
	}

	/**
	 * @return the vehicleTransmissionFrequency
	 */
	public int getVehicleTransmissionFrequency() {
		return vehicleTransmissionFrequency;
	}

	/**
	 * @param vehicleTransmissionFrequency
	 *          the vehicleTransmissionFrequency to set
	 */
	public void setVehicleTransmissionFrequency(int vehicleTransmissionFrequency) {
		this.vehicleTransmissionFrequency = vehicleTransmissionFrequency;
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
					new Element(EventAttributeName.vehicleData.toString());

			Element nextEle =
					new Element(EventAttributeName.altitudeFt.toString()).setText(Double
							.toString(getAltitudeFt()));
			weatherDataElement.addContent(nextEle);

			nextEle =
					new Element(EventAttributeName.latitudeDegrees.toString())
							.setText(Double.toString(getLatitudeDegrees()));
			weatherDataElement.addContent(nextEle);

			nextEle =
					new Element(EventAttributeName.longitudeDegrees.toString())
							.setText(Double.toString(getLongitudeDegrees()));
			weatherDataElement.addContent(nextEle);

			nextEle =
					new Element(EventAttributeName.groundspeedKts.toString())
							.setText(Double.toString(getGroundspeedKts()));
			weatherDataElement.addContent(nextEle);

			nextEle =
					new Element(EventAttributeName.headingDegrees.toString())
							.setText(Double.toString(getHeadingDegrees()));
			weatherDataElement.addContent(nextEle);

			nextEle =
					new Element(
							EventAttributeName.vehicleTransmissionFrequency.toString())
							.setText(Integer.toString(getVehicleTransmissionFrequency()));
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

		// private int vehicleTransmissionFrequency;

		if ((elementValue != null) && (elementValue.length() > 0)) {

			// vehicle data

			if (elementName.equalsIgnoreCase(EventAttributeName.airportCode
					.toString())) {
				airportCode = elementValue;
			} else if (elementName.equalsIgnoreCase(EventAttributeName.beaconCode
					.toString())) {
				beaconCode = elementValue;
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.latitudeDegrees.toString())) {
				latitudeDegrees = Double.valueOf(elementValue);
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.longitudeDegrees.toString())) {
				longitudeDegrees = Double.valueOf(elementValue);
			} else if (elementName.equalsIgnoreCase(EventAttributeName.altitudeFt
					.toString())) {
				altitudeFt = Double.valueOf(elementValue);
			} else if (elementName.equalsIgnoreCase(EventAttributeName.groundspeedKts
					.toString())) {
				groundspeedKts = Double.valueOf(elementValue);
			} else if (elementName.equalsIgnoreCase(EventAttributeName.headingDegrees
					.toString())) {
				headingDegrees = Double.valueOf(elementValue);
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.vehicleTransmissionFrequency
							.toString())) {
				vehicleTransmissionFrequency = Integer.valueOf(elementValue);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.data.IBaseDataObject#validation()
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
		long temp;
		temp = Double.doubleToLongBits(altitudeFt);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result =
				prime * result + ((beaconCode == null) ? 0 : beaconCode.hashCode());
		temp = Double.doubleToLongBits(groundspeedKts);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(headingDegrees);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(latitudeDegrees);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitudeDegrees);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + vehicleTransmissionFrequency;
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
		VehicleDataObject other = (VehicleDataObject) obj;
		if (airportCode == null) {
			if (other.airportCode != null)
				return false;
		} else if (!airportCode.equals(other.airportCode))
			return false;
		if (Double.doubleToLongBits(altitudeFt) != Double
				.doubleToLongBits(other.altitudeFt))
			return false;
		if (beaconCode == null) {
			if (other.beaconCode != null)
				return false;
		} else if (!beaconCode.equals(other.beaconCode))
			return false;
		if (Double.doubleToLongBits(groundspeedKts) != Double
				.doubleToLongBits(other.groundspeedKts))
			return false;
		if (Double.doubleToLongBits(headingDegrees) != Double
				.doubleToLongBits(other.headingDegrees))
			return false;
		if (Double.doubleToLongBits(latitudeDegrees) != Double
				.doubleToLongBits(other.latitudeDegrees))
			return false;
		if (Double.doubleToLongBits(longitudeDegrees) != Double
				.doubleToLongBits(other.longitudeDegrees))
			return false;
		if (vehicleTransmissionFrequency != other.vehicleTransmissionFrequency)
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
		return "VehicleDataObject [airportCode=" + airportCode + ", beaconCode="
				+ beaconCode + ", latitudeDegrees=" + latitudeDegrees
				+ ", longitudeDegrees=" + longitudeDegrees + ", altitudeFt="
				+ altitudeFt + ", groundspeedKts=" + groundspeedKts
				+ ", headingDegrees=" + headingDegrees
				+ ", vehicleTransmissionFrequency=" + vehicleTransmissionFrequency
				+ "]";
	}

}