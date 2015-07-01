//------------------------------------------------------------------------------
/*! \file	MLAmbassadorCallback.h
//
//  Contains declaration of the IMLAmbassadorCallback class
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
#if !defined(__ML_AMBASSADOR_CALLBACK_H__)
#define __ML_AMBASSADOR_CALLBACK_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>

//------------------------------------------------------------------------------
//	DEFINES
//------------------------------------------------------------------------------
 
//------------------------------------------------------------------------------
//	GLOBALS
//------------------------------------------------------------------------------

//------------------------------------------------------------------------------
//	DECLARATIONS
//------------------------------------------------------------------------------

//	Forward declarations
class CMLEvent;
class CMLException;

//------------------------------------------------------------------------------
//! \brief Interface class invoked by Ambassador to perform client notification
//------------------------------------------------------------------------------
class MUTHURLIB_API IMLAmbassadorCallback
{
	public:
	
								IMLAmbassadorCallback();
		virtual				   ~IMLAmbassadorCallback();
			
		virtual void			OnSuccess(CMLEvent* pMLEvent) = 0;
		virtual void			OnError(CMLException* pMLException) = 0;
};

#endif // !defined(__ML_AMBASSADOR_CALLBACK_H__)
