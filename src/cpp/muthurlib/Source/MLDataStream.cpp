//------------------------------------------------------------------------------
/*! \file	MLDataStream.cpp
//
//  Contains the implementation of the CMLDataStream class
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
#include <MLDataStream.h>

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
CMLDataStream::CMLDataStream()
{
	m_bRead = true;
	m_ulSize = 0;
	m_pBuffer = NULL;
}

//------------------------------------------------------------------------------
//!	Summary:	Copy constructor
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLDataStream::CMLDataStream(const CMLDataStream& rSource)
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
CMLDataStream::~CMLDataStream()
{
	FreeBuffer();
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLDataStream::Copy(const CMLDataStream& rSource)
{
	m_bRead = rSource.m_bRead;
	SetStream(rSource.m_pBuffer, rSource.m_ulSize);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to deallocate the stream's buffer
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLDataStream::FreeBuffer()
{
	if(m_pBuffer != NULL)
	{
		delete [] m_pBuffer;
		m_pBuffer = NULL;
	}
	m_ulSize = 0;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the stream buffer
//!
//!	Parameters:	None
//!
//!	Returns:	The buffer containing the data stream
//------------------------------------------------------------------------------
char* CMLDataStream::GetBuffer()
{
	return m_pBuffer;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to retrieve the flag that indicates if reading or writing
//!
//!	Parameters:	None
//!
//!	Returns:	true if reading data, false if writing data
//------------------------------------------------------------------------------
bool CMLDataStream::GetRead()
{
	return m_bRead;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the size of the stream
//!
//!	Parameters:	None
//!
//!	Returns:	The size of the stream in bytes
//------------------------------------------------------------------------------
unsigned long CMLDataStream::GetSize()
{
	return m_ulSize;
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded assignment operator
//!
//!	Parameters:	\li rSource - the object being assigned
//!
//!	Returns:	this object
//------------------------------------------------------------------------------
const CMLDataStream& CMLDataStream::operator = (const CMLDataStream& rSource)
{
	Copy(rSource);															
	return *this;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the direction of the stream
//!
//!	Parameters:	\li bRead - true to read, false to write
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLDataStream::SetRead(bool bRead)
{
	m_bRead = bRead;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the data buffer
//!
//!	Parameters:	\li pBuffer - the buffer containing the data stream
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLDataStream::SetStream(char* pBuffer, unsigned long ulSize)
{
	FreeBuffer(); // deallocate the existing buffer

	//	If the size is not specified assume this is a null-terminated character buffer
	if((ulSize == 0) && (pBuffer != NULL) && (lstrlen(pBuffer) > 0))
	{
		ulSize = lstrlen(pBuffer) + 1;
	}

	if((pBuffer != NULL) && (ulSize > 0))
	{
		m_ulSize = ulSize;

		m_pBuffer = new char[m_ulSize];
		memcpy(m_pBuffer, pBuffer, m_ulSize);
	}
	else
	{
		m_pBuffer = NULL;
		m_ulSize = 0;
	}

}

