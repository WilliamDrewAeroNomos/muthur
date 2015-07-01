/**
 * 
 */
package com.csc.muthur.server.model.event.data.flight;

import org.jdom.Element;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.event.EventAttributeName;
import com.csc.muthur.server.model.event.data.DataTypeEnum;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class FlightPosition extends FlightDataObject {

	/**
	 * position data
	 */
	private double latitudeDegrees;
	private double longitudeDegrees;
	private double altitudeFt;

	private double groundspeedKts;
	private double headingDegrees;
	private double airspeedKts;

	private double pitchDegrees;
	private double rollDegrees;
	private double yawDegrees;

	private String sector;
	private String center;
	private double verticalspeedKts;

	private boolean aircraftOnGround;
	private int aircraftTransmissionFrequency;
	private String squawkCode; // four characters

	private boolean ident; // true if the A/C has been IDENT'd

	/**
	 * 
	 * @param tailNumber
	 * @param callSign
	 * @throws MuthurException
	 */
	public FlightPosition(final String tailNumber, final String acid)
			throws MuthurException {
		super(tailNumber, acid);
	}

	/**
	 * 
	 */
	public FlightPosition() {
		super();
	}

	/**
	 * @return the latitudeDegrees
	 */
	public final double getLatitudeDegrees() {
		return latitudeDegrees;
	}

	/**
	 * @param latitudeDegrees
	 *          the latitudeDegrees to set
	 */
	public final void setLatitudeDegrees(double latitudeDegrees) {
		this.latitudeDegrees = latitudeDegrees;
	}

	/**
	 * @return the longitudeDegrees
	 */
	public final double getLongitudeDegrees() {
		return longitudeDegrees;
	}

	/**
	 * @param longitudeDegrees
	 *          the longitudeDegrees to set
	 */
	public final void setLongitudeDegrees(double longitudeDegrees) {
		this.longitudeDegrees = longitudeDegrees;
	}

	/**
	 * @return the altitudeFt
	 */
	public final double getAltitudeFt() {
		return altitudeFt;
	}

	/**
	 * @param altitudeFt
	 *          the altitudeFt to set
	 */
	public final void setAltitudeFt(double altitudeFt) {
		this.altitudeFt = altitudeFt;
	}

	/**
	 * @return the groundspeedKts
	 */
	public final double getGroundspeedKts() {
		return groundspeedKts;
	}

	/**
	 * @param groundspeedKts
	 *          the groundspeedKts to set
	 */
	public final void setGroundspeedKts(double groundspeedKts) {
		this.groundspeedKts = groundspeedKts;
	}

	/**
	 * @return the headingDegrees
	 */
	public final double getHeadingDegrees() {
		return headingDegrees;
	}

	/**
	 * @param headingDegrees
	 *          the headingDegrees to set
	 */
	public final void setHeadingDegrees(double headingDegrees) {
		this.headingDegrees = headingDegrees;
	}

	/**
	 * @return the airspeedKts
	 */
	public final double getAirspeedKts() {
		return airspeedKts;
	}

	/**
	 * @param airspeedKts
	 *          the airspeedKts to set
	 */
	public final void setAirspeedKts(double airspeedKts) {
		this.airspeedKts = airspeedKts;
	}

	/**
	 * @return the pitchDegrees
	 */
	public final double getPitchDegrees() {
		return pitchDegrees;
	}

	/**
	 * @param pitchDegrees
	 *          the pitchDegrees to set
	 */
	public final void setPitchDegrees(double pitchDegrees) {
		this.pitchDegrees = pitchDegrees;
	}

	/**
	 * @return the rollDegrees
	 */
	public final double getRollDegrees() {
		return rollDegrees;
	}

	/**
	 * @param rollDegrees
	 *          the rollDegrees to set
	 */
	public final void setRollDegrees(double rollDegrees) {
		this.rollDegrees = rollDegrees;
	}

	/**
	 * @return the yawDegrees
	 */
	public final double getYawDegrees() {
		return yawDegrees;
	}

	/**
	 * @param yawDegrees
	 *          the yawDegrees to set
	 */
	public final void setYawDegrees(double yawDegrees) {
		this.yawDegrees = yawDegrees;
	}

	/**
	 * @return the sector
	 */
	public final String getSector() {
		return sector;
	}

	/**
	 * @param sector
	 *          the sector to set
	 */
	public final void setSector(String sector) {
		this.sector = sector;
	}

	/**
	 * @return the center
	 */
	public final String getCenter() {
		return center;
	}

	/**
	 * @param center
	 *          the center to set
	 */
	public final void setCenter(String center) {
		this.center = center;
	}

	/**
	 * @return the verticalspeedKts
	 */
	public final double getVerticalspeedKts() {
		return verticalspeedKts;
	}

	/**
	 * @param verticalspeedKts
	 *          the verticalspeedKts to set
	 */
	public final void setVerticalspeedKts(double verticalspeedKts) {
		this.verticalspeedKts = verticalspeedKts;
	}

	/**
	 * @return the aircraftOnGround
	 */
	public final boolean getAircraftOnGround() {
		return aircraftOnGround;
	}

	/**
	 * @param aircraftOnGround
	 *          the aircraftOnGround to set
	 */
	public final void setAircraftOnGround(boolean aircraftOnGround) {
		this.aircraftOnGround = aircraftOnGround;
	}

	/**
	 * @return the aircraftTransmissionFrequency
	 */
	public final int getAircraftTransmissionFrequency() {
		return aircraftTransmissionFrequency;
	}

	/**
	 * @param aircraftTransmissionFrequency
	 *          the aircraftTransmissionFrequency to set
	 */
	public final void setAircraftTransmissionFrequency(
			int aircraftTransmissionFrequency) {
		this.aircraftTransmissionFrequency = aircraftTransmissionFrequency;
	}

	/**
	 * @return the squawkCode
	 */
	public final String getSquawkCode() {
		return squawkCode;
	}

	/**
	 * @param squawkCode
	 *          the squawkCode to set
	 */
	public final void setSquawkCode(String squawkCode) {
		this.squawkCode = squawkCode;
	}

	/**
	 * @return the ident
	 */
	public final boolean getIdent() {
		return ident;
	}

	/**
	 * @param ident
	 *          the ident to set
	 */
	public final void setIdent(boolean ident) {
		this.ident = ident;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.IBaseDataObject#getEventName()
	 */
	@Override
	public String getEventName() {
		return DataTypeEnum.FlightPosition.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.IBaseDataObject#getDataBlockElement()
	 */
	public void addDataBlockElements(Element dataBlockElement) {

		if (dataBlockElement != null) {

			// positionData section

			Element positionDataElement = new Element(EventAttributeName.positionData
					.toString());

			Element nextEle = new Element(EventAttributeName.latitudeDegrees
					.toString()).setText(Double.toString(getLatitudeDegrees()));
			positionDataElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.longitudeDegrees.toString())
					.setText(Double.toString(getLongitudeDegrees()));
			positionDataElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.altitudeFt.toString())
					.setText(Double.toString(getAltitudeFt()));
			positionDataElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.groundspeedKts.toString())
					.setText(Double.toString(getGroundspeedKts()));
			positionDataElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.cruiseAltitudeFt.toString())
					.setText(Double.toString(getGroundspeedKts()));
			positionDataElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.headingDegrees.toString())
					.setText(Double.toString(getHeadingDegrees()));
			positionDataElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.airspeedKts.toString())
					.setText(Double.toString(getAirspeedKts()));
			positionDataElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.pitchDegrees.toString())
					.setText(Double.toString(getPitchDegrees()));
			positionDataElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.rollDegrees.toString())
					.setText(Double.toString(getRollDegrees()));
			positionDataElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.yawDegrees.toString())
					.setText(Double.toString(getYawDegrees()));
			positionDataElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.sector.toString())
					.setText(getSector());
			positionDataElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.center.toString())
					.setText(getCenter());
			positionDataElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.verticalspeedKts.toString())
					.setText(Double.toString(getVerticalspeedKts()));
			positionDataElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.aircraftOnGround.toString())
					.setText(Boolean.toString(getAircraftOnGround()));
			positionDataElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.aircraftTransmissionFrequency
					.toString()).setText(Integer
					.toString(getAircraftTransmissionFrequency()));
			positionDataElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.squawkCode.toString())
					.setText(getSquawkCode());
			positionDataElement.addContent(nextEle);

			nextEle = new Element(EventAttributeName.ident.toString())
					.setText(Boolean.toString(getIdent()));
			positionDataElement.addContent(nextEle);

			// add the position element to the parent datablock element

			dataBlockElement.addContent(positionDataElement);
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

			// position data

			if (elementName.equalsIgnoreCase(EventAttributeName.callSign.toString())) {
				callSign = elementValue;
			} else if (elementName.equalsIgnoreCase(EventAttributeName.tailNumber
					.toString())) {
				tailNumber = elementValue;

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
			} else if (elementName.equalsIgnoreCase(EventAttributeName.airspeedKts
					.toString())) {
				airspeedKts = Double.valueOf(elementValue);
			} else if (elementName.equalsIgnoreCase(EventAttributeName.pitchDegrees
					.toString())) {
				pitchDegrees = Double.valueOf(elementValue);
			} else if (elementName.equalsIgnoreCase(EventAttributeName.rollDegrees
					.toString())) {
				rollDegrees = Double.valueOf(elementValue);
			} else if (elementName.equalsIgnoreCase(EventAttributeName.yawDegrees
					.toString())) {
				yawDegrees = Double.valueOf(elementValue);
			} else if (elementName.equalsIgnoreCase(EventAttributeName.sector
					.toString())) {
				sector = elementValue;
			} else if (elementName.equalsIgnoreCase(EventAttributeName.center
					.toString())) {
				center = elementValue;
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.verticalspeedKts.toString())) {
				verticalspeedKts = Double.valueOf(elementValue);
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.aircraftOnGround.toString())) {
				aircraftOnGround = Boolean.valueOf(elementValue);
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.aircraftTransmissionFrequency
							.toString())) {
				aircraftTransmissionFrequency = Integer.valueOf(elementValue);
			} else if (elementName.equalsIgnoreCase(EventAttributeName.squawkCode
					.toString())) {
				squawkCode = elementValue;
			} else if (elementName.equalsIgnoreCase(EventAttributeName.ident
					.toString())) {
				ident = Boolean.valueOf(elementValue);
			}

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.data.IBaseDataObject#getDataType()
	 */
	@Override
	public DataTypeEnum getDataType() {
		return DataTypeEnum.FlightPosition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.data.IBaseDataObject#validate()
	 */
	@Override
	public boolean validate() {

		if (!super.validate()) {
			return false;
		}

		if ((sector == null) || (sector.length() == 0)) {
			return false;
		}

		if ((getSquawkCode() == null) || (getSquawkCode().length() == 0)) {
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
		result = prime * result + (aircraftOnGround ? 1231 : 1237);
		result = prime * result + aircraftTransmissionFrequency;
		long temp;
		temp = Double.doubleToLongBits(airspeedKts);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(altitudeFt);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((center == null) ? 0 : center.hashCode());
		temp = Double.doubleToLongBits(groundspeedKts);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(headingDegrees);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (ident ? 1231 : 1237);
		temp = Double.doubleToLongBits(latitudeDegrees);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitudeDegrees);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(pitchDegrees);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(rollDegrees);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((sector == null) ? 0 : sector.hashCode());
		result = prime * result
				+ ((squawkCode == null) ? 0 : squawkCode.hashCode());
		temp = Double.doubleToLongBits(verticalspeedKts);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(yawDegrees);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		FlightPosition other = (FlightPosition) obj;
		if (aircraftOnGround != other.aircraftOnGround) {
			return false;
		}
		if (aircraftTransmissionFrequency != other.aircraftTransmissionFrequency) {
			return false;
		}
		if (Double.doubleToLongBits(airspeedKts) != Double
				.doubleToLongBits(other.airspeedKts)) {
			return false;
		}
		if (Double.doubleToLongBits(altitudeFt) != Double
				.doubleToLongBits(other.altitudeFt)) {
			return false;
		}
		if (center == null) {
			if (other.center != null) {
				return false;
			}
		} else if (!center.equals(other.center)) {
			return false;
		}
		if (Double.doubleToLongBits(groundspeedKts) != Double
				.doubleToLongBits(other.groundspeedKts)) {
			return false;
		}
		if (Double.doubleToLongBits(headingDegrees) != Double
				.doubleToLongBits(other.headingDegrees)) {
			return false;
		}
		if (ident != other.ident) {
			return false;
		}
		if (Double.doubleToLongBits(latitudeDegrees) != Double
				.doubleToLongBits(other.latitudeDegrees)) {
			return false;
		}
		if (Double.doubleToLongBits(longitudeDegrees) != Double
				.doubleToLongBits(other.longitudeDegrees)) {
			return false;
		}
		if (Double.doubleToLongBits(pitchDegrees) != Double
				.doubleToLongBits(other.pitchDegrees)) {
			return false;
		}
		if (Double.doubleToLongBits(rollDegrees) != Double
				.doubleToLongBits(other.rollDegrees)) {
			return false;
		}
		if (sector == null) {
			if (other.sector != null) {
				return false;
			}
		} else if (!sector.equals(other.sector)) {
			return false;
		}
		if (squawkCode == null) {
			if (other.squawkCode != null) {
				return false;
			}
		} else if (!squawkCode.equals(other.squawkCode)) {
			return false;
		}
		if (Double.doubleToLongBits(verticalspeedKts) != Double
				.doubleToLongBits(other.verticalspeedKts)) {
			return false;
		}
		if (Double.doubleToLongBits(yawDegrees) != Double
				.doubleToLongBits(other.yawDegrees)) {
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
		return "FlightPosition [" + getNaturalKey() + "] [aircraftOnGround="
				+ aircraftOnGround + ", aircraftTransmissionFrequency="
				+ aircraftTransmissionFrequency + ", airspeedKts=" + airspeedKts
				+ ", altitudeFt=" + altitudeFt + ", center=" + center
				+ ", groundspeedKts=" + groundspeedKts + ", headingDegrees="
				+ headingDegrees + ", ident=" + ident + ", latitudeDegrees="
				+ latitudeDegrees + ", longitudeDegrees=" + longitudeDegrees
				+ ", pitchDegrees=" + pitchDegrees + ", rollDegrees=" + rollDegrees
				+ ", sector=" + sector + ", squawkCode=" + squawkCode
				+ ", verticalspeedKts=" + verticalspeedKts + ", yawDegrees="
				+ yawDegrees + super.toString() + "]";
	}

}
