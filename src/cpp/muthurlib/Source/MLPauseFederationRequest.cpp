//------------------------------------------------------------------------------
/*! \file	MLPauseFederationParams.cpp
//
//  Contains the implementation of the CMLPauseFederationParams class
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
#include <MLPauseFederationRequest.h>
#include <Reporter.h>
extern CReporter _theReporter;
//-----------------------------------------------------------------------------
//	DEFINES
//-----------------------------------------------------------------------------

//-----------------------------------------------------------------------------
//	GLOBALS
//-----------------------------------------------------------------------------

////------------------------------------------------------------------------------
////!	Summary:	Constructor
////!
////!	Parameters:	\li pICallback - the system event callback interface
////!
////!	Returns:	None
//------------------------------------------------------------------------------
CMLPauseFederationParams::CMLPauseFederationParams() : CMLRequestParams()
{
	m_lLengthOfPauseMSecs = -1;
}

//------------------------------------------------------------------------------
//!	Summary:	Copy constructor
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLPauseFederationParams::CMLPauseFederationParams(const CMLPauseFederationParams& rSource)
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
CMLPauseFederationParams::~CMLPauseFederationParams()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLPauseFederationParams::Copy(const CMLPauseFederationParams& rSource)
{
	CMLRequestParams::Copy(rSource);
	m_lLengthOfPauseMSecs = rSource.m_lLengthOfPauseMSecs;
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded assignment operator
//!
//!	Parameters:	\li rSource - the object being assigned
//!
//!	Returns:	this object
//------------------------------------------------------------------------------
const CMLPauseFederationParams& CMLPauseFederationParams::operator = (const CMLPauseFederationParams& rSource)
{
	Copy(rSource);
	return *this;
}

//------------------------------------------------------------------------------
//!	Summary:	Retrieves the length of pause in msecs
//!
//!	Parameters:	\li none
//!
//!	Returns:	The length of pause in msecs
//------------------------------------------------------------------------------
long CMLPauseFederationParams::GetLengthOfPause()
{
	return m_lLengthOfPauseMSecs;
}

//------------------------------------------------------------------------------
//!	Summary:	Sets the length of the pause in msecs
//!
//!	Parameters:	\li l - the length of pause in msecs
//!
//!	Returns:	none
//------------------------------------------------------------------------------
void CMLPauseFederationParams::SetLengthOfPause(long l)
{
	m_lLengthOfPauseMSecs = l;
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
CMLPropMember* CMLPauseFederationParams::GetXmlBlock(EMLXMLBlock eXmlBlock)
{
	CMLPropMember* pXmlBlock = NULL;
	CMLPropMember* pXmlClasses = NULL;
	//	Use the base class to allocate the object
	if((pXmlBlock = CMLRequestParams::GetXmlBlock(eXmlBlock)) != NULL)
	{
		switch(eXmlBlock)
		{
		case ML_XML_BLOCK_CONTROL:

			break;

		case ML_XML_BLOCK_DATA:
			pXmlBlock->AddChild("lengthOfPauseMSecs", m_lLengthOfPauseMSecs);
			pXmlBlock->AddChild("federationExecutionModelHandle", m_fedExecModelHandle.GetMuthurId());	
			
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
bool CMLPauseFederationParams::SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember)
{
	bool				bSuccessful = false;
	CMLPropMember*		pChild = NULL;

	switch(eXmlBlock)
	{
	case ML_XML_BLOCK_CONTROL:
		// No base class data in this block
		if(lstrcmpi("federationExecutionHandle", pMLPropMember->GetName()) == 0)
		{
			m_fedExecHandle.SetMuthurId(pMLPropMember->GetValue());
		}
		break;
	case ML_XML_BLOCK_DATA:

		//	Is this the FEM group?
		if(lstrcmpi(pMLPropMember->GetName(), "federationExecutionModelHandle") == 0)
		{
			m_fedExecModelHandle.SetMuthurId(pMLPropMember->GetValue());			
		}
		else if(lstrcmpi(pMLPropMember->GetName(), "lengthOfPauseMSecs") == 0)
		{
			m_lLengthOfPauseMSecs = pMLPropMember->AsLong();			
		}
		break;

	default:			
		break;

	}// switch(eXmlBlock)

	return bSuccessful;
}