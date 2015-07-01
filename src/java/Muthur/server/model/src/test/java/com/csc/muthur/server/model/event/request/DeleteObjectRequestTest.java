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
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class DeleteObjectRequestTest extends AbstractModelTest {

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

	@Test
	public void testDeleteObjectRequestWithSpawnAirCraftDataObject() {

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

		// create a DeleteObjectRequest

		DeleteObjectRequest deleteObjectReq = new DeleteObjectRequest();
		assertNotNull(deleteObjectReq);

		deleteObjectReq.setFederationExecutionHandle(UUID.randomUUID().toString());
		deleteObjectReq.setFederateRegistrationHandle(UUID.randomUUID().toString());
		deleteObjectReq.setFederationExecutionModelHandle(UUID.randomUUID()
				.toString());

		deleteObjectReq.setDataObjectUUID(sacd.getDataObjectUUID());

		// serialize the object

		String deleteObjectAsXML = deleteObjectReq.serialize();

		// do standard validation

		assertNotNull(deleteObjectAsXML);
		assertFalse(deleteObjectAsXML.equals(""));

		writeToFile(deleteObjectReq, false);

		// create another object from previous object's serialized form

		DeleteObjectRequest deleteObjectReqFromXML = new DeleteObjectRequest();

		try {
			deleteObjectReqFromXML.initialization(deleteObjectAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// ensure that the two objects are equal

		assertTrue(deleteObjectReqFromXML.equals(deleteObjectReq));

		// get the serialized version of the new object

		String newObjSerializedAsXML = deleteObjectReqFromXML.serialize();

		assertNotNull(newObjSerializedAsXML);
		assertFalse(newObjSerializedAsXML.equals(""));

		String deletedDataObjectUUID = deleteObjectReqFromXML.getDataObjectUUID();

		assert (deletedDataObjectUUID != null);
		assertTrue(!deletedDataObjectUUID.equalsIgnoreCase(""));
		assert (deletedDataObjectUUID.equalsIgnoreCase(deleteObjectReq
				.getDataObjectUUID()));

	}

}
