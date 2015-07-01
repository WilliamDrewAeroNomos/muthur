//------------------------------------------------------------------------------
/*! \file	MLStartFederationResponse.cpp
//
//  Contains the implementation of the CMLStartFederationResponse class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>12-11-2011 <td>1.00		<td>Original Release <td>REB	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLStartFederationResponse.h>

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
CMLStartFederationResponse::CMLStartFederationResponse() : CMLEvent()
{
	m_bStartFedResponseAck = false;
}

//------------------------------------------------------------------------------
//!	Summary:	Copy constructor
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLStartFederationResponse::CMLStartFederationResponse(const CMLStartFederationResponse& rSource)
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
CMLStartFederationResponse::~CMLStartFederationResponse()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLStartFederationResponse::Copy(const CMLStartFederationResponse& rSource)
{
	CMLEvent::Copy(rSource); // base class processing first
	m_bStartFedResponseAck = rSource.m_bStartFedResponseAck;
}



//------------------------------------------------------------------------------
//!	Summary:	Overloaded assignment operator
//!
//!	Parameters:	\li rSource - the object being assigned
//!
//!	Returns:	this object
//------------------------------------------------------------------------------
const CMLStartFederationResponse& CMLStartFederationResponse::operator = (const CMLStartFederationResponse& rSource)
{
	Copy(rSource);
	return *this;
}


//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve startFedResponseAck
//!
//!	Parameters:	None
//!
//!	Returns:	startFedResponseAck
//------------------------------------------------------------------------------
bool CMLStartFederationResponse::GetStartFedResponseAck()
{
	return m_bStartFedResponseAck;
}
//------------------------------------------------------------------------------
//!	Summary:	Called to set startFedResponseAck
//!
//!	Parameters:	None
//!
//!	Returns:	startFedResponseAck
//------------------------------------------------------------------------------
void CMLStartFederationResponse::SetStartFedResponseAck(bool b)
{
	m_bStartFedResponseAck = b;
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
CMLPropMember* CMLStartFederationResponse::GetXmlBlock(EMLXMLBlock eXmlBlock)
{
	CMLPropMember* pXmlBlock = NULL;
	CMLPropMember* pXmlClasses = NULL;
	//	Use the base class to allocate the object
	if((pXmlBlock = CMLEvent::GetXmlBlock(eXmlBlock)) != NULL)
	{
		switch(eXmlBlock)
		{
		case ML_XML_BLOCK_CONTROL:

			pXmlClasses = new CMLPropMember("federationExecutionHandle", m_fedExecHandle.GetMuthurId());
			pXmlBlock->AddChild(pXmlClasses);
			break;

		case ML_XML_BLOCK_DATA:
			pXmlClasses = new CMLPropMember("startFederationResponseAck", m_bStartFedResponseAck);
			pXmlBlock->AddChild(pXmlClasses);


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
bool CMLStartFederationResponse::SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember)
{
	bool				bSuccessful = true;
	CMLPropMember*		pChild = NULL;

	switch(eXmlBlock)
	{
	case ML_XML_BLOCK_CONTROL:
		// No base class data in this block
		if(lstrcmpi("federationExecutionHandle", pMLPropMember->GetName()) == 0)
		{
			m_fedExecHandle.SetMuthurId(pMLPropMember->GetValue());
		}
		else
		{
			bSuccessful = CMLEvent::SetXmlValue(eXmlBlock, pMLPropMember);
		}
		break;
	case ML_XML_BLOCK_DATA:
		if(lstrcmpi(pMLPropMember->GetName(), "startFederationResponseAck") == 0)
		{
			m_bStartFedResponseAck = pMLPropMember->AsBool();			
		}

		break;
	default:

		bSuccessful = CMLEvent::SetXmlValue(eXmlBlock, pMLPropMember); // base class handles these
		break;

	}// switch(eXmlBlock)

	return bSuccessful;
}
