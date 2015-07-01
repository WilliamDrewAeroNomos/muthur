//------------------------------------------------------------------------------
/*! \file	MLSubDataParams.cpp
//
//  Contains the implementation of the CMLSubDataParams class
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
#include <MLSubDataParams.h>

//-----------------------------------------------------------------------------
//	DEFINES
//-----------------------------------------------------------------------------

//-----------------------------------------------------------------------------
//	GLOBALS
//-----------------------------------------------------------------------------

//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	\li hFRH - the federate registration handle assigned by the Ambassador
//!				\li hFEH - the federate execution handle assigned by the Ambassador
//!				\li pICallback - the data event callback interface
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLSubDataParams::CMLSubDataParams(TFedRegHandle hFRH, TFedExecHandle hFEH, IMLDataEventCallback* pICallback) 
				 :CMLRequestParams(hFRH, NULL, hFEH)
{
	m_pIDataCallback = NULL;
	
	if(pICallback != NULL)
		SetDataEventCallback(pICallback);
}

//------------------------------------------------------------------------------
//!	Summary:	Copy constructor
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLSubDataParams::CMLSubDataParams(const CMLSubDataParams& rSource)
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
CMLSubDataParams::~CMLSubDataParams()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called to add a class to be subscribed to
//!
//!	Parameters:	\li pszClassName - the name of the desired class
//!
//!	Returns:	true if successful
//------------------------------------------------------------------------------
bool CMLSubDataParams::AddClass(char* pszClassName)
{
	return false;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLSubDataParams::Copy(const CMLSubDataParams& rSource)
{
	//	Perform base class processing first
	CMLRequestParams::Copy(rSource);

	m_pIDataCallback = rSource.m_pIDataCallback;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the number of classes being subscribed to
//!
//!	Parameters:	None
//!
//!	Returns:	The number of classes being subscribed
//------------------------------------------------------------------------------
int CMLSubDataParams::GetClassCount()
{
	return 0;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the DataEvent callback interface
//!
//!	Parameters:	None
//!
//!	Returns:	The ambassador callback interface used by this federate
//------------------------------------------------------------------------------
IMLDataEventCallback* CMLSubDataParams::GetDataEventCallback()
{
	return m_pIDataCallback;
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded assignment operator
//!
//!	Parameters:	\li rSource - the object being assigned
//!
//!	Returns:	this object
//------------------------------------------------------------------------------
const CMLSubDataParams& CMLSubDataParams::operator = (const CMLSubDataParams& rSource)
{
	Copy(rSource);
	return *this;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the DataEvent callback interface
//!
//!	Parameters:	\li pICallback - the callback interface used for data events
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLSubDataParams::SetDataEventCallback(IMLDataEventCallback* pICallback)
{
	m_pIDataCallback = pICallback;
}

