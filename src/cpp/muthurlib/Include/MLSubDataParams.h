//------------------------------------------------------------------------------
/*! \file	MLSubDataParams.h
//
//  Contains declaration of the CMLSubDataParams class
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
#if !defined(__ML_SUB_DATA_PARAMS_H__)
#define __ML_SUB_DATA_PARAMS_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
#include <MLRequestParams.h>
#include <MLDataEventCallback.h>

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
class MUTHURLIB_API CMLSubDataParams : public CMLRequestParams
{
	protected:
	
		IMLDataEventCallback*			m_pIDataCallback; //!< Ambassador callback interface for data events
	
	public:
			
										CMLSubDataParams(TFedRegHandle hFRH = NULL, TFedExecHandle hFEH = NULL, 
														 IMLDataEventCallback* pICallback = NULL);
										CMLSubDataParams(const CMLSubDataParams& rSource);
						
		virtual							~CMLSubDataParams();
		
		virtual IMLDataEventCallback*	GetDataEventCallback();
		
		virtual void					SetDataEventCallback(IMLDataEventCallback* pICallback);

		virtual bool					AddClass(char* pszClassName);
		virtual int						GetClassCount();

		virtual void					Copy(const CMLSubDataParams& rSource);
		virtual const CMLSubDataParams&	operator = (const CMLSubDataParams& rSource);
};

#endif // !defined(__ML_SUB_DATA_PARAMS_H__)
