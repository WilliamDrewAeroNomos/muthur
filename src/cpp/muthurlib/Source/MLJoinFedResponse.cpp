//------------------------------------------------------------------------------
/*! \file	MLJoinFedResponse.cpp
//
//  Contains the implementation of the CMLJoinFedResponse class
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
#include <MLJoinFedResponse.h>

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
CMLJoinFedResponse::CMLJoinFedResponse() : CMLEvent()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Copy constructor
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLJoinFedResponse::CMLJoinFedResponse(const CMLJoinFedResponse& rSource)
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
CMLJoinFedResponse::~CMLJoinFedResponse()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLJoinFedResponse::Copy(const CMLJoinFedResponse& rSource)
{
	//	Perform base class processing first
	CMLEvent::Copy(rSource);

	m_fedExecHandle = rSource.m_fedExecHandle;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the federate execution handle
//!
//!	Parameters:	None
//!
//!	Returns:	The FEH assigned by the Ambassador
//------------------------------------------------------------------------------
CMLHandle CMLJoinFedResponse::GetFedExecHandle()
{
	return m_fedExecHandle;
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded assignment operator
//!
//!	Parameters:	\li rSource - the object being assigned
//!
//!	Returns:	this object
//------------------------------------------------------------------------------
const CMLJoinFedResponse& CMLJoinFedResponse::operator = (const CMLJoinFedResponse& rSource)
{
	Copy(rSource);
	return *this;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the federate regisration handle
//!
//!	Parameters:	\li hFRH - the federate registration handle
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLJoinFedResponse::SetFedExecHandle(CMLHandle& rHandle)
{
	m_fedExecHandle = rHandle;
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
CMLPropMember* CMLJoinFedResponse::GetXmlBlock(EMLXMLBlock eXmlBlock)
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

				// No additional control block properties for this derived class
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
bool CMLJoinFedResponse::SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember)
{
	bool				bSuccessful = false;
	CMLPropMember*		pChild = NULL;

	switch(eXmlBlock)
	{
		case ML_XML_BLOCK_CONTROL:
			
			if(lstrcmpi("federationExecutionHandle", pMLPropMember->GetName()) == 0)
			{
				m_fedExecHandle.SetMuthurId(pMLPropMember->GetValue());
				bSuccessful = true;
			}
			else
			{
				bSuccessful = CMLEvent::SetXmlValue(eXmlBlock, pMLPropMember); // base class handles these
			}
			break;

		case ML_XML_BLOCK_DATA:			
		default:
			
			bSuccessful = CMLEvent::SetXmlValue(eXmlBlock, pMLPropMember); // base class handles these
			break;

	}// switch(eXmlBlock)

	return bSuccessful;
}