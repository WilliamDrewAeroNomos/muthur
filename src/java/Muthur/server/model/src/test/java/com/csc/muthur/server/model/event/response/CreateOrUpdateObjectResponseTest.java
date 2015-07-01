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
import com.csc.muthur.server.model.event.request.CreateOrUpdateObjectRequest;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 * 
 */
public class CreateOrUpdateObjectResponseTest extends AbstractModelTest {

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
	public void testCreateOrUpdateObjectRequestandResponseWithSpawnAirCraftDataObject() {
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

		// create a CreateOrUpdateObjectRequest

		CreateOrUpdateObjectRequest cor = new CreateOrUpdateObjectRequest();
		assertNotNull(cor);

		cor.setSourceOfEvent("Test");
		cor.setDataObject(sacd);
		cor.setFederationExecutionHandle(UUID.randomUUID().toString());
		cor.setFederateRegistrationHandle(UUID.randomUUID().toString());

		// serialize the object

		String corAsXML = cor.serialize();

		// do standard validation

		assertNotNull(corAsXML);
		assertFalse(corAsXML.equals(""));

		// create another object from previous object's serialized form

		CreateOrUpdateObjectRequest objectFromXML =
				new CreateOrUpdateObjectRequest();

		try {
			objectFromXML.initialization(corAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// ensure that the two objects are equal

		assertTrue(objectFromXML.equals(cor));

		// get the serialized version of the new object

		String newObjSerializedAsXML = objectFromXML.serialize();

		assertNotNull(newObjSerializedAsXML);
		assertFalse(newObjSerializedAsXML.equals(""));

		IBaseDataObject bdo = objectFromXML.getDataObject();

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

		// create a response from a request
		// allows maintenance of the internal event values

		CreateObjectResponse response = new CreateObjectResponse();

		try {
			response.initialization(objectFromXML.serialize());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// check out the serialized response object

		assertNotNull(response.serialize());
		assertFalse("".equals(response.serialize()));

		// create a response from a request
		// and make it an error

		CreateObjectResponse errorResponse = new CreateObjectResponse();

		try {
			errorResponse.initialization(objectFromXML.serialize());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		errorResponse.setSuccess(false);
		errorResponse.setErrorDescription("This is a test error description");

		// check out the serialized response object

		assertNotNull(errorResponse.serialize());
		assertFalse("".equals(errorResponse.serialize()));

		writeToFile(response, false);
	}

}
