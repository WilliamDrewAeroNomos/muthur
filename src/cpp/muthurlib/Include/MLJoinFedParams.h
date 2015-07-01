//------------------------------------------------------------------------------
/*! \file	MLJoinFedParams.h
//
//  Contains declaration of the CMLJoinFedParams class
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
#if !defined(__ML_JOIN_FED_PARAMS_H__)
#define __ML_JOIN_FED_PARAMS_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
#include <MLRequestParams.h>
#include <MLFedExecModel.h>

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
//! \brief Class wrapper for parameters used to join a federation
//------------------------------------------------------------------------------


class MUTHURLIB_API CMLJoinFedParams : public CMLRequestParams
{
	protected:
		CMLFedExecModel					m_FEM;		//!< FEM to which user would like to join								
	public:
			
										CMLJoinFedParams();
										CMLJoinFedParams(const CMLJoinFedParams& rSource);
						
		virtual						   ~CMLJoinFedParams();

		virtual CMLFedExecModel			GetFEM();
		virtual void					SetFEM(CMLFedExecModel);
		
		virtual void					Copy(const CMLJoinFedParams& rSource);
		virtual const CMLJoinFedParams&	operator = (const CMLJoinFedParams& rSource);

		virtual CMLPropMember*			GetXmlBlock(EMLXMLBlock eXmlBlock);
		bool					SetXmlValue(EMLXMLBlock eXmlBlock, CMLPropMember* pMLPropMember);

		EMLRequestClass					GetClass(){ return ML_REQUEST_CLASS_JOIN_FED; }
};

#endif // !defined(__ML_JOIN_FED_PARAMS_H__)
