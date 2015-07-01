//------------------------------------------------------------------------------
/*! \file	MLReadyParams.cpp
//
//  Contains the implementation of the CMLStartFederationParams class
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
#include <MLStartFederationRequest.h>
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
CMLStartFederationParams::CMLStartFederationParams() : CMLRequestParams()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Copy constructor
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLStartFederationParams::CMLStartFederationParams(const CMLStartFederationParams& rSource)
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
CMLStartFederationParams::~CMLStartFederationParams()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLStartFederationParams::Copy(const CMLStartFederationParams& rSource)
{
	CMLRequestParams::Copy(rSource);
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded assignment operator
//!
//!	Parameters:	\li rSource - the object being assigned
//!
//!	Returns:	this object
//------------------------------------------------------------------------------
const CMLStartFederationParams& CMLStartFederationParams::operator = (const CMLStartFederationParams& rSource)
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
CMLPropMember* CMLStartFederationParams::GetXmlBlock(EMLXMLBlock eXmlBlock)
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
bool CMLStartFederationParams::SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember)
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

		break;

	default:			
		break;

	}// switch(eXmlBlock)

	return bSuccessful;
}