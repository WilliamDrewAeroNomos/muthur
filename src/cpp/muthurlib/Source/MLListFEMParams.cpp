//------------------------------------------------------------------------------
/*! \file	MLListFEMParams.cpp
//
//  Contains the implementation of the CMLListFEMParams class
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
#include <MLListFEMParams.h>
#include <MLAmbassador.h>
#include <Reporter.h>

//-----------------------------------------------------------------------------
//	DEFINES
//-----------------------------------------------------------------------------

//-----------------------------------------------------------------------------
//	GLOBALS
//-----------------------------------------------------------------------------
extern CMLAmbassador	_theAmbassador;	//!< The singleton Ambassador instance
CReporter		_theReporterListFems;	//!< The global diagnostics / error reporter

//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLListFEMParams::CMLListFEMParams() : CMLRequestParams()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Copy constructor
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLListFEMParams::CMLListFEMParams(const CMLListFEMParams& rSource)
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
CMLListFEMParams::~CMLListFEMParams()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLListFEMParams::Copy(const CMLListFEMParams& rSource)
{
	CMLRequestParams::Copy(rSource); // base class implementation
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
CMLPropMember* CMLListFEMParams::GetXmlBlock(EMLXMLBlock eXmlBlock)
{
	CMLPropMember* pXmlBlock = NULL;

	//	Use the base class to allocate the object
	if((pXmlBlock = CMLRequestParams::GetXmlBlock(eXmlBlock)) != NULL)
	{
		switch(eXmlBlock)
		{
			case ML_XML_BLOCK_CONTROL:

				break;
			
			case ML_XML_BLOCK_DATA:

				break;

		}// switch(eXmlBlock)
	
	}// if((pXmlBlock = CMLRequestParams::GetXmlBlock(eXmlBlock)) != NULL)

	return pXmlBlock;
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded assignment operator
//!
//!	Parameters:	\li rSource - the object being assigned
//!
//!	Returns:	this object
//------------------------------------------------------------------------------
const CMLListFEMParams& CMLListFEMParams::operator = (const CMLListFEMParams& rSource)
{
	Copy(rSource);															
	return *this;
}

