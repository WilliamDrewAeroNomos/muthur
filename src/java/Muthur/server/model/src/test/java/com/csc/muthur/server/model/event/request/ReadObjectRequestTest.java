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
public class ReadObjectRequestTest extends AbstractModelTest {

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
	public void testReadObjectRequestWithSpawnAirCraftDataObject() {

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

		// create a ReadObjectRequest

		ReadObjectRequest readObjectReq = new ReadObjectRequest();
		assertNotNull(readObjectReq);

		readObjectReq.setDataObjectUUID(sacd.getDataObjectUUID());
		readObjectReq.setFederationExecutionHandle(UUID.randomUUID().toString());
		readObjectReq.setFederateRegistrationHandle(UUID.randomUUID().toString());
		readObjectReq.setSourceOfEvent("NexSim");
		readObjectReq.setFederationExecutionModelHandle(UUID.randomUUID()
				.toString());

		// serialize the object

		String readObjectAsXML = readObjectReq.serialize();

		// do standard validation

		assertNotNull(readObjectAsXML);
		assertFalse(readObjectAsXML.equals(""));

		writeToFile(readObjectReq, false);

		// create another object from previous object's serialized form

		ReadObjectRequest readObjectReqFromXML = new ReadObjectRequest();

		try {
			readObjectReqFromXML.initialization(readObjectAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// ensure that the two objects are equal

		assertTrue(readObjectReqFromXML.equals(readObjectReq));

		// get the serialized version of the new object

		String newObjSerializedAsXML = readObjectReqFromXML.serialize();

		assertNotNull(newObjSerializedAsXML);
		assertFalse(newObjSerializedAsXML.equals(""));

	}

}
