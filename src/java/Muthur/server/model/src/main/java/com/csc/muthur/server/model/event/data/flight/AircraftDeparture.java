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
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class AircraftDeparture extends FlightDataObject {

	/**
	 * departure data
	 */
	private long actualDepartureTimeMSecs;
	private String departureRunway;
	private String departureAirportCode;

	/**
	 * 
	 * @param tailNumber
	 * @param callSign
	 * @throws MuthurException
	 */
	public AircraftDeparture(final String tailNumber, final String acid)
			throws MuthurException {
		super(tailNumber, acid);
	}

	/**
	 * 
	 */
	public AircraftDeparture() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.data.IBaseDataObject#getDataType()
	 */
	@Override
	public DataTypeEnum getDataType() {
		return DataTypeEnum.AircraftDeparture;
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
	 * @return Actual departure time for this aircraft in milliseconds
	 */
	public final long getActualDepartureTimeMSecs() {
		return actualDepartureTimeMSecs;
	}

	/**
	 * 
	 * @param actualDepartureTimeMSecs
	 */
	public final void setActualDepartureTimeMSecs(long actualDepartureTimeMSecs) {
		this.actualDepartureTimeMSecs = actualDepartureTimeMSecs;
	}

	/**
	 * @param departureRunway
	 *          the departureRunway to set
	 */
	public void setDepartureRunway(String departureRunwayName) {
		this.departureRunway = departureRunwayName;
	}

	/**
	 * @return the departureRunway
	 */
	public String getDepartureRunway() {
		return departureRunway;
	}

	/**
	 * @return the departureAirportCode
	 */
	public final String getDepartureAirportCode() {
		return departureAirportCode;
	}

	/**
	 * @param departureAirportCode
	 *          the departureAirportCode to set
	 */
	public final void setDepartureAirportCode(String departureAirportCode) {
		this.departureAirportCode = departureAirportCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.IBaseDataObject#getDataBlockElement()
	 */
	@Override
	public void addDataBlockElements(Element dataBlockElement) {

		if (dataBlockElement != null) {

			// departureData section

			Element departureDataElement =
					new Element(EventAttributeName.departureData.toString());

			Element nextEle =
					new Element(EventAttributeName.actualDepartureTimeMSecs.toString())
							.setText(Long.toString(getActualDepartureTimeMSecs()));
			departureDataElement.addContent(nextEle);

			nextEle =
					new Element(EventAttributeName.departureRunway.toString())
							.setText(getDepartureRunway());
			departureDataElement.addContent(nextEle);

			nextEle =
					new Element(EventAttributeName.departureAirportCode.toString())
							.setText(getDepartureAirportCode());
			departureDataElement.addContent(nextEle);

			dataBlockElement.addContent(departureDataElement);

		}
	}

	@Override
	public void newElement(String elementName, String elementValue) {

		if ((elementValue != null) && (elementValue.length() > 0)) {

			// departure data

			if (elementName.equalsIgnoreCase(EventAttributeName.callSign.toString())) {
				callSign = elementValue;
			} else if (elementName.equalsIgnoreCase(EventAttributeName.tailNumber
					.toString())) {
				tailNumber = elementValue;

			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.actualDepartureTimeMSecs
							.toString())) {
				actualDepartureTimeMSecs = Long.valueOf(elementValue);
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.departureRunway.toString())) {
				setDepartureRunway(elementValue);
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.departureAirportCode.toString())) {
				setDepartureAirportCode(elementValue);
			}
		}

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
						+ (int) (actualDepartureTimeMSecs ^ (actualDepartureTimeMSecs >>> 32));
		result =
				prime
						* result
						+ ((departureAirportCode == null) ? 0 : departureAirportCode
								.hashCode());
		result =
				prime * result
						+ ((departureRunway == null) ? 0 : departureRunway.hashCode());
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
		AircraftDeparture other = (AircraftDeparture) obj;
		if (actualDepartureTimeMSecs != other.actualDepartureTimeMSecs) {
			return false;
		}
		if (departureAirportCode == null) {
			if (other.departureAirportCode != null) {
				return false;
			}
		} else if (!departureAirportCode.equals(other.departureAirportCode)) {
			return false;
		}
		if (departureRunway == null) {
			if (other.departureRunway != null) {
				return false;
			}
		} else if (!departureRunway.equals(other.departureRunway)) {
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
		return "AircraftDeparture [" + getNaturalKey() + "] [, tailNumber="
				+ tailNumber + "], actualDepartureTimeMSecs="
				+ actualDepartureTimeMSecs + ", departureRunway=" + departureRunway
				+ ", departureAirportCode=" + departureAirportCode + "]";
	}
}
