//------------------------------------------------------------------------------
/*! \file	MLTimeManagementEventCallback.h
//
//  Contains declaration of the IMLTimeManagementEventCallback class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>10-10-2011 <td>1.00		<td>Original Release <td>REB	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------
#if !defined(__ML_TIMEMANAGEMENT_EVENT_CALLBACK_H__)
#define __ML_TIMEMANAGEMENT_EVENT_CALLBACK_H__

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
class CMLTimeManagementEvent;

//------------------------------------------------------------------------------
//! \brief Interface class invoked by Ambassador for time management update events
//------------------------------------------------------------------------------
class MUTHURLIB_API IMLTimeManagementEventCallback
{
	public:
	
						IMLTimeManagementEventCallback();
		virtual		   ~IMLTimeManagementEventCallback();
			
		virtual void	OnTimeUpdate(CMLTimeManagementEvent* pMLTimeManagementEvent) = 0;
};

#endif // !defined(__ML_TIMEMANAGEMENT_EVENT_CALLBACK_H__)
