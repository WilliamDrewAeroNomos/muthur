/**
 * 
 */
package com.csc.muthur.server.model.event.response;

import java.util.UUID;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.event.AbstractModelTest;
import com.csc.muthur.server.model.event.request.ReadyToRunRequest;

/**
 * @author <a href=mailto:Nexsim@foo.com>Nexsim</a>
 * @version $Revision$
 */
public class ReadyToRunResponseTest extends AbstractModelTest {

	private String eventSourceName = "NexSim";
	private String federateRegistrationHandle;
	private String feh;

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

	public void testReadyToRunRequest() {

		String federationHostName = "HostName.com";
		int federationPort = 5555;

		federateRegistrationHandle = UUID.randomUUID().toString();
		feh = UUID.randomUUID().toString();

		// create the request
		ReadyToRunRequest req = new ReadyToRunRequest();
		assertNotNull(req);

		req.setTimeToLiveSecs(10);
		req.setSourceOfEvent(eventSourceName);
		req.setFederateRegistrationHandle(federateRegistrationHandle);
		req.setFederationExecutionHandle(feh);

		String requestAsXML = req.serialize();
		assertNotNull(requestAsXML);
		assertFalse("".equals(requestAsXML));

		ReadyToRunRequest rrFromXML = null;
		try {
			rrFromXML = new ReadyToRunRequest();
			rrFromXML.initialization(requestAsXML);
			assertNotNull(rrFromXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assert (rrFromXML.equals(req));

		ReadyToRunResponse response = new ReadyToRunResponse();

		try {
			response.initialization(rrFromXML.serialize());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		response.setFederationHostName(federationHostName);
		response.setFederationPort(federationPort);

		assertNotNull(response);

		assert (response.getFederateRegistrationHandle()
				.equalsIgnoreCase(federateRegistrationHandle));
		assert (response.getFederationExecutionHandle().equalsIgnoreCase(feh));

		String responsAsXML = response.serialize();

		assertNotNull(responsAsXML);
		assertFalse("".equals(responsAsXML));

		ReadyToRunResponse responseFromXML = new ReadyToRunResponse();

		try {
			responseFromXML.initialization(responsAsXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		writeToFile(responseFromXML, false);
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
