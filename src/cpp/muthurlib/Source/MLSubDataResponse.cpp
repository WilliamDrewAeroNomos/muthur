//------------------------------------------------------------------------------
/*! \file	MLSubDataResponse.cpp
//
//  Contains the implementation of the CMLSubDataResponse class
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
#include <MLSubDataResponse.h>

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
CMLSubDataResponse::CMLSubDataResponse() : CMLEvent()
{
	m_iSubClassCount = 0;
}

//------------------------------------------------------------------------------
//!	Summary:	Copy constructor
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLSubDataResponse::CMLSubDataResponse(const CMLSubDataResponse& rSource)
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
CMLSubDataResponse::~CMLSubDataResponse()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLSubDataResponse::Copy(const CMLSubDataResponse& rSource)
{
	CMLEvent::Copy(rSource); // base class processing first

	m_iSubClassCount = rSource.m_iSubClassCount;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to check the total number of classes that were registered
//!
//!	Parameters:	None
//!
//!	Returns:	The total number registered
//------------------------------------------------------------------------------
int CMLSubDataResponse::GetSubClassCount()
{
	return m_iSubClassCount;
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded assignment operator
//!
//!	Parameters:	\li rSource - the object being assigned
//!
//!	Returns:	this object
//------------------------------------------------------------------------------
const CMLSubDataResponse& CMLSubDataResponse::operator = (const CMLSubDataResponse& rSource)
{
	Copy(rSource);
	return *this;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the total number of subscribed classes
//!
//!	Parameters:	\li iSubClassCount - the number of classes subscribed
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLSubDataResponse::SetSubClassCount(int iSubClassCount)
{
	m_iSubClassCount = iSubClassCount;
}

