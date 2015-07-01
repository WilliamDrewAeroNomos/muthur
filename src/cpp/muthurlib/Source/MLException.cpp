//------------------------------------------------------------------------------
/*! \file	MLException.cpp
//
//  Contains the implementation of the CMLException class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>05-22-2011 <td>1.00		<td>Original Release <td>KRM	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLException.h>
#include <MLRequestParams.h>

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
CMLException::CMLException()
{
	m_pRequest = NULL;
	m_sMessage = "";
}

//------------------------------------------------------------------------------
//!	Summary:	Destructor
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLException::~CMLException()
{
	if(m_pRequest != NULL)
		delete m_pRequest;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the formatted error message
//!
//!	Parameters:	None
//!
//!	Returns:	The error message
//------------------------------------------------------------------------------
char* CMLException::GetMessage()
{
	return m_sMessage;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the request associated with the error
//!
//!	Parameters:	None
//!
//!	Returns:	The request that triggered the error
//------------------------------------------------------------------------------
CMLRequestParams* CMLException::GetRequest()
{
	return m_pRequest;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set request that triggered this exception
//!
//!	Parameters:	\li pRequest - the request to be assigned to this exception
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLException::SetRequest(CMLRequestParams* pRequest)
{
	if(m_pRequest != NULL)
		delete m_pRequest;
		
	m_pRequest = pRequest;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the error message
//!
//!	Parameters:	\li pszMessage - the buffer containing the error message
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLException::SetMessage(char* pszMessage)
{
	if(pszMessage == NULL)
		m_sMessage = "no message";
	else
		m_sMessage = pszMessage;
}

