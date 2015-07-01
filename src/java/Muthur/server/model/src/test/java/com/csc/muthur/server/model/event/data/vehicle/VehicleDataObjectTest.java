/**
 * 
 */
package com.csc.muthur.server.model.event.data.vehicle;

import com.csc.muthur.server.model.event.AbstractModelTest;

/**
 * @author NexSim
 * 
 */
public class VehicleDataObjectTest extends AbstractModelTest {

	private static String airportCode = "KIAD";
	private static String beaconCode = "636";

	private static double groundspeedKts = 230.0;
	private static double latitudeDegrees = 38.948004;

	private static double longitudeDegrees = -77.44148;
	private static double altitudeFt = 3500;

	private static double headingDegrees = 180.40;
	private static int vehicleTransmissionFrequency = 2300;

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
	public void testVehicleDataObject() {

		VehicleDataObject vehicleDataObject = null;

		try {
			vehicleDataObject = new VehicleDataObject();
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(vehicleDataObject);

		vehicleDataObject.setAirportCode(airportCode);
		vehicleDataObject.setBeaconCode(beaconCode);
		vehicleDataObject.setGroundspeedKts(groundspeedKts);
		vehicleDataObject.setLatitudeDegrees(latitudeDegrees);
		vehicleDataObject.setLongitudeDegrees(longitudeDegrees);
		vehicleDataObject.setAltitudeFt(altitudeFt);
		vehicleDataObject.setHeadingDegrees(headingDegrees);
		vehicleDataObject
				.setVehicleTransmissionFrequency(vehicleTransmissionFrequency);

		// serialize and validate

		String dataAsXml = vehicleDataObject.serialize();
		assertNotNull(dataAsXml);
		assertFalse("".equalsIgnoreCase(dataAsXml));

		writeToFile(vehicleDataObject, false);

		// create a new object and initialize it from the serialized XML

		VehicleDataObject vehicleDataFromXML = new VehicleDataObject();
		try {
			vehicleDataFromXML.initialization(dataAsXml);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(vehicleDataFromXML);

		// check that the objects are equal after initialization from the XML

		assertTrue(vehicleDataFromXML.equals(vehicleDataObject));
		assertNotNull(vehicleDataFromXML.getDataObjectUUID());
		assertFalse(vehicleDataFromXML.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(vehicleDataFromXML.getObjectCreateTimeMSecs() != 0L);

	}

}
