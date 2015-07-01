//------------------------------------------------------------------------------
/*! \file	MLDataObject.cpp
//
//  Contains the implementation of the CMLDataObject class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>05-22-2011 <td>1.00		<td>Original Release <td>KRM	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLDataObject.h>
#include <MLAmbassador.h>
#include <MLHelper.h>
#include <Reporter.h>
#include <MLFlightPosition.h>
#include "MLAircraftData.h"
#include "MLAircraftArrivalData.h"
#include "MLAircraftDepartureData.h"
#include "MLFlightPlanData.h"
//#include "MLKillAircraft.h"
#include "MLAircraftTaxiOut.h"
#include "MLAircraftTaxiIn.h"

#include "tinyxml.h"
#include "tinystr.h"

//-----------------------------------------------------------------------------
//	DEFINES
//-----------------------------------------------------------------------------

//-----------------------------------------------------------------------------
//	GLOBALS
//-----------------------------------------------------------------------------
extern CMLAmbassador	_theAmbassador;	//!< The singleton Ambassador instance
extern CReporter		_theReporter;	//!< The global diagnostics / error reporter

//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLDataObject::CMLDataObject()
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
CMLDataObject::CMLDataObject(const CMLDataObject& rSource)
{
	Copy(rSource);
}

//------------------------------------------------------------------------------
//!	Summary:	Destructor
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLDataObject::~CMLDataObject()
{
	//m_apAttributes.RemoveAll(TRUE);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLDataObject::Copy(const CMLDataObject& rSource)
{	
	PTRNODE				Pos = NULL;
	CMLString*			pAttribute = NULL;

	m_dObjectCreateTimeMSecs = rSource.m_dObjectCreateTimeMSecs ;
	lstrcpy(m_szDataObjectUUID, rSource.m_szDataObjectUUID);
	lstrcpy(m_szACTailNumber, rSource.m_szACTailNumber);
	lstrcpy(m_szACId, rSource.m_szACId);

	//	Copy all the attributes
	/*m_apAttributes.RemoveAll(TRUE);
	const CMLPtrList& rAttributes = rSource.m_apAttributes;
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
//!	Summary:	Called to retrieve the aircraft's identifier
//!
//!	Parameters:	None
//!
//!	Returns:	The ID assigned to the aircraft
//------------------------------------------------------------------------------
char* CMLDataObject::GetACId()
{
	return m_szACId;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the aircraft's tail number
//!
//!	Parameters:	None
//!
//!	Returns:	The tail number assigned to the aircraft
//------------------------------------------------------------------------------
char* CMLDataObject::GetACTailNumber()
{
	return m_szACTailNumber;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the airport from which the aircraft is departing
//!
//!	Parameters:	None
//!
//!	Returns:	The airport from which the aircraft is departing
//------------------------------------------------------------------------------
//char* CMLDataObject::GetDeptAirportCode() { return m_szDeptAirportCode; }
//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the destination airport for the aircraft when it completes its flight plan
//!
//!	Parameters:	None
//!
//!	Returns:	The destination airport for the aircraft when it completes its flight plan
//------------------------------------------------------------------------------
//char* CMLDataObject::GetArrivalAirportCode() { return m_szArrivalAirportCode; }



//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the time when the object was created
//!
//!	Parameters:	None
//!
//!	Returns:	The time in milliseconds
//------------------------------------------------------------------------------
double CMLDataObject::GetObjectCreatedTimestamp()
{
	return m_dObjectCreateTimeMSecs;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the UUID assigned to this object
//!
//!	Parameters:	None
//!
//!	Returns:	The UUID assigned to the object
//------------------------------------------------------------------------------
char* CMLDataObject::GetDataObjectUUID()
{
	return m_szDataObjectUUID;
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded assignment operator
//!
//!	Parameters:	\li rSource - the object being assigned
//!
//!	Returns:	this object
//------------------------------------------------------------------------------
const CMLDataObject& CMLDataObject::operator = (const CMLDataObject& rSource)
{
	Copy(rSource);
	return *this;
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
CMLPropMember* CMLDataObject::GetXmlBlock(EMLXMLBlock eXmlBlock)
{
	CMLPropMember*  pXmlBlock = NULL;
	CMLPropMember*	pXmlChild = NULL;
	CMLPropMember*  pACID = NULL;

	switch(eXmlBlock)
	{
	case ML_XML_BLOCK_CONTROL:

		pXmlBlock = new CMLPropMember(ML_XML_BLOCK_NAME_CONTROL, "");

		
		
		break;

	case ML_XML_BLOCK_DATA:

		pXmlBlock = new CMLPropMember(ML_XML_BLOCK_NAME_DATA, "");
		pXmlBlock->AddChild(CREATED_TIMESTAMP_MSECS_ATTRIB_NAME, m_dObjectCreateTimeMSecs, 0);
		pXmlBlock->AddChild(UUID_ATTR_NAME, m_szDataObjectUUID);
		//	Create a child for the aircraft ID data and add it to the base class element
		pXmlChild = new CMLPropMember(ML_XML_DATA_BLOCK_AIRCRAFT_ID, "");
		pXmlChild->AddChild(AC_ID_ATTR_NAME, m_szACId);
		pXmlChild->AddChild(AC_TAIL_NUMBER_ATTR_NAME, m_szACTailNumber);
		pXmlBlock->AddChild(pXmlChild); // add <aircraftID> subblock

		break;

	case ML_XML_BLOCK_ERROR:

		// No error blocks in data objects
		break;

	default:

		_theReporter.Debug("", "GetXmlBlock", "Invalid XML Block Enumeration: #%d", eXmlBlock);
		if(pXmlBlock != NULL)
		{
			delete pXmlBlock;
			pXmlBlock = NULL;
		}
		break;

	}// switch(eXmlBlock)

	return pXmlBlock;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the value of the XML enum attribute for this
//!				data object type string.
//!
//!	Parameters:	\li pszName - the type as a string
//!
//!	Returns:	The associated data object (returns null if type not found)
//------------------------------------------------------------------------------
CMLDataObject* CMLDataObject::GetXMLTypeAsObject(char* pszType)
{
	if(pszType)
	{
		if(lstrcmpi("FlightPosition", pszType) == 0)
		{
			return new CMLFlightPosition();
		}
		else if(lstrcmpi("Aircraft", pszType) == 0)
		{
			return new CMLAircraft();
		}
		else if(lstrcmpi("AircraftArrival", pszType) == 0)
		{
			return new CMLAircraftArrivalData();
		}
		else if(lstrcmpi("FlightPlan", pszType) == 0)
		{
			return new CMLFlightPlan();
		}		
		else if(lstrcmpi("AircraftTaxiOut", pszType) == 0)
		{
			return new CMLAircraftTaxiOut();
		}
		else if(lstrcmpi("AircraftTaxiIn", pszType) == 0)
		{
			return new CMLAircraftTaxiIn();
		}
		else if(lstrcmpi("AircraftDeparture", pszType) == 0)
		{
			return new CMLAircraftDepartureData();
		}
		else
		{
			return NULL;
		}
	}
	else
	{
		return NULL;
	}

}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the enumerated type identifier for the class
//!				with the specified name
//!
//!	Parameters:	\li pszName - the class name
//!				\li bIsCPP - true if C++ name, false if XML name
//!
//!	Returns:	The associated data type if found
//------------------------------------------------------------------------------
EMLDataObjectClass CMLDataObject::GetTypeFromName(char* pszName, bool bIsCPP)
{
	char szName[128];

	//	We're going to use the C++ class name
	if(bIsCPP == true)
		lstrcpyn(szName, pszName, sizeof(szName));
	else
		lstrcpyn(szName, CMLHelper::GetCPPClassName(pszName), sizeof(szName));

	if(lstrcmpi("CMLFlightPosition", szName) == 0)
	{
		return ML_DATAOBJECT_CLASS_FL_POSITION;
	}
	else if(lstrcmpi("CMLAircraft", szName) == 0)
	{
		return ML_DATAOBJECT_CLASS_AIRCRAFT;
	}
	else if(lstrcmpi("CMLAircraftArrivalData", szName) == 0)
	{
		return ML_DATAOBJECT_CLASS_AC_ARRIVAL;
	}
	else if(lstrcmpi("CMLFlightPlan", szName) == 0)
	{
		return ML_DATAOBJECT_CLASS_FP_FILED;
	}
	else if(lstrcmpi("CMLAircraftTaxiOut", szName) == 0)
	{
		return ML_DATAOBJECT_CLASS_AC_TAXI_OUT;
	}
	else if(lstrcmpi("CMLAircraftTaxiIn", szName) == 0)
	{
		return ML_DATAOBJECT_CLASS_AC_TAXI_IN;
	}
	else if(lstrcmpi("CMLAircraftDepartureData", szName) == 0)
	{
		return ML_DATAOBJECT_CLASS_AC_DEPT;
	}
	else
	{
		return ML_DATAOBJECT_CLASS_BASE;
	}
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the value of the XML name attribute for this
//!				data object type.
//!
//!	Parameters:	\li pszName - the buffer in which to store the name
//!				\li iSize - the size of the specified buffer in bytes
//!
//!	Returns:	The buffer specified by the caller
//------------------------------------------------------------------------------
char* CMLDataObject::GetXMLName(char* pszName, int iSize)
{
	assert(pszName != NULL);
	assert(iSize > 1);

	switch(GetDataObjectType())
	{
	case ML_DATAOBJECT_CLASS_FL_POSITION:

		lstrcpyn(pszName, "FlightPosition", iSize);
		break;

	case ML_DATAOBJECT_CLASS_AIRCRAFT:

		lstrcpyn(pszName, "Aircraft", iSize);
		break;

	case ML_DATAOBJECT_CLASS_AC_ARRIVAL:

		lstrcpyn(pszName, "AircraftArrival", iSize);
		break;

	case ML_DATAOBJECT_CLASS_FP_FILED:

		lstrcpyn(pszName, "FlightPlan", iSize);
		break;

	case ML_DATAOBJECT_CLASS_AC_TAXI_OUT:

		lstrcpyn(pszName, "AircraftTaxiOut", iSize);
		break;

	case ML_DATAOBJECT_CLASS_AC_TAXI_IN:

		lstrcpyn(pszName, "AircraftTaxiIn", iSize);
		break;

	case ML_DATAOBJECT_CLASS_AC_DEPT:

		lstrcpyn(pszName, "AircraftDeparture", iSize);
		break;

	default:

		assert(0);
		pszName[0] = '\0';
		break;

	}

	return pszName;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get a property member that defines the root node for
//!				an XML stream
//!
//!	Parameters:	None
//!
//!	Returns:	The member used to build the XML document's root node
//!
//!	Remarks:	The caller is responsible for deallocation of the object
//------------------------------------------------------------------------------
CMLPropMember* CMLDataObject::GetXmlRoot()
{
	CMLPropMember*  pXmlRoot = NULL;
	char			szType[256];

	pXmlRoot = new CMLPropMember("event");
	pXmlRoot->AddAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
	pXmlRoot->AddAttribute("type", GetXMLName(szType, sizeof(szType)));
	pXmlRoot->AddAttribute("name", "Data");

	return pXmlRoot;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to reset the class members to their default values
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLDataObject::Reset()
{
	
	m_dObjectCreateTimeMSecs = 0;
	
	memset(m_szDataObjectUUID, 0, sizeof(m_szDataObjectUUID));
	memset(m_szACTailNumber, 0, sizeof(m_szACTailNumber));
	memset(m_szACId, 0, sizeof(m_szACId));
	//m_posAttributes = NULL;

	// Add attributes from base class
	/*CMLString*	pAttribute1 = new CMLString(CREATED_TIMESTAMP_MSECS_ATTRIB_NAME);
	CMLString*	pAttribute2 = new CMLString(UUID_ATTR_NAME);
	CMLString*	pAttribute3 = new CMLString(AC_TAIL_NUMBER_ATTR_NAME);
	CMLString*	pAttribute4 = new CMLString(AC_ID_ATTR_NAME);*/

	/*if(pAttribute1)*/ //AddAttribute(CREATED_TIMESTAMP_MSECS_ATTRIB_NAME);
	/*if(pAttribute2)*/ //AddAttribute(UUID_ATTR_NAME);
	/*if(pAttribute3)*/// AddAttribute(AC_TAIL_NUMBER_ATTR_NAME);
	/*if(pAttribute4)*/ //AddAttribute(AC_ID_ATTR_NAME);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the ID assigned to the aircraft
//!
//!	Parameters:	\li pszACId - the aircraft identifier
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLDataObject::SetACId(char* pszACId)
{
	if(pszACId != NULL)
		lstrcpyn(m_szACId, pszACId, sizeof(m_szACId));
	else
		memset(m_szACId, 0, sizeof(m_szACId));
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the tail number assigned to the aircraft
//!
//!	Parameters:	\li pszACTailNumber - the aircraft identifier
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLDataObject::SetACTailNumber(char* pszACTailNumber)
{
	if(pszACTailNumber != NULL)
		lstrcpyn(m_szACTailNumber, pszACTailNumber, sizeof(m_szACTailNumber));
	else
		memset(m_szACTailNumber, 0, sizeof(m_szACTailNumber));
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the destination airport for the aircraft when it completes its flight plan
//!
//!	Parameters:	\li p - destination airport for the aircraft when it completes its flight plan
//!
//!	Returns:	None
//------------------------------------------------------------------------------
//void CMLDataObject::SetArrivalAirportCode(char* p)		
//{
//	if(p != NULL)
//		lstrcpyn(m_szArrivalAirportCode, p, sizeof(m_szArrivalAirportCode));
//	else
//		memset(m_szArrivalAirportCode, 0, sizeof(m_szArrivalAirportCode));
//}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the time stamp when the object was created
//!
//!	Parameters:	\li ulTimestampMSec - time stamp in milliseconds
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLDataObject::SetObjectCreatedTimestamp(double dTimestampMSecs)
{
	m_dObjectCreateTimeMSecs = dTimestampMSecs;
}




//------------------------------------------------------------------------------
//!	Summary:	Called to set the airport from which the aircraft is departing
//!
//!	Parameters:	\li p - airport from which the aircraft is departing
//!
//!	Returns:	None
//------------------------------------------------------------------------------
//void CMLDataObject::SetDeptAirportCode(char* p)		
//{
//	if(p != NULL)
//		lstrcpyn(m_szDeptAirportCode, p, sizeof(m_szDeptAirportCode));
//	else
//		memset(m_szDeptAirportCode, 0, sizeof(m_szDeptAirportCode));
//}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the unique identifier for this object
//!
//!	Parameters:	\li pszUUID - the unique ID assigned to this object
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLDataObject::SetDataObjectUUID(char* pszUUID)
{
	if(pszUUID != NULL)
		lstrcpyn(m_szDataObjectUUID, pszUUID, sizeof(m_szDataObjectUUID));
	else
		memset(m_szDataObjectUUID, 0, sizeof(m_szDataObjectUUID));
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the value of a class member using the specified
//!				XML element
//!
//!	Parameters:	\li eXmlBlock - the enumerated block identifier
//!				\li pMLPropMember - the element that contains the value
//!
//!	Returns:	true if processed
//------------------------------------------------------------------------------
bool CMLDataObject::SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember)
{
	bool			bProcessed = false;

	_theReporter.Debug("MLDataObject", "1A. SetXmlValue", "pMLPropMember name:%s", pMLPropMember->GetName());
	_theReporter.Debug("MLDataObject", "1A. SetXmlValue", "pMLPropMember value:%s", pMLPropMember->GetValue());

	switch(eXmlBlock)
	{		
	case ML_XML_BLOCK_CONTROL:

		
		
		break;

	case ML_XML_BLOCK_DATA:

		//	The children of the <dataBlock> section are individual data groups
		_theReporter.Debug("MLDataObject", "1B. SetXmlValue", "pMLPropMember:%s", pMLPropMember->GetName());
		_theReporter.Debug("MLDataObject", "1B. SetXmlValue", "pMLPropMember:%s", pMLPropMember->GetValue());
		SetMemberValues(pMLPropMember);

		bProcessed = true;

		break;

	default:

		break;

	}// switch(eXmlBlock)

	return bProcessed;
}

static CMLDataObject* createDataObject(EMLDataObjectClass dataObjectClass)
{

}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the values of the class members that belong to the
//!				specified data group
//!
//!	Parameters:	\li pPropValues - the element that contains the values
//!
//!	Returns:	true if processed
//------------------------------------------------------------------------------
bool CMLDataObject::SetMemberValues(CMLPropMember* pPropValues)
{
	CMLPropMember*	pChild2 = pPropValues;
	bool			bProcessed = false;

	// Is this the AircraftID data group?
	_theReporter.Debug("MLDataObject", "2. SetMemberValues", "pPropValues->GetName():%s", pPropValues->GetName());
	
	if(lstrcmpi(ML_XML_DATA_BLOCK_AIRCRAFT_ID, pPropValues->GetName()) == 0)
	{
		CMLPropMember* pChild = pPropValues->GetFirstChild();
		//_theReporter.Debug("MLDataObject", "SetMemberValues", "Debug1");
		// Process all the child members	
		//_theReporter.Debug("MLDataObject", "3. SetMemberValues", "pChild->GetName():%s", pChild->GetName());
		//_theReporter.Debug("MLDataObject", "3. SetMemberValues", "pChild->GetValue():%s", pChild->GetValue());
		while(pChild != NULL)
		{
			if(lstrcmpi(AC_ID_ATTR_NAME, pChild->GetName()) == 0)
			{
				lstrcpyn(m_szACId, pChild->GetValue(), sizeof(m_szACId));
			}
			else if(lstrcmpi(AC_TAIL_NUMBER_ATTR_NAME, pChild->GetName()) == 0)
			{
				lstrcpyn(m_szACTailNumber, pChild->GetValue(), sizeof(m_szACTailNumber));
			}			

			pChild = pPropValues->GetNextChild();

		}// while(pChild != NULL)

		bProcessed = true;
		// CHECK WHY THESE ARENT BEING SENT DURING AN OBJECT EVENT needs to traverse thru siblings here
	}// if(lstrcmpi(ML_XML_DATA_BLOCK_AIRCRAFT_ID, pPropValues->GetName()) == 0)
	else if(lstrcmpi(UUID_ATTR_NAME, pPropValues->GetName()) == 0)
	{	
		lstrcpyn(m_szDataObjectUUID, pPropValues->GetValue(), sizeof(m_szDataObjectUUID));
	}
	else if(lstrcmpi(CREATED_TIMESTAMP_MSECS_ATTRIB_NAME, pPropValues->GetName()) == 0)
	{	
		m_dObjectCreateTimeMSecs = pPropValues->AsDouble();
	}
	else if(lstrcmpi(ML_XML_BLOCK_NAME_CONTROL, pPropValues->GetName()) == 0)
	{
	}// if(lstrcmpi(ML_XML_DATA_BLOCK_AIRCRAFT_ID, pPropValues->GetName()) == 0)

	return bProcessed;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get a list of attributes associated with this class
//!
//!	Parameters:	None
//!
//!	Returns:	The a list of attributes associated with this class
//------------------------------------------------------------------------------
//CMLPtrList& CMLDataObject::GetAttributes()
//{
//	return m_apAttributes;
//}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the list of attributes associated with this object
//!
//!	Parameters:	None
//!
//!	Returns:	The list of attributes associated with this object
//------------------------------------------------------------------------------
//const CMLPtrList& CMLDataObject::GetAttributes() const
//{	
//	return m_apAttributes;
//}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the first attribute in the list
//!
//!	Parameters:	None
//!
//!	Returns:	The first attribute in the local collection
//------------------------------------------------------------------------------
//const char* CMLDataObject::GetFirstAttribute()
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
//const char* CMLDataObject::GetNextAttribute()
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
//const char* CMLDataObject::AddAttribute(const char* pszAttribute)
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