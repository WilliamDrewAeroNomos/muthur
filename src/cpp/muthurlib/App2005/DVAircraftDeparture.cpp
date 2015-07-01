#include "stdafx.h"
#include "dvaircraftdeparture.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


CDVAircraftDeparture::CDVAircraftDeparture()
{
	CMLListCtrlElement* pElement = NULL;

	for(int i = 0; i <= DV_AC_DEPARTURE_UUID; i++)
	{
		pElement = new CMLListCtrlElement(this);
		pElement->SetOwner(i);
		m_apElements.AddTail(pElement);
	}
	/*m_acDepart.SetLatitudeDegrees(36.122527);
	m_acDepart.SetLongitudeDegrees(-86.670837);*/
}


CDVAircraftDeparture::~CDVAircraftDeparture()
{
}

CString CDVAircraftDeparture::GetName(int iListId)
{
	CString strName = "";

	switch(iListId)
	{
	case DV_AC_DEPARTURE_ACID:
		strName = "AC ID";
		break;

	case DV_AC_DEPARTURE_TAIL_NUMBER:
		strName = "Tail Number";
		break;

	case DV_AC_DEPARTURE_DEPARTURE_AIRPORT_CODE:
		strName = "Departure Airport";
		break;

	case DV_AC_DEPARTURE_TIME:
		strName = "AC Departure Time";
		break;

	case DV_AC_DEPARTURE_RUNWAY:
		strName = "Assigned Runway";
		break;

	case DV_AC_DEPARTURE_UUID:
		strName = "Object UUID";
		break;
	}

	return strName;
}

CString CDVAircraftDeparture::GetValue(int iListId)
{
	CString strValue = "";

	switch(iListId)
	{
	case DV_AC_DEPARTURE_ACID:
		strValue = m_acDepart.GetACId();
		break;

	case DV_AC_DEPARTURE_TAIL_NUMBER:
		strValue = m_acDepart.GetACTailNumber();
		break;

	case DV_AC_DEPARTURE_DEPARTURE_AIRPORT_CODE:
		strValue = m_acDepart.GetDeptAirportCode();
		break;

		/*case DV_AC_DEPARTURE_ARRIVAL_AIRPORT_CODE:
		strValue = m_acDepart.GetArrivalAirportCode();
		break;*/

	case DV_AC_DEPARTURE_TIME:
		strValue.Format("%14.0f", m_acDepart.GetActualDepartureTimeMSecs());
		break;

	case DV_AC_DEPARTURE_RUNWAY:
		strValue = m_acDepart.GetAssignedRunway();
		break;	

	case DV_AC_DEPARTURE_UUID:
		strValue = m_acDepart.GetDataObjectUUID();
			break;

	}

	return strValue;
}

void CDVAircraftDeparture::Randomize()
{
	CString strRandom = "";

	m_acDepart.SetACTailNumber("N0000");
	//	m_acDepart.SetArrivalAirportCode("KBNA");
	m_acDepart.SetDeptAirportCode("KBWI");

	m_acDepart.SetActualDepartureTimeMSecs(rand() % 40000);

	switch(rand() % 4)
	{
	case 0: m_acDepart.SetAssignedRunway("13");
		break;
	case 1: m_acDepart.SetAssignedRunway("31");
		break;
	case 2: m_acDepart.SetAssignedRunway("02L");
		break;
	case 3: m_acDepart.SetAssignedRunway("20C");
		break;
	default: m_acDepart.SetAssignedRunway("36");
		break;
	}	
}

void CDVAircraftDeparture::Update(CMLDataObject* pData)
{ 
	if(pData) 
		m_acDepart.Copy(*((CMLAircraftDepartureData*)pData));
	else
		m_acDepart.Reset();
}
