/**
 * 
 */
package com.csc.muthur.server.model.event.request;

import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.AccessControl;
import com.csc.muthur.server.model.event.AbstractModelTest;
import com.csc.muthur.server.model.event.EventAttributeName;
import com.csc.muthur.server.model.event.data.flight.Aircraft;

/**
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class UpdateObjectAccessControlRequestTest extends AbstractModelTest {

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
	public void testUpdateObjectAccessControlCreationAndSerialization() {

		Aircraft aircraft = null;

		try {
			aircraft = new Aircraft(tailNumber, acid);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(aircraft);

		assertEquals(aircraft.getCallSign(), acid);
		assertEquals(aircraft.getTailNumber(), tailNumber);
		assertNotNull(aircraft.getDataObjectUUID());
		assertFalse(aircraft.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(aircraft.getObjectCreateTimeMSecs() != 0L);

		// create a UpdateObjectAccessControlRequest

		UpdateObjectAccessControlRequest requestObject =
				new UpdateObjectAccessControlRequest();
		assertNotNull(requestObject);

		requestObject.setFederationExecutionHandle(UUID.randomUUID().toString());
		requestObject.setFederateRegistrationHandle(UUID.randomUUID().toString());
		requestObject.setFederationExecutionModelHandle(UUID.randomUUID()
				.toString());
		requestObject.setDataObjectUUID(aircraft.getDataObjectUUID());
		requestObject.setSourceOfEvent("Testing");
		requestObject.setTimeToLiveSecs(30);

		requestObject.addAttributeAccessControlUpdate(EventAttributeName.routePlan
				.toString(), AccessControl.READ_WRITE);
		requestObject.addAttributeAccessControlUpdate(EventAttributeName.altitudeFt
				.toString(), AccessControl.READ_WRITE);
		requestObject.addAttributeAccessControlUpdate(
				EventAttributeName.latitudeDegrees.toString(), AccessControl.READ_ONLY);
		requestObject
				.addAttributeAccessControlUpdate(EventAttributeName.longitudeDegrees
						.toString(), AccessControl.READ_ONLY);

		// serialize the object

		String objectAsXML = requestObject.serialize();

		// do standard validation

		assertNotNull(objectAsXML);
		assertFalse(objectAsXML.equals(""));

		// create another object from previous object's serialized form

		UpdateObjectAccessControlRequest objectReqFromXML =
				new UpdateObjectAccessControlRequest();

		try {
			objectReqFromXML.initialization(objectAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// ensure that the two objects are equal

		assertTrue(objectReqFromXML.equals(requestObject));

		// get the serialized version of the new object

		String newObjSerializedAsXML = objectReqFromXML.serialize();

		assertNotNull(newObjSerializedAsXML);
		assertFalse(newObjSerializedAsXML.equals(""));

		writeToFile(requestObject, false);
	}
}
