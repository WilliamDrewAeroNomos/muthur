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
import com.csc.muthur.server.model.event.data.flight.Aircraft;
import com.csc.muthur.server.model.event.request.RelinquishObjectOwnershipRequest;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public class RelinquishObjectOwnershipResponseTest extends AbstractModelTest {

	private static final String tailNumber = "N485UA";
	private static final String callSign = "DAL333";

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
	public void testRelinquishObjectOwnershipResponseWithAirCraftDataObject() {

		Aircraft aircraft = null;

		try {
			aircraft = new Aircraft(tailNumber, callSign);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(aircraft);

		assertEquals(aircraft.getCallSign(), callSign);
		assertEquals(aircraft.getTailNumber(), tailNumber);
		assertNotNull(aircraft.getDataObjectUUID());
		assertFalse(aircraft.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(aircraft.getObjectCreateTimeMSecs() != 0L);

		// create a RelinquishObjectOwnershipRequest

		RelinquishObjectOwnershipRequest request =
				new RelinquishObjectOwnershipRequest();
		assertNotNull(request);

		request.setSourceOfEvent("NexSim");
		request.setTimeToLiveSecs(10);
		request.setDataObjectUUID(aircraft.getDataObjectUUID());
		request.setFederationExecutionHandle(UUID.randomUUID().toString());
		request.setFederateRegistrationHandle(UUID.randomUUID().toString());
		request.setFederationExecutionModelHandle(UUID.randomUUID().toString());

		// serialize the object

		String requestAsXML = request.serialize();

		// do standard validation

		assertNotNull(requestAsXML);
		assertFalse(requestAsXML.equals(""));

		// create another object from previous object's serialized form

		RelinquishObjectOwnershipRequest requestFromXML =
				new RelinquishObjectOwnershipRequest();

		try {
			requestFromXML.initialization(requestAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// ensure that the two objects are equal

		assertTrue(requestFromXML.equals(request));

		// get the serialized version of the new object

		String newObjSerializedAsXML = requestFromXML.serialize();

		assertNotNull(newObjSerializedAsXML);
		assertFalse(newObjSerializedAsXML.equals(""));

		// create a response from a request
		// allows maintenance of the internal event values

		RelinquishObjectOwnershipResponse response =
				new RelinquishObjectOwnershipResponse();

		try {
			response.initialization(requestFromXML.serialize());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// check out the serialized response object

		assertNotNull(response.serialize());
		assertFalse("".equals(response.serialize()));

		// create a response from a request
		// and make it an error
		RelinquishObjectOwnershipResponse errorResponse =
				new RelinquishObjectOwnershipResponse();

		try {
			errorResponse.initialization(requestFromXML.serialize());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		errorResponse.setSuccess(false);
		errorResponse
				.setErrorDescription("Object with the supplied ID does not exist");

		// check out the serialized response object

		assertNotNull(errorResponse.serialize());
		assertFalse("".equals(errorResponse.serialize()));

		writeToFile(response, false);

	}

}
