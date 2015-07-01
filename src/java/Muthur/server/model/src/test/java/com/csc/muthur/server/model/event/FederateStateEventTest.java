/**
 * 
 */
package com.csc.muthur.server.model.event;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.csc.muthur.server.commons.MessageDestination;
import com.csc.muthur.server.commons.exception.MuthurException;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>NexSim</a>
 * @version $Revision$
 * 
 */
public class FederateStateEventTest extends AbstractModelTest {

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

	/*
	 * 
	 */
	@Test
	public void testFederateStateEventCreation() {

		FederateStateEvent fse = new FederateStateEvent();
		fse.setSourceOfEvent(MessageDestination.MUTHUR);
		fse.setFederateState(0);
		fse.setFederateName("TestFederate");

		String xml = fse.serialize();

		assertNotNull(xml);
		assertFalse("".equals(xml));

		FederateStateEvent fseFromXML = new FederateStateEvent();

		try {
			fseFromXML.initialization(xml);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(fseFromXML.equals(fse));

		writeToFile(fse, false);
	}
}
