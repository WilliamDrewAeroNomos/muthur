/**
 * 
 */
package com.csc.muthur.server.ownership.internal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.csc.muthur.server.commons.exception.MuthurException;
import com.csc.muthur.server.model.AccessControl;
import com.csc.muthur.server.model.event.EventAttributeName;
import com.csc.muthur.server.model.event.data.flight.FlightPlan;

/**
 * 
 * @author <a href=mailto:Nexsim-support@csc.com>Nexsim</a>
 * @version $Revision$
 */
public class OwnedObjectListTest {

	private static FlightPlan flightPlan = null;

	private String tailNumber = "N481UA";
	private String callSign = "DAL333";

	// attributes that will be set
	//
	private String aircraftType = "B774";
	private String source = "Flight";
	private double cruiseSpeedKts = 330;
	private double cruiseAltitudeFeet = 33000;
	private String route = "IND..ROD.J29.PLB.J595.BGR..BGR";
	private String departureCenter = "ZID";
	private String arrivalCenter = "ZBW";
	private String departureFix = "DEFIX";
	private String arrivalFix = "ARFIX";
	private String physicalClass = "J";
	private String weightClass = "H";
	private String userClass = "C";
	private String airborneEquipmentQualifier = "R";

	private static OwnedObjectList ool = null;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		ool = new OwnedObjectList();
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
	public void runAllTests() {
		testAddObject();
		testGetObjectAttributeAccessControl();
		testUpdateObjectAttributeAccessControl();
		testGetAttributeAccessControl();
	}

	/**
	 * 
	 */
	public void testAddObject() {

		// create a flight plan

		flightPlan = null;

		try {
			flightPlan = new FlightPlan(tailNumber, callSign);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}

		flightPlan.setSource(source);
		flightPlan.setAircraftType(aircraftType);
		flightPlan.setPlannedDepartureTimeMSecs(System.currentTimeMillis() + 10);
		flightPlan.setPlannedArrivalTimeMSecs(System.currentTimeMillis() + 20);
		flightPlan.setCruiseSpeedKts(cruiseSpeedKts);
		flightPlan.setCruiseAltitudeFt(cruiseAltitudeFeet);
		flightPlan.setRoutePlan(route);
		flightPlan.setDepartureCenter(departureCenter);
		flightPlan.setArrivalCenter(arrivalCenter);
		flightPlan.setDepartureFix(departureFix);
		flightPlan.setArrivalFix(arrivalFix);
		flightPlan.setPhysicalAircraftClass(physicalClass);
		flightPlan.setWeightAircraftClass(weightClass);
		flightPlan.setUserAircraftClass(userClass);
		flightPlan.setNumOfAircraft(1);
		flightPlan.setAirborneEquipmentQualifier(airborneEquipmentQualifier);

		try {
			ool.addObject(flightPlan);
		} catch (MuthurException e) {
			fail(e.getLocalizedMessage());
		}
	}

	/**
	 * 
	 */
	public void testGetObjectAttributeAccessControl() {

		assertNotNull(flightPlan);

		ObjectAttributeAccessControl oaac = ool
				.getObjectAttributeAccessControl(flightPlan.getDataObjectUUID());

		assertNotNull(oaac);

		// get one of the attributes and test it's access control
		//
		AccessControl ac = oaac.getAttributeAccessControl(EventAttributeName.source
				.toString());

		assertNotNull(ac);

		// should be equal to READ_ONLY since it hasn't been changed from it's
		// default
		//
		assertTrue(ac.equals(AccessControl.READ_ONLY));

	}

	/**
	 * 
	 */
	public void testUpdateObjectAttributeAccessControl() {

		assertNotNull(flightPlan);

		Map<String, AccessControl> fieldNameToAccessControlMap = new ConcurrentHashMap<String, AccessControl>();

		fieldNameToAccessControlMap.put(EventAttributeName.source.toString(),
				AccessControl.READ_WRITE);
		fieldNameToAccessControlMap
				.put(EventAttributeName.departureCenter.toString(),
						AccessControl.READ_WRITE);
		// this is NOT an attribute of a flight plan
		fieldNameToAccessControlMap.put(
				EventAttributeName.arrivalRunway.toString(), AccessControl.READ_WRITE);

		assertTrue(ool.updateAttributeAccessControl(flightPlan.getDataObjectUUID(),
				fieldNameToAccessControlMap));

		AccessControl ac = ool.getAttributeAccessControl(
				flightPlan.getDataObjectUUID(), EventAttributeName.source.toString());

		assertNotNull(ac);

		assertTrue(ac.equals(AccessControl.READ_WRITE));

	}

	/**
	 * 
	 */
	public void testGetAttributeAccessControl() {

		assertNotNull(flightPlan);

		Map<String, AccessControl> fieldNameToAccessControlMap = new ConcurrentHashMap<String, AccessControl>();

		fieldNameToAccessControlMap.put(EventAttributeName.source.toString(),
				AccessControl.READ_WRITE);
		fieldNameToAccessControlMap.put(
				EventAttributeName.departureCenter.toString(), AccessControl.READ_ONLY);
		// this is NOT an attribute of a flight plan
		fieldNameToAccessControlMap.put(
				EventAttributeName.arrivalRunway.toString(), AccessControl.READ_WRITE);

		assertTrue(ool.updateAttributeAccessControl(flightPlan.getDataObjectUUID(),
				fieldNameToAccessControlMap));

		// get the AC for an attribute

		AccessControl ac = ool.getAttributeAccessControl(
				flightPlan.getDataObjectUUID(), EventAttributeName.source.toString());

		assertNotNull(ac);

		// check that the AccessControl is correct

		assertTrue(ac.equals(AccessControl.READ_WRITE));

		// get the AC for an attribute that is NOT a valid attribute for this object

		ac = ool.getAttributeAccessControl(flightPlan.getDataObjectUUID(),
				EventAttributeName.taxiInGate.toString());

		assertNull(ac);

	}

}
