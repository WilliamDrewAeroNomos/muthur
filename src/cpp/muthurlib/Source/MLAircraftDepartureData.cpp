//------------------------------------------------------------------------------
/*! \file	MLAircraftDepartureData.cpp
//
//  Contains the implementation of the CMLAircraftDepartureData class
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
#include "MLAircraftDepartureData.h"
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
CMLAircraftDepartureData::CMLAircraftDepartureData()
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
CMLAircraftDepartureData::CMLAircraftDepartureData(const CMLAircraftDepartureData& rSource)
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
CMLAircraftDepartureData::~CMLAircraftDepartureData()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAircraftDepartureData::Copy(const CMLAircraftDepartureData& rSource)
{	
	PTRNODE				Pos = NULL;
	CMLString	*pAttribute = NULL;

	CMLDataObject::Copy(rSource);

	m_dActualDepartureTimeMSecs = rSource.m_dActualDepartureTimeMSecs;	
	lstrcpy(m_szAssignedRunway, rSource.m_szAssignedRunway); 
	lstrcpy(m_szDeptAirportCode, rSource.m_szDeptAirportCode);

	////	Copy all the attributes
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
void CMLAircraftDepartureData::Reset()
{		
	CMLDataObject::Reset();
	m_dActualDepartureTimeMSecs = 0;
	memset(m_szAssignedRunway, 0, sizeof(m_szAssignedRunway));
	memset(m_szDeptAirportCode, 0, sizeof(m_szDeptAirportCode));

	/*AddAttribute(ACTUAL_DEPT_TIME);
	AddAttribute(DEPT_RUNWAY);
	AddAttribute(DEPT_AIRPORT);*/
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the time in milliseconds when the aircraft actually departed the airport
//!
//!	Parameters:	None
//!
//!	Returns:	The time in milliseconds when the aircraft actually departed the airport
//------------------------------------------------------------------------------
double CMLAircraftDepartureData::GetActualDepartureTimeMSecs() { return m_dActualDepartureTimeMSecs; }

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the assigned runway
//!
//!	Parameters:	None
//!
//!	Returns:	The assigned runway
//------------------------------------------------------------------------------
char* CMLAircraftDepartureData::GetAssignedRunway() { return m_szAssignedRunway; }

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the airport from which the aircraft is departing
//!
//!	Parameters:	None
//!
//!	Returns:	The airport from which the aircraft is departing
//------------------------------------------------------------------------------
char* CMLAircraftDepartureData::GetDeptAirportCode() { return m_szDeptAirportCode; }

//------------------------------------------------------------------------------
//!	Summary:	Called to set the time in milliseconds when the aircraft actually departed the airport
//!
//!	Parameters:	\li l - time in milliseconds when the aircraft actually departed the airport
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAircraftDepartureData::SetActualDepartureTimeMSecs(double d)		
{
	m_dActualDepartureTimeMSecs = d;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the gate where the aircraft is assigned to park
//!
//!	Parameters:	\li p - the gate where the aircraft is assigned to park
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAircraftDepartureData::SetAssignedRunway(char* p)			
{
	if(p != NULL)
		lstrcpyn(m_szAssignedRunway, p, sizeof(m_szAssignedRunway));
	else
		memset(m_szAssignedRunway, 0, sizeof(m_szAssignedRunway));
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the airport from which the aircraft is departing
//!
//!	Parameters:	\li p - airport from which the aircraft is departing
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLAircraftDepartureData::SetDeptAirportCode(char* p)		
{
	if(p != NULL)
		lstrcpyn(m_szDeptAirportCode, p, sizeof(m_szDeptAirportCode));
	else
		memset(m_szDeptAirportCode, 0, sizeof(m_szDeptAirportCode));
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
CMLPropMember* CMLAircraftDepartureData::GetXmlBlock(EMLXMLBlock eXmlBlock)
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

				//	Create a child for the a/c departure data
				pXmlChild = new CMLPropMember(ML_XML_DATA_BLOCK_AC_DEPT_DATA, "");
				pXmlChild->AddChild(ACTUAL_DEPT_TIME, m_dActualDepartureTimeMSecs, 0);
				pXmlChild->AddChild(DEPT_RUNWAY, m_szAssignedRunway);
				pXmlChild->AddChild(DEPT_AIRPORT, m_szDeptAirportCode);
				pXmlBlock->AddChild(pXmlChild); // add <actualDepartureTime> subblock
				
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
bool CMLAircraftDepartureData::SetMemberValues(CMLPropMember* pPropValues)
{
	CMLPropMember*	pChild = NULL;
	bool			bProcessed = false;

	// Is this the <aircraftDeptData> group?
	if(lstrcmpi(ML_XML_DATA_BLOCK_AC_DEPT_DATA, pPropValues->GetName()) == 0)
	{
		// Process all the child members
		pChild = pPropValues->GetFirstChild();
		while(pChild != NULL)
		{
			if(lstrcmpi(ACTUAL_DEPT_TIME, pChild->GetName()) == 0)
			{
				m_dActualDepartureTimeMSecs = pChild->AsDouble();
			}
			else if(lstrcmpi(DEPT_RUNWAY, pChild->GetName()) == 0)
			{
				lstrcpy(m_szAssignedRunway, pChild->GetValue());
			}	
			else if(lstrcmpi(DEPT_AIRPORT, pChild->GetName()) == 0)
			{
				lstrcpyn(m_szDeptAirportCode, pChild->GetValue(), sizeof(m_szDeptAirportCode));
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
//CMLPtrList& CMLAircraftDepartureData::GetAttributes()
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
//const CMLPtrList& CMLAircraftDepartureData::GetAttributes() const
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
//const char* CMLAircraftDepartureData::GetFirstAttribute()
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
//const char* CMLAircraftDepartureData::GetNextAttribute()
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
//const char* CMLAircraftDepartureData::AddAttribute(const char* pszAttribute)
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