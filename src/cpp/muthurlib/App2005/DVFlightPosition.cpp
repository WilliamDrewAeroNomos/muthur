#include "stdafx.h"
#include "dvflightposition.h"


#ifdef _DEBUG
#define new DEBUG_NEW
#endif


CDVFlightPosition::CDVFlightPosition()
{
	CMLListCtrlElement* pElement = NULL;
	for(int i = 0; i <= DV_FLIGHT_POSITION_UUID; i++)
	{
		pElement = new CMLListCtrlElement(this);
		pElement->SetOwner(i);
		m_apElements.AddTail(pElement);
	}
	/*m_flightPosition.SetLatitudeDegrees(36.122527);
	m_flightPosition.SetLongitudeDegrees(-86.670837);*/
}


CDVFlightPosition::~CDVFlightPosition()
{
}

CString CDVFlightPosition::GetName(int iListId)
{
	CString strName = "";

	switch(iListId)
	{
	case DV_FLIGHT_POSITION_ACID:
		strName = "AC ID";
		break;

	case DV_FLIGHT_POSITION_TAIL_NUMBER:
		strName = "Tail Number";
		break;

		/*case DV_FLIGHT_POSITION_DEPARTURE_AIRPORT_CODE:
		strName = "Departure Airport";
		break;

		case DV_FLIGHT_POSITION_ARRIVAL_AIRPORT_CODE:
		strName = "Arrival Airport";
		break;*/

	case DV_FLIGHT_POSITION_LATITUDE_DEGREES:
		strName = "Latitude";
		break;

	case DV_FLIGHT_POSITION_LONGITUDE_DEGREES:
		strName = "Longitude";
		break;

	case DV_FLIGHT_POSITION_ALTITUDE_FEET:
		strName = "Altitude";
		break;

	case DV_FLIGHT_POSITION_GND_SPEED_KNOTS:
		strName = "Ground Speed";
		break;

	case DV_FLIGHT_POSITION_CRUISE_ALT_FEET:
		strName = "Cruise Altitude";
		break;

	case DV_FLIGHT_POSITION_AIR_SPEED_KNOTS:
		strName = "Air Speed";
		break;

	case DV_FLIGHT_POSITION_HEADING_DEGREES:
		strName = "Heading";
		break;

	case DV_FLIGHT_POSITION_PITCH_DEGREES:
		strName = "Pitch";
		break;

	case DV_FLIGHT_POSITION_ROLL_DEGREES:
		strName = "Roll";
		break;

	case DV_FLIGHT_POSITION_CENTER:
		strName = "Center";
		break;

	case DV_FLIGHT_POSITION_SECTOR:
		strName = "Sector";
		break;
	case DV_FLIGHT_POSITION_VERT_SPD:
		strName = "Vertical Speed";
		break;
	case DV_FLIGHT_POSITION_ONGROUND:
		strName = "AC on Ground?";
		break;
	case DV_FLIGHT_POSITION_FREQ:
		strName = "Frequency";
		break;
	case DV_FLIGHT_POSITION_SQ_CODE:
		strName = "Squawk Code";
		break;
	case DV_FLIGHT_POSITION_IDENT:
		strName = "Ident";
		break;
	case DV_FLIGHT_POSITION_UUID:
		strName = "Object UUID";
		break;
	}

	return strName;
}

CString CDVFlightPosition::GetValue(int iListId)
{
	CString strValue = "";

	switch(iListId)
	{
	case DV_FLIGHT_POSITION_ACID:
		strValue = m_flightPosition.GetACId();
		break;

	case DV_FLIGHT_POSITION_TAIL_NUMBER:
		strValue = m_flightPosition.GetACTailNumber();
		break;

		/*case DV_FLIGHT_POSITION_DEPARTURE_AIRPORT_CODE:
		strValue = m_flightPosition.GetDeptAirportCode();
		break;

		case DV_FLIGHT_POSITION_ARRIVAL_AIRPORT_CODE:
		strValue = m_flightPosition.GetArrivalAirportCode();
		break;*/

	case DV_FLIGHT_POSITION_LATITUDE_DEGREES:
		strValue.Format("%.7f", m_flightPosition.GetLatitudeDegrees());
		break;

	case DV_FLIGHT_POSITION_LONGITUDE_DEGREES:
		strValue.Format("%.7f", m_flightPosition.GetLongitudeDegrees());
		break;

	case DV_FLIGHT_POSITION_ALTITUDE_FEET:
		strValue.Format("%.4f", m_flightPosition.GetAltitudeFt());
		break;

	case DV_FLIGHT_POSITION_GND_SPEED_KNOTS:
		strValue.Format("%.4f", m_flightPosition.GetGroundspeedKts());
		break;

	case DV_FLIGHT_POSITION_CRUISE_ALT_FEET:
		strValue.Format("%.4f", m_flightPosition.GetFlightPosCruiseAltitudeFeet());
		break;

	case DV_FLIGHT_POSITION_AIR_SPEED_KNOTS:
		strValue.Format("%.4f", m_flightPosition.GetAirspeedKts());
		break;

	case DV_FLIGHT_POSITION_HEADING_DEGREES:
		strValue.Format("%.4f", m_flightPosition.GetHeadingDegrees());
		break;

	case DV_FLIGHT_POSITION_PITCH_DEGREES:
		strValue.Format("%.4f", m_flightPosition.GetPitchDegrees());
		break;

	case DV_FLIGHT_POSITION_ROLL_DEGREES:
		strValue.Format("%.4f", m_flightPosition.GetRollDegrees());
		break;

	case DV_FLIGHT_POSITION_CENTER:
		strValue = m_flightPosition.GetCenter();
		break;

	case DV_FLIGHT_POSITION_SECTOR:
		strValue = m_flightPosition.GetSector();
		break;
	case DV_FLIGHT_POSITION_VERT_SPD:
		strValue.Format("%.4f", m_flightPosition.GetVerticalSpeed());
		break;
	case DV_FLIGHT_POSITION_ONGROUND:
		strValue.Format("%d", m_flightPosition.GetIsAcOnGround());
		break;
	case DV_FLIGHT_POSITION_FREQ:
		strValue.Format("%d", m_flightPosition.GetFrequency());
		break;
	case DV_FLIGHT_POSITION_SQ_CODE:
		strValue = m_flightPosition.GetSquawkCode();
		break;
	case DV_FLIGHT_POSITION_IDENT:
		strValue.Format("%d", m_flightPosition.GetIdent());
		break;
	case DV_FLIGHT_POSITION_UUID:
		strValue = m_flightPosition.GetDataObjectUUID();
		break;

	}

	return strValue;
}

void CDVFlightPosition::Randomize()
{
	CString strRandom = "";

	m_flightPosition.SetACTailNumber("7253");

	/*m_flightPosition.SetArrivalAirportCode("KBNA");
	m_flightPosition.SetDeptAirportCode("KBWI");*/

	m_flightPosition.SetLatitudeDegrees(38.948228);
	m_flightPosition.SetLongitudeDegrees(-77.441334);
	m_flightPosition.SetAltitudeFt(312);

	m_flightPosition.SetGroundspeedKts(0);
	
	m_flightPosition.SetFlightPosCruiseAltitudeFeet(250);
	m_flightPosition.SetAirspeedKts(0);


	// At outer marker (about 1500 to 2000 ft); roughly 160 knots
	// About 10,000 ft at LUSIE
	// About 250 knots at 10,000

	// m_flightPosition.SetHeadingDegrees(359);
/*
	switch(rand() % 2)
	{
	case 0:*/ m_flightPosition.SetHeadingDegrees(38);
	//	break;
	//case 1: m_flightPosition.SetHeadingDegrees(180);
	//	break;
	//default: m_flightPosition.SetHeadingDegrees(90);
	//	break;
	//}
	m_flightPosition.SetPitchDegrees(0);
	m_flightPosition.SetRollDegrees(0);

	 m_flightPosition.SetCenter("ZDC");

	//strRandom.Format("%d", /*(rand() % 4) +*/ 66);
	m_flightPosition.SetSector("GND");	


	//m_flightPosition.SetLatitudeDegrees(36.122527);
		//m_flightPosition.SetLongitudeDegrees(-86.670837);
	//m_flightPosition.SetAltitudeFt(608.172476);
	//m_flightPosition.SetGroundspeedKts(0);
	//m_flightPosition.SetCruiseAltitudeFeet(0);
	//m_flightPosition.SetAirspeedKts(0);

	//m_flightPosition.SetHeadingDegrees(0);
	//m_flightPosition.SetPitchDegrees(0);
	//m_flightPosition.SetRollDegrees(0);

	m_flightPosition.SetVerticalSpeed(0);	
	m_flightPosition.SetIsAcOnGround(true);
	m_flightPosition.SetFrequency(12190);

	switch(rand() % 2)
	{
	case 0: m_flightPosition.SetSquawkCode("2467");
		break;
	case 1: m_flightPosition.SetSquawkCode("4321");
		break;
	default: m_flightPosition.SetSquawkCode("1234");
		break;
	}

	m_flightPosition.SetIdent(false);
}

void CDVFlightPosition::Update(CMLDataObject* pData)
{ 
	if(pData) 
		m_flightPosition.Copy(*((CMLFlightPosition*)pData));
	else
		m_flightPosition.Reset();
}
