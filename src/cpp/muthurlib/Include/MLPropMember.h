//------------------------------------------------------------------------------
/*! \file	MLPropMember.h
//
//  Contains declaration of the CMLPropMember class
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
#if !defined(__ML_XML_ELEMENT_H__)
#define __ML_XML_ELEMENT_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
#include <MLPropPair.h>
#include <MLPtrList.h>

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
//!	Class wrapper for property descriptor used to define the value(s) associated
//! with a class member. Class members can be single name=value pairs or they
//! can be complex objects with multiple values and child members
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLPropMember : public CMLPropPair
{
	protected:

		CMLPtrList						m_apChildren;	//!< The list of child members
		CMLPtrList						m_apAttributes;	//!< The list of values (eg. xml attributes)

		PTRNODE							m_posChildren;	//!< Used for iteration of child elements
		PTRNODE							m_posAttributes;//!< Used for iteration of attributes

	public:
			
										CMLPropMember(char* pszName = "", char* pszValue = "");
										CMLPropMember(char* pszName, bool bValue);
										CMLPropMember(char* pszName, CMLHandle& rMLHandle);
										CMLPropMember(char* pszName, double dValue, int iPrecision = ML_XML_DEFAULT_DOUBLE_PRECISION);
										CMLPropMember(char* pszName, float fValue, int iPrecision = ML_XML_DEFAULT_SINGLE_PRECISION);
										CMLPropMember(char* pszName, int iValue);
										CMLPropMember(char* pszName, long lValue);
										CMLPropMember(char* pszName, unsigned long lValue);

										CMLPropMember(const CMLPropMember& rSource);
		virtual						   ~CMLPropMember();

		virtual void					Copy(const CMLPropMember& rSource);
		virtual	void					Clear(bool bDelete);

		virtual const CMLPropMember&	operator = (const CMLPropMember& rSource);

		virtual CMLPropMember*			AddChild(const CMLPropMember& rChild);
		virtual CMLPropMember*			AddChild(char* pszName, char* pszValue);
		virtual CMLPropMember*			AddChild(char* pszName, bool bValue);
		virtual CMLPropMember*			AddChild(char* pszName, CMLHandle& rMLHandle);
		virtual CMLPropMember*			AddChild(char* pszName, double dValue, int iPrecision = ML_XML_DEFAULT_DOUBLE_PRECISION);
		virtual CMLPropMember*			AddChild(char* pszName, float fValue, int iPrecision = ML_XML_DEFAULT_SINGLE_PRECISION);
		virtual CMLPropMember*			AddChild(char* pszName, int iValue);
		virtual CMLPropMember*			AddChild(char* pszName, long lValue);
		virtual CMLPropMember*			AddChild(char* pszName, unsigned long lValue);

		virtual CMLPropPair*			AddAttribute(const CMLPropPair& rAttribute);
		virtual CMLPropPair*			AddAttribute(char* pszName, char* pszValue);
		virtual CMLPropPair*			AddAttribute(char* pszName, bool bValue);
		virtual CMLPropPair*			AddAttribute(char* pszName, CMLHandle& rMLHandle);
		virtual CMLPropPair*			AddAttribute(char* pszName, double dValue, int iPrecision = ML_XML_DEFAULT_DOUBLE_PRECISION);
		virtual CMLPropPair*			AddAttribute(char* pszName, float fValue, int iPrecision = ML_XML_DEFAULT_SINGLE_PRECISION);
		virtual CMLPropPair*			AddAttribute(char* pszName, int iValue);
		virtual CMLPropPair*			AddAttribute(char* pszName, long lValue);

		virtual const CMLPtrList&		GetChildren() const;
		virtual CMLPtrList&				GetChildren();
		virtual CMLPropMember*			GetFirstChild();
		virtual CMLPropMember*			GetNextChild();

		virtual const CMLPtrList&		GetAttributes() const;
		virtual CMLPtrList&				GetAttributes();
		virtual CMLPropPair*			GetFirstAttribute();
		virtual CMLPropPair*			GetNextAttribute();

		virtual CMLPropPair*			FindAttribute(char* pszName);
		virtual CMLPropMember*			FindChild(char* pszName);
		
		virtual CMLPropMember*			AddChild(CMLPropMember* pChild); //!< Reserved for internal library use
		virtual CMLPropPair*			AddAttribute(CMLPropPair* pAttribute); //!< Reserved for internal library use
};

#endif // !defined(__ML_XML_ELEMENT_H__)
