//------------------------------------------------------------------------------
/*! \file	MLRegisterResponse.cpp
//
//  Contains the implementation of the CMLRegisterResponse class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>05-31-2011 <td>1.00		<td>Original Release <td>KRM	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLRegisterResponse.h>
#include <MLAmbassador.h>
#include <Reporter.h>

//-----------------------------------------------------------------------------
//	DEFINES
//-----------------------------------------------------------------------------

//-----------------------------------------------------------------------------
//	GLOBALS
//-----------------------------------------------------------------------------
extern CMLAmbassador	_theAmbassador;	//!< The singleton Ambassador instance
CReporter		_theReporterRegResp;	//!< The global diagnostics / error reporter

//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLRegisterResponse::CMLRegisterResponse() : CMLEvent()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Copy constructor
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLRegisterResponse::CMLRegisterResponse(const CMLRegisterResponse& rSource)
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
CMLRegisterResponse::~CMLRegisterResponse()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLRegisterResponse::Copy(const CMLRegisterResponse& rSource)
{
	//	Perform base class processing first
	CMLEvent::Copy(rSource);

	m_fedRegHandle = rSource.m_fedRegHandle;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the federate regisration handle
//!
//!	Parameters:	None
//!
//!	Returns:	The FRH assigned by the Ambassador to this reponse
//------------------------------------------------------------------------------
CMLHandle CMLRegisterResponse::GetFedRegHandle()
{
	return m_fedRegHandle;
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded assignment operator
//!
//!	Parameters:	\li rSource - the object being assigned
//!
//!	Returns:	this object
//------------------------------------------------------------------------------
const CMLRegisterResponse& CMLRegisterResponse::operator = (const CMLRegisterResponse& rSource)
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
CMLPropMember* CMLRegisterResponse::GetXmlBlock(EMLXMLBlock eXmlBlock)
{
	CMLPropMember* pXmlBlock = NULL;

	//	Use the base class to allocate the object
	if((pXmlBlock = CMLEvent::GetXmlBlock(eXmlBlock)) != NULL)
	{
		switch(eXmlBlock)
		{
			case ML_XML_BLOCK_CONTROL:

				// No additional control block properties for this derived class
				break;
			
			case ML_XML_BLOCK_DATA:

				pXmlBlock->AddChild("federateName", _theAmbassador.GetFederateName());
				pXmlBlock->AddChild("registrationID", m_fedRegHandle);
				pXmlBlock->AddChild("status", "unknown");
				pXmlBlock->AddChild("success", true);
				pXmlBlock->AddChild("heartBeatEnabled",  _theAmbassador.GetIsHeartbeatEnabled());
				break;

		}// switch(eXmlBlock)
	
	}// if((pXmlBlock = CMLRequestParams::GetXmlElement(eXmlBlock)) != NULL)

	return pXmlBlock;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the federate registration handle
//!
//!	Parameters:	\li hFRH - the federate registration handle
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLRegisterResponse::SetFedRegHandle(CMLHandle& rHandle)
{
	m_fedRegHandle = rHandle;
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
bool CMLRegisterResponse::SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember)
{
	bool				bSuccessful = false;
	CMLPropMember*		pChild = NULL;

	switch(eXmlBlock)
	{
	case ML_XML_BLOCK_DATA:			

		if(lstrcmpi(pMLPropMember->GetName(), "registrationID") == 0)
		{
			m_fedRegHandle = pMLPropMember->AsMLHandle();
			bSuccessful = true;
		}
		else if(lstrcmpi(pMLPropMember->GetName(), "heartBeatEnabled") == 0)
		{
			_theAmbassador.SetIsHeartbeatEnabled(pMLPropMember->AsBool());
			bSuccessful = true;
		}
		else
			bSuccessful = CMLEvent::SetXmlValue(eXmlBlock, pMLPropMember); // base class handles these
		break;

	case ML_XML_BLOCK_CONTROL:
	default:

		bSuccessful = CMLEvent::SetXmlValue(eXmlBlock, pMLPropMember); // base class handles these
		break;

	}// switch(eXmlBlock)

	return bSuccessful;
}
