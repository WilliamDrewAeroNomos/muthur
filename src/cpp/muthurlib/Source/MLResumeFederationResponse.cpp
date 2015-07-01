//------------------------------------------------------------------------------
/*! \file	MLResumeFederationResponse.cpp
//
//  Contains the implementation of the CMLResumeFederationResponse class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>12-10-2011 <td>1.00		<td>Original Release <td>REB	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLResumeFederationResponse.h>

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
CMLResumeFederationResponse::CMLResumeFederationResponse() : CMLEvent()
{
	m_bResumeFedResponseAck = false;
}

//------------------------------------------------------------------------------
//!	Summary:	Copy constructor
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLResumeFederationResponse::CMLResumeFederationResponse(const CMLResumeFederationResponse& rSource)
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
CMLResumeFederationResponse::~CMLResumeFederationResponse()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLResumeFederationResponse::Copy(const CMLResumeFederationResponse& rSource)
{
	CMLEvent::Copy(rSource); // base class processing first
	m_bResumeFedResponseAck = rSource.m_bResumeFedResponseAck;
}



//------------------------------------------------------------------------------
//!	Summary:	Overloaded assignment operator
//!
//!	Parameters:	\li rSource - the object being assigned
//!
//!	Returns:	this object
//------------------------------------------------------------------------------
const CMLResumeFederationResponse& CMLResumeFederationResponse::operator = (const CMLResumeFederationResponse& rSource)
{
	Copy(rSource);
	return *this;
}


//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve resumeFedResponseAck
//!
//!	Parameters:	None
//!
//!	Returns:	resumeFedResponseAck
//------------------------------------------------------------------------------
bool CMLResumeFederationResponse::GetResumeFedResponseAck()
{
	return m_bResumeFedResponseAck;
}
//------------------------------------------------------------------------------
//!	Summary:	Called to set resumeFedResponseAck
//!
//!	Parameters:	None
//!
//!	Returns:	resumeFedResponseAck
//------------------------------------------------------------------------------
void CMLResumeFederationResponse::SetResumeFedResponseAck(bool b)
{
	m_bResumeFedResponseAck = b;
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
CMLPropMember* CMLResumeFederationResponse::GetXmlBlock(EMLXMLBlock eXmlBlock)
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
			pXmlClasses = new CMLPropMember("resumeFederationResponseAck", m_bResumeFedResponseAck);
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
bool CMLResumeFederationResponse::SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember)
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
		if(lstrcmpi(pMLPropMember->GetName(), "resumeFederationResponseAck") == 0)
		{
			m_bResumeFedResponseAck = pMLPropMember->AsBool();			
		}

		break;
	default:

		bSuccessful = CMLEvent::SetXmlValue(eXmlBlock, pMLPropMember); // base class handles these
		break;

	}// switch(eXmlBlock)

	return bSuccessful;
}
