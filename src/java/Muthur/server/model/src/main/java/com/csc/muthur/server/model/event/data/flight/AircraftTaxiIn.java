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
public class AircraftTaxiIn extends FlightDataObject {

	/**
	 * taxi in data
	 */

	private long taxiInTimeMSecs;
	private String taxiInGate;

	/**
	 * 
	 * @param tailNumber
	 * @param callSign
	 * @throws MuthurException
	 */
	public AircraftTaxiIn(final String tailNumber, final String acid)
			throws MuthurException {
		super(tailNumber, acid);
	}

	/**
	 * 
	 */
	public AircraftTaxiIn() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.data.IBaseDataObject#getDataType()
	 */
	@Override
	public DataTypeEnum getDataType() {
		return DataTypeEnum.AircraftTaxiIn;
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
	 * @param taxiInTimeMSecs
	 *          the taxiInTimeMSecs to set
	 */
	public void setTaxiInTimeMSecs(long taxiInTimeMSecs) {
		this.taxiInTimeMSecs = taxiInTimeMSecs;
	}

	/**
	 * @return the taxiInTimeMSecs
	 */
	public long getTaxiInTimeMSecs() {
		return taxiInTimeMSecs;
	}

	/**
	 * @param taxiInGate
	 *          the taxiInGate to set
	 */
	public void setTaxiInGate(String taxiInGate) {
		this.taxiInGate = taxiInGate;
	}

	/**
	 * @return the taxiInGate
	 */
	public String getTaxiInGate() {
		return taxiInGate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.IBaseDataObject#getDataBlockElement()
	 */
	public void addDataBlockElements(Element dataBlockElement) {

		if (dataBlockElement != null) {

			// taxiInData section

			Element taxiInDataElement =
					new Element(EventAttributeName.taxiInData.toString());

			Element nextEle =
					new Element(EventAttributeName.taxiInTimeMSecs.toString())
							.setText(Long.toString(getTaxiInTimeMSecs()));

			taxiInDataElement.addContent(nextEle);

			nextEle =
					new Element(EventAttributeName.taxiInGate.toString())
							.setText(getTaxiInGate());

			taxiInDataElement.addContent(nextEle);

			dataBlockElement.addContent(taxiInDataElement);

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

			// taxi out data

			if (elementName.equalsIgnoreCase(EventAttributeName.callSign.toString())) {
				callSign = elementValue;
			} else if (elementName.equalsIgnoreCase(EventAttributeName.tailNumber
					.toString())) {
				tailNumber = elementValue;

			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.taxiInTimeMSecs.toString())) {
				taxiInTimeMSecs = Long.valueOf(elementValue);

			} else if (elementName.equalsIgnoreCase(EventAttributeName.taxiInGate
					.toString())) {
				setTaxiInGate(elementValue);

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
				prime * result + ((taxiInGate == null) ? 0 : taxiInGate.hashCode());
		result =
				prime * result + (int) (taxiInTimeMSecs ^ (taxiInTimeMSecs >>> 32));
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
		AircraftTaxiIn other = (AircraftTaxiIn) obj;
		if (taxiInGate == null) {
			if (other.taxiInGate != null) {
				return false;
			}
		} else if (!taxiInGate.equals(other.taxiInGate)) {
			return false;
		}
		if (taxiInTimeMSecs != other.taxiInTimeMSecs) {
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
		return "AircraftTaxiIn [" + getNaturalKey() + "] [taxiInGate=" + taxiInGate
				+ ", taxiInTimeMSecs=" + taxiInTimeMSecs + super.toString() + "]";
	}

}
