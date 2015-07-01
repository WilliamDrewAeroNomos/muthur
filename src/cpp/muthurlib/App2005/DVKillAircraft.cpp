#include "stdafx.h"
#include "dvkillaircraft.h"

#ifdef _DEBUG
#define new DEBUG_NEW
#endif


CDVKillAircraft::CDVKillAircraft()
{
	CMLListCtrlElement* pElement = NULL;

	for(int i = 0; i <= DV_AC_TAXI_IN_ARRIVAL_AIRPORT_CODE; i++)
	{
		pElement = new CMLListCtrlElement(this);
		pElement->SetOwner(i);
		m_apElements.AddTail(pElement);
	}
	/*m_acKillAc.SetLatitudeDegrees(36.122527);
	m_acKillAc.SetLongitudeDegrees(-86.670837);*/
}


CDVKillAircraft::~CDVKillAircraft()
{
}

CString CDVKillAircraft::GetName(int iListId)
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

	case DV_AC_TAXI_IN_DEPARTURE_AIRPORT_CODE:
		strName = "Departure Airport";
		break;

	case DV_AC_TAXI_IN_ARRIVAL_AIRPORT_CODE:
		strName = "Arrival Airport";
		break;
	}

	return strName;
}

CString CDVKillAircraft::GetValue(int iListId)
{
	CString strValue = "";

	switch(iListId)
	{
	case DV_AC_TAXI_IN_ACID:
		strValue = m_acKillAc.GetACId();
		break;

	case DV_AC_TAXI_IN_TAIL_NUMBER:
		strValue = m_acKillAc.GetACTailNumber();
		break;

	case DV_AC_TAXI_IN_DEPARTURE_AIRPORT_CODE:
		strValue = m_acKillAc.GetDeptAirportCode();
		break;

	case DV_AC_TAXI_IN_ARRIVAL_AIRPORT_CODE:
		strValue = m_acKillAc.GetArrivalAirportCode();
		break;
	}

	return strValue;
}

void CDVKillAircraft::Randomize()
{
	CString strRandom = "";

	m_acKillAc.SetACTailNumber("N0000");
	m_acKillAc.SetDeptAirportCode("KBWI");

	switch(rand() % 4)
	{
	case 0: m_acKillAc.SetArrivalAirportCode("KBNA");
		break;
	case 1: m_acKillAc.SetArrivalAirportCode("KBWI");
		break;
	case 2: m_acKillAc.SetArrivalAirportCode("KMIA");
		break;
	case 3: m_acKillAc.SetArrivalAirportCode("KMEM");
		break;
	default: m_acKillAc.SetArrivalAirportCode("KIAD");
		break;
	}	

	switch(rand() % 4)
	{
	case 0: m_acKillAc.SetDeptAirportCode("KDCA");
		break;
	case 1: m_acKillAc.SetDeptAirportCode("KLGA");
		break;
	case 2: m_acKillAc.SetDeptAirportCode("KMCO");
		break;
	case 3: m_acKillAc.SetDeptAirportCode("KATL");
		break;
	default: m_acKillAc.SetDeptAirportCode("KPHL");
		break;
	}	
}

void CDVKillAircraft::Update(CMLDataObject* pData)
{ 
	if(pData) 
		m_acKillAc.Copy(*((CMLKillAircraft*)pData));
	else
		m_acKillAc.Reset();
}
