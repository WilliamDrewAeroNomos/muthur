//------------------------------------------------------------------------------
/*! \file	MLCreateObjectResponse.cpp
//
//  Contains the implementation of the CMLCreateObjectResponse class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>02-25-2012 <td>1.00		<td>Original Release <td>REB	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLCreateObjectResponse.h>
#include <Reporter.h>
//-----------------------------------------------------------------------------
//	DEFINES
//-----------------------------------------------------------------------------

//-----------------------------------------------------------------------------
//	GLOBALS
//-----------------------------------------------------------------------------
extern CReporter		_theReporter;	//!< The global diagnostics / error reporter
//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLCreateObjectResponse::CMLCreateObjectResponse() : CMLEvent()
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
CMLCreateObjectResponse::CMLCreateObjectResponse(const CMLCreateObjectResponse& rSource)
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
CMLCreateObjectResponse::~CMLCreateObjectResponse()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLCreateObjectResponse::Copy(const CMLCreateObjectResponse& rSource)
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
const CMLCreateObjectResponse& CMLCreateObjectResponse::operator = (const CMLCreateObjectResponse& 
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
char* CMLCreateObjectResponse::GetDataObjectUUID()
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
void CMLCreateObjectResponse::SetDataObjectUUID(char* pszUUID)
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
CMLPropMember* CMLCreateObjectResponse::GetXmlBlock(EMLXMLBlock eXmlBlock)
{
	CMLPropMember* pXmlBlock = NULL;
	CMLPropMember* pXmlClasses = NULL;
	//	Use the base class to allocate the object
	_theReporter.Debug("CMLCreateObjectResponse", "GetXmlBlock", "debug1");
	if((pXmlBlock = CMLEvent::GetXmlBlock(eXmlBlock)) != NULL)
	{
		_theReporter.Debug("CMLCreateObjectResponse", "GetXmlBlock", "debug2");
		switch(eXmlBlock)
		{
		case ML_XML_BLOCK_CONTROL:

			pXmlClasses = new CMLPropMember("federationExecutionHandle", m_fedExecHandle.GetMuthurId());
			pXmlBlock->AddChild(pXmlClasses);
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
bool CMLCreateObjectResponse::SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember)
{
	bool				bSuccessful = true;

	char* temp = pMLPropMember->GetName();
	char* temp1 = pMLPropMember->GetValue();

	switch(eXmlBlock)
	{
		_theReporter.Debug("CMLCreateObjectResponse", "SetXmlValue", "debug1");
	case ML_XML_BLOCK_CONTROL:
		
		if(lstrcmpi("federationExecutionHandle", pMLPropMember->GetName()) == 0)
		{
			_theReporter.Debug("CMLCreateObjectResponse", "SetXmlValue", "debug2");
			m_fedExecHandle.SetMuthurId(pMLPropMember->GetValue());
		}
		else
		{
			_theReporter.Debug("CMLCreateObjectResponse", "SetXmlValue", "debug3");
			bSuccessful = CMLEvent::SetXmlValue(eXmlBlock, pMLPropMember);
		}
		break;
	case ML_XML_BLOCK_DATA:
		if(lstrcmpi("dataObjectUUID", pMLPropMember->GetName()) == 0)
		{	
			_theReporter.Debug("CMLCreateObjectResponse", "SetXmlValue", "debug4");
			lstrcpyn(m_szDataObjectUUID, pMLPropMember->GetValue(), sizeof(m_szDataObjectUUID));
		}		
		else
		{
			_theReporter.Debug("CMLCreateObjectResponse", "SetXmlValue", "debug5");
			bSuccessful = CMLEvent::SetXmlValue(eXmlBlock, pMLPropMember);
		}
		break;
	default:
		_theReporter.Debug("CMLCreateObjectResponse", "SetXmlValue", "debug6");
		bSuccessful = CMLEvent::SetXmlValue(eXmlBlock, pMLPropMember); // base class handles these
		break;

	}// switch(eXmlBlock)

	return bSuccessful;
}