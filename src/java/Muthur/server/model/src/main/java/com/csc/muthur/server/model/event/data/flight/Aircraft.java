/**
 * 
 */
package com.csc.muthur.server.model.event.data.flight;

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
public class Aircraft extends FlightDataObject {

	/**
	 * 
	 * @param tailNumber
	 * @param callSign
	 * @throws MuthurException
	 */
	public Aircraft(final String tailNumber, final String acid)
			throws MuthurException {
		super(tailNumber, acid);
	}

	/**
	 * 
	 */
	public Aircraft() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.IBaseDataObject#getEventName()
	 */
	@Override
	public String getEventName() {
		return DataTypeEnum.Aircraft.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.csc.muthur.server.model.event.data.IBaseDataObject#getDataType()
	 */
	@Override
	public DataTypeEnum getDataType() {
		return DataTypeEnum.Aircraft;
	}

	@Override
	public void newElement(String elementName, String elementValue) {

		if ((elementValue != null) && (elementValue.length() > 0)) {

			// arrival data

			if (elementName.equalsIgnoreCase(EventAttributeName.callSign
					.toString())) {
				callSign = elementValue;
			} else if (elementName
					.equalsIgnoreCase(EventAttributeName.tailNumber.toString())) {
				tailNumber = elementValue;

			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Aircraft [" + getNaturalKey() + "] [" + super.toString() + "]";
	}

}
