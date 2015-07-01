//------------------------------------------------------------------------------
/*! \file	MLTerminateParams.h
//
//  Contains declaration of the CMLTerminateParams class
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
#if !defined(__ML_TERMINATE_PARAMS_H__)
#define __ML_TERMINATE_PARAMS_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
#include <MLRequestParams.h>

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
//! \brief This class wraps the parameters used to register a federate
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLTerminateParams : public CMLRequestParams
{
	protected:
	
		// No custom data at this time
	
	public:
			
											CMLTerminateParams();
											CMLTerminateParams(const CMLTerminateParams& rSource);
						
		virtual							   ~CMLTerminateParams();
		
		virtual void						Copy(const CMLTerminateParams& rSource);
		virtual const CMLTerminateParams&	operator = (const CMLTerminateParams& rSource);

		EMLRequestClass						GetClass(){ return ML_REQUEST_CLASS_TERMINATE; }

		virtual CMLPropMember*			GetXmlBlock(EMLXMLBlock eXmlBlock);
		virtual bool					SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember);
};

#endif // !defined(__ML_TERMINATE_PARAMS_H__)
