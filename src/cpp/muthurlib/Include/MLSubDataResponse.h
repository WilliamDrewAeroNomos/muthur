//------------------------------------------------------------------------------
/*! \file	MLSubDataResponse.h
//
//  Contains declaration of the CMLSubDataResponse class
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
#if !defined(__ML_SUB_DATA_RESPONSE_H__)
#define __ML_SUB_DATA_RESPONSE_H__

//------------------------------------------------------------------------------
//	INCLUDES
//------------------------------------------------------------------------------
#include <MLApi.h>
#include <MLEvent.h>

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
//! \brief Class wrapper for Ambassador's response to a request to add data
//!		   subscriptions
//------------------------------------------------------------------------------
class MUTHURLIB_API CMLSubDataResponse : public CMLEvent
{	
	protected:
	
		int									m_iSubClassCount;	//!< The number of classes successfully subscribed

	public:
			
											CMLSubDataResponse();
											CMLSubDataResponse(const CMLSubDataResponse& rSource);
						
		virtual							   ~CMLSubDataResponse();
		
		virtual int							GetSubClassCount();
		virtual void						SetSubClassCount(int iCount);

		virtual void						Copy(const CMLSubDataResponse& rSource);
		virtual const CMLSubDataResponse&	operator = (const CMLSubDataResponse& rSource);

		EMLEventClass						GetClass(){ return ML_EVENT_CLASS_SUB_DATA_RESPONSE; }
};

#endif // !defined(__ML_SUB_DATA_RESPONSE_H__)
