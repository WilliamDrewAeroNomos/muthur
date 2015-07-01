/**
 * 
 */
package com.csc.muthur.server.registration.internal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.List;
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
import javax.naming.InitialContext;
import javax.naming.spi.InitialContextFactoryBuilder;
import javax.naming.spi.NamingManager;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.atcloud.commons.CommonsService;
import com.atcloud.commons.exception.ATCloudException;
import com.atcloud.commons.internal.CommonsServiceImpl;
import com.atcloud.dcache.DistributedCacheService;
import com.atcloud.dcache.internal.DistributedCacheServiceImpl;
import com.atcloud.license.internal.LicenseServiceImpl;
import com.atcloud.model.ATCloudDataModelException;
import com.atcloud.model.License;
import com.atcloud.model.ModelService;
import com.atcloud.model.Organization;
import com.atcloud.model.SchemaFactorySourceLocator;
import com.atcloud.model.internal.ModelServiceImpl;
import com.atcloud.persistence.PersistenceService;
import com.atcloud.persistence.internal.PersistenceServiceImpl;
import com.atcloud.user.UserService;
import com.atcloud.user.internal.UserServiceImpl;
import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.configuration.ConfigurationService;
import com.csc.muthur.server.mocks.MockActiveMQBroker;
import com.csc.muthur.server.mocks.MockConfigurationServerImpl;
import com.csc.muthur.server.model.event.request.FederateRegistrationRequest;
import com.csc.muthur.server.registration.RegistrationService;
import com.csc.muthur.server.router.RouterService;
import com.csc.muthur.server.router.internal.RouterServiceImpl;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class RegistrationServiceImplTest implements ExceptionListener {

	private String testFederateName1 = "TestFederateName1";
	private String testFederateName2 = "TestFederateName2";
	private String testFederateName3 = "TestFederateName3";

	private static RegistrationService registrationService;

	private static ConfigurationService configurationService;

	private static MockActiveMQBroker broker = null;
	private static ActiveMQConnectionFactory connectionFactory;
	private static Connection connection;
	private static Session session;

	private static EntityManager em;
	private static UserService userService;

	private static ModelService modelService;
	private static PersistenceService persistenceService;
	private static License licenseOne;
	private static LicenseServiceImpl licenseService;
	private static License licenseTwo;

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

		connectionFactory =
				new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
						ActiveMQConnection.DEFAULT_PASSWORD,
						configurationService.getMessagingConnectionUrl());

		connection = connectionFactory.createConnection();

		connection.start();

		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		/*
		 * Router service
		 */
		RouterService routerService = new RouterServiceImpl();

		routerService.setConfigurationServer(configurationService);

		routerService.start();

		/*
		 * Registration service
		 */
		registrationService = new RegistrationServiceImpl();

		registrationService.setConfigurationService(configurationService);

		registrationService.setRouterService(routerService);

		registrationService.start();

		/*
		 * License service
		 */

		userService = new UserServiceImpl();

		CommonsService commonsService = new CommonsServiceImpl();

		commonsService.start();

		userService.setCommonsService(commonsService);

		persistenceService = new PersistenceServiceImpl();

		/*
		 * Initialize the embedded DB
		 */

		try {

			InitialContextFactoryBuilder icfb =
					commonsService.getInitialContextFactoryBuilder();

			assertNotNull(icfb);

			NamingManager.setInitialContextFactoryBuilder(icfb);

			InitialContext ic = new InitialContext();
			EmbeddedDataSource ds = new EmbeddedDataSource();
			ds.setDatabaseName("target/test");
			ds.setCreateDatabase("create");
			ic.bind(
					"osgi:service/javax.sql.DataSource/(osgi.jndi.service.name=jdbc/derbyds)",
					ds);

			EntityManagerFactory emf =
					Persistence.createEntityManagerFactory("userTest",
							System.getProperties());

			em = emf.createEntityManager();
			persistenceService.setEntityManager(em);

		} catch (Throwable t) {
			fail(t.getLocalizedMessage());
		}

		persistenceService.start();

		userService.setPersistenceService(persistenceService);

		DistributedCacheService distributeCacheService =
				new DistributedCacheServiceImpl();

		distributeCacheService.start();

		userService.setDcacheService(distributeCacheService);

		em.getTransaction().begin();

		userService.start();

		em.getTransaction().commit();

		modelService = new ModelServiceImpl();

		persistenceService.setModelService(modelService);

		modelService
				.setSchemaFactorySourceLocator(new SchemaFactorySourceLocator() {

					@Override
					public String getURL(String sourceName)
							throws ATCloudDataModelException {
						return System.getenv("ATCLOUD_SCHEMA_DIR") + "/" + sourceName;

					}

				});

		modelService.start();

		licenseService = new LicenseServiceImpl();

		licenseService.setModelService(modelService);

		licenseService.setPersistenceService(persistenceService);

		licenseService.start();

		registrationService.setLicenseService(licenseService);

		addLicenses();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		broker.stop();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void runAllTests() {
		testRegisterFederate();
		// testRemoveFederate();
		// testGetFederateRegistrationHandle();
		testDuplicateLicenseUsedOnRegistration();
	}

	/**
	 * Test method for
	 * {@link com.csc.muthur.server.registration.internal.RegistrationServiceImpl#registerFederate(java.lang.String)}
	 * .
	 */
	// @Test
	public void testRegisterFederate() {

		final Semaphore sem = new Semaphore(0);
		MessageConsumer consumer = null;
		Destination destination = null;

		try {
			destination = session.createQueue("test-federate-queue-name-01");
			consumer = session.createConsumer(destination);
			consumer.setMessageListener(new MessageListener() {

				@Override
				public void onMessage(Message message) {
					sem.release();
				}

			});
		} catch (JMSException e1) {
			fail(e1.getLocalizedMessage());
		}

		FederateRegistrationRequest fedReg1 = null;

		try {
			fedReg1 = new FederateRegistrationRequest();
			assertNotNull(fedReg1);
			fedReg1.setFederateName(testFederateName1);
			fedReg1.setFederateEventQueueName("test-federate-queue-name-01");
			fedReg1.setFederateHeartBeatIntervalSecs(1); // good enough
			fedReg1.setLicenseKey(licenseOne.getLicenseID());

		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}

		try {
			em.getTransaction().begin();
			registrationService.registerFederate(fedReg1);
			em.getTransaction().commit();
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// semaphore should be released by onMessage() calls in the message listener
		// above

		try {
			assertTrue(sem.tryAcquire(1, 10, TimeUnit.SECONDS));
		} catch (InterruptedException e) {
			fail(e.getLocalizedMessage());
		}

		FederateRegistrationRequest fedReg2 =
				registrationService.getFederateRegistrationRequest(testFederateName1);

		assertNotNull(fedReg2);

		try {
			em.getTransaction().begin();
			registrationService.deregisterFederate(testFederateName1);
			em.getTransaction().commit();
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// remove one that's already been removed
		try {
			em.getTransaction().begin();
			registrationService.deregisterFederate(testFederateName1);
			em.getTransaction().commit();
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// use null as parameter
		try {
			em.getTransaction().begin();
			registrationService.deregisterFederate(null);
			em.getTransaction().commit();
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}
		// exceptions

		try {
			em.getTransaction().begin();
			registrationService.registerFederate(null);
			em.getTransaction().commit();
			fail("Should have thrown exception");
		} catch (MuthurException e) {
			em.getTransaction().rollback();
		}

		try {
			consumer.close();
		} catch (JMSException e) {
			// do nothing here
		}

	}

	/**
	 * Test method for
	 * {@link com.csc.muthur.server.registration.internal.RegistrationServiceImpl#deregisterFederate(java.lang.String)}
	 * .
	 */
	// @Test
	public void testRemoveFederate() {

		FederateRegistrationRequest fedReg1 = null;
		try {
			fedReg1 = new FederateRegistrationRequest();
			fedReg1.setFederateName(testFederateName2);
			fedReg1.setFederateEventQueueName("bogus-federate-event-queue-name");
			fedReg1.setLicenseKey(licenseOne.getLicenseID());

		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}

		try {
			em.getTransaction().begin();
			registrationService.registerFederate(fedReg1);
			em.getTransaction().commit();
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		try {
			em.getTransaction().begin();
			registrationService.deregisterFederate(testFederateName1);
			em.getTransaction().commit();
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		FederateRegistrationRequest fedReg2 =
				registrationService.getFederateRegistrationRequest(testFederateName1);

		assertNull(fedReg2);

		// remove one that's already been removed
		try {
			em.getTransaction().begin();
			registrationService.deregisterFederate(testFederateName1);
			em.getTransaction().commit();
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		// use null as parameter
		try {
			em.getTransaction().begin();
			registrationService.deregisterFederate(null);
			em.getTransaction().commit();
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * Test method for
	 * {@link com.csc.muthur.server.registration.internal.RegistrationServiceImpl#getFederateRegistrationRequest(java.lang.String)}
	 * .
	 */
	// @Test
	public void testGetFederateRegistrationHandle() {

		FederateRegistrationRequest fedReg = null;
		try {
			fedReg = new FederateRegistrationRequest();
			fedReg.setFederateName(testFederateName3);
			fedReg.setFederateEventQueueName("bogus-federate-event-queue-name1");
			fedReg.setLicenseKey(licenseOne.getLicenseID());

			em.getTransaction().begin();
			registrationService.registerFederate(fedReg);
			em.getTransaction().commit();

		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		FederateRegistrationRequest fedReg2 =
				registrationService.getFederateRegistrationRequest(fedReg
						.getFederateName());

		assertNotNull(fedReg2);

		assertTrue(fedReg2.equals(fedReg));

		// exceptions

		FederateRegistrationRequest fedReg3 =
				registrationService.getFederateRegistrationRequest("bogusNameHere");

		assertNull(fedReg3);

		fedReg3 = registrationService.getFederateRegistrationRequest(null);

		assertNull(fedReg3);

	}

	/*
	 * 
	 */
	// @Test
	public void testGetRegisteredNameToFederateRegistrationRequest() {

		FederateRegistrationRequest fedReg1 = null;
		try {
			fedReg1 = new FederateRegistrationRequest();
			fedReg1.setFederateName("testFederateName11");
			fedReg1.setFederateEventQueueName("bogus-federate-event-queue-name");
			fedReg1.setLicenseKey(licenseOne.getLicenseID());

		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}

		FederateRegistrationRequest fedReg2 = null;
		try {
			fedReg2 = new FederateRegistrationRequest();
			fedReg2.setFederateName("testFederateName22");
			fedReg2.setFederateEventQueueName("bogus-federate-event-queue-name2");
			fedReg2.setLicenseKey(licenseOne.getLicenseID());

		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}

		try {
			em.getTransaction().begin();
			registrationService.registerFederate(fedReg1);
			em.flush();
			em.getTransaction().commit();

		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		try {
			em.getTransaction().begin();
			registrationService.registerFederate(fedReg2);
			em.flush();
			fail("Should have failed due to license in use");
		} catch (MuthurException e) {
			em.getTransaction().rollback();
		}

		assertEquals(fedReg1,
				registrationService
						.getFederateRegistrationRequest("testFederateName11"));

	}

	/*
	 * 
	 */
	// @Test
	public void testDuplicateLicenseUsedOnRegistration() {

		FederateRegistrationRequest licensedFed = null;
		try {
			licensedFed = new FederateRegistrationRequest();
			licensedFed.setFederateName("licensedFed");
			licensedFed.setFederateEventQueueName("bogus-federate-event-queue-name");
			licensedFed.setLicenseKey(licenseTwo.getLicenseID());

		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}

		FederateRegistrationRequest duplicateLicenseFed = null;
		try {
			duplicateLicenseFed = new FederateRegistrationRequest();
			duplicateLicenseFed.setFederateName("duplicateLicenseFed");
			duplicateLicenseFed
					.setFederateEventQueueName("bogus-federate-event-queue-name2");
			duplicateLicenseFed.setLicenseKey(licenseTwo.getLicenseID());

		} catch (Exception e1) {
			fail(e1.getLocalizedMessage());
		}

		try {
			em.getTransaction().begin();
			registrationService.registerFederate(licensedFed);
			em.getTransaction().commit();
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		try {
			em.getTransaction().begin();
			registrationService.registerFederate(duplicateLicenseFed);
			em.getTransaction().commit();
			fail("Should have thrown an exception due to attempting "
					+ "to register with a license that is in use.");
		} catch (MuthurException e) {
			em.getTransaction().rollback();
		}

	}

	/**
	 * Creates and adds the licenses for the federate to use during registration
	 * 
	 */
	static private void addLicenses() {
		/*
		 * Create the Organization
		 */

		Organization org = modelService.createOrganization();

		/*
		 * Add the Organization
		 */
		em.getTransaction().begin();

		persistenceService.add(org);

		em.getTransaction().commit();

		/*
		 * Create licenseOne
		 */

		licenseOne = modelService.createLicense();

		/*
		 * Add the License
		 */
		em.getTransaction().begin();

		persistenceService.add(licenseOne);

		em.getTransaction().commit();

		/*
		 * Add the licenseOne to the organization
		 */

		em.getTransaction().begin();

		persistenceService.assignLicenseToOrganization(licenseOne.getLicenseID(),
				org.getOrgID());

		em.getTransaction().commit();

		/*
		 * Create licenseTwo
		 */

		licenseTwo = modelService.createLicense();

		/*
		 * Add the License
		 */
		em.getTransaction().begin();

		persistenceService.add(licenseTwo);

		em.getTransaction().commit();

		/*
		 * Add the licenseOne to the organization
		 */

		em.getTransaction().begin();

		persistenceService.assignLicenseToOrganization(licenseTwo.getLicenseID(),
				org.getOrgID());

		em.getTransaction().commit();

		/*
		 * Get the licenses for the organization
		 */

		List<License> licenses = persistenceService.getLicenses(org);

		assertNotNull(licenses);

		assertTrue(licenses.size() == 2);

		Iterator<License> licenseIter = licenses.iterator();

		assertNotNull(licenseIter);

		/*
		 * This should contain the licenses that we added to the organization
		 */
		while (licenseIter.hasNext()) {
			License nextLicense = licenseIter.next();
			assertNotNull(nextLicense);
			assertTrue(nextLicense.equals(licenseOne)
					|| nextLicense.equals(licenseTwo));
		}

		try {
			em.getTransaction().begin();
			licenseService.activateLicense(licenseOne.getLicenseID());
			licenseService.activateLicense(licenseTwo.getLicenseID());
			em.getTransaction().commit();
		} catch (ATCloudException e) {
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
