//------------------------------------------------------------------------------
/*! \file	MLObjectOwnershipRelinquishCallback.h
//
//  Contains declaration of the IMLObjectOwnershipRelinquishCallback class
//
//	<table>
//	<tr> <td colspan="4"><b>History</b> </tr>		
//	<tr> <td>Date		<td>Revision	<td>Description		 <td>Author	</tr>
//	<tr> <td>02-28-2012 <td>1.00		<td>Original Release <td>REB	</tr>
//	</table>
//
*/
//------------------------------------------------------------------------------
//	Copyright CSC - All Rights Reserved
//------------------------------------------------------------------------------
#if !defined(__ML_OBJECT_OWNERSHIP_RELINQUISH_EVENT_CALLBACK_H__)
#define __ML_OBJECT_OWNERSHIP_RELINQUISH_EVENT_CALLBACK_H__

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
class CMLObjectOwnerRelinquishEvent;

//------------------------------------------------------------------------------
//! \brief Interface class invoked by Ambassador for object ownership relinquish events
//------------------------------------------------------------------------------
class MUTHURLIB_API IMLObjectOwnershipRelinquishCallback
{
	public:
	
						IMLObjectOwnershipRelinquishCallback();
		virtual		   ~IMLObjectOwnershipRelinquishCallback();
			
		virtual void	OnRelinquishOwnership(CMLObjectOwnerRelinquishEvent* pMLObjectOwnerRelinquishEvent) = 0;
};

#endif // !defined(__ML_OBJECT_OWNERSHIP_RELINQUISH_EVENT_CALLBACK_H__)
