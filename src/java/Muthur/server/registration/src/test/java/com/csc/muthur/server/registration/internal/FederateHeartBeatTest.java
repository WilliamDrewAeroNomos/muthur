/**
 * 
 */
package com.csc.muthur.server.registration.internal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.csc.muthur.server.commons.CommonsService;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.commons.internal.CommonsServiceImpl;
import com.csc.muthur.server.configuration.ConfigurationService;
import com.csc.muthur.server.mocks.MockActiveMQBroker;
import com.csc.muthur.server.mocks.MockConfigurationServerImpl;
import com.csc.muthur.server.model.event.request.FederateRegistrationRequest;
import com.csc.muthur.server.registration.RegistrationService;
import com.csc.muthur.server.router.RouterService;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>NexSim</a>
 * @version $Revision$
 */
public class FederateHeartBeatTest implements ExceptionListener {

	private static MockActiveMQBroker broker = null;
	private static ActiveMQConnectionFactory connectionFactory;
	private static Connection connection;
	private static ConfigurationService configurationService;
	private static RegistrationService rs;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		CommonsService cs = new CommonsServiceImpl();

		configurationService = new MockConfigurationServerImpl();

		int availablePort = cs.findAvailablePort(6000, 7000);

		assert (availablePort != -1);

		configurationService.setMessagingPort(availablePort);

		broker = new MockActiveMQBroker();
		broker.setConfigurationService(configurationService);
		broker.start();

		// connect to JMS server

		connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD,
				configurationService.getMessagingConnectionUrl());

		connection = connectionFactory.createConnection();

		connection.start();

		rs = new RegistrationServiceImpl();
		rs.setRouterService(EasyMock.createMock(RouterService.class));
		rs.setConfigurationService(configurationService);

	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		broker.stop();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		try {
			connection.setExceptionListener(this);
		} catch (JMSException e2) {
			fail(e2.getLocalizedMessage());
		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link com.csc.muthur.server.registration.internal.FederateHeartBeat#FederateHeartBeat(com.csc.muthur.model.event.request.FederateRegistrationRequest, javax.jms.Session)}
	 * .
	 */
	@Test
	public void testFederateHeartBeat() {

		Session session = null;

		try {
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		} catch (JMSException e) {
			fail(e.getLocalizedMessage());
		}

		FederateRegistrationRequest federateRegistrationRequest = new FederateRegistrationRequest();

		federateRegistrationRequest.setSourceOfEvent("FederateTestName");
		federateRegistrationRequest.setFederateName(federateRegistrationRequest
				.getSourceOfEvent());
		federateRegistrationRequest
				.setFederateEventQueueName("federate-queue-name-01");

		final Semaphore sem = new Semaphore(0);

		try {
			Destination destination = session.createQueue("federate-queue-name-01");
			MessageConsumer consumer = session.createConsumer(destination);
			consumer.setMessageListener(new MessageListener() {
				@Override
				public void onMessage(Message message) {
					sem.release();
				}

			});
		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		}

		assertNotNull(federateRegistrationRequest);
		String xml = federateRegistrationRequest.serialize();
		assertNotNull(xml);
		assertFalse("".equals(xml));

		FederateHeartBeat fhb = null;

		try {
			fhb = new FederateHeartBeat(rs, federateRegistrationRequest, session);
			fhb.setIntervalBetweenHeartBeatsSecs(1);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		Thread t = new Thread(fhb);

		t.start();

		try {
			assertTrue(sem.tryAcquire(10, 60, TimeUnit.SECONDS));
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

		// terminate the heart beat thread

		fhb.setStillAlive(false);

		// give the thread a chance to exit

		try {
			Thread.sleep(2 * 1000);
		} catch (InterruptedException e) {
			// do nothing here
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.jms.ExceptionListener#onException(javax.jms.JMSException)
	 */
	@Override
	public void onException(JMSException arg0) {
		// eats up exceptions caused by not cleaning up the JMS resources
	}
}
