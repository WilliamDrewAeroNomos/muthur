//------------------------------------------------------------------------------
/*! \file	MLRegPubParams.cpp
//
//  Contains the implementation of the CMLRegPubParams class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>06-01-2011 <td>1.00		<td>Original Release <td>REB	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLRegisterPublicationParams.h>

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
CMLRegPubParams::CMLRegPubParams(CMLFedExecModel f = NULL, char**dataTypes = NULL)
				 :CMLRequestParams(hFedRegHandle, NULL, NULL)
{
}

//------------------------------------------------------------------------------
//!	Summary:	Copy constructor
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLRegPubParams::CMLRegPubParams(const CMLRegPubParams& rSource)
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
CMLRegPubParams::~CMLRegPubParams()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLRegPubParams::Copy(const CMLRegPubParams& rSource)
{
	CMLRequestParams::Copy(rSource); // base class implementation
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded assignment operator
//!
//!	Parameters:	\li rSource - the object being assigned
//!
//!	Returns:	this object
//------------------------------------------------------------------------------
const CMLRegPubParams& CMLRegPubParams::operator = (const CMLRegPubParams& rSource)
{
	Copy(rSource);															
	return *this;
}

