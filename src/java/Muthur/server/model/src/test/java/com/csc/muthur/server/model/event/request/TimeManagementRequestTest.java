/**
 * 
 */
package com.csc.muthur.server.model.event.request;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.FederationExecutionDirective;
import com.csc.muthur.server.model.FederationExecutionState;
import com.csc.muthur.server.model.event.AbstractModelTest;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class TimeManagementRequestTest extends AbstractModelTest {

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
	 * Test method for
	 * {@link com.csc.muthur.server.model.event.request.TimeManagementRequest#TimeManagementRequest()}
	 * .
	 */
	@Test
	public void testTimeManagementRequest() {

		TimeManagementRequest req = new TimeManagementRequest();

		req.setTimeToLiveSecs(10);
		req.setSourceOfEvent("Muthur");
		req.setTimeIntervalMSecs(System.currentTimeMillis());
		req.setFederationExecutionDirective(FederationExecutionDirective.PAUSE);
		req.setFederationExecutionState(FederationExecutionState.PAUSED);

		assertNotNull(req);

		String objectAsXML = req.serialize();

		assertNotNull(objectAsXML);
		assertFalse("".equals(objectAsXML));

		TimeManagementRequest reqFromXML = new TimeManagementRequest();

		try {
			reqFromXML.initialization(objectAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(reqFromXML.equals(req));

		writeToFile(req, false);

	}

}
