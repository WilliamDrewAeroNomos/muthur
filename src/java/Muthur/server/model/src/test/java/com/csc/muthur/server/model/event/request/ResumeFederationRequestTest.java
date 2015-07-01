/**
 * 
 */
package com.csc.muthur.server.model.event.request;

import java.util.UUID;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.event.AbstractModelTest;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class ResumeFederationRequestTest extends AbstractModelTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

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
	public void testResumeFederationRequest() {

		ResumeFederationRequest req = new ResumeFederationRequest();

		req.setTimeToLiveSecs(10);
		req.setSourceOfEvent("Muthur");
		req.setFederationExecutionModelHandle(UUID.randomUUID().toString());

		assertNotNull(req);

		String objectAsXML = req.serialize();

		assertNotNull(objectAsXML);
		assertFalse("".equals(objectAsXML));

		ResumeFederationRequest reqFromXML = new ResumeFederationRequest();

		try {
			reqFromXML.initialization(objectAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(reqFromXML.equals(req));

		writeToFile(req, false);
	}

}
