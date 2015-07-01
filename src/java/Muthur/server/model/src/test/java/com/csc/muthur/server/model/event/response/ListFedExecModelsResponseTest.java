/**
 * 
 */
package com.csc.muthur.server.model.event.response;

import java.util.Iterator;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.event.AbstractModelTest;
import com.csc.muthur.server.model.event.request.ListFedExecModelsRequest;

/**
 * @author <a href=mailto:Nexsim@foo.com>Nexsim</a>
 * @version $Revision$
 */
public class ListFedExecModelsResponseTest extends AbstractModelTest {

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
	 * {@link com.csc.muthur.server.model.event.response.ListFedExecModelsResponse#ListFedExecModelsResponse(int)}
	 * .
	 */
	@Test
	public void testListFedExecModelsResponseInt() {

		// create a request

		ListFedExecModelsRequest req = new ListFedExecModelsRequest();
		assertNotNull(req);

		// ...and set the ttyl value
		req.setTimeToLiveSecs(10);

		// test that it serializes
		String xml = req.serialize();
		assertNotNull(xml);
		assertFalse("".equals(xml));

		// take the serialized object XML and reconstitute a new request
		ListFedExecModelsRequest rrFromXML = null;
		try {
			rrFromXML = new ListFedExecModelsRequest();
			rrFromXML.initialization(xml);

		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// check that it's valid and equals the previous XML string
		assertNotNull(rrFromXML);
		assert (rrFromXML.serialize().equals(xml));

		// test the creation of a fully populated FEM along with multiple FEMs

		ListFedExecModelsResponse response = null;

		try {
			response = new ListFedExecModelsResponse();
			response.initialization(xml);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// create a complex FEM and add it to the response

		FederationExecutionModel frascaNexSimFEM = null;
		try {
			frascaNexSimFEM =
					new FederationExecutionModel("Frasca NexSim integration");
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		frascaNexSimFEM
				.setDescription("Federation of the NexSim ATC and Frasca flight simulators");
		frascaNexSimFEM.addRequiredFededrate("NexSim");
		frascaNexSimFEM.addRequiredFededrate("Frasca");
		frascaNexSimFEM.setDurationFederationExecutionMSecs(120000);
		frascaNexSimFEM.setDurationJoinFederationMSecs(30000);
		frascaNexSimFEM.setDurationRegisterPublicationMSecs(31000);
		frascaNexSimFEM.setDurationRegisterSubscriptionMSecs(32000);
		frascaNexSimFEM.setDurationRegisterToRunMSecs(33000);
		frascaNexSimFEM.setDurationTimeToWaitAfterTerminationMSecs(34000);
		frascaNexSimFEM.setLogicalStartTimeMSecs(System.currentTimeMillis());
		frascaNexSimFEM.setAutoStart(false);

		response.addFEM(frascaNexSimFEM);

		// add a default FEM
		FederationExecutionModel defaultFEM = null;
		try {
			defaultFEM = new FederationExecutionModel("Default FEM");
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}
		defaultFEM.setDescription("This is the default FEM");

		response.addFEM(defaultFEM);

		// serialize

		String responseXML = response.serialize();

		assertNotNull(responseXML);

		// create a new response object from the serialized XML to test
		// reconstitution

		try {
			response = new ListFedExecModelsResponse();
			assertNotNull(response);
			response.initialization(responseXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		String reconstitutedObjectAsXML = response.serialize();
		assert (reconstitutedObjectAsXML != null);
		assert (reconstitutedObjectAsXML.equals(responseXML));

		// test iterating over the returned list of FEMs

		response.getFemList();

		Iterator<FederationExecutionModel> iter = response.getFemList().iterator();

		while (iter.hasNext()) {
			FederationExecutionModel nextFEM = iter.next();
			// check for null
			assert (nextFEM != null);
			// check that it's at least one of the FEMs that was added
			assert (((nextFEM.equals(frascaNexSimFEM)) || (nextFEM.equals(defaultFEM))));
		}

		response.setSuccess(false);
		response.setErrorDescription("Federation execution model not found");

		String xmlWithError = response.serialize();

		assert (xmlWithError != null);

		writeToFile(response, false);
	}
}
