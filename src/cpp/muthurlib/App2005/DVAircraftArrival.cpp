#include "stdafx.h"
#include "dvaircraftarrival.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


CDVAircraftArrival::CDVAircraftArrival()
{
	CMLListCtrlElement* pElement = NULL;

	for(int i = 0; i <= DV_AC_ARRIVAL_UUID; i++)
	{
		pElement = new CMLListCtrlElement(this);
		pElement->SetOwner(i);
		m_apElements.AddTail(pElement);
	}
	/*m_acArrival.SetLatitudeDegrees(36.122527);
	m_acArrival.SetLongitudeDegrees(-86.670837);*/
}


CDVAircraftArrival::~CDVAircraftArrival()
{
}

CString CDVAircraftArrival::GetName(int iListId)
{
	CString strName = "";

	switch(iListId)
	{
	case DV_AC_ARRIVAL_ACID:
		strName = "AC ID";
		break;

	case DV_AC_ARRIVAL_TAIL_NUMBER:
		strName = "Tail Number";
		break;

		/*case DV_AC_ARRIVAL_DEPARTURE_AIRPORT_CODE:
		strName = "Departure Airport";
		break;*/

	case DV_AC_ARRIVAL_ARRIVAL_AIRPORT_CODE:
		strName = "Arrival Airport";
		break;

	case DV_AC_ARRIVAL_TIME:
		strName = "AC Arrival Time";
		break;

	case DV_AC_ARRIVAL_RUNWAY:
		strName = "Assigned Runway";
		break;

	case DV_AC_ARRIVAL_UUID:
		strName = "Object UUID";
		break;
	}

	return strName;
}

CString CDVAircraftArrival::GetValue(int iListId)
{
	CString strValue = "";

	switch(iListId)
	{
	case DV_AC_ARRIVAL_ACID:
		strValue = m_acArrival.GetACId();
		break;

	case DV_AC_ARRIVAL_TAIL_NUMBER:
		strValue = m_acArrival.GetACTailNumber();
		break;

		//case DV_AC_ARRIVAL_DEPARTURE_AIRPORT_CODE:
		//	strValue = m_acArrival.GetDeptAirportCode();
		//	break;

	case DV_AC_ARRIVAL_ARRIVAL_AIRPORT_CODE:
		strValue = m_acArrival.GetArrivalAirportCode();
		break;

	case DV_AC_ARRIVAL_TIME:
		strValue.Format("%14.0f", m_acArrival.GetActualArrivalTimeMSecs());
		break;

	case DV_AC_ARRIVAL_RUNWAY:
		strValue = m_acArrival.GetAssignedRunway();
		break;	

	case DV_AC_ARRIVAL_UUID:
		strValue = m_acArrival.GetDataObjectUUID();
		break;

	}

	return strValue;
}

void CDVAircraftArrival::Randomize()
{
	CString strRandom = "";

	m_acArrival.SetACTailNumber("N0000");
	//m_acArrival.SetArrivalAirportCode("KBNA");
	//	m_acArrival.SetDeptAirportCode("KBWI");

	m_acArrival.SetActualArrivalTimeMSecs(rand() % 40000);

	switch(rand() % 4)
	{
	case 0: m_acArrival.SetAssignedRunway("13");
		break;
	case 1: m_acArrival.SetAssignedRunway("31");
		break;
	case 2: m_acArrival.SetAssignedRunway("02L");
		break;
	case 3: m_acArrival.SetAssignedRunway("20C");
		break;
	default: m_acArrival.SetAssignedRunway("36");
		break;
	}	

	switch(rand() % 4)
	{
	case 0: m_acArrival.SetArrivalAirportCode("KBWI");
		break;
	case 1: m_acArrival.SetArrivalAirportCode("KIAD");
		break;
	case 2: m_acArrival.SetArrivalAirportCode("KMCO");
		break;
	case 3: m_acArrival.SetArrivalAirportCode("KSJU");
		break;
	default: m_acArrival.SetArrivalAirportCode("KLGA");
		break;
	}	
}

void CDVAircraftArrival::Update(CMLDataObject* pData)
{ 
	if(pData) 
		m_acArrival.Copy(*((CMLAircraftArrivalData*)pData));
	else
		m_acArrival.Reset();
}
