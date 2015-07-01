/**
 * 
 */
package com.csc.muthur.server.federation.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import org.junit.Test;

import com.csc.muthur.server.commons.MessageDestination;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.federation.internal.execution.FederationExecutionControllerImpl;
import com.csc.muthur.server.federation.internal.execution.JoinFederationRequestProcessor;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.JoinFederationExecutionEntry;
import com.csc.muthur.server.model.event.request.JoinFederationRequest;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public class JoinFederationRequestProcessorTest extends
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

		// set the count down latch for the join processor to 2

		JoinFederationRequestProcessor joinFederationRequestProcessor = null;
		try {
			joinFederationRequestProcessor =
					new JoinFederationRequestProcessor(federationExecutionController);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		Thread joinFederationRequestProcessorInstance =
				new Thread(joinFederationRequestProcessor);

		joinFederationRequestProcessorInstance.start();

		// create a JoinFederationRequest for each federate

		JoinFederationRequest req = new JoinFederationRequest();

		assertNotNull(req);

		req.setTimeToLiveSecs(reqDurationMSecs);
		req.setSourceOfEvent(eventSourceName);
		req.setFederateRegistrationHandle(federateRegistrationHandle);
		req.setFederationExecutionModel(fem);
		req.setFederationExecutionHandle(federationExecutionHandle);

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

			txtMessage.setStringProperty(
					MessageDestination.getFederationEventQueuePropName(),
					federationEventQueue.getQueueName());

			// create an execution entry for the NexSim federate and call
			// registerParticipant()

			JoinFederationExecutionEntry jfee =
					new JoinFederationExecutionEntry(nexSimFederateName, fem, txtMessage,
							req);

			federationExecutionController.joinFederation(jfee);

			// create an execution entry for the Frasca federate and call
			// registerParticipant()

			jfee =
					new JoinFederationExecutionEntry(frascaFederateName, fem, txtMessage,
							req);

			federationExecutionController.joinFederation(jfee);

		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// the semaphore will be released by messages received in the
		// FedEventConsumer.onMessage()

		try {
			if (!sem.tryAcquire(fem.getNamesOfRequiredFederates().size(),
					joinFederationRequestProcessor.getTimeToLiveMSecs(),
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

		// set the count down latch for the join processor to 2

		JoinFederationRequestProcessor joinFederationRequestProcessor = null;
		try {
			joinFederationRequestProcessor =
					new JoinFederationRequestProcessor(federationExecutionController);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		Thread joinFederationRequestProcessorInstance =
				new Thread(joinFederationRequestProcessor);

		joinFederationRequestProcessorInstance.start();

		// create a JoinFederationRequest for each federate

		JoinFederationRequest req = new JoinFederationRequest();

		assertNotNull(req);

		req.setTimeToLiveSecs(reqDurationMSecs);
		req.setSourceOfEvent(eventSourceName);
		req.setFederateRegistrationHandle(federateRegistrationHandle);
		req.setFederationExecutionModel(fem);
		req.setFederationExecutionHandle(federationExecutionHandle);

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

			txtMessage.setStringProperty(
					MessageDestination.getFederationEventQueuePropName(),
					federationEventQueue.getQueueName());

			// create an execution entry for the NexSim federate and call
			// registerParticipant()

			JoinFederationExecutionEntry jfee =
					new JoinFederationExecutionEntry(nexSimFederateName, fem, txtMessage,
							req);

			federationExecutionController.joinFederation(jfee);

		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// the semaphore will be released by messages received in the
		// FedEventConsumer.onMessage()

		try {
			if (!sem.tryAcquire(fem.getNamesOfRequiredFederates().size(),
					joinFederationRequestProcessor.getTimeToLiveMSecs(),
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

		// set the count down latch for the join processor to 2

		JoinFederationRequestProcessor joinFederationRequestProcessor = null;
		try {
			joinFederationRequestProcessor =
					new JoinFederationRequestProcessor(federationExecutionController);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		Thread joinFederationRequestProcessorInstance =
				new Thread(joinFederationRequestProcessor);

		joinFederationRequestProcessorInstance.start();

		// create a JoinFederationRequest for each federate

		JoinFederationRequest req = new JoinFederationRequest();

		assertNotNull(req);

		req.setTimeToLiveSecs(reqDurationMSecs);
		req.setSourceOfEvent(eventSourceName);
		req.setFederateRegistrationHandle(federateRegistrationHandle);
		req.setFederationExecutionModel(fem);
		req.setFederationExecutionHandle(federationExecutionHandle);

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

			txtMessage.setStringProperty(
					MessageDestination.getFederationEventQueuePropName(),
					federationEventQueue.getQueueName());

			// create an execution entry for the NexSim federate and call
			// registerParticipant()

			JoinFederationExecutionEntry jfee =
					new JoinFederationExecutionEntry(nexSimFederateName, fem, txtMessage,
							req);

			federationExecutionController.joinFederation(jfee);

			// DO NOT execute the following:
			//
			// create an execution entry for the Frasca federate and call
			// registerParticipant()

			// jfee = new JoinFederationExecutionEntry(frascaFederateName, fem,
			// txtMessage, req);
			//
			// federationExecutionController.registerParticipant(jfee);

		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// the semaphore will NOT be released by messages received in the
		// FedEventConsumer.onMessage() since only one of the 2 required federates
		// registered

		try {
			if (sem.tryAcquire(fem.getNamesOfRequiredFederates().size(),
					(joinFederationRequestProcessor.getTimeToLiveMSecs() * 2),
					TimeUnit.MILLISECONDS)) {
				// should NOT get here
				fail("Should have timed out before acquiring semaphore since only 1 of 2 federates are registered.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

		// simply check that there is indeed only 1 permit available
		assertEquals(sem.availablePermits(), 1);
	}

	/**
	 * Tests to ensure that a federate can not join a federation if it's already
	 * joined
	 */
	@Test
	public void testFederationRequestProcessorDuplicateJoin() {

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

		// set the count down latch for the join processor to 2

		JoinFederationRequestProcessor joinFederationRequestProcessor = null;
		try {
			joinFederationRequestProcessor =
					new JoinFederationRequestProcessor(federationExecutionController);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		Thread joinFederationRequestProcessorInstance =
				new Thread(joinFederationRequestProcessor);

		joinFederationRequestProcessorInstance.start();

		// create a JoinFederationRequest for each federate

		JoinFederationRequest req = new JoinFederationRequest();

		assertNotNull(req);

		req.setTimeToLiveSecs(reqDurationMSecs);
		req.setSourceOfEvent(eventSourceName);
		req.setFederateRegistrationHandle(federateRegistrationHandle);
		req.setFederationExecutionModel(fem);
		req.setFederationExecutionHandle(federationExecutionHandle);

		// establish the semaphore BEFORE you call registerParticipant() for each
		// federate

		sem = new Semaphore(0);

		TextMessage txtMessage = null;

		JoinFederationExecutionEntry jfee = null;

		try {

			// create the TextMessage for each...

			txtMessage = session.createTextMessage();

			// ...set the join request as the serialized event

			txtMessage.setText(req.serialize());

			// Set the reply to field to the queue established in setUpBeforeClass()

			txtMessage.setJMSReplyTo(replyToQueue);

			txtMessage.setStringProperty(
					MessageDestination.getFederationEventQueuePropName(),
					federationEventQueue.getQueueName());

			// create an execution entry for the NexSim federate and call
			// registerParticipant()

			jfee =
					new JoinFederationExecutionEntry(nexSimFederateName, fem, txtMessage,
							req);

			federationExecutionController.joinFederation(jfee);

			// create an execution entry for the Frasca federate and call
			// registerParticipant()

			jfee =
					new JoinFederationExecutionEntry(frascaFederateName, fem, txtMessage,
							req);

			federationExecutionController.joinFederation(jfee);

		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// the semaphore will be released by messages received in the
		// FedEventConsumer.onMessage()

		try {
			if (!sem.tryAcquire(fem.getNamesOfRequiredFederates().size(),
					joinFederationRequestProcessor.getTimeToLiveMSecs(),
					TimeUnit.MILLISECONDS)) {
				fail("Timed out before acquiring semaphore");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

		try {
			federationExecutionController.joinFederation(jfee);
			fail("Should have thrown exception for duplicate join");
		} catch (MuthurException e) {

		}

	}
}
