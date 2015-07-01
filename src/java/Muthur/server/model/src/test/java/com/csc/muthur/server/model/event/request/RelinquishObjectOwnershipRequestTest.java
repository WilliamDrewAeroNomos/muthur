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
import com.csc.muthur.server.model.event.data.flight.Aircraft;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public class RelinquishObjectOwnershipRequestTest extends AbstractModelTest {

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
	public void testRelinquishObjectOwnershipRequestWithAirCraftDataObject() {

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
		request.setDataObjectUUID(aircraft.getDataObjectUUID());
		request.setTimeToLiveSecs(10);
		request.setFederationExecutionHandle(UUID.randomUUID().toString());
		request.setFederateRegistrationHandle(UUID.randomUUID().toString());
		request.setFederationExecutionModelHandle(UUID.randomUUID().toString());

		// serialize the object

		String requestAsXML = request.serialize();

		// do standard validation

		assertNotNull(requestAsXML);
		assertFalse(requestAsXML.equals(""));

		writeToFile(request, false);

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

	}

}
