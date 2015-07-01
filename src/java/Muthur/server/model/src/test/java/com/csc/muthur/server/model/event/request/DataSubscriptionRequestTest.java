/**
 * 
 */
package com.csc.muthur.server.model.event.request;

import java.util.Set;
import java.util.UUID;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.event.AbstractModelTest;

/**
 * 
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public class DataSubscriptionRequestTest extends AbstractModelTest {

	private String eventSourceName = "NexSim";
	private String SPAWN_FLIGHT_CLASS = "SpawnFlight";
	private String FLIGHT_POSITION_CLASS = "FlightPosition";

	private String federateRegistrationHandle;
	private String feh;

	private String ownershipEventQueueName = "test-ownership-event-queue-name";

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Test method for
	 * {@link com.csc.muthur.server.model.event.request.DataSubscriptionRequest#DataSubscriptionRequest()}
	 * .
	 */
	public void testDataSubscriptionRequest() {

		federateRegistrationHandle = UUID.randomUUID().toString();
		feh = UUID.randomUUID().toString();

		DataSubscriptionRequest request = new DataSubscriptionRequest();
		assertNotNull(request);

		request.setTimeToLiveSecs(10);
		request.setSourceOfEvent(eventSourceName);

		request.setFederateRegistrationHandle(federateRegistrationHandle);
		request.setFederationExecutionHandle(feh);
		request
				.setSubscriptionQueueName("this.is.a.test.queue.name.for.subscriptions");

		request.setOwnershipEventQueueName(ownershipEventQueueName);
		request.addSubscription(SPAWN_FLIGHT_CLASS);
		request.addSubscription(FLIGHT_POSITION_CLASS);

		String xml = request.serialize();

		assertNotNull(xml);
		assertFalse("".equals(xml));

		writeToFile(request, false);

		DataSubscriptionRequest req2 = new DataSubscriptionRequest();

		try {
			req2.initialization(xml);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		Set<String> classNames = req2.getDataSubscriptions();

		for (String clazzName : classNames) {
			assert (clazzName.equalsIgnoreCase(SPAWN_FLIGHT_CLASS) || clazzName
					.equalsIgnoreCase(FLIGHT_POSITION_CLASS));
		}

	}

}
