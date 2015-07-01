//------------------------------------------------------------------------------
/*! \file	MLFlightPlanData.cpp
//
//  Contains the implementation of the CMLFlightPlan class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>06-01-2011 <td>1.00		<td>Original Release <td>REB	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include "MLFlightPlanData.h"
//-----------------------------------------------------------------------------
//	DEFINES
//-----------------------------------------------------------------------------

//-----------------------------------------------------------------------------
//	GLOBALS
//-----------------------------------------------------------------------------

//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLFlightPlan::CMLFlightPlan()
{
	//	Assign defaults
	//CMLDataObject::Reset();
	Reset();
}

//------------------------------------------------------------------------------
//!	Summary:	Copy constructor
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLFlightPlan::CMLFlightPlan(const CMLFlightPlan& rSource)
{
	//CMLDataObject::Copy(rSource);
	Copy(rSource);
}

//------------------------------------------------------------------------------
//!	Summary:	Destructor
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLFlightPlan::~CMLFlightPlan()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPlan::Copy(const CMLFlightPlan& rSource)
{		
	PTRNODE				Pos = NULL;
	CMLString	*pAttribute = NULL;

	CMLDataObject::Copy(rSource);
	lstrcpy(m_szSource, rSource.m_szSource); 								
	lstrcpy(m_szAircraftType, rSource.m_szAircraftType); 					
	m_dPlannedDepartureTimeMSecs = rSource.m_dPlannedDepartureTimeMSecs;		
	m_dPlannedArrivalTimeMSecs = rSource.m_dPlannedArrivalTimeMSecs;			
	m_dbCruiseSpeedKts = rSource.m_dbCruiseSpeedKts;	
	m_dCruiseAltitudeFeet = rSource.m_dCruiseAltitudeFeet;						
	lstrcpy(m_szRoute, rSource.m_szRoute); 							
	lstrcpy(m_szDepartureCenter, rSource.m_szDepartureCenter); 		
	lstrcpy(m_szArrivalCenter, rSource.m_szArrivalCenter); 			
	lstrcpy(m_szDepartureFix, rSource.m_szDepartureFix); 			
	lstrcpy(m_szArrivalFix, rSource.m_szArrivalFix); 				
	lstrcpy(m_szPhysicalClass, rSource.m_szPhysicalClass); 			
	lstrcpy(m_szWeightClass, rSource.m_szWeightClass); 				
	lstrcpy(m_szUserClass, rSource.m_szUserClass); 					
	lstrcpy(m_szEquipmentQualifier, rSource.m_szEquipmentQualifier); 					
	m_iNumAircraft = rSource.m_iNumAircraft;
	lstrcpy(m_szPlannedDeptGate, rSource.m_szPlannedDeptGate); 
	lstrcpy(m_szPlannedArrGate, rSource.m_szPlannedArrGate); 
	lstrcpy(m_szPlannedDeptRunway, rSource.m_szPlannedDeptRunway); 
	lstrcpy(m_szPlannedArrRunway, rSource.m_szPlannedArrRunway);

	//	Copy all the attributes
	//const CMLPtrList& rAttributes = rSource.m_apAttributes;
	//Pos = rAttributes.GetHeadPosition();
	//while(Pos != NULL)
	//{
	//	if((pAttribute = (CMLString*)(rAttributes.GetNext(Pos))) != NULL)
	//	{
	//		AddAttribute(*pAttribute); // add a copy
	//	}
	//}
}

//------------------------------------------------------------------------------
//!	Summary:	Called to reset the class members to their default values
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPlan::Reset()
{		
	CMLDataObject::Reset();
	memset(m_szSource, 0, sizeof(m_szSource));							
	memset(m_szAircraftType, 0, sizeof(m_szAircraftType));					
	m_dPlannedDepartureTimeMSecs = 0;		
	m_dPlannedArrivalTimeMSecs = 0;			
	m_dbCruiseSpeedKts = 0;	
	m_dCruiseAltitudeFeet = 0;
	memset(m_szRoute, 0, sizeof(m_szRoute));
	memset(m_szDepartureCenter, 0, sizeof(m_szDepartureCenter));
	memset(m_szArrivalCenter, 0, sizeof(m_szArrivalCenter));	
	memset(m_szDepartureFix, 0, sizeof(m_szDepartureFix));		
	memset(m_szArrivalFix, 0, sizeof(m_szArrivalFix));			
	memset(m_szPhysicalClass, 0, sizeof(m_szPhysicalClass));		
	memset(m_szWeightClass, 0, sizeof(m_szWeightClass));	
	memset(m_szUserClass, 0, sizeof(m_szUserClass));			
	memset(m_szEquipmentQualifier, 0, sizeof(m_szEquipmentQualifier));			
	m_iNumAircraft = 0;	
	memset(m_szPlannedArrGate, 0, sizeof(m_szPlannedArrGate));
	memset(m_szPlannedDeptGate, 0, sizeof(m_szPlannedDeptGate));
	memset(m_szPlannedDeptRunway, 0, sizeof(m_szPlannedDeptRunway));
	memset(m_szPlannedArrRunway, 0, sizeof(m_szPlannedArrRunway));

	/*AddAttribute(FP_SOURCE);
	AddAttribute(AC_TYPE);
	AddAttribute(PLANNED_DEPT_TIME);
	AddAttribute(PLANNED_ARR_TIME);
	AddAttribute(CRUISE_SPEED);
	AddAttribute(CRUISE_ALT);
	AddAttribute(ROUTE_PLAN);
	AddAttribute(DEPT_CENTER);
	AddAttribute(ARR_CENTER);
	AddAttribute(DEPT_FIX);
	AddAttribute(ARR_FIX);
	AddAttribute(PHYS_AC_CLASS);
	AddAttribute(WEIGHT_AC_CLSS);
	AddAttribute(USER_AC_CLASS);
	AddAttribute(NUM_AC);
	AddAttribute(AC_EQUIP_QUAL);*/
}
//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the planned departure gate
//!
//!	Parameters:	None
//!
//!	Returns:	The planned departure gate
//------------------------------------------------------------------------------
char* CMLFlightPlan::GetPlannedDeptGate()				
{
	return m_szPlannedDeptGate;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the planned arrival gate
//!
//!	Parameters:	None
//!
//!	Returns:	The planned arrival gate
//------------------------------------------------------------------------------
char* CMLFlightPlan::GetPlannedArrGate()			
{
	return m_szPlannedArrGate;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the planned departure runway
//!
//!	Parameters:	None
//!
//!	Returns:	The planned departure runway
//------------------------------------------------------------------------------
char* CMLFlightPlan::GetPlannedDeptRunway()				
{
	return m_szPlannedDeptRunway;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the planned arrival runway
//!
//!	Parameters:	None
//!
//!	Returns:	The planned arrival runway
//------------------------------------------------------------------------------
char* CMLFlightPlan::GetPlannedArrRunway()			
{
	return m_szPlannedArrRunway;
}


//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the system or federate that supplied this flight
//!
//!	Parameters:	None
//!
//!	Returns:	The system or federate that supplied this flight
//------------------------------------------------------------------------------
char* CMLFlightPlan::GetSource() { return m_szSource; }	

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the airplane type designator
//!
//!	Parameters:	None
//!
//!	Returns:	The airplane type designator
//------------------------------------------------------------------------------
char* CMLFlightPlan::GetAircraftType() { return m_szAircraftType; }

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the time in milliseconds when the flight plans to depart the airport, which
//!				will be defined for now as when the aircraft pushes back from the gate.
//!
//!	Parameters:	None
//!
//!	Returns:	The time in milliseconds when the flight plans to depart the airport, which will be
//!				defined for now as when the aircraft pushes back from the gate.
//------------------------------------------------------------------------------
double CMLFlightPlan::GetPlannedDepartureTimeMSecs() { return m_dPlannedDepartureTimeMSecs; }

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the time in milliseconds that the aircraft plans to arrive at its destination airport. 
//!				Arrival time is defined as when the aircraft arrives at the gate.
//!
//!	Parameters:	None
//!
//!	Returns:	The time in milliseconds that the aircraft plans to arrive at its destination airport. 
//!             Arrival time is defined as when the aircraft arrives at the gate.
//------------------------------------------------------------------------------
double CMLFlightPlan::GetPlannedArrivalTimeMSecs() { return m_dPlannedArrivalTimeMSecs; }	

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the cruise altitude of the air craft in feet
//!
//!	Parameters:	None
//!
//!	Returns:	The cruise altitude of the air craft in feet
//------------------------------------------------------------------------------
double CMLFlightPlan::GetCruiseAltitudeFeet() { return m_dCruiseAltitudeFeet; }

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the cruise speed of the air craft in knots
//!
//!	Parameters:	None
//!
//!	Returns:	The cruise speed of the air craft in knots
//------------------------------------------------------------------------------
double CMLFlightPlan::GetCruiseSpeedKts() { return m_dbCruiseSpeedKts; }

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the flight plan for this aircraft patterned after the actual plans submitted by the airlines and pilots
//!
//!	Parameters:	None
//!
//!	Returns:	The flight plan for this aircraft patterned after the actual plans submitted by the airlines and pilots
//------------------------------------------------------------------------------
char* CMLFlightPlan::GetRoute() { return m_szRoute; }

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the enter from which the aircraft departed
//!
//!	Parameters:	None
//!
//!	Returns:	The enter from which the aircraft departed
//------------------------------------------------------------------------------
char* CMLFlightPlan::GetDepartureCenter() { return m_szDepartureCenter; }

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the center where the aircraft is destined to arrive
//!
//!	Parameters:	None
//!
//!	Returns:	Center where the aircraft is destined to arrive
//------------------------------------------------------------------------------
char* CMLFlightPlan::GetArrivalCenter() { return m_szArrivalCenter; }
//------------------------------------------------------------------------------
//!	Summary:	Reserved for future use
//!
//!	Parameters:	None
//!
//!	Returns:	Reserved for future use
//------------------------------------------------------------------------------
char* CMLFlightPlan::GetDepartureFix() { return m_szDepartureFix; }

//------------------------------------------------------------------------------
//!	Summary:	Reserved for future use
//!
//!	Parameters:	None
//!
//!	Returns:	Reserved for future use
//------------------------------------------------------------------------------
char* CMLFlightPlan::GetArrivalFix() { return m_szArrivalFix; }

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the physical category of the aircraft
//!
//!	Parameters:	None
//!
//!	Returns:	The physical category of the aircraft
//------------------------------------------------------------------------------
char* CMLFlightPlan::GetPhysicalClass() { return m_szPhysicalClass; }

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the weight class of the air craft
//!
//!	Parameters:	None
//!
//!	Returns:	The weight class of the air craft
//------------------------------------------------------------------------------
char* CMLFlightPlan::GetWeightClass() { return m_szWeightClass; }

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the aircraft's equipment qualifier
//!
//!	Parameters:	None
//!
//!	Returns:	The equipment qualifier
//------------------------------------------------------------------------------
char* CMLFlightPlan::GetEquipmentQualifier() { return m_szEquipmentQualifier; }

//------------------------------------------------------------------------------
//!	Summary:	Reserved for future use
//!
//!	Parameters:	None
//!
//!	Returns:	Reserved for future use
//------------------------------------------------------------------------------
char* CMLFlightPlan::GetUserClass() { return m_szUserClass; }					

//------------------------------------------------------------------------------
//!	Summary:	Reserved for future use
//!
//!	Parameters:	None
//!
//!	Returns:	
//------------------------------------------------------------------------------
int CMLFlightPlan::GetNumAircraft() { return m_iNumAircraft; }				

//------------------------------------------------------------------------------
//!	Summary:	Called to set the planned dept gate
//!
//!	Parameters:	\li p - planned dept gate
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPlan::SetPlannedDeptGate(char* p)	
{
	if(p != NULL)
		lstrcpyn(m_szPlannedDeptGate, p, sizeof(m_szPlannedDeptGate));
	else
		memset(m_szPlannedDeptGate, 0, sizeof(m_szPlannedDeptGate));
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the planned arrival gate
//!
//!	Parameters:	\li p - planned arrival gate
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPlan::SetPlannedArrGate(char* p)	
{
	if(p != NULL)
		lstrcpyn(m_szPlannedArrGate, p, sizeof(m_szPlannedArrGate));
	else
		memset(m_szPlannedArrGate, 0, sizeof(m_szPlannedArrGate));
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the planned dept runway
//!
//!	Parameters:	\li p - planned dept runway
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPlan::SetPlannedDeptRunway(char* p)	
{
	if(p != NULL)
		lstrcpyn(m_szPlannedDeptRunway, p, sizeof(m_szPlannedDeptRunway));
	else
		memset(m_szPlannedDeptRunway, 0, sizeof(m_szPlannedDeptRunway));
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the planned arrival runway
//!
//!	Parameters:	\li p - planned arrival runway
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPlan::SetPlannedArrRunway(char* p)	
{
	if(p != NULL)
		lstrcpyn(m_szPlannedArrRunway, p, sizeof(m_szPlannedArrRunway));
	else
		memset(m_szPlannedArrRunway, 0, sizeof(m_szPlannedArrRunway));
}


//------------------------------------------------------------------------------
//!	Summary:	Called to set the system or federate that supplied this flight
//!
//!	Parameters:	\li p - system or federate that supplied this flight
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPlan::SetSource(char* p)	
{
	if(p != NULL)
		lstrcpyn(m_szSource, p, sizeof(m_szSource));
	else
		memset(m_szSource, 0, sizeof(m_szSource));
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the airplane type designator
//!
//!	Parameters:	\li p - airplane type designator
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPlan::SetAircraftType(char* p)	
{
	if(p != NULL)
		lstrcpyn(m_szAircraftType, p, sizeof(m_szAircraftType));
	else
		memset(m_szAircraftType, 0, sizeof(m_szAircraftType));
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the Time in milliseconds when the flight plans to depart the airport, which will be 
//!				defined for now as when the aircraft pushes back from the gate.
//!
//!	Parameters:	\li l - Time in milliseconds when the flight plans to depart the airport, 
//!						which will be defined for now as when the aircraft pushes back from the gate.
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPlan::SetPlannedDepartureTimeMSecs(double d)	
{
	m_dPlannedDepartureTimeMSecs = d;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the time in milliseconds that the aircraft plans to arrive at its destination airport. 
//!				Arrival time is defined as when the aircraft arrives at the gate.
//!
//!	Parameters:	\li l - time in milliseconds that the aircraft plans to arrive at its destination airport.
//!									  Arrival time is defined as when the aircraft arrives at the gate.
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPlan::SetPlannedArrivalTimeMSecs(double d)
{
	m_dPlannedArrivalTimeMSecs = d;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the cruise altitude of the air craft in feet
//!
//!	Parameters:	\li d - cruise altitude of the air craft in feet
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPlan::SetCruiseAltitudeFeet(double d)		
{
	m_dCruiseAltitudeFeet = d;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the cruise speed of the air craft in knots
//!
//!	Parameters:	\li d - cruise speed of the air craft in knots
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPlan::SetCruiseSpeedKts(double d)		
{
	m_dbCruiseSpeedKts = d;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the flight plan for this aircraft patterned after the actual plans submitted by the airlines and pilots
//!
//!	Parameters:	\li p - flight plan for this aircraft patterned after the actual plans submitted by the airlines and pilots
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPlan::SetRoute(char* p)					
{
	if(p != NULL)
		lstrcpyn(m_szRoute, p, sizeof(m_szRoute));
	else
		memset(m_szRoute, 0, sizeof(m_szRoute));
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the center from which the aircraft departed
//!
//!	Parameters:	\li p - center from which the aircraft departed
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPlan::SetDepartureCenter(char* p)	
{
	if(p != NULL)
		lstrcpyn(m_szDepartureCenter, p, sizeof(m_szDepartureCenter));
	else
		memset(m_szDepartureCenter, 0, sizeof(m_szDepartureCenter));
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the Center where the aircraft is destined to arrive
//!
//!	Parameters:	\li p - Center where the aircraft is destined to arrive
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPlan::SetArrivalCenter(char* p)			
{
	if(p != NULL)
		lstrcpyn(m_szArrivalCenter, p, sizeof(m_szArrivalCenter));
	else
		memset(m_szArrivalCenter, 0, sizeof(m_szArrivalCenter));
}

//------------------------------------------------------------------------------
//!	Summary:	Reserved for future use
//!
//!	Parameters:	\li p - Reserved for future use
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPlan::SetDepartureFix(char* p)				
{
	if(p != NULL)
		lstrcpyn(m_szDepartureFix, p, sizeof(m_szDepartureFix));
	else
		memset(m_szDepartureFix, 0, sizeof(m_szDepartureFix));
}

//------------------------------------------------------------------------------
//!	Summary:	Reserved for future use
//!
//!	Parameters:	\li p - Reserved for future use
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPlan::SetArrivalFix(char* p)				
{
	if(p != NULL)
		lstrcpyn(m_szArrivalFix, p, sizeof(m_szArrivalFix));
	else
		memset(m_szArrivalFix, 0, sizeof(m_szArrivalFix));
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the time stamp when the object was created
//!
//!	Parameters:	\li p - physical category of the aircraft
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPlan::SetPhysicalClass(char* p)
{
	if(p != NULL)
		lstrcpyn(m_szPhysicalClass, p, sizeof(m_szPhysicalClass));
	else
		memset(m_szPhysicalClass, 0, sizeof(m_szPhysicalClass));
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the weight class of the air craft
//!
//!	Parameters:	\li p - weight class of the air craft
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPlan::SetWeightClass(char* p)	
{
	if(p != NULL)
		lstrcpyn(m_szWeightClass, p, sizeof(m_szWeightClass));
	else
		memset(m_szWeightClass, 0, sizeof(m_szWeightClass));
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the equipment qualifer
//!
//!	Parameters:	\li p - the equipment qualifier
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPlan::SetEquipmentQualifier(char* p)	
{
	if(p != NULL)
		lstrcpyn(m_szEquipmentQualifier, p, sizeof(m_szEquipmentQualifier));
	else
		memset(m_szEquipmentQualifier, 0, sizeof(m_szEquipmentQualifier));
}

//------------------------------------------------------------------------------
//!	Summary:	 Reserved for future use
//!
//!	Parameters:	\li p -  Reserved for future use
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPlan::SetUserClass(char* p)					
{
	if(p != NULL)
		lstrcpyn(m_szUserClass, p, sizeof(m_szUserClass));
	else
		memset(m_szUserClass, 0, sizeof(m_szUserClass));
}

//------------------------------------------------------------------------------
//!	Summary:	 Reserved for future use
//!
//!	Parameters:	\li i -  Reserved for future use
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLFlightPlan::SetNumAircraft(int i)					
{
	m_iNumAircraft = i;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get an XML element descriptor that defines all 
//!				properties in the specified block
//!
//!	Parameters:	\li eXmlBlock - the enumerated block identifier
//!
//!	Returns:	The element used to construct the XML stream
//!
//!	Remarks:	The caller is responsible for deallocation of the object
//------------------------------------------------------------------------------
CMLPropMember* CMLFlightPlan::GetXmlBlock(EMLXMLBlock eXmlBlock)
{
	CMLPropMember* pXmlBlock = NULL;
	CMLPropMember* pXmlChild = NULL;

	//	Use the base class to allocate the object
	if((pXmlBlock = CMLDataObject::GetXmlBlock(eXmlBlock)) != NULL)
	{
		switch(eXmlBlock)
		{
			case ML_XML_BLOCK_CONTROL:

				// No additional control block properties for this derived class
				break;
			
			case ML_XML_BLOCK_DATA:

				//	Create a child for the flight plan data and add it to the base class element
				pXmlChild = new CMLPropMember(ML_XML_DATA_BLOCK_FLIGHT_PLAN, "");
				
				pXmlChild->AddChild(FP_SOURCE, "Flight");
				pXmlChild->AddChild(AC_TYPE, m_szAircraftType);
				pXmlChild->AddChild(PLANNED_DEPT_TIME, m_dPlannedDepartureTimeMSecs, 0);
				pXmlChild->AddChild(PLANNED_DEPT_RW, m_szPlannedDeptRunway);
				pXmlChild->AddChild(PLANNED_DEPT_GATE, m_szPlannedDeptGate);
				pXmlChild->AddChild(PLANNED_ARR_TIME, m_dPlannedArrivalTimeMSecs, 0);
				pXmlChild->AddChild(PLANNED_ARR_RW, m_szPlannedArrRunway);
				pXmlChild->AddChild(PLANNED_ARR_GATE, m_szPlannedArrGate);
				pXmlChild->AddChild(CRUISE_SPEED, m_dbCruiseSpeedKts);
				pXmlChild->AddChild(CRUISE_ALT, m_dCruiseAltitudeFeet);
				pXmlChild->AddChild(ROUTE_PLAN, m_szRoute);
				pXmlChild->AddChild(DEPT_CENTER, m_szDepartureCenter);
				pXmlChild->AddChild(ARR_CENTER, m_szArrivalCenter);
				pXmlChild->AddChild(DEPT_FIX, m_szDepartureFix);
				pXmlChild->AddChild(ARR_FIX, m_szArrivalFix);
				pXmlChild->AddChild(PHYS_AC_CLASS, m_szPhysicalClass);
				pXmlChild->AddChild(WEIGHT_AC_CLSS, m_szWeightClass);
				pXmlChild->AddChild(USER_AC_CLASS, m_szUserClass);
				pXmlChild->AddChild(NUM_AC, m_iNumAircraft);
				pXmlChild->AddChild(AC_EQUIP_QUAL, m_szEquipmentQualifier);
				
				pXmlBlock->AddChild(pXmlChild); // add <flightPlanData> subblock to <dataBlock> member

				break;

		}// switch(eXmlBlock)
	
	}// if((pXmlBlock = CMLRequestParams::GetXmlElement(eXmlBlock)) != NULL)

	return pXmlBlock;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the values of the class members that belong to the
//!				specified data group
//!
//!	Parameters:	\li pPropValues - the element that contains the values
//!
//!	Returns:	true if processed
//------------------------------------------------------------------------------
bool CMLFlightPlan::SetMemberValues(CMLPropMember* pPropValues)
{
	CMLPropMember*	pChild = NULL;
	bool			bProcessed = false;

	// Is this the <flightPlanData> group?
	if(lstrcmpi(ML_XML_DATA_BLOCK_FLIGHT_PLAN, pPropValues->GetName()) == 0)
	{
		// Process all the child members
		pChild = pPropValues->GetFirstChild();
		while(pChild != NULL)
		{
			if(lstrcmpi(FP_SOURCE, pChild->GetName()) == 0)
			{
				lstrcpy(m_szSource, pChild->GetValue());
			}
			else if(lstrcmpi(AC_TYPE, pChild->GetName()) == 0)
			{
				lstrcpy(m_szAircraftType, pChild->GetValue());
			}
			else if(lstrcmpi(PLANNED_DEPT_TIME, pChild->GetName()) == 0)
			{
				m_dPlannedDepartureTimeMSecs = pChild->AsDouble();
			} 
			else if(lstrcmpi(PLANNED_DEPT_RW, pChild->GetName()) == 0)
			{
				lstrcpy(m_szPlannedDeptRunway, pChild->GetValue());
			}
			else if(lstrcmpi(PLANNED_DEPT_GATE, pChild->GetName()) == 0)
			{
				lstrcpy(m_szPlannedDeptGate, pChild->GetValue());
			}
			else if(lstrcmpi(PLANNED_ARR_TIME, pChild->GetName()) == 0)
			{
				m_dPlannedArrivalTimeMSecs = pChild->AsDouble();
			} 
			else if(lstrcmpi(PLANNED_ARR_RW, pChild->GetName()) == 0)
			{
				lstrcpy(m_szPlannedArrRunway, pChild->GetValue());
			}
			else if(lstrcmpi(PLANNED_ARR_GATE, pChild->GetName()) == 0)
			{
				lstrcpy(m_szPlannedArrGate, pChild->GetValue());
			}
			else if(lstrcmpi(PLANNED_DEPT_GATE, pChild->GetName()) == 0)
			{
				lstrcpy(m_szPlannedDeptGate, pChild->GetValue());
			}
			else if(lstrcmpi(CRUISE_SPEED, pChild->GetName()) == 0)
			{
				m_dbCruiseSpeedKts = pChild->AsLong();
			} 
			else if(lstrcmpi(CRUISE_ALT, pChild->GetName()) == 0)
			{
				m_dCruiseAltitudeFeet = pChild->AsLong();
			} 
			else if(lstrcmpi(ROUTE_PLAN, pChild->GetName()) == 0)
			{
				lstrcpy(m_szRoute, pChild->GetValue());
			} 
			else if(lstrcmpi(DEPT_CENTER, pChild->GetName()) == 0)
			{
				lstrcpy(m_szDepartureCenter, pChild->GetValue());
			} 
			else if(lstrcmpi(ARR_CENTER, pChild->GetName()) == 0)
			{
				lstrcpy(m_szArrivalCenter, pChild->GetValue());
			}
			else if(lstrcmpi(DEPT_FIX, pChild->GetName()) == 0)
			{
				lstrcpy(m_szDepartureFix, pChild->GetValue());
			}
			else if(lstrcmpi(ARR_FIX, pChild->GetName()) == 0)
			{
				lstrcpy(m_szArrivalFix, pChild->GetValue());
			}
			else if(lstrcmpi(PHYS_AC_CLASS, pChild->GetName()) == 0)
			{
				lstrcpy(m_szPhysicalClass, pChild->GetValue());
			}
			else if(lstrcmpi(WEIGHT_AC_CLSS, pChild->GetName()) == 0)
			{
				lstrcpy(m_szWeightClass, pChild->GetValue());
			}
			else if(lstrcmpi(USER_AC_CLASS, pChild->GetName()) == 0)
			{
				lstrcpy(m_szUserClass, pChild->GetValue());
			}
			else if(lstrcmpi(NUM_AC, pChild->GetName()) == 0)
			{
				m_iNumAircraft = pChild->AsInteger();
			}
			else if(lstrcmpi(AC_EQUIP_QUAL, pChild->GetName()) == 0)
			{
				lstrcpy(m_szEquipmentQualifier, pChild->GetValue());
			}
			
			pChild = pPropValues->GetNextChild();
		
		}// while(pChild != NULL)
		
		bProcessed = true;
		
	}
	else
	{
		//	Let the base class handle it
		bProcessed = CMLDataObject::SetMemberValues(pPropValues);
	}

	return bProcessed;
}


//------------------------------------------------------------------------------
//!	Summary:	Called to get a list of attributes associated with this class
//!
//!	Parameters:	None
//!
//!	Returns:	The a list of attributes associated with this class
//------------------------------------------------------------------------------
//CMLPtrList& CMLFlightPlan::GetAttributes()
//{
//	return m_apAttributes;
//}
//
////------------------------------------------------------------------------------
////!	Summary:	Called to get the list of attributes associated with this object
////!
////!	Parameters:	None
////!
////!	Returns:	The list of attributes associated with this object
////------------------------------------------------------------------------------
//const CMLPtrList& CMLFlightPlan::GetAttributes() const
//{	
//	return m_apAttributes;
//}
//
////------------------------------------------------------------------------------
////!	Summary:	Called to get the first attribute in the list
////!
////!	Parameters:	None
////!
////!	Returns:	The first attribute in the local collection
////------------------------------------------------------------------------------
//const char* CMLFlightPlan::GetFirstAttribute()
//{
//	CMLString* pString = NULL;
//
//	m_posAttributes = m_apAttributes.GetHeadPosition();
//	if(m_posAttributes != NULL)
//	{
//		pString = (CMLString*)(m_apAttributes.GetNext(m_posAttributes));
//	}
//	
//	if(pString != NULL)
//		return *pString;
//	else
//		return NULL;
//}
//
////------------------------------------------------------------------------------
////!	Summary:	Called to get the next attribute in the list
////!
////!	Parameters:	None
////!
////!	Returns:	The next FEM object in the local collection
////------------------------------------------------------------------------------
//const char* CMLFlightPlan::GetNextAttribute()
//{
//	CMLString* pString = NULL;
//
//	if(m_posAttributes != NULL)
//	{
//		pString = (CMLString*)(m_apAttributes.GetNext(m_posAttributes));
//	}
//	
//	if(pString != NULL)
//		return *pString;
//	else
//		return NULL;
//}
//
////------------------------------------------------------------------------------
////!	Summary:	Called to add an attribute
////!
////!	Parameters:	\li rAddAttr - the attribute to be added
////!
////!	Returns:	The attribute added to the list
////------------------------------------------------------------------------------
//const char* CMLFlightPlan::AddAttribute(const char* pszAttribute)
//{
//	CMLString* pString = NULL;
//
//	if((pszAttribute != NULL) && (lstrlen(pszAttribute) > 0))
//	{
//		pString = new CMLString();
//		*pString = pszAttribute;
//	
//		m_apAttributes.AddTail(pString);
//	}
//
//	return *pString;
//}