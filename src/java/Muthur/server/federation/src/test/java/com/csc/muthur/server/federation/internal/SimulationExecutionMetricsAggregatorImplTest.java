/**
 * 
 */
package com.csc.muthur.server.federation.internal;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.UUID;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.federation.SimulationExecutionMetricsAggregator;
import com.csc.muthur.server.federation.internal.execution.SimulationExecutionMetricsAggregatorImpl;
import com.csc.muthur.server.model.DataAction;
import com.csc.muthur.server.model.FederationExecutionModel;
import com.csc.muthur.server.model.event.DataPublicationEvent;
import com.csc.muthur.server.model.event.data.flight.Aircraft;
import com.csc.muthur.server.model.event.data.flight.FlightPosition;

/**
 * 
 * @author <a href=mailto:support@atcloud.com>support</a>
 * @version $Revision: $
 */
public class SimulationExecutionMetricsAggregatorImplTest {

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
	 * Test method for
	 * {@link com.csc.muthur.server.federation.internal.execution.SimulationExecutionMetricsAggregatorImpl#start()}
	 * .
	 */
	@Test
	public void testStart() {

		FederationExecutionModel fem = null;
		try {
			fem = new FederationExecutionModel("Federation Exec Controller Test");
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		fem.addRequiredFededrate("First Fed");
		fem.addRequiredFededrate("Second Fed");
		fem.setDefaultDurationWithinStartupProtocolMSecs(30000);
		fem.setDurationFederationExecutionMSecs(10000);

		SimulationExecutionMetricsAggregator sema = null;
		try {
			sema = new SimulationExecutionMetricsAggregatorImpl(fem);
		} catch (MuthurException e1) {
			fail(e1.getLocalizedMessage());
		}

		sema.start();

		Aircraft ac = new Aircraft();
		ac.setCallSign("DAL777");
		ac.setDataObjectUUID(UUID.randomUUID().toString());
		ac.setTailNumber("ABC123");

		DataPublicationEvent dpe = new DataPublicationEvent();
		dpe.setCreateTimeMilliSecs(System.currentTimeMillis());
		dpe.setDataAction(DataAction.Add);
		dpe.setDataObject(ac);

		sema.addDataEvent(dpe);

		FlightPosition fp = new FlightPosition();
		fp.setCallSign("DAL777");
		fp.setTailNumber(UUID.randomUUID().toString());

		dpe = new DataPublicationEvent();
		dpe.setCreateTimeMilliSecs(System.currentTimeMillis());
		dpe.setDataAction(DataAction.Add);
		dpe.setDataObject(fp);

		sema.addDataEvent(dpe);

		sema.addRoutedDataEvent(dpe);

		fp.setLatitudeDegrees(77.0876);
		fp.setLongitudeDegrees(-9.12345);
		fp.setAltitudeFt(35123);

		fp = new FlightPosition();
		fp.setCallSign("DAL777");
		fp.setTailNumber(UUID.randomUUID().toString());

		dpe = new DataPublicationEvent();
		dpe.setCreateTimeMilliSecs(System.currentTimeMillis());
		dpe.setDataAction(DataAction.Update);
		dpe.setDataObject(fp);

		sema.addDataEvent(dpe);

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}

		assertTrue(sema.getSimulationMetrics().getCntAircraft() == 1);
		assertTrue(sema.getSimulationMetrics().getCntFlightPosition() == 1);
		assertTrue(sema.getSimulationMetrics().getCntFlightPositionUpdates() == 1);
		assertTrue(sema.getSimulationMetrics().getTotalObjectsCreated() == 2);

		sema.stop();

		sema.addDataEvent(dpe);

		assertTrue(sema.getSimulationMetrics().getCntAircraft() == 1);
		assertTrue(sema.getSimulationMetrics().getCntFlightPosition() == 1);
		assertTrue(sema.getSimulationMetrics().getCntFlightPositionUpdates() == 1);
		assertTrue(sema.getSimulationMetrics().getTotalObjectsCreated() == 2);

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link com.csc.muthur.server.federation.internal.execution.SimulationExecutionMetricsAggregatorImpl#stop()}
	 * .
	 */
	@Test
	public void testStop() {

	}

	/**
	 * Test method for
	 * {@link com.csc.muthur.server.federation.internal.execution.SimulationExecutionMetricsAggregatorImpl#addRoutedDataEvent(com.csc.muthur.server.model.event.DataPublicationEvent)}
	 * .
	 */
	@Test
	public void testAddRoutedDataEvent() {

	}

}
