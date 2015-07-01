/**
 * 
 */
package com.csc.muthur.server.model.event.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csc.muthur.server.model.event.data.airport.AirportConfiguration;
import com.csc.muthur.server.model.event.data.flight.Aircraft;
import com.csc.muthur.server.model.event.data.flight.AircraftArrival;
import com.csc.muthur.server.model.event.data.flight.AircraftDeparture;
import com.csc.muthur.server.model.event.data.flight.AircraftTaxiIn;
import com.csc.muthur.server.model.event.data.flight.AircraftTaxiOut;
import com.csc.muthur.server.model.event.data.flight.FlightPlan;
import com.csc.muthur.server.model.event.data.flight.FlightPosition;
import com.csc.muthur.server.model.event.data.vehicle.VehicleDataObject;
import com.csc.muthur.server.model.event.data.weather.WeatherDataObject;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class DataObjectFactory {

	private static final Logger LOG = LoggerFactory
			.getLogger(DataObjectFactory.class.getName());

	public static IBaseDataObject getDataObject(final DataTypeEnum dataType) {

		IBaseDataObject dataObject = null;

		if (dataType != null) {

			switch (dataType) {
			case Aircraft:
				dataObject = new Aircraft();
				break;
			case FlightPlan:
				dataObject = new FlightPlan();
				break;
			case FlightPosition:
				dataObject = new FlightPosition();
				break;
			case AircraftDeparture:
				dataObject = new AircraftDeparture();
				break;
			case AircraftArrival:
				dataObject = new AircraftArrival();
				break;
			case AircraftTaxiOut:
				dataObject = new AircraftTaxiOut();
				break;
			case AircraftTaxiIn:
				dataObject = new AircraftTaxiIn();
				break;
			case AirportConfiguration:
				dataObject = new AirportConfiguration();
				break;
			case Weather:
				dataObject = new WeatherDataObject();
				break;
			case Vehicle:
				dataObject = new VehicleDataObject();
				break;
			default:
				LOG.error("Unknown data type encountered");
			}
		}

		return dataObject;
	}
}
