/**
 * 
 */
package com.csc.muthur.server.model.event.request;

import java.util.UUID;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.event.AbstractModelTest;

/**
 * @author <a href=mailto:Nexsim@foo.com>Nexsim</a>
 * @version $Revision$
 */
public class ReadyToRunRequestTest extends AbstractModelTest {

	private String eventSourceName = "NexSim";
	private String federateRegistrationHandle;
	private String feh;
	private String timeManagementQueueName = "test-time-management-queue-name";
	private String federateRequestQueueName = "test-federate-request-queue-name";

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

		federateRegistrationHandle = UUID.randomUUID().toString();
		feh = UUID.randomUUID().toString();

		// create the request
		ReadyToRunRequest readyToRunRequest = new ReadyToRunRequest();

		assertNotNull(readyToRunRequest);

		readyToRunRequest.setTimeToLiveSecs(10);
		readyToRunRequest.setSourceOfEvent(eventSourceName);
		readyToRunRequest.setFederateRegistrationHandle(federateRegistrationHandle);
		readyToRunRequest.setFederationExecutionModelHandle(UUID.randomUUID()
				.toString());
		readyToRunRequest.setTimeManagementQueueName(timeManagementQueueName);
		readyToRunRequest.setFederateRequestQueueName(federateRequestQueueName);

		readyToRunRequest.setFederationExecutionHandle(feh);

		String xml = readyToRunRequest.serialize();
		assertNotNull(xml);
		assertFalse("".equals(xml));

		writeToFile(readyToRunRequest, false);

		ReadyToRunRequest readyToRunRequestFromXML = null;

		try {
			readyToRunRequestFromXML = new ReadyToRunRequest();
			readyToRunRequestFromXML.initialization(xml);
			assertNotNull(readyToRunRequestFromXML);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assert (readyToRunRequestFromXML.equals(readyToRunRequest));

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
