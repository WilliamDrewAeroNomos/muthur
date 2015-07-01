//------------------------------------------------------------------------------
/*! \file	MLString.cpp
//
//  Contains the implementation of the CMLString class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>06-29-2011 <td>1.00		<td>Original Release <td>KRM	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLString.h>

//-----------------------------------------------------------------------------
//	DEFINES
//-----------------------------------------------------------------------------

//-----------------------------------------------------------------------------
//	GLOBALS
//-----------------------------------------------------------------------------

//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	pszString - the value to be assigned
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLString::CMLString(const char* pszString)
{
	m_pszString = NULL;

	//	Allocate a minimal buffer
	Empty();
	
	if((pszString != NULL) && (lstrlen(pszString) > 0))
		Set(pszString);
}

//------------------------------------------------------------------------------
//!	Summary:	Copy constructor
//!
//!	Parameters:	\li rString - the string to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLString::CMLString(const CMLString& rString)
{
	Set((const char*)rString);
}

//------------------------------------------------------------------------------
//!	Summary:	Destructor
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLString::~CMLString()
{
	//	Deallocate the string memory
	Free();
}

//------------------------------------------------------------------------------
//!	Summary:	Called to add the specified text to the string
//!
//!	Parameters:	\li pszString - the text to be added
//!
//!	Returns:	The new string
//------------------------------------------------------------------------------
char* CMLString::Add(const char* pszString)
{
	char* 	pBuffer;
	int		iLength;

	assert(m_pszString);

	//	Did the caller provide a string?
	if(pszString != NULL)
	{
		//	What is the length of the caller's string?
		if((iLength = lstrlen(pszString)) > 0)
		{
			//	Allow room for our local string
			iLength += lstrlen(m_pszString);

			//	Allocate a buffer for the caller's string
			if((pBuffer = Alloc(iLength)) != NULL)
			{
				//	Copy our local string to the new buffer
				lstrcpy(pBuffer, m_pszString);

				//	Add the caller's string
				lstrcat(pBuffer, pszString);
			}

			//	Reassign the local buffer
			Free();
			m_pszString = pBuffer;
		}
	}

	return m_pszString;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to allocate a buffer of the requested length
//!
//!	Parameters:	\li uLength - the size of the buffer to be allocated
//!
//!	Returns:	A pointer to the newly allocated buffer
//------------------------------------------------------------------------------
char* CMLString::Alloc(unsigned int uLength)
{
	char* pBuffer = NULL;

	pBuffer = new char[uLength + 1];
	if(pBuffer != NULL)
		memset(pBuffer, 0, (uLength + 1));
	assert(pBuffer);
	return pBuffer;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to compare a string to this string. The comparison is
//				case sensitive
//!
//!	Parameters:	\li pszCompare - pointer to string to use for comparison
//!
//!	Returns:	\li 0 if the two strings are equal
//!				\li > 0 if this string is greater than comparison string
//!				\li < 0 if this string is less than comparison string
//------------------------------------------------------------------------------
int CMLString::Compare(const char* pszCompare)
{
	if(m_pszString)
	{
		if(pszCompare != NULL)
			return lstrcmp(m_pszString, pszCompare);
		else
			return 1;
	}
	else
	{
		if(pszCompare != NULL)
			return -1;
		else
			return 1;
	}
}

//------------------------------------------------------------------------------
//!	Summary:	Called to compare a string to this string. The comparison is
//				NOT case sensitive
//!
//!	Parameters:	\li pszCompare - pointer to string to use for comparison
//!
//!	Returns:	\li 0 if the two strings are equal
//!				\li > 0 if this string is greater than comparison string
//!				\li < 0 if this string is less than comparison string
//------------------------------------------------------------------------------
int CMLString::CompareNoCase(const char* pszCompare)
{
	if(m_pszString)
	{
		if(pszCompare != NULL)
			return lstrcmpi(m_pszString, pszCompare);
		else
			return 1;
	}
	else
	{
		if(pszCompare != NULL)
			return -1;
		else
			return 1;
	}
}

//------------------------------------------------------------------------------
//!	Summary:	Called to clear the existing string
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLString::Empty()
{
	//	Free the existing buffer
	Free();

	if((m_pszString = (char*)malloc(1)) != NULL)
		memset(m_pszString, 0, 1);

	assert(m_pszString);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to deallocate the string buffer
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLString::Free()
{
	if(m_pszString != NULL)
	{
		delete [] m_pszString;
		m_pszString = NULL;
	}
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the character at the specified index
//!
//!	Parameters:	\li iIndex - the zero-based index of the desired character
//!
//!	Returns:	The character at the specified index
//------------------------------------------------------------------------------
char CMLString::GetAt(int iIndex)
{
	int iLength = GetLength();

	if((iIndex >= 0) && (iIndex < iLength))
		return m_pszString[iIndex];
	else
		return (char)0;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the length of the string
//!
//!	Parameters:	None
//!
//!	Returns:	The number of characters in the string
//------------------------------------------------------------------------------
int CMLString::GetLength()
{
	if(m_pszString != NULL)
	{
		return lstrlen(m_pszString);
	}
	else
	{
		return 0;
	}
}

//------------------------------------------------------------------------------
//!	Summary:	Called to determine if the string is empty
//!
//!	Parameters:	None
//!
//!	Returns:	true if empty
//------------------------------------------------------------------------------
bool CMLString::IsEmpty()
{
	if(m_pszString != NULL)
	{
		return (strlen(m_pszString) == 0);
	}
	else
	{
		return true;
	}
}

//------------------------------------------------------------------------------
//!	Summary:	Called to obtain a substring consisting of iCount characters 
//!				from the start of the current string
//!
//!	Parameters:	\li iCount - number of characters from the start of the string
//!				\li rSubString - reference to object in which to store substring
//!
//!	Returns:	The CMLString reference provided by the caller
//------------------------------------------------------------------------------
CMLString& CMLString::Left(int iCount, CMLString& rSubString)
{
	int 	iLength = GetLength();
	char*	pBuffer;

	//	Is the count valid?
	if(iCount <= 0)
	{
		rSubString.Empty();
	}

	//	Should we return the whole string?
	else if(iLength <= iCount)
	{
		rSubString = m_pszString;
	}

	else
	{
		pBuffer = Alloc(iCount + 1);
		lstrcpyn(pBuffer, m_pszString, iCount + 1);
		rSubString = pBuffer;
		delete pBuffer;
	}

	return rSubString;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to convert the string to lower case
//!
//!	Parameters: None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLString::MakeLower()
{
	if((m_pszString != NULL) && (lstrlen(m_pszString) > 0))
		_strlwr_s(m_pszString, lstrlen(m_pszString) + 1);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to convert the string to upper case
//!
//!	Parameters: None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLString::MakeUpper()
{
	if((m_pszString != NULL) && (lstrlen(m_pszString) > 0))
		_strupr_s(m_pszString, lstrlen(m_pszString) + 1);
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded equality operator
//!
//!	Parameters: \li rString - reference to the object used for comparison
//!
//!	Returns:	Non zero if objects are equal
//------------------------------------------------------------------------------
int	CMLString::operator == (const CMLString& rString)
{
	const char* lpCompare = (const char*)rString;

	if(m_pszString)
	{
		if(lpCompare != NULL)
			return (strcmp(m_pszString, lpCompare) == 0);
		else
			return 0;
	}
	else
	{
		if(lpCompare != NULL)
			return 0;
		else
			return 1;
	}
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded < operator
//!
//!	Parameters: \li rString - reference to the object used for comparison
//!
//!	Returns:	Non zero if this object is less than comparison object
//------------------------------------------------------------------------------
int	CMLString::operator < (const CMLString& rString)
{
	const char* lpCompare = (const char*)rString;

	if(m_pszString)
	{
		if(lpCompare != NULL)
			return (strcmp(m_pszString, lpCompare) < 0);
		else
			return 0;
	}
	else
	{
		if(lpCompare != NULL)
			return 0;
		else
			return 1;
	}
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded > operator
//!
//!	Parameters: \li rString - reference to the object used for comparison
//!
//!	Returns:	Non zero if this object is greater than comparison object
//------------------------------------------------------------------------------
int	CMLString::operator > (const CMLString& rString)
{
	const char* lpCompare = (const char*)rString;

	if(m_pszString)
	{
		if(lpCompare != NULL)
			return (strcmp(m_pszString, lpCompare) > 0);
		else
			return 1;
	}
	else
	{
		return 0;
	}
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded const char* () operator
//!
//!	Parameters: None
//!
//!	Returns:	Casted null terminated string
//------------------------------------------------------------------------------
CMLString::operator const char*() const
{
	return m_pszString;
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded char * () operator
//!
//!	Parameters: None
//!
//!	Returns:	Casted null terminated string
//------------------------------------------------------------------------------
CMLString::operator char * ()
{
	return m_pszString;
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded assignment operator
//!
//!	Parameters: \li pszString - the string to be copied
//!
//!	Returns:	Constant reference to this object
//------------------------------------------------------------------------------
const CMLString& CMLString::operator = (const char* pszString)
{
	Set(pszString);
	return *this;
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded assignment operator
//!
//!	Parameters: \li rString - the string to be copied
//!
//!	Returns:	Constant reference to this object
//------------------------------------------------------------------------------
const CMLString& CMLString::operator = (const CMLString& rString)
{
	Set((const char*)rString);
	return *this;
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded assignment operator
//!
//!	Parameters: \li rString - the string to be copied
//!
//!	Returns:	Constant reference to this object
//------------------------------------------------------------------------------
const CMLString& CMLString::operator += (const char* pszString)
{
	Add(pszString);
	return *this;
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded [] operator
//!
//!	Parameters: \li iIndex - the index of the desired character
//!
//!	Returns:	The character at the specified index
//------------------------------------------------------------------------------
char CMLString::operator [] (int iIndex)
{
	return GetAt(iIndex);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to obtain a substring consisting of iCount characters 
//!				from the start of the current string
//!
//!	Parameters: \li iCount - number of characters from the start of the string
//!				\li rSubString - reference to object in which to store substring
//!
//!	Returns:	The CMLString reference provided by the caller
//------------------------------------------------------------------------------
CMLString& CMLString::Right(int iCount, CMLString& rSubString)
{
	int iLength = GetLength();

	//	Is the count valid?
	if(iCount <= 0)
	{
		rSubString.Empty();
	}

	//	Should we return the whole string?
	else if(iLength <= iCount)
	{
		rSubString = m_pszString;
	}

	else
	{
		rSubString = &(m_pszString[iLength - iCount]);
	}

	return rSubString;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the value of this object 
//!
//!	Parameters: \li pszString - pointer to buffer containing the string
//!
//!	Returns:	A pointer to the local string buffer
//------------------------------------------------------------------------------
char* CMLString::Set(const char* pszString)
{
	char* 	pBuffer;
	int		iLength;

	assert(m_pszString);

	//	Did the caller provide a string?
	if(pszString != NULL)
	{
		//	What is the length of the caller's string?
		if((iLength = strlen(pszString)) > 0)
		{
			//	Allocate a buffer for the caller's string
			if((pBuffer = Alloc(iLength)) != NULL)
				lstrcpy(pBuffer, pszString);

			//	Reassign the local buffer
			Free();
			m_pszString = pBuffer;
		}
		else
		{
			//	Create a null string
			Empty();
		}
	}
	else
	{
		//	Create a null string
		Empty();
	}

	return m_pszString;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the character at the zero based index specified 
//!				by the caller 
//!
//!	Parameters: \li iIndex - the zero-based index of the desired position
//!				\li cChar - character to insert at specified position
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLString::SetAt(int iIndex, char cChar)
{
	int iLength = GetLength();

	if((iIndex >= 0) && (iIndex < iLength))
		m_pszString[iIndex] = cChar;
}





