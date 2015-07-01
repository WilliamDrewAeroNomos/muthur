/**
 * 
 */
package com.csc.muthur.server.model.event.response;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.TransferOwnershipResponse;
import com.csc.muthur.server.model.event.AbstractModelTest;
import com.csc.muthur.server.model.event.data.flight.Aircraft;
import com.csc.muthur.server.model.event.request.TransferObjectOwnershipRequest;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 * 
 */
public class TransferObjectOwnershipResponseTest extends AbstractModelTest {

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
	 * {@link com.csc.muthur.server.model.event.response.DeleteObjectResponse#DeleteObjectResponse()}
	 * .
	 */

	@Test
	public void testTransferObjectOwnershipRequestandResponseWithSpawnAirCraftDataObject() {

		Aircraft sacd = null;

		try {
			sacd = new Aircraft(tailNumber, acid);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(sacd);

		assertEquals(sacd.getCallSign(), acid);
		assertEquals(sacd.getTailNumber(), tailNumber);
		assertNotNull(sacd.getDataObjectUUID());
		assertFalse(sacd.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(sacd.getObjectCreateTimeMSecs() != 0L);

		// create a TransferObjectOwnershipRequest

		TransferObjectOwnershipRequest requestObject =
				new TransferObjectOwnershipRequest();
		assertNotNull(requestObject);

		requestObject.setSourceOfEvent("Test");
		requestObject.setDataObjectUUID(sacd.getDataObjectUUID());
		requestObject.setFederationExecutionHandle(UUID.randomUUID().toString());
		requestObject.setFederateRegistrationHandle(UUID.randomUUID().toString());

		// serialize the object

		String objectRequestAsXML = requestObject.serialize();

		// do standard validation

		assertNotNull(objectRequestAsXML);
		assertFalse(objectRequestAsXML.equals(""));

		// create another object from previous object's serialized form

		TransferObjectOwnershipRequest objectRequestFromXML =
				new TransferObjectOwnershipRequest();

		try {
			objectRequestFromXML.initialization(objectRequestAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// ensure that the two objects are equal

		assertTrue(objectRequestFromXML.equals(requestObject));

		// get the serialized version of the new object

		String newObjSerializedAsXML = objectRequestFromXML.serialize();

		assertNotNull(newObjSerializedAsXML);
		assertFalse(newObjSerializedAsXML.equals(""));

		// create a response from a request
		// allows maintenance of the internal event values

		TransferObjectOwnershipResponse response =
				new TransferObjectOwnershipResponse();

		try {
			response.initialization(objectRequestFromXML.serialize());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		response.setTransferOwnershipResponse(TransferOwnershipResponse.GRANTED);

		// check out the serialized response object

		assertNotNull(response.serialize());
		assertFalse("".equals(response.serialize()));

		// create a response from a request
		// and make it an error

		TransferObjectOwnershipResponse errorResponse =
				new TransferObjectOwnershipResponse();

		try {
			errorResponse.initialization(objectRequestFromXML.serialize());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		errorResponse.setSuccess(false);
		errorResponse
				.setErrorDescription("Object ownership transfer request is denied.");

		// check out the serialized response object

		assertNotNull(errorResponse.serialize());
		assertFalse("".equals(errorResponse.serialize()));

		writeToFile(response, false);

	}

}
