/**
 * 
 */
package com.csc.muthur.server.model.event.response;

import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.event.AbstractModelTest;
import com.csc.muthur.server.model.event.request.JoinFederationRequest;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class JoinFederationResponseTest extends AbstractModelTest {

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
	 * {@link com.csc.muthur.server.model.event.response.JoinFederationResponse#JoinFederationResponse()}
	 * .
	 */
	@Test
	public void testJoinFederationResponse() {

		String eventSourceName = "NexSim";
		String nexSimName = "NexSim";
		String FrascaName = "Frasca";
		String name = "Test Model";
		String description = "This is a test model";
		int femDurationMSecs = 60;
		int reqDurationMSecs = 10;
		String fedExecModelUUID = UUID.randomUUID().toString();

		Set<String> namesOfRequiredFederates = new TreeSet<String>();
		namesOfRequiredFederates.add(nexSimName);
		namesOfRequiredFederates.add(FrascaName);
		String federationExecutionHandle = UUID.randomUUID().toString();
		String federateRegistrationHandle = UUID.randomUUID().toString();

		FederationExecutionModel federationExecutionModel = null;
		try {
			federationExecutionModel = new FederationExecutionModel(name);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		federationExecutionModel.setDescription(description);
		federationExecutionModel
				.setDurationFederationExecutionMSecs(femDurationMSecs);
		federationExecutionModel.setFedExecModelUUID(fedExecModelUUID);
		federationExecutionModel.setLogicalStartTimeMSecs(System
				.currentTimeMillis());
		federationExecutionModel
				.setNamesOfRequiredFederates(namesOfRequiredFederates);

		JoinFederationRequest req = new JoinFederationRequest();

		assertNotNull(req);

		req.setTimeToLiveSecs(reqDurationMSecs);
		req.setSourceOfEvent(eventSourceName);
		req.setFederateRegistrationHandle(federateRegistrationHandle);
		req.setFederationExecutionModel(federationExecutionModel);
		req.setFederationExecutionHandle(federationExecutionHandle);

		String objectAsXML = req.serialize();

		assertNotNull(objectAsXML);
		assertFalse("".equals(objectAsXML));

		JoinFederationRequest reqFromXML = new JoinFederationRequest();

		try {
			reqFromXML.initialization(objectAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertTrue(reqFromXML.getFederationExecutionModel().equals(
				req.getFederationExecutionModel()));

		assertTrue(reqFromXML.getFederateRegistrationHandle().equals(
				req.getFederateRegistrationHandle()));

		reqFromXML.serialize().equalsIgnoreCase(objectAsXML);

		// JoinFederationResponse

		JoinFederationResponse response = new JoinFederationResponse();

		try {
			response.initialization(objectAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		response.setSuccess(false);

		String xmlFromResponse = response.serialize();

		assertNotNull(xmlFromResponse);

		response.setSuccess(true);

		xmlFromResponse = response.serialize();

		assertNotNull(xmlFromResponse);

		writeToFile(response, false);
	}

}
