/**
 * 
 */
package com.csc.muthur.server.model.event.response;

import java.util.Set;
import java.util.UUID;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.event.AbstractModelTest;
import com.csc.muthur.server.model.event.request.DataSubscriptionRequest;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 * 
 */
public class DataSubscriptionResponseTest extends AbstractModelTest {

	private String eventSourceName = "NexSim";
	private String SPAWN_FLIGHT_CLASS = "SpawnFlight";
	private String FLIGHT_POSITION_CLASS = "FlightPosition";

	private String federateRegistrationHandle;
	private String feh;

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
	 * 
	 */
	public void testDataSubscriptionResponse() {

		federateRegistrationHandle = UUID.randomUUID().toString();
		feh = UUID.randomUUID().toString();

		DataSubscriptionRequest req = new DataSubscriptionRequest();
		assertNotNull(req);

		req.setTimeToLiveSecs(10);
		req.setSourceOfEvent(eventSourceName);
		req.setSubscriptionQueueName("this.is.a.test.queue.name.for.subscriptions");
		req.setFederateRegistrationHandle(federateRegistrationHandle);
		req.setFederationExecutionHandle(feh);

		req.addSubscription(SPAWN_FLIGHT_CLASS);
		req.addSubscription(FLIGHT_POSITION_CLASS);

		String xml = req.serialize();

		assertNotNull(xml);
		assertFalse("".equals(xml));

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

		DataSubscriptionResponse response = new DataSubscriptionResponse();

		try {
			response.initialization(req2.serialize());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		xml = response.serialize();

		assertNotNull(xml);
		assertFalse("".equals(xml));

		writeToFile(response, false);

	}

}
