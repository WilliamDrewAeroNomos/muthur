/**
 * 
 */
package com.csc.muthur.server.federation.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.UUID;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.junit.Test;

import com.csc.muthur.server.commons.MessageDestination;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.federation.internal.execution.FederationExecutionControllerImpl;
import com.csc.muthur.server.federation.internal.execution.SubscriptionRequestProcessor;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.SubscriptionRegistrationFederationExecutionEntry;
import com.csc.muthur.server.model.event.request.DataSubscriptionRequest;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public class SubscriptionRequestProcessorTest extends
		AbstractBaseTestRequestProcessor {

	/**
	 * Standard processing for the join step using two federates and default
	 * settings in the FEM
	 */
	@Test
	public void baseFederationRequestProcessor() {

		try {
			fem = new FederationExecutionModel("Federation Exec Controller Test");
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}
		fem.addRequiredFededrate(nexSimFederateName);
		fem.addRequiredFededrate(frascaFederateName);
		fem.setDefaultDurationWithinStartupProtocolMSecs(30000);
		fem.setDurationFederationExecutionMSecs(10000);

		try {

			federationExecutionController =
					new FederationExecutionControllerImpl(federationService, fem);

		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// set the count down latch for the subscription request processor to 2

		SubscriptionRequestProcessor subscriptionRequestProcessor = null;
		try {
			subscriptionRequestProcessor =
					new SubscriptionRequestProcessor(federationExecutionController);
		} catch (MuthurException e2) {
			fail(e2.getLocalizedMessage());
		}

		Thread subscriptionRequestProcessorInstance =
				new Thread(subscriptionRequestProcessor);

		subscriptionRequestProcessorInstance.start();

		// create a DataSubscriptionRequest for each federate

		DataSubscriptionRequest req = new DataSubscriptionRequest();
		assertNotNull(req);

		req.setTimeToLiveSecs(10);
		req.setSourceOfEvent(eventSourceName);

		req.setFederateRegistrationHandle(federateRegistrationHandle);
		req.setFederationExecutionHandle(federationExecutionHandle);
		// req.setSubscriptionQueueName("this.is.a.test.queue.name.for.subscriptions");
		req.addSubscription("SpawnAircraft");
		req.addSubscription("FlightPositionUpdate");

		// establish the semaphore BEFORE you call registerParticipant() for each
		// federate

		sem = new Semaphore(0);

		TextMessage txtMessage = null;

		try {

			// create the TextMessage for each...

			txtMessage = session.createTextMessage();

			// ...set the join request as the serialized event

			txtMessage.setText(req.serialize());

			// Set the reply to field to the queue established in setUpBeforeClass()

			txtMessage.setJMSReplyTo(replyToQueue);
			txtMessage.setJMSCorrelationID(UUID.randomUUID().toString());

			txtMessage.setStringProperty(MessageDestination
					.getDataEventQueuePropName(), dataPublicationQueue.getQueueName());

			// create an execution entry for the NexSim federate and call
			// registerParticipant()

			SubscriptionRegistrationFederationExecutionEntry subReqFedExecEntry =
					new SubscriptionRegistrationFederationExecutionEntry(fem, req,
							txtMessage);

			subscriptionRequestProcessor.addEntry(subReqFedExecEntry);

			// create an execution entry for the Frasca federate

			req.setSourceOfEvent(frascaFederateName);

			subReqFedExecEntry =
					new SubscriptionRegistrationFederationExecutionEntry(fem, req,
							txtMessage);

			subscriptionRequestProcessor.addEntry(subReqFedExecEntry);

		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// the semaphore will be released by messages received in the
		// FedEventConsumer.onMessage()

		try {
			if (!sem.tryAcquire(fem.getNamesOfRequiredFederates().size(),
					subscriptionRequestProcessor.getTimeToLiveMSecs(),
					TimeUnit.MILLISECONDS)) {
				fail("Timed out before acquiring semaphore");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

	}

	/**
	 * Tests registration of a single federate
	 */
	@Test
	public void federationRequestProcessorSingleFederate() {

		// create a FEM with a single federate with defaults

		try {
			fem = new FederationExecutionModel("Single Federate Test");
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}
		fem.addRequiredFededrate(nexSimFederateName);
		fem.setDefaultDurationWithinStartupProtocolMSecs(30000);
		fem.setDurationFederationExecutionMSecs(10000);

		try {

			federationExecutionController =
					new FederationExecutionControllerImpl(federationService, fem);

		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// set the count down latch for the subscription request processor to 2

		SubscriptionRequestProcessor subscriptionRequestProcessor = null;
		try {
			subscriptionRequestProcessor =
					new SubscriptionRequestProcessor(federationExecutionController);
		} catch (MuthurException e2) {
			fail(e2.getLocalizedMessage());
		}

		Thread subscriptionRequestProcessorInstance =
				new Thread(subscriptionRequestProcessor);

		subscriptionRequestProcessorInstance.start();

		// create a DataSubscriptionRequest for each federate

		DataSubscriptionRequest req = new DataSubscriptionRequest();
		assertNotNull(req);

		req.setTimeToLiveSecs(10);
		req.setSourceOfEvent(eventSourceName);

		req.setFederateRegistrationHandle(federateRegistrationHandle);
		req.setFederationExecutionHandle(federationExecutionHandle);
		// req.setSubscriptionQueueName("this.is.a.test.queue.name.for.subscriptions");
		req.addSubscription("SpawnAircraft");
		req.addSubscription("FlightPositionUpdate");

		// establish the semaphore BEFORE you call registerParticipant() for each
		// federate

		sem = new Semaphore(0);

		TextMessage txtMessage = null;

		try {

			// create the TextMessage for each...

			txtMessage = session.createTextMessage();

			// ...set the join request as the serialized event

			txtMessage.setText(req.serialize());

			// Set the reply to field to the queue established in setUpBeforeClass()

			txtMessage.setJMSReplyTo(replyToQueue);
			txtMessage.setJMSCorrelationID(UUID.randomUUID().toString());

			txtMessage.setStringProperty(MessageDestination
					.getDataEventQueuePropName(), dataPublicationQueue.getQueueName());

			// create an execution entry for the NexSim federate

			SubscriptionRegistrationFederationExecutionEntry subReqFedExecEntry =
					new SubscriptionRegistrationFederationExecutionEntry(fem, req,
							txtMessage);

			subscriptionRequestProcessor.addEntry(subReqFedExecEntry);

		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// the semaphore will be released by messages received in the
		// FedEventConsumer.onMessage()

		try {
			if (!sem.tryAcquire(fem.getNamesOfRequiredFederates().size(),
					subscriptionRequestProcessor.getTimeToLiveMSecs(),
					TimeUnit.MILLISECONDS)) {
				fail("Timed out before acquiring semaphore");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

	}

	/**
	 * Tests error handling when a required federate does not register with
	 * default timing settings in FEM
	 */
	@Test
	public void federationRequestProcessorRequiredFederateNotRgisteredErrorHandling() {

		try {
			fem = new FederationExecutionModel("Federation Exec Controller Test");
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		fem.addRequiredFededrate(nexSimFederateName);
		fem.addRequiredFededrate(frascaFederateName);
		fem.setDefaultDurationWithinStartupProtocolMSecs(10000);
		fem.setDurationFederationExecutionMSecs(10000);

		try {

			federationExecutionController =
					new FederationExecutionControllerImpl(federationService, fem);

		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// set the count down latch for the subscription request processor to 2

		SubscriptionRequestProcessor subscriptionRequestProcessor = null;
		try {
			subscriptionRequestProcessor =
					new SubscriptionRequestProcessor(federationExecutionController);
		} catch (MuthurException e2) {
			fail(e2.getLocalizedMessage());
		}

		Thread subscriptionRequestProcessorInstance =
				new Thread(subscriptionRequestProcessor);

		subscriptionRequestProcessorInstance.start();

		// create a DataSubscriptionRequest for each federate

		DataSubscriptionRequest req = new DataSubscriptionRequest();
		assertNotNull(req);

		req.setTimeToLiveSecs(10);
		req.setSourceOfEvent(eventSourceName);

		req.setFederateRegistrationHandle(federateRegistrationHandle);
		req.setFederationExecutionHandle(federationExecutionHandle);
		// req.setSubscriptionQueueName("this.is.a.test.queue.name.for.subscriptions");
		req.addSubscription("SpawnAircraft");
		req.addSubscription("FlightPositionUpdate");

		// establish the semaphore BEFORE you call registerParticipant() for each
		// federate

		sem = new Semaphore(0);

		TextMessage txtMessage = null;

		try {

			// create the TextMessage for each...

			txtMessage = session.createTextMessage();

			// ...set the join request as the serialized event

			txtMessage.setText(req.serialize());

			// Set the reply to field to the queue established in setUpBeforeClass()

			txtMessage.setJMSReplyTo(replyToQueue);
			txtMessage.setJMSCorrelationID(UUID.randomUUID().toString());

			txtMessage.setStringProperty(MessageDestination
					.getDataEventQueuePropName(), dataPublicationQueue.getQueueName());

			// create an execution entry for the NexSim federate and call
			// registerParticipant()

			SubscriptionRegistrationFederationExecutionEntry subReqFedExecEntry =
					new SubscriptionRegistrationFederationExecutionEntry(fem, req,
							txtMessage);

			subscriptionRequestProcessor.addEntry(subReqFedExecEntry);

			// DO NOT create an execution entry for the required Frasca federate
			//
			// req.setSourceOfEvent(frascaFederateName);
			//
			// subscriptionRegistrationFederationExecutionEntry = new
			// SubscriptionRegistrationFederationExecutionEntry(
			// fem, req, txtMessage);
			//
			// federationExecutionController
			// .getFederateNameToFederationExecutionSubscription().put(
			// subscriptionRegistrationFederationExecutionEntry
			// .getFederateName(),
			// subscriptionRegistrationFederationExecutionEntry);
			//
			// federationExecutionController.decrementSubscriptionCountDownLatch();

		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// the semaphore will be released by messages received in the
		// FedEventConsumer.onMessage()

		// the semaphore will be released by messages received in the
		// FedEventConsumer.onMessage()

		try {
			if (sem.tryAcquire(fem.getNamesOfRequiredFederates().size(),
					subscriptionRequestProcessor.getTimeToLiveMSecs(),
					TimeUnit.MILLISECONDS)) {
				fail("Should have timed out before acquiring semaphore");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

		// simply check that there is indeed only 1 permit available
		assertEquals(sem.availablePermits(), 0);
	}

}
