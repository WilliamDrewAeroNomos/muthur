//------------------------------------------------------------------------------
/*! \file	MLException.h
//
//  Contains declaration of the CMLException class
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
#if !defined(__ML_EXCEPTION_H__)
#define __ML_EXCEPTION_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
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
class CMLRequestParams;

//------------------------------------------------------------------------------
//! \brief Class used to pass information to the client when errors occur
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLException
{
	protected:
	
		CMLRequestParams*			m_pRequest;	//!< Request that is associated with the error
		CMLString					m_sMessage;	//!< the error message

	public:
			
									CMLException();
		virtual					   ~CMLException();
		
		virtual CMLRequestParams*	GetRequest();
		virtual char*				GetMessage();
		
		virtual void				SetRequest(CMLRequestParams* pRequest);
		virtual void				SetMessage(char* pszMessage);
};

#endif // !defined(__ML_EXCEPTION_H__)
