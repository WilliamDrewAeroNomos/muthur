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
public class PauseFederateRequestTest extends AbstractModelTest {

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
	public void testPauseFederateRequest() {

		PauseFederateRequest req = new PauseFederateRequest();

		req.setTimeToLiveSecs(10);
		req.setSourceOfEvent("Muthur");
		req.setFederationExecutionModelHandle(UUID.randomUUID().toString());
		req.setLengthOfPauseMSecs(10000); // 10 secs

		assertNotNull(req);

		String objectAsXML = req.serialize();

		assertNotNull(objectAsXML);
		assertFalse("".equals(objectAsXML));

		writeToFile(req, false);

		PauseFederateRequest reqFromXML = new PauseFederateRequest();

		try {
			reqFromXML.initialization(objectAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(reqFromXML.equals(req));

	}

}
