//------------------------------------------------------------------------------
/*! \file	MLRelinquishObjectOwnershipResponse.cpp
//
//  Contains the implementation of the CMLRelinquishObjectOwnershipResponse class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>02-28-2012 <td>1.00		<td>Original Release <td>REB	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLRelinquishObjectOwnershipResponse.h>

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
CMLRelinquishObjectOwnershipResponse::CMLRelinquishObjectOwnershipResponse() : CMLEvent()
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
CMLRelinquishObjectOwnershipResponse::CMLRelinquishObjectOwnershipResponse(const CMLRelinquishObjectOwnershipResponse& rSource)
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
CMLRelinquishObjectOwnershipResponse::~CMLRelinquishObjectOwnershipResponse()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLRelinquishObjectOwnershipResponse::Copy(const CMLRelinquishObjectOwnershipResponse& rSource)
{
	CMLEvent::Copy(rSource); // base class processing first
	lstrcpy(m_szDataObjectUUID, rSource.m_szDataObjectUUID);
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded assignment operator
//!
//!	Parameters:	\li rSource - the object being assigned
//!
//!	Returns:	this object
//------------------------------------------------------------------------------
const CMLRelinquishObjectOwnershipResponse& CMLRelinquishObjectOwnershipResponse::operator = (const CMLRelinquishObjectOwnershipResponse& 
																	rSource)
{
	Copy(rSource);
	return *this;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the UUID assigned to this object
//!
//!	Parameters:	None
//!
//!	Returns:	The UUID assigned to the object
//------------------------------------------------------------------------------
char* CMLRelinquishObjectOwnershipResponse::GetDataObjectUUID()
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
void CMLRelinquishObjectOwnershipResponse::SetDataObjectUUID(char* pszUUID)
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
CMLPropMember* CMLRelinquishObjectOwnershipResponse::GetXmlBlock(EMLXMLBlock eXmlBlock)
{
	CMLPropMember* pXmlBlock = NULL;
	CMLPropMember* pXmlClasses = NULL;
	//	Use the base class to allocate the object
	if((pXmlBlock = CMLEvent::GetXmlBlock(eXmlBlock)) != NULL)
	{
		switch(eXmlBlock)
		{
		case ML_XML_BLOCK_CONTROL:

			//pXmlClasses = new CMLPropMember("federationExecutionHandle", m_fedExecHandle.GetMuthurId());
			//pXmlBlock->AddChild(pXmlClasses);
			break;

		case ML_XML_BLOCK_DATA:

			pXmlBlock->AddChild("dataObjectUUID", m_szDataObjectUUID);			
			break;

		}// switch(eXmlBlock)

	}// if((pXmlBlock = CMLRequestParams::GetXmlElement(eXmlBlock)) != NULL)

	return pXmlBlock;
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
bool CMLRelinquishObjectOwnershipResponse::SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember)
{
	bool				bSuccessful = true;
	//CMLPropMember*		pChild = NULL;

	switch(eXmlBlock)
	{
	case ML_XML_BLOCK_CONTROL:
		// No base class data in this block
		/*if(lstrcmpi("federationExecutionHandle", pMLPropMember->GetName()) == 0)
		{
			m_fedExecHandle.SetMuthurId(pMLPropMember->GetValue());
		}
		else
		{*/
			bSuccessful = CMLEvent::SetXmlValue(eXmlBlock, pMLPropMember);
		//}
		break;
	case ML_XML_BLOCK_DATA:
		if(lstrcmpi("dataObjectUUID", pMLPropMember->GetName()) == 0)
		{	
			lstrcpyn(m_szDataObjectUUID, pMLPropMember->GetValue(), sizeof(m_szDataObjectUUID));
		}		
		else
		{
			bSuccessful = CMLEvent::SetXmlValue(eXmlBlock, pMLPropMember);
		}
		break;
	default:

		bSuccessful = CMLEvent::SetXmlValue(eXmlBlock, pMLPropMember); // base class handles these
		break;

	}// switch(eXmlBlock)

	return bSuccessful;
}