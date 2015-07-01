//------------------------------------------------------------------------------
/*! \file	MLObjectEventCallback.h
//
//  Contains declaration of the IMLObjectEventCallback class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>05-31-2011 <td>1.00		<td>Original Release <td>KRM	</tr>
//	<tr> <td>02-25-2012 <td>2.00		<td>Renamed class	 <td>REB	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------
#if !defined(__ML_OBJECT_EVENT_CALLBACK_H__)
#define __ML_OBJECT_EVENT_CALLBACK_H__

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
class CMLObjectEvent;

//------------------------------------------------------------------------------
//! \brief Interface class invoked by Ambassador for object change events 
//------------------------------------------------------------------------------
class MUTHURLIB_API IMLObjectEventCallback
{
	public:
	
						IMLObjectEventCallback();
		virtual		   ~IMLObjectEventCallback();
			
		virtual void	OnObjectEvent(CMLObjectEvent* pMLDataEvent) = 0;
};

#endif // !defined(__ML_OBJECT_EVENT_CALLBACK_H__)
