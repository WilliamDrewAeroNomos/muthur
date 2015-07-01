//------------------------------------------------------------------------------
/*! \file	MLPropPair.cpp
//
//  Contains the implementation of the CMLPropPair class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>06-20-2011 <td>1.00		<td>Original Release <td>KRM	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLPropPair.h>
#include "Reporter.h"
#include <float.h>

extern CReporter _theReporter;

//-----------------------------------------------------------------------------
//	DEFINES
//-----------------------------------------------------------------------------

//-----------------------------------------------------------------------------
//	GLOBALS
//-----------------------------------------------------------------------------

//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	\li pszName - the name used to identify this property
//!				\li pszValue - the value to be assigned
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLPropPair::CMLPropPair(char* pszName, char* pszValue)
{
	SetProps(pszName, pszValue);
}

//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	\li pszName - the name used to identify this property
//!				\li bValue - the value to be assigned
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLPropPair::CMLPropPair(char* pszName, bool bValue)
{
	SetProps(pszName, bValue);
}

//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	\li pszName - the name used to identify this property
//!				\li rMLHandle - the value to be assigned
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLPropPair::CMLPropPair(char* pszName, CMLHandle& rMLHandle)
{
	SetProps(pszName, rMLHandle);
}

//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	\li pszName - the name used to identify this property
//!				\li dValue - the value to be assigned
//!				\li iPrecision - the decimal precision
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLPropPair::CMLPropPair(char* pszName, double dValue, int iPrecision)
{
	SetProps(pszName, dValue, iPrecision);
}

//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	\li pszName - the name used to identify this property
//!				\li fValue - the value to be assigned
//!				\li iPrecision - the decimal precision
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLPropPair::CMLPropPair(char* pszName, float fValue, int iPrecision)
{
	SetProps(pszName, fValue, iPrecision);
}

//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	\li pszName - the name used to identify this property
//!				\li iValue - the value to be assigned
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLPropPair::CMLPropPair(char* pszName, int iValue)
{
	SetProps(pszName, iValue);
}

//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	\li pszName - the name used to identify this property
//!				\li lValue - the value to be assigned
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLPropPair::CMLPropPair(char* pszName, long lValue)
{
	SetProps(pszName, lValue);
}

//------------------------------------------------------------------------------
//!	Summary:	Constructor
//!
//!	Parameters:	\li pszName - the name used to identify this property
//!				\li lValue - the value to be assigned
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLPropPair::CMLPropPair(char* pszName, unsigned long ulValue)
{
	SetProps(pszName, ulValue);
}

//------------------------------------------------------------------------------
//!	Summary:	Copy constructor
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
CMLPropPair::CMLPropPair(const CMLPropPair& rSource)
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
CMLPropPair::~CMLPropPair()
{
}

//------------------------------------------------------------------------------
//!	Summary:	Called to convert the XML value to boolean
//!
//!	Parameters:	\li bDefault - the default value if m_strValue is empty
//!
//!	Returns:	The boolean equivalent of the current value
//------------------------------------------------------------------------------
bool CMLPropPair::AsBool(bool bDefault)
{
	bool bValue = bDefault;

	if(lstrlen(m_strValue) > 0)
	{
		if(lstrcmpi(m_strValue, "true") == 0)
			bValue = true;
		else if(lstrcmpi(m_strValue, "1") == 0)
			bValue = true;
		else if(lstrcmpi(m_strValue, "yes") == 0)
			bValue = true;
		else 
			bValue = false;
	}
	return bValue;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to convert the XML value to double precision float
//!
//!	Parameters:	\li dDefault - the default value if m_strValue is empty
//!
//!	Returns:	The double precision equivalent of the current value
//------------------------------------------------------------------------------
double CMLPropPair::AsDouble(double dDefault)
{
	double		dValue = dDefault;
	_CRT_DOUBLE	crtDouble;

	if(lstrlen(m_strValue) > 0)
	{
		if(_atodbl(&crtDouble, m_strValue) == 0)
		{
			dValue = crtDouble.x;
		}
	}

	return dValue;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to convert the XML value to an integer
//!
//!	Parameters:	\li iDefault - the default value if m_strValue is empty
//!
//!	Returns:	The integer equivalent of the current value
//------------------------------------------------------------------------------
int CMLPropPair::AsInteger(int iDefault)
{
	int iValue = iDefault;

	if(lstrlen(m_strValue) > 0)
		iValue = atoi(m_strValue);
	return iValue;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to convert the XML value to a long integer
//!
//!	Parameters:	\li lDefault - the default value if m_strValue is empty
//!
//!	Returns:	The long integer equivalent of the current value
//------------------------------------------------------------------------------
long CMLPropPair::AsLong(long lDefault)
{
	long lValue = lDefault;

	if(lstrlen(m_strValue) > 0)
		lValue = atol(m_strValue);
	return lValue;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to convert the XML value to an unsigned long integer
//!
//!	Parameters:	\li lDefault - the default value if m_strValue is empty
//!
//!	Returns:	The unsigned long integer equivalent of the current value
//------------------------------------------------------------------------------
unsigned long CMLPropPair::AsUnsignedLong(unsigned ulDefault)
{
	unsigned long ulValue = ulDefault;

	if(lstrlen(m_strValue) > 0)
	{
		char*EndPtr;
		ulValue = strtoul(m_strValue, &EndPtr, 10);
	}
	return ulValue;
}

		

//------------------------------------------------------------------------------
//!	Summary:	Called to convert the XML value to a MuthurLib handle
//!
//!	Parameters:	\li pszDefault - the default Muthur assigned identifier
//!
//!	Returns:	The MuthurLib handle equivalent of the current value
//------------------------------------------------------------------------------
CMLHandle CMLPropPair::AsMLHandle(char* pszDefault)
{
	CMLHandle mlHandle;

	if(lstrlen(m_strValue) > 0)
		mlHandle.SetMuthurId(m_strValue);
	else
		mlHandle.SetMuthurId(pszDefault);

	return mlHandle;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to convert the XML value to single precision float
//!
//!	Parameters:	\li fDefault - the default value if m_strValue is empty
//!
//!	Returns:	The single precision equivalent of the current value
//------------------------------------------------------------------------------
float CMLPropPair::AsSingle(float fDefault)
{
	float		fValue = fDefault;
	_CRT_FLOAT	crtFloat;

	if(lstrlen(m_strValue) > 0)
	{
		if(_atoflt(&crtFloat, m_strValue) != 0)
		{
			fValue = crtFloat.f;
		}

	}

	return fValue;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to clear the current value and name
//!
//!	Parameters:	None
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLPropPair::Clear()
{
	m_strName = "";
	m_strValue = "";
}

//------------------------------------------------------------------------------
//!	Summary:	Called to make a copy of the specified source object
//!
//!	Parameters:	\li rSource - the object to be copied
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLPropPair::Copy(const CMLPropPair& rSource)
{
	m_strName = rSource.m_strName;
	m_strValue = rSource.m_strValue;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the property name
//!
//!	Parameters:	None
//!
//!	Returns:	The name used to identify this property in the XML document
//------------------------------------------------------------------------------
char* CMLPropPair::GetName()
{
	return m_strName;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to get the property value
//!
//!	Parameters:	None
//!
//!	Returns:	The value assigned to this property as text
//------------------------------------------------------------------------------
char* CMLPropPair::GetValue()
{
	return m_strValue;
}

//------------------------------------------------------------------------------
//!	Summary:	Overloaded assignment operator
//!
//!	Parameters:	\li rSource - the object being assigned
//!
//!	Returns:	this object
//------------------------------------------------------------------------------
const CMLPropPair& CMLPropPair::operator = (const CMLPropPair& rSource)
{
	Copy(rSource);															
	return *this;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the property name
//!
//!	Parameters:	\li pszName - the XML name used to identify the property
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLPropPair::SetName(char* pszName)
{
	if(pszName != NULL)
	{
		m_strName = pszName;
	}
	else
	{
		m_strName = "";
	}
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the name and value of this property
//!
//!	Parameters:	\li pszName - the name used to identify this property
//!				\li bValue - the value to be assigned
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLPropPair::SetProps(char* pszName, bool bValue)
{
	SetName(pszName);
	SetValue(bValue);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the name and value of this property
//!
//!	Parameters:	\li pszName - the name used to identify this property
//!				\li pszValue - the value to be assigned
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLPropPair::SetProps(char* pszName, char* pszValue)
{
	SetName(pszName);
	SetValue(pszValue);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the name and value of this property
//!
//!	Parameters:	\li pszName - the name used to identify this property
//!				\li rMLHandle - the new value as a Muthur handle
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLPropPair::SetProps(char* pszName, CMLHandle& rMLHandle)
{
	SetName(pszName);
	SetValue(rMLHandle);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the name and value of this property
//!
//!	Parameters:	\li pszName - the name used to identify this property
//!				\li dValue - the new value as a double precision floating point
//!				\li iPrecision - the number of characters after the decimal point
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLPropPair::SetProps(char* pszName, double dValue, int iPrecision)
{
	SetName(pszName);
	SetValue(dValue, iPrecision);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the name and value of this property
//!
//!	Parameters:	\li pszName - the name used to identify this property
//!				\li fValue - the new value as a single precision floating point
//!				\li iPrecision - the number of characters after the decimal point
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLPropPair::SetProps(char* pszName, float fValue, int iPrecision)
{
	SetName(pszName);
	SetValue(fValue, iPrecision);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the name and value of this property
//!
//!	Parameters:	\li pszName - the name used to identify this property
//!				\li iValue - the new value as an integer
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLPropPair::SetProps(char* pszName, int iValue)
{
	SetName(pszName);
	SetValue(iValue);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the name and value of this property
//!
//!	Parameters:	\li pszName - the name used to identify this property
//!				\li iValue - the new value as a long integer
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLPropPair::SetProps(char* pszName, long lValue)
{
	SetName(pszName);
	SetValue(lValue);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the name and value of this property
//!
//!	Parameters:	\li pszName - the name used to identify this property
//!				\li iValue - the new value as a long integer
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLPropPair::SetProps(char* pszName, unsigned long ulValue)
{
	SetName(pszName);
	SetValue(ulValue);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the property value
//!
//!	Parameters:	\li bValue - the new value as a boolean
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLPropPair::SetValue(bool bValue)
{
	if(bValue == true)
		m_strValue = "true";
	else
		m_strValue = "false";
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the property value
//!
//!	Parameters:	\li pszValue - the text equivalent of the property value
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLPropPair::SetValue(char* pszValue)
{
	if(pszValue != NULL)
	{
		m_strValue = pszValue;
	}
	else
	{
		m_strValue = "";
	}
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the property value
//!
//!	Parameters:	\li rHandle - the new value as a Muthur handle
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLPropPair::SetValue(CMLHandle& rHandle)
{
	SetValue(rHandle.GetMuthurId());
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the property value
//!
//!	Parameters:	\li dValue - the new value as a double precision floating point
//!				\li iPrecision - the number of characters after the decimal point
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLPropPair::SetValue(double dValue, int iPrecision)
{
	char*	pChar = NULL;
	char	szValue[256];
	int		i = 0;

	// Buffer was overflowing when dValue was filled with garbage and greater than the 
	// size of the buffer; added this check to force the value to 0 if it is out of range
	sprintf_s(szValue, sizeof(szValue), "%f", dValue);

	if((pChar = strrchr(szValue, '.')) != NULL)// find the decimal point
	{
		if(iPrecision < 0)
			iPrecision = 8; // our default precision for doubles

		if(iPrecision > 0)
		{
			pChar += 1; // line up on first number past decimal point
			
			//	Truncate anything beyond the maximum
			if(lstrlen(pChar) > iPrecision)
			{
				*(pChar + iPrecision) = '\0';
			}

			//	Strip all non-significant zeros unless they are all zeros
			//	If all zeros, leave the first one in place
			for(i = lstrlen(pChar) - 1; i > 0; i--)
			{
				if(pChar[i] == '0')
				{	
					pChar[i] = '\0';
				}
				else
				{
					break;
				}

			}// for(i = lstrlen(pChar); i >= 0; i--)

		}
		else
		{
			*pChar = '\0'; // truncate at the decimal point
		}

	}// if((pDecimal = strrchr(szValue)) != NULL)
	
	m_strValue = szValue;

}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the property value
//!
//!	Parameters:	\li fValue - the new value as a single precision floating point
//!				\li iPrecision - the number of characters after the decimal point
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLPropPair::SetValue(float fValue, int iPrecision)
{
	//	Assign default precision if necessary
	if(iPrecision < 0)
		iPrecision = 4;

	SetValue(double(fValue), iPrecision);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the property value
//!
//!	Parameters:	\li iValue - the new value as an integer
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLPropPair::SetValue(int iValue)
{
	SetValue((long)iValue);
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the property value
//!
//!	Parameters:	\li iValue - the new value as a long integer
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLPropPair::SetValue(long lValue)
{
	char szValue[64];
	
	sprintf_s(szValue, sizeof(szValue), "%ld", lValue);
	m_strValue = szValue;
}

//------------------------------------------------------------------------------
//!	Summary:	Called to set the property value
//!
//!	Parameters:	\li iValue - the new value as an unsigned long integer
//!
//!	Returns:	None
//------------------------------------------------------------------------------
void CMLPropPair::SetValue(unsigned long lValue)
{
	char szValue[64];
	
	sprintf_s(szValue, sizeof(szValue), "%lu", lValue);
	m_strValue = szValue;
}

