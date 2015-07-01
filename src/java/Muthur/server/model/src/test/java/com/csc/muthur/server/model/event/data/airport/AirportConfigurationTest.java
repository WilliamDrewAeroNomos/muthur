/**
 * 
 */
package com.csc.muthur.server.model.event.data.airport;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.csc.muthur.server.model.event.AbstractModelTest;
import com.csc.muthur.server.model.event.data.GeoPoint;
import com.csc.muthur.server.model.event.data.Runway;
import com.csc.muthur.server.model.event.data.RunwayFlow;

/**
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 * 
 */
public class AirportConfigurationTest extends AbstractModelTest {

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

	@Test
	public void testGenerateXml() {

		AirportConfiguration airportConfiguration = null;

		airportConfiguration = new AirportConfiguration();

		assertNotNull(airportConfiguration);

		airportConfiguration.setAirportCode("IAD");

		// runway 01L
		Runway runway = new Runway();
		runway.setName("01L");
		runway.setRunwayFlow(RunwayFlow.ARRIVAL);
		GeoPoint gp = new GeoPoint();
		gp.setLatitude(38.944966);
		gp.setLongitude(-77.474822);
		gp.setAltitude(382.0);
		runway.setUpperLeft(gp);
		gp = new GeoPoint(38.970765, -77.474483, 382.0);
		runway.setLowerRight(gp);

		airportConfiguration.addRunway(runway);

		// runway 01C
		runway = new Runway("01C");
		runway.setRunwayFlow(RunwayFlow.ARRIVAL);
		gp = new GeoPoint(38.944966, -77.474822, 382.0);
		runway.setUpperLeft(gp);
		gp = new GeoPoint(38.914966, -77.474822, 382.0);
		runway.setLowerRight(gp);

		airportConfiguration.addRunway(runway);

		// runway 01R
		runway = new Runway("01R");
		runway.setRunwayFlow(RunwayFlow.DEPARTURE);
		gp = new GeoPoint(38.939058, -77.459801, 382.0);
		runway.setUpperLeft(gp);
		gp = new GeoPoint(38.970628, -77.459354, 382.0);
		runway.setLowerRight(gp);

		airportConfiguration.addRunway(runway);

		// runway 30
		runway = new Runway("30");
		runway.setRunwayFlow(RunwayFlow.DEPARTURE);
		gp = new GeoPoint(38.933622, -77.455911, 382.0);
		runway.setUpperLeft(gp);
		gp = new GeoPoint(38.943778, -77.490448, 382.0);
		runway.setLowerRight(gp);

		airportConfiguration.addRunway(runway);

		String dataAsXml = airportConfiguration.serialize();
		assertNotNull(dataAsXml);
		assertFalse("".equalsIgnoreCase(dataAsXml));

		writeToFile(airportConfiguration, false);

		AirportConfiguration acddFromXML = new AirportConfiguration();

		try {
			acddFromXML.initialization(dataAsXml);
		} catch (Exception e) {
			fail(e.getLocalizedMessage());
		}

		acddFromXML.serialize();

		assertNotNull(acddFromXML);

		assertTrue(acddFromXML.equals(airportConfiguration));

	}

}
