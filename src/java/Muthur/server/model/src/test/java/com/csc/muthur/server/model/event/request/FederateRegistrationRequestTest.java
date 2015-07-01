/**
 * 
 */
package com.csc.muthur.server.model.event.request;

import java.util.UUID;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.event.AbstractModelTest;

/**
 * @author williamdrew
 * 
 */
public class FederateRegistrationRequestTest extends AbstractModelTest {

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * 
	 */
	public void testFederateRegistration() {

		FederateRegistrationRequest fr = new FederateRegistrationRequest();

		fr.setSourceOfEvent("FederateTestName");
		fr.setFederateName(fr.getSourceOfEvent());
		fr.setFederateEventQueueName("federate-queue-name-01");
		fr.setFederateHeartBeatIntervalSecs(2); // seems good
		fr.setLicenseKey(UUID.randomUUID().toString());

		assertNotNull(fr);
		String xml = fr.serialize();
		assertNotNull(xml);
		assertFalse("".equals(xml));

		FederateRegistrationRequest frFromXML = null;
		try {
			frFromXML = new FederateRegistrationRequest();

			assertNotNull(frFromXML);
			frFromXML.initialization(xml);

		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(frFromXML.equals(fr));

		writeToFile(frFromXML, false);
	}
}
