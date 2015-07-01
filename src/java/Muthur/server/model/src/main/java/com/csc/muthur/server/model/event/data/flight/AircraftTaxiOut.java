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
public class AircraftTaxiOut extends FlightDataObject {

	/**
	 * taxi out data
	 */

	private long taxiOutTimeMSecs;
	private String taxiOutGate;

	/**
	 * 
	 * @param tailNumber
	 * @param callSign
	 * @throws MuthurException
	 */
	public AircraftTaxiOut(final String tailNumber, final String acid)
			throws MuthurException {
		super(tailNumber, acid);
	}

	/**
	 * 
	 */
	public AircraftTaxiOut() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.data.IBaseDataObject#getDataType()
	 */
	@Override
	public DataTypeEnum getDataType() {
		return DataTypeEnum.AircraftTaxiOut;
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
	 * @param taxiOutTimeMSecs
	 *          the taxiOutTimeMSecs to set
	 */
	public void setTaxiOutTimeMSecs(long taxiOutTimeMSecs) {
		this.taxiOutTimeMSecs = taxiOutTimeMSecs;
	}

	/**
	 * @return the taxiOutTimeMSecs
	 */
	public long getTaxiOutTimeMSecs() {
		return taxiOutTimeMSecs;
	}

	/**
	 * @return the taxiOutGate
	 */
	public String getTaxiOutGate() {
		return taxiOutGate;
	}

	/**
	 * @param taxiOutGate
	 *          the taxiOutGate to set
	 */
	public void setTaxiOutGate(String taxiOutGate) {
		this.taxiOutGate = taxiOutGate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.IBaseDataObject#getDataBlockElement()
	 */
	public void addDataBlockElements(Element dataBlockElement) {

		if (dataBlockElement != null) {

			// taxiOutData section

			Element taxiOutDataElement =
					new Element(EventAttributeName.taxiOutData.toString());

			Element nextEle =
					new Element(EventAttributeName.taxiOutTimeMSecs.toString())
							.setText(Long.toString(getTaxiOutTimeMSecs()));

			taxiOutDataElement.addContent(nextEle);

			nextEle =
					new Element(EventAttributeName.taxiOutGate.toString())
							.setText(getTaxiOutGate());

			taxiOutDataElement.addContent(nextEle);

			dataBlockElement.addContent(taxiOutDataElement);

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
					.equalsIgnoreCase(EventAttributeName.taxiOutTimeMSecs.toString())) {
				taxiOutTimeMSecs = Long.valueOf(elementValue);

			} else if (elementName.equalsIgnoreCase(EventAttributeName.taxiOutGate
					.toString())) {
				setTaxiOutGate(elementValue);

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
				prime * result + ((taxiOutGate == null) ? 0 : taxiOutGate.hashCode());
		result =
				prime * result + (int) (taxiOutTimeMSecs ^ (taxiOutTimeMSecs >>> 32));
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
		AircraftTaxiOut other = (AircraftTaxiOut) obj;
		if (taxiOutGate == null) {
			if (other.taxiOutGate != null) {
				return false;
			}
		} else if (!taxiOutGate.equals(other.taxiOutGate)) {
			return false;
		}
		if (taxiOutTimeMSecs != other.taxiOutTimeMSecs) {
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
		return "AircraftTaxiOut [" + getNaturalKey() + "] [taxiOutGate="
				+ taxiOutGate + ", taxiOutTimeMSecs=" + taxiOutTimeMSecs
				+ super.toString() + "]";
	}

}
