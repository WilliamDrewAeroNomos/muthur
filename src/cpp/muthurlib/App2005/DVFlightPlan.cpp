#include "stdafx.h"
#include "DVFlightPlan.h"


#ifdef _DEBUG
#define new DEBUG_NEW
#endif


CDVFlightPlan::CDVFlightPlan()
{
	CMLListCtrlElement* pElement = NULL;

	for(int i = 0; i <= DV_FLIGHT_PLAN_PLANNED_ARRIVAL_RWY; i++)
	{
		pElement = new CMLListCtrlElement(this);
		pElement->SetOwner(i);
		m_apElements.AddTail(pElement);
	}
}

CDVFlightPlan::~CDVFlightPlan()
{
}

CString CDVFlightPlan::GetName(int iListId)
{
	CString strName = "";

	switch(iListId)
	{
	case DV_FLIGHT_PLAN_ACID:
		strName = "AC ID";
		break;

	case DV_FLIGHT_PLAN_TAIL_NUMBER:
		strName = "Tail Number";
		break;

	case DV_FLIGHT_PLAN_UUID:
		strName = "Object UUID";
		break;

	case DV_FLIGHT_PLAN_CRUISE_SPEED_KNOTS:
		strName = "Cruise Speed";
		break;

	case DV_FLIGHT_PLAN_CRUISE_ALT_FEET:
		strName = "Cruise Altitude";
		break;

	case DV_FLIGHT_PLAN_AIRCRAFT_TYPE:
		strName = "Aircraft Type";
		break;

	case DV_FLIGHT_PLAN_PLANNED_DEPARTURE_TIME:
		strName = "Departure Time";
		break;

	case DV_FLIGHT_PLAN_PLANNED_ARRIVAL_TIME:
		strName = "Arrival Time";
		break;

	case DV_FLIGHT_PLAN_ROUTE:
		strName = "Route";
		break;

	case DV_FLIGHT_PLAN_DEPARTURE_CENTER:
		strName = "Departure Center";
		break;

	case DV_FLIGHT_PLAN_ARRIVAL_CENTER:
		strName = "Arrival Center";
		break;

	case DV_FLIGHT_PLAN_DEPARTURE_FIX:
		strName = "Departure Fix";
		break;

	case DV_FLIGHT_PLAN_ARRIVAL_FIX:
		strName = "Arrival Fix";
		break;

	case DV_FLIGHT_PLAN_PHYSICAL_CLASS:
		strName = "Physical Class";
		break;

	case DV_FLIGHT_PLAN_WEIGHT_CLASS:
		strName = "Weight Class";
		break;

	case DV_FLIGHT_PLAN_EQUIP_QUAL:
		strName = "Equipment";
		break;

	case DV_FLIGHT_PLAN_USER_CLASS:
		strName = "User Class";
		break;

	case DV_FLIGHT_PLAN_PLANNED_DEPARTURE_GATE:
		strName = "Planned Dept Gate";
		break;

	case DV_FLIGHT_PLAN_PLANNED_ARRIVAL_GATE:
		strName = "Planned Arr Gate";
		break;

	case DV_FLIGHT_PLAN_PLANNED_DEPARTURE_RWY:
		strName = "Planned Dept Runway";
		break;

	case DV_FLIGHT_PLAN_PLANNED_ARRIVAL_RWY:
		strName = "Planned Arr Runway";
		break;

	}

	return strName;
}

CString CDVFlightPlan::GetValue(int iListId)
{
	CString strValue = "";

	switch(iListId)
	{
	case DV_FLIGHT_PLAN_ACID:
		strValue = m_FlightPlan.GetACId();
		break;

	case DV_FLIGHT_PLAN_TAIL_NUMBER:
		strValue = m_FlightPlan.GetACTailNumber();
		break;

	case DV_FLIGHT_PLAN_UUID:
		strValue = m_FlightPlan.GetDataObjectUUID();
		break;

	case DV_FLIGHT_PLAN_CRUISE_SPEED_KNOTS:
		strValue.Format("%.4f", m_FlightPlan.GetCruiseSpeedKts());
		break;

	case DV_FLIGHT_PLAN_CRUISE_ALT_FEET:
		strValue.Format("%.4f", m_FlightPlan.GetCruiseAltitudeFeet());
		break;

	case DV_FLIGHT_PLAN_AIRCRAFT_TYPE:
		strValue = m_FlightPlan.GetAircraftType();
		break;

	case DV_FLIGHT_PLAN_PLANNED_DEPARTURE_TIME:
		strValue.Format("%14.0f", m_FlightPlan.GetPlannedDepartureTimeMSecs());
		break;

	case DV_FLIGHT_PLAN_PLANNED_ARRIVAL_TIME:
		strValue.Format("%14.0f", m_FlightPlan.GetPlannedArrivalTimeMSecs());
		break;

	case DV_FLIGHT_PLAN_ROUTE:
		strValue = m_FlightPlan.GetRoute();
		break;

	case DV_FLIGHT_PLAN_DEPARTURE_CENTER:
		strValue = m_FlightPlan.GetDepartureCenter();
		break;

	case DV_FLIGHT_PLAN_ARRIVAL_CENTER:
		strValue = m_FlightPlan.GetArrivalCenter();
		break;

	case DV_FLIGHT_PLAN_DEPARTURE_FIX:
		strValue = m_FlightPlan.GetDepartureFix();
		break;

	case DV_FLIGHT_PLAN_ARRIVAL_FIX:
		strValue = m_FlightPlan.GetArrivalFix();
		break;

	case DV_FLIGHT_PLAN_PHYSICAL_CLASS:
		strValue = m_FlightPlan.GetPhysicalClass();
		break;

	case DV_FLIGHT_PLAN_WEIGHT_CLASS:
		strValue = m_FlightPlan.GetWeightClass();
		break;

	case DV_FLIGHT_PLAN_EQUIP_QUAL:
		strValue = m_FlightPlan.GetEquipmentQualifier();
		break;

	case DV_FLIGHT_PLAN_USER_CLASS:
		strValue = m_FlightPlan.GetUserClass();
		break;

	case DV_FLIGHT_PLAN_PLANNED_DEPARTURE_GATE:
		strValue = m_FlightPlan.GetPlannedDeptGate();
		break;

	case DV_FLIGHT_PLAN_PLANNED_ARRIVAL_GATE:
		strValue = m_FlightPlan.GetPlannedArrGate();
		break;

	case DV_FLIGHT_PLAN_PLANNED_DEPARTURE_RWY:
		strValue = m_FlightPlan.GetPlannedDeptRunway();
		break;

	case DV_FLIGHT_PLAN_PLANNED_ARRIVAL_RWY:
		strValue = m_FlightPlan.GetPlannedArrRunway();
		break;
	}

	return strValue;
}

void CDVFlightPlan::Randomize()
{
	CString strRandom = "";

	m_FlightPlan.SetACTailNumber("7253");		

	//m_Aircraft.SetDeptAirportCode("KBWI");

	//m_Aircraft.SetLatitudeDegrees(38.939059);
	//m_Aircraft.SetLongitudeDegrees(-77.45984);
	//m_Aircraft.SetAltitudeFt(1050);

	m_FlightPlan.SetCruiseSpeedKts(346);
	m_FlightPlan.SetCruiseAltitudeFeet(250);
	//m_Aircraft.SetAirspeedKts(rand() % 400);

	//m_Aircraft.SetHeadingDegrees((rand() % 20) - 10);
	//m_Aircraft.SetPitchDegrees((rand() % 20) - 10);
	//m_Aircraft.SetRollDegrees((rand() % 20) - 10);

	/*switch(rand() % 4)
	{
	case 0: m_Aircraft.SetACId("Cleveland");
	break;
	case 1: m_Aircraft.SetCenter("Dallas");
	break;
	case 2: m_Aircraft.SetCenter("LosAngeles");
	break;
	case 3: m_Aircraft.SetCenter("Memphis");
	break;
	default: m_Aircraft.SetCenter("Potomac");
	break;
	}*/

	//strRandom.Format("%d", (rand() % 4) + 66);
	//m_Aircraft.SetSector((char*)((LPCSTR)strRandom));	

	//m_Aircraft.SetSource(rand() % 1 ? "TestApp" : "NexSim");	

	/*	switch(rand() % 4)
	{
	case 0: */m_FlightPlan.SetAircraftType("CRJ7");
	//			break;
	//	case 1: m_FlightPlan.SetAircraftType("B768");
	//			break;
	//	case 2: m_FlightPlan.SetAircraftType("C172");
	//			break;
	//	case 3: m_FlightPlan.SetAircraftType("B737");
	//			break;
	//	default: m_FlightPlan.SetAircraftType("CRJ7");
	//			break;
	//}

	//m_FlightPlan.SetAircraftType("CRJ7");
	// Get UTC time
	struct tm t1, t2;
	time_t now, mytime, gmtime;
	char buff[256];
	char buff1[256];

	time( & now );

	_gmtime64_s( &t1, &now );
	_gmtime64_s( &t2, &now );

	t1.tm_hour = t1.tm_hour + 2;
	t2.tm_hour = t2.tm_hour + 4;

	mytime = _mkgmtime(&t1);
	gmtime = _mkgmtime(&t2);

	sprintf_s(buff, sizeof(buff), "%I64d", gmtime);
	sprintf_s(buff, sizeof(buff1), "%I64d", mytime);



	m_FlightPlan.SetPlannedDeptGate("A4");
	m_FlightPlan.SetPlannedDeptRunway("TBD");

	if(buff) m_FlightPlan.SetPlannedArrivalTimeMSecs(atol(buff));
	else m_FlightPlan.SetPlannedArrivalTimeMSecs(atol(buff));

	m_FlightPlan.SetPlannedArrGate("B6");
	m_FlightPlan.SetPlannedArrRunway("TBD");

	//m_FlightPlan.SetPlannedDepartureTimeMSecs(rand() % 500000);	
	//m_FlightPlan.SetPlannedArrivalTimeMSecs(rand() % 500000);	

/*	switch(rand() % 4)
	{
	case 0: */m_FlightPlan.SetRoute("IAD..SWANN.V268.BROSS.J42.RBV.J222.JFK..DPK.DPK2.KBDL");
	//	break;
	//case 1: m_FlightPlan.SetRoute("IAD..KMIA");
	//	break;
	//case 2: m_FlightPlan.SetRoute("IAD..KATL");
	//	break;
	//case 3: m_FlightPlan.SetRoute("IAD..KMCO");
	//	break;
	//default: m_FlightPlan.SetRoute("IAD..KBWI");
	//	break;
	//}

//	m_FlightPlan.SetRoute("IAD..MRB..IRL..KPIT");
	//m_FlightPlan.SetRoute("BWI..LUSIE..MOSBY..KIAD");

	m_FlightPlan.SetDepartureCenter("ZDC");
	m_FlightPlan.SetArrivalCenter("ZBW");

	m_FlightPlan.SetDepartureFix("IAD");
	m_FlightPlan.SetArrivalFix("BDL");


	m_FlightPlan.SetPhysicalClass("J");	
	m_FlightPlan.SetWeightClass("H");	
	m_FlightPlan.SetEquipmentQualifier("Q");	
	m_FlightPlan.SetUserClass(rand() % 1 ? "U1" : "U2");	

	//m_Aircraft.SetTaxiOutTimeMSecs(rand() % 500000);			
	//m_Aircraft.SetActualDepartureTimeMSecs(rand() % 500000);

	//m_Aircraft.SetVerticalSpeed(rand() % 400);	
	//m_Aircraft.SetIsAcOnGround(rand() % 1 ? true : false);
	//m_Aircraft.SetFrequency(1250);

	//switch(rand() % 2)
	//{
	//case 0: m_Aircraft.SetSquawkCode("2467");
	//	break;
	//case 1: m_Aircraft.SetSquawkCode("4321");
	//	break;
	//default: m_Aircraft.SetSquawkCode("1234");
	//	break;
	//}

	//m_Aircraft.SetIdent(rand() % 1 ? true : false);
}

void CDVFlightPlan::Update(CMLDataObject* pData)
{ 
	if(pData) 
		m_FlightPlan.Copy(*((CMLFlightPlan*)pData));
	else
		m_FlightPlan.Reset();
}
