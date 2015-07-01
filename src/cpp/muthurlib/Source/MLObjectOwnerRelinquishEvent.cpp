//------------------------------------------------------------------------------
/*! \file	MLObjectOwnerRelinquishEvent.cpp
//
//  Contains the implementation of the CMLObjectOwnerRelinquishEvent class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>02-28-2013 <td>1.00		<td>Original Release <td>REB	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLObjectOwnerRelinquishEvent.h>
#include <MLAmbassador.h>
#include <MLHelper.h>
#include <Reporter.h>

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
CMLObjectOwnerRelinquishEvent::CMLObjectOwnerRelinquishEvent()
{
	memset(m_szDataObjectUUID, 0, sizeof(m_szDataObjectUUID));
}

//------------------------------------------------------------------------------
//!	Summary:	Copy constructor
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLObjectOwnerRelinquishEvent::CMLObjectOwnerRelinquishEvent(const CMLObjectOwnerRelinquishEvent& rSource)
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
CMLObjectOwnerRelinquishEvent::~CMLObjectOwnerRelinquishEvent()
{	
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLObjectOwnerRelinquishEvent::Copy(const CMLObjectOwnerRelinquishEvent& rSource)
{
	lstrcpy(m_szDataObjectUUID, rSource.m_szDataObjectUUID);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the UUID assigned to this object
//!
//!	Parameters:	None
//!
//!	Returns:	The UUID assigned to the object
//------------------------------------------------------------------------------
char* CMLObjectOwnerRelinquishEvent::GetDataObjectUUID()
{
	return m_szDataObjectUUID;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the unique identifier for this object
//!
//!	Parameters:	\li pszUUID - the unique ID assigned to this object
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLObjectOwnerRelinquishEvent::SetDataObjectUUID(char* pszUUID)
{
	if(pszUUID != NULL)
		lstrcpyn(m_szDataObjectUUID, pszUUID, sizeof(m_szDataObjectUUID));
	else
		memset(m_szDataObjectUUID, 0, sizeof(m_szDataObjectUUID));
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
CMLPropMember* CMLObjectOwnerRelinquishEvent::GetXmlBlock(EMLXMLBlock eXmlBlock)
{
	CMLPropMember*  pXmlBlock = NULL;
	char			szXmlValue[256];

	switch(eXmlBlock)
	{
	case ML_XML_BLOCK_CONTROL:			
		break;

	case ML_XML_BLOCK_DATA:

		pXmlBlock = new CMLPropMember(ML_XML_BLOCK_NAME_DATA, "");
		pXmlBlock->AddChild("dataObjectUUID", m_szDataObjectUUID);	

		break;

	case ML_XML_BLOCK_ERROR:

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
//!	Summary:	Called to get the value of the XML name attribute for this
//!				event type.
//!
//!	Parameters:	\li pszName - the buffer in which to store the name
//!				\li iSize - the size of the specified buffer in bytes
//!
//!	Returns:	The buffer specified by the caller
//------------------------------------------------------------------------------
char* CMLObjectOwnerRelinquishEvent::GetXMLName(char* pszName, int iSize)
{
	assert(pszName != NULL);
	assert(iSize > 1);

	lstrcpyn(pszName, "ObjectOwnershipRelinquishedEvent", iSize);
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
CMLPropMember* CMLObjectOwnerRelinquishEvent::GetXmlRoot()
{
	CMLPropMember*  pXmlRoot = NULL;

	pXmlRoot = new CMLPropMember("event");
	pXmlRoot->AddAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
	pXmlRoot->AddAttribute("type", "System");

	return pXmlRoot;
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded assignment operator
//!
//!	Parameters:	\li rSource - the object being assigned
//!
//!	Returns:	this object
//------------------------------------------------------------------------------
const CMLObjectOwnerRelinquishEvent& CMLObjectOwnerRelinquishEvent::operator = (const CMLObjectOwnerRelinquishEvent& rSource)
{
	Copy(rSource);
	lstrcpy(m_szDataObjectUUID, rSource.m_szDataObjectUUID);
	return *this;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the value of a class member using the specified
//!				XML element
//!
//!	Parameters:	\li eXmlBlock - the enumerated block identifier
//!				\li pMLPropMember - the element that contains the value
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
bool CMLObjectOwnerRelinquishEvent::SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember)
{
	CMLPropMember*	pObjProp = NULL;
	CMLPropMember*	pTypeProp = NULL;
	bool			bSuccessful = true;
	char			szXmlValue[256];

	switch(eXmlBlock)
	{
	case ML_XML_BLOCK_CONTROL:

		break;

	case ML_XML_BLOCK_DATA:
		if(lstrcmpi("dataObjectUUID", pMLPropMember->GetName()) == 0)
		{	
			lstrcpyn(m_szDataObjectUUID, pMLPropMember->GetValue(), sizeof(m_szDataObjectUUID));
		}			

		break;

	case ML_XML_BLOCK_ERROR:

		break;

	default:

		break;

	}// switch(eXmlBlock)

	return bSuccessful;
}
