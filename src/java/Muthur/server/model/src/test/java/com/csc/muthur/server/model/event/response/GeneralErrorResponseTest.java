/**
 * 
 */
package com.csc.muthur.server.model.event.response;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.csc.muthur.server.commons.MessageDestination;
import com.csc.muthur.server.model.event.AbstractModelTest;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class GeneralErrorResponseTest extends AbstractModelTest {

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

	@Test
	public void testGeneralErrorResponse() {

		GeneralErrorResponse ger = new GeneralErrorResponse();

		ger.setSuccess(false);
		ger.setSourceOfEvent(MessageDestination.MUTHUR);
		ger.setTimeToLiveSecs(30);
		ger.setStatus("complete");
		ger.setErrorDescription("JUnit error description");

		String serializedObject = ger.serialize();

		assertNotNull(serializedObject);

		writeToFile(ger, false);

	}

}
