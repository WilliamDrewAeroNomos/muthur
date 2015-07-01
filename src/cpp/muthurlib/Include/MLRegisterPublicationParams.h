//------------------------------------------------------------------------------
/*! \file	MLJoinFedParams.h
//
//  Contains declaration of the CMLRegPubParams class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>06-02-2011 <td>1.00		<td>Original Release <td>REB	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------
#if !defined(__ML_REG_PUB_PARAMS_H__)
#define __ML_REG_PUB_PARAMS_H__

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
//! \brief Class wrapper for parameters used to register a publication
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLRegPubParams : public CMLRequestParams
{
	protected:
	
	public:
			
										CMLRegPubParams(CMLFedExecModel f = NULL, char**dataTypes = NULL);
										CMLRegPubParams(const CMLRegPubParams& rSource);
						
		virtual						   ~CMLRegPubParams();
		
		virtual void					Copy(const CMLRegPubParams& rSource);
		virtual const CMLRegPubParams&	operator = (const CMLRegPubParams& rSource);
};

#endif // !defined(__ML_JOIN_FED_PARAMS_H__)
