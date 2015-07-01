//------------------------------------------------------------------------------
/*! \file	MLHelper.h
//
//  Contains declaration of the CMLHelper class
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
#if !defined(__ML_HELPER_H__)
#define __ML_HELPER_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
#include <MLDataStream.h>
#include <MLPropMember.h>
#include <MLSerialize.h>
#include <MLString.h>
//#include <MLPtrList.h>
//#include <MLFlightPosition.h>
//#include "MLAircraftData.h"
//#include "MLAircraftArrivalData.h"
//#include "MLAircraftDepartureData.h"
//#include "MLFlightPlanData.h"
//#include "MLAircraftTaxiOut.h"
//#include "MLAircraftTaxiIn.h"

class CReporter;

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
//! \brief Class wrapper for set of static helper functions
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLHelper
{
	public:
			
								CMLHelper();
		virtual				   ~CMLHelper();

		static bool				GetXmlStream(IMLSerialize* pISerialize, CMLDataStream* pStream);
		static bool				GetXmlStream(CMLPropMember* pMLPropTree, CMLDataStream* pStream);

		static bool				SaveXmlStream(IMLSerialize* pISerialize, char* pszFileSpec);

		static CMLPropMember*	GetPropTree(char* pXmlStream);
		static CMLPropMember*	GetPropTree(IMLSerialize* pISerialize);

		static bool				LoadXmlStream(IMLSerialize* pISerialize, char* pXmlStream);
		static bool				LoadXmlBlock(IMLSerialize* pISerialize, EMLXMLBlock eXmlBlock, CMLPropMember* pParent);
		static bool				LoadPropTree(IMLSerialize* pISerialize, CMLPropMember* pMLPropTree);
		
		static const char*		GetCPPClassName(const char* pszMuthurClassName);
		static const char*		GetMuthurClassNameFromCppTypeId(const char* pszCPPClassName);
		static const char*		GetMuthurClassName(const char* pszCPPClassName);

	//	static CMLPtrList&		GetObjectAttributes(const char* pszMuthurClassName);
};

#endif // !defined(__ML_HELPER_H__)
