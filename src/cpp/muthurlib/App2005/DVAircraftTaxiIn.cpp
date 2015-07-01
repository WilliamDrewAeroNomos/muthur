#include "stdafx.h"
#include "dvaircrafttaxiin.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


CDVAircraftTaxiIn::CDVAircraftTaxiIn()
{
	CMLListCtrlElement* pElement = NULL;

	for(int i = 0; i <= DV_AC_TAXI_IN_UUID; i++)
	{
		pElement = new CMLListCtrlElement(this);
		pElement->SetOwner(i);
		m_apElements.AddTail(pElement);
	}
	/*m_acTaxiIn.SetLatitudeDegrees(36.122527);
	m_acTaxiIn.SetLongitudeDegrees(-86.670837);*/
}


CDVAircraftTaxiIn::~CDVAircraftTaxiIn()
{
}

CString CDVAircraftTaxiIn::GetName(int iListId)
{
	CString strName = "";

	switch(iListId)
	{
	case DV_AC_TAXI_IN_ACID:
		strName = "AC ID";
		break;

	case DV_AC_TAXI_IN_TAIL_NUMBER:
		strName = "Tail Number";
		break;

	//case DV_AC_TAXI_IN_DEPARTURE_AIRPORT_CODE:
	//	strName = "Departure Airport";
	//	break;

	//case DV_AC_TAXI_IN_ARRIVAL_AIRPORT_CODE:
	//	strName = "Arrival Airport";
	//	break;

	case DV_AC_TAXI_IN_TIME:
		strName = "AC Taxi In Time";
		break;

	case DV_AC_TAXI_IN_GATE:
		strName = "Assigned Gate";
		break;

	case DV_AC_TAXI_IN_UUID:
		strName = "Object UUID";
		break;
	}

	return strName;
}

CString CDVAircraftTaxiIn::GetValue(int iListId)
{
	CString strValue = "";

	switch(iListId)
	{
	case DV_AC_TAXI_IN_ACID:
		strValue = m_acTaxiIn.GetACId();
		break;

	case DV_AC_TAXI_IN_TAIL_NUMBER:
		strValue = m_acTaxiIn.GetACTailNumber();
		break;

	/*case DV_AC_TAXI_IN_DEPARTURE_AIRPORT_CODE:
		strValue = m_acTaxiIn.GetDeptAirportCode();
		break;

	case DV_AC_TAXI_IN_ARRIVAL_AIRPORT_CODE:
		strValue = m_acTaxiIn.GetArrivalAirportCode();
		break;*/

	case DV_AC_TAXI_IN_TIME:
		strValue.Format("%14.0f", m_acTaxiIn.GetTaxiInTimeMSecs());
		break;

	case DV_AC_TAXI_IN_GATE:
		strValue = m_acTaxiIn.GetTaxiInGate();
		break;	
		
	case DV_AC_TAXI_IN_UUID:
		strValue = m_acTaxiIn.GetDataObjectUUID();
			break;

	}

	return strValue;
}

void CDVAircraftTaxiIn::Randomize()
{
	CString strRandom = "";

	m_acTaxiIn.SetACTailNumber("N0000");
	//m_acTaxiIn.SetArrivalAirportCode("KBNA");
	//m_acTaxiIn.SetDeptAirportCode("KBWI");

	m_acTaxiIn.SetTaxiInTimeMSecs(rand() % 40000);
	
	switch(rand() % 4)
	{
	case 0: m_acTaxiIn.SetTaxiInGate("A1");
		break;
	case 1: m_acTaxiIn.SetTaxiInGate("B1");
		break;
	case 2: m_acTaxiIn.SetTaxiInGate("C1");
		break;
	case 3: m_acTaxiIn.SetTaxiInGate("D1");
		break;
	default: m_acTaxiIn.SetTaxiInGate("E1");
		break;
	}	
}

void CDVAircraftTaxiIn::Update(CMLDataObject* pData)
{ 
	if(pData) 
		m_acTaxiIn.Copy(*((CMLAircraftTaxiIn*)pData));
	else
		m_acTaxiIn.Reset();
}
