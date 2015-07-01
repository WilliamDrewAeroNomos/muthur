//------------------------------------------------------------------------------
/*! \file	MLPropPair.h
//
//  Contains declaration of the CMLPropPair class
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
#if !defined(__ML_PROP_VALUE_H__)
#define __ML_PROP_VALUE_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
#include <MLHandle.h>
#include <MLString.h>

//------------------------------------------------------------------------------
//	DEFINES
//------------------------------------------------------------------------------
 
//------------------------------------------------------------------------------
//	GLOBALS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	DECLARATIONS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//! \brief Class wrapper for an individual property name=value pair
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLPropPair
{
	protected:

		CMLString						m_strName;	//!< The XML node name
		CMLString						m_strValue;//!< The XML node value

	public:
			
										CMLPropPair(char* pszName = "", char* pszValue = "");
										CMLPropPair(char* pszName, bool bValue);
										CMLPropPair(char* pszName, CMLHandle& rMLHandle);
										CMLPropPair(char* pszName, double dValue, int iPrecision = ML_XML_DEFAULT_DOUBLE_PRECISION);
										CMLPropPair(char* pszName, float fValue, int iPrecision = ML_XML_DEFAULT_SINGLE_PRECISION);
										CMLPropPair(char* pszName, int iValue);
										CMLPropPair(char* pszName, long lValue);
										CMLPropPair(char* pszName, unsigned long ulValue);
										
										CMLPropPair(const CMLPropPair& rSource);
		virtual						   ~CMLPropPair();

		virtual void					SetProps(char* pszName, char* pszValue);
		virtual void					SetProps(char* pszName, bool bValue);
		virtual void					SetProps(char* pszName, CMLHandle& rMLHandle);
		virtual void					SetProps(char* pszName, double dValue, int iPrecision = ML_XML_DEFAULT_DOUBLE_PRECISION);
		virtual void					SetProps(char* pszName, float fValue, int iPrecision = ML_XML_DEFAULT_SINGLE_PRECISION);
		virtual void					SetProps(char* pszName, int iValue);
		virtual void					SetProps(char* pszName, long lValue);
		virtual void					SetProps(char* pszName, unsigned long ulValue);

		virtual void					Copy(const CMLPropPair& rSource);
		virtual void					Clear();
		virtual const CMLPropPair&		operator = (const CMLPropPair& rSource);

		virtual char*					GetName();
		virtual char*					GetValue();
		
		virtual void					SetName(char* pszName);
		virtual void					SetValue(char* pszValue);

		virtual void					SetValue(double dValue, int iPrecision = ML_XML_DEFAULT_DOUBLE_PRECISION);
		virtual void					SetValue(float fValue, int iPrecision = ML_XML_DEFAULT_SINGLE_PRECISION);
		virtual void					SetValue(long lValue);
		virtual void					SetValue(unsigned long ulValue);
		virtual void					SetValue(int iValue);
		virtual void					SetValue(bool bValue);
		virtual void					SetValue(CMLHandle& rMLHandle);

		virtual double					AsDouble(double dDefault = 0);
		virtual float					AsSingle(float fDefault = 0);
		virtual long					AsLong(long lDefault = 0L);
		virtual unsigned long			AsUnsignedLong(unsigned ulDefault = 0L);
		virtual int						AsInteger(int iDefault = 0);
		virtual bool					AsBool(bool bDefault = false);
		virtual CMLHandle				AsMLHandle(char* pszDefault = "");
};

#endif // !defined(__ML_PROP_VALUE_H__)
