/**
 * 
 */
package com.csc.muthur.server.model.event;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.csc.muthur.server.commons.exception.MuthurException;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class FederateHeartbeatEventTest extends AbstractModelTest {

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
	 * {@link com.csc.muthur.server.model.event.FederateHeartbeatEvent#FederateHeartbeatEvent()}
	 * .
	 */
	@Test
	public void testFederateHeartbeatEvent() {

		FederateHeartbeatEvent fhbe = new FederateHeartbeatEvent();

		String xml = fhbe.serialize();

		assertNotNull(xml);
		assertFalse("".equals(xml));

		writeToFile(fhbe, false);

		FederateHeartbeatEvent fhbeFromXML = new FederateHeartbeatEvent();

		try {
			fhbeFromXML.initialization(xml);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(fhbeFromXML.equals(fhbe));
	}

}
