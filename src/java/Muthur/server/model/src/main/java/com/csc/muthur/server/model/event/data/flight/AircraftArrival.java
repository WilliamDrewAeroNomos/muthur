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
public class AircraftArrival extends FlightDataObject {

	/**
	 * arrival data
	 */
	private long actualArrivalTimeMSecs;
	private String arrivalRunway;
	private String arrivalAirportCode;

	/**
	 * 
	 * @param tailNumber
	 * @param aircraftID
	 * @throws MuthurException
	 */
	public AircraftArrival(final String tailNumber, final String aircraftID)
			throws MuthurException {
		super(tailNumber, aircraftID);
	}

	/**
	 * 
	 */
	public AircraftArrival() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.data.IBaseDataObject#getDataType()
	 */
	@Override
	public DataTypeEnum getDataType() {
		return DataTypeEnum.AircraftArrival;
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
	 * @param actualArrivalTimeMSecs
	 *          the actualArrivalTimeMSecs to set
	 */
	public void setActualArrivalTimeMSecs(long actualArrivalTimeMSecs) {
		this.actualArrivalTimeMSecs = actualArrivalTimeMSecs;
	}

	/**
	 * @return the actualArrivalTimeMSecs
	 */
	public long getActualArrivalTimeMSecs() {
		return actualArrivalTimeMSecs;
	}

	/**
	 * @param arrivalRunway
	 *          the arrivalRunwayName to set
	 */
	public void setArrivalRunway(String arrivalRunway) {
		this.arrivalRunway = arrivalRunway;
	}

	/**
	 * @return the arrivalRunway
	 */
	public String getArrivalRunway() {
		return arrivalRunway;
	}

	/**
	 * @return the arrivalAirportCode
	 */
	public final String getArrivalAirportCode() {
		return arrivalAirportCode;
	}

	/**
	 * @param arrivalAirportCode
	 *          the arrivalFix to set
	 */
	public final void setArrivalAirportCode(String arrivalAirportCode) {
		this.arrivalAirportCode = arrivalAirportCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.IBaseDataObject#getDataBlockElement()
	 */
	@Override
	public void addDataBlockElements(Element dataBlockElement) {

		if (dataBlockElement != null) {

			// arrival section

			Element arrivalDataElement =
					new Element(EventAttributeName.arrivalData.toString());

			Element nextEle =
					new Element(EventAttributeName.actualArrivalTimeMSecs.toString())
							.setText(Long.toString(getActualArrivalTimeMSecs()));
			arrivalDataElement.addContent(nextEle);

			nextEle =
					new Element(EventAttributeName.arrivalRunway.toString())
							.setText(getArrivalRunway());
			arrivalDataElement.addContent(nextEle);

			nextEle =
					new Element(EventAttributeName.arrivalAirportCode.toString())
							.setText(getArrivalAirportCode());
			arrivalDataElement.addContent(nextEle);

			dataBlockElement.addContent(arrivalDataElement);

		}
	}

	@Override
	public void newElement(String elementName, String elementValue) {

		if ((elementValue != null) && (elementValue.length() > 0)) {

			// arrival data

			if (elementName.equalsIgnoreCase(EventAttributeName.callSign.toString())) {
				callSign = elementValue;
			} else if (elementName.equalsIgnoreCase(EventAttributeName.tailNumber
					.toString())) {
				tailNumber = elementValue;

			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.actualArrivalTimeMSecs
							.toString())) {
				actualArrivalTimeMSecs = Long.valueOf(elementValue);
			} else if (elementName.equalsIgnoreCase(EventAttributeName.arrivalRunway
					.toString())) {
				setArrivalRunway(elementValue);
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.arrivalAirportCode.toString())) {
				arrivalAirportCode = elementValue;
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
				prime * result
						+ (int) (actualArrivalTimeMSecs ^ (actualArrivalTimeMSecs >>> 32));
		result =
				prime * result
						+ ((arrivalRunway == null) ? 0 : arrivalRunway.hashCode());
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
		AircraftArrival other = (AircraftArrival) obj;
		if (actualArrivalTimeMSecs != other.actualArrivalTimeMSecs) {
			return false;
		}
		if (arrivalRunway == null) {
			if (other.arrivalRunway != null) {
				return false;
			}
		} else if (!arrivalRunway.equals(other.arrivalRunway)) {
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
		return "AircraftArrival [" + getNaturalKey() + "] [actualArrivalTimeMSecs="
				+ actualArrivalTimeMSecs + ", arrival A/P Code =" + arrivalAirportCode
				+ ", arrivalRunway=" + arrivalRunway + super.toString() + "]";
	}
}
