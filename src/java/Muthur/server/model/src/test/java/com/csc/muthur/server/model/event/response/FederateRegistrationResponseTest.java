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
import com.csc.muthur.server.model.event.request.FederateRegistrationRequest;

/**
 * @author wdrew
 * 
 */
public class FederateRegistrationResponseTest extends AbstractModelTest {

	private static String TEST_FEDERATE_NAME = "MyTestFederate";
	private static int TEST_TTL = 10;

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
	 * {@link com.csc.muthur.server.model.response.RegistrationResponse#RegistrationResponse(java.lang.String, com.csc.muthur.server.model.request.FederateRequest)}
	 * .
	 */
	@Test
	public void testRegistrationResponse() {

		FederateRegistrationRequest request = new FederateRegistrationRequest();
		request.setSourceOfEvent(TEST_FEDERATE_NAME);
		request.setTimeToLiveSecs(TEST_TTL);
		request.setFederateName(TEST_FEDERATE_NAME);
		request.setStatus("success");
		request.setSuccess(true);

		String requestAsXML = request.serialize();

		assertNotNull(requestAsXML);
		assertFalse("Serialized request was empty", ("".equals(requestAsXML)));

		FederateRegistrationResponse response = null;
		try {
			response = new FederateRegistrationResponse();
			response.initialization(requestAsXML);
			response.setFederateRegistrationHandle(UUID.randomUUID().toString());
			response.setSuccess(true);
			response.setStatus("completed");
			response.setbHeartBeanEnabled(false);

			// serialize
			String responseAsXML = response.serialize();

			// assert still valid
			assertNotNull(responseAsXML);
			assertFalse("Serialized response was empty", ("".equals(responseAsXML)));

			// instantiate a new object and initialize
			FederateRegistrationResponse responseFromXML =
					new FederateRegistrationResponse();
			responseFromXML.initialization(responseAsXML);

			// test that they're equal
			assertEquals(responseFromXML, response);

		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		writeToFile(response, false);

	}

	@Test
	public void testRegistrationResponseException() {

		FederateRegistrationRequest rr = new FederateRegistrationRequest();
		rr.setSourceOfEvent(TEST_FEDERATE_NAME);
		rr.setTimeToLiveSecs(TEST_TTL);
		rr.setFederateName(TEST_FEDERATE_NAME);

		rr.setStatus("success");
		rr.setSuccess(true);

		String xml = rr.serialize();

		assertNotNull(xml);
		assertFalse("Serialized response was empty", ("".equals(xml)));

		try {
			FederateRegistrationResponse response =
					new FederateRegistrationResponse();
			response.initialization(xml);
			response.setFederateRegistrationHandle(UUID.randomUUID().toString());
			response.setSuccess(false);
			response.setStatus("failed");
			response.setErrorDescription("Error set during unit testing");

			String responseXML = response.serialize();

			assertNotNull(responseXML);

			assertTrue(response.getErrorDescription().equalsIgnoreCase(
					"Error set during unit testing"));

		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}
	}
}
