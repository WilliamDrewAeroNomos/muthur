/**
 * 
 */
package com.csc.muthur.server.federation.internal;

import static org.junit.Assert.fail;

import javax.jms.Connection;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.csc.muthur.server.commons.CommonsService;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.commons.internal.CommonsServiceImpl;
import com.csc.muthur.server.configuration.ConfigurationService;
import com.csc.muthur.server.mocks.MockConfigurationServerImpl;
import com.csc.muthur.server.model.FederationExecutionModel;

/**
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class FederationExecutionControllerImplProtocolTimingTest implements
		ExceptionListener {

	private final static String nexSimFederateName = "NexSim";
	private final static String frascaFederateName = "Frasca";

	private static BrokerService broker = null;
	private static Connection connection;
	private static ActiveMQConnectionFactory connectionFactory;
	private static ConfigurationService configurationService;

	/**
	 * 
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		CommonsService cs = new CommonsServiceImpl();

		configurationService = new MockConfigurationServerImpl();

		int availablePort = cs.findAvailablePort(6000, 7000);

		assert (availablePort != -1);

		configurationService.setMessagingPort(availablePort);

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

	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {

		if (connection != null) {
			connection.close();
		}
		if (broker != null) {
			broker.stop();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Before
	public void setUp() throws Exception {

		try {
			connection.setExceptionListener(this);
		} catch (JMSException e2) {
			// nothing to do here
		}
	}

	/**
	 * 
	 */
	@Test
	public void testFederationTimingsDefault() {

		// default durations with 10 secs to wait for termination

		FederationExecutionModel fem = null;
		try {
			fem = new FederationExecutionModel("Federation Exec Controller Test");
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}
		fem.addRequiredFededrate(nexSimFederateName);
		fem.addRequiredFededrate(frascaFederateName);
		fem.setDurationFederationExecutionMSecs(3600000);
		fem.setDefaultDurationWithinStartupProtocolMSecs(30000);
		fem.setDurationTimeToWaitAfterTerminationMSecs(10000);

		FederationProtocolRun fpr =
				new FederationProtocolRun(configurationService, connection, fem);

		try {
			fpr.start();
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		} catch (JMSException e) {
			fail(e.getLocalizedMessage());
		}

	}

	/**
	 * 
	 */
	@Test
	public void testFederationTimingsMinimualTimeToWaitAfterTermination() {

		// default durations with 1 msec to wait for termination

		FederationExecutionModel fem = null;
		try {
			fem = new FederationExecutionModel("Federation Exec Controller Test");
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}
		fem.addRequiredFededrate(nexSimFederateName);
		fem.addRequiredFededrate(frascaFederateName);
		fem.setDurationFederationExecutionMSecs(3600000);
		fem.setDefaultDurationWithinStartupProtocolMSecs(30000);
		fem.setDurationTimeToWaitAfterTerminationMSecs(1);

		FederationProtocolRun fpr =
				new FederationProtocolRun(configurationService, connection, fem);

		try {
			fpr.start();
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		} catch (JMSException e) {
			fail(e.getLocalizedMessage());
		}

	}

	/**
	 * 
	 */
	@Test
	public void testFederationTimingsShortWaitTimesBetweenPhases() {

		// default durations with 5 sec wait time between phases

		FederationExecutionModel fem = null;
		try {
			fem = new FederationExecutionModel("Federation Exec Controller Test");
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}
		fem.addRequiredFededrate(nexSimFederateName);
		fem.addRequiredFededrate(frascaFederateName);
		fem.setDurationFederationExecutionMSecs(3600000);
		fem.setDefaultDurationWithinStartupProtocolMSecs(10000);

		FederationProtocolRun fpr =
				new FederationProtocolRun(configurationService, connection, fem);

		try {
			fpr.start();
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		} catch (JMSException e) {
			fail(e.getLocalizedMessage());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.jms.ExceptionListener#onException(javax.jms.JMSException)
	 */
	@Override
	public void onException(JMSException arg0) {
		// just eat the exception which is caused by not cleaning up JMS resources
	}

}
