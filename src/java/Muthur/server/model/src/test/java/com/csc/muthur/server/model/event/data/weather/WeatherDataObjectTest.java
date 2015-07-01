/**
 * 
 */
package com.csc.muthur.server.model.event.data.weather;

import com.csc.muthur.server.model.event.AbstractModelTest;
import com.csc.muthur.server.model.event.data.flight.FlightPosition;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class WeatherDataObjectTest extends AbstractModelTest {

	private static String airportCode = "KIAD";

	// TODO: Get a valid value here
	private static String atisCode = "D";
	private static String metarString =
			"SETWX = METAR \"IAD 061700 VRB03KT 10SM FEW120 05/M07 A3064 \"";
	private static String centerFieldWind = "VRB03";
	private static double centerFieldWindSpeedKts = 25.345;
	private static double altimeterReading = 30.64;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * 
	 */
	public void testWeatherDataObjectData() {

		FlightPosition fpd = new FlightPosition();
		assertNotNull(fpd);

	}

	public void testWeatherDataObjectDataGetDocument() {

		WeatherDataObject weatherDataObject = null;

		try {
			weatherDataObject = new WeatherDataObject();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(weatherDataObject);

		weatherDataObject.setAirportCode(airportCode);
		weatherDataObject.setAtisCode(atisCode);
		weatherDataObject.setMetarString(metarString);
		weatherDataObject.setCenterFieldWind(centerFieldWind);
		weatherDataObject.setCenterFieldWindSpeedKts(centerFieldWindSpeedKts);
		weatherDataObject.setAltimeterReading(altimeterReading);

		// serialize and validate

		String dataAsXml = weatherDataObject.serialize();
		assertNotNull(dataAsXml);
		assertFalse("".equalsIgnoreCase(dataAsXml));

		writeToFile(weatherDataObject, false);

		// create a new object and initialize it from the serialized XML

		WeatherDataObject weatherDataFromXML = new WeatherDataObject();
		try {
			weatherDataFromXML.initialization(dataAsXml);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		// check that the objects are equal after initialization from the XML

		assertNotNull(weatherDataFromXML.equals(weatherDataObject));

	}

}
