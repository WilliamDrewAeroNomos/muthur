//------------------------------------------------------------------------------
/*! \file	MLJoinFedParams.cpp
//
//  Contains the implementation of the CMLJoinFedParams class
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
#include <MLJoinFedParams.h>
#include <MLAmbassador.h>
#include <MLHandle.h>
#include <MLFedExecModel.h>
extern CMLAmbassador _theAmbassador;//!< The singleton Ambassador instance
//-----------------------------------------------------------------------------
//	DEFINES
//-----------------------------------------------------------------------------

//-----------------------------------------------------------------------------
//	GLOBALS
//-----------------------------------------------------------------------------

//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	\li hFedRegHandle - Registration handle returned by Ambassador
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLJoinFedParams::CMLJoinFedParams() : CMLRequestParams()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Copy constructor
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLJoinFedParams::CMLJoinFedParams(const CMLJoinFedParams& rSource)
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
CMLJoinFedParams::~CMLJoinFedParams()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLJoinFedParams::Copy(const CMLJoinFedParams& rSource)
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
const CMLJoinFedParams& CMLJoinFedParams::operator = (const CMLJoinFedParams& rSource)
{
	Copy(rSource);
	return *this;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the FEM to which the user would like to join
//!
//!	Parameters:	NONE
//!
//!	Returns:	The FEM
//------------------------------------------------------------------------------
CMLFedExecModel CMLJoinFedParams::GetFEM() { return m_FEM; }


//------------------------------------------------------------------------------
//!	Summary:	Called to set the of the FEM to which the user would like to join
//!
//!	Parameters:	\li eXmlBlock - the enumerated block identifier
//!				\li pMLPropMember - the element that contains the value
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
void CMLJoinFedParams::SetFEM(CMLFedExecModel f)
{
	m_FEM = CMLFedExecModel(f);
	SetFedExecModelHandle(m_FEM.GetFedExecModelHandle());
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
CMLPropMember* CMLJoinFedParams::GetXmlBlock(EMLXMLBlock eXmlBlock)
{
	CMLPropMember*  pXmlBlock = NULL;

	if((pXmlBlock = CMLRequestParams::GetXmlBlock(eXmlBlock)) != NULL)
	{
		switch(eXmlBlock)
		{
			case ML_XML_BLOCK_CONTROL:
				
				break;
			
			case ML_XML_BLOCK_DATA:

				pXmlBlock->AddChild(m_FEM.GetXmlMember());
				break;

			default:
				
				if(pXmlBlock != NULL)
				{
					delete pXmlBlock;
					pXmlBlock = NULL;
				}
				break;

		}// switch(eXmlBlock)
	}
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
bool CMLJoinFedParams::SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember)
{
	bool bSuccessful = false;

	switch(eXmlBlock)
	{
		case ML_XML_BLOCK_CONTROL:

			// Set by base class			
			break;
		
		case ML_XML_BLOCK_DATA:

			m_FEM.SetXmlValues(pMLPropMember);
			break;

		default:
			
			break;

	}// switch(eXmlBlock)

	return bSuccessful;
}

