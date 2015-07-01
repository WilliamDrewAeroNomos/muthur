//------------------------------------------------------------------------------
/*! \file	MLListFEMParams.h
//
//  Contains declaration of the CMLListFEMParams class
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
#if !defined(__ML_LIST_FEM_PARAMS_H__)
#define __ML_LIST_FEM_PARAMS_H__

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
//! \brief Class wrapper for parameters used to get the list of Federation
//!		   Execution Models from the Ambassador
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLListFEMParams : public CMLRequestParams
{
	protected:
	
	public:
			
										CMLListFEMParams();
										CMLListFEMParams(const CMLListFEMParams& rSource);
						
		virtual						   ~CMLListFEMParams();
		
		virtual void					Copy(const CMLListFEMParams& rSource);
		virtual const CMLListFEMParams&	operator = (const CMLListFEMParams& rSource);

		virtual CMLPropMember*			GetXmlBlock(EMLXMLBlock eXmlBlock);
		EMLRequestClass					GetClass(){ return ML_REQUEST_CLASS_LIST_FEM; }
};

#endif // !defined(__ML_LIST_FEM_PARAMS_H__)
