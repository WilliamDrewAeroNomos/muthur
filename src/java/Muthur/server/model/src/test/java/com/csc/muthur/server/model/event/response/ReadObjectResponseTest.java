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
import com.csc.muthur.server.model.event.data.flight.Aircraft;
import com.csc.muthur.server.model.event.request.ReadObjectRequest;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 * 
 */
public class ReadObjectResponseTest extends AbstractModelTest {

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
	 * 
	 */
	@Test
	public void testReadObjectRequestandResponseWithSpawnAirCraftDataObject() {

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

		// create a CreateObjectRequest

		ReadObjectRequest readObjectRequest = new ReadObjectRequest();
		assertNotNull(readObjectRequest);

		readObjectRequest.setSourceOfEvent("Test");
		readObjectRequest.setDataObjectUUID(sacd.getDataObjectUUID());
		readObjectRequest
				.setFederationExecutionHandle(UUID.randomUUID().toString());
		readObjectRequest.setFederateRegistrationHandle(UUID.randomUUID()
				.toString());
		readObjectRequest.setFederationExecutionModelHandle(UUID.randomUUID()
				.toString());

		// serialize the object

		String readObjectRequestAsXML = readObjectRequest.serialize();

		// do standard validation

		assertNotNull(readObjectRequestAsXML);
		assertFalse(readObjectRequestAsXML.equals(""));

		// create another object from previous object's serialized form

		ReadObjectRequest readObjectRequestFromXML = new ReadObjectRequest();

		try {
			readObjectRequestFromXML.initialization(readObjectRequestAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// ensure that the two objects are equal

		assertTrue(readObjectRequestFromXML.equals(readObjectRequest));

		// get the serialized version of the new object

		String newObjSerializedAsXML = readObjectRequestFromXML.serialize();

		assertNotNull(newObjSerializedAsXML);
		assertFalse(newObjSerializedAsXML.equals(""));

		String dataObjectUUID = readObjectRequestFromXML.getDataObjectUUID();

		assertNotNull(dataObjectUUID);

		// create a response from a request
		// allows maintenance of the internal event values

		ReadObjectResponse response = new ReadObjectResponse();

		try {
			response.initialization(readObjectRequestFromXML.serialize());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		response.setDataObject(sacd);

		// check out the serialized response object

		assertNotNull(response.serialize());
		assertFalse("".equals(response.serialize()));

		IBaseDataObject bdo = response.getDataObject();

		assertNotNull(bdo);
		assertTrue(bdo instanceof Aircraft);

		// create a response from a request
		// allows maintenance of the internal event values

		response = new ReadObjectResponse();

		try {
			response.initialization(readObjectRequestFromXML.serialize());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		response.setDataObject(null);

		// check out the serialized response object

		assertNotNull(response.serialize());
		assertFalse("".equals(response.serialize()));

		bdo = response.getDataObject();

		assertNull(bdo);

		// create a response from a request and make it an error

		ReadObjectResponse errorResponse = new ReadObjectResponse();

		try {
			errorResponse.initialization(readObjectRequestFromXML.serialize());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		errorResponse.setSuccess(false);
		errorResponse.setErrorDescription("No object found for supplied object id");

		// check out the serialized response object

		assertNotNull(errorResponse.serialize());
		assertFalse("".equals(errorResponse.serialize()));

		writeToFile(response, false);
	}

}
