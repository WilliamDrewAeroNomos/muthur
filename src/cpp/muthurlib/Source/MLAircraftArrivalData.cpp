//------------------------------------------------------------------------------
/*! \file	MLAircraftArrivalData.cpp
//
//  Contains the implementation of the CMLAircraftArrivalData class
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
#include "MLAircraftArrivalData.h"
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
CMLAircraftArrivalData::CMLAircraftArrivalData()
{
	//	Assign defaults
	Reset();
}

//------------------------------------------------------------------------------
//!	Summary:	Copy constructor
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLAircraftArrivalData::CMLAircraftArrivalData(const CMLAircraftArrivalData& rSource)
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
CMLAircraftArrivalData::~CMLAircraftArrivalData()
{	
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAircraftArrivalData::Copy(const CMLAircraftArrivalData& rSource)
{	
	PTRNODE		Pos = NULL;
	CMLString*	pAttribute = NULL;

	CMLDataObject::Copy(rSource);

	m_dActualArrivalTimeMSecs = rSource.m_dActualArrivalTimeMSecs;	
	lstrcpy(m_szAssignedRunway, rSource.m_szAssignedRunway); 
	lstrcpy(m_szArrivalAirportCode, rSource.m_szArrivalAirportCode); 

	//	Copy all the attributes
	/*const CMLPtrList& rAttributes = rSource.m_apAttributes;
	Pos = rAttributes.GetHeadPosition();
	while(Pos != NULL)
	{
		if((pAttribute = (CMLString*)(rAttributes.GetNext(Pos))) != NULL)
		{
			AddAttribute(*pAttribute);
		}
	}*/
}

//------------------------------------------------------------------------------
//!	Summary:	Called to reset the class members to their default values
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAircraftArrivalData::Reset()
{		
	CMLDataObject::Reset();
	m_dActualArrivalTimeMSecs = 0;
	memset(m_szAssignedRunway, 0, sizeof(m_szAssignedRunway));
	memset(m_szArrivalAirportCode, 0, sizeof(m_szArrivalAirportCode));
	//m_posAttributes = NULL;

	/*AddAttribute(ACTUAL_ARRIVAL_TIME);
	AddAttribute(ARRIVAL_RUNWAY);
	AddAttribute(ARRIVAL_AIRPORT);*/
}		

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the time in milliseconds when the aircraft actually arrived at the airport
//!
//!	Parameters:	None
//!
//!	Returns:	The time in milliseconds when the aircraft actually arrived at the airport
//------------------------------------------------------------------------------
double CMLAircraftArrivalData::GetActualArrivalTimeMSecs() { return m_dActualArrivalTimeMSecs; }

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the assigned runway
//!
//!	Parameters:	None
//!
//!	Returns:	The assigned runway
//------------------------------------------------------------------------------
char* CMLAircraftArrivalData::GetAssignedRunway() { return m_szAssignedRunway; }

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the destination airport for the aircraft when it completes 
//! its flight plan
//!
//!	Parameters:	None
//!
//!	Returns:	The destination airport for the aircraft when it completes its flight plan
//------------------------------------------------------------------------------
char* CMLAircraftArrivalData::GetArrivalAirportCode() { return m_szArrivalAirportCode; }

//------------------------------------------------------------------------------
//!	Summary:	Called to set the time in milliseconds when the aircraft actually arrived at the airport
//!
//!	Parameters:	\li l - time in milliseconds when the aircraft actually arrived at the airport
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAircraftArrivalData::SetActualArrivalTimeMSecs(double d)		
{
	m_dActualArrivalTimeMSecs = d;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the gate where the aircraft is assigned to park
//!
//!	Parameters:	\li p - the gate where the aircraft is assigned to park
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAircraftArrivalData::SetAssignedRunway(char* p)			
{
	if(p != NULL)
		lstrcpyn(m_szAssignedRunway, p, sizeof(m_szAssignedRunway));
	else
		memset(m_szAssignedRunway, 0, sizeof(m_szAssignedRunway));
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the destination airport for the aircraft when it completes 
//!             its flight plan.
//!
//!	Parameters:	\li p - destination airport for the aircraft when it completes its flight plan
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAircraftArrivalData::SetArrivalAirportCode(char* p)		
{
	if(p != NULL)
		lstrcpyn(m_szArrivalAirportCode, p, sizeof(m_szArrivalAirportCode));
	else
		memset(m_szArrivalAirportCode, 0, sizeof(m_szArrivalAirportCode));
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
CMLPropMember* CMLAircraftArrivalData::GetXmlBlock(EMLXMLBlock eXmlBlock)
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

				//	Create a child for the a/c arrival data
				pXmlChild = new CMLPropMember(ML_XML_DATA_BLOCK_AC_ARRIVAL_DATA, "");
				pXmlChild->AddChild(ACTUAL_ARRIVAL_TIME, m_dActualArrivalTimeMSecs, 0);
				pXmlChild->AddChild(ARRIVAL_RUNWAY, m_szAssignedRunway);
				pXmlChild->AddChild(ARRIVAL_AIRPORT, m_szArrivalAirportCode);
				pXmlBlock->AddChild(pXmlChild); // add <arrivalData> subblock
				
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
bool CMLAircraftArrivalData::SetMemberValues(CMLPropMember* pPropValues)
{
	CMLPropMember*	pChild = NULL;
	bool			bProcessed = false;

	// Is this the <aircraftArrivalData> group?
	if(lstrcmpi(ML_XML_DATA_BLOCK_AC_ARRIVAL_DATA, pPropValues->GetName()) == 0)
	{
		// Process all the child members
		pChild = pPropValues->GetFirstChild();
		while(pChild != NULL)
		{
			if(lstrcmpi(ACTUAL_ARRIVAL_TIME, pChild->GetName()) == 0)
			{
				m_dActualArrivalTimeMSecs = pChild->AsDouble();
			}
			else if(lstrcmpi(ARRIVAL_RUNWAY, pChild->GetName()) == 0)
			{
				lstrcpy(m_szAssignedRunway, pChild->GetValue());
			}	
			else if(lstrcmpi(ARRIVAL_AIRPORT, pChild->GetName()) == 0)
			{
				lstrcpyn(m_szArrivalAirportCode, pChild->GetValue(), sizeof(m_szArrivalAirportCode));
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
//CMLPtrList& CMLAircraftArrivalData::GetAttributes()
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
//const CMLPtrList& CMLAircraftArrivalData::GetAttributes() const
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
//const char* CMLAircraftArrivalData::GetFirstAttribute()
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
//const char* CMLAircraftArrivalData::GetNextAttribute()
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
//const char* CMLAircraftArrivalData::AddAttribute(const char* pszAttribute)
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