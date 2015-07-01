//------------------------------------------------------------------------------
/*! \file	MLSystemCallback.h
//
//  Contains declaration of the IMLSystemCallback class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>05-31-2011 <td>1.00		<td>Original Release <td>KRM	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------
#if !defined(__ML_SYSTEM_CALLBACK_H__)
#define __ML_SYSTEM_CALLBACK_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
#include <MLSystemEvent.h>

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
//! \brief Interface class invoked by Ambassador when system events occur
//------------------------------------------------------------------------------
class MUTHURLIB_API IMLSystemCallback
{
	public:
	
						IMLSystemCallback();
		virtual		   ~IMLSystemCallback();
			
		virtual void	OnSystemEvent(CMLSystemEvent* pSysEvent) = 0;
};

#endif // !defined(__ML_SYSTEM_CALLBACK_H__)
