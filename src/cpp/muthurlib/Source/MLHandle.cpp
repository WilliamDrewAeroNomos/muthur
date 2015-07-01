//------------------------------------------------------------------------------
/*! \file	MLHandle.cpp
//
//  Contains the implementation of the CMLHandle class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>06-12-2011 <td>1.00		<td>Original Release <td>KRM	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLHandle.h>

//-----------------------------------------------------------------------------
//	DEFINES
//-----------------------------------------------------------------------------

//-----------------------------------------------------------------------------
//	GLOBALS
//-----------------------------------------------------------------------------

//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	\li pszMuthurId - the ID assigned by Muthur
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLHandle::CMLHandle(char* pszMuthurId)
{
	SetMuthurId(pszMuthurId);
}

//------------------------------------------------------------------------------
//!	Summary:	Copy constructor
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLHandle::CMLHandle(const CMLHandle& rSource)
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
CMLHandle::~CMLHandle()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLHandle::Copy(const CMLHandle& rSource)
{
	lstrcpyn(m_szMuthurId, rSource.m_szMuthurId, sizeof(m_szMuthurId));
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the Muthur system identifier
//!
//!	Parameters:	None
//!
//!	Returns:	The identifier assigned my Muthur
//------------------------------------------------------------------------------
char* CMLHandle::GetMuthurId()
{
	return m_szMuthurId;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to determine if specified handle equals this handle
//!
//!	Parameters:	\li rCompare - the object to be compared
//!
//!	Returns:	true if equal
//------------------------------------------------------------------------------
bool CMLHandle::IsEqual(const CMLHandle& rCompare)
{
	return (lstrcmpi(m_szMuthurId, rCompare.m_szMuthurId) == 0);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to determine if the handle is valid
//!
//!	Parameters:	None
//!
//!	Returns:	true if the handle is valid
//------------------------------------------------------------------------------
bool CMLHandle::IsValid()
{
	return (lstrlen(m_szMuthurId) > 0);
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded assignment operator
//!
//!	Parameters:	\li rSource - the object being assigned
//!
//!	Returns:	this object
//------------------------------------------------------------------------------
const CMLHandle& CMLHandle::operator = (const CMLHandle& rSource)
{
	Copy(rSource);
	return *this;
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded equality operator
//!
//!	Parameters:	\li rCompare - the object being compared
//!
//!	Returns:	true if equal
//------------------------------------------------------------------------------
bool CMLHandle::operator == (const CMLHandle& rCompare)
{
	return IsEqual(rCompare);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to reset the class members to their default values
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLHandle::Reset()
{
	memset(m_szMuthurId, 0, sizeof(m_szMuthurId));
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the handle identifier assigned by Muthur
//!
//!	Parameters:	\li pszMuthurId - the id assigned by Muthur
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLHandle::SetMuthurId(char* pszMuthurId)
{
	if(pszMuthurId != NULL)
	{
		lstrcpyn(m_szMuthurId, pszMuthurId, sizeof(m_szMuthurId));
	}
	else
	{
		memset(m_szMuthurId, 0, sizeof(m_szMuthurId));
	}
}

