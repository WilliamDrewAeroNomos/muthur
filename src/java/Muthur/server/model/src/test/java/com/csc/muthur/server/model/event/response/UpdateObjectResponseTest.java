/**
 * 
 */
package com.csc.muthur.server.model.event.response;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.event.AbstractModelTest;
import com.csc.muthur.server.model.event.data.IBaseDataObject;
import com.csc.muthur.server.model.event.data.flight.FlightPlan;
import com.csc.muthur.server.model.event.request.UpdateObjectRequest;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 * 
 */
public class UpdateObjectResponseTest extends AbstractModelTest {

	private static final String tailNumber = "N485UA";
	private static final String acid = "DAL333";

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

	/**
	 * Test method for
	 * {@link com.csc.muthur.server.model.event.response.CreateObjectResponse#UpdateObjectResponse()}
	 * .
	 */
	@Test
	public void testCreateObjectRequestandResponseWithSpawnAirCraftDataObject() {

		FlightPlan flightPlan = null;

		try {
			flightPlan = new FlightPlan(tailNumber, acid);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(flightPlan);

		assertEquals(flightPlan.getCallSign(), acid);
		assertEquals(flightPlan.getTailNumber(), tailNumber);
		assertNotNull(flightPlan.getDataObjectUUID());
		assertFalse(flightPlan.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(flightPlan.getObjectCreateTimeMSecs() != 0L);

		// flight plan data

		flightPlan.setSource("Flight");
		flightPlan.setAircraftType("B771");
		flightPlan.setPlannedDepartureTimeMSecs(System.currentTimeMillis() + 10);
		flightPlan.setPlannedArrivalTimeMSecs(System.currentTimeMillis() + 20);
		flightPlan.setCruiseSpeedKts(400);
		flightPlan.setCruiseAltitudeFt(330);
		flightPlan.setRoutePlan("IND..ROD.J29.PLB.J595.BGR..BGR");
		flightPlan.setDepartureCenter("ZID");
		flightPlan.setArrivalCenter("ZBW");
		flightPlan.setDepartureFix("DEFIX");
		flightPlan.setArrivalFix("ARFIX");
		flightPlan.setPhysicalAircraftClass("J");
		flightPlan.setWeightAircraftClass("H");
		flightPlan.setUserAircraftClass("C");
		flightPlan.setNumOfAircraft(1);
		flightPlan.setAirborneEquipmentQualifier("R");

		// create an UpdateObjectRequest

		UpdateObjectRequest updateObjectReq = new UpdateObjectRequest();
		assertNotNull(updateObjectReq);

		updateObjectReq.setSourceOfEvent("Test");
		updateObjectReq.setDataObject(flightPlan);
		updateObjectReq.setFederationExecutionHandle(UUID.randomUUID().toString());
		updateObjectReq.setFederateRegistrationHandle(UUID.randomUUID().toString());

		// serialize the object

		String updateObjectReqAsXML = updateObjectReq.serialize();

		// do standard validation

		assertNotNull(updateObjectReqAsXML);
		assertFalse(updateObjectReqAsXML.equals(""));

		// create another object from previous object's serialized form

		UpdateObjectRequest updateObjectReqFromXML = new UpdateObjectRequest();

		try {
			updateObjectReqFromXML.initialization(updateObjectReqAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// ensure that the two objects are equal

		assertTrue(updateObjectReqFromXML.equals(updateObjectReq));

		// get the serialized version of the new object

		String newObjSerializedAsXML = updateObjectReqFromXML.serialize();

		assertNotNull(newObjSerializedAsXML);
		assertFalse(newObjSerializedAsXML.equals(""));

		IBaseDataObject bdo = updateObjectReqFromXML.getDataObject();

		assertNotNull(bdo);
		assertTrue(bdo instanceof FlightPlan);

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

		/**
		 * create a response from a request which occurs when the response to the
		 * related request needs to be returned to the requestr
		 */

		UpdateObjectResponse response = new UpdateObjectResponse();

		// initialization
		try {
			response.initialization(updateObjectReqFromXML.serialize());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// check out the serialized response object

		assertNotNull(response.serialize());
		assertFalse("".equals(response.serialize()));

		// create a response from a request
		// and make it an error

		UpdateObjectResponse errorResponse = new UpdateObjectResponse();

		try {
			errorResponse.initialization(updateObjectReqFromXML.serialize());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		errorResponse.setSuccess(false);
		errorResponse.setErrorDescription("Duplicate object encountered");

		// check out the serialized response object

		assertNotNull(errorResponse.serialize());
		assertFalse("".equals(errorResponse.serialize()));

		writeToFile(response, false);

	}

}
