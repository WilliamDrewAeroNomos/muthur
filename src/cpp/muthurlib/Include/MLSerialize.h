//------------------------------------------------------------------------------
/*! \file	MLAmbassadorCallback.h
//
//  Contains declaration of the IMLSerialize class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>06-22-2011 <td>1.00		<td>Original Release <td>KRM	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------
#if !defined(__ML_SERIALIZE_H__)
#define __ML_SERIALIZE_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
#include <MLPtrList.h>
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
class CMLPropMember; // Forward declaraton

//------------------------------------------------------------------------------
//! \brief Interface class inherited by all derived classes that support 
//!		   serialization.
//------------------------------------------------------------------------------
class MUTHURLIB_API IMLSerialize
{
	public:
	
									IMLSerialize();
		virtual					   ~IMLSerialize();
			
	virtual char*					GetXMLName(char* pszName, int iSize) = 0;
	virtual CMLPropMember*			GetXmlRoot() = 0;
	virtual CMLPropMember*			GetXmlBlock(EMLXMLBlock eXmlBlock) = 0;
	
	virtual bool					SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember) = 0;

	//virtual const CMLPtrList&			GetAttributes() const = 0;
	//virtual CMLPtrList&					GetAttributes() = 0;
	//virtual CMLString*					AddAttribute(const CMLString&) = 0;
	//virtual CMLString*					GetFirstAttribute() = 0;
	//virtual CMLString*					GetNextAttribute() = 0;
};

#endif // !defined(__ML_SERIALIZE_H__)
