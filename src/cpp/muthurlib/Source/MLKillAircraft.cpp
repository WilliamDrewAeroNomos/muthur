//------------------------------------------------------------------------------
/*! \file	MLKillAircraft.cpp
//
//  Contains the implementation of the CMLKillAircraft class
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
//#include "MLKillAircraft.h"
#include "Reporter.h"
extern CReporter _theReporter;
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
CMLKillAircraft::CMLKillAircraft()
{
	//	Assign defaults
	CMLDataObject::Reset();
	Reset();
}

//------------------------------------------------------------------------------
//!	Summary:	Copy constructor
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLKillAircraft::CMLKillAircraft(const CMLKillAircraft& rSource)
{
	CMLDataObject::Copy(rSource);
	Copy(rSource);
}

//------------------------------------------------------------------------------
//!	Summary:	Destructor
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLKillAircraft::~CMLKillAircraft()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLKillAircraft::Copy(const CMLKillAircraft& rSource)
{	
	CMLDataObject::Copy(rSource);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to reset the class members to their default values
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLKillAircraft::Reset()
{
	CMLDataObject::Reset();
}

//------------------------------------------------------------------------------
//!	Summary:	Called to add a class member property to the list of properties to be serialized
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLKillAircraft::BuildStandardPropertyList()
{
	//CMLDataObject::BuildStandardPropertyList();

	//// Create a property for class member m_ulCreatedTimestampMSecs and add it to the list
	//CMLProperty *cmlProperty = new CMLProperty();

	//assert(cmlProperty != NULL); // Should never happen; if it does something is seriously wrong!!

	//char buffer[2];
	//_itoa_s(m_iDataObjectTypeMemberId, buffer, sizeof(buffer), 10);
	//cmlProperty->SetMemberId(buffer);
	//cmlProperty->SetAttributeName(OBJECT_DATA_TYPE_ATTR_NAME);
	//_itoa_s(ML_DATAOBJECT_CLASS_KILL_AC, buffer, sizeof(buffer), 10);
	//cmlProperty->SetAttributeValue(buffer);
	//cmlProperty->SetAttributeType(((char*)typeid(ML_DATAOBJECT_CLASS_KILL_AC).name()));
	//AddStandardPropertyToList(cmlProperty);

	//if(cmlProperty) 
	//{
	//	delete cmlProperty;
	//	cmlProperty = NULL;
	//}
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
CMLPropMember* CMLKillAircraft::GetXmlBlock(EMLXMLBlock eXmlBlock)
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
		}// switch(eXmlBlock)
	
	}// if((pXmlBlock = CMLAircraftTaxiOut::GetXmlElement(eXmlBlock)) != NULL)

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
bool CMLKillAircraft::SetMemberValues(CMLPropMember* pPropValues)
{
	CMLPropMember*	pChild = NULL;
	bool			bProcessed = false;

	//	Let the base class handle it
	_theReporter.Debug("CMLKillAircraft", "SetMemberValues", "Debug1");
	bProcessed = CMLDataObject::SetMemberValues(pPropValues);
	

	return bProcessed;
}

