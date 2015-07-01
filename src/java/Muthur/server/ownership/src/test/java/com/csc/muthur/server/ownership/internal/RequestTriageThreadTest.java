/**
 * 
 */
package com.csc.muthur.server.ownership.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.UUID;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.csc.muthur.server.commons.CommonsService;
import com.csc.muthur.server.commons.DataChannelControlBlock;
import com.csc.muthur.server.commons.IEvent;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.commons.internal.CommonsServiceImpl;
import com.csc.muthur.server.mocks.MockConfigurationServerImpl;
import com.csc.muthur.server.model.event.data.IBaseDataObject;
import com.csc.muthur.server.model.event.data.flight.Aircraft;
import com.csc.muthur.server.model.event.request.TransferObjectOwnershipRequest;
import com.csc.muthur.server.ownership.PendingFederateRequestEntry;
import com.csc.muthur.server.ownership.RequestTriageThread;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>NexSim</a>
 * @version $Revision$
 */
public class RequestTriageThreadTest {

	private static final String tailNumber = "N485UA";
	private static final String acid = "DAL333";
	private static Semaphore callbackSuccessSemaphore = new Semaphore(0);
	private static Semaphore callbackErrorSemaphore = new Semaphore(0);

	private static CommonsService commonsService;
	private static MockConfigurationServerImpl configurationService;

	private static BrokerService broker;
	private static ActiveMQConnectionFactory connectionFactory;
	private static Connection connection;
	private static Session session;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		commonsService = new CommonsServiceImpl();

		configurationService = new MockConfigurationServerImpl();

		int availablePort = commonsService.findAvailablePort(6000, 7000);

		assert (availablePort != -1);

		configurationService.setMessagingPort(availablePort);
		configurationService.setGenerateHeartBeat(false);

		// This message broker is embedded for testing purposes. It emulates
		// having the ActiveMQ bundle running in the OSGi container
		//
		broker = new BrokerService();
		broker.setPersistent(false);
		broker.setUseJmx(false);

		String connUrl = configurationService.getMessagingConnectionUrl();

		broker.addConnector(connUrl);
		broker.start();

		// connect to JMS server

		connectionFactory =
				new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
						ActiveMQConnection.DEFAULT_PASSWORD, connUrl);

		connection = connectionFactory.createConnection();

		connection.start();

		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if (connection != null) {
			connection.stop();
		}
		if (session != null) {
			session.close();
		}
		if (broker.isStarted()) {
			broker.stop();
		}
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
	 * 
	 */
	@Test
	public void runAllTests() {
		testRequestTriageThread();
		testAddingSingleRequest();
		testAddingMultipleRequests();

	}

	/**
	 * 
	 */
	public void testRequestTriageThread() {

		RequestTriageThread rtt = new RequestTriageThreadImpl();

		Thread t = new Thread(rtt);

		t.start();

		rtt.setContinueRunning(false);

	}

	/**
	 * 
	 */
	public void testAddingSingleRequest() {

		RequestTriageThread rtt = new RequestTriageThreadImpl();

		rtt.setIntervalBetweenTriages(3);

		Thread t = new Thread(rtt);

		t.start();

		/**
		 * Create the request callback object
		 */
		MyCallback cb = new MyCallback();

		/**
		 * Create the object to go with the request
		 */
		Aircraft aircraft = null;

		try {
			aircraft = new Aircraft(tailNumber, acid);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(aircraft);

		assertEquals(aircraft.getCallSign(), acid);
		assertEquals(aircraft.getTailNumber(), tailNumber);
		assertNotNull(aircraft.getDataObjectUUID());
		assertFalse(aircraft.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(aircraft.getObjectCreateTimeMSecs() != 0L);

		/**
		 * Create a TransferObjectOwnershipRequest
		 */
		TransferObjectOwnershipRequest requestObject =
				createTransferRequest(aircraft);

		DataChannelControlBlock dccBlock = new DataChannelControlBlock();
		dccBlock.setCorrelationID(requestObject.getEventUUID());
		dccBlock.setEventName(requestObject.getEventName());
		dccBlock.setReplyToQueueName("BOGUS QUEUE NAME");
		dccBlock.setPayLoadLength(requestObject.serialize().length());

		callbackErrorSemaphore = new Semaphore(0);

		try {
			rtt.addRequest(requestObject, cb, dccBlock);
		} catch (MuthurException e1) {
			fail(e1.getLocalizedMessage());
		}

		try {
			if (!callbackErrorSemaphore.tryAcquire(30, TimeUnit.SECONDS)) {
				fail("Should have triaged request.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

		rtt.setContinueRunning(false);

	}

	/**
	 * 
	 */
	public void testAddingMultipleRequests() {

		RequestTriageThread rtt = new RequestTriageThreadImpl();

		rtt.setIntervalBetweenTriages(1);

		Thread t = new Thread(rtt);

		t.start();

		/**
		 * Create the request callback object
		 */
		MyCallback cb = new MyCallback();

		/**
		 * Create the object to go with the request
		 */
		IBaseDataObject aircraft = createObject(tailNumber, acid);

		/**
		 * Create a TransferObjectOwnershipRequest
		 */
		TransferObjectOwnershipRequest requestObject =
				createTransferRequest(aircraft);
		requestObject.setTimeToLiveSecs(5);

		DataChannelControlBlock dccBlock = new DataChannelControlBlock();
		dccBlock.setCorrelationID(requestObject.getEventUUID());
		dccBlock.setEventName(requestObject.getEventName());
		dccBlock.setReplyToQueueName("BOGUS QUEUE NAME");
		dccBlock.setPayLoadLength(requestObject.serialize().length());

		callbackErrorSemaphore = new Semaphore(0);

		try {
			rtt.addRequest(requestObject, cb, dccBlock);
		} catch (MuthurException e1) {
			fail(e1.getLocalizedMessage());
		}

		/**
		 * Create the object to go with the request
		 */
		aircraft = createObject(tailNumber, acid);

		/**
		 * Create a second TransferObjectOwnershipRequest
		 */
		requestObject = createTransferRequest(aircraft);
		requestObject.setTimeToLiveSecs(15);

		dccBlock = new DataChannelControlBlock();
		dccBlock.setCorrelationID(requestObject.getEventUUID());
		dccBlock.setEventName(requestObject.getEventName());
		dccBlock.setReplyToQueueName("BOGUS QUEUE NAME");
		dccBlock.setPayLoadLength(requestObject.serialize().length());

		callbackErrorSemaphore = new Semaphore(0);

		try {
			rtt.addRequest(requestObject, cb, dccBlock);
		} catch (MuthurException e1) {
			fail(e1.getLocalizedMessage());
		}

		try {
			if (!callbackErrorSemaphore.tryAcquire(2, 60, TimeUnit.SECONDS)) {
				fail("Should have triaged request.");
			}
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

		rtt.setContinueRunning(false);

	}

	/**
	 * @param tailnumber2
	 * @param acid2
	 * @return
	 */
	private IBaseDataObject createObject(String tailnumber, String acid) {
		Aircraft aircraft = null;

		try {
			aircraft = new Aircraft(tailNumber, acid);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		assertNotNull(aircraft);

		assertEquals(aircraft.getCallSign(), acid);
		assertEquals(aircraft.getTailNumber(), tailNumber);
		assertNotNull(aircraft.getDataObjectUUID());
		assertFalse(aircraft.getDataObjectUUID().equalsIgnoreCase(""));
		assertTrue(aircraft.getObjectCreateTimeMSecs() != 0L);

		return aircraft;
	}

	/**
	 * @return
	 */
	private TransferObjectOwnershipRequest createTransferRequest(
			IBaseDataObject bdo) {

		TransferObjectOwnershipRequest requestObject =
				new TransferObjectOwnershipRequest();
		assertNotNull(requestObject);

		requestObject.setFederationExecutionHandle(UUID.randomUUID().toString());
		requestObject.setFederateRegistrationHandle(UUID.randomUUID().toString());
		requestObject.setFederationExecutionModelHandle(UUID.randomUUID()
				.toString());
		requestObject.setDataObjectUUID(bdo.getDataObjectUUID());
		requestObject.setSourceOfEvent("Testing");
		requestObject.setTimeToLiveSecs(10);
		return requestObject;
	}

	/**
	 * 
	 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
	 * @version $Revision$
	 * 
	 */
	private class MyCallback implements RequestCallback {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.csc.muthur.server.ownership.internal.RequestCallback#onError(java
		 * .lang .Exception )
		 */
		@Override
		public void onError(PendingFederateRequestEntry entry, Exception e) {
			callbackErrorSemaphore.release();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.csc.muthur.server.ownership.internal.RequestCallback#onSuccess(com
		 * .csc .muthur .model.event.IEvent)
		 */
		@Override
		public void onSuccess(IEvent response) {
			callbackSuccessSemaphore.release();
		}

	}

}
