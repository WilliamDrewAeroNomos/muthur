#include "stdafx.h"
#include "DVAircraft.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


CDVAircraft::CDVAircraft()
{
	CMLListCtrlElement* pElement = NULL;

	for(int i = 0; i <= DV_AIRCRAFT_UUID; i++)
	{
		pElement = new CMLListCtrlElement(this);
		pElement->SetOwner(i);
		m_apElements.AddTail(pElement);
	}
}

CDVAircraft::~CDVAircraft()
{
}

CString CDVAircraft::GetName(int iListId)
{
	CString strName = "";

	switch(iListId)
	{
	case DV_AIRCRAFT_ACID:
		strName = "AC ID";
		break;

	case DV_AIRCRAFT_TAIL_NUMBER:
		strName = "Tail Number";
		break;

	case DV_AIRCRAFT_UUID:
		strName = "Object UUID";
		break;

		/*case DV_SPAWN_AIRCRAFT_ARRIVAL_AIRPORT_CODE:
		strName = "Arrival Airport";
		break;

		case DV_SPAWN_AIRCRAFT_LATITUDE_DEGREES:
		strName = "Latitude";
		break;

		case DV_SPAWN_AIRCRAFT_LONGITUDE_DEGREES:
		strName = "Longitude";
		break;

		case DV_SPAWN_AIRCRAFT_GND_SPEED_KNOTS:
		strName = "Ground Speed";
		break;

		case DV_SPAWN_AIRCRAFT_ALTITUDE_FEET:
		strName = "Altitude";
		break;

		case DV_SPAWN_AIRCRAFT_CRUISE_SPEED_KNOTS:
		strName = "Cruise Speed";
		break;

		case DV_SPAWN_AIRCRAFT_CRUISE_ALT_FEET:
		strName = "Cruise Altitude";
		break;

		case DV_SPAWN_AIRCRAFT_AIR_SPEED_KNOTS:
		strName = "Air Speed";
		break;

		case DV_SPAWN_AIRCRAFT_HEADING_DEGREES:
		strName = "Heading";
		break;

		case DV_SPAWN_AIRCRAFT_PITCH_DEGREES:
		strName = "Pitch";
		break;

		case DV_SPAWN_AIRCRAFT_ROLL_DEGREES:
		strName = "Roll";
		break;

		case DV_SPAWN_AIRCRAFT_CENTER:
		strName = "Center";
		break;

		case DV_SPAWN_AIRCRAFT_SECTOR:
		strName = "Sector";
		break;

		case DV_SPAWN_AIRCRAFT_SOURCE:
		strName = "Source";
		break;

		case DV_SPAWN_AIRCRAFT_AIRCRAFT_TYPE:
		strName = "Aircraft Type";
		break;

		case DV_SPAWN_AIRCRAFT_PLANNED_DEPARTURE_TIME:
		strName = "Departure Time";
		break;

		case DV_SPAWN_AIRCRAFT_PLANNED_ARRIVAL_TIME:
		strName = "Arrival Time";
		break;

		case DV_SPAWN_AIRCRAFT_ROUTE:
		strName = "Route";
		break;

		case DV_SPAWN_AIRCRAFT_DEPARTURE_CENTER:
		strName = "Departure Center";
		break;

		case DV_SPAWN_AIRCRAFT_ARRIVAL_CENTER:
		strName = "Arrival Center";
		break;

		case DV_SPAWN_AIRCRAFT_DEPARTURE_FIX:
		strName = "Departure Fix";
		break;

		case DV_SPAWN_AIRCRAFT_ARRIVAL_FIX:
		strName = "Arrival Fix";
		break;

		case DV_SPAWN_AIRCRAFT_PHYSICAL_CLASS:
		strName = "Physical Class";
		break;

		case DV_SPAWN_AIRCRAFT_WEIGHT_CLASS:
		strName = "Weight Class";
		break;

		case DV_SPAWN_AIRCRAFT_EQUIPMENT:
		strName = "Equipment";
		break;

		case DV_SPAWN_AIRCRAFT_USER_CLASS:
		strName = "User Class";
		break;

		case DV_SPAWN_AIRCRAFT_TAXI_OUT_TIME_MS:
		strName = "Taxi Out Time";
		break;

		case DV_SPAWN_AIRCRAFT_ACTUAL_DEPARTURE_TIME_MS:
		strName = "Actual Departure Time";
		break;*/
	}

	return strName;
}

CString CDVAircraft::GetValue(int iListId)
{
	CString strValue = "";

	switch(iListId)
	{
	case DV_AIRCRAFT_ACID:
		strValue = m_Aircraft.GetACId();
		break;

	case DV_AIRCRAFT_TAIL_NUMBER:
		strValue = m_Aircraft.GetACTailNumber();
		break;

	case DV_AIRCRAFT_UUID:
		strValue = m_Aircraft.GetDataObjectUUID();
		break;

		/*case DV_SPAWN_AIRCRAFT_ARRIVAL_AIRPORT_CODE:
		strValue = m_Aircraft.GetArrivalAirportCode();
		break;

		case DV_SPAWN_AIRCRAFT_LATITUDE_DEGREES:
		strValue.Format("%.4f", m_Aircraft.GetLatitudeDegrees());
		break;

		case DV_SPAWN_AIRCRAFT_LONGITUDE_DEGREES:
		strValue.Format("%.4f", m_Aircraft.GetLongitudeDegrees());
		break;

		case DV_SPAWN_AIRCRAFT_GND_SPEED_KNOTS:
		strValue.Format("%.4f", m_Aircraft.GetGroundspeedKts());
		break;

		case DV_SPAWN_AIRCRAFT_ALTITUDE_FEET:
		strValue.Format("%.4f", m_Aircraft.GetAltitudeFt());
		break;

		case DV_SPAWN_AIRCRAFT_CRUISE_SPEED_KNOTS:
		strValue.Format("%.4f", m_Aircraft.GetCruiseSpeedKts());
		break;

		case DV_SPAWN_AIRCRAFT_CRUISE_ALT_FEET:
		strValue.Format("%.4f", m_Aircraft.GetCruiseAltitudeFeet());
		break;

		case DV_SPAWN_AIRCRAFT_AIR_SPEED_KNOTS:
		strValue.Format("%.4f", m_Aircraft.GetAirspeedKts());
		break;

		case DV_SPAWN_AIRCRAFT_HEADING_DEGREES:
		strValue.Format("%.4f", m_Aircraft.GetHeadingDegrees());
		break;

		case DV_SPAWN_AIRCRAFT_PITCH_DEGREES:
		strValue.Format("%.4f", m_Aircraft.GetPitchDegrees());
		break;

		case DV_SPAWN_AIRCRAFT_ROLL_DEGREES:
		strValue.Format("%.4f", m_Aircraft.GetRollDegrees());
		break;

		case DV_SPAWN_AIRCRAFT_SECTOR:
		strValue = m_Aircraft.GetSector();
		break;

		case DV_SPAWN_AIRCRAFT_CENTER:
		strValue = m_Aircraft.GetCenter();
		break;

		case DV_SPAWN_AIRCRAFT_SOURCE:
		strValue = m_Aircraft.GetSource();
		break;

		case DV_SPAWN_AIRCRAFT_AIRCRAFT_TYPE:
		strValue = m_Aircraft.GetAircraftType();
		break;

		case DV_SPAWN_AIRCRAFT_PLANNED_DEPARTURE_TIME:
		strValue.Format("%d", m_Aircraft.GetPlannedDepartureTimeMSecs());
		break;

		case DV_SPAWN_AIRCRAFT_PLANNED_ARRIVAL_TIME:
		strValue.Format("%d", m_Aircraft.GetPlannedArrivalTimeMSecs());
		break;

		case DV_SPAWN_AIRCRAFT_ROUTE:
		strValue = m_Aircraft.GetRoute();
		break;

		case DV_SPAWN_AIRCRAFT_DEPARTURE_CENTER:
		strValue = m_Aircraft.GetDepartureCenter();
		break;

		case DV_SPAWN_AIRCRAFT_ARRIVAL_CENTER:
		strValue = m_Aircraft.GetArrivalCenter();
		break;

		case DV_SPAWN_AIRCRAFT_DEPARTURE_FIX:
		strValue = m_Aircraft.GetDepartureFix();
		break;

		case DV_SPAWN_AIRCRAFT_ARRIVAL_FIX:
		strValue = m_Aircraft.GetArrivalFix();
		break;

		case DV_SPAWN_AIRCRAFT_PHYSICAL_CLASS:
		strValue = m_Aircraft.GetPhysicalClass();
		break;

		case DV_SPAWN_AIRCRAFT_WEIGHT_CLASS:
		strValue = m_Aircraft.GetWeightClass();
		break;

		case DV_SPAWN_AIRCRAFT_EQUIPMENT:
		strValue = m_Aircraft.GetEquipmentQualifier();
		break;

		case DV_SPAWN_AIRCRAFT_USER_CLASS:
		strValue = m_Aircraft.GetUserClass();
		break;

		case DV_SPAWN_AIRCRAFT_TAXI_OUT_TIME_MS:
		strValue.Format("%d", m_Aircraft.GetTaxiOutTimeMSecs());
		break;

		case DV_SPAWN_AIRCRAFT_ACTUAL_DEPARTURE_TIME_MS:
		strValue.Format("%d", m_Aircraft.GetActualDepartureTimeMSecs());
		break;*/
	}

	return strValue;
}

void CDVAircraft::Randomize()
{
	CString strRandom = "";

	m_Aircraft.SetACTailNumber("123");

	//m_Aircraft.SetDeptAirportCode("KBWI");

	//m_Aircraft.SetLatitudeDegrees(38.939059);
	//m_Aircraft.SetLongitudeDegrees(-77.45984);
	//m_Aircraft.SetAltitudeFt(1050);

	//m_Aircraft.SetCruiseSpeedKts(rand() % 400);
	//m_Aircraft.SetCruiseAltitudeFeet(rand() % 40000);
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

	//switch(rand() % 4)
	//{
	//	case 0: m_Aircraft.SetAircraftType("B737");
	//			break;
	//	case 1: m_Aircraft.SetAircraftType("B768");
	//			break;
	//	case 2: m_Aircraft.SetAircraftType("C172");
	//			break;
	//	case 3: m_Aircraft.SetAircraftType("B737");
	//			break;
	//	default: m_Aircraft.SetAircraftType("CRJ7");
	//			break;
	//}

	////m_Aircraft.SetAircraftType("CRJ7");

	//m_Aircraft.SetPlannedDepartureTimeMSecs(rand() % 500000);	
	//m_Aircraft.SetPlannedArrivalTimeMSecs(rand() % 500000);	

	//switch(rand() % 2)
	//{
	//case 0: m_Aircraft.SetRoute("BNA..JJB..KYC..JAN");
	//	break;
	//case 1: m_Aircraft.SetRoute("CHI.MOMPS..GERBS.ALPS.BNA");
	//	break;
	//default: m_Aircraft.SetRoute("PHI190.HAPS..LIMA.JBON.BNA");
	//	break;
	//}

	//switch(rand() % 2)
	//{
	//case 0: m_Aircraft.SetDepartureCenter("Cleveland");
	//	break;
	//case 1: m_Aircraft.SetDepartureCenter("Dallas");
	//	break;
	//default: m_Aircraft.SetDepartureCenter("Potomac");
	//	break;
	//}

	//switch(rand() % 2)
	//{
	//case 0: m_Aircraft.SetArrivalCenter("Cleveland");
	//	break;
	//case 1: m_Aircraft.SetArrivalCenter("Dallas");
	//	break;
	//default: m_Aircraft.SetArrivalCenter("Potomac");
	//	break;
	//}

	//switch(rand() % 2)
	//{
	//case 0: m_Aircraft.SetDepartureFix("KSLC");
	//	break;
	//case 1: m_Aircraft.SetDepartureFix("SLC");
	//	break;
	//default: m_Aircraft.SetDepartureFix("DUT");
	//	break;
	//}

	//switch(rand() % 2)
	//{
	//case 0: m_Aircraft.SetArrivalFix("PHNL");
	//	break;
	//case 1: m_Aircraft.SetArrivalFix("CKS");
	//	break;
	//default: m_Aircraft.SetArrivalFix("DUT");
	//	break;
	//}

	//m_Aircraft.SetPhysicalClass(rand() % 1 ? "Q" : "C");	
	//m_Aircraft.SetWeightClass(rand() % 1 ? "H" : "L");	
	//m_Aircraft.SetEquipmentQualifier(rand() % 1 ? "W" : "G");	
	//m_Aircraft.SetUserClass(rand() % 1 ? "U1" : "U2");	

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

void CDVAircraft::Update(CMLDataObject* pData)
{ 
	if(pData) 
		m_Aircraft.Copy(*((CMLAircraft*)pData));
	else
		m_Aircraft.Reset();
}
