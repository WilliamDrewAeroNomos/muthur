/**
 * 
 */
package com.csc.muthur.server.model.event.request;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.event.AbstractModelTest;
import com.csc.muthur.server.model.event.data.IBaseDataObject;
import com.csc.muthur.server.model.event.data.flight.Aircraft;
import com.csc.muthur.server.model.event.data.flight.FlightPosition;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public class UpdateObjectRequestTest extends AbstractModelTest {

	private static final String tailNumber = "N485UA";
	private static final String callSign = "DAL333";

	private double verticalspeedKts = 405;
	private boolean aircraftOnGround = false;
	private int aircraftTransmissionFrequency = 2850;

	private String squawkCode = "0363"; // four characters
	private boolean ident = true; // true if the A/C has been IDENT'd

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testUpdateObjectRequestWithAirCraftDataObject() {

		Aircraft sacd = null;

		try {
			sacd = new Aircraft(tailNumber, callSign);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(sacd);

		assertEquals(sacd.getCallSign(), callSign);
		assertEquals(sacd.getTailNumber(), tailNumber);
		assertNotNull(sacd.getDataObjectUUID());
		assertFalse(sacd.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(sacd.getObjectCreateTimeMSecs() != 0L);

		// create a CreateObjectRequest

		UpdateObjectRequest updateObjectReq = new UpdateObjectRequest();
		assertNotNull(updateObjectReq);

		updateObjectReq.setDataObject(sacd);
		updateObjectReq.setFederationExecutionHandle(UUID.randomUUID().toString());
		updateObjectReq.setFederateRegistrationHandle(UUID.randomUUID().toString());

		// serialize the object

		String updateObjectReqAsXML = updateObjectReq.serialize();

		// do standard validation

		assertNotNull(updateObjectReqAsXML);
		assertFalse(updateObjectReqAsXML.equals(""));

		// create another object from previous object's serialized form

		UpdateObjectRequest udpateObjectFromXML = new UpdateObjectRequest();

		try {
			udpateObjectFromXML.initialization(updateObjectReqAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// ensure that the two objects are equal

		assertTrue(udpateObjectFromXML.equals(updateObjectReq));

		// get the serialized version of the new object

		String newObjSerializedAsXML = udpateObjectFromXML.serialize();

		assertNotNull(newObjSerializedAsXML);
		assertFalse(newObjSerializedAsXML.equals(""));

		IBaseDataObject bdo = udpateObjectFromXML.getDataObject();

		assertNotNull(bdo);
		assertTrue(bdo instanceof Aircraft);

		/**
		 * Initialize the internal data object from the serialized containing object
		 */
		try {
			bdo.initialization(newObjSerializedAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// check the serialized form of the data object
		String bdoSerialized = bdo.serialize();
		assertNotNull(bdoSerialized);

	}

	@Test
	public void testUpdateObjectRequestWithFlightPositionDataObject() {

		FlightPosition flightPosition = null;

		try {
			flightPosition = new FlightPosition(tailNumber, callSign);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(flightPosition);

		assertEquals(flightPosition.getCallSign(), callSign);
		assertEquals(flightPosition.getTailNumber(), tailNumber);
		assertNotNull(flightPosition.getDataObjectUUID());
		assertFalse(flightPosition.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(flightPosition.getObjectCreateTimeMSecs() != 0L);

		// position data

		flightPosition.setLatitudeDegrees(42.65);
		flightPosition.setLongitudeDegrees(-76.72);
		flightPosition.setAltitudeFt(30000);
		flightPosition.setGroundspeedKts(400);
		flightPosition.setHeadingDegrees(90);
		flightPosition.setAirspeedKts(444);
		flightPosition.setPitchDegrees(0.5);
		flightPosition.setRollDegrees(0);
		flightPosition.setYawDegrees(0);
		flightPosition.setSector("ZOB48");
		flightPosition.setCenter("ZOB");

		flightPosition.setVerticalspeedKts(verticalspeedKts);
		flightPosition.setAircraftOnGround(aircraftOnGround);
		flightPosition
				.setAircraftTransmissionFrequency(aircraftTransmissionFrequency);
		flightPosition.setSquawkCode(squawkCode);
		flightPosition.setIdent(ident);

		// create a CreateObjectRequest

		UpdateObjectRequest updateObjectReq = new UpdateObjectRequest();
		assertNotNull(updateObjectReq);

		updateObjectReq.setDataObject(flightPosition);
		updateObjectReq.setFederationExecutionHandle(UUID.randomUUID().toString());
		updateObjectReq.setFederateRegistrationHandle(UUID.randomUUID().toString());

		// serialize the object

		String updateObjectReqAsXML = updateObjectReq.serialize();

		// do standard validation

		assertNotNull(updateObjectReqAsXML);
		assertFalse(updateObjectReqAsXML.equals(""));

		// create another object from previous object's serialized form

		UpdateObjectRequest udpateObjectFromXML = new UpdateObjectRequest();

		try {
			udpateObjectFromXML.initialization(updateObjectReqAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// ensure that the two objects are equal

		assertTrue(udpateObjectFromXML.equals(updateObjectReq));

		// get the serialized version of the new object

		String newObjSerializedAsXML = udpateObjectFromXML.serialize();

		assertNotNull(newObjSerializedAsXML);
		assertFalse(newObjSerializedAsXML.equals(""));

		IBaseDataObject bdo = udpateObjectFromXML.getDataObject();

		assertNotNull(bdo);
		assertTrue(bdo instanceof FlightPosition);

		/**
		 * Initialize the internal data object from the serialized containing object
		 */
		try {
			bdo.initialization(newObjSerializedAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// check the serialized form of the data object
		String bdoSerialized = bdo.serialize();
		assertNotNull(bdoSerialized);

		writeToFile(updateObjectReq, false);
	}

}
