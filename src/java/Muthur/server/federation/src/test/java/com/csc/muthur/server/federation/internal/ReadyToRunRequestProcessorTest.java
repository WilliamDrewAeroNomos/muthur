/**
 * 
 */
package com.csc.muthur.server.federation.internal;

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
import com.csc.muthur.server.federation.ReadyToRunRequestProcessor;
import com.csc.muthur.server.federation.internal.execution.FederationExecutionControllerImpl;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.ReadyToRunFederationExecutionEntry;
import com.csc.muthur.server.model.event.request.ReadyToRunRequest;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public class ReadyToRunRequestProcessorTest extends
		AbstractBaseTestRequestProcessor {

	/**
	 * Standard processing for ready to run requests using two federates and
	 * default settings in the FEM
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

			federationExecutionController = new FederationExecutionControllerImpl(
					federationService, fem);

		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		ReadyToRunRequestProcessor readyToRunRequestProcessor = null;

		try {
			readyToRunRequestProcessor = new ReadyToRunRequestProcessor(
					federationExecutionController);
		} catch (MuthurException e2) {
			fail(e2.getLocalizedMessage());
		}

		Thread readyToRunRequestProcessorInstance = new Thread(
				readyToRunRequestProcessor);

		readyToRunRequestProcessorInstance.start();

		// create a ReadyToRunRequest for each federate

		ReadyToRunRequest req = new ReadyToRunRequest();
		assertNotNull(req);

		req.setTimeToLiveSecs(10);
		req.setSourceOfEvent(eventSourceName);

		try {
			req.setFederateRequestQueueName(federateRequestEventQueue.getQueueName());
			req.setTimeManagementQueueName(timeMgmtEventQueue.getQueueName());
		} catch (JMSException e2) {
			fail(e2.getLocalizedMessage());
		}

		req.setFederateRegistrationHandle(federateRegistrationHandle);
		req.setFederationExecutionHandle(federationExecutionHandle);

		// establish the semaphore early

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

			txtMessage.setStringProperty(
					MessageDestination.getDataEventQueuePropName(),
					dataPublicationQueue.getQueueName());

			// create an execution entry for the NexSim federate and call
			// registerParticipant()

			ReadyToRunFederationExecutionEntry readyToRunFederationExecutionEntry = new ReadyToRunFederationExecutionEntry(
					fem, req, txtMessage);

			readyToRunRequestProcessor.addEntry(readyToRunFederationExecutionEntry);

			// create an execution entry for the Frasca federate

			req.setSourceOfEvent(frascaFederateName);

			readyToRunFederationExecutionEntry = new ReadyToRunFederationExecutionEntry(
					fem, req, txtMessage);

			readyToRunRequestProcessor.addEntry(readyToRunFederationExecutionEntry);

		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// the semaphore will be released by messages received in the
		// FedEventConsumer.onMessage()

		try {
			if (!sem.tryAcquire(fem.getNamesOfRequiredFederates().size(),
					readyToRunRequestProcessor.getTimeToLiveMSecs(),
					TimeUnit.MILLISECONDS)) {
				fail("Timed out before acquiring semaphore");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

	}

}
