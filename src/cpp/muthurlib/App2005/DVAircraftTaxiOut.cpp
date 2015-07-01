#include "stdafx.h"
#include "dvaircrafttaxiout.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


CDVAircraftTaxiOut::CDVAircraftTaxiOut()
{
	CMLListCtrlElement* pElement = NULL;

	for(int i = 0; i <= DV_AC_TAXI_OUT_UUID; i++)
	{
		pElement = new CMLListCtrlElement(this);
		pElement->SetOwner(i);
		m_apElements.AddTail(pElement);
	}
	/*m_acTaxiOut.SetLatitudeDegrees(36.122527);
	m_acTaxiOut.SetLongitudeDegrees(-86.670837);*/
}


CDVAircraftTaxiOut::~CDVAircraftTaxiOut()
{
}

CString CDVAircraftTaxiOut::GetName(int iListId)
{
	CString strName = "";

	switch(iListId)
	{
	case DV_AC_TAXI_OUT_ACID:
		strName = "AC ID";
		break;

	case DV_AC_TAXI_OUT_TAIL_NUMBER:
		strName = "Tail Number";
		break;

	//case DV_AC_TAXI_OUT_DEPARTURE_AIRPORT_CODE:
	//	strName = "Departure Airport";
	//	break;

	//case DV_AC_TAXI_OUT_ARRIVAL_AIRPORT_CODE:
	//	strName = "Arrival Airport";
	//	break;

	case DV_AC_TAXI_OUT_TIME:
		strName = "AC Taxi In Time";
		break;

	case DV_AC_TAXI_OUT_GATE:
		strName = "Assigned Gate";
		break;
		
	case DV_AC_TAXI_OUT_UUID:
		strName = "Object UUID";
		break;
	}

	return strName;
}

CString CDVAircraftTaxiOut::GetValue(int iListId)
{
	CString strValue = "";

	switch(iListId)
	{
	case DV_AC_TAXI_OUT_ACID:
		strValue = m_acTaxiOut.GetACId();
		break;

	case DV_AC_TAXI_OUT_TAIL_NUMBER:
		strValue = m_acTaxiOut.GetACTailNumber();
		break;

	/*case DV_AC_TAXI_OUT_DEPARTURE_AIRPORT_CODE:
		strValue = m_acTaxiOut.GetDeptAirportCode();
		break;

	case DV_AC_TAXI_OUT_ARRIVAL_AIRPORT_CODE:
		strValue = m_acTaxiOut.GetArrivalAirportCode();
		break;*/

	case DV_AC_TAXI_OUT_TIME:
		strValue.Format("%14.0f", m_acTaxiOut.GetTaxiOutTimeMSecs());
		break;

	case DV_AC_TAXI_OUT_GATE:
		strValue = m_acTaxiOut.GetTaxiOutGate();
		break;	

	case DV_AC_TAXI_OUT_UUID:
		strValue = m_acTaxiOut.GetDataObjectUUID();
		break;
	}

	return strValue;
}

void CDVAircraftTaxiOut::Randomize()
{
	CString strRandom = "";

	m_acTaxiOut.SetACTailNumber("N0000");
//	m_acTaxiOut.SetArrivalAirportCode("KBNA");
//	m_acTaxiOut.SetDeptAirportCode("KBWI");

	m_acTaxiOut.SetTaxiOutTimeMSecs(rand() % 40000);
	
	switch(rand() % 4)
	{
	case 0: m_acTaxiOut.SetTaxiOutGate("A10");
		break;
	case 1: m_acTaxiOut.SetTaxiOutGate("B10");
		break;
	case 2: m_acTaxiOut.SetTaxiOutGate("C10");
		break;
	case 3: m_acTaxiOut.SetTaxiOutGate("D10");
		break;
	default: m_acTaxiOut.SetTaxiOutGate("E10");
		break;
	}	
}

void CDVAircraftTaxiOut::Update(CMLDataObject* pData)
{ 
	if(pData) 
		m_acTaxiOut.Copy(*((CMLAircraftTaxiOut*)pData));
	else
		m_acTaxiOut.Reset();
}
