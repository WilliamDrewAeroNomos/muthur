/**
 * 
 */
package com.csc.muthur.server.model.event.request;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.event.AbstractModelTest;
import com.csc.muthur.server.model.event.response.ListFedExecModelsResponse;

/**
 * 
 * @author <a href=mailto:Nexsim@foo.com>Nexsim</a>
 * @version $Revision$
 */
public class ListFedExecModelsRequestTest extends AbstractModelTest {

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

	public void testListFedExecModelsRequest() {

		String groupName = "MTSU FOCUS";

		ListFedExecModelsRequest req = new ListFedExecModelsRequest();
		req.setTimeToLiveSecs(10);
		req.setGroupName(groupName);

		assertNotNull(req);
		String xml = req.serialize();
		assertNotNull(xml);
		assertFalse("".equals(xml));

		try {
			ListFedExecModelsRequest rrFromXML = new ListFedExecModelsRequest();
			rrFromXML.initialization(xml);
			assertNotNull(rrFromXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		ListFedExecModelsResponse response = new ListFedExecModelsResponse();
		req.setTimeToLiveSecs(10);

		assertNotNull(response);

		List<FederationExecutionModel> fems = response.getFemList();

		assertNull(fems);

		String xmlResponse = response.serialize();

		assertNotNull(xmlResponse);

		writeToFile(req, false);
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

}
